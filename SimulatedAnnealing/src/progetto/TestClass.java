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
import utils.PlanImpossibleException;
import utils.RectImpossibleException;
import utils.SoluzioneSemplificata;
import utils.visualization.PannelloAltezzaSoluzione;
import utils.visualization.PannelloSfasamentoSoluzione;

import java.io.File;
import java.io.IOException;



public class TestClass {

	public static int altezzaFinestra = 600;
	public static int larghezzaFinestra = 600;

	public static boolean newProblem = false, conTraslazione = false, saveSolution = false;
	
	public static int macchine_tranquille = 4, macchine_urgnti = 0;
	
	public static void main(String[] args) throws RectImpossibleException, JsonMappingException, JsonProcessingException, PlanImpossibleException {
		
		if(newProblem) {
			GeneratoreIstanze.generaIstanzaProblema(macchine_tranquille, macchine_urgnti);  
			return;
		}
		
		//carica istanza del problema
		Soluzione soluzioneIniziale = JSON.caricaIstanzaProblema("data/istanze/problema_con_4_macchine_tranquille_0_macchine_urgenti.json");
		double costoSoluzioneIniziale = soluzioneIniziale.costoSoluzione();
        visualizzaDatiIniziali(soluzioneIniziale, costoSoluzioneIniziale);                           
        
        Soluzione migliore_soluzione;
        double costo_migliore_soluzione;
        
        if(conTraslazione) {
        	migliore_soluzione = AlgoritmoSimulatedAnnealing.simulatedAnnealingTraslaz(soluzioneIniziale, costoSoluzioneIniziale);
            costo_migliore_soluzione = migliore_soluzione.costoSoluzione();
            
            visualizzaDatiIntermedi(migliore_soluzione, costo_migliore_soluzione);
            
            migliore_soluzione = AlgoritmoSimulatedAnnealing.simulatedAnnealing(migliore_soluzione, costo_migliore_soluzione);
            costo_migliore_soluzione = migliore_soluzione.costoSoluzione();
        }
        
        else {
        	migliore_soluzione = AlgoritmoSimulatedAnnealing.simulatedAnnealing(soluzioneIniziale, costoSoluzioneIniziale);
            costo_migliore_soluzione = migliore_soluzione.costoSoluzione();
        }
        
        visualizzaDatiFinali(migliore_soluzione, costo_migliore_soluzione);
        controllaSoluzione(migliore_soluzione);
        		
	}

	
	private static void controllaSoluzione(Soluzione migliore_soluzione) throws PlanImpossibleException, JsonMappingException, JsonProcessingException {
	 if(migliore_soluzione.sfasamento() > Soluzione.massimoSfasamentoConsentito) {
        	throw new PlanImpossibleException("piano impossibile, troppo sbilanciamento di fase nel punto: " + migliore_soluzione.puntoMassimoSfasamento().toString());
        }
        
        if(migliore_soluzione.costoSoluzione() > Soluzione.massimaAltezzaConsentita) {
        	throw new PlanImpossibleException("piano impossibile, troppa richiesta di potenza nel punto: " + migliore_soluzione.puntoCritico().toString());
        }  
        
        if(saveSolution) {
        	SoluzioneSemplificata sol = migliore_soluzione.creaSoluzioneDaMettereNelJson();
        	JSON.salvaSoluzione("data/soluzioni/", "problema_con_" + macchine_tranquille +"_macchine_tranquille_" + macchine_urgnti + "_macchine_urgenti.json", sol);
        }
	}


	private static void visualizzaDatiIntermedi(Soluzione migliore_soluzione, double costo_migliore_soluzione) {
		visualizzaAltezzaSoluzione(migliore_soluzione, "ALTEZZE DOPO TRASLAZIONE");
        visualizzaSfasamentoSoluzione(migliore_soluzione, "SFASAMENTO DOPO TRASLAZIONE");
        migliore_soluzione.printSoluzione();
        System.out.println("costo dopo traslaz " + costo_migliore_soluzione);
        System.out.println("altezza max dopo traslaz " + migliore_soluzione.altezzaMassima());
        System.out.println("sfasamento max dopo traslaz " + migliore_soluzione.sfasamento());
        System.out.println(" \n" + "/+++++++++++++++++++++++++++" + "\n");
	}
	
	
	private static void visualizzaDatiFinali(Soluzione migliore_soluzione, double costo_migliore_soluzione) {
		visualizzaAltezzaSoluzione(migliore_soluzione, "ALTEZZE FINALI");
        visualizzaSfasamentoSoluzione(migliore_soluzione, "SFASAMENTO FINALE");
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