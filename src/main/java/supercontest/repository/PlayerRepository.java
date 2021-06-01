package supercontest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import supercontest.model.player.Player;

public interface PlayerRepository extends MongoRepository<Player, Integer> {
}
