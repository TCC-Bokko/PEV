package es.ucm.fdi.pev.seleccion;
import es.ucm.fdi.pev.estructura.Cromosoma;
import java.lang.Math;



public class Ruleta {
	
	public static int[] ruleta(Cromosoma[] poblacion) {
		int[] pob_idx = new int[poblacion.length];
		
		// 1) Seleccionamos aleatoriamente a los candidatos en funcion de su puntuacion
		for (int i = 0; i < poblacion.length; i++ ) {
			pob_idx[i] = seleccion(poblacion);
		}
				
		return pob_idx;		
	}
	
	
	private static int seleccion(Cromosoma[] poblacion) {
		double r = Math.random();
	
		int idx = 0;
		while(r > poblacion[idx].getPuntuacionAcum() && idx < poblacion.length)
			idx++;
			
		return idx;		
	}
}
