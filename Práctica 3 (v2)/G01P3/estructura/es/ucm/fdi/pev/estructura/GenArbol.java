package es.ucm.fdi.pev.estructura;

import java.util.ArrayList;

public class GenArbol implements Gen, Cloneable {

	
	private String valor;
	private ArrayList<GenArbol> hijos;
	
	private int numHijos;
	
	private int max_prof;
	private int profundidad;

	private boolean esHoja;
	private boolean esRaiz;

	public GenArbol(GenArbol g) 
	{
		this.valor = g.valor;
		this.hijos = (ArrayList<GenArbol>) g.hijos.clone();
		this.numHijos = g.numHijos;
		this.max_prof = g.max_prof;
		this.profundidad = g.profundidad;
		this.esHoja = g.esHoja;
		this.esRaiz = g.esRaiz;
	}
	
	public GenArbol(String v) 
	{
		valor = v;
	}
	
	public GenArbol(int p) 
	{
		hijos = new ArrayList<GenArbol>();
		max_prof = p;
	}


	public ArrayList<String> toArray()
	{
		ArrayList<String> array = new ArrayList<String>();
		toArrayAux(array, this);
		return array;
	}
	
	// Insertar un valor en el arbol (nodo simple)
	public GenArbol insert(String v, int index)
	{
		GenArbol a = new GenArbol(v);
		if(index == -1){
			hijos.add(a);
			numHijos = hijos.size();
		}
		else
			hijos.set(index, a);
		return a;
	}
	
	// Insertar un arbol en otro arbol.
	public void insert(GenArbol a, int index)
	{
	if(index == -1){
		hijos.add(a);
		numHijos = hijos.size();
	}
	
	else
		hijos.set(index, a);
	}
	
	public GenArbol at(int index)
	{
		return at(this, 0, index);
	}
	
	private GenArbol at(GenArbol a, int pos, int index)
	{
		GenArbol s = null;
		if(pos >= index) s = a;
		else if(a.getNumHijos() > 0)
		{
				for(int i = 0; i < a.getNumHijos(); i++)
					if(s == null) s = at(a.getHijos().get(i), pos+i+1, index);
		}
		return s;
	}
	
	
	public void set(GenArbol a, int index) 
	{
		set(this, 0, index, a);
	} 
		
	private void set(GenArbol a, int pos, int index, GenArbol n)
	{
		if(pos >= index) a = (GenArbol) n.clone();
		
		else if(a.getNumHijos() > 0)
		{
				for(int i = 0; i < a.getNumHijos(); i++)
					set(a.getHijos().get(i), pos+i+1, index, n);
		}
	}
	
	
	public int getNumHijos() { return numHijos; }

	public ArrayList<GenArbol> getHijos() { return hijos; }

	private void toArrayAux(ArrayList<String> array, GenArbol a)
	{
		array.add(a.valor);
		for(int i = 0; i < a.hijos.size(); i++)
		{
			toArrayAux(array, a.hijos.get(i));
		}
	}

	
	public int getMaxProfundidad() {
		return max_prof;
	}
	public void setProfundidad(int profundidad) {
		this.profundidad = profundidad;
	}
	
	
	public String getValor() {
		return valor;
	}
	public void setValor(String v) {
		valor = v;
	}
	
	
	public boolean esRaiz() {
		return esRaiz;
	}
	public void setRaiz() {
		esRaiz = true;
		esHoja = false;
	}
	
	public void getRaices(ArrayList<GenArbol> hijos, ArrayList<GenArbol> nodos) 
	{
		for(int i = 0; i < hijos.size(); i++)
		{
			if(hijos.get(i).esRaiz())
			{
				nodos.add((GenArbol) hijos.get(i));
				getRaices(hijos.get(i).hijos, nodos);
			}
		}
	}
	
	
	
	public boolean esHoja() {
		return esHoja;
	}
	public void setHoja() {
		esRaiz = false;
		esHoja = true;
	}
	
	public void getHojas(ArrayList<GenArbol> hijos, ArrayList<GenArbol> nodos) 
	{
		for(int i = 0; i < hijos.size(); i++)
		{
			if(hijos.get(i).esHoja())
				nodos.add((GenArbol) hijos.get(i));
			else
				getHojas(hijos.get(i).hijos, nodos);
		}
	}

	
	public void addHijo(GenArbol h) {
		hijos.add(h);
		numHijos++;
	}
	
	@Override
	public Gen clone() {
		return new GenArbol(this);
	}

	@Override
	public int size() {
		int s = 0;
		for(GenArbol h : hijos)
		{
			 s += h.size();
		}
		return s + 1;
	}

	@Override
	public String genotipo() {
		String f = "";
		for(GenArbol h : hijos)
		{
			 f += h.genotipo();
		}
		return valor + " " + f;	
	}
	
	
	
	
//---------------------- NO SON NECESARIOS ---------------------- //
	
	@Override
	public void randomInit() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean muta(float prob) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Gen cruce(int corte, Gen g) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
