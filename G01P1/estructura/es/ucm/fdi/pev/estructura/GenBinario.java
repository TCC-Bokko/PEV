package es.ucm.fdi.pev.estructura;
import java.util.ArrayList;
import java.util.Random;


public class GenBinario implements Gen 
{
	protected Boolean[] bits;
	
	protected float minRange;
	protected float maxRange;
	protected String tipo;
	
	
public GenBinario(int tam, float minR, float maxR) 
{
	bits = new Boolean[tam];
	minRange = minR;
	maxRange = maxR;
	tipo = "binario";
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
	
	@Override
	public String getType() {
		return tipo;
	}
	
	public void setBits(Boolean[] b) { bits = b; }
	public Boolean[] getBits(){ return bits; }


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


	@Override
	public Gen cruce(int corte, Gen g) {
		
		// Realizamos este casting de forma segura porque sabemos que solo se cruzarán genes del mismo tipo.
		GenBinario g2 = (GenBinario)g;
		
		Boolean[] g_bits = g2.getBits();
		Boolean[] aux = bits.clone();
			
		for (int i = 0; i < corte; i++)
		{	
			bits[i] = g_bits[i];
			g_bits[i] = aux[i];
		}
				
		g2.setBits(g_bits);
			
		return g2;
	}

	@Override
	public void muta(float prob) 
	{
		Random r = new Random();
		for (boolean b : bits)
		{
			float rand = r.nextFloat();
			
			if(rand < prob)
			{
				if(b == true)
					b = false;
				else
					b = true;
			}
		}	
	}
}


