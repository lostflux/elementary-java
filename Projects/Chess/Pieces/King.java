import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class King extends PieceAbstract {

    public King(String suit, Point position, ChessGame chessgame) throws IOException {
        super('K', suit, position, chessgame);

        // Initialize move behavior
        this.posMoves = Arrays.asList(-9, -8, -7, -1, 1, 7, 8, 9);

        // Set value
        this.value = Double.MAX_VALUE;
    }

    public String getImageFile() {
        if (this.color == PieceAbstract.Suit.WHITE) {
            return "Projects/Chess/img/K-white.png";
        }
        else {
            return "Projects/Chess/img/K-black.png";
        }
    }

    public List<Move> getMoves() {
        List<Integer> validMoves = new ArrayList<>();
        int current = ChessLib.refToInt(this.position.x, this.position.y);
        for (int step : posMoves) {
            int next = current + step;
//            if (this.chessboard.get(next))
        }

        return null;
    }
}
