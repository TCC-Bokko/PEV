package es.ucm.fdi.pev.evaluacion;
import java.util.*;

public class FuncionesEv {
	// FUNCION 1
	public static double funcion1(float x1, float x2) {
		double pi = 3.14159265359f;
		double y = 0.0f;
		
		y = 21.5 + x1 * java.lang.Math.sin(4 * pi * x1) + x2 * java.lang.Math.sin(20 * pi * x2);
		return y;
	}
	
	// FUNCION 2: Holder Table
	// Existen 4 mínimos de esta función valorados en -19.2085
	// Los valores x,y deben pertenecer a [-10,10]
	public static double f2holderTable(float x, float y) {
		double r = 0.0d;
		double pi = 3.14159265359f;
		
		r = - java.lang.Math.abs(java.lang.Math.sin(x)*
				java.lang.Math.cos(y)*
				java.lang.Math.exp(
						java.lang.Math.abs(1-(
								java.lang.Math.sqrt(x*x + y*y)/pi)
								)
						)
				);
		return r;
		}
	
	// FUNCION 3: Schubert
	// Existen 18 mínimos de esta función valorados en -186.7309
	// Los valores x1 y x2 deben pertenecer a [-10,10]
	public static double f3schubert(float[] xi) {
		double r = 0.0d;
		
		//double sum2 = 0.0d;
		
		for(int i = 0; i < xi.length; i++)
		{
			// Summatory
			double sum = 0.0d;
			for (int s1 = 1; s1 < 6; s1++)
				sum += (s1*java.lang.Math.cos((s1+1)*(xi[i]+s1)));
			
		if(i == 0)
			r = sum;
		else
			r *= sum;
		}
		/*// Summatory 2
		for (int s2 = 1; s2 < 6; s2++) {
			sum1 += (s2*java.lang.Math.cos((s2+1)*(x2+s2)));
		}*/
		
	//	r = sum1 * sum2;
		
		return r;
	}
	
	// Funcion 4: Michalewicz
	// xs puede tener de 1 a 7 x
	public static double f4michalewicz(float[] xi) {
		double pi = 3.14159265359d;
		double r = 0.0d;
		int n = xi.length;
		
		double sum = 0.0d;
		for (int i = 1; i < n; i++) {
			sum += Math.sin(xi[i]) * Math.pow((Math.sin((i+1) * Math.pow(xi[i],2) / pi)), 20);
		}
		
		r = -sum;
		
		return r;
	}
}
	
