package com.fat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SistemaDeFicheros {
	Cluster[] clustersSF;
	EntradaFAT[] entradasSF;
	
	public SistemaDeFicheros(int numeroClusters,int sizeClusters) {
		Cluster.size=sizeClusters;
		this.clustersSF = new Cluster[numeroClusters+1];
		this.entradasSF = new EntradaFAT[numeroClusters];
		
		
		//Inicializamos la lista de entradas a FAT
		this.clustersSF[0]=new Directorio(); //Directorio ROOT
		for(int i=1;i<clustersSF.length;i++) {
			this.entradasSF[i]=new EntradaFAT();
		}
	}
	
	//CREATE
	public void crearArchivo(String nombreDirEntrada,int size,String[]info,String nombreArchivo) {
		
		//Para el caso del directorio solo ocupa un Cluster
		List<Integer>clusterInic=entradasDisponibles(size);
		if(!clusterInic.isEmpty()) {
			Directorio dirAMeterDir;
			//Se hace en la raiz
			dirAMeterDir=buscarDirectorioPorNombre((Directorio)clustersSF[0],nombreDirEntrada);

			//Ya tenemos el directorio donde vamos a meter nuestro directorio
			if(dirAMeterDir!=null) {
				int i=0;
				for(Integer cluster:clusterInic) {
					clustersSF[cluster]=new ParteArchivo(info[i]);
					dirAMeterDir.add(new EntradaDir(nombreArchivo,true,clusterInic.get(0)));
					i++;
				}
			}
		}	
	}
	
	public void crearDirectorio(String nombreDirEntrada,String nombreDir) {
		
		//Para el caso del directorio solo ocupa un Cluster
		List<Integer>clusterInic=entradasDisponibles(1);
		if(!clusterInic.isEmpty()) {
			Directorio dirAMeterDir;
			//Se hace en la raiz
			dirAMeterDir=buscarDirectorioPorNombre((Directorio)clustersSF[0],nombreDirEntrada);

			//Ya tenemos el directorio donde vamos a meter nuestro directorio
			if(dirAMeterDir!=null) {
				clustersSF[clusterInic.get(0)]=new Directorio();
				dirAMeterDir.add(new EntradaDir(nombreDir,true,clusterInic.get(0)));
			}
		}
	}
	
	private Directorio buscarDirectorioPorNombre(Directorio dir,String nombreDirEntrada) {	
		for(EntradaFAT e:dir.entradas) {
			if(e.esDirectorio) {
				if(e.nombre.equals(nombreDirEntrada)) {
					//Está el directorio que buscamos, cojo su cluster de inicio
					return (Directorio)clustersSF[e.clusterInicio];					
				}else {
					//Me meto a mirar lo de dentro del deirectorio que me encuentro a ver si está
					return buscarDirectorioPorNombre((Directorio)clustersSF[e.clusterInicio],nombreDirEntrada);
				}
			}
		}
		//Si no hay directorio encontrado
		return null;
	}
	
	//Si existen los suficientes clusters libres, les cambia la disponibilidad
	private List<Integer> entradasDisponibles(int numEntradas) {	
		//Buscamos el número de entradas disponibles en nuestro sistema de Metadatos
		List<Integer>disponibles=new ArrayList<Integer>();

		for(int i=0;i<entradasSF.length;i++) {
			if(entradasSF[i].getDisponible()) {
				//Metemos en una lista los ID de cada entrada para identificarlos
				disponibles.add(entradasSF[i].getID());
			}
			if(disponibles.size()==numEntradas)
				break; //Sal cuando tengas las necesarias
		}
		
		if(disponibles.size()>=numEntradas) {
			//Si existen asi que cambiamos la disponibilidad
			for(int i=0;i<numEntradas;i++) {
				entradasSF[disponibles.get(i)].cambiarDisponibilidad();
				if(i==numEntradas-1) { //Fin a true (último necesitado)
					entradasSF[disponibles.get(i)].cambiarSiEsFinal(true);
				}else { //Cambio el siguiente y el fin a falso
					entradasSF[disponibles.get(i)].cambiarSiguienteCluster(disponibles.get(i+1));
					entradasSF[disponibles.get(i)].cambiarSiEsFinal(false);
				}
			}
		}else {
			disponibles.clear();
			System.err.println("No existen clusters suficientes para crear el archivo\n");
		}
		return disponibles;
	}
	
	//MOVE
	public void mover(boolean esArchivo) {
		
	}
	
	//REMOVE
	public void borrar(boolean esArchivo) {
		
	}
	
	//COPY
	public void copiar(boolean esArchivo) {
		
	}
	
	// MOSTRAR MENU
	public void mostrarOpcionesDeMenu() {
		
	}
	
	
}
