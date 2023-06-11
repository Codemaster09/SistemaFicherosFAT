package com.fat;

import java.util.Arrays;

public class Cluster {
	
	private String nombre;
    static int size; // Se elige el tamaño de cada cluster
    private boolean disponible;
    private int idEntrada;
    private static int cont=0;
    
    public Cluster() {
    	this.disponible = true;
        this.idEntrada = cont;
        cont++;
    }
    
    // Retornar y setear si está ocupado o no
    public boolean estaDisponible() {
        return disponible;
    }
    
    public void ocupar() {
        disponible = true;
    }
    
    public void liberar() {
        disponible = false;
    }
     
    public void changeSize(int newSize) {
    	Cluster.size = newSize;
    }
    
    public void setID(int idEntrada) {
    	this.idEntrada = idEntrada;
    }
    
    public int getID() {
    	return this.idEntrada;
    }
    
    public void setNombre(String nuevoNombre) {
    	this.nombre = nuevoNombre;
    }
    
    public String getNombre() {
    	return this.nombre;
    }

	@Override
	public String toString() {
		return "[Cluster " + idEntrada + "]" + SistemaDeFicheros.newLine;
	}

}
