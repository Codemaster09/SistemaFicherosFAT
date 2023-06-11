package com.fat;

public class ParteArchivo extends Cluster {
	
    private String nombreArchivo;
    private int sizeInCluster;
    
    public ParteArchivo(String nombreArchivo, int sizeInCluster) {
        super();
        this.nombreArchivo = nombreArchivo;
        this.sizeInCluster = sizeInCluster;
    }
    
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nuevoNombreArchivo) {
    	this.nombreArchivo = nuevoNombreArchivo;
    }
    
    @Override
    public String toString() {
    	return this.nombreArchivo + ": " + sizeInCluster + "/" + super.size + "bytes";
    }
}
