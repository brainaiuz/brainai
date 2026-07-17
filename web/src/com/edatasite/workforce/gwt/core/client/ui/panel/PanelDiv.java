package com.edatasite.workforce.gwt.core.client.ui.panel;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid
 * Date: Nov 2, 2010
 */

public abstract class PanelDiv extends FlowPanel {
    protected abstract String getFlowStyle();

    public void add(Widget w, boolean addDisplayStyle) {
        if (addDisplayStyle) {
            add(w);
        } else {
            super.add(w);
        }
    }

    @Override
    public void add(Widget w) {
        w.getElement().getStyle().setProperty("display", getFlowStyle());
        super.add(w);
    }

    public void add(Widget w, Integer title) {
        super.add(w);
    }

    public void add(Widget w, Style.Float aFloat) {
        w.getElement().getStyle().setFloat(aFloat);
        add(w);
    }

    @Override
    public void insert(Widget w, int beforeIndex) {
        w.getElement().getStyle().setProperty("display", getFlowStyle());
        super.insert(w, beforeIndex);
    }

    public void add(int spacing, Widget... w) {
        for (int i = 0, wLength = w.length; i < wLength; i++) {
            final Widget widget = w[i];
            add(widget);
            if (i == wLength - 1 && wLength > 1)
                break;
            widget.getElement().getStyle().setMarginRight(spacing, Style.Unit.PX);
        }
    }

    public Style getStyle() {
        return getElement().getStyle();
    }

    public void replace(Widget toRemove, Widget toPut) {
        final int index = this.getWidgetIndex(toRemove);
        this.remove(toRemove);
        this.insert(toPut, index);
    }

    public void setProperty(String name, String value) {
        getStyle().setProperty(name, value);
    }

    public void setFloat(Style.Float v) {
        getStyle().setFloat(v);
    }

    public void setPosition(Style.Position p) {
        getStyle().setPosition(p);
    }

    public void setMarginLeft(int a) {
        getStyle().setMarginLeft(a, Style.Unit.PX);
    }

    public void setMarginRight(int a) {
        getStyle().setMarginRight(a, Style.Unit.PX);
    }

    public void setMarginTop(int a) {
        getStyle().setMarginTop(a, Style.Unit.PX);
    }

    public void setMarginBottom(int a) {
        getStyle().setMarginBottom(a, Style.Unit.PX);
    }

    public void setMargin(int a) {
        getStyle().setMargin(a, Style.Unit.PX);
    }

    public void setPaddingLeft(int a) {
        getStyle().setPaddingLeft(a, Style.Unit.PX);
    }

    public void setPaddingRight(int a) {
        getStyle().setPaddingRight(a, Style.Unit.PX);
    }

    public void setPaddingTop(int a) {
        getStyle().setPaddingTop(a, Style.Unit.PX);
    }

    public void setPaddingBottom(int a) {
        getStyle().setPaddingBottom(a, Style.Unit.PX);
    }

    public void setPadding(int a) {
        getStyle().setPadding(a, Style.Unit.PX);
    }


    /**
     * run this method after adding all widgets in it.
     *
     * @param space margin:space;  in PX
     */
    public void setSpacing(int space) {
        if (this.getWidgetCount() == 0)
            new IllegalStateException("Please use PanelDiv#setSpacing(int a) after adding all Widgets!!!");
        for (int i = 0; i < this.getWidgetCount() - 1; i++) {
            this.getWidget(i).getElement().getStyle().setMargin(space, Style.Unit.PX);
        }
    }

    public void setTop(double t) {
        getStyle().setTop(t, Style.Unit.PX);
    }

    public void setLeft(double t) {
        getStyle().setLeft(t, Style.Unit.PX);
    }

    public void setRight(double t) {
        getStyle().setRight(t, Style.Unit.PX);
    }

    public void setBottom(double t) {
        getStyle().setBottom(t, Style.Unit.PX);
    }

    public void setDisplay(Style.Display d) {
        getStyle().setDisplay(d);
    }

    public void clearMargin() {
        getStyle().clearMargin();
    }

    public void setTextAlign(String align) {
        getStyle().setProperty("textAlign", align);
    }
}
