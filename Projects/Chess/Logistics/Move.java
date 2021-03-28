
/**
 * Handler for chess moves
 */
public class Move {
    protected int dx, dy;
    double reward;

    public Move(int dx, int dy) {
        this.dx = dx; this.dy = dy;
    }


    public double[] getMove() {
        return new double[]{this.dx, this.dy, this.reward};
    }
}
