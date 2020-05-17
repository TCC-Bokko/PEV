package es.ucm.fdi.pev.mutacion;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.pev.estructura.GenArbol;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaP3;

public class Permutacion {
	public static boolean permutacion(Cromosoma c, float prob) 
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
		// Permutación: Intercambia el orden de dos operandos en una funcion.
		CromosomaP3 CA = (CromosomaP3) c;
		CA.actualizaArbol();
		List<GenArbol> nodos = CA.getListaNodos();
		
		// Caso 1 solo nodo
		int posNodo;
		GenArbol nodoMutable;
		int numNodos = nodos.size();
		if (numNodos == 1) {
			// Es una raiz operando, no hace nada
			return false;
		}		
		else if (numNodos == 2) {
			// Es un operando NOT con un operando, no hacemos nada
			return false;
		} else {
			//Obtenemos un nodo Funcion con un operador
			posNodo = buscaFuncion(nodos);
			nodoMutable = nodos.get(posNodo);
			String valor = nodoMutable.getValor();
			int aridad = getAridad(valor);
			GenArbol aux;
			Random r = new Random();
			
			// Intercambiamos los hijos del nodo.
			if (aridad == 2) {
				aux = nodoMutable.getHi();
				nodoMutable.setHI(nodoMutable.getHd());
				nodoMutable.setHD(aux);
			} else if (aridad == 3) {
				int intercambio = r.nextInt(3);
				// Con 3 valores pueden aparecer 3 intercambios de 2 elementos.
				if (intercambio == 0) {
					// ICD to DCI
					aux = nodoMutable.getHi();
					nodoMutable.setHI(nodoMutable.getHd());
					nodoMutable.setHD(aux);
				} else if (intercambio == 1) {
					// ICD to CID
					aux = nodoMutable.getHi();
					nodoMutable.setHI(nodoMutable.getHc());
					nodoMutable.setHC(aux);
				} else if (intercambio == 2) {
					// ICD to IDC
					aux = nodoMutable.getHc();
					nodoMutable.setHC(nodoMutable.getHd());
					nodoMutable.setHD(aux);
				} else {
					//System.out.println("[Mutacion-Permutacion] Error intercambio en aridad 3. Devolviendo el cromosoma sin mutar.");
					return false;
				}
			} else {
				//System.out.println("[Mutacion-Permutacion] Error: Aridad = 0. Devolviendo el cromosoma sin mutar.");
				return false;
			}
			
			//Lo metemos en la lista -- (A partir de aqui necesario?)
			nodos.set(posNodo, nodoMutable);
			CA.setListaNodos(nodos);
			c = CA;
			
			return true;
		}
		
	}
	
	//Devuelve la posición del nodo operador valido.
	private static int buscaFuncion(List<GenArbol> nodos) {
		Random r = new Random();
		Boolean valido = false;
		String tipo;
		int i = -1;
		while (!valido || i == -1) {
			i = r.nextInt(nodos.size()); //Como la raiz puede ser un operador, tambien la incluimos
			tipo = nodos.get(i).getValor();
			//Si es una de las funciones
			if (tipo == "AND" || tipo == "OR" || tipo == "IF") {
				valido = true;
			}
			
			if(valido && i == -1) System.out.println("[Mutacion-Permutacion] ERROR: nodo valido pero en posicion -1.");
		}
		
		return i;
	}
	
	private static int getAridad(String funcion) {
		int aridad = 0;
		
		if (funcion == "IF") aridad = 3;
		else if (funcion == "OR" || funcion == "AND") aridad = 2;
		//else System.out.println("[Mutacion-Permutacion] Argumento funcion no corresponde con ningun operador valido, devolviendo aridad 0");
		
		return aridad;
	}
}