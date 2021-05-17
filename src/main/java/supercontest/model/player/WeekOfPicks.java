package supercontest.model.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import supercontest.model.weeklylines.GameLine;
import supercontest.model.weeklylines.WeekOfLines;

import java.util.List;

@Data
@AllArgsConstructor
public class WeekOfPicks {

    // When submitting picks
    private int weekNumber;
    private List<Pick> picks;

    // After the games
    private float weeklyScore;

    // Used by Jackson
    public WeekOfPicks() {
        this.weeklyScore = 0;
    }

    public float calculateWeeklyScore(WeekOfLines weekOfLines) {
        weeklyScore = 0;
        for (Pick pick : picks) {
            GameLine gameLine = weekOfLines.getLinesOfTheWeek().get(pick.getGameId() - 1);
            weeklyScore += pick.getPointsAwarded(gameLine);
        }
        return weeklyScore;
    }
}