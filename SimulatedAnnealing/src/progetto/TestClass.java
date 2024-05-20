package progetto;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import utils.GeneratoreIstanze;
import utils.JSON;
import utils.PlanImpossibleException;
import utils.RectImpossibleException;
import utils.SoluzioneSemplificata;
import utils.visualization.Grafico;
import utils.visualization.PannelloAltezzaSoluzione;
import utils.visualization.PannelloSfasamentoSoluzione;
import utils.visualization.Dato;
import utils.visualization.Dataset;


public class TestClass {

	public static int altezzaFinestra = 600;
	public static int larghezzaFinestra = 600;

	//li uso come fossero dei bottoni per attivare/disattivare le funzionalità
	public static boolean newProblem = false, saveSolution = false, istanzaSpecifica = false, 
							grafico = false, datasetCreation = false, conTraslazione = true;
	
	//per decidere quali dati creare o lggere
	public static boolean tempo_su_temperatura = false, costo_su_temperatura = false, tempo_su_raffreddamento = false, 
						  costo_su_raffreddamento = false, costo_su_passo = false;
	
	//per decidere la configurazione del problema
	public static int macchine_tranquille = 5, macchine_urgenti = 1;
	
	//perchè possono esserci più versioni della stessa configurazione
	public static int istanza = 1;
	
	
	public static void main(String[] args) throws RectImpossibleException, JsonMappingException, JsonProcessingException, PlanImpossibleException {
				
		if(newProblem) {			
			GeneratoreIstanze.generaIstanzaProblema(macchine_tranquille, macchine_urgenti);  
			return;
		}	
		
		if(istanzaSpecifica) {
			GeneratoreIstanze.generaIstanzaSpecifica();  
			return;
		}
			
		if(grafico) {
			disegnaGrafico();
			return;
		}
		
		if(datasetCreation) {
			creaNuoviDati();
			return;
		}
		
		//se non sono state richieste funzionalità particolari, risolve semplicemente il problema
		//carica istanza del problema
		Soluzione soluzioneIniziale = JSON.caricaIstanzaProblema("data/istanze/" + istanza + "_problema_con_"+macchine_tranquille+
																"_macchine_tranquille_"+macchine_urgenti+"_macchine_urgenti.json");
		double costoSoluzioneIniziale = soluzioneIniziale.costoSoluzione();
        visualizzaDatiIniziali(soluzioneIniziale, costoSoluzioneIniziale);                           
        
        Soluzione migliore_soluzione;
        double costo_migliore_soluzione;
        
        //per fare dei test, la soluzione sembra migliorare se prima distribuisco i rect nel tempo
        if(conTraslazione) {
        	migliore_soluzione = AlgoritmoSimulatedAnnealing.simulatedAnnealingTraslaz(soluzioneIniziale, costoSoluzioneIniziale);
            costo_migliore_soluzione = migliore_soluzione.costoSoluzione();
            
            visualizzaDatiIntermedi(migliore_soluzione, costo_migliore_soluzione);
            
            migliore_soluzione = AlgoritmoSimulatedAnnealing.simulatedAnnealing(migliore_soluzione, costo_migliore_soluzione);
            costo_migliore_soluzione = migliore_soluzione.costoSoluzione();
        }
        
        //algoritmo normale
        else {
        	migliore_soluzione = AlgoritmoSimulatedAnnealing.simulatedAnnealing(soluzioneIniziale, costoSoluzioneIniziale);
            costo_migliore_soluzione = migliore_soluzione.costoSoluzione();
        }
        
        visualizzaDatiFinali(migliore_soluzione, costo_migliore_soluzione);
        controllaSoluzione(migliore_soluzione);
        		
	}

	
	////////////////////////// METODI AUSILIARI ///////////////////////////
	
	private static void creaNuoviDati() throws JsonMappingException, JsonProcessingException, RectImpossibleException {
		//genera casualmente 10 istanze del problema con stesso numero di macchine e le risolve, poi fa la media del tempo/costo
		System.out.println("dataset creation...");
		
		Soluzione istanzaCasuale; 
		double costoIstanzaCasuale; 
		ArrayList<Dato> dati = new ArrayList<>();
			
		if(tempo_su_temperatura) {
			//crea 10 istanze del problema e le risolve con temperatura crescente
			double[][] tempiImpiegati = new double[10][5];
			for(int istanza = 0; istanza < 10; istanza++) {
				istanzaCasuale = GeneratoreIstanze.generaIstanzaProblemaSenzaSalvare(macchine_tranquille, macchine_urgenti);
				costoIstanzaCasuale = istanzaCasuale.costoSoluzione();
				
				//la stessa istanza la risolve 4 volte, aumentando sempre la temperatura
				int iterazione = 0;
				for(int temp = 1000; temp <= 100*1000*1000; temp*=10) {
					tempiImpiegati[istanza][iterazione] = AlgoritmoSimulatedAnnealing.misuraSimulatedAnnealing(istanzaCasuale, 
																							costoIstanzaCasuale, temp, 0.00001);
					iterazione++;
				}
				System.out.println("problema " + istanza + " creato");
			}
			//ora fa la media dei valori sulle colonne, che sarebbero i tempi di diversi problemi ma alla stessa temperatura
			int temperatura = 10000;
			for(int iterazione = 0; iterazione < 4; iterazione++) {
				double tempoMedioConQuellaTemperatura = 0;

				for(int istanza = 0; istanza < 10; istanza++) {
					tempoMedioConQuellaTemperatura += tempiImpiegati[istanza][iterazione];
				}
				tempoMedioConQuellaTemperatura /= 10;
				
				Dato d = new Dato(tempoMedioConQuellaTemperatura, Integer.toString(temperatura/1000), "tempo");
				dati.add(d);
				
				System.out.println("dati per temperatuta = " + temperatura + " completati");
				temperatura *= 10;
			}
			Dataset dataset = new Dataset(dati);
			JSON.salvaDatiProblema("data/grafici/", "_tempo_su_temperatura_" + macchine_tranquille +"_" + macchine_urgenti +".json", dataset);
		}
		
		
		else if (costo_su_temperatura) {
			//crea 10 istanze del problema e le risolve con temperatura crescente			
			double[] costi = new double[6];
			
			for(int istanza = 0; istanza < 10; istanza++) {
				istanzaCasuale = GeneratoreIstanze.generaIstanzaProblemaSenzaSalvare(macchine_tranquille, macchine_urgenti);
				costoIstanzaCasuale = istanzaCasuale.costoSoluzione();
				double temperatura = 1000;
				for(int iterazione = 0; iterazione < 6; iterazione++) {
					double costo = AlgoritmoSimulatedAnnealing.misuraSimulatedAnnealing(istanzaCasuale, costoIstanzaCasuale, temperatura,  0.00001);
					costi[iterazione] += costo/10; 
					System.out.println(costo + ", " + temperatura);
					temperatura *= 10;
				}
				System.out.println("istanza " +  istanza + " ultimata");
			}
			int temperatura = 1000;
			for(int iterazione = 0; iterazione < 6; iterazione++) {
				Dato d = new Dato(costi[iterazione], Integer.toString(temperatura/1000), "costo");
				dati.add(d);
				temperatura *= 10;
			}
			Dataset dataset = new Dataset(dati);
			JSON.salvaDatiProblema("data/grafici/", "_costo_su_temperatura_" + macchine_tranquille +"_" + macchine_urgenti +".json", dataset);
		}

		else if(costo_su_passo) {
			//crea 10 istanze del problema e le risolve con temperatura crescente			
			double[] costi = new double[10];
			
			for(int istanza = 0; istanza < 10; istanza++) {
				istanzaCasuale = GeneratoreIstanze.generaIstanzaProblemaSenzaSalvare(macchine_tranquille, macchine_urgenti);
				costoIstanzaCasuale = istanzaCasuale.costoSoluzione();
				int passo = 1;
				for(int iterazione = 0; iterazione < 10; iterazione++) {
					double costo = AlgoritmoSimulatedAnnealing.misuraSimulatedAnnealing(istanzaCasuale, costoIstanzaCasuale, 100000000,  0.00001);
					costi[iterazione] += costo/10; 
					System.out.println(costo + ", passo " + passo);
					passo++;
				}
				System.out.println("istanza " +  istanza + " ultimata");
			}
			int passo = 1;
			for(int iterazione = 0; iterazione < 10; iterazione++) {
				Dato d = new Dato(costi[iterazione], Integer.toString(passo+1), "costo");
				dati.add(d);
				passo++;
			}
			Dataset dataset = new Dataset(dati);
			JSON.salvaDatiProblema("data/grafici/", "_costo_su_passo_" + macchine_tranquille +"_" + macchine_urgenti + ".json", dataset);
		}
	}


	private static void disegnaGrafico() throws JsonMappingException, JsonProcessingException {
		if (tempo_su_temperatura) {
			Grafico g = new Grafico("grafico", "tempo su temperatura", "temperatura (in migliaia)", "tempo (in millisecondi)", 
									"data/grafici/_tempo_su_temperatura_" + macchine_tranquille + "_"+ macchine_urgenti +".json");
			g.printPlot();
		}
		
		else if(costo_su_temperatura) {
			Grafico g = new Grafico("grafico", "costo su temperatura", "temperatura (in migliaia)", "costo",
									"data/grafici/_costo_su_temperatura_" + macchine_tranquille + "_"+ macchine_urgenti + ".json");
			g.printPlot();
		}
		
		else if(tempo_su_raffreddamento) {
			
		}

		else if(costo_su_raffreddamento) {
			Grafico g = new Grafico("grafico", "costo su raffreddamento", "raffreddamento", "costo", "data/grafici/2costo_su_raffreddamento.json");
			g.printPlot();
		}
		
		else if(costo_su_passo) {
			Grafico g = new Grafico("grafico", "costo su passo", "passo", "costo", 
									"data/grafici/_costo_su_passo_"+ macchine_tranquille + "_"+ macchine_urgenti + ".json");
			g.printPlot();
		}
		
	}

	
	private static void controllaSoluzione(Soluzione migliore_soluzione) throws PlanImpossibleException, JsonMappingException, JsonProcessingException {
	 if(migliore_soluzione.sfasamento() > Soluzione.massimoSfasamentoConsentito) {
        	throw new PlanImpossibleException("piano impossibile, troppo sbilanciamento di fase nel punto: " + migliore_soluzione.puntoMassimoSfasamento().toString());
        }
        
        if(migliore_soluzione.costoSoluzione() > Soluzione.massimaAltezzaConsentita) {
        	throw new PlanImpossibleException("piano impossibile, troppa richiesta di potenza nel punto: " + migliore_soluzione.puntoMaxAltezza().toString());
        }  
        
        if(saveSolution) {
        	SoluzioneSemplificata sol = migliore_soluzione.creaSoluzioneDaMettereNelJson();
        	JSON.salvaSoluzione("data/soluzioni/", "problema_con_" + macchine_tranquille +"_macchine_tranquille_" + macchine_urgenti + "_macchine_urgenti.json", sol);
        }
	}
	

	private static void visualizzaDatiIntermedi(Soluzione migliore_soluzione, double costo_migliore_soluzione) {
		visualizzaAltezzaSoluzione(migliore_soluzione, "ALTEZZE DOPO TRASLAZIONE");
        visualizzaSfasamentoSoluzione(migliore_soluzione, "SFASAMENTO DOPO TRASLAZIONE");
        migliore_soluzione.printSoluzione();
        System.out.println("costo dopo traslaz " + costo_migliore_soluzione);
        System.out.println("altezza max dopo traslaz " + migliore_soluzione.altezzaMassima());
        System.out.println("sfasamento max dopo traslaz " + migliore_soluzione.sfasamento());
        System.out.println(" \n" + "/+++++++++++++++++++++++++++" + "\n");
	}
	
	
	private static void visualizzaDatiFinali(Soluzione migliore_soluzione, double costo_migliore_soluzione) {
		visualizzaAltezzaSoluzione(migliore_soluzione, "ALTEZZE FINALI");
        visualizzaSfasamentoSoluzione(migliore_soluzione, "SFASAMENTO FINALE");
        migliore_soluzione.printSoluzione();
        System.out.println("costo finale " + costo_migliore_soluzione);
        System.out.println("altezza max finale " + migliore_soluzione.altezzaMassima());
        System.out.println("sfasamento max finale " + migliore_soluzione.sfasamento());
		
	}
	

	private static void visualizzaDatiIniziali(Soluzione soluzioneIniziale, double costoSoluzioneIniziale) {
		visualizzaAltezzaSoluzione(soluzioneIniziale, "ALTEZZE PRIMA");
        visualizzaSfasamentoSoluzione(soluzioneIniziale, "SFASAMENTO PRIMA");
        soluzioneIniziale.printSoluzione();
        System.out.println("costo iniziale " + costoSoluzioneIniziale);
        System.out.println("altezza max iniziale " + soluzioneIniziale.altezzaMassima());
        System.out.println("sfasamento max iniziale " + soluzioneIniziale.sfasamento());
        System.out.println(" \n" + "/////////////////////////" + "\n");
		
	}
	
	
	public static void visualizzaAltezzaSoluzione(Soluzione soluzione, String title) {
		JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PannelloAltezzaSoluzione solPan = new PannelloAltezzaSoluzione(soluzione);
        solPan.setPreferredSize(new Dimension(larghezzaFinestra, altezzaFinestra));
        frame.add(solPan);
        frame.pack();
        frame.setVisible(true);
	}
	
	
	public static void visualizzaSfasamentoSoluzione(Soluzione soluzione, String title) {
		JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PannelloSfasamentoSoluzione solPan = new PannelloSfasamentoSoluzione(soluzione);
        solPan.setPreferredSize(new Dimension(larghezzaFinestra, altezzaFinestra));
        frame.add(solPan);
        frame.pack();
        frame.setVisible(true);
	}
	
}




//grafici da fare: 
//tempo di esecuzione in funzione del raffreddamento (è lineare?)
//costo in funzione del raffreddamento	(da vedere col prof, non migliora all'aumentare della temperatura...)
//costo in funzione del passo nella generazione di nuove soluzioni (non sembra esserci una correlazione!)
//costo se prima facciamo la traslazione (migliora di molto)
//tempo in funzione del num di macchine






