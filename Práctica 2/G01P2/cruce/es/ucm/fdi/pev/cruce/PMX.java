package es.ucm.fdi.pev.cruce;

import java.util.Random;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenEntero;

public class PMX {
	// Cruce de emparejamiento parcial (PMX) -> Pag 38 tema 6 PEV.
	// Cromosomas con vectores de enteros (Caminos)
	// Ejemplo:
	//		c1  [1,2,3,4,5,6,7,8,9]
	//		c2  [4,5,2,1,8,7,6,9,3]
	public static void pmx(Cromosoma c1, Cromosoma c2) {
		// Obtenemos los genes y generamos uno auxiliar
		Gen[] g_c1 = c1.getGenes().clone();
		Gen[] g_c2 = c2.getGenes().clone();
		// Vemos cuantos genes contiene
		int lg = g_c1.length;
		
		// Generamos un vector booleano para marcar conflictos
		Boolean[] correcto = new Boolean[lg];
		for (int b = 0; b < correcto.length; b++) {
			correcto[b] = false;
		}
		
		//por cada gen en el cromosoma
		for (int g = 0; g < lg; g++) {
			//Obtenemos los genes correspondientes
			GenEntero gen1 = (GenEntero)g_c1[g];
			GenEntero gen2 = (GenEntero)g_c2[g];
			int[] alelos1 = gen1.getAlelo();
			int[] alelos2 = gen2.getAlelo();
			int[] alelosAux = alelos1;
			
			//Vemos el tamaño del gen
			int tamGen = alelos1.length;			
			
			// Elegir dos puntos de corte al azar
			//   c1 [1,2,3|4,5,6,7|8,9]
			//   c2 [4,5,2|1,8,7,6|9,3]
			// Corte en posiciones 3 y 7 (elegidos al azar)
			//Elegimos dos puntos al azar
			Random r = new Random();
			int corte1 = r.nextInt(tamGen);
			int corte2 = r.nextInt(tamGen);
			//Ordenamos
			if (corte2 < corte1) {
				int corteaux = corte1;
				corte1 = corte2;
				corte2 = corteaux;
			}
			
			// Se intercambian los segmentos sin que importe lo de fuera
			//   c1 [x,x,x|1,8,7,6|x,x]
			//   c2 [x,x,x|4,5,6,7|x,x]
			for (int i = corte1; i <= corte2; i++) {
				alelos1[i] = alelos2[i];
				alelos2[i] = alelosAux[i];
			}
			
			// Se espeficican las posiciones de los progenitores que
			// NO plantean conflicto
			//   c1 [x,2,3|1,8,7,6|x,9]
			//   c2 [x,x,2|4,5,6,7|9,3]
			for (int j = 0; j < g_c1.length; j++) {
				if (j < corte1 || j > corte2) {
					
					/// WORK IN PROGRESS//
					int value1 = alelos1[j];
					int value2 = alelos2[j];
				}
			}
			
			// Se intercambian aquellos que generan conflicto
			// ANTES DEL CAMBIO
			//   c1 [1,2,3|1,8,7,6|8,9]
			//   c2 [4,5,2|4,5,6,7|9,3]
			// PRIMER CAMBIO (1 por 4)		
			//   c1 [4,2,3|1,8,7,6|x,9]
			//   c2 [1,x,2|4,5,6,7|9,3]
			// SEGUNDO CAMBIO (8 por 5)
			//   c1 [4,2,3|1,8,7,6|5,9]
			//   c2 [1,8,2|4,5,6,7|9,3]
			
			// Devolvemos los hijos generados

		}
				
	}
	
}
