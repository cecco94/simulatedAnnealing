package utils.visualization;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import progetto.TestClass;
import progetto.Punto;
import progetto.Rettangolo;
import progetto.Soluzione;

public class PannelloSfasamentoSoluzione extends JPanel{

	private static final long serialVersionUID = 1L;
	public Soluzione soluzione;

	
	public PannelloSfasamentoSoluzione(Soluzione sol) {
		super();
		soluzione = sol;
		setBackground(Color.black);
	}
	
	 @Override
     protected void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 Graphics2D g2 = (Graphics2D)g;
		 drawRectangles(g2);
		 drawSfasamentoFunction(g2);
		 drawAxes(g2);
 }

	
	private void drawAxes(Graphics2D g2) {
		g2.setColor(Color.white);
		int spessore = 5;
		for(int x = 0; x < TestClass.larghezzaFinestra; x += 10) {
			if (x == 480){
				spessore = 20;
			}
			else if(x % 60 == 0) {
				spessore = 10;
			}
			else {
				spessore = 5;
			}
			g2.drawLine(x, TestClass.altezzaFinestra - spessore, x, TestClass.altezzaFinestra + spessore);
		}
		
		g2.drawLine(TestClass.larghezzaFinestra - 5, 0, TestClass.larghezzaFinestra - 5, TestClass.altezzaFinestra);
		for(int y = 0; y < TestClass.altezzaFinestra; y += 10) {
			if(y % 40 == 0) {
				spessore = 10;
			}
			else {
				spessore = 5;
			}
			g2.drawLine(TestClass.larghezzaFinestra - spessore, y, TestClass.larghezzaFinestra + spessore, y);
		}
	}

	private void drawSfasamentoFunction(Graphics2D g2) {
	   	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g2.setColor(Color.red);
   
		for(int i = 0; i < soluzione.puntiDiInizioFineRettangoli.size() - 1; i++) {
	   
			Punto p = soluzione.puntiDiInizioFineRettangoli.get(i);
			Punto p2 = soluzione.puntiDiInizioFineRettangoli.get(i + 1);

			Rectangle2D.Double rect = new Rectangle2D.Double(p.x, TestClass.altezzaFinestra - p.sfasamentoNelPunto*40 - 2, p2.x - p.x, 2);  
			g2.fill(rect);
		}
		
		g2.setColor(Color.magenta);
		g2.drawLine(0, (int)(TestClass.altezzaFinestra - 40*Soluzione.massimoSfasamentoConsentito), 800, 
						(int)(TestClass.altezzaFinestra - 40*Soluzione.massimoSfasamentoConsentito));
		
	}

	
	private void drawRectangles(Graphics2D g2) {
    	 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
    	 
    	 for(int i = 0; i < soluzione.rettangoli.size(); i++) {
    		 Rettangolo r = soluzione.rettangoli.get(i);
    		 
    		 Rectangle2D.Double rect = new Rectangle2D.Double(r.margineSinistro, TestClass.altezzaFinestra - r.altezza*40, r.base, r.altezza*40);    		 
    		 if((r.fase) == 1) {
    			 g2.setColor(Color.GREEN);
    		 }
    		 else if((r.fase) == 2) {
    			 g2.setColor(Color.BLUE);
    		 }
    		 else if((r.fase) == 3) {
    			 g2.setColor(Color.orange);
    		 }
    		 else if((r.fase) == 4) {
    			 g2.setColor(Color.magenta);
    		 }
    		 
    		 g2.fill(rect);
    		 
    	 }
    	 
     }

}
