package es.ucm.fdi.pev;
import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.ui.GUI;
import es.ucm.fdi.pev.estructura.*;
import java.util.ArrayList;


public class Main 
{
	//Punto de entrada
	public static void main(String[] args)
	{
		// EJECUCION USANDO
		GUI gui = new GUI();
		gui.setSize(600, 600);
		gui.setVisible(true);	
		
		// EJECUCIÓN SIN USAR GUI
		//int tamPoblacion = 100;
		//int maxGeneraciones = 100;
		//AGeneticoEj1 ag = new AGeneticoEj1(tamPoblacion, maxGeneraciones);
		//ag.ejecuta();
		 
	}
}
