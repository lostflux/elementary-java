import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Knight extends PieceAbstract {

    public Knight(String suit, Point position, ChessGame chessgame) throws IOException {
        super('N', suit, position, chessgame);

        // Initialize move behavior
        posMoves = Arrays.asList(-17, -15, -10, -6, 6, 10, 15, 17);

        // Initialize value of Piece
        this.value = 3;
    }

    public String getImageFile() {
        if (this.color == PieceAbstract.Suit.WHITE) {
            return "Projects/Chess/img/N-white.png";
        }
        else {
            return "Projects/Chess/img/N-black.png";
        }
    }

    public List<Move> getMoves() {
        List<Move> possibleMoves;

        return null;
    }
}
