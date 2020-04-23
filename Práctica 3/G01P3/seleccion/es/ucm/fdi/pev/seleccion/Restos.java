package es.ucm.fdi.pev.seleccion;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.pev.estructura.Cromosoma;

public class Restos {
	public static int[] restos(Cromosoma[] poblacion) {
		boolean debug = false;
		// Poblacion seleccionada
		int[] seleccion = new int[poblacion.length];
		int k = poblacion.length;
		float cut = 1 / k;
		List<Integer> seleccionados = new ArrayList<Integer>();
		
		for(int i = 0; i < poblacion.length; i++) {
			float punt_ind = poblacion[i].getPuntuacion();
			if(debug)System.out.printf("%d: %f", i, punt_ind);
			float PiK = punt_ind*k;
			if (PiK > cut) seleccionados.add(i);
		}
		
		//Una vez tenemos la lista de seleccionados rellenamos selección
		int sel_idx = 0;
		for (int j = 0; j < poblacion.length; j++) {
			seleccion[j] = seleccionados.get(sel_idx);
			sel_idx++;
			if (sel_idx == seleccionados.size()) sel_idx = 0;
		}

		// Devolvemos la población seleccionada
		return seleccion;	
	}
}
