package supercontest.model.player;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import supercontest.model.weeklylines.WeekOfLines;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "players")
public class Player {
    @Id
    private String username;
    private String password;
    private String loginToken;
    private String firstName;
    private String lastName;
    private List<WeekOfPicks> allPicks;
    private float seasonScore;
    private int seasonWins;
    private int seasonLosses;
    private int seasonPushes;

    // Register player - used by Jackson
    public Player() {
        this.loginToken = UUID.randomUUID().toString();
        this.allPicks = new ArrayList<>();
        this.seasonScore = 0;
        this.seasonWins = 0;
        this.seasonLosses = 0;
        this.seasonPushes = 0;
    }

    public void calculateSeasonScore(List<WeekOfLines> allWeeksOfLines) {
        seasonScore = 0;
        seasonWins = 0;
        seasonLosses = 0;
        seasonPushes = 0;
        allPicks.forEach(weekOfPicks -> {
            weekOfPicks.calculateWeeklyScore(allWeeksOfLines.get(weekOfPicks.getWeekNumber() - 1));
            seasonScore += weekOfPicks.getWeeklyScore();
            seasonWins += weekOfPicks.getWeeklyWins();
            seasonLosses += weekOfPicks.getWeeklyLosses();
            seasonPushes += weekOfPicks.getWeeklyPushes();
        });
    }

}
