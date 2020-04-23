package es.ucm.fdi.pev.evaluacion;
import java.lang.Math;

// FUNCION 2: Holder Table
// Existen 4 mínimos de esta función valorados en -19.2085
// Los valores x,y deben pertenecer a [-10,10]
public class Funcion2 {
	public double f(float x, float y) {
		double r = 0.0d;
		double pi = 3.14159265359f;
		
		r = - Math.abs(Math.sin(x)*
				Math.cos(y)*
				Math.exp(
						Math.abs(1-(
								Math.sqrt(x*x + y*y)/pi)
								)
						)
				);
		return r;
	}
}