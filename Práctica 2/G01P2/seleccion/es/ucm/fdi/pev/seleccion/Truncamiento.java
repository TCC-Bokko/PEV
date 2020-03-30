package es.ucm.fdi.pev.seleccion;


import java.util.TreeMap;
import es.ucm.fdi.pev.estructura.Cromosoma;

public class Truncamiento {
	public static int[] truncamiento(Cromosoma[] poblacion) {
		// Poblacion seleccionada
		int[] seleccion = new int[poblacion.length];
		float indiceTruncamiento = 0.5f;
		int numSeleccionados = (int)(poblacion.length * indiceTruncamiento);
		
		// ORDENAR POR FITNESS
		// Obtenemos parejas de fitness e indice en población 
		TreeMap<Float, Integer> OrderMap = new TreeMap<Float, Integer>();
		for (int i = 0; i < poblacion.length; i++) {
			float fitness = poblacion[i].getFitness();
			int index = i;
			OrderMap.put(fitness, index);
		}

		int[] seleccionados = new int[numSeleccionados];
		int sel_idx = 0;
		while (sel_idx != numSeleccionados-1) {
			int actualOrder = OrderMap.lastEntry().getValue();
			seleccionados[sel_idx] = actualOrder;
			sel_idx++;
			OrderMap.remove(OrderMap.lastEntry().getKey());
		}
		
		//Una vez tenemos la lista de seleccionados rellenamos selección
		sel_idx = 0;
		for (int j = 0; j < poblacion.length; j++) {
			seleccion[j] = seleccionados[sel_idx];
			sel_idx++;
			if (sel_idx == numSeleccionados) sel_idx = 0;
		}
		 
		
		// Devolvemos la población seleccionada
		return seleccion;
	}
	
}
