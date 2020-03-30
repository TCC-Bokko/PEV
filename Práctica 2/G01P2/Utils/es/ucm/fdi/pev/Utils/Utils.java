package es.ucm.fdi.pev.Utils;

import java.util.ArrayList;

import es.ucm.fdi.pev.estructura.Gen;

public class Utils {
	
		// Calcula el logaritmo en base 'b' de 'x'
		public static int log(float x, float b)
		{
		    return (int) (Math.log(x) / Math.log(b));
		}
		
		
		// Calcula la longitud en bits necesaria dado el intervalo [Xmin, Xmax]
		public static int longitud_bits(float Xmin, float Xmax, float tol)
		{	
			float aux = 1 + ((Xmax - Xmin) / tol);	
			return log(aux, 2);
		}
		
		public static float bin2dec(Boolean[] booleans)
		{
			float result = 0;
			for (boolean bit : booleans) {
			    result = result * 2 + (bit ? 1 : 0);    
			}
			
			return result;
		}
		
		
		public static int factorial(int n) 
		{
		    return n == 1 ? 1 : n * factorial(n-1);
		}
		
		
		public static void swap(Gen[] genes, int i, int j) 
		{ 
	        Gen aux = genes[i];
	        
	        genes[i] = genes[j]; 
	        genes[j] = aux;
		}
}
