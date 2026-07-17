package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Nov 3, 2010
 * Time: 4:54:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SmsSenderService extends RemoteService {

    Boolean smsToQueue(ArrayList<SmsSendItem> sms);

    class App {
        public static SmsSenderServiceAsync get() {
            ServiceDefTarget target = GWT.create(SmsSenderService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/smssender");
            return (SmsSenderServiceAsync) target;
        }
    }
}
