import java.util.Random;

public class Simulated_annealing_algorithm {

	
	public static void main(String[] args) {
		
		//crea la prima soluzione, quella coi tect di base massima
		//crea la variabile che salva la soluzione migliore, inizialmente è la soluzione iniziale
		
        //Set initial temp
        double temperature = 100000;

        //Cooling rate
        double cooling_rate = 0.003;
        
        Rettangolo r = new Rettangolo(0, 10, 20, 10, 1);
        System.out.println(r.toString());
        
        Random rand = new Random();
        Rettangolo r1 = r.random_generation(rand);
        System.out.println(r1.toString());

//        for(double t = temperature; t > 1; t *= (1 - cooling_rate)){
//        	
//        	//genera nuova soluzione tramite piccole perturbazioni casuali della soluzione attuale
//        	
//        	//se il costo della nuova soluzione è < costo vecchia soluzione, accetta la nuova soluzione
//        	//aggiorna la soluzione migliore, se serve
//        	
//        	//se il costo è invece maggiore, accetta la soluzione con un probabilià e ^ (costoVecchiaSol - costoNuovaSol)/temperatura)
//
//        }
	}

}
