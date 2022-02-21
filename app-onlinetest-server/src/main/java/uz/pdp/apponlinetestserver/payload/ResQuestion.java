package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ResQuestion {
    private UUID id;
    private String question;
    private List<ResAnswer> resAnswers;
}
