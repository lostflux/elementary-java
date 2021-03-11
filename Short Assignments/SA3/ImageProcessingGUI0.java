import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * A class demonstrating manipulation of image pixels.
 * Version 0: just the core definition
 * Load an image and display it
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 */
public class ImageProcessingGUI0 extends DrawingGUI {
	private final ImageProcessor0 proc;		// handles the image processing

	private char action = 'd';
	private boolean brush = false;		// variable to track whether brush is down

	/**
	 * Creates the GUI for the image processor, with the window scaled to the to-process image's size
	 */
	public ImageProcessingGUI0(ImageProcessor0 proc) {
		super("Image processing", proc.getImage().getWidth(), proc.getImage().getHeight());
		this.proc = proc;
	}

	/**
	 * DrawingGUI method, here showing the current image
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(proc.getImage(), 0, 0, null);
		
	}

	/**
	 * DrawingGUI method, here dispatching on image processing operations
	 */
	@Override
	public void handleKeyPress(char op) {
		System.out.println("Handling key '"+op+"'");
		if (op=='s') { // save a snapshot
			saveImage(proc.getImage(), "pictures/snapshot.png", "png");
		}
		else if (op == 'm'){	// switch to monochrome mode
			action = op;
		}
		else if (op == ' '){	// flip the status of the brush
			brush = !brush;
		}
		else {
			action = op;
			System.err.println("Unknown operation");
		}

		repaint(); // Re-draw, since image has changed
	}

	public void handleMousePress(int x, int y){
		if (brush && action == 'm') {        // if the brush is down
			proc.monochrome(x, y);
		}
		else if (!brush) {
			System.err.println("Sorry, the brush is not down. Press space-bar to activate");
		}
		else {
			System.err.println("Not in monochrome mode. Press 'm' to activate");
		}
		repaint();
	}

	@Override
	public void handleMouseMotion(int x, int y) {
		if (brush && action == 'm') {        // if the brush is down
			proc.monochrome(x, y);
		}
		else if (!brush) {
			System.err.println("Sorry, the brush is not down. Press space-bar to activate");
		}
		else {
			System.err.println("Not in monochrome mode. Press 'm' to activate");
		}

		repaint();
	}

	public static void main(String[] args) {
		final String filename = "pictures/baker.jpg";		// name of image file inside CS10 project
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Load the image to process
				// BufferedImage baker = loadImage("pictures/baker.jpg");
				BufferedImage image = loadImage(filename);
				// Create a new processor, and a GUI to handle it
				new ImageProcessingGUI0(new ImageProcessor0(image));
			}
		});
	}
}
