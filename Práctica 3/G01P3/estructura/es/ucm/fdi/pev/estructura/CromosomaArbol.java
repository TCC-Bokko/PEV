package es.ucm.fdi.pev.estructura;


public abstract class CromosomaArbol extends Cromosoma {
	
	Arbol arbol;
	String inicializacion;
	int prof_min;
	int prof_max;
	
	//Ver donde poner esto para no estar generando 
	private String[] fulloperador = new String[] {"IF", "AND", "OR", "NOT"};
	private String[] operador = new String[] {"AND", "OR", "NOT"};
	
	public CromosomaArbol() {
		inicializacion = "Completa"; //Cambiar a lo que reciba por GUI
		prof_min = 1; //Cambiar a lo que reciba por GUI
		prof_max = 3; //Cambiar a lo que reciba por GUI
		inicializaCromosoma();
	}
	
	@Override
	protected void inicializaCromosoma() {
	
		switch (inicializacion) {
		case "Completa":
			creaArbolCompleto(arbol, prof_min-1, prof_max-1);
			break;
		case "Creciente":
			creaArbolCreciente(arbol, prof_min-1, prof_max-1);
			break;
		case "Ramped&Half":
			creaArbolRampedAndHalf(arbol, prof_min-1, prof_max-1);
			break;
		
		}
	}
	
	private void creaArbolCompleto(Arbol arbol, int prof_min, int prof_max) {
		//Si no es hoja
		if (prof_min > 0) {
			//Generar subardol de operador
			//Meter aqui un randomizador y ver si usamos IF o no.
			String operador = fulloperador[1];
			arbol.dato = operador;
		}
	}
	
	private void creaArbolCreciente(Arbol arbol, int prof_min, int prof_max) {
		
	}
	
	private void creaArbolRampedAndHalf(Arbol arbol, int prof_min, int prof_max) {
		
	}
}
