package com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.AccountingSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.target.TargetErpService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Printer;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.PostFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceDynamicBankAccountListBox;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceDynamicProjectLookupBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 24.02.2009
 * Time: 19:50:25
 * To change this template use File | Settings | File Templates.
 */
public class SaleInvoiceSummaryView extends InvoiceSummaryView {
    private final Integer saleInvoiceID;
    public static NumberFormat numberFormat = NumberFormat.getFormat("#.00");
    private String invoice = "";
    private SplitButtonItem pdfVersion, packingSlip, shippingLabel;
    private boolean isRecurring = false;
    private CrmAccountItem profileItem;
    private final List<SplitButtonItem> approveCommandSubItems = new ArrayList<>();


    public SaleInvoiceSummaryView(Integer saleInvoiceID, boolean isRecurringInvoice) {
        super("summary", isRecurringInvoice ? recurringInvoice : accountingStrings.viewProductInvoice(), isRecurringInvoice ? RECURRING_INVOICE : SALE_INVOICE, isRecurringInvoice || Utils.hasPermission(ACCOUNTING_SALES_INVOICE_HISTORY_NOTES), Utils.hasPermission(ACCOUNTING_SALES_INVOICE_UPLOAD_FILES));
        this.saleInvoiceID = saleInvoiceID;
        invoice = isRecurringInvoice ? "recurringInvoice_" : "saleInvoice_";
        isRecurring = isRecurringInvoice;
        property = new Property(getPropertyCode());
    }

    private void addFormListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALE_INVOICE_ADD_CREDIT_NOTE, SaleInvoiceSummaryView.this, (sender, args) -> reloadForm(true));

    }

    @Override
    protected LinkedHashMap<String, DynamicTableColumn> getColumnsMap(ColumnConfigs[] customColumns) {
        LinkedHashMap<String, DynamicTableColumn> columnsMap = new LinkedHashMap<>();

        if (customColumns != null && customColumns.length > 0) {
            DynamicTableColumn dynamicTableColumn;
            for (ColumnConfigs column : customColumns) {

                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);

                switch (column.getCode()) {
                    case ProductsTable.PRODUCT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.itemName(), ProductsTable.PRODUCT, Utils.getColumnWidth(column.getWidth(), 200));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.PRODUCT, dynamicTableColumn);
                        break;
                    case ProductsTable.DESCRIPTION:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.description(), ProductsTable.DESCRIPTION, Utils.getColumnWidth(column.getWidth(), 250));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.DESCRIPTION, dynamicTableColumn);
                        break;
                    case ProductsTable.QTY:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.qty(), ProductsTable.QTY, Utils.getColumnWidth(column.getWidth(), 75), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.QTY, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.QTY).setPixel(true);
                        break;
                    case ProductsTable.MEASUREMENT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.measurement(), ProductsTable.MEASUREMENT, Utils.getColumnWidth(column.getWidth(), 60));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.MEASUREMENT, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.MEASUREMENT).setPixel(true);
                        break;
                    case ProductsTable.UNITPRICE:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.price(), ProductsTable.UNITPRICE, Utils.getColumnWidth(column.getWidth(), 75), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.UNITPRICE, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.UNITPRICE).setPixel(true);
                        break;
                    case ProductsTable.DISCOUNT_AMT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.discount(), ProductsTable.DISCOUNT_AMT, Utils.getColumnWidth(column.getWidth(), 75), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.DISCOUNT_AMT, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.DISCOUNT_AMT).setPixel(true);
                        break;
                    case ProductsTable.DEPARTMENT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), ProductsTable.DEPARTMENT, Utils.getColumnWidth(column.getWidth(), 60));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.DEPARTMENT, dynamicTableColumn);
                        break;
                    case ProductsTable.ACCOUNT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.salesAccount(), ProductsTable.ACCOUNT, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.ACCOUNT, dynamicTableColumn);
                        break;
                    case ProductsTable.NET_AMT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.netAmount(), ProductsTable.NET_AMT, Utils.getColumnWidth(column.getWidth(), 80), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.NET_AMT, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.NET_AMT).setPixel(true);
                        break;
                    case ProductsTable.TAX_LIST:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.taxRate(), ProductsTable.TAX_LIST, Utils.getColumnWidth(column.getWidth(), 105));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.TAX_LIST, dynamicTableColumn);
                        break;
                    case ProductsTable.DOUBLE_TAX_LIST:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.taxRate(), ProductsTable.DOUBLE_TAX_LIST, Utils.getColumnWidth(column.getWidth(), 105));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.DOUBLE_TAX_LIST, dynamicTableColumn);
                        break;
                    case ProductsTable.WAREHOUSE:
                        if (!invoiceData.isProjectBasedInvoice()) {
                            dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : accountingStrings.warehouse(), ProductsTable.WAREHOUSE, Utils.getColumnWidth(column.getWidth(), 100));
                            dynamicTableColumn.setPixel(isPixel);
                            dynamicTableColumn.setForceWidthInPercent(!isPixel);
                            columnsMap.put(ProductsTable.WAREHOUSE, dynamicTableColumn);
                        }
                        break;
                    case ProductsTable.TOTAL_AMT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.totalAmount(), ProductsTable.TOTAL_AMT, Utils.getColumnWidth(column.getWidth(), 100), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.TOTAL_AMT, dynamicTableColumn);
                        break;
                    case ProductsTable.PROJECT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : Property.get(Constants.PROJECT, wfmStrings.project()), ProductsTable.PROJECT, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.PROJECT, dynamicTableColumn);
                        break;
                    case ProductsTable.FAI_CATEGORY:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.category(), ProductsTable.FAI_CATEGORY, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.FAI_CATEGORY, dynamicTableColumn);
                        break;
                    default:
                        dynamicTableColumn = new DynamicTableColumn(column.getTitle(), column.getCode(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired(), DATA_TYPE_NUMBER.equals(column.getDataType()) ? RIGHT_ALIGN_CELL : null);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(column.getCode(), dynamicTableColumn);
                        //columnsMap.get(column.getCode()).setPixel(true);
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
            columnsMap.put(ProductsTable.FAI_CATEGORY, new DynamicTableColumn(wfmStrings.category(), ProductsTable.FAI_CATEGORY, 100));

            if (Utils.isMultiWarehouseEnabled() && !invoiceData.isProjectBasedInvoice()) {
                columnsMap.put(ProductsTable.WAREHOUSE, new DynamicTableColumn(accountingStrings.warehouse(), ProductsTable.WAREHOUSE, 100));
            }
        }
        return columnsMap;
    }

    protected void initializeInvoiceData() {
        invoiceService.getInvoiceSummaryData(saleInvoiceID, new AbstractAsyncCallback<NewInvoice>() {
            public void success(NewInvoice result) {

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

    protected void initializeButtons() {
        //this one contains approve, approve&send to client, resend to client command list

        //this one contains edit, copy to new, add credit note, void options
        List<SplitButtonItem> optionsCommandSubItems = new ArrayList<>();
        //this one contains a list of pdf templates
        List<SplitButtonItem> pdfCommandSubItems = new ArrayList<>();

        String status = invoiceData.getStatusCode();
        if (!PENDING.equals(status)) {
            final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(invoiceData.getProjectStatusCode()));
            boolean hasAccountingBeforeBlockDate = (Utils.isSalesLocked() && DateUtils.getTransactionLockDate().after(invoiceData.getInvoiceDate().getNonConvertedDate()));
            boolean canEdit = !(PAID.equals(invoiceData.getStatusCode()) || OVER_DUE.equals(invoiceData.getStatusCode()) || APPROVE.equals(invoiceData.getStatusCode())) || invoiceData.getCurrentApproverSelectItem() == null ||
                    (invoiceData.getCurrentApproverSelectItem() != null && invoiceData.getCurrentApproverSelectItem().getId().equals(Utils.getUserID()));

            if (APPROVE.equals(invoiceData.getStatusCode()) && invoiceData.getCurrentApproverSelectItem() != null) {
                canEdit = invoiceData.getCurrentApproverSelectItem().getId().equals(Utils.getUserID());
            }

            if (!status.equals(REVERSED)) {
                if (hasAccessToChange && !hasAccountingBeforeBlockDate &&
                        ((invoiceData.isSubmitter(Utils.getUserID()) && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_EDIT)) || Utils.hasPermission(ACCOUNTING_SALES_INVOICE_FULL_EDIT_ACCESS)) &&
                        (!PAID.equals(invoiceData.getStatusCode()) || Utils.hasPermission(ACCOUNTING_SALES_INVOICE_PAID_STATUS_EDIT)) && canEdit && !invoiceData.isZatcaReported()) {

                    SplitButtonItem editOption = new SplitButtonItem("EDIT_OPTION", wfmStrings.edit(), () -> {
                        closeTab();
                        goTo((invoiceData.isRecurringInvoice() ? "recurringinvoice" : "saleinvoice") + "|edit/" + saleInvoiceID + (invoiceData.isProjectBasedInvoice() ? "/projectbased" : ""), invoiceData.getInvoiceNumber());
                    });
                    optionsCommandSubItems.add(editOption);
                }

                if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_COPY) && hasAccessToChange && !invoiceData.isRecurringInvoice()) {
                    SplitButtonItem copyToNewOption = new SplitButtonItem("COPY_TO_NEW_OPTION", property.getShort(accountingStrings.copyNewInvoice(), accountingStrings.invoice()), () -> {
                        goTo("saleinvoice|add/add/copyFromExistingData/" + saleInvoiceID);
                    });
                    optionsCommandSubItems.add(copyToNewOption);
                }

                if (!PAID.equals(status) && Utils.hasPermission(ACCOUNTING_SALES_CREDIT_NOTE_ADD)) {
                    SplitButtonItem addCreditNoteOption = new SplitButtonItem("ADD_CREDIT_NOTE", (wfmStrings.convert() + " " + accountingStrings.creditNote()), () -> {
                        goTo("receivablecreditnote|add/add/fromInvoice/" + saleInvoiceID);
                    });
                    optionsCommandSubItems.add(addCreditNoteOption);
                }

                if (Utils.hasGenericAccess(GenericSettingsEnum.ADD_CUSTOMER_PRE_PAYMENT_FROM_SALES_INVOCIE) && !status.equals(SUBMITTED_TO_MANAGER) && !status.equals(DRAFT) && !status.equals(REJECTED)) {
                    SplitButtonItem costumerPrePayment = new SplitButtonItem("CUSTOMER_PRE_PAYMENT", Property.get(Constants.CUSTOMER_PREPAYMENT, accountingStrings.addcustomerPrepayment()),
                            () -> redirectProperly("prepayment|add/prePaymentFromSI/" + invoiceData.getID(), ""));
                    optionsCommandSubItems.add(costumerPrePayment);
                }

                if (Utils.hasGenericAccess(GenericSettingsEnum.INTEGRATED_WITH_TARGET)) {
                    SplitButtonItem sendToTargetOption = new SplitButtonItem("SEND_TO_TARGET", accountingStrings.sendToTarget(), () -> {
                        TargetErpService.App.get().sendInvoiceToTarget(invoiceData.getID(), new AsyncCallback<String>() {
                            @Override
                            public void onFailure(Throwable throwable) {
//                            sendToTarget.setEnabled(true);
                            }

                            @Override
                            public void onSuccess(String s) {
                                if (s != null && !s.startsWith("OK")) {
                                    Info.show(s, Info.Type.WARNING);
                                } else {
                                    Info.show(s.replace("OK:", ""), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, invoiceData.getID(), SaleInvoiceSummaryView.this);
                                }
                            }
                        });
                    });
                    optionsCommandSubItems.add(sendToTargetOption);
                }

                if (!optionsCommandSubItems.isEmpty()) {

                    if (optionsCommandSubItems.size() > 1) {
                        optionsCommandSubItems.add(new SplitButtonItem("OPTIONS", wfmStrings.options(), null, true));
                    }
                    optionsSplitButton.addItemList(optionsCommandSubItems);
                }

                /**
                 * Send email to client or if it was already sendt then resend
                 */

                boolean isManager = invoiceData.isCurrentApprover(Utils.getUserID());
                if (invoiceData.getCurrentApproverSelectItem() != null && SUBMITTED_TO_MANAGER.equals(status) && isManager) {
                    approveCommandSubItems.add(new SplitButtonItem(APPROVE, property.getShort(wfmStrings.approve(), accountingStrings.invoice()), () -> {
                        setEnableButtons(false);
                        changeInvoiceStatus(APPROVE);
                    }, true));

                    approveCommandSubItems.add(new SplitButtonItem(MANAGER_REJECT, wfmStrings.reject(), () -> changeInvoiceStatus(MANAGER_REJECT)));
                }
                if (FAILED.equals(status) && (Utils.hasPermission(PermissionConstants.ACCOUNTING_SI_SAVE_APPROVE) || AccountingSinksContainer.isHashAccessForPMRole)) {
                    approveCommandSubItems.add(new SplitButtonItem("RETRY", wfmStrings.retry(), () -> {
                        setEnableButtons(false);
                        sendToFifo(saleInvoiceID);
                    }, true));
                }
                if (!Utils.hasRole(CLIENT) && !DRAFT.equals(status)) {
                    if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_SEND_EMAIL)
                            && !SUBMITTED_TO_MANAGER.equals(status) && !MANAGER_REJECT.equals(status)) {
                        approveCommandSubItems.add(new SplitButtonItem(EMAIL, OPEN.equals(status) ? accountingStrings.resendInvoiceToClient() : wfmStrings.sendEmail(),
                                () -> {
                                    sendToClient(invoiceData.isRecurringInvoice() ? RECURRING_INVOICE_CATEGORY : invoiceData.isProjectBasedInvoice() ? PROJECT_BASE_INVOICE_CATEGORY : SALES_INVOICE_CATEGORY);
                                }, true));
                    }

                    if (PAID.equals(invoiceData.getStatusCode()) && Utils.hasPermission(ACCOUNTING_SALES_INVOICE_SEND_EMAIL)) {
                        approveCommandSubItems.add(new SplitButtonItem(SEND_RECEIPT_BUTTON, accountingStrings.sendReceiptToClient(), () -> sendToClient(RECEIPT_CATEGORY, true)));
                    }
                }


                invoiceService.getSaleInvoiceCustomer(saleInvoiceID, new AsyncCallback<CrmAccountItem>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                    }

                    @Override
                    public void onSuccess(CrmAccountItem result) {
                        profileItem = result;
                    }
                });

                if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_SMS_BUTTON)) {
                    approveCommandSubItems.add(new SplitButtonItem(SEND_RECEIPT_BUTTON, wfmStrings.sms(), () -> new ActivityQuickAddForm(Appointment.SMS, profileItem, saleInvoiceID, RelationItem.newEventRelation(RelationItem.TYPE_SALESINVOICE, saleInvoiceID, invoiceData.getInvoiceNumber()))));
                }


                if (invoiceData != null && invoiceData.getAmazonLink() != null) {
                    approveCommandSubItems.add(new SplitButtonItem(SEND_BY_WHATSAPP, wfmStrings.sendByWhatsApp(), () -> {
                        profileItem.setAmazonLink(invoiceData.getAmazonLink());
                        profileItem.setSaleType("invoice");
                        profileItem.setObjectId(invoiceData.getID());
                        AccountingService.App.get().shortenLink(invoiceData.getAmazonLink(), profileItem, new AsyncCallback<String>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                throw new RuntimeException("Failed to shorten link or save link: {} ", caught);
                            }

                            @Override
                            public void onSuccess(String link) {
                                profileItem.setShortLink(link);
                                new ActivityQuickAddForm(Appointment.WHATSAPP, profileItem, saleInvoiceID, RelationItem.newEventRelation(RelationItem.TYPE_SALESINVOICE, saleInvoiceID, invoiceData.getInvoiceNumber()));
                            }
                        });
                    }));
                }


                //It needs to be temporarily hidden because information is being sent to the Zatca via the taxilla.
            /*if (canSendToZatca()) {
                approveCommandSubItems.add(sendZatcaButton(SaleInvoiceSummaryView.this));
            }*/
                if (!approveCommandSubItems.isEmpty()) {
                    approveButton.addItemList(approveCommandSubItems);
                }
            }

            /**
             * Print functionality sub items
             */
            {
                if (Utils.hasPermission(ACCOUNTING_SALES_INVOICE_PDF) || Utils.hasPermission(CRM_SALES_INVOICE_PDF)) {
                    Integer defaultTemplateId = null;
                    if (invoiceData != null
                            && invoiceData.getPdfTemplateList() != null
                            && invoiceData.getPdfTemplateList().getItems() != null) {
                        invoiceData.getPdfTemplateList().getItems();
                        for (SelectItem pdfItem : invoiceData.getPdfTemplateList().getItems()) {

                            if (pdfItem.isDefaultSelected()) {
                                defaultTemplateId = pdfItem.getId();
                            }
                            pdfCommandSubItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> pdfVersion(htmlPanel, pdfItem.getId(), false)));
                        }
                    }
                    Integer finalDefaultTemplateId = defaultTemplateId;
                    pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> pdfVersion(htmlPanel, finalDefaultTemplateId, false), true);
                    pdfVersion.ensureDebugId(invoice + "pdfVersionItem");
                    pdfCommandSubItems.add(pdfVersion);

                    if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PACK_SHIP_PDF_FOR_SALE_INVOICE) && invoiceData != null) {/* && !SERVICE_INVOICE_TYPE.equals(invoiceData.getInvoiceType())*/
                        packingSlip = (new SplitButtonItem(PACKING_SLIP, accountingStrings.packingSlip(), () -> pdfVersion(htmlPanel, null, true), false));
                        packingSlip.ensureDebugId(invoice + "packingSlipItem");
                        pdfCommandSubItems.add(packingSlip);

                        shippingLabel = new SplitButtonItem("SHIPPING_LABEL", accountingStrings.shippingLabel(), () -> {
                            new ShippingLabelDialogBox(invoiceData.getID()).open();
                        });
                        shippingLabel.ensureDebugId(invoice + "shippingLabel");
                        pdfCommandSubItems.add(shippingLabel);
                    }
                    boolean hasTemplateList = invoiceData.getHtmlTemplateList() != null &&
                            invoiceData.getHtmlTemplateList().getItems() != null &&
                            invoiceData.getHtmlTemplateList().getItems().length > 0;

                    if (Utils.hasGenericAccess(GenericSettingsEnum.PAID_AND_PRINT_INVOICE)) {
                        if (hasTemplateList &&
                                (APPROVE.equals(invoiceData.getStatusCode()) ||
                                        OPEN.equals(invoiceData.getStatusCode()) ||
                                        OVER_DUE.equals(invoiceData.getStatusCode()))) {

                            pdfCommandSubItems.add(this.initPrintButton());
                        }
                    } else if (hasTemplateList) {
                        pdfCommandSubItems.add(this.initPrintButton());
                    }

                    if (invoiceData != null && invoiceData.isCustomExcelEnabled()) {
                        pdfCommandSubItems.add(new SplitButtonItem("EXCEL_VERSION", accountingStrings.packingSlip() + "(Excel)", () -> excelVersion(htmlPanel, true), false));
                    }
                }

                if (!Utils.hasRole(CLIENT) && (isApprovedInvoiceOrCreditNote || PAID.equals(status))) {

                    if (AccountingSinksContainer.hasRolesForAccounting && invoiceData.getPaymentItems() != null && invoiceData.getPaymentItems().length > 0) {
                        pdfCommandSubItems.add(new SplitButtonItem("GENERATE_RECEIPT", accountingStrings.generateReceipt(),
                                () -> {
                                    RequestObject requestObject = new RequestObject(saleInvoiceID);
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
                            Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.SALES_INVOICE.name());
                        }
                    }));
                }
                if (!pdfCommandSubItems.isEmpty()) {
                    printPdfSplitButton.addItemList(pdfCommandSubItems);
                }
            }
        }
    }


    private void redirectProperly(String url, String tabName) {
        if (Utils.isAccounting()) {
            goTo(url, tabName);
        } else {
            Utils.openURL(GWT.getHostPageBaseURL() + "Accounting.html#" + url);
        }
    }

    private void changeInvoiceStatus(String statusCode) {
        if (!validation()) {
            setEnableButtons(true);
            return;
        }
        LoadingPanel.loading(true);
        invoiceService.changeInvoiceStatus(saleInvoiceID, statusCode, new AbstractAsyncCallback<Void>() {
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
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, SaleInvoiceSummaryView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALES_INVOICE_APPROVAL, result, SaleInvoiceSummaryView.this);
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

    private SplitButtonItem initPrintButton() {
        final SplitButtonItem printVersionButton = new SplitButtonItem(PRINT_VERSION, wfmStrings.print(), () -> {
            new PDFTemplateSelector(invoiceData.getHtmlTemplateList(), new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    printVersion(id);
                }
            });
        }, false);


        printVersionButton.ensureDebugId(invoice + "printVersionItem");
        return printVersionButton;
    }


    public void excelVersion(Panel hp, boolean isPacking) {
        String action = CommandConstants.COMMON_URL + "/downloadInvoiceDataXML?objectID=" + invoiceData.getID() + "&isPacking=" + isPacking;
        PostFormPanel post = new PostFormPanel(action, "_blank");
        hp.add(post);
        post.submit();
    }

    @Override
    protected void initializeSpecificWidgets() {
        //NUMBER FIELD LABEL
        if (widgetsMap.get(INPUT_NUMBER) != null) {
            FormGroup numberField = (FormGroup) widgetsMap.get(INPUT_NUMBER);
            numberField.setLabel(property.getShortForNumber(wfmStrings.invoiceNumber()));
        }

        //INVOICE_DATE FIELD LABEL
        if (widgetsMap.get(INPUT_DATE) != null) {
            FormGroup dateField = (FormGroup) widgetsMap.get(INPUT_DATE);
            dateField.setLabel(wfmStrings.invoiceDate());
        }

        //DUE_DATE FIELD LABEL
        if (widgetsMap.get(INPUT_DUE_DATE) != null) {
            FormGroup dueDateField = (FormGroup) widgetsMap.get(INPUT_DUE_DATE);
            dueDateField.setLabel(invoiceData.getInvoiceTermsItem() != null ? wfmStrings.terms() : wfmStrings.dueDate());
        }

        if (invoiceData.getBankAccount() != null && invoiceData.getBankAccount().getName() != null) {
            widgetsMap.put(INPUT_BANK, new FormGroup(wfmStrings.bankDetails(), getWidgetAsFormControl(invoiceData.getBankAccount().getName())));
        } else {
            widgetsMap.put(INPUT_BANK, new FormGroup(wfmStrings.bankDetails(), getWidgetAsFormControl("")));
        }
        String approverName = invoiceData.getCurrentApproverSelectItem() != null
                ? invoiceData.getCurrentApproverSelectItem().getName()
                : "";
        widgetsMap.put(INPUT_MANAGER, new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.manager(), getWidgetAsFormControl(approverName))));

        addFormListeners();
    }

    @Override
    protected InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {
            @Override
            public List<Widget> getOptionWidgets() {
                List<Widget> result = new ArrayList<>();

                //Bank Account
                result.add(new InvoiceDynamicBankAccountListBox(saleInvoiceID, invoiceData.getBankAccount(), viewType));

                //PROJECT FIELD
                if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                    result.add(new InvoiceDynamicProjectLookupBox(saleInvoiceID, invoiceData.getRelatedProject(), RECEIVABLE, viewType, invoiceData.getClientID()));
                }
                //related sale quote number
                if (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_SUMMARY) && !invoiceData.isSalesOrder() && invoiceData.getQuoteNumber() != null && invoiceData.getQuoteId() != null) {
                    HTML label = new HTML("<a href=\"javascript:\">" + invoiceData.getQuoteNumber() + "</a>");
                    label.addClickHandler(clickEvent -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged(SALE_QUOTE + "|summary/" + invoiceData.getQuoteId(), invoiceData.getQuoteNumber());
                    });
                    result.add(new FormGroup(invoiceData.isSalesOrder() ? Property.getShortName(SALE_ORDER_CODE, wfmStrings.saleorder()) + " #" : Property.getShortName(SALE_QUOTE, wfmStrings.salesQuote()) + "#", wrapWidgetToFormControl(label)));
                } else if (Utils.hasPermission(ACCOUNTING_SALES_ORDER_SUMMARY) && invoiceData.isSalesOrder() && invoiceData.getQuoteNumber() != null && invoiceData.getQuoteId() != null) {
                    HTML label = new HTML("<a href=\"javascript:\">" + invoiceData.getQuoteNumber() + "</a>");
                    label.addClickHandler(clickEvent -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged(SALE_ORDER_CODE + "|summary/" + invoiceData.getQuoteId(), invoiceData.getQuoteNumber());
                    });
                    result.add(new FormGroup(invoiceData.isSalesOrder() ? Property.getShortName(SALE_ORDER_CODE, wfmStrings.saleorder()) + " #" : Property.getShortName(SALE_QUOTE, wfmStrings.salesQuote()) + "#", wrapWidgetToFormControl(label)));
                } else {
                    result.add(new FormGroup(invoiceData.isSalesOrder() ? Property.getShortName(SALE_ORDER_CODE, wfmStrings.saleorder()) + " #" : Property.getShortName(SALE_QUOTE, wfmStrings.salesQuote()) + "#", getWidgetAsFormControl(invoiceData.getQuoteNumber())));
                }

                //related purchase order number
//                result.add(new FormGroup(Property.getShortName(PURCHASE_ORDER, accountingStrings.poNumber()) + "#", getWidgetAsFormControl(invoiceData.getPoNumber())));
                result.add(new FormGroup(accountingStrings.relatedPriceLevel(), getWidgetAsFormControl(invoiceData.getPriceLevel() != null ? invoiceData.getPriceLevel().getName() : "N/A")));

                return result;
            }
        });
    }

    @Override
    protected void loadTotals() {
        super.loadTotals();
        if (hasBillExp && (customItemColumns == null || customItemColumns != null && customItemColumns.length == 0 || customItemColumns != null && customItemColumns.length > 0 && columnsMap.get(ProductsTable.UNITPRICE) != null)) {
            setTotalData(accountingStrings.billableExpenseAmount(), invoiceData.getBillableExpenseAmount());
            setTotalData("Billable Exp. Tax Total", invoiceData.getBillableExpenseTaxAmount());
        }
    }

    @Override
    protected Integer getUploadFolderType() {
        return F_SALE_INV;
    }

    @Override
    protected void setEnableButtons(boolean b) {
        approveButton.setEnabled(b);

        if (approveButton.getItemsMap() != null && approveButton.getItemsMap().size() > 0) {
            approveButton.showOrHideMenuItem(APPROVE, b);
            //approve.setEnabled(b);
        }
        if (approveButton.getItemsMap() != null && approveButton.getItemsMap().size() > 0) {
            approveButton.showOrHideMenuItem(APPROVE_AND_SEND, b);
        }
    }

    private void pdfVersion(final HTMLPanel hp, Integer templateId, final boolean isPacking) {
        if (isPacking) {
            new PDFTemplateSelector(Constants.PACKING_SLIP, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    generatePDF(hp, id, isPacking);
                }
            });
        } else {
            generatePDF(hp, templateId, false);
        }
    }

    private void printVersion(Integer pdfTemplateId) {
        LoadingPanel.loading(true);
        InvoiceService.App.get().generateInvoicePdfTemplateHtml(invoiceData.getID(), pdfTemplateId, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(String html) {
                LoadingPanel.loading(false);
                if (html != null) {
                    Printer.openPrintWindow(html);
                } else {
                    if (Utils.hasGenericAccess(GenericSettingsEnum.PAID_AND_PRINT_INVOICE)) {
                        Info.show(accountingStrings.pleaseSelectPdfTemplateOrCheckDefaultPaymentAccount(), Info.Type.WARNING);
                    } else {
                        Info.show(wfmStrings.pleaseSelectPdfTemplate(), Info.Type.WARNING);
                    }
                }
            }
        });
    }

    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID, boolean isPacking) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(saleInvoiceID, pdfTemplateID, null);
        String pdfURL = CommandConstants.PDF_URL + (invoiceData.isProjectBasedInvoice() ? "/savedProjectBaseInvoiceViewPDFHandler" : "/savedSaleInvoceViewPDFHandler");
        if (isPacking) {
            pdfURL = CommandConstants.PDF_URL + ("/savedPackingSlipPDFHandler");
        }
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    public String getIconStyle() {
        return null;
    }

    protected ViewAddFiledsCodeName getViewTypeForCustomFields() {
        return ViewAddFiledsCodeName.SaleInvoiceAdd;
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
        if (isRecurring) {
            return Constants.RECURRING_INVOICE;
        }
        return Constants.SALE_INVOICE;
    }
}
