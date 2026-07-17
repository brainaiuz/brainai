package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceDynamicProjectLookupBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 06.03.2009
 * Time: 16:16:20
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseInvoiceSummaryView extends InvoiceSummaryView implements PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final Integer purchaseInvoiceID;
    private final boolean isRecurringBill;
    private WfmButton2 addCreditNote, copyToNewInvoice, edit;
    private String recurring = "";

    public PurchaseInvoiceSummaryView(Integer purchaseInvoiceID, boolean isRecurringBill) {
        super("summary", (isRecurringBill ? recurringBill : purchaseInvoice), (isRecurringBill ? RECURRING_BILL : PURCHASE_INVOICE), isRecurringBill || Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_HISTORY_NOTES), true);
        this.purchaseInvoiceID = purchaseInvoiceID;
        this.isRecurringBill = isRecurringBill;
        property = new Property(getPropertyCode());
        recurring = isRecurringBill ? "reccuringBill_" : "purchaseInvoice_";
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASE_INVOICE_ADD_CREDIT_NOTE, PurchaseInvoiceSummaryView.this, (sender, args) -> {
            clear();
            rightWidgets.clear();
            onInitialize();
            show("summary");
        });
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
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : accountingStrings.productOrService(), ProductsTable.PRODUCT, Utils.getColumnWidth(column.getWidth(), 200));
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
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.accountType(), ProductsTable.ACCOUNT, Utils.getColumnWidth(column.getWidth(), 100));
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
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : accountingStrings.warehouse(), ProductsTable.WAREHOUSE, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.WAREHOUSE, dynamicTableColumn);
                        break;
                    case ProductsTable.TOTAL_AMT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.totalAmount(), ProductsTable.TOTAL_AMT, Utils.getColumnWidth(column.getWidth(), 100), Constants.RIGHT_ALIGN_CELL);
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.TOTAL_AMT, dynamicTableColumn);
                        //columnsMap.get(ProductsTable.TOTAL_AMT).setPixel(true);
                        break;
                    case ProductsTable.PROJECT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : Property.get(Constants.PROJECT, wfmStrings.project()), ProductsTable.PROJECT, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.PROJECT, dynamicTableColumn);
                        break;
                    case ProductsTable.CLIENT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : accountingStrings.billing(), ProductsTable.CLIENT, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.CLIENT, dynamicTableColumn);
                    case ProductsTable.FAI_CATEGORY:
                        dynamicTableColumn = new DynamicTableColumn(wfmStrings.category(), ProductsTable.FAI_CATEGORY, Utils.getColumnWidth(column.getWidth(), 100));
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(ProductsTable.FAI_CATEGORY, dynamicTableColumn);
                        break;
                    default:
                        dynamicTableColumn = new DynamicTableColumn(column.getTitle(), column.getCode(), Utils.getColumnWidth(column.getWidth(), 100), column.isRequired());
                        dynamicTableColumn.setPixel(isPixel);
                        dynamicTableColumn.setForceWidthInPercent(!isPixel);
                        columnsMap.put(column.getCode(), dynamicTableColumn);
                        //columnsMap.get(column.getCode()).setPixel(true);
                        break;
                }
            }
        } else {
            columnsMap.put(ProductsTable.PRODUCT, new DynamicTableColumn(accountingStrings.productOrService(), ProductsTable.PRODUCT, 120));
            columnsMap.put(ProductsTable.DESCRIPTION, new DynamicTableColumn(wfmStrings.description(), ProductsTable.DESCRIPTION, 200));

            columnsMap.put(ProductsTable.QTY, new DynamicTableColumn(wfmStrings.qty(), ProductsTable.QTY, 75, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.QTY).setPixel(true);

            columnsMap.put(ProductsTable.UNITPRICE, new DynamicTableColumn(wfmStrings.price(), ProductsTable.UNITPRICE, 75, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.UNITPRICE).setPixel(true);

            columnsMap.put(ProductsTable.DISCOUNT_AMT, new DynamicTableColumn(wfmStrings.discount(), ProductsTable.DISCOUNT_AMT, 75, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.DISCOUNT_AMT).setPixel(true);

            columnsMap.put(ProductsTable.ACCOUNT, new DynamicTableColumn(wfmStrings.accountType(), ProductsTable.ACCOUNT, 100));

            columnsMap.put(ProductsTable.NET_AMT, new DynamicTableColumn(wfmStrings.netAmount(), ProductsTable.NET_AMT, 80, Constants.RIGHT_ALIGN_CELL));
            columnsMap.get(ProductsTable.NET_AMT).setPixel(true);

            columnsMap.put(ProductsTable.TAX_LIST, new DynamicTableColumn(wfmStrings.taxRate(), ProductsTable.TAX_LIST, 100));
            columnsMap.put(ProductsTable.FAI_CATEGORY, new DynamicTableColumn(wfmStrings.category(), ProductsTable.FAI_CATEGORY, 100));

            if (Utils.isMultiWarehouseEnabled()) {
                columnsMap.put(ProductsTable.WAREHOUSE, new DynamicTableColumn(accountingStrings.warehouse(), ProductsTable.WAREHOUSE, 100));
            }
        }
        return columnsMap;
    }

    protected void initializeInvoiceData() {
        invoiceService.getInvoiceSummaryData(purchaseInvoiceID, new AbstractAsyncCallback<NewInvoice>() {
            public void success(NewInvoice result) {
//                initFileUploadPanel();
                if (result.getPaymentInstruction() != null && !result.getPaymentInstruction().isEmpty()) {
                    drawInstructionPanel(result.getPaymentInstruction());
                }
                initializeFormData(invoiceData = result);
            }
        });
    }

    /*@Override
    public void initFileUploadPanel() {
        uploadPanel = new FooterUploadPanel(F_PUR_INV, purchaseInvoiceID, false);
    }*/

    protected void initializeButtons() {
        //this one contains approve, approve&send to client, resend to client command list
        List<SplitButtonItem> approveCommandSubItems = new ArrayList<>();
        //this one contains edit, copy to new, add credit note, void options
        List<SplitButtonItem> optionsCommandSubItems = new ArrayList<>();
        //this one contains a list of pdf templates
        List<SplitButtonItem> pdfCommandSubItems = new ArrayList<>();

        String status = invoiceData.getStatusCode();
        if (!PENDING.equals(status)) {
            final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(invoiceData.getProjectStatusCode()));
            boolean hasAccountingBeforeBlockDate = (Utils.isPurchasesLocked() && DateUtils.getTransactionLockDate().after(invoiceData.getInvoiceDate().getNonConvertedDate()));

            boolean editPermissionPaidInvoices = !PAID.equals(invoiceData.getStatusCode()) || PAID.equals(invoiceData.getStatusCode()) && Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_PAID_STATUS_EDIT);

            boolean editPermission = Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_INVOICE_EDIT : ACCOUNTING_PURCHASE_INVOICE_EDIT);

            boolean editFullPermission = Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_INVOICE_FULL_EDIT_ACCESS : ACCOUNTING_PURCHASE_INVOICE_FULL_EDIT_ACCESS);

            boolean isAccessToEdit = (invoiceData.isSubmitter(Utils.getUserID()) && editPermission) || editFullPermission;
            if (invoiceData.isApprover() && APPROVE.equals(invoiceData.getStatusCode())) {
                isAccessToEdit = editPermission && invoiceData.getCurrentApproverSelectItem() != null && Utils.getUserID().equals(invoiceData.getCurrentApproverSelectItem().getId());
            }
            if (!status.equals(REVERSED)) {
                if (hasAccessToChange && isAccessToEdit && editPermissionPaidInvoices && (invoiceData.getConvertedShippingDataList() == null || invoiceData.getConvertedShippingDataList().isEmpty())) {
                    optionsCommandSubItems.add(new SplitButtonItem("EDIT", wfmStrings.edit(), () -> {
                        setEnableButtons(false);
                        closeTab();

                        if (isRecurringBill) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("recurringbill|edit/" + purchaseInvoiceID, invoiceData.getInvoiceNumber());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|edit/" + purchaseInvoiceID, invoiceData.getInvoiceNumber());
                        }
                    }));
                }

                if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_INVOICE_COPY : ACCOUNTING_PURCHASE_INVOICE_COPY) && hasAccessToChange) {
                    optionsCommandSubItems.add(new SplitButtonItem(COPY_TO_NEW_BUTTON, property.getShort(accountingStrings.copyNewInvoice(), accountingStrings.invoice()), () -> {
                        if (isRecurringBill) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("recurringbill|add/add/copyFromExistingData/" + purchaseInvoiceID);
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|add/add/copyFromExistingData/" + purchaseInvoiceID);
                        }
                    }));
                }

                if (!isRecurringBill && Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_CREDIT_NOTE_ADD : ACCOUNTING_PURCHASE_CREDIT_NOTE_ADD) && hasAccessToChange) {

                    if (APPROVE.equals(status) || OPEN.equals(status) || OVER_DUE.equals(status)) {
                        optionsCommandSubItems.add(new SplitButtonItem("ADD_CREDIT_NOTE", accountingStrings.addDebitNote(), () -> SinksContainerFactory.entryPoint.onHistoryChanged("payablecreditnote|add/add/fromInvoice/" + purchaseInvoiceID)));
                    }
                }

                if (optionsCommandSubItems.size() > 1) {
                    optionsCommandSubItems.add(new SplitButtonItem("OPTIONS", wfmStrings.options(), null, true));
                }
                optionsSplitButton.addItemList(optionsCommandSubItems);

            }
            if (!isRecurringBill && invoiceData.getCurrentApproverSelectItem() != null && SUBMITTED_TO_MANAGER.equals(status) && invoiceData.getCurrentApproverSelectItem().getId().equals(Utils.getUserID())) {
                approveCommandSubItems.add(new SplitButtonItem(APPROVE, property.getShort(wfmStrings.approve(), accountingStrings.invoice()), () -> {
                    setEnableButtons(false);
                    changeInvoiceStatus(APPROVE);
                }, true));

                approveCommandSubItems.add(new SplitButtonItem(MANAGER_REJECT, wfmStrings.reject(), () -> changeInvoiceStatus(MANAGER_REJECT)));
            }
            if (!isRecurringBill && FAILED.equals(status)) {
                approveCommandSubItems.add(new SplitButtonItem("RETRY", wfmStrings.retry(), () -> {
                    setEnableButtons(false);
                    sendToFifo(purchaseInvoiceID);
                }, true));
            }
            approveButton.addItemList(approveCommandSubItems);

            Integer defaultTemplateId = null;
            if (invoiceData.getPdfTemplateList() != null && invoiceData.getPdfTemplateList().getItems() != null) {
                for (SelectItem pdfItem : invoiceData.getPdfTemplateList().getItems()) {
                    if (pdfItem.isDefaultSelected()) {
                        defaultTemplateId = pdfItem.getId();
                    }
                    pdfCommandSubItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> pdfVersion(htmlPanel, pdfItem.getId())));
                }
            }
            Integer finalDefaultTemplateId = defaultTemplateId;
            SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> pdfVersion(htmlPanel, finalDefaultTemplateId), true);
            pdfVersion.ensureDebugId(recurring + "pdfVersionItem");
            pdfCommandSubItems.add(pdfVersion);

            if (Utils.hasRoles(Constants.ADMIN)) {
                pdfCommandSubItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), new Command() {
                    @Override
                    public void execute() {
                        Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.PURCHASE_INVOICE.name());
                    }
                }));
            }
            printPdfSplitButton.addItemList(pdfCommandSubItems);
        }
    }

    private void changeInvoiceStatus(String statusCode) {
        if (!validation()) {
            setEnableButtons(true);
            return;
        }
        LoadingPanel.loading(true);
        invoiceService.changePurchaseInvoiceStatus(purchaseInvoiceID, statusCode, new AbstractAsyncCallback<Void>() {
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
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, result, PurchaseInvoiceSummaryView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASE_INVOICE_APPROVAL, result, PurchaseInvoiceSummaryView.this);
                closeTab();
            }
        });
    }

    public boolean validation() {
        if (Utils.isPurchasesLocked() && DateUtils.getTransactionLockDate().after(invoiceData.getInvoiceDate().getNonConvertedDate())) {
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
            numberField.setLabel(property.getShortForNumber(wfmStrings.invoiceNumber()));
        }
        if (invoiceData.getClientItem() != null) {
            widgetsMap.put(INPUT_CUSTOMER, new FormGroup(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), getWidgetAsFormControl(invoiceData.getClientItem().getName())));
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

                if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                    result.add(new InvoiceDynamicProjectLookupBox(purchaseInvoiceID, invoiceData.getRelatedProject(), PAYABLE, viewType, null));
                }

                if (invoiceData.getPoNumber() != null && !"".equals(invoiceData.getPoNumber().trim())) {
                    result.add(new FormGroup(wfmStrings.poNumber(), getWidgetAsFormControl(invoiceData.getPoNumber())));
                }
                return result;
            }
        });
    }

    @Override
    protected Integer getUploadFolderType() {
        return F_PUR_INV;
    }

    /*@Override
    protected ViewAddFiledsCodeName getCustomFieldCode() {
        return ViewAddFiledsCodeName.PurchaseInvoiceAdd;
    }*/

    protected void setEnableButtons(boolean b) {
        if (addCreditNote != null) {
            addCreditNote.setEnabled(b);
        }
        if (copyToNewInvoice != null) {
            copyToNewInvoice.setEnabled(b);
        }
        if (edit != null) {
            edit.setEnabled(b);
        }

        if (approveButton != null) {
            approveButton.setEnabled(b);
        }
    }

    private AbstractAsyncCallback getAsyncCallback() {
        return new AbstractAsyncCallback() {

            public void failure(Throwable caught) {
                setEnableButtons(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Object result) {
                setEnableButtons(true);
                Info.show(property.getSingular(wfmStrings.messSuccessfullyApproved(), accountingStrings.invoice()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, result, PurchaseInvoiceSummaryView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, result, PurchaseInvoiceSummaryView.this);
                closeTab("accounting|purchaseinvoice");
            }
        };
    }

    private void pdfVersion(final HTMLPanel hp, Integer templateId) {
        generatePDF(hp, templateId);
    }

    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(purchaseInvoiceID, pdfTemplateID, null);
        String pdfURL = CommandConstants.PDF_URL + "/savedPurchaseInvoiceViewPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    public String getIconStyle() {
        return null;
    }

    @Override
    public String getPropertyCode() {
        if (isRecurringBill) {
            return Constants.RECURRING_BILL;
        } else {
            return Constants.PURCHASE_INVOICE;
        }
    }

    protected ViewAddFiledsCodeName getViewTypeForCustomFields() {
        return ViewAddFiledsCodeName.PurchaseInvoiceAdd;
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
