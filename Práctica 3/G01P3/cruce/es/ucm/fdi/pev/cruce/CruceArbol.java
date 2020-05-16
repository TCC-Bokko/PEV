package es.ucm.fdi.pev.cruce;

import es.ucm.fdi.pev.estructura.GenArbol;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaArbol;
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
		/*
		CromosomaP3 CA1 = (CromosomaP3) c1;
		CromosomaP3 CA2 = (CromosomaP3) c2;
		
		List<GenArbol> nodosP1 = CA1.getListaNodos();
		List<GenArbol> nodosP2 = CA2.getListaNodos();
		*/
		
		GenArbol[] nodosP1 = (GenArbol[]) c1.getGenes();
		GenArbol[] nodosP2 = (GenArbol[]) c2.getGenes();
		
		
		// DEBUG
		System.out.println("__PADRE_1__");
		System.out.printf("Profundidad ANTES cruce: %d\n", nodosP1[0].getProfundidad());
		System.out.printf("Nodos ANTES cruce: %d\n", nodosP1.length); //List<Arbol> usa .size()
		System.out.println("__PADRE_2__");
		System.out.printf("Profundidad ANTES cruce: %d\n", nodosP2[0].getProfundidad());
		System.out.printf("Nodos ANTES cruce: %d\n", nodosP2.length);
		
		////////////////////////////////////////////////////////////////////
		// ENCONTRAR NODOS DE CRUCE
		////////////////////////////////////////////////////////////////////
		// Seleccionar nodos
		// Elegir al azar del padre1
		int posCruce1 = eligeNodo(nodosP1);
		int posCruce2 = eligeNodo(nodosP2);
		GenArbol subArb1 = nodosP1[posCruce1];
		GenArbol subArb2 = nodosP2[posCruce2];
		
		// Intercambiar Subarboles
		GenArbol padreSubArbol1 = subArb1.getPadre();
		GenArbol padreSubArbol2 = subArb2.getPadre();
		
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
		CromosomaP3 CA1 = (CromosomaP3) c1;
		CA1.actualizaArbol();
		System.out.println("__HIJO_2__");
		CromosomaP3 CA2 = (CromosomaP3) c1;
		CA2.actualizaArbol();
		
		////////////////////////////////////////////////////////////////////
		// Devolvemos hijos
		///////////////////////////////////////////////////////////////////
		nodosP1[posCruce1] = subArb1;
		nodosP2[posCruce2] = subArb2;
		
		c1.setGenes(nodosP1);
		c2.setGenes(nodosP2);
	}
	
	private static GenArbol eligeNodo(List<GenArbol> lista) {
		Boolean valido = false;
		GenArbol nodo = null;
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
	
	private static int eligeNodo(GenArbol[] lista) {
		Boolean valido = false;
		GenArbol nodo = null;
		Random r = new Random();
		int aceptarHoja = 1;
		int posElegida = -1;
		
		while (!valido || posElegida == -1) {
			//Sacamos un nodo de la lista
			int nodoElegido1 = r.nextInt(lista.length-1); // Dado que esta en postorden, el último nodo es la raiz, lo ignoramos.
			nodo = lista[nodoElegido1];
			
			// Vemos si es operador u operando
			String dato = nodo.getValor();
			if (dato != "IF" && dato != "OR" && dato != "NOT" && dato != "AND") {
				// Si es un operando
				int result = r.nextInt(10);
				if (result < aceptarHoja) { // Solo aceptaremos operandos un 10% de las veces.
					valido = true;
					posElegida = nodoElegido1;
				}
			} else {
				// Es un operador
				valido = true;
				posElegida = nodoElegido1;
			}
		}
		
		return posElegida;
	}
}
