package es.ucm.fdi.pev.mutacion;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.pev.estructura.Arbol;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaArbol;

public class Funcion {
	public static boolean funcion(Cromosoma c, float prob) 
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
		// Funcion: Cambia uno de los nodos operador (función).
		// Ojo, debe tener el mismo nivel de aridad (cantidad de parametros)
		CromosomaArbol CA = (CromosomaArbol) c;
		List<Arbol> nodos = CA.getListaNodos();
		
		//Obtenemos un nodo Funcion con un operador
		int posNodo = buscaFuncion(nodos);
		Arbol nodoMutable = nodos.get(posNodo);
		String valor = nodoMutable.getValor();
		int aridad = getAridad(valor);
		
		
		// Establecemos un nuevo valor
		String nuevoValor = valor; //Lo inicializamos igual al que tenía antes por si falla algo.
		String[] operadoresAridad1 = new String[] {"NOT"};
		String[] operadoresAridad2 = new String[] {"AND", "OR"};
		String[] operadoresAridad3 = new String[] {"IF"};
		Random r = new Random();
		
		if (aridad == 1) {
			// Soy consciente de que no cambiara nada, pero esto nos vale para otras funciones que tengan más de un operador con aridad 1
			int selec = r.nextInt(operadoresAridad1.length);
			nuevoValor = operadoresAridad1[selec];
		}
		else if (aridad == 2) {
			int selec = r.nextInt(operadoresAridad2.length);
			nuevoValor = operadoresAridad2[selec];
		} else if (aridad == 3) {
			// Soy consciente de que no cambiara nada, pero esto nos vale para otras funciones que tengan más de un operador con aridad 3
			int selec = r.nextInt(operadoresAridad3.length);
			nuevoValor = operadoresAridad3[selec];
		}
		
		//Se lo pasamos al nodo
		nodoMutable.setValor(nuevoValor);
		
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
			if (tipo == "AND" || tipo == "OR" || tipo == "NOT" || tipo == "IF") {
				valido = true;
			}
			
			if(valido && i == -1) System.out.println("[Mutacion Funcion] ERROR: nodo valido pero en posicion -1.");
		}
		
		return i;
	}
	
	private static int getAridad(String funcion) {
		int aridad = 0;
		
		if (funcion == "IF") aridad = 3;
		else if (funcion == "OR" || funcion == "AND") aridad = 2;
		else if (funcion == "NOT") aridad = 1;
		else System.out.println("[Mutacion-Funcion] Argumento funcion no corresponde con ningun operador, devolviendo aridad 0");
		
		return aridad;
		
	}
}