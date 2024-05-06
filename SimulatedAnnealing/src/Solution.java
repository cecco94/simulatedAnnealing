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
		   
		   if( maxH < rettangoli.get(i).altezza) 
			   maxH = rettangoli.get(i).altezza;
		   		   
		   int j = i + 1; 
		   double  hSum = rettangoli.get(i).altezza;
		   
		   while(j < rettangoli.size() && rettangoli.get(i).margine_destro > rettangoli.get(j).margine_sinistro) { 
			   hSum += rettangoli.get(j).altezza;
			   j++;
		   }
			   
		   if(hSum > maxH) 
			   maxH = hSum;
		   
	   }

    	return maxH;	
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
		   System.out.println();
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
