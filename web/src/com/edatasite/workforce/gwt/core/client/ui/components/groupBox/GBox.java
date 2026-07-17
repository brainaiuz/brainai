package com.edatasite.workforce.gwt.core.client.ui.components.groupBox;

import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Span;
@Deprecated
public class GBox extends MaterialPanel {

    public static final String STYLE_GBOX = "group-box";
    public static final String STYLE_SEPARATE_TOP = "group-box--separate-top";
    public static final String STYLE_UNITED = "group-box--united";
    public static final String STYLE_WIDTH_FREE = "group-box--width-free";
    public static final String STYLE_NO_PADDING = "group-box--no-padding";

    @Deprecated
    public GBox() {
        super(STYLE_GBOX);
    }

    public void setTitle(String text) {
        MaterialPanel titlePanel = new MaterialPanel("group-box__title");

        Span span = new Span();
        span.setText(text);
        titlePanel.add(span);

        this.add(titlePanel);
    }

    public GBox(GBoxRow... rows) {
        this();
        if (rows != null && rows.length > 0) {
            for (GBoxRow row : rows) {
                this.add(row);
            }
        }
    }
    public GBox(String title, GBoxRow... rows) {
        setTitle(title);
        if (rows != null && rows.length > 0) {
            for (GBoxRow row : rows) {
                this.add(row);
            }
        }
    }

    public void setStyleSeparateTop(boolean separateTop) {
        if (separateTop) {
            this.addStyleName(STYLE_SEPARATE_TOP);
        } else {
            this.removeStyleName(STYLE_SEPARATE_TOP);
        }
    }

    public void setStyleUnited(boolean hasStyle) {
        if (hasStyle) {
            this.addStyleName(STYLE_UNITED);
        } else {
            this.removeStyleName(STYLE_UNITED);
        }
    }

    public void setStyleWidthFree(boolean hasStyle) {
        if (hasStyle) {
            this.addStyleName(STYLE_WIDTH_FREE);
        } else {
            this.removeStyleName(STYLE_WIDTH_FREE);
        }
    }

    public void setStyleNoPadding(boolean hasStyle) {
        if (hasStyle) {
            this.addStyleName(STYLE_NO_PADDING);
        } else {
            this.removeStyleName(STYLE_NO_PADDING);
        }
    }

    public void add(GBoxRow child) {
        super.add(child);
    }
}
