package uz.pdp.apponlinetestserver.payload;

import lombok.Data;
import uz.pdp.apponlinetestserver.entity.enums.Level;

import java.util.List;
import java.util.UUID;

@Data
public class ReqTest {
    private UUID id;
    private String title;
    private Level level;
    private Integer subjectId;
    private List<ReqQuestion> reqQuestionList;
}
