import java.util.ArrayList;
import java.util.Random;

public class Simulated_annealing_algorithm {

	
	public static void main(String[] args) {
		
		Random rand = new Random();
		
		//crea la prima soluzione, quella coi tect di base massima
        ArrayList<Rettangolo> rect = new ArrayList<>();
        rect.add(new Rettangolo(0, 3, 9, 10, 1));
        rect.add(new Rettangolo(2, 5, 3, 2, 1));
        rect.add(new Rettangolo(8, 12, 8, 10, 1));
        rect.add(new Rettangolo(10, 14, 4, 10, 1));
        rect.add(new Rettangolo(11, 15, 12, 10, 1));
        rect.add(new Rettangolo(14, 18, 8, 10, 1));

        
        Solution current_solution = new Solution(rect);
        double cost_current_solution = current_solution.cost_of_solution();
        
        System.out.println("costo attuale " + cost_current_solution);
        //current_solution.print_solution();
        
		//crea la variabile che salva la soluzione migliore, inizialmente è la soluzione iniziale  
		double best_h = cost_current_solution;
		Solution best_solution = current_solution.clone();
        
        double initial_temperature = 100000;
        double cooling_rate = 0.003;
        
        for(double t = initial_temperature; t > 1; t *= (1 - cooling_rate)){
        	
        	//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
        	Solution new_solution = current_solution.generate_new_random_solution();
        	
        	//se il costo della nuova soluzione è < costo vecchia soluzione, accetta la nuova soluzione e aggiorna la soluzione migliore, se serve
        	double cost_new_solution = new_solution.cost_of_solution();
        	if(cost_new_solution < cost_current_solution) {
        		current_solution = new_solution;
        		cost_current_solution = cost_new_solution;
        		
        		if(cost_current_solution < best_h) {
            		best_h = cost_current_solution;
            		best_solution = current_solution;
        		}
        		
        	}
        	//se il costo è invece maggiore, accetta la soluzione con un probabilià e ^ (costoVecchiaSol - costoNuovaSol)/temperatura)
        	else {
        		double probability = Math.exp((cost_current_solution - cost_new_solution)/t);
        		if(probability >= rand.nextDouble()) {
        			current_solution = new_solution;
            		cost_current_solution = cost_new_solution;
        		}
        	}
        	
        }//end for 
        
        best_solution.print_solution();
        System.out.println("costo finale " + best_h);

	}

	
}
