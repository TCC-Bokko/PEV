package es.ucm.fdi.pev.estructura;

import java.util.Random;

public class GenEntero implements Gen {
	// Vector de int
	protected int alelo;
	
	protected int minRange;
	protected int maxRange;
	protected int tam;
	protected String tipo;
	
	public GenEntero(int minInt, int maxInt) 
	{
		minRange = minInt;
		maxRange = maxInt;
		tipo = "Entero";
	}
	
	public GenEntero(int value) 
	{
		alelo = value;
		tipo = "Entero";
	}
	//Constructora pasandole un gen
	public GenEntero(GenEntero g)
	{
		alelo = g.getAlelo();
		minRange = g.minRange();
		maxRange = g.maxRange();
	}
	
	public GenEntero clone()
	{
		return new GenEntero(this);
	}
	
	@Override
	public int size() {
		return 1;
	}
	
	public int minRange() {
		return minRange;
	}
	
	public int maxRange() {
		return maxRange;
	}
	
	@Override
	public void randomInit() {
		alelo = (int) (minRange + Math.random() * (maxRange - minRange));
	}
	
	@Override
	public String getType() {
		return tipo;
	}
	
	@Override
	public String genotipo() {
		return String.valueOf(alelo);
	}
	
	public int getAlelo() { return alelo; }
	public void setAlelo(int a) { alelo = a; }
	
	
	@Override
	public void muta(float prob) {
		/*
		for (int i = 0; i < alelo.length; i++) {
			Random r = new Random();	
			float rand = r.nextFloat();
			// Como usamos este gen para caminos, al mutar lo que hará
			// sera intercambiar dos valores de la cadena
			// asi nos olvidamos de conflictos.
			if (rand < prob) {
				int posSwitch = r.nextInt(maxRange);
				int valuei = alelo[i];
				alelo[i] = alelo[posSwitch];
				alelo[posSwitch] = valuei;
			}
		}
		*/
	}

	@Override
	public Gen cruce(int corte, Gen g) {
		// TODO Auto-generated method stub
		return null;
	}	
}