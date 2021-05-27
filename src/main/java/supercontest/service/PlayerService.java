package supercontest.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import supercontest.model.player.Player;
import supercontest.model.player.WeekOfPicks;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.model.wrapper.PlayerAndPicks;
import supercontest.repository.PlayerRepository;
import supercontest.repository.WeeklyLinesRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final WeeklyLinesRepository weeklyLinesRepository;

    public Player registerPlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player submitPicks(PlayerAndPicks playerAndPicks) {
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
                return null;
            }
            return playerRepository.save(player);
        } else {
            // player with provided ID not found
            return null;
        }
    }

    public WeekOfPicks getWeekOfPicks(int playerId, int weekNumber) {
        Optional<Player> playerOptional = playerRepository.findById(playerId);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            return player.getAllPicks().get(weekNumber - 1);
        } else {
            // playerId or weekNumber invalid
            return null;
        }
    }

    public List<Player> scoreAllPicks(int weekNumber) {
        List<Player> allPlayers = playerRepository.findAll();
        List<WeekOfLines> allWeeksOfLines = weeklyLinesRepository.findAll();
        allPlayers.forEach(player -> {
            if (player.getAllPicks().size() < weekNumber) {
                player.getAllPicks().add(new WeekOfPicks(weekNumber));
            }
            player.calculateSeasonScore(allWeeksOfLines);
        });
        return playerRepository.saveAll(allPlayers);
    }
}
