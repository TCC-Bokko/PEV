package es.ucm.fdi.pev;

import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.cruce.*;
import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.estructura.*;

import java.util.ArrayList;
import java.util.Random;

//Quiza necesita tipo T
public abstract class AGenetico
{

	protected Cromosoma[] poblacion;
	
	protected int tamPoblacion;
	
	protected Cromosoma mejor_indiv;
	protected Cromosoma mejor_abs;
	
	protected float mejor_fitness;
	protected float abs_fitness = 0;

	
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
	
	abstract protected void inicializaPoblacion();
	abstract protected void inicializaGenes();
	abstract protected Cromosoma inicializaCromosoma();
	abstract protected Cromosoma sustituyeCromosoma(Cromosoma c);
	abstract protected void evalua_mejor(Cromosoma c); // Actualiza el mejor individuo en función del problema
	abstract protected double calculaMedia(); //Calcula la media de cada generación
	abstract protected void actualizaGrafica();
	abstract protected void dibujaGrafica();
	//abstract protected void evaluaCromosoma(Cromosoma c);
	
	public void ejecuta()
	{
		// Bucle del algoritmo
		//Genera
		System.out.println("-------- INICIO DE POBLACION"  + " --------" );
		generacionActual = 1;
		
		inicializaPoblacion();
		//Evalua
		evaluacion();
		actualizaGrafica();
		
		//
		while (!terminado()) 
		{	
			System.out.println("-------- GENERACION " + generacionActual + " --------" );
			
			//El modifica internamente la poblacion
			seleccion();
		
			cruce();

			mutacion();
			
			evaluacion();			
			
			actualizaGrafica(); //Pasa los datos de esta generación a la gráfica, calcula media y compara maxAbsoluto.
		
			generacionActual++;
		}	
		
		dibujaGrafica();
	}
	

	// Funciones del bucle: 
	
	
	
	private void seleccion()
	{	
		Cromosoma[] nueva_pob = new Cromosoma[poblacion.length];
		int[] pob_idx = new int[poblacion.length]; // Indices de los individuos seleccionados
		//Switch dependiendo del tipo de cruce
		
		/////////////////////////////////////
		//
		// CAMBIAR AQUI LA SELECCION
		//
		//////////////////////////////////////
		
		pob_idx = Ruleta.ruleta(poblacion);
		//pob_idx = Torneo.torneo(poblacion, 3);
		//pob_idx = MUE.mue(poblacion);
		
		// Sustitucion de los individuos seleccionados
		for(int i = 0; i < pob_idx.length; i++)
		{
			int idx = pob_idx[i];
			nueva_pob[i] = sustituyeCromosoma(poblacion[idx]);
		}
		
		poblacion = nueva_pob.clone();
	}
	
	private void cruce() 
	{
		//PARA PROBAR: LO PONEMOS AQUI
		prob_cruce = 0.6f;
			
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
			int padre1 = sel.get(i);
			int padre2 = sel.get(i+1);
			
			//Uniforme.uniforme(poblacion[padre1], poblacion[padre2]);
			Monopunto.monopunto(poblacion[padre1], poblacion[padre2]);
		}
	}
	
	private void mutacion()
	{
		//PARA PROBAR: LO PONEMOS AQUI
		prob_mutacion = 0.05f;
				
		for (Cromosoma c : poblacion)
			c.muta(prob_mutacion);		
	}
	
	private void evaluacion() 
	{
		fitness_total = mejor_fitness = 0;
		
		for (Cromosoma c : poblacion)
		{
			// Calculo de fitness de cada individuo
			c.evalua();
			
			// Calculo del fitness total de la poblacion		
			fitness_total += c.getFitness();
			
			//System.out.println("Fitness: " + poblacion[i].getFitness());
		
					
			evalua_mejor(c);
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
	
	
	private boolean terminado() 
	{
		return generacionActual > maxGeneraciones;
	}
}