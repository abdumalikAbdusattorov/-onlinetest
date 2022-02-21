package uz.pdp.apponlinetestserver.service;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accauntSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.trial.number}")
    private String trialNumber;

    public int sendMessage(String phoneNumber) {
        Twilio.init(accauntSid, authToken);

        phoneNumber = phoneNumber.startsWith("+") ? phoneNumber.replace(" ", "") : "+" + phoneNumber.replace(" ", "");
        if (phoneNumber.length() < 13) {
            return 0;
        }
        int code = generateRandom();

        Message message = Message
                .creator(new PhoneNumber(phoneNumber), // to
                        new PhoneNumber(trialNumber), // from
                        "Hi manga SMS kevottimi opa:" + code)
                .create();
        return code;

    }

    public int generateRandom() {
        return new Random().nextInt((999999 - 100000) + 1) + 100000;
    }
}
