package es.ucm.fdi.pev;

import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.cruce.*;
import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.estructura.*;

import java.awt.Color;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

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
	
	protected float elitismo;
	protected Queue<Cromosoma> elite;
	
	protected String tipoSeleccion;
	protected String tipoCruce;
	
	// -------- GRAFICA --------- // 
	
	// Ploteo
	protected Plot2DPanel panel;
	protected JFrame marco;
	protected double[] x_plot;
	// Tendremos 3 líneas, necesitamos 3 ys // PLOT LINE USA DOUBLES
	protected double[] maxGen_y_plot; // Máximo de la generación
	protected double[] genMed_y_plot; // media generación
	protected double[] maxAbs_y_plot; // Maximo absoluto

	//Constructora vacia
	public AGenetico() {
		
	}
	
	//Constructora con 2 parametros
	public AGenetico(int tamPob, int maxGen) 
	{
		elite = new LinkedList<Cromosoma>();
		
		tamPoblacion = tamPob;
		maxGeneraciones = maxGen;
		// Recibimos
		// Tamaño de poblacion y genes en cada individuo
		// Probabilidades 
		// Funcion de evaluacion
		
		iniciaGrafica();
	}
	

	// ---------------- FUNCIONES ---------------- //
	abstract protected void inicializaGenes();
	abstract protected Cromosoma inicializaCromosoma();
	abstract protected Cromosoma sustituyeCromosoma(Cromosoma c);	
	protected void inicializaPoblacion() 
	{
		inicializaGenes();
		
		poblacion = new Cromosoma[tamPoblacion];
		
		for(int i = 0; i < tamPoblacion; i++)
		{
			poblacion[i] = inicializaCromosoma();
			//System.out.println(poblacion[i].fenotipos()[0]+","+poblacion[i].fenotipos()[1]);
		}	
	}
	
	//abstract protected void evaluaCromosoma(Cromosoma c);
	
	public void ejecuta()
	{
		// Bucle del algoritmo
		//Genera
		System.out.println("-------- INICIO DE POBLACION"  + " --------" );
		generacionActual = 1;
		
		inicializaPoblacion();
		evaluacion();
		actualizaGrafica();
	
		
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
		
		//PRUEBA ELITISMO
		elitismo = 0.1f;
		
		
		Cromosoma[] nueva_pob = new Cromosoma[poblacion.length];
		int[] pob_idx = new int[poblacion.length]; // Indices de los individuos seleccionados
		//Switch dependiendo del tipo de cruce
		
		/////////////////////////////////////
		//
		// CAMBIAR AQUI LA SELECCION
		//
		//////////////////////////////////////
		switch (tipoSeleccion) {
			case "Ruleta":
				pob_idx = Ruleta.ruleta(poblacion);
				break;
			case "MUE":
				pob_idx = MUE.mue(poblacion);
				break;
			case "Torneo":
				pob_idx = Torneo.torneo(poblacion, 3);
				break;	
		}
		
		// Sustitucion de los individuos seleccionados
		for(int i = 0; i < pob_idx.length; i++)
		{
			int idx = pob_idx[i];
			nueva_pob[i] = sustituyeCromosoma(poblacion[idx]);
		}
		
		
		poblacion = sustituyeElite(nueva_pob);
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
			
			switch (tipoCruce) {
				case "Monopunto":
					Monopunto.monopunto(poblacion[padre1], poblacion[padre2]);
					break;
				case "Uniforme":
					System.out.print("OJO CRUCE UNIFORME COMENTADO");
					Uniforme.uniforme(poblacion[padre1], poblacion[padre2]);
					break;
			}
		}
	}
	
	private void mutacion()
	{
		//PARA PROBAR: LO PONEMOS AQUI
		prob_mutacion = 0.03f;
				
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
		
		adapta_puntuacion();
		
		elitismo();
	}
	
	protected void elitismo()
	{
		// Si hay porcentaje de elitismo:
		if(elitismo > 0.0f)
		{
			int tamElite = (int) (tamPoblacion * elitismo);
			Arrays.sort(poblacion);		
			
			System.out.println("Tam Elite: " + tamElite);
			
			for(int i = 0; i < tamElite; i++)
				elite.add(poblacion[i]);
		}
	}
	
	protected Cromosoma[] sustituyeElite(Cromosoma[] pob)
	{
		if(elitismo > 0 && elite.size() > 0)
		{
			Arrays.sort(pob, Collections.reverseOrder());
			
			for(int i = 0; i < elite.size(); i++)
				pob[i] = sustituyeCromosoma(elite.poll());
		}
		
		return pob.clone();
	}
	
	
	protected void evalua_mejor(Cromosoma c) 
	{	
		//System.out.println("Fitness: " + c.getFitness());
		//System.out.println("Abs: " + abs_fitness + "  Gen: " + mejor_fitness);
		// PARA MAXIMIZACION SERÍA ASÍ. MINIMIZACION SERÍA '<'
		
		if (generacionActual == 1) abs_fitness = mejor_fitness;
		if(c.compara_mejor_fitness(mejor_fitness))
		{
			mejor_fitness = c.getFitness();
			
			if(c.compara_mejor_fitness(abs_fitness))
				abs_fitness = c.getFitness();
		}	
	}
	
	
	private boolean terminado() 
	{
		return generacionActual > maxGeneraciones;
	}
	
	
	protected void adapta_puntuacion() 
	{
		// Probabilidad relativa [0,1) para metodos de seleccion
		float punt_acum = 0;
		for (int j = 0; j < poblacion.length; j++)
		{
			poblacion[j].actualiza_puntuacion(fitness_total);
			poblacion[j].actualiza_punt_acum(punt_acum);
			punt_acum = punt_acum + poblacion[j].getPuntuacion();
		}
	}
	
	
	// -------------------------- GRAFICA -------------------------- // 
	
	
		private void iniciaGrafica() {
			
			x_plot = new double[maxGeneraciones]; //Empezamos en generación 1! OJO!
			maxGen_y_plot = new double[maxGeneraciones]; // Máximo de la generación
			genMed_y_plot = new double[maxGeneraciones]; // media generación
			maxAbs_y_plot = new double[maxGeneraciones]; // Maximo absoluto

			
			panel = new Plot2DPanel();
			marco = new JFrame("Funcion1");
			
			//Los Xs van establecidos por defecto (0,1,2,3,4,..., MAX_GENERACIONES-1), tam = maxGeneraciones
			// OJO QUE EMPEZAMOS POR GENERACION 1, guardar los datos en una posición generación-1.
			for (int i = 0; i < maxGeneraciones; i++) {
				x_plot[i] = i;
			}
		}
		
		protected void dibujaGrafica() 
		{
			//Dibujamos las líneas
			panel.addLinePlot("MaxGen", Color.blue, x_plot, maxGen_y_plot);
			panel.addLinePlot("MaxAbs", Color.red, x_plot, maxAbs_y_plot);
			panel.addLinePlot("genMed", Color.green, x_plot, genMed_y_plot);
			
			//Propiedades marco
			marco.setSize(800,600);
			marco.setContentPane(panel);
			marco.setVisible(true);
		}
		
		protected double calculaMedia() {
			//Recorre los valores de fitness de la generación y saca una media
			float sum = 0.0f;
			double media = 0.0f;
			
			//Sumamos los fitnes
			for (int i = 0; i < tamPoblacion; i++) {
				sum = sum + poblacion[i].getFitness();
			}
			
			media = (double)sum / (double)tamPoblacion;		
			
			
			System.out.println("Mejor abs: " + abs_fitness);
			
			return media;		
		}
		
		
		protected void actualizaGrafica() {
			// Rellena valores grafica
			maxGen_y_plot[generacionActual-1] = (double)mejor_fitness; // Generacion -1 por que empezamos en 1! 
			maxAbs_y_plot[generacionActual-1] = (double)abs_fitness;
			genMed_y_plot[generacionActual-1] = calculaMedia();
		}
		
		/////////////////////////////////////////////////
		//
		// GETTERS Y SETTERS (Usados en GUI)
		//
		/////////////////////////////////////////////////
		public void setTamPob(int tamPob) {
			tamPoblacion = tamPob;
		}
		public void setMaxGen(int maxGen) {
			maxGeneraciones = maxGen;
		}
		public void setProbCruce(double probCruce) {
			prob_cruce = (float)probCruce;
		}
		public void setProbMut(double probMut) {
			prob_mutacion = (float)probMut;
		}
		public void setElitismo(double elit) {
			elitismo = (float)elit;
		}
}