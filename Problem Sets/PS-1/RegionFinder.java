import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 * @author Amittai J. Wekesa, Dartmouth CS 10, Winter 2021
 */
public class RegionFinder<E> {
	private static int maxColorDiff = 21;				// how similar a pixel color must be to the target color, to belong to a region
															// suitable value for maxColorDiff depends on your implementation of colorMatch() and how much difference in color you want to allow
	private static final int minRegion = 20; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> allRegions;			// a region is a list of points
															// so the identified regions are in a list of lists of points

	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;		
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Method to modify the maximum allowed maximum color difference
	 * @param maxColorDiff	--> integer value of the maximum color difference
	 */
	public void setMaxColorDiff(int maxColorDiff) {
		RegionFinder.maxColorDiff = maxColorDiff;
	}

	/**
	 * Sets regions to the flood fill regions in the image, similar enough to the targetColor.
	 */
	public void findRegions(Color targetColor) {
		// TODO: YOUR CODE HERE

		// for ease, get dimensions of image and save them to two variables
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// create "visited" image to be used to track visited status of different points
		BufferedImage visited = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

		allRegions = new ArrayList<>();					// initialize ArrayList to hold regions
		for (int x = 0; x<imageWidth; x++) {			// Loop over all points in image
			for (int y = 0; y<imageHeight; y++) {

				// if point is unvisited and the correct color:
				if ((visited.getRGB(x, y) == 0) && (colorMatch(targetColor, new Color(image.getRGB(x, y))))){
					ArrayList<Point> currentRegion = new ArrayList<>();		// start new region
					ArrayList<Point> currentQueue = new ArrayList<>();		// start new queue of points to visit
					currentQueue.add(new Point(x, y));						// add current point to queue

					// while currentQueue ia not empty
					while (!currentQueue.isEmpty()){

						// get point to visit, remove it from list
						Point nextPoint = currentQueue.remove(0);
						int nextX = (int) nextPoint.getX();
						int nextY = (int) nextPoint.getY();

						// check if the current point is unvisited
						if (visited.getRGB(nextX, nextY) == 0) {
							currentRegion.add(nextPoint);			// add current point to the current region
							visited.setRGB(nextX, nextY, 1);	// mark the current point as visited

							// loop over all neighbor points to the current point
							for (int i = nextX - 1; i <= nextX + 1; i++) {
								for (int j = nextY - 1; j <= nextY + 1; j++) {

									// check if neighbor point is in range of the image
									if (i>0 && i<imageWidth && j>0 && j<imageHeight) {

										// check if neighbor point has been visited before
										if (visited.getRGB(i, j)==0){

											// check if color of neighbor point in the image matches the target color
											if (colorMatch(targetColor, new Color(image.getRGB(i, j)))){

												// queue the neighbor point to be visited
												currentQueue.add(new Point(i, j));
											}// end if block to check for matching colors
										}// end if block to check for unvisited points
									}
								}
							}// end inner for loops to check neighbors
						}
					}// end while loop over the current queue --> only exit when queue is empty

					// after exiting while-loop, check if the generated region is big enough to save
					// IF region is big enough to save --> save it
					if (currentRegion.size() > minRegion){
						allRegions.add(currentRegion);
					}
				}
			}//end outer for loops to check all pixels in image
		}
	}// end method findRegions()


	/**
	 * Tests whether the two colors are "similar enough"
	 * (your definition, subject to the maxColorDiff threshold, which you can vary)
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		// TODO: YOUR CODE HERE
		// I built a helper "compare()" function that takes in two colors and a "channel"
		// and automatically compares the appropriate channel for the two colors.

		// return a check of each of the different color channels -- red, green, and blue.
		return compare(c1, c2, 'r') && compare(c1, c2, 'g') && compare(c1, c2, 'b');
	}// end method colorMatch()

	/**
	 * Compares a specified channel of two colors
	 * @param color1	--> first color to compare: must be Color() object
	 * @param color2	--> second color to compare: must be Color() object
	 * @param channel	--> the channel to check; 'r', 'g', or 'b' for red, green, or blue respectively
	 * @return			--> returns a boolean depending on if the channel comparison passed or failed
	 */
	private static boolean compare(Color color1, Color color2, char channel) {
		return switch (channel) {
			case 'r' -> Math.abs(color1.getRed() - color2.getRed()) <= maxColorDiff;
			case 'g' -> Math.abs(color1.getGreen() - color2.getGreen()) <= maxColorDiff;
			case 'b' -> Math.abs(color1.getBlue() - color2.getBlue()) <= maxColorDiff;
			default -> false;
		};
	}// end method compare()

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		// TODO: YOUR CODE HERE

		// initialize the largest size to 0,
		// create new ArrayList to later hold the points in largest region
		int largestSize = 0;
		ArrayList<Point> largestRegion = new ArrayList<>();

		// Loop over all identified regions in the ArrayList of all regions
		for (ArrayList<Point> currentRegion : allRegions){

			// Check if the currentRegion is larger than the presently recognized largestRegion
			if (currentRegion.size() > largestSize){

				// if currentRegion is larger than largestRegion then...
				// largestRegion is currentRegion, largest size is size of current currentRegion
				largestSize = currentRegion.size();
				largestRegion = currentRegion;
			}// end check for size of currentRegion
		}// end for-loop check for all regions

		// return the identified largestRegion
		return largestRegion;
	}// end method largestRegion()

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(),
				image.copyData(null),
				image.getColorModel().isAlphaPremultiplied(),null);
		// Now recolor the regions in it
		// TODO: YOUR CODE HERE
		// loop over all regions in the ArrayList containing all regions
		for (ArrayList<Point> region : this.allRegions){
			// For each region:

			// generate a random color
			Random random = new Random();
			int r = random.nextInt(255);
			int g = random.nextInt(255);
			int b = random.nextInt(255);
			Color randomColor = new Color(r, g, b);

			// loop over all points in the region
			for (Point point : region){
				// For each point:

				// get the x and y coordinate of the point
				int x = (int) point.getX();
				int y = (int) point.getY();

				// recolor the point at the coordinates in the image
				recoloredImage.setRGB(x, y, randomColor.getRGB());
			}// end for loop over points in each region
		}// end for-loop over identified regions
	}// end method recolorImage()
}// end class RegionFinder<>
