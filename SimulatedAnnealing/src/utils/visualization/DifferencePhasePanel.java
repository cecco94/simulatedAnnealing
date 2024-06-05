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

public class DifferencePhasePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	public Solution solution;

	
	public DifferencePhasePanel(Solution sol) {
		super();
		solution = sol;
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
		int thickness = 5;
		for( int x = 0; x < TestClass.windowWidth; x += 10 ) {
			if ( x == 480 ){
				thickness = 20;
			}
			else if( x % 60 == 0 ) {
				thickness = 10;
			}
			else {
				thickness = 5;
			}
			g2.drawLine(x, TestClass.windowHeight - thickness, x, TestClass.windowHeight + thickness);
		}
		
		g2.drawLine(TestClass.windowWidth - 5, 0, TestClass.windowWidth - 5, TestClass.windowHeight);
		for( int y = 0; y < TestClass.windowHeight; y += 10 ) {
			if( y % 40 == 0 ) {
				thickness = 10;
			}
			else {
				thickness = 5;
			}
			g2.drawLine(TestClass.windowWidth - thickness, y, TestClass.windowWidth + thickness, y);
		}
		
		//linea dello sfasamento massimo
		g2.setColor(Color.magenta);
		g2.drawLine(0, (int)(TestClass.windowHeight - 40*solution.getMaxDifferenceBetweenPhases()), 800, 
						(int)(TestClass.windowHeight - 40*solution.getMaxDifferenceBetweenPhases()));
	}

	private void drawSfasamentoFunction(Graphics2D g2) {
	   	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g2.setColor(Color.red);
   
		for( int i = 0; i < solution.getEndStartPontsList().size() - 1; i++ ) {
	   
			Point p = solution.getEndStartPontsList().get(i);
			Point p2 = solution.getEndStartPontsList().get(i + 1);

			Rectangle2D.Double rect = new Rectangle2D.Double(p.getMinute(), 
															TestClass.windowHeight - p.getPhaseDifferenceInThisPoint()*40 - 2,
															p2.getMinute() - p.getMinute(), 
															2);  
			g2.fill(rect);
		}		
	}

	
	private void drawRectangles(Graphics2D g2) {
    	 g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
    	 
    	 for( int i = 0; i < solution.getRectanglesList().size(); i++ ) {
    		 Rectangle r = solution.getRectanglesList().get(i);    		 
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

}
