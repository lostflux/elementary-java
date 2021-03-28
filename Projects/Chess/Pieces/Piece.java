import java.awt.*;

/**
 * Interface for Chess Pieces
 */
public interface Piece {

    void move(Move newMove);

    String getID();

    void draw(Graphics g);

}
