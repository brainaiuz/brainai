package com.edatasite.workforce.gwt.core.client.ui.panel;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid
 * Date: Nov 2, 2010
 */


/**
 * This class can be  used instead of GWT's
 * VerticalPanel which creates table,tr,td tags.
 */

public class VerticalPanelDiv extends PanelDiv {
    public VerticalPanelDiv() {
        super();
    }

    public VerticalPanelDiv(int spacing, Widget... w) {
        super();
        add(spacing, w);
    }

    @Override
    public void add(int spacing, Widget... w) {
        for (int i = 0, wLength = w.length; i < wLength; i++) {
            final Widget widget = w[i];
            add(widget);
            if (i == wLength - 1 && wLength > 1)
                break;
            widget.getElement().getStyle().setMarginBottom(spacing, Style.Unit.PX);
        }
    }

    @Override
    protected String getFlowStyle() {
        return "block";
    }

    /**
     * run this method after adding all widgets in it.
     *
     * @param space margin-bottom:space;  in PX
     */
    public void setVerticalSpacing(int space) {
        if (this.getWidgetCount() == 0)
            new IllegalStateException("Please use VerticalPanelDiv#setVerticalSpacing(int a) after adding all Widgets!!!");
        for (int i = 0; i < this.getWidgetCount() - 1; i++) {
            this.getWidget(i).getElement().getStyle().setMarginBottom(space, Style.Unit.PX);
        }
    }
}
