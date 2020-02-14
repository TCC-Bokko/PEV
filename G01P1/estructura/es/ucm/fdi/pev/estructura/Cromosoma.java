package es.ucm.fdi.pev.estructura;

public abstract class Cromosoma {
	
	
	// Array de tipo T : genotipo
	float fenotipo;
	
	float fitness;
	
	float puntuacion;
	
	float punt_acum;
	
	int longitud;
	
	abstract public float fenotipo();
	abstract public float evalua();
}
