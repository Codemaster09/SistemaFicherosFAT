package com.fat;

public class EntradaFAT {
	boolean disponible;
	Cluster siguiente;
	boolean esFinal;
	
	public EntradaFAT() {
		super();
		this.disponible=true;
		this.siguiente=null;
		this.esFinal=true;
	}
	
	public void cambiarDisponibilidad() {
		
	}
	
	public void cambiarSiguienteCluster(Cluster c) {
		
	}
	
	public void cambiarSiEsFinal(boolean esFinal) {
		
	}

}
