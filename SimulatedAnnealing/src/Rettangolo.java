import java.util.Random;

public class Rettangolo{

	public int base;
	//è la massima portata erogabile dal plug, collegato ad una certa rete = min(portata plug, portata impiano elettrico)
	public double altezza, max_altezza_possibile;
	//è la minima carica che usa la macchina per ricaricarsi
	public double min_altezza_possibile;
	
	public double area;
	
	public int margine_sinistro_minimo;
	public int margine_destro_massimo;
	
	public int base_massima, base_minima;
	
	int margine_sinistro, margine_destro;
	
	//rettangolo che viene creato durante il ciclo for
	public Rettangolo(int msm, int mdm, int ms, int md, double a, double max_h, double min_h, int bmin, int bmax) {
		margine_sinistro_minimo = msm;
		margine_destro_massimo = mdm;
		
		margine_sinistro = ms;
		margine_destro = md;
		
		base_minima = bmin;
		base_massima = bmax;
		
		max_altezza_possibile = max_h;
		min_altezza_possibile = min_h;
		
		if(margine_destro > margine_destro_massimo || margine_sinistro < margine_sinistro_minimo) {
			System.out.println("dimensioni base sballate");
		}
		
		area = a;
		base = margine_destro - margine_sinistro;
		//se il rect con base massima è troppo stretto, non possiamo che riportare un errore
		base = margine_destro - margine_sinistro;
		if(base < base_minima) {
			System.out.println("troppo poco tempo per caricare il veicolo");
		}
		
		//se il rect con base massima è troppo largo, lo accorciamo fino al valore di base massimo ammissibile
		if(base > base_massima) {
			margine_destro = margine_sinistro_minimo + base_massima;
			base = margine_destro - margine_sinistro;
		}
		
		altezza = area/base;
		
	}
	
	//la prima volta che viene creato, il rettangolo ha base = base massima
	public Rettangolo(int msm, int mdm, double a, double max_h, double min_h) {
		if(mdm <= msm) {
			System.out.println("base negativa o nulla!");
		}
		
		area = a;
		
		max_altezza_possibile = max_h;
		min_altezza_possibile = min_h;
		
		margine_sinistro_minimo = msm;
		margine_destro_massimo = mdm;
		
		margine_sinistro = msm;
		margine_destro = mdm;
		
		base_minima = (int)(Math.ceil(area/max_altezza_possibile));
		base_massima = (int)(area/min_altezza_possibile);
		
		//se il rect con base massima è troppo stretto, non possiamo che riportare un errore
		base = margine_destro - margine_sinistro;
		if(base < base_minima) {
			System.out.println("troppo poco tempo per caricare il veicolo");
		}
		
		//se il rect con base massima è troppo largo, lo accorciamo fino al valore di base massimo ammissibile
		if(base > base_massima) {
			margine_destro = margine_sinistro_minimo + base_massima;
			base = margine_destro - margine_sinistro;
		}
		
		altezza = area/base;
		
	}
	
	public boolean intersect(Rettangolo rect) {
		boolean primo_caso = rect.margine_sinistro < margine_destro && margine_destro < rect.margine_destro;	//sta a sinistra dell'alatro
		boolean secondo_caso = rect.margine_sinistro < margine_sinistro && margine_sinistro < rect.margine_destro;	//sta a destra dellaltro rect
		boolean terzo_caso = margine_sinistro <= rect.margine_sinistro && margine_destro >= rect.margine_destro;	//contiene l'altro rect
		boolean quarto_caso = margine_sinistro >= rect.margine_sinistro && margine_destro <= rect.margine_destro;	//è contenuto nell'altro rect
		return primo_caso || secondo_caso || terzo_caso || quarto_caso;
	}
	
	
	//dato il rettangolo, dovrebbe generare un altro rettangolo ammissibile attraverso piccole perturbazioni casuali dei valor idella base
	public Rettangolo random_generation(Random rand) {
		
		int nuovo_margine_sinistro, nuovo_margine_destro;
		
		//sistemiamo il margine sinistro:
		if(rand.nextBoolean())
			nuovo_margine_sinistro = margine_sinistro + 1;
		else
			nuovo_margine_sinistro = margine_sinistro - 1;
		
		//se il margine sisnitro è andato troppo a sinistra
		if(nuovo_margine_sinistro < margine_sinistro_minimo)
			nuovo_margine_sinistro = margine_sinistro_minimo;
		
		//se è andato troppo a destra
		if(nuovo_margine_sinistro > margine_destro_massimo - base_minima) {
			nuovo_margine_sinistro = margine_destro_massimo - base_minima;
		}
			
		//sistemiamo ora il margine destro
		if(rand.nextBoolean())
			nuovo_margine_destro = margine_destro + 1;
		else
			nuovo_margine_destro = margine_destro - 1;
		
		if(nuovo_margine_destro > margine_destro_massimo)
			nuovo_margine_destro = margine_destro_massimo;
		
		//se abbiamo stretto troppo, dobbiamo allargare il rect
		if(nuovo_margine_destro - nuovo_margine_sinistro < base_minima) {
			nuovo_margine_destro = nuovo_margine_sinistro + base_minima;
		}
		
		//se abbiamo allargato troppo, dobbiamo stringere il rect
		if(nuovo_margine_destro - nuovo_margine_sinistro > base_massima) {
			nuovo_margine_destro = nuovo_margine_sinistro + base_massima;
		}
		
		return new Rettangolo(margine_sinistro_minimo, margine_destro_massimo, nuovo_margine_sinistro, nuovo_margine_destro, 
												area, max_altezza_possibile, min_altezza_possibile, base_minima, base_massima);
	}
	
	public String toString() {
		return "area " + area + ", altezza " + altezza + ", sin " + margine_sinistro + ", des " + margine_destro;
	}
	
}

