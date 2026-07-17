package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.ui.view.*;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Date: 29.01.2010
 * Time: 17:13:30
 * To change this template use File | Settings | File Templates.
 */
public class MessageViewSinksContainer extends SinksContainer {

    public MessageViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new MessageSummaryView(id, params.length > 1 && "sms".equals(params[1]), params.length > 2 && "true".equals(params[2])));
        if (params.length > 2 && "true".equals(params[2])) {
            addView(new BouncedMessagesListView(id));
            addView(new UnsubscribedListView(id));
            addView(new MessageTrackListView(id));
            addView(new MessageClickTrackListView(id));
        }
    }
}
