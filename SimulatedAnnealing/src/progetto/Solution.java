package progetto;
import java.util.ArrayList;
import java.util.Collections;

import lombok.Data;
import utils.sorting.ComparatorStartTime;
import utils.sorting.PhaseDifferenceComparator;
import utils.sorting.SumHighComparator;

@Data
public class Solution {

    ArrayList<Rectangle> rectanglesList;
    ArrayList<Point> endStartPontsList;
    double maxDifferenceBetweenPhases = 4.2;
    double maxAvaiblePower = 7.4;
    
    
    //for the json library
    public Solution() {  }
    
    
    public Solution(ArrayList<Rectangle> rect, double maxSfas, double maxH) {
    	maxDifferenceBetweenPhases = maxSfas;
    	maxAvaiblePower = maxH;
    	
    	//sort the rectangles based on their starting point
    	rectanglesList = rect;
  	    Collections.sort(rectanglesList);
 	    
    	//handle the start/end points of the rectangles 
    	endStartPontsList = new ArrayList<>();
    	for( int i = 0; i < rectanglesList.size(); i++ ) {
    		Rectangle r = rectanglesList.get(i);
    		endStartPontsList.add(new Point(r, r.startMinute, r.height, true));
    		endStartPontsList.add(new Point(r, r.endMinute, r.height, false));
    	}
    	ComparatorStartTime c = new ComparatorStartTime();
    	Collections.sort(endStartPontsList, c);	    	
    }
   
   
   public double cost() {
	   return maxHigh() + 100*differenceOfPhases();
   }
   
   
   public double maxHigh() {
	   double h = 0;
	   double maxH = 0;
	   for( int i = 0; i < endStartPontsList.size(); i++ ) {
		   
		   Point p = endStartPontsList.get(i);
		   
		   if( p.startPoint ) {
			   h += p.rectHeight;
			   p.sumHeightInThisPoint = h;
		   }		   
		   else {
			   h -= p.rectHeight;
			   p.sumHeightInThisPoint = h;
		   }
		   if( h > maxH )
			   maxH = h;
	   }
	   return maxH;
   }
   
   
   public double differenceOfPhases() {
	   double h_fase_1 = 0;
	   double h_fase_2 = 0;
	   double h_fase_3 = 0;
	   double maxPhaseDifference = 0; 
	   
	   for( int i = 0; i < endStartPontsList.size(); i++ ) {
		   
		   Point p = endStartPontsList.get(i);
		   
		   if( p.startPoint ) {
			   if( p.r.phase == 1 ) {
				   h_fase_1 += p.rectHeight;
			   }
			   else if( p.r.phase == 2 ) {
				   h_fase_2 += p.rectHeight;
			   }
			   else if( p.r.phase == 3 ) {
				   h_fase_3 += p.rectHeight;
			   }
			   else if( p.r.phase == 0 ) {
				   h_fase_1 += p.rectHeight/3;
				   h_fase_2 += p.rectHeight/3;
				   h_fase_3 += p.rectHeight/3;
			   }
		   }
			   
		   else {
			   if( p.r.phase == 1 ) {
				   h_fase_1 -= p.rectHeight;
			   }
			   else if( p.r.phase == 2 ) {
				   h_fase_2 -= p.rectHeight;
			   }
			   else if( p.r.phase == 3 ) {
				   h_fase_3 -= p.rectHeight;
			   }
			   else if( p.r.phase == 0 ) {
				   h_fase_1 -= p.rectHeight/3;
				   h_fase_2 -= p.rectHeight/3;
				   h_fase_3 -= p.rectHeight/3;
			   }
		   }
		   
		   double faseDifference_1_2 = Math.abs(h_fase_1 - h_fase_2);
		   double faseDifference_1_3 = Math.abs(h_fase_1 - h_fase_3);
		   double faseDifference_2_3 = Math.abs(h_fase_2 - h_fase_3);

		   double totalPhaseDifferenceInThisPoint = Math.max( faseDifference_1_2, Math.max(faseDifference_1_3, faseDifference_2_3) ); 
		   p.phaseDifferenceInThisPoint = totalPhaseDifferenceInThisPoint;
		   
		   if( totalPhaseDifferenceInThisPoint > maxPhaseDifference ) {
			   maxPhaseDifference = totalPhaseDifferenceInThisPoint;
		   }
	   }//end for
	   
	   if( maxPhaseDifference < maxDifferenceBetweenPhases ) {
		   return 0;
	   }
		   
	   return maxPhaseDifference;
   }
   
   
   public int countIntersections() {
	   int intersections = 0;
	   //takes all the starting points
	   for( int i = 0; i < endStartPontsList.size(); i++ ) {
		   Point p = endStartPontsList.get(i);
		   if( p.startPoint ) {
			  int endMin = p.r.endMinute;
			  
			  //takes all the following strating point in the length of the rectangle
			  for( int j = i+1; j < endStartPontsList.size(); j++ ) {
				  Point p2 = endStartPontsList.get(j);
				  if( p2.getMinute() > endMin ) 
					  break;
				  //each time it founds a starting point, increases the intersection count
				  if( p2.startPoint ) 
					  intersections++;
			  }
		   }		   
	   }
	   return intersections;
   }

   
   public double averageRectSqueezefication() {
	   double averageRectSqueezefication = 0;
	   for( Rectangle r : rectanglesList ) {
		   averageRectSqueezefication += (r.maxBase - r.base);
	   }
	   averageRectSqueezefication /= rectanglesList.size();
	   return averageRectSqueezefication;
   }
   
   
   public Point maxPowerRequestPoint() {
	   SumHighComparator cc = new SumHighComparator();
	   Collections.sort(endStartPontsList, cc);
	   
	   Point p = endStartPontsList.get(0);
	   
	   ComparatorStartTime cs = new ComparatorStartTime();
	   Collections.sort(endStartPontsList, cs);
	   
	   return p;
   }
   
  
   public Point maxPhaseDifferencePoint() {
	   PhaseDifferenceComparator c = new PhaseDifferenceComparator();
	   Collections.sort(endStartPontsList, c);
	   
	   Point p = endStartPontsList.get(0);
	   
	   ComparatorStartTime cs = new ComparatorStartTime();
	   Collections.sort(endStartPontsList, cs);
	   
	   return p;
   }
   
   
   //generaates a new solution with little randomic variations of the rects in the current solution
   public Solution generateNewRandomSolution(int varianceOfRanomicGeneration) throws RequestImpossibleException {
		ArrayList<Rectangle> new_list = new ArrayList<>();
		
	   for( int i = 0; i < rectanglesList.size(); i++ ) {
		   Rectangle new_rect = rectanglesList.get(i).randomGeneration(varianceOfRanomicGeneration);
		   new_list.add(new_rect);
	   }
	   
	   Solution new_solution = new Solution(new_list, maxDifferenceBetweenPhases, maxAvaiblePower);
	   return new_solution;
   }
   
   
   //for the SA algorithm in the preprocessing. to distribute the rectangles in the timeline
   public Solution generateNewSolutionOnlyTranslation(int varianceOfRanomicGeneration) throws RequestImpossibleException {
		ArrayList<Rectangle> new_list = new ArrayList<>();
		
	   for( int i = 0; i < rectanglesList.size(); i++ ) {
		   Rectangle new_rect = rectanglesList.get(i).randomGenerationForTranslationProblem(varianceOfRanomicGeneration);
		   new_list.add(new_rect);
	   }
	   
	   Solution new_solution = new Solution(new_list, maxDifferenceBetweenPhases, maxAvaiblePower);
	   return new_solution;
   }
        
   
   //for the preprocessing, creates a new random initial configuration
   public Solution generateNewInitialSolution() throws RequestImpossibleException {
	   ArrayList<Rectangle> new_list = new ArrayList<>();
		
	   for( int i = 0; i < rectanglesList.size(); i++ ) {
		   Rectangle new_rect = rectanglesList.get(i).generateNewRectWithMaxBaseLenght();
		   new_list.add(new_rect);
	   }
	   
	   Solution new_solution = new Solution(new_list, maxDifferenceBetweenPhases, maxAvaiblePower);
	   return new_solution;
	}
   
	
   public Solution clone() {
	   ArrayList<Rectangle> newList = new ArrayList<>();
	   for( int i = 0; i < rectanglesList.size(); i++ ) {
		   Rectangle newRect = rectanglesList.get(i).clone();
		   newList.add(newRect);
	   }
	   Solution new_solution = new Solution(newList, maxDifferenceBetweenPhases, maxAvaiblePower);
	   return new_solution;
   }
   
   
   public void printSolution() {
	   for( int i = 0; i < rectanglesList.size(); i++ ) {
		   System.out.println(rectanglesList.get(i).toString());
	   }
   }


	public Plan createPlan() {
		ArrayList<Request> richieste = new ArrayList<>();
		for( int i = 0; i < rectanglesList.size(); i++ ) {
			Rectangle r = rectanglesList.get(i);
			richieste.add(r.fromRectToRequest());
		}
		return new Plan(richieste, maxDifferenceBetweenPhases, maxAvaiblePower);
	}


	public static Solution convertPlanToSolution(Plan planRequest) throws RequestImpossibleException {		
		ArrayList<Rectangle> rectList = new ArrayList<>();
		double maxPhaseDiff = planRequest.getMaxdifferenceBetweenPhases();
		double maxPower = planRequest.getMaxNetPower();
		
		//for each request in the instance, creates a rectangle with feasible dimensions
		for(Request request : planRequest.getRequestList()) {
			Rectangle rect = new Rectangle(request.vehicleId, 
											 request.fase, 
											 request.startMinute, 
											 request.endMinute, 
											 request.energy, 
											 request.maxVehiclePower, 
											 request.minVehiclePower);
			rectList.add(rect);
		}
		
		Solution sol = new Solution(rectList, maxPhaseDiff, maxPower);
		return sol;
	}

   
    
}

