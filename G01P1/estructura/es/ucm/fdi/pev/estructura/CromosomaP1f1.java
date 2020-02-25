package es.ucm.fdi.pev.estructura;
import java.lang.Math;
import java.util.ArrayList;

import es.ucm.fdi.pev.Utils.Utils;
import es.ucm.fdi.pev.evaluacion.FuncionesEv;

public class CromosomaP1f1 extends Cromosoma {


	
	public CromosomaP1f1(ArrayList<GenBinario> g) {
		super();
		
		fenotipos = new float[g.size()];
	
		
		for(int i = 0; i < fenotipos.length; i++)
		{
			addGen(g.get(i));
			fenotipos[i] = fenotipoInd((GenBinario) g.get(i));
		}
	}
	
	

	public CromosomaP1f1(Cromosoma c) {
		super(c);
	}



	@Override
	public float[] fenotipos() {
		
		for(int i = 0; i < fenotipos.length; i++)
		{
			//addGen(g.get(i));
			fenotipos[i] = fenotipoInd((GenBinario) genes.get(i));
		}
		
		return fenotipos;
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
	public float evalua() 
	{	
		for(int i = 0; i < fenotipos.length; i++)
		{
			fenotipos[i] = fenotipoInd((GenBinario) genes.get(i));
		}
		
		float result = (float) FuncionesEv.funcion1(fenotipos[0], fenotipos[1]);
		
		fitness = result;
		
		return result;
	}



	@Override
	public boolean compara_mejor_fitness(Cromosoma c) {
		return fitness > c.getFitness();
	}
}
