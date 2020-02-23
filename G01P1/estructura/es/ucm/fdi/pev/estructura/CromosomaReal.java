package es.ucm.fdi.pev.estructura;

public class CromosomaReal extends Cromosoma {

	
	float tolerancia;
	
	public CromosomaReal(float tol) {
		super();
		tolerancia = tol;
	}
	
	

	@Override
	public float[] fenotipo() {
		
		float[] fenotipos = new float[genes.size()];
		
		
		return fenotipos;
	}
	
	// Calcula un fenotipo individual
	protected float fenotipoInd()
	{
		return 0;
	}
	

	@Override
	public float evalua() {
		return 0;
	}

}
