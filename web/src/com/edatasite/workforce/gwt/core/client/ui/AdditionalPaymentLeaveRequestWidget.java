package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
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
import java.util.Optional;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.END_OF_SERVICE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.OVERTIME;

public class AdditionalPaymentLeaveRequestWidget extends KpiSideNavBox {

    private final DataGrid<SelectItem> dataGrid;
    private final ListDataProvider<SelectItem> dataProvider;
    public static final ProvidesKey<SelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId();
    private final String type;
    private final boolean isFromLeaveRequest;


    public AdditionalPaymentLeaveRequestWidget(SelectItem items, String type, boolean isFromLeaveRequest) {
        super(WIDE_FORM_WIDTH);
        this.type = type;
        this.isFromLeaveRequest = isFromLeaveRequest;

        dataProvider = new ListDataProvider<>();
        dataGrid = new DataGrid<>();
        dataGrid.setWidth("100%");
        dataGrid.setHeight("510px");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataProvider.addDataDisplay(dataGrid);
        initializeColumns();

        List<SelectItem> list = dataProvider.getList();
        list.add(items);
        dataProvider.refresh();

        Heading header = new Heading(HeadingSize.H1);
        header.setText("Link");
        addHeader(header);
        addBody(dataGrid);
    }

    private void initializeColumns() {

        SimpleLinkCell cell = new SimpleLinkCell();
        Column<SelectItem, String> number = new Column<SelectItem, String>(cell) {
            @Override
            public String getValue(SelectItem object) {
                cell.setClickHandler(e -> {

                    if (object.getName().trim().length() > 0) {
                        switch (type) {
                            case OVERTIME:
                                Utils.openURL("Payroll.html#overtime|summary/" + object.getId() + "/" + object.getName() + "/" + Optional.ofNullable(object.getDescription()).orElse(wfmStrings.add() + " " + wfmStrings.overtime()));
                            case END_OF_SERVICE:
                                Utils.openURL("Hrms.html#itemList" + "|summary/" + object.getId() + "/" + "18/" + object.getName() + "/" + object.getDescription());
                            case "MATERIALJNAYA_POMOSCHJ._FORM":
                                Utils.openURL("Hrms.html#itemList" + "|summary/" + object.getId() + "/" + "20/" + object.getName() + "/" + object.getDescription());
                            default:
                                Utils.openURL("Hrms.html#leaverequest/" + object.getId() + "/" + object.getName() + "/" + object.getName());
                        }
                    }
                    close();
                });

                return isFromLeaveRequest ? object.getName() : String.valueOf(object.getId());
            }
        };
        dataGrid.addColumn(number, wfmStrings.number());
        dataGrid.setColumnWidth(number, 25, Style.Unit.PCT);

        //date
        Column<SelectItem, String> date = new Column<SelectItem, String>(new TextCell()) {
            @Override
            public String getValue(final SelectItem object) {
                return object.getDescription() != null ? object.getDescription() : "";
            }
        };
        dataGrid.addColumn(date, wfmStrings.date());
        dataGrid.setColumnWidth(date, 20, Style.Unit.PCT);

        //type
        Column<SelectItem, String> from = new Column<SelectItem, String>(new TextCell()) {
            @Override
            public String getValue(SelectItem object) {
                return isFromLeaveRequest ? "Leave Request" : type;
            }
        };
        dataGrid.addColumn(from, wfmStrings.type());
        dataGrid.setColumnWidth(from, 20, Style.Unit.PCT);

        //amount
//        Column<NewInvoice, String> amount = new Column<NewInvoice, String>(new TextCell()) {
//            @Override
//            public String getValue(NewInvoice invoice) {
//                return invoice.getTotal() != null ? AccountingUtils.get().formatPrice(invoice.getTotal()) : "";
//            }
//        };
//        dataGrid.addColumn(amount, InvoiceStrings.App.get().invoiceAmount());
//        dataGrid.setColumnWidth(amount, 20, com.google.gwt.dom.client.Style.Unit.PCT);
//
//        //status
//        Column<NewInvoice, String> status = new Column<NewInvoice, String>(new TextCell()) {
//            @Override
//            public String getValue(NewInvoice invoice) {
//                return invoice.getStatus();
//            }
//        };
//        dataGrid.addColumn(status, wfmStrings.status());
//        dataGrid.setColumnWidth(status, 15, com.google.gwt.dom.client.Style.Unit.PCT);
    }

}
