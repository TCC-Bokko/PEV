package es.ucm.fdi.pev.cruce;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenEntero;

public class CX {
	private static int posFind = -1;
	
	public static void cx(Cromosoma c1, Cromosoma c2) {
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
		if(debug) System.out.println("\nNUEVO CRUCE CX");
		// Vector de marcados: Marca si el valor int esta dentro del hijo
		boolean[] cambiadoH1 = new boolean[lg];
		boolean[] cambiadoH2 = new boolean[lg];
		
		///////////////DEBUG CARGA PADRES
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
		
		///////////////DEBUG HIJOS GENERADOS
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
			System.out.printf("Posiciones cambiadas en Hijo1: ");
			System.out.println(Arrays.toString(cambiadoH1));
			System.out.printf("Posiciones cambiadas en Hijo2: ");
			System.out.println(Arrays.toString(cambiadoH2));
		}	
		///////////////////////
		
		if(fulldebug)System.out.println("\nINICIO CICLOS");
		////////////////////////////////////////////////////////////////////
		//  CICLO EN PADRE1 PARA HIJO 1
		////////////////////////////////////////////////////////////////////
		//Ciclo 1: Empezando por el primer elemento del padre1
		if(fulldebug) System.out.println("CICLO HIJO 1");
		int posCiclo = 0;
		int nextCiclo = 0;
		int valorHijo = -1;
		while (valorHijo == -1) {
			//Primer descendiente toma primer valor del primer padre
			GenEntero GP1 = (GenEntero) padre1[posCiclo];
			int valP1 = GP1.getAlelo();
			GenEntero GH1 = (GenEntero) hijo1[posCiclo];
			GH1.setAlelo(valP1);
			hijo1[posCiclo] = GH1;
			//Marcamos la posición como cambiada
			cambiadoH1[posCiclo] = true;
			
			// Vemos valor en Padre2 de esta posición de ciclo
			GenEntero GP2 = (GenEntero) padre2[posCiclo];
			int valP2 = GP2.getAlelo();
			
			//Buscamos la posición del elemento valP2 en Padre1
			for(int i = 0; i < lg; i++) {
				GP1 = (GenEntero) padre1[i];
				valP1 = GP1.getAlelo();
				if (valP1 == valP2) nextCiclo = i;
			}
			//Actualizamos posición en el siguiente ciclo
			posCiclo = nextCiclo;
			
			//Actualizamos condición de corte
			//Vemos si en la siguiente posición el hijo tiene valor -1 para continuar
			GH1 = (GenEntero) hijo1[nextCiclo];
			valorHijo = GH1.getAlelo();
		}
		
		
		////////////////////////////////////////////////////////////////////
		//  CICLO EN PADRE2 PARA HIJO 2
		////////////////////////////////////////////////////////////////////
		//Ciclo 2: Empezando por el primer elemento del padre2
		if(fulldebug) System.out.println("CICLO HIJO 2");
		int posCiclo2 = 0;
		int nextCiclo2 = 0;
		int valorHijo2 = -1;
		while (valorHijo2 == -1) {
			//Primer descendiente toma primer valor del primer padre
			GenEntero GP2 = (GenEntero) padre2[posCiclo2];
			int valP2 = GP2.getAlelo();
			GenEntero GH2 = (GenEntero) hijo2[posCiclo2];
			GH2.setAlelo(valP2);
			hijo2[posCiclo2] = GH2;
			//Marcamos la posición como cambiada
			cambiadoH2[posCiclo2] = true;
			
			// Vemos valor en Padre1 de esta posición de ciclo
			GenEntero GP1 = (GenEntero) padre1[posCiclo2];
			int valP1 = GP1.getAlelo();
			
			//Buscamos la posición del elemento valP1 en Padre2
			for(int i = 0; i < lg; i++) {
				GP2 = (GenEntero) padre2[i];
				valP2 = GP2.getAlelo();
				if (valP2 == valP1) nextCiclo2 = i;
			}
			//Actualizamos posición en el siguiente ciclo
			posCiclo2 = nextCiclo2;
			
			//Actualizamos condición de corte
			//Vemos si en la siguiente posición el hijo tiene valor -1 para continuar
			GH2 = (GenEntero) hijo2[nextCiclo2];
			valorHijo2 = GH2.getAlelo();
		}
		
		///////////////DEBUG POST CICLOS
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
			System.out.println("\nCICLOS TERMINADOS");
			System.out.printf("Cromosoma PADRE 1: ");
			System.out.println(Arrays.toString(P1));
			System.out.printf("Cromosoma PADRE 2: ");
			System.out.println(Arrays.toString(P2));
			System.out.printf("Cromosoma  HIJO 1: ");
			System.out.println(Arrays.toString(H1));
			System.out.printf("Cromosoma  HIJO 2: ");
			System.out.println(Arrays.toString(H2));
			System.out.printf("Posiciones cambiadas en Hijo1: ");
			System.out.println(Arrays.toString(cambiadoH1));
			System.out.printf("Posiciones cambiadas en Hijo2: ");
			System.out.println(Arrays.toString(cambiadoH2));
		}	
		///////////////////////
		
		////////////////////////////////////////////////////////////////////
		//  RELLENADO
		////////////////////////////////////////////////////////////////////
		if(fulldebug)System.out.println("\nRELLENADO DE ELEMENTOS EN HIJOS");
		// Siempre habra el mismo numero de posiciones sin intercambiar y siempre estaran en el mismo punto.
		// Pueden sustituirse directamente ya que los elementos no coincidiran gracias a los bucles
		for (int j = 0; j < lg; j++) {
			//recorremos uno a uno los elementos de un padre
			//Si no esta cambiado metemos en los hijos el valor dentro del padre contrario
			if (!cambiadoH1[j]) {
				//Obtenemos valores de los padres en este j
				GenEntero GP1 = (GenEntero) padre1[j];
				GenEntero GP2 = (GenEntero) padre2[j];
				int valP1 = GP1.getAlelo();
				int valP2 = GP2.getAlelo();
				// Los ponemos en el hijo contrario
				GenEntero GH1 = (GenEntero) hijo1[j];
				GenEntero GH2 = (GenEntero) hijo2[j];
				GH1.setAlelo(valP2);
				GH2.setAlelo(valP1);
				hijo1[j] = GH1;
				hijo2[j] = GH2;
				cambiadoH1[j] = true;
				cambiadoH2[j] = true;
			}
		}
		
		////////////////// DEBUG FINAL
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
			if(fulldebug)System.out.printf("Posiciones cambiadas en Hijo1: ");
			if(fulldebug)System.out.println(Arrays.toString(cambiadoH1));
			if(fulldebug)System.out.printf("Posiciones cambiadas en Hijo2: ");
			if(fulldebug)System.out.println(Arrays.toString(cambiadoH2));
			System.out.println("");
		}
		
		// DEVOLVEMOS LOS HIJOS GENERADOS
		c1.setGenes(hijo1);
		c2.setGenes(hijo2);	
	}
	
}
