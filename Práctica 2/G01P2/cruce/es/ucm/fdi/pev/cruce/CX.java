package es.ucm.fdi.pev.cruce;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenEntero;

public class CX {
	public static void cx(Cromosoma c1, Cromosoma c2) {
		// Obtenemos los genes dentro del cromosoma como un array de ints.
		Gen[] g_c1 = c1.getGenes().clone();
		Gen[] g_c2 = c2.getGenes().clone();
		Gen[] g_aux = g_c1;
		// Vemos cuantos genes contiene
		int lg = g_c1.length;
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
