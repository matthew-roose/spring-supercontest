package supercontest.model.weeklylines;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "weekly_lines")
public class WeekOfLines {

    @Id
    private int weekNumber;
    private List<GameLine> linesOfTheWeek;

}
