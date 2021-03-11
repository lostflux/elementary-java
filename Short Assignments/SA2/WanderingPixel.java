import java.awt.*;

/**
 * A blob that moves randomly and has a color
 */
public class WanderingPixel extends Wanderer {
	private final Color color;


	// Added constructor for case with Color and r inputs
	public WanderingPixel(double x, double y, double r, Color c) {
		super(x, y);
		this.r = r;
		this.color = c;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
	}

	// Added a getColor() method to return the color of the current Blob
	public Color getColor(){
		return this.color;
	}
}
