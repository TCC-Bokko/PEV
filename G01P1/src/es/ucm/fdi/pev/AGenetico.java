package es.ucm.fdi.pev;

import es.ucm.fdi.pev.evaluacion.*;
import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.cruce.Monopunto;
import es.ucm.fdi.pev.estructura.*;

import java.util.ArrayList;
import java.util.Random;

//Quiza necesita tipo T
public abstract class AGenetico
{

	protected Cromosoma[] poblacion;
	
	protected int tamPoblacion;
	
	protected Cromosoma mejor_indiv;
	protected float mejor_fitness;
	
	protected float fitness_total;
	
	protected int maxGeneraciones;
	protected int generacionActual;
	
	protected float prob_cruce;
	protected float prob_mutacion;
	
	protected float tolerancia;
	
	// ---------------- FUNCIONES ---------------- //
	
	public AGenetico(int tamPob, int maxGen) 
	{
		tamPoblacion = tamPob;
		maxGeneraciones = maxGen;
		// Recibimos
		// Tamaño de poblacion y genes en cada individuo
		// Probabilidades 
		// Funcion de evaluacion
	}
	
	
	
	
	public void ejecuta()
	{
		// Bucle del algoritmo
		//Genera
		System.out.println("-------- INICIO DE POBLACION"  + " --------" );
		generacionActual = 1;
		inicializaPoblacion();
		
		//Evalua
		evaluacion();
		
		//
		while (!terminado()) 
		{	
			System.out.println("-------- GENERACION " + generacionActual + " --------" );
			//El modifica internamente la poblacion
			seleccion();
		
			cruce();

			mutacion();
			
			evaluacion();			
			
			generacionActual++;
		}	
	}
	
	
	
	// Funciones del blucle: 

	abstract protected void inicializaPoblacion();
	abstract protected void inicializaGenes();
	abstract protected Cromosoma inicializaCromosoma();
	abstract protected void evalua_mejor(Cromosoma c); // Actualiza el mejor individuo en función del problema
	
	//abstract protected void evaluaCromosoma(Cromosoma c);
	
	
	private void evaluacion() 
	{
		fitness_total = mejor_fitness = 0;
		for (int i = 0; i < poblacion.length; i++) 
		{
			// Calculo de fitness de cada individuo
			poblacion[i].evalua();
			
			// Calculo del fitness total de la poblacion		
			fitness_total += poblacion[i].getFitness();
					
			evalua_mejor(poblacion[i]);
		}
		
		
		// Probabilidad relativa [0,1) para metodos de seleccion
		float punt_acum = 0;
		for (int j = 0; j < poblacion.length; j++)
		{
			poblacion[j].actualiza_puntuacion(fitness_total);
			poblacion[j].actualiza_punt_acum(punt_acum);
			punt_acum = punt_acum + poblacion[j].getPuntuacion();
		}
	}
	
	private void seleccion()
	{
		//Switch dependiendo del tipo de cruce
		poblacion = Ruleta.ruleta(poblacion);
	}
	
	private void cruce() 
	{
		//PARA PROBAR: LO PONEMOS AQUI
		prob_cruce = 0.8f;
		
		
		// Array con los índices de los padres seleccionados para cruzarse
		ArrayList<Integer> sel = new ArrayList<Integer>();
		
		Random r = new Random();
		for (int i = 0; i < poblacion.length; i++)
		{
			float prob = r.nextFloat();
			if(prob < prob_cruce)
				sel.add(i);
		}
		
		// Si salen impares, eliminamos al último simplemente
		if((sel.size() % 2) == 1)
			sel.remove(sel.size() -1);
		
					
		for (int i = 0; i < sel.size(); i+=2)
		{
			//Switch dependiendo del tipo de cruce
			int padre1 = sel.get(i);
			int padre2 = sel.get(i+1);
			
			Monopunto.monopunto(poblacion[padre1], poblacion[padre2]);
		}		
	}
	
	private void mutacion()
	{
		//Switch dependiendo del tipo de mutacion
	}
	
	
	private boolean terminado() 
	{
		return generacionActual > maxGeneraciones;
	}
}