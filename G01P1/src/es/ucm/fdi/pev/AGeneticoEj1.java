package es.ucm.fdi.pev;

import es.ucm.fdi.pev.estructura.*;
import es.ucm.fdi.pev.evaluacion.*;
import javafx.util.Pair;
import es.ucm.fdi.pev.Utils.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.*;

import org.math.plot.Plot2DPanel;


public class AGeneticoEj1 extends AGenetico {

	// Numero de genes que compone cada cromosoma, y su tamaño (indice: nº de gen, valor: tam del gen)
	protected ArrayList<Pair<Float, Float>> genes_len;
	

	public AGeneticoEj1(int tamPob, int maxGen) {
		super(tamPob, maxGen);
		//iniciaGrafica();
	}
	

	
	// Inicializa la division en genes de los cromosomas, asi como el rango de valores de cada uno
	protected void inicializaGenes() 
	{
		tolerancia = 0.001f;
		
		float Xmin, Xmax;
		ArrayList<Pair<Float, Float>> genes_l = new ArrayList<Pair<Float, Float>>();
		
		 Xmin = -3.0f; Xmax = 12.1f;
		genes_l.add(new Pair<Float, Float>(Xmin, Xmax));
		
		 Xmin = 4.1f; Xmax = 5.8f;
		genes_l.add(new Pair<Float, Float>(Xmin, Xmax));
		
		genes_len = genes_l;
	}


	@Override
	protected Cromosoma inicializaCromosoma() 
	{
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
		
		return new CromosomaP1f1(genes);
	}
	
	@Override
	protected Cromosoma sustituyeCromosoma(Cromosoma c) {
		return new CromosomaP1f1(c);
	}

}



