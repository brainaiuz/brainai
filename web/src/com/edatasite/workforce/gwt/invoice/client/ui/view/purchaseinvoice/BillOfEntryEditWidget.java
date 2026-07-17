package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.cell.SelectItemCell;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.BillOfEntry;
import com.edatasite.workforce.gwt.invoice.client.rpc.BillOfEntryItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Label;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class BillOfEntryEditWidget extends KpiSideNavBox implements Colapse {


    private DataGrid<BillOfEntryItem> dataGrid;
    private ListDataProvider<BillOfEntryItem> dataProvider;
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final AccountingUtils utils = AccountingUtils.get();
    private Integer billOfEntryId;
    NewInvoice purchaseInvoice;
    private BillOfEntry billOfEntryData;

    public static final ProvidesKey<BillOfEntryItem> KEY_PROVIDER = item -> item == null ? null : item.getItemID();

    interface BillOfEntryWidgetUiBinder extends UiBinder<HTMLPanel, BillOfEntryEditWidget> {
    }

    private static BillOfEntryWidgetUiBinder ourUiBinder = GWT.create(BillOfEntryWidgetUiBinder.class);
    @UiField
    Label billOfEntryLabel;
    @UiField
    TextBox billOfEntry;
    @UiField
    HTMLPanel bodyPanel;
    @UiField
    Label portCodeLabel;
    @UiField
    DataListBox portCode;
    @UiField
    Label billOfEntryDateLabel;
    @UiField
    DatePicker billOfEntryDate;
    @UiField
    Label paidThroughLabel;
    @UiField(provided = true)
    PaymentAccountsLookUp paidThrough;
    @UiField
    Label referenceLabel;
    @UiField
    TextBox reference;
    @UiField
    Label descriptionLabel;
    @UiField
    TextArea description;
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

    WfmButton2 save;

    public BillOfEntryEditWidget(Integer billOfEntryId, NewInvoice invoiceData) {
        super(KpiSideNavBox.WIDE_FORM_WIDTH);
        paidThrough = new PaymentAccountsLookUp(true);
        paidThrough.setCurrencyID(AccountingUtils.getBaseCurrencyId());
        ourUiBinder.createAndBindUi(this);

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

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            save.setEnabled(false);
            if (validateForm()) {
                save();
            } else {
                save.setEnabled(true);
            }
        });

        Heading header = new Heading(HeadingSize.H1);
        header.setText(billOfEntryId != null && billOfEntryId > 0 ? accountingStrings.updateBillOfEntry() : accountingStrings.createBillOfEntry());
        addHeader(header);
        addBody(bodyPanel);
        addFooter(save);

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
                } else if (billOfEntryData.getZeroTaxes() != null && billOfEntryData.getZeroTaxes().size() > 0 && billOfEntryData.getZeroTaxes().get(0).getTaxPercent() != null && billOfEntryData.getZeroTaxes().get(0).getTaxPercent().doubleValue() > 0) {
                    totalTax = totalTax.add(item.getTaxableAmount().multiply(billOfEntryData.getZeroTaxes().get(0).getTaxPercent()).divide(new BigDecimal("100")));
                }
//                totalAmount = totalAmount.add(item.getTaxableAmount());
            }
        }
        this.totalCustomDuty.setInnerHTML(utils.formatPrice(totalCustomDuty.doubleValue()));
        this.totalTax.setInnerHTML(utils.formatPrice(totalTax.doubleValue()));
        this.totalAmount.setInnerHTML(utils.formatPrice(totalTax.add(totalCustomDuty)));

    }

    public boolean validateForm() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(billOfEntry)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(paidThrough)) {
            errors++;
        }
        if (!Validation.validateDate(billOfEntryDate)) {
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }

    private void save() {
        BillOfEntry boeObj = new BillOfEntry();
        boeObj.setObjectID(billOfEntryId);
        boeObj.setBoeNumber(billOfEntry.getText());
        if (portCode.getSelectedItem() != null) {
            GWT.log(portCode.getSelectedItem().getId() + " " + portCode.getSelectedItem().getName());
            boeObj.setPortId(portCode.getSelectedItem().getId());
        }
        boeObj.setBoeDate(billOfEntryDate.getDate());
        boeObj.setReference(reference.getText());
        boeObj.setPaidThrough(paidThrough.getSelectedItem());
        boeObj.setDescription(description.getText());

        BigDecimal totalCustomDuty = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
//        BigDecimal totalAmount = BigDecimal.ZERO;

        boeObj.getItems().clear();
        for (BillOfEntryItem item : dataProvider.getList()) {
            //Add as Item
            boeObj.getItems().add(item);
            //Calculate totals

            totalCustomDuty = totalCustomDuty.add(item.getCustomDutyAdditionalCharges());

            if (item.getTax() != null) {

                if (item.getTax().getTaxPercent() != null && item.getTax().getTaxPercent().doubleValue() > 0) {
                    totalTax = totalTax.add(item.getTaxableAmount().multiply(item.getTax().getTaxPercent()).divide(new BigDecimal("100")));
                }
            } else if (billOfEntryData.getZeroTaxes() != null && billOfEntryData.getZeroTaxes().size() > 0) {
                item.setTax(billOfEntryData.getZeroTaxes().get(0));
                if (billOfEntryData.getZeroTaxes().get(0).getTaxPercent() != null && billOfEntryData.getZeroTaxes().get(0).getTaxPercent().doubleValue() > 0) {
                    totalTax = totalTax.add(item.getTaxableAmount().multiply(billOfEntryData.getZeroTaxes().get(0).getTaxPercent()).divide(new BigDecimal("100")));
                }
            }
//            totalAmount = totalAmount.add(item.getTaxableAmount());
        }

        boeObj.setTotalCustomDuty(totalCustomDuty);
        boeObj.setTotalTaxAmount(totalTax);
        boeObj.setTotalAmount(totalTax.add(totalCustomDuty));

        InvoiceService.App.get().saveBillOfEntry(purchaseInvoice.getID(), boeObj, new AsyncCallback<BillOfEntry>() {
            @Override
            public void onFailure(Throwable throwable) {
                save.setEnabled(true);
                GWT.log("", throwable);
            }

            @Override
            public void onSuccess(BillOfEntry result) {
//                    super.success(result);
//                setValues(result);
//                initTableColumns();
                save.setEnabled(true);
                if (result != null) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.item()));
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BILLOFENTRY_CREATED, result, BillOfEntryEditWidget.this);
                    /*billOfEntry.setText(result.getBoeNumber());
                    portCode.setSelectedByValue(result.getPortCode(), true);
                    billOfEntryDate.setText(result.getBoeDate().toString());
                    reference.setText(result.getReference());
                    paidThrough.setSelected(result.getPaidThrough());
                    description.setText(result.getDescription());*/

                }
            }
        });
    }

    private void loadData() {

        CommonService.App.get().getReferences(ReferenceParentEnum._UAE_PORTCODE, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log("Error", throwable);
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                portCode.setItems(selectItems);

                InvoiceService.App.get().getBillOfEntry(purchaseInvoice.getID(), billOfEntryId, new AsyncCallback<BillOfEntry>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(BillOfEntry result) {
//                    super.success(result);
//
                        if (result != null) {
                            billOfEntryData = result;
                            billOfEntry.setText(result.getBoeNumber());
                            portCode.setSelected(result.getPortId());
                            billOfEntryDate.setDate(result.getBoeDate());
                            reference.setText(result.getReference());
                            if (result.getPaidThrough() != null) {
                                paidThrough.setSelected(result.getPaidThrough());
                            }
                            description.setText(result.getDescription());
                            setValues(result.getItems());
                            initTableColumns(result.getZeroTaxes());

                            calculateTotals();
                        }
                    }
                });
            }
        });




        /*if (billOfEntryId != null && billOfEntryId > 0) {

        } else {

        }*/
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
                        close();
                    });
                }*/
                return data.getFullItemName();
            }
        };
        dataGrid.addColumn(number, wfmStrings.itemName());
        dataGrid.setColumnWidth(number, 25, Style.Unit.PCT);

        //Assessable Value ( Quantity x Rate ) + Additional Charges if any
        TextInputCell assessableValueCell = new TextInputCell();
        assessableValueCell.setWidth("110px");
        Column<BillOfEntryItem, String> assessableValue = new Column<BillOfEntryItem, String>(assessableValueCell) {
            @Override
            public String getValue(final BillOfEntryItem data) {
                return data.getAssessableValue() != null ? utils.formatPrice(data.getAssessableValue().doubleValue()) : utils.formatPrice(0d);
            }
        };
        assessableValue.setFieldUpdater((i, billEntryItem, value) -> {
            billEntryItem.setAssessableValue(Utils.parseToBigDecimal(value));
            billEntryItem.setTaxableAmount(billEntryItem.getAssessableValue().add(billEntryItem.getCustomDutyAdditionalCharges()));
            calculateTotals();
        });
        dataGrid.addColumn(assessableValue, "Assessable Value" /* "( Quantity x Rate ) + Additional Charges if any"*/);
        dataGrid.setColumnWidth(assessableValue, 25, Style.Unit.PCT);


        //Custom Duty + Additional Charges
        TextInputCell additionalChargesCell = new TextInputCell();
        additionalChargesCell.setWidth("100px");
        Column<BillOfEntryItem, String> customDuty = new Column<BillOfEntryItem, String>(additionalChargesCell) {
            @Override
            public String getValue(final BillOfEntryItem data) {
                return data.getCustomDutyAdditionalCharges() != null ? Utils.formatDouble(data.getCustomDutyAdditionalCharges().doubleValue()) : "";
            }
        };
        customDuty.setFieldUpdater((i, billEntryItem, value) -> {
            billEntryItem.setCustomDutyAdditionalCharges(Utils.parseToBigDecimal(value));
            billEntryItem.setTaxableAmount(billEntryItem.getAssessableValue().add(billEntryItem.getCustomDutyAdditionalCharges()));
            calculateTotals();
        });
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
        SelectItemCell selectionCell = new SelectItemCell(zeroTaxes);

        Column<BillOfEntryItem, TaxItem> taxColumn = new Column<BillOfEntryItem, TaxItem>(selectionCell) {
            @Override
            public TaxItem getValue(BillOfEntryItem object) {
                return object.getTax() != null ? object.getTax() : new TaxItem();
            }
        };
        taxColumn.setFieldUpdater((i, billEntryItem, value) -> {
            billEntryItem.setTax(value);
            calculateTotals();
        });

        dataGrid.addColumn(taxColumn, wfmStrings.tax());
        dataGrid.setColumnWidth(taxColumn, 30, Style.Unit.PCT);


    }
}
