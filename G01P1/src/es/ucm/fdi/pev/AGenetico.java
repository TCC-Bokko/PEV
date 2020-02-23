package es.ucm.fdi.pev;

import es.ucm.fdi.pev.evaluacion.*;
import es.ucm.fdi.pev.seleccion.*;
import javafx.util.Pair;
import es.ucm.fdi.pev.estructura.*;
import java.util.ArrayList;

//Quiza necesita tipo T
public abstract class AGenetico
{

	protected Cromosoma[] poblacion;
	
	protected int tamPoblacion;
	
	protected Cromosoma elMejor;
	protected int mejor_idx;
	
	protected float fitness_total;
	
	protected int maxGeneraciones;
	protected int generacionActual;
	
	protected float prob_cruce;
	protected float prob_mutacion;
	
	protected float tolerancia;
	
	// ---------------- FUNCIONES ---------------- //
	
	public AGenetico(int tamPob) 
	{
		tamPoblacion = tamPob;
		// Recibimos
		// Tamaño de poblacion y genes en cada individuo
		// Probabilidades 
		// Funcion de evaluacion
	}
	
	
	
	
	public void ejecuta()
	{
		// Bucle del algoritmo
		//Genera
		generacionActual = 0;
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

	abstract protected void inicializaPoblacion();
	abstract protected void inicializaGenes();
	abstract protected Cromosoma inicializaCromosoma();
	
	abstract protected void evaluaCromosoma(Cromosoma c);
	
	
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