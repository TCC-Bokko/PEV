package es.ucm.fdi.pev.estructura;
import java.util.ArrayList;
import java.util.Random;


public class GenBinario implements Gen 
{

	protected Boolean[] bits;
	
	protected float minRange;
	protected float maxRange;
	
	
public GenBinario(int tam, float minR, float maxR) 
{
	bits = new Boolean[tam];
	minRange = minR;
	maxRange = maxR;
}

	
	@Override
	public int size() 
	{
		return bits.length;
	}
	
	public float minRange() {
		return minRange;
	}
	public float maxRange() {
		return maxRange;
	}
	
	
	
	public Boolean[] getBits()
	{
		return bits;
	}


	@Override
	public void randomInit() 
	{
		Random r = new Random();
		
		for (int i = 0; i < size(); i++)
		{
			boolean v = r.nextBoolean();
			bits[i] = v;
		}		
	}
}
