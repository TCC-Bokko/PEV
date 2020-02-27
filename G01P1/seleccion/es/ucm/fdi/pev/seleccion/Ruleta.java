package es.ucm.fdi.pev.seleccion;
import es.ucm.fdi.pev.estructura.Cromosoma;
import java.lang.Math;



public class Ruleta {
	
	public static int[] ruleta(Cromosoma[] poblacion) {
		int[] pob_idx = new int[poblacion.length];
		// Consideramos que la poblacion ya tiene evaluado su fitness
		// Esto sucedio en la funcion de evaluacion del main.
		
		
		// Tiradas
		for (int j = 0; j < poblacion.length; j++ ) {
			pob_idx[j] = seleccion(poblacion);
		}
				
		return pob_idx;		
	}
	
	
	private static int seleccion(Cromosoma[] poblacion) {
		double r = Math.random();
	
		int idx = 0;
		while(r > poblacion[idx].getPuntuacionAcum() && idx < poblacion.length) idx++;
			
			//System.out.println("Punt acum. :" + poblacion[i].getPuntuacionAcum());
		
		return idx;		
	}
}
