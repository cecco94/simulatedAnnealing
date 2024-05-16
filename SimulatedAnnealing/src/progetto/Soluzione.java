package progetto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import utils.RectImpossibleException;
import utils.RettangoloSemplificato;
import utils.SoluzioneSemplificata;
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
			   else if(p.r.fase == 4) {
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
   
   
   public Soluzione generaNuovaSoluzioneCasuale() throws RectImpossibleException {
		Random rand = new Random();
		ArrayList<Rettangolo> new_list = new ArrayList<>();
		
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).randomGeneration(rand);
		   new_list.add(new_rect);
	   }
	   
	   Collections.sort(new_list);
	   Soluzione new_solution = new Soluzione(new_list);
	   return new_solution;
   }
   
   
   public Soluzione generaNuovaSoluzioneCasualeTraslazione() throws RectImpossibleException {
		Random rand = new Random();
		ArrayList<Rettangolo> new_list = new ArrayList<>();
		
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).randomGenerationForTranslationProblem(rand);
		   new_list.add(new_rect);
	   }
	   
	   Collections.sort(new_list);
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
   
   
   public SoluzioneSemplificata creaSoluzioneDaMettereNelJson() {
		ArrayList<RettangoloSemplificato> rettangoliSoluzione = new ArrayList<>();
		for(int i = 0; i < rettangoli.size(); i++) {
			Rettangolo r = rettangoli.get(i);
			rettangoliSoluzione.add(new RettangoloSemplificato(r.identificativo, r.fase, r.area, r.margineSinistro, r.margineDestro));
		}
	   return new SoluzioneSemplificata(rettangoliSoluzione, sfasamento(), altezzaMassima(), costoSoluzione());
   }
   
    
}

