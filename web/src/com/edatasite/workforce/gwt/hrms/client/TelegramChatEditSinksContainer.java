package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.ui.EditTelegramChatView;

import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 01.08.2017 2:10
 */
public class TelegramChatEditSinksContainer extends SinksContainer {

    public TelegramChatEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        addView(new EditTelegramChatView(id));
    }
}
