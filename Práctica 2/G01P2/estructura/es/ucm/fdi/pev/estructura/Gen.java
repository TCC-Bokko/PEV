package es.ucm.fdi.pev.estructura;

// T es el tipo de alelo
// Si es Binario le damos un tipo Bool
// Si es de floats pues floats
public interface Gen
{ 
    // La agrupaci�n de alelos
    // ArrayList<T> valores;
    
    // constructor
	public Gen clone();
    
    //Conocer tama�o del gen
    public int size();
    
    public void randomInit();
    
    public boolean muta(float prob);
    
    public Gen cruce(int corte, Gen g);
    
    //Devuelve si es binario, real o entero
    public String getType();
    
    public String genotipo();
}

