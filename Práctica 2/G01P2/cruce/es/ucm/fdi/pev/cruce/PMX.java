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
		// Obtenemos los genes dentro del cromosoma como un array de ints.
		Gen[] g_c1 = c1.getGenes().clone();
		Gen[] g_c2 = c2.getGenes().clone();
		Gen[] g_aux = g_c1;
		// Vemos cuantos genes contiene
		int lg = g_c1.length;
		
		// Generamos un vector booleano para marcar conflictos
		Boolean[] correcto1 = new Boolean[lg];
		Boolean[] correcto2 = new Boolean[lg];
		for (int b = 0; b < correcto1.length; b++) {
			correcto1[b] = false;
			correcto2[b] = false;
		}
			
		// Elegir dos puntos de corte al azar
		//   c1 [1,2,3|4,5,6,7|8,9]
		//   c2 [4,5,2|1,8,7,6|9,3]
		// Corte en posiciones 3 y 7 (elegidos al azar)
		// Elegimos dos puntos al azar
		Random r = new Random();
		int corte1 = r.nextInt(lg);
		int corte2 = r.nextInt(lg);
		
		//Ordenamos
		if (corte2 < corte1) {
			int corteaux = corte1;
			corte1 = corte2;
			corte2 = corteaux;
		}
			
		// Se intercambian los segmentos sin que importe lo de fuera
		//   c1 [x,x,x|1,8,7,6|x,x]
		//   c2 [x,x,x|4,5,6,7|x,x]
		// corr [F,F,F|T,T,T,T|F,F]
		for (int i = corte1; i <= corte2; i++) {
			g_c1[i] = g_c2[i];
			g_c2[i] = g_aux[i];
			//Vector de comprobación
			correcto1[i] = true;
			correcto2[i] = true;
		}
		
		//[WORK IN PROGRESS]
		// Se espeficican las posiciones de los progenitores que
		// NO plantean conflicto 
		//   c1 [x,2,3|1,8,7,6|x,9]
		//   c2 [x,x,2|4,5,6,7|9,3]
		for (int j = 0; j < lg; j++) {
			if (j < corte1 || j > corte2) {
				// Chequeo en c1
				GenEntero gE1 = (GenEntero)g_c1[j];
				int num_check1 = gE1.getAlelo(); // Lo sacamos
				// Chequeo en c2
				GenEntero gE2 = (GenEntero)g_c1[j];
				int num_check2 = gE2.getAlelo();
				
				boolean isThere1 = false;
				boolean isThere2 = false;
				for (int k = 0; k < lg; k++) {
					if (k != j && (k < corte1 || k > corte2)) { // no comprobar su propia posicion ni las posiciones dentro del rango intercambiado
						//Vamos numero a numero comprobando si existe en el vector
						GenEntero GC1 = (GenEntero)g_c1[k];
						GenEntero GC2 = (GenEntero)g_c2[k];
						int num_out1 = GC1.getAlelo();
						int num_out2 = GC2.getAlelo();
						
						if (num_check1 == num_out1) isThere1 = true;
						if (num_check2 == num_out2) isThere2 = true;
						k = k+1;
					}
				}
				
				//J solo recorre valores fuera de el rango intercambiado, no debería cambiar
				// los valores de correcto en ese rango.
				if (isThere1) correcto1[j] = false;
				else correcto1[j] = true;
				if (isThere2) correcto2[j] = false;
				else correcto2[j] = true;
				
				// Se intercambian aquellos que generan conflicto
				// ANTES DEL CAMBIO
				//   c1 [1,2,3|1,8,7,6|8,9]
				// cor1 [F,T,T|T,T,T,T|F,T] correcto mria dentro del propio vector si hay conflicto.
				//   c2 [4,5,2|4,5,6,7|9,3]
				// cor2 [F,F,T|T,T,T,T|T,T]
				 
				// Tendremos el mismo número de falsos en correcto1 y correcto2, solo tenemos
				// que intercambiar las posiciones donde falle por orden.
				for (int p = 0; p < correcto1.length; p++) {
					if (!correcto1[p]) { //Si encuentra un falso en la posicion c
						//Buscar la primera posición de correcto2 con un falso
						int p2 = 0;
						while (correcto2[p2] && p2 < lg) {
							p2 = p2 + 1;
						}
						//Al salir del bucle tenemos la primera posición con otro falso.
						GenEntero GC1 = (GenEntero)g_c1[p];
						GenEntero GC2 = (GenEntero)g_c2[p2];
						int valor1 = GC1.getAlelo();
						int valor2 = GC2.getAlelo();
						//Ver que no sea el mismo número
						if (valor1 != valor2) {
							// los intercambiamos
							g_c1[p] = GC2;
							g_c2[p2] = GC1;
						} 
						//Si es el mismo... ¿Puede ser el mismo?
						else {
							//Creo que no puede ser el mismo, si estan los dos en el mismo array es por que no existe en el otro.
						}
					}
				}
				// PRIMER CAMBIO (1 por 4)		
				//   c1 [4,2,3|1,8,7,6|x,9]
				//   c2 [1,x,2|4,5,6,7|9,3]
				// SEGUNDO CAMBIO (8 por 5)
				//   c1 [4,2,3|1,8,7,6|5,9]
				//   c2 [1,8,2|4,5,6,7|9,3]				
			}
		}
			
		// Devolvemos los hijos generados
		c1.setGenes(g_c1);
		c2.setGenes(g_c2);				
	}
	
}
