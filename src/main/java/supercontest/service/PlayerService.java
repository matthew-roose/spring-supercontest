package supercontest.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import supercontest.model.player.Pick;
import supercontest.model.player.Player;
import supercontest.model.player.WeekOfPicks;
import supercontest.model.weeklylines.GameLine;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.repository.PlayerRepository;
import supercontest.repository.WeeklyLinesRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final WeeklyLinesRepository weeklyLinesRepository;

    public Player registerPlayer(Player player) {
        Optional<Player> existingPlayer = playerRepository.findByUsername(player.getUsername());
        if (existingPlayer.isPresent()) {
            // username is taken
            return null; // or throw exception?
        } else {
            int numberOfWeeksAlreadyPlayed = weeklyLinesRepository.findAll().size();
            // catch the player up if they are joining during the season
            for (int i = 1; i <= numberOfWeeksAlreadyPlayed; i++) {
                player.getAllPicks().add(new WeekOfPicks(i));
            }
            // safe to save using this username
            return playerRepository.save(player);
        }
    }

    public Player login(String username, String password) {
        Optional<Player> playerOptional = playerRepository.findByUsername(username);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            if (player.getPassword().equals(password)) {
                // set new login token
                player.setLoginToken(UUID.randomUUID().toString());
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

    public Player submitPicks(String loginToken, WeekOfPicks weekOfPicks) {
        if (weekOfPicks.getPicks().size() > 5) {
            return null;
        }
        Optional<Player> playerOptional = playerRepository.findByLoginToken(loginToken);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            int weekNumberIndex = weekOfPicks.getWeekNumber() - 1;
            List<Pick> currentListOfPicks = player.getAllPicks().get(weekNumberIndex).getPicks();
            List<GameLine> officialGameLines = weeklyLinesRepository.findAll().get(weekNumberIndex).getLinesOfTheWeek();
            for (Pick pick : weekOfPicks.getPicks()) {
                long gameTime = officialGameLines.get(pick.getGameId() - 1).getGameTime();
                // don't submit picks if any pick is expired AND NOT already submitted
                if (gameTime < Instant.now().toEpochMilli() &&
                        currentListOfPicks.stream().noneMatch(
                                existingPick -> existingPick.getGameId() == pick.getGameId())) {
                    return null;
                }
            }
            List<WeekOfPicks> allPicks = player.getAllPicks();
            allPicks.set(weekNumberIndex, weekOfPicks);
            return playerRepository.save(player);
        } else {
            // player with provided ID not found
            return null;
        }
    }

    public WeekOfPicks getWeekOfPicks(String username, int weekNumber) {
        Optional<Player> playerOptional = playerRepository.findByUsername(username);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            if (player.getAllPicks().size() < weekNumber) {
                // no picks yet
                return new WeekOfPicks(weekNumber);
            } else {
                return player.getAllPicks().get(weekNumber - 1);
            }
        } else {
            // playerId invalid
            return null;
        }
    }

    public void scoreAllPicks() {
        List<Player> allPlayers = playerRepository.findAll();
        List<WeekOfLines> allWeeksOfLines = weeklyLinesRepository.findAll();
        allPlayers.forEach(player -> {
            player.calculateSeasonScore(allWeeksOfLines);
        });
        playerRepository.saveAll(allPlayers);
    }

    public boolean authenticate(String loginToken, String username) {
        if (loginToken == null || username == null) {
            return false;
        }
        Optional<Player> playerOptional = playerRepository.findByLoginToken(loginToken);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            // the loginToken presented matches the username they are authenticating for
            return player.getUsername().equals(username);
        }
        // invalid loginToken was presented
        return false;
    }

    public void addNewEmptyWeekOfPicks(int weekNumber) {
        List<Player> allPlayers = playerRepository.findAll();
        allPlayers.forEach(player -> {
            player.getAllPicks().add(new WeekOfPicks(weekNumber));
        });
        playerRepository.saveAll(allPlayers);
    }
}
