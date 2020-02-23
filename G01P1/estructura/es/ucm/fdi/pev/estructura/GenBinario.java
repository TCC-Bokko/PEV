package es.ucm.fdi.pev.estructura;
import java.util.ArrayList;
import java.util.Random;


public class GenBinario implements Gen 
{

	Boolean[] valores;
	
	
public GenBinario(int tam) 
{
	valores = new Boolean[tam];
}

	
	@Override
	public int size() 
	{
		return valores.length;
	}


	@Override
	public void randomInit() 
	{
		Random r = new Random();
		
		for (int i = 0; i < size(); i++)
		{
			boolean v = r.nextBoolean();
			valores[i] = v;
		}		
	}
}
