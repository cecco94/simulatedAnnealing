package utils;

import java.util.ArrayList;

import lombok.Data;

@Data
public class SoluzioneSemplificata {
    ArrayList<RettangoloSemplificato> rettangoli;
    double sfasamento;
    double altezzaMax;
    double costo;
    
    //serve per creare il json
    public SoluzioneSemplificata() {
    	
    }
    
    public SoluzioneSemplificata(ArrayList<RettangoloSemplificato> rettangoli, double s, double a, double c) {
    	this.rettangoli = rettangoli;
    	sfasamento = s;
    	altezzaMax = a;
    	costo = c;
    }
}
