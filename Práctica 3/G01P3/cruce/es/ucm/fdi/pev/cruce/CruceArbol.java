package es.ucm.fdi.pev.cruce;

import es.ucm.fdi.pev.estructura.Arbol;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaP3;

import java.util.List;
import java.util.Random;

public class CruceArbol {
	public static void cruceArbol(Cromosoma c1, Cromosoma c2) {
		/*
		[] Permutación:
			Buscar un punto aleatorio en cada uno de los padres (nodo función)
			Intercambia los subarboles de los nodos entre los padres.
			Se intenta que se intercambien nodos función con más posiblidad (0.9)
		*/
		
		////////////////////////////////////////////////////////////////////
		// CARGA DE LOS PADRES
		////////////////////////////////////////////////////////////////////
		CromosomaP3 CA1 = (CromosomaP3) c1;
		CromosomaP3 CA2 = (CromosomaP3) c2;
		List<Arbol> nodosP1 = CA1.getListaNodos();
		List<Arbol> nodosP2 = CA2.getListaNodos();
		
		// DEBUG
		System.out.println("__PADRE_1__");
		System.out.printf("Profundidad ANTES cruce: %d\n", CA1.getProfInd());
		System.out.printf("Nodos ANTES cruce: %d\n", nodosP1.size());
		System.out.println("__PADRE_2__");
		System.out.printf("Profundidad ANTES cruce: %d\n", CA2.getProfInd());
		System.out.printf("Nodos ANTES cruce: %d\n", nodosP2.size());
		
		////////////////////////////////////////////////////////////////////
		// ENCONTRAR NODOS DE CRUCE
		////////////////////////////////////////////////////////////////////
		// Seleccionar nodos
		// Elegir al azar del padre1		
		Arbol subArb1 = eligeNodo(nodosP1);
		Arbol subArb2 = eligeNodo(nodosP2);
		
		// Intercambiar Subarboles
		Arbol padreSubArbol1 = subArb1.getPadre();
		Arbol padreSubArbol2 = subArb2.getPadre();
		
		// Intercambio en P1
		if (padreSubArbol1.getHi() == subArb1) {
			padreSubArbol1.setHI(subArb2);
		} else if (padreSubArbol1.getHc() == subArb1) {
			padreSubArbol1.setHC(subArb2);
		} else if (padreSubArbol1.getHd() == subArb1) {
			padreSubArbol1.setHD(subArb2);
		} else {
			System.out.println("[CRUCEARBOL] Ningun hijo del padreSubArbol1 es el subArbol1. Fallo al intercambiar subarboles.");
		}
		
		// Intercambio en P2
		if (padreSubArbol2.getHi() == subArb2) {
			padreSubArbol2.setHI(subArb1);
		} else if (padreSubArbol2.getHc() == subArb2) {
			padreSubArbol2.setHC(subArb1);
		} else if (padreSubArbol2.getHd() == subArb2) {
			padreSubArbol2.setHD(subArb1);
		} else {
			System.out.println("[CRUCEARBOL] Ningun hijo del padreSubArbol1 es el subArbol1. Fallo al intercambiar subarboles.");
		}

		// Actualizamos padres
		subArb1.setPadre(padreSubArbol2);
		subArb2.setPadre(padreSubArbol1);
		
		////////////////////////////////////////////////////////////////////
		// Actualizamos listas de nodos
		////////////////////////////////////////////////////////////////////
		System.out.println("__HIJO_1__");
		CA1.actualizaArbol();
		System.out.println("__HIJO_2__");
		CA2.actualizaArbol();

	}
	
	private static Arbol eligeNodo(List<Arbol> lista) {
		Boolean valido = false;
		Arbol nodo = null;
		Random r = new Random();
		int aceptarHoja = 1;
		
		while (!valido || nodo == null) {
			//Sacamos un nodo de la lista
			int nodoElegido1 = r.nextInt(lista.size()-1); // Dado que esta en postorden, el último nodo es la raiz, lo ignoramos.
			nodo = lista.get(nodoElegido1);
			
			// Vemos si es operador u operando
			String dato = nodo.getValor();
			if (dato != "IF" && dato != "OR" && dato != "NOT" && dato != "AND") {
				// Si es un operando
				int result = r.nextInt(10);
				if (result < aceptarHoja) { // Solo aceptaremos operandos un 10% de las veces.
					valido = true;
				}
			} else {
				// Es un operador
				valido = true;
			}
		}
		
		return nodo;
	}
}
