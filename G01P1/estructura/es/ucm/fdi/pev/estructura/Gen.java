package es.ucm.fdi.pev.estructura;

//T es el tipo de alelo
// Si es Binario le damos un tipo Bool
// Si es de floats pues floats
public class Gen<T> 
{ 
    // La agrupaci�n de alelos
    T[] valores;
    
    //Conocer tama�o del gen
    public int size() {
    	return valores.length;
    }
    
    // constructor
    Gen() {  
      
    }   
    
 
} 