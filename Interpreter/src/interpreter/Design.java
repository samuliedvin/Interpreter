import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

// tällä pitäisi luoda komponentit joita lisätään jframeen.
class Design extends JComponent {	  
	 
		public List<Shape> shapes = new ArrayList<Shape>();
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(2));
			for(Shape s : shapes){
				g2d.draw(s);
			}
		}

		public void addDot(int x,int y) {
			shapes.add(new Line2D.Double(x,y,x,y));
			repaint();
		}
		
		public void addRect(int xPos, int yPos, int width, int height) {
            shapes.add(new Rectangle(xPos,yPos,width,height));
            repaint();
        }
     
		public void addLine(int x1, int y1, int x2, int y2) {
			shapes.add(new Line2D.Double(x1, y1, x2, y2));
			repaint();
		}
     
		public void addCircle(int x, int y, int r) {
			shapes.add(new Ellipse2D.Double(x, y, r, r));
			repaint();
		}
 }