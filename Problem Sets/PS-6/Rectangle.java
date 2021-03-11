import java.awt.Color;
import java.awt.Graphics;

/**
 * A rectangle-shaped Shape
 * Defined by an upper-left corner (x1,y1) and a lower-right corner (x2,y2)
 * with x1<=x2 and y1<=y2
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, updated Fall 2016
 * @author Amittai Joel Wekesa
 */
public class Rectangle implements Shape {
	// TODO: YOUR CODE HERE
	private int x1, y1, x2, y2;
	private Color color;

	public Rectangle(int x1, int y1, int x2, int y2, Color color) {
		this.x1 = Math.min(x1, x2);
		this.y1 = Math.min(y1, y2);
		this.x2 = Math.max(x1, x2);
		this.y2 = Math.max(y1, y2);
		this.color = color;
	}
	public Rectangle(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.color = Color.BLACK; // Default Color = BLACK
	}

	public void moveBy(int dx, int dy) {
		x1 += dx;
		y1 += dy;
		x2 += dx;
		y2 += dy;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setColor(int rgb) {
		color = new Color(rgb);
	}

	public boolean contains(int x, int y) {
		boolean x_check, y_check;
		// Check if x is in range
		if (x1 < x2) {
			x_check = x1 < x && x2 > x;
		}
		else {
			x_check = x1 > x && x2 < x;
		}
		// Check if y is in range
		if (y1 < y2) {
			y_check = y1 < y && y2 > y;
		}
		else {
			y_check = y1 > y && y2 < y;
		}
		// Return check of x and y
		return x_check && y_check;
	}

	public void draw(Graphics g) {
		// Draw rectangle of this color
		g.setColor(color);
		g.fillRect(x1, y1, x2 - x1, y2 - y1);
	}

	public String toString() {
		return "rectangle "+x1+" "+y1+" "+x2+" "+y2+" "+color.getRGB();
	}
}
