import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pawn extends PieceAbstract {

    public Pawn(String suit, Point position, ChessGame chessgame) throws IOException {
        super('P', suit, position, chessgame);

        // Initialize move behavior
        posMoves = Collections.singletonList(8);

        // Set value
        value = 1;
    }

    public String getImageFile() {
        if (this.color == PieceAbstract.Suit.WHITE) {
            return "Projects/Chess/img/P-white.png";
        }
        else {
            return "Projects/Chess/img/P-black.png";
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
