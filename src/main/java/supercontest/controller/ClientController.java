package supercontest.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        try {
            Player registeredPlayer = playerService.registerPlayer(player);
            return new ResponseEntity<>(registeredPlayer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/submitPicks")
    public ResponseEntity<Player> submitPicks(@RequestBody PlayerAndPicks playerAndPicks) {
        try {
            Player playerWhoSubmittedPicks = playerService.submitPicks(playerAndPicks);
            if (playerWhoSubmittedPicks != null) {
                return new ResponseEntity<>(playerWhoSubmittedPicks, HttpStatus.OK);
            } else {
                // invalid playerId or weekNumber
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getPicks/{weekNumber}")
    public ResponseEntity<WeekOfPicks> getWeekOfPicks(@RequestBody int playerId,
                                                      @PathVariable("weekNumber") int weekNumber) {
        try {
            WeekOfPicks weekOfPicks = playerService.getWeekOfPicks(playerId, weekNumber);
            if (weekOfPicks != null) {
                return new ResponseEntity<>(weekOfPicks, HttpStatus.OK);
            } else {
                // invalid playerId or weekNumber
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getLines/{weekNumber}")
    public ResponseEntity<WeekOfLines> getWeekOfLines(@PathVariable("weekNumber") int weekNumber) {
        try {
            WeekOfLines weekOfLines = weeklyLinesService.getWeekOfLines(weekNumber);
            if (weekOfLines != null) {
                return new ResponseEntity<>(weekOfLines, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
