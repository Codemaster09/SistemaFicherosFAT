package com.fat;

import java.awt.Color;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.Iterator;
import java.util.List;
=======
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import com.fat.utils.ConsoleColours;
>>>>>>> 70f3a6814bb738060117f3794d09e02800bc5776

public class SistemaDeFicheros {
	Cluster[] clustersSF;
	EntradaFAT[] entradasSF;
	
<<<<<<< HEAD
	public SistemaDeFicheros(int numeroClusters,int sizeClusters) {
=======
	private static final boolean ES_WINDOWS = Boolean.TRUE; 
	// Si no se cree un sistema de ficheros con directorio raíz '/'
	
	private static final String newLine = "\n";
	
	private static final int MOSTRAR_SISTEMA_FICHEROS = 0;
	private static final int CREAR_ARCHIVO_NUEVO = 1;
	private static final int CREAR_DIRECTORIO_NUEVO = 2;
	private static final int COPIAR_ARCHIVO = 3;
	private static final int COPIAR_DIRECTORIO = 4;
	private static final int MOVER_ARCHIVO = 5;
	private static final int MOVER_DIRECTORIO = 6;
	private static final int BORRAR_ARCHIVO = 7;
	private static final int BORRAR_DIRECTORIO = 8;
	private static final int SALIR_PROGRAMA = 9;
	
	boolean tipoDeSistemaDeFicheros;
	
	ArrayList<Cluster> clustersSistemaDeFicheros;
	ArrayList<EntradaFAT> entradasSistemaDeFicheros;

	// LANZAMIENTO SISTEMA FICHEROS FAT
	public static void main(String[] args) {
		ejecutarProgramaPorConsola();
	}
	
	public SistemaDeFicheros(boolean tipoDeSistemaDeFicheros, int numeroClusters, int sizeClusters) {
		this.tipoDeSistemaDeFicheros = tipoDeSistemaDeFicheros;
		Cluster.size=sizeClusters;
		this.clustersSF = new Cluster[numeroClusters+1];
		this.entradasSF = new EntradaFAT[numeroClusters];
		
		
		//Inicializamos la lista de entradas a FAT
		this.clustersSF[0]=new Directorio(); //Directorio ROOT
		for(int i=1;i<clustersSF.length;i++) {
			this.entradasSF[i]=new EntradaFAT();
		}
	}
	
	//CREATE
	public static void crearArchivo(String nombreArchivo,String[]info,int size) {
		
		//Para el caso del directorio solo ocupa un Cluster
		List<Integer>clusterInic=entradasDisponibles(size);
		if(!clusterInic.isEmpty()) {
			Directorio dirAMeterDir;
			//Se hace en la raiz
			dirAMeterDir=buscarDirectorioPorNombre((Directorio)clustersSF[0],nombreDirEntrada);

			//Ya tenemos el directorio donde vamos a meter nuestro directorio
			if(dirAMeterDir!=null) {
				int i=0;
				for(Integer cluster:clusterInic) {
					clustersSF[cluster]=new ParteArchivo(info[i]);
					dirAMeterDir.add(new EntradaDir(nombreArchivo,true,clusterInic.get(0)));
					i++;
				}
			}
		}	
	}
	
	public void crearDirectorio(String nombreDirEntrada,String nombreDir) {
		
		//Para el caso del directorio solo ocupa un Cluster
		List<Integer>clusterInic=entradasDisponibles(1);
		if(!clusterInic.isEmpty()) {
			Directorio dirAMeterDir;
			//Se hace en la raiz
			dirAMeterDir=buscarDirectorioPorNombre((Directorio)clustersSF[0],nombreDirEntrada);

			//Ya tenemos el directorio donde vamos a meter nuestro directorio
			if(dirAMeterDir!=null) {
				clustersSF[clusterInic.get(0)]=new Directorio();
				dirAMeterDir.add(new EntradaDir(nombreDir,true,clusterInic.get(0)));
			}
		}
	}
	
	private Directorio buscarDirectorioPorNombre(Directorio dir,String nombreDirEntrada) {	
		for(EntradaFAT e:dir.entradas) {
			if(e.esDirectorio) {
				if(e.nombre.equals(nombreDirEntrada)) {
					//Está el directorio que buscamos, cojo su cluster de inicio
					return (Directorio)clustersSF[e.clusterInicio];					
				}else {
					//Me meto a mirar lo de dentro del deirectorio que me encuentro a ver si está
					return buscarDirectorioPorNombre((Directorio)clustersSF[e.clusterInicio],nombreDirEntrada);
				}
			}
		}
		//Si no hay directorio encontrado
		return null;
	}
	
	//Si existen los suficientes clusters libres, les cambia la disponibilidad
	private List<Integer> entradasDisponibles(int numEntradas) {	
		//Buscamos el número de entradas disponibles en nuestro sistema de Metadatos
		List<Integer>disponibles=new ArrayList<Integer>();

		for(int i=0;i<entradasSF.length;i++) {
			if(entradasSF[i].getDisponible()) {
				//Metemos en una lista los ID de cada entrada para identificarlos
				disponibles.add(entradasSF[i].getID());
			}
			if(disponibles.size()==numEntradas)
				break; //Sal cuando tengas las necesarias
		}
		
		if(disponibles.size()>=numEntradas) {
			//Si existen asi que cambiamos la disponibilidad
			for(int i=0;i<numEntradas;i++) {
				entradasSF[disponibles.get(i)].cambiarDisponibilidad();
				if(i==numEntradas-1) { //Fin a true (último necesitado)
					entradasSF[disponibles.get(i)].cambiarSiEsFinal(true);
				}else { //Cambio el siguiente y el fin a falso
					entradasSF[disponibles.get(i)].cambiarSiguienteCluster(disponibles.get(i+1));
					entradasSF[disponibles.get(i)].cambiarSiEsFinal(false);
				}
			}
		}else {
			disponibles.clear();
			System.err.println("No existen clusters suficientes para crear el archivo\n");
		}
		return disponibles;
	}
	
	
	
	//MOVE
	public static void mover(String nombre, boolean esArchivo) {
		
		// Case: mover un archivo
		if(esArchivo) {
			
		} 
		// Case: mover directorio
		else {
			
		}
	}
	
	//REMOVE
	public static void borrar(String nombre, boolean esArchivo) {
		
		// Case: borrar archivo
		if(esArchivo) {
			
		}
		// Case: borrar directorio
		else {
			
		}
	}
	
	//COPY
	public static void copiar(String nombre, Directorio directorioDeDestino, boolean esArchivo) {
		
		// Case: copiar un archivo a un directorio
		if(esArchivo) {
			
		} 
		// Case: copiar un directorio a otro directorio
		else {
			
		}
	}
	
	// MOSTRAR MENÚ POR CONSOLA
	
	public static void crearYMostrarConsola(SistemaDeFicheros sistemaDeFicherosFat) {
		
		int opcionElegida = 0;
		
		Scanner input = new Scanner(System.in);
		
		System.out.println(ConsoleColours.TEXT_BRIGHT_GREEN + "SISTEMA DE FICHEROS FAT" + ConsoleColours.TEXT_RESET);
		System.out.println();
		
		while(opcionElegida != SALIR_PROGRAMA && 
				opcionElegida >= MOSTRAR_SISTEMA_FICHEROS && 
				opcionElegida < SALIR_PROGRAMA) {
			System.out.println(ConsoleColours.TEXT_BG_GREEN + "¡Elige una opción!" + ConsoleColours.TEXT_RESET + 
						newLine + newLine +
					   "Mostrar sistema de ficheros (0)" + newLine +
					   ConsoleColours.TEXT_CYAN + "Crear nuevo archivo (1)" + newLine + 
					   "Crear nuevo directorio (2)" + ConsoleColours.TEXT_RESET + newLine +
					   ConsoleColours.TEXT_YELLOW + "Copiar archivo (3)" + newLine +
					   "Copiar directorio (4)" + ConsoleColours.TEXT_RESET + newLine + 
					   ConsoleColours.TEXT_PURPLE + "Mover archivo (5)" + newLine +
					   "Mover directorio (6)" + ConsoleColours.TEXT_RESET + newLine +
					   ConsoleColours.TEXT_RED + "Borrar archivo (7)" + newLine +
					   "Borrar directorio (8)" + ConsoleColours.TEXT_RESET + newLine +
					   "Salir del programa (9)" + 
					   newLine + newLine +
					   "Opción: ");
			opcionElegida = input.nextInt();
		}
		
	}
	
	// EJECUTAR PROGRAMA POR CONSOLA
	
	public static void ejecutarProgramaPorConsola() {
		
		SistemaDeFicheros sistemaDeFicherosFat = new SistemaDeFicheros(ES_WINDOWS, BORRAR_DIRECTORIO, BORRAR_ARCHIVO);
		crearYMostrarConsola(sistemaDeFicherosFat);
	}
	
	// MOSTRAR MENÚ POR GUI
	public static void crearYMostrarGUI() {
		
		// Construir la ventana
		JFrame ventanaPrincipal = new JFrame("Sistema de Ficheros - FAT");
		ventanaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventanaPrincipal.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		// Construir los paneles de la ventana
		
		// Panel metadatos
		JPanel panelMetadatos = new JPanel();
		panelMetadatos.setBackground(Color.blue);
		
		// Panel datos
		JPanel panelDatos = new JPanel();
		panelDatos.setBackground(Color.red);
		
		// Panel principal
		JSplitPane panelDividido = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelMetadatos, panelDatos);
		panelDividido.setResizeWeight(0.5);
		ventanaPrincipal.add(panelDividido);
		
		// Mostrar ventana
		ventanaPrincipal.pack();
		ventanaPrincipal.setVisible(true);
	}
	
	// EJECUTAR PROGRAMA POR GUI
	public static void ejecutarProgramaGUI() {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				crearYMostrarGUI();
				// Esto es un comentario
			}
		});
	}
	
}
