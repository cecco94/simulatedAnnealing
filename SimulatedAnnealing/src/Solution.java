import java.util.ArrayList;

public class Solution {

    ArrayList<Rettangolo> fase;
    double cost;
    
    public Solution(ArrayList<Rettangolo> rettangoli) {
    	fase = rettangoli;
    }
    
    //public double cost_of_solution() {
    //ordiniamo la lista dei rect in ordine di inizio della carica
    //criamo un grafo dove ci salviamo chi interseca chi
    //ogni rect guarda i suoi successori, se il loro punto di inizio è prima del suo punto di fine, vuol dire che si intersecano
    //salva nel grafo questa informazione (per es a interseca b, creiamo un collegamento tra i nodi a,b)
    //appena arriva al rect il cui punto di inizio è > del punto di fine del rect che sta scandendo, smette di scandire: non ci saranno sicuramente altri rect intersecanti
    
    //si passa al rect successivo, che scandisce la lista dalla posizione i + 1
    	
    //una volta creato il grafo, il costo è il massimo tra la somma delle altezze dei vari gruppetti
    //}
    
}
