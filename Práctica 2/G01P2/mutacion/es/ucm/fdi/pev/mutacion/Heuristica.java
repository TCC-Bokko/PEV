package es.ucm.fdi.pev.mutacion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;

public class Heuristica {

	public static void heuristica(Cromosoma c, float prob)
	{
		Random r = new Random();
		float rand = r.nextFloat();
		
		if(rand < prob)
			muta(c);
	}

	private static void muta(Cromosoma c) 
	{
		Random r = new Random();
		// Escoger n = 2 ó 3 como mucho para no meter ruido
		int n = r.nextInt(2) + 2;
	
		int[] indices = indices(n, c.getLongitud());
			
		 Cromosoma[] candidatos = candidatos(c, indices, n);
		 
		 Cromosoma candidato = evaluaCandidatos(candidatos);
		 
		 c.setGenes(candidato.getGenes());
	}
	
	
	
	// Obtiene los indices (sin repeticiones) de los genes con los que se generaran las permutaciones 
	private static int[] indices(int n, int tam)
	{
		Random r = new Random();
		
		// 1) Inicializamos con los posibles valores a obtener (en este caso, simplemente los indices)
		int[] idx = new int[n];
		Set<Integer> values = new HashSet<Integer>();	
		for(int i = 0; i < tam; i++)
			values.add(i);
		
		
		// 2) Obtenemos los indices sin repeticiones
		int added = 0;
		while(added < n)
		{
			int value = r.nextInt(tam);
			
			if(values.remove(value))
			{
				idx[added] = value;
				added++;
			}
		}
		
		return idx;
	}
	

	// Genera los posibles candidatos en funcion de las permutaciones creadas
	private static Cromosoma[] candidatos(Cromosoma c, int[] indices, int tam)
	{
		ArrayList<Gen[]> permutaciones = new ArrayList<Gen[]>();
		Gen[] valores = new Gen[tam];
		
		// 1) Obtenemos los valores a permutar	
		for(int i = 0; i < indices.length; i++)
		{
			int idx = indices[i];
			valores[i] = c.getGenes()[idx];
		}			
		// 2) Obtenemos las posibles permutaciones
		permuta(permutaciones, valores, 0);
		
		// 2) Generamos los posibles candidatos con las permutaciones obtenidas
		Cromosoma[] candidatos = new Cromosoma[permutaciones.size()];	
		
		for (int i = 0; i < permutaciones.size(); i++)	
			candidatos[i] = creaCandidato(c, permutaciones.get(i), indices);
		
		
		return candidatos;
	}
	
	
	// Crea un candidato dada una permutacion
	private static Cromosoma creaCandidato(Cromosoma c, Gen[] p, int[] indices) 
	{
		// 1) Creamos el clon del cromosoma 
		Cromosoma candidato = c.clone();
		Gen[] genes = candidato.getGenes();
		
		// 2) Cambiamos los genes en las posiciones correspondientes con los valores de cada permutacion
		for(int i = 0; i < indices.length; i++)
		{
			int idx = indices[i];
			genes[idx] = p[i];
		}
		
		candidato.setGenes(genes);
		
		return candidato;
	}
	
	
	// Devuelve el mejor candidato de entre los posibles
	private static Cromosoma evaluaCandidatos(Cromosoma[] candidatos)
	{
		// 1) Inicializamos con el primer candidato
		Cromosoma mejor = candidatos[0];
		mejor.evalua();
		
		// 2) Comparamos con el resto para obtener el mejor
		for(int i = 1; i < candidatos.length; i++)
		{
			float fitness = candidatos[i].evalua();
			
			if(!mejor.compara_mejor_fitness(fitness))
				mejor = candidatos[i];
		}
				
		return mejor;
	}
	
	
	 private static void permuta(ArrayList<Gen[]> p, Gen[] valores, int currentIdx) 
	    { 
	        if (currentIdx == valores.length - 1)    
	            p.add(valores);    
	        
	        else
	            for (int i = currentIdx; i < valores.length; i++) 
	            { 
	            	swap(valores, currentIdx, i); 
	                permuta(p, valores.clone(), currentIdx + 1); 
	                swap(valores, currentIdx, i); 
	            } 	        
	    }
	 
	 
	 private static void swap(Gen[] indices, int i, int j) 
	    { 
	         Gen aux = indices[i];
	        
	        indices[i] = indices[j]; 
	        indices[j] = aux;
	    }
}

