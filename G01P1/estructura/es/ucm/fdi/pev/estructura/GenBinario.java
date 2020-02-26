package es.ucm.fdi.pev.estructura;
import java.util.ArrayList;
import java.util.Random;


public class GenBinario implements Gen 
{

	protected ArrayList<Boolean> bits;
	protected Boolean[] bits;
	
	protected float minRange;
	protected float maxRange;
	protected int size;
	protected String tipo;
	
	
public GenBinario(int tam, float minR, float maxR) 
{
	bits = new ArrayList<Boolean>();
	minRange = minR;
	maxRange = maxR;
	size = tam;
	tipo = "binario";
}

	
	@Override
	public int size() 
	{
		return size;
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
	
	public void setBits(ArrayList<Boolean> b) { bits = b; }
	public ArrayList<Boolean> getBits(){ return bits; }


	@Override
	public void randomInit() 
	{
		Random r = new Random();
		
		for (int i = 0; i < size; i++)
		{
			boolean v = r.nextBoolean();
			bits.add(v);
		}		
	}


	
	
	@Override
	public Gen cruce(int i, Gen g) 
	{	
		// Realizamos este casting de forma segura porque sabemos que solo se cruzar�n genes del mismo tipo.
		GenBinario g2 = (GenBinario)g;
		
		ArrayList<Boolean> g_bits = g2.getBits();
		ArrayList<Boolean> aux = new ArrayList<Boolean>(bits);
			
			bits.set(i, g_bits.get(i));
			g_bits.set(i, aux.get(i));
				
		g2.setBits(g_bits);
		
		return g2;
	}
	
	/*
	@Override
	public Gen cruce(int corte, Gen g) {
		
		// Realizamos este casting de forma segura porque sabemos que solo se cruzar�n genes del mismo tipo.
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
	}*/

	@Override
	public void muta(float prob) 
	{	
		//ArrayList<Boolean> new_b = new ArrayList<Boolean>(bits);
		
		Random r = new Random();
		for (int i = 0; i < bits.size(); i++)
		{
			float rand = r.nextFloat();
			
			if(rand < prob)
				bits.set(i, !bits.get(i));
		}
		
		//bits = new ArrayList<Boolean>(new_b);
	}
}


