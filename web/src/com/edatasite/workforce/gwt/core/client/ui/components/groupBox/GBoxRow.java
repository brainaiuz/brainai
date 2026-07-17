package com.edatasite.workforce.gwt.core.client.ui.components.groupBox;

import gwt.material.design.client.ui.MaterialPanel;

@Deprecated
public class GBoxRow extends MaterialPanel {
    @Deprecated
    public GBoxRow() {
        super("group-box__items");
    }
    @Deprecated
    public GBoxRow(GBoxItem... items) {
        this();
        if (items !=  null && items.length > 0) {
            for (GBoxItem item : items) {
                this.add(item);
            }
        }
    }

    public void add(GBoxItem child) {
        super.add(child);
    }

    public void add(GBoxDatePeriodItem child) {
        super.add(child);
    }
}
