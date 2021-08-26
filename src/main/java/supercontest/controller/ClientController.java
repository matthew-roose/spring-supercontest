package supercontest.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supercontest.model.player.Player;
import supercontest.model.player.WeekOfPicks;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.model.wrapper.UsernameAndPassword;
import supercontest.service.LeaderboardService;
import supercontest.service.PlayerService;
import supercontest.service.WeeklyLinesService;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class ClientController {

    private final LeaderboardService leaderboardService;
    private final PlayerService playerService;
    private final WeeklyLinesService weeklyLinesService;

    @PostMapping("/register")
    public ResponseEntity<Player> registerPlayer(@RequestBody Player player) {
        try {
            Player registeredPlayer = playerService.registerPlayer(player);
            if (registeredPlayer != null) {
                return new ResponseEntity<>(registeredPlayer, HttpStatus.OK);
            } else {
                // username is already taken
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Player> login(@RequestBody UsernameAndPassword usernameAndPassword) {
        try {
            String username = usernameAndPassword.getUsername();
            String password = usernameAndPassword.getPassword();
            Player loggedInPlayer = playerService.login(username, password);
            if (loggedInPlayer != null) {
                return new ResponseEntity<>(loggedInPlayer, HttpStatus.OK);
            } else {
                // invalid username or password
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/submitPicks")
    public ResponseEntity<Player> submitPicks(@RequestHeader("Login-Token") String loginToken,
                                              @RequestBody WeekOfPicks weekOfPicks) {
        try {
            Player playerWhoSubmittedPicks = playerService.submitPicks(loginToken, weekOfPicks);
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

    @GetMapping("/getPicks/{username}")
    public ResponseEntity<WeekOfPicks> getWeekOfPicks(@RequestHeader("Login-Token") String loginToken,
                                                      @PathVariable("username") String username,
                                                      @QueryParam("weekNumber") int weekNumber) {
        try {
            WeekOfPicks weekOfPicks = playerService.getWeekOfPicks(loginToken, weekNumber);
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

    @GetMapping("/getOverallLeaderboard")
    public ResponseEntity<List<Player>> getOverallLeaderboard() {
        try {
            List<Player> overallLeaderboard = leaderboardService.getOverallLeaderboard();
            return new ResponseEntity<>(overallLeaderboard, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getWeeklyLeaderboard/{weekNumber}")
    public ResponseEntity<List<Player>> getWeeklyLeaderboard(@PathVariable("weekNumber") int weekNumber) {
        try {
            List<Player> weeklyLeaderboard = leaderboardService.getWeeklyLeaderboard(weekNumber);
            return new ResponseEntity<>(weeklyLeaderboard, HttpStatus.OK);
        } catch (IndexOutOfBoundsException e) {
            // leaderboard not available for this week yet
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
