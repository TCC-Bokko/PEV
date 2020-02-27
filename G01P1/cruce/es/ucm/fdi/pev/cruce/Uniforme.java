package es.ucm.fdi.pev.cruce;
import java.util.ArrayList;
import java.util.Random;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenBinario;

public class Uniforme {

	public static void uniforme(Cromosoma c1, Cromosoma c2) {
		/*
		//Obtenemos lista de genes
		ArrayList<Gen> g_c1 = new ArrayList<Gen>(c1.getGenes());
		ArrayList<Gen> g_c2 = new ArrayList<Gen>(c2.getGenes());
		float prob_cambio = 0.3f;

		Random r = new Random();
		
		
		for (int i = 0; i < g_c1.size(); i++)
		{
			Gen g1 = g_c1.get(i);
			Gen g2 = g_c2.get(i);
			
			for(int j = 0; j < g1.size(); j++)
				
				if(r.nextFloat() < prob_cambio)
					g1.cruce(j, g2);		
		}		
		
		c1.setGenes(g_c1);
		c2.setGenes(g_c2);
		*/
	}	
}
