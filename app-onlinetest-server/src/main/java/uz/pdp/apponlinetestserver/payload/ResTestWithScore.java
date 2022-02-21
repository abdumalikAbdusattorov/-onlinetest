package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ResTestWithScore {
    private UUID id;
    private ResTest resTest;
    private List<ResTest> resTestListBySubjectAndLevel;
    private double score;
}
