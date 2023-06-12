package com.fat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
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
	public static final String WINDOWS_FILE_SEPARATOR = "\\";
	
	// Metadatos del sistema de ficheros
	EntradaFAT[] entradasSistemaDeFicheros;
	
	// Datos del sistema de ficheros
	Cluster[] clustersSistemaDeFicheros;
	
	public SistemaDeFicheros(int numeroClusters, int sizeClusters) {	
		
		// Establecemos el tamaño en bytes de información ficticia
		// que podrá almacenar cada cluster
		Cluster.size = sizeClusters;
		
		// Inicializamos las entradas de la FAT (metadatos)
		this.entradasSistemaDeFicheros = new EntradaFAT[numeroClusters];
		// Inicializamos los clusters de la FAT (datos)
		this.clustersSistemaDeFicheros = new Cluster[numeroClusters + 1];
		
		//Inicializamos el directorio raíz
		this.clustersSistemaDeFicheros[0] = new Directorio("C:\\", 0); //Directorio ROOT
		this.clustersSistemaDeFicheros[0].ocupar();
		
		// Inicializamos el resto de clusters
		for(int cluster = 1; cluster < numeroClusters + 1; cluster++) {
			clustersSistemaDeFicheros[cluster] = new Cluster();
		}
		
		// Inicializamos las entradas a la fat
		for(int entrada=0; entrada < numeroClusters; entrada++) {
			entradasSistemaDeFicheros[entrada] = new EntradaFAT();
		}
		
		// Añadimos datos para realizar pruebas
		
	}
	
	//XXX MOSTRAR ESTADO ACTUAL DE LA FAT
	
	public void mostrarEstadoFat() {
		
		// Mostrar metadatos:
		mostrarMetadatos();
		
		// Mostrar datos
		mostrarDatos();
	}
	
	public void mostrarMetadatos() {
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
	}
	
	public void mostrarDatos() {
		System.out.println(ConsoleColours.TEXT_BG_RED + "DATOS" + newLine + ConsoleColours.TEXT_RESET);
		for(Cluster cluster: this.clustersSistemaDeFicheros) {
			System.out.println("[Cluster " + cluster.getID() + "]");
			if(cluster instanceof Directorio) {
				Directorio directorioImpreso = (Directorio) cluster;
				directorioImpreso.mostrar();
				
			} else if(cluster instanceof ParteArchivo) {
				ParteArchivo archivoImpreso = (ParteArchivo) cluster;
				 System.out.println(archivoImpreso);
			} else {
				System.out.println();
			}
			
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
		
		System.out.println("Introduzca el tamaño en bytes del nuevo archivo: ");
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
	
	
	public boolean crearArchivo(String nombreArchivo, int sizeOfArchivo, String pathDirectorioDestino) {
		
		// Obtener número de clusters necesitados para guardar el archivo
		int numClustersParaCrearArchivo = 0;
		numClustersParaCrearArchivo = obtenerNumeroDePartesDeArchivo(sizeOfArchivo);
		
		// Obtener listas de entradas de la fat disponibles 
		List<EntradaFAT> entradasFatLibres = obtenerListaEntradasFatLibres(); 
		List<EntradaFAT> entradasFatOcupadas = obtenerListaEntradasFatOcupadas();
		
		// Obtener nombres de directorios
		String[] nombresDirectorios = obtenerNombresDeDirectorios();
		
		// Obtener clusters libres 
		List<Cluster> clustersLibres = obtenerListaClustersLibres();
		List<Cluster> clustersOcupados = obtenerListaClustersOcupados();
		
		// Obtener partes de archivo que necesitamos introducir en los clusters
		List<ParteArchivo> partesDeArchivoNuevas = crearPartesDeArchivo(nombreArchivo, sizeOfArchivo, pathDirectorioDestino, clustersLibres);
		
		// Case: espacio disponible y directorio existe
		if(numClustersParaCrearArchivo <= this.obtenerNumeroEntradasFatLibres()) {
			if(directorioExiste(pathDirectorioDestino, nombresDirectorios)) {
				
				// Modificar entradas FAT (METADATOS)
				modificarEntradasFat(entradasFatLibres, numClustersParaCrearArchivo);
				actualizarMetadatos(entradasFatLibres, entradasFatOcupadas);
				// Modificar clusters (DATOS)
				
				// Añadir archivo a las entradas dir
				Directorio directorioParaArchivo = buscarDirectorio(pathDirectorioDestino);
				directorioParaArchivo.addEntrada(new EntradaDir(nombreArchivo, true, clustersLibres.get(0).getID()));
				
				// Añadir partes de archivo a los clusters y ocuparlos
				agregarPartesArchivoAClusters(partesDeArchivoNuevas, clustersLibres);
				actualizarDatos(clustersLibres, clustersOcupados);
				return true;
			}
		}
		// Case: archivo demasiado grande o directorio no existe 
		return false;
	}
	
	public void modificarEntradasFat(List<EntradaFAT> entradasFatLibres, int numClustersParaCrearArchivo) {
		
		for(int entrada = 0; entrada < numClustersParaCrearArchivo; entrada++) {
			// Cambiar a si es final
			if(entrada == numClustersParaCrearArchivo - 1) {
				// Cambiar disponibilidad e indicar que es final
				entradasFatLibres.get(entrada).disponibilidadAFalse();
				entradasFatLibres.get(entrada).cambiarSiEsFinal(true);
			} else {
				// Cambiar disponiblidad e indicar siguiente cluster
				entradasFatLibres.get(entrada).disponibilidadAFalse();
				entradasFatLibres.get(entrada).cambiarSiEsFinal(false);
				entradasFatLibres.get(entrada).cambiarSiguienteCluster(entradasFatLibres.get(entrada+1).getID());
			}
		}
	}
	
	public void agregarPartesArchivoAClusters(List<ParteArchivo> partesDeArchivo, List<Cluster> clustersLibres) {
		int numCluster = 0;
		for(ParteArchivo archivo: partesDeArchivo) {
			archivo.ocupar();
			clustersLibres.set(numCluster, archivo);
			numCluster++;
		}
	}
	
	public void agregarDirectorioAClusters(Directorio nuevoDirectorio, List<Cluster> clustersLibres) {
		nuevoDirectorio.ocupar();
		clustersLibres.set(0, nuevoDirectorio);
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
	
	public List<EntradaFAT> obtenerListaEntradasFatLibres() {
		
		List<EntradaFAT> entradasFatDisponibles = new ArrayList<EntradaFAT>();
		
		for(EntradaFAT entrada: this.entradasSistemaDeFicheros) {
			if(entrada.getDisponible()) {
				entradasFatDisponibles.add(entrada);
			}
		}
		
		return entradasFatDisponibles;
	}
	
public List<EntradaFAT> obtenerListaEntradasFatOcupadas() {
		
		List<EntradaFAT> entradasFatDisponibles = new ArrayList<EntradaFAT>();
		
		for(EntradaFAT entrada: this.entradasSistemaDeFicheros) {
			if(!entrada.getDisponible()) {
				entradasFatDisponibles.add(entrada);
			}
		}
		
		return entradasFatDisponibles;
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
		
		String[] directorios = new String[obtenerNumeroDeDirectorios()];
		int numDirectorio = 0;
		
		for(Cluster cluster: this.clustersSistemaDeFicheros) {
			if(cluster instanceof Directorio) {
				Directorio directorioEncontrado = (Directorio) cluster;
				directorios[numDirectorio] = directorioEncontrado.getNombre();
				numDirectorio++;
			}
		}
				
		return directorios;
	}
	
	public List<Directorio> obtenerListaDeDirectorios(){
		
		List<Directorio> listaDirectorios = new ArrayList<Directorio>();
		
		for(Cluster cluster: this.clustersSistemaDeFicheros) {
			if(cluster instanceof Directorio) {
				Directorio directorio = (Directorio) cluster;
				listaDirectorios.add(directorio);
			}
		}
		
		return listaDirectorios;
	}
	
	public boolean directorioExiste(String nombreDirectorio, String[] directorios) {
		List<String> listaDirectorios = Arrays.asList(directorios);
		return listaDirectorios.contains(nombreDirectorio);
	}
	
	public int obtenerNumeroDePartesDeArchivo(int sizeOfArchivo) {
		return (sizeOfArchivo / SIZE_OF_CLUSTER + 1);
	}
	
	public List<Cluster> obtenerListaClustersLibres(){
		
		List<Cluster> clustersLibres = new ArrayList<Cluster>();
		
		for(Cluster cluster: this.clustersSistemaDeFicheros) {
			if(cluster.estaDisponible()) {
				clustersLibres.add(cluster);
			}
		}
		
		return clustersLibres;
	}
	
	public List<Cluster> obtenerListaClustersOcupados(){
		
		List<Cluster> clustersOcupados = new ArrayList<Cluster>();
		
		for(Cluster cluster: this.clustersSistemaDeFicheros) {
			if(!cluster.estaDisponible()) {
				clustersOcupados.add(cluster);
			}
		}
		
		return clustersOcupados;
	}
	
	public List<ParteArchivo> crearPartesDeArchivo(String nombreDeArchivo, int sizeOfArchivo, String pathDirectorio, List<Cluster> clustersLibres) {
		
		int numeroDePartesDeArchivo = obtenerNumeroDePartesDeArchivo(sizeOfArchivo);
		List<ParteArchivo> partesDeArchivos = new ArrayList<ParteArchivo>();
		String pathArchivoCompleto = pathDirectorio + nombreDeArchivo +WINDOWS_FILE_SEPARATOR; 
		
		// Crear partes de archivo con su tamaño y nombre correspondientes
		for(int numArchivo = 0; numArchivo < numeroDePartesDeArchivo; numArchivo++) {
			// Si es el último, establecer como tamaño el resto entre el tamaño de un cluster
			int idCluster = clustersLibres.get(numArchivo).getID();
			if(numArchivo == numeroDePartesDeArchivo-1) {
				int restoDeBytesArchivo = sizeOfArchivo % SIZE_OF_CLUSTER;
				partesDeArchivos.add(new ParteArchivo(pathArchivoCompleto, restoDeBytesArchivo, idCluster));
			} else {
				partesDeArchivos.add(new ParteArchivo(pathArchivoCompleto, SIZE_OF_CLUSTER, idCluster));
			}
		}
		
		return partesDeArchivos;
	}
	
	public boolean crearDirectorio(String pathDirectorioOrigen, String nombreNuevoDirectorio) {
		
		String[] nombresDeDirectorios = obtenerNombresDeDirectorios();
		
		List<EntradaFAT> entradasFatLibres = obtenerListaEntradasFatLibres();
		List<EntradaFAT> entradasFatOcupadas = obtenerListaEntradasFatOcupadas();
		
		List<Cluster> clustersLibres = obtenerListaClustersLibres();
		List<Cluster> clustersOcupados = obtenerListaClustersOcupados();
		
		if(obtenerNumeroEntradasFatLibres() >= 1) {
			if(directorioExiste(pathDirectorioOrigen, nombresDeDirectorios)) {
				
				// Modificar metadatos
				modificarEntradasFat(entradasFatLibres, 1);
				actualizarMetadatos(entradasFatLibres, entradasFatOcupadas);
				
				// Modificar datos
				Directorio directorioNuevo = crearDirectorioEnCluster(clustersLibres, pathDirectorioOrigen, 
																	  nombreNuevoDirectorio);
				agregarDirectorioAClusters(directorioNuevo, clustersLibres);
				actualizarDatos(clustersLibres, clustersOcupados);
				return true;
			}
		}
		
		return false;
	}
	
	public Directorio crearDirectorioEnCluster(List<Cluster> clustersLibres, String pathDirectorioOrigen, String nombreNuevoDirectorio) {
		int idPrimerClusterLibre = clustersLibres.get(0).getID();
		String pathDestinoCompleto = pathDirectorioOrigen + nombreNuevoDirectorio + WINDOWS_FILE_SEPARATOR;
		Directorio directorioOrigen = buscarDirectorio(pathDirectorioOrigen);
		directorioOrigen.addEntrada(new EntradaDir(pathDestinoCompleto, false, idPrimerClusterLibre));
		Directorio directorioNuevo = new Directorio(pathDestinoCompleto, idPrimerClusterLibre);
		return directorioNuevo;
	}
	
	public void actualizarMetadatos(List<EntradaFAT> entradasFatLibres, List<EntradaFAT> entradasFatOcupadas) {
		
		List<EntradaFAT> nuevasEntradasFat = new ArrayList<EntradaFAT>();
		nuevasEntradasFat.addAll(entradasFatOcupadas);
		nuevasEntradasFat.addAll(entradasFatLibres);
		
		Collections.sort(nuevasEntradasFat);
		
		Iterator<EntradaFAT> itEntradas = nuevasEntradasFat.iterator();
		for(EntradaFAT entradaAntigua: this.entradasSistemaDeFicheros) {
			EntradaFAT entradaNueva = itEntradas.next();
			entradaAntigua = entradaNueva; 
		}
	}
	
	public void actualizarDatos(List<Cluster> clustersLibres, List<Cluster> clustersOcupados) {
		
		List<Cluster> nuevosClusters = new ArrayList<Cluster>();
		nuevosClusters.addAll(clustersLibres);
		nuevosClusters.addAll(clustersOcupados);
		
		Collections.sort(nuevosClusters);
		
		Iterator<Cluster> itClusters = nuevosClusters.iterator();
		for(int numCluster = 0; numCluster < this.clustersSistemaDeFicheros.length; numCluster++) {
			
			Cluster clusterNuevo = itClusters.next();
			if(clusterNuevo instanceof ParteArchivo) {
				this.clustersSistemaDeFicheros[numCluster] = (ParteArchivo) clusterNuevo;
			} else if(clusterNuevo instanceof Directorio) {
				this.clustersSistemaDeFicheros[numCluster] = (Directorio) clusterNuevo;
			} else {
				this.clustersSistemaDeFicheros[numCluster] = clusterNuevo;
			}
		}
		
	}
	
	//XXX COPIAR
	
	public void mostrarDialogoParaCopiarArchivo(Scanner input) {
		
		String pathArchivoACopiar = null;
		String pathDirectorioDestino = null;
		
		System.out.println("Introduzca el path completo del archivo a copiar: ");
		pathArchivoACopiar = input.nextLine();
		
		System.out.println("Introduca el path completo del directorio donde quiere guardar la copia: ");
		pathDirectorioDestino = input.nextLine();
		
		// Case: archivo copiado con éxito
		if(copiarArchivo(pathArchivoACopiar, pathDirectorioDestino)) {
			System.out.println("¡Archivo copiado con éxito!");
		}
		// Case: archivo origen o directorio destino no encontrados
		else {
			System.err.println("Archivo a copiar o directorio destino no encontrados.");
		}
		
	}
	
	public void mostrarDialogoParaCopiarDirectorio(Scanner input) {
		
		String pathDirectorioACopiar = null;
		String pathDirectorioDestino = null;
		
		System.out.println("Introduzca el path completo del directorio a copiar: ");
		pathDirectorioACopiar = input.nextLine();
		
		System.out.println("Introduzca el path completo del directorio donde quiere guardar la copia: ");
		pathDirectorioDestino = input.nextLine();
		
		// Case: directorio copiado con éxito 
		if(copiarDirectorio(pathDirectorioACopiar, pathDirectorioDestino)) {
			System.out.println("¡Directorio copiado con éxito!");
		}
		// Case: direcorio origen o destino no encontrados
		else {
			System.err.println("Directorio origen o destino no encontrados.");
		}
	}
	
	// COPIAR ARCHIVO
    public boolean copiarArchivo(String pathArchivo, String pathDirectorioDestino) {
        Directorio directorioDeArchivoACopiar = obtenerDirectorioPadre(pathArchivo);
        if(directorioDeArchivoACopiar != null) {
            String nombreArchivo = obtenerNombreHijo(pathArchivo);
            for(EntradaDir entradaDir: directorioDeArchivoACopiar.getEntradas()) {
                if(entradaDir.getNombre().equals(nombreArchivo)) {
                   int numCluster=entradaDir.getClusterInicio();
                   int sizeOfArchivo=0;
                   for(EntradaFAT entradaFat: this.entradasSistemaDeFicheros) {
                	   if(entradaFat.getID() == numCluster) {    	  
                    	   Cluster c=this.clustersSistemaDeFicheros[numCluster];
                    	   if(c instanceof ParteArchivo) {
                    		   ParteArchivo parte=(ParteArchivo)c;
                    		   sizeOfArchivo+=parte.getSizeInCluster();
                    	   }
                    	   numCluster=entradaFat.getSiguienteEntrada();
                    	   
                      	  if(entradaFat.getEsFinal()) {
                      		  //Tenemos archivo completo
                              return crearArchivo(nombreArchivo,sizeOfArchivo,pathDirectorioDestino);  
                          }
                      }
                   }
                }
            }
        }
        return false;
     }
	
	
	// COPIAR DIRECTORIO
	
	public boolean copiarDirectorio(String pathDirectorioOrigen, String pathDirectorioDestino) {
		
		return false;
	}
	
	//XXX MOVER
	public void mostrarDialogoParaMoverArchivo(Scanner input) {
		String pathArchivoAMover = null;
		String pathDirectorioDestino = null;
		
		System.out.println("Introduzca el path completo del archivo a mover: ");
		pathArchivoAMover = input.nextLine();
		
		System.out.println("Introduzca el path completo del directorio a donde quiere mover el archivo: ");
		pathDirectorioDestino = input.nextLine();
		
		// Case: archivo movido con éxito
		if(moverArchivo(pathArchivoAMover, pathDirectorioDestino)) {
			System.out.println("¡El archivo ha sido movido con éxito!");
		}
		// Case: archivo no encontrado
		else {
			System.err.println("El archivo no ha sido encontrado.");
		}
	}
	
	public void mostrarDialogoParaMoverDirectorio(Scanner input) {
		
		String pathDirectorioAMover = null;
		String pathDirectorioDestino = null;
		
		System.out.println("Introduzca el path completo del directorio a mover: ");
		pathDirectorioAMover = input.nextLine();
		
		System.out.println("Introduzca el path completo del directorio donde quiere mover el archivo: ");
		pathDirectorioDestino = input.nextLine();
		
		// Case: directorio movido con éxito
		if(moverDirectorio(pathDirectorioAMover, pathDirectorioDestino)) {
			System.out.println("¡El directorio ha sido movido con éxito!");
		}
		// Case: directorio no encontrado
		else {
			System.err.println("El directorio no se ha encontrado.");
		}
	}
	
	// MOVER ARCHIVO 
	
	public boolean moverArchivo(String pathArchivo, String pathDirectorioDestino) {
		return cambiarReferencia(pathArchivo, pathDirectorioDestino, true);
	}
	
	// MOVER DIRECTORIO
	
	public boolean moverDirectorio(String pathDirectorioOrigen,String pathDirectorioDestino) {
		return cambiarReferencia(pathDirectorioOrigen, pathDirectorioDestino, false);
	}
	
	public boolean cambiarReferencia(String pathOrigen,String pathDestino,boolean esArchivo) {
		Directorio dirPadre = obtenerDirectorioPadre(pathOrigen);
		String nombreArchivo = obtenerNombreHijo(pathOrigen);
		if(dirPadre==null) {
			//No existe esa ruta
			return false;
		}
		for(EntradaDir e:dirPadre.getEntradas()) {
			//Si las rutas de entrada coinciden es momento de cambiar su referencia
			if(esArchivo) {
				if(e.getNombre().equals(nombreArchivo)) {
					//Añadimos la referencia con una nueva entrada
					Directorio dirDestino = buscarDirectorio(pathDestino);
					dirDestino.addEntrada(new EntradaDir(e.getNombre(),esArchivo,e.getClusterInicio()));
					
					//Quitamos la referencia en el padre
					dirPadre.removeEntrada(e);
					return true;
				}
			} else {
				if(e.getNombre().equals(pathOrigen)) {
					//Añadimos la referencia con una nueva entrada
					Directorio dirDestino = buscarDirectorio(pathDestino);
					dirDestino.addEntrada(new EntradaDir(e.getNombre(),esArchivo,e.getClusterInicio()));
					
					//Quitamos la referencia en el padre
					dirPadre.removeEntrada(e);
					return true;
				}
			}
		}
		return false;
	}
	public Directorio obtenerDirectorioPadre(String pathHijo) {
		String[] contenido=pathHijo.split("\\\\");
		String rutaPadre="";
		for(int numPath=0;numPath<contenido.length;numPath++) {
			if(numPath!=contenido.length-1) {
				rutaPadre+=contenido[numPath] + WINDOWS_FILE_SEPARATOR;
			}
		}
		return buscarDirectorio(rutaPadre);
	}
	
	public String obtenerNombreHijo(String pathArchivo) {
		String[] contenido = pathArchivo.split("\\\\");
		String nombreHijo = "";
		nombreHijo = contenido[contenido.length-1];
		return nombreHijo;
	}
	
	//XXX BORRAR
	
	// BORRAR ARCHIVO
	public void mostrarDialogoParaBorrarArchivo(Scanner input) {
		String pathArchivoABorrar;
		System.out.println("Introduzca el path completo del archivo a borrar: ");
		pathArchivoABorrar = input.nextLine();
		
		// Case: borrar archivo con éxito
		if(borrarArchivo(pathArchivoABorrar)) {
			System.out.println("¡Archivo borrado con éxito!");
		}
		// Case: archivo no encontrado
		else {
			System.err.println("Archivo a borrar no encontrado.");
		}
	}
	
	// BORRAR DIRECTORIO
	public void mostrarDialogoParaBorrarDirectorio(Scanner input) {
		String pathDirectorioABorrar;
		System.out.println("Introduzca el path completo del directorio a borrar: ");
		pathDirectorioABorrar = input.nextLine();
		
		// Case: borrar directorio con éxito
		if(borraDirectorio(pathDirectorioABorrar)) {
			System.out.println("¡Directorio borrado con éxito!");
		} 
		// Case: directorio no encontrado
		else {
			System.err.println("Directorio a borrar no encontrado.");
		}
	} 
	
	
	/**
	 * Método usado para borrar un archivo dado su nombre
	 * @param nombre path completo de dónde se encuentra el archivo
	 */
	public boolean borraDirectorio(String pathDirectorioABorrar) {
		Directorio dirABuscar=buscarDirectorio(pathDirectorioABorrar);	
		if(dirABuscar==null) {	
			//No encontrado
			return false;
		}
		List<EntradaDir>entradasDirectorio=dirABuscar.getEntradas();
		//Buscar entradas
		for(int numEntrada = 0; numEntrada < entradasDirectorio.size(); numEntrada++) {
			EntradaDir entrada = entradasDirectorio.get(numEntrada);
			if(!entrada.esArchivo()) {
				borraDirectorio(entrada.getNombre());
			} else {
				borrarArchivo(pathDirectorioABorrar + entrada.getNombre() + WINDOWS_FILE_SEPARATOR);
			}
		}
		
		//Quitamos disponibilidad del directorio padre
		int indexABorrar=dirABuscar.getID();
		for(EntradaFAT e:entradasSistemaDeFicheros) {
			// Cambiar disponibilidad
			if(e.getID()==indexABorrar) {
				e.disponibilidadATrue();
				break;
			}
		}
		
		// Liberar directorio padre de los clusters
		dirABuscar.liberar();
		
		return true;
		
	}

	public boolean borrarArchivo(String pathArchivoABorrar) {		
		//Buscamos el archivo real en la zona de datos (con todos los clusters que ocupa)
		List<ParteArchivo>archivoReal=buscarArchivo(pathArchivoABorrar);
		
		//Existe esa ruta al archivo
		if(!archivoReal.isEmpty()) {		
			//Nos quedamos los identificadores de los clusters para la ENTRADA FAT
			List<Integer>idABorrar=new ArrayList<Integer>();
			for(ParteArchivo pa: archivoReal) {
				pa.liberar();
				idABorrar.add(pa.getID());
			}
			
			//Accedemos a las ENTRADA FAT y modificamos la visibilidad
			for(EntradaFAT e:entradasSistemaDeFicheros) {
				if(idABorrar.contains(e.getID())) {
					//Cambiamos la disponibilidad para que sea modificable
					e.disponibilidadATrue();
				}
			}
			
			// Accedemos a los clusters y quitamos la referencia donde aparece
			
			// Quitar referencia del directorio padre
			Directorio directorioUsadoPorArchivo = obtenerDirectorioPadre(pathArchivoABorrar); 
			String nombreHijo = obtenerNombreHijo(pathArchivoABorrar);
			List<EntradaDir> entradasDirectorioUsadoPorArchivo = directorioUsadoPorArchivo.getEntradas();
			
			for(int numEntrada = 0; numEntrada < entradasDirectorioUsadoPorArchivo.size(); numEntrada++) {
				EntradaDir entrada = entradasDirectorioUsadoPorArchivo.get(numEntrada);
				if(entrada.getNombre().equals(nombreHijo)) {
					directorioUsadoPorArchivo.removeEntrada(entrada);
				}
			}
			directorioUsadoPorArchivo.setEntradas(entradasDirectorioUsadoPorArchivo);
			//Existe la ruta
			return true;
		}
		
		return false;
	}
	
	// BUSCAR ARCHIVO
	
	public List<ParteArchivo> buscarArchivo(String pathArchivo) {
		
		List<ParteArchivo> partesArchivo = new ArrayList<ParteArchivo>();
		
		// Buscar en los clusters
		for(Cluster cluster: this.clustersSistemaDeFicheros) {
			if(cluster instanceof ParteArchivo) {
				ParteArchivo parteArchivo = (ParteArchivo) cluster;
				if(parteArchivo.getNombreArchivo().equals(pathArchivo)) {
					partesArchivo.add(parteArchivo);
				}
			}
		}
		
		return partesArchivo;
	}
	
	// BUSCAR DIRECTORIO
	
	public Directorio buscarDirectorio(String pathDirectorio) {
		
		// Buscar en los clusters
		List<Directorio> directorios = obtenerListaDeDirectorios();
		
		for(Directorio directorio: directorios) {
			if(directorio.getNombre().equals(pathDirectorio)) {
				return directorio;
			}
		}
		
		return null;
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
		
		Scanner input = new Scanner(System.in);
		
		switch (opcionElegida) {
		case MOSTRAR_ESTADO_FAT: 
			sistemaDeFicherosFat.mostrarEstadoFat();
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case CREAR_ARCHIVO:
			sistemaDeFicherosFat.mostrarDialogoParaCrearArchivo(input);
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case CREAR_DIRECTORIO:
			sistemaDeFicherosFat.mostrarDialogoParaCrearDirectorio(input);;
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case COPIAR_ARCHIVO:
			sistemaDeFicherosFat.mostrarDialogoParaCopiarArchivo(input);
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case COPIAR_DIRECTORIO:
			//sistemaDeFicherosFat.mostrarDialogoParaCopiarDirectorio(input);
			crearYMostrarConsola(sistemaDeFicherosFat);
		case MOVER_ARCHIVO:
			sistemaDeFicherosFat.mostrarDialogoParaMoverArchivo(input);;
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case MOVER_DIRECTORIO:
			sistemaDeFicherosFat.mostrarDialogoParaMoverDirectorio(input);
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case BORRAR_ARCHIVO:
			sistemaDeFicherosFat.mostrarDialogoParaBorrarArchivo(input);
			crearYMostrarConsola(sistemaDeFicherosFat);
			break;
		case BORRAR_DIRECTORIO:
			sistemaDeFicherosFat.mostrarDialogoParaBorrarDirectorio(input);;
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
	
	// LANZAMIENTO SISTEMA FICHEROS FAT
	public static void main(String[] args) {

		SistemaDeFicheros sistemaDeFicheros = new SistemaDeFicheros(NUMERO_DE_CLUSTERS, SIZE_OF_CLUSTER);
		crearYMostrarConsola(sistemaDeFicheros);
		//realizarPruebas(sistemaDeFicheros);
	}
}
