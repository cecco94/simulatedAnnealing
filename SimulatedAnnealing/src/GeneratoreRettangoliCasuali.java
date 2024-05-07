import java.util.Random;

public class GeneratoreRettangoliCasuali {

	public static Rettangolo generaRettangolo() throws RectImpossibleException {
		Random rand = new Random();	
		
		double altezza_massima = 7;
		double altezza_minima = 2.3;
		
		int inizio = rand.nextInt(480);	//8 ore della notte
		int base = 20 + rand.nextInt(480);	//la ricarica dura al massi mo 8 ore
		double area = 30 + rand.nextDouble(150); //quanti kwh può accumulare in media un'auto
		
		return new Rettangolo(inizio, inizio + base, area, altezza_massima, altezza_minima);

		
	}
}
