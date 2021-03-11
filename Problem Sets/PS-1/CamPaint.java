import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Webcam-based drawing 
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Spring 2015 (based on a different webcam app from previous terms)
 */
public class CamPaint extends Webcam {
	private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RegionFinder<?> finder;			// handles the finding
	private Color targetColor;          	// color of regions of interest (set by mouse press)
	private final Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece
	private boolean brushDown = true;		// boolean variable to track whether brush is up or down


	/**
	 * Initializes the region finder and the drawing
	 */
	public CamPaint() {
//		super();
		finder = new RegionFinder<>(image);
		finder.setMaxColorDiff(45);	// set maximum color difference for this RegionFinder instance.
		clearPainting();
	}

	/**
	 * Resets the painting to a blank image
	 */
	protected void clearPainting() {
		painting = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * DrawingGUI method, here drawing one of live webcam, recolored image, or painting, 
	 * depending on display variable ('w', 'r', or 'p')
	 */
	@Override
	public void draw(Graphics g) {
		// TODO: YOUR CODE HERE
		if (displayMode == 'w') {
			g.drawImage(image, 0, 0, null);
		}
		else if (displayMode == 'r') {
			// recolor the current frame
			finder.recolorImage();

			// get recoloredImage from the RegionFinder instance and draw it
			BufferedImage recoloredImage = finder.getRecoloredImage();
			g.drawImage(recoloredImage, 0, 0, null);
		}
		else if (displayMode == 'p') {
			g.drawImage(painting, 0, 0, null);
		}
	}

	/**
	 * Webcam method, here finding regions and updating painting.
	 */
	@Override
	public void processImage() {
		// TODO: YOUR CODE HERE

		// If the painting image is not initialized:
		// Initialize it to the same size as the webcam
		if (painting == null){
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			painting = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		}

		// Next, make sure targetColor and paintColor are not null
		if (targetColor != null && paintColor != null) {

			// Create a new RegionFinder instance with the feed from Webcam
			finder = new RegionFinder<>(image);
			finder.findRegions(targetColor);				// check the webcam feed for regions of the target color
			ArrayList<?> paintBrush = finder.largestRegion();	// set the paintBrush to the largest identified region


			// make sure the paint brush is down
			if (brushDown) {
				// For every abstract object in the paintBrush Array
				for (Object object : paintBrush){

					// if the object is a Point()
					if (object instanceof Point){

						// Convert the object to a Point()
						Point point = (Point) object;

						// Get the x and y coordinates of the Point
						int xCoordinate = (int) point.getX();
						int yCoordinate = (int) point.getY();

						// make sure the point is within frame of the image
						if (xCoordinate <= painting.getWidth() && yCoordinate <= painting.getHeight()){
							// set the corresponding pixel in te painting to the paintColor
							painting.setRGB(xCoordinate, yCoordinate, paintColor.getRGB());
						}

					}// end inner IF statement for point instances
				}// end FOR loop over objects (potential points) in paintBrush array

			} // end of IF statement for brush down
		}// end outer IF statement
	}// end method

	/**
	 * Overrides the DrawingGUI method to set targetColor.
	 */
	@Override
	public void handleMousePress(int x, int y) {
		if (image != null) { // to be safe, make sure webcam is grabbing an image
			// TODO: YOUR CODE HERE
			targetColor = new Color(image.getRGB(x, y));
			clearPainting();
		}
	}

	/**
	 * DrawingGUI method, here doing various drawing commands
	 */
	@Override
	public void handleKeyPress(char k) {
		if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
			displayMode = k;
		}
		else if (k == 'c') { // clear
			clearPainting();
		}
		else if (k == 'o') { // save the recolored image
			saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
		}
		else if (k == 's') { // save the painting
			saveImage(painting, "pictures/painting.png", "png");
		}
		else if (k == ' ') { // pause or resume painting
			brushDown = !brushDown;
		}
		else {
			System.out.println("unexpected key "+k);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CamPaint();
			}
		});
	}
}
