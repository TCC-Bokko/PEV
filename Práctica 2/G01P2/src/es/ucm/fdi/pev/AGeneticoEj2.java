package es.ucm.fdi.pev;

import java.util.ArrayList;

import es.ucm.fdi.pev.Utils.Utils;
import es.ucm.fdi.pev.estructura.Cromosoma;
import es.ucm.fdi.pev.estructura.CromosomaP1f1;
import es.ucm.fdi.pev.estructura.CromosomaP1f2;
import es.ucm.fdi.pev.estructura.GenBinario;
import javafx.util.Pair;

public class AGeneticoEj2 extends AGenetico {

	
	
	// Numero de genes que compone cada cromosoma, y su tamaño (indice: nº de gen, valor: tam del gen)
	protected ArrayList<Pair<Float, Float>> genes_len;
		
	public AGeneticoEj2() {
		
	}
		
	public AGeneticoEj2(int tamPob, int maxGen) {
		super(tamPob, maxGen);
		
	}


	@Override
	protected void inicializaGenes() {
		tolerancia = 0.001f;
		
		float Xmin, Xmax;
		ArrayList<Pair<Float, Float>> genes_l = new ArrayList<Pair<Float, Float>>();
		
		 Xmin = -10f; Xmax = 10f;
		genes_l.add(new Pair<Float, Float>(Xmin, Xmax));
		
		 Xmin = -10f; Xmax = 10f;
		genes_l.add(new Pair<Float, Float>(Xmin, Xmax));
		
		genes_len = genes_l;
	}

	@Override
	protected Cromosoma inicializaCromosoma() {
		
	GenBinario[] genes = new GenBinario[genes_len.size()];
		
		int i = 0;
		for(Pair<Float, Float> genRange : genes_len)
		{
			float min = genRange.getKey();
			float max = genRange.getValue();
			
			int len = Utils.longitud_bits(min, max, tolerancia);
			
			GenBinario g = new GenBinario(len, min, max);
			g.randomInit();
			genes[i] = g;
			
			i++;
		}
		
		return new CromosomaP1f2(genes);
	}

	@Override
	protected Cromosoma sustituyeCromosoma(Cromosoma c) {
		 return new CromosomaP1f2(c);
	}

}


