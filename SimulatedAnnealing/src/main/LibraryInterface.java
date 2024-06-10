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

//la classe che prende un file json con il problema e restituisce un file json con la soluzione
//per usare la libreria senza dover richiamare il metodo main
public class LibraryInterface {
	
	public static int windowHeight = 600;
	public static int windowWidth = 600;
	
		
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
            visualizePreprocessData(bestSolution, bestSolutionCost);
    	}  
    	
        bestSolution = SimulatedAnnealingAlgorithm.simulatedAnnealing(bestSolution);
        if( showData ) {
            double bestSolutionCost = bestSolution.cost();
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
        	throw new PlanImpossibleException("impossible plan, too much power needed in the point: " + bestSolution.maxPowerRequestPoint().toString());
        }     
       Plan sol = bestSolution.createPlan();
       return sol;
	}
	
	
	
	////////////////////////// SECUNDARY METHODS ///////////////////////////
	private static void visualizeInitialData(Solution initialSolution, double initialSolutionCost) {
		visualizeHight(initialSolution, "POWER REQUEST BEFORE");
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
		visualizeHight(bestSolution, "POWER REQUEST AFTER TRANSLATION");
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
		visualizeHight(bestSolution, "FINAL POWER REQUEST");
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
