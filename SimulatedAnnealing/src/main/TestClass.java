package main;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import progetto.JSON;
import progetto.Plan;
import progetto.PlanImpossibleException;
import progetto.RequestImpossibleException;
import utils.CommandLineChecker;



public class TestClass {

	private static String usage = "\n ----how to use the library---- \n"
			+ "1) '-i' + ' input path' + (optional)( '-o' + ' output path' ) + (optional)( '-p' )\n"
			+ "2) if you want to use the library without the main, use the static method LibraryInterface.solveProblem(): \n"
			+ "it takes a string representing the list of requests and retutns a string representing the plan";
		
	
	public static void main(String[] args) throws RequestImpossibleException, JsonMappingException, JsonProcessingException, PlanImpossibleException {		
	    
	    CommandLineChecker commandLineChecker = new CommandLineChecker(args);
	    if ( !commandLineChecker.checkInput() ) {
	    	System.out.println(usage);
	    	return;
	    }
	    
	    System.out.println("solving problem...\n");
	    
	    try {
	    	String solutionFile = LibraryInterface.solveProblem( JSON.readInputFile(commandLineChecker.getInputPath()), commandLineChecker.isPrintSol() ); 
	    	JSON.saveSolution( commandLineChecker.getOutputPath(), JSON.stringToObj(solutionFile, Plan.class) );      
	    }
	    catch(PlanImpossibleException p) {
	    	System.err.println(p.getMessage());
	    	return;
	    }
	    catch(RequestImpossibleException r) {
	    	System.err.println(r.getMessage());
	    	return;
	    }
	    
	}
	
}




