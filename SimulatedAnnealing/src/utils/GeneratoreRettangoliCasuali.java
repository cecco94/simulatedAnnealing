package utils;

import java.util.Random;

import progetto.Rettangolo;



public class GeneratoreRettangoliCasuali {

	//crea istanza casuale del problema, con rettangoli di base massima
	public static Rettangolo generaRettangolo(int id, boolean traslaz) throws RectImpossibleException {
		Random rand = new Random();	
		
		//l'altezza dei rect è in kw, questi numeri sono limiti fisici che ho letto su internet
		double altezza_massima = 7.2;
		double altezza_minima = 2.3;
		
		int fase = 1 + rand.nextInt(3);
		
		double area = 30.0 + rand.nextDouble(250.0); //quanti kwh può accumulare in media un'auto
		
		if(traslaz) {
			int inizio = 0;	
			int fine = 480;
			
			//a caso crea richieste con tempi minori di tutta la notte, per simulare richieste più urgenti
			if(rand.nextInt(4) > 1) {
				int base = (int)(area/altezza_minima);		
				inizio = rand.nextInt(480 - base);
				return new Rettangolo(id, fase, inizio, inizio + base, area, altezza_massima, altezza_minima);
			}
			
			//crea richieste con tempo a disposizione = tutta la notte
			return new Rettangolo(id, fase, inizio, fine, area, altezza_massima, altezza_minima);
		}
		
		
		//vecchia versionee che crea istanze con tempi casuali
		int base = (int)(area/altezza_minima);		
		int inizio = rand.nextInt(480 - base);	
		return new Rettangolo(id, fase, inizio, inizio + base, area, altezza_massima, altezza_minima);
	}
	
}
