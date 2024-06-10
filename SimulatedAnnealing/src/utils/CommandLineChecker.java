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
	CommandLineParser commandLineParser;
	
	public CommandLineChecker(String[] args) {
		this.args = args;
		commandLineParser = new CommandLineParser(args);
	}
	
	
	public boolean checkInput() {		
		//the possible length is 2 or 4 
		if( !(commandLineParser.getNumParameters() == 2 || commandLineParser.getNumParameters() == 4)) {
			return false;
		}
		
		//if there are 2 parameters, checks the -i param
		if ( commandLineParser.getNumParameters() == 2 ) {	
			if ( commandLineParser.getSwitchValue("-i") == "" ) {
				return false;
			}
			//check the path
			Path inputPath = Paths.get( commandLineParser.getSwitchValue("-i") );	
			if( !Files.exists(inputPath) ) {
				return false;
			}
			
			this.inputPath = commandLineParser.getSwitchValue("-i");
			LocalDateTime date = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
			String formattedDate = date.format(myFormatObj); 
			this.outputPath = formattedDate + ".json";
		}
		
		//if there are 4 params, chack the -i param and the -o param
		else if ( commandLineParser.getNumParameters() == 4 ) {	
			if ( commandLineParser.getSwitchValue("-i") == "" || commandLineParser.getSwitchValue("-o") == "" ) {
				return false;
			}
			Path inputPath = Paths.get( commandLineParser.getSwitchValue("-i") );	
			Path outpuPath = Paths.get( commandLineParser.getSwitchValue("-o") );	
			if( !Files.exists(inputPath) || !Files.exists(outpuPath) ) {
				return false;
			}
			this.inputPath = commandLineParser.getSwitchValue("-i");
			this.outputPath = commandLineParser.getSwitchValue("-o");
		}		
		return true;
	}	
	
}




