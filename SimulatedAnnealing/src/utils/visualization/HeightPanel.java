package utils.visualization;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import main.LibraryInterface;
import progetto.Point;
import progetto.Rectangle;
import progetto.Solution;


public class HeightPanel extends JPanel {

	static final long serialVersionUID = 1L;
	Solution solution;
	int xAxisHeight = 3*LibraryInterface.windowHeight/4;
	
	
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
		for(int x = 0; x < LibraryInterface.windowWidth; x += 10) {
			if (x == 480){
				thickness = 30;
			}
			else if(x % 60 == 0) {
				thickness = 15;
			}
			else {
				thickness = 7;
			}
			g2.drawLine(x, LibraryInterface.windowHeight - thickness, x, LibraryInterface.windowHeight + thickness);
		}
		
		g2.drawLine(0, xAxisHeight, LibraryInterface.windowWidth, xAxisHeight);
		
		g2.drawLine(LibraryInterface.windowWidth - 5, 0, LibraryInterface.windowWidth - 5, xAxisHeight);
		
		for(int y = 0; y < xAxisHeight; y += 10) {
			if(y % 40 == 0) {
				thickness = 10;
			}
			else {
				thickness = 5;
			}
			g2.drawLine(LibraryInterface.windowWidth - thickness, y, LibraryInterface.windowWidth + thickness, y);
		}
	}

	
	private void drawRectangles(Graphics2D g2) {
    	 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
    	 
    	 for( int i = 0; i < solution.getRectanglesList().size(); i++) { 
    		 Rectangle r = solution.getRectanglesList().get(i);
    		 
    		 Rectangle2D.Double rect = new Rectangle2D.Double(r.getStartMinute(), 
    				 											xAxisHeight - r.getHeight()*40, 
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
       		 g2.setColor(Color.black);
    		 g2.draw(rect);
    		 
    		 String id = String.valueOf(r.getIdNumber());
    		 g2.drawString(id, r.getStartMinute() + r.getBase()/2, (int)(xAxisHeight - r.getHeight()*40 - 5));
    		 
    		 //disegna tempo a disposizione rect
    		 g2.drawLine(r.getMinStartMinute(), 20 + xAxisHeight + i*20, r.getMaxStopMinute(), 20 + xAxisHeight + i*20);
    		 g2.drawString(id, r.getMaxStopMinute(), 20 + xAxisHeight + i*20);
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
	   														xAxisHeight - p.getSumHeightInThisPoint()*40 - 2, 
	   														p2.getMinute() - p.getMinute(), 
	   														2);  
	   		g2.fill(rect);
	   	 }
   	 
	   	g2.drawLine(0, 
	   				(int)(xAxisHeight - solution.maxHigh()*40), 
	   				LibraryInterface.windowWidth, 
	   				(int)(xAxisHeight - solution.maxHigh()*40));
	}
 
}
