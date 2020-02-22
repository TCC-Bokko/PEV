package es.ucm.fdi.pev.cruce;
import es.ucm.fdi.pev.estructura.*;
import java.util.Random;

public class Monopunto<T> 
{
	public void monopunto(Cromosoma<T> c1, Cromosoma<T> c2) 
	{
		int l1 = c1.getLongitud();
		Gen<T>[] g_c1 = c1.getGenes();
		Gen<T>[] g_c2 = c2.getGenes();
		Gen<T>[] g_aux = g_c1;
		
		//Elegimos un punto al azar
		Random r = new Random();
		int corte = r.nextInt(l1);
		
		// Cambiamos el frente
		for (int i = 0; i < corte; i++) {
			g_c1[i] = g_c2[i];
			g_c2[i] = g_aux[i];
		}
		
		c1.setGenes(g_c1);
		c2.setGenes(g_c2);		
	}
}
