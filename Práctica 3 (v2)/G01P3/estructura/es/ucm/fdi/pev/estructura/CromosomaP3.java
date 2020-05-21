package es.ucm.fdi.pev.estructura;

import java.io.Console;
import java.util.ArrayList;
import java.util.Random;

import es.ucm.fdi.pev.Utils.Pair;

public class CromosomaP3 extends CromosomaArbol {
	
	
	public static enum multiplexor { ENTRADAS_6, ENTRADAS_11 };

	public static final String terminales6[] = { "A0", "A1", "D0", "D1", "D2", "D3" };
	public static final String terminales11[] = { "A0", "A1", "A2", "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7" };
	
	public static final String funciones[] = { "AND", "OR", "NOT", "IF" };
	public static final String funcionesAridad1[] = { "NOT" };
	public static final String funcionesAridad2[] = { "AND", "OR" };
	public static final String funcionesAridad3[] = { "IF" };
	
	public static ArrayList<String> permutaciones;
	public static int numEntradas;
	
	protected String fenotipo;
	protected double fitness_bruto;
	
	public CromosomaP3(CromosomaP3 c) {
		this.arbol = c.arbol;
		this.fitness_bruto = c.fitness_bruto;
		this.fenotipo = c.fenotipo;
		
		this.fitness = c.fitness;
		this.puntuacion = c.puntuacion;
		this.punt_acum = c.punt_acum;
		this.longitud = c.longitud;
	}
	
	
	public CromosomaP3(int profundidad, String tipoCreacion, boolean useIf) 
	{
		arbol = new GenArbol(profundidad);
		switch(tipoCreacion)
		{
		case "Completa":
			inicializacionCompleta(arbol, 0, 0, useIf);
			break;
		case "Creciente":
			inicializacionCreciente(arbol, 0, useIf);
			break;
		case "RampedANDHalf":
			int ini = new Random().nextInt(2);
			if(ini == 0) inicializacionCreciente(arbol, 0, useIf);
			else inicializacionCompleta(arbol, 0, 0, useIf);
			break;
		}
	}
	
//---------------------------------------------------------------------------------------------------//	
//------------------------------------------ PERMUTACIONES ------------------------------------------//
//---------------------------------------------------------------------------------------------------//	
	
	
	public CromosomaP3() {
	}


	public static ArrayList<String> creaPermutaciones(int tamEntrada) 
	{
		permutaciones =  new ArrayList<String>();
		
		int tamDir = (int) Math.pow(2, tamEntrada);
		int tamMultiplexor = tamEntrada + tamDir;
		
		for(int i = 0; i < Math.pow(2, tamMultiplexor); i++)
		{
			String p = Integer.toBinaryString(i);
			
			while(p.length() < tamMultiplexor)
				p = p.concat("0");
	
			p = generaOutput(p, tamEntrada);	
			permutaciones.add(p);
		}		
		numEntradas = tamEntrada;	
		
		return permutaciones;
	}
	
	protected static String generaOutput(String p, int tamEntrada) 
	{
		String entrada = "";
		
		// 1) Leemos a que valor corresponde la entrada "A"
		int i = 0;
		while(i < tamEntrada)
		{
			entrada += (p.charAt(i));
			i++;
		}
		
		// 2) Ponemos el valor del output correspondiente si coincide o no coincide con la salida
		int dir = Integer.parseInt(entrada, 2);
		
		if(p.charAt(tamEntrada + dir) == '0')
			return p.concat("0");
		else
			return p.concat("1");
	}
	
	
	public static ArrayList<String> getPermutaciones() {
		return permutaciones;
	}

	
	
//--------------------------------------------------------------------------------------------------------//	
//------------------------------------------ RESTO DE FUNCIONES ------------------------------------------//
//--------------------------------------------------------------------------------------------------------//	
	
	
	
	@Override
	public Cromosoma clone() {
		return new CromosomaP3(this);
	}
	
	
	protected int inicializacionCompleta(GenArbol a, int p, int nodos, boolean useIF) 
	{
		int n = nodos;
		int nHijos = 2;
		
		Random rnd = new Random();
		
		// Si no hemos llegado a la profundidad máxima, entonces seguimos añadiendo operadores
		if(p < a.getMaxProfundidad())
		{	
			int func = 0;
			
			// Si usamos el "IF", inicializamos obteniendo cualquiera de ellas. Si no, omitimos la func. de "IF".
			if(useIF)
				func = rnd.nextInt(funciones.length);
			else
				func = rnd.nextInt(funciones.length - 1);
			
			a.setValor(funciones[func]);
			a.setRaiz();
			
			if(a.getValor().equals("IF")) nHijos = 3;
			else if(a.getValor().equals("NOT")) nHijos = 1;
			
			for(int i = 0; i < nHijos; i++)
			{
				GenArbol h = new GenArbol(a.getMaxProfundidad());
				n++;
				n = inicializacionCompleta(h, p + 1, n, useIF);
				a.addHijo(h);
			}
		}
		
		// En caso de llegar a la profundidad máxima, añadimos los terminales
		else
		{
			int terminal;
			switch (numEntradas)
			{
			case 2: 
				terminal = rnd.nextInt(terminales6.length);	
				a.setValor(terminales6[terminal]);
				break;
				
			case 3:
				terminal = rnd.nextInt(terminales11.length);
				a.setValor(terminales11[terminal]);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + numEntradas);
			}
					
			a.setHoja();
		}
		
		a.setProfundidad(p);
		return n;
	}
	
	
	protected int inicializacionCreciente(GenArbol a, int p, boolean useIF) 
	{
		return p;
	}
	
	

	@Override
	public float evalua() 
	{	
		boolean output;
		fitness = 0;
		
		for(String p : permutaciones)
		{	
			if(p.charAt(p.length() - 1) == '0')
				output = false;
			else
				output = true;
			
		//	System.out.println("Genotipo: " + genotipo());
			
			if(evalua_rec(arbol, p) == output)
				fitness++;
		}
			
		return fitness;
	}
	
	
	protected boolean evalua_rec(GenArbol a, String p) 
	{
		String nodo = a.getValor();	
			
		if(a.esHoja())
		{
			if (devuelve_terminal(nodo, p) == '0')
				return false;
			else 
				return true;		
		}
		else
		{
			ArrayList<GenArbol> hijos = a.getHijos();
			// BLOQUE IF
			if (nodo == "IF")
			{		
				boolean bIzq = evalua_rec(hijos.get(0), p);
				boolean bCen = evalua_rec(hijos.get(1), p);
				boolean bDer = evalua_rec(hijos.get(2), p);	
				
				if(bIzq) return bCen;
				else return bDer;
			
			// BLOQUE OR
			}
			else if (nodo == "OR") 
			{	
				// Obtener valores de hijos	
				boolean bIzq = evalua_rec(hijos.get(0), p);
				boolean bDer = evalua_rec(hijos.get(1), p);
				
				// Devolviendo el resultado de la operacion lógica
				return (bIzq || bDer);
			
			// BLOQUE AND
			} 
			else if (nodo == "AND") 
			{		
				boolean bIzq = evalua_rec(hijos.get(0), p);
				boolean bDer = evalua_rec(hijos.get(1), p);
				
				//Devolver operacion AND
				return (bIzq && bDer);
				
			// BLOQUE NOT
			}
			else if (nodo == "NOT") 
			{		
				boolean bIzq = evalua_rec(hijos.get(0), p);
				
				return !bIzq;
				
			} 
		}
		
		// No debe llegar a este punto
		return false;
	}
	
	
	protected char devuelve_terminal(String v, String p) {
		boolean value;
		
		switch (numEntradas) 
		{
		case 2: 
			for(int i = 0; i < terminales6.length ;i++)
				if(v == terminales6[i])
					return p.charAt(i);
			break;
		case 3:
			
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + numEntradas);
		}
		return 0;
	}

	@Override
	public boolean compara_mejor_fitness(float f) {
		if (fitness > f) return true;
		else return false;
	}

	@Override
	public boolean compara_peor_fitness(float f) {
		if (fitness < f) return true;
		else return false;
	}

	@Override
	public int compareTo(Cromosoma c) {
		if (fitness > c.fitness) return 0;
		else return 1;
	}
	
	public String genotipo() {
		return arbol.genotipo();
	}


	public int insertTerminal(ArrayList<GenArbol> list_hijos, GenArbol terminal, int index, int pos)
	{
		int p = pos;
		for(int i = 0; i < list_hijos.size(); i++)
		{
			if(list_hijos.get(i).esHoja() && (p == index))
			{
				//terminal.padre = list_hijos.get(i).padre;
				list_hijos.set(i, (GenArbol) terminal.clone());
				return p;
			}
			else if(list_hijos.get(i).esHoja() && (p != index))
				p++;
			else
				p = insertTerminal(list_hijos.get(i).getHijos(), terminal, index, p);
		}
		
		return p;
	}
	
	public int insertFuncion(ArrayList<GenArbol> list_hijos, GenArbol funcion, int index, int pos)
	{
		int p = pos;
		
		for(int i = 0; i < list_hijos.size(); i++)
		{
			if(list_hijos.get(i).esRaiz() && (p == index))
			{
				//terminal.padre = list_hijos.get(i).padre;
				list_hijos.set(i, (GenArbol) funcion.clone());
				return p;
			}
			else if(list_hijos.get(i).esRaiz() && (p != index))
			{
				p++;
				p = insertFuncion(list_hijos.get(i).getHijos(), funcion, index, p);
			}			
		}
		return p;
	}
}



