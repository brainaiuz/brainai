package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Nov 3, 2010
 * Time: 4:56:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SmsSenderServiceAsync {
    void smsToQueue(ArrayList<SmsSendItem> sms, AsyncCallback<Boolean> async);
}
