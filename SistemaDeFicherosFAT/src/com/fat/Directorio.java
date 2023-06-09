package com.fat;


import java.util.ArrayList;
import java.util.List;

public class Directorio extends Cluster{
	
    private List<EntradaDir> entradas = new ArrayList<>();

    public Directorio(String nombreDirectorio, int indexOfCluster) {
        super(indexOfCluster);
        super.setNombre(nombreDirectorio); 
    }

    public void addEntrada(EntradaDir entrada) {
        entradas.add(entrada);
        super.ocupar(); // Al agregar una entrada, el cluster se ocupa
    }

    public void removeEntrada(EntradaDir entrada) {
        entradas.remove(entrada);
        if(entradas.isEmpty()) {
            super.liberar(); // Si no hay entradas, el cluster se libera
        }
    }

    public List<EntradaDir> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<EntradaDir> entradas) {
        this.entradas = entradas;
        if(entradas.isEmpty()) {
            super.liberar(); // Si no hay entradas, el cluster se libera
        }
        else {
            super.ocupar(); // Al agregar una entrada, el cluster se ocupa
        }
    }

	public String getNombreDirectorio() {
		return super.getNombre();
	}

	public void setNombreDirectorio(String nombreDirectorio) {
		super.setNombre(nombreDirectorio);
	}
	
	public void mostrarEntradas() {
		for(EntradaDir entrada: this.entradas) {
			entrada.mostrar();
		}
	}
	
	public void mostrar() {
		System.out.println(super.getNombre() + SistemaDeFicheros.newLine);
		mostrarEntradas();
	}

}
