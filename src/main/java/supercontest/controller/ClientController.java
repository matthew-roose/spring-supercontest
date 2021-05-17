package supercontest.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supercontest.model.player.Player;
import supercontest.model.player.WeekOfPicks;
import supercontest.model.weeklylines.GameLine;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.model.wrapper.PlayerAndPicks;
import supercontest.repository.PlayerRepository;
import supercontest.repository.WeeklyLinesRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class ClientController {

    private final PlayerRepository playerRepository;
    private final WeeklyLinesRepository weeklyLinesRepository;

    @PostMapping("/register")
    public ResponseEntity<Player> registerPlayer(@RequestBody Player player) {
        try {
            return new ResponseEntity<>(playerRepository.save(player), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/submitPicks")
    public ResponseEntity<Player> submitPicks(@RequestBody PlayerAndPicks playerAndPicks) {
        WeekOfPicks weekOfPicks = playerAndPicks.getWeekOfPicks();
        Optional<Player> playerOptional = playerRepository.findById(playerAndPicks.getPlayerId());
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            List<WeekOfPicks> allPicks = player.getAllPicks();
            if (allPicks.size() == weekOfPicks.getWeekNumber() - 1) {
                // making picks for this week for the first time
                allPicks.add(weekOfPicks);
            } else if (allPicks.size() == weekOfPicks.getWeekNumber()) {
                // changing picks for the current week
                allPicks.set(weekOfPicks.getWeekNumber() - 1, weekOfPicks);
            } else {
                // invalid week
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            playerRepository.save(player);
            return new ResponseEntity<>(player, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getPicks/{weekNumber}")
    public ResponseEntity<WeekOfPicks> getWeekOfPicks(@RequestBody int playerId, @PathVariable("weekNumber") int weekNumber) {
        Optional<Player> playerOptional = playerRepository.findById(playerId);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            WeekOfPicks weekOfPicks = player.getAllPicks().get(weekNumber - 1);
            return new ResponseEntity<>(weekOfPicks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getPicks")
    public ResponseEntity<List<WeekOfPicks>> getAllWeeksOfPicks(@RequestBody int playerId) {
        Optional<Player> playerOptional = playerRepository.findById(playerId);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            return new ResponseEntity<>(player.getAllPicks(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getLines/{weekNumber}")
    public ResponseEntity<WeekOfLines> getWeekOfLines(@PathVariable("weekNumber") Integer weekNumber) {
        Optional<WeekOfLines> weekOfLines = weeklyLinesRepository.findById(weekNumber);
        return weekOfLines.map(week -> new ResponseEntity<>(week, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getLines")
    public ResponseEntity<List<WeekOfLines>> getAllWeeksOfLines() {
        try {
            List<WeekOfLines> allWeeksOfLines = weeklyLinesRepository.findAll();
            if (allWeeksOfLines.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(allWeeksOfLines, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getLines/{weekNumber}/{gameId}")
    public ResponseEntity<GameLine> getSingleGameLine(@PathVariable("weekNumber") Integer weekNumber, @PathVariable("gameId") int gameId) {
        Optional<WeekOfLines> weekOfLines = weeklyLinesRepository.findById(weekNumber);
        if (weekOfLines.isPresent()) {
            try {
                GameLine gameLine = weekOfLines.get().getLinesOfTheWeek().get(gameId - 1); // gameIds will start with 1 but index starts at 0
                return new ResponseEntity<>(gameLine, HttpStatus.OK);
            } catch (IndexOutOfBoundsException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
