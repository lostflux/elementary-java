import java.awt.*;
import java.util.*;

public class Pixel extends Point {
    Color color;
    boolean visited = false;

    public Pixel(int xPosition, int yPosition){
        super(xPosition, yPosition);
    }

    public Pixel(int xPosition, int yPosition, Color color){
        super(xPosition, yPosition);
        this.color = color;
    }
}
