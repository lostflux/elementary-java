import java.awt.*;

import javax.swing.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Using a quadtree for collision detection
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, updated for blobs
 * @author CBK, Fall 2016, using generic PointQuadtree
 */
public class CollisionGUI extends DrawingGUI {
	private static final int width=800, height=600;	// size of the universe

	private static final int radius = 6;			// radius of blobs. Changing this automatically modifies the search parameters

	private List<Blob> blobs;						// all the blobs
	private List<Blob> colliders;					// the blobs who collided at this step
	private char blobType = 'b';					// what type of blob to create
	private char collisionHandler = 'c';			// when there's a collision, 'c'olor them, or 'd'estroy them
	private int delay = 100;						// timer control

	public CollisionGUI() {
		super("super collider", width, height);

		blobs = new ArrayList<Blob>();

		// Timer drives the animation.
		startTimer();
	}


	/**
	 * Adds a blob of the current blobType at the location
	 */
	private void add(int x, int y) {
		if (blobType=='b') {
			blobs.add(new Bouncer(x,y,width,height));
		}
		else if (blobType=='w') {
			blobs.add(new Wanderer(x,y));
		}
		else {
			System.err.println("Unknown blob type "+blobType);
		}
	}

	/**
	 * DrawingGUI method, here creating a new blob
	 */
	public void handleMousePress(int x, int y) {
		add(x,y);
		repaint();
	}

	/**
	 * DrawingGUI method
	 */
	public void handleKeyPress(char k) {
		if (k == 'f') { // faster
			if (delay>1) delay /= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 's') { // slower
			delay *= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 'r') { // add some new blobs at random positions
			for (int i=0; i<10; i++) {
				add((int)(width*Math.random()), (int)(height*Math.random()));
				repaint();
			}			
		}
		else if (k == 'c' || k == 'd' || k == ' ' || k == 'i') { // control how collisions are handled
			collisionHandler = k;
			System.out.println("collision: "+k);
		}
		else { // set the type for new blobs
			blobType = k;			
		}
	}

	/**
	 * DrawingGUI method, here drawing all the blobs and then re-drawing the colliders in red
	 */
	public void draw(Graphics g) {
		// TODO: YOUR CODE HERE
		// Ask all the blobs to draw themselves.
		// Set the color for this level
		g.setColor(Color.BLACK);
		// Draw this node's dot and lines through it
		// TODO: YOUR CODE HERE
		for (Blob blob : blobs) {
			g.setColor(Color.BLACK);
			int x = (int) blob.x;
			int y = (int) blob.y;
			g.fillOval(x, y, radius, radius);
		}

		// Ask the colliders to draw themselves in red.
		if (colliders != null) {
			g.setColor(Color.RED);
			for (Blob collider : colliders) {
				int x = (int) collider.x;
				int y = (int) collider.y;
				g.fillOval(x, y, radius, radius); //3
			}
		}
	}

	/**
	 * Sets colliders to include all blobs in contact with another blob
	 */
	private void findColliders() {
		// TODO: YOUR CODE HERE
		// Create the tree
		PointQuadtree<Blob> tree = new PointQuadtree<>(blobs.get(0), 0, 0, width, height);
		for (Blob blob : blobs) {
			tree.insert(blob);
		}
		// For each blob, see if anybody else collided with it

		colliders = new ArrayList<>();
		for (Blob blob: blobs) {

			int x = (int) blob.getX();
			int y = (int) blob.getY();
			List<Blob> collisions = tree.findInCircle(x, y, radius);
			// since blob at point will also be added into the list of blobs in circle,
			// We need to only save list if more than 1 blobs are detected in a collision
			if (collisions.size() > 1) {
				colliders.addAll(collisions); 	// dump colliding blobs in the list of all colliders
			}
		}
	}

	/**
	 * DrawingGUI method, here moving all the blobs and checking for collisions
	 */
	public void handleTimer() {
		// Ask all the blobs to move themselves.
		for (Blob blob : blobs) {
			blob.step();
		}
		// Check for collisions
		if (blobs.size() > 0) {
			findColliders();
			if (collisionHandler=='d') {
				blobs.removeAll(colliders);
				colliders = null;
			}
			if (collisionHandler == ' ') {
				freezeVelocities();
				collisionHandler = 'c';
			}
		}
		// Now update the drawing
		repaint();
	}

	public void freezeVelocities() {
		if (colliders != null) {
			for (Blob collider1 : colliders) {
				for (Blob collider2 : colliders) {
					if (Math.pow(collider1.x - collider2.x, 2) + Math.pow(collider1.y - collider2.y, 2) <= (radius * radius)) {
						collider1.dx = collider2.dx = collider1.dy = collider2.dy = 0;
					}

				}
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CollisionGUI();
			}
		});
	}
}
