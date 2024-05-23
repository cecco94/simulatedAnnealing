package progetto;

import java.util.Collections;
import java.util.Random;

import utils.RequestImpossibleException;
import utils.ordinamento.ComparatorTempoDiFine;

public class AlgoritmoSimulatedAnnealing {

	
	public static Soluzione preProcessing(Soluzione soluzioneIniziale) throws RequestImpossibleException {
		//per noi, la durata del piano di ricarica è = minuto finale massimo dell'ultimo rect - minuto iniziale minimo del primo rect
		int minutoPartenzaPiano = soluzioneIniziale.rettangoli.get(0).margineSinistroMinimo;
		
		//ordino i rect dall'ultimo al primo per vedere qual è il massimo margine destro
		ComparatorTempoDiFine ctf = new ComparatorTempoDiFine();
		Collections.sort(soluzioneIniziale.rettangoli, ctf);
		int minutoFinePiano = soluzioneIniziale.rettangoli.get(0).margineDestroMassimo;
		//fatto ciò, riordino i rettangoli com'erano prima
		Collections.sort(soluzioneIniziale.rettangoli);
		
		int durataPianoRicarica =  minutoFinePiano - minutoPartenzaPiano;
		
		//sommo la durata di tutti i rettangoli
		int sommaBaseRettangoli = 0;
		for(int i = 0; i < soluzioneIniziale.rettangoli.size(); i++) {
			sommaBaseRettangoli += soluzioneIniziale.rettangoli.get(i).baseMassima;
		}
		
		//se la durata del piano è maggiore della somma delle basi dei rettangoli, allora è forse possibile separarli
		int spazioTraDueRicariche = (durataPianoRicarica-sommaBaseRettangoli)/soluzioneIniziale.rettangoli.size();
		if(spazioTraDueRicariche > 0) {
			//sistema i rect equispaziandoli quando possibile
			int puntoPartenza = 0;
			for(int i = 0; i < soluzioneIniziale.rettangoli.size(); i++) {
				Rettangolo r = soluzioneIniziale.rettangoli.get(i);
				if(puntoPartenza + r.baseMassima < r.margineDestroMassimo && puntoPartenza >= r.margineSinistroMinimo) {
					r.margineSinistro = puntoPartenza;
					r.margineDestro = puntoPartenza + r.baseMassima;
					puntoPartenza = spazioTraDueRicariche + r.margineDestro;
				}
			}
			
			Soluzione soluzConRettangoliDistribuiti = new Soluzione(soluzioneIniziale.rettangoli);
			return preProcessAnnealing2(soluzConRettangoliDistribuiti);
		}
	
		//altrimenti li metto uno dopo l'altro e quando arrivo a fine nottata riparto da capo (oppure li inizio a mettere a caso)
		int puntoPartenza = 0;
		for(int i = 0; i < soluzioneIniziale.rettangoli.size(); i++) {
			Rettangolo r = soluzioneIniziale.rettangoli.get(i);
			if(puntoPartenza + r.baseMassima < r.margineDestroMassimo && puntoPartenza >= r.margineSinistroMinimo) {
				r.margineSinistro = puntoPartenza;
				r.margineDestro = puntoPartenza + r.baseMassima;
				puntoPartenza = r.margineDestro;
			}
			//quando arrivo a fine nottata
			else if(puntoPartenza + r.baseMassima > minutoFinePiano) {
				System.out.println(r.toString());
				System.out.println("punto partenza " + puntoPartenza);
				System.out.println("fine piano " + minutoFinePiano);
				puntoPartenza = 0;
			}
		}
		
		Soluzione soluzConRettangoliDistribuiti = new Soluzione(soluzioneIniziale.rettangoli);
		return preProcessAnnealing2(soluzConRettangoliDistribuiti);
		
	}
	
	
	public static Soluzione simulatedAnnealing(Soluzione soluzioneCorrente) throws RequestImpossibleException {
		double costoSoluzioneCorrente = soluzioneCorrente.costoSoluzione();

		//in principio la soluzione migliore è quella iniziale
		Soluzione soluzioneMigliore = soluzioneCorrente.clone();
		double costoSoluzioneMigliore = costoSoluzioneCorrente;
		
        double temperaturaIniziale = 1, temperaturaFinale = 0.0001, temperatura = temperaturaIniziale;
        double raffreddamneto = 0.99;
        Random rand = new Random();
        
        while(temperatura > temperaturaFinale) {
        	for(int iterazione = 0; iterazione < 1000; iterazione++) {

        		//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
        		Soluzione nuovaSoluzione = soluzioneCorrente.generaNuovaSoluzioneCasuale();

        		//se il costo della nuova soluzione è < costo vecchia soluzione, accetta la nuova soluzione e aggiorna la soluzione migliore, se serve
        		double costoNuovaSoluzione = nuovaSoluzione.costoSoluzione();
        		if(costoNuovaSoluzione < costoSoluzioneCorrente) {
        			soluzioneCorrente = nuovaSoluzione;
        			costoSoluzioneCorrente = costoNuovaSoluzione;
        			
        			//aggiorna il valore della soluzione migliore
            		if(costoSoluzioneCorrente < costoSoluzioneMigliore) {
            			costoSoluzioneMigliore = costoSoluzioneCorrente;
            			soluzioneMigliore = soluzioneCorrente;
            		}
        		}
        		//se hanno la stessa altezza massima, la soluzione migliore è quella con i rettangoli mediamente più larghi
        		else if(costoNuovaSoluzione == costoSoluzioneCorrente) {
        			if(nuovaSoluzione.mediaInnalzamentoRettangoli < soluzioneCorrente.mediaInnalzamentoRettangoli) {
        				soluzioneCorrente = nuovaSoluzione;
        			}
        		}
            	
        		//se il costo è invece maggiore, accetta la soluzione con un probabilià e ^ (costoVecchiaSol - costoNuovaSol)/temperatura)
        		else {
        			double probability = Math.exp( ((costoSoluzioneCorrente - costoNuovaSoluzione)/temperatura) );
        			if(probability >= rand.nextDouble()) {
        				//System.out.println("differenza " + (costoSoluzioneCorrente - costoNuovaSoluzione) + ", prob " + probability + ", temp " + temperatura);
        				soluzioneCorrente = nuovaSoluzione;
        				costoSoluzioneCorrente = costoNuovaSoluzione;
        			}
        		}
        	}	
        	temperatura *= raffreddamneto;
        }
        
        return soluzioneMigliore;
	}
	
	
	public static Soluzione simulatedAnnealing2(Soluzione soluzioneCorrente, double costoSoluzioneCorrente) throws RequestImpossibleException {
		
		//in principio la soluzione migliore è quella iniziale
			Soluzione soluzioneMigliore = soluzioneCorrente.clone();
			double costoSoluzioneMigliore = costoSoluzioneCorrente;
			
	        double temperaturaIniziale = 1000000;
	        double raffreddamneto = 0.00001;
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
	
	
	//per fare misurazioni e test
//	public static double misuraSimulatedAnnealing(Soluzione soluzioneCorrente, double costoSoluzioneCorrente, double temp, double raffreddam) throws RequestImpossibleException {
//		Soluzione soluzioneMigliore = soluzioneCorrente.clone();
//		double costoSoluzioneMigliore = costoSoluzioneCorrente;
//		
//        double temperaturaIniziale = temp;		//100 000 000;
//        double raffreddamneto = raffreddam;   //0.000 01;
//        Random rand = new Random();
//        
//		long inizio = System.currentTimeMillis();
//        
//        //la temperatura diminuisce in modo geometrico
//        for(double t = temperaturaIniziale; t > 1; t *= (1 - raffreddamneto)){
//        	//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
//        	Soluzione nuovaSoluzione = soluzioneCorrente.generaNuovaSoluzioneCasuale();
//        	
//        	//se il costo della nuova soluzione è < costo vecchia soluzione, accetta la nuova soluzione e aggiorna la soluzione migliore, se serve
//        	double costoNuovaSoluzione = nuovaSoluzione.costoSoluzione();
//        	if(costoNuovaSoluzione < costoSoluzioneCorrente) {
//        		soluzioneCorrente = nuovaSoluzione;
//        		costoSoluzioneCorrente = costoNuovaSoluzione;
//        		
//        		if(costoSoluzioneCorrente < costoSoluzioneMigliore) {
//            		costoSoluzioneMigliore = costoSoluzioneCorrente;
//            		soluzioneMigliore = soluzioneCorrente;
//        		}
//        	}
//        	//se il costo è invece maggiore, accetta la soluzione con un probabilià e ^ (costoVecchiaSol - costoNuovaSol)/temperatura)
//        	else {
//        		double probability = Math.exp((costoSoluzioneCorrente - costoNuovaSoluzione)/t);
//        		if(probability >= rand.nextDouble()) {
//        			soluzioneCorrente = nuovaSoluzione;
//            		costoSoluzioneCorrente = costoNuovaSoluzione;
//        		}
//        	}
//        } 
//        
////    	if(TestClass.costo_su_passo || TestClass.costo_su_raffreddamento || TestClass.costo_su_temperatura) {
////            return costoSoluzioneMigliore;
////    	}
//    	
//    	long tempoImpiegato = System.currentTimeMillis() - inizio;
//    	System.out.println("tempo1, " + tempoImpiegato);
//    	return costoSoluzioneMigliore;
//        
//	}

	
//	public static double misuraSimulatedAnnealing2(Soluzione soluzioneCorrente, double costoSoluzioneCorrente) throws RequestImpossibleException {
//		//in principio la soluzione migliore è quella iniziale
//		Soluzione soluzioneMigliore = soluzioneCorrente.clone();
//		double costoSoluzioneMigliore = costoSoluzioneCorrente;
//		
//        double temperaturaIniziale = 1, temperaturaFinale = 0.0001, temperatura = temperaturaIniziale;
//        double raffreddamneto = 0.99;
//        Random rand = new Random();
//        
//        
//        long inizio = System.currentTimeMillis();
//        
//        while(temperatura > temperaturaFinale) {
//        	for(int iterazione = 0; iterazione < 1000; iterazione++) {
//
//        		//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
//        		Soluzione nuovaSoluzione = soluzioneCorrente.generaNuovaSoluzioneCasuale();
//
//        		//se il costo della nuova soluzione è < costo vecchia soluzione, accetta la nuova soluzione e aggiorna la soluzione migliore, se serve
//        		double costoNuovaSoluzione = nuovaSoluzione.costoSoluzione();
//        		if(costoNuovaSoluzione < costoSoluzioneCorrente) {
//        			soluzioneCorrente = nuovaSoluzione;
//        			costoSoluzioneCorrente = costoNuovaSoluzione;
//        			
//        			//aggiorna il valore della soluzione migliore
//            		if(costoSoluzioneCorrente < costoSoluzioneMigliore) {
//            			costoSoluzioneMigliore = costoSoluzioneCorrente;
//            			soluzioneMigliore = soluzioneCorrente;
//            		}
//        		}
//        		
//        		else if(costoNuovaSoluzione == costoSoluzioneCorrente) {
//        			if(nuovaSoluzione.mediaInnalzamentoRettangoli < soluzioneCorrente.mediaInnalzamentoRettangoli) {
//        				soluzioneCorrente = nuovaSoluzione;
//        			}
//        		}
//            	
//        		//se il costo è invece maggiore, accetta la soluzione con un probabilià e ^ (costoVecchiaSol - costoNuovaSol)/temperatura)
//        		else {
//        			double probability = Math.exp( ((costoSoluzioneCorrente - costoNuovaSoluzione)/temperatura) );
//        			if(probability >= rand.nextDouble()) {
//        				//System.out.println("differenza " + (costoSoluzioneCorrente - costoNuovaSoluzione) + ", prob " + probability + ", temp " + temperatura);
//        				soluzioneCorrente = nuovaSoluzione;
//        				costoSoluzioneCorrente = costoNuovaSoluzione;
//        			}
//        		}
//        	}	
//        	temperatura *= raffreddamneto;
//        }
//        System.out.println("tempo2, " + (System.currentTimeMillis() - inizio));
//        return costoSoluzioneMigliore;
//	}
	
	
	//per risolvere prima il problema di distribuire i rect
	public static Soluzione preProcessAnnealing(Soluzione soluzioneCorrente) throws RequestImpossibleException {
		double costoSoluzioneCorrente = soluzioneCorrente.costoSoluzione();
		//in principio la soluzione migliore è quella iniziale
		Soluzione soluzioneMigliore = soluzioneCorrente.clone();
		double costoSoluzioneMigliore = costoSoluzioneCorrente;
		
        double temperaturaIniziale = 1, temperaturaFinale = 0.0001, temperatura = temperaturaIniziale;
        double raffreddamneto = 0.99;
        Random rand = new Random();
        
        while(temperatura > temperaturaFinale) {
        	for(int iterazione = 0; iterazione < 1000; iterazione++) {

        		//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
        		Soluzione nuovaSoluzione = soluzioneCorrente.generaNuovaSoluzioneCasualeTraslazione();

        		//se il costo della nuova soluzione è < costo vecchia soluzione, accetta la nuova soluzione e aggiorna la soluzione migliore, se serve
        		double costoNuovaSoluzione = nuovaSoluzione.costoSoluzione();
        		if(costoNuovaSoluzione <= costoSoluzioneCorrente) {
        			soluzioneCorrente = nuovaSoluzione;
        			costoSoluzioneCorrente = costoNuovaSoluzione;
        			
        			//aggiorna il valore della soluzione migliore
            		if(costoSoluzioneCorrente < costoSoluzioneMigliore) {
            			costoSoluzioneMigliore = costoSoluzioneCorrente;
            			soluzioneMigliore = soluzioneCorrente;
            		}
        		}            	
        		//se il costo è invece maggiore, accetta la soluzione con un probabilià e ^ (costoVecchiaSol - costoNuovaSol)/temperatura)
        		else {
        			double probability = Math.exp( ((costoSoluzioneCorrente - costoNuovaSoluzione)/temperatura) );
        			if(probability >= rand.nextDouble()) {
        				soluzioneCorrente = nuovaSoluzione;
        				costoSoluzioneCorrente = costoNuovaSoluzione;
        			}
        		}
        	}	
        	temperatura *= raffreddamneto;
        }
        
        return soluzioneMigliore;
	}
	
	
	public static Soluzione preProcessAnnealing2(Soluzione soluzioneCorrente ) throws RequestImpossibleException {
		
		double costoSoluzioneCorrente = soluzioneCorrente.costoSoluzione();
		//in principio la soluzione migliore è quella iniziale
		Soluzione soluzioneMigliore = soluzioneCorrente.clone();
		double costoSoluzioneMigliore = costoSoluzioneCorrente;
		
        double temperaturaIniziale = 100000;
        double raffreddamneto = 0.0001;
        Random rand = new Random();
        
        for(double t = temperaturaIniziale; t > 1; t *= (1 - raffreddamneto)){
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
        return soluzioneMigliore;
	}
	
}
