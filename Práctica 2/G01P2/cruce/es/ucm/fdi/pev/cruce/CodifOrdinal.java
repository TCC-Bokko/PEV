package es.ucm.fdi.pev.cruce;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenEntero;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class CodifOrdinal {
	public static void codifOrdinal(Cromosoma c1, Cromosoma c2) {
		// Obtenemos los genes dentro del cromosoma como un array de ints.
		Gen[] padre1 = c1.getGenes().clone();
		Gen[] padre2 = c2.getGenes().clone();

		// Vemos cuantos genes contiene
		int lg = padre1.length;
		
		/////////////////////////////
		// Listas Ordenadas
		/////////////////////////////
		List<Integer> List1 = new ArrayList<Integer>();
		List<Integer> List2 = new ArrayList<Integer>();
		//Rellenando las listas
		for (int p1 = 0; p1 < padre1.length; p1++) {
			GenEntero GP1 = (GenEntero) padre1[p1];
			GenEntero GP2 = (GenEntero) padre2[p1];
			List1.add(GP1.getAlelo());
			List2.add(GP2.getAlelo());
		}
		//Ordenar las listas de modo ascendente.
		Collections.sort(List1);
		Collections.sort(List2);
		
		/////////////////////////////
		// Vectores Representativos
		/////////////////////////////
		int[] VR1 = new int[lg];
		int[] VR2 = new int[lg];
		
	}
	
	//Metodo que devuelve true o false dependiendo de si el valor ya esta contenido en el cromosoma.
	public static boolean isInside(int value, int posValue, int corte1, int corte2, Gen[] c) {
		boolean isThere = false;
		int lg = c.length;	
		for (int k = 0; k < lg; k++) {
			if (k != posValue && (k < corte1 || k > corte2)) { // no comprobar su propia posicion ni las posiciones dentro del rango intercambiado
				//Vamos numero a numero comprobando si existe en el vector
				GenEntero GC1 = (GenEntero)c[k];
				int num_out1 = GC1.getAlelo();
				if (value == num_out1) isThere = true;
			}
		}
		return isThere;
	}
}
