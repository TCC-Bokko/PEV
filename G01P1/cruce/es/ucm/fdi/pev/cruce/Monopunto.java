package es.ucm.fdi.pev.cruce;
import es.ucm.fdi.pev.estructura.*;

import java.util.ArrayList;
import java.util.Random;

public class Monopunto<T> 
{
	public void monopunto(Cromosoma c1, Cromosoma c2) 
	{
		int l1 = c1.getLongitud();
		ArrayList<Gen> g_c1 = c1.getGenes();
		ArrayList<Gen> g_c2 = c2.getGenes();
		ArrayList<Gen> g_aux = g_c1;
		
		//Elegimos un punto al azar
		Random r = new Random();
		int corte = r.nextInt(l1);
		
		// Cambiamos el frente
		for (int i = 0; i < corte; i++) {
			g_c1.set(i, g_c2.get(i));
			g_c2.set(i, g_aux.get(i));
		}
		
		c1.setGenes(g_c1);
		c2.setGenes(g_c2);		
	}
}
