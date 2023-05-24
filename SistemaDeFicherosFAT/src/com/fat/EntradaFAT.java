package com.fat;

public class EntradaFAT {
	boolean disponible;
	int indexSiguiente;
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
			this.indexSiguiente=indexCluster;
		}
	}
	
	public void cambiarSiEsFinal(boolean esFinal) {
		this.esFinal=esFinal;
	}

}
