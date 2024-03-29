import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Random;

/**
 * Painting random colors with wanderers
 * Template for SA-2, Dartmouth CS 10, Spring 2016
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * Modified by Amittai Wekesa to use WanderingPixels instead of Blobs, Winter 2021
 */
public class Pollock extends DrawingGUI {
	private static final int width = 800, height = 600; // setup: window size
	private static final int numBlobs = 20000;			// setup: how many blobs
	private static final int numToMove = 5000;			// setup: how many blobs to animate each frame

	private final BufferedImage result;						// the picture being painted
	private ArrayList<Blob> blobs;						// the blobs representing the picture
	private final ArrayList<WanderingPixel> wanderingPixels;
	
	public Pollock() {
		super("Pollock", width, height);

		result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		// Create a bunch of random blobs.
		// blobs = new ArrayList<Blob>();
		wanderingPixels = new ArrayList<WanderingPixel>();
		for (int i=0; i<numBlobs; i++) {
			int x = (int)(width*Math.random());
			int y = (int)(height*Math.random());
			// Create a blob with a random color
			// TODO: YOUR CODE HERE
			Random random = new Random();								// Using java.util.Random
			int v = random.nextInt(16777216);
			Color color = new Color(v);
			wanderingPixels.add(new WanderingPixel(x, y, 2, color));
		}

		// Timer drives the animation.
		startTimer();
	}

	/**
	 * DrawingGUI method, here just drawing all the blobs
	 */
	@Override
	public void draw(Graphics g) {
		g.drawImage(result,0, 0, null);
		for (WanderingPixel wanderingPixel : wanderingPixels) {
			wanderingPixel.draw(g);
		}
	}

	/**
	 * DrawingGUI method, here moving some of the blobs
	 */
	@Override
	public void handleTimer() {
		for (int b = 0; b < numToMove; b++) {
			// Pick a random wanderingPixel, leave a trail where it is, and ask it to move.
			WanderingPixel wanderingPixel = wanderingPixels.get((int)(Math.random()*wanderingPixels.size()));
			int x = (int)wanderingPixel.getX(), y = (int)wanderingPixel.getY();
			// Careful to stay within the image
			if (x>=0 && x<width && y>=0 && y<height) {
				// Leave a trail of the wanderingPixel's color
				// TODO: YOUR CODE HERE
				Color color = wanderingPixel.getColor();
				result.setRGB(x, y, color.getRGB());
			}
			wanderingPixel.step();
		}
		// Now update the drawing
		repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Pollock();
			}
		});
	}
}
