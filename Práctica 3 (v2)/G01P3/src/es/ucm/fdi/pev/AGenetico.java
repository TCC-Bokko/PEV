package es.ucm.fdi.pev;

import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.Utils.Utils;
import es.ucm.fdi.pev.cruce.*;
import es.ucm.fdi.pev.estructura.*;
import es.ucm.fdi.pev.mutacion.Funcion;
import es.ucm.fdi.pev.mutacion.Terminal;
import es.ucm.fdi.pev.ui.GUI;
import es.ucm.fdi.pev.ui.Grafica;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

//Quiza necesita tipo T
public class AGenetico
{
	protected Cromosoma[] poblacion;
	protected Queue<Cromosoma> elite;	
	
	protected Cromosoma mejor_indiv;
	protected Cromosoma mejor_abs;
	protected float mejor_fitness;
	protected float abs_fitness = 0;
	protected float abs_worst_fitness = 0;

	protected Grafica _grafica;
	protected float fitness_total;
	protected float tolerancia;
	
	protected int tamPoblacion;
	protected int maxGeneraciones;
	protected int generacionActual;
	protected String tipoCruce;
	protected String tipoSeleccion;

	protected int numProblema;
	
	protected float prob_cruce;
	protected float prob_mutacion;
	protected float elitismo;
	
	//Memoria P2
	protected int generacionMejor = 0;
	protected int generacionPeor = 0;
	protected int numCrucesTotal = 0;
	protected int numMutacionesTotal = 0;
	protected Cromosoma peor_abs;
	protected float peor_fitness;
	
	enum Tipo {MINIMIZACION, MAXIMIZACION, DEFAULT}
	Tipo tipo;
	
	// Practica 2
	protected String tipoMutacion;
	//protected int n;
	
	
	// Pr�ctica 3
	protected int entradas;	//cantidad de lineas de direccionamiento (Adress)
	protected String useif;
	protected int pmax;
	//protected int pmin;
	protected String initC;
	protected String bloat;
	
	protected double mediaNodos;
	protected double mediaProfundidad;
	protected double k; // Coeficiente penalizacion para bloating.

		// Para la evaluaci�n.
	
	// -------- GRAFICA --------- // 
	
	// Ploteo
	protected Plot2DPanel panel;
	protected JFrame marco;
	protected double[] x_plot;
	// Tendremos 3 lineas, necesitamos 3 ys // PLOT LINE USA DOUBLES
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
		
		// MENSAJES DE FIN DE EJECUCION		
		System.out.println("__FIN DE LA EJECUCION__");
		System.out.printf("Poblacion: %d\n", tamPoblacion);
		System.out.printf("Generaciones: %d\n", maxGeneraciones);
		System.out.printf("Cruces: %d\n", numCrucesTotal);
		System.out.printf("Mutaciones: %d\n", numMutacionesTotal);

		_grafica.dibujaGrafica();
	}
	
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
		poblacion = new Cromosoma[tamPoblacion];
		elite = new LinkedList<Cromosoma>();
		
		CromosomaP3.creaPermutaciones(entradas);
		
		for(int i = 0; i < tamPoblacion; i++)
			poblacion[i] = new CromosomaP3(pmax, initC, true);
		
	
		
		// Valores por defecto inicializados:
		mejor_abs = poblacion[0];
		peor_abs = poblacion[0];
		abs_fitness = mejor_fitness = peor_fitness = abs_worst_fitness = poblacion[0].evalua();
		
		tipo = Tipo.MAXIMIZACION;
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
			case "Ranking":
				pob_idx = Ranking.ranking(poblacion);
				break;
			case "MUE":
				pob_idx = MUE.mue(poblacion);
				break;
			case "Torneo":
				pob_idx = Torneo.torneo(poblacion, 3);
				break;
			case "Restos":
				pob_idx = Restos.restos(poblacion);
				break;
			case "Truncamiento":
				pob_idx = Truncamiento.truncamiento(poblacion, "minimizacion");
				break;
		}
		
		// Sustitucion de los individuos seleccionados
		for(int i = 0; i < pob_idx.length; i++)
		{
			int idx = pob_idx[i];
			nueva_pob[i] = poblacion[idx].clone(); //sustituyeCromosoma(poblacion[idx]);
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
				case "Permutacion":
					CruceArbol.cruceArbol((CromosomaP3)poblacion[padre1], (CromosomaP3)poblacion[padre2]);
					break;
					
					default:
						CruceArbol.cruceArbol((CromosomaP3)poblacion[padre1], (CromosomaP3)poblacion[padre2]);
						break;
			}
			numCrucesTotal++;
		}
	}
	
	private void mutacion()
	{			
		boolean haMutado = false;
		int i = 0; //indice individuos
		int mutacionesGeneracion = 0;
		for (Cromosoma c : poblacion) {
			switch (tipoMutacion)
			{
			 // PRACTICA 3
			 case "Terminal":
			 	haMutado = Terminal.terminal(c, entradas, prob_mutacion);
			 	break;
			 case "Funcion":
			 	haMutado = Funcion.funcion(c, prob_mutacion);
			 	break;
			 case "Permutacion":
			 	//haMutado = Permutacion.permutacion(c, prob_mutacion);
			 	break;
			 case "Hoist":
			 //	haMutado = Hoist.hoist(c, prob_mutacion);
			 	break;
			 case "Expansion":
			 //	haMutado = Expansion.expansion(c, prob_mutacion);
			 	break;
			 case "Contraccion":
			 //	haMutado = Contraccion.contraccion(c, prob_mutacion);
			 	break;
			 case "Subarbol":
			 //	haMutado = Subarbol.subarbol(c, prob_mutacion);
			 	break;
			}
			
			if (haMutado) {
				System.out.printf("en el individuo #%d.\n", i);
				numMutacionesTotal++;
				mutacionesGeneracion++;
			}
			
			i++;
		}
		if (mutacionesGeneracion == 0) System.out.println("No se han producido mutaciones en esta generaci�n.");
		else System.out.printf("Se han producido %d mutaciones esta generaci�n. \n", mutacionesGeneracion);
	}
	
	private void evaluacion() 
	{	
		fitness_total = 0;
		mejor_fitness = poblacion[0].getFitness();
		
		for (Cromosoma c : poblacion)
		{
			// Calculo de fitness de cada individuo
			c.evalua();
			
			// Calculo del fitness total de la poblacion		
			fitness_total += c.getFitness();
			evalua_mejor(c);
		}
		
		
		switch (tipo)
		{
		case MINIMIZACION:
			minimizacion();
			break;
		case MAXIMIZACION:
			maximizacion();
			break;			
		default:
			adapta_puntuacion();
			break;
		}
		
		System.out.println("Mejor fitness gen.: " + mejor_fitness);
		System.out.println("Abs fitness: " + abs_fitness);
		
		bloating();

		elitismo();
	}
	
	protected void bloating()
	{	
		switch (bloat) 
		{
		case "Tarpeian": 
			bloating_tarpeian();
			break;
			
		case "Penalizacion":
			bloating_penalizacion();
			break;
		default:
			break;
		}
		
		for (Cromosoma c : poblacion)
			bloating((CromosomaP3) c);
	}
	
	protected void bloating(CromosomaP3 c) 
	{
		switch (bloat) 
		{
		case "Tarpeian": 
			
			int n = 1; // Se mirara probabilidad 1/n
			int rnd = new Random().nextInt();
			
			// Ponemos un fitness muy bajo
			if(c.getArbol().size() > mediaNodos && rnd % n == 0)
				c.setFitness(-1000000);
			break;
			
		case "Penalizacion":
			float f = c.getFitness() - (float)(k * c.getArbol().size());
			c.setFitness(f);
			break;
			
		default:
			break;
		}
	}
	
	
	protected void bloating_tarpeian() {
		int nodos = 0;
		
		for(Cromosoma c: poblacion)
			nodos += ((CromosomaP3) c).getArbol().size();
			
		mediaNodos = nodos / poblacion.length;
	}
	

	protected void bloating_penalizacion() {
		
		ArrayList<Float> nodos = new ArrayList<Float>();
		ArrayList<Float> fitness = new ArrayList<Float>();
		
		for(Cromosoma c : poblacion)
		{
			nodos.add((float) ((CromosomaP3) c).getArbol().size());
			fitness.add(c.getFitness());
		}
		
		k = Utils.covarianza(nodos, fitness) / Utils.varianza(nodos);		
	}
	
	
	protected void elitismo()
	{
		// Si hay porcentaje de elitismo:
		if(elitismo > 0.0f)
		{
			int tamElite = (int) (tamPoblacion * elitismo);
			Arrays.sort(poblacion);
			
			//System.out.println("Tam Elite: " + tamElite);
			
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
				pob[i] = elite.poll().clone();
		}
		
		return pob.clone();
	}
		
	protected void evalua_mejor(Cromosoma c) 
	{	
		if(c.compara_mejor_fitness(mejor_fitness))
		{
			mejor_fitness = c.getFitness();
			
			if(c.compara_mejor_fitness(abs_fitness)) {
				abs_fitness = c.getFitness();
				mejor_abs = c;
				generacionMejor = generacionActual;
			}
		}
		
		if(c.compara_peor_fitness(peor_fitness))
		{
			peor_fitness = c.getFitness();
			
			if(c.compara_peor_fitness(abs_worst_fitness)) {
				abs_worst_fitness = c.getFitness();
				peor_abs = c;
				generacionPeor = generacionActual;
			}
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
	
	
	protected void maximizacion()
	{
		float fmin = Float.POSITIVE_INFINITY;
		
		for (Cromosoma c : poblacion)
		{
			if(c.getFitness() < fmin)
				fmin = c.getFitness();
		}	
		
		fmin = fmin * 1.05f; // Margen
		
		fitness_total = 0;
		
		float[] fitness = new float[poblacion.length];
		
		for (int i = 0; i < fitness.length; i++)
		{
			fitness[i] = poblacion[i].getFitness() - fmin;
			fitness_total += fitness[i];
		}
		
		float punt_acum = 0;
		for (int i = 0; i < fitness.length; i++)
		{
			poblacion[i].setPuntuacion(fitness[i] / fitness_total);
			poblacion[i].actualiza_punt_acum(punt_acum);
			punt_acum = punt_acum + poblacion[i].getPuntuacion();
		}
	}
	
	protected void minimizacion()
	{
		float fmax = Float.NEGATIVE_INFINITY;
		
		for (Cromosoma c : poblacion)
		{
			if(c.getFitness() > fmax)
				fmax = c.getFitness();
		}	
		
		fmax = fmax * 1.05f; // Margen
		
		fitness_total = 0;
		
		float[] fitness = new float[poblacion.length];
		
		for (int i = 0; i < fitness.length; i++)
		{
			fitness[i] = fmax - poblacion[i].getFitness();
			fitness_total += fitness[i];
		}
		
		float punt_acum = 0;
		for (int i = 0; i < fitness.length; i++)
		{
			poblacion[i].setPuntuacion(fitness[i] / fitness_total);
			poblacion[i].actualiza_punt_acum(punt_acum);
			punt_acum = punt_acum + poblacion[i].getPuntuacion();
		}
	}
		
	protected double calculaMedia() {
			//Recorre los valores de fitness de la generaci�n y saca una media
			float sum = 0.0f;
			double media = 0.0f;
			
			//Sumamos los fitness
			for (int i = 0; i < tamPoblacion; i++) {
				sum = sum + poblacion[i].getFitness();
			}
			
			media = (double)sum / (double)tamPoblacion;		
			
			
			//System.out.println("Mejor abs: " + abs_fitness);
			
			return media;		
		}
		
	/////////////////////////////////////////////////
	//
	// GETTERS Y SETTERS (Usados en GUI)
	//
	/////////////////////////////////////////////////
	
	public void setNumProblema(int n) { numProblema = n; }
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
	public void setMutacion(String Mutacion) {
		tipoMutacion = Mutacion;
	}
	// Setters Pr�ctica 3
	public void setNumAs(String As) {
		if (As == "2") entradas = 2;
		else if (As == "3") entradas = 3;
	}
	public void setUseIf(String uif){
		useif = uif;
	}
	/*
	public void setPmin(int pm) {
		pmin = pm;
	}
	*/
	public void setPmax(int pM) {
		pmax = pM;
	}
	public void setInitType(String iniT) {
		initC = iniT;
	}
	public void setBloat(String b) {
		bloat = b;
	}
	
	//GETTERS
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
		// Especifico para P3
		/*
		CromosomaP3 CAma = (CromosomaP3) mejor_abs;
		return CAma.getFitness();
		*/
		return abs_fitness;
	}
	
	public float[] getMejorFeno() {
		return mejor_abs.fenotipos();
	}
	public String getMejorFenoString() {
		// Especifico para P3
		CromosomaP3 CAma = (CromosomaP3) mejor_abs;
		return CAma.genotipo();
	}
	public String getMutacion() {
		return tipoMutacion;
	}
	public int getNumProbl() {
		return numProblema;
	}
	public int getGenMejor() {
		return generacionMejor;
	}
}