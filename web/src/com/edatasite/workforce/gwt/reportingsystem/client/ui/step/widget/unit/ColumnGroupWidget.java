package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.TableRpc;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;

public class ColumnGroupWidget extends Composite {
    interface ColumnGroupUiBinder extends UiBinder<ListItem, ColumnGroupWidget> {
    }

    private static ColumnGroupUiBinder ourUiBinder = GWT.create(ColumnGroupUiBinder.class);
    @UiField
    Div checkboxes;
    @UiField
    Span titleSpan;
    @UiField
    Div header;
    @UiField
    KpiCheckBox selectAllWidget;

    private ListItem li;

    private TableRpc group;

    public ColumnGroupWidget(TableRpc group) {
        li = ourUiBinder.createAndBindUi(this);
        initWidget(li);
        this.group = group;
        titleSpan.setText(this.group.getTableName());
        init();
    }

    private void init() {
        selectAllWidget.addClickHandler((event) -> {
            event.stopPropagation();
            selectAll(selectAllWidget.getValue());
        });
        header.addClickHandler(clickEvent -> {
            if (header.getStyleName().contains("active")) {
                header.removeStyleName("active");
                li.removeStyleName("active");
            } else {
                header.addStyleName("active");
                li.addStyleName("active");
            }
        });
    }

    public void selectAll(boolean value) {
        for (Widget checkbox : checkboxes.getChildrenList()) {
            if (((SelectColumnItem) checkbox).isEnabled()) {
                ((SelectColumnItem) checkbox).check(value, false);
            }
        }
        selectAllWidget.setValue(value);

    }

    public void timeToSelectAll() {
        boolean allSelected = true;
        for (Widget checkbox : checkboxes.getChildrenList()) {
            allSelected &= ((SelectColumnItem) checkbox).getValue();
        }
        selectAllWidget.setValue(allSelected);
    }

    public void setActive(boolean value) {
        if (value) {
            header.addStyleName("active");
            li.addStyleName("active");
        } else {
            header.removeStyleName("active");
            li.removeStyleName("active");
        }
    }

    public SelectColumnItem addField(ColumnRpc columnRpc) {
        SelectColumnItem columnItem = new SelectColumnItem(columnRpc);
        checkboxes.add(columnItem);
        return columnItem;
    }

}
