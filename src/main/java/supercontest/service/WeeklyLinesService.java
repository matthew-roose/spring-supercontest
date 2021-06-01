package supercontest.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import supercontest.model.weeklylines.GameLine;
import supercontest.model.weeklylines.ScoreUpdate;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.repository.WeeklyLinesRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class WeeklyLinesService {

    private final WeeklyLinesRepository weeklyLinesRepository;

    public WeekOfLines addWeekOfLines(WeekOfLines weekOfLines, int weekNumber) {
        weekOfLines.setWeekNumber(weekNumber);
        return weeklyLinesRepository.save(weekOfLines);
    }

    public WeekOfLines getWeekOfLines(int weekNumber) {
        Optional<WeekOfLines> weekOfLinesOptional = weeklyLinesRepository.findById(weekNumber);
        return weekOfLinesOptional.orElse(null);
    }

    public WeekOfLines scoreWeekOfLines(List<ScoreUpdate> scoreUpdates, int weekNumber) {
        Optional<WeekOfLines> weekOfLinesOptional = weeklyLinesRepository.findById(weekNumber);
        if (weekOfLinesOptional.isPresent()) {
            WeekOfLines weekOfLines = weekOfLinesOptional.get();
            scoreUpdates.forEach(scoreUpdate -> {
                GameLine gameLine = weekOfLines.getLinesOfTheWeek().get(scoreUpdate.getGameId() - 1);
                gameLine.setHomeTeamScore(scoreUpdate.getHomeTeamScore());
                gameLine.setAwayTeamScore(scoreUpdate.getAwayTeamScore());
            });
            return weeklyLinesRepository.save(weekOfLines);
        } else {
            return null;
        }
    }


}
