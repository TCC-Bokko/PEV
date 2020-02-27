package es.ucm.fdi.pev.cruce;
import es.ucm.fdi.pev.estructura.*;

import java.util.ArrayList;
import java.util.Random;

public class Monopunto 
{
	
	public static void monopunto(Cromosoma c1, Cromosoma c2) 
	{
		int l1 = c1.getLongitud();
		
		Gen[] g_c1 = c1.getGenes().clone();
		Gen[] g_c2 = c2.getGenes().clone();	
		Gen[] g_aux = g_c1.clone();
		
		//Elegimos un punto al azar
		Random r = new Random();
		int corte = r.nextInt(l1);
		
		int i = 0;
		int j = 0;
		
		// Avanzamos por genes
		for (Gen g : g_c1)
		{
			i += g.size() - 1;
			
			// Si corta entre medias de un gen:
			if(i > corte)
			{
				// Intercambiamos en ese gen hasta el corte
				for(int c = 0; c < g.size() - (i - corte); c++)
				{
					g.cruce(c, g_c2[j]);
					//g_c2.set(j, g2);
				}
				break; 
			}
			
			// Si no, intercambiamos el gen por completo
			else
			{
				g_c1[j] = g_c2[j];
				g_c2[j] = g_aux[j];
			}
				
			i++;// Reseteamos "i", que hemos modificado para cuadrar los indices
			j++;
		}
		
		c1.setGenes(g_c1);
		c2.setGenes(g_c2);		
	}
}

