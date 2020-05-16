package es.ucm.fdi.pev.mutacion;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.pev.estructura.GenArbol;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaP3;

public class Terminal {
	public static boolean terminal(Cromosoma c, float prob) 
	{	
		boolean haMutado = false;
		Random r = new Random();
		float rand = r.nextFloat();
		
		if(rand < prob) {
			haMutado = muta(c);
			//haMutado = true;
		}
		return haMutado;
	}
	
	private static boolean muta(Cromosoma c)
	{
		// Terminal: Cambia uno de las hojas operando.
		CromosomaP3 CA = (CromosomaP3) c;
		CA.actualizaArbol();
		List<GenArbol> nodos = CA.getListaNodos();
		int numAs = CA.getNumAs();
		
		GenArbol nodoMutable;
		int posNodo;
		int numNodos = nodos.size();
		
		if (numNodos == 1) {
			// Solo existe un nodo, la raiz, y es un operando
			posNodo = 0;
			nodoMutable = nodos.get(posNodo);
		}		
		else if (numNodos == 2) {
			// Existen dos nodos, un NOT (raiz) y un operando
			posNodo = 1;
			nodoMutable = nodos.get(posNodo);
		} else {
			//Buscamos un nodo operando
			posNodo = buscaTerminal(nodos);
			nodoMutable = nodos.get(posNodo);
		}
		
		// Buscamos un nuevo valor
		String nuevoValor;
		String[] operandos = null;
		Random r = new Random();
		if (numAs == 2) {
			operandos = new String[] {"A0", "A1", "D0", "D1", "D2", "D3"};
		} else if (numAs == 3) {
			operandos = new String[] {"A0", "A1", "A2", "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7"};
		}
		
		int it = r.nextInt(operandos.length);;
		nuevoValor = operandos[it];
		
		//Establecemos el nuevo operando en el nodo escogido.
		nodoMutable.setValor(nuevoValor);
		
		//Lo metemos en la lista -- (A partir de aqui necesario?)
		nodos.set(posNodo, nodoMutable);
		
		CA.setListaNodos(nodos);
		
		c = CA;
		
		return true;
	}
	
	//Devuelve la posición del nodo operando valido.
	private static int buscaTerminal(List<GenArbol> nodos) {
		Random r = new Random();
		Boolean valido = false;
		String tipo;
		int i = -1;
		while (!valido || i == -1) {
			i = r.nextInt(nodos.size()-1); //No miramos la raiz
			tipo = nodos.get(i).getValor();
			//Si no es una de las funciones
			if (tipo != "AND" && tipo != "OR" && tipo != "NOT" && tipo != "IF") {
				valido = true;
			}
			
			if(valido && i == -1) System.out.println("[Mutacion Terminal] ERROR: nodo valido pero en posicion -1.");
		}
		
		return i;
	}
}
