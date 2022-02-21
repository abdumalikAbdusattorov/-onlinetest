package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ReqHistoryUser {

    private UUID testBlockId;

    private boolean fromBot;

    private List<UUID> answerId;

    private double totalScore;
}
