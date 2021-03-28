import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Queen extends PieceAbstract{

    public Queen(String suit, Point position, ChessGame chessgame) throws IOException {
        super('Q', suit, position, chessgame);

        // Initialize move behavior
        this.posMoves = Arrays.asList(-9, -8, -7, -1, 1, 7, 8, 9);

        // Set value
        this.value = 9;
    }

    public String getImageFile() {
        if (this.color == PieceAbstract.Suit.WHITE) {
            return "Projects/Chess/img/Q-white.png";
        }
        else {
            return "Projects/Chess/img/Q-black.png";
        }
    }

    public List<Move> getMoves() {
        Set<Move> possibleMoves;

        return null;
    }
}
