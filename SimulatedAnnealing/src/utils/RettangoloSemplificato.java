package utils;

public class RettangoloSemplificato {

	public int identificativo;
	
	public int fase;
	
	public double area;
	
	//intervallo temporale imigliore, da trovare
	public int margineSinistro, margineDestro;
	
	
	//serve un costruttore vuoto per la creazione del file json
	public RettangoloSemplificato() {
	}

	public RettangoloSemplificato(int identificativo, int fase, double area, int margineSinistro, int margineDestro) {
		super();
		this.identificativo = identificativo;
		this.fase = fase;
		this.area = area;
		this.margineSinistro = margineSinistro;
		this.margineDestro = margineDestro;
	}
	
}
