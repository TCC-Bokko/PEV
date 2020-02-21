package es.ucm.fdi.pev.estructura;


public abstract class Cromosoma<T> {
	
	//Cromosoma compuesto de array de genes
	Gen<T>[] genes; 
	
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
	
	abstract public float fenotipo();
	abstract public float evalua();
	
	public float getFitness() {
		return fitness;
	}
	
	public float getRelFit() {
		return prob_rel_fitness;
	}
	
	public int getLongitud() {
		return longitud;
	}
	
	public Gen<T>[] getGenes() {
		return genes;
	}
	
	public void setGenes(Gen<T>[] new_g) {
		genes = new_g;
	}
	
	public void setRelFit(float fitness_total) {
		prob_rel_fitness = fitness / fitness_total;
	}
	

	
	
}
