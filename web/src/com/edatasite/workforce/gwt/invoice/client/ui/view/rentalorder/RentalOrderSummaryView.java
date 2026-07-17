package com.edatasite.workforce.gwt.invoice.client.ui.view.rentalorder;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderData;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnOffsetEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.view.ExtendedHTML;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartProductLookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class RentalOrderSummaryView extends CustomForm2 implements Colapse, Constants, HasLinksInterface {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingUtils utils = AccountingUtils.get();
    protected final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    private final Integer objectID;
    private RentalOrderData data;
    private Anchor crmAccountLookUp;
    private MaterialLink customerBalanceLink;
    private EditableTable itemsTable;
    private RentalOrderReceiptTable totalsTable;
    private HTML termsLookUp;
    private HTML startDatePicker;
    private HTML expirationDatePicker;
    private HTML numberWidget;
    private HTML taxCalcTypeListBox;
    private HTML approvers;
    private SplitButton printPdfSplitButton;
    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private FooterInformer link;
    private HasLinks linkingUtil;
    private final AtomicBoolean firstClick = new AtomicBoolean(true);
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

    public RentalOrderSummaryView(Integer objectID) {
        super("rentalordersummary");
        setDescription(property.getSingular(wfmStrings.summaryView(), accountingStrings.rentalOrder()));
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALES_INVOICE_CONVERT_AND_ADD, RentalOrderSummaryView.this, (sender, args) -> {
            if (args instanceof SaveResult) {
                updateStatusRentalOrder(RENTAL_INVOICED, new SelectItem (((SaveResult) args).getId(), ((SaveResult) args).getNumber()));
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PREPAYMENT_ADDED_FROM_RENTAL_ORDER, RentalOrderSummaryView.this, (sender, args) -> closeTab());

        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.RentalOrdersView, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                RentalOrderSummaryView.super.onInitialize();
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
                    initButtonsPanel();
                    drawItemTableSection();
                    setInnerHTML(numberWidget, data.getNumberData().getNumberString());
                    if (data.getStartDate() != null) {
                        startDatePicker.setHTML(DateUtils.getDateFormatShort(data.getStartDate()));
                    }
                    if (data.getExpirationDate() != null) {
                        expirationDatePicker.setHTML(DateUtils.getDateFormatShort(data.getExpirationDate()));
                    }
                    if (data.getCustomer() != null) {
                        crmAccountLookUp.setHTML(data.getCustomer().getName());
                        crmAccountLookUp.addClickHandler(clickEvent -> {
                            SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + data.getCustomer().getId(), data.getCustomer().getName(), data.getCustomer().getName());
                        });
                        if (data.getSupplierCustomerBalance() >= 0) {
                            customerBalanceLink.setText(utils.formatPrice(data.getSupplierCustomerBalance()));
                        } else {
                            customerBalanceLink.setText("(" + utils.formatPrice((-1) * data.getSupplierCustomerBalance()) + ")");
                        }
                        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                        customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + data.getCustomer().getId() + "/" + CrmAccountItem.CUSTOMER,
                                wfmStrings.balance() + ": " + data.getCustomer().getName(), data.getCustomer().getName()));
                    }
                    if (data.getPaymentTerms() != null) {
                        termsLookUp.setHTML(data.getPaymentTerms().getName());
                    }
                    if (data.getApprover() != null) {
                        approvers.setHTML(data.getApprover().getName());
                    }
                    taxCalcTypeListBox.setHTML(data.getTaxCalculationType() != null ? AccountingUtils.getTaxCalcType(data.getTaxCalculationType()).getName() : AccountingUtils.getTaxCalcType(AccountingConstants.TAX_CALCULATION_EXCLUSIVE).getName());
                    link.setBadgeCount(data.getRelationItems() != null ? data.getRelationItems().size() : 0);

                    getCustomFieldUtil().fillCustomFieldsWithData(data.getCustomFieldItems(), true);

                    if (data.getRentalOrderItems() != null && !data.getRentalOrderItems().isEmpty()) {
                        for (int i = 0; i < data.getRentalOrderItems().size(); i++) {
                            itemsTable.addRow(getWidgets(data.getRentalOrderItems().get(i)));
                        }
                        drawTotalsTable();
                    }
                    pdfTool(data);
                    drawFooter();
                }
            }
        });
    }

    private void drawFooter() {
        NoteHistoryWidget noteHistoryWidget = new NoteHistoryWidget(callback -> {
            if (objectID == null) return;
            RentalOrderService.App.get().loadRentalOrderHistory(objectID, callback);
        });
        if (objectID != null) {
            noteHistoryWidget.setSaveIntoDatabase(historyItem -> {
                if (historyItem != null) {
                    LoadingPanel.loading(true);
                    RentalOrderService.App.get().saveRentalOrderHistory(objectID, historyItem, new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Integer hisItemId) {
                            historyItem.setObjectID(hisItemId);
                            LoadingPanel.loading(false);
                        }
                    });
                }
            });

            noteHistoryWidget.setRemoveFromDatabase((hisItem) -> {
                if (hisItem != null && hisItem.getObjectID() != null) {
                    LoadingPanel.loading(true);
                    RentalOrderService.App.get().deleteRentalOrderComment(hisItem.getObjectID(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Void aVoid) {
                            LoadingPanel.loading(false);
                        }
                    });
                }
            });
        }

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.setInitialClasses("informer-item history-notes-container");
        footer.addToLeftSide(informer);
    }

    private void initButtonsPanel() {
        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_RENTAL_ORDER_PRINT_PDF)) {
            addRightButton(printPdfSplitButton);
        }

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PREPAYMENT_ADD)) {
            WfmButton2 addPrepaymentBtn = new WfmButton2(accountingStrings.addcustomerPrepayment(), BTN_PRIMARY);
            addPrepaymentBtn.addClickHandler(click -> goTo("prepayment|add/addFromRentalOrder/" + data.getObjectID(), ""));
            addRightButton(addPrepaymentBtn);
        }

        if (Utils.hasPermission(ACCOUNTING_RENTAL_ORDER_RETURN) && (RENTAL_INVOICED.equals(data.getStatusCode()))) {
            WfmButton2 returnBtn = new WfmButton2("Return", WfmButton2.BTN_PRIMARY); // todo
            returnBtn.addClickHandler(click -> Info.show(wfmStrings.comingsoon(), Info.Type.INFO));
            addRightButton(returnBtn);
        }

        if (Utils.hasPermission(ACCOUNTING_RENTAL_ORDER_DELIVER) && (RENTAL_APPROVED.equals(data.getStatusCode()))) {
            WfmButton2 deliverBtn = new WfmButton2("Deliver", BTN_SUCCESS); // todo
            deliverBtn.addClickHandler(click -> {
                if (validateItemTable()) {
                    Info.show(wfmStrings.unableToSave() + ".", Info.Type.WARNING);
                } else {
                    save();
                }
            });
            addRightButton(deliverBtn);
        }

        if (data.isApproveProcessEnabled()) {
            WfmButton2 submitButton = addButton(wfmStrings.submit(), BTN_SUCCESS, clickEvent -> updateStatusRentalOrder(Constants.RENTAL_SUBMITTED, null));
            submitButton.setVisible(false);
            WfmButton2 approveButton = addButton(wfmStrings.approve(), BTN_SUCCESS, clickEvent -> updateStatusRentalOrder(Constants.RENTAL_APPROVED, null));
            approveButton.setVisible(false);
            WfmButton2 rejectButton = addButton(wfmStrings.reject(), BTN_REJECT, clickEvent -> updateStatusRentalOrder(Constants.RENTAL_REJECTED, null));
            rejectButton.setVisible(false);
            addField(CustomFormConstants.APPROVERS, approvers, getTitle(wfmStrings.approvers()));

            Integer currentApproverId = data.getApprover() != null ? data.getApprover().getId() : null;
            Integer currentUserId = Utils.getUserID();
            if (currentUserId.equals(currentApproverId)) {
                if (RENTAL_SUBMITTED.equals(data.getStatusCode())) {
                    approveButton.setVisible(true);
                    rejectButton.setVisible(true);
                } else if (RENTAL_REJECTED.equals(data.getStatusCode())) {
                    approveButton.setVisible(true);
                }
            } else {
                if (RENTAL_REJECTED.equals(data.getStatusCode()) && data.getCreator() != null && currentUserId.equals(data.getCreator().getId())) {
                    submitButton.setVisible(true);
                }
            }
        }
    }

    private void drawForm() {
        drawMainSection();
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID, true);
    }

    private void drawMainSection() {
        crmAccountLookUp = new Anchor(wfmStrings.notAvailable());
        numberWidget = initHTML();
        termsLookUp = initHTML();
        startDatePicker = initHTML();
        expirationDatePicker = initHTML();
        taxCalcTypeListBox = initHTML();
        approvers = initHTML();
        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        FormGroup customerField = new FormGroup(crmAccountLookUp);
        customerField.ensureDebugId(InvoiceFormFields.CUSTOMER);
        Div clientFieldLabel = customerField.getGroupLabel();
        clientFieldLabel.addStyleName("label-group");
        clientFieldLabel.add(new Span(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
        Span balance = new Span(wfmStrings.balance() + ": ");
        balance.add(customerBalanceLink);
        clientFieldLabel.add(balance);

        addTitleField(CustomFormConstants.INFORMATION, property.getSingular(accountingStrings.productInformation(), accountingStrings.rentalOrder()));

        if (formPropertyMap != null && formPropertyMap.get(AccountingCustomFormConstants.CUSTOMER) != null) {
            addField(AccountingCustomFormConstants.CUSTOMER, crmAccountLookUp, Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        } else {
            addField(AccountingCustomFormConstants.CUSTOMER, crmAccountLookUp, Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, numberWidget, formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number());
        } else {
            addField(CustomFormConstants.NUMBER, numberWidget, wfmStrings.number());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM) != null) {
            addField(CustomFormConstants.CLIENT_INVOICE_TERM, termsLookUp, formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_INVOICE_TERM).getTitle() : wfmStrings.paymentTerms());
        } else {
            addField(CustomFormConstants.CLIENT_INVOICE_TERM, termsLookUp, getTitle(wfmStrings.paymentTerms()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null) {
            addField(CustomFormConstants.START_DATE, startDatePicker, formPropertyMap.get(CustomFormConstants.START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.START_DATE).getTitle() : wfmStrings.startDate());
        } else {
            addField(CustomFormConstants.START_DATE, startDatePicker, getTitle(wfmStrings.startDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DATE) != null) {
            addField(CustomFormConstants.DATE, expirationDatePicker, formPropertyMap.get(CustomFormConstants.DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DATE).getTitle() : wfmStrings.expiryDate());
        } else {
            addField(CustomFormConstants.DATE, expirationDatePicker, getTitle(wfmStrings.expiryDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE) != null) {
            addField(TAX_CALC_TYPE, taxCalcTypeListBox, formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.TAX_CALC_TYPE).getTitle() : wfmStrings.amount());
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

        itemsTable = new EditableTable(getColumns(), false, false);
        itemsTable.setDraggable(false);
        totalsTable = new RentalOrderReceiptTable();
        totalsTable.addStyleName("totalsTable");
        initTotalTableWidgets();

        GColumn cTotalTable = new GColumn(GColumnEnum.COL_3, totalsTable);
        cTotalTable.setOffset(GColumnOffsetEnum.OFFSET_9);
        Div itemsTableContainer = new Div();
        itemsTableContainer.add(new GRow(new GColumn(GColumnEnum.COL_12, itemsTable)));
        itemsTableContainer.add(new GRow(cTotalTable));
        addField(CustomFormConstants.ITEMS, itemsTableContainer, null, true);
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
        subTotal.setHTML(data.getSubTotal() != null ? numberFormat.format(data.getSubTotal()) : "0.00");
        total.setHTML(data.getTotal() != null ? numberFormat.format(data.getTotal()) : "0.00");
        totalsTable.setSubtotalItem(subTotalLabel, subTotal);

        if (data.getTaxAmount() != null && !AccountingConstants.NO_TAX_CALCULATION.equals(data.getTaxCalculationType()) && data.getTaxAmount().compareTo(BigDecimal.ZERO) > 0) {
            ExtendedHTML tax = getZeroAsHTML();
            HTML taxLabel = new HTML(wfmStrings.taxTotal());
            taxLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
            tax.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
            tax.setHTML(numberFormat.format(data.getTaxAmount()));
            totalsTable.addItem(taxLabel, tax);
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
        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(data.getRelationItems(), false);
                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });

        footer.addToLeftSide(link);
    }

    private boolean validateItemTable() {
        itemsTable.setValidRows(0);
        boolean hasErrors = false;
        int rowCount = itemsTable.getGrid().getRowCount();

        for (int rowID = 0; rowID < rowCount; rowID++) {
            itemsTable.resetValidation(rowID);

            SmartProductLookUp product = (SmartProductLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.PRODUCT_FOR_RENT);
            boolean isValid = product != null && product.getSelectedItem() != null && product.getSelectedItem().getId() != null;

            if (isValid) {
                itemsTable.setItemValid(rowID, true);
                itemsTable.incValidRow();
            } else {
                itemsTable.notValid(rowID, ItemTableConstants.PRODUCT_FOR_RENT);
                hasErrors = true;
            }
        }

        if (itemsTable.getValidRows() == 0 && rowCount > 0) {
            itemsTable.notValid(0, ItemTableConstants.PRODUCT_FOR_RENT);
            hasErrors = true;
        }

        return hasErrors;
    }

    private void save() {
        HashMap<Integer, Integer> rentalProductsIds = new HashMap<>();
        ArrayList<RentalOrderItem> rentalOrderItems = data.getRentalOrderItems();
        for (int rowID = 0; rowID < itemsTable.getGrid().getRowCount(); rowID++) {
            SmartProductLookUp rentalItem = (SmartProductLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.PRODUCT);
            SmartProductLookUp productForRent = (SmartProductLookUp) itemsTable.getColumnById(rowID, ItemTableConstants.PRODUCT_FOR_RENT);
            for (RentalOrderItem rentalOrderItem : rentalOrderItems) {
                if (rentalOrderItem.getRentalItem().getId().equals(rentalItem.getSelectedItemID())) {
                    rentalProductsIds.put(rentalOrderItem.getObjectID(), productForRent.getSelectedItemID());
                }
            }
        }

        RentalOrderService.App.get().saveProductForRentItemToRentalOrder(data.getObjectID(), rentalProductsIds, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(Void result) {
                goTo("saleinvoice|add/add/convertToInvoiceFromRentalOrder/" + data.getObjectID(), data.getNumber());
            }
        });
    }

    private void updateStatusRentalOrder(String statusCode, SelectItem invoiceItem) {
        LoadingPanel.loading(true);
        RentalOrderService.App.get().updateStatusRentalOrder(data.getObjectID(), statusCode, invoiceItem, new AbstractAsyncCallback<Void>() {

            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void result) {
                WfmUiEventsBus.fireWfmUiEvent(invoiceItem == null ? WfmUiEventType.ON_RENTAL_ORDER_ADDED : WfmUiEventType.ON_SALES_INVOICE_CONVERT_AND_ADD, result, RentalOrderSummaryView.this);
                LoadingPanel.loading(false);
                closeTab();
            }
        });
    }

    public void pdfTool(RentalOrderData result) {
        if (printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectID);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/rentalOrderViewPdfHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
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
        return LayoutRPC.VIEW;
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

    private ColumnConfig[] getColumns() {
        final LinkedList<ColumnConfig> columnsList = new LinkedList<>();
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
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, columnConfigs.isChanged() ? columnConfigs.getTitle() : accountingStrings.rentalItem(), Utils.getColumnWidth(columnConfigs.getWidth(), 200), columnConfigs.isRequired());
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.PRODUCT_FOR_RENT:
                    columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT_FOR_RENT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.item(), Utils.getColumnWidth(columnConfigs.getWidth(), 200), columnConfigs.isRequired());
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.DESCRIPTION:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.description(), Utils.getColumnWidth(columnConfigs.getWidth(), 250), columnConfigs.isRequired(), "product-description-cell");
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.UNITPRICE:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.UNITPRICE, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.price(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.QTY:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.QTY, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.qty(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.TAX_LIST:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.TAX_LIST, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.taxRate(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.NET_AMT:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.NET_AMT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.netAmount(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
                    columnConfig.setPixel(isPixel);
                    columnConfig.setForceWidthInPercent(!isPixel);
                    columnsList.add(columnConfig);
                    break;
                case ItemTableConstants.TOTAL_AMT:
                    columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.TOTAL_AMT, columnConfigs.isChanged() ? columnConfigs.getTitle() : wfmStrings.totalAmount(), Utils.getColumnWidth(columnConfigs.getWidth(), 75), columnConfigs.isRequired(), Constants.RIGHT_ALIGN_CELL);
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
            if (ItemTableConstants.CATEGORY.equals(columnCode)) {
                ExtendedHTML category = new ExtendedHTML();
                if (item.getProductCategory() != null) {
                    category.setText(item.getProductCategory().getName());
                }
                widgets[index++] = category;
            } else if (ItemTableConstants.BRAND.equals(columnCode)) {
                ExtendedHTML brand = new ExtendedHTML();
                if (item.getProductBrand() != null) {
                    brand.setText(item.getProductBrand().getName());
                }
                widgets[index++] = brand;
            } else if (ItemTableConstants.PRODUCT.equals(columnCode)) {
                SmartProductLookUp rentalItem = new SmartProductLookUp(Constants.RENTAL_PRODUCTS);
                rentalItem.setValueNotEmptyMeansSelected(true);
                rentalItem.setWidth("100%");
                rentalItem.addStyleName("lookUp-moveRight");
                rentalItem.getSuggestBox().setWidth("100%");
                rentalItem.setEnabled(false);
                if (item.getRentalItem() != null) {
                    rentalItem.setSelected(new ProductSelectItem(item.getRentalItem().getId(), item.getRentalItem().getName()));
                }
                widgets[index++] = rentalItem;
            } else if (ItemTableConstants.PRODUCT_FOR_RENT.equals(columnCode)) {
                    SmartProductLookUp productForRent = new SmartProductLookUp(Constants.RENTAL_ORDERS, item.getObjectID(), item.getFromDate(), item.getToDate());
                    productForRent.setValueNotEmptyMeansSelected(true);
                    productForRent.setWidth("100%");
                    productForRent.addStyleName("lookUp-moveRight");
                    productForRent.addStyleName("lookUp-moveRight");
                    productForRent.getSuggestBox().setWidth("100%");
                    productForRent.setEnabled(columnsMap.get(columnCode) != null && !columnsMap.get(columnCode).isDisabled() && !RENTAL_INVOICED.equals(data.getStatusCode()));
                    if (item.getProductItem() != null) {
                        productForRent.setSelected(new ProductSelectItem(item.getProductItem().getId(), item.getProductItem().getName()));
                    }
                    productForRent.setTitle(columnCode);
                    widgets[index++] = productForRent;
            } else if (ItemTableConstants.DESCRIPTION.equals(columnCode)) {
                ExtendedHTML txtDescription = new ExtendedHTML();
                if (item.getDescription() != null) {
                    txtDescription.setText(item.getDescription());
                }
                txtDescription.setTitle(columnCode);
                widgets[index++] = txtDescription;
            } else if (ItemTableConstants.QTY.equals(columnCode)) {
                ExtendedHTML quantity = new ExtendedHTML();
                if (item.getQty() != null) {
                    quantity.setText(utils.formatQty(item.getQty()));
                }
                quantity.setTitle(columnCode);
                widgets[index++] = quantity;
            } else if (ItemTableConstants.UNITPRICE.equals(columnCode)) {
                ExtendedHTML txtPrice = new ExtendedHTML();
                if (item.getPrice() != null) {
                    txtPrice.setText(utils.formatPrice(item.getPrice()));
                }
                txtPrice.setTitle(columnCode);
                widgets[index++] = txtPrice;
            } else if (ItemTableConstants.TAX_LIST.equals(columnCode)) {
                ExtendedHTML taxLookUp = new ExtendedHTML();
                if (item.getTaxItem() != null) {
                    taxLookUp.setHTML(item.getTaxItem().getName());
                }
                widgets[index++] = taxLookUp;
            } else if (ItemTableConstants.NET_AMT.equals(columnCode)) {
                ExtendedHTML netAmount = getZeroAsHTML();
                if (item.getNetAmount() != null) {
                    netAmount.setText(numberFormat.format(item.getNetAmount()));
                }
                widgets[index++] = netAmount;
            } else if (ItemTableConstants.TOTAL_AMT.equals(columnCode)) {
                ExtendedHTML totalAmount = getZeroAsHTML();
                if (item.getSubTotal() != null) {
                    totalAmount.setText(numberFormat.format(item.getSubTotal()));
                }
                widgets[index++] = totalAmount;
            }
        }
        return widgets;
    }

    public ExtendedHTML getZeroAsHTML() {
        final ExtendedHTML zeroValue = new ExtendedHTML(AccountingUtils.getZero());
        zeroValue.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        return zeroValue;
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(RentalOrderSummaryView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return objectID;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_RENTAL_ORDER;
                }

                @Override
                public String getRelationName() {
                    return String.valueOf(objectID);
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }
}