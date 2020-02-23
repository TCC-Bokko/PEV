package es.ucm.fdi.pev;

import es.ucm.fdi.pev.estructura.*;
import es.ucm.fdi.pev.Utils.*;
import java.util.ArrayList;


public class AGeneticoEj1 extends AGenetico {

	

	public AGeneticoEj1(int tamPob) {
		super(tamPob);
		
	}

	@Override
	protected void evaluaCromosoma(Cromosoma c) 
	{
		
	}
	
	
	
	protected void inicializaGenes() 
	{
		ArrayList<Integer> genes_l = new ArrayList<Integer>();
		genes_l.add(Utils.calculaLongitud(0, 10, 1));
		genes_l.add(Utils.calculaLongitud(0, 4, 1));
		
		genes_len = genes_l;
	}

	@Override
	protected void inicializaPoblacion() 
	{
		inicializaGenes();
		
		poblacion = new CromosomaReal[tamPoblacion];
		
		for(int i = 0; i < tamPoblacion; i++)
		{
			poblacion[i] = inicializaCromosoma();
		}	
	}

	@Override
	protected Cromosoma inicializaCromosoma() 
	{
		CromosomaReal c = new CromosomaReal(tolerancia);
		
		for(int genTam : genes_len)
		{
			Gen g = new GenBinario(genTam);
			g.randomInit();
			c.addGen(g);
		}
		
		return c;
	}
}
