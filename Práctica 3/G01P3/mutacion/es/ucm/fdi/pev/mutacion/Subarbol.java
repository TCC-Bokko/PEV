package es.ucm.fdi.pev.mutacion;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.pev.estructura.GenArbol;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaP3;

public class Subarbol {
	public static boolean subarbol(Cromosoma c, float prob) 
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
		// Subarbol: descarta un subarbol y vuelve a generarlo aleatoriamente.
		CromosomaP3 CA = (CromosomaP3) c;
		CA.actualizaArbol();
		List<GenArbol> nodos = CA.getListaNodos();
		
		int numNodos = nodos.size();
		int posNodo;
		if (numNodos == 1) {
			//Si solo tiene uno usa la raiz (no existen subarboles)
			posNodo = 0;
		} else {
			// Cambia un subarbol
			posNodo = buscaNodo(nodos);
		}
		
		// DEBUG
		System.out.println("__Arbol_ANTES_Mutacion_Subarbol__");
		System.out.printf("Número de nodos: %d\n", CA.getNodosInd());
		System.out.printf("Profundidad arbol: %d\n", CA.getProfInd());
		
		// Elegimos un nodo (Sin incluir raiz)
		GenArbol nodoMutable = nodos.get(posNodo);
		GenArbol padreNodo;
		String posicion;
		// Nos quedamos con el padre
		if (numNodos > 1) {
			 padreNodo = nodoMutable.getPadre();
			posicion = "NA";
			int aridad = getAridad(padreNodo.getValor());
			
			//buscar en el arbol
			if (padreNodo.getHi() == nodoMutable) posicion = "Izq";
			if (aridad == 3) {
				// Comprobar si es el centro
				if (padreNodo.getHc() == nodoMutable) posicion = "Cnt";
			}
			if (aridad >= 2) {
				if (padreNodo.getHi() == nodoMutable) posicion = "Der";
				
			}
			
		}
		
		// Reconstruimos el nodo (de modo completo)
		Random r = new Random();
		int profmax = r.nextInt(4); // Genera un nuevo arbol de profundidad variable de 0 (hoja) a 3
		int nivel = nodoMutable.getProfundidad();
		profmax = profmax + nivel;
		// Crear arbol nuevo
		GenArbol nuevoNodo = CA.creaArbolCreciente(nivel, profmax, padreNodo);
		// Enlazar con el padre.
		if (numNodos > 1) {
			if (posicion == "Izq") {
				padreNodo.setHI(nuevoNodo);
			} else if (posicion == "Cnt"){
				padreNodo.setHC(nuevoNodo);
			} else if (posicion == "Der") {
				padreNodo.setHD(nuevoNodo);	
			} else {
				System.out.println("[MutacionSubarbol] Error: Devolviendo el mismo hijo.");
			}
		}
		
		// Tenemos que recalcular el cromosoma (generar lista nodos, ver profundidad, ver cantidad nodos)
		CA.actualizaArbol();
		
		// DEBUG
		System.out.println("__Arbol_DESPUES_Mutacion_Subarbol__");
		
		//Devolvemos el cromosoma mutado
		c = CA;
		
		return true;
	}
		
	//Devuelve la posición del nodo operador valido.
	private static int buscaNodo(List<GenArbol> nodos) {
		Random r = new Random();
		int i = r.nextInt(nodos.size()-1); // Cualquier nodo excepto raiz
		
		return i;
	}
	
	private static boolean esOperador(GenArbol arbol) {
		String valor = arbol.getValor();
		if (valor == "OR" || valor == "AND" || valor == "NOT" || valor == "IF") return true;
		else return false;
	}
	
	private static int getAridad(String funcion) {
		int aridad = 0;
		
		if (funcion == "IF") aridad = 3;
		else if (funcion == "OR" || funcion == "AND") aridad = 2;
		else System.out.println("[Mutacion-Permutacion] Argumento funcion no corresponde con ningun operador valido, devolviendo aridad 0");
		
		return aridad;
	}
}