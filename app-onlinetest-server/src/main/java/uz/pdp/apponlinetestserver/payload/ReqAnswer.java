package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class ReqAnswer {
    private UUID id;
    private String answer;
    private boolean correctAnswer;
}
