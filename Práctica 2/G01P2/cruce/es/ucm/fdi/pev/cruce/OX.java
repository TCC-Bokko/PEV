package es.ucm.fdi.pev.cruce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenEntero;

public class OX {
	public static void ox(Cromosoma c1, Cromosoma c2) {
		//Variables debugueo para mostrar mensajes en consola
		boolean debug = true;
		boolean fulldebug = false;
		
		
		////////////////////////////////////////////////////////////////////
		// CARGA DE LOS PADRES (OK)
		////////////////////////////////////////////////////////////////////
		// Obtenemos los genes dentro del cromosoma como un array de ints.
		Gen[] padre1 = c1.getGenes().clone();
		Gen[] padre2 = c2.getGenes().clone();
		
		// Vemos cuantos genes contiene
		int lg = padre1.length;
		if(debug) System.out.println("\nNUEVO CRUCE OX");
		
		// Vector de marcados: Marca si el valor int esta dentro del hijo
		boolean[] usadoH1 = new boolean[lg];
		boolean[] usadoH2 = new boolean[lg];
		// Inicializamos a false
		for (int i = 0; i < usadoH1.length; i++) {
			usadoH1[i] = false;
			usadoH2[i] = false;
		}
				
		
		///////////////DEBUG PADRES
		if (fulldebug) {
			// Variables debugeo para mostrar padres e hijos
			int[] P1 = new int[lg];
			int[] P2 = new int[lg];
			for (int dc = 0; dc < lg; dc++) {
				GenEntero GP1 = (GenEntero) padre1[dc];
				GenEntero GP2 = (GenEntero) padre2[dc];
				P1[dc] = GP1.getAlelo();
				P2[dc] = GP2.getAlelo();
			}
			System.out.println("\nDATOS ENTRADA PADRES");
			System.out.printf("Cromosoma PADRE 1: ");
			System.out.println(Arrays.toString(P1));
			System.out.printf("Cromosoma PADRE 2: ");
			System.out.println(Arrays.toString(P2));
		}		
		///////////////////////
				
		////////////////////////////////////////////////////////////////////
		// ELEGIR PUNTOS DE CORTE (OK)
		////////////////////////////////////////////////////////////////////		
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
		if (debug) {
			System.out.printf("Corte en sección: [%d, %d]\n", corte1, corte2);
		}

		
		////////////////////////////////////////////////////////////////////
		// GENERADO DE LOS HIJOS (OK)
		////////////////////////////////////////////////////////////////////
		Gen[] hijo1 = new GenEntero[lg];
		Gen[] hijo2 = new GenEntero[lg];
		ArrayList<GenEntero> aux1 = new ArrayList<GenEntero>();
		ArrayList<GenEntero> aux2 = new ArrayList<GenEntero>();
		for(int i = 0; i < lg; i++)
		{
			GenEntero g = new GenEntero(-1);
			GenEntero h = new GenEntero(-1);
			aux1.add(g);
			aux2.add(h);
		}
		aux1.toArray(hijo1);
		aux2.toArray(hijo2);
		
		///////////////DEBUG
		if (fulldebug) {
			// Variables debugeo para mostrar padres e hijos
			int[] P1 = new int[lg];
			int[] P2 = new int[lg];
			int[] H1 = new int[lg];
			int[] H2 = new int[lg];
			for (int dc = 0; dc < lg; dc++) {
				GenEntero GP1 = (GenEntero) padre1[dc];
				GenEntero GP2 = (GenEntero) padre2[dc];
				GenEntero GH1 = (GenEntero) hijo1[dc];
				GenEntero GH2 = (GenEntero) hijo2[dc];
				P1[dc] = GP1.getAlelo();
				P2[dc] = GP2.getAlelo();
				H1[dc] = GH1.getAlelo();
				H2[dc] = GH2.getAlelo();
			}
			System.out.println("\nHIJOS GENERADOS");
			System.out.printf("Cromosoma PADRE 1: ");
			System.out.println(Arrays.toString(P1));
			System.out.printf("Cromosoma PADRE 2: ");
			System.out.println(Arrays.toString(P2));
			System.out.printf("Cromosoma  HIJO 1: ");
			System.out.println(Arrays.toString(H1));
			System.out.printf("Cromosoma  HIJO 2: ");
			System.out.println(Arrays.toString(H2));
		}
		///////////////
		
		////////////////////////////////////////////////////////////////////
		// INTERCAMBIO DE SEGMENTOS (OK)
		////////////////////////////////////////////////////////////////////
		// 2.- Copiar los valores de la subcadena comprendida entre dichos puntos en los hijos
		for (int i = corte1; i <= corte2; i++) {
			GenEntero GP1 = (GenEntero) padre1[i];
			GenEntero GP2 = (GenEntero) padre2[i];
			int valp1 = GP1.getAlelo();
			int valp2 = GP2.getAlelo();
			GenEntero GHij1 = (GenEntero) hijo1[i];
			GenEntero GHij2 = (GenEntero) hijo2[i];
			GHij1.setAlelo(valp2); //INTERCAMBIO AQUI
			GHij2.setAlelo(valp1);
			hijo1[i] = GHij1;
			hijo2[i] = GHij2;
			usadoH1[valp2] = true;
			usadoH2[valp1] = true;
		}
		
		///////////////DEBUG
		if (fulldebug) {
			// Variables debugeo para mostrar padres e hijos
			int[] P1 = new int[lg];
			int[] P2 = new int[lg];
			int[] H1 = new int[lg];
			int[] H2 = new int[lg];
			for (int dc = 0; dc < lg; dc++) {
				GenEntero GP1 = (GenEntero) padre1[dc];
				GenEntero GP2 = (GenEntero) padre2[dc];
				GenEntero GH1 = (GenEntero) hijo1[dc];
				GenEntero GH2 = (GenEntero) hijo2[dc];
				P1[dc] = GP1.getAlelo();
				P2[dc] = GP2.getAlelo();
				H1[dc] = GH1.getAlelo();
				H2[dc] = GH2.getAlelo();
			}
			System.out.printf("\nELEMENTOS INTERCAMBIADOS entre posiciones [%d, %d]\n", corte1, corte2);
			System.out.printf("Cromosoma PADRE 1: ");
			System.out.println(Arrays.toString(P1));
			System.out.printf("Cromosoma PADRE 2: ");
			System.out.println(Arrays.toString(P2));
			System.out.printf("Cromosoma  HIJO 1: ");
			System.out.println(Arrays.toString(H1));
			System.out.printf("Cromosoma  HIJO 2: ");
			System.out.println(Arrays.toString(H2));
		}	
		
		
		////////////////////////////////////////////////////////////////////
		// RELLENADO DE POSICIONES (FALLA)
		////////////////////////////////////////////////////////////////////
		// Para los valores que faltan en los hijos se copian los valores de los padres
		// comenzando a partir de la zona copiada y respetando el orden. Elegir elementos
		// que no generen conflicto
		int falsosEnH1 = checkFalsos(usadoH1);
		if (falsosEnH1 != 0) {  //Si no se han intercambiado todas las posiciones
			
			if(fulldebug)System.out.println("\nRELLENADO DE POSICIONES");
			
			int posP1 = corte2;
			int posP2 = corte2;
			int posH1 = corte2+1;
			if (posH1 >= lg) posH1 = 0;
			int posH2 = corte2+1;
			if (posH2 >= lg) posH2 = 0;
			
			//Necesitamos comprobar que el siguiente al corte no sea un corte previo, corte inicio y final-> siguiente al final es el del inicio
			// Comprobamos que contenga un -1
			GenEntero GH1p = (GenEntero) hijo1[posH1];
			int valor1 = GH1p.getAlelo();
			while (valor1 != -1) {
				posH1++;
				if (posH1 >= lg) posH1 = 0;
				GH1p = (GenEntero) hijo1[posH1];
				valor1 = GH1p.getAlelo();
			}
			GenEntero GH2p = (GenEntero) hijo2[posH2];
			int valor2 = GH2p.getAlelo();
			while (valor2 != -1) {
				posH2++;
				if (posH2 >= lg) posH2 = 0;
				GH2p = (GenEntero) hijo2[posH2];
				valor2 = GH2p.getAlelo();
			}
			
			if(fulldebug) { 
				int[] H1 = new int[lg];
				for (int dc = 0; dc < lg; dc++) {
					GenEntero GH1 = (GenEntero) hijo1[dc];
					H1[dc] = GH1.getAlelo();
				}
				System.out.printf("\nHIJO1 PREV RELLENO: ");
				System.out.println(Arrays.toString(H1));
			}
			// PROGENITOR 1 para empezar a rellenar hijo1
			// posP1 empieza en la posición siguiente a lastCut, luego da la vuelta y acaba el bucle al llegar a la misma posición
			// Necesitamos un DO-WHILE PARA QUE RECORRA TODA LA VUELTA INCLUYENDO EL ÚLTIMO
			// 
		
			do {
				// Avanzamos el puntero del padre
				if(fulldebug)System.out.printf("Avanzamos posición PADRE\n");
				posP1++;
				//Controlamos que no nos salgamos
				if (posP1 >= lg) posP1 = 0;
				if (posH1 >= lg) posH1 = 0;
				if(fulldebug)System.out.printf("\nNueva vuelta de bucle, posP1: %d , posH1: %d \n", posP1, posH1);
				
				//Comprobamos que el valor en la posicion actual no este en hijo
				GenEntero GP1 = (GenEntero)padre1[posP1];
				int valorP1 = GP1.getAlelo();
				if(fulldebug)System.out.printf("Valor Padre1[posP1]; %d \n", valorP1);
				
				//Si no esta en el hijo lo metemos
				if (!usadoH1[valorP1]) {
					if(fulldebug)System.out.printf("Valor Padre1[posP1]=%d no esta usado en el HIJO 1\n", valorP1);
					// lo metemos en la posicion actual
					GenEntero GH1 = (GenEntero) hijo1[posH1];
					GH1.setAlelo(valorP1);
					hijo1[posH1] = GH1;
					usadoH1[valorP1] = true;
					falsosEnH1 = checkFalsos(usadoH1);
					if(fulldebug)System.out.printf("Incluimos en la posición posH1(%d) del HIJO1.\n", posH1);
					// avanzamos la posicion del hijo a la siguiente con -1
					if(fulldebug)System.out.printf("Avanzamos posición HIJO\n");
					posH1++;
					if (posH1 >= lg) posH1 = 0;
					// Comprobamos que contenga un -1
					GH1 = (GenEntero) hijo1[posH1];
					int valor = GH1.getAlelo();
					while (valor != -1 && falsosEnH1 > 0) {
						posH1++;
						if (posH1 >= lg) posH1 = 0;
						GH1 = (GenEntero) hijo1[posH1];
						valor = GH1.getAlelo();
					}
				}
				else {
					if(fulldebug)System.out.printf("Valor Padre1[posP1]=%d SI esta usado en el HIJO 1\n", valorP1);
				}
			} while (posP1 != corte2 && falsosEnH1 > 0);
		
			if(fulldebug) { 
				int[] H1 = new int[lg];
				for (int dc = 0; dc < lg; dc++) {
					GenEntero GH1 = (GenEntero) hijo1[dc];
					H1[dc] = GH1.getAlelo();
				}
				System.out.println("\n");
				System.out.printf("HIJO1 POST RELLENO: ");
				System.out.println(Arrays.toString(H1));
			}
			
			if(fulldebug) System.out.println("\nRELLENADO DEL HIJO2");
			// Ahora lo mismo con el Padre2
			if(fulldebug) { 
				int[] H2 = new int[lg];
				for (int dc = 0; dc < lg; dc++) {
					GenEntero GH2 = (GenEntero) hijo2[dc];
					H2[dc] = GH2.getAlelo();
				}
				System.out.printf("\nHIJO2 PREV RELLENO: ");
				System.out.println(Arrays.toString(H2));
			}
			int falsosEnH2 = checkFalsos(usadoH2);
			
			do {
				// Si esta dentro no hacemos nada
				// Avanzamos el puntero del padre
				if(fulldebug)System.out.printf("Avanzamos posición PADRE2\n");
				posP2++;
				if (posP2 >= lg) posP2 = 0;
				if (posH2 >= lg) posH2 = 0;
				if(fulldebug)System.out.printf("\nNueva vuelta de bucle, posP2: %d , posH2: %d \n", posP2, posH2);
				
				//Comprobamos que el valor en la posicion actual no este en hijo
				GenEntero GP2 = (GenEntero)padre2[posP2];
				int valorP2 = GP2.getAlelo();
				if(fulldebug)System.out.printf("Valor Padre2[posP2]; %d \n", valorP2);
				
				//Si no esta en el hijo lo metemos
				if (!usadoH2[valorP2]) {
					if(fulldebug)System.out.printf("Valor Padre2[posP2]=%d no esta usado en el HIJO2 \n", valorP2);
					// lo metemos en la posicion actual
					GenEntero GH2 = (GenEntero) hijo2[posH2];
					GH2.setAlelo(valorP2);
					hijo2[posH2] = GH2;
					usadoH2[valorP2] = true;
					falsosEnH2 = checkFalsos(usadoH2);
					if(fulldebug)System.out.printf("Incluimos en la posición posH2(%d) del HIJO2.\n", posH2);
					// avanzamos la posicion del hijo
					if(fulldebug)System.out.printf("Avanzamos posición HIJO2\n");
					posH2++;
					if (posH2 >= lg) posH2 = 0;
					// Comprobamos que contenga un -1
					GH2 = (GenEntero) hijo2[posH2];
					int valor = GH2.getAlelo();
					while (valor != -1 && falsosEnH2 > 0) {
						posH2++;
						if (posH2 >= lg) posH2 = 0;
						GH2 = (GenEntero) hijo2[posH2];
						valor = GH2.getAlelo();
					}
				}
				else {
					if(fulldebug)System.out.printf("Valor Padre2[posP2]=%d SI esta usado en el HIJO 2\n", valorP2);
				}
				
			} while (posP2 != corte2 && falsosEnH2 > 0);
			
			if(fulldebug) { 
				int[] H2 = new int[lg];
				for (int dc = 0; dc < lg; dc++) {
					GenEntero GH2 = (GenEntero) hijo2[dc];
					H2[dc] = GH2.getAlelo();
				}
				System.out.println("\n");
				System.out.printf("HIJO2 POST RELLENO: ");
				System.out.println(Arrays.toString(H2));
			}
		}
		
		
		
		
		
		
		////////////////////////////////////////////////////////////////////
		// FINAL (OK)
		////////////////////////////////////////////////////////////////////
		if (debug) {
			// Variables debugeo para mostrar padres e hijos
			int[] P1 = new int[lg];
			int[] P2 = new int[lg];
			int[] H1 = new int[lg];
			int[] H2 = new int[lg];
			for (int dc = 0; dc < lg; dc++) {
				GenEntero GP1 = (GenEntero) padre1[dc];
				GenEntero GP2 = (GenEntero) padre2[dc];
				GenEntero GH1 = (GenEntero) hijo1[dc];
				GenEntero GH2 = (GenEntero) hijo2[dc];
				P1[dc] = GP1.getAlelo();
				P2[dc] = GP2.getAlelo();
				H1[dc] = GH1.getAlelo();
				H2[dc] = GH2.getAlelo();
			}
			if(fulldebug)System.out.println("\nRESULTADO FINAL CRUCE");
			System.out.printf("Cromosoma PADRE 1: ");
			System.out.println(Arrays.toString(P1));
			System.out.printf("Cromosoma PADRE 2: ");
			System.out.println(Arrays.toString(P2));
			System.out.printf("Cromosoma  HIJO 1: ");
			System.out.println(Arrays.toString(H1));
			System.out.printf("Cromosoma  HIJO 2: ");
			System.out.println(Arrays.toString(H2));
			System.out.println("");
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
	
	private static int checkFalsos(boolean[] usados) {
		int falsos = 0;
		for (int i = 0; i < usados.length; i++) {
			if (!usados[i]) falsos++;
		}
		return falsos;
	}
}
