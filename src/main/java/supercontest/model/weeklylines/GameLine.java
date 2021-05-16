package supercontest.model.weeklylines;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class GameLine {

    // Before the game
    @Id
    private String gameId;
    private Team homeTeam;
    private Team awayTeam;
    private float homeTeamHandicap; // positive if underdog, negative if favored
    private String gameTime; // will parse and use as Date where we allow picks to be submitted

    // After the game
    private Integer homeTeamScore; // null until initialized after the game
    private Integer awayTeamScore; // null until initialized after the game

    public GameLine(String gameId, Team homeTeam, Team awayTeam, float homeTeamHandicap, String gameTime) {
        this.gameId = gameId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeTeamHandicap = homeTeamHandicap;
        this.gameTime = gameTime;
    }
}
