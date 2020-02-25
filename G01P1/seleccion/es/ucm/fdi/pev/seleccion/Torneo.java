package es.ucm.fdi.pev.seleccion;
import java.util.Random;

import es.ucm.fdi.pev.estructura.Cromosoma;

public class Torneo {

	// Seleccion por torneo: pasamos el nº k que representa el tam de los grupos formados, de los cuales escogeremos al mejor individuo
	public static int[] torneo(Cromosoma[] poblacion, int k) 
	{
		int[] pob_idx = new int[poblacion.length];
		
		// Tiradas
		for (int j = 0; j < poblacion.length; j++ ) {
			pob_idx[j] = seleccion(poblacion, k);
		}
				
		return pob_idx;		
		
	}
	
	
	private static int seleccion(Cromosoma[] poblacion, int k)
	{
		Random r = new Random();
		
		int mejor = r.nextInt(poblacion.length);
		for(int i = 1; i < k; i++)
		{	
			int idx = r.nextInt(poblacion.length);
			if(poblacion[idx].compara_mejor_fitness(poblacion[mejor]))
				mejor = idx;	
		}
		
		//Cromosoma c = new CromosomaP1f1(poblacion[i]);
		return mejor;	
	}
}
