package es.ucm.fdi.pev;

import es.ucm.fdi.pev.evaluacion.*;
import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.estructura.*;

//Quiza necesita tipo T
public abstract class AGenetico<T>
{

	Cromosoma<T>[] poblacion;
	int tamPoblacion;
	
	Cromosoma<T> elMejor;
	int mejor_idx;
	
	float fitness_total;
	
	int maxGeneraciones;
	int generacionActual;
	
	float prob_cruce;
	float prob_mutacion;
	
	float toleracia;
	
	// ---------------- FUNCIONES ---------------- //
	
	public AGenetico( ) 
	{
		// Recibimos
		// Tama�o de poblacion y genes en cada individuo
		// Probabilidades 
		// Funcion de evaluacion
	}
	
	
	public void ejecuta()
	{
		// Bucle del algoritmo
		//Genera
		inicializaPoblacion();
		
		//Evalua
		evaluacion();
		
		//
		while (!terminado()) {
			generacionActual++;
			//El modifica internamente la poblacion
			seleccion();
			//
			cruce();
			//Mutacion
			mutacion();
			//
			evaluacion();			
		}
		
	}
	
	
	
	// Funciones del blucle: 

	private void inicializaPoblacion()
	{
		generacionActual = 0;
	}
	
	abstract void evaluaCromosoma(Cromosoma<T> c);
	
	
	private void evaluacion() 
	{
		fitness_total = 0;
		for (int i = 0; i < poblacion.length; i++) {
			// Calculo de fitness de cada individuo
			evaluaCromosoma(poblacion[i]);
			
			// Calculo del fitness total de la poblacion		
			fitness_total += poblacion[i].getFitness();
			
		}
		
		// Probabilidad relativa [0,1) para metodos de seleccion
		for (int j = 0; j < poblacion.length; j++) {
			poblacion[j].setRelFit(fitness_total);
		}
		
	}
	
	private void seleccion()
	{
		//Switch dependiendo del tipo de seleccion	
	}
	
	private void cruce() 
	{
		//Switch dependiendo del tipo de cruce
	}
	
	private void mutacion()
	{
		//Switch dependiendo del tipo de mutacion
	}
	
	
	private boolean terminado() 
	{
		return generacionActual >= maxGeneraciones;
	}
}