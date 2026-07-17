package com.finnetlimited.reportservice.core.client.ui.button;

import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;


/**
 * User: ${Dilsh0d}
 * Date: 13-Mar-2010
 * Time: 15:27:06
 */
public class DRSButton extends WfmButton2 {

    public static final String ADD_REPORT_STYLE = "";
    public static final String PREV_STYLE = "left";
    public static final String NEXT_STYLE = "right";
    public static final String BUTTON_STYLE = "right";


    public DRSButton(String text, String style) {
        super(text);
        addStyleName(style);
    }
}
