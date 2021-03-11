import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Tourist extends Blob {
    private BufferedImage map;
    // TODO: your code here
    private  ArrayList<Character> allowedDirections = new ArrayList<>();    // ArrayList of permitted directions
    private char currentDirection;                                          // char holding current direction
    private ArrayList<Point> tour = new ArrayList<>();                      // list of previously visited points

    public Tourist(BufferedImage map) {
        // TODO: your code here
        super(); // assuming superclass has a no-argument initializer
        this.map = map;
        allowedDirections.add('N');
        allowedDirections.add('S');
        allowedDirections.add('E');
        allowedDirections.add('W');
    }

    public void updateDirection() {
        // TODO: your code here
        // create new instance of Random() class to be used to generate integers directly
        Random random = new Random();

        // boolean to track if a chosen direction is valid
        boolean validDirection = false;

        // char to temporarily store a new direction
        char newDirection;

        // While loop that ensures a valid direction is chosen
        while (validDirection == false) {

            // generate a new index integer from 0 to 3
            int n = random.nextInt(4); // generates between 0 and passed number, exclusive

            // get direction from list of allowed directions
            newDirection = allowedDirections.get(n);

            // check if direction is valid -- No U-turns

            if ((newDirection == 'E' && currentDirection == 'W') || (newDirection == 'W' && currentDirection == 'E')){
                validDirection = false;
            }
            else if ((newDirection == 'N' && currentDirection == 'S') || (newDirection == 'S' && currentDirection == 'N')) {
                validDirection = false;
            }
            else {
                // Direction is valid.
                // save the generated direction
                currentDirection = newDirection;

                // Set validDirection to True, which will cause exit of while loop
                validDirection = true;
            }
        }


        //Update dx and dy according to given direction
        // If East, step right
        if (currentDirection == 'E'){
            dx = 1;
            dy = 0;
        }

        // If West, step left
        else if (currentDirection == 'W'){
            dx = -1;
            dy = 0;
        }

        // If North, step up
        else if (currentDirection == 'N'){
            dx = 0;
            dy = -1;
        }

        // If South, step down
        else if (currentDirection == 'S') {
            dx = 0;
            dy = 1;
        }
        else {
            System.out.println("Unknown direction!");
        }
    }

    public void step() {
        // TODO: your code here

        // Since no U-turns allowed, we only need to update direction if there is a branch or turn in the path
        // parse over all neighbors, check those to the sides of current path
        for (int rX=(int) (x-1); rX<= (int) (x+1); rX++) {
            for (int rY=(int) (y-1); rY<= (int) (y+1); rY++) {
                // only interested in North, South, East and West Neighbors

                // if current direction is North or South,
                if (currentDirection == 'N' || currentDirection == 'S') {
                    // Check East and West
                    if ((rX == x-1 || rX == x+1) && (rY == y)) {
                        // get RGB from map and see if it is Black i.e. point is a pathway
                        if (map.getRGB(rX, rY) == Color.BLACK.getRGB()) {

                            // if point to side of pathway is a direction, update direction
                            updateDirection();
                        }
                    }
                }

                // Or else if current direction is East or West
                else if (currentDirection == 'E' || currentDirection == 'W') {
                    // Check North and South
                    if ((rY == y - 1 || rY == y + 1) && (rX == x)) {
                        // get RGB from map and see if it is Black i.e. point is a pathway
                        if (map.getRGB(rX, rY) == Color.BLACK.getRGB()) {

                            // if point to side of pathway is a direction, update direction
                            updateDirection();
                        }
                    }
                }
            }
        }

        // After determining whether an update to the direction is needed, take the step
        x += dx;
        y += dy;

    }

    public void draw(Graphics g) {
        // TODO: your code here
        // draw previously toured points
        if (!tour.isEmpty()) {
            g.setColor(Color.BLUE);
            for (Point point : tour) {
                int pointX = (int) point.getX();
                int pointY = (int) point.getY();
                g.fillOval(pointX, pointY, 3, 3);
            }
        }

        // draw current point
        g.setColor(Color.GREEN);
        g.fillOval((int)x,(int)y, (int)r, (int)r);

        // finally, add current point to tours before doing next update
        tour.add(new Point((int)x, (int)y));
    }
}
