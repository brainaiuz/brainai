package com.edatasite.workforce.gwt.invoice.client.ui.view.rentalorder;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderData;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.client.client.ui.view.quickadd.CusSuppQuickAddView;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LoadingPanelCallback;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnOffsetEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.view.ExtendedHTML;
import com.edatasite.workforce.gwt.core.client.ui.view.InvoiceTermsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartCrmAccountLookup;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartProductLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartTaxRateLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.TaxView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.ZERO;

public class RentalOrderAddEditView extends CustomForm2 implements Colapse, Constants {


    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private Integer taxCalculationType = AccountingConstants.TAX_CALCULATION_EXCLUSIVE;

    public DataListBox taxCalcTypeListBox;
    private EditableTable itemsTable;
    private RentalOrderReceiptTable totalsTable;
    private LookUp crmAccountLookUp;
    private MaterialLink customerBalanceLink;
    private InvoiceTermsLookUp termsLookUp;
    private DatePicker startDatePicker;
    private DatePicker expirationDatePicker;
    private HashMap<Integer, Widget> taxWidgetMap;
    private Numbering numberWidget;
    private final Integer objectID;
    private RentalOrderData data;
    private WfmButton2 submitButton, approveButton, rejectButton;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private FormHasCustomField customFieldUtil;
    private ChosenApproversWidget approver;
    private final Map<String, ColumnConfigs> columnsMap = new LinkedHashMap<>();

    private ExtendedHTML subTotal,
            total,
            baseTotal,
            netAmountTotal,
            shippingTaxValue;

    private HTML subTotalLabel,
            totalLabel,
            baseTotalLabel,
            comissionAmount,
            netAmountTotalLabel;

    public RentalOrderAddEditView(Integer objectID) {
        super("rentalorderadd");
        setDescription(objectID == null ? wfmStrings.addRentalOrder() : property.getSingular(accountingStrings.editProduct(), accountingStrings.rentalOrder()));
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.RentalOrdersView, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                RentalOrderAddEditView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void getDataToFillFields() {
        drawForm();
        LoadingPanel.loading(true);
        RentalOrderService.App.get().getRentalOrderData(objectID, false, new AbstractAsyncCallback<RentalOrderData>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(RentalOrderData result) {
                LoadingPanel.loading(false);
                data = result;
                if (data != null) {
                    drawItemTableSection();
                    numberWidget.setNumberData(data.getNumberData());
                    startDatePicker.setDate(data.getStartDate());
                    startDatePicker.setEnabled(objectID == null);
                    expirationDatePicker.setDate(data.getExpirationDate());
                    if (data.getCustomer() != null) {
                        crmAccountLookUp.setSelected(data.getCustomer());
                    }
                    if (data.getPaymentTerms() != null) {
                        termsLookUp.setSelected(data.getPaymentTerms());
                    }
                    taxCalcTypeListBox.setSelected(data.getTaxCalculationType() != null ? AccountingUtils.getTaxCalcType(data.getTaxCalculationType()) : AccountingUtils.getTaxCalcType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE));
                    onTaxCalculationTypeChange(taxCalcTypeListBox.getSelectedId(), false);
                    getCustomFieldUtil().fillCustomFieldsWithData(data.getCustomFieldItems());


                    int rowCount = data.getRentalOrderItems() != null && data.getRentalOrderItems().size() > 0 ? data.getRentalOrderItems().size() : 0;
                    int size = rowCount < 3 ? 3 : rowCount;
                    for (int i = 0; i < size; i++) {
                        itemsTable.addRow(getWidgets(rowCount >= i + 1 ? data.getRentalOrderItems().get(i) : new RentalOrderItem()));
                    }
                    calculate();
                    addButton();
                }

                if (objectID == null) {
                    setDefaultValues();
                    setDefaultValuesByFormProperty();
                }
            }
        });
    }

    private void addButton() {
        if (data.isApproveProcessEnabled()) {
            approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_SUCCESS);
            approveButton.setVisible(false);
            approveButton.getElement().setId("rental_order_add_view_approve_button");
            approveButton.addClickHandler(click -> {
                approveButton.setEnabled(false);
                if (!validate()) {
                    approveButton.setEnabled(true);
                    return;
                }
                save(Constants.RENTAL_APPROVED);
            });

            rejectButton = new WfmButton2(wfmStrings.reject(), WfmButton2.BTN_REJECT);
            rejectButton.setVisible(false);
            rejectButton.getElement().setId("rental_order_add_view_reject_button");
            rejectButton.addClickHandler(click -> {
                if (!validate()) {
                    rejectButton.setEnabled(true);
                    return;
                }
                save(Constants.RENTAL_REJECTED);
            });

            addRightButton(approveButton);
            addRightButton(rejectButton);

            approver = new ChosenApproversWidget(RelationItem.TYPE_RENTAL_ORDER, data.getApprover() != null ? objectID : null);
            addField(CustomFormConstants.APPROVERS, approver, wfmStrings.approvers());

            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, RentalOrderAddEditView.this, (sender, args) -> {
                if (approver.getFirstApproverLookUp() != null) {
                    approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                        SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                        Integer itemId = item != null ? item.getId() : null;
                        Integer currentUserId = Utils.getUserID();
                        if (currentUserId.equals(itemId)) {
                            approveButton.setVisible(true);
                            rejectButton.setVisible(true);
                            submitButton.setVisible(false);
                        } else {
                            submitButton.setVisible(true);
                            approveButton.setVisible(false);
                            rejectButton.setVisible(false);
                        }
                    });
                    if (approver.getFirstApproverLookUp().getSelectedItem() != null) {
                        SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                        if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                            approveButton.setVisible(true);
                            rejectButton.setVisible(true);
                            submitButton.setVisible(false);
                        } else {
                            approveButton.setVisible(false);
                            rejectButton.setVisible(false);
                            submitButton.setVisible(true);
                        }
                    }
                    if (approver != null && data != null && data.getObjectID() != null) {
                        approver.setEnabled(RENTAL_APPROVED.equals(data.getStatusCode()) || RENTAL_REJECTED.equals(data.getStatusCode()));
                    }
                }
            });
        }

//        if (objectID == null) {
//            Info.warn(wfmStrings.youDontHavePermission());
//        }
    }

    private void drawForm() {
        drawMainSection();
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID, false);

    }

    private void drawMainSection() {

        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        boolean hasPermissonCustomerQuick = Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_QUICK_ADD);
        boolean hasPermissonCustomerAdd = Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_ADD);

        crmAccountLookUp = new SmartCrmAccountLookup(CrmAccountLookUp.CUSTOMER, true, () -> {
            if (hasPermissonCustomerQuick) {
                new CusSuppQuickAddView(CrmAccountLookUp.CUSTOMER, crmAccountLookUp.getLastValueBeforeClick());
            } else if (hasPermissonCustomerAdd) {
                SinksContainerFactory.entryPoint.onHistoryChanged("client|add/add");
            }
        }, false, hasPermissonCustomerQuick || hasPermissonCustomerAdd);
        crmAccountLookUp.ensureDebugId("rental_order_crmAccountLookUp");

        crmAccountLookUp.getSuggestBox().addSelectionHandler(sh -> {
            if (crmAccountLookUp.getSelectedItemID() != null) {
                InvoiceService.App.get().getClientOrSupplier(crmAccountLookUp.getSelectedItemID(), RECEIVABLE, new AsyncCallback<TypeItem>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(TypeItem typeItem) {
                        if (typeItem.getSupplierCustomerBalance() >= 0) {
                            customerBalanceLink.setText(AccountingUtils.get().formatPrice(typeItem.getSupplierCustomerBalance()));
                        } else {
                            customerBalanceLink.setText("(" + AccountingUtils.get().formatPrice((-1) * typeItem.getSupplierCustomerBalance()) + ")");
                        }
                        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                        customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + crmAccountLookUp.getSelectedItemID() + "/" + CrmAccountItem.CUSTOMER,
                                wfmStrings.balance() + ": " + typeItem.getName(), typeItem.getName()));

                    }
                });
            }
        });

        FormGroup customerField = new FormGroup(crmAccountLookUp);
        customerField.ensureDebugId(InvoiceFormFields.CUSTOMER);

        Div clientFieldLabel = customerField.getGroupLabel();
        clientFieldLabel.addStyleName("label-group");

        clientFieldLabel.add(new Span(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));

        Span balance = new Span(wfmStrings.balance() + ": ");
        balance.add(customerBalanceLink);
        clientFieldLabel.add(balance);

        numberWidget = new Numbering();
        numberWidget.getTxtNumber().setAlignment(ValueBoxBase.TextAlignment.RIGHT);

        termsLookUp = new InvoiceTermsLookUp();
        termsLookUp.ensureDebugId("terms");

        startDatePicker = new DatePicker(true);
        startDatePicker.setDate(new Date());
        startDatePicker.ensureDebugId("rental_order_startDate");

        expirationDatePicker = new DatePicker(true);
        expirationDatePicker.setDate(new Date());
        expirationDatePicker.ensureDebugId("rental_order_expiration");

        startDatePicker.addChangeHandler(event -> {
            Date start = startDatePicker.getDate();
            Date expiration = expirationDatePicker.getDate();

            if (start != null && expiration != null && start.after(expiration)) {
                Info.warn(wfmStrings.startDateNotLaterDueDate());
                startDatePicker.setDate(null);
            }
        });

        expirationDatePicker.addChangeHandler(event -> {
            Date expiration = expirationDatePicker.getDate();
            Date start = startDatePicker.getDate();

            if (expiration != null && start != null && expiration.before(start)) {
                Info.warn(wfmStrings.startDateNotLaterDueDate());
                expirationDatePicker.setDate(null);
            }
        });

        taxCalcTypeListBox = new DataListBox();
        taxCalcTypeListBox.setWithoutNullLabel(true);
        taxCalcTypeListBox.setItems(AccountingUtils.getTaxCalcTypes());
        taxCalcTypeListBox.setSelected(AccountingUtils.getTaxCalcType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE));
        taxCalcTypeListBox.addValueChangeHandler(event -> onTaxCalculationTypeChange(taxCalcTypeListBox.getSelectedId(), true));
        taxWidgetMap = new HashMap<>();

        addTitleField(CustomFormConstants.INFORMATION, property.getSingular(accountingStrings.productInformation(), accountingStrings.rentalOrder()));

        if (formPropertyMap != null && formPropertyMap.get(AccountingCustomFormConstants.CUSTOMER) != null) {
            addField(AccountingCustomFormConstants.CUSTOMER, customerField, null);
            customerField.setEnabled(!formPropertyMap.get(AccountingCustomFormConstants.CUSTOMER).isDisabled());
        } else {
            addField(AccountingCustomFormConstants.CUSTOMER, customerField, null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, numberWidget, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()));
            numberWidget.setEnabled(!formPropertyMap.get(CustomFormConstants.NUMBER).isDisabled());
        } else {
            addField(CustomFormConstants.NUMBER, numberWidget, wfmStrings.number());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM) != null) {
            addField(CustomFormConstants.CLIENT_INVOICE_TERM, termsLookUp, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).getTitle() : wfmStrings.paymentTerms(), formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).isRequired()));
            termsLookUp.setEnabled(!formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).isDisabled());
        } else {
            addField(CustomFormConstants.CLIENT_INVOICE_TERM, termsLookUp, getTitle(wfmStrings.paymentTerms()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null) {
            addField(CustomFormConstants.START_DATE, startDatePicker, getTitle(formPropertyMap.get(CustomFormConstants.START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.START_DATE).getTitle() : wfmStrings.startDate(), formPropertyMap.get(CustomFormConstants.START_DATE).isRequired()));
            startDatePicker.setEnabled(!formPropertyMap.get(CustomFormConstants.START_DATE).isDisabled());
        } else {
            addField(CustomFormConstants.START_DATE, startDatePicker, getTitle(wfmStrings.startDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DATE) != null) {
            addField(CustomFormConstants.DATE, expirationDatePicker, getTitle(formPropertyMap.get(CustomFormConstants.DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DATE).getTitle() : wfmStrings.expiryDate(), formPropertyMap.get(CustomFormConstants.DATE).isRequired()));
            expirationDatePicker.setEnabled(!formPropertyMap.get(CustomFormConstants.DATE).isDisabled());
        } else {
            addField(CustomFormConstants.DATE, expirationDatePicker, getTitle(wfmStrings.expiryDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE) != null) {
            addField(TAX_CALC_TYPE, taxCalcTypeListBox, getTitle(formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).getTitle() : wfmStrings.amount(), formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).isRequired()));
            taxCalcTypeListBox.setEnabled(!formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).isDisabled());
        } else {
            addField(CustomFormConstants.TAX_CALC_TYPE, taxCalcTypeListBox, getTitle(wfmStrings.amount()));
        }
    }

    private void drawItemTableSection() {

        if (data.getItemColumns() != null) {
            for (ColumnConfigs cc : data.getItemColumns()) {
                if (cc.isSelected()) {
                    columnsMap.put(cc.getCode(), cc);
                }
            }
        }

        itemsTable = new EditableTable(getColumns(), true, true);
        itemsTable.setDraggable(true);
        itemsTable.setWidth("100%");
        itemsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                itemsTable.addRow(getWidgets(new RentalOrderItem()));
            }

            @Override
            public void removeRow() {
                calculate();
            }
        });

        totalsTable = new RentalOrderReceiptTable();
        totalsTable.addStyleName("totalsTable java-RentalOrderAddEditView");
        initTotalTableWidgets();

        GColumn cTotalTable = new GColumn(GColumnEnum.COL_3, totalsTable);
        cTotalTable.setOffset(GColumnOffsetEnum.OFFSET_9);
        Div itemsTableContainer = new Div();
        itemsTableContainer.add(new GRow(new GColumn(GColumnEnum.COL_12, itemsTable)));
        itemsTableContainer.add(new GRow(cTotalTable));
        addField(CustomFormConstants.ITEMS, itemsTableContainer, null, true);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_RENTAL_ORDER_CALCULATE_MIN_PRICE, RentalOrderAddEditView.this, (sender, args) -> {
            RentalOrderPriceItem rentalOrderPriceItem = (RentalOrderPriceItem) args;
            if (rentalOrderPriceItem != null) {
                RentalOrderItem rentalOrderItem = rentalOrderPriceItem.getItem();
                SmartProductLookUp rentalItemLookUp = (SmartProductLookUp) itemsTable.getColumnById(rentalOrderPriceItem.getRowID(), ItemTableConstants.PRODUCT);
                TextArea2 description = (TextArea2) itemsTable.getColumnById(rentalOrderPriceItem.getRowID(), ItemTableConstants.DESCRIPTION);
                CustomCellTextBox quantity = (CustomCellTextBox) itemsTable.getColumnById(rentalOrderPriceItem.getRowID(), ItemTableConstants.QTY);
                CustomCellTextBox price = (CustomCellTextBox) itemsTable.getColumnById(rentalOrderPriceItem.getRowID(), ItemTableConstants.UNITPRICE);

                rentalItemLookUp.setSelected(rentalOrderItem.getRentalItem());
                description.setText(rentalOrderItem.getDescription());
                refreshCustomCellDisplayValue(rentalOrderPriceItem.getRowID(), ItemTableConstants.DESCRIPTION);


                if (rentalOrderItem.getQty() != null) {
                    quantity.setText(AccountingUtils.get().formatQty(rentalOrderItem.getQty()));
                    refreshCustomCellDisplayValue(rentalOrderPriceItem.getRowID(), ItemTableConstants.QTY);
                }
                if (rentalOrderItem.getPrice() != null) {
                    price.setText(AccountingUtils.get().formatPrice(rentalOrderItem.getPrice()));
                    refreshCustomCellDisplayValue(rentalOrderPriceItem.getRowID(), ItemTableConstants.UNITPRICE);
                }
                calculate();

                if (objectID == null || startDatePicker.getDate().after(rentalOrderItem.getFromDate())) {
                    startDatePicker.setDate(rentalOrderItem.getFromDate());
                }
                expirationDatePicker.setDate(rentalOrderItem.getToDate());
            }
        });
    }


    public void refreshCustomCellDisplayValue(Integer rowId, String key) {
        CustomCell customCell = (CustomCell) this.itemsTable.getColumnCellWidgetById(rowId, key);
        customCell.InActive();
    }

    private void initTotalTableWidgets() {
        subTotalLabel = new HTML(wfmStrings.subtotal());
        totalLabel = new HTML(wfmStrings.total());
        baseTotalLabel = new HTML(wfmStrings.total());
        netAmountTotalLabel = new HTML(wfmStrings.netAmount());

        subTotal = getZeroAsHTML();
        total = getZeroAsHTML();
        baseTotal = getZeroAsHTML();
        comissionAmount = getZeroAsHTML();
        shippingTaxValue = getZeroAsHTML();
        netAmountTotal = getZeroAsHTML();

        subTotalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        totalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        baseTotalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        netAmountTotalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);

        subTotal.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        total.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        baseTotal.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        comissionAmount.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        shippingTaxValue.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        netAmountTotal.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
    }

    public void drawTotalsTable() {
        totalsTable.clear();
        totalsTable.setSubtotalItem(subTotalLabel, subTotal);

        if (!AccountingConstants.NO_TAX_CALCULATION.equals(taxCalculationType)) {
            for (Integer key : taxWidgetMap.keySet()) {
                TaxView tax = (TaxView) taxWidgetMap.get(key);
                HTML taxLabel = new HTML(tax.getItem().getName());
                taxLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
                tax.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);

                totalsTable.addItem(taxLabel, tax);
            }
        }
        totalsTable.addGrossItem(totalLabel, total);

//        if (currencyId != null
//                && currencyWidget.getBaseCurrency() != null
//                && !currencyId.equals(currencyWidget.getBaseCurrency().getId())) {
//            totalsTable.addGrossItem(baseTotalLabel, baseTotal);
//        }
    }

    @Override
    protected void addButtons() {
        submitButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        submitButton.addClickHandler(click -> {
            submitButton.setEnabled(false);
            if (!validate()) {
                submitButton.setEnabled(true);
                return;
            }
            save(RENTAL_SUBMITTED);
        });
        addButton(submitButton);
    }

    private RentalOrderData getRentalOrderData(String statusCode) {
        data.setObjectID(objectID);
        NumberData numberData = numberWidget.getNumberData(false);

        data.setNumberData(numberData);
        data.setCustomer(crmAccountLookUp.getSelectedItem());
        data.setStartDate(startDatePicker.getDate());
        data.setExpirationDate(expirationDatePicker.getDate());
        data.setPaymentTerms(termsLookUp.getSelectedItem());
        data.setTaxCalculationType(taxCalcTypeListBox.getSelectedId());
        data.setSubTotal(subTotal.getValue());
        data.setTotal(total.getValue());
        if (taxWidgetMap != null) {
            BigDecimal taxTotal = BigDecimal.ZERO;
            for (Integer key : taxWidgetMap.keySet()) {
                TaxView tax = (TaxView) taxWidgetMap.get(key);
                taxTotal = taxTotal.add(AccountingUtils.parsePriceToBigDecimal(tax.getText()));
            }
            if (taxTotal.compareTo(BigDecimal.ZERO) > 0) {
                data.setTaxAmount(taxTotal);
            }
        }
        if (data.isApproveProcessEnabled()) {
            data.setApprovers(approver.getChosenApprovers());
        }

        data.setStatusCode(statusCode);
        data.setRentalOrderItems(getRentalOrderItemsData());
        data.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());

        return data;
    }

    private boolean validate() {
        int errors = 0;

        errors += markAsError(numberWidget, !numberWidget.validate());
        errors += getCustomFieldUtil().validateCustomFields();


        if (formPropertyMap != null && formPropertyMap.get(AccountingCustomFormConstants.CUSTOMER) != null && formPropertyMap.get(AccountingCustomFormConstants.CUSTOMER).isRequired()) {
            errors += markAsError(crmAccountLookUp, !Validation.validateLookUpRequired(crmAccountLookUp));
        }

        if (formPropertyMap != null && formPropertyMap.get(CLIENT_INVOICE_TERM) != null && formPropertyMap.get(CLIENT_INVOICE_TERM).isRequired() && termsLookUp != null) {
            errors += markAsError(termsLookUp, !Validation.validateLookUpRequired(termsLookUp));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null && formPropertyMap.get(CustomFormConstants.START_DATE).isRequired() && startDatePicker != null) {
            errors += markAsError(startDatePicker, !Validation.validateDate(startDatePicker));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DATE) != null && formPropertyMap.get(CustomFormConstants.DATE).isRequired() && expirationDatePicker != null) {
            errors += markAsError(expirationDatePicker, !Validation.validateDate(expirationDatePicker));
        }
        if (formPropertyMap != null && formPropertyMap.get(APPROVERS) != null && formPropertyMap.get(APPROVERS).isRequired() && data != null && data.isApproveProcessEnabled() && !approver.isValid()) {
            errors++;
        }

        Date startDate = startDatePicker.getDate();
        Date expiration = expirationDatePicker.getDate();
        if (startDate != null && expiration != null && startDate.after(expiration)) {
            errors++;
            markAsError(startDatePicker, true);
            markAsError(expirationDatePicker, true);
            Info.warn(wfmStrings.startDateNotLaterDueDate());
        }

        if (validateItemTable()) {
            errors++;
            Utils.openParentSection(itemsTable);
        }

        if (errors > 0) {
            Info.show(wfmStrings.unableToSave() + ".", Info.Type.WARNING);
        }

        return errors == 0;
    }

    private void save(String statusCode) {
        data = getRentalOrderData(statusCode);
        LoadingPanel.loading(true);
        RentalOrderService.App.get().saveRentalOrder(data, new AbstractAsyncCallback<SelectItem>() {
            public void failure(Throwable caught) {
                Info.warn(wfmStrings.sorrySomethingWentWrong());
                approveButton.setEnabled(true);
                submitButton.setEnabled(true);
                rejectButton.setEnabled(true);
                LoadingPanel.loading(false);
            }

            public void success(SelectItem result) {
                LoadingPanel.loading(false);
                if (result != null && result.getId() == -1) {
                    numberWidget.addStyleName(Constants.ERROR_FORM_STYLE);
                    Info.show(property.getSingular(wfmStrings.numberAlreadyExist(), accountingStrings.rentalOrder()), Info.Type.WARNING);
                    return;
                }
                closeTab(null);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_RENTAL_ORDER_ADDED, result, RentalOrderAddEditView.this);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-edit";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.RENTAL_ORDER_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return Constants.RENTAL_ORDERS;
    }

    private void setDefaultValuesByFormProperty() {

        if (formPropertyMap != null && formPropertyMap.get(AccountingCustomFormConstants.CUSTOMER) != null && formPropertyMap.get(AccountingCustomFormConstants.CUSTOMER).getSelectedId() != null && crmAccountLookUp != null) {
            crmAccountLookUp.setSelected(new SelectItem(formPropertyMap.get(AccountingCustomFormConstants.CUSTOMER).getSelectedId(), formPropertyMap.get(AccountingCustomFormConstants.CUSTOMER).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CLIENT_INVOICE_TERM) != null && formPropertyMap.get(CLIENT_INVOICE_TERM).getSelectedId() != null && termsLookUp != null) {
            termsLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).getSelectedId(), formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null && formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue() != null && startDatePicker != null) {
            try {
                startDatePicker.setDate(DateUtils.parse(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue()));
            } catch (final DateFormatException e) {
                e.printStackTrace();
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DATE) != null && formPropertyMap.get(CustomFormConstants.DATE).getDefaultValue() != null && expirationDatePicker != null) {
            try {
                expirationDatePicker.setDate(DateUtils.parse(formPropertyMap.get(CustomFormConstants.DATE).getDefaultValue()));
            } catch (final DateFormatException e) {
                e.printStackTrace();
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE) != null && formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).getSelectedId() != null) {
            taxCalcTypeListBox.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).getSelectedId(), formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).getDefaultValue()));
        }
    }

    public boolean isNumeric(final String str) {
        return str.matches("-?\\d+(.\\d+)?");
    }

    private ColumnConfig[] getColumns() {
        LinkedList<ColumnConfig> columnsList = new LinkedList<>();
        for (final String columnCode : columnsMap.keySet()) {
            final ColumnConfig columnConfig;
            final ColumnConfigs columnConfigs = columnsMap.get(columnCode);
            final boolean isPixel = (columnConfigs.getWidth() == null || columnConfigs.getWidth() == 0);
            switch (columnCode) {
                case ItemTableConstants.CATEGORY:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.CATEGORY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.category(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired());
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.BRAND:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.BRAND, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.brand(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired());
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.PRODUCT:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, columnConfigs.isChanged() ? columnConfigs.getTitle() : RentalOrderAddEditView.accountingStrings.rentalItem(), Utils.getColumnWidth(columnConfigs.getWidth(), 200), columnConfigs.isRequired());
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.PRODUCT_FOR_RENT:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.PRODUCT_FOR_RENT, columnConfigs.isChanged() ? columnConfigs.getTitle() : RentalOrderAddEditView.wfmStrings.item(), Utils.getColumnWidth(0, 0), false);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.DESCRIPTION:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, columnConfigs.isChanged() ? columnConfigs.getTitle() : RentalOrderAddEditView.wfmStrings.description(), Utils.getColumnWidth(columnConfigs.getWidth(), 250), columnConfigs.isRequired(), "product-description-cell");
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.UNITPRICE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.UNITPRICE, columnConfigs.isChanged() ? columnConfigs.getTitle() : RentalOrderAddEditView.wfmStrings.price(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.QTY:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, columnConfigs.isChanged() ? columnConfigs.getTitle() : RentalOrderAddEditView.wfmStrings.qty(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.TAX_LIST:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.TAX_LIST, columnConfigs.isChanged() ? columnConfigs.getTitle() : RentalOrderAddEditView.wfmStrings.taxRate(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.NET_AMT:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.NET_AMT, columnConfigs.isChanged() ? columnConfigs.getTitle() : RentalOrderAddEditView.wfmStrings.netAmount(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.TOTAL_AMT:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.TOTAL_AMT, columnConfigs.isChanged() ? columnConfigs.getTitle() : RentalOrderAddEditView.wfmStrings.totalAmount(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
            }
        }
        return columnsList.toArray(new ColumnConfig[]{});
    }

    private Widget[] getWidgets(RentalOrderItem item) {
        int index = 0;
        Widget[] widgets = new Widget[columnsMap.keySet().size()];
        for (final String columnCode : columnsMap.keySet()) {
            if (ItemTableConstants.PRODUCT.equals(columnCode)) {
                SmartProductLookUp rentalItem = new SmartProductLookUp(Constants.RENTAL_PRODUCTS);
                rentalItem.setValueNotEmptyMeansSelected(true);
                rentalItem.setWidth("100%");
                rentalItem.addStyleName("lookUp-moveRight");
                rentalItem.getSuggestBox().setWidth("100%");
                rentalItem.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                if (item.getRentalItem() != null) {
                    rentalItem.setSelected(new ProductSelectItem(item.getRentalItem().getId(), item.getRentalItem().getName()));
                }
                rentalItem.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                    item.setRentalItem(rentalItem.getSelectedItem());
                    new RentalOrderSideNavBox(itemsTable.getGrid().getCurrentRow(), item);
                    ProductService.App.get().getProductBaseData(rentalItem.getSelectedItemID(), true, new LoadingPanelCallback<NewProduct>(itemsTable, wfmStrings.pleaseWait()) {
                        public void success(NewProduct product1) {
                            if (product1 != null) {
                                if (product1.getCategoryID() != null) {
                                    DataListBox categoryBox = new DataListBox();
                                    categoryBox.setSelected(new SelectItem(product1.getCategoryID(),product1.getCategoryName()));
                                    itemsTable.getGrid().getModel().update(itemsTable.getGrid().getCurrentRow(), itemsTable.getColumnId(ItemTableConstants.CATEGORY), categoryBox);
                                }
                                if (product1.getBrandID() != null) {
                                    DataListBox brandBox = new DataListBox();
                                    brandBox.setSelected(new SelectItem(product1.getBrandID(), product1.getBrandName()));
                                    itemsTable.getGrid().getModel().update(itemsTable.getGrid().getCurrentRow(), itemsTable.getColumnId(ItemTableConstants.BRAND), brandBox);
                                }
                            }
                        }
                    });
                });

                rentalItem.setTitle(columnCode);
                widgets[index++] = rentalItem;
            } else if (ItemTableConstants.PRODUCT_FOR_RENT.equals(columnCode)) {
                Label productItem = new Label();
                productItem.setText("");
                productItem.getElement().setAttribute("style", "display: none");
                widgets[index++] = productItem;
            } else if (ItemTableConstants.DESCRIPTION.equals(columnCode)) {
                final TextArea2 txtDescription = new TextArea2(TextArea2.AREA_LENGTH_3);
                txtDescription.setText(item.getDescription());
                txtDescription.setTitle(columnCode);
                txtDescription.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                txtDescription.hideCharacterLimitPanel();
                widgets[index++] = txtDescription;
            } else if (ItemTableConstants.QTY.equals(columnCode)) {
                final CustomCellTextBox quantity = new CustomCellTextBox();
                quantity.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                quantity.setWidth("100%");
                Validation.addNumericKeyboardListener(quantity, 2);
                if (item.getQty() != null) {
                    quantity.setText(AccountingUtils.get().formatQty(item.getQty()));
                }
                quantity.addChangeHandler(changeEvent -> calculate());
                quantity.setTitle(columnCode);
                widgets[index++] = quantity;
            } else if (ItemTableConstants.UNITPRICE.equals(columnCode)) {
                CustomCellTextBox txtPrice = new CustomCellTextBox();
                txtPrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                txtPrice.setWidth("100%");
                txtPrice.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                Validation.addNumericKeyboardListener(txtPrice, 2);
                if (item.getPrice() != null) {
                    txtPrice.setText(AccountingUtils.get().formatPrice(item.getPrice()));
                }
                txtPrice.addChangeHandler(changeEvent -> calculate());
                txtPrice.setTitle(columnCode);
                widgets[index++] = txtPrice;
            } else if (ItemTableConstants.TAX_LIST.equals(columnCode)) {
                final SmartTaxRateLookUp taxLookUp = new SmartTaxRateLookUp(Constants.PAYABLE);
                taxLookUp.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                if (item.getTaxItem() != null) {
                    taxLookUp.addTaxItem(item.getTaxItem());
                }
                taxLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> reDrawTaxesDropdown());
                taxLookUp.getSuggestBox().addKeyUpHandler(keyUpEvent -> reDrawTaxesDropdown());
                widgets[index++] = taxLookUp;
            } else if (ItemTableConstants.NET_AMT.equals(columnCode)) {
                final ExtendedHTML netAmount = getZeroAsHTML();
                widgets[index++] = netAmount;
            } else if (ItemTableConstants.TOTAL_AMT.equals(columnCode)) {
                final ExtendedHTML totalAmount = getZeroAsHTML();
                widgets[index++] = totalAmount;
            } else if (ItemTableConstants.CATEGORY.equals(columnCode)) {
                DataListBox categoryBox = new DataListBox();
                categoryBox.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                categoryBox.addValueChangeHandler(valueChangeEvent -> {
                    if (valueChangeEvent.getValue().getId() != null) {
                        onChangeProductCategory(valueChangeEvent, item);
                    }
                    LookUpCell productCell = (LookUpCell) itemsTable.getColumnCellWidgetById(itemsTable.getGrid().getCurrentRow(), ItemTableConstants.PRODUCT);
                    ProductLookUp productLookUp = (ProductLookUp) productCell.getLookUp();
                    productCell.clear();
                    productLookUp.clearAndClearItems();
                    productLookUp.setCategoryId(valueChangeEvent.getValue().getId());

                });
                if (item != null) {
                    categoryBox.setItems(data.getProductCategories());
                }

                if (item.getProductCategory() != null) {
                    categoryBox.setSelected(item.getProductCategory());
                }
                widgets[index++] = categoryBox;
            } else if (ItemTableConstants.BRAND.equals(columnCode)) {
                DataListBox brandBox = new DataListBox();
                brandBox.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled());
                brandBox.addValueChangeHandler(valueChangeEvent -> {
                    LookUpCell productCell = (LookUpCell) itemsTable.getColumnCellWidgetById(itemsTable.getGrid().getCurrentRow(), ItemTableConstants.PRODUCT);
                    ProductLookUp productLookUp = (ProductLookUp) productCell.getLookUp();
                    productCell.clear();
                    productLookUp.clearAndClearItems();
                    productLookUp.setBrandId(valueChangeEvent.getValue().getId());
                });
                if (item != null) {
                    brandBox.setItems(data.getProductBrands());
                }

                if (item.getProductBrand() != null) {
                    brandBox.setSelected(item.getProductBrand());
                }
                widgets[index++] = brandBox;
            }
        }
        return widgets;
    }


    private void onChangeProductCategory(ValueChangeEvent<SelectItem> categoryItem, RentalOrderItem item) {
        item.setProductCategory(categoryItem.getValue());
        ProductService.App.get().getProductCategoryCF(categoryItem.getValue().getId(), new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {

                if (result != null && result.size() > 0) {
                    if (itemsTable.getGrid().getCurrentRow() < itemsTable.getGrid().getRowCount()) {
                        itemsTable.addRow(itemsTable.getGrid().getCurrentRow(), getWidgets(item));
                    } else {
                        itemsTable.addRow(getWidgets(item));
                    }
                }
            }
        });
    }

    public ExtendedHTML getZeroAsHTML() {
        final ExtendedHTML zeroValue = new ExtendedHTML(AccountingUtils.getZero());
        zeroValue.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        return zeroValue;
    }

    public ArrayList<RentalOrderItem> getRentalOrderItemsData() {
        final ArrayList<RentalOrderItem> rentalOrderItems = new ArrayList<>();
        for (int i = 0; i < itemsTable.getGrid().getRowCount(); i++) {
            if (itemsTable.isItemValid(i)) {
                final RentalOrderItem rentalOrderItem = new RentalOrderItem();

                final SmartProductLookUp rentalItemLookUp = (SmartProductLookUp) itemsTable.getColumnById(i, ItemTableConstants.PRODUCT);
                rentalOrderItem.setRentalItem(rentalItemLookUp.getSelectedItem());

                final TextArea2 description = (TextArea2) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
                if (description != null) {
                    rentalOrderItem.setDescription(description.getText());
                }

                final CustomCellTextBox quantity = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.QTY);
                if (quantity != null) {
                    rentalOrderItem.setQty(quantity.getValue() != null ? AccountingUtils.get().parseToBigDecimal(quantity.getValue()) : ZERO);
                }

                final CustomCellTextBox price = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.UNITPRICE);
                if (price != null) {
                    rentalOrderItem.setPrice(price.getValue() != null ? AccountingUtils.parsePriceToBigDecimal(price.getValue()) : ZERO);
                }

                final SmartTaxRateLookUp taxLookUp = (SmartTaxRateLookUp) itemsTable.getColumnById(i, ItemTableConstants.TAX_LIST);
                if (taxLookUp != null) {
                    rentalOrderItem.setTaxItem(taxLookUp.getSelectedData());
                    rentalOrderItem.setTaxAmount(taxLookUp.getItemTaxAmount());
                }
                final ExtendedHTML netamount = (ExtendedHTML) itemsTable.getColumnById(i, ItemTableConstants.NET_AMT);
                if (netamount != null) {
                    rentalOrderItem.setNetAmount(netamount.getValue());
                }

                final ExtendedHTML linetotal = (ExtendedHTML) itemsTable.getColumnById(i, ItemTableConstants.TOTAL_AMT);
                if (linetotal != null) {
                    rentalOrderItem.setSubTotal(linetotal.getValue());
                }

                if (rentalOrderItem.getRentalItem() != null && rentalOrderItem.getRentalItem().getId() != null) {
                    rentalOrderItems.add(rentalOrderItem);
                }
            }
        }
        return rentalOrderItems;
    }

    private boolean validateItemTable() {
        itemsTable.setValidRows(0);

        boolean errorFound = false;

        final ArrayList<String> requiredColumnCodes = new ArrayList<>();
        int requiredRow = 0;

        if (columnsMap != null && columnsMap.values().size() > 0) {
            for (final ColumnConfigs columnConfigs : columnsMap.values()) {
                if (columnConfigs != null && columnConfigs.isRequired() && columnConfigs.getCompanyCustomFieldID() == null) {
                    requiredRow++;
                    requiredColumnCodes.add(columnConfigs.getCode());
                }
            }
        }

        for (int rowID = 0; rowID < itemsTable.getGrid().getRowCount(); rowID++) {
            int rowError;

            itemsTable.resetValidation(rowID);
            rowError = validateRequiredItems(rowID, requiredColumnCodes);
            if (rowError == 0) {
                itemsTable.setItemValid(rowID, true);
                itemsTable.incValidRow();
            } else if (rowError == requiredRow) {

                if (!areOtherRowsAffected(rowID)) {
                    itemsTable.setItemValid(rowID, false); // exclude
                } else {
                    colorizeErrorField(rowID, requiredColumnCodes);
                    errorFound = true;
                }
            } else {
                colorizeErrorField(rowID, requiredColumnCodes);
                errorFound = true;
            }
        }
        if (itemsTable.getValidRows() == 0) {
            colorizeErrorField(0, requiredColumnCodes);
            errorFound = true;
        }
        return errorFound;
    }

    private int validateRequiredItems(final int rowID, final ArrayList<String> requiredColumnCodes) {
        int errors = 0;
        final SmartProductLookUp product = (SmartProductLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.PRODUCT);

        if (requiredColumnCodes.contains(ItemTableConstants.PRODUCT) && !Validation.validateLookUpRequired(product)) {
            itemsTable.setColumnValid(ItemTableConstants.DESCRIPTION);
            errors++;
        }

        if (requiredColumnCodes.contains(ItemTableConstants.DESCRIPTION)) {
            final TextArea2 descriptionTxtArea = (TextArea2) itemsTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
            if (!Validation.validateTextAreaRequired(descriptionTxtArea)) {
                itemsTable.setColumnValid(ItemTableConstants.DESCRIPTION);
                errors++;
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.QTY)) {
            final CustomCellTextBox qtyTxtBox = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.QTY);
            if (!Validation.validateTextBoxRequired(qtyTxtBox)) {
                itemsTable.setColumnValid(ItemTableConstants.QTY);
                errors++;
            } else if (columnsMap.get(ItemTableConstants.QTY) != null && AccountingUtils.get().parseToBigDecimal(qtyTxtBox.getValue()).compareTo(columnsMap.get(ItemTableConstants.QTY).getMinValue()) < 0) {
                itemsTable.setColumnValid(ItemTableConstants.QTY);
                errors++;
            }

        }
        if (requiredColumnCodes.contains(ItemTableConstants.UNITPRICE)) {
            final CustomCellTextBox txtPrice = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);
            if (!Validation.validateTextBoxRequired(txtPrice)) {
                itemsTable.setColumnValid(ItemTableConstants.UNITPRICE);
                errors++;
            } else if (columnsMap.get(ItemTableConstants.UNITPRICE) != null && AccountingUtils.get().parseToBigDecimal(txtPrice.getValue()).compareTo(columnsMap.get(ItemTableConstants.UNITPRICE).getMinValue()) < 0) {
                itemsTable.setColumnValid(ItemTableConstants.UNITPRICE);
                errors++;
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.TAX_LIST)) {
            final SmartTaxRateLookUp taxRateLookUp = (SmartTaxRateLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.TAX_LIST);
            if (taxRateLookUp.getSelectedItemID() == null) {
                itemsTable.setColumnValid(ItemTableConstants.TAX_LIST);
                errors++;
            }
        }

        return errors;
    }

    private boolean areOtherRowsAffected(final int rowID) {
        boolean result = false;

        final SmartProductLookUp rentalItemLookup = (SmartProductLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        final CustomCellTextBox qtyTxtBox = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.QTY);
        final TextArea2 descriptionTxtArea = (TextArea2) itemsTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
        final SmartTaxRateLookUp taxRateLookUp = (SmartTaxRateLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.TAX_LIST);
        final CustomCellTextBox price = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);

        result |= descriptionTxtArea != null && (descriptionTxtArea.getText() != null && !"".equals(descriptionTxtArea.getText().trim()));
        result |= taxRateLookUp != null && (taxRateLookUp.getSelectedItem() != null && taxRateLookUp.getSelectedItem().getId() != null);
        result |= price != null && (price.getText() != null && !"".equals(price.getText().trim()));
        result |= rentalItemLookup != null && (rentalItemLookup.getSelectedItem() != null && rentalItemLookup.getSelectedItem().getId() != null);
        result |= qtyTxtBox != null && (qtyTxtBox.getText() != null && !"".equals(qtyTxtBox.getText().trim()));
        return result;
    }

    private void colorizeErrorField(final int rowID, final ArrayList<String> requiredColumnCodes) {
        SmartProductLookUp productLookUp = (SmartProductLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
        CustomCellTextBox qtyTxtBox = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.QTY);

        if (requiredColumnCodes.contains(ItemTableConstants.PRODUCT)) {
            if (productLookUp.getSelectedItem() == null || productLookUp.getSelectedItem().getId() == null) {
                itemsTable.notValid(rowID, ItemTableConstants.PRODUCT);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.DESCRIPTION)) {
            final TextArea2 descriptionTxtArea = (TextArea2) itemsTable.getColumnById(rowID, ItemTableConstants.DESCRIPTION);
            if (!Validation.validateTextAreaRequired(descriptionTxtArea)) {
                itemsTable.notValid(rowID, ItemTableConstants.DESCRIPTION);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.QTY)) {
            if (!Validation.validateTextBoxRequired(qtyTxtBox)) {
                itemsTable.notValid(rowID, ItemTableConstants.QTY);
            } else if (columnsMap.get(ItemTableConstants.QTY) != null && AccountingUtils.get().parseToBigDecimal(qtyTxtBox.getValue()).compareTo(columnsMap.get(ItemTableConstants.QTY).getMinValue()) < 0) {
                itemsTable.notValid(rowID, ItemTableConstants.QTY);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.UNITPRICE)) {
            final CustomCellTextBox txtPrice = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);
            if (!Validation.validateTextBoxRequired(txtPrice)) {
                itemsTable.notValid(rowID, ItemTableConstants.UNITPRICE);
            } else if (columnsMap.get(ItemTableConstants.UNITPRICE) != null && AccountingUtils.get().parseToBigDecimal(qtyTxtBox.getValue()).compareTo(columnsMap.get(ItemTableConstants.UNITPRICE).getMinValue()) < 0) {
                itemsTable.notValid(rowID, ItemTableConstants.UNITPRICE);
            }
        }

        if (requiredColumnCodes.contains(ItemTableConstants.TAX_LIST)) {
            final SmartTaxRateLookUp taxRateLookUp = (SmartTaxRateLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.TAX_LIST);
            if (!Validation.validateLookUpRequired(taxRateLookUp)) {
                itemsTable.notValid(rowID, ItemTableConstants.TAX_LIST);
            }
        }
    }

    public void calculate() {

        BigDecimal subTotalAmount = BigDecimal.ZERO, totalAmount = BigDecimal.ZERO;
        Map<Integer, BigDecimal> taxTotal = new HashMap<>();
        for (int rowID = 0; rowID < itemsTable.getGrid().getRowCount(); rowID++) {
            BigDecimal subtotal = BigDecimal.ZERO;
            BigDecimal itemTaxAmount = BigDecimal.ZERO;
            CustomCellTextBox txtPrice = null;
            SmartProductLookUp productLookUp = (SmartProductLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
            if (productLookUp != null && productLookUp.getSelectedItemID() != null) {
                if (columnsMap.containsKey(ItemTableConstants.UNITPRICE)) {
                    txtPrice = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.UNITPRICE);
                    if (txtPrice.getLayoutData() != null) {
                        txtPrice.setValue(AccountingUtils.get().formatPrice(AccountingUtils.get().parseToBigDecimal(txtPrice.getText())));
                    }
                }
                if (columnsMap.containsKey(ItemTableConstants.QTY)) {
                    final CustomCellTextBox txtQty = (CustomCellTextBox) itemsTable.getColumnById(rowID, ItemTableConstants.QTY);
                    if (txtQty.getValue() != null && !txtQty.getValue().isEmpty()) {
                        if (txtPrice != null && txtPrice.getValue() != null && !txtPrice.getValue().isEmpty()) {
                            subtotal = subtotal.add(AccountingUtils.get().parseToBigDecimal(txtPrice.getValue().replace(" ", ""))
                                    .multiply(AccountingUtils.get().parseToBigDecimal(txtQty.getValue().replace(" ", ""))));
                        }
                    }
                }
                if (columnsMap.containsKey(ItemTableConstants.TAX_LIST)) {
                    SmartTaxRateLookUp taxLookUp = (SmartTaxRateLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.TAX_LIST);
                    if (taxLookUp != null && taxLookUp.getSelectedItemID() != null) {
                        itemTaxAmount = calculateTaxAmount(taxLookUp, taxTotal, subtotal);
                    } else if (taxLookUp != null) {
                        taxLookUp.setItemTaxAmount(BigDecimal.ZERO);
                    }
                }
                ExtendedHTML netAmount = (ExtendedHTML) itemsTable.getColumnById(rowID, ItemTableConstants.NET_AMT);
                if (netAmount != null) {
                    netAmount.setText(numberFormat.format(subtotal));
                    netAmount.setValue(subtotal);
                    final CustomCell netAmountCell = (CustomCell) itemsTable.getColumnCellWidgetById(rowID, ItemTableConstants.NET_AMT);
                    netAmountCell.InActive();
                }

                ExtendedHTML totalWidget = (ExtendedHTML) itemsTable.getColumnById(rowID, ItemTableConstants.TOTAL_AMT);
                if (totalWidget != null) {
                    BigDecimal lineTotal = subtotal;
                    if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(taxCalcTypeListBox.getSelectedId())) {
                        lineTotal = lineTotal.add(itemTaxAmount);
                    }
                    totalWidget.setText(numberFormat.format(lineTotal));
                    totalWidget.setValue(lineTotal);
                    final CustomCell totalCell = (CustomCell) itemsTable.getColumnCellWidgetById(rowID, ItemTableConstants.TOTAL_AMT);
                    totalCell.InActive();
                }

                subTotalAmount = subTotalAmount.add(subtotal);
                totalAmount = totalAmount.add(subtotal);
                if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(taxCalcTypeListBox.getSelectedId())) {
                    totalAmount = totalAmount.add(itemTaxAmount);
                }
            }
        }
        subTotal.setValue(subTotalAmount);
        total.setValue(totalAmount);

        drawTotalsTable();
    }

    private BigDecimal calculateTaxAmount(TaxLookUp taxLookUp, Map<Integer, BigDecimal> taxTotal, BigDecimal discountedNet) {
        TaxView taxHTML = (TaxView) taxWidgetMap.get(taxLookUp.getSelectedItemID());
        BigDecimal itemTaxAmount = BigDecimal.ZERO;

        if (taxHTML == null) {
            taxHTML = new TaxView();
            taxHTML.setItem(taxLookUp.getSelectedData());
            taxWidgetMap.put(taxLookUp.getSelectedItemID(), taxHTML);
        }

        if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
            BigDecimal taxPercent = taxHTML.getItem().getEffectiveTaxPercent();
            itemTaxAmount = discountedNet.multiply(taxPercent).divide(AccountingConstants.HUNDRED.add(taxPercent), AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
        } else if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(taxCalculationType)) {
            itemTaxAmount = discountedNet.multiply(taxHTML.getItem().getEffectiveTaxPercent().divide(AccountingConstants.HUNDRED, AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP));
        }

        itemTaxAmount = itemTaxAmount.setScale(AccountingUtils.systemCalculationScale, RoundingMode.HALF_UP);
        BigDecimal totalTax = taxTotal.get(taxLookUp.getSelectedItemID());
        BigDecimal currentTaxTotal = (totalTax != null ? totalTax : BigDecimal.ZERO).add(itemTaxAmount);

        taxHTML.setHTML(numberFormat.format(currentTaxTotal));
        taxTotal.put(taxLookUp.getSelectedItemID(), currentTaxTotal);
        taxLookUp.setItemTaxAmount(itemTaxAmount);

        return itemTaxAmount;
    }

    public void onTaxCalculationTypeChange(Integer type, boolean calculate) {
        setTaxCalculationType(type == null ? AccountingConstants.TAX_CALCULATION_EXCLUSIVE : type, calculate);
    }

    private void setTaxCalculationType(Integer taxCalculationType, boolean calculate) {
        this.taxCalculationType = taxCalculationType;
        for (int i = 0; i < itemsTable.getGrid().getRowCount(); i++) {
            SmartTaxRateLookUp lookUp = (SmartTaxRateLookUp) itemsTable.getColumnById(i, ItemTableConstants.TAX_LIST);
            if (lookUp != null) {
                if (AccountingConstants.NO_TAX_CALCULATION.equals(taxCalculationType)) {
                    lookUp.setSelected(new SelectItem());
                    lookUp.clear();
                    lookUp.setEnabled(false);
                    taxWidgetMap.clear();
                } else {
                    lookUp.setEnabled(true);
                }
            }
        }
        if (calculate) {
            calculate();
        }
    }

    private void reDrawTaxesDropdown() {
        taxWidgetMap.clear();
        for (int i = 0; i < itemsTable.getGrid().getRowCount(); i++) {
            if (itemsTable.getColumnById(i, ItemTableConstants.TAX_LIST) != null) {
                SmartTaxRateLookUp taxLookUp = (SmartTaxRateLookUp) itemsTable.getColumnById(i, ItemTableConstants.TAX_LIST);
                if (taxLookUp.getSelectedItemID() != null && !taxWidgetMap.containsKey(taxLookUp.getSelectedItemID())) {
                    TaxView tax = new TaxView(AccountingUtils.getZero());
                    tax.setItem(taxLookUp.getData(taxLookUp.getSelectedItemID()));
                    taxWidgetMap.put(taxLookUp.getSelectedItemID(), tax);
                }
            }
        }
        calculate();
    }
}
