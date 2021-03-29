import java.awt.*;
import java.util.List;

/**
 * Interface for Chess Pieces
 */
public interface Piece {

    /**
     * move a piece
     * @param newMove move to apply!
     */
    void move(Move newMove);

    /**
     * Get ID of Piece
     * @return String ID
     */
    String getID();

    String getSuit();

    List<Integer> getPosMoves();

    /**
     * Draw a Piece
     * @param g Graphics instance
     */
    void draw(Graphics g);

}
