package es.ucm.fdi.pev.estructura;

public class Arbol {
	// AND y OR 2 argumentos
	// NOT acepta un argumento
	
	public String dato; // operando u operador (AND OR NOT IF)
	Arbol Hi;		//Hijo izquierdo
	Arbol Hc;		//Hijo central
	Arbol Hd;		//Hijo derecho
	int num_nodos;	//Número de nodos
	int profundidad; //profundidad
	
	public Arbol(String tipo) {
	}

}
