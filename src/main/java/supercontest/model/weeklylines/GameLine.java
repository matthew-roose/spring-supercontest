package supercontest.model.weeklylines;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class GameLine {

    // Before the game
    private int gameId;
    private Team homeTeam;
    private Team awayTeam;
    private float homeTeamHandicap; // positive if underdog, negative if favored
    private String gameTime; // will parse and use as Date where we allow picks to be submitted

    // After the game
    private Integer homeTeamScore; // null until initialized after the game
    private Integer awayTeamScore; // null until initialized after the game

    public Result getResult(Team pickedTeam) {
        Result result;
        if (pickedTeam == homeTeam) {
            if (homeTeamScore + homeTeamHandicap > awayTeamScore) {
                result = Result.WIN;
            } else if (awayTeamScore > homeTeamScore + homeTeamHandicap) {
                result = Result.LOSS;
            } else {
                result = Result.PUSH;
            }
        } else if (pickedTeam == awayTeam){
            if (awayTeamScore > homeTeamScore + homeTeamHandicap) {
                result = Result.WIN;
            } else if (homeTeamScore + homeTeamHandicap > awayTeamScore) {
                result = Result.LOSS;
            } else {
                result = Result.PUSH;
            }
        } else {
            result = null; // pickedTeam wasn't playing in this game
        }
        return result;
    }
}
