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
		GenArbol padreNodo;
		String posHijoEnPadre; 
		int nivel;
		Random r = new Random();
		int profmax = r.nextInt(4); // Genera un nuevo arbol de profundidad variable de 0 (hoja) a 3
		GenArbol nuevoNodo;
		
		// Subarbol: descarta un subarbol y vuelve a generarlo aleatoriamente.
		CromosomaP3 CA = (CromosomaP3) c;
		CA.actualizaArbol();
		List<GenArbol> nodos = CA.getListaNodos();
		
		// Obtenemos nodo mutable
		int numNodos = nodos.size();
		int posNodo;
		if (numNodos == 1) {
			//Si solo tiene uno usa la raiz (no existen subarboles)
			posNodo = 0;
		} else {
			// Cambia un subarbol
			posNodo = buscaNodo(nodos);
		}
		GenArbol nodoMutable = nodos.get(posNodo);
		padreNodo = nodoMutable.getPadre();
		
		// DEBUG
		System.out.println("__Arbol_ANTES_Mutacion_Subarbol__");
		System.out.printf("Número de nodos: %d\n", CA.getNodosInd());
		System.out.printf("Profundidad arbol: %d\n", CA.getProfInd());
		
		// Gestion del padre
		
		if (padreNodo == null) {
			// No tiene padre, simplemente cambiar la raiz
			nivel = nodoMutable.getProfundidad();
			nuevoNodo = CA.creaArbolCreciente(nivel, profmax+nivel, null);
			CA.setArbol(nuevoNodo);
		} else {
			// Nos quedamos con el padre
			posHijoEnPadre = "NA";
			
			// Vemos la cantidad de hijos del padre
			int aridad = getAridad(padreNodo.getValor());
			
			//buscar en el arbol
			if (aridad == 0) {
				System.out.println("[Mutacion Subarbol] ERROR!!!! Padre es un operando.");
			}
			else if (aridad == 1) {
				posHijoEnPadre = "Izq";
			} 
			else if (aridad == 2) {
				if (padreNodo.getHi() == nodoMutable) posHijoEnPadre = "Izq";
				else if (padreNodo.getHd() == nodoMutable) posHijoEnPadre = "Der";
			}
			else if (aridad == 3) {
				// Comprobar si es el centro
				if (padreNodo.getHi() == nodoMutable) posHijoEnPadre = "Izq";
				else if (padreNodo.getHc() == nodoMutable) posHijoEnPadre = "Cnt";
				else if (padreNodo.getHi() == nodoMutable) posHijoEnPadre = "Der";
			}
			
			nivel = nodoMutable.getProfundidad();
			nuevoNodo = CA.creaArbolCreciente(nivel, profmax+nivel, padreNodo);
			
			// Enlazar con el padre.
			if (posHijoEnPadre == "Izq") {
				padreNodo.setHI(nuevoNodo);
			} else if (posHijoEnPadre == "Cnt"){
				padreNodo.setHC(nuevoNodo);
			} else if (posHijoEnPadre == "Der") {
				padreNodo.setHD(nuevoNodo);	
			} else {
				//System.out.println("[MutacionSubarbol] Error: Devolviendo el mismo hijo.");
				return false;
			}
			
		}
		
		// Tenemos que recalcular el cromosoma (generar lista nodos, ver profundidad, ver cantidad nodos)
		CA.actualizaArbol();
		//Devolvemos el cromosoma mutado
		c = CA;
		
		// DEBUG
		System.out.println("__Arbol_DESPUES_Mutacion_Subarbol__");
		System.out.printf("Número de nodos: %d\n", CA.getNodosInd());
		System.out.printf("Profundidad arbol: %d\n", CA.getProfInd());
		
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