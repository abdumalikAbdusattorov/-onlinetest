package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ResHistoryUser {
    private Timestamp createdAt;
    private ResUser resUser;
    private ResTestBlock resTestBlock;
    private List<ResAnswer> resAnswerList;
    private boolean fromBot;
    private double totalScore;
    private double maxScore;
}
