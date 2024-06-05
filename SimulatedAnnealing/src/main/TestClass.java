package main;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import progetto.JSON;
import progetto.Plan;
import progetto.PlanImpossibleException;
import progetto.RequestImpossibleException;
import utils.CommandLineChecker;



public class TestClass {

	private static String usage = "info su come usare la libreria, da fare";
		
	
	public static void main(String[] args) throws RequestImpossibleException, JsonMappingException, JsonProcessingException, PlanImpossibleException {		
	    
	    CommandLineChecker commandLineChecker = new CommandLineChecker(args);
	    if ( !commandLineChecker.checkInput() ) {
	    	System.out.println(usage);
	    	return;
	    }
	    
	    String solutionFile = LibraryInterface.solveProblem( JSON.readInputFile(commandLineChecker.getInputPath()), false );    
	    JSON.saveSolution( commandLineChecker.getOutputPath(), JSON.stringToObj(solutionFile, Plan.class) );    	    
	}
	
}




