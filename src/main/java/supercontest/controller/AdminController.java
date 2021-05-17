package supercontest.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supercontest.model.player.Player;
import supercontest.model.weeklylines.GameLine;
import supercontest.model.weeklylines.ScoreUpdate;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.repository.PlayerRepository;
import supercontest.repository.WeeklyLinesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class AdminController {

    private final PlayerRepository playerRepository;
    private final WeeklyLinesRepository weeklyLinesRepository;

    @PostMapping("/postLines")
    public ResponseEntity<WeekOfLines> addWeekOfLines(@RequestBody WeekOfLines weekOfLines) {
        try {
            return new ResponseEntity<>(weeklyLinesRepository.save(weekOfLines), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/scoreGames/{weekNumber}")
    public ResponseEntity<WeekOfLines> scoreWeekOfLines(@RequestBody List<ScoreUpdate> scoreUpdates, @PathVariable("weekNumber") int weekNumber) {
        Optional<WeekOfLines> weekOfLinesOptional = weeklyLinesRepository.findById(weekNumber);
        if (weekOfLinesOptional.isPresent()) {
            WeekOfLines weekOfLines = weekOfLinesOptional.get();
            for (ScoreUpdate scoreUpdate : scoreUpdates) {
                GameLine gameLine = weekOfLines.getLinesOfTheWeek().get(scoreUpdate.getGameId() - 1);
                gameLine.setHomeTeamScore(scoreUpdate.getHomeTeamScore());
                gameLine.setAwayTeamScore(scoreUpdate.getAwayTeamScore());
            }
            weeklyLinesRepository.save(weekOfLines);
            return new ResponseEntity<>(weekOfLines, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/scorePicks/{weekNumber}")
    public ResponseEntity<List<Player>> scoreWeekOfPicks(@PathVariable("weekNumber") int weekNumber) {
        List<Player> allPlayers = playerRepository.findAll();
        Optional<WeekOfLines> weekOfLinesOptional = weeklyLinesRepository.findById(weekNumber);
        if (weekOfLinesOptional.isPresent()) {
            WeekOfLines weekOfLines = weekOfLinesOptional.get();
            List<WeekOfLines> weekOfLinesList = new ArrayList<>();
            weekOfLinesList.add(weekOfLines);
            for (Player player : allPlayers) {
                player.calculateSeasonScore(weekOfLinesList);
            }
            playerRepository.saveAll(allPlayers);
            return new ResponseEntity<>(allPlayers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/scorePicks")
    public ResponseEntity<List<Player>> scorePicks() {
        try {
            List<Player> allPlayers = playerRepository.findAll();
            List<WeekOfLines> allWeeksOfLines = weeklyLinesRepository.findAll();
            if (allPlayers.isEmpty() || allWeeksOfLines.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                for (Player player : allPlayers) {
                    player.calculateSeasonScore(allWeeksOfLines);
                }
                playerRepository.saveAll(allPlayers);
                return new ResponseEntity<>(allPlayers, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
