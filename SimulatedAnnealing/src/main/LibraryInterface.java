package main;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import progetto.JSON;
import progetto.Plan;
import progetto.PlanImpossibleException;
import progetto.RequestImpossibleException;
import progetto.SimulatedAnnealingAlgorithm;
import progetto.Solution;
import utils.visualization.DifferencePhasePanel;
import utils.visualization.HeightPanel;


public class LibraryInterface {
	
	public static int windowHeight = 600;
	public static int windowWidth = 600;
	public static int yWindowPosition = 0;
	public static int xWindowPosition = 0;

	
		
	public static String solveProblem(String problem, boolean showData) throws RequestImpossibleException, PlanImpossibleException, JsonMappingException, JsonProcessingException {		
		
		Plan initialPlan = JSON.stringToObj(problem, Plan.class);
		Solution initialSolution = Solution.convertPlanToSolution(initialPlan);
		
		if( showData ) {
			double initialSolutionCost = initialSolution.cost();
	        visualizeInitialData(initialSolution, initialSolutionCost); 
		} 
	
    	Solution bestSolution = SimulatedAnnealingAlgorithm.preprocessing(initialSolution.clone());
    	if( showData ) {
            double bestSolutionCost = bestSolution.cost();  
            yWindowPosition += 30;
            xWindowPosition += 30;
            visualizePreprocessData(bestSolution, bestSolutionCost);
    	}  
    	
        bestSolution = SimulatedAnnealingAlgorithm.simulatedAnnealing(bestSolution);
        if( showData ) {
            double bestSolutionCost = bestSolution.cost();
            yWindowPosition += 30;
            xWindowPosition += 30;
            visualizeFinalData(bestSolution, bestSolutionCost);
        }

        Plan solution = checkSolution(bestSolution);		
		return JSON.objToString(solution);
	}
	
	
	private static Plan checkSolution(Solution bestSolution) throws PlanImpossibleException {		
		if( bestSolution.differenceOfPhases() >  bestSolution.getMaxDifferenceBetweenPhases() ) {
        	throw new PlanImpossibleException("impossible plan, too much difference between phases in the point: " + bestSolution.maxPhaseDifferencePoint().toString());
        }     
        if( bestSolution.cost() > bestSolution.getMaxAvaiblePower() ) {
        	throw new PlanImpossibleException("too much power needed in the point: " + bestSolution.maxPowerRequestPoint().toString());
        }     
       Plan sol = bestSolution.createPlan();
       return sol;
	}
	
	
	
	////////////////////////// SECUNDARY METHODS ///////////////////////////
	private static void visualizeInitialData(Solution initialSolution, double initialSolutionCost) {
		visualizeHight(initialSolution, "1: INITIAL POWER REQUEST");
        visualizePhaseDifference(initialSolution, "1: INITIAL PHASE DIFFERENCE");
        System.out.println("INITIAL SOLUTION");
        initialSolution.printSolution();
        System.out.println();
        System.out.println("intersections " + initialSolution.countIntersections());
        System.out.println("average squeezefication " + initialSolution.averageRectSqueezefication());
        System.out.println("initial cost " + initialSolutionCost);
        System.out.println("initial max hight " + initialSolution.maxHigh());
        System.out.println("initial max phase diff " + initialSolution.differenceOfPhases());
        System.out.println(" \n" + "/////////////////////////" + "\n");
		
	}
	
	
	private static void visualizePreprocessData(Solution bestSolution, double bestSolutionCost) {
		visualizeHight(bestSolution, "2: POWER REQUEST AFTER TRANSLATION");
        visualizePhaseDifference(bestSolution, "2: PHASE DIFFERENCE AFTER TRANSATION");
        System.out.println("BEFORE PREPROCESSING");
        bestSolution.printSolution();
        System.out.println();
        System.out.println("intersections " + bestSolution.countIntersections());
        System.out.println("average squeezefication " + bestSolution.averageRectSqueezefication());
        System.out.println("cost after translation " + bestSolutionCost);
        System.out.println("max hight after translation " + bestSolution.maxHigh());
        System.out.println("max phase diff after translation " + bestSolution.differenceOfPhases());
        System.out.println(" \n" + "/+++++++++++++++++++++++++++" + "\n");
	}
	
	
	private static void visualizeFinalData(Solution bestSolution, double bestSolutionCost) {
		visualizeHight(bestSolution, "3: FINAL POWER REQUEST");
        visualizePhaseDifference(bestSolution, "3: FINAL PHASE DIFFERENCE");
        System.out.println("BEST SOLUTION FOUND");
        bestSolution.printSolution();
        System.out.println();
        System.out.println("intersections " + bestSolution.countIntersections());
        System.out.println("average squeezefication " + bestSolution.averageRectSqueezefication());
        System.out.println("final cost " + bestSolutionCost);
        System.out.println("final max hight " + bestSolution.maxHigh());
        System.out.println("final max phase diff " + bestSolution.differenceOfPhases());
        System.out.println("\n");
		
	}
	
	
	public static void visualizeHight(Solution solution, String title) {
		JFrame frame = new JFrame(title);
		frame.setLocation(xWindowPosition, yWindowPosition);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        HeightPanel solPan = new HeightPanel(solution);
        solPan.setPreferredSize(new Dimension(windowWidth, windowHeight));
        frame.add(solPan);
        frame.pack();
        frame.setVisible(true);
	}
	
	
	public static void visualizePhaseDifference(Solution solution, String title) {
		JFrame frame = new JFrame(title);
		frame.setLocation(xWindowPosition + windowWidth + 30, yWindowPosition);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DifferencePhasePanel solPan = new DifferencePhasePanel(solution);
        solPan.setPreferredSize(new Dimension(windowWidth, windowHeight));
        frame.add(solPan);
        frame.pack();
        frame.setVisible(true);
	}
	

}
