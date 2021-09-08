package supercontest.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supercontest.model.weeklylines.ScoreUpdate;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.service.PlayerService;
import supercontest.service.WeeklyLinesService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin")
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class AdminController {

    private final PlayerService playerService;
    private final WeeklyLinesService weeklyLinesService;

    @GetMapping("/authenticate")
    public ResponseEntity<Boolean> authenticate(@RequestHeader("Login-Token") String loginToken) {
        try {
            boolean isAdminLoginToken = playerService.authenticate(loginToken, "mattyiceeee");
            return new ResponseEntity<>(isAdminLoginToken, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/postLines/{weekNumber}")
    public ResponseEntity<WeekOfLines> addWeekOfLines(@RequestHeader("Login-Token") String loginToken,
                                                      @RequestBody WeekOfLines weekOfLines,
                                                      @PathVariable("weekNumber") int weekNumber) {
        try {
            boolean isAdminLoginToken = playerService.authenticate(loginToken, "mattyiceeee");
            if (!isAdminLoginToken) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            playerService.addNewEmptyWeekOfPicks(weekNumber);
            WeekOfLines postedWeekOfLines = weeklyLinesService.addWeekOfLines(weekOfLines, weekNumber);
            return new ResponseEntity<>(postedWeekOfLines, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("scoreGamesAndPicks/{weekNumber}")
    public ResponseEntity<WeekOfLines> scoreWeekOfLinesAndPicks(@RequestHeader("Login-Token") String loginToken,
                                                                @RequestBody List<ScoreUpdate> scoreUpdates,
                                                                @PathVariable("weekNumber") int weekNumber) {
        try {
            boolean isAdminLoginToken = playerService.authenticate(loginToken, "mattyiceeee");
            if (!isAdminLoginToken) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            WeekOfLines weekOfLines = weeklyLinesService.scoreWeekOfLines(scoreUpdates, weekNumber);
            playerService.scoreAllPicks(weekNumber);
            return new ResponseEntity<>(weekOfLines, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
