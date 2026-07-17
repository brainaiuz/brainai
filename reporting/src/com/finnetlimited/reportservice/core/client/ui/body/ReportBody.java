package com.finnetlimited.reportservice.core.client.ui.body;

import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: ${Dilsh0d}
 * Date: 11-Mar-2010
 * Time: 19:37:55
 */
public class ReportBody extends Composite {

    private static final String id = IdType.REPORT_BODY.getName();

    private static final HTMLPanel content = new HTMLPanel("<div class='content'>" +
            "<div class='content-outer'>" +
            "<div class='content-innner'>" +
            "<div class='full-main'>" +
            "<div class='full-main-outer'>" +
            "<div class='full-main-inner' id='full-main-inner'>" +
            // content
            "</div>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "</div>");

    private Widget oldWidget;

    public ReportBody() {
        initWidget(content);
    }

    public void setWidget(Widget widget) {
        oldWidget = widget;
        content.add(widget, id);
    }

    public void setWidgetAnReplace(Widget widget) {
        if (oldWidget != null) {
            content.remove(oldWidget);
        }
        oldWidget = widget;
        content.add(widget, id);
    }
}
