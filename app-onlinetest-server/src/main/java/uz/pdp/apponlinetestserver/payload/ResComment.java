package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class ResComment {
    private UUID id;
    private String commentText;
    private boolean viewed;
    private ResUser resUser;
    private ResQuestion resQuestion;
}
