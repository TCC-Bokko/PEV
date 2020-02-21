package es.ucm.fdi.pev.seleccion;
import es.ucm.fdi.pev.estructura.Cromosoma;
import java.lang.Math;



public class Ruleta<T> {
	
	public Cromosoma<T>[] ruleta(Cromosoma<T>[] poblacion, float fitness_total) {
		Cromosoma<T>[] pob_res = new Cromosoma[poblacion.length];
		// Consideramos que la poblacion ya tiene evaluado su fitness
		// Esto sucedio en la funcion de evaluacion del main.
		
		//Calculo propor
		float aux = 0.0f;
		float[] prob_acum = new float[poblacion.length];
		
		for(int i = 0; i < poblacion.length; i++) {
			aux += poblacion[i].getRelFit();
			prob_acum[i] = aux; 
		}
		// Tiradas
		for (int j = 0; j < poblacion.length; j++ ) {
			pob_res[j] = seleccion(poblacion, prob_acum);
		}
				
		return pob_res;		
	}
	
	
	private Cromosoma<T> seleccion(Cromosoma<T>[] poblacion, float[] prob_acum) {
		double r_res = Math.random();
		
		for(int i = 0; i < prob_acum.length; i++) {
			if (prob_acum[i] >= r_res) {
				return poblacion[i];
			}
		}
		
		return null;
	}

}
