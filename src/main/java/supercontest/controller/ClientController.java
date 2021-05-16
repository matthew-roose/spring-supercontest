package supercontest.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import supercontest.model.weeklylines.GameLine;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.repository.WeeklyLinesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class ClientController {

    private final WeeklyLinesRepository weeklyLinesRepository;

    @GetMapping("/getLines/{weekNumber}")
    public ResponseEntity<WeekOfLines> getWeekOfLines(@PathVariable("weekNumber") String weekNumber) {
        Optional<WeekOfLines> weekOfLines = weeklyLinesRepository.findById(weekNumber);
        return weekOfLines.map(week -> new ResponseEntity<>(week, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getLines")
    public ResponseEntity<List<WeekOfLines>> getAllWeeksOfLines() {
        try {
            List<WeekOfLines> allWeeksOfLines = new ArrayList<>(weeklyLinesRepository.findAll());
            if (allWeeksOfLines.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(allWeeksOfLines, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getLines/{weekNumber}/{gameId}")
    public ResponseEntity<GameLine> getSingleGameLine(@PathVariable("weekNumber") String weekNumber, @PathVariable("gameId") int gameId) {
        Optional<WeekOfLines> weekOfLines = weeklyLinesRepository.findById(weekNumber);
        if (weekOfLines.isPresent()) {
            try {
                GameLine gameLine = weekOfLines.get().getLinesOfTheWeek().get(gameId - 1); // gameIds will start with 1 but index starts at 0
                return new ResponseEntity<>(gameLine, HttpStatus.OK);
            } catch (IndexOutOfBoundsException ex) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
