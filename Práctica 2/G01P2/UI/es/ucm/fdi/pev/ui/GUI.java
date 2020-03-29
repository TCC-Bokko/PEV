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
		//String[] gen = new String[] {"Binario", "Real"};
		String[] funciones = new String[] { "func 1", "f2: Hölder Table", "f3: Schubert", "f4: Michalewicz", "f5: f4 con reales", "P2: Edificios"};
		String[] selectores = new String[] {"Ruleta", "Torneo", "MUE", "Ranking", "Truncamiento", "Restos"};
		String[] cruces = new String[] {"Monopunto", "Uniforme", "Aritmetico", "PMX", "OX", "OXpp", "CX", "ERX", "CO"};
		// Practica 2
		String[] numValores = new String[] {"1", "2", "3", "4", "5", "6", "7"};
		String[] mutaciones = new String[] {"Basica", "Inversa"};
		
		////////////////////////////////////
		// AÑADIR ELEMENTOS
		// se pueden añadir las opciones de forma independiente, o "de seguido"; el resultado es el mismo.
		// LOS 3 strings son: 
		//        "Etiqueta: Lo que se ve en la ventana"
		//		  "Tooltip: Pista que se muestra al pasar el raton por encima"
		//		  "Campo: Buscara Getters y Setters con ese nombre p.ej. getFuncion, setFuncion. (O eso entiendo yo que hace)
		// ESTABLECER VALORES
		configAlGen.addOption(new IntegerOption<AlGen>("Poblacion:","Define cantidad de individuos", "tamPob", 0, 500));
		configAlGen.addOption(new IntegerOption<AlGen>("Generaciones:","Define cantidad de generaciones", "maxGen", 10, 2000));
		configAlGen.addOption(new DoubleOption<AlGen>("Prob. Cruce:","Con que % se cruzaran [0.0, 1.0]", "probCruce", 0.0, 1.0));
		configAlGen.addOption(new DoubleOption<AlGen>("Prob. Mutacion:","Con que % mutara [0.0, 1.0]", "probMut", 0.0, 1.0));
		configAlGen.addOption(new DoubleOption<AlGen>("Elitismo:","% poblacion elite [0.0, 1.0]", "elitismo", 0.0, 1.0));
		// CHOICE OPTION
		configAlGen.addOption(new ChoiceOption<AlGen>("Funcion", "fitness del individuo", "funcion", funciones));                         // elecciones posibles
		configAlGen.addOption(new ChoiceOption<AlGen>("Seleccion","Que tipo de seleccion usar","seleccion", selectores));
		configAlGen.addOption(new ChoiceOption<AlGen>("Cruces","Tipo de Cruce","cruce", cruces));
		configAlGen.addOption(new ChoiceOption<AlGen>("Mutacion","Tipo de Mutacion","mutacion", mutaciones));
		configAlGen.addOption(new ChoiceOption<AlGen>("N","Cantidad de valores P1Prob4", "n", numValores));
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
		public String funcion = "P2: Edificios";
		public String cruce = "ERX";
		public String seleccion = "Ruleta";
		public double elitismo = 0.05;
		public double probCruce = 0.4;
		public double probMut = 0.03;
		protected AGenetico aGen;
		//Practica 2
		public String mutacion = "Inversa";
		public String n = "5";
	
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
		//Practica 2
		public void setMutacion(String Mutacion) {
			this.mutacion = Mutacion;
		}
		public String getMutacion() {
			return this.mutacion;
		}
		public void setN(String N) {
			this.n = N;
		}
		public void setN(int N) {
			this.n = Integer.toString(N);
		}
		public String getN() {
			return this.n;
		}
		public int getNInt() {
			return Integer.parseInt(this.n);
		}
		
		//METODOS PROPIOS
		public void preparaEvolucion() {
			// "func 1", "f2: Hölder Table", "f3: Schubert", "f4: Michalewicz"
			//INICIALIZAMOS EL AG
			
			aGen = new AGenetico();
			
			switch (funcion) {
				case "func 1":
					aGen.setNumProblema(1);
					break;
				case "f2: Hölder Table":
					aGen.setNumProblema(2);
					break;
				case "f3: Schubert":
					aGen.setNumProblema(3);
					break;
				case "f4: Michalewicz":
					aGen.setNumProblema(4);
					break;
				case "f5: f4 con reales":
					aGen.setNumProblema(5);
					break;
				//Practica 2
				case "P2: Edificios":
					aGen.setNumProblema(6);
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
			//Practica2
			this.aGen.setN(this.n);
			this.aGen.setMutacion(this.mutacion);
		}

		public void ejecutaEvolucion(Grafica grafica) {
				this.aGen.setGrafica(grafica);
				this.aGen.ejecuta();
				this.mejor_feno = this.aGen.getMejorFeno();
				this.mejor_fit = this.aGen.getMejorFit();
		}
	}
}	
	

