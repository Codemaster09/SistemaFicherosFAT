package com.fat;

public class EntradaFAT {
	private int id;
	private boolean disponible;
	private int siguienteEntrada;
	private boolean esFinal;
	private static int cont = 0; // El 0 es el directorio Raíz siempre
	
	public EntradaFAT() {
		super();
		this.disponible=true;
		this.siguienteEntrada = -1;
		this.id=cont;
		cont++;
	}
	
	public void disponibilidadAFalse() {
		this.disponible=false;
	}

	public void disponibilidadATrue() {
		this.disponible=true;
	}
	
	public void cambiarSiguienteCluster(int indexEntrada) {
		if(disponible) {
			this.siguienteEntrada=indexEntrada;
		}
	}
	
	public void cambiarSiEsFinal(boolean esFinal) {
		this.esFinal=esFinal;
	}

	public boolean getDisponible() {
		return disponible;
	}
	
	public int getSiguienteEntrada() {
		return siguienteEntrada;
	}

	public boolean getEsFinal() {
		return esFinal;
	}

	public int getID() {
		return id;
	}
	
	public void mostrar() {
		
		System.out.format("%-15s %-15s %-15s %-15s", "Cluster ".concat(Integer.toString(this.id)), 
												Boolean.toString(disponible), 
												Integer.toString(siguienteEntrada), 
												Boolean.toString(esFinal));
	}

//	@Override
//	public String toString() {
//		return "[Cluster " + id + ", disponible = " + (disponible? "T" : "F") + ", Siguiente = " + siguienteEntrada
//				+ ", esFinal = " + (esFinal? "T": "F") + "]";
//	}

}
