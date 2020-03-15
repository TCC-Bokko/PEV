package es.ucm.fdi.pev.seleccion;
import es.ucm.fdi.pev.estructura.Cromosoma;

//Muestreo Universal Estocastico
public class MUE {
	
	public static int[] mue(Cromosoma[] poblacion) {
		float corte = 1.0f/(poblacion.length+1);
		int[] seleccion = new int[poblacion.length];
		int i_pob = 0; //[0, poblacion.lenght-1]
	
		float iterator = corte;
		for (int i = 0; i < seleccion.length; i++) {
			//System.out.print("Indice: " + i_pob);
			while (!(poblacion[i_pob].getPuntuacionAcum() > iterator)) {
				//System.out.print("Punt acum indiv: ");
				//System.out.println(poblacion[i_pob].getPuntuacionAcum());
				i_pob++;				
			}
			seleccion[i] = i_pob;
			iterator += corte;
		}
		/*
		for (int j = 0; j < seleccion.length; j++) {
			System.out.print(" Selected: ");
			System.out.println(seleccion[j]);
		}
		*/
		
		
		return seleccion;
	}
	
}
