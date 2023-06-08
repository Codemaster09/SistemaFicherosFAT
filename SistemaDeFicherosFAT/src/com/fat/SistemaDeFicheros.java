package com.fat;

import java.util.ArrayList;

public class SistemaDeFicheros {
	
	private static final int ES_MAC = 0;
	private static final int ES_WINDOWS = 1;
	
	int tipoDeSistemaDeFicheros;
	
	ArrayList<Cluster> clustersSistemaDeFicheros;
	ArrayList<EntradaFAT> entradasSistemaDeFicheros;

	//LANZAMIENTO SISTEMA FICHEROS FAT
	public static void main(String[] args) {
		
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
	
	// MOSTRAR MENU
	public void mostrarOpcionesDeMenu() {
		
		
	}
	
}
