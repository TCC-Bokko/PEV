package es.ucm.fdi.pev.estructura;

import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public class CromosomaP1f1 extends CromosomaBinario {

	
	public CromosomaP1f1(Cromosoma c) {
		super(c);
	}
	
	
	
	public CromosomaP1f1(GenBinario[] g) {
	super(g);
	}


	@Override
	public float evalua() 
	{	
		fenotipos();
		
		float result = (float) FuncionesEv.funcion1(fenotipos[0], fenotipos[1]);
		
		fitness = result;
		
		return result;
	}

	@Override
	public boolean compara_mejor_fitness(float f) {
		return fitness > f;
	}
}
