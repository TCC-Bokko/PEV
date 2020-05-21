package es.ucm.fdi.pev.mutacion;

import java.util.ArrayList;
import java.util.Random;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaP3;
import es.ucm.fdi.pev.estructura.GenArbol;

public class Funcion {
	public static boolean funcion(Cromosoma c, float prob) 
	{	
		boolean haMutado = false;
		Random r = new Random();
		float rand = r.nextFloat();
		
		if(rand < prob) {
			haMutado = muta((CromosomaP3) c);
		}
		return haMutado;
	}
	
	private static boolean muta(CromosomaP3 c)
	{
		GenArbol arbol = c.getArbol();
	
		
		// 1) Obtenemos todas las funciones del arbol, incluida la raiz si esta lo fuera tambien 
		ArrayList<GenArbol> funciones = new ArrayList<GenArbol>();
		arbol.getRaices(arbol.getHijos(), funciones);
		
		if(arbol.esRaiz())
			funciones.add(arbol);
		
		
		// 2) Obtenemos una de ellas y miramos su aridad
		int puntoMutacion = (int) (Math.random() * funciones.size());
			
		GenArbol funcion = funciones.get(puntoMutacion);
		int aridad = funcion.getHijos().size();
			
		// 3) Seleccionamos una funcion al azar de entre las posibles segun la aridad
		int rnd;
		String valor;
		switch (aridad)
		{
		case 1: 
			rnd = (int) (Math.random() * CromosomaP3.funcionesAridad1.length);
			valor = CromosomaP3.funcionesAridad1[rnd]; 
			break;
		case 2:
			rnd = (int) (Math.random() * CromosomaP3.funcionesAridad2.length);
			valor = CromosomaP3.funcionesAridad2[rnd];
			break;
			
		case 3:
			rnd = (int) (Math.random() * CromosomaP3.funcionesAridad3.length);
			valor = CromosomaP3.funcionesAridad3[rnd];
			break;
			
		default:
			throw new IllegalArgumentException("Unexpected value: " + aridad);
		}
		
		
		// 4) Mutamos con la nueva funcion
		funcion.setValor(valor);
		
		return true;
	}
}
