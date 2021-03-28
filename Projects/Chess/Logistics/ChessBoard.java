import java.awt.*;
import java.awt.image.BufferedImage;

public class ChessBoard {
    public static final int SIZE = 800;
    public static final int STEP = SIZE /8;
    private final BufferedImage board;
    int dark = Color.GRAY.getRGB();
    int light = Color.PINK.getRGB();


    public ChessBoard() {
        // Create image of chess board
        board = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);

        // Track x coordinate

        // Loop over columns in board
        for (int file=1; file<=8; file++) {

            // Track y coordinate

            // Loop over rows in board
            for (int rank=1; rank<=8; rank++) {

                // Determine whether to color Black or white
                boolean isLightSquare = (file + rank) % 2 != 0;
                int color = isLightSquare? light: dark;


                Point currentDims = ChessLib.refToReal(new Point(file, rank));
                int x = (int) currentDims.getX();
                int y = (int) currentDims.getY();
                // Loop over each pixel in cell and set color.
                for (int px = x; px < x+STEP; px++) {
                    for (int py = y; py < y+STEP; py++) {
                        try {
                            board.setRGB(px, py, color);
                        }
                        catch (Exception e) {
                            System.err.println("x: " + px + ", y: " + py + " is out of bounds.");
                        }
                    }
                }
                // step to next row
            }
            // step to next column
        }
    }

    public boolean isInBoard(Point p) {
        return 0<=p.getX() && p.getX()< SIZE && 0 <= p.getY() && p.getY() < SIZE;
    }

    /**
     * Method to draw board.
     * @param g Graphics window
     */
    public void draw(Graphics g) {
        g.drawImage(board, 0, 0, null);
    }
}
