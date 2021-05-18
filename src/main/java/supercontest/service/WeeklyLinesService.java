package supercontest.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<WeekOfLines> addWeekOfLines(WeekOfLines weekOfLines) {
        try {
            return new ResponseEntity<>(weeklyLinesRepository.save(weekOfLines), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<WeekOfLines> getWeekOfLines(int weekNumber) {
        Optional<WeekOfLines> weekOfLines = weeklyLinesRepository.findById(weekNumber);
        return weekOfLines.map(week ->
                new ResponseEntity<>(week, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<WeekOfLines> scoreWeekOfLines(List<ScoreUpdate> scoreUpdates, int weekNumber) {
        Optional<WeekOfLines> weekOfLinesOptional = weeklyLinesRepository.findById(weekNumber);
        if (weekOfLinesOptional.isPresent()) {
            WeekOfLines weekOfLines = weekOfLinesOptional.get();
            for (ScoreUpdate scoreUpdate : scoreUpdates) {
                GameLine gameLine = weekOfLines.getLinesOfTheWeek().get(scoreUpdate.getGameId() - 1);
                gameLine.setHomeTeamScore(scoreUpdate.getHomeTeamScore());
                gameLine.setAwayTeamScore(scoreUpdate.getAwayTeamScore());
            }
            weeklyLinesRepository.save(weekOfLines);
            return new ResponseEntity<>(weekOfLines, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
