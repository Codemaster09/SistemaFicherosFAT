package com.fat;

import java.util.Arrays;

public class Cluster implements Comparable{
	
	private String nombre;
    static int size; // Se elige el tamaño de cada cluster
    private boolean disponible;
    private int idEntrada;
    private static int cont=1;
    
    public Cluster() {
    	this.disponible = true;
        this.idEntrada = cont;
        cont++;
    }
    
    public Cluster(int idEntrada) {
    	this.disponible = true;
    	this.idEntrada = idEntrada;
    }
    
    // Retornar y setear si está ocupado o no
    public boolean estaDisponible() {
        return disponible;
    }
    
    public void ocupar() {
        disponible = false;
    }
    
    public void liberar() {
        disponible = true;
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

	@Override
	public int compareTo(Object o) {
		Cluster c = (Cluster) o;
		return this.idEntrada - c.idEntrada;
	}

}
