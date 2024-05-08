import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class SolutionPanel extends JPanel {

	public Solution soluzione;
	
	public SolutionPanel(Solution sol) {
		super();
		soluzione = sol;
	}
	
	 @Override
     protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         drawRectangles(g);
         drawCostFunction(g);
     }


	private void drawRectangles(Graphics g) {
    	 Graphics2D g2 = (Graphics2D)g;
    	 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
    	 
    	 
    	 for(int i = 0; i < soluzione.rettangoli.size(); i++) {
    		 Rettangolo r = soluzione.rettangoli.get(i);
    		 
    		 Rectangle2D.Double rect = new Rectangle2D.Double(r.margine_sinistro, 400 - r.altezza*40, r.base, r.altezza*40);    		 
    		 if((r.identificativo&7) == 0) {
    			 g2.setColor(Color.RED);
    		 }
    		 else if((r.identificativo&7) == 1) {
    			 g2.setColor(Color.GREEN);
    		 }
    		 else if((r.identificativo&7) == 2) {
    			 g2.setColor(Color.BLUE);
    		 }
    		 else if((r.identificativo&7) == 3) {
    			 g2.setColor(Color.magenta);
    		 }
    		 else if((r.identificativo&7) == 4) {
    			 g2.setColor(Color.LIGHT_GRAY);
    		 }
    		 else if((r.identificativo&7) == 5) {
    			 g2.setColor(Color.ORANGE);
    		 }
    		 else if((r.identificativo&7) == 6) {
    			 g2.setColor(Color.PINK);
    		 }
    		 
    		 g2.fill(rect);
    		 
    	 }
    	 
     }
	
	
    private void drawCostFunction(Graphics g) {
    	 
   	 Graphics2D g2 = (Graphics2D)g;
   	 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
   	 g2.setColor(Color.black);
   	 
   	 double h = 0;
   	 
   	 for(int i = 0; i < soluzione.punti_di_inizio_fine.size() - 1; i++) {
   		Punto p = soluzione.punti_di_inizio_fine.get(i);
   		Punto p2 = soluzione.punti_di_inizio_fine.get(i + 1);
   		
   		if(p.punto_di_inizio)
   			h += p.altezza_rect;
   		else
   			h -= p.altezza_rect;
   		
   		//g2.drawLine(p.x, (int)(400 - p.altezza_rect*40), p2.x - p.x, (int)(400 - p.altezza_rect*40));
   		Rectangle2D.Double rect = new Rectangle2D.Double(p.x, 400 - h*40, p2.x - p.x, 2);  
   		g2.fill(rect);
   	 }
	}
 
}
