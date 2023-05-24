package com.fat;

import java.util.Arrays;

public class Cluster {
    private boolean ocupado;
    private byte[] datos;
    public static final int TAMANO_CLUSTER = 4096; // Este es el tamaño del cluster en bytes 
    //como no sabia que valor poner he puesto un static para que sea para todos igual
    public Cluster() {
        
    	this.ocupado = false;
        datos = new byte[TAMANO_CLUSTER];
    }
    
    public boolean estaOcupado() {
        return ocupado;
    }
    
    public void ocupar() {
        ocupado = true;
    }
    
    public void liberar() {
        ocupado = false;
    }
    
    public byte[] getDatos() {
        return datos;
    }
    
    public void setDatos(byte[] nuevosDatos) {
    	  datos = Arrays.copyOf(nuevosDatos, nuevosDatos.length);
    }
}
