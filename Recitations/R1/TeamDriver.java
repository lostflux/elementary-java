import java.util.*;

public class TeamDriver {

    public static void main(String[] args) {
        ArrayList<Team> match = new ArrayList<Team>();  // Create match as an array of Team instances
        Team Paris = new Team("Paris");      // Create team one
        Team Bayern = new Team("Bayern");    // Create team two

        // Add teams to match
        match.add(Paris);
        match.add(Bayern);

        // Add team scores
        int teamOneScore = (int) (Math.random() * 10);
        int teamTwoScore = (int) (Math.random() * 10);

        int i = 0;
        while(i <= teamOneScore){
            Paris.addScore();
            i++;
        }

        int j = 0;
        while (j < teamTwoScore){
            Bayern.addScore();
            j++;
        }

        // Print team names
        for (Team team : match) {
            System.out.println(team.getName());
        }

        // Find team with highest and lower score
        for (Team team : match){
            for (Team other_team : match){
                if (team == other_team){
                    ;
                }
                else{
                    if (team.getScore() > other_team.getScore()){
                        System.out.println(team.getName() + " wins!");
                    }
                    else if (team.getScore() == other_team.getScore()){
                        System.out.println(team.getName() + " and " + other_team.getName() + " draw");
                    }
                    else{
                        System.out.println(team.getName() + " loses!");
                    }
                }
            }
        }
//        if (Paris.getScore() > Bayern.getScore()){
//            System.out.println("Paris has more points than Bayern");
//        }
//        else if (Bayern.getScore() > Paris.getScore()){
//            System.out.println("Bayern has more points than Paris");
//        }
//        else{
//            System.out.println("The team scores are currently even");
//        }
    }
}
