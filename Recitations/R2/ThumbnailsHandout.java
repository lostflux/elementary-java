import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

/**
 * Thumbnail display of a set of slides.
 * Programming drill, week 2.
 * There are some bugs in the scaffold -- fix them up, and complete the code in order to display a set of thumbnail images (extract them from dart.zip and add them to your pictures directory)
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016, based on similar codes from previous terms
 */
public class ThumbnailsHandout extends DrawingGUI {
	private static int trows = 3;
	private static int tcols = 3; 	// setup: number of thumbnails per row and column
	private static int thumbWidth = 200, thumbHeight = 150; 			// setup: scaled size of thumbnails
	private final List<BufferedImage> thumbs;		// thumbnail images to display

	public ThumbnailsHandout(ArrayList<BufferedImage> images) {
		initWindow(thumbWidth*tcols, thumbHeight*trows);

		thumbs = new ArrayList<>();

		// Create the thumbnails
		for (int i=0; i<trows*tcols; i++) {
			thumbs.add(thumbnailify(images.get(i)));
		}
	}

	/**
	 * Crude scaling down of an image to thumbnail size
	 * @param image		image to scale
	 * @return			scaled image
	 */
	private static BufferedImage thumbnailify(BufferedImage image) {
		BufferedImage result = new BufferedImage(thumbWidth, thumbHeight, image.getType());

		// Set each pixel (x,y) in result from the value at (x * w/w', y * h/h') in image
		// where w and h are of image and w' and h' are of result
		// TODO: Your code here
		for (int i=0; i < thumbWidth; i++) {
			for (int j=0; j < thumbHeight; j++) {
				result.setRGB(i, j, image.getRGB(i * image.getWidth()/thumbWidth, j * image.getHeight()/thumbHeight));
			}
		}
		return result;

	}

	/**
	 * Draws the grid of thumbnail images.
	 */
	public void draw(Graphics g) {
		for (int i=0; i<trows; i++) {
			for (int j=0; j<tcols; j++) {
				g.drawImage(thumbs.get(i*tcols+j), j*thumbWidth, i*thumbHeight, null);
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			// Read the images, named dart0.jpg, dart1.jpg, ..., and store in list.
			ArrayList<BufferedImage> images = createImages(trows, tcols);
			// Fire off the thumbnail viewer.
			new ThumbnailsHandout(images);
		});
	}

	static ArrayList<BufferedImage> createImages(int trows, int tcols) {
		ThumbnailsHandout.trows = trows;
		ThumbnailsHandout.tcols = tcols;
		ArrayList<BufferedImage> images = new ArrayList<>();
		for (int i = 0; i< trows; i++) {
			for (int j = 0; j< tcols; j++) {
				images.add(loadImage("pictures/dart"+(i* tcols +j)+".jpg"));
			}
		}
		return images;
	}
}
