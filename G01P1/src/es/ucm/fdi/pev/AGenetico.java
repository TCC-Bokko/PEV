package es.ucm.fdi.pev;

import es.ucm.fdi.pev.evaluacion.*;
import es.ucm.fdi.pev.seleccion.*;
import es.ucm.fdi.pev.estructura.*;

public class AGenetico 
{

	Cromosoma[] poblacion;
	int tamPoblacion;
	
	Cromosoma elMejor;
	int mejor_idx;
	
	int maxGeneraciones;
	int generacionActual;
	
	float prob_cruce;
	float prob_mutacion;
	
	float toleracia;
	
	// ---------------- FUNCIONES ---------------- //
	
	
	public AGenetico( ) 
	{
		// Recibimos
		// Tamaño de poblacion y genes en cada individuo
		// Probabilidades 
		// Funcion de evaluacion
	}
	
	
	public void ejecuta()
	{
		// Bucle del algoritmo
	}
	
	
	
	// Funciones del blucle: 
	
	private void inicializaPoblacion()
	{
		
	}
	
	private void evaluacion() 
	{
		
	}
	
	private void seleccion()
	{
	
	}
	
	private void reproduccion() 
	{
		
	}
	
	private void mutacion()
	{
		
	}
	
	
	private boolean terminado() 
	{
		return generacionActual >= maxGeneraciones;
	}
}