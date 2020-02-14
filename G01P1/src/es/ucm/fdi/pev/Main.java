package es.ucm.fdi.pev;
import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.estructura.*;


public class Main 
{
	//Punto de entrada
	public static void main(String[] args)
	{
		// Por cada problema llamamos a un AGEN
		
		// Definimos el agen
		AGenetico agen = new AGenetico();
		agen.ejecuta();		
	}
}
