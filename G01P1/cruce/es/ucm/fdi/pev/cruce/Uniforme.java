package es.ucm.fdi.pev.cruce;
import java.util.ArrayList;
import java.util.Random;

import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;

public class Uniforme {

	public static void uniforme(Cromosoma c1, Cromosoma c2) {
		float prob_int = 0.5f;
		ArrayList<Gen> g_c1 = new ArrayList<Gen>(c1.getGenes());
		ArrayList<Gen> g_c2 = new ArrayList<Gen>(c2.getGenes());
		ArrayList<Gen> g_aux = new ArrayList<Gen>(g_c1);
		
		Random r = new Random();
	//	int corte = r.nextFloat();
		
		//Por cada  gen (c1 y c2 tienen los mismos)
		for (int i = 0; i < g_c1.size(); i++)
		{
			//recorremos el tamaño del gen
			//for (int j = 0; j < g_c1[i].)
		}
		
	}
}
