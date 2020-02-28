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
		maxGeneraciones = maxGen;
	}
	public void setPob(int poblacion) {
		tamPoblacion = poblacion;
	}
	
	public Grafica(JPanel panelGrafica) {
		_grafica = new Plot2DPanel();
		_panel = panelGrafica;
		_panel.add(_grafica);
	}
	
	// Esto debe ser llamado en caso de haber generado la gráfica
	// con la constructora sin argumentos
	// Despues de haber hecho un setTamPob y setMaxGen
	// Antes de dibujar.
	public void init() {
		x_plot = new double[maxGeneraciones]; //Empezamos en generación 1! OJO!
		// Tendremos 3 líneas, necesitamos 3 ys // PLOT LINE USA DOUBLES
		maxGen_y_plot = new double[maxGeneraciones]; // Máximo de la generación
		maxAbs_y_plot = new double[maxGeneraciones]; // Maximo absoluto
		genMed_y_plot = new double[maxGeneraciones]; // media generación
		
		//Los Xs van establecidos por defecto (0,1,2,3,4,..., MAX_GENERACIONES-1), tam = maxGeneraciones
		// OJO QUE EMPEZAMOS POR GENERACION 1, guardar los datos en una posición generación-1.
		for (int i = 0; i < maxGeneraciones; i++) {
			x_plot[i] = i;
		}
	}
	
	//GETTERS Y SETTERS NORMALES
	public Plot2DPanel getGrafica() {
		return _grafica;
	}
	
	public void setGrafica(Plot2DPanel grafica) {
		_grafica = grafica;
	}
			
	//Método de dibujado
	public void dibujaGrafica() 
	{
		//Dibujamos las líneas
		_grafica.addLinePlot("MaxGen", Color.blue, x_plot, maxGen_y_plot);
		_grafica.addLinePlot("MaxAbs", Color.red, x_plot, maxAbs_y_plot);
		_grafica.addLinePlot("genMed", Color.green, x_plot, genMed_y_plot);
	}
	
	//Actualización de datos
	public void actualizaGrafica(Cromosoma[] poblacion, int generacionActual, float mejor_fitness, float abs_fitness, float media) {
		for(int i = 0; i < tamPoblacion; i++)
		{
			System.out.println(poblacion[i].fenotipos()[0]+","+poblacion[i].fenotipos()[1]);
		}				
		// Rellena valores grafica
		maxGen_y_plot[generacionActual-1] = (double)mejor_fitness; // Generacion -1 por que empezamos en 1! 
		maxAbs_y_plot[generacionActual-1] = (double)abs_fitness;
		genMed_y_plot[generacionActual-1] = media; //calculaMedia
	}
}