package es.ucm.fdi.pev.estructura;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.ucm.fdi.pev.evaluacion.FuncionEvalArbol;

public class CromosomaArbol extends Cromosoma {
	//Raiz
	protected Arbol raizArbol;
	
	//Auxiliares
	protected Arbol HI;	
	protected Arbol HD;
	protected Arbol HC;
	protected String inicializacion;
	protected String Bloating;
	protected String[] valoresMultiplexor;
	protected double tamMedioPob;
	protected int profundidadIndividuo;
	
	//int prof_min;
	protected int prof_max;
	protected Random r;
	protected List<Arbol> nodos; //Almacena nodos (arboles) creados en POSTORDEN IZQ, DER, RAIZ.
	protected double k; // Factor de correcci�n para control bloating por penalizacion
	
	// Opciones del GUI
	protected int numAs;
	protected int numDs;
	protected boolean useIf;
	
	protected String[] operadores;
	protected String[] operandos; 
	protected boolean debugEjecucion = true;
	
	public CromosomaArbol(Cromosoma c) {
	}
	
	public CromosomaArbol() {
		super();
	}
	
	
	@Override
	protected void inicializaCromosoma() {
		genes = new Arbol[1];
		raizArbol = new Arbol();
		genes[0] = raizArbol;
	}
	
	// Creaci�n del individuo
	private Arbol creaArbolCompleto(int nivel, int prof_max, Arbol padre) {
		//if (debugEjecucion) System.out.println("[CromosomaArbol.creaArbolCompleto()]");
		// Completa genera nodos de tipo operador hasta alcanzar profundidad maxima donde genera nodos operando.
		Arbol arbol = new Arbol();
		
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
			//A�adimos este nodo y le sumamos los nodos de los hijos.
			arbol.setNumNodos(arbol.getNumNodos()+1);	//Cada nodo (sub-arbol) almacena el n�mero de nodos que contiene (raiz incluida)
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
	
	// Publico para la mutaci�n subarbol
	public Arbol creaArbolCreciente(int nivel, int prof_max, Arbol padre) {
		if (debugEjecucion) System.out.println("[CromosomaArbol.creaArbolCreciente()]");
		// Crecientea genera nodos aleatorios de cualquier tipo
		// hasta alcanzar profundidad maxima donde �nicamente genera nodos operando.
		Arbol arbol = new Arbol();
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
				//A�adimos este nodo y le sumamos los nodos de los hijos.
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
	
	// M�todos propios del individuo
	private String ValoresArbol(String cadena, Arbol arbol) {
		if (debugEjecucion) System.out.println("[CromosomaArbol.ValoresArbol()]");
		//A�adimos el valor del arbol
		cadena = cadena + arbol.getValor();
		
		//Hijo Izquierdo
		if (arbol.getHi() != null) {
			//Si encontramos un operador
			if (arbol.getHi().getValor() == "AND" || 
					arbol.getHi().getValor() == "IF" || 
					arbol.getHi().getValor() == "NOT" || 
					arbol.getHi().getValor() == "OR") {
				//Nueva operaci�n, abrimos y hacemos llamada recursiva
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
				//Nueva operaci�n, abrimos y hacemos llamada recursiva
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
				//Nueva operaci�n, abrimos y hacemos llamada recursiva
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
		
	private boolean tres_operandos(String operador) {
		boolean isValid = false;
		
		if (operador == "IF") isValid = true;
		
		return isValid;
	}
	
	private boolean dos_operandos(String operador) {
		boolean isValid = false;
		
		if (operador != "NOT") isValid = true;
		
		return isValid;
	}
	
	public String fenotipoArbol() {
		if (debugEjecucion) System.out.println("[CromosomaArbol.fenotipoArbol()]");
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
			// Si el tama�o del arbol del individuo es mayor que el tama�o medio de la poblaci�n
			// Tiene una posibilidad X de devolver un mal fitness (OJO: Maximizamos, m�s puntuaje mejor)
			int num_nodos = raizArbol.getNumNodos();
			if (num_nodos > tamMedioPob) {
				// 50% posibilidades dar mal fitness
				int prob = r.nextInt(10);
				if (prob < 5) {
					fitnessFinal = 1.0d;
				}
			}
			
		} else if (Bloating == "Penalizacion") {
			// Aplica al fitness un coeficiente de correcci�n dependiendo de la cantidad de 
			// Al ser un problema de maximizaci�n se resta del fitness el resultado.
			fitnessFinal = fitness - k * raizArbol.getNumNodos();			
		}
		
		return fitnessFinal;
	}

	protected void checkProfundidad() {
		if (debugEjecucion) System.out.println("[CromosomaArbol.checkProfundidad()]");
		int maxProf = 0;
		int actProf;
		Arbol actArb;
		for (int i = 0; i < nodos.size(); i++) {
			actArb = nodos.get(i);
			actProf = actArb.getProfundidad();
			if (actProf > maxProf) maxProf = actProf;
		}
		
		profundidadIndividuo = maxProf;
	}
	
	public void actualizaArbol() {
		if (debugEjecucion) System.out.println("[CromosomaArbol.actualizaArbol()]");
		//Una vez terminado el cruce hace falta actualizar el cromosoma
		// Actualizar lista
		actualizaLista(null);
		// actualiza profundidad
		checkProfundidad();
		System.out.printf("Profundidad despues: %d\n", profundidadIndividuo);
		// actualiza num nodos
		System.out.printf("Nodos despues: %d\n", nodos.size());
	}
	
	private void actualizaLista(Arbol a) {
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
		// Si es un OPERANDO simplemente se a�ade a la lista de nodos.
		
		// Empezamos a recorrer por la raizArbol
		nodos.add(a);
	}
	
	
	// METODOS DEL PADRE
	@Override
	public float evalua() {
		fitness = (float) FuncionEvalArbol.funcionEvalArbol(raizArbol, numAs, valoresMultiplexor);
		
		fitness = (float) controlBloating();
		
		return fitness;
	}
	
	@Override
	public Cromosoma clone() {
		return new CromosomaArbol(this);
	}
	
	@Override
	public boolean compara_mejor_fitness(float f) {
		// Recibe un fitness y devuelve si el cromosoma es mejor (true) o peor (false)
		if (fitness > f) return true;
		else return false;
	}

	@Override
	public boolean compara_peor_fitness(float f) {
		// Recibe un fitness y devuelve si el cromosoma es peor (true) o mejor (false)
		if (fitness < f) return true;
		else return false;
	}

	@Override
	public int compareTo(Cromosoma c) {
		if(this.fitness < c.getFitness())
			return -1;
		else if(this.fitness > c.getFitness())
			return 1;
		
		return 0;
	}
	
	@Override
	public float[] fenotipos() {
		String fenotipo;
		fenotipo = fenotipoArbol();
		System.out.println(fenotipo);
		return null;
	}
	
	// GETTERS Y SETTERS
	public Arbol getArbol() {
		return raizArbol;
	}
	
	public void setArbol(Arbol a) {
		raizArbol = a;
	}
	
	public int getTamArbol() {
		//Comprobaci�n num nodos
		System.out.printf("Nodos en lista: %d", nodos.size());
		System.out.printf("Nodos segun raiz: %d", raizArbol.getNumNodos());
		
		return raizArbol.getNumNodos();
	}
	
	public int getProfInd(){
		checkProfundidad();
		return profundidadIndividuo;
	}
	
	public int getNumAs() { //Usado por la mutaci�n
		return numAs;
	}
	
	public void setMediaPob(double tmp) {
		tamMedioPob = tmp;
	}
	
	public void setk(double Kin) {
		k = Kin;
	}
	
	public List<Arbol> getListaNodos(){
		return nodos;
	}
	
	public void setListaNodos(List<Arbol> nl) {
		nodos = nl;
	}
	
	public boolean getUseIf() { //Usado por mutacion
		return useIf;
	}
	
}
