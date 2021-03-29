
/**
 * Handler for chess moves
 */
public class Move {
    protected int steps;
    protected int target;
    protected double reward;
    private final Piece currentPiece;
    protected boolean valid = true;
    ChessGame chessgame;

    public Move(ChessGame chessgame, Piece currentPiece, int currentPosition, int steps) {
        target = currentPosition + steps;
        this.currentPiece = currentPiece;
        this.chessgame = chessgame;
    }


    public int getMove() {
        return this.target;
    }

    public double getReward() {
        return reward;
    }

    private boolean checkMove() {
        if (currentPiece instanceof Knight) {
            if (target>0 && target<65 && target%8 != (target-steps) % 8) {
                this.valid = true;
            }
        }
        else {
            int currentDirection = 0;
            for (int direction : currentPiece.getPosMoves()) {
                if (steps / target > 0 && steps % target == 0) {
                    currentDirection = direction;
                    break;
                }
            }
            assert currentDirection != 0;
            String suit = currentPiece.getSuit();
            for (int i = 0; i <= currentDirection * (steps/currentDirection); i++) {
//                if (chessgame.get)
            }

        }



        return false;
    }
}
