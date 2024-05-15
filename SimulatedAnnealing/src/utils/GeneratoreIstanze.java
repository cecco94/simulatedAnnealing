package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import progetto.Rettangolo;
import progetto.Soluzione;



public class GeneratoreIstanze {
	
	public static int numero_macchine = 1;
	public static int probabilita_richieste_urgenti = 100;
	
	//crea istanza casuale di un piano
	public static Soluzione generaIstanzaProblema() throws RectImpossibleException {
        ArrayList<Rettangolo> rect = new ArrayList<>(); 
        for(int i = 0; i < numero_macchine; i++) {
        	rect.add(generaRettangolo(i));        	
        }        
        Collections.sort(rect);
        return new Soluzione(rect);
	}
	
	//crea istanza casuale di una macchina
	public static Rettangolo generaRettangolo(int id) throws RectImpossibleException {
		Random rand = new Random();	
		
		//l'altezza dei rect è in kw, questi numeri sono limiti fisici che ho letto su internet
		double altezza_massima = 7.2;
		double altezza_minima = 2.3;
		
		int fase = 1 + rand.nextInt(3);
		
		double area = 30.0 + rand.nextDouble(250.0); //quanti kwh può accumulare in media un'auto

		int inizio_nottata = 0;	
		int fine_nottata = 480;
		
		//a caso crea richieste con tempi minori di tutta la notte, per simulare richieste più urgenti
		if(rand.nextInt(100) < probabilita_richieste_urgenti) {
			
			int base_massima = (int)(area/altezza_minima);	
			int base_minima = (int)(Math.ceil(area/altezza_massima));

			int base = rand.nextInt(base_minima, base_massima);			//prende come base una qualsiasi base fattibile
			
			int inizio = rand.nextInt(fine_nottata - base_massima);		//prende come punto di inizio un qualsiasi momento fattibile della nottata
			int fine =  inizio + base; 
			
			return new Rettangolo(id, fase, inizio, fine, area, altezza_massima, altezza_minima);
		}
		
		//crea richieste con tempo a disposizione = tutta la notte
		return new Rettangolo(id, fase, inizio_nottata, fine_nottata, area, altezza_massima, altezza_minima);
	}
	
	
}










