package supercontest.model.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import supercontest.model.player.WeekOfPicks;

@Data
@AllArgsConstructor
public class PlayerAndPicks {

    private int playerId;
    private WeekOfPicks weekOfPicks;
}
