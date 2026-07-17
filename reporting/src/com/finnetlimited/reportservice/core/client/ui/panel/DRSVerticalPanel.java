package com.finnetlimited.reportservice.core.client.ui.panel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: ${Dilsh0d}
 * Date: 17-Mar-2010
 * Time: 18:50:01
 */
public class DRSVerticalPanel extends VerticalPanel {

    public DRSVerticalPanel(String text, Widget widget) {
        setStyleName("drs-datepicker");
        add(new HTML(text + ":"));
        add(widget);
    }
}
