package utils;
import java.util.Comparator;

import progetto.Punto;

public class ComparatorCosto implements Comparator<Punto>{
//mi serve per trovare velocemente i punti dei rect dove la somma delle altezze è maggiore
	
	
	@Override
	public int compare(Punto p1, Punto p2) {
		if(p1.costoNelPunto < p2.costoNelPunto)
			return 1;
		else if(p1.costoNelPunto > p2.costoNelPunto)
			return -1;
		return 0;
	}

}
