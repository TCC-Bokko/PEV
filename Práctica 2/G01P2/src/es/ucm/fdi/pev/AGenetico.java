package es.ucm.fdi.pev;

import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.cruce.*;
import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.estructura.*;
import es.ucm.fdi.pev.ui.GUI;
import es.ucm.fdi.pev.ui.Grafica;

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
	protected Cromosoma mejor_indiv;
	protected Cromosoma mejor_abs;
	
	protected float mejor_fitness;
	protected float abs_fitness = 0;

	protected Grafica _grafica;
	protected float fitness_total;
	protected float tolerancia;
	
	protected int tamPoblacion;
	protected int maxGeneraciones;
	protected int generacionActual;
	protected String tipoCruce;
	protected String tipoSeleccion;
	protected float prob_cruce;
	protected float prob_mutacion;
	protected float elitismo;
	protected Queue<Cromosoma> elite;	
	
	// -------- GRAFICA --------- // 
	
	// Ploteo
	protected Plot2DPanel panel;
	protected JFrame marco;
	protected double[] x_plot;
	// Tendremos 3 l�neas, necesitamos 3 ys // PLOT LINE USA DOUBLES
	protected double[] maxGen_y_plot; // M�ximo de la generaci�n
	protected double[] genMed_y_plot; // media generaci�n
	protected double[] maxAbs_y_plot; // Maximo absoluto

	//Constructora vacia
	public AGenetico() {
	}
	
	//Constructora con 2 parametros
	public AGenetico(int tamPob, int maxGen) 
	{	
		tamPoblacion = tamPob;
		maxGeneraciones = maxGen;
		tipoSeleccion = "MUE";
		tipoCruce = "Monopunto";
		
		
		inicializaGrafica();
	}
	

	// ---------------- FUNCIONES ---------------- //
	
	
	abstract protected void inicializaGenes();
	abstract protected Cromosoma inicializaCromosoma();
	abstract protected Cromosoma sustituyeCromosoma(Cromosoma c);
	
	protected void inicializaGrafica() {
		marco = new JFrame();
		panel = new Plot2DPanel();
		//panel.setSize(600, 600);
		//panel.setVisible(true);	
		_grafica = new Grafica(panel);
		_grafica.setGen(maxGeneraciones);
		_grafica.setPob(tamPoblacion);
		_grafica.init();
		marco.setSize(600, 600);
		marco.setVisible(true);
		marco.add(_grafica.getGrafica());
	}
	
	protected void inicializaPoblacion() 
	{
		inicializaGenes();
		
		poblacion = new Cromosoma[tamPoblacion];
		elite = new LinkedList<Cromosoma>();
		
		for(int i = 0; i < tamPoblacion; i++)
			poblacion[i] = inicializaCromosoma();
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
		double media = calculaMedia();
		_grafica.actualizaGrafica(poblacion, generacionActual, mejor_fitness, abs_fitness, (float)media);
	
		while (!terminado()) 
		{	
			System.out.println("-------- GENERACION " + generacionActual + " --------" );
			
			//El modifica internamente la poblacion
			seleccion();
		
			cruce();

			mutacion();
			
			evaluacion();	
			
			media = calculaMedia();
			_grafica.actualizaGrafica(poblacion, generacionActual, mejor_fitness, abs_fitness, (float)media); //Pasa los datos de esta generacion a la grafica, calcula media y compara maxAbsoluto.
		
			generacionActual++;
		}	
		
		_grafica.dibujaGrafica();
	}
	
	// FUNCIONES DEL BUCLE
	
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
		// Array con los indices de los padres seleccionados para cruzarse
		ArrayList<Integer> sel = new ArrayList<Integer>();
		
		Random r = new Random();
		for (int i = 0; i < poblacion.length; i++)
		{
			float prob = r.nextFloat();
			if(prob < prob_cruce)
				sel.add(i);
		}
		
		// Si salen impares, eliminamos al ultimo simplemente
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
					Uniforme.uniforme(poblacion[padre1], poblacion[padre2]);
					break;
				case "Aritmetico":
					Aritmetico.aritmetico(poblacion[padre1], poblacion[padre2]);
					break;
			}
		}
	}
	
	private void mutacion()
	{			
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
		if (generacionActual == 1) {
			abs_fitness = mejor_fitness;
			mejor_abs = c;
		}
		if(c.compara_mejor_fitness(mejor_fitness))
		{
			mejor_fitness = c.getFitness();
			
			if(c.compara_mejor_fitness(abs_fitness))
				abs_fitness = c.getFitness();
				mejor_abs = c;
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
		
	protected double calculaMedia() {
			//Recorre los valores de fitness de la generaci�n y saca una media
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
	public void setTipSel(String tipoSel) {
		tipoSeleccion = tipoSel;
	}
	public void setTipCru(String tipoCru) {
		tipoCruce = tipoCru;
	}
	public void setGrafica(Grafica grafica) {
		_grafica = grafica;
	}
	public int getTamPob() {
		return tamPoblacion;
	}
	public int getMaxGen() {
		return maxGeneraciones;
	}
	public double getProbCruce() {
		return (double)prob_cruce;
	}
	public double getProbMut() {
		return (double)prob_mutacion;
	}
	public double getElitismo() {
		return (double)elitismo;
	}
	public String getTipSel() {
		return tipoSeleccion;
	}
	public String getTipCru() {
		return tipoCruce;
	}
	public float getMejorFit() {
		return mejor_abs.getFitness();
	}
	public float[] getMejorFeno() {
		return mejor_abs.fenotipos();
	}
}