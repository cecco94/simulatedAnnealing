package progetto;

import java.util.concurrent.ThreadLocalRandom;


public class SimulatedAnnealingAlgorithm {
	
	//randomly generates 10 initial rect distribution, then takes the best and uses the SA algortithm to obtan a better distribution
	public static Solution preprocessing(Solution initialSolution) throws RequestImpossibleException {
		Solution bestInitialDistribution = initialSolution;
		double bestDistributionCost = initialSolution.cost();
		
		for( int iteraz = 1; iteraz < 10; iteraz++ ) {
			Solution newInitialDistribution = initialSolution.generateNewInitialSolution();
			double newInitialDistributionCost = newInitialDistribution.cost();
			
			if( trunc(bestDistributionCost) > trunc(newInitialDistributionCost) ) {
				bestInitialDistribution = newInitialDistribution;
				bestDistributionCost = newInitialDistributionCost;
			}
			//if they have the same cost, it takes the distribution with less intersections
			else if( trunc(bestDistributionCost) == trunc(newInitialDistributionCost) ) {
				if( bestInitialDistribution.countIntersections() > newInitialDistribution.countIntersections() ) {
					bestInitialDistribution = newInitialDistribution;
					bestDistributionCost = newInitialDistributionCost;
				}
			}
		}		
		return preProcessAnnealing(bestInitialDistribution);
	}
	
	
	//uses a quick version of the SA algorithm to shift the rectangles in the timeline
	public static Solution preProcessAnnealing(Solution currentSolution) throws RequestImpossibleException {
		double currentSolutionCost = trunc(currentSolution.cost());
		Solution bestSolution = currentSolution.clone();
		double bestSolutionCost = currentSolutionCost;
		
        double initialTemperature = 1000, finalTemperature = 0.1, temperature = initialTemperature;
        double coolingRate = 0.9;       
    	int varianceOfRanomicGeneration = 20;

        while( temperature > finalTemperature ) {
        	for(int i = 0; i < 800; ++i) {
        		//generaates a new solution with little randomic variations of the rects in the current solution
            	Solution newSolution = currentSolution.generateNewSolutionOnlyTranslation(varianceOfRanomicGeneration);
        		double newSolutionCost = trunc(newSolution.cost());
        		
        		//if the new solution is better, always accepts the new solution 
        		if( newSolutionCost <= currentSolutionCost ) {
        			currentSolution = newSolution;
        			currentSolutionCost = newSolutionCost;
        			
        			//if the new solution is better than the actual best solution, change the best solution
            		if( newSolutionCost < bestSolutionCost ) {
            			bestSolutionCost = currentSolutionCost;
            			bestSolution = currentSolution;
            		}
        		}            	
        		//if the new solution is worse than the current solution, ccepts the new solution with 
        		//probability = exp(current solution cost - new solution cost)/temperature)
        		else {
        			double probability = Math.exp( (( currentSolutionCost - newSolutionCost )/temperature) );
        			if(probability >= ThreadLocalRandom.current().nextDouble()) {
        				currentSolution = newSolution;
        				currentSolutionCost = newSolutionCost;
        			}
        		}
        	}//end for
        	temperature *= coolingRate;
        }//end while
        return bestSolution;
	}

	
	public static Solution simulatedAnnealing(Solution currentSolution) throws RequestImpossibleException {
		//if the preprocessing has found a solution whitout intersections, there is no need of the SA algorithm
		if( currentSolution.countIntersections() == 0 ) {
			return currentSolution;
		}
		
		double currentSolutionCost = trunc(currentSolution.cost());
		Solution bestSolution = currentSolution.clone();		//in principio la soluzione migliore è quella iniziale
		double bestSolutionCost = currentSolutionCost;
		
        double initialTemperature = 1000, finalTemperature = 0.1, temperature = initialTemperature;
        double coolingRate = 0.99;
    	int varianceOfRanomicGeneration = 15;

        while( temperature > finalTemperature ) {
        	for( int i = 0; i < 1000; ++i ) {  		
        		Solution newSolution = currentSolution.generateNewRandomSolution(varianceOfRanomicGeneration);
        		double newSolutionCost = trunc(newSolution.cost());
        		
        		if( newSolutionCost < currentSolutionCost ) {
        			currentSolution = newSolution;
        			currentSolutionCost = newSolutionCost;
        			
            		if( newSolutionCost < bestSolutionCost ) {
            			bestSolutionCost = newSolutionCost;
            			bestSolution = newSolution;
            		}
            		else if ( newSolutionCost == bestSolutionCost ) {
            			double newSolutionSecondaryCost 	= trunc( newSolution.averageRectSqueezefication()  + newSolution.countIntersections() );
            			double bestSolutionSecondaryCost    = trunc( bestSolution.averageRectSqueezefication() + bestSolution.countIntersections() );
            			if ( newSolutionSecondaryCost < bestSolutionSecondaryCost ) {
            				bestSolution = newSolution;
            			}
            		}
        		} 
        		else if( newSolutionCost == currentSolutionCost ) {
        			double newSolutionSecondaryCost 	= trunc( newSolution.averageRectSqueezefication() + newSolution.countIntersections() );
        			double currentSolutionSecondaryCost = trunc( currentSolution.averageRectSqueezefication() + currentSolution.countIntersections() );
        			if ( newSolutionSecondaryCost < currentSolutionSecondaryCost ) {
        				currentSolution = newSolution;
        			}
        		}
        		else {
        			double probability = Math.exp( (( currentSolutionCost - newSolutionCost ) / temperature) );
        			if( probability >= ThreadLocalRandom.current().nextDouble() ) {
        				currentSolution = newSolution;
        				currentSolutionCost = newSolutionCost;
        			}
        		}	
        	}//end for
        	temperature *= coolingRate;
        }//end while   
        return bestSolution;
	}
	
	
	//returns the number with only 5 digits after the decimal poin
	private static double trunc(double number) {
		double precision = 100000.0;
		return Math.floor(number*precision)/precision;
	}
	
}
