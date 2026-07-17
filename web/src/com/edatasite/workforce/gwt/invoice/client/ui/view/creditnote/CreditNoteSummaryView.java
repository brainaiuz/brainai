package com.edatasite.workforce.gwt.invoice.client.ui.view.creditnote;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.AccountingSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.target.TargetErpService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceDynamicProjectLookupBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 15.07.2010
 * Time: 15:28:23
 * To change this template use File | Settings | File Templates.
 */
public class CreditNoteSummaryView extends InvoiceSummaryView {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final Integer creditNoteID;
    private final String noteType;


    public CreditNoteSummaryView(Integer creditNoteID, String type) {
        super("summary", RECEIVABLE.equals(type) ? creditNote : debitNote, RECEIVABLE.equals(type) ? RECEIVABLE_CREDIT_NOTE : PAYABLE_CREDIT_NOTE, true, true);
        this.creditNoteID = creditNoteID;
        this.noteType = type;
        property = new Property(getPropertyCode());
    }

    @Override
    protected LinkedHashMap<String, DynamicTableColumn> getColumnsMap(ColumnConfigs[] customColumns) {
        LinkedHashMap<String, DynamicTableColumn> columnsMap = new LinkedHashMap<>();
        if (customColumns != null && customColumns.length > 0) {

            for (ColumnConfigs column : customColumns) {
                switch (column.getCode()) {
                    case ProductsTable.PRODUCT:
                        columnsMap.put(ProductsTable.PRODUCT, new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.itemName(), ProductsTable.PRODUCT, 200));
                        break;
                    case ProductsTable.DESCRIPTION:
                        columnsMap.put(ProductsTable.DESCRIPTION, new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.description(), ProductsTable.DESCRIPTION, 250));
                        break;
                    case ProductsTable.QTY:
                        columnsMap.put(ProductsTable.QTY, new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.qty(), ProductsTable.QTY, 55, Constants.RIGHT_ALIGN_CELL));
                        columnsMap.get(ProductsTable.QTY).setPixel(true);
                        break;
                    case ProductsTable.MEASUREMENT:
                        columnsMap.put(ProductsTable.MEASUREMENT, new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.measurement(), ProductsTable.MEASUREMENT, 60));
                        columnsMap.get(ProductsTable.MEASUREMENT).setPixel(true);
                        break;
                    case ProductsTable.UNITPRICE:
                        columnsMap.put(ProductsTable.UNITPRICE, new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.price(), ProductsTable.UNITPRICE, 75, Constants.RIGHT_ALIGN_CELL));
                        columnsMap.get(ProductsTable.UNITPRICE).setPixel(true);
                        break;
                    case ProductsTable.DISCOUNT_AMT:
                        columnsMap.put(ProductsTable.DISCOUNT_AMT, new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.discount(), ProductsTable.DISCOUNT_AMT, 80, Constants.RIGHT_ALIGN_CELL));
                        columnsMap.get(ProductsTable.DISCOUNT_AMT).setPixel(true);
                        break;
                    case ProductsTable.DEPARTMENT:
                        columnsMap.put(ProductsTable.DEPARTMENT, new DynamicTableColumn(column.isChanged() ? column.getTitle() : Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), ProductsTable.DEPARTMENT, 60));
                        break;
                    case ProductsTable.ACCOUNT:
                        columnsMap.put(ProductsTable.ACCOUNT, new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.accountType(), ProductsTable.ACCOUNT, 100));
                        break;
                    case ProductsTable.NET_AMT:
                        columnsMap.put(ProductsTable.NET_AMT, new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.netAmount(), ProductsTable.NET_AMT, 80, Constants.RIGHT_ALIGN_CELL));
                        columnsMap.get(ProductsTable.NET_AMT).setPixel(true);
                        break;
                    case ProductsTable.TAX_LIST:
                        columnsMap.put(ProductsTable.TAX_LIST, new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.taxRate(), ProductsTable.TAX_LIST, 100));
                        break;
                    case ProductsTable.DOUBLE_TAX_LIST:
                        columnsMap.put(ProductsTable.DOUBLE_TAX_LIST, new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.taxRate(), ProductsTable.DOUBLE_TAX_LIST, 100));
                        break;
                    case ProductsTable.WAREHOUSE:
                        columnsMap.put(ProductsTable.WAREHOUSE, new DynamicTableColumn(column.isChanged() ? column.getTitle() : accountingStrings.warehouse(), ProductsTable.WAREHOUSE, 100));
                        break;
                    case ProductsTable.TOTAL_AMT:
                        columnsMap.put(ProductsTable.TOTAL_AMT, new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.totalAmount(), ProductsTable.TOTAL_AMT, 100, Constants.RIGHT_ALIGN_CELL));
                        columnsMap.get(ProductsTable.TOTAL_AMT).setPixel(true);
                        break;
                    case ProductsTable.PROJECT:
                        columnsMap.put(ProductsTable.PROJECT, new DynamicTableColumn(column.isChanged() ? column.getTitle() : Property.get(Constants.PROJECT, wfmStrings.project()), ProductsTable.PROJECT, 100));
                        break;
                    default:
                        columnsMap.put(column.getCode(), new DynamicTableColumn(column.getTitle(), column.getCode(), 100, column.isRequired()));
                        columnsMap.get(column.getCode()).setPixel(true);
                        break;
                }
            }
        } else {
            columnsMap.put(ProductsTable.PRODUCT, new DynamicTableColumn(wfmStrings.itemName(), ProductsTable.PRODUCT, 200));
            columnsMap.put(ProductsTable.DESCRIPTION, new DynamicTableColumn(wfmStrings.description(), ProductsTable.DESCRIPTION, 250));

            columnsMap.put(ProductsTable.QTY, new DynamicTableColumn(wfmStrings.qty(), ProductsTable.QTY, 75, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.QTY).setPixel(true);

            columnsMap.put(ProductsTable.UNITPRICE, new DynamicTableColumn(wfmStrings.price(), ProductsTable.UNITPRICE, 75, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.UNITPRICE).setPixel(true);

            columnsMap.put(ProductsTable.DISCOUNT_AMT, new DynamicTableColumn(wfmStrings.discount(), ProductsTable.DISCOUNT_AMT, 75, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.DISCOUNT_AMT).setPixel(true);

            columnsMap.put(ProductsTable.ACCOUNT, new DynamicTableColumn(wfmStrings.salesAccount(), ProductsTable.ACCOUNT, 100));

            columnsMap.put(ProductsTable.NET_AMT, new DynamicTableColumn(wfmStrings.netAmount(), ProductsTable.NET_AMT, 80, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.NET_AMT).setPixel(true);

            columnsMap.put(ProductsTable.TAX_LIST, new DynamicTableColumn(wfmStrings.taxRate(), ProductsTable.TAX_LIST, 100));
        }
        return columnsMap;
    }

    @Override
    protected void initializeInvoiceData() {
        invoiceService.getCreditNoteSummaryData(creditNoteID, new AbstractAsyncCallback<NewInvoice>() {
            public void success(NewInvoice result) {
                result.setCreditNote(true);
                if (result.getIntroduction() != null && !result.getIntroduction().isEmpty()) {
                    drawIntroductionPanel(result.getIntroduction());
                }
                if (result.getPaymentInstruction() != null && !result.getPaymentInstruction().isEmpty()) {
                    drawInstructionPanel(result.getPaymentInstruction());
                }
                initializeFormData(invoiceData = result);
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return Constants.SALE_INVOICE;
    }

    @Override
    protected void initializeButtons() {
        String status = invoiceData.getStatusCode();
        if (!PENDING.equals(status)) {
            //this one contains approve, approve&send to client, resend to client command list
            List<SplitButtonItem> approveCommandSubItems = new ArrayList<>();
            //this one contains edit, copy to new, add credit note, void options
            List<SplitButtonItem> optionsCommandSubItems = new ArrayList<>();
            //this one contains a list of pdf templates
            List<SplitButtonItem> pdfCommandSubItems = new ArrayList<>();

            boolean editPermission = RECEIVABLE.equals(noteType) ? Utils.hasPermission(ACCOUNTING_SALES_CREDIT_NOTE_EDIT) : Utils.hasPermission(ACCOUNTING_PURCHASE_CREDIT_NOTE_EDIT);
            boolean editFullPermission = RECEIVABLE.equals(noteType) ? Utils.hasPermission(ACCOUNTING_SALES_CREDIT_NOTE_FULL_EDIT_ACCESS) : Utils.hasPermission(ACCOUNTING_PURCHASE_CREDIT_NOTE_FULL_EDIT_ACCESS);

            if (Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
                optionsCommandSubItems.add(new SplitButtonItem("SEND_TO_TARGET", accountingStrings.sendToTarget(), () -> {
                    TargetErpService.App.get().sendInvoiceToTarget(invoiceData.getID(), new AsyncCallback<String>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                        }

                        @Override
                        public void onSuccess(String s) {
                            if (s != null && !s.startsWith("OK")) {
                                Info.show(s, Info.Type.WARNING);
                            } else {
                                Info.show(s.replace("OK:", ""), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, invoiceData.getID(), CreditNoteSummaryView.this);
                            }
                        }
                    });
                }));
            }

            if (((invoiceData.isSubmitter(Utils.getUserID()) && editPermission) || editFullPermission)
                    && (status.equals(DRAFT) || status.equals(REJECT) || status.equals(APPROVE) || status.equals(OPEN) || status.equals(OVER_DUE))
                    && (invoiceData.getPaymentItems() == null || invoiceData.getPaymentItems().length == 0)) {
                optionsCommandSubItems.add(new SplitButtonItem("EDIT_CREDIT_NOTE", wfmStrings.edit(), () -> {
                    closeTab();

                    if (RECEIVABLE.equals(noteType)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("receivablecreditnote|edit/" + creditNoteID, invoiceData.getInvoiceNumber());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("payablecreditnote|edit/" + creditNoteID, invoiceData.getInvoiceNumber());
                    }
                }));
            }

            if (status.equals(DRAFT)) {
                approveCommandSubItems.add(new SplitButtonItem(APPROVE, accountingStrings.approveCreditNote(), () -> {
                    setEnableButtons(false);

                    if (invoiceData.getTotal().compareTo(BigDecimal.ZERO) < 0) {
                        setEnableButtons(true);
                        Info.show(accountingMessages.totalAmountCantLessThanZero(), Info.Type.WARNING);
                        return;
                    }
                    invoiceService.approveCreditNote(creditNoteID, getAsyncCallback());
                }, true));
            }
            if (!Utils.hasRole(CLIENT) && isApprovedInvoiceOrCreditNote) {
                if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_SEND_EMAIL)) {
                    approveCommandSubItems.add(new SplitButtonItem(APPROVE_AND_SEND, status.equals(OPEN) ? accountingStrings.resendCreditNoteToClient() : wfmStrings.sendEmail(), () -> {
                        setEnableButtons(false);

                        if (invoiceData.getTotal().compareTo(BigDecimal.ZERO) < 0) {
                            setEnableButtons(true);
                            Info.show(accountingMessages.totalAmountCantLessThanZero(), Info.Type.WARNING);
                            return;
                        }
                        sendToClient(CREDIT_NOTE_CATEGORY);
                    }, approveCommandSubItems.size() == 0));
                }
                //It needs to be temporarily hidden because information is being sent to the Zatca via the taxilla.
            /*if (canSendToZatca()) {
                approveCommandSubItems.add(sendZatcaButton(CreditNoteSummaryView.this));
            }*/
            }

            /**
             * Print functionality sub items
             */
            {
                Integer defaultTemplateId = null;
                if (invoiceData != null
                        && invoiceData.getPdfTemplateList() != null
                        && invoiceData.getPdfTemplateList().getItems() != null) {
                    invoiceData.getPdfTemplateList().getItems();
                    for (SelectItem pdfItem : invoiceData.getPdfTemplateList().getItems()) {

                        if (pdfItem.isDefaultSelected()) {
                            defaultTemplateId = pdfItem.getId();
                        }
                        pdfCommandSubItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> pdfVersion(htmlPanel, pdfItem.getId())));
                    }
                }
                Integer finalDefaultTemplateId = defaultTemplateId;
                pdfCommandSubItems.add(new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> pdfVersion(htmlPanel, finalDefaultTemplateId), true));

                if (!Utils.hasRole(CLIENT) && (isApprovedInvoiceOrCreditNote || PAID.equals(status))) {

                    if (AccountingSinksContainer.hasRolesForAccounting && invoiceData.getPaymentItems() != null && invoiceData.getPaymentItems().length > 0) {
                        pdfCommandSubItems.add(new SplitButtonItem("GENERATE_RECEIPT", accountingStrings.generateReceipt(),
                                () -> {
                                    RequestObject requestObject = new RequestObject(creditNoteID);
                                    String pdfURL = CommandConstants.PDF_URL + "/salesReceiptViewPDFHandler";
                                    HashMap<String, String> parametrs = requestObject.getRequestParams();
                                    Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
                                }));
                    }
                }

                if (Utils.hasRoles(Constants.ADMIN)) {
                    pdfCommandSubItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", "Customize", new Command() {
                        @Override
                        public void execute() {
                            Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + (RECEIVABLE.equals(noteType) ? PdfTemplateTypeEnum.RECEIVABLE_CREDIT_NOTE.name() : PdfTemplateTypeEnum.PAYABLE_CREDIT_NOTE.name()));
                        }
                    }));
                }

                if (!pdfCommandSubItems.isEmpty()) {
                    printPdfSplitButton.addItemList(pdfCommandSubItems);
                }
            }

            boolean isManager = invoiceData.isCurrentApprover(Utils.getUserID());
            if (invoiceData.getCurrentApproverSelectItem() != null && SUBMITTED_TO_MANAGER.equals(status) && isManager) {
                approveCommandSubItems.add(new SplitButtonItem(APPROVE, property.getShort(wfmStrings.approve(), accountingStrings.invoice()), () -> {
                    setEnableButtons(false);
                    changeInvoiceStatus(APPROVE);
                }, true));

                approveCommandSubItems.add(new SplitButtonItem(MANAGER_REJECT, wfmStrings.reject(), () -> changeInvoiceStatus(MANAGER_REJECT)));
            }


            if (!Utils.hasRole(CLIENT) && PAID.equals(invoiceData.getStatusCode()) && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_SEND_EMAIL)) {
                if (AccountingSinksContainer.hasRolesForAccounting && invoiceData.getPaymentItems() != null && invoiceData.getPaymentItems().length > 0) {
                    approveCommandSubItems.add(new SplitButtonItem(SEND_RECEIPT_BUTTON, accountingStrings.sendReceiptToClient(), () -> sendToClient(CREDIT_NOTE_CATEGORY)));
                }
            }

            if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(PM) || Utils.hasRole(ACCOUNTANT)) {
                if (FAILED.equals(status)) {
                    approveCommandSubItems.add(new SplitButtonItem("RETRY", wfmStrings.retry(), () -> {
                        setEnableButtons(false);
                        sendToFifo(creditNoteID);
                    }, true));
                }
                approveButton.addItemList(approveCommandSubItems);
            }
            if (!optionsCommandSubItems.isEmpty()) {

                if (optionsCommandSubItems.size() > 1) {
                    optionsCommandSubItems.add(new SplitButtonItem("OPTIONS", wfmStrings.options(), null, true));
                }
                optionsSplitButton.addItemList(optionsCommandSubItems);
            }
        }
    }

    private void changeInvoiceStatus(String statusCode) {
        if (!validation()) {
            setEnableButtons(true);
            return;
        }
        LoadingPanel.loading(true);
        invoiceService.changeInvoiceStatus(creditNoteID, statusCode, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void result) {
                setEnableButtons(true);
                LoadingPanel.loading(false);
                if (MANAGER_REJECT == statusCode) {
                    Info.show(property.getSingular(wfmStrings.messSuccessfullyRejected(), accountingStrings.invoice()), Info.Type.INFO);
                } else {
                    Info.show(property.getSingular(wfmStrings.messSuccessfullyApproved(), accountingStrings.invoice()), Info.Type.INFO);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, CreditNoteSummaryView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALES_INVOICE_APPROVAL, result, CreditNoteSummaryView.this);
                closeTab();
            }
        });
    }

    public boolean validation() {
        if (Utils.isSalesLocked() && DateUtils.getTransactionLockDate().after(invoiceData.getInvoiceDate().getNonConvertedDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate("Invoice", Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    protected void initializeSpecificWidgets() {

        //NUMBER FIELD LABEL
        if (widgetsMap.get(INPUT_NUMBER) != null) {
            FormGroup numberField = (FormGroup) widgetsMap.get(INPUT_NUMBER);
            numberField.setLabel(wfmStrings.invoiceNumber());
        }

        if (invoiceData.getReference() != null) {
            widgetsMap.put(INPUT_REFERENCE, new FormGroup(wfmStrings.reference(), getWidgetAsFormControl(invoiceData.getReference())));
        }
        //INVOICE_DATE FIELD LABEL
        if (widgetsMap.get(INPUT_DATE) != null) {
            FormGroup dateField = (FormGroup) widgetsMap.get(INPUT_DATE);
            dateField.setLabel(wfmStrings.date());
        }

        //DUE_DATE FIELD LABEL
        if (widgetsMap.get(INPUT_DUE_DATE) != null) {
            FormGroup dueDateField = (FormGroup) widgetsMap.get(INPUT_DUE_DATE);
            dueDateField.setLabel(wfmStrings.dueDate());
        }

        String approverName = invoiceData.getCurrentApproverSelectItem() != null
                ? invoiceData.getCurrentApproverSelectItem().getName()
                : "";
        widgetsMap.put(INPUT_MANAGER, new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.manager(), getWidgetAsFormControl(approverName))));
    }

    @Override
    protected InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {
            @Override
            public List<Widget> getOptionWidgets() {
                List<Widget> result = new ArrayList<>();

                if (invoiceData.getBankAccount() != null && invoiceData.getBankAccount().getName() != null) {
                    result.add(new FormGroup(wfmStrings.bankDetails(), getWidgetAsFormControl(invoiceData.getBankAccount().getName())));
                }

                if (invoiceData.getRelatedProjectName() != null) {
                    result.add(new InvoiceDynamicProjectLookupBox(creditNoteID, invoiceData.getRelatedProject(), noteType, viewType, invoiceData.getClientID()));
                }

                GRow formRow = new GRow();
                GColumn pnumberCol = new GColumn(GColumnEnum.COL_6);
                GColumn snumberCol = new GColumn(GColumnEnum.COL_6);

                if (invoiceData.getPoNumber() != null) {
                    pnumberCol.add(new FormGroup(accountingStrings.po(), getWidgetAsFormControl(invoiceData.getPoNumber())));
                    formRow.add(pnumberCol);
                    result.add(formRow);
                }

                if (invoiceData.getQuoteNumberCN() != null) {
                    snumberCol.add(new FormGroup(accountingStrings.sq(), getWidgetAsFormControl(invoiceData.getQuoteNumberCN())));
                    formRow.add(snumberCol);
                    result.add(formRow);
                }
                result.add(new FormGroup(accountingStrings.relatedPriceLevel(), getWidgetAsFormControl(invoiceData.getPriceLevel() != null ? invoiceData.getPriceLevel().getName() : "N/A")));
                if (Utils.isSaudiCompany() && Utils.isVatRegistered()) {
                    result.add(new FormGroup(wfmStrings.reason(), createResonDropDown()));
                    result.add(new FormGroup(wfmStrings.reason(), createPaymentTypeCodeDropDown()));
                }
                return result;
            }
        });
    }

    @Override
    protected Integer getUploadFolderType() {
        return F_SALE_INV;
    }

    protected ViewAddFiledsCodeName getViewTypeForCustomFields() {
        return RECEIVABLE.equals(noteType) ? ViewAddFiledsCodeName.SaleInvoiceAdd : ViewAddFiledsCodeName.PurchaseInvoiceAdd;
    }

    protected void setEnableButtons(boolean b) {

        if (approveButton != null) {
            approveButton.setEnabled(b);
        }
    }

    private void pdfVersion(final HTMLPanel hp, Integer templateId) {
        generatePDF(hp, templateId);
    }

    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(creditNoteID, pdfTemplateID, null);
        String pdfURL = CommandConstants.PDF_URL +
                (RECEIVABLE.equals(noteType) ? "/savedReceivableCreditNoteViewPDFHandler" : "/savedPayableCreditNoteViewPDFHandler");
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    private AbstractAsyncCallback getAsyncCallback() {
        return new AbstractAsyncCallback() {
            public void failure(Throwable caught) {
                setEnableButtons(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Object result) {
                setEnableButtons(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyApproved(), accountingStrings.invoice()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, CreditNoteSummaryView.this);
                closeTab(/*"accounting|creditnote"*/);
            }
        };
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
}
