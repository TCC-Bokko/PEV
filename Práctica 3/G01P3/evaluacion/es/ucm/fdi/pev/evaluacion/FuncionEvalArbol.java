package es.ucm.fdi.pev.evaluacion;
import es.ucm.fdi.pev.estructura.Arbol;

public class FuncionEvalArbol {
	public static double funcionEvalArbol(Arbol arbol, int numAs, String[] multiplexor) {
		//WIP - TO DO
		// FUNCION DE EVALUACION DEL ARBOL, VER CASOS CORRECTOS DONDE SALIDA CORRESPONDE A DATO APUNTADO.
		double aciertos = 0;
		int tam = 0; 
		//Integer[][] valoresMultiplexor;
		String[] valoresMultiplexor = multiplexor;
		
		// Pasar valores por el arbol de programación y
		// Comprobar cuantos datos apuntados aciertan con el output.
		for (int i = 0; i < valoresMultiplexor.length; i++) {
			if (ejecutaPrograma(arbol, valoresMultiplexor[i])) {
				aciertos++;
			}
		}
		
		return aciertos;
	}
	
	private static Boolean ejecutaPrograma(Arbol arbol, String valores) {
		char resultado = '0';
		
		// Ejecución
		
			// OJO: FALTA LA EJECUCIÓN AQUI.
		
		// Ver elemento apuntado. 
		if (valores.length() == 6) {
			// Vemos el valor apuntado
			String direccion = "";
			for (int i = 0; i < 2; i++) {
				direccion.concat(Character.toString(valores.charAt(i)));
			}
			int posSalida = Integer.parseInt(direccion)+2;
			
			//Comparamos con el resultado de la ejecución
			if (resultado == valores.charAt(posSalida)) return true;
			else return false;
			
		} else if (valores.length() == 11) {
			String direccion = "";
			for (int i = 0; i < 3; i++) {
				direccion.concat(Character.toString(valores.charAt(i)));
			}
			int posSalida = Integer.parseInt(direccion)+3;
			
			//Comparamos con el resultado de la ejecución
			if (resultado == valores.charAt(posSalida)) return true;
			else return false;
		} else {
			System.out.println("[Evaluacion-Arbol.ejecutaPrograma] Error: longitud de string valores no es 6 o 11. Devolviendo fallo.");
			return false;
		}
	
	}
	
}
