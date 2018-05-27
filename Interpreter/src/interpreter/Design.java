import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

class Design extends JComponent {

    //public List<Shape> shapes = new ArrayList<Shape>();
    HashMap<Shape, Color> shapes = new HashMap<>();
    Graphics2D g2d;


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        for (Map.Entry<Shape, Color> entry : shapes.entrySet()) {
            g2d.setColor(entry.getValue());
            g2d.draw(entry.getKey());
        }

        /*for (Shape s : shapes) {
            g2d.draw(s);
        }*/
    }

    public void addDot(int x, int y, String color) {
        shapes.put(new Line2D.Double(x, y, x, y), changeColor(color));
        repaint();
    }

    public void addRect(int xPos, int yPos, int width, int height, String color) {
        shapes.put(new Rectangle(xPos, yPos, width, height), changeColor(color));
        repaint();
    }

    public void addLine(int x1, int y1, int x2, int y2, String color) {
        shapes.put(new Line2D.Double(x1, y1, x2, y2), changeColor(color));
        repaint();
    }

    public void addCircle(int x, int y, int r, String color) {
        shapes.put(new Ellipse2D.Double(x, y, r, r), changeColor(color));
        repaint();
    }
    public void addTriangle(int x1, int y1, int x2, int y2, int x3, int y3, String color) {
    	shapes.put(new Line2D.Double(x1,y1,x2,y2), changeColor(color));
    	shapes.put(new Line2D.Double(x2,y2,x3,y3), changeColor(color));
    	shapes.put(new Line2D.Double(x3,y3,x1,y1), changeColor(color));
    	repaint();
    }

    public Color changeColor(String colorName) {
        Color color;
        try {
            color = (Color) Color.class.getField(colorName).get(null);

        } catch (Exception e) {
            System.out.println("Color not detected. Defaulting to black.");
            color = Color.BLACK;
        }
        return color;
    }
}
