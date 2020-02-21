package es.ucm.fdi.pev.evaluacion;
import java.lang.Math;


public class Funcion1{
	public double f(float x1, float x2) {
		double pi = 3.14159265359f;
		double y = 0.0f;
		y = 25 + x1 * Math.sin(4 * pi * x1) + x2 * Math.sin(20 * pi * x2);
		return y;	
	}
}