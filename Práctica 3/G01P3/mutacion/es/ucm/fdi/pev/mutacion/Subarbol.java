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
			muta(c);
			haMutado = true;
		}
		return haMutado;
	}
	
	private static void muta(Cromosoma c)
	{
		// Subarbol: descarta un subarbol y vuelve a generarlo aleatoriamente.
		CromosomaP3 CA = (CromosomaP3) c;
		List<GenArbol> nodos = CA.getListaNodos();
		
		// DEBUG
		System.out.println("__Arbol_ANTES_Mutacion_Subarbol__");
		System.out.printf("Número de nodos: %d", CA.getNodosInd());
		System.out.printf("Profundidad arbol: %d", CA.getProfInd());
		
		// Elegimos un nodo (Sin incluir raiz)
		int posNodo = buscaNodo(nodos);
		GenArbol nodoMutable = nodos.get(posNodo);
	
		// Nos quedamos con el padre
		GenArbol padreNodo = nodoMutable.getPadre();
		String posicion = "NA";
		
		if (padreNodo.getHi() == nodoMutable) posicion = "Izq";
		else if (padreNodo.getHc() == nodoMutable) posicion = "Cnt";
		else if (padreNodo.getHi() == nodoMutable) posicion = "Der";
		else System.out.println("[MutacionSubarbol] Error: No es posible discernir la posicion del nodo obtenido en su padre");
		
		// Reconstruimos el nodo (de modo completo)
		Random r = new Random();
		int profmax = r.nextInt(4); // Genera un nuevo arbol de profundidad variable de 0 (hoja) a 3
		int nivel = nodoMutable.getProfundidad();
		profmax = profmax + nivel;
		// Crear arbol nuevo
		GenArbol nuevoNodo = CA.creaArbolCreciente(nivel, profmax, padreNodo);
		// Enlazar con el padre.
		if (posicion == "Izq") {
			padreNodo.setHI(nuevoNodo);
		} else if (posicion == "Cnt"){
			padreNodo.setHC(nuevoNodo);
		} else if (posicion == "Der") {
			padreNodo.setHD(nuevoNodo);	
		} else {
			System.out.println("[MutacionSubarbol] Error: Devolviendo el mismo hijo.");
		}
		
		// Tenemos que recalcular el cromosoma (generar lista nodos, ver profundidad, ver cantidad nodos)
		CA.actualizaArbol();
		
		// DEBUG
		System.out.println("__Arbol_DESPUES_Mutacion_Subarbol__");
		
		//Devolvemos el cromosoma mutado
		c = CA;
		
	}
		
	//Devuelve la posición del nodo operador valido.
	private static int buscaNodo(List<GenArbol> nodos) {
		Random r = new Random();
		int i = r.nextInt(nodos.size()-1); // Cualquier nodo excepto raiz
		
		return i;
	}
}