package utils;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Istanza {
    ArrayList<Richiesta> richieste;

    
    //serve per creare il json
    public Istanza() {
    	
    }
    
    public Istanza(ArrayList<Richiesta> rettangoli) {
    	this.richieste = rettangoli;
    }
}
