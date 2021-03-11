//import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Color;

/**
 * A class demonstrating manipulation of image pixels.
 * Version 0: just the core definition
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, Winter 2014, rewritten for BufferedImage
 * @author CBK, Spring 2015, refactored to separate GUI from operations
 * @author Amittai, Winter 2021, added monochrome() method to process image
 */
public class ImageProcessor0 {
	private BufferedImage image;		// the current image being processed

	/**
	 * @param image		the original
	 */
	public ImageProcessor0(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * Turns RGB-colored pixels into monochrome color scheme within a 10-by-10 square around current mouse position
	 *  by extracting the current color of each pixel and calculating  a new monochrome (black to white) color
	 * @param x		current x position of the mouse
	 * @param y		current y position fo the mouse
	 */
	public void monochrome(int x, int y){
		// Nested loop over every pixel in 10-by-10 square around mouse position
		for (int j = Math.max(0, x-5); j < Math.min(x+5, image.getWidth()); j++){
			for (int k = Math.max(0, y-5); k < Math.min(y+5, image.getHeight()); k++){
				Color color = new Color(image.getRGB(j, k));				// color of current pixel
				System.out.println(color);
				int red = color.getRed();									// R value of color
				int green = color.getGreen();								// G value of color
				int blue = color.getBlue();									// B value of color
				int monoCode = (int) (0.3*red + 0.6*green + 0.1*blue);		// new monochrome color code
				Color monoColor = new Color(monoCode, monoCode, monoCode);	// new monochrome color
				image.setRGB(j, k, monoColor.getRGB());
			}
		}
	}
}
