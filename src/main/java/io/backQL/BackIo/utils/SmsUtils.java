package io.backQL.BackIo.utils;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class SmsUtils {
    public static final String accoundSid = "ACb6e3b75ae27aa21d7b1eff3b5f49c513";
    public static final String fromNumber = "+19165464168";
    public static final String authToken = "f1857a024c889b1f43ca21b67a9adc06";

    public static void sendSms(String toNumber, String SmsBoddy){
        Twilio.init(accoundSid, authToken);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+48" + toNumber),
                new com.twilio.type.PhoneNumber(fromNumber),
                SmsBoddy
        ).create();
        System.out.println(message);
    }
}
