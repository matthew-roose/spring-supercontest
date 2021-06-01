package supercontest.model.player;

import lombok.Data;
import supercontest.model.weeklylines.WeekOfLines;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Player {

    private UUID loginToken;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private List<WeekOfPicks> allPicks;
    private float seasonScore;

    // Register player - used by Jackson
    public Player() {
        this.loginToken = UUID.randomUUID();
        this.allPicks = new ArrayList<>();
        this.seasonScore = 0;
    }

    public void calculateSeasonScore(List<WeekOfLines> allWeeksOfLines) {
        seasonScore = 0;
        allPicks.forEach(weekOfPicks ->
                seasonScore += weekOfPicks.calculateWeeklyScore(allWeeksOfLines.get(weekOfPicks.getWeekNumber() - 1)));
    }

}
