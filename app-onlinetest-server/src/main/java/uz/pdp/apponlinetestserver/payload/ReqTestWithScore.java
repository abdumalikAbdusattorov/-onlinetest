package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class ReqTestWithScore {
    private UUID testId;

    private double score;
}
