package es.ucm.fdi.pev.estructura;

import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public class CromosomaP1f4 extends CromosomaBinario {

	public CromosomaP1f4(Cromosoma c) {
		super(c);
	}

	public CromosomaP1f4(GenBinario[] g) {
		super(g);
	}

	@Override
	public float evalua() {
		
fenotipos();
		
		float result = (float) FuncionesEv.f4michalewicz(fenotipos);
		
		fitness = result;
		
		return result;
	}

	@Override
	public boolean compara_mejor_fitness(float f) {
		return fitness < f;
	}
}
