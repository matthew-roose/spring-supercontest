package supercontest.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import supercontest.model.player.Player;
import supercontest.repository.PlayerRepository;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class LeaderboardService {

    private final PlayerRepository playerRepository;

    public List<Player> getOverallLeaderboard() {
        return playerRepository.findAllOrderBySeasonScore();
    }

    public List<Player> getWeeklyLeaderboard(int weeklyIndex) {
        return playerRepository.findAllOrderByWeeklyScore(weeklyIndex);
    }
}
