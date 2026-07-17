package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LinkInformation {

    private HorizontalAlignmentConstant horizontalAlignment;
    private Widget link;

    public LinkInformation(HorizontalAlignmentConstant horizontalAlignment, Widget link) {
        this.setHorizontalAlignment(horizontalAlignment);
        this.link = link;
    }

    public Widget getLink() {
        return link;
    }

    public void setLink(Widget link) {
        this.link = link;
    }

    public void setHorizontalAlignment(HorizontalAlignmentConstant horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public HorizontalAlignmentConstant getHorizontalAlignment() {
        return horizontalAlignment == null ? HorizontalPanel.ALIGN_CENTER : horizontalAlignment;
    }

}
