import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;

public class SimulatedAnnealingAlgorithm {

	
	public static void main(String[] args) throws RectImpossibleException {
				
		//crea istanza casuale del problema, con rettangoli di base massima
        ArrayList<Rettangolo> rect = new ArrayList<>(); 
        
        rect.add(new Rettangolo(0, 1, 10, 135, 905.0, 10, 1));
        rect.add(new Rettangolo(1, 2, 0, 157, 200.0, 10, 1));

//        rect.add(new Rettangolo(2, 2, 90, 130, 162.0, 10, 1));
//        rect.add(new Rettangolo(3, 2, 110, 200, 90.0, 10, 1));
//        rect.add(new Rettangolo(4, 1, 180, 240, 60.0, 10, 1));
//        rect.add(new Rettangolo(5, 2, 201, 272, 80.5, 10, 1));
//        rect.add(new Rettangolo(6, 1, 280, 340, 63.1, 10, 1));
//        rect.add(new Rettangolo(7, 1, 321, 399, 77.0, 10, 1));

        
        
//        for(int i = 0; i < 10; i++) {
//        	rect.add(GeneratoreRettangoliCasuali.generaRettangolo(i));        	
//        }
//        Collections.sort(rect);
        
        Solution initial_solution = new Solution(rect);    
        visualizzaSoluzione(initial_solution, "prima");
//        double cost_initial_solution = initial_solution.costOfSolution();  
        initial_solution.printSolution();
//        System.out.println("costo iniziale " + cost_initial_solution);
//        System.out.println("punto critico: ");
//        System.out.println(initial_solution.puntoCritico().toString());
        
        System.out.println("differenza massima tra le fasi " + initial_solution.sfasamento());
        System.out.println("punto massimo sfasamento: " + initial_solution.puntoMassimoSfasamento());
               
//        Solution best_global_solution = initial_solution.clone();
//        double best_global_solution_cost = cost_initial_solution;
//        System.out.println(" \n" + "/////////////////////////" + "\n");
//                   
//        //svolgo l'algoritmo n volte e prendo la soluzione migliore
//        for(int epoch = 0; epoch < 15; epoch++) {
//            
//            Solution current_solution_in_loop = simulatedAnnealing(initial_solution.clone(), cost_initial_solution);
//            double cost_current_solution_in_loop = current_solution_in_loop.costOfSolution();
//            
//            if(cost_current_solution_in_loop < best_global_solution_cost) {
//            	best_global_solution = current_solution_in_loop;
//            	best_global_solution_cost = cost_current_solution_in_loop;
//            }
//        }
//        
//        visualizzaSoluzione(best_global_solution, "dopo");
//        best_global_solution.printSolution();
//        System.out.println("costo finale " + best_global_solution_cost);
//        System.out.println("punto critico: ");
//        System.out.println(best_global_solution.puntoCritico().toString());
        
	}
	
	public static Solution simulatedAnnealing(Solution current_solution, double cost_current_solution) throws RectImpossibleException {
		//inizialmente la soluzione migliore è quella iniziale
		Solution best_solution = current_solution.clone();
		double h_best_solution = cost_current_solution;
		
		
        double initial_temperature = 100000000;
        double cooling_rate = 0.0005;
        Random rand = new Random();
        
        for(double t = initial_temperature; t > 1; t *= (1 - cooling_rate)){
        	
        	//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
        	Solution new_solution = current_solution.generateNewRandomSolution();
        	
        	//se il costo della nuova soluzione è < costo vecchia soluzione, accetta la nuova soluzione e aggiorna la soluzione migliore, se serve
        	double cost_new_solution = new_solution.costOfSolution();
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
	}
	
	public static void visualizzaSoluzione(Solution soluzione, String title) {
		JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SolutionPanel solPan = new SolutionPanel(soluzione);
        solPan.setPreferredSize(new Dimension(600, 400));
        frame.add(solPan);
        frame.pack();
        frame.setVisible(true);
	}
	
}
