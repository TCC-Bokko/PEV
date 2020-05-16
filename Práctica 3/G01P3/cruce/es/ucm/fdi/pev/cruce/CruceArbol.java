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
		// Arbol completo
		CromosomaP3 CA1 = (CromosomaP3) c1;
		CromosomaP3 CA2 = (CromosomaP3) c2;
		// DEBUG
		System.out.println("__PADRE_1__");
		System.out.printf("Profundidad ANTES cruce: %d\n", CA1.getProfInd());
		System.out.printf("Nodos Arbol ANTES cruce: %d\n", CA1.getNodosInd()); //List<Arbol> usa .size()
		System.out.println(CA1.fenotipoArbol());
		System.out.println("__PADRE_2__");
		System.out.printf("Profundidad ANTES cruce: %d\n", CA2.getProfInd());
		System.out.printf("Nodos Arbol ANTES cruce: %d\n", CA2.getNodosInd());
		System.out.println(CA2.fenotipoArbol());
		
		List<GenArbol> nodosP1 = CA1.getListaNodos();
		List<GenArbol> nodosP2 = CA2.getListaNodos();
		
		////////////////////////////////////////////////////////////////////
		// ENCONTRAR NODOS DE CRUCE
		////////////////////////////////////////////////////////////////////
		// Seleccionar nodos
		// Elegir al azar del padre1
		int posCruce1 = eligeNodo(nodosP1);
		int posCruce2 = eligeNodo(nodosP2);
		
		// Usando Lista nodos
		GenArbol subArb1 = nodosP1.get(posCruce1);
		GenArbol subArb2 = nodosP2.get(posCruce2);
		GenArbol aux = new GenArbol();
		
		// Intercambiar Subarboles
		GenArbol padreSubArbol1 = subArb1.getPadre();
		GenArbol padreSubArbol2 = subArb2.getPadre();
		
		
		// Poner arbol2 en arbol1
		if (padreSubArbol1 != null) {
			// Intercambio en P1 si es tipo Operador
			if (padreSubArbol1.getHi() == subArb1) {
				padreSubArbol1.setHI(subArb2);
			} else if (padreSubArbol1.getHc() == subArb1) {
				padreSubArbol1.setHC(subArb2);
			} else if (padreSubArbol1.getHd() == subArb1) {
				padreSubArbol1.setHD(subArb2);
			} else {
				System.out.println("[CRUCEARBOL] Ningun hijo del padreSubArbol1 es el subArbol1. Fallo al intercambiar subarboles.");
			}
			subArb1.setPadre(padreSubArbol2);
		} else {
			// Si el padre de Arbol1 es null, es la raiz del arbol.
			aux = subArb1;
			subArb1.setPadre(null);
			CA1.setArbol(subArb2);
		}
		
		// Poner arbol 1 en arbol2
		if (padreSubArbol2 != null) {
			// Intercambio en P2 si es operador
			if (padreSubArbol2.getHi() == subArb2) {
				padreSubArbol2.setHI(subArb1);
			} else if (padreSubArbol2.getHc() == subArb2) {
				padreSubArbol2.setHC(subArb1);
			} else if (padreSubArbol2.getHd() == subArb2) {
				padreSubArbol2.setHD(subArb1);
			} else {
				System.out.println("[CRUCEARBOL] Ningun hijo del padreSubArbol1 es el subArbol1. Fallo al intercambiar subarboles.");
			}
			subArb2.setPadre(padreSubArbol1);
		} else {
			subArb1.setPadre(null);
			CA2.setArbol(subArb1);
			
		}
		
		////////////////////////////////////////////////////////////////////
		// Devolvemos hijos
		///////////////////////////////////////////////////////////////////
		
		// Usando Lista nodos
		nodosP1.set(posCruce1, subArb1);
		nodosP2.set(posCruce2, subArb2);
		CA1.setListaNodos(nodosP1);
		CA2.setListaNodos(nodosP2);
		CA1.actualizaArbol();
		CA2.actualizaArbol();
		c1 = CA1;
		c2 = CA2;
		
		////////////////////////////////////////////////////////////////////
		// Actualizamos listas de nodos
		////////////////////////////////////////////////////////////////////
		System.out.println("__HIJO_1__");
		System.out.printf("Posicion de cruce: %d\n", posCruce1);
		System.out.printf("Profundidad Despues cruce: %d\n", CA1.getProfInd());
		System.out.printf("Nodos Arbol Despues cruce: %d\n", CA1.getNodosInd()); //List<Arbol> usa .size()
		System.out.println(CA1.fenotipoArbol());
		System.out.println("__HIJO_2__");
		System.out.printf("Posicion de cruce: %d\n", posCruce2);
		System.out.printf("Profundidad Despues cruce: %d\n", CA2.getProfInd());
		System.out.printf("Nodos Arbol Despues cruce: %d\n", CA2.getNodosInd());
		System.out.println(CA2.fenotipoArbol());
	}
	
	private static int eligeNodo(List<GenArbol> lista) {
		Boolean valido = false;
		GenArbol nodo = null;
		Random r = new Random();
		int posElegida = -1;
		int aceptarHoja = 1;
		
		while (!valido || posElegida == -1) {
			//Sacamos un nodo de la lista
			int nodoElegido1;
			// Dado que esta en postorden, el último nodo es la raiz, lo ignoramos.
			if (lista.size() > 1) nodoElegido1 = r.nextInt(lista.size()-1);
			// Si solo tiene un nodo, será un operando, así que el nodo elegido es el 0 (raiz).
			else nodoElegido1 = 0;
			nodo = lista.get(nodoElegido1);
			
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

	private static boolean esOperador(GenArbol arbol) {
		String valor = arbol.getValor();
		if (valor == "OR" || valor == "AND" || valor == "NOT" || valor == "IF") return true;
		else return false;
	}
}
