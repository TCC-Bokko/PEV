package es.ucm.fdi.pev.estructura;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.ucm.fdi.pev.evaluacion.FuncionEvalArbol;
import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public class CromosomaP3 extends CromosomaArbol {
	//Variables propias
	//Raiz
	protected GenArbol raizArbol;
	
	//Auxiliares
	protected GenArbol HI;	
	protected GenArbol HD;
	protected GenArbol HC;
	protected String inicializacion;
	protected String Bloating;
	protected String[] valoresMultiplexor;
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
		if (debugEjecucion) System.out.println("[CromosomaP3.constructora()]");
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
	}
	
	@Override
	protected void inicializaCromosoma() {
		if (debugEjecucion) System.out.println("[CromosomaP3.inicializaCromosoma()]");
		r = new Random();
		// INICIALIZACION ARRAYS
		// NODOS
		nodos = new ArrayList<GenArbol>();	// usado en varios sitios
		// OPERANDOS
		if (numAs == 2) {
			//2 Address que direccionan 4 datos
			operandos = new String[] {"A0", "A1", "D0", "D1", "D2", "D3"};
		} else if (numAs == 3) {
			// 3 Address que direccionan 8 datos
			operandos = new String[] {"A0", "A1", "A2", "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7"};
		} else {
			System.out.println("[CromosomaP3.inicializaCromosoma] Error: numAs no es 2 ni 3.");
		}
		// OPERADORES
		if (useIf) operadores = new String[] {"IF", "AND", "OR", "NOT"};
		else operadores = new String[] {"AND", "OR", "NOT"};
		
		// INICIALIZACION ARBOL
		if (this.inicializacion == "Completa") {
			raizArbol = creaArbolCompleto(0, prof_max-1, null);
		} else if (this.inicializacion == "Creciente") {
			raizArbol = creaArbolCreciente(0, prof_max-1, null);
		} else {
			System.out.println("[CromosomaP3.inicializaCromosoma] Error: inicializacion no es Completa ni creciente.");
			raizArbol = creaArbolCompleto(0, prof_max-1, null);
		}
		
		//Datos
		fitness = 0;
		genes = new GenArbol[1];
		genes[0] = raizArbol;
		actualizaArbol();
		actualizaGenes();
	}
	
	@Override
	public Cromosoma clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// METODOS HEREDADOS
	@Override
	public float[] fenotipos() {
		String fenotipo;
		fenotipo = fenotipoArbol();
		System.out.println(fenotipo);
		return null;
	}
	
	@Override
	void inicializaGenes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float evalua() {
		fenotipos();
		
		float result = (float) FuncionEvalArbol.funcionEvalArbol(raizArbol, numAs, valoresMultiplexor);
		
		fitness = result;
		
		return result;
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
		if (debugEjecucion) System.out.println("[CromosomaP3.fenotipoArbol()]");
		String operacion = "(";
		
		//Recorrido recursivo por los hijos del arbol
		ValoresArbol(operacion, raizArbol);
		
		operacion = operacion + ")";
		
		return operacion;
	}
	
	protected double controlBloating() {
		if (debugEjecucion) System.out.println("[CromosomaArbol.controlBloating()]");
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
		
		return fitnessFinal;
	}
	
	public void actualizaArbol() {
		if (debugEjecucion) System.out.println("[CromosomaArbol.actualizaArbol()]");
		//Una vez terminado el cruce hace falta actualizar el cromosoma
		// Actualizar lista
		actualizaLista(null);
		actualizaGenes();
		// actualiza profundidad
		profundidadIndividuo = checkProfundidad();
		System.out.printf("Profundidad despues: %d\n", profundidadIndividuo);
		// actualiza num nodos
		System.out.printf("Nodos despues: %d\n", nodos.size());
	}	
	
	// Creación del individuo
	public GenArbol creaArbolCompleto(int nivel, int prof_max, GenArbol padre) {
		//if (debugEjecucion) System.out.println("[CromosomaArbol.creaArbolCompleto()]");
		// Completa genera nodos de tipo operador hasta alcanzar profundidad maxima donde genera nodos operando.
		GenArbol arbol = new GenArbol();
		
		//Profundidad de este nodo.
		arbol.setProfundidad(nivel);
		//Padre
		if (padre != null) {
			arbol.setPadre(padre);
		} 
		
		//Si no es hoja
		if (nivel < prof_max) {
			//Generar subardol usando operador {IF, NOT, OR, AND}
			int it = r.nextInt(operadores.length);
			String operador = operadores[it];
			arbol.setValor(operador);
			
			//GENERAR HIJOS
			
			//Izquierdo (Usado en todos)
			arbol.setHI(creaArbolCompleto(nivel+1, prof_max, arbol));
			//Añadimos este nodo y le sumamos los nodos de los hijos.
			arbol.setNumNodos(arbol.getNumNodos()+1);	//Cada nodo (sub-arbol) almacena el número de nodos que contiene (raiz incluida)
			arbol.setNumNodos(arbol.getNumNodos() + arbol.getHi().getNumNodos());
			
			//Central (Usado en IF)
			if (tres_operandos(operador)) {
				arbol.setHC(creaArbolCompleto(nivel+1, prof_max, arbol));
				arbol.setNumNodos(arbol.getNumNodos()+1);
				arbol.setNumNodos(arbol.getNumNodos() + arbol.getHc().getNumNodos());
			} else {
				HC = null;
				arbol.setHC(HC);
			}
			
			//Derecho (Usado en AND, OR, IF)
			if (dos_operandos(operador)) {
				arbol.setHD(creaArbolCompleto(nivel+1, prof_max, arbol));
				arbol.setNumNodos(arbol.getNumNodos()+1);
				arbol.setNumNodos(arbol.getNumNodos() + arbol.getHd().getNumNodos());
			}
		} 
		
		//Si es una HOJA
		if (nivel == prof_max) {
			//Generar subarbol usando datos de tipo operandos
			int it = r.nextInt(operandos.length);
			String operando = operandos[it];
			arbol.setValor(operando);
			arbol.setNumNodos(arbol.getNumNodos()+1);			
		}
		
		nodos.add(arbol); //Una vez el arbol esta construido lo metemos en la lista de nodos.
		return arbol;
	}
	
	// Publico para la mutación subarbol
	public GenArbol creaArbolCreciente(int nivel, int prof_max, GenArbol padre) {
		if (debugEjecucion) System.out.println("[CromosomaArbol.creaArbolCreciente()]");
		// Crecientea genera nodos aleatorios de cualquier tipo
		// hasta alcanzar profundidad maxima donde únicamente genera nodos operando.
		GenArbol arbol = new GenArbol();
		//Profundidad de este nodo.
		arbol.setProfundidad(nivel);
		//Padre
		if (padre != null) {
			arbol.setPadre(padre);
		}
		
		//Si no es hoja
		if (nivel < prof_max) {
			//Generar subardol usando operador u operando {IF, NOT, OR, AND}
			int type = r.nextInt(2);
			if (type == 0) {
				int it = r.nextInt(operadores.length);
				String operador = operadores[it];
				arbol.setValor(operador);
				
				//GENERAR HIJOS si ha salido un operador
				//Izquierdo (Usado en todos)
				arbol.setHI(creaArbolCompleto(nivel+1, prof_max, arbol));
				//Añadimos este nodo y le sumamos los nodos de los hijos.
				arbol.setNumNodos(arbol.getNumNodos()+1);
				arbol.setNumNodos(arbol.getNumNodos() + arbol.getHi().getNumNodos());
				
				//Central (Usado en IF)
				if (tres_operandos(operador)) {
					arbol.setHC(creaArbolCompleto(nivel+1, prof_max, arbol));
					arbol.setNumNodos(arbol.getNumNodos()+1);
					arbol.setNumNodos(arbol.getNumNodos() + arbol.getHc().getNumNodos());
				} else {
					HC = null;
					arbol.setHC(HC);
				}
				
				//Derecho (Usado en AND, OR, IF)
				if (dos_operandos(operador)) {
					arbol.setHD(creaArbolCompleto(nivel+1, prof_max, arbol));
					arbol.setNumNodos(arbol.getNumNodos()+1);
					arbol.setNumNodos(arbol.getNumNodos() + arbol.getHd().getNumNodos());
				}
			} else {
				// En el caso de que se genere un operando
				int it = r.nextInt(operandos.length);
				String operando = operandos[it];
				arbol.setValor(operando);
				arbol.setNumNodos(arbol.getNumNodos()+1);	
			}					
		} 
		
		//Si es una HOJA
		if (nivel == prof_max) {
			//Generar subarbol usando datos de tipo operandos
			int it = r.nextInt(operandos.length);
			String operando = operandos[it];
			arbol.setValor(operando);
			arbol.setNumNodos(arbol.getNumNodos()+1);			
		}
		
		nodos.add(arbol); //Una vez el arbol esta construido lo metemos en la lista de nodos.
		return arbol;
	}
	
	// Métodos propios del individuo
	protected String ValoresArbol(String cadena, GenArbol arbol) {
		if (debugEjecucion) System.out.println("[CromosomaArbol.ValoresArbol()]");
		//Añadimos el valor del arbol
		cadena = cadena + arbol.getValor();
		
		//Hijo Izquierdo
		if (arbol.getHi() != null) {
			//Si encontramos un operador
			if (arbol.getHi().getValor() == "AND" || 
					arbol.getHi().getValor() == "IF" || 
					arbol.getHi().getValor() == "NOT" || 
					arbol.getHi().getValor() == "OR") {
				//Nueva operación, abrimos y hacemos llamada recursiva
				cadena = cadena + "(" + arbol.getHi().getValor() + " ";
				cadena = ValoresArbol(cadena, arbol.getHi());
			}
			else {
				cadena = cadena + arbol.getHi().getValor() + " ";
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
				cadena = cadena + "(" + arbol.getHc().getValor() + " ";
				cadena = ValoresArbol(cadena, arbol.getHc());
			}
			else {
				cadena = cadena + arbol.getHc().getValor() + " ";
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
				cadena = cadena + "(" + arbol.getHd().getValor() + " ";
				cadena = ValoresArbol(cadena, arbol.getHd());
			}
			else {
				cadena = cadena + arbol.getHd().getValor() + " ";
			}
		}
		
		//Cerramos
		cadena = cadena + ")";
		
		//PRINT DEBUG
		System.out.println(cadena);
		
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
	
	protected int checkProfundidad() {
		if (debugEjecucion) System.out.println("[CromosomaArbol.checkProfundidad()]");
		int maxProf = 0;
		int actProf;
		GenArbol actArb;
		for (int i = 0; i < nodos.size(); i++) {
			actArb = nodos.get(i);
			actProf = actArb.getProfundidad();
			if (actProf > maxProf) maxProf = actProf;
		}
		
		return maxProf;
	}
	
	protected void actualizaLista(GenArbol a) {
		if (debugEjecucion) System.out.println("[CromosomaArbol.actualizaLista()]");
		if (a == null) {
			// Vaciamos lista nodos Postorden
			nodos.clear();
			// Establecemos raizArbol como inicio del recorrido
			a = raizArbol;
		}
		
		// Si es un OPERANDOR recorrer sus hijos
		String valorA = a.getValor();
		if (valorA == "AND" || valorA == "OR" || valorA == "NOT" || valorA == "IF") {
			// recorrido arbol postorden IZQ, CENTRAL, DERECHO, RAIZ
			actualizaLista(a.getHi());
			if (tres_operandos(a.getValor())) actualizaLista(a.getHc());
			if (dos_operandos(a.getValor())) actualizaLista(a.getHi());
		}
		// Si es un OPERANDO simplemente se añade a la lista de nodos.
		
		// Empezamos a recorrer por la raizArbol
		nodos.add(a);
	}
	
	protected void actualizaGenes() {
		int tam = nodos.size();
		genes = new GenArbol[tam];
		
		for (int i = 0; i < tam; i++) {
			genes[i] = nodos.get(i);
		}
	}
	
	// GETTERS Y SETTERS
	public GenArbol getArbol() {
		return raizArbol;
	}
	
	public void setArbol(GenArbol a) {
		raizArbol = a;
	}
	
	public int getTamArbol() {
		//Comprobación num nodos
		System.out.printf("Nodos en lista: %d", nodos.size());
		System.out.printf("Nodos segun raiz: %d", raizArbol.getNumNodos());
		
		return raizArbol.getNumNodos();
	}
	
	public int getProfInd(){
		checkProfundidad();
		return profundidadIndividuo;
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
