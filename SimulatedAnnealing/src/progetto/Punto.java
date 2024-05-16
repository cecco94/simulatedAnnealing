package progetto;

import lombok.Data;


@Data
public class Punto {

	public Rettangolo r;
	public int x;
	public double altezzaRettangolo, sfasamentoNelPunto = 0, sommaAltezzeNelPunto = 0;
	public boolean punto_di_inizio;
	
	
	public Punto() {
		
	}
	
	public Punto(Rettangolo r, int m, double altezza_rect, boolean punto_di_inizio) {
		super();
		this.r = r;
		this.x = m;
		this.altezzaRettangolo = altezza_rect;
		this.punto_di_inizio = punto_di_inizio;
	}
	
	public String toString() {
		return "id rect " + r.identificativo + ",  minuto " + x + ",  altezza " + altezzaRettangolo +
				",  fase " + r.fase + ",  sommaAltezze " + sommaAltezzeNelPunto + ",  sfasamento " + sfasamentoNelPunto;
	}
	
	
}
