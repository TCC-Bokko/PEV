package es.ucm.fdi.pev.evaluacion;
import java.lang.Math;


public class Funcion1{
	public double f(float x, float y) {
		double pi = 3.14159265359f;
		double f = 0.0f;
		f = 25 + x * Math.sin(4 * pi * x) + y * Math.sin(20 * pi * y);
		return f;	
	}
}