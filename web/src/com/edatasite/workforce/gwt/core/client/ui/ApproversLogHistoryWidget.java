package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.util.List;

public class ApproversLogHistoryWidget extends KpiSideNavBox {
    private final DataGrid<SelectItem> dataGrid;
    private final ListDataProvider<SelectItem> dataProvider;
    public static final ProvidesKey<SelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId();

    public ApproversLogHistoryWidget(List<SelectItem> items) {
        super(WIDE_FORM_WIDTH);

        dataProvider = new ListDataProvider<>();
        dataGrid = new DataGrid<>();
        dataGrid.setWidth("100%");
        dataGrid.setHeight("510px");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataProvider.addDataDisplay(dataGrid);
        initializeColumns();

        List<SelectItem> list = dataProvider.getList();
        list.addAll(items);
        dataProvider.refresh();

        Heading header = new Heading(HeadingSize.H1);
        header.setText("History");
        addHeader(header);
        addBody(dataGrid);
    }

    private void initializeColumns() {

        Column<SelectItem, String> empName = new Column<SelectItem, String>(new TextCell()) {
            @Override
            public String getValue(SelectItem object) {
                    return object.getName() != null ? object.getName() : "";

            };
        };
        dataGrid.addColumn(empName, wfmStrings.name());
        dataGrid.setColumnWidth(empName, 25, com.google.gwt.dom.client.Style.Unit.PCT);

        //date
        Column<SelectItem, String> date = new Column<SelectItem, String>(new TextCell()) {
            @Override
            public String getValue(final SelectItem object) {
                if (object.getDate() != null) {
                    return DateUtils.format(object.getDate());
                }
                return "";
            }

        };
        dataGrid.addColumn(date, wfmStrings.date());
        dataGrid.setColumnWidth(date, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        //type
        Column<SelectItem, String> type = new Column<SelectItem, String>(new TextCell()) {
            @Override
            public String getValue(SelectItem object) {
                return object.getDescription() != null ? object.getDescription() : "";
            }
        };
        dataGrid.addColumn(type, wfmStrings.type());
        dataGrid.setColumnWidth(type, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        //Value
        Column<SelectItem, String> value = new Column<SelectItem, String>(new TextCell()) {
            @Override

            public String getValue(SelectItem object) {
                return object.getParam() != null ? object.getParam() : "";
            }
        };
        dataGrid.addColumn(value, wfmStrings.value());
        dataGrid.setColumnWidth(value, 40, com.google.gwt.dom.client.Style.Unit.PCT);

    }
}
