package es.ucm.fdi.pev.mutacion;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.pev.estructura.Arbol;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaArbol;

public class Permutacion {
	public static boolean permutacion(Cromosoma c, float prob) 
	{	
		boolean haMutado = false;
		Random r = new Random();
		float rand = r.nextFloat();
		
		if(rand < prob) {
			muta(c);
			haMutado = true;
		}
		return haMutado;
	}
	
	private static void muta(Cromosoma c)
	{
		// Permutación: Intercambia el orden de dos operandos.
		CromosomaArbol CA = (CromosomaArbol) c;
		List<Arbol> nodos = CA.getListaNodos();
		
		//Obtenemos un nodo Funcion con un operador
		int posNodo = buscaFuncion(nodos);
		Arbol nodoMutable = nodos.get(posNodo);
		String valor = nodoMutable.getValor();
		int aridad = getAridad(valor);
		Arbol aux;
		Random r = new Random();
		
		// Intercambiamos los hijos del nodo.
		if (aridad == 2) {
			aux = nodoMutable.getHi();
			nodoMutable.setHI(nodoMutable.getHd());
			nodoMutable.setHD(aux);
		} else if (aridad == 3) {
			int intercambio = r.nextInt(3);
			// Con 3 valores pueden aparecer 3 intercambios de 2 elementos.
			if (intercambio == 0) {
				// ICD to DCI
				aux = nodoMutable.getHi();
				nodoMutable.setHI(nodoMutable.getHd());
				nodoMutable.setHD(aux);
			} else if (intercambio == 1) {
				// ICD to CID
				aux = nodoMutable.getHi();
				nodoMutable.setHI(nodoMutable.getHc());
				nodoMutable.setHC(aux);
			} else if (intercambio == 2) {
				// ICD to IDC
				aux = nodoMutable.getHc();
				nodoMutable.setHC(nodoMutable.getHd());
				nodoMutable.setHD(aux);
			} else {
				System.out.println("[Mutacion-Permutacion] Error intercambio en aridad 3. Devolviendo el cromosoma sin mutar.");
			}
		} else {
			System.out.println("[Mutacion-Permutacion] Error: Aridad = 0. Devolviendo el cromosoma sin mutar.");
		}
		
		//Lo metemos en la lista -- (A partir de aqui necesario?)
		nodos.set(posNodo, nodoMutable);
		CA.setListaNodos(nodos);
		c = CA;
	}
	
	//Devuelve la posición del nodo operador valido.
	private static int buscaFuncion(List<Arbol> nodos) {
		Random r = new Random();
		Boolean valido = false;
		String tipo;
		int i = -1;
		while (!valido || i == -1) {
			i = r.nextInt(nodos.size()); //Como la raiz puede ser un operador, tambien la incluimos
			tipo = nodos.get(i).getValor();
			//Si es una de las funciones
			if (tipo == "AND" || tipo == "OR" || tipo == "IF") {
				valido = true;
			}
			
			if(valido && i == -1) System.out.println("[Mutacion-Permutacion] ERROR: nodo valido pero en posicion -1.");
		}
		
		return i;
	}
	
	private static int getAridad(String funcion) {
		int aridad = 0;
		
		if (funcion == "IF") aridad = 3;
		else if (funcion == "OR" || funcion == "AND") aridad = 2;
		else System.out.println("[Mutacion-Permutacion] Argumento funcion no corresponde con ningun operador valido, devolviendo aridad 0");
		
		return aridad;
	}
}