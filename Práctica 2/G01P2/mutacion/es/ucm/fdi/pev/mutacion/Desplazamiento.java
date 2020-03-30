package es.ucm.fdi.pev.mutacion;

import java.util.Random;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;

public class Desplazamiento {

	public static void desplazamiento(Cromosoma c, float prob) 
	{	
		Random r = new Random();
		float rand = r.nextFloat();
		
		if(rand < prob)
			muta(c);		
	}

	private static void muta(Cromosoma c) 
	{
		Random r = new Random();
		
		// Rango de desplazamientos que se van a realizar, elegidas de manera aleatoria
		int reps = 3;
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
			swap(genes, pos, 0);
		else
			swap(genes, pos, pos + 1);
		
		
		c.setGenes(genes);
	}
	
	
	
	private static void swap(Gen[] genes, int i, int j) 
	{ 
	    Gen aux = genes[i];
	    
	    genes[i] = genes[j]; 
	    genes[j] = aux;
	}
}

	
