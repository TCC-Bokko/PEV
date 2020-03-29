package es.ucm.fdi.pev.cruce;

import java.util.Arrays;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenEntero;

public class HT {
	public static void ht(Cromosoma c1, Cromosoma c2) {
		// CRUCE PROPIO, nombrado: HEAD AND TAIL (HT)
		// [1,2,5,8,7,6,4,3] Padre1
		// [8,5,7,1,3,6,2,4] Padre2
		// Se coge el primer valor del padre1 (Head) y se intercambia por el último del padre2 (Tail)
		// [1,2,5,8,7,6,4,3] Head: 1
		// [8,5,7,1,3,6,2,4] Tail: 4
		// INTERCAMBIO
		// [4,2,5,8,7,6,4,3] conflicto padre1 posición: 6 (el 4 repetido)
		// [8,5,7,1,3,6,2,1] conflicto padre2 posición: 3 (el 1 repetido)
		// Despues se resuelve el conflicto intercambiando los elementos en las posiciones de conflicto padre1 y padre2
		// INTERCAMBIO padre1[6] por padre2[3]
		// [4,2,5,8,7,6,4,3] Hijo1
		// [8,5,7,1,3,6,2,1] Hijo2
		// Se puede extender si se hace el mismo proceso con el Head del padre2 y el Tail del padre1
		
		// MUESTRA MENSAJES POR CONSOLA
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
		if(debug) System.out.println("\nNUEVO CRUCE INVENTADO: HT");
		///////////////DEBUG CARGA PADRES
		if (debug) {
			// Variables debugeo para mostrar padres e hijos
			int[] P1 = new int[lg];
			int[] P2 = new int[lg];
			for (int dc = 0; dc < lg; dc++) {
				GenEntero GP1 = (GenEntero) padre1[dc];
				GenEntero GP2 = (GenEntero) padre2[dc];
				P1[dc] = GP1.getAlelo();
				P2[dc] = GP2.getAlelo();
			}
			System.out.println("DATOS ENTRADA PADRES");
			System.out.printf("Cromosoma PADRE 1: ");
			System.out.println(Arrays.toString(P1));
			System.out.printf("Cromosoma PADRE 2: ");
			System.out.println(Arrays.toString(P2));
		}		
		///////////////////////
		
		////////////////////////////////////////////////////////////////////
		// INTERCAMBIO HEAD (P1) & TAIL (P2)
		////////////////////////////////////////////////////////////////////
		if(fulldebug) System.out.println("\nHEAD AND TAIL 1");
		GenEntero GHead1 = (GenEntero) padre1[0];
		int headp1 = GHead1.getAlelo();
		GenEntero GTail2 = (GenEntero) padre2[lg-1];
		int tailp2 = GTail2.getAlelo();
		GHead1.setAlelo(tailp2);
		padre1[0] = GHead1;
		GTail2.setAlelo(headp1);
		padre2[lg-1] = GTail2;
		
		////////////////////////////////////////////////////////////////////
		// RESOLUCION CONFLICTOS
		////////////////////////////////////////////////////////////////////
		// Solo es necesario si ambos (headp1 y tailp2) no son iguales.
		if (headp1 != tailp2) {
			if(fulldebug) System.out.println("\nRESOLVER CONFLICTOS");
			int headConflict = 1;
			boolean encontrado = false;
			//Recorremos el padre1 en busca de la posición en conflicto (ignorando el pos head)
			while (!encontrado) {
				if(fulldebug) System.out.printf("\nWhile del Head, headConflict: %d\n", headConflict);
				GenEntero GP1 = (GenEntero) padre1[headConflict];
				int p1value = GP1.getAlelo();
				// Si el nuevo valor del Head del padre1 (almacenado en el int tailp2)
				// coincide con otro valor dentro del padre1
				if (tailp2 == p1value) {
					if(fulldebug) System.out.printf("Valor encontrado.\n");
					encontrado = true;
				} else {
					if(fulldebug) System.out.printf("Valor No encontrado.\n");
					headConflict++;
				}
			}
			//Aqui habremos encontrado la posición del conflicto dentro de la variable headConflict
			
			//Ahora buscamos el conflicto en el padre2 (hacia atras)
			int tailConflict = lg-2;
			boolean encontrado2 = false;
			while(!encontrado2) {
				if(fulldebug) System.out.printf("\nWhile del Tail, tailConflict: %d\n", tailConflict);
				GenEntero GP2 = (GenEntero) padre2[tailConflict];
				int p2value = GP2.getAlelo();
				// Si el nuevo valor del Tail del padre2 (almacenado en el int headp1)
				// coincide con otro valor dentro del padre2
				if (headp1 == p2value) {
					if(fulldebug) System.out.printf("Valor encontrado.\n");
					encontrado2 = true;
				} else {
					if(fulldebug) System.out.printf("Valor No encontrado.\n");
					tailConflict--;
				}
			}
			// Ahora que tenemos ambas posiciones en conflicto, solo queda intercambiarlas
			GenEntero GP1 = (GenEntero) padre1[headConflict];
			GenEntero GP2 = (GenEntero) padre2[tailConflict];
			int p1value = GP1.getAlelo();
			int p2value = GP2.getAlelo();
			GP1.setAlelo(p2value);
			GP2.setAlelo(p1value);
		}
			
		////////////////////////////////////////////////////////////////////
		// INTERCAMBIO HEAD (P2) & TAIL (P1)
		////////////////////////////////////////////////////////////////////
		if(fulldebug) System.out.println("\nHEAD AND TAIL 2");
		GenEntero GHead2 = (GenEntero) padre2[0];
		int headp2 = GHead2.getAlelo();
		GenEntero GTail1 = (GenEntero) padre1[lg-1];
		int tailp1 = GTail1.getAlelo();
		GHead2.setAlelo(tailp1);
		padre2[0] = GHead2;
		GTail1.setAlelo(headp2);
		padre1[lg-1] = GTail1;
		
		////////////////////////////////////////////////////////////////////
		// RESOLUCION CONFLICTOS
		////////////////////////////////////////////////////////////////////
		// Solo es necesario si ambos (headp2 y tailp1) no son iguales.
		if (headp2 != tailp1) {
			if(fulldebug) System.out.println("\nRESOLVER CONFLICTOS");
			int headConflict = 1;
			boolean encontrado = false;
			//Recorremos el padre1 en busca de la posición en conflicto (ignorando el pos head)
			while (!encontrado) {
				if(fulldebug) System.out.printf("\nWhile del Head, headConflict: %d\n", headConflict);
				GenEntero GP2 = (GenEntero) padre2[headConflict];
				int p2value = GP2.getAlelo();
				// Si el nuevo valor del Head del padre1 (almacenado en el int tailp2)
				// coincide con otro valor dentro del padre1
				if (tailp1 == p2value) {
					if(fulldebug) System.out.printf("Valor encontrado.\n");
					encontrado = true;
				} else {
					if(fulldebug) System.out.printf("Valor No encontrado.\n");
					headConflict++;
				}
			}
			//Aqui habremos encontrado la posición del conflicto dentro de la variable headConflict
			
			//Ahora buscamos el conflicto en el padre1 (hacia atras)
			int tailConflict = lg-2;
			boolean encontrado2 = false;
			while(!encontrado2) {
				if(fulldebug) System.out.printf("\nWhile del Tail, tailConflict: %d\n", tailConflict);
				GenEntero GP1 = (GenEntero) padre1[tailConflict];
				int p1value = GP1.getAlelo();
				// Si el nuevo valor del Tail del padre2 (almacenado en el int headp1)
				// coincide con otro valor dentro del padre2
				if (headp2 == p1value) {
					if(fulldebug) System.out.printf("Valor encontrado.\n");
					encontrado2 = true;
				} else {
					if(fulldebug) System.out.printf("Valor No encontrado.\n");
					tailConflict--;
				}
			}
			// Ahora que tenemos ambas posiciones en conflicto, solo queda intercambiarlas
			GenEntero GP2 = (GenEntero) padre2[headConflict];
			GenEntero GP1 = (GenEntero) padre1[tailConflict];
			int p2value = GP2.getAlelo();
			int p1value = GP1.getAlelo();
			GP2.setAlelo(p1value);
			GP1.setAlelo(p2value);
		}
		
		////////////////////////////////////////////////////////////////////
		// Devolvemos los hijos
		////////////////////////////////////////////////////////////////////
		if (debug) {
			// Variables debugeo para mostrar hijos (Padres modificados)
			int[] H1 = new int[lg];
			int[] H2 = new int[lg];
			for (int dc = 0; dc < lg; dc++) {
				GenEntero GH1 = (GenEntero) padre1[dc];
				GenEntero GH2 = (GenEntero) padre2[dc];
				H1[dc] = GH1.getAlelo();
				H2[dc] = GH2.getAlelo();
			}
			if(fulldebug)System.out.println("\nRESULTADO FINAL CRUCE");
			System.out.printf("Cromosoma  HIJO 1: ");
			System.out.println(Arrays.toString(H1));
			System.out.printf("Cromosoma  HIJO 2: ");
			System.out.println(Arrays.toString(H2));
			System.out.println("");
		}
		c1.setGenes(padre1);
		c2.setGenes(padre2);	
	}

}
