package es.ucm.fdi.pev.evaluacion;
import java.lang.Math;

public class Funcion4 {
	// Funcion 4: Michalewicz
	// xs puede tener de 1 a 7 x
	public static double f(float[] xs) {
		double pi = 3.14159265359d;
		double r = 0.0d;
		int n = xs.length;
		
		double sum = 0.0d;
		for (int xi = 1; xi < n; xi++) {
			sum += Math.sin(xs[xi]) * Math.pow((Math.sin((xi+1) * Math.pow(xs[xi],2) / pi)), 20);
		}
		
		r = - sum;
		
		return r;
	}
}
