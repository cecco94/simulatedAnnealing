package utils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import progetto.Rettangolo;
import progetto.Soluzione;



public class GeneratoreIstanze {
	

	public static int numero_richieste_urgenti;
	
	//l'altezza dei rect è in kw, questi numeri sono limiti fisici che ho letto su internet
	public static double altezza_massima = Soluzione.massimaAltezzaConsentita/2;
	public static double altezza_minima = 2.3;
	
	
	//crea istanza casuale di un piano e la salva
	public static void generaIstanzaProblema(int macchine_tranquille, int macchine_urgenti) throws RectImpossibleException, JsonMappingException, JsonProcessingException {
       
		numero_richieste_urgenti = macchine_urgenti;
		int numero_macchine = macchine_tranquille + macchine_urgenti;
		
		ArrayList<Rettangolo> rect = new ArrayList<>(); 
        for(int i = 0; i < numero_macchine; i++) {
        	rect.add(generaRettangolo(i));     
        }        
        Collections.sort(rect);
          
        Soluzione istanza = new Soluzione(rect);
        
        String path = "data/istanze/";
        String filename = "1_problema_con_" + macchine_tranquille +"_macchine_tranquille_" + macchine_urgenti +"_macchine_urgenti.json";
        JSON.salvaIstanzaProblema(path, filename, istanza);
        
 	}
	
	//crea istanza casuale di un piano senza salvarla
	public static Soluzione generaIstanzaProblemaSenzaSalvare(int macchine_tranquille, int macchine_urgenti) throws RectImpossibleException {
	       
		numero_richieste_urgenti = macchine_urgenti;
		int numero_macchine = macchine_tranquille + macchine_urgenti;
		
		ArrayList<Rettangolo> rect = new ArrayList<>(); 
        for(int i = 0; i < numero_macchine; i++) {
        	rect.add(generaRettangolo(i));     
        }        
        Collections.sort(rect);
          
        Soluzione istanza = new Soluzione(rect);
        return istanza;
	}
	
	//crea istanza casuale di una macchina
	public static Rettangolo generaRettangolo(int id) throws RectImpossibleException {
		Random rand = new Random();	
		
		int fase = 1 + rand.nextInt(4);		//fase = 4 dignifica che la macchina usa tutte e tre le fasi
		
		double area = 30.0 + rand.nextDouble(250.0); //quanti kwh può accumulare in media un'auto

		int inizio_nottata = 0;	
		int fine_nottata = 480;
		
		//a caso crea richieste con tempi minori di tutta la notte, per simulare richieste più urgenti
		if(numero_richieste_urgenti > 0) {
			
			int base_massima = (int)(area/altezza_minima);	
			int base_minima = (int)(Math.ceil(area/altezza_massima));

			int base = rand.nextInt(base_minima, base_massima);			//prende come base una qualsiasi base fattibile
			
			int inizio = rand.nextInt(fine_nottata - base_massima);		//prende come punto di inizio un qualsiasi momento fattibile della nottata
			int fine =  inizio + base; 
			
			numero_richieste_urgenti--;
			return new Rettangolo(id, fase, inizio, fine, area, altezza_massima, altezza_minima);
		}
		
		//crea richieste con tempo a disposizione = tutta la notte
		return new Rettangolo(id, fase, inizio_nottata, fine_nottata, area, altezza_massima, altezza_minima);
	}
	
	
	public static void generaIstanzaSpecifica() throws RectImpossibleException, JsonMappingException, JsonProcessingException {
		int macchine_tranquille = 0;
		int macchine_urgenti = 0;
		ArrayList<Rettangolo> rect = new ArrayList<>();
		rect.add(new Rettangolo(1, 1, 0, 100, 300.0, 10, 3));
		rect.add(new Rettangolo(2, 4, 80, 180, 300.0, 10, 3));
//		rect.add(new Rettangolo(0, 0, 0, 0, 0, 0, 0));
//		rect.add(new Rettangolo(0, 0, 0, 0, 0, 0, 0));
//		rect.add(new Rettangolo(0, 0, 0, 0, 0, 0, 0));

		
		Soluzione istanzaSpecifica = new Soluzione(rect);
		String path = "data/istanze/";
        String filename = "1_problema_con_" + macchine_tranquille +"_macchine_tranquille_" + macchine_urgenti +"_macchine_urgenti.json";
        JSON.salvaIstanzaProblema(path, filename, istanzaSpecifica);
	}
}


////ObjectMapper objectMapper = new ObjectMapper();
//
//System.out.println(s);
//
//Soluzione sol = JSON.stringToObj(s, Soluzione.class);
//
////Soluzione sol = objectMapper.readValue(s, Soluzione.class);
//System.out.println(sol.toString());







