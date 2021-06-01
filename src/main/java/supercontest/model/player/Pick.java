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

    // After the game
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
