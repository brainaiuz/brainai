package com.finnetlimited.reportservice.core.client.ui.listbox;

import com.finnetlimited.reportservice.core.client.enumtype.IdType;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * User: ${Dilsh0d}
 * Date: 17-Mar-2010
 * Time: 15:41:08
 */
public final class DRSOrderListBox extends HTMLPanel {

    private static final String _id = IdType.ORDER_LIST_BOX.getName();
    private static final String STYLE = "act";
    private static int num = 0;

    private String id;

    public DRSOrderListBox() {
        super("");
    }
}
