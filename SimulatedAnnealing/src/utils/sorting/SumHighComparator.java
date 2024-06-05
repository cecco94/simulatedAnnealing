package utils.sorting;
import java.util.Comparator;

import progetto.Point;

public class SumHighComparator implements Comparator<Point>{
//mi serve per trovare velocemente i punti dei rect dove la somma delle altezze è maggiore
	
	
	@Override
	public int compare(Point p1, Point p2) {
		if(p1.getSumHeightInThisPoint() < p2.getSumHeightInThisPoint())
			return 1;
		else if(p1.getSumHeightInThisPoint() > p2.getSumHeightInThisPoint())
			return -1;
		return 0;
	}

}
