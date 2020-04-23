package es.ucm.fdi.pev.mutacion;

import es.ucm.fdi.pev.estructura.Cromosoma;

public class Basica {

	public static boolean basica(Cromosoma c, float prob) 
	{
		boolean haMutado = false;
		haMutado = c.muta(prob);
		return haMutado;
	}
}
