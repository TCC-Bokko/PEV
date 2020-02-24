package es.ucm.fdi.pev.evaluacion;
import java.lang.Math;

public class Funcion3 {
	// FUNCION 3: Schubert
	// Existen 18 mínimos de esta función valorados en -186.7309
	// Los valores x1 y x2 deben pertenecer a [-10,10]
	public double f(float x1, float x2) {
		double r = 0.0d;
		double sum1 = 0.0d;
		double sum2 = 0.0d;
		
		// Summatory 1
		for (int s1 = 1; s1 < 6; s1++) {
			sum1 += (s1*Math.cos((s1+1)*(x1+s1)));
		}
		
		// Summatory 2
		for (int s2 = 1; s2 < 6; s2++) {
			sum1 += (s2*Math.cos((s2+1)*(x2+s2)));
		}
		
		r = sum1 * sum2;
		
		return r;
	}
}
