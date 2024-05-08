import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Solution {

    ArrayList<Rettangolo> rettangoli;
    ArrayList<Punto> punti_di_inizio_fine;
    double massimo_sfasamento_consentito = 5;
    
    
   public Solution(ArrayList<Rettangolo> rettangoli) {
    	this.rettangoli = rettangoli;
    	punti_di_inizio_fine = new ArrayList<>();
    	
    	//sistema i punti di inizio e fine dei rettangoli
    	for(int i = 0; i < rettangoli.size(); i++) {
    		Rettangolo r = rettangoli.get(i);
    		punti_di_inizio_fine.add(new Punto(r, r.margine_sinistro, r.altezza, true));
    		punti_di_inizio_fine.add(new Punto(r, r.margine_destro, r.altezza, false));
    	}
    	Collections.sort(punti_di_inizio_fine);
    	
    }
   
   public double costOfSolution() {
	   double h = 0;
	   double maxH = 0;
	   for(int i = 0; i < punti_di_inizio_fine.size(); i++) {
		   
		   Punto p = punti_di_inizio_fine.get(i);
		   
		   if(p.punto_di_inizio)
			   h += p.altezza_rect;
		   
		   else 
			   h -= p.altezza_rect;
		   
		   if(h > maxH)
			   maxH = h;
		   
	   }
	   return maxH;
   }
   
   
   public Punto puntoCritico() {
	   double h = 0;
	   double maxH = 0;
	   Punto punto_critico = null;
	   for(int i = 0; i < punti_di_inizio_fine.size(); i++) {
		   
		   Punto p = punti_di_inizio_fine.get(i);
		   
		   if(p.punto_di_inizio)
			   h += p.altezza_rect;
		   
		   else 
			   h -= p.altezza_rect;
		   
		   if(h > maxH) {
			   maxH = h;
			   punto_critico = p;
		   }
		   
	   }
	   return punto_critico;
   }
   
   
   public double sfasamento() {
	   double h_fase_1 = 0;
	   double h_fase_2 = 0;
	   double sfasamento_massimo = 0;  //da mettere 
	   
	   for(int i = 0; i < punti_di_inizio_fine.size(); i++) {
		   
		   Punto p = punti_di_inizio_fine.get(i);
		   
		   if(p.punto_di_inizio) {
			   if(p.r.fase == 1) {
				   h_fase_1 += p.altezza_rect;
			   }
			   else if(p.r.fase == 2) {
				   h_fase_2 += p.altezza_rect;
			   }
		   }
			   
		   else 
			   if(p.r.fase == 1) {
				   h_fase_1 -= p.altezza_rect;
			   }
			   else if(p.r.fase == 2) {
				   h_fase_2 -= p.altezza_rect;
			   }
		   
		   System.out.println( Math.abs(h_fase_1 - h_fase_2));
		   double sfasamento = Math.abs(h_fase_1 - h_fase_2);
		   if(sfasamento > sfasamento_massimo) {
			   sfasamento_massimo = sfasamento;
		   }
		   
	   }
	   
	   if(sfasamento_massimo < massimo_sfasamento_consentito) {
		   return 0;
	   }
		   
	   return sfasamento_massimo;
   }

   
   public Punto puntoMassimoSfasamento() {
	   double h_fase_1 = 0;
	   double h_fase_2 = 0;
	   double massimo_sfasamento = 0;  //da mettere
	   Punto punto_max_sfasamento = null;
	   
	   for(int i = 0; i < punti_di_inizio_fine.size(); i++) {
		   
		   Punto p = punti_di_inizio_fine.get(i);
		   
		   if(p.punto_di_inizio) {
			   if(p.r.fase == 1) {
				   h_fase_1 += p.altezza_rect;
			   }
			   else if(p.r.fase == 2) {
				   h_fase_2 += p.altezza_rect;
			   }
		   }
			   
		   else 
			   if(p.r.fase == 1) {
				   h_fase_1 -= p.altezza_rect;
			   }
			   else if(p.r.fase == 2) {
				   h_fase_2 -= p.altezza_rect;
			   }
		   
		   double sfasamento = Math.abs(h_fase_1 - h_fase_2);
		   if(sfasamento > massimo_sfasamento) {
			   massimo_sfasamento = sfasamento;
			   if(massimo_sfasamento > massimo_sfasamento_consentito) {
				   punto_max_sfasamento = p;
			   }
		   }
		   
	   }
	   
	   return punto_max_sfasamento;
   }
   
   
   public Solution generateNewRandomSolution() throws RectImpossibleException {
		Random rand = new Random();
		ArrayList<Rettangolo> new_list = new ArrayList<>();
		
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).randomGeneration(rand);
		   new_list.add(new_rect);
	   }
	   
	   Collections.sort(new_list);
	   Solution new_solution = new Solution(new_list);
	   return new_solution;
   }
        
   
   public Solution clone() {
	   ArrayList<Rettangolo> new_list = new ArrayList<>();
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).clone();
		   new_list.add(new_rect);
	   }
	   Solution new_solution = new Solution(new_list);
	   return new_solution;
   }
   
   
   public void printSolution() {
	   for(int i = 0; i < rettangoli.size(); i++) {
		   System.out.println(rettangoli.get(i).toString());
	   }
   }
   
   
//   public double altroCosto() {
//	   //altezza massima trovata fino ad ora
//	   double maxH = 0;
//	   
//	   //scandisce tutti i rettangoli, ordinati per tempo di inizio
//	   for(int i = 0; i < rettangoli.size(); i++) {
//		   
//		   Rettangolo current_rect = rettangoli.get(i);
//		   //come minimo la somma delle altezze dei rect che si intersecano con quello attuale = altezza rect attuale
//		   double  somma_altezze = current_rect.altezza;
//		   		   
//		   ArrayList<Rettangolo> rect_intersecanti = new ArrayList<>();
//		   int j = i + 1; 
//		   //guarda tutti i rettangoli successivi, finchè sono intersecanti con quello attuale		   
//		   while(j < rettangoli.size() && current_rect.margine_destro > rettangoli.get(j).margine_sinistro) { 
//			   rect_intersecanti.add(rettangoli.get(j));
//			   //somma le altezze dei rect che si intersecano con quello attuale			   
//			   somma_altezze = maxAltezzaCluster(rect_intersecanti, current_rect.altezza); 			   
//			   j++;
//		   }
//		   		   			   
//		   if(somma_altezze > maxH) 
//			   maxH = somma_altezze;
//	   }
//    	return maxH;	
//    }
//   
//   
//   //simile al metodo visto sopra, solo che viene avolto solo sui rect che si intersecano con quello attuale
//   private double maxAltezzaCluster(ArrayList<Rettangolo> rect_intersecanti, double altezza_current_rect) {
//	   
//	   double max_somma = altezza_current_rect;
//	   
//	   for(int i = 0; i < rect_intersecanti.size(); i++) {
//		   		   
//		   double somma_parziale = altezza_current_rect + rect_intersecanti.get(i).altezza;
//		  
//		   int j = i + 1;
//		   while(j < rect_intersecanti.size() && rect_intersecanti.get(i).margine_destro > rect_intersecanti.get(j).margine_sinistro) { 
//			   somma_parziale += rect_intersecanti.get(j).altezza;
//			   j++;
//		   }
//		   
//		   if(somma_parziale > max_somma) {
//			   max_somma = somma_parziale;
//		   }
//		   
//	   }
//	   return max_somma;
//   }
    
}
