package com.fat;

import java.util.Arrays;

public class Cluster {
    private boolean ocupado;
    private byte[] datos;
    private int size; // Lo hemos cambiado para que el usuario pueda elegir
    // el tamaño del cluster
    
    public Cluster(int size) {
    	this.ocupado = false;
    	this.size = size;
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
    	  datos = Arrays.copyOf(nuevosDatos, nuevosDatos.length);
    }
    
    public int getSize() {
    	return size;
    }
    
    public void changeSize(int newSize) {
    	this.size = newSize;
    }
}
