package es.ucm.fdi.pev.cruce;
import es.ucm.fdi.pev.estructura.*;

import java.util.ArrayList;
import java.util.Random;

public class Monopunto 
{
	public static void monopunto(Cromosoma c1, Cromosoma c2) 
	{
		int l1 = c1.getLongitud() - 1;
		ArrayList<Gen> g_c1 = new ArrayList<Gen>(c1.getGenes());
		ArrayList<Gen> g_c2 = new ArrayList<Gen>(c2.getGenes());
		ArrayList<Gen> g_aux = new ArrayList<Gen>(g_c1);
		
		//Elegimos un punto al azar
		Random r = new Random();
		int corte = r.nextInt(l1);
		
		int i = 0;
		int j = 0;
		
		// Avanzamos por genes
		for (Gen g : g_c1)
		{
			i += g.size();
			
			// Si corta entre medias de un gen:
			if(i > corte)
			{
				
				for(int c = 0; c < i - corte ; c++)
				{
					Gen g2 = g.cruce(c, g_c2.get(j));
					g_c2.set(j, g2);
				}
				break; 
			}
			
			// Si no, intercambiamos el gen por completo
			else
			{
				g_c1.set(j, g_c2.get(j));
				g_c2.set(j, g_aux.get(j));
			}
						
			j++;
		}
		
		c1.setGenes(g_c1);
		c2.setGenes(g_c2);			
	}
}

