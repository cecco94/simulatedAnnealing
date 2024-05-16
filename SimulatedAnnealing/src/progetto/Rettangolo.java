package progetto;
import java.util.ArrayList;
import java.util.Random;

import utils.RectImpossibleException;
import utils.RettangoloSemplificato;

public class Rettangolo implements Comparable<Rettangolo>{
	
	public int identificativo;
	
	public int fase;
	
	//è la massima portata erogabile dal plug, collegato ad una certa rete = min(portata plug, portata impiano elettrico)
	public double altezza, maxAltezzaPossibile;
	
	//è la minima carica che usa la macchina per ricaricarsi
	public double minAltezzaPossibile;
	
	public double area;
	public int base;

	//intervallo temporale massimo, inserito dall'utente
	public int margineSinistroMinimo;
	public int margineDestroMassimo;
	
	public int baseMassima, baseMinima;
	
	//intervallo temporale imigliore, da trovare
	public int margineSinistro, margineDestro;
	
	
	//serve un costruttore vuoto per la creazione del file json
	public Rettangolo() {
	}
	
	//la prima volta che viene creato, il rettangolo ha base = base massima. Se ha più tempo a disposizione, il rettangolo avrà punto di inizio 
	//margineSinistroMinimo e punto di fine margineSinistroMinimo + baseMassima
	public Rettangolo(int id, int fase, int msm, int mdm, double a, double max_h, double min_h) throws RectImpossibleException {
		if(mdm <= msm) {
			throw new RectImpossibleException("intervallo di tempo negativo o nullo");
		}
		
		identificativo = id;
		
		this.fase = fase;
		
		area = a;
		
		maxAltezzaPossibile = max_h;
		minAltezzaPossibile = min_h;
		
		margineSinistroMinimo = msm;
		margineDestroMassimo = mdm;
		
		margineSinistro = margineSinistroMinimo;
		margineDestro = margineDestroMassimo;
		
		baseMinima = (int)(Math.ceil(area/maxAltezzaPossibile));
		//può capitare che il tempo a disposizione sia maggiore del massimo intervallo che ci mette la macchina a caricarsi (quando abbiamo tutta la notte a disposizione)
		//può anche capitare invece che il tempo a disposizione sia minore del massimo intervallo che ci mette la macchina a caricarsi
		baseMassima = Math.min((int)(area/minAltezzaPossibile), mdm - msm);
		
		//se il rect con base massima è troppo stretto, non possiamo che riportare un errore
		base = margineDestro - margineSinistro;
		if(base < baseMinima) {
			throw new RectImpossibleException("troppo poco tempo per caricare il veicolo " + "area " + a + " base " + base + " altezza " + (area/base));
		}
		
		//se il rect con base massima è troppo largo, accorciamo il margine destro fino al valore di base massimo ammissibile
		//accade solo se diamo tutta la notte a disposizione per la ricarica
		if(base > baseMassima) {
			margineDestro = margineSinistroMinimo + baseMassima;
			base = margineDestro - margineSinistro;
		}
		
		altezza = area/base;
		
	}	
	
	
	//rettangolo che viene creato durante il ciclo for, con la generazione casuale
	public Rettangolo(int id, int fase, int msm, int mdm, int ms, int md, double a, double max_h, double min_h, int bmin, int bmax) throws RectImpossibleException {
				
		if(md <= ms) {
			throw new RectImpossibleException("intervallo di tempo negativo o nullo");
		}
		
		identificativo = id;
		this.fase = fase;

		margineSinistroMinimo = msm;
		margineDestroMassimo = mdm;
		
		margineSinistro = ms;
		margineDestro = md;
		
		baseMinima = bmin;
		baseMassima = bmax;
		
		maxAltezzaPossibile = max_h;
		minAltezzaPossibile = min_h;
		
		if(margineDestro > margineDestroMassimo || margineSinistro < margineSinistroMinimo) {
			throw new RectImpossibleException("sei andato oltre il tempo massimo/minimo " + id + ",  ms " + margineSinistro + ",  md " + margineDestro + 
												",  msm " + margineSinistroMinimo +  ",  mdm " + margineDestroMassimo);
		}
		
		area = a;
		
		base = margineDestro - margineSinistro;
				
		//se il rect con base massima è troppo largo, lo accorciamo fino al valore di base massimo ammissibile
		if(base > baseMassima) {
			margineDestro = margineSinistroMinimo + baseMassima;
			base = margineDestro - margineSinistro;
		}

		altezza = area/base;
		//se il reect è troppo basso, 
		if(altezza < minAltezzaPossibile) {
			margineDestro = margineDestroMassimo;
			margineSinistro = margineDestroMassimo - baseMassima;
			base = baseMassima;
			altezza = area/baseMassima;
		}
		
	}

	
	//dato il rettangolo, dovrebbe generare un altro rettangolo ammissibile attraverso piccole perturbazioni casuali dei valori idella base
	public Rettangolo randomGeneration(Random rand) throws RectImpossibleException {
		
		int nuovoMargineSinistro, nuovoMargineDestro;
		
		//sistemiamo il margine sinistro:
		nuovoMargineSinistro = margineSinistro + rand.nextInt(-15, 16);
		
		//se il margine sisnitro è andato troppo a sinistra
		if(nuovoMargineSinistro < margineSinistroMinimo)
			nuovoMargineSinistro = margineSinistroMinimo;
		
		//se è andato troppo a destra
		if(nuovoMargineSinistro > margineDestroMassimo - baseMinima) {
			nuovoMargineSinistro = margineDestroMassimo - baseMinima;
		}
			
		//sistemiamo ora il margine destro
		nuovoMargineDestro = margineDestro + rand.nextInt(-15, 16);
		
		//se abbiamo stretto troppo, dobbiamo allargare il rect
		if(nuovoMargineDestro - nuovoMargineSinistro < baseMinima) {
			nuovoMargineDestro = nuovoMargineSinistro + baseMinima;
		}
		
		//se abbiamo allargato troppo, dobbiamo stringere il rect
		if(nuovoMargineDestro - nuovoMargineSinistro > baseMassima) {
			nuovoMargineDestro = margineDestroMassimo; //nuovo_margine_sinistro + base_massima;
		}
		
		if(nuovoMargineDestro > margineDestroMassimo)
			nuovoMargineDestro = margineDestroMassimo;

		return new Rettangolo(identificativo, fase, margineSinistroMinimo, margineDestroMassimo, nuovoMargineSinistro, nuovoMargineDestro, 
												area, maxAltezzaPossibile, minAltezzaPossibile, baseMinima, baseMassima);
	
	}

	
	//trasla il rettangolo di base massima lungo tutto il tempo a sua disposizione
	public Rettangolo randomGenerationForTranslationProblem(Random rand) throws RectImpossibleException {
		int nuovoMargineSinistro = margineSinistro;
		int nuovoMargineDestro = margineDestro;
				
		//sistemiamo il margine sinistro:
		nuovoMargineSinistro += rand.nextInt(-10, 11);
		
		if(nuovoMargineSinistro < margineSinistroMinimo) {
			nuovoMargineSinistro = margineSinistroMinimo;
		}
		
		if(nuovoMargineSinistro > margineDestroMassimo - baseMassima) {
			nuovoMargineSinistro = margineDestroMassimo - baseMassima;
		}
		
		nuovoMargineDestro = nuovoMargineSinistro + baseMassima;
		
		Rettangolo nuovo_rect = new Rettangolo(identificativo, fase, margineSinistroMinimo, margineDestroMassimo, nuovoMargineSinistro, nuovoMargineDestro, 
				area, maxAltezzaPossibile, minAltezzaPossibile, baseMinima, baseMassima);	
		
		//System.out.println(nuovo_rect.toString());
		
		return nuovo_rect;
	}
	
	
	@Override
	public int compareTo(Rettangolo r) {
		
		if(margineSinistro < r.margineSinistro)
			return -1;
		
		if(margineSinistro > r.margineSinistro)
			return 1;
		
		return 0;
	}
	
	
	public Rettangolo clone() {

		Rettangolo nuovo_rettangolo = null;
		
		try {
			nuovo_rettangolo = new Rettangolo(identificativo, fase, margineSinistroMinimo, margineDestroMassimo, margineSinistro, margineDestro, area, 
									maxAltezzaPossibile, minAltezzaPossibile, baseMinima, baseMassima);
		} 
		catch (RectImpossibleException e) {
			e.printStackTrace();
		}
		
		return nuovo_rettangolo;
	}
	
	
	public String toString() {
		return "id " + identificativo + ",  fase " + fase + ",  area " + area + ",  altezza " + altezza + ",  start " + margineSinistro + ",  stop " + margineDestro + 
				",  msm " + margineSinistroMinimo + ",  mdM " + margineDestroMassimo + ",  base massima " + baseMassima + ",  base" + base;
	}
	
	public String toFile() {
		return "id " + identificativo + ",  ora inizio " + margineSinistro + ",  ora fine " + margineDestro;
	}
	
	public RettangoloSemplificato generaRectDaMettereNelJson() {
		return new RettangoloSemplificato(identificativo, fase, area, margineSinistro, margineDestro);
	}
	
}

