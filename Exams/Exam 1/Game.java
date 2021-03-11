public class Game extends DrawingGUI {
    private int[] scores;           // each player's score
    // TODO: your code here
    private int increment;          // score if the player is in a run
    private int lastScorer;         // last controller's int
    private int numTicks;           // Total time allowed. Set at the start of each game

    public Game(int numPlayers, int numTicks) {

        // TODO: your code here

        scores = new int[numPlayers];
        increment = 0;
        lastScorer = -1;
        this.numTicks = numTicks;
        startTimer();
    }

    boolean goodPosition(double x, double y, double z) {
        // Assume this is implemented to actually return true if and only if (x,y,z) should score points
        return true;
    }

    public void handleController(int controller, double x, double y, double z) {
        // TODO: your code here

        // If time is over, do nothing
        if (numTicks <= 0) {
            ;   // Do nothing
        }

        // Else, handle the current controller
        else {
            // Check if player is in good position
            if (goodPosition(x, y, z)) {
                // check if player is not last scorer and set them to be lastScorer, set increment value to 1
                if (lastScorer != controller) {
                    lastScorer = controller;
                    increment = 1;
                }

                // else player is the last scorer, increase their score addition
                else {
                    increment++;
                }

                // Finally, increment the score
                int currentScore = scores[controller];
                scores[controller] = currentScore + increment;
            }
        }
    }

    @Override
    public void handleTimer() {
        // TODO: your code here
        // If there's time remaining, decrement
        // NOTE: instead of using two variables, I preferred to just decrement numTicks
        // and check when it reaches 0

        if (numTicks > 0){
            numTicks--;
        }
    }
}
