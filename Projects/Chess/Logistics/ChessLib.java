import java.awt.*;

/**
 * A Library of utility functions for the Chess project.
 */
public class ChessLib {

    /**
     * Static function to get an int and return its position on board
     * @param pos position int
     * @return position Point carrying  and y coordinates
     */
    public static Point intToRef(int pos) {
        assert 0<pos && pos <= 64;
        int rank = boolToInt(pos % 8 != 0) + (pos / 8);
        int row = pos % 8;
        if (row == 0) row += 8;
        return new Point(row, rank);
    }

    /**
     * Static Method to convert a reference (row and rank) to int
     * @param row Row -> x var
     * @param rank Rank -> y var
     * @return int depending on x and y
     */
    public static int refToInt(int row, int rank) {
        return (8 * (rank-1) + row);
    }

    public static Point realToRef(Point p) {

        // Get point coordinates (row and rank)
        double px = p.getX(), py = p.getY();

        // Get size of ChessBoard
        int step = ChessBoard.STEP;

        // Check range of x and convert to appropriate cell
        int x = (int) Math.ceil(px/step);

        // Check range of y and convert to appropriate cell
        // TODO: NOTE THAT y GOES IN REVERSE ORDER!
        int y = (int) (8 - Math.floor(py/step));

        // Return Point carrying info of cell
        return new Point(x, y);
    }

    public static Point refToReal(Point cell) {
        double row = cell.getX(), rank = cell.getY();

        // return Point at top-left corner of cell
        int x = (int) (row - 1) * ChessBoard.STEP;
        int y = 800 - (int) (rank) * ChessBoard.STEP;
        return new Point (x, y);
    }

    /**
     * Helper method to conveniently convert a boolean to int
     * @param bool boolean value
     * @return false -> 0, true -> 1
     */
    public static int boolToInt(boolean bool) {
        return bool? 1: 0;
    }
}
