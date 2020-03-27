package es.ucm.fdi.pev.cruce;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenEntero;

public class CX {
	private static int posFind = -1;
	
	public static void cx(Cromosoma c1, Cromosoma c2) {
		// Obtenemos los genes dentro del cromosoma como un array de ints.
		Gen[] padre1 = c1.getGenes().clone();
		Gen[] padre2 = c2.getGenes().clone();
		// Vemos cuantos genes contiene
		int lg = padre1.length;
		// Preparamos vectores soluciones
		Gen[] hijo1 = padre1;
		Gen[] hijo2 = padre2;
		// Los inicializamos a valor invalido (-1)
		for (int h = 0; h < lg; h++) {
			GenEntero GEH1 = (GenEntero)hijo1[h];
			GenEntero GEH2 = (GenEntero)hijo2[h];
			GEH1.setAlelo(-1);
			GEH2.setAlelo(-1);
		}	
		
		/////////////////////
		//  HIJO 1
		/////////////////////
		//Ciclo 1: Empezando por el primer elemento del padre1
		int posCiclo = 0;
		int valorHijo = -1;
		while (valorHijo == -1) {
			//Primer descendiente toma primer valor del primer padre
			hijo1[posCiclo] = padre1[posCiclo];
			// Vemos el valor el segundo padre en esa posición
			GenEntero GP2 = (GenEntero) padre2[posCiclo];
			int valor = GP2.getAlelo();
			// Buscamos ese valor en el padre1 y al repetir bucle se almacenara en hijo.
			if (isInside(valor, padre1)) {
				//Actualizamos posición ciclo a la siguiente
				posCiclo = posFind;
				//Actualizamos condicion de corte
				GenEntero GH1 = (GenEntero) hijo1[posCiclo];
				valorHijo = GH1.getAlelo();
			}
			else {
				// no debería entrar aqui nunca
				System.out.println("Cruce CX: Error. Revisar Ciclo Hijo 1.");
			}
		}
		//Una vez cerrado el ciclo se rellenan el resto de ciudades con el otro padre
		for (int h1 = 0; h1 < lg; h1++) {
			GenEntero GH1 = (GenEntero) hijo1[h1];
			int valorH1 = GH1.getAlelo();
			// Si no se ha rellenado esta posición, se le da el valor del padre2
			if (valorH1 == -1) {
				GenEntero GP2 = (GenEntero) padre2[h1];
				int valorP2 = GP2.getAlelo();
				GH1.setAlelo(valorP2);
				hijo1[h1] = GH1;
			}
		}
		
		/////////////////////
		//  HIJO 2
		/////////////////////
		//Ciclo 2: Empezando por el primer elemento del padre2
		int posCiclo2 = 0;
		int valorHijo2 = -1;
		while (valorHijo2 == -1) {
			//Primer descendiente toma primer valor del primer padre
			hijo2[posCiclo2] = padre2[posCiclo2];
			// Vemos el valor el segundo padre en esa posición
			GenEntero GP1 = (GenEntero) padre1[posCiclo2];
			int valor = GP1.getAlelo();
			// Buscamos ese valor en el padre1 y al repetir bucle se almacenara en hijo.
			if (isInside(valor, padre2)) {
				//Actualizamos posición ciclo a la siguiente
				posCiclo = posFind;
				//Actualizamos condicion de corte
				GenEntero GH2 = (GenEntero) hijo2[posCiclo];
				valorHijo2 = GH2.getAlelo();
			}
			else {
				// no debería entrar aqui nunca
				System.out.println("Cruce CX: Error. Revisar Ciclo Hijo 2.");
			}
		}
		//Una vez cerrado el ciclo se rellenan el resto de ciudades con el otro padre
		for (int h2 = 0; h2 < lg; h2++) {
			GenEntero GH2 = (GenEntero) hijo2[h2];
			int valorH2 = GH2.getAlelo();
			// Si no se ha rellenado esta posición, se le da el valor del padre2
			if (valorH2 == -1) {
				GenEntero GP1 = (GenEntero) padre1[h2];
				int valorP1 = GP1.getAlelo();
				GH2.setAlelo(valorP1);
				hijo2[h2] = GH2;
			}
		}
	}
	
	//Metodo que devuelve true o false dependiendo de si el valor ya esta contenido en el cromosoma.
	public static boolean isInside(int value, Gen[] c) {
		boolean isThere = false;
		int lg = c.length;	
		for (int k = 0; k < lg; k++) {
			GenEntero GC1 = (GenEntero)c[k];
			int num_out1 = GC1.getAlelo();
			if (value == num_out1) {
				isThere = true;
				posFind = k;
			}
		}
		return isThere;
	}
}
