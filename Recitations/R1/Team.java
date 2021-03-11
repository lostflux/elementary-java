public class Team {
    private final String team_name;
    private int score;

    public Team(String team_name){
        this.team_name = team_name;
        score = 0;
    }

    public String getName(){
        return team_name;
    }

    public int getScore(){
        return score;
    }

    public void addScore(){
        score += 2;
    }

}
