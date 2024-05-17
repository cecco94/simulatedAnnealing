package utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import progetto.Soluzione;
import utils.visualization.Dataset;

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
	

	public static void salvaIstanzaProblema(String path, String filename, Soluzione istanza) throws JsonMappingException, JsonProcessingException {
		//salva il problema nella cartella data
		String nomeCompleto = path+filename;
        try {
            File myObj = new File(nomeCompleto);
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            }
            //se già esiste il file, ne crea un altro con un numero più alto
            else {
            	int i = 1;
            	while(!myObj.createNewFile()) {
            		i++;
            		nomeCompleto = path+ i +filename.substring(1);
            		myObj = new File(nomeCompleto);
            	}
            }
          } 
        	catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        
        String s = JSON.objToString(istanza);
        
        try {
            FileWriter myWriter = new FileWriter(nomeCompleto);
            myWriter.write(s);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
		
	}
	
	
	public static void salvaSoluzione(String path, String filename, SoluzioneSemplificata soluzione) throws JsonMappingException, JsonProcessingException {
		String nomeCompleto = path+filename;
        try {
            File myObj = new File(nomeCompleto);
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            }
            else {
            	int i = 1;
            	while(!myObj.createNewFile()) {
            		i++;
            		nomeCompleto = path+ i +filename;
            		myObj = new File(nomeCompleto);
            	}
            }
          } 
        	catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        
        String s = JSON.objToString(soluzione);
        
        try {
            FileWriter myWriter = new FileWriter(nomeCompleto);
            myWriter.write(s);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
	}
	
	
	public static void salvaDatiProblema(String path, String filename, Dataset dati) throws JsonMappingException, JsonProcessingException {
		//salva il problema nella cartella data
		String nomeCompleto = path+filename;
        try {
            File myObj = new File(nomeCompleto);
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            }
            //se già esiste il file, ne crea un altro con un numero più alto
            else {
            	int i = 1;
            	while(!myObj.createNewFile()) {
            		i++;
            		nomeCompleto = path+ i +filename.substring(1);
            		myObj = new File(nomeCompleto);
            	}
            }
          } 
        	catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        
        String s = JSON.objToString(dati);
        
        try {
            FileWriter myWriter = new FileWriter(nomeCompleto);
            myWriter.write(s);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
		
	}
	
	
	public static Dataset caricaDatiProblema(String path) throws JsonMappingException, JsonProcessingException {
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
				
		Dataset d = stringToObj(data, Dataset.class);
		return d;
	}
}