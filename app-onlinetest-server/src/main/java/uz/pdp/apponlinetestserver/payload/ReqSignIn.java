package uz.pdp.apponlinetestserver.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReqSignIn {
    @NotBlank
    private String phoneNumberOrEmail;

    @NotBlank
    private String password;
}
