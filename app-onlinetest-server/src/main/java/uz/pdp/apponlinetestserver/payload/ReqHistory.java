package uz.pdp.apponlinetestserver.payload;

import lombok.Data;
import uz.pdp.apponlinetestserver.entity.Answer;
import uz.pdp.apponlinetestserver.entity.Block;
import uz.pdp.apponlinetestserver.entity.User;

import java.util.List;
import java.util.UUID;

@Data
public class ReqHistory {
    private UUID id;

    private User user;

    private Block block;

    private boolean fromBot;

    private List<Answer> answer;
}
