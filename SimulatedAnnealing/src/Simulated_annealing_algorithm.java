import java.util.ArrayList;
import java.util.Random;

public class Simulated_annealing_algorithm {

	
	public static void main(String[] args) {
				
		//crea la prima soluzione, quella coi tect di base massima
        ArrayList<Rettangolo> rect = new ArrayList<>();
        rect.add(new Rettangolo(0, 35, 70, 10, 1));
        rect.add(new Rettangolo(10, 20, 30, 10, 1));
        rect.add(new Rettangolo(30, 40, 40, 10, 1));
        rect.add(new Rettangolo(50, 65, 15, 10, 1));
        rect.add(new Rettangolo(60, 75, 45, 10, 1));
        rect.add(new Rettangolo(70, 90, 40, 10, 1));

        
        Solution initial_solution = new Solution(rect);
        double cost_initial_solution = initial_solution.cost_of_solution();   
        System.out.println("costo iniziale " + cost_initial_solution);

        Solution best_global_solution = initial_solution.clone();
        double best_global_solution_cost = cost_initial_solution;
        System.out.println("/////////////////////////");
          
        
        //svolgo l'algoritmo n volte e prendo la soluzione venuta meglio
        for(int epoch = 0; epoch < 5; epoch++) {
            
            Solution current_solution_in_loop = simulated_annealing(initial_solution.clone(), cost_initial_solution);
            double cost_current_solution_in_loop = current_solution_in_loop.cost_of_solution();
            
            if(cost_current_solution_in_loop < best_global_solution_cost) {
            	best_global_solution = current_solution_in_loop;
            	best_global_solution_cost = cost_current_solution_in_loop;
            }
        }
        
        best_global_solution.print_solution();
        System.out.println("costo finale " + best_global_solution_cost);

	}
	
	public static Solution simulated_annealing(Solution current_solution, double cost_current_solution) {
        
		//inizialmente la soluzione migliore è quella iniziale
		Solution best_solution = current_solution.clone();
		double h_best_solution = cost_current_solution;
		
		
        double initial_temperature = 1000000000;
        double cooling_rate = 0.001;
        Random rand = new Random();
        
        for(double t = initial_temperature; t > 1; t *= (1 - cooling_rate)){
        	
        	//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
        	Solution new_solution = current_solution.generate_new_random_solution();
        	
        	//se il costo della nuova soluzione è < costo vecchia soluzione, accetta la nuova soluzione e aggiorna la soluzione migliore, se serve
        	double cost_new_solution = new_solution.cost_of_solution();
        	if(cost_new_solution < cost_current_solution) {
        		current_solution = new_solution;
        		cost_current_solution = cost_new_solution;
        		
        		if(cost_current_solution < h_best_solution) {
            		h_best_solution = cost_current_solution;
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
        
        return best_solution;
//        best_solution.print_solution();
//        System.out.println("costo finale " + h_best_solution);
	}

	
}
