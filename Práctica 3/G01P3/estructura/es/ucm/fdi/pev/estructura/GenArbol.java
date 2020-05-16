package es.ucm.fdi.pev.estructura;


public class GenArbol implements Gen, Cloneable {
	// AND y OR 2 argumentos
	// NOT acepta un argumento
	
	private String dato; // operando u operador (AND OR NOT IF)
	private GenArbol padre;
	private GenArbol Hi;		//Hijo izquierdo
	private GenArbol Hc;		//Hijo central
	private GenArbol Hd;		//Hijo derecho
	private int num_nodos;	//Número de nodos (Tamaño del arbol)
	private int profundidad; //profundidad a la que se encuentra la raiz de este nodo.
	
	public GenArbol() {
		num_nodos = 0;
		profundidad = -1;
		padre = null;
		Hi = null;
		Hc = null;
		Hd = null;
	}
	
	public GenArbol(GenArbol a) {
		dato = a.dato;
		padre = a.padre;
		Hi = a.Hi;
		Hc = a.Hc;
		Hd = a.Hd;
		num_nodos = a.num_nodos;
		profundidad = a.profundidad;
	}
	
	//GETTERS
	public String getValor() {
		return dato;
	}
	
	public GenArbol getHi() {
		return Hi;
	}
	
	public GenArbol getHc() {
		return Hc;
	}
	
	public GenArbol getHd() {
		return Hd;
	}
	
	public int getNumNodos() {
		return num_nodos;
	}
	
	public int getProfundidad() {
		return profundidad;
	}
	public GenArbol getPadre() {
		return this.padre;
	}
	
	//SETTERS
	public void setPadre(GenArbol parent) {
		this.padre = parent;
	}
	public void setValor(String value) {
		this.dato = value;
	}
	public void setHI(GenArbol hi) {
		this.Hi = hi;
	}
	public void setHC(GenArbol hc) {
		this.Hc = hc;
	}
	public void setHD(GenArbol hd) {
		this.Hd = hd;
	}
	public void setNumNodos(int nd) {
		this.num_nodos = nd;
	}
	public void setProfundidad(int pf) {
		this.profundidad = pf;
	}

	@Override
	public Gen clone() {
		return new GenArbol(this);
	}

	@Override
	public int size() {
		return this.num_nodos;
	}

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

	@Override
	public String genotipo() {
		// TODO Auto-generated method stub
		return null;
	}
}
