import java.util.Random;

public class GeneratoreRettangoliCasuali {

	//crea istanza casuale del problema, con rettangoli di base massima
	public static Rettangolo generaRettangolo(int id) throws RectImpossibleException {
		Random rand = new Random();	
		
		double altezza_massima = 7.2;
		double altezza_minima = 2.3;
		
		int inizio = rand.nextInt(480);	//8 ore della notte
		double area = 30.0 + rand.nextDouble(150.0); //quanti kwh può accumulare in media un'auto
		int base = (int)(area/altezza_minima);
		
		return new Rettangolo(id, inizio, inizio + base, area, altezza_massima, altezza_minima);

	}
	
}
