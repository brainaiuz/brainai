package com.edatasite.workforce.gwt.core.client.ui.dynamicTable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class TotalTable extends Composite {
    interface TotalTableUiBinder extends UiBinder<HTMLPanel, TotalTable> {
    }

    private static TotalTable.TotalTableUiBinder ourUiBinder = GWT.create(TotalTable.TotalTableUiBinder.class);
    @UiField
    TableSectionElement totalBody;

    public TotalTable() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void addItem(Widget label, Widget value) {
        label.setStyleName("totallabel");
        value.setStyleName("totalvalue");

        Element tr = DOM.createTR();

        Element tdLabel = DOM.createTD();
        tdLabel.appendChild(label.getElement());

        Element tdValue = DOM.createTD();
        tdValue.appendChild(value.getElement());

        tr.appendChild(tdLabel);
        tr.appendChild(tdValue);

        totalBody.appendChild(tr);
    }

    public void addItem(String label, String value) {
        addItem(new HTML(label), new HTML(value));
    }

    public void addItem(String label, Widget value) {
        addItem(new HTML(label), value);
    }

    public TotalTable addWidgetsInARow(Widget... widgets) {
        if (widgets != null && widgets.length > 0) {
            Element tr = DOM.createTR();
            for (Widget widget : widgets) {
                Element td = DOM.createTD();
                td.getStyle().setPaddingLeft(5, Style.Unit.PX);
                td.getStyle().setPaddingRight(5, Style.Unit.PX);
                td.appendChild(widget.getElement());
                tr.appendChild(td);
            }
            totalBody.appendChild(tr);
        }
        return TotalTable.this;
    }

    public void clear() {
        totalBody.removeAllChildren();
    }
}
