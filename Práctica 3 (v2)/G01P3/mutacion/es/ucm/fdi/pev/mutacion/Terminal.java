package es.ucm.fdi.pev.mutacion;

import java.util.ArrayList;
import java.util.Random;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaP3;
import es.ucm.fdi.pev.estructura.GenArbol;

public class Terminal {
	public static boolean terminal(Cromosoma c, int entradas, float prob) 
	{	
		boolean haMutado = false;
		Random r = new Random();
		float rand = r.nextFloat();
		
		if(rand < prob) {
			haMutado = muta((CromosomaP3) c, entradas);
		}
		return haMutado;
	}
	
	private static boolean muta(CromosomaP3 c, int entradas)
	{
		GenArbol arbol = c.getArbol();
	
		
		// 1) Obtenemos todos los terminales del arbol, incluida la raiz si esta lo fuera tambien 
		ArrayList<GenArbol> terminales = new ArrayList<GenArbol>();
		arbol.getHojas(arbol.getHijos(), terminales);
		
		if(arbol.esHoja())
			terminales.add(arbol);
		
		
		// 2) Obtenemos una de ellas y sustituimos por un terminal al azar de entre los posibles
		int puntoMutacion = (int) (Math.random() * terminales.size());
		
		
		// 3) Diferenciamos los casos posibles de terminales en funcion del tam. de la entrada del multiplexor
		int rnd;
		String valor;
		switch (entradas) 
		{
		case 2: 
			rnd = (int) (Math.random() * CromosomaP3.terminales6.length);
			valor = CromosomaP3.terminales6[rnd]; 
			break;
			
		case 3:
			rnd = (int) (Math.random() * CromosomaP3.terminales11.length);
			valor = CromosomaP3.terminales11[rnd]; 
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + entradas);
		}
		
				
		GenArbol terminal = terminales.get(puntoMutacion);
		terminal.setValor(valor);
		
		return true;
	}
}
