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
							grafico = false, datasetCreation = true, conTraslazione = false;
	
	//per decidere quali dati creare o lggere
	public static boolean tempo_su_temperatura = false, costo_su_temperatura = true, tempo_su_raffreddamento = false, 
							costo_su_raffreddamento = false, costo_su_passo = false;
	
	//per decidere la configurazione del problema
	public static int macchine_tranquille = 2, macchine_urgnti = 4;
	
	//perchè possono esserci più versioni della stessa configurazione
	public static int istanza = 3;
	
	
	public static void main(String[] args) throws RectImpossibleException, JsonMappingException, JsonProcessingException, PlanImpossibleException {
				
		if(newProblem) {			
			GeneratoreIstanze.generaIstanzaProblema(macchine_tranquille, macchine_urgnti);  
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
																"_macchine_tranquille_"+macchine_urgnti+"_macchine_urgenti.json");
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

	
	////////////////////////// METODI AUSILIARI ////////////////
	
	private static void creaNuoviDati() throws JsonMappingException, JsonProcessingException, RectImpossibleException {
		System.out.println("dataset creation...");
		
		Soluzione soluzioneInizialeMisurazione; 
		double costoSoluzioneInizialeMisurazione; 
		ArrayList<Dato> dati = new ArrayList<>();
		
		
		
		
		
		if(tempo_su_temperatura) {
			
			double[][] tempiImpiegati = new double[10][4];
			
			//crea 10 istanze del problema e le risolve con temperatura crescente
			for(int istanza = 0; istanza < 10; istanza++) {
				soluzioneInizialeMisurazione = GeneratoreIstanze.generaIstanzaProblemaSenzaSalvare(macchine_tranquille, macchine_urgnti);
				costoSoluzioneInizialeMisurazione = soluzioneInizialeMisurazione.costoSoluzione();
				
				int iterazione = 0;
				for(int temp = 10*1000; temp <= 10*1000*1000; temp*=10) {
					tempiImpiegati[istanza][iterazione] = AlgoritmoSimulatedAnnealing.misuraSimulatedAnnealing(soluzioneInizialeMisurazione, 
																							costoSoluzioneInizialeMisurazione, temp, 0.00001);
					iterazione++;
				}
			}
			
			for(int iterazione = 0; iterazione < 4; iterazione++) {
				double tempoMedioConQuellaTemperatura = 0;
				int temperatura = 10000;

				for(int istanza = 0; istanza < 10; istanza++) {
					tempoMedioConQuellaTemperatura += tempiImpiegati[istanza][iterazione];
				}
				tempoMedioConQuellaTemperatura /= 10;
				Dato d = new Dato(tempoMedioConQuellaTemperatura, Integer.toString(temperatura/1000), "tempo");
				dati.add(d);
				temperatura *= 10;
			}
			
			Dataset dataset = new Dataset(dati);
			JSON.salvaDatiProblema("data/grafici/", "_tempo_su_temperatura_" + macchine_tranquille +"_" + macchine_urgnti +".json", dataset);
			
//			double tempo = 0;
//			for(int temp = 10*1000; temp <= 10*1000*1000; temp*=10) {
//				//faccio una media su 10 problemi generati casualmente ma con lo stesso numero di macchine
//				for(int i = 0; i < 10 ; i++) {
//					soluzioneInizialeMisurazione = GeneratoreIstanze.generaIstanzaProblemaSenzaSalvare(macchine_tranquille, macchine_urgnti);
//					costoSoluzioneInizialeMisurazione = soluzioneInizialeMisurazione.costoSoluzione();
//					tempo += AlgoritmoSimulatedAnnealing.misuraSimulatedAnnealing(soluzioneInizialeMisurazione, costoSoluzioneInizialeMisurazione, temp, 0.00001);
//				}	
//				tempo /= 10;
//				System.out.println("tempo medio del problema con temperatura" + temp + " = " + tempo);
//				Dato d = new Dato(tempo, Integer.toString(temp/1000), "tempo");
//				dati.add(d);
//			}
//			Dataset dataset = new Dataset(dati);
//			JSON.salvaDatiProblema("data/grafici/", "_tempo_su_temperatura_" + macchine_tranquille +"_" + macchine_urgnti +".json", dataset);
		}
		
		
			
		
		
		
		
		
		else if (costo_su_temperatura) {
			double costo = 0;
			for(int temp = 10*1000; temp <= 900*1000*1000; temp*=10) {
				//faccio una media su 10 problemi generati casualmente ma con lo stesso numero di macchine
				for(int i = 0; i < 10 ; i++) {
					soluzioneInizialeMisurazione = GeneratoreIstanze.generaIstanzaProblemaSenzaSalvare(macchine_tranquille, macchine_urgnti);
					costoSoluzioneInizialeMisurazione = soluzioneInizialeMisurazione.costoSoluzione();
					costo += AlgoritmoSimulatedAnnealing.misuraSimulatedAnnealing(soluzioneInizialeMisurazione, costoSoluzioneInizialeMisurazione, temp, 0.00001);
				}
				costo /= 10;
				System.out.println("costo medio del problema con temperatura" + temp + " = " + costo);
				Dato d = new Dato(costo, Integer.toString(temp/1000), "costo");
				dati.add(d);
			}
			Dataset dataset = new Dataset(dati);
			JSON.salvaDatiProblema("data/grafici/", "_costo_su_temperatura_" +macchine_tranquille +"_" + macchine_urgnti + ".json", dataset);
		}
		
		else if(tempo_su_raffreddamento) {
			double tempo = 0;
			for(double raffredd = 0.1; raffredd > 0.000001; raffredd /= 10) {
				//faccio una media su 10 problemi generati casualmente ma con lo stesso numero di macchine
				for(int i = 0; i < 10 ; i++) {
					soluzioneInizialeMisurazione = GeneratoreIstanze.generaIstanzaProblemaSenzaSalvare(macchine_tranquille, macchine_urgnti);
					costoSoluzioneInizialeMisurazione = soluzioneInizialeMisurazione.costoSoluzione();
					tempo += AlgoritmoSimulatedAnnealing.misuraSimulatedAnnealing(soluzioneInizialeMisurazione, costoSoluzioneInizialeMisurazione, 100000000, raffredd);
					System.out.println("tempo del problema " + i + " = " + tempo);
				}
				tempo = tempo /= 10;
				Dato d = new Dato(tempo, Double.toString(raffredd), "tempo");
				dati.add(d);
			}
			Dataset dataset = new Dataset(dati);
			JSON.salvaDatiProblema("data/grafici/", "_tempo_su_raffreddamento" + macchine_tranquille +"_" + macchine_urgnti + ".json", dataset);
		}
		
		else if(costo_su_raffreddamento) {
			double costo = 0;
			for(double raffredd = 0.1; raffredd > 0.000001; raffredd /= 10) {
				//faccio una media su 10 problemi generati casualmente ma con lo stesso numero di macchine
				for(int i = 0; i < 10 ; i++) {
					soluzioneInizialeMisurazione = GeneratoreIstanze.generaIstanzaProblemaSenzaSalvare(macchine_tranquille, macchine_urgnti);
					costoSoluzioneInizialeMisurazione = soluzioneInizialeMisurazione.costoSoluzione();
					costo += AlgoritmoSimulatedAnnealing.misuraSimulatedAnnealing(soluzioneInizialeMisurazione, costoSoluzioneInizialeMisurazione, 100000000, raffredd);
					System.out.println("costo del problema " + i + " = " + costo);
				}
				costo /= 10;
				Dato d = new Dato(costo, Double.toString(raffredd), "costo");
				dati.add(d);
			}
			Dataset dataset = new Dataset(dati);
			JSON.salvaDatiProblema("data/grafici/", "_costo_su_raffreddamento" + macchine_tranquille +"_" + macchine_urgnti + ".json", dataset);
		}

		else if(costo_su_passo) {
			double costo = 0;
			for(int passo = 1; passo < 30; passo++) {
				for(int i = 0; i < 10 ; i++) {
					soluzioneInizialeMisurazione = GeneratoreIstanze.generaIstanzaProblemaSenzaSalvare(macchine_tranquille, macchine_urgnti);
					costoSoluzioneInizialeMisurazione = soluzioneInizialeMisurazione.costoSoluzione();
					Rettangolo.passoGenerazioneRandomica = passo;
					costo += AlgoritmoSimulatedAnnealing.misuraSimulatedAnnealing(soluzioneInizialeMisurazione, costoSoluzioneInizialeMisurazione, 100000000, 0.00001);
					System.out.println("costo del problema " + i + " = " + costo);
				}
				costo /= 10;
				Dato d = new Dato(costo, Integer.toString(passo), "costo");
				dati.add(d);
			}
			Dataset dataset = new Dataset(dati);
			JSON.salvaDatiProblema("data/grafici/", "_costo_su_passo" + macchine_tranquille +"_" + macchine_urgnti + ".json", dataset);
		}
	}


	private static void disegnaGrafico() throws JsonMappingException, JsonProcessingException {
		if (tempo_su_temperatura) {
			Grafico g = new Grafico("grafico", "tempo su temperatura", "temperatura (in migliaia)", "tempo (in millisecondi)", 
									"data/grafici/_tempo_su_temperatura_" + macchine_tranquille + "_"+ macchine_urgnti +".json");
			g.printPlot();
		}
		
		else if(costo_su_temperatura) {
			Grafico g = new Grafico("grafico", "costo su temperatura", "temperatura (in migliaia)", "costo", "data/grafici/_costo_su_temperatura.json");
			g.printPlot();
		}
		
		else if(tempo_su_raffreddamento) {
			
		}

		else if(costo_su_raffreddamento) {
			Grafico g = new Grafico("grafico", "costo su raffreddamento", "raffreddamento", "costo", "data/grafici/2costo_su_raffreddamento.json");
			g.printPlot();
		}
		
		else if(costo_su_passo) {
			Grafico g = new Grafico("grafico", "costo su passo", "passo", "costo", "data/grafici/2costo_su_passo.json");
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
        	JSON.salvaSoluzione("data/soluzioni/", "problema_con_" + macchine_tranquille +"_macchine_tranquille_" + macchine_urgnti + "_macchine_urgenti.json", sol);
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
//costo in funzione del raffreddamento	(a tratti scende molto, a tratti è costante)
//costo in funzione del passo nella generazione di nuove soluzioni
//costo de prima facciamo la traslazione


//genera casualmente 10 istanze del problema con stesso numero di macchine e le risolve, poi fa la media del tempo/costo





