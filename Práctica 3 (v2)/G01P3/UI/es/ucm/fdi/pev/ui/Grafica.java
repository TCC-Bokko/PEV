package es.ucm.fdi.pev.ui;

import java.awt.Color;
import javax.swing.JPanel;
import org.math.plot.Plot2DPanel;
import es.ucm.fdi.pev.estructura.Cromosoma;

/** Grafica **/
public class Grafica {
	protected Plot2DPanel _grafica;
	protected JPanel _panel;
	protected double[] x_plot;
	// Tendremos 3 líneas, necesitamos 3 ys // PLOT LINE USA DOUBLES
	protected double[] maxGen_y_plot;
	protected double[] maxAbs_y_plot;
	protected double[] genMed_y_plot;
	protected int maxGeneraciones;
	protected int tamPoblacion;
	
	public void setGen(int maxGen) {
		this.maxGeneraciones = maxGen;
	}
	public void setPob(int poblacion) {
		this.tamPoblacion = poblacion;
	}
	
	public Grafica(JPanel panelGrafica) {
		this._grafica = new Plot2DPanel();
		this._panel = panelGrafica;
		this._panel.add(this._grafica);
	}

	// Esto debe ser llamado en caso de haber generado la gráfica
	// con la constructora sin argumentos
	// Despues de haber hecho un setTamPob y setMaxGen
	// Antes de dibujar.
	public void init() {
		this.x_plot = new double[this.maxGeneraciones]; //Empezamos en generación 1! OJO!
		// Tendremos 3 líneas, necesitamos 3 ys // PLOT LINE USA DOUBLES
		this.maxGen_y_plot = new double[this.maxGeneraciones]; // Máximo de la generación
		this.maxAbs_y_plot = new double[this.maxGeneraciones]; // Maximo absoluto
		this.genMed_y_plot = new double[this.maxGeneraciones]; // media generación
		
		//Los Xs van establecidos por defecto (0,1,2,3,4,..., MAX_GENERACIONES-1), tam = maxGeneraciones
		// OJO QUE EMPEZAMOS POR GENERACION 1, guardar los datos en una posición generación-1.
		for (int i = 0; i < this.maxGeneraciones; i++) {
			this.x_plot[i] = i;
		}
	}
	
	//GETTERS Y SETTERS NORMALES
	public Plot2DPanel getGrafica() {
		return this._grafica;
	}
	
	public void setGrafica(Plot2DPanel grafica) {
		this._grafica = grafica;
	}
			
	//Método de dibujado
	public void dibujaGrafica() 
	{
		//Limpiamos la gráfica
		this._grafica.removeAllPlots();
		//Dibujamos las líneas
		this._grafica.addLinePlot("MaxGen", Color.blue, this.x_plot, this.maxGen_y_plot);
		this._grafica.addLinePlot("MaxAbs", Color.red, this.x_plot, this.maxAbs_y_plot);
		this._grafica.addLinePlot("genMed", Color.green, this.x_plot, this.genMed_y_plot);
	}
	
	//Actualización de datos
	public void actualizaGrafica(Cromosoma[] poblacion, int generacionActual, float mejor_fitness, float abs_fitness, float media) {
		/*
		for(int i = 0; i < this.tamPoblacion; i++)
		{
			System.out.println(poblacion[i].fenotipos()[0]+","+poblacion[i].fenotipos()[1]);
		}*/				
		// Rellena valores grafica
		this.maxGen_y_plot[generacionActual-1] = (double)mejor_fitness; // Generacion -1 por que empezamos en 1! 
		this.maxAbs_y_plot[generacionActual-1] = (double)abs_fitness;
		this.genMed_y_plot[generacionActual-1] = media; //calculaMedia
	}
}