package progetto;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;

import utils.GeneratoreRettangoliCasuali;
import utils.RectImpossibleException;
import utils.visualization.PannelloAltezzaSoluzione;
import utils.visualization.PannelloSfasamentoSoluzione;

public class AlgoritmoSimulatedAnnealing {

	public static int altezzaFinestra = 600;
	public static int larghezzaFinestra = 600;
	public static boolean traslazione = true;

	
	public static void main(String[] args) throws RectImpossibleException {
				
		
		//crea istanza casuale del problema, con rettangoli di base massima
        ArrayList<Rettangolo> rect = new ArrayList<>();         
        for(int i = 0; i < 10; i++) {
        	rect.add(GeneratoreRettangoliCasuali.generaRettangolo(i, traslazione));        	
        }
        Collections.sort(rect);
        
        Soluzione soluzioneIniziale = null;
        
        if(traslazione) {
            //il primo problema è sistemare i rect di base massima lungo la linea del tempo in modo che siano ben ditribuiti
            Soluzione soluzioneInizialePrimaDellaTraslazione = new Soluzione(rect); 
            soluzioneInizialePrimaDellaTraslazione.printSoluzione();
            System.out.println("costo finale " + soluzioneInizialePrimaDellaTraslazione.costoSoluzione());
            System.out.println("altezza max finale " + soluzioneInizialePrimaDellaTraslazione.altezzaMassima());
            System.out.println("sfasamento max finale " + soluzioneInizialePrimaDellaTraslazione.sfasamento());
            System.out.println("*****************************");
            visualizzaAltezzaSoluzione(soluzioneInizialePrimaDellaTraslazione, "prima della traslazione");
            visualizzaSfasamentoSoluzione(soluzioneInizialePrimaDellaTraslazione, "prima della traslazione");
            soluzioneIniziale = SimulatedAnnealingTraslazione.simulatedAnnealingTraslaz(soluzioneInizialePrimaDellaTraslazione);
            //una volta fatto ciò, si risolve il problema di trovare le dimensioni dei rettangoli migliori
        }

        else {
        	soluzioneIniziale = new Soluzione(rect);
        }
                
        double costoSoluzioneIniziale = soluzioneIniziale.costoSoluzione();   

        //mostra i dati della soluzione iniziale
        visualizzaAltezzaSoluzione(soluzioneIniziale, "ALTEZZE PRIMA");
        visualizzaSfasamentoSoluzione(soluzioneIniziale, "SFASAMENTO PRIMA");
        soluzioneIniziale.printSoluzione();
        System.out.println("costo iniziale " + costoSoluzioneIniziale);
        System.out.println("altezza max iniziale " + soluzioneIniziale.altezzaMassima());
        System.out.println("sfasamento max iniziale " + soluzioneIniziale.sfasamento());
        System.out.println(" \n" + "/////////////////////////" + "\n");
                       
        //in principio si salva come soluzione migliore quella inizale
        Soluzione miglioreSoluzioneGlobale = soluzioneIniziale.clone();
        double costoMiglioreSoluzioneGlobale = costoSoluzioneIniziale;
                   
        //svolge l'algoritmo n volte e prende la soluzione migliore
        for(int epoca = 0; epoca < 5; epoca++) {
            
            Soluzione soluzioneCorrenteNelLoop = simulatedAnnealing(soluzioneIniziale.clone(), costoSoluzioneIniziale);
            double costoSoluzioneCorrenteNelLoop = soluzioneCorrenteNelLoop.costoSoluzione();
            
            if(costoSoluzioneCorrenteNelLoop < costoMiglioreSoluzioneGlobale) {
            	miglioreSoluzioneGlobale = soluzioneCorrenteNelLoop;
            	costoMiglioreSoluzioneGlobale = costoSoluzioneCorrenteNelLoop;
            }
                        
        }
        
        //visualizza dati soluzione migliore trovata
        visualizzaAltezzaSoluzione(miglioreSoluzioneGlobale, "ALTEZZE DOPO");
        visualizzaSfasamentoSoluzione(miglioreSoluzioneGlobale, "SFASAMENTO DOPO");
        miglioreSoluzioneGlobale.printSoluzione();
        System.out.println("costo finale " + costoMiglioreSoluzioneGlobale);
        System.out.println("altezza max finale " + miglioreSoluzioneGlobale.altezzaMassima());
        System.out.println("sfasamento max finale " + miglioreSoluzioneGlobale.sfasamento());
        
        if(miglioreSoluzioneGlobale.sfasamento() > Soluzione.massimoSfasamentoConsentito) {
        	System.out.println("piano impossibile, troppo sbilanciamento di fase nel punto: " + miglioreSoluzioneGlobale.puntoMassimoSfasamento().toString());
        }
        
        if(miglioreSoluzioneGlobale.costoSoluzione() > Soluzione.massimaAltezzaConsentita) {
        	System.out.println("piano impossibile, troppa richiesta di potenza nel punto: " + miglioreSoluzioneGlobale.puntoCritico().toString());
        }
        
	}

	
	public static Soluzione simulatedAnnealing(Soluzione soluzioneCorrente, double costoSoluzioneCorrente) throws RectImpossibleException {
		//in principio la soluzione migliore è quella iniziale
		Soluzione soluzioneMigliore = soluzioneCorrente.clone();
		double costoSoluzioneMigliore = costoSoluzioneCorrente;
		
        double temperaturaIniziale = 100000000;
        double raffreddamneto = 0.0001;
        Random rand = new Random();
        
        for(double t = temperaturaIniziale; t > 1; t *= (1 - raffreddamneto)){
        	
        	//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
        	Soluzione nuovaSoluzione = soluzioneCorrente.generaNuovaSoluzioneCasuale();
        	
        	//se il costo della nuova soluzione è < costo vecchia soluzione, accetta la nuova soluzione e aggiorna la soluzione migliore, se serve
        	double costoNuovaSoluzione = nuovaSoluzione.costoSoluzione();
        	if(costoNuovaSoluzione < costoSoluzioneCorrente) {
        		soluzioneCorrente = nuovaSoluzione;
        		costoSoluzioneCorrente = costoNuovaSoluzione;
        		
        		if(costoSoluzioneCorrente < costoSoluzioneMigliore) {
            		costoSoluzioneMigliore = costoSoluzioneCorrente;
            		soluzioneMigliore = soluzioneCorrente;
        		}
        		
        	}
        	//se il costo è invece maggiore, accetta la soluzione con un probabilià e ^ (costoVecchiaSol - costoNuovaSol)/temperatura)
        	else {
        		double probability = Math.exp((costoSoluzioneCorrente - costoNuovaSoluzione)/t);
        		if(probability >= rand.nextDouble()) {
        			soluzioneCorrente = nuovaSoluzione;
            		costoSoluzioneCorrente = costoNuovaSoluzione;
        		}
        	}

        }
        
        return soluzioneMigliore;
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
