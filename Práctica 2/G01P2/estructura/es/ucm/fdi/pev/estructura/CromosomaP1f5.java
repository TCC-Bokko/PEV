package es.ucm.fdi.pev.estructura;

import java.util.ArrayList;

import es.ucm.fdi.pev.Utils.Pair;
import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public class CromosomaP1f5 extends CromosomaReal {

	

	public CromosomaP1f5() {
		super();
	}
	
	
	public CromosomaP1f5(Cromosoma c) {
		super(c);
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
		return this.fitness < f;
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
	
		return new CromosomaP1f5(this);
	}


	@Override
	ArrayList<Pair<Float, Float>> inicializaGenes() {
		float pi = 3.14159265359f;
		
		int n = 7;
		
		float Xmin, Xmax;
		ArrayList<Pair<Float, Float>> genes_l = new ArrayList<Pair<Float, Float>>();
		
		for(int i = 0; i < n ; i++)
		{
			 Xmin = 0f; Xmax = pi;
				genes_l.add(new Pair<Float, Float>(Xmin, Xmax));
		}		
		
		return genes_l;
	}
}


