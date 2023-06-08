package com.fat;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public class SistemaDeFicheros {
	
	private static final int ES_MAC = 0;
	private static final int ES_WINDOWS = 1;
	
	int tipoDeSistemaDeFicheros;
	
	ArrayList<Cluster> clustersSistemaDeFicheros;
	ArrayList<EntradaFAT> entradasSistemaDeFicheros;

	//LANZAMIENTO SISTEMA FICHEROS FAT
	public static void main(String[] args) {
		ejecutarPrograma();
	}
	
	public SistemaDeFicheros(int tipoDeSistemaDeFicheros, int numeroClusters, int sizeClusters) {
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
	
	// MOSTRAR MENÚ
	public static void crearYMostrarGUI() {
		
		// Construir la ventana
		JFrame ventana = new JFrame("Sistema de Ficheros - FAT");
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
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
		ventana.add(panelDividido);
		
		// Mostrar ventana
		ventana.pack();
		ventana.setVisible(true);
	}
	
	// EJECUTAR PROGRAMA
	public static void ejecutarPrograma() {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				crearYMostrarGUI();
			}
		});
	}
	
}
