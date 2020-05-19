package es.ucm.fdi.pev.estructura;

import java.util.ArrayList;

import es.ucm.fdi.pev.Utils.Pair;

public abstract class CromosomaArbol extends Cromosoma {

	protected GenArbol arbol;
	
	public CromosomaArbol(Cromosoma c) {
		super(c);
	}
	
	public CromosomaArbol() {
		super();
	}
	
	
	public GenArbol getArbol() {
		return arbol;
	}
	
	
	// NO SON NECESARIOS
	@Override
	protected void inicializaCromosoma() {
	}
	
	@Override
	public float[] fenotipos() {
		return null;
	}

	@Override
	public void setGenes(Gen[] g) {
	}
}

