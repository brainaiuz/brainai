package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.BillOfEntry;
import com.edatasite.workforce.gwt.invoice.client.rpc.BillOfEntryItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Label;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class BillOfEntrySummaryWidget extends KpiSideNavBox implements Colapse {


    private DataGrid<BillOfEntryItem> dataGrid;
    private ListDataProvider<BillOfEntryItem> dataProvider;
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final AccountingUtils utils = AccountingUtils.get();
    private Integer billOfEntryId;
    NewInvoice purchaseInvoice;

    public static final ProvidesKey<BillOfEntryItem> KEY_PROVIDER = item -> item == null ? null : item.getItemID();

    interface BillOfEntryWidgetUiBinder extends UiBinder<HTMLPanel, BillOfEntrySummaryWidget> {
    }

    private static BillOfEntryWidgetUiBinder ourUiBinder = GWT.create(BillOfEntryWidgetUiBinder.class);
    @UiField
    Label billOfEntryLabel;
    @UiField
    Label billOfEntry;
    @UiField
    HTMLPanel bodyPanel;
    @UiField
    Label portCodeLabel;
    @UiField
    Label portCode;
    @UiField
    Label billOfEntryDateLabel;
    @UiField
    Label billOfEntryDate;
    @UiField
    Label paidThroughLabel;
    @UiField
    Label paidThrough;
    @UiField
    Label referenceLabel;
    @UiField
    Label reference;
    @UiField
    Label descriptionLabel;
    @UiField
    Label description;
    @UiField
    FlowPanel billOfEntryItems;
    @UiField
    Label totalCustomDutyLabel;
    @UiField
    Label totalTaxLabel;
    @UiField
    Label totalAmountLabel;
    @UiField
    DivElement totalCustomDuty;
    @UiField
    DivElement totalTax;
    @UiField
    DivElement totalAmount;

    WfmButton2 delete;
    WfmButton2 edit;
    ClickHandler onEdit;
    ClickHandler onDelete;

    public BillOfEntrySummaryWidget(Integer billOfEntryId, NewInvoice invoiceData, ClickHandler onEdit, ClickHandler onDelete) {
        super(KpiSideNavBox.WIDE_FORM_WIDTH);
        ourUiBinder.createAndBindUi(this);
        this.onEdit = onEdit;
        this.onDelete = onDelete;
        this.billOfEntryId = billOfEntryId;
        this.purchaseInvoice = invoiceData;
        init();
    }

    private void init() {
        billOfEntryLabel.setText("Bill Of Entry #:");
        portCodeLabel.setText("Port Code:");
        billOfEntryDateLabel.setText("Date:");
        paidThroughLabel.setText("PaidThrough:");
        referenceLabel.setText("Reference #:");
        descriptionLabel.setText("Description:");

        dataProvider = new ListDataProvider<>();
        dataGrid = new DataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("360px");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataProvider.addDataDisplay(dataGrid);
        billOfEntryItems.add(dataGrid);

        loadData();

        delete = new WfmButton2(wfmStrings.delete(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            remove();
            if (onDelete!=null) {
                onDelete.onClick(null);
            }
        });
        edit = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            remove();
            if (onEdit!=null) {
                onEdit.onClick(null);
            }
        });

        Heading header = new Heading(HeadingSize.H1);
        header.setText(accountingStrings.billOfEntry());
        addHeader(header);
        addBody(bodyPanel);
        addFooter(edit);

    }

    private void calculateTotals() {

        dataGrid.redraw();

        BigDecimal totalCustomDuty = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
//        BigDecimal totalAmount = BigDecimal.ZERO;

        List<BillOfEntryItem> items = dataProvider.getList();

        if (items != null) {

            for (BillOfEntryItem item : items) {
                totalCustomDuty = totalCustomDuty.add(item.getCustomDutyAdditionalCharges());

                if (item.getTax() != null) {

                    if (item.getTax().getTaxPercent() != null && item.getTax().getTaxPercent().doubleValue() > 0) {
                        totalTax = totalTax.add(item.getTaxableAmount().multiply(item.getTax().getTaxPercent()).divide(new BigDecimal("100")));
                    }
                }
//                totalAmount = totalAmount.add(item.getTaxableAmount());
            }
        }
        this.totalCustomDuty.setInnerHTML(utils.formatPrice(totalCustomDuty.doubleValue()));
        this.totalTax.setInnerHTML(utils.formatPrice(totalTax.doubleValue()));
        this.totalAmount.setInnerHTML(utils.formatPrice(totalTax.add(totalCustomDuty)));

    }


    private void loadData() {

        InvoiceService.App.get().getBillOfEntry(purchaseInvoice.getID(), billOfEntryId, new AsyncCallback<BillOfEntry>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(BillOfEntry result) {
//                    super.success(result);
//
                if (result != null) {
                    billOfEntry.setText(result.getBoeNumber());
                    portCode.setText(result.getPortName());
                    billOfEntryDate.setText(result.getBoeDate().toString());
                    reference.setText(result.getReference());
                    if (result.getPaidThrough() != null) {
                        paidThrough.setText(result.getPaidThrough().getName());
                    }
                    description.setText(result.getDescription());
                    setValues(result.getItems());
                    initTableColumns(result.getZeroTaxes());

                    calculateTotals();
                }
            }
        });
    }

    private void setValues(List<BillOfEntryItem> result) {
        BillOfEntryItem[] expenseItem = new BillOfEntryItem[result.size()];
        int i = 0;
        for (BillOfEntryItem shippingData1 : result) {
            expenseItem[i] = shippingData1;
            i++;
        }
        initDataProviderApply(expenseItem);
        dataProvider.refresh();
    }

    private void initDataProviderApply(BillOfEntryItem[] billOfEntryItems) {
        List<BillOfEntryItem> items = dataProvider.getList();
        items.clear();
        Collections.addAll(items, billOfEntryItems);
    }

    private void initTableColumns(List<TaxItem> zeroTaxes) {

        final SimpleLinkCell[] cell = {new SimpleLinkCell()};
        Column<BillOfEntryItem, String> number = new Column<BillOfEntryItem, String>(cell[0]) {
            @Override
            public String getValue(BillOfEntryItem data) {
                /*if (data.getExpenseReportNumber() != null) {
                    cell[0].setClickHandler(e -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|previewReport/" + data.getReportId(), data.getExpenseReportNumber());
                        remove();
                    });
                }*/
                return data.getFullItemName();
            }
        };
        dataGrid.addColumn(number, wfmStrings.itemName());
        dataGrid.setColumnWidth(number, 25, Style.Unit.PCT);

        //Assessable Value ( Quantity x Rate ) + Additional Charges if any
        TextCell assessableValueCell = new TextCell();
        Column<BillOfEntryItem, String> assessableValue = new Column<BillOfEntryItem, String>(assessableValueCell) {
            @Override
            public String getValue(final BillOfEntryItem data) {
                return data.getAssessableValue() != null ? utils.formatPrice(data.getAssessableValue().doubleValue()) : utils.formatPrice(0d);
            }
        };

        dataGrid.addColumn(assessableValue, "Assessable Value" /* "( Quantity x Rate ) + Additional Charges if any"*/);
        dataGrid.setColumnWidth(assessableValue, 25, Style.Unit.PCT);


        //Custom Duty + Additional Charges
        TextCell additionalChargesCell = new TextCell();
        Column<BillOfEntryItem, String> customDuty = new Column<BillOfEntryItem, String>(additionalChargesCell) {
            @Override
            public String getValue(final BillOfEntryItem data) {
                return data.getCustomDutyAdditionalCharges() != null ? Utils.formatDouble(data.getCustomDutyAdditionalCharges().doubleValue()) : "";
            }
        };
        dataGrid.addColumn(customDuty, "Additional Charges");
        dataGrid.setColumnWidth(customDuty, 27, Style.Unit.PCT);

        //Taxable Amount
        Column<BillOfEntryItem, String> taxableAmount = new Column<BillOfEntryItem, String>(new TextCell()) {
            @Override
            public String getValue(final BillOfEntryItem data) {
                return data.getTaxableAmount() != null ? Utils.formatDouble(data.getTaxableAmount().doubleValue()) : "";
            }
        };
        dataGrid.addColumn(taxableAmount, "Taxable Amount");
        dataGrid.setColumnWidth(taxableAmount, 22, Style.Unit.PCT);

        //Tax
        TextCell selectionCell = new TextCell();

        Column<BillOfEntryItem, String> taxColumn = new Column<BillOfEntryItem, String>(selectionCell) {
            @Override
            public String getValue(BillOfEntryItem object) {
                return object.getTax() != null ? object.getTax().getName() : "";
            }
        };

        dataGrid.addColumn(taxColumn, wfmStrings.tax());
        dataGrid.setColumnWidth(taxColumn, 30, Style.Unit.PCT);


    }
}
