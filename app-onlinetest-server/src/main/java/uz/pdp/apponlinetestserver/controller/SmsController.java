package uz.pdp.apponlinetestserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.apponlinetestserver.payload.Reqsms;
import uz.pdp.apponlinetestserver.service.TwilioService;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    TwilioService twilioService;

    @PostMapping("/smsCode")
    public int sendSms(@RequestBody Reqsms reqsms){
        return twilioService.sendMessage(reqsms.getPhoneNumber());
    }
}
