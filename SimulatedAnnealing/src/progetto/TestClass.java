package progetto;
import java.awt.Dimension;

import javax.swing.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import utils.GeneratoreIstanze;
import utils.JSON;
import utils.PlanImpossibleException;
import utils.RequestImpossibleException;
import utils.visualization.PannelloAltezzaSoluzione;
import utils.visualization.PannelloSfasamentoSoluzione;



public class TestClass {

	public static int altezzaFinestra = 600;
	public static int larghezzaFinestra = 600;

	//li uso come fossero dei bottoni per attivare/disattivare le funzionalità
	public static boolean newProblem = false, saveSolution = false, istanzaSpecifica = false, 
							grafico = false, datasetCreation = false, preprocessing = true;
	
	//per decidere la configurazione del problema
	public static int macchine_tranquille = 5, macchine_urgenti = 4;
	
	//perchè possono esserci più versioni della stessa configurazione
	public static int istanza = 1;
	
	
	public static void main(String[] args) throws RequestImpossibleException, JsonMappingException, JsonProcessingException, PlanImpossibleException {		
		
		if(newProblem) {			
			GeneratoreIstanze.generaIstanzaProblema(macchine_tranquille, macchine_urgenti);  
			return;
		}	
		
		if(istanzaSpecifica) {
			Soluzione ist = GeneratoreIstanze.generaIstanzaSpecificaProblemaSenzaSalvare();  
			visualizzaDatiIniziali(ist, ist.costoSoluzione());
			
			Soluzione preprocessata = AlgoritmoSimulatedAnnealing.preProcessing2(ist);
			visualizzaDatiIntermedi(preprocessata, preprocessata.costoSoluzione());
			
			Soluzione sol = AlgoritmoSimulatedAnnealing.simulatedAnnealing(ist);
			visualizzaDatiFinali(sol, sol.costoSoluzione());
			
			return;
		}
		
		//se non sono state richieste funzionalità particolari, risolve semplicemente il problema
		//carica istanza del problema
		Soluzione soluzioneIniziale = JSON.caricaIstanzaProblema("data/istanze/" + istanza + "_problema_con_"+macchine_tranquille+
																 "_macchine_tranquille_"+macchine_urgenti+"_macchine_urgenti.json");
		double costoSoluzioneIniziale = soluzioneIniziale.costoSoluzione();
        visualizzaDatiIniziali(soluzioneIniziale, costoSoluzioneIniziale); 
        
        Soluzione migliore_soluzione;
        double costo_migliore_soluzione;
        
        //per fare dei test, la soluzione sembra migliorare se prima distribuisco i rect nel tempo
        if(preprocessing) { 
        	migliore_soluzione = AlgoritmoSimulatedAnnealing.preProcessing2(soluzioneIniziale.clone());
            costo_migliore_soluzione = migliore_soluzione.costoSoluzione();
            
            visualizzaDatiIntermedi(migliore_soluzione, costo_migliore_soluzione);
            
            migliore_soluzione = AlgoritmoSimulatedAnnealing.simulatedAnnealing(migliore_soluzione);
            costo_migliore_soluzione = migliore_soluzione.costoSoluzione();
        }
        
        //algoritmo normale
        else {
        	migliore_soluzione = AlgoritmoSimulatedAnnealing.simulatedAnnealing(soluzioneIniziale);
            costo_migliore_soluzione = migliore_soluzione.costoSoluzione();
        }
        
        visualizzaDatiFinali(migliore_soluzione, costo_migliore_soluzione);
        controllaSoluzione(migliore_soluzione);
        		
	}

	
	////////////////////////// METODI AUSILIARI ///////////////////////////
	private static void controllaSoluzione(Soluzione migliore_soluzione) throws PlanImpossibleException, JsonMappingException, JsonProcessingException {
	 if(migliore_soluzione.sfasamento() > Soluzione.massimoSfasamentoConsentito) {
        	throw new PlanImpossibleException("piano impossibile, troppo sbilanciamento di fase nel punto: " + migliore_soluzione.puntoMassimoSfasamento().toString());
        }
        
        if(migliore_soluzione.costoSoluzione() > Soluzione.massimaAltezzaConsentita) {
        	throw new PlanImpossibleException("piano impossibile, troppa richiesta di potenza nel punto: " + migliore_soluzione.puntoMaxAltezza().toString());
        }  
        
        if(saveSolution) {
//        	SoluzioneSemplificata sol = migliore_soluzione.creaSoluzioneDaMettereNelJson();
//        	JSON.salvaSoluzione("data/soluzioni/", "problema_con_" + macchine_tranquille +"_macchine_tranquille_" + macchine_urgenti + "_macchine_urgenti.json", sol);
        }
	}
	

	private static void visualizzaDatiIntermedi(Soluzione migliore_soluzione, double costo_migliore_soluzione) {
		visualizzaAltezzaSoluzione(migliore_soluzione, "ALTEZZE DOPO TRASLAZIONE");
        visualizzaSfasamentoSoluzione(migliore_soluzione, "SFASAMENTO DOPO TRASLAZIONE");
        migliore_soluzione.printSoluzione();
        System.out.println("intersezioni " + migliore_soluzione.contaIntersezioni());
        System.out.println("media restringimento basi " + migliore_soluzione.mediaInnalzamentoRettangoli());
        System.out.println("costo dopo traslaz " + costo_migliore_soluzione);
        System.out.println("altezza max dopo traslaz " + migliore_soluzione.altezzaMassima());
        System.out.println("sfasamento max dopo traslaz " + migliore_soluzione.sfasamento());
        System.out.println(" \n" + "/+++++++++++++++++++++++++++" + "\n");
	}
	
	
	private static void visualizzaDatiFinali(Soluzione migliore_soluzione, double costo_migliore_soluzione) {
		visualizzaAltezzaSoluzione(migliore_soluzione, "ALTEZZE FINALI");
        visualizzaSfasamentoSoluzione(migliore_soluzione, "SFASAMENTO FINALE");
        migliore_soluzione.printSoluzione();
        System.out.println("intersezioni " + migliore_soluzione.contaIntersezioni());
        System.out.println("media restringimento basi " + migliore_soluzione.mediaInnalzamentoRettangoli());
        System.out.println("costo finale " + costo_migliore_soluzione);
        System.out.println("altezza max finale " + migliore_soluzione.altezzaMassima());
        System.out.println("sfasamento max finale " + migliore_soluzione.sfasamento());
		
	}
	

	private static void visualizzaDatiIniziali(Soluzione soluzioneIniziale, double costoSoluzioneIniziale) {
		visualizzaAltezzaSoluzione(soluzioneIniziale, "ALTEZZE PRIMA");
        visualizzaSfasamentoSoluzione(soluzioneIniziale, "SFASAMENTO PRIMA");
        soluzioneIniziale.printSoluzione();
        System.out.println("intersezioni " + soluzioneIniziale.contaIntersezioni());
        System.out.println("media restringimento basi " + soluzioneIniziale.mediaInnalzamentoRettangoli());
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

//distribuire i rect nel tempo (prima dell'algoritmo viene fatto un preprocessing dove i rect vengono distribuiti in modo uniforme nel tempo)
//per fare ciò, prima si mettono i rect uno dopo l'altro (separati oppure no), poi si fa un mini annealing che cerca di separarli meglio
//il prof vuole un algo che crea tot soluzioni casuali e usa la migliore come punto di partenza	(tanto vale usare il SA per traslarli, no?)
//all'aumentare delle richieste urgenti, il preprocessing funziona sempre meno

//implementato un criterio secondario per la scelta dove si privilegiano le soluzioni con rect di base più larga/meno sovrapposti

//aggiustare i parametri di temperatura, raffreddamento, passo (vedi qaunte iterazioni fa senza cambiare)	(cambiata implementazione)
//diminuire il passo al diminuire della temperatura non pare cambiare di molto la situazione (anzi, peggiora la soluzione)
//potrei pensare di spostare maggiormente i rect che sono più vicini al punto critico

//usa excell per i grafici
//controllare cosa fa ogni tot (ogni volta che cambia, fatto) 




