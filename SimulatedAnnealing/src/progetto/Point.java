package progetto;

import lombok.Data;


@Data
public class Point {

	Rectangle rect;
	int minute;
	double rectHeight, phaseDifferenceInThisPoint = 0, sumHeightInThisPoint = 0;
	boolean startPoint;
	
	
	public Point() {
		
	}
	
	public Point(Rectangle r, int m, double h_rect, boolean startingPoint) {
		super();
		this.rect = r;
		this.minute = m;
		this.rectHeight = h_rect;
		this.startPoint = startingPoint;
	}
	
	public String toString() {
		return "id vehicle " + rect.idNumber + ",  minute " + minute + ",  vehicle power " + rectHeight +
				",  phase " + rect.phase + ",  sum power " + sumHeightInThisPoint + ",  difference of phase " + phaseDifferenceInThisPoint;
	}
	
	
}
