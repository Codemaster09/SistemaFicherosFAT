package com.fat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import com.fat.utils.ConsoleColours;

public class SistemaDeFicheros {
	
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
		
		this.clustersSistemaDeFicheros = new ArrayList<Cluster>();
		this.entradasSistemaDeFicheros = new ArrayList<EntradaFAT>();
		this.clustersSistemaDeFicheros.add(new Directorio()); //Add Root (Index=0)
		
		//Inicializamos la lista de entradas a FAT
		for(int i=0;i<numeroClusters;i++) {
			this.entradasSistemaDeFicheros.add(new EntradaFAT());
		}
	}
	
	//CREATE
	public static void crearArchivo(String nombre, boolean esArchivo) {
		
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
