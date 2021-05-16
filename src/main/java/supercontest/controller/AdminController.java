package supercontest.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supercontest.model.weeklylines.WeekOfLines;
import supercontest.repository.WeeklyLinesRepository;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class AdminController {

    private final WeeklyLinesRepository weeklyLinesRepository;

    @PostMapping("/addLines")
    public ResponseEntity<WeekOfLines> addWeekOfLines(@RequestBody() WeekOfLines weekOfLines) {
        try {
            return new ResponseEntity<>(weeklyLinesRepository.save(weekOfLines), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
