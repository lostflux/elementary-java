import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Quilter {
    private int width, height;                      // size of the quilt
    private BufferedImage quilt;                    // the quilt put together by a call to makeNewQuilt
    private ArrayList<BufferedImage> fabrics;       // possible fabrics to incorporate in the quilt
    private ArrayList<ArrayList<Point>> shapes;     // possible shapes of the pieces to use in the quilt

    public Quilter(int width, int height,
                   ArrayList<BufferedImage> fabrics, ArrayList<ArrayList<Point>> shapes) {
        this.width = width;
        this.height = height;
        this.fabrics = fabrics;
        this.shapes = shapes;
    }

    /**
     * Apply a shape-sized piece of the fabric to the quilt,
     * taking the colors from fabric according to the points in shape shifted by (fx,fy)
     * and putting them in quilt according to those points shifted by (qx,qy)
     */
    public void applyPatch(BufferedImage fabric, ArrayList<Point> shape, int fx, int fy, int qx, int qy) {
        // TODO: your code here
        // Get each point in shape
        for (Point point : shape) {

            // Get it's x and y values
            int x = (int) point.getX();
            int y = (int) point.getY();

            // Get color at appropriate position in fabric
            Color color = new Color(fabric.getRGB(x+fx, y+fy));

            // Set color at appropriate position in quilt
            quilt.setRGB(x + qx, y + qy, color.getRGB());
        }
    }

    public void makeNewQuilt(int numPatches) {
        // TODO: your code here

        // Create new BufferedImage instance and set it to the quilt instance variable of Quilter
        quilt = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // While there is a patch to be applied, apply it.
        while (numPatches > 0) {

            // Initialize new instance of Random()
            Random random = new Random();

            // Generate random index of shape to get
            int maxIndex = shapes.size() - 1;
            int shapeIndex = random.nextInt(maxIndex);

            // Get shape at index
            ArrayList<Point> shape = shapes.get(shapeIndex);

            // Generate random index of fabric to get
            maxIndex = fabrics.size() - 1;
            int fabricIndex = random.nextInt(maxIndex);

            // Get the fabric -- an instance of BufferedImage
            BufferedImage fabric = fabrics.get(fabricIndex);

            // Generate random fx, fy, qx, and qy values within a range -- I chose 50
            int fx = random.nextInt(50);
            int fy = random.nextInt(50);
            int qx = random.nextInt(50);
            int qy = random.nextInt(50);

            // finally, invoke the applyPatch method with the selected fabric, shape, and values
            applyPatch(fabric, shape, fx, fy, qx, qy);

            // Decrement the number of patches remaining
            numPatches--;
        }
    }
}
