/**
 * A Blob that moves a number of steps in given direction before changing direction
 * @author Amittai Joel Wekesa, CS-10, WInter 2021 (Adapted from Wanderer class)
 */
public class Rush extends Blob{

    protected int steps_taken, steps_interval;

    // Constructor for when no input is provided
    public Rush(){
        super();
        // Generating the number of steps interval
        int lower_step_count = 12;
        int range = (int) Math.round(Math.random() * 18);
        steps_interval = lower_step_count + range;
    }

    // Constructor for when only x and y positions are provided
    public Rush(double x, double y){
        super(x, y);
        // Generating the number of steps interval
        int lower_step_count = 12;
        int range = (int) Math.round(Math.random() * 18);
        steps_interval = lower_step_count + range;
    }

    // Constructor for when x, y, and r are provided
    public Rush(double x, double y, double r){
        super(x, y, r);
        // Generating the number of steps interval
        int lower_step_count = 12;
        int range = (int) Math.round(Math.random() * 18);
        steps_interval = lower_step_count + range;
    }

    // Override step method
    //NOTE: press 'r' to create a Rush subclass instance
    @Override
    public void step() {
        // Choose a new step interval if steps taken is divisible by set number of steps per interval
        if (steps_taken % steps_interval == 0) {
            dx = 2 * (Math.random()-0.5);
            dy = 2 * (Math.random()-0.5);
        }
        // Take appropriate steps and increment the steps counter
        x += dx;
        y += dy;
        steps_taken += 1;
    }
}
