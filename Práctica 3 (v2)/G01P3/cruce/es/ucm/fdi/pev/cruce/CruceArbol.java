package es.ucm.fdi.pev.cruce;

import es.ucm.fdi.pev.estructura.GenArbol;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaArbol;
import es.ucm.fdi.pev.estructura.CromosomaP3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CruceArbol 
{
	
	private static final double PROB_FUNC = 0.9;
	
	public static void cruceArbol(CromosomaP3 padre1, CromosomaP3 padre2)
	{	
		ArrayList<GenArbol> nodos_selec1 = new ArrayList<GenArbol>();
		ArrayList<GenArbol> nodos_selec2 = new ArrayList<GenArbol>();
		
		
		//Seleccionamos los nodos más relevante según la probabilidad
		//0.9 se cruzará en una función
		//resto se cruzará en un terminal
		nodos_selec1 = obtieneNodos((GenArbol) padre1.getArbol().clone());
		nodos_selec2 = obtieneNodos((GenArbol) padre2.getArbol().clone());
		
		//obtenemos los puntos de cruce a partir de los nodos seleccionados
		int puntoCruce1 = (int) (Math.random()*nodos_selec1.size());
		int puntoCruce2 = (int) (Math.random()*nodos_selec2.size());
		
		//copiamos los cromosomas padre en los hijos
		CromosomaP3 hijo1 = (CromosomaP3) padre1.clone();
		CromosomaP3 hijo2 = (CromosomaP3) padre2.clone();
		
		//Cogemos los nodos de cruce seleccionados
		GenArbol temp1 = (GenArbol) nodos_selec1.get(puntoCruce1).clone();
		GenArbol temp2 = (GenArbol) nodos_selec2.get(puntoCruce2).clone();
		
		//realizamos el corte sobre los arboles de los hijos
		corte(hijo1, temp2, puntoCruce1, temp1.esRaiz());
		corte(hijo2, temp1, puntoCruce2, temp2.esRaiz());
		
		/*
		int nodos = hijo1.getArbol().size();
		hijo1.getArbol().setNumNodos(nodos);
		nodos = hijo2.getArbol().obtieneNodos(hijo2.getArbol(), 0);
		hijo2.getArbol().setNumNodos(nodos);
		*/
		
		//Finalmente se evalúan
		hijo1.evalua();
		hijo2.evalua();
		
		padre1 = (CromosomaP3) hijo1.clone();
		padre2 = (CromosomaP3) hijo2.clone();
		}
		
		private static void corte(CromosomaP3 hijo, GenArbol temp, int puntoCruce, boolean esRaiz) 
		{
			if(esRaiz)
			//dependiendo de qué tipo era el nodo que ya no va a estar, se inserta el nuevo
				hijo.insertFuncion(hijo.getArbol().getHijos(), temp, puntoCruce, 0);	
			else
				hijo.insertTerminal(hijo.getArbol().getHijos(), temp, puntoCruce, 0);
		}
		
		
		
		private static ArrayList<GenArbol> obtieneNodos(GenArbol arbol) 
		{
			ArrayList<GenArbol> nodos = new ArrayList<GenArbol>();
			//Obtenemos una probabilidad al azar
			if(seleccionaFunciones())
			{//Si devuelve true, el corte se hará en una función
				arbol.getRaices(arbol.getHijos(), nodos);
				
				if(nodos.size() == 0)//Si no existen funciones, se seleccionan los terminales
					arbol.getHojas(arbol.getHijos(), nodos);
			}
			 else//Si devuelve false, el corte se hará por un terminal
				 arbol.getHojas(arbol.getHijos(), nodos);
			
			return nodos;
		}
		
		
		private static boolean seleccionaFunciones()
		{
			double prob = Math.random();
			if(prob < PROB_FUNC)
				return true;
			else
				return false;
		}
}



