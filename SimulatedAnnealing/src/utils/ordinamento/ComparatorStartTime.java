package utils.ordinamento;

import java.util.Comparator;

import progetto.Point;

public class ComparatorStartTime implements Comparator<Point>{
//mi serve per ordinare i punti dei rect in ordine di tempo
	
	@Override
	public int compare(Point p1, Point p2) {
		if(p1.getMinute() < p2.getMinute())
			return -1;
		else if(p1.getMinute() > p2.getMinute())
			return 1;
		return 0;
	}


}
