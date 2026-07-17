package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioService;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioSettings;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.voice.Record;
import com.twilio.twiml.voice.Say;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Jan 29, 2011
 * Time: 5:29:39 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class TwilioVoiceXmlController {
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private TwilioService twilioService;

    @RequestMapping(value = "/twilio/voice", method = {RequestMethod.GET, RequestMethod.POST},produces = {MediaType.APPLICATION_XML_VALUE}, consumes = {MediaType.ALL_VALUE})
    public String handleRequestInternal(HttpServletRequest request, HttpServletResponse response, @RequestParam("cid") String cid, @RequestParam("number") String number, @RequestParam("To") String to) throws ServletException, IOException {
        ServerUtils.fillHostParameters(request);
        if(cid.equalsIgnoreCase("noaccount")){
            return noAccount(response, number, to);
        }
        number = EncryptionHelper.decrypt(number);
        cid = EncryptionHelper.decrypt(cid);
        String databaseType = globalAuthJdbcSpringManager.getCompanyClusterType(Integer.valueOf(cid));

        SecurityContext.getInstance().setDatabase(databaseType);
        SecurityContext.getInstance().setCompanyId(cid);
        TwilioSettings settings = null;
        try {
            settings = twilioService.getByNumber(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if(settings == null){
            return fail(response);
        }
        VoiceResponse voiceTwimlResponse = null;
        if (to != null  && !"".equalsIgnoreCase(to)) {
            to = to.replaceAll("[^0-9]", "");
            if(!to.contains("+")){
                to = "+" + to;
            }
            if (!"+".equalsIgnoreCase(to)){
                Dial.Builder dialBuilder = new Dial.Builder().callerId(settings.getNumber());

                // wrap the phone number or client name in the appropriate TwiML verb
                // by checking if the number given has only digits and format symbols
                dialBuilder = dialBuilder.number(new com.twilio.twiml.voice.Number.Builder(to).build());
//            if(to.matches("^[\\d\\+\\-\\(\\) ]+$")) {
//                dialBuilder = dialBuilder.number(new Number.Builder(to).build());
//            } else {
//                dialBuilder = dialBuilder.client(new Client.Builder(to).build());
//            }

//            voiceTwimlResponse;
                Record record = new Record.Builder().timeout(25)
                        .build();
                if(settings.isRecord()){
                    voiceTwimlResponse = new VoiceResponse.Builder()
                            .dial(dialBuilder.record(settings.isRecord() ? Dial.Record.RECORD_FROM_ANSWER : Dial.Record.DO_NOT_RECORD).build())
                            .record(settings.isRecord() ? record :null)
                            .build();
                } else {
                    voiceTwimlResponse = new VoiceResponse.Builder()
                            .dial(dialBuilder.record(Dial.Record.DO_NOT_RECORD).build())
                            .build();
                }
            }
        }
        if (voiceTwimlResponse == null){
            voiceTwimlResponse = new VoiceResponse.Builder().say(new Say.Builder("Thanks for calling, But we can't connect you with destination. Good Bye!").build()).build();
        }

        response.setHeader("Content-Type", "text/xml");
        System.out.println(voiceTwimlResponse.toXml());
        response.setStatus(200);
        response.getWriter().write(voiceTwimlResponse.toXml());
        return null;
    }

    private String noAccount(HttpServletResponse response, String number, String to) throws IOException {
        String s = """
                <?xml version="1.0" encoding="UTF-8"?>
                <Response>
                    <Say voice="woman">Please ask kpi support for more information about setting a call account.! Bye</Say>
                </Response>
                """;
        System.out.println(s);
        response.setHeader("Content-Type", "text/xml");
        response.setStatus(200);
        response.getWriter().write(s);
        return null;
    }

    private String fail(HttpServletResponse response) throws IOException {// we need to generate ERROR occurred XML
//        response.setHeader("Content-Type", "text/xml");
        String s = """
                <?xml version="1.0" encoding="UTF-8"?>
                <Response>
                    <Say voice="woman">Please check your settings in kpi.com. Bye!</Say>
                </Response>
                """;
        System.out.println(s);
        response.setHeader("Content-Type", "text/xml");
        response.setStatus(200);
        response.getWriter().write(s);
        return null;
    }
}
