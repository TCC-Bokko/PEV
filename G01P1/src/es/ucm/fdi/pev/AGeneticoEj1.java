package es.ucm.fdi.pev;

import es.ucm.fdi.pev.estructura.*;
import es.ucm.fdi.pev.evaluacion.*;
import javafx.util.Pair;
import es.ucm.fdi.pev.Utils.*;
import java.util.ArrayList;


public class AGeneticoEj1 extends AGenetico {

	// Numero de genes que compone cada cromosoma, y su tamaño (indice: nº de gen, valor: tam del gen)
	protected ArrayList<Pair<Float, Float>> genes_len; 

	public AGeneticoEj1(int tamPob, int maxGen) {
		super(tamPob, maxGen);
		
	}

	/*
	@Override
	protected void evaluaCromosoma(Cromosoma c) 
	{
		float[] fenotipos = c.fenotipos();		
		float result = (float) FuncionesEv.funcion1(fenotipos[0], fenotipos[1]);
		c.setRelFit(result);
		
		System.out.println(result);
	}*/
	
	@Override
	protected void inicializaPoblacion() 
	{
		tolerancia = 0.001f;
		inicializaGenes();
		
		poblacion = new CromosomaReal[tamPoblacion];
		
		for(int i = 0; i < tamPoblacion; i++)
		{
			poblacion[i] = inicializaCromosoma();
		}	
	}

	
	// Inicializa la division en genes de los cromosomas, asi como el rango de valores de cada uno
	protected void inicializaGenes() 
	{
		float Xmin, Xmax;
		ArrayList<Pair<Float, Float>> genes_l = new ArrayList<Pair<Float, Float>>();
		
		 Xmin = -3f; Xmax = 12.1f;
		genes_l.add(new Pair<Float, Float>(Xmin, Xmax));
		
		 Xmin = 4.1f; Xmax = 5.8f;
		genes_l.add(new Pair<Float, Float>(Xmin, Xmax));
		
		genes_len = genes_l;
	}


	@Override
	protected Cromosoma inicializaCromosoma() 
	{
		ArrayList<GenBinario> genes = new ArrayList<GenBinario>();
		
		for(Pair<Float, Float> genRange : genes_len)
		{
			float min = genRange.getKey();
			float max = genRange.getValue();
			
			int len = Utils.longitud_bits(min, max, tolerancia);
			
			GenBinario g = new GenBinario(len, min, max);
			g.randomInit();
			genes.add(g);
		}
		
		return new CromosomaReal(genes);
	}
}
