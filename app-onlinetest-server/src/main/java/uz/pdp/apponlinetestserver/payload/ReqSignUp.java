package uz.pdp.apponlinetestserver.payload;

import lombok.Data;
import uz.pdp.apponlinetestserver.entity.enums.RoleName;

import java.util.UUID;

@Data
public class ReqSignUp {
    private UUID id;

    private String phoneNumber;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private RoleName roleName;
}
