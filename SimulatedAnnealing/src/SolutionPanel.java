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
     }

     private void drawRectangles(Graphics g) {
    	 Graphics2D g2 = (Graphics2D)g;
    	 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
    	 int color = 0;
    	 
    	 for(int i = 0; i < soluzione.rettangoli.size(); i++) {
    		 Rettangolo r = soluzione.rettangoli.get(i);
    		 
    		 Rectangle2D.Double rect = new Rectangle2D.Double(r.margine_sinistro, 400 - r.altezza*40, r.base, r.altezza*40);    		 
    		 if(color == 0) {
    			 g2.setColor(Color.RED);
    		 }
    		 else if(color == 1) {
    			 g2.setColor(Color.GREEN);
    		 }
    		 else if(color == 2) {
    			 g2.setColor(Color.BLUE);
    		 }
    		 else if(color == 3) {
    			 g2.setColor(Color.magenta);
    		 }
    		 else if(color == 4) {
    			 g2.setColor(Color.LIGHT_GRAY);
    		 }
    		 else if(color == 5) {
    			 g2.setColor(Color.ORANGE);
    		 }
    		 else if(color == 6) {
    			 g2.setColor(Color.PINK);
    		 }
    		 color++;
    		 if(color >= 6) {
    			 color = 0;
    		 }
    		 
    		 g2.fill(rect);
    		 
    	 }
    	 
//    	 g2.setColor(Color.RED);
//    	 g2.fillRect(0, 400 - 10, 200, 10);
//    	 
//    	 g2.setColor(Color.GREEN);
//    	 g2.fillRect(100, 400 - 20, 200, 20);

//         g.setColor(Color.RED);
//         g.fillRect(50, 50, 100, 100);
//
//         g.setColor(Color.BLUE);
//         g.fillRect(200, 50, 150, 100);
//
//         g.setColor(Color.GREEN);
//         g.fillRect(100, 200, 200, 150);
     }
 
}
