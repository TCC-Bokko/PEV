package es.ucm.fdi.pev.cruce;

import java.util.Random;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenEntero;

public class OXpp {
	public static void oxpp(Cromosoma c1, Cromosoma c2) {
		// Obtenemos los genes dentro del cromosoma como un array de ints.
		Gen[] g_c1 = c1.getGenes().clone();
		Gen[] g_c2 = c2.getGenes().clone();
		
		// Vemos cuantos genes contiene
		int lg = g_c1.length;					
		
		/////////////////////////////////
		// Cruce por Orden
		/////////////////////////////////
		// Copiar en cada uno de los hijos una subcadena de uno de los padres
		// mientras semantiene le orden relativo de las ciudades que aparecen en el otro padre
		
		// 1.- Elegir dos puntos de corte al azar
		//   c1 [1,2,3|4,5,6,7|8,9]
		//   c2 [4,5,2|1,8,7,6|9,3]
		// Corte en posiciones 3 y 7 (elegidos al azar)
		// Elegimos dos puntos al azar (next Int es exclusivo con el numero dado)
		Random r = new Random();
		int corte1 = r.nextInt(lg);
		int corte2 = r.nextInt(lg);
		// Ordenamos en caso de que salgan intercambiados (2 < 1)
		if (corte2 < corte1) {
			int corteaux = corte1;
			corte1 = corte2;
			corte2 = corteaux;
		}

		// 2.- Copiar los valores de la subcadena comprendida entre dichos puntos en los hijos
		// Preparamos vectores soluciones
		Gen[] hijo1 = g_c1;
		Gen[] hijo2 = g_c2;
		// Los inicializamos a valor invalido (-1)
		for (int h = 0; h < lg; h++) {
			GenEntero GEH1 = (GenEntero)hijo1[h];
			GenEntero GEH2 = (GenEntero)hijo2[h];
			GEH1.setAlelo(-1);
			GEH2.setAlelo(-1);
		}		
		// intercambio de segmentos
		for (int i = corte1; i <= corte2; i++) {
			//g_c1[i] = g_c2[i];
			//g_c2[i] = g_aux[i];
			hijo1[i] = g_c2[i];
			hijo2[i] = g_c1[i];
		}
		
		// Para los valores que faltan en los hijos se copian los valores de los padres
		// comenzando a partir de la zona copiada y respetando el orden.
		// EMPEZANDO POR EL PUNTO DE CORTE 2 DE CADA PROGENITOR
		// empezamos a rellenar la solución por la posicion de corte2 (no incluida en el intercambio)
		
		// Hijo 1 se rellena primero con padre 1 y luego con padre 2
		// Hijo 2 se rellena primero con padre 2 y luego con padre 1
		int posS1 = corte2;
		int posS2 = corte2;
		
		// PROGENITOR 1 para empezar a rellenar hijo1
		for (int j = corte2; j != corte1; j++) {
			//Control de limites
			if (j == lg) j = 0; // Si sale por la derecha del vector, lo llevamos al inicio.
			if (j == corte1) break; // por si acaso
			// Empieza en corte2-> llega al final, vuelve a empezar de 0, hasta corte1.
			
			// Obtenemos el valor en la posición J del primer padre
			GenEntero gE1 = (GenEntero)g_c1[j];
			int num_padre1 = gE1.getAlelo(); // Lo sacamos
			
			// EMPEZAR HIJO1
			// Vemos si ya existe en la cadena del hijo
			if (!isInside(num_padre1, corte1, corte2, hijo1)) {
				// Si el elemento no esta en el hijo, lo añadimos a la posición posS1 de este
				GenEntero GEh1 = (GenEntero)hijo1[posS1];
				GEh1.setAlelo(num_padre1);
				hijo1[posS1] = GEh1;
				//Avanzamos la posición de S1
				posS1 = posS1+1;
				if (posS1 == lg) posS1 = 0;
			}
			else {
				// Si un valor esta en la subcadena intercambiada, entonces se pasa al siguiente
			}
		}
		// PROGENITOR 2 para acabar hijo1 y empezar hijo2
		for (int k = corte2; k != corte1; k++) {
			//Control de limites
			if (k == lg) k = 0; // Si sale por la derecha del vector, lo llevamos al inicio.
			if (k == corte1) break; // por si acaso
			// Empieza en corte2-> llega al final, vuelve a empezar de 0, hasta corte1.
			
			// Obtenemos el valor en la posición K del segundo padre
			GenEntero gE2 = (GenEntero)g_c2[k];
			int num_padre2 = gE2.getAlelo(); // Lo sacamos
			
			// ACABAR HIJO 1
			// Vemos si ya existe en la cadena del hijo 1
			if (!isInside(num_padre2, corte1, corte2, hijo1)) {
				// Si el elemento no esta en el hijo, lo añadimos a la posición posS1 de este
				GenEntero GEh1 = (GenEntero)hijo1[posS1];
				GEh1.setAlelo(num_padre2);
				hijo1[posS1] = GEh1;
				//Avanzamos la posición de S1
				posS1 = posS1+1;
				if (posS1 == lg) posS1 = 0;
			}
			else {
				// Si un valor esta en la subcadena intercambiada, entonces se pasa al siguiente
			}
			
			// EMPEZAR HIJO 2
			if (!isInside(num_padre2, corte1, corte2, hijo2)) {
				// Si el elemento no esta en el hijo, lo añadimos a la posición posS1 de este
				GenEntero GEh2 = (GenEntero)hijo2[posS2];
				GEh2.setAlelo(num_padre2);
				hijo2[posS2] = GEh2;
				//Avanzamos la posición de S1
				posS2 = posS2+1;
				if (posS2 == lg) posS2 = 0;
			}
			else {
				// Si un valor esta en la subcadena intercambiada, entonces se pasa al siguiente
			}
		}
		// PROGENITOR 1 de nuevo para acabar de rellenar hijo2
		for (int j = corte2; j != corte1; j++) {
			//Control de limites
			if (j == lg) j = 0; // Si sale por la derecha del vector, lo llevamos al inicio.
			if (j == corte1) break; // por si acaso
			// Empieza en corte2-> llega al final, vuelve a empezar de 0, hasta corte1.
			
			// Obtenemos el valor en la posición J del primer padre
			GenEntero gE1 = (GenEntero)g_c1[j];
			int num_padre1 = gE1.getAlelo(); // Lo sacamos
			
			// EMPEZAR HIJO1
			// Vemos si ya existe en la cadena del hijo
			if (!isInside(num_padre1, corte1, corte2, hijo2)) {
				// Si el elemento no esta en el hijo, lo añadimos a la posición posS1 de este
				GenEntero GEh2 = (GenEntero)hijo2[posS2];
				GEh2.setAlelo(num_padre1);
				hijo2[posS2] = GEh2;
				//Avanzamos la posición de S1
				posS2 = posS2+1;
				if (posS2 == lg) posS2 = 0;
			}
			else {
				// Si un valor esta en la subcadena intercambiada, entonces se pasa al siguiente
			}
		}
		
		// DEVOLVEMOS LOS HIJOS GENERADOS
		c1.setGenes(hijo1);
		c2.setGenes(hijo2);	
	}
	
	//Metodo que devuelve true o false dependiendo de si el valor ya esta contenido en el cromosoma.
	public static boolean isInside(int value, int corte1, int corte2, Gen[] c) {
		// NOTA: No pasamos la posición del valor ya que comprobamos que no este dentro del hijo,
		// por lo que esta bien comprobar todas las posiciones. El hijo debio ser inicializado
		// con un valor no valido de alelo, por ejemplo -1.
		boolean isThere = false;
		int lg = c.length;	
		for (int k = 0; k < lg; k++) {
			if (k < corte1 || k > corte2) { // no comprobar las posiciones dentro del rango intercambiado
				//Vamos numero a numero comprobando si existe en el vector
				GenEntero GC1 = (GenEntero)c[k];
				int num_out1 = GC1.getAlelo();
				if (value == num_out1) isThere = true;
			}
		}
		return isThere;
	}
}
