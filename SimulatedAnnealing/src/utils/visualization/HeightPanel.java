package utils.visualization;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import progetto.TestClass;
import progetto.Point;
import progetto.Rectangle;
import progetto.Solution;


public class HeightPanel extends JPanel {

	static final long serialVersionUID = 1L;
	Solution solution;
	
	
	public HeightPanel(Solution sol) {
		super();
		solution = sol;
	}
	
	 @Override
     protected void paintComponent(Graphics g) {
		 super.paintComponent(g);
    	 Graphics2D g2 = (Graphics2D)g;
         drawRectangles(g2);
         drawHigthFunction(g2);
         drawAxes(g2);
     }


	private void drawAxes(Graphics2D g2) {
		g2.setColor(Color.black);
		int thickness = 5;
		for(int x = 0; x < TestClass.windowWidth; x += 10) {
			if (x == 480){
				thickness = 20;
			}
			else if(x % 60 == 0) {
				thickness = 10;
			}
			else {
				thickness = 5;
			}
			g2.drawLine(x, TestClass.windowHeight - thickness, x, TestClass.windowHeight + thickness);
		}
		
		g2.drawLine(TestClass.windowWidth - 5, 0, TestClass.windowWidth - 5, TestClass.windowHeight);
		for(int y = 0; y < TestClass.windowHeight; y += 10) {
			if(y % 40 == 0) {
				thickness = 10;
			}
			else {
				thickness = 5;
			}
			g2.drawLine(TestClass.windowWidth - thickness, y, TestClass.windowWidth + thickness, y);
		}
	}

	
	private void drawRectangles(Graphics2D g2) {
    	 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
    	 
    	 for( Rectangle r : solution.getRectanglesList()) {    		 
    		 Rectangle2D.Double rect = new Rectangle2D.Double(r.getStartMinute(), 
    				 											TestClass.windowHeight - r.getHeight()*40, 
    				 											r.getBase(), 
    				 											r.getHeight()*40);    		 
    		 if( r.getPhase() == 1 ) {
    			 g2.setColor(Color.GREEN);
    		 }
    		 else if( r.getPhase() == 2 ) {
    			 g2.setColor(Color.BLUE);
    		 }
    		 else if( r.getPhase() == 3 ) {
    			 g2.setColor(Color.orange);
    		 }
    		 else if( r.getPhase() == 0 ) {
    			 g2.setColor(Color.magenta);
    		 }
    		 
    		 g2.fill(rect);
    	 }
    	 
     }
	
	
    private void drawHigthFunction(Graphics2D g2) { 	 
	   	 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	   	 g2.setColor(Color.black);
	   	 
	   	 //double h = 0;	   	 
	   	 for(int i = 0; i < solution.getEndStartPontsList().size() - 1; i++) {
	   		 
	   		Point p = solution.getEndStartPontsList().get(i);
	   		Point p2 = solution.getEndStartPontsList().get(i + 1);   
	   		
	   		Rectangle2D.Double rect = new Rectangle2D.Double(p.getMinute(), 
	   														TestClass.windowHeight - p.getSumHeightInThisPoint()*40 - 2, 
	   														p2.getMinute() - p.getMinute(), 
	   														2);  
	   		g2.fill(rect);
	   	 }
   	 
	   	g2.drawLine(0, 
	   				(int)(TestClass.windowHeight - solution.maxHigh()*40), 
	   				TestClass.windowWidth, 
	   				(int)(TestClass.windowHeight - solution.maxHigh()*40));
	}
 
}
