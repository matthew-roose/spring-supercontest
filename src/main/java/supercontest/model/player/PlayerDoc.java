package supercontest.model.player;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document(collection = "players")
public class PlayerDoc {
    @Id
    private String username;
    private UUID loginToken;
    private Player player;
}
