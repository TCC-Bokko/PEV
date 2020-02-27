package es.ucm.fdi.pev.estructura;
import java.lang.Math;
import java.util.ArrayList;

import es.ucm.fdi.pev.Utils.Utils;
import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public abstract class CromosomaBinario extends Cromosoma {

	public CromosomaBinario(GenBinario[] g) {
		super();
		
		genes = new Gen[g.length];
		fenotipos = new float[g.length];
		
		for(int i = 0; i < genes.length; i++)
		{
			genes[i] = g[i]; 
			fenotipos[i] = fenotipoInd((GenBinario) g[i]);
			longitud += g[i].size(); 
		}
	}
	
	
	public CromosomaBinario(Cromosoma c) {
		super(c);
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
	
	@Override
	public String genotipos() 
	{
		String genotipoCompleto = "";
		for (Gen g: genes)
			genotipoCompleto += g.genotipo(); 
	
		return genotipoCompleto;
	}
	
	// Calcula un fenotipo individual
	protected float fenotipoInd(GenBinario g)
	{	
		float gTam = g.size();
		float gMin = g.minRange();
		float gMax = g.maxRange();
		
		float dec = Utils.bin2dec(g.getBits());
		float aux = (float) ((gMax - gMin) / (Math.pow(2, gTam) - 1));
				
		return gMin + (dec * aux);
	}
	
	@Override
	public void actualiza_puntuacion(float fitness_total) { puntuacion = fitness / fitness_total; }
		
	
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
