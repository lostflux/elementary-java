public class Team {
    private String name;
    private int score;

    /**
     * Creating a new Team
     * @param name: The team's name.
     */
    public Team(String name) {

        // set team name
        this.name = name;
    }

    /**
     * Get the team's name.
     * @return  A string equivalent to the team's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the team's current score
     * @return an int equal to the team's current score.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Score method -- increments the team's score.
     */
    public void score () {
        score += 2;
    }

    public static void main(String[] args) {

        // (a) Create two teams.
        Team argentina = new Team("Argentina");
        Team france = new Team("Les Bleus");


        // (b-c) score
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
