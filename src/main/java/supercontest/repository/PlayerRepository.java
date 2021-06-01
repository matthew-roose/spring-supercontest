package supercontest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import supercontest.model.player.Player;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends MongoRepository<Player, String> {
    Optional<Player> findByLoginToken(UUID loginToken);
    Optional<Player> findByUsername(String username);
}
