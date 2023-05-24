package com.fat;

public class EntradaFAT {
	boolean disponible;
	int siguiente;
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

	public void cambiarSiguienteCluster(int indexCluster) {
		if(disponible) {
			this.siguiente=indexCluster;
		}
	}
	public void cambiarSiguienteCluster(Cluster clusterSiguiente) {
		this.clusterSiguiente=clusterSiguiente;
	}
	
	public void cambiarSiEsFinal(boolean esFinal) {
		this.esFinal=esFinal;
	}

}
