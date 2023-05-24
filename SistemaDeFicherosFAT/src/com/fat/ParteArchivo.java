package com.fat;

public class ParteArchivo extends Cluster {
	    private String nombreArchivo;
	    
	    public ParteArchivo(String nombreArchivo, int size) {
	        super(size);
	        this.nombreArchivo = nombreArchivo;
	    }
	    
	    public String getNombreArchivo() {
	        return nombreArchivo;
	    }

}
