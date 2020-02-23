package es.ucm.fdi.pev;

import es.ucm.fdi.pev.estructura.*;
import java.util.ArrayList;


public class AGeneticoEj1 extends AGenetico {

	

	public AGeneticoEj1(int tamPob, ArrayList<Integer> genes) {
		super(tamPob, genes);
		
	}

	@Override
	protected void evaluaCromosoma(Cromosoma c) 
	{
		
	}

	@Override
	protected void inicializaPoblacion() 
	{
		poblacion = new CromosomaBinario[tamPoblacion];
		
		for(int i = 0; i < tamPoblacion; i++)
		{
			poblacion[i] = inicializaCromosoma();
			System.out.println(poblacion[0].getLongitud());
		}	
	}

	@Override
	protected Cromosoma inicializaCromosoma() 
	{
		CromosomaBinario c = new CromosomaBinario();
		
		for(int genTam : numGenes)
		{
			Gen g = new GenBinario(genTam);
			g.randomInit();
			c.addGen(g);
		}
		
		return c;
	}
}
