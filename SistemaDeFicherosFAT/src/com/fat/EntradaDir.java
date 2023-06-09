
package com.fat;
public class EntradaDir {
    private String nombre;
    private String tipo;
    private int idEntrada; 

    public EntradaDir(String nombre, String tipo, int idEntrada) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.idEntrada = idEntrada;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
    }
}
