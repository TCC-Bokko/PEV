package es.ucm.fdi.pev;
import es.ucm.fdi.pev.ui.GUI;

// Practica 1 Programación Evolutiva
// Alumnos:
// 		Antonio Cardona Costa
// 		Raúl Fernandez Guardia

// REQUISITOS:
// 	- librería jmathplot.jar : Ponerla en una carpeta llamada 'lib' dentro del proyecto, actualizar el proyecto en eclipse (G01P1)

public class Main 
{
	//Punto de entrada
	public static void main(String[] args)
	{
		GUI gui = new GUI();
		gui.setSize(800, 600);
		gui.setVisible(true);	 
	}
}
