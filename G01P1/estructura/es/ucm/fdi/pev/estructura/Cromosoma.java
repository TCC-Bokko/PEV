package es.ucm.fdi.pev.estructura;
import java.util.ArrayList;


public abstract class Cromosoma {
	
	//Cromosoma compuesto de array de genes
	ArrayList<Gen> genes; 
	
	//Representacion de los genes
	float fenotipo;
	float genotipo;

	//Funciones
	float fitness;
	float puntuacion;
	float punt_acum;
	float prob_rel_fitness; // Usado en ruleta y restos
	
	//Tamaño total de los genes
	int longitud;
	
	
	public Cromosoma()
	{
		genes = new ArrayList<Gen>();
	}
	
	
	abstract public float fenotipo();
	abstract public float evalua();
	
	public void addGen(Gen g)
	{
		genes.add(g);
		longitud+= g.size();
	}
	
	public float getFitness() {
		return fitness;
	}
	
	public float getRelFit() {
		return prob_rel_fitness;
	}
	
	public int getLongitud() {
		return longitud;
	}
	
	public ArrayList<Gen> getGenes() {
		return genes;
	}
	
	public void setGenes(ArrayList<Gen> new_g) {
		genes = new_g;
	}
	
	public void setRelFit(float fitness_total) {
		prob_rel_fitness = fitness / fitness_total;
	}	
}
