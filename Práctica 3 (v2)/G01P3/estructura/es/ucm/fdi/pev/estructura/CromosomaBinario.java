package es.ucm.fdi.pev.estructura;
import java.lang.Math;
import java.util.ArrayList;

import es.ucm.fdi.pev.Utils.Pair;
import es.ucm.fdi.pev.Utils.Utils;
import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public abstract class CromosomaBinario extends Cromosoma {

	
	protected float tolerancia = 0.01f;
		
	
	public CromosomaBinario(Cromosoma c) {
		super(c);
	}


	public CromosomaBinario() {
		super();
	}
	
	
	
	// --------------------- FUNCIONES --------------------- //
	
	
	abstract ArrayList<Pair<Float, Float>> inicializaGenes();
	
	
	@Override
	protected void inicializaCromosoma()
	{
		ArrayList<Pair<Float, Float>> genes_len = inicializaGenes();
		
		genes = new GenBinario[genes_len.size()];
		fenotipos = new float[genes_len.size()];
		
		int i = 0;
		for(Pair<Float, Float> genRange : genes_len)
		{
			float min = genRange.getFirst();
			float max = genRange.getSecond();
			int len = Utils.longitud_bits(min, max, tolerancia);
			
			GenBinario g = new GenBinario(len, min, max);
			g.randomInit();
			
			genes[i] = g;
			fenotipos[i] = fenotipoInd(g);
			longitud += g.size();
			
			i++;
		}
	}
	


	@Override
	public float[] fenotipos() {	
		for(int i = 0; i < fenotipos.length; i++)
		{
			//addGen(g.get(i));
			fenotipos[i] = fenotipoInd((GenBinario) genes[i]);
		}
		
		return fenotipos;
	}
	
	// Calcula un fenotipo individual
	protected float fenotipoInd(GenBinario g)
	{	
		float gTam = g.size();
		float gMin = g.minRange();
		float gMax = g.maxRange();
		
		float dec = Utils.bin2dec(g.getAlelos());
		float aux = (float) ((gMax - gMin) / (Math.pow(2, gTam) - 1));
				
		return gMin + (dec * aux);
	}
		
	
	@Override
	public void setGenes(Gen[] g)
	{
		genes = new Gen[g.length];
		for(int i = 0; i < g.length; i++)
		{
			genes[i] = new GenBinario((GenBinario) g[i]);
		}
	}
}
