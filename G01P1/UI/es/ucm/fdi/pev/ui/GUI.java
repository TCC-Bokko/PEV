package es.ucm.fdi.pev.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.ui.ConfigPanel.ChoiceOption;
import es.ucm.fdi.pev.ui.ConfigPanel.ConfigListener;
import es.ucm.fdi.pev.ui.ConfigPanel.DoubleOption;
import es.ucm.fdi.pev.ui.ConfigPanel.InnerOption;
import es.ucm.fdi.pev.ui.ConfigPanel.IntegerOption;
import es.ucm.fdi.pev.ui.ConfigPanel.StrategyOption;

import org.math.plot.Plot2DPanel;

import java.io.*;

/**
 * Demo para el panel de configuracion
 * 
 * @author mfreire
 */

////
// JFRAME (BorderLayout)
// 		HOLDS:  - JPANEL (GridLayout) with components 
//				- JPANEL PLOT 
// POSICIONES EAST; WEST; CENTER


public class GUI extends JFrame {

	private static final long serialVersionUID = 5393378737313833016L;

	//ENUMS
	public enum TipoGen { BINARIO, REAL }
	public enum TipoSel { RULETA, TORNEO, MUE }
	public enum TipoCru { MONOPUNTO, UNIFORME }
	
	// CONSTRUCTORA
	public GUI() {
		// Llama al constructor de la clase padre (JFRAME)
		// y le pasa el título de la ventana
		super("Demo de panel de configuracion");
		// Opciones de cerrado de ventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Metodo de la clase padre JFRAME
		// Establece el controlador de la ventana
		setLayout(new BorderLayout());
		
		///////////////////////////////////////
		//
		//  GENERACION DE PANELES
		//
		///////////////////////////////////////
		
		//////////////////////////////////////
		//
		//   PANEL DE BOTONES (SOUTH)
		//
		//////////////////////////////////////	
		// DECLARACION
		JPanel panelBotones = new JPanel(new GridLayout(3, 2, 4, 4));
		// ELEMENTOS
		// Boton 1: Declaracion, inicializacion, callback y añadir al panel
		JButton boton;
		boton = new JButton("Ejecuta Evolucion");
		boton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("BOTON: EJECUTA EVOLUCION");
				//METER AQUI EL RUN DEL PROBLEMA
			}
		});
		panelBotones.add(boton);

		// AÑADIR AL LAYOUT
		add(panelBotones, BorderLayout.SOUTH); //JFRAME
		
		////////////////////////////////////////////
		//
		//   PANEL DE LA GRAFICA (EAST)
		//
		////////////////////////////////////////////
		// DECLARACION
		JPanel panelGrafica = new JPanel(new GridLayout());
		// ELEMENTOS
		// Grafica:  Declaracion
		// Ojo las variables final no pueden ser modificadas
		Grafica g1 = new Grafica();
		// Le asociamos su Plot2Panel a su panel
		//_marco.setContentPane(_panel);??? esto como encaja? dentro de la grafica?
		panelGrafica.add(g1.getGrafica());
		add(panelGrafica, BorderLayout.EAST);
		
		///////////////////////////////////////
		//
		// PANEL DE CONFIGURACION (WEST)
		//
		//////////////////////////////////////		
		// crea un panel central y lo asocia con la grafica
		final ConfigPanel<Grafica> cp = creaPanelConfiguracion();
		// asocia el panel con la grafica
		cp.setTarget(g1);
		// carga los valores del panel en la gráfica
		cp.initialize();		
		add(cp, BorderLayout.WEST);
		
	}
	
	//// PANEL DE CONFIGURACION: 
	// AQUI ESTABLECEMOS LOS PARAMETROS DEL ALGORITMO GENETICO
	public ConfigPanel<Grafica> creaPanelConfiguracion() {
		/////////////// NUESTRAS OPCIONES /////////////////
		String[] funciones = new String[] { "func 1", "f2: Hölder Table", "f3: Schubert", "f4: Michalewicz"};
		// Configuracion de opciones posibles
		// Color[] colores = new Color[] { Color.red, Color.blue, Color.green };		
		// Selectores
		//    Tipo de Gen
		//    TipoGen gen;
		//    Seleccion
		//    Cruce
		
		// DECLARACION / INICIALIZACION DEL PANEL
		ConfigPanel<Grafica> config = new ConfigPanel<Grafica>();
		
		////////////////////////////////////
		// AÑADIR ELEMENTOS
		// se pueden añadir las opciones de forma independiente, o "de seguido"; el resultado es el mismo.
		config.addOption(new ChoiceOption<Grafica>(	 // -- eleccion de objeto no-configurable
			    "funciones",						 // etiqueta 
			    "fitness del individuo", 			 // tooltip
			    "color",   							 // campo (debe haber un getColor y un setColor)
			    funciones));                         // elecciones posibles
		//////////////////////////////////////
		config.endOptions();
		
		return config;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// --- clases del GUI
	//////////////////////////////////////////////////////////////////////////////////////
	
	/** Grafica **/
	public static class Grafica {
		protected int maxGeneraciones;
		protected int tamPoblacion;
		protected Plot2DPanel _grafica;
		//protected JFrame _marco;
		protected double[] x_plot = new double[maxGeneraciones]; //Empezamos en generación 1! OJO!
		// Tendremos 3 líneas, necesitamos 3 ys // PLOT LINE USA DOUBLES
		protected double[] maxGen_y_plot = new double[maxGeneraciones]; // Máximo de la generación
		protected double[] genMed_y_plot = new double[maxGeneraciones]; // media generación
		protected double[] maxAbs_y_plot = new double[maxGeneraciones]; // Maximo absoluto
		
		// Constructora sin parametros
		public void grafica() {
			_grafica = new Plot2DPanel();
		}
		
		//Constructora, inicializa las variables
		public void grafica(int maxGen, int tamPob) {
			maxGeneraciones = maxGen;
			tamPoblacion = tamPob;
			// PANEL es el JPANEL (panel grafica)
			// Grafica (plot) es Plot2DPanel()
			_grafica = new Plot2DPanel();
			
			//Los Xs van establecidos por defecto (0,1,2,3,4,..., MAX_GENERACIONES-1), tam = maxGeneraciones
			// OJO QUE EMPEZAMOS POR GENERACION 1, guardar los datos en una posición generación-1.
			for (int i = 0; i < maxGeneraciones; i++) {
				x_plot[i] = i;
			}
		}
		
		// Esto debe ser llamado en caso de haber generado la gráfica
		// con la constructora sin argumentos
		// Despues de haber hecho un setTamPob y setMaxGen
		// Antes de dibujar.
		public void initXs() {
			//Los Xs van establecidos por defecto (0,1,2,3,4,..., MAX_GENERACIONES-1), tam = maxGeneraciones
			// OJO QUE EMPEZAMOS POR GENERACION 1, guardar los datos en una posición generación-1.
			for (int i = 0; i < maxGeneraciones; i++) {
				x_plot[i] = i;
			}
		}
		
		//GETTERS Y SETTERS
		// Si se ha llamado a la gráfica con la constructora vacía
		// será necesario llamar a setTamPob y setMaxGen 
		public void setTamPob(int tamPob) {
			tamPoblacion = tamPob;
		}
		public void setMaxGen(int maxGen) {
			maxGeneraciones = maxGen;
		}
		public Plot2DPanel getGrafica() {
			return _grafica;
		}
		
		//Método de dibujado
		protected void dibujaGrafica() 
		{
			//Dibujamos las líneas
			_grafica.addLinePlot("MaxGen", Color.blue, x_plot, maxGen_y_plot);
			_grafica.addLinePlot("MaxAbs", Color.red, x_plot, maxAbs_y_plot);
			_grafica.addLinePlot("genMed", Color.green, x_plot, genMed_y_plot);
		}
		
		//Actualización de datos
		protected void actualizaGrafica(Cromosoma[] poblacion, int generacionActual, float mejor_fitness, float abs_fitness, float media) {
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
}	
	

