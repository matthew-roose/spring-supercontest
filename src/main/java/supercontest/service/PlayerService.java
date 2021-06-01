package supercontest.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import supercontest.model.player.Player;
import supercontest.model.player.PlayerDoc;
import supercontest.model.player.WeekOfPicks;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.model.wrapper.PlayerAndPicks;
import supercontest.repository.PlayerRepository;
import supercontest.repository.WeeklyLinesRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final WeeklyLinesRepository weeklyLinesRepository;

    public Player registerPlayer(Player player) {
        Optional<Player> existingPlayer = playerRepository.findPlayerByUsername(player.getUsername());
        if (existingPlayer.isPresent()) {
            // username is taken
            return null; // or throw exception?
        } else {
            // safe to save using this username
            PlayerDoc playerDoc = playerRepository.save(player);
            return playerDoc.getPlayer();
        }
    }

    public Player login(String username, String password) {
        Optional<Player> playerOptional = playerRepository.findPlayerByUsername(username);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            if (player.getPassword().equals(password)) {
                // set new login token
                player.setLoginToken(UUID.randomUUID());
                playerRepository.save(player);
                // client will use the UUID in subsequent calls
                return player;
            } else {
                // wrong password
                return null; // or throw exception?
            }
        } else {
            // username doesn't exist
            return null; // or throw exception?
        }
    }

    public Player submitPicks(PlayerAndPicks playerAndPicks) {
        WeekOfPicks weekOfPicks = playerAndPicks.getWeekOfPicks();
        Optional<Player> playerOptional = playerRepository.findPlayerByLoginToken(playerAndPicks.getLoginToken());
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
            PlayerDoc playerDoc = playerRepository.save(player);
            return playerDoc.getPlayer();
        } else {
            // player with provided ID not found
            return null;
        }
    }

    public WeekOfPicks getWeekOfPicks(UUID loginToken, int weekNumber) {
        Optional<Player> playerOptional = playerRepository.findPlayerByLoginToken(loginToken);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            return player.getAllPicks().get(weekNumber - 1);
        } else {
            // playerId or weekNumber invalid
            return null;
        }
    }

    public List<Player> scoreAllPicks(int weekNumber) {
        List<PlayerDoc> allPlayerDocs = playerRepository.findAll();
        List<WeekOfLines> allWeeksOfLines = weeklyLinesRepository.findAll();
        allPlayerDocs.forEach(playerDoc -> {
            Player player = playerDoc.getPlayer();
            if (player.getAllPicks().size() < weekNumber) {
                player.getAllPicks().add(new WeekOfPicks(weekNumber));
            }
            player.calculateSeasonScore(allWeeksOfLines);
        });
        playerRepository.saveAll(allPlayerDocs);
        return allPlayerDocs.stream().map(PlayerDoc::getPlayer).collect(Collectors.toList());
    }
}
