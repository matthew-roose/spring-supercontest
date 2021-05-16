package supercontest.model.weeklylines;

import lombok.Data;

@Data
public class GameLine {

    // Before the game
    private Team homeTeam;
    private Team awayTeam;
    private float homeTeamHandicap; // 0 if favored
    private float awayTeamHandicap; // 0 if favored
    private String gameTime; // will parse and use as Date where we allow picks to be submitted

    // After the game
    private Integer homeTeamScore; // null until initialized
    private Integer awayTeamScore; // null until initialized

    public GameLine(Team homeTeam, Team awayTeam, float homeTeamHandicap, float awayTeamHandicap, String gameTime) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeTeamHandicap = homeTeamHandicap;
        this.awayTeamHandicap = awayTeamHandicap;
        this.gameTime = gameTime;
    }
}