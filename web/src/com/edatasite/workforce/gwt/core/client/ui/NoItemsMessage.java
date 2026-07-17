package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 07.04.2009
 * Time: 12:35:25
 * To change this template use File | Settings | File Templates.
 */
public abstract class NoItemsMessage {

    public String getMessageAsString() {
        return null;
    }

    public Widget getMessageAsWidget() {
        return null;
    }

    public String getAddNewLinkAsString() {
        return null;
    }

    public SimpleLink getAddNewLinkAsWidget() {
        return null;
    }


    public abstract VerticalPanel getWholeMessage();
}