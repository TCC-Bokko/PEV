package es.ucm.fdi.pev.estructura;
import java.util.ArrayList;
import java.util.Random;


public class GenBinario implements Gen, Cloneable
{

	protected Boolean[] alelos;
	
	protected float minRange;
	protected float maxRange;
	protected int size;
	protected String tipo;
	
	
public GenBinario(int tam, float minR, float maxR) 
{
	alelos = new Boolean[tam];
	minRange = minR;
	maxRange = maxR;
	size = tam;
	tipo = "binario";
}


public GenBinario(GenBinario g)
{
	alelos = g.getAlelos().clone();
	minRange = g.minRange();
	maxRange = g.maxRange();
	size = g.size();
}

public GenBinario clone() {
	return new GenBinario(this);
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
	
	public void setAlelos(Boolean[] b) { alelos = b; }
	public Boolean[] getAlelos(){ return alelos; }


	@Override
	public void randomInit() 
	{
		Random r = new Random();
		
		for (int i = 0; i < size; i++)
			alelos[i] = r.nextBoolean();
	}
	
	
	@Override
	public Gen cruce(int i, Gen g) 
	{	
		Boolean[] g_bits = ((GenBinario) g).getAlelos();
		boolean aux = alelos[i];
		
		alelos[i] =  g_bits[i];
		g_bits[i] = aux;
		
		((GenBinario) g).setAlelos(g_bits.clone());
		
		return g;		
	}

	@Override
	public boolean muta(float prob) 
	{	
		//ArrayList<Boolean> new_b = new ArrayList<Boolean>();
		boolean haMutado = false;
		Random r = new Random();
		for (int i = 0; i < alelos.length; i++)
		{
			float rand = r.nextFloat();
			if(rand < prob) {
				alelos[i] = !alelos[i];
				haMutado = true;
			}
		}
		
		return haMutado;
		//bits = new_b;
	}

	@Override
	public String genotipo() 
	{
		String genotipo = "";
		
		for(Boolean b : alelos)
			genotipo +=  b ? 1 : 0;
		
		return genotipo;
	}
}


