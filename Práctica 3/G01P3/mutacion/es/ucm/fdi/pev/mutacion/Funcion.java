package es.ucm.fdi.pev.mutacion;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.pev.estructura.GenArbol;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaArbol;
import es.ucm.fdi.pev.estructura.CromosomaP3;

public class Funcion {
	public static boolean funcion(Cromosoma c, float prob) 
	{	
		boolean haMutado = false;
		Random r = new Random();
		float rand = r.nextFloat();
		
		if(rand < prob) {
			haMutado = muta(c);
			//haMutado = true;
		}
		return haMutado;
	}
	
	private static boolean muta(Cromosoma c)
	{
		// Funcion: Cambia uno de los nodos operador (función).
		// Ojo, debe tener el mismo nivel de aridad (cantidad de parametros)
		CromosomaP3 CA = (CromosomaP3) c;
		CA.actualizaArbol();
		int numNodos = CA.getNodosInd();
		System.out.printf("Numero nodos: %d", numNodos);
		
		if (numNodos == 1) {
			// Es una raiz operando. No hacer nada.
			return false;
		} else {
			
			//CA.actualizaArbol();
			GenArbol[] nodos = (GenArbol[]) CA.getGenes();
			//List<GenArbol> nodos = CA.getListaNodos();
			
			//Obtenemos un nodo Funcion con un operador
			System.out.println("Busca Funcion.");
			int posNodo = buscaFuncion(nodos);
			GenArbol nodoMutable = nodos[posNodo];
			//GenArbol nodoMutable = nodos.get(posNodo);
			String valor = nodoMutable.getValor();
			int aridad = getAridad(valor);
			
			if (aridad == 0) {
				// Si tenemos un operando, no hacemos nada.
				System.out.println("Aridad 0: Devolviendo el nodo como estaba.");
			} else {
				// Establecemos un nuevo valor
				String nuevoValor = valor; //Lo inicializamos igual al que tenía antes por si falla algo.
				String[] operadoresAridad1 = new String[] {"NOT"};
				String[] operadoresAridad2 = new String[] {"AND", "OR"};
				String[] operadoresAridad3 = new String[] {"IF"};
				Random r = new Random();
				
				if (aridad == 1) {
					// Soy consciente de que no cambiara nada, 
					// pero esto nos vale para otras funciones que tengan más de un 
					// operador con aridad 1
					int selec = r.nextInt(operadoresAridad1.length);
					nuevoValor = operadoresAridad1[selec];
				}
				else if (aridad == 2) {
					int selec = r.nextInt(operadoresAridad2.length);
					nuevoValor = operadoresAridad2[selec];
				} else if (aridad == 3) {
					// Soy consciente de que no cambiara nada, pero esto nos vale para otras funciones que tengan más de un operador con aridad 3
					int selec = r.nextInt(operadoresAridad3.length);
					nuevoValor = operadoresAridad3[selec];
				}
				
				//Se lo pasamos al nodo
				nodoMutable.setValor(nuevoValor);
				
				//Lo metemos en la lista -- (A partir de aqui necesario?)
				nodos[posNodo] = nodoMutable;
				//nodos.set(posNodo, nodoMutable);
				CA.setGenes(nodos);
				//CA.setListaNodos(nodos);
				c = CA;
				
				
			}
			return true;
		}
		
	}
	
	
	//Devuelve la posición del nodo operador valido.
	private static int buscaFuncion(GenArbol[] nodos) {
		Random r = new Random();
		Boolean valido = false;
		String tipo;
		int i = -1;
		while (!valido || i == -1) {
			i = r.nextInt(nodos.length); //Como la raiz puede ser un operador, tambien la incluimos
			tipo = nodos[i].getValor();
			//Si es una de las funciones
			if (tipo == "AND" || tipo == "OR" || tipo == "NOT" || tipo == "IF") {
				valido = true;
				System.out.printf("PosValido: %d", i);
			}
			
			if(valido && i == -1) System.out.println("[Mutacion Funcion] ERROR: nodo valido pero en posicion -1.");
		}
		
		return i;
	}
	
	//Devuelve la posición del nodo operador valido.
	private static int buscaFuncion(List<GenArbol> nodos) {
		Random r = new Random();
		Boolean valido = false;
		String tipo;
		int posValido = -1;
		while (!valido || posValido == -1) {
			posValido = r.nextInt(nodos.size()); //Como la raiz puede ser un operador, tambien la incluimos
			tipo = nodos.get(posValido).getValor();
			//Si es una de las funciones
			if (tipo == "AND" || tipo == "OR" || tipo == "NOT" || tipo == "IF") {
				valido = true;
				System.out.printf("PosValido: %d", posValido);
			}
			
			if(valido && posValido == -1) System.out.println("[Mutacion Funcion] ERROR: nodo valido pero en posicion -1.");
		}
		
		
		return posValido;
	}
	
	private static int getAridad(String funcion) {
		int aridad = 0;
		
		if (funcion == "IF") aridad = 3;
		else if (funcion == "OR" || funcion == "AND") aridad = 2;
		else if (funcion == "NOT") aridad = 1;
		
		
		return aridad;
		
	}
	
}