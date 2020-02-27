package es.ucm.fdi.pev.estructura;
import java.util.ArrayList;


public abstract class Cromosoma {
	
	//Cromosoma compuesto de array de genes
	protected ArrayList<Gen> genes;
	
	//Representacion de los genes
	protected float[] fenotipos;
	//float genotipo;

	//Funciones
	protected float fitness;
	protected float puntuacion; // Puntuación (o fitness relativo en función del total). Usado en ruleta y restos
	protected float punt_acum;
	
	//Tamaño total de los genes
	protected int longitud;
	
	
	public Cromosoma()
	{
		genes = new ArrayList<Gen>();
	}
	
	
	public Cromosoma(Cromosoma c)
	{
		this.genes = new ArrayList<Gen>(c.genes);
		this.fenotipos = c.fenotipos;
		this.fitness = c.fitness;
		this.puntuacion = c.puntuacion;
		this.punt_acum = c.punt_acum;
		this.longitud = c.longitud;
	}
	
	
	abstract public float[] fenotipos(); // Devuelve el fenotipo de cada gen
	abstract public String genotipos();
	abstract public float evalua();
	abstract public boolean compara_mejor_fitness(Cromosoma c);
	
	public void addGen(Gen g)
	{
		genes.add(g);
		longitud += g.size();
	}
	
	public void muta(float prob)
	{
		for(Gen g: genes)
			g.muta(prob);
	}
	
	public void actualiza_puntuacion(float fitness_total) { puntuacion = fitness / fitness_total; }
	public float getPuntuacion() { return puntuacion; }
	
	public float getPuntuacionAcum() { return punt_acum; }
	public void actualiza_punt_acum(float acum) { punt_acum = acum + puntuacion; }
	
	public void setFitness(float f) { fitness = f; }
	public float getFitness() {	return fitness; }
	
	public int getLongitud() { return longitud; }
	
	
	public void setGenes(ArrayList<Gen> new_g) { genes = new_g; }
	public ArrayList<Gen> getGenes() { return genes; }
}


