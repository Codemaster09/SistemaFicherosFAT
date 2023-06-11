package com.fat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.fat.utils.ConsoleColours;

public class SistemaDeFicheros {
	
	private static final int MOSTRAR_ESTADO_FAT = 0;
	private static final int CREAR_ARCHIVO = 1;
	private static final int CREAR_DIRECTORIO = 2;
	private static final int COPIAR_ARCHIVO = 3;
	private static final int COPIAR_DIRECTORIO = 4;
	private static final int MOVER_ARCHIVO = 5;
	private static final int MOVER_DIRECTORIO = 6;
	private static final int BORRAR_ARCHIVO = 7;
	private static final int BORRAR_DIRECTORIO = 8;
	private static final int SALIR_DE_PROGRAMA = 9;
	
	private static final int NUMERO_DE_CLUSTERS = 16;
	private static final int SIZE_OF_CLUSTER = 1024;
	
	public static final String newLine = "\n";
	
	// Datos del sistema de ficheros
	Cluster[] clustersSistemaDeFicheros;
	
	// Metadatos del sistema de ficheros
	EntradaFAT[] entradasSistemaDeFicheros;
	
	public SistemaDeFicheros(int numeroClusters, int sizeClusters) {	
		
		// Establecemos el tamaño en bytes de información ficticia
		// que podrá almacenar cada cluster
		Cluster.size = sizeClusters;
		
		// Inicializamos las entradas de la FAT (metadatos)
		this.entradasSistemaDeFicheros = new EntradaFAT[numeroClusters];
		// Inicializamos los clusters de la FAT (datos)
		this.clustersSistemaDeFicheros = new Cluster[numeroClusters + 1];
		
		//Inicializamos el directorio raíz
		this.clustersSistemaDeFicheros[0] = new Directorio("C:\\"); //Directorio ROOT
		
		// Inicializamos las entradas a la fat
		for(int i=0; i < numeroClusters; i++) {
			entradasSistemaDeFicheros[i] = new EntradaFAT();
		}
	}
	
	//XXX MOSTRAR ESTADO ACTUAL DE LA FAT
	
	public void mostrarEstadoFat() {
		
		// Mostrar metadatos:
		System.out.println(ConsoleColours.TEXT_BG_BLUE + "METADATOS" + newLine + ConsoleColours.TEXT_RESET);
		System.out.format("%-20s %-15s %-15s %-15s\n", 
						  ConsoleColours.TEXT_CYAN + "Cluster", 
						  "Disponible", 
						  "Siguiente", 
						  "Final" + ConsoleColours.TEXT_RESET);
		
		for(EntradaFAT entradaFat: this.entradasSistemaDeFicheros) {
			entradaFat.mostrar();
			System.out.println();
		}
		System.out.println();
		
		// Mostrar datos
		System.out.println(ConsoleColours.TEXT_BG_RED + "DATOS" + newLine + ConsoleColours.TEXT_RESET);
		for(Cluster cluster: this.clustersSistemaDeFicheros) {
			System.out.println(cluster + " " + Integer.toString(cluster.getID()));
		}
	}
	
	//XXX CREAR
	
	public void mostrarDialogoParaCrearArchivo(Scanner input) {
		
		String pathDirectorioDestino = null;
		String nombreArchivo = null;
		int sizeOfArchivo = 0;
		
		System.out.println("Introduzca el path del directorio donde quiere crear el archivo: ");
		pathDirectorioDestino = input.nextLine();
		
		System.out.println("Introduzca el nombre del nuevo archivo: ");
		nombreArchivo = input.nextLine();
		
		System.out.println("Introduzcale tamaño en bytes del nuevo archivo: ");
		sizeOfArchivo = input.nextInt();
		
		if(crearArchivo(nombreArchivo, sizeOfArchivo, pathDirectorioDestino)){
			System.out.println("¡Archivo creado con éxito!");
		} else {
			System.err.println("El directorio introducido no es válido o no existe suficiente espacio.");
		}
	}
	
	public void mostrarDialogoParaCrearDirectorio(Scanner input) {
		
		String pathDirectorioOrigen = null;
		String pathNuevoDirectorio = null;
		
		System.out.println("Introduzca el path completo del directorio donde quiere crear un nuevo directorio: ");
		pathDirectorioOrigen = input.nextLine();
		
		System.out.println("Introduzca el nombre del nuevo directorio: ");
		pathNuevoDirectorio = input.nextLine();
		
		// Case: directorio creado con éxito
		if(crearDirectorio(pathDirectorioOrigen, pathNuevoDirectorio)) {
			System.out.println("¡Directorio creado con éxito!");
		} 
		// Case: directorio origen no existe
		else {
			System.err.println("El directorio origen no existe.");
		}
	}
	
	
	public boolean crearArchivo(String nombreArchivo,int sizeOfArchivo, String pathDirectorioDestino) {
		
		// Obtener número de clusters necesitados para guardar el archivo
		int numClustersNecesitadosParaArchivo = 0;
		numClustersNecesitadosParaArchivo = obtenerNumeroDePartesDeArchivo(sizeOfArchivo);
		
		// Obtener nombres de directorios
		String[] nombresDirectorios = obtenerNombresDeDirectorios();
		
		// Case: espacio disponible y directorio existe
		if(numClustersNecesitadosParaArchivo <= this.obtenerNumeroEntradasFatLibres()) {
			if(directorioExiste(pathDirectorioDestino, nombresDirectorios)) {
				
				// Modificar entradas FAT (METADATOS)
				
				// Modificar clusters (DATOS)
				
				return true;
			}
		}
		// Case: archivo demasiado grande o directorio no existe 
		return false;
	}
	
	public int obtenerNumeroEntradasFatLibres() {
		
		int numeroEntradasFatLibres = 0;
		
		for(EntradaFAT entrada: this.entradasSistemaDeFicheros) {
			if(entrada.getDisponible()) {
				numeroEntradasFatLibres++;
			}
		}
		
		return numeroEntradasFatLibres;
	}
	
	public int[] obtenerIndicesEntradasFatLibres() {
		
		int [] indicesEntradasFatLibres = new int[this.obtenerNumeroEntradasFatLibres()];
		int numEntrada = 0;
		
		for(EntradaFAT entrada: this.entradasSistemaDeFicheros) {
			if(entrada.getDisponible()) {
				indicesEntradasFatLibres[numEntrada] = entrada.getID();
				numEntrada++;
			}
		}
		
		return indicesEntradasFatLibres;
	}
	
	public int obtenerNumeroDeDirectorios() {
		
		int numeroDeDirectorios = 0;
		
		// Buscamos por todos los clusters
		for(Cluster cluster: this.clustersSistemaDeFicheros) {
			if(cluster instanceof Directorio) {
				numeroDeDirectorios++;
			}
		}
		
		return numeroDeDirectorios;
	}
	
	public String[] obtenerNombresDeDirectorios() {
		
		String[] nombresDeDirectorios = new String[this.obtenerNumeroDeDirectorios()];
		int numeroDeDirectorio = 0;
		
		for(Cluster cluster: this.clustersSistemaDeFicheros) {
			if(cluster instanceof Directorio) {
				Directorio directorio = (Directorio) cluster;
				nombresDeDirectorios[numeroDeDirectorio] = directorio.getNombre();
				numeroDeDirectorio++;
			}
		}
		
		return nombresDeDirectorios;
	}
	
	public boolean directorioExiste(String nombreDirectorio, String[] directorios) {
		
		List<String> listOfDirectories = Arrays.asList(directorios);
		
		if(listOfDirectories.contains(nombreDirectorio)) {
			return true;
		}
		return false;
	}
	
	public int obtenerNumeroDePartesDeArchivo(int sizeOfArchivo) {
		return sizeOfArchivo / SIZE_OF_CLUSTER;
	}
	
	public ParteArchivo[] crearPartesDeArchivo(String nombreDeArchivo, int numeroDePartesDeArchivo) {
		
		ParteArchivo[] partesDeArchivos = new ParteArchivo[numeroDePartesDeArchivo];
		
		// Modificar los metadatos
		
		
		// Modificar los datos
		
		return partesDeArchivos;
	}
	
//	//CREATE
//	public void crearArchivo(String nombreDirEntrada, String nombreArchivo, String info) {
//		
//		//Para el caso del archivo ocupará en función de lo que se le pase por entrada
//		int size;
//		if(info.length()%Cluster.size>0)
//			size=1+info.length()/Cluster.size;
//		else
//			size=info.length()/Cluster.size;;
//			
//		String[]infoParts=infoToParts(info,size); //Dividimos el string en partes
//		List<Integer>clusterInic=entradasDisponibles(size); //Seleccionamos los clusters necesarios
//		
//		if(!clusterInic.isEmpty()) {
//			Directorio dirAMeterDir;
//			//Se hace en la raiz
//			dirAMeterDir=buscarDirectorioPorNombre((Directorio)clustersSistemaDeFicheros[0],nombreDirEntrada);
//
//			//Ya tenemos el directorio donde vamos a meter nuestro directorio
//			if(dirAMeterDir!=null) {
//				int i=0;
//				for(Integer cluster:clusterInic) {
//					dirAMeterDir.addEntrada(new EntradaDir(nombreArchivo,true,cluster));
//					//clustersSistemaDeFicheros[cluster]=new ParteArchivo(infoParts[i]);
//					clustersSistemaDeFicheros[cluster].setID(cluster);
//					i++;
//				}
//				System.out.println("Archivo creado con éxito");
//			}else {
//				System.out.println("Error al crear archivo: "
//						+ "No se ha encontrado el nombre del directorio en donde se va a introducir");
//			}
//		}	
//	}
//	
//	public void crearDirectorio(String nombreDirEntrada,String nombreDir) {
//		
//		//Para el caso del directorio solo ocupa un Cluster
//		List<Integer>clusterInic=entradasDisponibles(1);
//		if(!clusterInic.isEmpty()) {
//			Directorio dirAMeterDir;
//			//Buscará en C:\ el directorio para meter la info
//			dirAMeterDir=buscarDirectorioPorNombre((Directorio)clustersSistemaDeFicheros[0],nombreDirEntrada);
//			//Ya tenemos el directorio donde vamos a meter nuestro directorio
//			if(dirAMeterDir!=null) {
//				dirAMeterDir.addEntrada(new EntradaDir(nombreDir,true,clusterInic.get(0)));
//				clustersSistemaDeFicheros[clusterInic.get(0)]=dirAMeterDir;
//				clustersSistemaDeFicheros[clusterInic.get(0)].setID(clusterInic.get(0));
//				System.out.println("Directorio creado con éxito");
//			}else {
//				System.out.println("Error al crear directorio: "
//						+ "No se ha encontrado el nombre del directorio en donde se va a introducir");
//			}
//		}
//	}
//	
//	private Directorio buscarDirectorioPorNombre(Directorio dir,String nombreDirEntrada) {	
//		if(nombreDirEntrada.equals("C:\\")) {
//			return (Directorio)clustersSistemaDeFicheros[0];
//		}else { //Si no es el root, buscamos dentro de él
//			for(EntradaDir e:dir.getEntradas()) {
//				if(e.getIsDir()) {
//					if(e.getNombre().equals(nombreDirEntrada)) {
//						//Está el directorio que buscamos, cojo su cluster de inicio
//						return (Directorio)clustersSistemaDeFicheros[e.getClusterInicio()];					
//					}
//				}
//			}
//		}
//		//Si no hay directorio encontrado
//		return null;
//	}
//	
//	//Si existen los suficientes clusters libres, les cambia la disponibilidad
//	private List<Integer> entradasDisponibles(int numEntradas) {	
//		//Buscamos el número de entradas disponibles en nuestro sistema de Metadatos
//		List<Integer>disponibles=new ArrayList<Integer>();
//		
//		for(int i=0;i<entradasSistemaDeFicheros.length;i++) {
//			if(entradasSistemaDeFicheros[i].getDisponible()) {
//				//Metemos en una lista los ID de cada entrada para identificarlos
//				disponibles.add(entradasSistemaDeFicheros[i].getID());
//			}
//			if(disponibles.size()==numEntradas)
//				break; //Sal cuando tengas los clusters exactos que necesitas (así no coge más de la cuenta)
//		}
//		
//		if(disponibles.size()>=numEntradas) {
//			//Con la condición de arriba solo entrarán numEntradas clusters
//			//Como existen dichos clusters, cambiamos la disponibilidad de estos
//			for(int i=0;i<numEntradas;i++) {
//				entradasSistemaDeFicheros[disponibles.get(i)].disponibilidadAFalse();
//				if(i==numEntradas-1) { //Fin a true (último necesitado)
//					entradasSistemaDeFicheros[disponibles.get(i)].cambiarSiEsFinal(true);
//				}else { //Cambio el siguiente y el fin a falso
//					entradasSistemaDeFicheros[disponibles.get(i)].cambiarSiguienteCluster(disponibles.get(i+1));
//					entradasSistemaDeFicheros[disponibles.get(i)].cambiarSiEsFinal(false);
//				}
//			}
//		}else {
//			//Retornamos una lista vacía si no hay suficientes clusters (esto lo usaremos como condición posterior)
//			disponibles.clear();
//			System.err.println("No existen clusters suficientes para crear el archivo\n");
//		}
//		return disponibles;
//	}
//	
//	//Un string a array de string dividido en partes
//	private String[] infoToParts(String s,int size) {
//		String []parts=new String[size];
//		int cont=0;
//		for(int i=0;i<size;i++) {
//			for(int j=cont;j<Cluster.size;j++) {
//				if(j<s.length()) {
//					parts[i]+=s.charAt(j);
//				}else {
//					break;
//				}
//			}
//			cont+=Cluster.size;
//		}
//		return parts;
//	}
	
	//XXX COPIAR
	public void copiar(String nombre, Directorio directorioDestino, boolean esArchivo) {
		
		// Case: copiar un archivo a un directorio
		if(esArchivo) {
			copiarArchivo(nombre, directorioDestino);
		} 
		// Case: copiar un directorio a otro directorio
		else {
			
			// Buscamos el directorio en el que se encuentra el archivo actual
			for(Cluster cluster: clustersSistemaDeFicheros) {
				
				// Mirar si está ocupado
				if(cluster.estaOcupado() && cluster instanceof Directorio) {
					
					Directorio directorioAuxiliar = (Directorio) cluster;
					List<EntradaDir> entradasDirectorioAuxiliar = directorioAuxiliar.getEntradas();
					
					// Mirar si el nombre se encuentra en alguna de las entradas al dir
					for(EntradaDir entrada: entradasDirectorioAuxiliar) {
						if(entrada.getNombre().equals(nombre)) {
							//directorioOrigen = 
						}
					}
				}	
			}
			//copiarDirectorio(directorioOrigen, directorioDestino);
		}
	}
	
	// COPIAR ARCHIVO
	public void copiarArchivo(String pathArchivo, Directorio directorioDestino) {
		
	}
	
	// COPIAR DIRECTORIO
	
	public void copiarDirectorio(Directorio directorioOrigen, Directorio directorioDestino) {
		
	}
	
	//XXX MOVER
	public void mover(String nombre, boolean esArchivo) {
		
		// Case: mover un archivo
		if(esArchivo) {
			
		} 
		// Case: mover directorio
		else {
			
		}
	}
	
	// MOVER ARCHIVO 
	
	public void moverArchivo(String pathArchivo, Directorio directorioDestino) {
		
	}
	
	// MOVER DIRECTORIO
	
	public void moverDirectorio(Directorio directorioOrigen, Directorio directorioDestino) {
		
	}
	
	
	//XXX BORRAR
	
	/**
	 * Método usado para borrar un archivo dado su nombre
	 * @param nombre path completo de dónde se encuentra el archivo
	 */
	public void borrarArchivo(String nombre) {
		
	}
	
	public void borrarDirectorio(String nombre) {
		Directorio dirRemove = buscarDirectorioPorNombre((Directorio)clustersSistemaDeFicheros[0],nombre);
		
		if(dirRemove != null) {
			for(EntradaDir entrada:dirRemove.getEntradas())
				entradasSistemaDeFicheros[entrada.getClusterInicio()].disponibilidadATrue(); //Sus entradas disponibles
			entradasSistemaDeFicheros[dirRemove.getID()].disponibilidadATrue(); //El directorio lo eliminas
			System.out.println("Directorio eliminado");
			
		}else {
			System.out.println("Directorio no encontrado");
		}
	}
	
	// BUSCAR ARCHIVO
	
	public static ParteArchivo buscarArchivo(String nombreArchivo) {
		
		// Buscar en los clusters
	}
	
	// BUSCAR DIRECTORIO
	
	public static Directorio buscarDirectorio(String nombreDirectorio) {
		
	}
	
	public static void mostrarTituloInicial() {
		
		// Mostrar título inicial
		System.out.println(ConsoleColours.TEXT_BRIGHT_GREEN + "SISTEMA DE FICHEROS FAT" + ConsoleColours.TEXT_RESET);
		System.out.println();
		
		// Mostrar información de importancia
		System.out.println("El presente programa simula el funcionamiento de un sistema de ficheros FAT con 16 clusters de 1024 bytes de"
						   + " tamaño cada uno.");
		System.out.println();
		
		// Mostrar ayuda para el uso del programa
		System.out.println("Para crear, copiar, mover y borrar un archivo/directorio deberá escrbir su path completo.");
		System.out.println("Por ejemplo, para crear el archivo gataca.avi deberá escribir C:\\gataca.avi o para borrar el directorio "
				           + "dir2 que se encuentra en el directorio dir1 deberá escribir C:\\dir1\\dir2.");
		System.out.println();
	}

	// MOSTRADOR DE OPCIONES
	
	public static void mostrarOpciones() {
		System.out.println(ConsoleColours.TEXT_BG_GREEN + "¡Elige una opción!" + ConsoleColours.TEXT_RESET + 
				newLine + newLine +
			   "Mostrar sistema de ficheros (0)" + newLine + newLine +
			   ConsoleColours.TEXT_CYAN + "Crear nuevo archivo (1)" + newLine + 
			   "Crear nuevo directorio (2)" + ConsoleColours.TEXT_RESET + newLine + newLine +
			   ConsoleColours.TEXT_YELLOW + "Copiar archivo (3)" + newLine +
			   "Copiar directorio (4)" + ConsoleColours.TEXT_RESET + newLine + newLine +
			   ConsoleColours.TEXT_PURPLE + "Mover archivo (5)" + newLine +
			   "Mover directorio (6)" + ConsoleColours.TEXT_RESET + newLine + newLine +
			   ConsoleColours.TEXT_RED + "Borrar archivo (7)" + newLine +
			   "Borrar directorio (8)" + ConsoleColours.TEXT_RESET + newLine + newLine +
			   "Salir del programa (9)" + 
			   newLine + newLine);
	}
	
	// ELECTOR DE OPCIONES
	public static int obtenerOpcionUsuario(Scanner input, int opcionElegida) {
		System.out.println("Opción: ");
		opcionElegida = input.nextInt();
		return opcionElegida;
	}
	
	// GESTOR DE FUNCIONES DE LA FAT
	private static void gestionarFunciones(SistemaDeFicheros sistemaDeFicherosFat, int opcionElegida) {
		
		switch (opcionElegida) {
		case MOSTRAR_ESTADO_FAT: 
			sistemaDeFicherosFat.mostrarEstadoFat();
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case CREAR_ARCHIVO:
			sistemaDeFicherosFat.mostrarDialogoParaCrearArchivo();;
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case CREAR_DIRECTORIO:
			sistemaDeFicherosFat.crearDirectorio(newLine, newLine);
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case COPIAR_ARCHIVO:
			sistemaDeFicherosFat.copiar(newLine, null, false);
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case COPIAR_DIRECTORIO:
			sistemaDeFicherosFat.copiar(newLine, null, false);
			crearYMostrarConsola(sistemaDeFicherosFat);
		case MOVER_ARCHIVO:
			sistemaDeFicherosFat.mover(newLine, false);
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case MOVER_DIRECTORIO:
			sistemaDeFicherosFat.mover(newLine, false);
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case BORRAR_ARCHIVO:
			sistemaDeFicherosFat.borrarArchivo(newLine);
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case BORRAR_DIRECTORIO:
			sistemaDeFicherosFat.borrarDirectorio(newLine);
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case SALIR_DE_PROGRAMA:
			System.exit(0);
		default:
			break;
		}	
	}
	
	// MOSTRAR MENÚ POR CONSOLA
	
	public static void crearYMostrarConsola(SistemaDeFicheros sistemaDeFicherosFat) {
		
		// Inicializamos opción del usuario y el scanner para leer la opción elegida por el usuario
		int opcionElegida = -1;
		Scanner input = new Scanner(System.in);
		
		// Mostrar título inicial
		mostrarTituloInicial();
		
		// Mostrar opciones y ejecutar lo solicitado por el usuario
		mostrarOpciones();
		opcionElegida = obtenerOpcionUsuario(input, opcionElegida);
		gestionarFunciones(sistemaDeFicherosFat, opcionElegida);
	}
	
//	// PRUEBA DE LOS CASOS POSIBLES
//	public static void realizarPruebas(SistemaDeFicheros sistemaDeFicheros) {
//		
//		// Pruebas para crear directorios
//		sistemaDeFicheros.crearDirectorio("C:\\","Dir1");
//		sistemaDeFicheros.crearDirectorio("Dir1","Dir2");
//		sistemaDeFicheros.crearDirectorio("Dir2","DirectorioLast");
//		sistemaDeFicheros.crearDirectorio("C:\\","Dir3");
//		sistemaDeFicheros.crearDirectorio("Dir3","Dir4");
//		
//		// Pruebas para crear archivos
//		sistemaDeFicheros.crearArchivo("DirectorioLaste","Nacho","Info no relevante"); //Hecho aposta para que falle (mal nombre)
//		sistemaDeFicheros.crearArchivo("DirectorioLast","Nacho","Info relevante"); //Este acierta
//		
//		// Pruebas para copiar archivos
//		
//	}
	
	// LANZAMIENTO SISTEMA FICHEROS FAT
	public static void main(String[] args) {

		SistemaDeFicheros sistemaDeFicheros = new SistemaDeFicheros(NUMERO_DE_CLUSTERS, SIZE_OF_CLUSTER);
		crearYMostrarConsola(sistemaDeFicheros);
		//realizarPruebas(sistemaDeFicheros);
	}
	
}
