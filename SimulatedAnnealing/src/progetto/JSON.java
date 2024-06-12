package progetto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class JSON {

	public static <T extends Object> T stringToObj(String input, Class<T> cls) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(input, cls);
	}
	
	
	public static String objToString(Object obj) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return  objectMapper.writeValueAsString(obj);
	}
	
	
	//loads the json file and transforms it into a string
	public static String readInputFile(String path) {
		String line = null;
		String data = "";
		try {
		      File myObj = new File(path);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        line = myReader.nextLine();
		        data = data + line;
		      }
		      myReader.close();
		    } 
		catch (FileNotFoundException e) {
		      System.err.println("An error occurred.");
		      e.printStackTrace();
		}
		return data;	
	}
	
	
	//loads the json file and transforms it into a plan
	public static Plan loadInputPlan(String path) throws JsonMappingException, JsonProcessingException {
		String line = null;
		String data = "";
		try {
		      File myObj = new File(path);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        line = myReader.nextLine();
		        data = data + line;
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.err.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		//takes the string and creates the plan request
		Plan planRequest = stringToObj(data, Plan.class);
		return planRequest;
	}	
	
	//loads the json file and transforms it into a solution
	public static Solution loadPlanRequest(String path) throws JsonMappingException, JsonProcessingException, RequestImpossibleException {
		//loads the json file and transforms it into a string
		String line = null;
		String data = "";
		try {
		      File myObj = new File(path);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        line = myReader.nextLine();
		        data = data + line;
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.err.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		//takes the string and creates the plan request
		Plan planRequest = stringToObj(data, Plan.class);
		ArrayList<Rectangle> rectList = new ArrayList<>();
		
		double maxPhaseDiff = planRequest.getMaxdifferenceBetweenPhases();
		double maxPower = planRequest.getMaxNetPower();
		
		//for each request in the instance, creates a rectangle with feasible dimensions
		for(Request request : planRequest.getRequestList()) {
			Rectangle rect = new Rectangle(request.vehicleId, 
											 request.phase, 
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

	
	//save the solution, if exists another solution with the same name, add a number to the name
	public static void saveSolution(String path, String filename, Plan solution) throws JsonMappingException, JsonProcessingException {
		String completeName = path+filename;
        try {
            File myObj = new File(completeName);
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            }
            else {
            	int i = 1;
            	while(!myObj.createNewFile()) {
            		i++;
            		completeName = path+ i +filename;
            		myObj = new File(completeName);
            	}
            }
          } 
        	catch (IOException e) {
            System.err.println("An error occurred during file saving.");
            e.printStackTrace();
          }
        
        String s = JSON.objToString(solution);
        
        try {
            FileWriter myWriter = new FileWriter(completeName);
            myWriter.write(s);
            myWriter.close();
            System.err.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.err.println("An error occurred during file writing.");
            e.printStackTrace();
          }
	}
	
	
	public static void saveSolution(String path, Plan solution) throws JsonMappingException, JsonProcessingException {
        try {
            File myObj = new File(path);
            myObj.createNewFile();
            System.out.println("File created: " + myObj.getName());
        }
    	catch (IOException e) {
    		System.err.println("An error occurred during file creation.");
    		System.out.println("path: " + path);
    		e.printStackTrace();
        }
        
        String s = JSON.objToString(solution);      
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(s);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } 
        catch (IOException e) {
            System.err.println("An error occurred during file writing.");
            e.printStackTrace();
         }
	}
	
	
}