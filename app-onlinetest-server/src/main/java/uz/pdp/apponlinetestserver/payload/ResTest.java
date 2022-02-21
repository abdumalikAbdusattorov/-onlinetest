package uz.pdp.apponlinetestserver.payload;

import lombok.Data;
import uz.pdp.apponlinetestserver.entity.enums.Level;

import java.util.List;
import java.util.UUID;

@Data
public class ResTest {
    private UUID id;
    private String title;
    private Level level;
    private ResSubject resSubject;
    private List<ResQuestion> resQuestionList;
}
