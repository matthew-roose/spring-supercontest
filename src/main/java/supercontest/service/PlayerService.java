package supercontest.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import supercontest.model.player.Player;
import supercontest.model.player.WeekOfPicks;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.model.wrapper.PlayerAndPicks;
import supercontest.repository.PlayerRepository;
import supercontest.repository.WeeklyLinesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final WeeklyLinesRepository weeklyLinesRepository;

    public ResponseEntity<Player> registerPlayer(Player player) {
        try {
            return new ResponseEntity<>(playerRepository.save(player), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Player> submitPicks(PlayerAndPicks playerAndPicks) {
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

    public ResponseEntity<WeekOfPicks> getWeekOfPicks(int playerId, int weekNumber) {
        Optional<Player> playerOptional = playerRepository.findById(playerId);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            WeekOfPicks weekOfPicks = player.getAllPicks().get(weekNumber - 1);
            return new ResponseEntity<>(weekOfPicks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Player>> scoreWeekOfPicks(int weekNumber) {
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
}
