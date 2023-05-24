package com.fat;

import java.util.ArrayList;

public class SistemaDeFicheros {
	
	private static final int ES_MAC = 0;
	private static final int ES_WINDOWS = 1;
	
	int tipoDeSistemaDeFicheros;
	int numeroClusters;
	int sizeClusters;
	
	ArrayList<Cluster> clustersSistemaDeFicheros;
	ArrayList<EntradaFAT> entradasSistemaDeFicheros;

	//LANZAMIENTO SISTEMA FICHEROS FAT
	public static void main(String[] args) {
		
	}
	
	public SistemaDeFicheros(int tipoDeSistemaDeFicheros, int numeroClusters, int sizeClusters) {
		this.tipoDeSistemaDeFicheros = tipoDeSistemaDeFicheros;
		this.numeroClusters = numeroClusters;
		this.sizeClusters = sizeClusters;
		
		this.clustersSistemaDeFicheros = new ArrayList<Cluster>();
		this.entradasSistemaDeFicheros = new ArrayList<EntradaFAT>();
		
		for(int cluster=0; cluster<numeroClusters; cluster++) {
			clustersSistemaDeFicheros.add(new Cluster);
		}
		
	}
	
	//CREATE
	public static void crear(boolean esArchivo) {
		
	}
	
	//MOVE
	public static void mover(boolean esArchivo) {
		
	}
	
	//REMOVE
	public static void borrar(boolean esArchivo) {
		
	}
	
	//COPY
	public static void copiar(boolean esArchivo) {
		
	}
	
	// MOSTRAR MENU
	public void mostrarOpcionesDeMenu() {
		
	}
	
	
}
