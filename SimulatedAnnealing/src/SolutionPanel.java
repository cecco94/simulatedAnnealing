import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class SolutionPanel extends JPanel {

	public Solution soluzione;
	
	 @Override
     protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         drawRectangles(g);
     }

     private void drawRectangles(Graphics g) {
         g.setColor(Color.RED);
         g.fillRect(50, 50, 100, 100);

         g.setColor(Color.BLUE);
         g.fillRect(200, 50, 150, 100);

         g.setColor(Color.GREEN);
         g.fillRect(100, 200, 200, 150);
     }
 
}
