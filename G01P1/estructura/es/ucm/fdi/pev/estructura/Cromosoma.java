package es.ucm.fdi.pev.estructura;
import java.util.ArrayList;


public abstract class Cromosoma {
	
	//Cromosoma compuesto de array de genes
	protected Gen[] genes;
	
	//Representacion de los genes
	protected float[] fenotipos;
	//float genotipo;

	//Funciones
	protected float fitness;
	protected float puntuacion; // Puntuación (o fitness relativo en función del total). Usado en ruleta y restos
	protected float punt_acum;
	
	//Tamaño total de los genes
	protected int longitud;
	
	public Cromosoma() {}
	public Cromosoma(Cromosoma c)
	{
		
		setGenes(c.getGenes());
		this.fenotipos = c.fenotipos.clone();
		this.fitness = c.fitness;
		this.puntuacion = c.puntuacion;
		this.punt_acum = c.punt_acum;
		this.longitud = c.longitud;
	}
	
	
	abstract public float[] fenotipos(); // Devuelve el fenotipo de cada gen
	abstract public String genotipos();
	abstract public float evalua();
	abstract public void actualiza_puntuacion(float fitness_total);
	abstract public boolean compara_mejor_fitness(float f);
	
	
	
	public void muta(float prob)
	{
		for(Gen g: genes)
			g.muta(prob);
	}
	
	
	public float getPuntuacion() { return puntuacion; }
	public void setPuntuacion(float p) {  puntuacion = p; }
	
	public float getPuntuacionAcum() { return punt_acum; }
	public void actualiza_punt_acum(float acum) { punt_acum = acum + puntuacion; }
	
	public void setFitness(float f) { fitness = f; }
	public float getFitness() {	return fitness; }
	
	public int getLongitud() { return longitud; }
	
	
	public void setGenes(Gen[] new_g) { /*genes = new_g.clone(); */}
	public Gen[] getGenes() { return genes.clone(); }
}


