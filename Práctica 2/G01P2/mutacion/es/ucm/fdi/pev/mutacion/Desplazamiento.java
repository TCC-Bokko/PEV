package es.ucm.fdi.pev.mutacion;

import java.util.Random;

import es.ucm.fdi.pev.Utils.Utils;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;

public class Desplazamiento {

	public static boolean desplazamiento(Cromosoma c, float prob) 
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
		Random r = new Random();
		
		// Rango de desplazamientos que se van a realizar, elegidas de manera aleatoria
		int reps = 2;
		reps = r.nextInt(reps) + 1; 
		
		for(int i = 0; i < reps; i++)
			desplaza(c);		
		
	}

	private static void desplaza(Cromosoma c) 
	{
		Random r = new Random();
		
		Gen[] genes = c.getGenes();
		
		int pos = r.nextInt(c.getLongitud());
		
		
		if(pos == genes.length - 1)
			Utils.swap(genes, pos, 0);
		else
			Utils.swap(genes, pos, pos + 1);
		
		
		c.setGenes(genes);
	}
}

	
