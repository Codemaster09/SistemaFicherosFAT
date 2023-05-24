package com.fat;

public class EntradaFAT {
	boolean disponible;
	Cluster clusterSiguiente;
	boolean esFinal;
	
	public EntradaFAT() {
		super();
		this.disponible=true;
		this.clusterSiguiente=null;
		this.esFinal=true;
	}
	
	public void cambiarDisponibilidad() {
		if(this.disponible) {
			this.disponible=false;
		}else {
			this.disponible=true;
		}
	}
	
	public void cambiarSiguienteCluster(Cluster clusterSiguiente) {
		this.clusterSiguiente=clusterSiguiente;
	}
	
	public void cambiarSiEsFinal(boolean esFinal) {
		this.esFinal=esFinal;
	}

}
