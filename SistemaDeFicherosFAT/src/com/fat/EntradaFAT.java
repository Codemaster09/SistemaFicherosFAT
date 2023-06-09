package com.fat;

public class EntradaFAT {
	private int id;
	private boolean disponible;
	private int siguienteEntrada;
	private boolean esFinal;
	private static int cont=1;
	
	public EntradaFAT() {
		super();
		this.disponible=true;
		this.id=cont;
		cont++;
	}
	
	public void cambiarDisponibilidad() {
		
		if(this.disponible) {
			this.disponible=false;
		}else {
			this.disponible=true;
		}
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

}
