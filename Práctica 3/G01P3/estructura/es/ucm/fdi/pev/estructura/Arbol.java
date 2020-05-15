package es.ucm.fdi.pev.estructura;


public class Arbol implements Gen {
	// AND y OR 2 argumentos
	// NOT acepta un argumento
	
	private String dato; // operando u operador (AND OR NOT IF)
	private Arbol padre;
	private Arbol Hi;		//Hijo izquierdo
	private Arbol Hc;		//Hijo central
	private Arbol Hd;		//Hijo derecho
	private int num_nodos;	//Número de nodos (Tamaño del arbol)
	private int profundidad; //profundidad a la que se encuentra la raiz de este nodo.
	
	public Arbol() {
		num_nodos = 0;
		profundidad = -1;
		padre = null;
		Hi = null;
		Hc = null;
		Hd = null;
	}
	
	public Arbol(Arbol a) {
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
	
	public Arbol getHi() {
		return Hi;
	}
	
	public Arbol getHc() {
		return Hc;
	}
	
	public Arbol getHd() {
		return Hd;
	}
	
	public int getNumNodos() {
		return num_nodos;
	}
	
	public int getProfundidad() {
		return profundidad;
	}
	public Arbol getPadre() {
		return this.padre;
	}
	
	//SETTERS
	public void setPadre(Arbol parent) {
		this.padre = parent;
	}
	public void setValor(String value) {
		this.dato = value;
	}
	public void setHI(Arbol hi) {
		this.Hi = hi;
	}
	public void setHC(Arbol hc) {
		this.Hc = hc;
	}
	public void setHD(Arbol hd) {
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
		return new Arbol(this);
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
