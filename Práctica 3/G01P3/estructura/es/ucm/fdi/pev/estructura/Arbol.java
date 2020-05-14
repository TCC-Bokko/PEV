package es.ucm.fdi.pev.estructura;


public class Arbol {
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
}
