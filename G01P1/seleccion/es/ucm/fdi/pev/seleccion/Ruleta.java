package es.ucm.fdi.pev.seleccion;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaReal;
import java.lang.Math;



public class Ruleta {
	
	public static Cromosoma[] ruleta(Cromosoma[] poblacion) {
		Cromosoma[] pob_res = new Cromosoma[poblacion.length];
		// Consideramos que la poblacion ya tiene evaluado su fitness
		// Esto sucedio en la funcion de evaluacion del main.
		
		
		// Tiradas
		for (int j = 0; j < poblacion.length; j++ ) {
			pob_res[j] = seleccion(poblacion);
		}
				
		return pob_res;		
	}
	
	
	private static Cromosoma seleccion(Cromosoma[] poblacion) {
		double r = Math.random();
		
		for(int i = 0; i < poblacion.length; i++) {
			if (poblacion[i].getPuntuacionAcum() >= r) {
				
				Cromosoma c = new CromosomaReal(poblacion[i]);
				return c; //poblacion[i];
			}
		}
		
		return null;
	}
}
