package progetto;

import java.util.Random;

import utils.RectImpossibleException;

public class AlgoritmoSimulatedAnnealing {

	public static Soluzione simulatedAnnealing(Soluzione soluzioneCorrente, double costoSoluzioneCorrente) throws RectImpossibleException {
		//in principio la soluzione migliore è quella iniziale
		Soluzione soluzioneMigliore = soluzioneCorrente.clone();
		double costoSoluzioneMigliore = costoSoluzioneCorrente;
		
        double temperaturaIniziale = 10000000;
        double raffreddamneto = 0.00001;
        Random rand = new Random();
        
        //long start = System.currentTimeMillis();
        //int i = 0;
        //la temperatura diminuisce in modo geometrico
        for(double t = temperaturaIniziale; t > 1; t *= (1 - raffreddamneto)){
        	//i++;
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
        //System.out.println("tempo impiegato " + (System.currentTimeMillis() - start)/1000.0 + " secondi");
        //System.out.println("iterazioni " + i);   
        return soluzioneMigliore;
	}

	
	//per fare misurazioni e test
	public static double misuraSimulatedAnnealing(Soluzione soluzioneCorrente, double costoSoluzioneCorrente, double temp, double raffreddam) throws RectImpossibleException {
		Soluzione soluzioneMigliore = soluzioneCorrente.clone();
		double costoSoluzioneMigliore = costoSoluzioneCorrente;
		
        double temperaturaIniziale = temp;		//100 000 000;
        double raffreddamneto = raffreddam;   //0.000 01;
        Random rand = new Random();
        
		long inizio = System.currentTimeMillis();
        
        //la temperatura diminuisce in modo geometrico
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
        
    	if(TestClass.costo_su_passo || TestClass.costo_su_raffreddamento || TestClass.costo_su_temperatura) {
            return costoSoluzioneMigliore;
    	}
    	
    	long tempoImpiegato = System.currentTimeMillis() - inizio;
    	return tempoImpiegato;
        
	}

	
	
	//per risolvere prima il problema di distribuire i rect
	public static Soluzione simulatedAnnealingTraslaz(Soluzione soluzioneCorrente, double costoSoluzioneCorrente) throws RectImpossibleException {
		
		//in principio la soluzione migliore è quella iniziale
			Soluzione soluzioneMigliore = soluzioneCorrente.clone();
			double costoSoluzioneMigliore = costoSoluzioneCorrente;
			
	        double temperaturaIniziale = 100000000;
	        double raffreddamneto = 0.00001;
	        Random rand = new Random();
	        
	        long start = System.currentTimeMillis();
	        int i = 0;
	        for(double t = temperaturaIniziale; t > 1; t *= (1 - raffreddamneto)){
	        	//i++;
	        	//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
	        	Soluzione nuovaSoluzione = soluzioneCorrente.generaNuovaSoluzioneCasualeTraslazione();
	        	
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
	        //System.out.println("tempo impiegato " + (System.currentTimeMillis() - start)/1000.0 + " secondi");
	        //System.out.println("iterazioni " + i);   
	        return soluzioneMigliore;
	}
	
}
