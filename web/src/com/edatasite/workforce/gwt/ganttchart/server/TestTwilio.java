package com.edatasite.workforce.gwt.ganttchart.server;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.net.URISyntaxException;
import java.util.Date;

public class TestTwilio {
    private static final Logger log = LoggerFactory.getLogger("messageCenter");
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID_TEST = "ACfe921b815f674790db4d41a8307532d4";
    public static final String AUTH_TOKEN_TEST = "90aca140dc2ee88872eb1fc6851bc5f4";

    public static final String ACCOUNT_SID = "AC8f9be29bbecd385fd478f1c1f4bd7daf";
    public static final String AUTH_TOKEN = "d33a52004f283388ad90b446e5d51e99";

    public static void main(String[] args) throws URISyntaxException {
        System.out.println(new Date().getTime());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(new Date().getTime());
//        String from = "\"app.kpi.com\" test@gmail.com";
//        String fullName = "";
//        if(from != null && from.contains("\"")){
//            fullName = from.substring(from.indexOf("\"")+1, from.lastIndexOf("\""));
//            String[] e = from.split("\\s");
//            for (String e_ : e){
//                if(e_.contains("@")){
//                    from = e_;
//                }
//            }
//        }
//        System.out.println(from + "---" + fullName);
//        //        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
////
////        String from = "+12133440101";
////        String to = "+18447268446";
////
////        Call call = Call.creator(new PhoneNumber(to), new PhoneNumber(from),
////                new URI("http://demo.twilio.com/docs/voice.xml")).create();
//
////        System.out.println("rsa2154+-5847".replaceAll("[^0-9]+",""));
//        int a = 3;
//        int a1 = 2;
//        int b = a&a1;
//        System.out.println(a+"-"+a1+"-"+b);
        log.info("Let's see what is this!");
    }
}
