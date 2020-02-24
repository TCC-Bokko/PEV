package es.ucm.fdi.pev;

import es.ucm.fdi.pev.estructura.*;
import es.ucm.fdi.pev.evaluacion.*;
import javafx.util.Pair;
import es.ucm.fdi.pev.Utils.*;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.*;

import org.math.plot.Plot2DPanel;


public class AGeneticoEj1 extends AGenetico {

	// Numero de genes que compone cada cromosoma, y su tama�o (indice: n� de gen, valor: tam del gen)
	protected ArrayList<Pair<Float, Float>> genes_len; 
	
	// Ploteo
	protected Plot2DPanel _panel;
	protected JFrame _marco;
	protected double[] x_plot = new double[maxGeneraciones]; //Empezamos en generaci�n 1! OJO!
	
	// Tendremos 3 l�neas, necesitamos 3 ys // PLOT LINE USA DOUBLES
	protected double[] genMax_y_plot = new double[maxGeneraciones]; // M�ximo de la generaci�n
	protected double[] genMed_y_plot = new double[maxGeneraciones]; // media generaci�n
	protected double[] maxAbs_y_plot = new double[maxGeneraciones]; // Maximo absoluto
	

	public AGeneticoEj1(int tamPob, int maxGen) {
		super(tamPob, maxGen);
		iniciaGrafica();
	}
	
	private void iniciaGrafica() {
		_panel = new Plot2DPanel();
		_marco = new JFrame("Funcion1");
		
		//Los Xs van establecidos por defecto (0,1,2,3,4,..., MAX_GENERACIONES-1), tam = maxGeneraciones
		// OJO QUE EMPEZAMOS POR GENERACION 1, guardar los datos en una posici�n generaci�n-1.
		for (int i = 0; i < maxGeneraciones; i++) {
			x_plot[i] = i;
		}
		

	}
	
	@Override
	protected void dibujaGrafica() {
		//Dibujamos las l�neas
		_panel.addLinePlot("MaxGen", Color.blue, x_plot, genMax_y_plot);
		_panel.addLinePlot("MaxAbs", Color.red, x_plot, maxAbs_y_plot);
		_panel.addLinePlot("genMed", Color.green, x_plot, genMed_y_plot);
		
		//Propiedades marco
		_marco.setSize(600,600);
		_marco.setContentPane(_panel);
		_marco.setVisible(true);
	}
	
	@Override
	protected double calculaMedia() {
		//Recorre los valores de fitness de la generaci�n y saca una media
		float sum = 0.0f;
		double media = 0.0f;
		
		//Sumamos los fitnes
		for (int i = 0; i < tamPoblacion; i++) {
			sum = sum + poblacion[i].getFitness();
		}
		
		media = (double)sum / (double)tamPoblacion;		
		
		return media;		
	}
	
	/*
	@Override
	protected void evaluaCromosoma(Cromosoma c) 
	{
		float[] fenotipos = c.fenotipos();		
		float result = (float) FuncionesEv.funcion1(fenotipos[0], fenotipos[1]);
		c.setRelFit(result);
		
		System.out.println(result);
	}*/
	
	@Override
	protected void actualizaGrafica() {
		//Calcular media
		double media = calculaMedia();
		
		// Rellena valores grafica
		genMax_y_plot[generacionActual-1] = (double)mejor_indiv.getFitness(); // Generacion -1 por que empezamos en 1! 
		genMed_y_plot[generacionActual-1] = media;
		genMed_y_plot[generacionActual-1] = (double)mejor_abs.getFitness();
	}
	
	@Override
	protected void inicializaPoblacion() 
	{
		tolerancia = 0.1f;
		inicializaGenes();
		
		poblacion = new CromosomaReal[tamPoblacion];
		
		for(int i = 0; i < tamPoblacion; i++)
		{
			poblacion[i] = inicializaCromosoma();
		}	
	}

	
	// Inicializa la division en genes de los cromosomas, asi como el rango de valores de cada uno
	protected void inicializaGenes() 
	{
		float Xmin, Xmax;
		ArrayList<Pair<Float, Float>> genes_l = new ArrayList<Pair<Float, Float>>();
		
		 Xmin = -3f; Xmax = 12.1f;
		genes_l.add(new Pair<Float, Float>(Xmin, Xmax));
		
		 Xmin = 4.1f; Xmax = 5.8f;
		genes_l.add(new Pair<Float, Float>(Xmin, Xmax));
		
		genes_len = genes_l;
	}


	@Override
	protected Cromosoma inicializaCromosoma() 
	{
		ArrayList<GenBinario> genes = new ArrayList<GenBinario>();
		
		for(Pair<Float, Float> genRange : genes_len)
		{
			float min = genRange.getKey();
			float max = genRange.getValue();
			
			int len = Utils.longitud_bits(min, max, tolerancia);
			
			GenBinario g = new GenBinario(len, min, max);
			g.randomInit();
			genes.add(g);
		}
		
		return new CromosomaReal(genes);
	}

	@Override
	protected void evalua_mejor(Cromosoma c) 
	{
		// PARA MAXIMIZACION SER�A AS�. MINIMIZACION SER�A '<'
		if(c.getFitness() > mejor_fitness)
		{
			mejor_indiv = c;
			mejor_fitness = mejor_indiv.getFitness();
		}
		
	}
	
}
