package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ReqTestBlock {
    private UUID id;

    private List<ReqTestWithScore> reqTestWithScores;

    private Integer blockId;

}
