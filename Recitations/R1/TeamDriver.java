import java.util.*;

public class TeamDriver {

    public static void main(String[] args) {

        // Create two teams.
        Team argentina = new Team("Argentina");
        Team france = new Team("Les Bleus");


        // score
        argentina.score();

        france.score();
        france.score();

        // Check which team wins.
        if (france.getScore() == argentina.getScore()) {
            System.out.println("The game ended in a draw.");
        } else if (france.getScore() > argentina.getScore()) {
            System.out.println("France wins!");
        } else {
            System.out.println("Argentina wins!");
        }

        System.out.println("THE END.");
    }
}
