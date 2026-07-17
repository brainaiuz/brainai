package com.edatasite.workforce.gwt.core.client.ui.components.groupBox;

import gwt.material.design.client.ui.MaterialPanel;
@Deprecated
public class GBoxUnited extends MaterialPanel {
    private static final String STYLE_SEPARATE_TOP = "group-box--separate-top";
    @Deprecated
    public GBoxUnited() {
        super("group-box group-box--united");
    }
    @Deprecated
    public GBoxUnited(GBoxRow... rows) {
        this();
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

    public void add(GBoxRow child) {
        super.add(child);
    }
}
