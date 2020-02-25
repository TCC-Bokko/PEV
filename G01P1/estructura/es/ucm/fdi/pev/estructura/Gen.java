package es.ucm.fdi.pev.estructura;
import java.util.ArrayList;

// T es el tipo de alelo
// Si es Binario le damos un tipo Bool
// Si es de floats pues floats
public interface Gen
{ 
    // La agrupaci�n de alelos
    // ArrayList<T> valores;
    
    // constructor
    
    
    //Conocer tama�o del gen
    public int size();
    
    public void randomInit();
    
    public void muta(float prob);
    
    public Gen cruce(int corte, Gen g);
}