package com.finnetlimited.reportservice.core.client.ui.panel;

import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: ${Dilsh0d}
 * Date: 15-Mar-2010
 * Time: 18:21:12
 */
public class BannerPanel extends HTMLPanel {

    private static int num = 0;
    private static final String id = IdType.HELP_PANEL.getName();

    private String idName;

    public BannerPanel() {
        super("<div class='sdbr-2' id='" + (id + num) + "'>" +
                "</div>");
        idName = id + num;
        num++;
    }

    public void addWidget(Widget widget) {
        add(widget, idName);
    }
}
