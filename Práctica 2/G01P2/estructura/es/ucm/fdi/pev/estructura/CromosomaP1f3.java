package es.ucm.fdi.pev.estructura;

import java.util.ArrayList;

import es.ucm.fdi.pev.Utils.Pair;
import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public class CromosomaP1f3 extends CromosomaBinario {

	public CromosomaP1f3() {
		super();
	}
	
	public CromosomaP1f3(Cromosoma c) {
		super(c);
	}


	
	@Override
	ArrayList<Pair<Float, Float>> inicializaGenes() {
		tolerancia = 0.01f;
		
		float Xmin, Xmax;
		ArrayList<Pair<Float, Float>> genes_l = new ArrayList<Pair<Float, Float>>();
		
		 Xmin = -10f; Xmax = 10f;
		genes_l.add(new Pair<Float, Float>(Xmin, Xmax));
		
		Xmin = -10f; Xmax = 10f;
		genes_l.add(new Pair<Float, Float>(Xmin, Xmax));
		
		return genes_l;
	}
	
	
	
	@Override
	public float evalua()
	{
		fenotipos();
		
		float result = (float) FuncionesEv.f3schubert(fenotipos);
		
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

	@Override
	public Cromosoma clone() {
		// TODO Auto-generated method stub
		return new CromosomaP1f3(this);
	}

	
}
