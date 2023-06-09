
package com.fat;
public class EntradaDir {
    private String nombre;
    private boolean esDirectorio;
    private int clusterInicio;

    public EntradaDir(String nombre,boolean esDirectorio, int clusterInicio) {
        this.nombre = nombre;
        this.esDirectorio =esDirectorio;
        this.clusterInicio = clusterInicio;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getIsDir() {
        return esDirectorio;
    }

    public void setIsDir(boolean esDirectorio) {
        this.esDirectorio = esDirectorio;
    }

    public int getClusterInicio() {
        return clusterInicio;
    }

    public void setClusterInicio(int clusterInicio) {
        this.clusterInicio = clusterInicio;
    }
}
