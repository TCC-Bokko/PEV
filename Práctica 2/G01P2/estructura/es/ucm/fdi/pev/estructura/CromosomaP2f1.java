package es.ucm.fdi.pev.estructura;

import java.util.ArrayList;
import java.util.Collections;

import es.ucm.fdi.pev.Utils.Pair;
import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public class CromosomaP2f1 extends CromosomaEntero {

	int[][] distancias;
	int[][] flujos;
	
	
	public CromosomaP2f1(Cromosoma c) {
		super(c);
	}
	
	public CromosomaP2f1(int tam, int[][] d, int[][] f)
	{
		longitud = tam;
		
		distancias = d;
		flujos = f;
		
		inicializaCromosoma();
	}
	
	@Override
	ArrayList<Pair<Float, Float>> inicializaGenes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void inicializaCromosoma() {
		
		genes = new GenEntero[longitud];
		fenotipos = new float[longitud];
		
		ArrayList<GenEntero> aux = new ArrayList<GenEntero>();
		
		for(int i = 0; i < longitud; i++)
		{
			GenEntero g = new GenEntero(i);
			aux.add(g);
		}
		
		Collections.shuffle(aux);
		aux.toArray(genes);
	}
	

	@Override
	public Cromosoma clone() {

		return new CromosomaP2f1(this);
	}

	@Override
	public float evalua() {
			fenotipos();
			
			float result = (float) FuncionesEv.f6p2(this.fenotipos(), distancias, flujos);
			
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
}
