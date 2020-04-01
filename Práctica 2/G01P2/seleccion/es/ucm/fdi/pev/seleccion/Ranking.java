package es.ucm.fdi.pev.seleccion;

import java.util.Arrays;
import java.util.Random;

import es.ucm.fdi.pev.estructura.Cromosoma;

public class Ranking {

	public static int[] ranking(Cromosoma[] poblacion) 
	{
		int tam = poblacion.length;	
		int[] pob_idx = new int[tam];
		double[] ranking = new double[tam];
		
		Random r = new Random();
		
		// Obtenemos Beta como valor entre 1 y 2
		int B = r.nextInt(2) + 1;
		
		
		// 1) Ordenamos la poblacion por su fitness
		Arrays.sort(poblacion);
		
		// 2) Calculamos la probabilidad de seleccion de cada posicion en la poblacion
		ranking = calcula_ranking(tam, B);
		
		// 3) Seleccionamos aleatoriamente a los individuos segun su posicion en el "ranking" calculado anteriormente
		for (int i = 0; i < poblacion.length; i++ ) {
			pob_idx[i] = seleccion(poblacion, ranking);
		}
		
		return pob_idx;		
	}
	
	
	// Calcula la probabilidad de seleccionar cada individuo de la poblacion por su posicion, asumiendo que esta ordenada
	private static double[] calcula_ranking(int tam, int B) 
	{	
		double[] probabilidades = new double[tam];
		
		for(int i = 0; i < tam; i++)
		{
			double prob = (double) i/tam;
			prob = prob*2*(B - 1);
			prob = B - prob;
			prob = prob*((double) 1/tam);
			
			
			if(i == 0)
				probabilidades[i] = prob;
			else
				probabilidades[i] = probabilidades[i-1] + prob;	
		}
		
		return probabilidades;
	}
	
	
	private static int seleccion(Cromosoma[] poblacion, double[] prob) {
		double r = Math.random();
	
		int idx = 0;
		while(r > prob[idx] && idx < poblacion.length)
			idx++;
		
			
		return idx;		
	}
}
