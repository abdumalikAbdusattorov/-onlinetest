package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class ResAnswer {
    private UUID id;
    private String answer;
    private boolean isCorrect;
}
