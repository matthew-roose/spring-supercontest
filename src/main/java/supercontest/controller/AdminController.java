package supercontest.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supercontest.model.player.Player;
import supercontest.model.weeklylines.ScoreUpdate;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.service.PlayerService;
import supercontest.service.WeeklyLinesService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class AdminController {

    private final PlayerService playerService;
    private final WeeklyLinesService weeklyLinesService;

    @PostMapping("/postLines/{weekNumber}")
    public ResponseEntity<WeekOfLines> addWeekOfLines(@RequestBody WeekOfLines weekOfLines,
                                                      @PathVariable("weekNumber") int weekNumber) {
        return weeklyLinesService.addWeekOfLines(weekOfLines, weekNumber);
    }

    @PutMapping("/scoreGames/{weekNumber}")
    public ResponseEntity<WeekOfLines> scoreWeekOfLines(@RequestBody List<ScoreUpdate> scoreUpdates,
                                                        @PathVariable("weekNumber") int weekNumber) {
        return weeklyLinesService.scoreWeekOfLines(scoreUpdates, weekNumber);
    }

    @PutMapping("/scorePicks/{weekNumber}")
    public ResponseEntity<List<Player>> scoreWeekOfPicks(@PathVariable("weekNumber") int weekNumber) {
        return playerService.scoreWeekOfPicks(weekNumber);
    }
}
