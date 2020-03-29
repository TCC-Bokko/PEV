package es.ucm.fdi.pev.cruce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenEntero;

public class ERX {
	public static void erx(Cromosoma c1, Cromosoma c2) {
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
		if(debug) System.out.println("\nNUEVO CRUCE ERX");
		
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
		// GENERAR TABLAS DE CONECTIVIDAD (OK)
		///////////////////////////////////////////////////////////////////
		// HASH MAP (KEY, VALUE)
		// .put("KEY","VALUE") : añade una pareja clave / valor.
		// .get("KEY")		: Devuelve el valor de ese KEY
		// .size()			: Devuelve la cantidad de elementos dentro del diccionario
		// .remove("KEY")	: elimina una pareja clave/valor.
		// .clear() 		: Elimina todos los elementos
		// .keySet()		: Devuelve una lista de las KEYS dentro del diccionario
		// .values()		: Devuelve una lista de los valores dentro del diccionario
		//Declaración de hash map
		HashMap<Integer, List<Integer>> TablaConexiones = new HashMap<Integer, List<Integer>>();
		
		//Rellenado de valores
		//recorremos de inicio a final el padre1
		for (int i = 0; i < lg; i++) {
			//Lista de los adyacentes
			List<Integer> adyacentes = new ArrayList<Integer>();
			boolean[] utilizados = new boolean[lg];
			for (int b = 0; b < lg; b++) {
				utilizados[b] = false;
			}
			
			//Obtenemos la KEY (valor actual del padre1 (i))
			GenEntero GP1 = (GenEntero) padre1[i];
			int valueP1 = GP1.getAlelo();
			
			//Almacenamos sus adyacentes (Aqui no hay peligro de que repita)
			int prev = i - 1;
			int next = i + 1;
			// Control de límites
			if (prev < 0) prev = lg - 1;
			if (next == lg) next = 0;
			// Obtenemos valor
			GenEntero GPrev = (GenEntero) padre1[prev];
			GenEntero GNext = (GenEntero) padre1[next];
			int valuePrev = GPrev.getAlelo();
			int valueNext = GNext.getAlelo();
			// Los metemos en la lista de adyacentes
			adyacentes.add(valuePrev);
			adyacentes.add(valueNext);
			// marcamos como utilizados
			utilizados[valuePrev] = true;
			utilizados[valueNext] = true;
			
			// Busqueda del valor en el otro padre
			int posPadre2 = -1;
			for (int j = 0; j < lg; j++) {
				GenEntero GP2 = (GenEntero) padre2[j];
				int valueP2 = GP2.getAlelo();
				//Si lo encontramos...
				if (valueP2 == valueP1) {
					posPadre2 = j;
					// Hemos encontrado el valor considerado (valueP1)
					// Vemos sus adyacentes en el padre2
					int prev2 = j - 1;
					int next2 = j + 1;
					// Control de límites
					if (prev2 < 0) prev2 = lg - 1;
					if (next2 == lg) next2 = 0;
					// Obtenemos valor
					GenEntero GPrev2 = (GenEntero) padre2[prev2];
					GenEntero GNext2 = (GenEntero) padre2[next2];
					int valuePrev2 = GPrev2.getAlelo();
					int valueNext2 = GNext2.getAlelo();
					// Los agregamos a la lista de adyactentes
					// si no esta repetido
					if (!utilizados[valuePrev2]) adyacentes.add(valuePrev2);
					if (!utilizados[valueNext2]) adyacentes.add(valueNext2);
				}
			}
			
			//Metemos en el diccionario el valueP1 como clave y la lista adyacentes como valor.
			TablaConexiones.put(valueP1, adyacentes);
		}
		
		////////////////////////////////////////////////////////////////////
		// CONSTRUIR HIJO 1 (WIP)
		///////////////////////////////////////////////////////////////////
		// Declaración e inicializado
		Gen[] hijo1 = new GenEntero[lg];
		ArrayList<GenEntero> aux1 = new ArrayList<GenEntero>();
		for(int i = 0; i < lg; i++)
		{
			GenEntero h1 = new GenEntero(-1);
			aux1.add(h1);
		}
		aux1.toArray(hijo1);
		int posHijo = 0;
		// Array de marcados
		boolean[] utilizadosH1 = new boolean[lg];
		for (int b = 0; b < lg; b++) {
			utilizadosH1[b] = false;
		}
		// Construir ruta
		// Elegimos el primer valor del padre2
		GenEntero GP2 = (GenEntero) padre2[0];
		int posActual = GP2.getAlelo();
		//Lo metemos en el hijo
		GenEntero GH1 = (GenEntero) hijo1[posHijo];
		GH1.setAlelo(posActual);
		hijo1[posHijo] = GH1;
		utilizadosH1[posActual] = true;
		
		// Bucle mientras no este el hijo completo (haya usado todos los edificios posibles)
		while (!completo(utilizadosH1)) {
			if (fulldebug) {
				System.out.println("\nNuevo bucle del While");
				//System.out.printf("Utilizados H1: ");
				//System.out.println(Arrays.toString(utilizadosH1));
				//System.out.printf("posHijo: %d. \n", posHijo);
				System.out.printf("Edif Actual: %d. \n", posActual);
				System.out.println("__ADYACENTES__");
			}
			// Adyacentes (todos)
			List<Integer> adyacentes = new ArrayList<Integer>();
			// Candidatos validos
			List<Integer> posibleNext = new ArrayList<Integer>();
			// Ver las conexiones del pos Actual
			adyacentes = TablaConexiones.get(posActual);
			// Comprobar si la Key esta usada en el H1
			for (int i = 0; i < adyacentes.size(); i++) {
				int candidato = adyacentes.get(i);
				//Si el candidato aun no ha sido utilizado
				if (!utilizadosH1[candidato]) {
					posibleNext.add(candidato);
					if (fulldebug) System.out.printf("Candidato: %d. No utilizado aún, agregado a posibleNext.\n", candidato);
				} else {
					if (fulldebug) System.out.printf("Candidato: %d. Ya utilizado, descartado.\n", candidato);
				}
			}
			
			// Una vez seleccionados los candidatos válidos
			// Comprobar tamaño de cada lista adyacentes y elegir la menor conectada o el primero que llegue en caso de empate
			// Variables para almacenar el mejor, inicializadas con el primer candidato
			int tamCandValidos = posibleNext.size();
			if (tamCandValidos > 0) {
				if (fulldebug) System.out.println("__CANDIDATOS VALIDOS__");
				// SI EXISTEN CANDIDATOS VALIDOS
				int mejorCandidato = posibleNext.get(0);
				List<Integer> adyacentesCandidato = new ArrayList<Integer>();
				adyacentesCandidato = TablaConexiones.get(mejorCandidato);
				int conexionesMejorCandidato = adyacentesCandidato.size();
				if (fulldebug) System.out.printf("Candidato: %d (%d conexiones). \n", mejorCandidato, conexionesMejorCandidato);
				// Comparamos con los otros candidatos si hay más de 1 
				if (posibleNext.size() > 1) { 
					for (int j = 1; j < posibleNext.size(); j++) {
						int candidato = posibleNext.get(j);
						adyacentesCandidato = TablaConexiones.get(candidato);
						int conexionesCandidato = adyacentesCandidato.size();
						if (fulldebug) System.out.printf("Candidato: %d (%d conexiones). \n", candidato, conexionesCandidato);
						//El primero se establece como mejor para ir comparando
						if (conexionesCandidato < conexionesMejorCandidato) {
							conexionesMejorCandidato = conexionesCandidato;
							mejorCandidato = candidato;
						}
					}
				}
				
				//Aqui ya hemos seleccionado el mejor candidato, que será el siguiente, así pues
				//Movemos la posición actual al mejor candidato.
				if (fulldebug) System.out.printf("Mejor Candidato: %d (%d conexiones). \n", mejorCandidato, conexionesMejorCandidato);
				posActual = mejorCandidato;
				if (fulldebug) System.out.printf("Siguiente posicion: %d. \n", posActual);
				
				// lo añadimos al hijo
				posHijo++;
				GH1 = (GenEntero) hijo1[posHijo];
				GH1.setAlelo(posActual);
				hijo1[posHijo] = GH1;
				utilizadosH1[posActual] = true;
				
			} else {
				// EN CASO DE QUE NO EXISTAN CANDIDATOS VALIDOS
				if (fulldebug) {
					System.out.println("__NO EXISTEN CANDIDATOS VALIDOS!!!__");
					System.out.println("Retomando desde siguiente valor no usado");
				}
				//Busqueda del siguiente valor no usado
				boolean encontrado = false;
				int posUnused = 0;
				while (!encontrado) {
					if (utilizadosH1[posUnused]) posUnused++;
					else {
						encontrado = true;
					}
				}
				//Metemos en el hijo el siguiente valor no usado
				posActual = posUnused;
				posHijo++;
				GH1 = (GenEntero) hijo1[posHijo];
				GH1.setAlelo(posUnused);
				utilizadosH1[posUnused] = true;
			}
			//Y siguiente vuelta de bucle (mientras utilizadosH1 no este completo y el hijo lleno)
			if (fulldebug) {
				// Variables debugeo para mostrar padres e hijos
				int[] H1 = new int[lg];
				for (int dc = 0; dc < lg; dc++) {
					GenEntero GHi1 = (GenEntero) hijo1[dc];
					H1[dc] = GHi1.getAlelo();
				}			
				System.out.printf("Cromosoma  HIJO 1: ");
				System.out.println(Arrays.toString(H1));
				System.out.printf("Utilizados H1: ");
				System.out.println(Arrays.toString(utilizadosH1));
			}
			
			
		}
		
		////////////////////////////////////////////////////////////////////
		// CONSTRUIR HIJO 2 (WIP)
		///////////////////////////////////////////////////////////////////
		// Declaración e inicializado
		Gen[] hijo2 = new GenEntero[lg];
		ArrayList<GenEntero> aux2 = new ArrayList<GenEntero>();
		for(int i = 0; i < lg; i++)
		{
			GenEntero h2 = new GenEntero(-1);
			aux2.add(h2);
		}
		aux2.toArray(hijo2);
		int posHijo2 = 0;
		// Array de marcados
		boolean[] utilizadosH2 = new boolean[lg];
		for (int b = 0; b < lg; b++) {
			utilizadosH2[b] = false;
		}
		// Construir ruta
		// Elegimos el primer valor del padre2
		GenEntero GP1 = (GenEntero) padre1[0];
		int posActual2 = GP1.getAlelo();
		//Lo metemos en el hijo
		GenEntero GH2 = (GenEntero) hijo2[posHijo2];
		GH2.setAlelo(posActual2);
		hijo2[posHijo2] = GH2;
		utilizadosH2[posActual2] = true;
		
		// Bucle mientras no este el hijo completo (haya usado todos los edificios posibles)
		while (!completo(utilizadosH2)) {
			if (fulldebug) {
				System.out.println("\nNuevo bucle del While");
				//System.out.printf("Utilizados H2: ");
				//System.out.println(Arrays.toString(utilizadosH2));
				//System.out.printf("posHijo2: %d. \n", posHijo2);
				System.out.printf("Edif Actual: %d. \n", posActual2);
				System.out.println("__ADYACENTES__");
			}
			// Adyacentes (todos)
			List<Integer> adyacentes = new ArrayList<Integer>();
			// Candidatos validos
			List<Integer> posibleNext = new ArrayList<Integer>();
			// Ver las conexiones del pos Actual
			adyacentes = TablaConexiones.get(posActual);
			// Comprobar si la Key esta usada en el H1
			for (int i = 0; i < adyacentes.size(); i++) {
				int candidato = adyacentes.get(i);
				//Si el candidato aun no ha sido utilizado
				if (!utilizadosH2[candidato]) {
					posibleNext.add(candidato);
					if (fulldebug) System.out.printf("Candidato: %d. No utilizado aún, agregado a posibleNext.\n", candidato);
				} else {
					if (fulldebug) System.out.printf("Candidato: %d. Ya utilizado, descartado.\n", candidato);
				}
			}
			
			// Una vez seleccionados los candidatos válidos
			// Comprobar tamaño de cada lista adyacentes y elegir la menor conectada o el primero que llegue en caso de empate
			int tamCandValidos = posibleNext.size();
			if (tamCandValidos > 0) {
				if (fulldebug) System.out.println("__CANDIDATOS VALIDOS__");
				int mejorCandidato = posibleNext.get(0);
				List<Integer> adyacentesCandidato = new ArrayList<Integer>();
				adyacentesCandidato = TablaConexiones.get(mejorCandidato);
				int conexionesMejorCandidato = adyacentesCandidato.size();
				if (fulldebug) System.out.printf("Candidato: %d (%d conexiones). \n", mejorCandidato, conexionesMejorCandidato);
				// Comparamos con los otros candidatos si hay más de 1 
				if (posibleNext.size() > 1) { 
					for (int j = 1; j < posibleNext.size(); j++) {
						int candidato = posibleNext.get(j);
						adyacentesCandidato = TablaConexiones.get(candidato);
						int conexionesCandidato = adyacentesCandidato.size();
						if (fulldebug) System.out.printf("Candidato: %d (%d conexiones). \n", candidato, conexionesCandidato);
						//El primero se establece como mejor para ir comparando
						if (conexionesCandidato < conexionesMejorCandidato) {
							conexionesMejorCandidato = conexionesCandidato;
							mejorCandidato = candidato;
						}
					}
				}
			
				//Aqui ya hemos seleccionado el mejor candidato, que será el siguiente, así pues
				//Movemos la posición actual al mejor candidato.
				if (fulldebug) System.out.printf("Mejor Candidato: %d (%d conexiones). \n", mejorCandidato, conexionesMejorCandidato);
				posActual2 = mejorCandidato;
				if (fulldebug) System.out.printf("Siguiente posicion: %d. \n", posActual2);
				// lo añadimos al hijo
				posHijo2++;
				GH2 = (GenEntero) hijo2[posHijo2];
				GH2.setAlelo(posActual2);
				hijo2[posHijo2] = GH2;
				utilizadosH2[posActual2] = true;
			} else {
				// EN CASO DE QUE NO EXISTAN CANDIDATOS VALIDOS
				if (fulldebug) {
					System.out.println("__NO EXISTEN CANDIDATOS VALIDOS!!!__");
					System.out.println("Retomando desde siguiente valor no usado");
				}
				//Busqueda del siguiente valor no usado
				boolean encontrado = false;
				int posUnused = 0;
				while (!encontrado) {
					if (utilizadosH2[posUnused]) posUnused++;
					else {
						encontrado = true;
					}
				}
				//Metemos en el hijo el siguiente valor no usado
				posActual2 = posUnused;
				posHijo2++;
				GH2 = (GenEntero) hijo2[posHijo2];
				GH2.setAlelo(posUnused);
				utilizadosH2[posUnused] = true;
			}
			//Y siguiente vuelta de bucle (mientras utilizadosH1 no este completo y el hijo lleno)
			if (fulldebug) {
				// Variables debugeo para mostrar padres e hijos
				int[] H2 = new int[lg];
				for (int dc = 0; dc < lg; dc++) {
					GenEntero GHi2 = (GenEntero) hijo2[dc];
					H2[dc] = GHi2.getAlelo();
				}			
				System.out.printf("Cromosoma  HIJO 2: ");
				System.out.println(Arrays.toString(H2));
				System.out.printf("Utilizados H2: ");
				System.out.println(Arrays.toString(utilizadosH2));
			}
		}
		
		////////////////////////////////////////////////////////////////////
		// DEVOLVER HIJOS GENERADOS
		///////////////////////////////////////////////////////////////////
		// Muestra de resultado del cruce
		if (debug) {
			// Variables debugeo para mostrar padres e hijos
			int[] P1 = new int[lg];
			int[] P2 = new int[lg];
			int[] H1 = new int[lg];
			int[] H2 = new int[lg];
			for (int dc = 0; dc < lg; dc++) {
				GenEntero GPa1 = (GenEntero) padre1[dc];
				GenEntero GPa2 = (GenEntero) padre2[dc];
				GenEntero GHi1 = (GenEntero) hijo1[dc];
				GenEntero GHi2 = (GenEntero) hijo2[dc];
				P1[dc] = GPa1.getAlelo();
				P2[dc] = GPa2.getAlelo();
				H1[dc] = GHi1.getAlelo();
				H2[dc] = GHi2.getAlelo();
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
	
	public static boolean completo(boolean[] marcados) {
		boolean completo = true;
		
		for (int i = 0; i < marcados.length; i++) {
			if (!marcados[i]) completo = false;
		}
		
		return completo;
	}
	
}
