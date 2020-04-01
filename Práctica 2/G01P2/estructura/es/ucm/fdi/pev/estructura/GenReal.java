package es.ucm.fdi.pev.estructura;

import java.util.Random;

public class GenReal implements Gen {
	
	
	protected float alelo;
	
	protected float minRange;
	protected float maxRange;

	
	public GenReal(float minR, float maxR) 
	{
		minRange = minR;
		maxRange = maxR;
	}


	public GenReal(GenReal g)
	{
		alelo = g.getAlelo();
		minRange = g.minRange();
		maxRange = g.maxRange();
	}
	
	public GenReal clone()
	{
		return new GenReal(this);
	}
	
	@Override
	public int size() {
	
		return 1;
	}
	
	public float minRange() {
		return minRange;
	}
	public float maxRange() {
		return maxRange;
	}
	
	
	public float getAlelo() { return alelo; }
	public void setAlelo(float a) { alelo = a; }

	@Override
	public void randomInit() {
		alelo = (float) (minRange + Math.random() * (maxRange - minRange));
	}

	@Override
	public boolean muta(float prob) {
		boolean haMutado = false;
		Random r = new Random();	
		float rand = r.nextFloat();
			
		if (rand < prob) {
			randomInit();
			haMutado = true;
		}
		return haMutado;
	}

	@Override
	public Gen cruce(int corte, Gen g) {
		
		float aux = alelo;
		
		alelo = ((GenReal) g).getAlelo();
		((GenReal) g).setAlelo(aux);
		
		return g;
	}

	@Override
	public String getType() {
		
		return null;
	}

	@Override
	public String genotipo() {
		return String.valueOf(alelo);
	}

}
