package com.edatasite.workforce.gwt.core.client.ui.panel;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Jamshid
 * Date: Nov 2, 2010
 */

/**
 * This class can be  used instead of
 * GWT's HorizontalPanel which creates table,tr,td tags.
 */
public class HorizontalPanelDiv extends PanelDiv {

    public HorizontalPanelDiv() {
    }

    public HorizontalPanelDiv(int spacing, Widget... w) {
        add(spacing, w);
    }

    /**
     * white-space by default normal
     *
     * @param isWordWrap
     */
    public HorizontalPanelDiv(boolean isWordWrap) {
        if (isWordWrap) {
            getStyle().setProperty("whiteSpace", "noWrap");
        }
    }

    public void addField(String title, int spacing, Widget... w) {
        this.add(new HTML(title == null ? " " : title));
        for (Widget widget : w) {
            this.add(widget);
        }
        setHorizontalSpacing(spacing);
    }

    @Override
    protected String getFlowStyle() {
        return "inline";
    }

    /**
     * run this method after adding all widgets in it.
     *
     * @param space margin-right:space; in PX
     */
    public void setHorizontalSpacing(int space) {
        if (this.getWidgetCount() == 0) {
            new IllegalStateException("Please use HorizontalPanelDiv#setHorizontalSpacing(int a) after adding all Widgets!!!");
        }
        for (int i = 0; i < this.getWidgetCount() - 1; i++) {
            this.getWidget(i).getElement().getStyle().setMarginRight(space, Style.Unit.PX);
        }
    }

    /**
     * Used Builder pattern
     */
    public HorizontalPanelDiv setWidget(String title, int spacing, Widget... w) {
        addField(title, spacing, w);
        return this;
    }
}