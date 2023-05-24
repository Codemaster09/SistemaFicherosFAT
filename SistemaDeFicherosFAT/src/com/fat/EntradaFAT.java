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
		if(this.disponible) {
			this.disponible=false;
		}else {
			this.disponible=true;
		}
	}
	
	public void cambiarSiguienteCluster(Cluster c) {
		this.siguiente=c;
	}
	
	public void cambiarSiEsFinal(boolean esFinal) {
		this.esFinal=esFinal;
	}
}
