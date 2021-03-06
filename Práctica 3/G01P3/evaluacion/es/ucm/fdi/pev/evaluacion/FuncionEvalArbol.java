package es.ucm.fdi.pev.evaluacion;
import es.ucm.fdi.pev.estructura.GenArbol;

public class FuncionEvalArbol {
	static boolean debug = false;
	
	public static double funcionEvalArbol(GenArbol arbol, int numAs, String[] multiplexor) {
		//WIP - TO DO
		// FUNCION DE EVALUACION DEL ARBOL, VER CASOS CORRECTOS DONDE SALIDA CORRESPONDE A DATO APUNTADO.
		double aciertos = 0;

		//Integer[][] valoresMultiplexor;
		//System.out.printf("Primer valor del multiplexor: [%s]\n", multiplexor[0]);
		String[] valoresMultiplexor = multiplexor;
		
		// Pasar valores por el arbol de programación y
		// Comprobar cuantos datos apuntados aciertan con el output.
		for (int i = 0; i < valoresMultiplexor.length; i++) {
			if (debug) System.out.printf("\nValor multiplexor en posicion i = %d\n", i);
			if (ejecutaPrograma(arbol, valoresMultiplexor[i])) {
				aciertos++;
			}
		}
		
		return aciertos;
	}
	
	private static boolean ejecutaPrograma(GenArbol arbol, String valores) {
		// Ejecución
		boolean resultadoEjecucion = ejecutaArbol(arbol, valores);
		char resultado = boolAchar(resultadoEjecucion); 
		
		// Ver elemento apuntado. 
		if (valores.length() == 6) {
			// Vemos el valor apuntado
			String direccion = Character.toString(valores.charAt(0));
			direccion.concat(Character.toString(valores.charAt(1)));
			int posSalida = Integer.parseInt(direccion)+2;
			
			// Mostramos
			if (debug) System.out.println("A0  A1  D0  D1  D2  D3  OUTPUT");
			if (debug) System.out.printf(valores);
			if (debug) System.out.printf(Character.toString(resultado));
			if (debug) System.out.println("");
			
			//Comparamos con el resultado de la ejecución
			if (resultado == valores.charAt(posSalida)) return true;
			else return false;
			
		} else if (valores.length() == 11) {
			String direccion = Character.toString(valores.charAt(0));
			direccion.concat(Character.toString(valores.charAt(1)));
			direccion.concat(Character.toString(valores.charAt(2)));
			int posSalida = Integer.parseInt(direccion)+3;
			
			// Mostramos
			if (debug) System.out.println("A0  A1  A2  D0  D1  D2  D3  D4  D5  D6  D7  OUTPUT");
			if (debug) System.out.printf(valores);
			if (debug) System.out.printf(Character.toString(resultado));
			if (debug) System.out.println("");
			
			//Comparamos con el resultado de la ejecución
			if (resultado == valores.charAt(posSalida)) return true;
			else return false;
		} else {
			if (debug) System.out.println("[Evaluacion-Arbol.ejecutaPrograma] Error: longitud de string valores no es 6 o 11. Devolviendo fallo.");
			return false;
		}	
	}
	
	private static boolean ejecutaArbol(GenArbol arbol, String valores) {
		String nodo = arbol.getValor();
	
		// BLOQUE IF
		if (nodo == "IF") {
			boolean bIzq;
			boolean bCen;
			boolean bDer;
			
			// Obtener valores de hijos
			// Llamadas recursivas si son operador
			// H. IZQUIERDO
			
			bIzq = ejecutaArbol(arbol.getHi(), valores);
			bCen = ejecutaArbol(arbol.getHc(), valores);
			bDer = ejecutaArbol(arbol.getHd(), valores);
			
			
			// Devolviendo el resultado de la operación lógica
			if(bIzq) return bCen;
			else return bDer;
		
		// BLOQUE OR
		} else if (nodo == "OR") {
			boolean bIzq;
			boolean bDer;
			
			// Obtener valores de hijos	
			bIzq = ejecutaArbol(arbol.getHi(), valores);	
			bDer = ejecutaArbol(arbol.getHd(), valores);
			
			// Devolviendo el resultado de la operacion lógica
			return (bIzq || bDer);
		
		// BLOQUE AND
		} else if (nodo == "AND") {
			boolean bIzq;
			boolean bDer;
				
			bIzq = ejecutaArbol(arbol.getHi(), valores);	
			bDer = ejecutaArbol(arbol.getHd(), valores);
	
			
			//Devolver operacion AND
			return (bIzq && bDer);
			
		// BLOQUE NOT
		} else if (nodo == "NOT") {
			boolean bIzq;
				
			bIzq = ejecutaArbol(arbol.getHi(), valores);
			return !bIzq;
			
			
		} else {
			return operandoAboolean(nodo, valores);
		}
	}
	
	// Devuelve si el valor es true o falso desde los valores
	private static boolean operandoAboolean(String tipo, String valores) {
		char valor;
		
		switch (tipo) {
			case "A0":
				valor = valores.charAt(0);
				break;
			case "A1":
				valor = valores.charAt(1);
				break;
			case "A2":
				valor = valores.charAt(2);
				break;
			case "D0":
				if (valores.length() == 6) valor = valores.charAt(2);
				else valor = valores.charAt(3);
				break;
			case "D1":
				if (valores.length() == 6) valor = valores.charAt(3);
				else valor = valores.charAt(4);
				break;
			case "D2":
				if (valores.length() == 6) valor = valores.charAt(4);
				else valor = valores.charAt(5);
				break;
			case "D3":
				if (valores.length() == 6) valor = valores.charAt(5);
				else valor = valores.charAt(6);
				break;
			case "D4":
				valor = valores.charAt(7);
				break;
			case "D5":
				valor = valores.charAt(8);
				break;
			case "D6":
				valor = valores.charAt(9);
				break;
			case "D7":
				valor = valores.charAt(10);
				break;
			default:
				valor = 'N';
				break;
		}
		
		if (valor == '0') return false;
		else if (valor == '1') return true;
		else {
			System.out.println("[Evalua-Arbol.charAboolean] Error: caracter leido no es 0 o 1. Devolviendo false.");
			return false;
		}
	}
	
	private static char boolAchar(boolean b) {
		if (b) return '1';
		else return '0';
	}
	
	// Devuelve si el nodo corresponde a un operador o un operando
	private static boolean isOperador(String tipo) {
		if (tipo == "OR" || tipo == "AND" || tipo == "NOT" || tipo == "IF") {
			return true;
		} else {
			return false;
		}
		
	}	
}
