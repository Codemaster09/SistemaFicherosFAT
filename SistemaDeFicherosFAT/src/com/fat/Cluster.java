package com.fat;

import java.util.Arrays;

public class Cluster {
    private boolean ocupado;
    private byte[] datos;
    private int idEntrada;
    static int size; // Se elige el tamaño de cada cluster
    
    public Cluster() {
    	this.ocupado = false;
        datos = new byte[size];
    }
    
    // Retornar y setear si está ocupado o no
    public boolean estaOcupado() {
        return ocupado;
    }
    
    public void ocupar() {
        ocupado = true;
    }
    
    public void liberar() {
        ocupado = false;
    }
    
    // Retornar o setear los datos que contiene
    public byte[] getDatos() {
        return datos;
    }
    
    public void setDatos(byte[] nuevosDatos) {
    	  datos = Arrays.copyOf(nuevosDatos,nuevosDatos.length);
    }
     
    public void changeSize(int newSize) {
    	Cluster.size = newSize;
    }
    
    public void setID(int idEntrada) {
    	this.idEntrada=idEntrada;
    }
    
    public int getID() {
    	return this.idEntrada;
    }
}
