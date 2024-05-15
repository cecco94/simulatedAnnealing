package utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import progetto.Soluzione;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
	
	
	public static Soluzione caricaIstanzaProblema(String path) throws JsonMappingException, JsonProcessingException {
		//prende il file json e lo trasforma in una stringa
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
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
				
		Soluzione sol = stringToObj(data, Soluzione.class);
		return sol;
	}
	
	


	public static void salvaIstanzaProblema(String path, Soluzione istanza) throws JsonMappingException, JsonProcessingException {
		//salva il problema nella cartella data
        try {
            File myObj = new File(path);
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            }
            else {
              System.out.println("File already exists.");
            }
          } 
        	catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        
        String s = JSON.objToString(istanza);
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(s);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
		
	}
	
	
	
	
	
}