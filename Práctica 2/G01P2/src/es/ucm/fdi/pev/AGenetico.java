package es.ucm.fdi.pev;

import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.cruce.*;
import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.estructura.*;
import es.ucm.fdi.pev.mutacion.Basica;
import es.ucm.fdi.pev.mutacion.Insercion;
import es.ucm.fdi.pev.mutacion.Inversa;
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
	
	enum Tipo {MINIMIZACION, MAXIMIZACION, DEFAULT}
	Tipo tipo;
	
	// Practica 2
	protected String tipoMutacion;
	//protected int n;
	
	// -------- GRAFICA --------- // 
	
	// Ploteo
	protected Plot2DPanel panel;
	protected JFrame marco;
	protected double[] x_plot;
	// Tendremos 3 lineas, necesitamos 3 ys // PLOT LINE USA DOUBLES
	protected double[] maxGen_y_plot; // Mï¿½ximo de la generaciï¿½n
	protected double[] genMed_y_plot; // media generaciï¿½n
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
	
	
	protected void creaPoblacion()
	{	
		switch (numProblema)
		{
		case 1:
			tipo = Tipo.MAXIMIZACION;
			for(int i = 0; i < tamPoblacion; i++)
				poblacion[i] = new CromosomaP1f1();
			break;
		case 2:
			tipo = Tipo.MINIMIZACION;
			for(int i = 0; i < tamPoblacion; i++)
				poblacion[i] = new CromosomaP1f2();
			break;
		case 3:
			tipo = Tipo.MINIMIZACION;
			for(int i = 0; i < tamPoblacion; i++)
				poblacion[i] = new CromosomaP1f3();
			break;
		case 4:
			tipo = Tipo.MINIMIZACION;
			for(int i = 0; i < tamPoblacion; i++)
				poblacion[i] = new CromosomaP1f4();
			break;
		case 5:
			tipo = Tipo.MINIMIZACION;
			for(int i = 0; i < tamPoblacion; i++)
				poblacion[i] = new CromosomaP1f5();
			break;	
		}
	}
	
	protected void inicializaPoblacion() 
	{	
		poblacion = new Cromosoma[tamPoblacion];
		elite = new LinkedList<Cromosoma>();
		
		switch (numProblema)
		{
			case 6:
				tipo = Tipo.MINIMIZACION;
				P2_ej1(0);
				break;
			case 7:
				tipo = Tipo.MINIMIZACION;
				P2_ej1(1);
				break;
			case 8:
				tipo = Tipo.MINIMIZACION;
				P2_ej1(2);
				break;
			case 9:
				tipo = Tipo.MINIMIZACION;
				P2_ej1(3);
				break;
			default:
				creaPoblacion();
				break;
		}
		
		
		// Valores por defecto inicializados:
		mejor_abs = poblacion[0];
		abs_fitness = mejor_fitness = poblacion[0].evalua();
	}
	
	// Crea la poblacion para el ejercicio concreto (leyendo, en este caso, de fichero)
	protected void P2_ej1(int tipo)
	{
		int tam = 0; 	
		int[][] distancias = null;
		int[][] flujos = null;
		
		String filePath = "";
		switch (tipo) {
			case 0:
				filePath = "Data/ajuste.txt";
				break;
			case 1:
				filePath = "Data/datos12.txt";
				break;
			case 2:
				filePath = "Data/datos15.txt";
				break;
			case 3:
				filePath = "Data/datos30.txt";
				break;
		}
		
		BufferedReader reader;	
		try {
			
			reader = new BufferedReader(new FileReader(filePath));

			String line = reader.readLine();	
			
			
			// 1) INICIALIZACION Y LECTURA DEL TAM. DE LAS MATRICES
			tam = Integer.parseInt(line.trim());
			distancias = new int[tam][tam];
			flujos = new int[tam][tam];
			
			reader.readLine(); // Nos quitamos el espacio en blanco
			line = reader.readLine(); // Leemos la primera línea de la matriz
			
			
			// 2) LECTURA DE LA MATRIZ DE FLUJOS: 
			int i = 0;
			while (!line.isEmpty()) 
			{	
				
				Scanner s = new Scanner(line);
				
				int x = 0;
				while(s.hasNextInt())
				{
					distancias[i][x] = s.nextInt();
					x++;
				}
				
				i++;
				
				s.close();
				line = reader.readLine();
			}
			
			
			// 3) LECTURA DE LA MATRIZ DE FLUJOS: 
			line = reader.readLine(); // Leemos la primera linea de la matriz
			i = 0;
			while (line != null) 
			{	
				Scanner s = new Scanner(line);
				
				int x = 0;
				while(s.hasNextInt())
				{
					flujos[i][x] = s.nextInt();
					x++;
				}
				i++;
				
				s.close();
				line = reader.readLine();
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 4) CREACION DE LA POBLACION CON LOS DATOS YA OBTENIDOS
		
		for(int i = 0; i < tamPoblacion; i++)
			poblacion[i] = new CromosomaP2f1(tam, distancias, flujos);
		
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
			case "Restos":
				pob_idx = Restos.restos(poblacion);
				break;
			case "Truncamiento":
				pob_idx = Truncamiento.truncamiento(poblacion);
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
				case "Monopunto":
					Monopunto.monopunto(poblacion[padre1], poblacion[padre2]);
					break;
				case "Uniforme":
					Uniforme.uniforme(poblacion[padre1], poblacion[padre2]);
					break;
				case "Aritmetico":
					Aritmetico.aritmetico(poblacion[padre1], poblacion[padre2]);
					break;
				case "PMX":
					PMX.pmx(poblacion[padre1], poblacion[padre2]);
					break;
				case "OX":
					OX.ox(poblacion[padre1], poblacion[padre2]);
					break;
				case "OXpp":
					OXpp.oxpp(poblacion[padre1], poblacion[padre2]);
					break;
				case "CX":
					CX.cx(poblacion[padre1], poblacion[padre2]);
					break;
				case "CO":
					CodifOrdinal.codifOrdinal(poblacion[padre1], poblacion[padre2]);
					break;
				case "ERX":
					ERX.erx(poblacion[padre1], poblacion[padre2]);
					break;
				case "HT":
					HT.ht(poblacion[padre1], poblacion[padre2]);
					break;
			}
		}
	}
	
	private void mutacion()
	{			
		for (Cromosoma c : poblacion)
		switch (tipoMutacion)
		{
		case "Basica":
			Basica.basica(c, prob_mutacion);
			break;
		case "Inversa":
			//Inversa.inversa(c, prob_mutacion);
			Insercion.insercion(c, prob_mutacion);
			break;
		}
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
		
		elitismo();
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
			//Recorre los valores de fitness de la generaciï¿½n y saca una media
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
	// Setters Practica 2
	public void setMutacion(String Mutacion) {
		tipoMutacion = Mutacion;
	}
	/*
	public void setN(String N) {
		n = Integer.parseInt(N);
	}
	public void setN(int N) {
		n = N;
	}*/
	
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
		return mejor_abs.getFitness();
	}
	public float[] getMejorFeno() {
		return mejor_abs.fenotipos();
	}
	//Practica 2
	public String getMutacion() {
		return tipoMutacion;
	}
	/*
	public int getN() {
		return n;
	}*/
}