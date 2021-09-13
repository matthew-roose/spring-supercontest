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
    private int wins;
    private int losses;
    private int pushes;

    // Empty week of picks is added to every player when new lines are posted
    public WeekOfPicks(int weekNumber) {
        this.weekNumber = weekNumber;
        this.picks = new ArrayList<>();
        this.weeklyScore = 0;
        this.wins = 0;
        this.losses = 0;
        this.pushes = 0;
    }

    // Used by Jackson when a player enters picks (calls setters for other fields)
    public WeekOfPicks() {
        this.weeklyScore = 0;
        this.wins = 0;
        this.losses = 0;
        this.pushes = 0;
    }

    public void calculateWeeklyScore(WeekOfLines weekOfLines) {
        weeklyScore = 0;
        wins = 0;
        losses = 0;
        pushes = 0;
        picks.forEach(pick -> {
            GameLine gameLine = weekOfLines.getLinesOfTheWeek().get(pick.getGameId() - 1);
            if (gameLine.getHomeTeamScore() != null) {
                pick.setHomeTeamScore(gameLine.getHomeTeamScore());
                pick.setAwayTeamScore(gameLine.getAwayTeamScore());
                float pointsAwardedForPick = pick.getPointsAwarded(gameLine);
                weeklyScore += pointsAwardedForPick;
                if (pointsAwardedForPick == 1.0) {
                    wins++;
                } else if (pointsAwardedForPick == 0.0) {
                    losses++;
                } else if (pointsAwardedForPick == 0.5) {
                    pushes++;
                }
            }
        });
    }
}
