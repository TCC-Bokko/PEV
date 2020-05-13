package es.ucm.fdi.pev.cruce;

import es.ucm.fdi.pev.estructura.Arbol;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaArbol;

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
		CromosomaArbol CA1 = (CromosomaArbol) c1;
		CromosomaArbol CA2 = (CromosomaArbol) c2;
		
		Arbol padre1 = CA1.getArbol();
		Arbol padre2 = CA2.getArbol();
		
		Arbol hijo1 =  CA1.getArbol();
		Arbol hijo2 = CA2.getArbol();
		
		////////////////////////////////////////////////////////////////////
		// ENCONTRAR NODOS DE CRUCE
		////////////////////////////////////////////////////////////////////
		// Seleccionar nodos
		
		// Obtener subarboles
		
		// Intercambiar subarboles

		
		////////////////////////////////////////////////////////////////////
		// GENERADO DE LOS HIJOS
		////////////////////////////////////////////////////////////////////
		
		/////////////////////////////
		// DEVOLVER HIJOS GENERADOS
		/////////////////////////////
		CA1.setArbol(hijo1);
		CA2.setArbol(hijo2);
		
		c1 = CA1;
		c2 = CA2;
	}
}
