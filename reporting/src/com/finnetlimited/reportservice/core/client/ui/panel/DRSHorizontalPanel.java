package com.finnetlimited.reportservice.core.client.ui.panel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 25.07.2010
 * Time: 12:15:56
 */
public class DRSHorizontalPanel extends HorizontalPanel {
    public DRSHorizontalPanel(String text, Widget widget) {
        setStyleName("drs-datepicker");
        HTML textHtml = new HTML(text + ":");
        add(textHtml);
        setCellVerticalAlignment(textHtml, HasVerticalAlignment.ALIGN_MIDDLE);
        add(widget);
    }
}
