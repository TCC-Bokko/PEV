package es.ucm.fdi.pev.ui;

//Librerías gráficas
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.ucm.fdi.pev.ui.ConfigPanel.ChoiceOption;
import es.ucm.fdi.pev.ui.ConfigPanel.ConfigListener;
import es.ucm.fdi.pev.ui.ConfigPanel.DoubleOption;
import es.ucm.fdi.pev.ui.ConfigPanel.InnerOption;
import es.ucm.fdi.pev.ui.ConfigPanel.IntegerOption;
import es.ucm.fdi.pev.ui.ConfigPanel.StrategyOption;
import es.ucm.fdi.pev.ui.Demo.Figura;

import org.math.plot.Plot2DPanel;
//Archivos propios del Algoritmo Genético
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.*;
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

	private AlGen algorGenetico;
	
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
		// Inicializamos objetos que usaremos
		algorGenetico = new AlGen();
		
		///////////////////////////////////////
		//
		//  GENERACION DE PANELES
		//
		///////////////////////////////////////
				
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
		Grafica grafica = new Grafica(panelGrafica);
		add(panelGrafica, BorderLayout.CENTER);
		
		//////////////////////////////////////
		//
		// PANEL DE CONFIGURACION (WEST)
		//
		//////////////////////////////////////
		ConfigPanel<AlGen> controlAG = creaConfAlGen();
		controlAG.setTarget(algorGenetico);
		//controlAG.initialize();
		add(controlAG, BorderLayout.WEST);

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
				//Prepara algoritmo genetico
				algorGenetico.preparaEvolucion();
				//Prepara grafica
				grafica.setGen(algorGenetico.getMaxGen());
				grafica.setPob(algorGenetico.getTamPob());
				grafica.init();
				//ejecuta algoritmo
				algorGenetico.ejecutaEvolucion(grafica);
			}
		});
		panelBotones.add(boton);

		// AÑADIR AL LAYOUT
		add(panelBotones, BorderLayout.SOUTH); //JFRAME

		
	}
	
	//// PANEL DE CONFIGURACION: 
	public ConfigPanel<AlGen> creaConfAlGen(){
		// DECLARACION E INICIALIZACION
		ConfigPanel<AlGen> configAlGen = new ConfigPanel<AlGen>();
		
		///////////// NUESTRAS OPCIONES /////////////////
		String[] gen = new String[] {"Binario", "Real"};
		String[] funciones = new String[] { "func 1", "f2: Hölder Table", "f3: Schubert", "f4: Michalewicz"};
		String[] selectores = new String[] {"Ruleta", "Torneo", "MUE"};
		String[] cruces = new String[] {"Monopunto", "Uniforme"};
		
		////////////////////////////////////
		// AÑADIR ELEMENTOS
		// se pueden añadir las opciones de forma independiente, o "de seguido"; el resultado es el mismo.
		// LOS 3 strings son: 
		//        "Etiqueta: Lo que se ve en la ventana"
		//		  "Tooltip: Pista que se muestra al pasar el raton por encima"
		//		  "Campo: Buscara Getters y Setters con ese nombre p.ej. getFuncion, setFuncion. (O eso entiendo yo que hace)
		// ESTABLECER VALORES
		configAlGen.addOption(new IntegerOption<AlGen>("Poblacion:","Define cantidad de individuos", "TamPob", 0, 100));
		configAlGen.addOption(new IntegerOption<AlGen>("Generaciones:","Define cantidad de generaciones", "MaxGen", 10, 100));
		configAlGen.addOption(new DoubleOption<AlGen>("Prob. Cruce:","Con que % se cruzaran", "ProbCruce", 0.0, 100.0));
		configAlGen.addOption(new DoubleOption<AlGen>("Prob. Mutacion:","Con que % mutara", "ProbMut", 0.0, 100.0));
		configAlGen.addOption(new DoubleOption<AlGen>("Elitismo:","% poblacion elite", "Elitismo", 0.0, 100.0));
		// CHOICE OPTION
		configAlGen.addOption(new ChoiceOption<AlGen>("Gen","Tipo de gen","Gen", gen));
		configAlGen.addOption(new ChoiceOption<AlGen>("Funcion", "fitness del individuo", "Funcion", funciones));                         // elecciones posibles
		configAlGen.addOption(new ChoiceOption<AlGen>("Seleccion","Que tipo de seleccion usar","Seleccion", selectores));
		configAlGen.addOption(new ChoiceOption<AlGen>("Cruces","Tipo de Cruce","Cruce", cruces));
		// BOOLEAN (Elitismo, Mutación)
		// WORK TO DO.
		//////////////////////////////////////
		configAlGen.endOptions();

		return configAlGen;
	}
	
	//////////////////////
	//  ALGORITMO GENÉTICO
	//////////////////////
	// Métodos de los AG
	//		- 
	//		- ejecuta(): Corre el algoritmo genético, llamar con el boton del gui
	//
	// Pulsar el botón de ejecución:
	// 	Inicializa lso valores en el AGen y la gráfica
	
	public static class AlGen{
		// TOCARA QUITAR DE LAS CONSTRUCTORAS el tamPob y maxGen
		// Para pasarselo mediante métodos.
		
		/// VARIABLES PARA CONFIGURAR EL ALGEN
		protected int maxGeneraciones;
		protected int tamPoblacion;
		protected String gen;
		protected String funcion;
		protected String cruce;
		protected String seleccion;
		protected double elitismo;
		protected double probCruce;
		protected double probMut;
		protected AGenetico aGen;
		protected AGeneticoEj1 aGenE1;
		protected AGeneticoEj2 aGenE2;
		protected AGeneticoEj3 aGenE3;
		protected AGeneticoEj4 aGenE4;
		
		// Constructora
		public AlGen() {
			
		}
		
		//GETTERS Y SETTERS REQUERIDOS POR EL CONTROL PANEL
		// Si se ha llamado a la gráfica con la constructora vacía
		// será necesario llamar a setTamPob y setMaxGen 
		public void setTamPob(int tamPob) {
			tamPoblacion = tamPob;
		}
		public int getTamPob() {
			return tamPoblacion;
		}
		public void setMaxGen(int maxGen) {
			maxGeneraciones = maxGen;
		}
		public int getMaxGen() {
			return maxGeneraciones;
		}
		public void setSeleccion(String Seleccion) {
			seleccion = Seleccion;
		}
		public void setGen(String Gen) {
			gen = Gen;
		}
		public String getGen() {
			return gen;
		}
		public void setFuncion(String Funcion) {
			funcion = Funcion;
		}
		public String getFuncion() {
			return funcion;
		}
		public String getSeleccion() {
			return seleccion;
		}
		public void setCruce(String Cruce) {
			cruce = Cruce;
		}
		public String getCruce() {
			return cruce;
		}
		public void setElitismo(double Elitismo) {
			elitismo = Elitismo;
		}
		public double getElitismo() {
			return elitismo;
		}
		public double getProbCruce() {
			return probCruce;
		}
		public void setProbCruce(double ProbCruce) {
			probCruce = ProbCruce;
		}
		public double getProbMut() {
			return probCruce;
		}
		public void setProbMut(double ProbMut) {
			probMut = ProbMut;
		}
		
		//METODOS PROPIOS
		public void preparaEvolucion() {
			// "func 1", "f2: Hölder Table", "f3: Schubert", "f4: Michalewicz"
			//INICIALIZAMOS EL AG
			switch (funcion) {
				case "func 1":
					aGen = new AGeneticoEj1();
					break;
				case "f2: Hölder Table":
					aGen = new AGeneticoEj2();
					break;
				case "f3: Schubert":
					aGen = new AGeneticoEj3();
					break;
				case "f4: Michalewicz":
					aGen = new AGeneticoEj4();
					break;
			}
			// LE PASAMOS LOS VALORES
			// TamPob, MaxGen, ProbCruce, prob mut, elitismo, tipo seleccion, tipo cruce
			aGen.setTamPob(tamPoblacion);
			aGen.setMaxGen(maxGeneraciones);
			aGen.setProbCruce(probCruce);
			aGen.setProbMut(probMut);
			aGen.setElitismo(elitismo);
		}
		
		public void actualizaGrafica() {
			
		}
		
		public void ejecutaEvolucion(Grafica grafica) {
			aGen.setGrafica(grafica);
			aGen.ejecuta();
		}
	}
}	
	

