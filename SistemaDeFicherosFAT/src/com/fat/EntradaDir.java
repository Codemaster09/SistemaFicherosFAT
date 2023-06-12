
package com.fat;
public class EntradaDir {
    private String nombreArchivo;
    private boolean esArchivo;
    private int clusterInicio;

    public EntradaDir(String nombreArchivo, boolean esArchivo, int clusterInicio) {
        this.nombreArchivo = nombreArchivo; 
        this.esArchivo = esArchivo;
        this.clusterInicio = clusterInicio;
    }

    // Getters y Setters
    public String getNombre() {
        return this.nombreArchivo;
    }

    public void setNombre(String nuevoNombreArchivo) {
        this.nombreArchivo = nuevoNombreArchivo;
    }

    public boolean getIsDir() {
        return esArchivo;
    }

    public void setIsArchivo() {
        this.esArchivo = true;
    }

    public int getClusterInicio() {
        return clusterInicio;
    }

    public void setClusterInicio(int clusterInicio) {
        this.clusterInicio = clusterInicio;
    }
    
    public String getTipoEntrada() {
    	return esArchivo? "Archivo": "Directorio";
    }
    
    public void mostrar() {
    	System.out.format("%-15s %-15s %-15s\n", "Nombre", "Tipo", "Cluster Inicio");
    	System.out.format("%-15s %-15s %-15s\n", this.nombreArchivo, this.getTipoEntrada(), this.clusterInicio);
    }
    
//    @Override
//    public String toString() {
//    	return "Nombre: " + this.nombreArchivo + "Tipo: " + (esArchivo? "A":"D") + "Ci: " + clusterInicio;
//    }
}
