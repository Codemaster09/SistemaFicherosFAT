package com.fat;

public class ParteArchivo extends Cluster {
	    private String nombreArchivo;
	    
	    public ParteArchivo(String nombreArchivo) {
	        super();
	        this.nombreArchivo = nombreArchivo;
	        super.setDatos(this.nombreArchivo.getBytes());
	    }
	    
	    public String getNombreArchivo() {
	        return nombreArchivo;
	    }

}
