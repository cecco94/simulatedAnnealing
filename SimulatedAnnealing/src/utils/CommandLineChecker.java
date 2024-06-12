package utils;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class CommandLineChecker {
	
	String[] args;
	String inputPath;
	String outputPath;
	boolean printSol = false;
	CommandLineParser commandLineParser;
	
	public CommandLineChecker(String[] args) {
		this.args = args;
		commandLineParser = new CommandLineParser(args);
	}
	
	
	public boolean checkInput() {	
		
		inputPath = commandLineParser.getSwitchValue("-i");		
		if( inputPath == "" ) {
			System.err.println("'-i' not found");
			return false;
		}
		
		Path inputPath = Paths.get( commandLineParser.getSwitchValue("-i") );	
		if( !Files.exists(inputPath) ) {
			System.err.println("imput file not found");
			return false;
		}
		
		outputPath = commandLineParser.getSwitchValue("-o");
		if( outputPath == "" ) {
			LocalDateTime date = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
			String formattedDate = date.format(myFormatObj); 
			outputPath = formattedDate + ".json";
		}
		
		printSol = commandLineParser.getFlag("-p");	
		return true;
	}	
	
}




