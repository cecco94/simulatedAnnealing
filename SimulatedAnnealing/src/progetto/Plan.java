package progetto;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Plan {
    ArrayList<Request> requestList;
    double maxdifferenceBetweenPhases = 4.2;
    double maxNetPower = 7.4;
    
    
    //for json library
    public Plan() {}
    
    
    public Plan(ArrayList<Request> r, double sfasMax, double hMax) {
    	requestList = r;
    	maxdifferenceBetweenPhases = sfasMax;
        maxNetPower = hMax;
    }
    
}
