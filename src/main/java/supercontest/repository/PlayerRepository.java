package supercontest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import supercontest.model.player.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public interface PlayerRepository extends MongoRepository<Player, String> {
    Optional<Player> findByLoginToken(UUID loginToken);
    Optional<Player> findByUsername(String username);

    default List<Player> findAllOrderBySeasonScore() {
        Comparator<Player> seasonScoreComparator =
                Comparator.comparing(Player::getSeasonScore);
        return findAll()
                .stream()
                .sorted(seasonScoreComparator.reversed())
                .collect(Collectors.toList());
    }

    default List<Player> findAllOrderByWeeklyScore(int weeklyIndex) {
        Comparator<Player> weeklyScoreComparator =
                Comparator.comparing(player -> player.getAllPicks().get(weeklyIndex - 1).getWeeklyScore());
        return findAll()
                .stream()
                .sorted(weeklyScoreComparator.reversed())
                .collect(Collectors.toList());
    }
}
