package es.ucm.fdi.pev.estructura;
import java.util.Random;

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
	
	//Ver donde poner esto para no estar generandolo en cada cromosoma
	////// WIP - TO DO ///////// Que los datos de dentro dependan del selector del GUI (para el IF)
	private String[] fulloperador = new String[] {"IF", "AND", "OR", "NOT"};
	private String[] operador = new String[] {"AND", "OR", "NOT"};
	// ////// WIP - TO DO /////////
	// ESTO HAY QUE CAMBIARLO: hay que adaptarlo a lo metido por GUI.
	// Inicializar vacío, crear tantas entradas A* como solicite el gui.
	// y luego crear tantos direccionadores D* como solicite el gui.
	// Ver que las A direccionen las D (2A -> 4D) (3A -> 8D) (4A - 16D)
	private String[] operandos = new String[] {"A0", "A1", "D0", "D1", "D2", "D3"}; 
	
	public CromosomaArbol() {
		r = new Random();
		raizArbol = new Arbol();
		inicializacion = "Completa"; //Cambiar a lo que reciba por GUI
		prof_min = 1; //Cambiar a lo que reciba por GUI
		prof_max = 3; //Cambiar a lo que reciba por GUI
		inicializaCromosoma();
	}
	
	@Override
	protected void inicializaCromosoma() {
		switch (inicializacion) {
		case "Completa":
			raizArbol = creaArbolCompleto(raizArbol, 0, prof_min-1, prof_max-1);
			break;
		case "Creciente":
			raizArbol = creaArbolCreciente(raizArbol, 0, prof_min-1, prof_max-1);
			break;
		case "Ramped&Half":
			raizArbol = creaArbolRampedAndHalf(raizArbol, 0, prof_min-1, prof_max-1);
			break;
		
		}
	}
	
	private Arbol creaArbolCompleto(Arbol arbol, int nivel, int prof_min, int prof_max) {
		if (arbol == null) {
			arbol = new Arbol();
		}
		
		//Profundidad de este nodo.
		arbol.setProfundidad(nivel);
		
		//Si no es hoja
		if (prof_min > 0) {
			//Generar subardol usando operador {IF, NOT, OR, AND}
			////// WIP - TO DO ///////// Ver si usamos IF o no en el GUI.
			int it = r.nextInt(fulloperador.length);
			String operador = fulloperador[it];
			arbol.setValor(operador);
			
			//GENERAR HIJOS
			
			//Izquierdo
			HI = creaArbolCompleto(arbol.getHi(), nivel+1, prof_min-1, prof_max-1);
			arbol.setNumNodos(arbol.getNumNodos() + arbol.getHi().getNumNodos());
			
			//Central (Si procede)
			if (tres_operandos(operador)) {
				HC = creaArbolCompleto(arbol.getHc(), nivel+1, prof_min-1, prof_max-1);
				arbol.setNumNodos(arbol.getNumNodos() + arbol.getHi().getNumNodos());
			} else {
				HC = null;
				arbol.setHC(HC);
			}
			
			//Derecho
			HD = creaArbolCompleto(arbol.getHd(), nivel+1, prof_min-1, prof_max-1);
			arbol.setNumNodos(arbol.getNumNodos() + arbol.getHd().getNumNodos());
		} else {
			prof_min = 0;
		}
		
		//Si es una HOJA
		if (prof_max == 0) {
			//Generar subarbol usando datos de tipo operandos
			int it = r.nextInt(operandos.length);
			String operando = operandos[it];
			arbol.setValor(operando);
			arbol.setNumNodos(arbol.getNumNodos()+1);			
		} else {
			//Se decide aleatoriamente si colocamos operando u operador
			int tipo = r.nextInt(2);
			if (tipo == 0) {
				////// WIP - TO DO ///////// Ver si usamos IF o no en el GUI.
				int it = r.nextInt(fulloperador.length);
				String operador = fulloperador[it];
				arbol.setValor(operador);
				
				//GENERAR HIJOS
				
				//Izquierdo
				HI = creaArbolCompleto(arbol.getHi(), nivel+1, prof_min-1, prof_max-1);
				arbol.setNumNodos(arbol.getNumNodos() + arbol.getHi().getNumNodos());
				
				//Central (Si procede)
				if (tres_operandos(operador)) {
					HC = creaArbolCompleto(arbol.getHc(), nivel+1, prof_min-1, prof_max-1);
					arbol.setNumNodos(arbol.getNumNodos() + arbol.getHi().getNumNodos());
				} else {
					HC = null;
					arbol.setHC(HC);
				}
				
				//Derecho
				HD = creaArbolCompleto(arbol.getHd(), nivel+1, prof_min-1, prof_max-1);
				arbol.setNumNodos(arbol.getNumNodos() + arbol.getHd().getNumNodos());
			} else {
				// Generación subarbol operando
				int it = r.nextInt(operandos.length);
				String operando = operandos[it];
				arbol.setValor(operando);
				arbol.setNumNodos(arbol.getNumNodos()+1);	
			}
		}
		
		return arbol;
	}
	
	private Arbol creaArbolCreciente(Arbol arbol, int nivel, int prof_min, int prof_max) {
		return arbol;
	}
	
	private Arbol creaArbolRampedAndHalf(Arbol arbol, int nivel, int prof_min, int prof_max) {
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
		// TODO Auto-generated method stub
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

	@Override
	public float evalua() {
		// TODO Auto-generated method stub
		return 0;
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
