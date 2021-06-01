package supercontest.model.wrapper;

import lombok.Data;
import supercontest.model.player.WeekOfPicks;

import java.util.UUID;

@Data
public class PlayerAndPicks {
    private UUID loginToken;
    private WeekOfPicks weekOfPicks;
}
