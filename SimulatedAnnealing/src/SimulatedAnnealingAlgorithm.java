import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;

public class SimulatedAnnealingAlgorithm {

	
	public static void main(String[] args) throws RectImpossibleException {
				
		//crea la prima soluzione, quella coi rect di base massima
        ArrayList<Rettangolo> rect = new ArrayList<>();        
        for(int i = 0; i < 50; i++) {
        	rect.add(GeneratoreRettangoliCasuali.generaRettangolo());        	
        }
        Collections.sort(rect);
        
        Solution initial_solution = new Solution(rect);    
        visualizzaSoluzione(initial_solution);
//        double cost_initial_solution = initial_solution.costOfSolution();  
//        initial_solution.printSolution();
//        System.out.println("costo iniziale " + cost_initial_solution);
//        
//        Solution best_global_solution = initial_solution.clone();
//        double best_global_solution_cost = cost_initial_solution;
//        System.out.println("/////////////////////////");
//                  
//        
//        Long inizio = System.currentTimeMillis();
//        //svolgo l'algoritmo n volte e prendo la soluzione migliore
//        for(int epoch = 0; epoch < 15; epoch++) {
//            
//            Solution current_solution_in_loop = simulatedAnnealing(initial_solution.clone(), best_global_solution_cost);
//            double cost_current_solution_in_loop = current_solution_in_loop.costOfSolution();
//            
//            if(cost_current_solution_in_loop < best_global_solution_cost) {
//            	best_global_solution = current_solution_in_loop;
//            	best_global_solution_cost = cost_current_solution_in_loop;
//            }
//        }
//        Long fine = System.currentTimeMillis();
//        
//        best_global_solution.printSolution();
//        System.out.println("costo finale " + best_global_solution_cost + " durata " + (fine-inizio));
//        
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
        	
        	//System.out.println(h_best_solution);
        	
        }//end for 
        
        return best_solution;
	}
	
	public static void visualizzaSoluzione(Solution soluzione) {
		JFrame frame = new JFrame("Disegno Rettangoli");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawRectangles(g);
            }

            private void drawRectangles(Graphics g) {
                g.setColor(Color.RED);
                g.fillRect(50, 50, 100, 100);

                g.setColor(Color.BLUE);
                g.fillRect(200, 50, 150, 100);

                g.setColor(Color.GREEN);
                g.fillRect(100, 200, 200, 150);
            }
        };

        frame.add(panel);
        frame.setVisible(true);
	}
	
}
