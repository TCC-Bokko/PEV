package es.ucm.fdi.pev.mutacion;

import java.util.Random;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;

public class Intercambio {

	public static void intercambio(Cromosoma c, float prob) 
	{	
		Random r = new Random();
		float rand = r.nextFloat();
		
		if(rand < prob)
			muta(c);	
	}

	private static void muta(Cromosoma c) 
	{
		Random r = new Random();
		
		Gen[] genes = c.getGenes();
		int pos1, pos2; 
		
		// 1) Nos aseguramos de que no sale la misma posicion
		do
		{
			pos1 = r.nextInt(c.getLongitud());
			pos2 = r.nextInt(c.getLongitud());
		}
		while(pos1 == pos2 && genes.length > 1);
		
		// 2) Realizamos el swap de genes en las posiciones correspondientes
		swap(genes, pos1, pos2);
		
		c.setGenes(genes);
	}
	
	
	private static void swap(Gen[] genes, int i, int j) 
	{ 
        Gen aux = genes[i];
        
        genes[i] = genes[j]; 
        genes[j] = aux;
	}
	
}
