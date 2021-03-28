import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class PieceAbstract implements Piece {

    Point position;                     // Track square with Piece
    Suit color;                         // Black or White
    private final String id;            // Identifier
    protected BufferedImage image;      // Visual representation of Piece
    protected double value;             // value of Piece
    protected ChessGame chessgame;      // current game instance
    List<Integer> posMoves;             // List of possible moves

    /**
     * BLACK or WHITE
     */
    public enum Suit {
        WHITE,
        BLACK
    }

    /**
     * Constructor with name, suit, position
     * @param name K, Q, R, B, N, P
     * @param suit white or black
     * @param point current position square
     * @throws IOException Error loading piece image
     */
    public PieceAbstract(char name, String suit, Point point, ChessGame chessgame) throws IOException {

        // Set Suit
        color = suit.equals("white")? Suit.WHITE : Suit.BLACK;

        // Set Point (row and rank)
        this.position = point;

        // Set ID
        id = name +" @ " + position.getX() + " , " + position.getY();

        // Set image
        this.setImage();

        // Assign to game
        this.chessgame = chessgame;
    }

    /**
     * Method to get ID of Piece
     * @return String ID
     */
    @Override
    public String getID() {
        return this.id;
    }

    /**
     * Method to move piece
     */
    @Override
    public void move(Move newMove) {

    }

    public void setImage() throws IOException {
        String filename = this.getImageFile();
        try {
            image = ImageIO.read(new File((filename)));
        }
        catch(IOException e) {
            System.err.println(e.getMessage());
            System.err.println(filename);
            throw new IOException();
        }
    }

    public Point getCoordinates() {
        int x = (int) (position.getX() - 1) * ChessBoard.STEP + ChessBoard.STEP/5;
        int y = (int) (Math.abs(position.getY() - 8)) * ChessBoard.STEP + ChessBoard.STEP/5;

        return new Point(x, y);
    }

    public String toString() {
        return id;
    }

    /**
     * Abstract method implemented in subclasses
     * @return String filename
     */
    public abstract String getImageFile();

    /**
     * Abstract method to get moves, implemented in subclasses
     * @return List of Moves
     */
    public abstract List<Move> getMoves();

    @Override
    public void draw(Graphics g) {
//        Point currentPos = getCoordinates();
        Point currentPos = ChessLib.refToReal(position);
        int px = (int) currentPos.getX() + ChessBoard.STEP/5;
        int py = (int) currentPos.getY() + ChessBoard.STEP/5;
        g.drawImage(this.image, px, py, null, null);

    }
}
