package progetto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import utils.RequestImpossibleException;
import utils.ordinamento.ComparatorSfasamento;
import utils.ordinamento.ComparatorSommaAltezze;
import utils.ordinamento.ComparatorTempoDiInizio;


public class Soluzione {

    public ArrayList<Rettangolo> rettangoli;
    public ArrayList<Punto> puntiDiInizioFineRettangoli;
    public static double massimoSfasamentoConsentito = 4.2;
    public static double massimaAltezzaConsentita = 7.4;
    
    
    //serve per creare il json
    public Soluzione() {  }
    
    
    public Soluzione(ArrayList<Rettangolo> rettangoli) {
    	this.rettangoli = rettangoli;
    	
    	//ordina i rettangoli per punto di inizio
 	    Collections.sort(rettangoli);
 	   
    	puntiDiInizioFineRettangoli = new ArrayList<>();
    	
    	//sistema i punti di inizio e fine dei rettangoli
    	for(int i = 0; i < rettangoli.size(); i++) {
    		Rettangolo r = rettangoli.get(i);
    		puntiDiInizioFineRettangoli.add(new Punto(r, r.margineSinistro, r.altezza, true));
    		puntiDiInizioFineRettangoli.add(new Punto(r, r.margineDestro, r.altezza, false));
    	}
    	ComparatorTempoDiInizio c = new ComparatorTempoDiInizio();
    	Collections.sort(puntiDiInizioFineRettangoli, c);	    	
    }
   
   
   public double costoSoluzione() {
	   return altezzaMassima() + 100*sfasamento();
   }
   
   
   public double altezzaMassima() {
	   double h = 0;
	   double maxH = 0;
	   for(int i = 0; i < puntiDiInizioFineRettangoli.size(); i++) {
		   
		   Punto p = puntiDiInizioFineRettangoli.get(i);
		   
		   if(p.punto_di_inizio) {
			   h += p.altezzaRettangolo;
			   p.sommaAltezzeNelPunto = h;
		   }		   
		   else {
			   h -= p.altezzaRettangolo;
			   p.sommaAltezzeNelPunto = h;
		   }
		   if(h > maxH)
			   maxH = h;
	   }
	   return maxH;
   }
   
   
   public double sfasamento() {
	   double h_fase_1 = 0;
	   double h_fase_2 = 0;
	   double h_fase_3 = 0;
	   double sfasamento_massimo = 0; 
	   
	   for(int i = 0; i < puntiDiInizioFineRettangoli.size(); i++) {
		   
		   Punto p = puntiDiInizioFineRettangoli.get(i);
		   
		   if(p.punto_di_inizio) {
			   if(p.r.fase == 1) {
				   h_fase_1 += p.altezzaRettangolo;
			   }
			   else if(p.r.fase == 2) {
				   h_fase_2 += p.altezzaRettangolo;
			   }
			   else if(p.r.fase == 3) {
				   h_fase_3 += p.altezzaRettangolo;
			   }
			   else if(p.r.fase == 0) {
				   h_fase_1 += p.altezzaRettangolo/3;
				   h_fase_2 += p.altezzaRettangolo/3;
				   h_fase_3 += p.altezzaRettangolo/3;
			   }
		   }
			   
		   else {
			   if(p.r.fase == 1) {
				   h_fase_1 -= p.altezzaRettangolo;
			   }
			   else if(p.r.fase == 2) {
				   h_fase_2 -= p.altezzaRettangolo;
			   }
			   else if(p.r.fase == 3) {
				   h_fase_3 -= p.altezzaRettangolo;
			   }
			   else if(p.r.fase == 0) {
				   h_fase_1 -= p.altezzaRettangolo/3;
				   h_fase_2 -= p.altezzaRettangolo/3;
				   h_fase_3 -= p.altezzaRettangolo/3;
			   }
		   }
		   
		   double sfasamento_fase_1_2 = Math.abs(h_fase_1 - h_fase_2);
		   double sfasamento_fase_1_3 = Math.abs(h_fase_1 - h_fase_3);
		   double sfasamento_fase_2_3 = Math.abs(h_fase_2 - h_fase_3);

		   double sfasamento_complessivo = Math.max(sfasamento_fase_1_2, Math.max(sfasamento_fase_1_3, sfasamento_fase_2_3)); 
		   p.sfasamentoNelPunto = sfasamento_complessivo;
		   
		   if(sfasamento_complessivo > sfasamento_massimo) {
			   sfasamento_massimo = sfasamento_complessivo;
		   }
	   }
	   
	   if(sfasamento_massimo < massimoSfasamentoConsentito) {
		   return 0;
	   }
		   
	   return sfasamento_massimo;
   }
   
   
   public int contaIntersezioni() {
	   int intersezioni = 0;
	   //prende tutti i punti di inizio
	   for(int i = 0; i < puntiDiInizioFineRettangoli.size(); i++) {
		   Punto p = puntiDiInizioFineRettangoli.get(i);
		   if(p.punto_di_inizio) {
			  int md = p.r.margineDestro;
			  //prende tutti i punti di inizio successivi fino alla fine del rettangolo
			  for(int j = i+1; j < puntiDiInizioFineRettangoli.size(); j++) {
				  Punto p2 = puntiDiInizioFineRettangoli.get(j);
				  if(p2.x > md) 
					  break;
				  //ogni volta che ne trova uno, aumenta le intersezioni
				  if(p2.punto_di_inizio) 
					  intersezioni++;
			  }
		   }		   
	   }
	   return intersezioni;
   }

   
   public double mediaInnalzamentoRettangoli() {
	   double mediaInnalzamentoRettangoli = 0;
	   for(int i = 0; i < rettangoli.size(); i++) {
		   
		   Rettangolo r = rettangoli.get(i);
		   mediaInnalzamentoRettangoli += (r.baseMassima - r.base);
	   }
	   
	   mediaInnalzamentoRettangoli /= rettangoli.size();
	   return mediaInnalzamentoRettangoli;
   }
   
   
   public Punto puntoMaxAltezza() {
	   ComparatorSommaAltezze cc = new ComparatorSommaAltezze();
	   Collections.sort(puntiDiInizioFineRettangoli, cc);
	   
	   Punto p = puntiDiInizioFineRettangoli.get(0);
	   
	   ComparatorTempoDiInizio cs = new ComparatorTempoDiInizio();
	   Collections.sort(puntiDiInizioFineRettangoli, cs);
	   
	   return p;
   }
   
  
   public Punto puntoMassimoSfasamento() {
	   ComparatorSfasamento c = new ComparatorSfasamento();
	   Collections.sort(puntiDiInizioFineRettangoli, c);
	   
	   Punto p = puntiDiInizioFineRettangoli.get(0);
	   
	   ComparatorTempoDiInizio cs = new ComparatorTempoDiInizio();
	   Collections.sort(puntiDiInizioFineRettangoli, cs);
	   
	   return p;
   }
   
   
   //serve per l'algoritmo, crea una nuova soluzione sposyando a caso gli estremi dei rettangoli
   public Soluzione generaNuovaSoluzioneCasuale(int passoGenerazione) throws RequestImpossibleException {
		Random rand = new Random();
		ArrayList<Rettangolo> new_list = new ArrayList<>();
		
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).randomGeneration(rand, passoGenerazione);
		   new_list.add(new_rect);
	   }
	   
	   Soluzione new_solution = new Soluzione(new_list);
	   return new_solution;
   }
   
   
   //serve per il preprocessing: crea una nuova soluzione traslando a caso alcuni rettangoli
   public Soluzione generaNuovaSoluzioneCasualeTraslazione(int passoTraslazione) throws RequestImpossibleException {
		Random rand = new Random();
		ArrayList<Rettangolo> new_list = new ArrayList<>();
		
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).randomGenerationForTranslationProblem(rand, passoTraslazione);
		   new_list.add(new_rect);
	   }
	   
	   Soluzione new_solution = new Soluzione(new_list);
	   return new_solution;
   }
        
   
   //serve per il preprocessing: crea una nuova configurazione iniziale mettendo i rect a caso nel tempo
   public Soluzione generaNuovaSituazioneDiPartenza() throws RequestImpossibleException {
		Random rand = new Random();
		ArrayList<Rettangolo> new_list = new ArrayList<>();
		
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).generaNuovoRectDiBaseMassima(rand);
		   new_list.add(new_rect);
	   }
	   
	   Soluzione new_solution = new Soluzione(new_list);
	   return new_solution;
	}
   
	
   public Soluzione clone() {
	   ArrayList<Rettangolo> new_list = new ArrayList<>();
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).clone();
		   new_list.add(new_rect);
	   }
	   Soluzione new_solution = new Soluzione(new_list);
	   return new_solution;
   }
   
   
   public void printSoluzione() {
	   for(int i = 0; i < rettangoli.size(); i++) {
		   System.out.println(rettangoli.get(i).toString());
	   }
   }

   
    
}

