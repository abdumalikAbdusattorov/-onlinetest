package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ResTestBlock {
    private UUID id;
    private ResBlock resBlock;
    private List<ResBlock> resBlockListByLevel;
    private List<ResTestWithScore> resTestWithScoreList;
}
