package progetto;
import java.awt.Dimension;

import javax.swing.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import utils.visualization.HeightPanel;
import utils.visualization.DifferencePhasePanel;



public class TestClass {

	public static int windowHeight = 600;
	public static int windowWidth = 600;
		
	
	public static void main(String[] args) throws RequestImpossibleException, JsonMappingException, JsonProcessingException, PlanImpossibleException {		
		
		String dataPath = args[0];
	    String solutionPath = args[1];
		
		Solution initialSolution = JSON.loadPlanRequest(dataPath);
		double initialSolutionCost = initialSolution.cost();
        visualizeInitialData(initialSolution, initialSolutionCost); 
        
    	Solution bestSolution = SimulatedAnnealingAlgorithm.preprocessing(initialSolution.clone());
        double bestSolutionCost = bestSolution.cost();    
        visualizePreprocessData(bestSolution, bestSolutionCost);
        
        bestSolution = SimulatedAnnealingAlgorithm.simulatedAnnealing(bestSolution);
        bestSolutionCost = bestSolution.cost();
        
        visualizeFinalData(bestSolution, bestSolutionCost);
        checkSolution(bestSolution, solutionPath);
        		
	}

	
	private static void checkSolution(Solution bestSolution, String solutionPath) throws PlanImpossibleException, JsonMappingException, JsonProcessingException {
		if(bestSolution.differenceOfPhases() >  bestSolution.maxDifferenceBetweenPhases) {
        	throw new PlanImpossibleException("impossible plan, too much difference between phases in the point: " + bestSolution.maxPhaseDifferencePoint().toString());
        }
        
        if( bestSolution.cost() >bestSolution.maxAvaiblePower ) {
        	throw new PlanImpossibleException("impossible plan, too much power needed in the point: " + bestSolution.maxPowerRequestPoint().toString());
        }     
       Plan sol = bestSolution.createPlan();
       JSON.saveSolution(solutionPath, "soluzione.json", sol);	
	}
	
	
	
	
	////////////////////////// SECUNDARY METHODS ///////////////////////////
	private static void visualizeInitialData(Solution initialSolution, double initialSolutionCost) {
		visualizeHight(initialSolution, "HIGTH BEFORE");
        visualizePhaseDifference(initialSolution, "PHASE DIFFERENCE BEFORE");
        initialSolution.printSolution();
        System.out.println("intersections " + initialSolution.countIntersections());
        System.out.println("average squeezefication " + initialSolution.averageRectSqueezefication());
        System.out.println("initial cost " + initialSolutionCost);
        System.out.println("initial max hight " + initialSolution.maxHigh());
        System.out.println("initial max phase diff " + initialSolution.differenceOfPhases());
        System.out.println(" \n" + "/////////////////////////" + "\n");
		
	}
	
	
	private static void visualizePreprocessData(Solution bestSolution, double bestSolutionCost) {
		visualizeHight(bestSolution, "HIGHT AFTER TRANSLATION");
        visualizePhaseDifference(bestSolution, "PHASE DIFFERENCE AFTER TRANSATION");
        bestSolution.printSolution();
        System.out.println("intersections " + bestSolution.countIntersections());
        System.out.println("average squeezefication " + bestSolution.averageRectSqueezefication());
        System.out.println("cost after translation " + bestSolutionCost);
        System.out.println("max hight after translation " + bestSolution.maxHigh());
        System.out.println("max phase diff after translation " + bestSolution.differenceOfPhases());
        System.out.println(" \n" + "/+++++++++++++++++++++++++++" + "\n");
	}
	
	
	private static void visualizeFinalData(Solution bestSolution, double bestSolutionCost) {
		visualizeHight(bestSolution, "ALTEZZE FINALI");
        visualizePhaseDifference(bestSolution, "SFASAMENTO FINALE");
        bestSolution.printSolution();
        System.out.println("intersections " + bestSolution.countIntersections());
        System.out.println("average squeezefication " + bestSolution.averageRectSqueezefication());
        System.out.println("final cost " + bestSolutionCost);
        System.out.println("final max hight " + bestSolution.maxHigh());
        System.out.println("final max phase diff " + bestSolution.differenceOfPhases());
		
	}
	
	
	public static void visualizeHight(Solution solution, String title) {
		JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        HeightPanel solPan = new HeightPanel(solution);
        solPan.setPreferredSize(new Dimension(windowWidth, windowHeight));
        frame.add(solPan);
        frame.pack();
        frame.setVisible(true);
	}
	
	
	public static void visualizePhaseDifference(Solution solution, String title) {
		JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DifferencePhasePanel solPan = new DifferencePhasePanel(solution);
        solPan.setPreferredSize(new Dimension(windowWidth, windowHeight));
        frame.add(solPan);
        frame.pack();
        frame.setVisible(true);
	}
	
}




