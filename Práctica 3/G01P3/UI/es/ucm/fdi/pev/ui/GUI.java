package es.ucm.fdi.pev.ui;

//Librerías gráficas
import java.awt.BorderLayout;
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

import es.ucm.fdi.pev.*;
import es.ucm.fdi.pev.estructura.Cromosoma;
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
	// Algoritmo
	private AlGen algorGenetico;
	private Grafica grafica;
	// Variables del mejor individuo absoluto
	//private float[] X;
	//private float Y;
	private String textoMejorAbs = "Info del mejor individuo.";
	
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
				
		//////////////////////////////////////
		//
		//  "PANEL" Etiqueta de texto (NORTH)
		//
		//////////////////////////////////////
		JLabel textoBestOne = new JLabel(textoMejorAbs);
		add(textoBestOne, BorderLayout.NORTH);
				
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
		grafica = new Grafica(panelGrafica);
		add(panelGrafica, BorderLayout.CENTER);
		
		//////////////////////////////////////
		//
		// PANEL DE CONFIGURACION (WEST)
		//
		//////////////////////////////////////
		ConfigPanel<AlGen> controlAG = creaConfAlGen();
		controlAG.setTarget(algorGenetico);
		controlAG.initialize();
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
				grafica.setGen(algorGenetico.getmaxGen());
				grafica.setPob(algorGenetico.gettamPob());
				grafica.init();
				//ejecuta algoritmo
				algorGenetico.ejecutaEvolucion(grafica);
				actualizaLabel(algorGenetico.getMejorFeno(), algorGenetico.getMejorFit(), textoBestOne);
			}
		});
		panelBotones.add(boton);
		// AÑADIR AL LAYOUT
		add(panelBotones, BorderLayout.SOUTH); //JFRAME

	}

	public void actualizaLabel(float[] xs, float y, JLabel label) {
		// Obtener mejor cromosoma absoluto del algoritmo genético
		textoMejorAbs = "Mejor Invididuo. Genes: [";
		float X;
		String value;
		
		for (int i = 0; i < xs.length; i++) {
			X = xs[i];
			value = String.valueOf(X);
			textoMejorAbs = textoMejorAbs + value;
			textoMejorAbs = textoMejorAbs + ", ";
		}
		
		// Fitness del mejor cromosoma (Y)
		textoMejorAbs = textoMejorAbs + "].  FITNESS: ";
		value = String.valueOf(y);
		textoMejorAbs = textoMejorAbs + value;
		
		label.setText(textoMejorAbs);
	}
	
	//// PANEL DE CONFIGURACION: 
	public ConfigPanel<AlGen> creaConfAlGen(){
		// DECLARACION E INICIALIZACION
		ConfigPanel<AlGen> configAlGen = new ConfigPanel<AlGen>();
		
		///////////// NUESTRAS OPCIONES /////////////////
		String[] funciones = new String[] {"Max. Aciertos"};
		String[] selectores = new String[] {"Ruleta", "Torneo", "MUE", "Ranking", "Truncamiento", "Restos"};
		String[] cruces = new String[] {"Permutacion"};
		String[] entradas = new String[] {"2", "3"};
		String[] mutaciones = new String[] {"Funcion", "Terminal", "Permutacion", "Subarbol"}; //, "Hoist", "Expansion", "Contraccion", };
		String[] bloating = new String[] {"Tarpeian", "Penalizacion"};
		String[] generacion = new String[] {"Completa", "Creciente", "RampedANDHalf"};
		String[] usarif = new String[] { "True", "False"};
		
		////////////////////////////////////
		// AÑADIR ELEMENTOS
		// se pueden añadir las opciones de forma independiente, o "de seguido"; el resultado es el mismo.
		// LOS 3 strings son: 
		//        "Etiqueta: Lo que se ve en la ventana"
		//		  "Tooltip: Pista que se muestra al pasar el raton por encima"
		//		  "Campo: Buscara Getters y Setters con ese nombre p.ej. getFuncion, setFuncion. (O eso entiendo yo que hace)
		// ESTABLECER VALORES
		configAlGen.addOption(new IntegerOption<AlGen>("Poblacion:","Define cantidad de individuos", "tamPob", 0, 500));
		configAlGen.addOption(new IntegerOption<AlGen>("Generaciones:", "Define cantidad de generaciones", "maxGen", 10, 2000));
		//configAlGen.addOption(new IntegerOption<AlGen>("Prof. Minima:", "Define profundidad minima", "minProf", 0, 3));
		configAlGen.addOption(new IntegerOption<AlGen>("Prof. Maxima:","Define profundidad máxima", "maxProf", 1, 20));
		configAlGen.addOption(new DoubleOption<AlGen>("Prob. Cruce:","Con que % se cruzaran [0.0, 1.0]", "probCruce", 0.0, 1.0));
		configAlGen.addOption(new DoubleOption<AlGen>("Prob. Mutacion:","Con que % mutara [0.0, 1.0]", "probMut", 0.0, 1.0));
		configAlGen.addOption(new DoubleOption<AlGen>("Elitismo:","% poblacion elite [0.0, 1.0]", "elitismo", 0.0, 1.0));
		// CHOICE OPTION
		configAlGen.addOption(new ChoiceOption<AlGen>("Entradas(Ax)","Define cantidad de entradas direccionadoras", "numAs", entradas));
		configAlGen.addOption(new ChoiceOption<AlGen>("Usar IF","Permite o no el uso de funciones IF", "useIf", usarif));
		// No ponemos un selector de lineas de datos (Dx) ya que son dependientes del direccionamiento de las Ax.
		configAlGen.addOption(new ChoiceOption<AlGen>("Generacion", "Establece la inicializacion de la poblacion", "generador", generacion));
		configAlGen.addOption(new ChoiceOption<AlGen>("C. Bloating", "Control de crecimiento arboles", "bloating", bloating));
		configAlGen.addOption(new ChoiceOption<AlGen>("Evalucion", "fitness del individuo", "funcion", funciones));
		configAlGen.addOption(new ChoiceOption<AlGen>("Seleccion","Que tipo de seleccion usar","seleccion", selectores));
		configAlGen.addOption(new ChoiceOption<AlGen>("Cruces","Tipo de Cruce","cruce", cruces));
		configAlGen.addOption(new ChoiceOption<AlGen>("Mutacion","Tipo de Mutacion","mutacion", mutaciones));

		// CERRAR LAS OPCIONES
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
		public int maxGen = 100;
		public int tamPob = 100;
		public String funcion = "Max. Aciertos";
		public String cruce = "Permutacion";
		public String seleccion = "Ruleta";
		public String mutacion = "Funcion";
		public String bloating = "Tarpeian";
		public double elitismo = 0.05;
		public double probCruce = 0.4;
		public double probMut = 0.03;
		protected AGenetico aGen;
		// P3
		public String numAs = "2";
		//public int minProf = 0;
		public int maxProf = 3;
		public String useIf = "True";
		public String generador = "Completa";
		
		
		//Mejores abs
		protected float mejor_fit = 1.0f;
		protected float[] mejor_feno;
		
		
		// Constructora vacía
		public AlGen() {
			
		}
		
		//GETTERS Y SETTERS REQUERIDOS POR EL CONTROL PANEL
		// Si se ha llamado a la gráfica con la constructora vacía
		// será necesario llamar a setTamPob y setMaxGen 
		public void settamPob(int TamPob) {
			this.tamPob = TamPob;
		}
		public int gettamPob() {
			return this.tamPob;
		}
		public void setmaxGen(int MaxGen) {
			this.maxGen = MaxGen;
		}
		public int getmaxGen() {
			return this.maxGen;
		}
		public void setSeleccion(String Seleccion) {
			this.seleccion = Seleccion;
		}
		public void setFuncion(String Funcion) {
			this.funcion = Funcion;
		}
		public String getFuncion() {
			return this.funcion;
		}
		public String getSeleccion() {
			return this.seleccion;
		}
		public void setCruce(String Cruce) {
			this.cruce = Cruce;
		}
		public String getCruce() {
			return this.cruce;
		}
		public void setElitismo(double Elitismo) {
			this.elitismo = Elitismo;
		}
		public double getElitismo() {
			return this.elitismo;
		}
		public double getProbCruce() {
			return this.probCruce;
		}
		public void setProbCruce(double ProbCruce) {
			this.probCruce = ProbCruce;
		}
		public double getProbMut() {
			return this.probMut;
		}
		public void setProbMut(double ProbMut) {
			this.probMut = ProbMut;
		}
		public float getMejorFit() {
			return mejor_fit;
		}
		public float[] getMejorFeno() {
			return mejor_feno;
		}
		public void setMutacion(String Mutacion) {
			this.mutacion = Mutacion;
		}
		public String getMutacion() {
			return this.mutacion;
		}
		// PRáctica 3
		// Address
		public void setnumAs(String As) {
			this.numAs = As;
		}
		public String getnumAs() {
			return this.numAs;
		}
		// Profundidades
		public void setmaxProf(int Mp) {
			this.maxProf = Mp;
		}
		public int getmaxProf() {
			return this.maxProf;
		}
		/*
		public void setminProf(int mp) {
			this.minProf = mp;
		}
		public int getminProf() {
			return this.minProf;
		}
		*/
		// Tipo creacion
		public void setgenerador(String g) {
			this.generador = g;
		}
		public String getgenerador() {
			return this.generador;
		}
		// IF
		public void setuseIf(String uif) {
			this.useIf = uif;
		}
		public String getuseIf() {
			return this.useIf;
		}
		//Bloating
		public void setbloating(String b) {
			this.bloating = b;
		}
		public String getbloating() {
			return this.bloating;
		}
		

		//METODOS PROPIOS
		public void preparaEvolucion() {
	
			aGen = new AGenetico();
			
			switch (funcion) {
				//Práctica 3
				case "P3: P.Genetica":
					aGen.setNumProblema(10);
					break;
			}
			
			// LE PASAMOS LOS VALORES
			// TamPob, MaxGen, ProbCruce, prob mut, elitismo, tipo seleccion, tipo cruce
			this.aGen.setTamPob(this.tamPob);
			this.aGen.setMaxGen(this.maxGen);
			this.aGen.setProbCruce(this.probCruce);
			this.aGen.setProbMut(this.probMut);
			this.aGen.setElitismo(this.elitismo);
			this.aGen.setTipSel(this.seleccion);
			this.aGen.setTipCru(this.cruce);
			this.aGen.setMutacion(this.mutacion);
			//Practica3
			this.aGen.setNumAs(this.numAs);
			this.aGen.setPmax(this.maxProf);
			//this.aGen.setPmin(this.minProf);
			this.aGen.setUseIf(this.useIf);
			this.aGen.setInitType(this.generador);
			this.aGen.setBloat(this.bloating);
		}

		public void ejecutaEvolucion(Grafica grafica) {
				this.aGen.setGrafica(grafica);
				this.aGen.ejecuta();
				this.mejor_feno = this.aGen.getMejorFeno();
				this.mejor_fit = this.aGen.getMejorFit();
		}
	}
}	
	

