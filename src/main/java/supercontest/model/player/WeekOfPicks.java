package supercontest.model.player;

import lombok.Data;
import supercontest.model.weeklylines.GameLine;
import supercontest.model.weeklylines.WeekOfLines;

import java.util.ArrayList;
import java.util.List;

@Data
public class WeekOfPicks {

    // When submitting picks
    private int weekNumber;
    private List<Pick> picks;

    // After the games
    private float weeklyScore;

    // Used if player didn't enter picks for the week
    public WeekOfPicks(int weekNumber) {
        this.weekNumber = weekNumber;
        this.picks = new ArrayList<>();
        this.weeklyScore = 0;
    }

    // Used by Jackson when a player enters picks (calls setters for other fields)
    public WeekOfPicks() {
        this.weeklyScore = 0;
    }

    public float calculateWeeklyScore(WeekOfLines weekOfLines) {
        weeklyScore = 0;
        picks.forEach(pick -> {
            GameLine gameLine = weekOfLines.getLinesOfTheWeek().get(pick.getGameId() - 1);
            weeklyScore += pick.getPointsAwarded(gameLine);
        });
        return weeklyScore;
    }
}
