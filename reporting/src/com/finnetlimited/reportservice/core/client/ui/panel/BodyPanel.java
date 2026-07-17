package com.finnetlimited.reportservice.core.client.ui.panel;

import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: ${Dilsh0d}
 * Date: 17-Mar-2010
 * Time: 16:18:05
 */
public class BodyPanel extends HTMLPanel {

    private static final String _id = IdType.BODY_PANEL.getName();
    private static int num = 0;

    private String id;

    public BodyPanel() {
        super("");
        setStyleName("overhide mainBar");
        getElement().setAttribute("id", (_id + num));
        id = _id + num;
        num++;
    }

    public void addWdget(Widget widget) {
        add(widget, id);
    }
}
