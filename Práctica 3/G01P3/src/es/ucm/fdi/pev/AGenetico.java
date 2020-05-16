package es.ucm.fdi.pev;

import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.cruce.*;
import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.estructura.*;

//Clase Cruce
import es.ucm.fdi.pev.cruce.CruceArbol;

//Clases Mutacion
import es.ucm.fdi.pev.mutacion.Terminal;
import es.ucm.fdi.pev.mutacion.Funcion;
import es.ucm.fdi.pev.mutacion.Permutacion;
import es.ucm.fdi.pev.mutacion.Hoist;
import es.ucm.fdi.pev.mutacion.Expansion;
import es.ucm.fdi.pev.mutacion.Contraccion;
import es.ucm.fdi.pev.mutacion.Subarbol;

//Clases GUI
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
	protected String tipoMutacion;

	protected int numProblema;
	
	protected float prob_cruce;
	protected float prob_mutacion;
	protected float elitismo;
	
	// Memoria
	protected int generacionMejor = 0;
	protected int generacionPeor = 0;
	protected int numCrucesTotal = 0;
	protected int numMutacionesTotal = 0;
	protected Cromosoma peor_abs;
	protected float peor_fitness;
	
	enum Tipo {MINIMIZACION, MAXIMIZACION, DEFAULT}
	Tipo tipo;

	// Práctica 3
	protected int numAs;	//cantidad de lineas de direccionamiento (Adress)
	protected String useif;
	protected int pmax;
	//protected int pmin;
	protected String initC;
	protected String bloat;
	protected double tamArbolMedioPobl;
	protected double k; // Coeficiente penalizacion para bloating.
	protected double nodos_total;
	// Para la evaluación.
	String[] valoresMultiplexor;
	Boolean debugEjecucion = true;
	
	
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
		if (debugEjecucion) System.out.println("[AGenetico.Ejecuta()]");
		// Bucle del algoritmo
		// Genera
		System.out.println("-------- INICIO DE POBLACION"  + " --------" );
		generacionActual = 1;
		
		// INICIALIZACIONES ESPECIFICAS P3
		k = 0d;
		tamArbolMedioPobl = pmax;
		// Inicialización del vector de operandos (Solo una vez durante toda la ejecución).
		int tam = 0;
		if (numAs == 2) {
			tam = 6; // A0,A1,D0,D1,D2,D3
			valoresMultiplexor = new String[128];
		} else if (numAs == 3) {
			tam = 11; // A0, A1, A2, D0, D1, D2, D3, D4, D5, D6, D7
			valoresMultiplexor = new String[2048];
		} else {
			System.out.println("[Evaluacion-Arbol] Error. NumAs tiene un valor distinto de 2 o 3.");
		}
		valoresMultiplexor = inicializaValores(valoresMultiplexor, tam);
		
		// Llamadas a metodos de la ejecución
		inicializaPoblacion();
		evaluacion(); 
		double media = calculaMedia();
		_grafica.actualizaGrafica(poblacion, generacionActual, mejor_fitness, abs_fitness, (float)media);
		
		
		
		// BUCLE PRINCIPAL
		while (!terminado()) 
		{	
			System.out.println("-------- GENERACION " + generacionActual + " --------" );
			
			//El modifica internamente la poblacion
			seleccion();
		
			cruce();

			mutacion();
			
			evaluacion();	
			
			media = calculaMedia();
			
			// Especificos de P3
			tamArbolMedioPobl = calculaTamMed(); // Primera ejecución coge el tamMax. En siguientes lee de variable.
			calculaK(); // Calculo de coeficiente para penalizar en bloating (Actualiza k) Primera ejecución k = 0;
			
			_grafica.actualizaGrafica(poblacion, generacionActual, mejor_fitness, abs_fitness, (float)media); //Pasa los datos de esta generacion a la grafica, calcula media y compara maxAbsoluto.
		
			generacionActual++;
		}
		
		// MENSAJES DE FIN DE EJECUCION		
		System.out.println("__FIN DE LA EJECUCION__");
		System.out.printf("Poblacion: %d\n", tamPoblacion);
		System.out.printf("Generaciones: %d\n", maxGeneraciones);
		System.out.printf("Cruces: %d\n", numCrucesTotal);
		System.out.printf("Mutaciones: %d\n", numMutacionesTotal);
		// Obtener mejor cromosoma absoluto del algoritmo genético
		Gen[] genes = mejor_abs.getGenes();
		String textoMejorAbs = "Mejor Invididuo. Genes: [";
		int value;
		for (int i = 0; i < genes.length; i++) {
			GenEntero GMJ = (GenEntero) genes[i];
			value = GMJ.getAlelo();
			textoMejorAbs = textoMejorAbs + Integer.toString(value);
			textoMejorAbs = textoMejorAbs + ", ";
		}
		textoMejorAbs = textoMejorAbs + "]";
		System.out.println(textoMejorAbs);
		System.out.printf("Mejor fitness: %d\n", (int) mejor_fitness);
		System.out.printf("Generacion del mejor: %d\n", generacionMejor);
		// Obtener peor cromosoma
		genes = peor_abs.getGenes();
		String textoPeorAbs = "Peor Invididuo. Genes: [";
		for (int i = 0; i < genes.length; i++) {
			GenEntero GMJ = (GenEntero) genes[i];
			value = GMJ.getAlelo();
			textoPeorAbs = textoPeorAbs + Integer.toString(value);
			textoPeorAbs = textoPeorAbs + ", ";
		}
		textoPeorAbs = textoPeorAbs + "]";
		System.out.println(textoPeorAbs);
		System.out.printf("Peor fitness: %d\n", (int) peor_fitness);
		System.out.printf("Generacion del peor: %d\n", generacionPeor);
		System.out.printf("Tamaño medio poblacion: %d", tamArbolMedioPobl);
		System.out.printf("Coef. Bloating penalizacion: %d", k);
		_grafica.dibujaGrafica();
	}
	
	protected void inicializaGrafica() {
		if (debugEjecucion) System.out.println("[AGenetico.inicializaGrafica()]");
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
		if (debugEjecucion) System.out.printf("[AGenetico.creaPoblacion()] InitC = %s\n", initC);
		if (initC == "RampedAndHalf") {
			//Debemos dividir la población en grupos
			// Tantos grupos como PROF_MAX-1
			int grupos = pmax - 1;
			
			// Dividir individuos entre grupos
			boolean dividesWell = false;
			int sobran = tamPoblacion % grupos;
			if (sobran == 0) dividesWell = true;
			int indXgrupo = tamPoblacion / grupos;
			int indv = 0;	//Itedrador de individuo.
			int groupLimit = indXgrupo;
			int INDVcount = 0; //Para garantizar que estan todos.
			
			//Bucle recorrido por la población.
			for (int g = 0; g < grupos; g++) {
				for (int i = indv; i < groupLimit; i++) {
					if (i % 2 == 0) {
						//Creción completa
						poblacion[i] = new CromosomaP3(numAs, useif, g+2, "Completa", bloat, valoresMultiplexor);
						INDVcount++;
					} else {
						//Creación creciente
						poblacion[i] = new CromosomaP3(numAs, useif, g+2, "Creciente", bloat, valoresMultiplexor);
						INDVcount++;
					}
					indv = i;
				}
				groupLimit = groupLimit + indXgrupo;
				
				//Control de los individuos que puedan sobrar al final al no dividir bien poblacion entre grupos
				if (g == grupos-1 && !dividesWell) {
					for (int i = 0; i < sobran; i++) {
						if (i % 2 == 0) {
							//Creción completa
							poblacion[i] = new CromosomaP3(numAs, useif, g+2, "Completa", bloat, valoresMultiplexor);
							INDVcount++;
						} else {
							//Creación creciente
							poblacion[i] = new CromosomaP3(numAs, useif, g+2, "Creciente", bloat, valoresMultiplexor);
							INDVcount++;
						}
					}
				}
				
				//Comprobación final
				if (INDVcount == tamPoblacion) {
					System.out.println("[RAMPED AND HALF] Se ha inicializado toda la población.");
				} else {
					System.out.println("[RAMPED AND HALF] ERROR!!!!!! NO SE HA INICIALIZADO TODA LA POBLACION.");
					System.out.printf("INDVCount: %d\n", INDVcount);
					System.out.printf("tamPoblacion: %d\n", tamPoblacion);
				}
			}
		} else {
			// DEBUG
			boolean MostrarEsto = false;;
			if (MostrarEsto) {
				System.out.println("[AGenetico.creaPoblacion()]");
				System.out.println("Objetos enviados:");
				System.out.printf("As: %d\n", numAs);
				System.out.printf("uif: %s\n", useif);
				System.out.printf("pmax: %d\n", pmax);
				System.out.printf("initC: %s\n", initC);
				System.out.printf("bloat: %s\n", bloat);
			}
			// Si no es ramped and half procedemos normalmente.
			for (int i = 0; i < tamPoblacion; i++) {
				System.out.printf("Nuevo Arbol número (empieza en 1): %d\n", i+1);
				poblacion[i] = new CromosomaP3(numAs, useif, pmax, initC, bloat, valoresMultiplexor); //Mirara el tipo de inicialización
			}
		}
		
	}
	
	protected void inicializaPoblacion() 
	{	
		if (debugEjecucion) System.out.println("[AGenetico.inicializaPoblacion()]");
		poblacion = new Cromosoma[tamPoblacion];
		elite = new LinkedList<Cromosoma>();
		tipo = Tipo.MAXIMIZACION; //Buscamos sacar más aciertos
		
		//Práctica 3
		creaPoblacion();
		
		// Valores por defecto inicializados:
		mejor_abs = poblacion[0];
		peor_abs = poblacion[0];
		abs_fitness = mejor_fitness = peor_fitness = abs_worst_fitness = poblacion[0].evalua();
	}

	// FUNCIONES DEL BUCLE
	
	private void seleccion()
	{		
		if (debugEjecucion) System.out.println("[AGenetico.Seleccion()]");
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
		if (debugEjecucion) System.out.println("[AGenetico.Cruce()]");
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
			
			// PRACTICA 3
			// Solo hay un tipo de cruzamiento.
			CruceArbol.cruceArbol(poblacion[padre1], poblacion[padre2]);
			
			numCrucesTotal++;
		}
	}
	
	private void mutacion()
	{			
		if (debugEjecucion) System.out.println("[AGenetico.Mutacion()]");
		boolean haMutado = false;
		for (Cromosoma c : poblacion) {
			switch (tipoMutacion)
			{
			 // PRACTICA 3
			 case "Terminal":
			 	haMutado = Terminal.terminal(c, prob_mutacion);
			 	break;
			 case "Funcion":
			 	haMutado = Funcion.funcion(c, prob_mutacion);
			 	break;
			 case "Permutacion":
			 	haMutado = Permutacion.permutacion(c, prob_mutacion);
			 	break;
			 case "Hoist":
			 	haMutado = Hoist.hoist(c, prob_mutacion);
			 	break;
			 case "Expansion":
			 	haMutado = Expansion.expansion(c, prob_mutacion);
			 	break;
			 case "Contraccion":
			 	haMutado = Contraccion.contraccion(c, prob_mutacion);
			 	break;
			 case "Subarbol":
			 	haMutado = Subarbol.subarbol(c, prob_mutacion);
			 	break;
			}
			
			if (haMutado) numMutacionesTotal++;
		}
	}
	
	private void evaluacion() 
	{
		if (debugEjecucion) System.out.println("[AGenetico.Evaluacion()]");
		fitness_total = 0;
		mejor_fitness = 0; //poblacion[0].getFitness();
		
		for (Cromosoma c : poblacion)
		{			
			//ESPECIFICO P3 (Setup del Bloating)
			CromosomaP3 CAp = (CromosomaP3) c;
			CAp.setMediaPob(tamArbolMedioPobl);
			CAp.setk(k);
			
			
			// Calculo de fitness de cada individuo
			CAp.evalua();
			
			// Calculo del fitness total de la poblacion		
			fitness_total += CAp.getFitness();
					
			evalua_mejor(CAp);
			
			c = CAp;
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
		if (debugEjecucion) System.out.println("[AGenetico.Elitismo()]");
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
		if (debugEjecucion) System.out.println("[AGenetico.sustituyeElite()]");
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
		if (debugEjecucion) System.out.println("[AGenetico.evalua_mejor()]");
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
		if (debugEjecucion) System.out.println("[AGenetico.terminado()]");
		return generacionActual > maxGeneraciones;
	}
		
	protected void adapta_puntuacion() 
	{
		if (debugEjecucion) System.out.println("[AGenetico.adapta_puntuacion()]");
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
		if (debugEjecucion) System.out.println("[AGenetico.maximizacion()]");
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
		if (debugEjecucion) System.out.println("[AGenetico.minimizacion()]");
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
		if (debugEjecucion) System.out.println("[AGenetico.calculaMedia()]");
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
	
	// METODOS ESPECIFICOS DE LA PRACTICA 3
	
	protected double calculaTamMed() {
		if (debugEjecucion) System.out.println("[AGenetico.calculaTamMed()]");
		double media = 0d;
		double sumatorio = 0d;
		
		// Sumatorio
		for (int i = 0; i < tamPoblacion; i++) {
			CromosomaP3 CA = (CromosomaP3) poblacion[i];
			sumatorio += CA.getTamArbol(); //Nodos
		}
		
		// MEDIA
		media = sumatorio / tamPoblacion;
		
		return media;
	}
	
	protected void calculaK() {
		if (debugEjecucion) System.out.println("[AGenetico.calculaK()]");
		Double Cov;
		Double Var;
		
		// COVARIANZA (tamaño individuo, fitness)
		// Cov = (Sum (tam individuo * fitness) / tamPoblacion) - (tamañoMedio * fitnesMedio)
		
		// VARIANZA (tamaño individuos)
		// Var(x) = SUM(tamArbol - mediaTamaños)^2 / poblacion
		// Sumatorio
		double SumVar = 0d;
		double tamRes;
		
		// Sumatorios
		double SumCov = 0d;
		double operando1;
		double fInd;
		double tamArb;
		
		for (int i = 0; i < tamPoblacion; i++) {
			// Obtenemos datos del individuo
			fInd = poblacion[i].getFitness(); //Cov
			CromosomaP3 CA = (CromosomaP3)poblacion[i];
			tamArb = CA.getTamArbol();
			tamRes = tamArb - tamArbolMedioPobl; //Var
			// Calculos
			SumCov += fInd * tamArb; // Cov
			SumVar += Math.pow(tamRes, 2); // Var
		}
		operando1 = SumCov / tamPoblacion;
		
		// Tamaño y fitness Medio
		double operando2;
		double fitness_medio = fitness_total / tamPoblacion;
		operando2 = tamArbolMedioPobl * fitness_medio;
		
		// Calculamos ambas partes Cov
		Cov = operando1 - operando2;
		System.out.printf("[Calculo K] Covarianza: %f", Cov);
			
		// Calculamos Var
		Var = SumVar / tamPoblacion;
		System.out.printf("[Calculo K] Varianza: %f", Var);
		
		//Actualizamos K (Factor corrección)
		k = Cov / Var;
		System.out.printf("[Calculo K] K final: %f", k);
	}
	
	protected String[] inicializaValores(String[] multiplex, int tam) {
		if (debugEjecucion) System.out.println("[AGenetico.inicializaValores()]");
		int num = 0;
		String convertido;
		
		for (int f = 0; f < multiplex.length; f++) {
			convertido = Integer.toBinaryString(num);
			convertido = alargaString(convertido, tam);
			multiplex[f] = convertido;
		}
		
		return multiplex;
	}
	
	protected String alargaString(String numBin, int longitud) {
		String cero = "0";
		while(numBin.length() != longitud) {
			numBin = cero.concat(numBin);
		}
		return numBin;
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
	// Setters Práctica 3
	public void setNumAs(String As) {
		if (As == "2") numAs = 2;
		else if (As == "3") numAs = 3;
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
		return mejor_abs.getFitness();
	}
	public float[] getMejorFeno() {
		return mejor_abs.fenotipos();
	}
	public String getMutacion() {
		return tipoMutacion;
	}

}