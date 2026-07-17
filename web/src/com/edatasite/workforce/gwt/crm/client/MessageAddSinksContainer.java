package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddMessageView;
import com.edatasite.workforce.gwt.crm.client.ui.view.sms.AddSmsMessageView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Date: 29.01.2010
 * Time: 17:12:56
 * To change this template use File | Settings | File Templates.
 */
public class MessageAddSinksContainer extends SinksContainer {

    public MessageAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        Integer objectID = params != null && params.length > 1 && params[1] != null && params[1].matches(Constants.REGEX_INTEGER) ? Integer.parseInt(params[1]) : null;
        boolean isSmsMessage = params != null && params.length > 2 && params[2] != null && "true".equals(params[2]);
        boolean copy = params != null && params.length > 3 && params[3] != null && "copy".equals(params[3]);
        Integer campaignID = params != null && params.length > 4 && params[4] != null && params[4].matches(Constants.REGEX_INTEGER) ? Integer.parseInt(params[4]) : null;
        String campaignName = params != null && params.length > 5 ? params[5] : null;
        if (isSmsMessage) {
            addView(new AddSmsMessageView(objectID, campaignID, campaignName, copy));
        } else {
            addView(new AddMessageView(objectID, campaignID, campaignName, copy));
        }
    }
}
