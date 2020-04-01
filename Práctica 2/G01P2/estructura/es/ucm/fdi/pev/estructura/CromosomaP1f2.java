package es.ucm.fdi.pev.estructura;

import java.util.ArrayList;

import es.ucm.fdi.pev.Utils.Pair;
import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public class CromosomaP1f2 extends CromosomaBinario {

	
	public CromosomaP1f2() {
		super();
	}
	
	public CromosomaP1f2(Cromosoma c) {
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

	@Override
	public Cromosoma clone() {
		return new CromosomaP1f2(this);
	}

	@Override
	public boolean compara_peor_fitness(float f) {
		// TODO Auto-generated method stub
		return false;
	}


	
}
