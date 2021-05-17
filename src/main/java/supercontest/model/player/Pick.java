package supercontest.model.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import supercontest.model.weeklylines.GameLine;
import supercontest.model.weeklylines.Result;
import supercontest.model.weeklylines.Team;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pick {

    // When submitting the pick
    private int gameId;
    private Team pickedTeam;

    // After the game
    private Result result;

    // 1 for win, 0.5 for push, 0 for loss
    public float getPointsAwarded(GameLine gameLine) {
        result = gameLine.getResult(pickedTeam);
        if (result == Result.WIN) {
            return 1;
        } else if (result == Result.PUSH) {
            return 0.5f;
        } else {
            return 0;
        }
    }
}
