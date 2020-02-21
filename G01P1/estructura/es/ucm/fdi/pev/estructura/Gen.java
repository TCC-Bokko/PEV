package es.ucm.fdi.pev.estructura;

//T es el tipo de alelo
// Si es Binario le damos un tipo Bool
// Si es de floats pues floats
public class Gen<T> 
{ 
    // La agrupación de alelos
    T[] valores;
    
    //Conocer tamaño del gen
    public int size() {
    	return valores.length;
    }
    
    // constructor
    Gen() {  
      
    }   
    
 
} 