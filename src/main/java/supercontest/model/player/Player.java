package supercontest.model.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import supercontest.model.weeklylines.WeekOfLines;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "players")
@AllArgsConstructor
public class Player {

    @Id
    private int playerId;
    private String firstName;
    private String lastName;
    private List<WeekOfPicks> allPicks;
    private float seasonScore;

    // Register player - used by Jackson
    public Player() {
        this.allPicks = new ArrayList<>();
        this.seasonScore = 0;
    }

    public void calculateSeasonScore(List<WeekOfLines> allWeeksOfLines) {
        seasonScore = 0;
        allPicks.forEach(weekOfPicks ->
                seasonScore += weekOfPicks.calculateWeeklyScore(allWeeksOfLines.get(weekOfPicks.getWeekNumber() - 1)));
    }

}
