package es.ucm.fdi.pev.mutacion;

import java.util.Random;

import es.ucm.fdi.pev.estructura.Cromosoma;

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
		// WIP - TO DO
		// Terminal: Cambia uno de las hojas operando.
	}
}
