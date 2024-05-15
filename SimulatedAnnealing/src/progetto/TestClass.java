package progetto;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import utils.GeneratoreIstanze;
import utils.JSON;
import utils.RectImpossibleException;
import utils.visualization.PannelloAltezzaSoluzione;
import utils.visualization.PannelloSfasamentoSoluzione;

import java.io.File;
import java.io.IOException;



public class TestClass {

	public static int altezzaFinestra = 600;
	public static int larghezzaFinestra = 600;

	
	public static void main(String[] args) throws RectImpossibleException, JsonMappingException, JsonProcessingException {
			
        //GeneratoreIstanze.generaIstanzaProblema(6, 3);       
		
		//carica istanza del problema
		Soluzione soluzioneIniziale = JSON.caricaIstanzaProblema("data/problema_con_6_tranquille_3_macchine_urgenti.json");
		double costoSoluzioneIniziale = soluzioneIniziale.costoSoluzione();
		
        //mostra i dati della soluzione iniziale
        visualizzaDatiIniziali(soluzioneIniziale, costoSoluzioneIniziale);                           
        
        //long t1 = System.currentTimeMillis();
        //Soluzione migliore_soluzione = AlgoritmoSimulatedAnnealing.simulatedAnnealingTraslaz(soluzioneIniziale, costoSoluzioneIniziale); // (soluzioneIniziale.clone(), costoSoluzioneIniziale);
        //long t2 = System.currentTimeMillis() - t1;
        //System.out.println("tempo impiegato " + t2);
        //double costo_migliore_soluzione = migliore_soluzione.costoSoluzione();
            
        //visualizza dati soluzione migliore trovata
        //visualizzaDatiFinali(migliore_soluzione, costo_migliore_soluzione);
        
      
        //controlla se la soluzione è fattibile
//        if(migliore_soluzione.sfasamento() > Soluzione.massimoSfasamentoConsentito) {
//        	System.out.println("piano impossibile, troppo sbilanciamento di fase nel punto: " + migliore_soluzione.puntoMassimoSfasamento().toString());
//        }
//        
//        if(migliore_soluzione.costoSoluzione() > Soluzione.massimaAltezzaConsentita) {
//        	System.out.println("piano impossibile, troppa richiesta di potenza nel punto: " + migliore_soluzione.puntoCritico().toString());
//        }
        

        		
	}

	
	private static void visualizzaDatiFinali(Soluzione migliore_soluzione, double costo_migliore_soluzione) {
		visualizzaAltezzaSoluzione(migliore_soluzione, "ALTEZZE DOPO");
        visualizzaSfasamentoSoluzione(migliore_soluzione, "SFASAMENTO DOPO");
        migliore_soluzione.printSoluzione();
        System.out.println("costo finale " + costo_migliore_soluzione);
        System.out.println("altezza max finale " + migliore_soluzione.altezzaMassima());
        System.out.println("sfasamento max finale " + migliore_soluzione.sfasamento());
		
	}


	private static void visualizzaDatiIniziali(Soluzione soluzioneIniziale, double costoSoluzioneIniziale) {
		visualizzaAltezzaSoluzione(soluzioneIniziale, "ALTEZZE PRIMA");
        visualizzaSfasamentoSoluzione(soluzioneIniziale, "SFASAMENTO PRIMA");
        soluzioneIniziale.printSoluzione();
        System.out.println("costo iniziale " + costoSoluzioneIniziale);
        System.out.println("altezza max iniziale " + soluzioneIniziale.altezzaMassima());
        System.out.println("sfasamento max iniziale " + soluzioneIniziale.sfasamento());
        System.out.println(" \n" + "/////////////////////////" + "\n");
		
	}
	

	public static void visualizzaAltezzaSoluzione(Soluzione soluzione, String title) {
		JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PannelloAltezzaSoluzione solPan = new PannelloAltezzaSoluzione(soluzione);
        solPan.setPreferredSize(new Dimension(larghezzaFinestra, altezzaFinestra));
        frame.add(solPan);
        frame.pack();
        frame.setVisible(true);
	}
	
	
	public static void visualizzaSfasamentoSoluzione(Soluzione soluzione, String title) {
		JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PannelloSfasamentoSoluzione solPan = new PannelloSfasamentoSoluzione(soluzione);
        solPan.setPreferredSize(new Dimension(larghezzaFinestra, altezzaFinestra));
        frame.add(solPan);
        frame.pack();
        frame.setVisible(true);
	}
	
}