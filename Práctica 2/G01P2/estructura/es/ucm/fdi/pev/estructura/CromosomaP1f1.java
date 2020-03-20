package es.ucm.fdi.pev.estructura;

import java.util.ArrayList;

import es.ucm.fdi.pev.Utils.Pair;
import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public class CromosomaP1f1 extends CromosomaBinario {

	public CromosomaP1f1() {
		super();
	}
	
	public CromosomaP1f1(Cromosoma c) {
		super(c);
	}
	

	
	@Override
	protected ArrayList<Pair<Float, Float>> inicializaGenes() {
		
		tolerancia = 0.01f;
		// Numero de genes que compone cada cromosoma, y su tamaño (indice: nº de gen, valor: tam del gen)
		ArrayList<Pair<Float, Float>> genes_len = new ArrayList<Pair<Float, Float>>();
		float Xmin, Xmax;
					
		
		Xmin = -3.0f; Xmax = 12.1f;
		genes_len.add(new Pair<Float, Float>(Xmin, Xmax));
			
		Xmin = 4.1f; Xmax = 5.8f;
		genes_len.add(new Pair<Float, Float>(Xmin, Xmax));
			
		return genes_len;
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



	@Override
	public int compareTo(Cromosoma c) {
		
		if(this.fitness < c.getFitness())
			return 1;
		else if(this.fitness > c.getFitness())
			return -1;
		
		return 0;
	}



	@Override
	public Cromosoma clone() {
		return new CromosomaP1f1(this);
	}
}


