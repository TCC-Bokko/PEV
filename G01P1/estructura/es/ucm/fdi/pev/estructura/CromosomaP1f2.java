package es.ucm.fdi.pev.estructura;

import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public class CromosomaP1f2 extends CromosomaBinario {

	public CromosomaP1f2(Cromosoma c) {
		super(c);
	}

	public CromosomaP1f2(GenBinario[] g) {
		super(g);
	}

	@Override
	public float evalua() 
	{
		fenotipos();
		
		float result = (float) FuncionesEv.f2holderTable(fenotipos[0], fenotipos[1]);
		
		fitness = result;
		
		return result;
	}

	@Override
	public boolean compara_mejor_fitness(float f) {
		return fitness < f;
	}

	@Override
	public int compareTo(Cromosoma c) {
		
		if(this.fitness < c.getFitness())
			return -1;
		else if(this.fitness > c.getFitness())
			return 1;
		
		return 0;
	}
}
