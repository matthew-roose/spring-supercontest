package supercontest.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supercontest.model.player.Player;
import supercontest.model.player.WeekOfPicks;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.model.wrapper.PlayerAndPicks;
import supercontest.service.PlayerService;
import supercontest.service.WeeklyLinesService;

@RestController
@RequestMapping("/")
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class ClientController {

    private final PlayerService playerService;
    private final WeeklyLinesService weeklyLinesService;

    @PostMapping("/register")
    public ResponseEntity<Player> registerPlayer(@RequestBody Player player) {
        return playerService.registerPlayer(player);
    }

    @PostMapping("/submitPicks")
    public ResponseEntity<Player> submitPicks(@RequestBody PlayerAndPicks playerAndPicks) {
        return playerService.submitPicks(playerAndPicks);
    }

    @GetMapping("/getPicks/{weekNumber}")
    public ResponseEntity<WeekOfPicks> getWeekOfPicks(@RequestBody int playerId,
                                                      @PathVariable("weekNumber") int weekNumber) {
        return playerService.getWeekOfPicks(playerId, weekNumber);
    }

    @GetMapping("/getLines/{weekNumber}")
    public ResponseEntity<WeekOfLines> getWeekOfLines(@PathVariable("weekNumber") int weekNumber) {
        return weeklyLinesService.getWeekOfLines(weekNumber);
    }
}
