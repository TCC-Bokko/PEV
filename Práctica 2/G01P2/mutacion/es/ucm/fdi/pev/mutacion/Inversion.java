package es.ucm.fdi.pev.mutacion;

import java.util.Random;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;

public class Inversion {

	public static void inversion(Cromosoma c, float prob) 
	{	
		Random r = new Random();
		float rand = r.nextFloat();
		
		if(rand < prob)
			muta(c);	
	}
	
	
	private static void muta(Cromosoma c)
	{
		Random r = new Random();
		
		int tam = c.getGenes().length;
		// 1) Obtenemos los cortes para invertir los genes
		int ini = r.nextInt(tam);
		int fin = r.nextInt(tam);
				
		if(fin < ini)
		{
			int aux = ini;
			ini = fin; fin = aux;
		}
				
		
		// 2) Invertimos los genes de un punto al otro
		Gen[] genes = c.getGenes();
				
		int j = fin;
		for(int i = ini; i < j; i++)
		{
			swap(genes, i, j);			
			j--;
		}
		
		c.setGenes(genes);
	}
	
	private static void swap(Gen[] genes, int i, int j) 
	{ 
        Gen aux = genes[i];
        
        genes[i] = genes[j]; 
        genes[j] = aux;
	}
}


