package es.ucm.fdi.pev.Utils;

public class Utils {
	
	// Calcula el log en base 'b' de 'x'
		public static int log(float x, float b)
		{
		    return (int) (Math.log(x) / Math.log(b));
		}
		
		
		// Calcula la longitud en bits necesaria dado el intervalo [Xmin, Xmax]
		public  static int calculaLongitud(float Xmin, float Xmax, float tol)
		{
			int len;
			
			float aux = 1 + ((Xmax - Xmin) / tol);
			
			
			System.out.println( log(aux, 2));
			return log(aux, 2);
			
		}
}
