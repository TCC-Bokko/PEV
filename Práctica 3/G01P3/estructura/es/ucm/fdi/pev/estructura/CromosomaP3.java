package es.ucm.fdi.pev.estructura;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.ucm.fdi.pev.evaluacion.FuncionEvalArbol;
import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public class CromosomaP3 extends CromosomaArbol {
	//Variables propias
	//Raiz
	protected GenArbol raizArbol = null;
	
	//Auxiliares
	protected GenArbol HI;	
	protected GenArbol HD;
	protected GenArbol HC;
	protected String inicializacion;
	protected String Bloating;
	protected String[] valoresMultiplexor;
	protected String creacion;
	protected double tamMedioPob;
	protected int profundidadIndividuo;
	
	//int prof_min;
	protected int prof_max;
	protected Random r;
	protected List<GenArbol> nodos; //Almacena nodos (arboles) creados en POSTORDEN IZQ, DER, RAIZ.
	protected double k; // Factor de corrección para control bloating por penalizacion
	
	// Opciones del GUI
	protected int numAs;
	protected int numDs;
	protected boolean useIf;
	
	protected String[] operadores;
	protected String[] operandos; 
	protected boolean debugEjecucion = true;
	
	// METODOS CROMOSOMAS
	public CromosomaP3(CromosomaP3 c) {
		super(c);
		//if (debugEjecucion) System.out.println("[CromosomaP3.constructora(CROMOSOMA)]");
		raizArbol = c.raizArbol;
		HI = c.HI;	
		HD = c.HD;
		HC = c.HC;
		inicializacion = c.inicializacion;
		Bloating = c.Bloating;
		valoresMultiplexor = c.valoresMultiplexor;
		tamMedioPob = c.tamMedioPob;
		profundidadIndividuo = c.profundidadIndividuo;
		prof_max = c.prof_max;
		r = c.r;
		nodos = c.nodos; //Almacena nodos (arboles) creados en POSTORDEN IZQ, DER, RAIZ.
		k = c.k; // Factor de corrección para control bloating por penalizacion
		numAs = c.numAs;
		numDs = c.numDs;
		useIf = c.useIf;
		operadores = c.operadores;
		operandos = c.operandos; 
		debugEjecucion = c.debugEjecucion;
	}
	
	public CromosomaP3(int As, String uif, int pmax, String initC, String bloat, String[] multiplex) {
		//if (debugEjecucion) System.out.println("[CromosomaP3.constructora(Parametros)]");
		//Datos del AGen establecidos en el GUI
		
		//USO DEL OPERADOR IF
		if (uif == "True") useIf = true;
		else if (uif == "False") useIf = false;
		else System.out.println("[CromosomaArbol.Constructora] Valor useif no es string True o False.");
	
		// CANTIDAD DE OPERANDOS
		numAs = As;
		//prof_min = pmin; 
		prof_max = pmax; 				// para inicializacion
		inicializacion = initC;			// para inicializacion
		
		Bloating = bloat;				// para cruce
		valoresMultiplexor = multiplex;	// para evaluacion
		
		// Llamadas a metodos
		inicializaCromosoma();
		
		// DEBUG
		System.out.printf("Bits Direccionado (As): %d\n", numAs);
		System.out.printf("Tipo Inicializacion: %s\n", inicializacion);
		if (inicializacion == "RampedANDHalf") System.out.printf("Generado con: %s\n", creacion);
		System.out.printf("Control Bloating: %s\n", Bloating);
		System.out.printf("Prof. máxima inicializacion: %d\n", prof_max);
		System.out.printf("Uso de if: %s\n", uif);
		System.out.printf("Profundidad alcanzada: %d\n", profundidadIndividuo);
		System.out.printf("Nodos generados: %d\n", nodos.size());
		System.out.printf("Valor del nodo raiz: %s\n", raizArbol.getValor());
		fenotipos();
		System.out.println("////////////////////\n");
	}
	
	@Override
	protected void inicializaCromosoma() {
		//if (debugEjecucion) System.out.println("[CromosomaP3.inicializaCromosoma()]");
		r = new Random();
		nodos = new ArrayList<GenArbol>();	// usado en varios sitios
		
		// OPERANDOS
		if (numAs == 2) {
			//2 Address que direccionan 4 datos
			operandos = new String[] {"A0", "A1", "D0", "D1", "D2", "D3"};
		} else if (numAs == 3) {
			// 3 Address que direccionan 8 datos
			operandos = new String[] {"A0", "A1", "A2", "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7"};
		} else {
			//System.out.println("[CromosomaP3.inicializaCromosoma] Error: numAs no es 2 ni 3.");
		}
		// OPERADORES
		if (useIf) operadores = new String[] {"IF", "AND", "OR", "NOT"};
		else operadores = new String[] {"AND", "OR", "NOT"};
		
		// ARBOLES
		if (this.inicializacion == "Completa") {
			raizArbol = creaArbolCompleto(1, prof_max, null); // RAIZ EMPEZARA EN PROFUNDIDAD 1
		} else if (this.inicializacion == "Creciente") {
			raizArbol = creaArbolCreciente(1, prof_max, null);
		} else {
			//System.out.println("[CromosomaP3.inicializaCromosoma] Error: inicializacion no es Completa ni creciente.");
			raizArbol = creaArbolCompleto(1, prof_max, null);
		}
		
		//Datos
		fitness = 0;
		genes = new GenArbol[1];
		genes[0] = raizArbol;
		
		actualizaArbol();
	}
	
	@Override
	public Cromosoma clone() {
		return new CromosomaP3(this);
	}
	
	// METODOS HEREDADOS
	@Override
	public float[] fenotipos() {
		// El fenotipo del arbol es un texto
		String fenotipo;
		fenotipo = fenotipoArbol();
		System.out.println(fenotipo);
		
		// Ver si como representar los nodos como floats?
		float[] fenoFloat = new float[nodos.size()]; 
		
		return fenoFloat;
	}
	
	@Override
	public float evalua() {
		float result = (float) FuncionEvalArbol.funcionEvalArbol(raizArbol, numAs, valoresMultiplexor);
		
		fitness = result;
		
		if (Bloating != "No usar") fitness = controlBloating();
		
		return fitness;
	}

	@Override
	public boolean compara_mejor_fitness(float f) {
		if (fitness > f) return true;
		else return false;
	}

	@Override
	public boolean compara_peor_fitness(float f) {
		if (fitness < f) return true;
		else return false;
	}

	@Override
	public int compareTo(Cromosoma c) {
		if (fitness > c.fitness) return 0;
		else return 1;
	}
	
	// METODOS ESPECIFICOS
	public String fenotipoArbol() {
		//if (debugEjecucion) System.out.println("[CromosomaP3.fenotipoArbol()]");
		String operacion = "(";
		
		System.out.printf("FENOTIPO: ");
		//Recorrido recursivo por los hijos del arbol
		operacion = ValoresArbol(operacion, raizArbol);
		
		return operacion;
	}
	
	public void estadoCromosoma() {
		System.out.println("//////ESTADO CROMOSOMA//////\n");
		System.out.printf("Bits Direccionado (As): %d\n", numAs);
		System.out.printf("Tipo Inicializacion: %s\n", inicializacion);
		System.out.printf("Control Bloating: %s\n", Bloating);
		System.out.printf("Prof. máxima inicializacion: %d\n", prof_max);
		if (useIf)	System.out.println("Uso de if: True");
		else System.out.println("Uso de if: False");
		//Mostrar datos individuo
		System.out.printf("Profundidad alcanzada: %d\n", profundidadIndividuo);
		// actualiza num nodos
		System.out.printf("Nodos generados: %d\n", nodos.size());
		System.out.printf("Valor del nodo raiz: %s\n", raizArbol.getValor());
		fenotipos();
		System.out.println("////////////////////\n");
	}
	
	protected float controlBloating() {
		//if (debugEjecucion) System.out.println("[CromosomaP3.controlBloating()]");
		double fitnessFinal = fitness;
		
		if (Bloating == "Tarpeian") {
			// Si el tamaño del arbol del individuo es mayor que el tamaño medio de la población
			// Tiene una posibilidad X de devolver un mal fitness (OJO: Maximizamos, más puntuaje mejor)
			int num_nodos = raizArbol.getNumNodos();
			if (num_nodos > tamMedioPob) {
				// 50% posibilidades dar mal fitness
				int prob = r.nextInt(10);
				if (prob < 5) {
					fitnessFinal = 1.0d;
				}
			}
			
		} else if (Bloating == "Penalizacion") {
			// Aplica al fitness un coeficiente de corrección dependiendo de la cantidad de 
			// Al ser un problema de maximización se resta del fitness el resultado.
			fitnessFinal = fitness - k * raizArbol.getNumNodos();			
		}
		
		return (float) fitnessFinal;
	}
		
	// Creación del individuo
	public GenArbol creaArbolCompleto(int nivel, int prof_max, GenArbol padre) {
		//if (debugEjecucion) System.out.println("[CromosomaP3.creaArbolCompleto()]");
		// Completa genera nodos de tipo operador hasta alcanzar profundidad maxima donde genera nodos operando.
		GenArbol arbol = new GenArbol();
		String funcPadre = "";
		
		//Profundidad de este nodo.
		arbol.setProfundidad(nivel);
		//Padre
		if (padre != null) {
			arbol.setPadre(padre);
			funcPadre = padre.getValor();
		} 
		
		//Si no es hoja
		if (nivel < prof_max) {
			//Generar subardol usando operador {IF, NOT, OR, AND}
			int it = r.nextInt(operadores.length);
			String operador = operadores[it];
			
			// Evitar encadenar NOTs
			while (funcPadre == "NOT" && operador == "NOT") {
				it = r.nextInt(operadores.length);
				operador = operadores[it];
			}
			
			arbol.setValor(operador);
			
			//GENERAR HIJOS
			
			//Izquierdo (Usado en todos)
			arbol.setHI(creaArbolCompleto(nivel+1, prof_max, arbol));
			//Añadimos este nodo y le sumamos los nodos de los hijos.
			//Cada nodo (sub-arbol) almacena el número de nodos que contiene (raiz incluida)
			arbol.setNumNodos(arbol.getNumNodos() + arbol.getHi().getNumNodos());
			
			//Central (Usado en IF)
			if (tres_operandos(operador)) {
				arbol.setHC(creaArbolCompleto(nivel+1, prof_max, arbol));
				arbol.setNumNodos(arbol.getNumNodos() + arbol.getHc().getNumNodos());
			} else {
				HC = null;
				arbol.setHC(HC);
			}
			
			//Derecho (Usado en AND, OR, IF)
			if (dos_operandos(operador)) {
				arbol.setHD(creaArbolCompleto(nivel+1, prof_max, arbol));
				arbol.setNumNodos(arbol.getNumNodos() + arbol.getHd().getNumNodos());
			}
		} 
		
		//Si es una HOJA
		if (nivel == prof_max) {
			//Generar subarbol usando datos de tipo operandos
			int it = r.nextInt(operandos.length);
			String operando = operandos[it];
			
			// Evitar obtener el mismo operando en ambos hijos de un operador AND u OR.
			if (funcPadre == "OR" || funcPadre == "AND") {
				// Si este es el hijo derecho
				if (padre.getHi() != null) {
					String valorHI = padre.getHi().getValor();
					while (operando == valorHI) {
						it = r.nextInt(operandos.length);
						operando = operandos[it];
					}
				}
			}
			// Evitar que el 2o y 3er operando de un IF sean iguales.
			if (funcPadre == "IF") {
				if (padre.getHc() != null) {
					String valorHC = padre.getHc().getValor();
					while (operando == valorHC) {
						it = r.nextInt(operandos.length);
						operando = operandos[it];
					}
				}
			}
						
			arbol.setValor(operando);
			arbol.setNumNodos(arbol.getNumNodos()+1);			
		}
		creacion = "Completa";
		nodos.add(arbol); //Una vez el arbol esta construido lo metemos en la lista de nodos.
		return arbol;
	}
	
	// Publico para la mutación subarbol
	public GenArbol creaArbolCreciente(int nivel, int prof_max, GenArbol padre) {
		if (debugEjecucion) System.out.println("[CromosomaP3.creaArbolCreciente()]");
		// Crecientea genera nodos aleatorios de cualquier tipo
		// hasta alcanzar profundidad maxima donde únicamente genera nodos operando.
		GenArbol arbol = new GenArbol();
		String funcPadre = "";
		
		//Profundidad de este nodo.
		arbol.setProfundidad(nivel);
		//Padre
		if (padre != null) {
			arbol.setPadre(padre);
			funcPadre = padre.getValor();
		}
		
		//Si no es hoja
		if (nivel < prof_max) {
			//Generar subardol usando operador u operando {IF, NOT, OR, AND}
			int type = r.nextInt(10); // 70% de ser Operador y 30% Operando
			// OPERADOR
			if (type < 7) {
				int it = r.nextInt(operadores.length);
				String operador = operadores[it];
				
				// Evitar encadenar NOTs
				while (funcPadre == "NOT" && operador == "NOT") {
					it = r.nextInt(operadores.length);
					operador = operadores[it];
				}
				
				
				arbol.setValor(operador);
				
				//GENERAR HIJOS si ha salido un operador
				//Izquierdo (Usado en todos)
				arbol.setHI(creaArbolCompleto(nivel+1, prof_max, arbol));
				//Añadimos este nodo y le sumamos los nodos de los hijos.
				arbol.setNumNodos(arbol.getNumNodos() + arbol.getHi().getNumNodos());
				
				//Central (Usado en IF)
				if (tres_operandos(operador)) {
					arbol.setHC(creaArbolCompleto(nivel+1, prof_max, arbol));
					arbol.setNumNodos(arbol.getNumNodos() + arbol.getHc().getNumNodos());
				} else {
					HC = null;
					arbol.setHC(HC);
				}
				
				//Derecho (Usado en AND, OR, IF)
				if (dos_operandos(operador)) {
					arbol.setHD(creaArbolCompleto(nivel+1, prof_max, arbol));
					arbol.setNumNodos(arbol.getNumNodos() + arbol.getHd().getNumNodos());
				}
			// OPERANDO
			} else {
				// En el caso de que se genere un operando
				int it = r.nextInt(operandos.length);
				String operando = operandos[it];
				// Evitar obtener el mismo operando en ambos hijos de un operador AND u OR.
				if (funcPadre == "OR" || funcPadre == "AND") {
					// Si este es el hijo derecho
					if (padre.getHi() != null) {
						String valorHI = padre.getHi().getValor();
						while (operando == valorHI) {
							it = r.nextInt(operandos.length);
							operando = operandos[it];
						}
					}
				}	
				// Evitar que el 2o y 3er operando de un IF sean iguales.
				if (funcPadre == "IF") {
					if (padre.getHc() != null) {
						String valorHC = padre.getHc().getValor();
						while (operando == valorHC) {
							it = r.nextInt(operandos.length);
							operando = operandos[it];
						}
					}
				}
				
				arbol.setValor(operando);
				arbol.setNumNodos(arbol.getNumNodos()+1);	
			}					
		} 
		
		//Si es una HOJA
		if (nivel == prof_max) {
			//Generar subarbol usando datos de tipo operandos
			int it = r.nextInt(operandos.length);
			String operando = operandos[it];
			// Evitar obtener el mismo operando en ambos hijos de un operador AND u OR.
			if (funcPadre == "OR" || funcPadre == "AND") {
				// Si este es el hijo derecho
				if (padre.getHi() != null) {
					String valorHI = padre.getHi().getValor();
					while (operando == valorHI) {
						it = r.nextInt(operandos.length);
						operando = operandos[it];
					}
				}
			}
			// Evitar que el 2o y 3er operando de un IF sean iguales.
			if (funcPadre == "IF") {
				if (padre.getHc() != null) {
					String valorHC = padre.getHc().getValor();
					while (operando == valorHC) {
						it = r.nextInt(operandos.length);
						operando = operandos[it];
					}
				}
			}
			
			arbol.setValor(operando);
			arbol.setNumNodos(arbol.getNumNodos()+1);			
		}
		
		creacion = "Creciente";
		nodos.add(arbol); //Una vez el arbol esta construido lo metemos en la lista de nodos.
		return arbol;
	}
	
	// Métodos propios del individuo
	protected String ValoresArbol(String cadena, GenArbol arbol) {
		//if (debugEjecucion) System.out.println("[CromosomaAP3.ValoresArbol()]");
		//Añadimos el valor del arbol
		cadena = cadena + arbol.getValor() + " ";
		
		//Hijo Izquierdo
		if (arbol.getHi() != null) {
			//Si encontramos un operador
			if (arbol.getHi().getValor() == "AND" || 
					arbol.getHi().getValor() == "IF" || 
					arbol.getHi().getValor() == "NOT" || 
					arbol.getHi().getValor() == "OR") {
				//Nueva operación, abrimos y hacemos llamada recursiva
				//cadena = cadena + "(" + arbol.getHi().getValor() + " ";
				cadena = cadena + "(";
				cadena = ValoresArbol(cadena, arbol.getHi());
			}
			else {
				cadena = cadena + arbol.getHi().getValor();
				if (arbol.getValor() != "NOT") cadena = cadena + " ";
			}
		}
		
		//Hijo Central
		if (arbol.getHc() != null) {
			//Si encontramos un operador
			if (arbol.getHc().getValor() == "AND" || 
					arbol.getHc().getValor() == "IF" || 
					arbol.getHc().getValor() == "NOT" || 
					arbol.getHc().getValor() == "OR") {
				//Nueva operación, abrimos y hacemos llamada recursiva
				cadena = cadena + "(";
				//cadena = cadena + "(" + arbol.getHc().getValor() + " ";
				cadena = ValoresArbol(cadena, arbol.getHc());
			}
			else {
				cadena = cadena + arbol.getHc().getValor()+" ";
			}
		}
		
		//Hijo Derecho
		if (arbol.getHd() != null) {
			//Si encontramos un operador
			if (arbol.getHd().getValor() == "AND" || 
					arbol.getHd().getValor() == "IF" || 
					arbol.getHd().getValor() == "NOT" || 
					arbol.getHd().getValor() == "OR") {
				//Nueva operación, abrimos y hacemos llamada recursiva
				cadena = cadena + "(";
				//cadena = cadena + "(" + arbol.getHd().getValor() + " ";
				cadena = ValoresArbol(cadena, arbol.getHd());
			}
			else {
				cadena = cadena + arbol.getHd().getValor();
			}
		}
		
		//Cerramos
		cadena = cadena + ")";
		
		//PRINT DEBUG
		//System.out.println(cadena);
		
		return cadena; 
	}
		
	protected boolean tres_operandos(String operador) {
		boolean isValid = false;
		
		if (operador == "IF") isValid = true;
		
		return isValid;
	}
	
	protected boolean dos_operandos(String operador) {
		boolean isValid = false;
		
		if (operador != "NOT") isValid = true;
		
		return isValid;
	}
	
	public void actualizaArbol() {
		//if (debugEjecucion) System.out.println("[CromosomaP3.actualizaArbol()]");
		//Una vez terminado el cruce hace falta actualizar el cromosoma
		// Actualizar lista
		actualizaLista(raizArbol);
		actualizaGenes();
		raizArbol.setNumNodos(nodos.size());
		
		// actualiza profundidad
		profundidadIndividuo = checkProfundidad();
		//profundidadIndividuo = profundidadArbol(raizArbol);
	}
	
	protected void actualizaLista(GenArbol a) {
		//if (debugEjecucion) System.out.println("[CromosomaP3.actualizaLista()]");
		// Si recibe la raiz o 
		if (a == raizArbol) {
			// Vaciamos lista nodos Postorden
			nodos.clear();
		}
		
		// Si es un OPERANDOR recorrer sus hijos
		String valorA = a.getValor();
		if (valorA == "AND" || valorA == "OR" || valorA == "NOT" || valorA == "IF") {
			// recorrido arbol postorden IZQ, CENTRAL, DERECHO, RAIZ
			actualizaLista(a.getHi());
			if (tres_operandos(a.getValor())) actualizaLista(a.getHc());
			if (dos_operandos(a.getValor())) actualizaLista(a.getHd());
		}
		// Si es un OPERANDO simplemente se añade a la lista de nodos.
		
		// Empezamos a recorrer por la raizArbol
		nodos.add(a);
		
	}
	
	public void actualizaGenes() {
		int tam = nodos.size();
		genes = new GenArbol[tam];
		
		for (int i = 0; i < tam; i++) {
			genes[i] = nodos.get(i);
		}
	}
	
	protected int checkProfundidad() {
		//if (debugEjecucion) System.out.println("[CromosomaP3.checkProfundidad()]");
		int maxProf = 0;
		
		// Usando Lista
		int actProf;
		GenArbol actArb;
		for (int i = 0; i < nodos.size(); i++) {
			actArb = nodos.get(i);
			actProf = actArb.getProfundidad();
			if (actProf > maxProf) maxProf = actProf;
		}
		
		return maxProf;
	}
	
	protected int profundidadArbol(GenArbol raiz) {
		if (raiz == null) return 0;
		int mayor = 0;
		int temp = 0;
		if (raiz == null) {
			return 0;
		} else {
			int hijos = raiz.getHijos();
			if (hijos == 0) return 1;
			else {
				for (int i = 0; i < hijos; i++) {
					if (i == 0) temp = profundidadArbol(raiz.getHi());
					if (i == 1) temp = profundidadArbol(raiz.getHd());
					if (i == 2) temp = profundidadArbol(raiz.getHc());
					if (temp > mayor) mayor = temp;
				}
			}
			return mayor+1;
		}
	}
	
	// GETTERS Y SETTERS
	public GenArbol getArbol() {
		return raizArbol;
	}
	
	public void setArbol(GenArbol a) {
		raizArbol = a;
	}
	
	public int getNodosInd() {		
		return nodos.size();
	}
	
	public int getProfInd(){
		//checkProfundidad();
		//actualizaArbol();
		//return profundidadIndividuo;
		return profundidadArbol(raizArbol);
		
	}
	
	public int getNumAs() { //Usado por la mutación
		return numAs;
	}
	
	public void setMediaPob(double tmp) {
		tamMedioPob = tmp;
	}
	
	public void setk(double Kin) {
		k = Kin;
	}
	
	public List<GenArbol> getListaNodos(){
		return nodos;
	}
	
	public void setListaNodos(List<GenArbol> nl) {
		nodos = nl;
	}
	
	public boolean getUseIf() { //Usado por mutacion
		return useIf;
	}

	
}
