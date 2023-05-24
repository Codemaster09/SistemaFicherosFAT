package com.fat;

public class EntradaFAT {
	boolean disponible;
	int siguienteCluster;
	boolean esFinal;
	
	public EntradaFAT() {
		super();
		this.disponible=true;
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
			this.siguienteCluster=indexCluster;
		}
	}
	
	public void cambiarSiEsFinal(boolean esFinal) {
		this.esFinal=esFinal;
	}

}
