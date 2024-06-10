package progetto;

import lombok.Data;

@Data
public class Request {

	int vehicleId;
	int phase;
	double energy, maxVehiclePower, minVehiclePower;
	
	//the minutes we want to find
	public int startMinute, endMinute;
	
	
	//for json library
	public Request() {}

	
	public Request(int id, int f, double e, int start, int stop, double kwMax, double kwMin) {
		vehicleId = id;
		phase = f;
		energy = e;
		startMinute = start;
		endMinute = stop;
		maxVehiclePower = kwMax;
		minVehiclePower = kwMin;
	}
	
}
