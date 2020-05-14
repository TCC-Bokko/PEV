package es.ucm.fdi.pev.mutacion;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.pev.estructura.Arbol;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaArbol;

public class Terminal {
	public static boolean terminal(Cromosoma c, float prob) 
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
		// Terminal: Cambia uno de las hojas operando.
		CromosomaArbol CA = (CromosomaArbol) c;
		List<Arbol> nodos = CA.getListaNodos();
		int numAs = CA.getNumAs();
		
		//Obtenemos un nodo hoja terminal con un operando
		int posNodo = buscaTerminal(nodos);
		Arbol nodoMutable = nodos.get(posNodo); 
		
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
	}
	
	//Devuelve la posición del nodo operando valido.
	private static int buscaTerminal(List<Arbol> nodos) {
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
