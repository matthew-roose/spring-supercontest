package supercontest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import supercontest.model.weeklylines.WeekOfLines;

public interface WeeklyLinesRepository extends MongoRepository<WeekOfLines, String> {
}
