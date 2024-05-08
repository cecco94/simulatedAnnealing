
public class Punto implements Comparable<Punto>{

	Rettangolo r;
	int x;
	double altezza_rect;
	boolean punto_di_inizio;
	
	
	public Punto(Rettangolo r, int x, double altezza_rect, boolean punto_di_inizio) {
		super();
		this.r = r;
		this.x = x;
		this.altezza_rect = altezza_rect;
		this.punto_di_inizio = punto_di_inizio;
	}


	@Override
	public int compareTo(Punto p) {
		if(x > p.x)
			return 1;
		if(x < p.x)
			return -1;
		return 0;
	}
	
	
	public String toString() {
		return "id rect " + r.identificativo + ",  minuto " + x + ",  altezza " + altezza_rect + ",  inizio " + punto_di_inizio;
	}
	
	
}
