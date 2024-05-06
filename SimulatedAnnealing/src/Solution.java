import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Solution {

    ArrayList<Rettangolo> rettangoli;
    
    //double cost;
    
    public Solution(ArrayList<Rettangolo> rettangoli) {
    	this.rettangoli = rettangoli;
    }
    
   public double cost_of_solution() {
	   double maxH = 0;
	   
	   for(int i = 0; i < rettangoli.size(); i++) {
		   
		   Rettangolo current_rect = rettangoli.get(i);
		   double  somma_altezze = current_rect.altezza;
		   		   
		   ArrayList<Rettangolo> rect_intersecanti = new ArrayList<>();
		   int j = i + 1; 
		   
		   while(j < rettangoli.size() && current_rect.margine_destro > rettangoli.get(j).margine_sinistro) { 
			   rect_intersecanti.add(rettangoli.get(j));
			   
			   somma_altezze = max_altezza_cluster(rect_intersecanti, current_rect.altezza);
			   
			   j++;
		   }
			   
		   if(somma_altezze > maxH) 
			   maxH = somma_altezze;
		   
	   }

    	return maxH;	
    }
   
   
   private double max_altezza_cluster(ArrayList<Rettangolo> rect_intersecanti, double altezza_current_rect) {
	   
	   double max_somma = altezza_current_rect;
	   
	   for(int i = 0; i < rect_intersecanti.size(); i++) {
		   
		   double somma_parziale = rect_intersecanti.get(i).altezza + altezza_current_rect;
		  
		   int j = i + 1;
		   while(j < rect_intersecanti.size() && rect_intersecanti.get(i).margine_destro > rect_intersecanti.get(j).margine_sinistro) { 
			   somma_parziale += rect_intersecanti.get(j).altezza;
			   j++;
		   }
		   
		   if(somma_parziale > max_somma) {
			   max_somma = somma_parziale;
		   }
		   
	   }
	   return max_somma;
   }

   
   public Solution generate_new_random_solution() {
		Random rand = new Random();
		ArrayList<Rettangolo> new_list = new ArrayList<>();
		
	   for(int i = 0; i < rettangoli.size(); i++) {
		   Rettangolo new_rect = rettangoli.get(i).random_generation(rand);
		   new_list.add(new_rect);
	   }
	   
	   Collections.sort(new_list);
	   Solution new_solution = new Solution(new_list);
	   return new_solution;
   }
   
   public void print_solution() {
	   for(int i = 0; i < rettangoli.size(); i++) {
		   System.out.println(rettangoli.get(i).toString());
	   }
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
    
}
