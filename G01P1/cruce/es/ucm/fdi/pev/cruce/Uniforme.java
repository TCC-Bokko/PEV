package es.ucm.fdi.pev.cruce;
import java.util.ArrayList;
import java.util.Random;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.Gen;
import es.ucm.fdi.pev.estructura.GenBinario;

public class Uniforme {

	public static void uniforme(Cromosoma c1, Cromosoma c2) {
		//Obtenemos lista de genes
		ArrayList<Gen> g_c1 = new ArrayList<Gen>(c1.getGenes());
		ArrayList<Gen> g_c2 = new ArrayList<Gen>(c2.getGenes());
		float prob_cambio = 0.5f;
		float r_result = 0.0f;
		String tipo = c1.getGenes().get(0).getType();
		Random r = new Random();

		//////// BINARIO
		if (tipo == "binario") {
			Boolean[] genA;
			Boolean[] genB;
			Boolean[] genAux;
			
			//Por cada  gen (c1 y c2 tienen los mismos)
			for (int i = 0; i < g_c1.size(); i++){
				//Cast del Gen actual a Gen Binario
				GenBinario gb_c1 = (GenBinario) g_c1.get(i);
				GenBinario gb_c2 = (GenBinario) g_c2.get(i);
				
				//Obtenemos los bits
				genA = gb_c1.getBits();
				genB = gb_c2.getBits();
				genAux = genA;
				
				//recorremos el tamaño del gen bit a bit
				for (int j = 0; j < genA.length; j++) {
					//Random
					r_result = r.nextFloat();
					//Si supera el umbral intercambiamos el bit
					if (r_result < prob_cambio) {
						genA[j] = genB[j];
						genB[j] = genAux[j];
					}
				}
				
				//
				gb_c1.setBits(genA);
				gb_c2.setBits(genB);
				g_c1.set(i, gb_c1);
				g_c2.set(i, gb_c2);
				
			}		
			c1.setGenes(g_c1);
			c2.setGenes(g_c2);
		}
		///////// REAL
		else if (tipo == "real") {
			
		}
		
	}
}
