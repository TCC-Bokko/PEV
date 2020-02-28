package es.ucm.fdi.pev.cruce;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenReal;

public class Aritmetico {

	
	// PARA CRUCES ENTRE GENES REALES
	public static void aritmetico(Cromosoma c1, Cromosoma c2)
	{
		Gen[] g_c1 = c1.getGenes().clone();
		Gen[] g_c2 = c2.getGenes().clone();	
		
		
		float alpha = 0.5f;
		int len = c1.getLongitud();
		
			
		for (int i = 0; i < len; i++)
		{
			GenReal g1 = (GenReal) g_c1[i];
			GenReal g2 = (GenReal) g_c2[i];
				
			float a1 = g1.getAlelo();
			float a2 = g2.getAlelo();
			
			g1.setAlelo(alpha*a1 + (1f-alpha)*a2);
			g2.setAlelo(alpha*a2 + (1f-alpha)*a1);	
			
			g_c1[i] = g1;
			g_c2[i] = g2;
		}
		
		c1.setGenes(g_c1);
		c2.setGenes(g_c2);
	}
}
