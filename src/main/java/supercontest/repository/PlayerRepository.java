package supercontest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import supercontest.model.player.Player;
import supercontest.model.player.PlayerDoc;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends MongoRepository<PlayerDoc, String> {

    Optional<PlayerDoc> findByLoginToken(UUID loginToken);

    default Optional<Player> findPlayerByLoginToken(UUID loginToken) {
        return findByLoginToken(loginToken).map(PlayerDoc::getPlayer);
    }

    default Optional<Player> findPlayerByUsername(String username) {
        return findById(username).map(PlayerDoc::getPlayer);
    }

    default PlayerDoc save(Player player) {
        PlayerDoc toSave = new PlayerDoc();
        toSave.setLoginToken(player.getLoginToken());
        toSave.setUsername(player.getUsername());
        toSave.setPlayer(player);
        return save(toSave);
    }
}
