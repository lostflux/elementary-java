import java.util.ArrayList;
import java.util.List;

public class Match {
    String team1, team2;        // the names of the teams playing in this match
    int score1, score2;         // the scores of team1 and 2, respectively
    Match prev1, prev2;         // the previous matches that team1 and 2, respectively, played to get here;
    boolean gameIsOver;
    //    or both null if this is a leaf
    public int numWins(String team) {
        // TODO: your code here

        // Check if current game is over, count it else don't count it
        int gamesWon = 0;                   // Initialize counter of games won

        // If current game is over,
        if (gameIsOver) {
            // if team won current game, increment games won by 1
            if ((team.equals(team1) && score1 > score2) || (team.equals(team2) && score2 > score1)) {
                gamesWon += 1;
            }
        }

        //Base case: if no previous matches,
        // return current value
        if (team.equals(team1) && prev1 == null){
            return gamesWon;
        }
        // Didn't use else because other possibilities are available
        else if (team.equals(team2) && prev2 == null){
            return gamesWon;
        }

        // if previous matches have been played,
        // recursively get their values and add to current count
        if (team.equals(team1)) {
            gamesWon += prev1.numWins(team);
        }
        else if (team.equals(team2)) {
            gamesWon += prev2.numWins(team);
        }

        // finally, return the tally of games won
        return gamesWon;
    }

    public List<List<String>> teamsByLevel() {
        // TODO: your code here
        List<List<String>> teams = new ArrayList<>();
        addToList(teams, 0);    // Pass accumulator list into helper function
        return teams;
    }
    //Helper function to add items to accumulator
    private void addToList(List<List<String>> teams, int level) {

        // Initialize list to hold teams at current level
        List<String> currentTeams;

        // Try to get sublist at index = current level in main list
        try {
            currentTeams = teams.get(level);
        }

        // If index does not exist, create list and insert
        catch (Exception IndexOutOfBoundsException) {
            currentTeams = new ArrayList<String>();
            teams.add(level, currentTeams);
        }
        if (teams.get(level) == null) {
            teams.add(level, new ArrayList<String>());
        }

        // STEP 2: Add this match's teams to the sub-list
        currentTeams.add(team1);
        currentTeams.add(team2);

        // Finally, if previous matches exist, recursively add them into list,
        // Increasing level accordingly
        if (prev1 != null) {
            prev1.addToList(teams, level+1);    // Not incrementing but generating a new variable = level + 1
        }
        if (prev2 != null) {
            prev2.addToList(teams, level+1);
        }
    }
}