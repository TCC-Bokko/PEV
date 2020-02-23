package es.ucm.fdi.pev;
import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.estructura.*;
import java.util.ArrayList;


public class Main 
{
	//Punto de entrada
	public static void main(String[] args)
	{
		// Por cada problema llamamos a un AGEN
		
		// Definimos el agen
		//AGeneticoEj1<boolean> agen = new AGeneticoEj1<boolean>();
		//agen.ejecuta();
		
		int tamPoblacion = 6;
		int maxGeneraciones = 10;
		
		AGeneticoEj1 ag = new AGeneticoEj1(tamPoblacion, maxGeneraciones);
		ag.ejecuta();
		
		//ag.evaluacion();
		
		//Gen<Boolean> test = new Gen<Boolean>(20); 
	}
}
