package supercontest.model.weeklylines;

import lombok.Data;

@Data
public class GameLine {

    // Before the game
    private int gameId;
    private Team homeTeam;
    private Team awayTeam;
    private float homeTeamHandicap; // positive if underdog, negative if favored
    private long gameTime; // will compare to Instant.now().toEpochMilli()

    // After the game
    private Integer homeTeamScore; // null until initialized after the game
    private Integer awayTeamScore; // null until initialized after the game

    public Team calculateCoveringTeam() {
        float homeTeamAdjustedScore = homeTeamScore + homeTeamHandicap;
        if (homeTeamAdjustedScore > awayTeamScore) {
            return homeTeam;
        } else if (homeTeamAdjustedScore < awayTeamScore) {
            return awayTeam;
        } else {
            return null; // represents push
        }
    }
}
