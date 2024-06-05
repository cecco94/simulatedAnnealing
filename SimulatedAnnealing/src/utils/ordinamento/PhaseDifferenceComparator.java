package utils.ordinamento;

import java.util.Comparator;

import progetto.Point;

public class PhaseDifferenceComparator implements Comparator<Point>{

	@Override
	public int compare(Point p1, Point p2) {
		if(p1.getPhaseDifferenceInThisPoint() > p2.getPhaseDifferenceInThisPoint())
			return -1;
		else if(p1.getPhaseDifferenceInThisPoint() < p2.getPhaseDifferenceInThisPoint())
			return 1;
		return 0;
	}

}
