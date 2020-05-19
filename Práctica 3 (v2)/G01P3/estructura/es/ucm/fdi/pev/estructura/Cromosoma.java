package es.ucm.fdi.pev.estructura;



public abstract class Cromosoma  implements Comparable<Cromosoma> {
	
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
	
	public Cromosoma() {
		inicializaCromosoma();
	}
	
	public Cromosoma(Cromosoma c)
	{	
		setGenes(c.getGenes());
		this.fenotipos = c.fenotipos.clone();
		this.fitness = c.fitness;
		this.puntuacion = c.puntuacion;
		this.punt_acum = c.punt_acum;
		this.longitud = c.longitud;
	}
	
	
	abstract public Cromosoma clone();
	abstract protected void inicializaCromosoma();
	
	abstract public float[] fenotipos(); // Devuelve el fenotipo de cada gen
	abstract public float evalua();
	abstract public boolean compara_mejor_fitness(float f);
	abstract public boolean compara_peor_fitness(float f);
	

	@Override
	public abstract int compareTo(Cromosoma c);
	
	public boolean muta(float prob)
	{
		boolean haMutado = false;
		
		for(Gen g: genes) {
			if (g.muta(prob)) haMutado = true;
		}
		return haMutado;
	}
	
	public String genotipos() 
	{
		String genotipoCompleto = "";
		for (Gen g: genes)
			genotipoCompleto += g.genotipo(); 
	
		return genotipoCompleto;
	}
	
	
	public void actualiza_puntuacion(float fitness_total) { puntuacion = fitness / fitness_total; }
	
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


