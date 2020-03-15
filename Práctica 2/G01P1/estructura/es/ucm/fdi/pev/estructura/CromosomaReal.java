package es.ucm.fdi.pev.estructura;

public abstract class CromosomaReal extends Cromosoma {

	
	
	public CromosomaReal(GenReal[] g) {
		super();
		
		genes = new Gen[g.length];
		fenotipos = new float[g.length];
		
		for(int i = 0; i < genes.length; i++)
		{
			genes[i] = g[i]; 
			longitud += g[i].size(); 
		}
		
		fenotipos();
	}
	
	
	public CromosomaReal(Cromosoma c) {
		super(c);
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
