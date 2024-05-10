package progetto;

import java.util.Random;

import utils.RectImpossibleException;

public class SimulatedAnnealingTraslazione {

	public static Soluzione simulatedAnnealingTraslaz(Soluzione soluzioneIniziale) throws RectImpossibleException {
		
		//in principio la soluzione migliore è quella iniziale
		Soluzione soluzioneMigliore = soluzioneIniziale.clone();
		double costoSoluzioneCorrente = soluzioneIniziale.costoSoluzione();
		double costoSoluzioneMigliore = costoSoluzioneCorrente;
		
        double temperaturaIniziale = 100000000;
        double raffreddamneto = 0.0001;
        Random rand = new Random();
        
        for(double t = temperaturaIniziale; t > 1; t *= (1 - raffreddamneto)){
        	
        	//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
        	Soluzione nuovaSoluzione = soluzioneIniziale.generaNuovaSoluzioneCasualeTraslazione();
        	
        	//se il costo della nuova soluzione è < costo vecchia soluzione, accetta la nuova soluzione e aggiorna la soluzione migliore, se serve
        	double costoNuovaSoluzione = nuovaSoluzione.costoSoluzione();
        	if(costoNuovaSoluzione < costoSoluzioneCorrente) {
        		soluzioneIniziale = nuovaSoluzione;
        		costoSoluzioneCorrente = costoNuovaSoluzione;
        		
        		if(costoSoluzioneCorrente < costoSoluzioneMigliore) {
            		costoSoluzioneMigliore = costoSoluzioneCorrente;
            		soluzioneMigliore = soluzioneIniziale;
        		}
        		
        	}
        	//se il costo è invece maggiore, accetta la soluzione con un probabilià e ^ (costoVecchiaSol - costoNuovaSol)/temperatura)
        	else {
        		double probability = Math.exp((costoSoluzioneCorrente - costoNuovaSoluzione)/t);
        		if(probability >= rand.nextDouble()) {
        			soluzioneIniziale = nuovaSoluzione;
            		costoSoluzioneCorrente = costoNuovaSoluzione;
        		}
        	}
        }
        return soluzioneMigliore;
	}
	
	
}
