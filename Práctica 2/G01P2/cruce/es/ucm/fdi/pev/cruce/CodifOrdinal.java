package es.ucm.fdi.pev.cruce;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenEntero;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CodifOrdinal {
	public static void codifOrdinal(Cromosoma c1, Cromosoma c2) {
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
		if(debug) System.out.println("\nNUEVO CRUCE CO");
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
			System.out.println("\nDATOS ENTRADA PADRES");
			System.out.printf("Cromosoma PADRE 1: ");
			System.out.println(Arrays.toString(P1));
			System.out.printf("Cromosoma PADRE 2: ");
			System.out.println(Arrays.toString(P2));
		}		
		///////////////////////
		
		
		//////////////////////////////////
		// Listas Ordenadas de referencia.
		//////////////////////////////////
		// ARRAYLIST:
		// .add				= Agrega un elemento por el final de la cola (derecha)
		// .remove(position)= Elimina un elemento en la posición dada
		// .clear()			= Elimina todos los elementos y deja la lista vacia.
		// .size() 			= Devuelve el tamaño de la lista (cantidad de elementos dentro de ella)
		// .get(position) 	= Devuelve el elemento en una posición sin eliminarlo.
		List<Integer> List1 = new ArrayList<Integer>();
		List<Integer> List2 = new ArrayList<Integer>();
		List<Integer> ListaGeneral = new ArrayList<Integer>(); //la que usaremos para decodificar
		//Rellenando las listas
		for (int p1 = 0; p1 < padre1.length; p1++) {
			GenEntero GP1 = (GenEntero) padre1[p1];
			GenEntero GP2 = (GenEntero) padre2[p1];
			List1.add(GP1.getAlelo());
			List2.add(GP2.getAlelo());
			ListaGeneral.add(GP1.getAlelo());
		}
		//Ordenar las listas de modo ascendente.
		Collections.sort(List1);
		Collections.sort(List2);
		
		///////////////DEBUG Listas Ordenadas
		if (fulldebug) {
			System.out.println("\nDATOS LISTAS REFERENCIA");
			System.out.printf("List1: [");
			for (int i = 0; i < List1.size(); i++) {
				System.out.printf("%d,", List1.get(i));
			}
			System.out.printf("] \nList2: [");
			for (int i = 0; i < List2.size(); i++) {
				System.out.printf("%d,", List2.get(i));
			}
			System.out.printf("]\n");
		}		
		///////////////////////
		
		/////////////////////////////
		// LISTAS REPRESENTATIVAS: CODIFICACION
		/////////////////////////////
		if(fulldebug) System.out.println("\nCODIFICACION");
		List<Integer> LR1 = new ArrayList<Integer>();
		List<Integer> LR2 = new ArrayList<Integer>();
		
		//recorremos el padre1
		for (int i = 0; i < lg; i++) {
			//vemos el valor del padre
			GenEntero GP1 = (GenEntero) padre1[i];
			int valorP1 = GP1.getAlelo();
			int listPos = 0;
			boolean found = false;
					
			//Buscamos la posición este valor en su lista ordenada
			while(!found && listPos < lg) {
				//vemos el valor en la posición de la lista
				int valorL = List1.get(listPos);
				// Si corresponde... 
				if (valorL == valorP1) {
					// lo metemos en la lista representativa
					LR1.add(listPos);
					// actualizamos corte bucle
					found = true;
					// Eliminamos el elemento de la lista
					List1.remove(listPos);
				}
				if (!found) listPos++;
			}
		}
		
		//recorremos el padre2
		for (int i = 0; i < lg; i++) {
			//vemos el valor del padre
			GenEntero GP2 = (GenEntero) padre2[i];
			int valorP2 = GP2.getAlelo();
			int listPos = 0;
			boolean found = false;
					
			//Buscamos la posición este valor en su lista ordenada
			while(!found && listPos < lg) {
				//vemos el valor en la posición de la lista
				int valorL = List2.get(listPos);
				// Si corresponde... 
				if (valorL == valorP2) {
					// lo metemos en la lista representativa
					LR2.add(listPos);
					// actualizamos corte bucle
					found = true;
					// Eliminamos el elemento de la lista
					List2.remove(listPos);
				}
				if (!found) listPos++;
			}
		}
		///////////////DEBUG Listas Referencia Codificadas
		if (fulldebug) {
			System.out.println("\nDATOS COFIDICACION");
			System.out.printf("Codificacion Padre1: [");
			for (int i = 0; i < LR1.size(); i++) {
				System.out.printf("%d,", LR1.get(i));
			}
			System.out.printf("] \nCodificacion Padre2: [");
			for (int i = 0; i < LR2.size(); i++) {
				System.out.printf("%d,", LR2.get(i));
			}
			System.out.printf("]\n");
		}		
		///////////////////////
		
		/////////////////////////////
		// CRUCE MONOPUNTO NORMAL
		/////////////////////////////
		// Con las listas codificadas podemos aplicar el cruce monopunto normal
		// Decidimos un punto de corte
		// Elegir dos puntos de corte al azar
		//   c1 [1,2,3|4,5,6,7|8,9]
		//   c2 [4,5,2|1,8,7,6|9,3]
		// Corte en posiciones 3 y 7 (elegidos al azar)
		// Elegimos dos puntos al azar (next Int es exclusivo con el numero dado)
		Random r = new Random();
		int corte = r.nextInt(lg);
		if (fulldebug) System.out.printf("Corte en posicion: %d \n", corte);
		
		// Almacenamos esos valores
		int[] cruzado1 = new int[lg];
		int[] cruzado2 = new int[lg];
		
		// Realizamos cruce monopunto
		for(int i = 0; i < lg; i++) {
			if(i < corte) {
				cruzado1[i] = LR1.get(i);
				cruzado2[i] = LR2.get(i);
			}
			else {
				cruzado2[i] = LR1.get(i);
				cruzado1[i] = LR2.get(i);
			}
		}
		///////////////DEBUG Cruce Monopunto
		if (fulldebug) {
			System.out.println("\nCRUCE MONOPUNTO");
			System.out.printf("Codificacion cruzada1: ");
			System.out.println(Arrays.toString(cruzado1));
			System.out.printf("Codificacion cruzada2: ");
			System.out.println(Arrays.toString(cruzado2));
		
		}		
		///////////////////////
		
		/////////////////////////////
		// DECODIFICACION
		/////////////////////////////
		//Reconstruimos lista1 y lista2 con la lista general
		//deben estar vacías.
		if (fulldebug) {
			if(List1.isEmpty()) System.out.println("Lista1 vacía.");
			else System.out.println("[ERROR] Lista1 NO ESTA VACIA.");
			if(List2.isEmpty()) System.out.println("Lista2 vacía.");
			else System.out.println("[ERROR] Lista2 NO ESTA VACIA.");
		}
		for (int i = 0; i < ListaGeneral.size(); i++) {
			List1.add(ListaGeneral.get(i));
			List2.add(ListaGeneral.get(i));
		}
		//Con las listas reconstruidas, decodificamos en los hijos
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
		
		//Recorremos cada cruzado y decodificamos
		for(int i=0; i < cruzado1.length; i++) {
			int pos1 = cruzado1[i];
			int pos2 = cruzado2[i];
			//Sacamos el elemento en la pos1 de la lista1 al hijo
			int value1 = List1.get(pos1);
			int value2 = List2.get(pos2);
			// Lo metemos en el hijo en posicion i
			GenEntero GH1 = (GenEntero) hijo1[i];
			GenEntero GH2 = (GenEntero) hijo2[i];
			GH1.setAlelo(value1);
			GH2.setAlelo(value2);
			hijo1[i] = GH1;
			hijo2[i] = GH2;
			//eliminamos elemento de la lista
			List1.remove(pos1);
			List2.remove(pos2);
		}
		
		/////////////////////////////
		// DEVOLVER HIJOS GENERADOS
		/////////////////////////////
		//////////////////DEBUG FINAL
		if (debug) {
			// Variables debugeo para mostrar padres e hijos
			int[] H1 = new int[lg];
			int[] H2 = new int[lg];
			for (int dc = 0; dc < lg; dc++) {
				GenEntero GH1 = (GenEntero) hijo1[dc];
				GenEntero GH2 = (GenEntero) hijo2[dc];
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
		c1.setGenes(hijo1);
		c2.setGenes(hijo2);	
	}
}
