import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Quest {
    // TODO: your code here

    // HashMap to associate scenes with set of actions
    HashMap<String, HashSet<String>> allStates;
    HashMap<String, HashMap<String, HashSet<String>>> corr;

    // HashMap to associate actions with results
    HashMap<String, String> results;

    // String to hold current state
    String state;

    public String getAction(String state) {
        // Assume this returns a valid action for the given state
        return "hello";
    }

    public void play(String start, boolean undo) {
        // TODO: your code here
        // Get start action
        String action = getAction(start);
//        undo = false;

        // if mode is not undo -- i.e. undo = false
        if (!undo) {  // --> play forward
            // While action is not to end game, keep running
            while(!action.equals("endgame")) {
                String result = results.get(action);    // Get results of current action
                action = getAction(result);             // get new action for this scene
            }
        }
        // else if mode is undo, rewind
        else {
            // Find action that led to current result
            for (String key : results.keySet()) {
                if (results.get(key).equals(state)) {
                    action = key;
                }
            }

            // Find scene with the given action
            for (String key : allStates.keySet()) {             // for every scene
                if (allStates.get(key).contains(action)) {      // if set of actions for scene contains given action
                    String previousScene = key;                 // then scene was the previous scene
                    play(previousScene, undo);                  // replay the scene
                }
            }

        }
    }
}