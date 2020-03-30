package es.ucm.fdi.pev.mutacion;

import java.util.Random;

import es.ucm.fdi.pev.Utils.Utils;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;

public class Insercion {

	public static void insercion(Cromosoma c, float prob) 
	{	
		Random r = new Random();
		float rand = r.nextFloat();
		
		if(rand < prob)
			muta(c);	
	}
	
	
	private static void muta(Cromosoma c)
	{
		Random r = new Random();
		
		// Rango de inserciones que se van a realizar, elegidas de manera aleatoria
		int reps = 3;
		reps = r.nextInt(reps) + 1; 
		
		for(int i = 0; i < reps; i++)
			inserta(c);		
	}
	
	
	private static void inserta(Cromosoma c)
	{
		Random r = new Random();
		
		Gen[] genes = c.getGenes();
		
		int pos, newPos; 
			
		// 1) Nos aseguramos de que no sale la misma posicion
		do
		{
			pos = r.nextInt(c.getLongitud());
			newPos = r.nextInt(c.getLongitud());
		}
		while(pos == newPos && genes.length > 1);
		
		
		// 2) Miramos hacia qué lado tenemos que desplazarnos
		if(newPos < pos)
			for(int i = pos; i > newPos; i--)			
				Utils.swap(genes, i, i-1);
		else
			for(int i = pos; i < newPos; i++)
				Utils.swap(genes, i, i+1);
			
		
		c.setGenes(genes);
	}
}
