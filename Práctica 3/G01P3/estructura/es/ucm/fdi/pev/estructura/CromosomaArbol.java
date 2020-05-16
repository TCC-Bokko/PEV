package es.ucm.fdi.pev.estructura;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.ucm.fdi.pev.evaluacion.FuncionEvalArbol;

public abstract class CromosomaArbol extends Cromosoma {
	
	public CromosomaArbol(Cromosoma c) {
		super(c);
	}
	
	public CromosomaArbol() {
		super();
	}
	
	@Override
	protected void inicializaCromosoma() {
		genes = new GenArbol[1];
		GenArbol raizArbol = new GenArbol();
		genes[0] = raizArbol;
	}
	
}
