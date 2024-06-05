package utils;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

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
		//la lunghezza può essere 2 o 4 
		if( !(commandLineParser.getNumParameters() == 2 || commandLineParser.getNumParameters() == 4)) {
			return false;
		}
		
		//se la lunghezza è 2, controllo che il campo di -i non sia vuoto
		if ( commandLineParser.getNumParameters() == 2 ) {	
			if ( commandLineParser.getSwitchValue("-i") == "" ) {
				return false;
			}
			//controllo che il path esista
			Path inputPath = Paths.get( commandLineParser.getSwitchValue("-i") );	
			if( !Files.exists(inputPath) ) {
				return false;
			}
			
			this.inputPath = commandLineParser.getSwitchValue("-i");
			LocalDateTime date = LocalDateTime.now();
			this.outputPath = date.toString();
		}
		
		//se invece è 4, controllo sia il campo di -i, sia quello di -o
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




