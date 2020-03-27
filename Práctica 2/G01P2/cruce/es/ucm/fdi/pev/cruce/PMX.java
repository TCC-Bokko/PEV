package es.ucm.fdi.pev.cruce;

import java.util.Arrays;
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
		boolean debug = false;
		
		// Obtenemos los genes dentro del cromosoma como un array de ints.
		Gen[] g_c1 = c1.getGenes().clone();
		//Gen[] g_aux = c1.getGenes().clone();
		Gen[] g_c2 = c2.getGenes().clone();
		
		// Vemos cuantos genes contiene  
		int lg = g_c1.length;
		
		//DEBUGEO ////////////////////////////////////////////////////////////////////////////
			// Variables debugeo
			int[] V1 = new int[lg];
			int[] V2 = new int[lg];
			//int[] Vaux = new int[lg];
			for (int dc = 0; dc < lg; dc++) {
				GenEntero GE1 = (GenEntero) g_c1[dc];
				GenEntero GE2 = (GenEntero) g_c2[dc];
				//GenEntero Gaux = (GenEntero) g_aux[dc];
				V1[dc] = GE1.getAlelo();
				V2[dc] = GE2.getAlelo();
				//Vaux[dc] = Gaux.getAlelo();
			}
			System.out.println("\nINICIALIZACION DE CRUCE");
			System.out.printf("Cromosoma PADRE 1: ");
			System.out.println(Arrays.toString(V1));
			System.out.printf("Cromosoma PADRE 2: ");
			System.out.println(Arrays.toString(V2));
			//System.out.println("Cromosoma aux:");
			//System.out.println(Arrays.toString(Vaux));
		//FIN DEBUGEO ////////////////////////////////////////////////////////////////////////////
		
		// Generamos un vector booleano para marcar conflictos
		Boolean[] correcto1 = new Boolean[lg];
		Boolean[] correcto2 = new Boolean[lg];
		for (int b = 0; b < correcto1.length; b++) {
			correcto1[b] = false;
			correcto2[b] = false;
		}
		
		//DEBUGEO ////////////////////////////////////////////////////////////////////////////
		if (debug) {
			System.out.printf("Correcto 1: ");
			System.out.println(Arrays.toString(correcto1));
			System.out.printf("Correcto 2: ");
			System.out.println(Arrays.toString(correcto2));
			System.out.println("DEBEN SER TODO FALSE");
		}
		//FIN DEBUGEO ////////////////////////////////////////////////////////////////////////////
			
		// Elegir dos puntos de corte al azar
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
		System.out.printf("Corte en el rango: [%d,%d]\n", corte1, corte2);
			
		// Se intercambian los segmentos sin que importe lo de fuera
		//   c1 [x,x,x|1,8,7,6|x,x]
		//   c2 [x,x,x|4,5,6,7|x,x]
		// corr [F,F,F|T,T,T,T|F,F]
		for (int i = corte1; i <= corte2; i++) {
			// INTERCAMBIO a lo bestia
			GenEntero GC1I = (GenEntero)g_c1[i];
			GenEntero GC2I = (GenEntero)g_c2[i];
			int valor1 = GC1I.getAlelo();
			int valor2 = GC2I.getAlelo();
			GC1I.setAlelo(valor2);
			GC2I.setAlelo(valor1);
			g_c1[i] = GC1I;
			g_c2[i] = GC2I;
			
			//Vector de comprobación
			correcto1[i] = true;
			correcto2[i] = true;
		}
		
		//DEBUGEO ////////////////////////////////////////////////////////////////////////////
		if (debug) {
			System.out.println("\nINTERCAMBIO");	
			for (int dc = 0; dc < lg; dc++) {
				GenEntero GE1 = (GenEntero) g_c1[dc];
				GenEntero GE2 = (GenEntero) g_c2[dc];
				V1[dc] = GE1.getAlelo();
				V2[dc] = GE2.getAlelo();
			}
			System.out.printf("Cromosoma 1: ");
			System.out.println(Arrays.toString(V1));
			System.out.printf("Cromosoma 2: ");
			System.out.println(Arrays.toString(V2));
			System.out.printf("Correcto 1: ");
			System.out.println(Arrays.toString(correcto1));
			System.out.printf("Correcto 2: ");
			System.out.println(Arrays.toString(correcto2));
		}
		//FIN DEBUGEO ////////////////////////////////////////////////////////////////////////////
		
		// Se espeficican las posiciones de los progenitores que
		// NO plantean conflicto 
		//   c1 [x,2,3|1,8,7,6|x,9]
		//   c2 [x,x,2|4,5,6,7|9,3]
		// corr1[F,F,F|T,T,T,T|F,F] 
		// corr2[F,F,F|T,T,T,T|F,F]
		//Desde el inicio hasta el final
		if(debug)System.out.println("\nVER CONFLICTOS");	
		for (int j = 0; j < lg; j++) {
			// En aquellas posiciones fuera del rango intercambiado
			if (j < corte1 || j > corte2) {
				if(debug)System.out.printf("\nJ esta fuera del rango intercambiado (");
				if(debug)System.out.printf("j = %d)\n", j);
				// Vemos el valor en esta posición del primer cromosoma 
				GenEntero gE1 = (GenEntero)g_c1[j];
				int num_check1 = gE1.getAlelo(); // Lo sacamos
				if(debug)System.out.printf("Valor C1[j]: %d\n", num_check1);
				// Vemos el valor en esta posición del segundo cromosoma 
				GenEntero gE2 = (GenEntero)g_c2[j];
				int num_check2 = gE2.getAlelo();
				if(debug)System.out.printf("Valor C2[j]: %d\n", num_check2);
				// QUEREMOS VER PARALELAMENTE EN AMBOS CROMOSOMAS QUE POSICIONES
				// FUERA DEL RANGO TIENEN VALORES QUE CREAN CONFLICTO CON LOS DE DENTRO
				// SI EL VALOR EN CUESTION NO SE REPITE
				if (!isInside(num_check1, j, corte1, corte2, g_c1)) {
					if(debug)System.out.printf("El valor %d NO esta en la sección intercambiada de C1\n", num_check1);
					correcto1[j] = true;
				} else {
					if(debug)System.out.printf("El valor %d SI esta en la sección intercambiada de C1\n", num_check1);
				}
				if (!isInside(num_check2, j, corte1, corte2, g_c2)) {
					if(debug)System.out.printf("El valor %d NO esta en la sección intercambiada de C2\n", num_check2);
					correcto2[j] = true;
				}else {
					if(debug)System.out.printf("El valor %d SI esta en la sección intercambiada de C2\n", num_check2);
				}
					// Se intercambian aquellos que generan conflicto
				// ANTES DEL CAMBIO (Solucion conflicos)
				//   c1 [1,2,3|1,8,7,6|8,9]
				// cor1 [F,T,T|T,T,T,T|F,T] correcto mira dentro del propio vector si hay conflicto.
				//   c2 [4,5,2|4,5,6,7|9,3]
				// cor2 [F,F,T|T,T,T,T|T,T]
				if(debug) {
					System.out.printf("Correcto 1: ");
					System.out.println(Arrays.toString(correcto1));
					System.out.printf("Correcto 2: ");
					System.out.println(Arrays.toString(correcto2));
				}
				
			}
		}
		
		
		if(debug)System.out.println("\nRESOLVER CONFLICTOS");	
		// Tendremos el mismo número de falsos en correcto1 y correcto2, solo tenemos
		// que intercambiar las posiciones donde falle por orden.
		//RECORREMOS EL PRIMER VECTOR
		for (int p1 = 0; p1 < correcto1.length; p1++) {
			//DONDE SE ENCUENTRA UN FALSE
			if (!correcto1[p1]) {
				if(debug)System.out.printf("Encontrado false en correcto1 en la posicion p1 = %d \n", p1);
				//Buscar la primera posición de correcto2 con un falso
				int p2 = 0;
				while (correcto2[p2] && (p2 < correcto2.length)) {
					p2 = p2 + 1;
				}
				if(debug)System.out.printf("Encontrado false en correcto2 en la posicion p2 = %d \n", p2);
				
				//Intercambiamos los valores de C1 y C2
				GenEntero GC1 = (GenEntero)g_c1[p1];
				GenEntero GC2 = (GenEntero)g_c2[p2];
				int valor1 = GC1.getAlelo();
				int valor2 = GC2.getAlelo();
				
				//Ver que no sea el mismo número
				if (valor1 != valor2) {
					// los intercambiamos
					GC1.setAlelo(valor2);
					GC2.setAlelo(valor1);
					// Actualizamos
					g_c1[p1] = GC1;
					g_c2[p2] = GC2;
				} 
				//Si es el mismo... ¿Puede ser el mismo?
				else {
					System.out.println("Cruce PMX: Error. Revisar RESOLVER CONFLICTOS.");
				}
			}
		}
		
		// PRIMER CAMBIO (1 por 4)		
		//   c1 [4,2,3|1,8,7,6|x,9]
		//   c2 [1,x,2|4,5,6,7|9,3]
		// SEGUNDO CAMBIO (8 por 5)
		//   c1 [4,2,3|1,8,7,6|5,9]
		//   c2 [1,8,2|4,5,6,7|9,3]		

		
		//DEBUGEO ////////////////////////////////////////////////////////////////////////////
		// Variables debugeo
		for (int dc = 0; dc < lg; dc++) {
			GenEntero GE1 = (GenEntero) g_c1[dc];
			GenEntero GE2 = (GenEntero) g_c2[dc];
			//GenEntero Gaux = (GenEntero) g_aux[dc];
			V1[dc] = GE1.getAlelo();
			V2[dc] = GE2.getAlelo();
			//Vaux[dc] = Gaux.getAlelo();
		}
		if(debug)System.out.printf("\nRESULTADO\n");
		System.out.printf("Cromosoma HIJO 1: ");
		System.out.println(Arrays.toString(V1));
		System.out.printf("Cromosoma HIJO 2: ");
		System.out.println(Arrays.toString(V2));
		//System.out.println("Cromosoma aux:");
		//System.out.println(Arrays.toString(Vaux));
		System.out.println("FIN DE CRUCE\n");
		//FIN DEBUGEO ////////////////////////////////////////////////////////////////////////////
		
		// Devolvemos los hijos generados
		c1.setGenes(g_c1);
		c2.setGenes(g_c2);				
	}
	
	//Metodo que devuelve true o false dependiendo de si el valor ya esta contenido en la parte intercambiada cromosoma.
	//Fuera es imposible ya que antes del intercambio nunca va a repetirse
	public static boolean isInside(int value, int posValue, int corte1, int corte2, Gen[] c) {
		boolean isThere = false;
		int lg = c.length;
		//Desde corte1 a corte2 inclusive
		for (int k = corte1; k < corte2+1; k++) {
				//Vamos numero a numero comprobando si existe en el vector
				GenEntero GC1 = (GenEntero)c[k];
				int num_out1 = GC1.getAlelo();
				if (value == num_out1) isThere = true;
		}
		return isThere;
	}
}
