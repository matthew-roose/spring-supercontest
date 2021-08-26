package supercontest.model.player;

import lombok.Data;
import supercontest.model.weeklylines.GameLine;
import supercontest.model.weeklylines.Result;
import supercontest.model.weeklylines.Team;

@Data
public class Pick {

    // When submitting the pick
    private int gameId;
    private Team pickedTeam;
    private Team homeTeam; // only used by UI
    private Team awayTeam; // only used by UI
    private float homeTeamHandicap; // only used by UI // positive if underdog, negative if favored
    private String gameTime;

    // After the game - used by UI
    private Integer homeTeamScore; // set by corresponding GameLine when scores are uploaded
    private Integer awayTeamScore; // set by corresponding GameLine when scores are uploaded
    private Result result;

    // 1 for win, 0.5 for push, 0 for loss
    public float getPointsAwarded(GameLine gameLine) {
        Team coveringTeam = gameLine.calculateCoveringTeam();
        if (coveringTeam == pickedTeam) {
            result = Result.WIN;
            return 1;
        } else if (coveringTeam == null) {
            result = Result.PUSH;
            return .5f;
        } else {
            result = Result.LOSS;
            return 0;
        }
    }
}
