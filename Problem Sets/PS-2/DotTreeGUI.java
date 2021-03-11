import java.awt.*;
import java.util.List;

import javax.swing.*;

/**
 * Driver for interacting with a quadtree:
 * inserting points, viewing the tree, and finding points near a mouse press
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, updated for dots
 * @author CBK, Fall 2016, generics, dots, extended testing
 */
public class DotTreeGUI extends DrawingGUI {
	private static final int width=800, height=600;		// size of the universe
	private static final int dotRadius = 10;				// to draw dot, so it's visible
	private static final Color[] rainbow = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};
			// to color different levels differently
	private static String drawMode = "dots";
	private PointQuadtree<Dot> highestTree;

	private PointQuadtree<Dot> tree = null;			// holds the dots
	private char mode = 'a';						// 'a': adding; 'q': querying with the mouse
	private int mouseX, mouseY;						// current mouse location, when querying
	private int mouseRadius = 10;					// circle around mouse location, for querying
	private boolean trackMouse = false;				// if true, then print out where the mouse is as it moves
	private List<Dot> found = null;					// who was found near mouse, when querying
	
	public DotTreeGUI() {
		super("dot tree", width, height);
	}

	public DotTreeGUI(String drawMode) {
		super("dot tree", width, height);
		DotTreeGUI.drawMode = drawMode;
	}

	/**
	 * DrawingGUI method, here keeping track of the location and redrawing to show it
	 */
	@Override
	public void handleMouseMotion(int x, int y) {
		if (mode == 'q') {
			mouseX = x; mouseY = y;
			repaint();
		}
		if (trackMouse) {
			System.out.println("@ ("+x+","+y+")");
		}
	}

	/**
	 * DrawingGUI method, here either adding a new point or querying near the mouse
	 */
	@Override
	public void handleMousePress(int x, int y) {
		if (mode == 'a') {
			// Add a new dot at the point
			// TODO: YOUR CODE HERE
			Dot dot = new Dot(x, y);
			if (tree == null){
				tree = new PointQuadtree<>(dot, 0, 0, width, height);
				highestTree = tree;
			}
			else {
				tree.insert(dot);
			}
		}
		else if (mode == 'q') {
			// Set "found" to what tree says is near the mouse press
			// TODO: YOUR CODE HERE
			found = tree.findInCircle(x, y, mouseRadius);
		}
		else {
			System.out.println("clicked at ("+x+","+y+")");
		}
		repaint();
	}

	/**
	 * DrawingGUI method, here toggling the mode between 'a' and 'q'
	 * and increasing/decreasing mouseRadius via +/-
	 */
	@Override
	public void handleKeyPress(char key) {
		if (key=='a' || key=='q') mode = key;
		else if (key=='+') {
			mouseRadius += 10;
		}
		else if (key=='-') {
			mouseRadius -= 10;
			if (mouseRadius < 0) mouseRadius=0;
		}
		else if (key=='m') {
			trackMouse = !trackMouse;
		}
		repaint();
	}
	
	/**
	 * DrawingGUI method, here drawing the quadtree
	 * and if in query mode, the mouse location and any found dots
	 */
	@Override
	public void draw(Graphics g) {
		if (tree != null) drawTree(g, tree, 0);
		if (mode == 'q') {
			g.setColor(Color.BLACK);
			g.drawOval(mouseX-mouseRadius, mouseY-mouseRadius, 2*mouseRadius, 2*mouseRadius);			
			if (found != null) {
				g.setColor(Color.BLACK);
				for (Dot d : found) {
					g.fillOval((int)d.getX()-dotRadius/2, (int)d.getY()-dotRadius/2, dotRadius, dotRadius);
				}
			}
		}
	}

	/**
	 * Draws the dot tree
	 * @param g		the graphics object for drawing
	 * @param tree	a dot tree (not necessarily root)
	 * @param level	how far down from the root qt is (0 for root, 1 for its children, etc.)
	 */
	public void drawTree(Graphics g, PointQuadtree<Dot> tree, int level) {
		// Set the color for this level
		g.setColor(rainbow[level % rainbow.length]);
		// Draw this node's dot and lines through it
		// TODO: YOUR CODE HERE

		// get x and y, draw current point
		int x = (int) tree.getPoint().getX();
		int y = (int) tree.getPoint().getY();

		// Conditional modes to draw squares or connections
		if (drawMode.equals("boxes")) {
			g.fillRect(tree.getX1(), tree.getY1(), tree.getX2() - tree.getX1(), tree.getY2() - tree.getY1());
			g.setColor(Color.BLACK);
			g.fillOval(x-dotRadius/2, y-dotRadius/2,dotRadius, dotRadius);

		}
		else if (drawMode.equals("connections")) {
			int[] parentCoordinates = new int[2];
			highestTree.findParent(tree.getPoint(), parentCoordinates);
			int x1 = parentCoordinates[0];	int y1 = parentCoordinates[1];
			int x2 = (int) tree.getPoint().getX();
			int y2 = (int) tree.getPoint().getY();

			// By default, when tree has no parent, the list returns [0, 0]
			// in that case, no need to draw connection
			if (x1 != 0 && y1 != 0) {
				g.drawLine(x1, y1, x2, y2);
			}
		}

		// draw Blob
		g.fillOval(x-dotRadius/2, y-dotRadius/2,dotRadius, dotRadius);

		// Increment the level
		level++;

		// Recurse with children
		for (int i=1; i<=4; i++) {
			if (tree.hasChild(i)) {
				PointQuadtree<Dot> child = tree.getChild(i);
				drawTree(g, child, level);
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new DotTreeGUI();
			}
		});
	}
}
