package progetto;

import java.util.Collections;
import java.util.Random;

import utils.RequestImpossibleException;
import utils.ordinamento.ComparatorTempoDiFine;

public class AlgoritmoSimulatedAnnealing {
	
	//vecchio metodo
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
			return  soluzConRettangoliDistribuiti;	//preProcessAnnealing2(soluzConRettangoliDistribuiti);
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
		return 	soluzConRettangoliDistribuiti;	//preProcessAnnealing2(soluzConRettangoliDistribuiti);
		
	}
	
	
	//crea 10 situazioni iniziali a caso (sposta i rettangoli che hanno più tempo del necessario) e prende quella migliore
	public static Soluzione preProcessing2(Soluzione soluzioneIniziale) throws RequestImpossibleException {
		Soluzione distribuzioneMigliore = soluzioneIniziale;
		double costoDistribuzioneMigliore = soluzioneIniziale.costoSoluzione();
		
		for(int iteraz = 1; iteraz < 10; iteraz++) {
			Soluzione soluzConRettangoliDistribuiti = soluzioneIniziale.generaNuovaSituazioneDiPartenza();
			double costoSoluzConRettangoliDistribuiti = soluzConRettangoliDistribuiti.costoSoluzione();
			
			if(troncaCifreDopoVirgola(costoDistribuzioneMigliore) > troncaCifreDopoVirgola(costoSoluzConRettangoliDistribuiti)) {
				distribuzioneMigliore = soluzConRettangoliDistribuiti;
				costoDistribuzioneMigliore = costoSoluzConRettangoliDistribuiti;
			}
			//se hanno lo stesso costo, predilige le soluzioni con meno intersezioni
			else if(troncaCifreDopoVirgola(costoDistribuzioneMigliore) == troncaCifreDopoVirgola(costoSoluzConRettangoliDistribuiti)) {
				if(distribuzioneMigliore.contaIntersezioni() > soluzConRettangoliDistribuiti.contaIntersezioni()) {
					distribuzioneMigliore = soluzConRettangoliDistribuiti;
					costoDistribuzioneMigliore = costoSoluzConRettangoliDistribuiti;
				}
			}
		}
		
		//usa la distribuzione migliore come punto di partenza per l'annealing che trasla i rettangoli
		return preProcessAnnealing(distribuzioneMigliore);
	}
	
	
	public static Soluzione simulatedAnnealing(Soluzione soluzioneCorrente) throws RequestImpossibleException {
		//se il preprocessing ha portato ad una situazione senza intersezioni con rettangoli di base massima
		if(soluzioneCorrente.contaIntersezioni() == 0) {
			return soluzioneCorrente;
		}
		
		double costoSoluzioneCorrente = soluzioneCorrente.costoSoluzione();
		Soluzione soluzioneMigliore = soluzioneCorrente.clone();		//in principio la soluzione migliore è quella iniziale
		double costoSoluzioneMigliore = costoSoluzioneCorrente;
		
		//la temperatura iniziale è alta per avere probabilità quasi = 1 di scegliere soluzioni peggiori all'inizio
        double temperaturaIniziale = 1000, temperaturaFinale = 0.1, temperatura = temperaturaIniziale;
        double raffreddamneto = 0.99;
        Random rand = new Random();   
     
        while(temperatura > temperaturaFinale) {
        	//al diminuire della temperatura, diminuisce il raggio di ricerca della nuova soluzione
        	int passoGenerazioneRandomica = 15;
        	
        	for(int iterazione = 0; iterazione < 1000; iterazione++) {  //cerca tra 1000 soluzioni a temperatura costante
        		
        		//genera nuova soluzione tramite perturbazioni casuali della soluzione attuale
        		Soluzione nuovaSoluzione = soluzioneCorrente.generaNuovaSoluzioneCasuale(passoGenerazioneRandomica);
        		double costoNuovaSoluzione = nuovaSoluzione.costoSoluzione();
        		
        		//se il costo della nuova soluzione è < costo vecchia soluzione, accetta la nuova soluzione
        		if(troncaCifreDopoVirgola(costoNuovaSoluzione) < troncaCifreDopoVirgola(costoSoluzioneCorrente)) {
        			soluzioneCorrente = nuovaSoluzione;
        			costoSoluzioneCorrente = costoNuovaSoluzione;
        			
        			//aggiorna il valore della soluzione migliore, se serve
            		if(troncaCifreDopoVirgola(costoNuovaSoluzione) < troncaCifreDopoVirgola(costoSoluzioneMigliore)) {
            			costoSoluzioneMigliore = costoNuovaSoluzione;
            			soluzioneMigliore = nuovaSoluzione;
            		}
            		//se la nuova soluz ha lo stesso costo della migliore, controlla quale ha i rettangoli mediamente più larghi e con meno intersezioni
            		else if (troncaCifreDopoVirgola(costoNuovaSoluzione) == troncaCifreDopoVirgola(costoSoluzioneMigliore)) {
            			double costoSecondarioSoluzNuova = nuovaSoluzione.mediaInnalzamentoRettangoli() + nuovaSoluzione.contaIntersezioni();
            			double costoSecondarioSoluzMigliore = soluzioneMigliore.mediaInnalzamentoRettangoli() + soluzioneMigliore.contaIntersezioni();
            			if (costoSecondarioSoluzNuova < costoSecondarioSoluzMigliore) {
            				soluzioneMigliore = nuovaSoluzione;
            			}
            		}
        		} 
        		
        		//se la nuova soluz ha lo stesso costo della vecchia, controlla quale ha i rettangoli mediamente più larghi e con meno intersezioni
        		else if(troncaCifreDopoVirgola(costoNuovaSoluzione) == troncaCifreDopoVirgola(costoSoluzioneCorrente)) {
        			double costoSecondarioSoluzNuova = nuovaSoluzione.mediaInnalzamentoRettangoli() + nuovaSoluzione.contaIntersezioni();
        			double costoSecondarioSoluzCorrente = soluzioneCorrente.mediaInnalzamentoRettangoli() + soluzioneCorrente.contaIntersezioni();
        			if (costoSecondarioSoluzNuova < costoSecondarioSoluzCorrente) {
        				soluzioneCorrente = nuovaSoluzione;
        			}
        		}
            	
        		//se il costo è invece maggiore, accetta la soluzione con un probabilià e ^ (costoVecchiaSol - costoNuovaSol)/temperatura)
        		else {
        			double probability = Math.exp( ((troncaCifreDopoVirgola(costoSoluzioneCorrente) - troncaCifreDopoVirgola(costoNuovaSoluzione)) / temperatura) );
        			if(probability >= rand.nextDouble()) {
        				soluzioneCorrente = nuovaSoluzione;
        				costoSoluzioneCorrente = costoNuovaSoluzione;
        			}
        		}	
        		
        	}//for
        	temperatura *= raffreddamneto;
        	
        }//while
        
        return soluzioneMigliore;
	}
	
	
	public static Soluzione simulatedAnnealing2(Soluzione soluzioneCorrente) throws RequestImpossibleException {
		double costoSoluzioneCorrente = soluzioneCorrente.costoSoluzione();
		
		//in principio la soluzione migliore è quella iniziale
		Soluzione soluzioneMigliore = soluzioneCorrente.clone();
		double costoSoluzioneMigliore = costoSoluzioneCorrente;
		
        double temperaturaIniziale = 1000000;
        double raffreddamneto = 0.00001;
        Random rand = new Random();
        
        for(double t = temperaturaIniziale; t > 1; t *= (1 - raffreddamneto)){
        	//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
        	Soluzione nuovaSoluzione = soluzioneCorrente.generaNuovaSoluzioneCasuale(  10  );
        	
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
	
	
	//per risolvere prima il problema di distribuire i rect
	public static Soluzione preProcessAnnealing(Soluzione soluzioneCorrente) throws RequestImpossibleException {
		double costoSoluzioneCorrente = soluzioneCorrente.costoSoluzione();
		//in principio la soluzione migliore è quella iniziale
		Soluzione soluzioneMigliore = soluzioneCorrente.clone();
		double costoSoluzioneMigliore = costoSoluzioneCorrente;
		
        double temperaturaIniziale = 1000, temperaturaFinale = 0.1, temperatura = temperaturaIniziale;
        double raffreddamneto = 0.9;
        Random rand = new Random();
        while(temperatura > temperaturaFinale) {
        	for(int iterazione = 0; iterazione < 800; iterazione++) {
            	//al diminuire della temperatura, diminuisce il raggio di ricerca della nuova soluzione
            	int passoGenerazioneRandomica = 20;//(int)(temperatura / 50) + 1;
            	
        		//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
        		Soluzione nuovaSoluzione = soluzioneCorrente.generaNuovaSoluzioneCasualeTraslazione(passoGenerazioneRandomica);

        		//se il costo della nuova soluzione è < costo vecchia soluzione, accetta la nuova soluzione e aggiorna la soluzione migliore, se serve
        		double costoNuovaSoluzione = nuovaSoluzione.costoSoluzione();
        		if(troncaCifreDopoVirgola(costoNuovaSoluzione) <= troncaCifreDopoVirgola(costoSoluzioneCorrente)) {
        			soluzioneCorrente = nuovaSoluzione;
        			costoSoluzioneCorrente = costoNuovaSoluzione;
        			
        			//aggiorna il valore della soluzione migliore
            		if(troncaCifreDopoVirgola(costoNuovaSoluzione) < troncaCifreDopoVirgola(costoSoluzioneMigliore)) {
            			costoSoluzioneMigliore = costoSoluzioneCorrente;
            			soluzioneMigliore = soluzioneCorrente;
            		}
        		}            	
        		//se il costo è invece maggiore, accetta la soluzione con un probabilià e ^ (costoVecchiaSol - costoNuovaSol)/temperatura)
        		else {
        			double probability = Math.exp( ((troncaCifreDopoVirgola(costoSoluzioneCorrente) - troncaCifreDopoVirgola(costoNuovaSoluzione))/temperatura) );
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
        	Soluzione nuovaSoluzione = soluzioneCorrente.generaNuovaSoluzioneCasualeTraslazione( 20 );
        	
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
	
	
	private static double troncaCifreDopoVirgola(double numero) {
		//tronco il costo della soluzione a 5 cifre dopo la virgola
		double precisione = 100000.0;
		return Math.floor(numero*precisione)/precisione;
	}
	
}
