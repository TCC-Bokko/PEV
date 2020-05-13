package es.ucm.fdi.pev.estructura;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.ucm.fdi.pev.evaluacion.FuncionEvalArbol;

public class CromosomaArbol extends Cromosoma {
	//Raiz
	Arbol raizArbol;
	//Auxiliares
	Arbol HI;	
	Arbol HD;
	Arbol HC;
	String inicializacion;
	int prof_min;
	int prof_max;
	Random r;
	List<Arbol> nodos; 
	double fitness;
	
	// Opciones del GUI
	protected int numAs;
	protected int numDs;
	protected Boolean useIf;
	
	private String[] operadores;
	private String[] operandos; 
	
	public CromosomaArbol(int As, String uif, int pmin, int pmax, String initC) {
		//Datos del AGen establecidos en el GUI
		
		//USO DEL OPERADOR IF
		if (uif == "True") { 
			useIf = true;
			operadores = new String[] {"IF", "AND", "OR", "NOT"};
		}
		else { 
			useIf = false;
			operadores = new String[] {"AND", "OR", "NOT"};
		}
	
		// CANTIDAD DE OPERANDOS
		numAs = As;
		if (numAs == 2) {
			//2 Address que direccionan 4 datos
			operandos = new String[] {"A0", "A1", "D0", "D1", "D2", "D3"};
		} else if (numAs == 3) {
			// 3 Address que direccionan 8 datos
			operandos = new String[] {"A0", "A1", "A2", "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7"};
		}
		
		prof_min = pmin; 
		prof_max = pmax; 
		inicializacion = initC;
		nodos = new ArrayList<Arbol>();
		
		// Llamadas a metodos
		r = new Random();
		inicializaCromosoma();
	}
	
	@Override
	protected void inicializaCromosoma() {
		switch (inicializacion) {
		case "Completa":
			raizArbol = creaArbolCompleto(0, prof_max-1);
			break;
		case "Creciente":
			raizArbol = creaArbolCreciente(0, prof_max-1);
			break;
		default:
			raizArbol = creaArbolCompleto(0, prof_max-1);
			break;
		}
	}
	
	private Arbol creaArbolCompleto(int nivel, int prof_max) {
		// Completa genera nodos de tipo operador hasta alcanzar profundidad maxima donde genera nodos operando.
		Arbol arbol = new Arbol();
		
		//Profundidad de este nodo.
		arbol.setProfundidad(nivel);
		
		//Si no es hoja
		if (nivel < prof_max) {
			//Generar subardol usando operador {IF, NOT, OR, AND}
			int it = r.nextInt(operadores.length);
			String operador = operadores[it];
			arbol.setValor(operador);
			
			//GENERAR HIJOS
			
			//Izquierdo
			arbol.setHI(creaArbolCompleto(nivel+1, prof_max));
			//A�adimos este nodo y le sumamos los nodos de los hijos.
			arbol.setNumNodos(arbol.getNumNodos()+1);
			arbol.setNumNodos(arbol.getNumNodos() + arbol.getHi().getNumNodos());
			
			//Central (Si procede)
			if (tres_operandos(operador)) {
				arbol.setHC(creaArbolCompleto(nivel+1, prof_max));
				arbol.setNumNodos(arbol.getNumNodos()+1);
				arbol.setNumNodos(arbol.getNumNodos() + arbol.getHc().getNumNodos());
			} else {
				HC = null;
				arbol.setHC(HC);
			}
			
			//Derecho
			arbol.setHD(creaArbolCompleto(nivel+1, prof_max));
			arbol.setNumNodos(arbol.getNumNodos()+1);
			arbol.setNumNodos(arbol.getNumNodos() + arbol.getHd().getNumNodos());
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
	
	private Arbol creaArbolCreciente(int nivel, int prof_max) {
		// Crecientea genera nodos aleatorios de cualquier tipo
		// hasta alcanzar profundidad maxima donde �nicamente genera nodos operando.
		Arbol arbol = new Arbol();
		//Profundidad de este nodo.
		arbol.setProfundidad(nivel);
		
		//Si no es hoja
		if (nivel < prof_max) {
			//Generar subardol usando operador u operando {IF, NOT, OR, AND}
			int type = r.nextInt(2);
			if (type == 0) {
				int it = r.nextInt(operadores.length);
				String operador = operadores[it];
				arbol.setValor(operador);
				
				//GENERAR HIJOS si ha salido un operador
				//Izquierdo
				arbol.setHI(creaArbolCompleto(nivel+1, prof_max));
				//A�adimos este nodo y le sumamos los nodos de los hijos.
				arbol.setNumNodos(arbol.getNumNodos()+1);
				arbol.setNumNodos(arbol.getNumNodos() + arbol.getHi().getNumNodos());
				
				//Central (Si procede)
				if (tres_operandos(operador)) {
					arbol.setHC(creaArbolCompleto(nivel+1, prof_max));
					arbol.setNumNodos(arbol.getNumNodos()+1);
					arbol.setNumNodos(arbol.getNumNodos() + arbol.getHc().getNumNodos());
				} else {
					HC = null;
					arbol.setHC(HC);
				}
				
				//Derecho
				arbol.setHD(creaArbolCompleto(nivel+1, prof_max));
				arbol.setNumNodos(arbol.getNumNodos()+1);
				arbol.setNumNodos(arbol.getNumNodos() + arbol.getHd().getNumNodos());
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
	
	private Boolean tres_operandos(String operador) {
		Boolean isValid = false;
		
		if (operador == "IF") isValid = true;
		
		return isValid;
	}

	@Override
	public Cromosoma clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float[] fenotipos() {
		String fenotipo;
		fenotipo = fenotipoArbol();
		System.out.println(fenotipo);
		return null;
	}
	
	public String fenotipoArbol() {
		String operacion = "(";
		
		//Recorrido recursivo por los hijos del arbol
		ValoresArbol(operacion, raizArbol);
		
		operacion = operacion + ")";
		
		return operacion;
	}
	
	private String ValoresArbol(String cadena, Arbol arbol) {
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

	@Override
	public float evalua() {
		fitness = FuncionEvalArbol.funcionEvalArbol(raizArbol);
		return (float)fitness;
	}

	@Override
	public boolean compara_mejor_fitness(float f) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean compara_peor_fitness(float f) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int compareTo(Cromosoma c) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
