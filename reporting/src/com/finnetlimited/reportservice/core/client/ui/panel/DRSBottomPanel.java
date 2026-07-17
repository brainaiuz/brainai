package com.finnetlimited.reportservice.core.client.ui.panel;

import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.finnetlimited.reportservice.core.client.ui.button.DRSButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * User: ${Dilsh0d}
 * Date: 15-Mar-2010
 * Time: 14:32:21
 */
public class DRSBottomPanel extends HTMLPanel {

    private static final String id = IdType.BOTTOM_PANEL.getName();

    public DRSBottomPanel() {
        super("<div class='slippery' " + ("report".equals(GWT.getModuleName()) ? "style='bottom:0px !important'" : "") + ">" +
                "</div>" +
                "<div class='slippery-2' id='button-panel-inner' " + ("report".equals(GWT.getModuleName()) ? "style='bottom:8px !important;'" : "style='left:2.8%'") + ">" +
                // button panel content
                "</div>");
    }

    public void addButton(DRSButton anchor) {
        add(anchor, id);
    }
}
