import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Rook extends PieceAbstract {

    public Rook(String suit, Point position, ChessGame chessgame) throws IOException {
        super('R', suit, position, chessgame);

        // Initialize move behavior
        posMoves = Arrays.asList(-8, -1, 1, 8);

        // set value
        this.value = 5;
    }

    public String getImageFile() {
        if (this.color == PieceAbstract.Suit.WHITE) {
            return "Projects/Chess/img/R-white.png";
        }
        else {
            return "Projects/Chess/img/R-black.png";
        }
    }

    public List<Move> getMoves() {
        List<Move> possibleMoves;

        return null;
    }
}
