package supercontest.model.weeklylines;

import lombok.Data;

@Data
public class ScoreUpdate {
    private int gameId;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
}
