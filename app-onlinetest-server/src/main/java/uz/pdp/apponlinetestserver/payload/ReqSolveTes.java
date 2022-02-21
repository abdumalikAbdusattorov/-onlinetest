package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class ReqSolveTes {
    private UUID answerId;
    private double score;
    private UUID testBlockId;
}
