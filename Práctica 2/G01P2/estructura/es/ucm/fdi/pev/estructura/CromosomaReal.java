package es.ucm.fdi.pev.estructura;

import java.util.ArrayList;

import es.ucm.fdi.pev.Utils.Pair;
import es.ucm.fdi.pev.Utils.Utils;

public abstract class CromosomaReal extends Cromosoma {

	
	public CromosomaReal() {
		super();
	}
	
	
	public CromosomaReal(Cromosoma c) {
		super(c);
	}
	
	
	
	// --------------------- FUNCIONES --------------------- //
	
	
		abstract ArrayList<Pair<Float, Float>> inicializaGenes();
		
		
		@Override
		protected void inicializaCromosoma()
		{
			ArrayList<Pair<Float, Float>> genes_len = inicializaGenes();
			
			genes = new GenReal[genes_len.size()];
			fenotipos = new float[genes_len.size()];
			
			int i = 0;
			for(Pair<Float, Float> genRange : genes_len)
			{
				float min = genRange.getFirst();
				float max = genRange.getSecond();
				
				GenReal g = new GenReal(min, max);
				g.randomInit();
				
				genes[i] = g;
				fenotipos[i] = g.getAlelo();
				longitud += g.size();
				
				i++;
			}
		}
		
	
	
	
	@Override
	public float[] fenotipos() {
		for(int i = 0; i < fenotipos.length; i++)
		{
			fenotipos[i] = ((GenReal) genes[i]).getAlelo();
		}
		
		return fenotipos;
	}

	@Override
	public void setGenes(Gen[] g)
	{
		genes = new Gen[g.length];
		for(int i = 0; i < g.length; i++)
		{
			genes[i] = new GenReal((GenReal) g[i]);
		}
	}
}
