package utils;

import java.util.Random;

import progetto.Rettangolo;



public class GeneratoreRettangoliCasuali {

	//crea istanza casuale del problema, con rettangoli di base massima
	public static Rettangolo generaRettangolo(int id) throws RectImpossibleException {
		Random rand = new Random();	
		
		//l'altezza dei rect è in kw, questi numeri sono limiti fisici che ho letto su internet
		double altezza_massima = 7.2;
		double altezza_minima = 2.3;
		
		int fase = 1 + rand.nextInt(2);
		
		double area = 30.0 + rand.nextDouble(150.0); //quanti kwh può accumulare in media un'auto
		int base = (int)(area/altezza_minima);
		int inizio = rand.nextInt(480 - base);	//8 ore della notte
		
		return new Rettangolo(id, fase, inizio, inizio + base, area, altezza_massima, altezza_minima);

	}
	
}
