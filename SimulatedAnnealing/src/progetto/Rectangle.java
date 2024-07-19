package progetto;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Data;

@Data
public class Rectangle implements Comparable<Rectangle>{
	
	int idNumber;
	int phase;
	
	//max power = min(plug power, net power)
	double height, maxHeight;
	//min power recieved by the vehicle
	double minHeight;
	
	double area;
	int base;

	//start time found in the plan
	int minStartMinute;
	int maxStopMinute;
	//the two numbers we want to find
	int startMinute, endMinute;
	
	int maxBase, minBase;

	
	//for the json library
	public Rectangle() {}
	
	
	//constructor used when we read the json file, creates rectangles with base = max base
	public Rectangle(int id, int f, int min_start_t, int max_stop_t, double a, double max_h, double min_h) throws RequestImpossibleException {
		if( max_stop_t <= min_start_t ) {
			throw new RequestImpossibleException("the stop minute is equal or less than the start minute ");
		}
		
		idNumber = id;
		phase = f;
		
		area = a;
		
		maxHeight = max_h;
		minHeight = min_h;
		
		minStartMinute = min_start_t;
		maxStopMinute = max_stop_t;
		
		startMinute = minStartMinute;
		endMinute = maxStopMinute;
		
		minBase = (int)( Math.ceil(area*60/maxHeight) );		
		//the avaible charge time may be smaller than the max charging time with h = minH
		//the max charging time with h = minH may be smaller than the avaible charge time
		maxBase = Math.min((int)(area*60/minHeight), maxStopMinute - minStartMinute);		
		base = maxStopMinute - minStartMinute;		
		//if the rect base is too small
		if( base < minBase ) {
			throw new RequestImpossibleException("not enough time for charging the vehicle, id " + id+ ", energy " + a + ", given time " + base + ", power " + (area/base));
		}		
		//if the rect base is too large, we sqeeze the rect
		if( base > maxBase ) {
			base = maxBase;
			startMinute = minStartMinute;
			endMinute = startMinute + maxBase;
		}
		
		height = area*60/base;
	}	
	
	
	//rect generated during the random generation
	public Rectangle(int id, int fase, int min_start_m, int max_stop_m, int start, int stop, double a, double max_h, double min_h, int bmin, int bmax) throws RequestImpossibleException {
				
		if( stop <= start ) {
			throw new RequestImpossibleException("the stop minute is equal or less than the start minute");
		}
		
		idNumber = id;
		this.phase = fase;

		minStartMinute = min_start_m;
		maxStopMinute = max_stop_m;
		
		startMinute = start;
		endMinute = stop;
		
		minBase = bmin;
		maxBase = bmax;
		
		maxHeight = max_h;
		minHeight = min_h;
		
		if( endMinute > maxStopMinute || startMinute < minStartMinute ) {
			throw new RequestImpossibleException("the rect is out of given time, id " + id + ",  start minute " + startMinute + ",  end minute " + endMinute + 
												",  min start minute " + minStartMinute +  ",  max stop minute " + maxStopMinute);
		}
		area = a;		
		base = endMinute - startMinute;
				
		////if the rect base is too large, we sqeeze the rect
		if( base > maxBase ) {
			endMinute = minStartMinute + maxBase;
			base = endMinute - startMinute;
		}

		height = area*60/base;
		//fi the rect is too low, we change the end minute 
		if( height < minHeight ) {
			endMinute = maxStopMinute;
			startMinute = maxStopMinute - maxBase;
			base = maxBase;
			height = area*60/maxBase;
		}
		
	}

	
	//generates a new rect with a random variation of the starting/ending poind
	public Rectangle randomGeneration(int varianceOfRanomicGeneration) throws RequestImpossibleException {
		int newStartTime, newStopTime;
		
		//first the start time
		newStartTime = startMinute + ThreadLocalRandom.current().nextInt(-varianceOfRanomicGeneration, 1 + varianceOfRanomicGeneration);	
		//handle inconsistence
		if( newStartTime < minStartMinute ) {
			newStartTime = minStartMinute;
		}
		if( newStartTime > maxStopMinute - minBase ) { 
			newStartTime = maxStopMinute - minBase;
		}
					
		//then the stop time
		newStopTime = endMinute + ThreadLocalRandom.current().nextInt(-varianceOfRanomicGeneration, 1 + varianceOfRanomicGeneration);	
		//handle inconsistence
		if( newStopTime - newStartTime < minBase ) {
			newStopTime = newStartTime + minBase;
		}
		if( newStopTime - newStartTime > maxBase ) { 
			newStopTime = maxStopMinute; 
		}
		if( newStopTime > maxStopMinute ) {
			newStopTime = maxStopMinute;
		}
		
		return new Rectangle(idNumber, phase, minStartMinute, maxStopMinute, newStartTime, newStopTime, area, maxHeight, minHeight, minBase, maxBase);
	}

	
	//shift the rectange with base = maxBase
	public Rectangle randomGenerationForTranslationProblem(int varianceOfRanomicGeneration) throws RequestImpossibleException {
		int newStartTime = startMinute;
		int newStopTime = endMinute;
				
		newStartTime += ThreadLocalRandom.current().nextInt(-varianceOfRanomicGeneration, 1 + varianceOfRanomicGeneration);
		if( newStartTime < minStartMinute ) {
			newStartTime = minStartMinute;
		}
		if( newStartTime > maxStopMinute - maxBase ) { 
			newStartTime = maxStopMinute - maxBase;
		}
		newStopTime = newStartTime + maxBase;
		
		Rectangle newRect = new Rectangle(idNumber, phase, minStartMinute, maxStopMinute, newStartTime, newStopTime, area, maxHeight, minHeight, minBase, maxBase);			
		return newRect;
	}
	
	
	//generates randomly a new rect with base = maxBase in a feasible point in the timeline 
	public Rectangle generateNewRectWithMaxBaseLenght() throws RequestImpossibleException {
		//we can move the rect only if maxBase < avaible time given by the plan
		if( maxStopMinute - minStartMinute > maxBase ) {
			int newSTartMinute = startMinute;
			int newStopMinute = endMinute;
			newSTartMinute = minStartMinute + ThreadLocalRandom.current().nextInt(maxStopMinute - (minStartMinute + maxBase));
			newStopMinute = newSTartMinute + maxBase;
			
			Rectangle newRect = new Rectangle(idNumber, phase, minStartMinute, maxStopMinute, newSTartMinute, newStopMinute, 
													area, maxHeight, minHeight, minBase, maxBase);	
			return newRect;
		}
		//else return the old rectangle
		return this.clone();
	}
	
	
	@Override
	public int compareTo(Rectangle r) {
		if( startMinute < r.startMinute ) {
			return -1;
		}
		if( startMinute > r.startMinute ) {
			return 1;
		}
		//first the rect with less avaible time
		if( startMinute == r.startMinute ) {
			if(maxStopMinute < r.maxStopMinute) {
				return -1;
			}
		}
		return 0;
	}
	
	
	public Rectangle clone() {
		Rectangle newRect = null;
		try {
			newRect = new Rectangle(idNumber, phase, minStartMinute, maxStopMinute, startMinute, endMinute, area, 
									maxHeight, minHeight, minBase, maxBase);
		} 
		catch (RequestImpossibleException e) {
			e.printStackTrace();
		}
		return newRect;
	}
	
	
	public String toString() {
		return "id " + idNumber + ",  phase " + phase + ",  area " + area + ",  height " + height + ",  start " + startMinute + ",  stop " + endMinute + 
				",  min_start_minute " + minStartMinute + ",  max_stop_minute " + maxStopMinute + ",  max_base " + maxBase + ",  base" + base;
	}
	
	
	public Request fromRectToRequest() {
		Request r = new Request(idNumber, phase, area, startMinute, endMinute, maxHeight, minBase);
		return r;
	}


	
}

