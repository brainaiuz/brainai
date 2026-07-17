package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsSmsMessage;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 22.08.2012
 * Time: 18:07:27
 * To change this template use File | Settings | File Templates.
 */
public interface SmsSenderServiceLocal {
    void processSmsSender(EdsSmsMessage message) throws Exception;

    Boolean smsToQueue(ArrayList<SmsSendItem> smss);
}
