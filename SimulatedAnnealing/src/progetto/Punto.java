package progetto;

public class Punto {

	public Rettangolo r;
	public int x;
	public double altezzaRettangolo, sfasamentoNelPunto = 0, costoNelPunto = 0;
	public boolean punto_di_inizio;
	
	
	public Punto(Rettangolo r, int x, double altezza_rect, boolean punto_di_inizio) {
		super();
		this.r = r;
		this.x = x;
		this.altezzaRettangolo = altezza_rect;
		this.punto_di_inizio = punto_di_inizio;
	}
	
	public String toString() {
		return "id rect " + r.identificativo + ",  minuto " + x + ",  altezza " + altezzaRettangolo +
				",  inizio " + punto_di_inizio + ",  costo " + costoNelPunto + ",  sfasamento " + sfasamentoNelPunto;
	}
	
	
}
