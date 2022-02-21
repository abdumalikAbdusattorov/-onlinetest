package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class ReqComment {
    private UUID id;

    private UUID questionId;

    private String commentText;

}
