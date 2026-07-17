package com.edatasite.workforce.gwt.expenses.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AccountLookUpForExpense;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AddEditAccountView2;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NotesWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.upload.GeneralAttachmentLinksComponent;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpensePaymentData;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseRequestObject;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceAsync;
import com.edatasite.workforce.gwt.expenses.client.rpc.ReportData;
import com.edatasite.workforce.gwt.expenses.client.ui.ExpenseConstants;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartAccountLookUpForExpense;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_EXPENSE_REPORT_HISTORY_NOTES;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_JOURNAL_REPORT;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 16.11.2008
 * Time: 17:45:53
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseSummaryView extends FooteredView implements Colapse, Constants, FittedContent, AccountingConstants, AccountingCustomFormConstants, ExpenseConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final ExpenseServiceAsync expenseService = ExpenseService.App.get();
    protected final InvoiceServiceAsync invoiceService = InvoiceService.App.get();

    private final Integer objectId;
    private final String section;
    private ExpenseReportsListItem expenseReportData;
    private final int subTotalColumnIndex = 0;
    private boolean isOnlyShowLinks;
    private boolean isDoubleTaxEnabled;
    private boolean isCompanyExpense;
    private TotalHTML subTotal, totalTax, baseTotal, total, amountDue;
    private TotalHTML subTotalLabel, totalTaxLabel, baseTotalLabel, totalLabel, amountDueLabel;
    private EditableTable itemsTable;
    private ReceiptTable totalsTable;

    private AddEditAccountView2 addAccountPopup;
    private SmartAccountLookUpForExpense categoryLookUp;

    public SplitButton printPdfSplitButton;
    private final List<Widget> result = new ArrayList<>();
    private ExpensePaymentPanel expensePaymentPanel;

    private NoteHistoryWidget noteHistoryWidget;
    private NotesWidget declineNoteWidget;
    private final HashMap<String, Widget> widgetsMap = new HashMap<>();
    private HTMLPanel htmlPanel;

    private boolean hasAccountingBeforeBlockDate;

    //    private DataListBox pdfTemplateListBox;
    private boolean canApprove = false;
    private boolean canRelateToProject = false;
    private boolean canAddCategory = false;
    private boolean canAddPayment = false;

    private LinkedList<String> itemColumns;
    private FooterUploadPanel uploadPanel;
    private MaterialLink showMoreLink;
    public InvoiceAdvancedOptions advancedOptions;

    public ExpenseSummaryView(Integer objectId) {

        super("previewReport");
        setDescription(property.getSingular(wfmStrings.summaryView(), wfmStrings.expenseClaim()));
        this.objectId = objectId;
        this.section = PermissionConstants.ACCOUNTING_CONTEXT;
    }

    public ExpenseSummaryView(Integer objectId, String sectionContext) {
        super("previewReport");
        setDescription(property.getSingular(wfmStrings.summaryView(), wfmStrings.expenseClaim()));
        this.objectId = objectId;
        this.section = sectionContext;
    }

    public String getIconStyle() {
        return null;
    }

    protected Widget onInitialize() {

        loadExpenseData();
        canApprove = Utils.hasPermission(PermissionConstants.ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM) || Utils.hasPermission(PermissionConstants.HRMS_CAN_APPROVE_EXPENSE_CLAIM);
        canRelateToProject = Utils.hasPermission(PermissionConstants.EXPENSE_ADD_VIEW_FULL_ACCESS) || Utils.hasPermission(PermissionConstants.HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS);
        canAddPayment = Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_ADD_PAYMENT) || Utils.hasPermission(PermissionConstants.HRMS_EXPENSE_ADD_PAYMENT);
        canAddCategory = Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY);
        return null;
    }

    private void loadExpenseData() {

        if (objectId != null) {
            LoadingPanel.loading(true);
            expenseService.getReportSummaryData(objectId, new AbstractAsyncCallback<ReportData>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(ReportData result) {
                    expenseReportData = result.getReport();
                    initPaymentsPanel(result);

                    isOnlyShowLinks = result.isOnlyLinksShow();
                    isDoubleTaxEnabled = result.isDoubleTaxEnabled();
                    hasAccountingBeforeBlockDate = (Utils.isExpensesLocked() && DateUtils.getTransactionLockDate().after(expenseReportData.getStartDate().getNonConvertedDate()));
                    isCompanyExpense = expenseReportData.isCompanyExpense();

                    initForm();
                    fillDynamicTable(expenseReportData.getItems());
                    fillTotalTable();
                    loadExpenseReportTotals(expenseReportData.getPaymentItems(), true);
                    systemCustomFields = expenseReportData.getSystemCustomFields();


                    initWidgetsMap();

                    htmlPanel = new WftHTMLPanel(result.getLayoutHTML(), widgetsMap).getContainer();
                    htmlPanel.setStyleName("add-form");
                    htmlPanel.add(createFooter());
                    add(htmlPanel);

                    LoadingPanel.loading(false);
                }
            });
        }
    }

    private void initPaymentsPanel(ReportData result) {
        expensePaymentPanel = new ExpensePaymentPanel(result);
        expensePaymentPanel.setVisible(false);
        expensePaymentPanel.setLoadTotals(data -> {
            BigDecimal dueAmount = loadExpenseReportTotals(data, false);
            boolean dueAmountLeft = dueAmount.compareTo(BigDecimal.ZERO) > 0;
            expensePaymentPanel.setVisible(dueAmountLeft);
            expensePaymentPanel.setDueAmount(dueAmount);
        });
        if ((EXPENSE_APPROVED.equals(expenseReportData.getStatusCode()) || PARTIALLY_PAID.equals(expenseReportData.getStatusCode())) && canAddPayment) {
            expensePaymentPanel.setVisible(true);
            if (expenseReportData.getPaymentAccount() != null) {
                expensePaymentPanel.addPaymentLookupItem(expenseReportData.getPaymentAccount());
            }
            String number = expenseReportData.getExpenseNumber() != null ? expenseReportData.getExpenseNumber() : "";
            expensePaymentPanel.setReferenceNumber(number);

        }
        widgetsMap.put(INPUT_PAYMENT_PANEL, expensePaymentPanel);
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return ExpenseSummaryView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return ExpenseSummaryView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> buttonList = initButtons(expenseReportData);
        result.addAll(buttonList);
        return result;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.addClickHandler(click -> noteHistoryWidget.setLoadData(callback -> {
            if (objectId == null) {
                return;
            }
            expenseService.loadExpenseNoteHistory(objectId, callback);
        }));
        informer.setInitialClasses("informer-item history-notes-container");
        if (Utils.hasPermission(ACCOUNTING_EXPENSE_REPORT_HISTORY_NOTES)) {
            leftSideWidgets.add(informer);
        }
        leftSideWidgets.add(uploadPanel);

        if (expenseReportData != null && expenseReportData.getJournalId() != null && Utils.hasPermission(ACCOUNTING_JOURNAL_REPORT)) {
            FooterInformer showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
            showJournal.addClickHandler(clickEvent -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + expenseReportData.getJournalId(), accountingStrings.reportView() + ": " + expenseReportData.getExpenseNumber(), accountingStrings.reportView() + ": " + expenseReportData.getExpenseNumber());
            });
            if (expenseReportData.getStatusCode().equals(EXPENSE_REVERSED)) {
                showJournal.setBadgeCount(2);
            } else {
                showJournal.setBadgeCount(1);
            }
            leftSideWidgets.add(showJournal);
        }

        return leftSideWidgets;
    }

    private void fillTotalTable() {
        if (expenseReportData.getBaseCurrency() != null) {
            String value = "(" + (expenseReportData.getBaseCurrency().getName() != null && !"".equals(expenseReportData.getBaseCurrency().getName()) ? expenseReportData.getBaseCurrency().getName() : "") + ")";
            baseTotalLabel.setCurrency(value);
        }

        if (expenseReportData.getBaseCurrency() != null && expenseReportData.getExpenseCurrency() != null && !expenseReportData.getExpenseCurrency().getId().equals(expenseReportData.getBaseCurrency().getId())) {
            String value = "(" + (expenseReportData.getExpenseCurrency().getName() != null && !"".equals(expenseReportData.getExpenseCurrency().getName()) ? expenseReportData.getExpenseCurrency().getName() + ")" : ")");
            totalLabel.setCurrency(value);
        }
    }

    private ColumnConfig[] getColumnArray() {
        itemColumns = new LinkedList<>();
        LinkedList<ColumnConfig> columnsList = new LinkedList<>();

        if (expenseReportData.getCustomItemColumns() != null && expenseReportData.getCustomItemColumns().length > 0) {

            ColumnConfig columnConfig;

            for (ColumnConfigs column : expenseReportData.getCustomItemColumns()) {

                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);

                switch (column.getCode()) {
                    case ExpenseConstants.ACCOUNT_LIST:
                        if (canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ExpenseConstants.ACCOUNT_LIST, column.isChanged() ? column.getTitle() : wfmStrings.category(), Utils.getColumnWidth(column.getWidth(), 150));
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.ACCOUNT_LIST);
                        }
                        break;
                    case ExpenseConstants.DESCRIPTION:
                        columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.DESCRIPTION, column.isChanged() ? column.getTitle() : wfmStrings.description(), Utils.getColumnWidth(column.getWidth(), 250));
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ExpenseConstants.DESCRIPTION);
                        break;
                    case ExpenseConstants.UNITS:
                        columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.UNITS, column.isChanged() ? column.getTitle() : wfmStrings.qty(), Utils.getColumnWidth(column.getWidth(), 75), false, RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ExpenseConstants.UNITS);
                        break;
                    case ExpenseConstants.COST:
                        columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.COST, column.isChanged() ? column.getTitle() : wfmStrings.price(), Utils.getColumnWidth(column.getWidth(), 75), false, RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ExpenseConstants.COST);
                        break;
                    case ExpenseConstants.TAX_LIST:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.TAX_LIST, column.isChanged() ? column.getTitle() : wfmStrings.tax(), Utils.getColumnWidth(column.getWidth(), 150));
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.TAX_LIST);
                        }
                        break;
                    case ExpenseConstants.DOUBLE_TAX:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.DOUBLE_TAX, column.isChanged() ? column.getTitle() : wfmStrings.tax(), Utils.getColumnWidth(column.getWidth(), 150));
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.DOUBLE_TAX);
                        }
                        break;
                    case ExpenseConstants.CUSTOMER_LIST:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.CUSTOMER_LIST, column.isChanged() ? column.getTitle() : accountingStrings.billing(), Utils.getColumnWidth(column.getWidth(), 150));
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.CUSTOMER_LIST);
                        }
                        break;
                    case ExpenseConstants.MARKUP_AMOUNT:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.MARKUP_AMOUNT, column.isChanged() ? column.getTitle() : accountingStrings.markupAmountOrPercent(), Utils.getColumnWidth(column.getWidth(), 75), false, RIGHT_ALIGN_CELL);
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.MARKUP_AMOUNT);
                        }
                        break;
                    case ExpenseConstants.PROJECT_LIST:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.PROJECT_LIST, column.isChanged() ? column.getTitle() : Property.get(Constants.PROJECT, wfmStrings.project()), Utils.getColumnWidth(column.getWidth(), 150));
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.PROJECT_LIST);
                        }
                        break;
                    case ExpenseConstants.PO_LIST:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.PO_LIST, column.isChanged() ? column.getTitle() : wfmStrings.purchaseorder(), Utils.getColumnWidth(column.getWidth(), 120));
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnsList.add(columnConfig);
                            itemColumns.add(ExpenseConstants.PO_LIST);
                        }
                        break;
                    case ExpenseConstants.RECEIPTS_PANEL:
                        columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.RECEIPTS_PANEL, column.isChanged() ? column.getTitle() : accountingStrings.receipts(), Utils.getColumnWidth(column.getWidth(), 85));
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ExpenseConstants.RECEIPTS_PANEL);
                        break;
                    case ExpenseConstants.TOTAL:
                        columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.TOTAL, column.isChanged() ? column.getTitle() : wfmStrings.total(), Utils.getColumnWidth(column.getWidth(), 85), false, RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ExpenseConstants.TOTAL);
                        break;
                    case ExpenseConstants.BASE_SUBTOTAL:
                        columnConfig = new ColumnConfig(CustomCell.class, ExpenseConstants.BASE_SUBTOTAL, column.isChanged() ? column.getTitle() : accountingStrings.baseTotal(), Utils.getColumnWidth(column.getWidth(), 85), false, RIGHT_ALIGN_CELL);
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ExpenseConstants.BASE_SUBTOTAL);
                        break;
                    default:
                        columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 100));
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(column.getCode());
                        break;
                }
            }
        } else {
            if (canAddCategory || canApprove || canRelateToProject) {
                columnsList.add(new ColumnConfig(LookUpCell.class, ExpenseConstants.ACCOUNT_LIST, wfmStrings.category(), 150));
                itemColumns.add(ExpenseConstants.ACCOUNT_LIST);
            }
            columnsList.add(new ColumnConfig(CustomCell.class, ExpenseConstants.DESCRIPTION, wfmStrings.description(), 250));
            itemColumns.add(ExpenseConstants.DESCRIPTION);

            columnsList.add(new ColumnConfig(CustomCell.class, ExpenseConstants.UNITS, wfmStrings.qty(), 100, false, RIGHT_ALIGN_CELL));
            itemColumns.add(ExpenseConstants.UNITS);

            columnsList.add(new ColumnConfig(CustomCell.class, ExpenseConstants.COST, wfmStrings.price(), 100, false, RIGHT_ALIGN_CELL));
            itemColumns.add(ExpenseConstants.COST);
//
            if (isCompanyExpense || canApprove || canRelateToProject) {
                columnsList.add(new ColumnConfig(CustomCell.class, TAX_LIST, wfmStrings.tax(), 150));
                itemColumns.add(ExpenseConstants.TAX_LIST);
            }

            columnsList.add(new ColumnConfig(CustomCell.class, ExpenseConstants.RECEIPTS_PANEL, accountingStrings.receipts(), 100));
            itemColumns.add(ExpenseConstants.RECEIPTS_PANEL);

            columnsList.add(new ColumnConfig(CustomCell.class, TOTAL, wfmStrings.total(), 100, false, RIGHT_ALIGN_CELL));
            itemColumns.add(ExpenseConstants.TOTAL);
//
            columnsList.add(new ColumnConfig(CustomCell.class, ExpenseConstants.BASE_SUBTOTAL, accountingStrings.baseTotal(), 100, false, RIGHT_ALIGN_CELL));
            itemColumns.add(ExpenseConstants.BASE_SUBTOTAL);
        }

        return columnsList.toArray(new ColumnConfig[0]);
    }

    private void initForm() {
        advancedOptions = createAdvancedOptions();
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions));
        showMoreLink.addStyleName("btn-flat ExpenseSummaryVIew");

        itemsTable = new EditableTable(getColumnArray(), false);
        initTotalsTable();
//        initPdfTemplatePanel();
    }


    /*private void initPdfTemplatePanel() {
        pdfTemplateListBox = new DataListBox();
        if (expenseReportData.getPdfTemplateList() != null && expenseReportData.getPdfTemplateList().getItems() != null && expenseReportData.getPdfTemplateList().getItems().length > 0) {
            pdfTemplateListBox.setItems(expenseReportData.getPdfTemplateList().getItems());
            if (expenseReportData.getPdfTemplateId() != null) {
                pdfTemplateListBox.setSelected(expenseReportData.getPdfTemplateId());
            }
            FormGroup pdfField = new FormGroup(wfmStrings.chooseTemplate(), pdfTemplateListBox);
            advancedOptions.addToBodyContainer(pdfField);
        }
    }*/

    private void initWidgetsMap() {

        HTML dateValue = new HTML(DateUtils.format(expenseReportData.getStartDate()));
        FormGroup dateField = new FormGroup(wfmStrings.date(), wrapWidgetToFormControl(dateValue));
        widgetsMap.put(INPUT_DATE, dateField);

        systemCustomFieldsMap.put(INPUT_EXP_TITLE, getWidgetAsFormControl(expenseReportData.getTitle()));

        if (expenseReportData.getExpenseNumber() != null) {
            //if (expenseReportData.getNumber().getIntNumber() != null || expenseReportData.getNumber().getNumberString() != null) {
            String number = expenseReportData.getExpenseNumber();
            HTML numberValue = new HTML(number);
            FormGroup numberField = new FormGroup(property.getShortForNumber(wfmStrings.number()), wrapWidgetToFormControl(numberValue));
            widgetsMap.put(INPUT_NUMBER, numberField);
            //}
        }
        if (expenseReportData.getCurrentApproverEmployeeID() != null) {
            widgetsMap.put(INPUT_MANAGER, new FormGroup(wfmStrings.approver(), getWidgetAsFormControl(expenseReportData.getCurrentApproverEmployeeName())));
        }
        if (expenseReportData.getExpenseCurrency() != null) {
            HTML rateLabel = new HTML("1 " + expenseReportData.getBaseCurrency().getName() + " = " +
                    AccountingUtils.get().formatExRate(expenseReportData.getExchangeRate()) + " " + expenseReportData.getExpenseCurrency().getName());
            systemCustomFieldsMap.put(INPUT_EXCHANGE_RATE, wrapWidgetToFormControl(rateLabel));
        }
        if (!isCompanyExpense /*&& (Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_ADD_TO_STAFF)*/) {
            FormGroup employeeField = new FormGroup(wfmStrings.employee(), getWidgetAsFormControl(expenseReportData.getReporterName()));
            widgetsMap.put(INPUT_EMPLOYEE, employeeField);
        }

        FormGroup supplierField = new FormGroup(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), getWidgetAsFormControl(expenseReportData.getSupplier() != null ? expenseReportData.getSupplier().getName() : "N/A"));
        if (isCompanyExpense) {
            widgetsMap.put(INPUT_SUPPLIER, supplierField);
        } else {
            widgetsMap.put(INPUT_CUSTOMER, supplierField);
        }

        Widget taxCalcTypeWidget = getWidgetAsFormControl(wfmStrings.taxExclusive());
        if (expenseReportData.getTaxCalculationType() != null) {
            if (NO_TAX_CALCULATION.equals(expenseReportData.getTaxCalculationType())) {
                taxCalcTypeWidget = getWidgetAsFormControl(wfmStrings.noTax());
            } else if (TAX_CALCULATION_INCLUSIVE.equals(expenseReportData.getTaxCalculationType())) {
                taxCalcTypeWidget = getWidgetAsFormControl(wfmStrings.taxInclusive());
            } else if (TAX_CALCULATION_EXCLUSIVE.equals(expenseReportData.getTaxCalculationType())) {
                taxCalcTypeWidget = getWidgetAsFormControl(wfmStrings.taxExclusive());
            }
        }
        if (hasActiveSystemCustomField(INPUT_TAX_CALC_TYPE)) {
            systemCustomFieldsMap.put(INPUT_TAX_CALC_TYPE, taxCalcTypeWidget);
        }
        addSystemCustomFields(widgetsMap);

        widgetsMap.put(INPUT_ITEM_TABLE, itemsTable);
        widgetsMap.put(INPUT_TOTALS_TABLE, totalsTable);

        declineNoteWidget = new NotesWidget(true);
        noteHistoryWidget = new NoteHistoryWidget(null);

        if (objectId != null) { //If someone is going to use it as add
            noteHistoryWidget.setSaveIntoDatabase(historyItem -> {
                if (historyItem != null) {
                    LoadingPanel.loading(true);
                    expenseService.createExpenseClaimHistory(objectId, historyItem, new AsyncCallback<Integer>() {
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
                    expenseService.deleteExpenseHistory(hisItem.getObjectID(), new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Boolean isDeletionSuccessfull) {
                            LoadingPanel.loading(false);
                        }
                    });
                }
            });
        }
        uploadPanel = new FooterUploadPanel(F_EXP_DOC, expenseReportData.getId(), true, wfmStrings.attachments());

        initCustomFields();


        FormGroup showMoreField = new FormGroup(showMoreLink);
        showMoreField.setLabel("&nbsp;");
        widgetsMap.put(INPUT_SHOW_MORE, showMoreField);
    }

    private String getInfoText(String text) {
        return "<b class=customTitle>" + text + ":</b>";
    }


    protected InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {
            @Override
            public List<Widget> getOptionWidgets() {
                List<Widget> result = new ArrayList<>();

                FormGroup faField = new FormGroup(wfmStrings.fixedAsset(), getWidgetAsFormControl(expenseReportData.getFixedAsset() != null ? expenseReportData.getFixedAsset().getName() : "N/A"));
                result.add(faField);

                if (expenseReportData.isJoinOpportunityToExpenseClaim()) {
                    FormGroup opportunityField = new FormGroup(Property.get(Constants.Opportunities, wfmStrings.opportunity()), getWidgetAsFormControl(expenseReportData.getOpportunity() != null ? expenseReportData.getOpportunity().getName() : "N/A"));
                    result.add(opportunityField);
                }

                if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
                    FormGroup projectField = new FormGroup(Property.get(Constants.PROJECT, wfmStrings.relatedProject(), wfmStrings.project()), getWidgetAsFormControl(expenseReportData.getProject() != null ? expenseReportData.getProject().getName() : "N/A"));
                    result.add(projectField);
                }
                if (AccountingUtils.get().isEnableLandedCost() && !Utils.hasGenericAccess(GenericSettingsEnum.PO_IN_LINE_ITEM_ENABLE) && expenseReportData.getPurchaseOrder() != null) {
                    HTML label = new HTML("<a href=\"javascript:\">" + expenseReportData.getPurchaseOrder().getName() + "</a>");
                    label.addClickHandler(clickEvent -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged(PURCHASE_ORDER + "|summary/" + expenseReportData.getPurchaseOrder().getId(), expenseReportData.getPurchaseOrder().getName());
                    });
                    result.add(new FormGroup(Property.getShortName(PURCHASE_ORDER, accountingStrings.relatedPO()), wrapWidgetToFormControl(label)));
                }
                if (expenseReportData.getSaleOrder() != null) {
                    HTML label = new HTML("<a href=\"javascript:\">" + expenseReportData.getSaleOrder().getName() + "</a>");
                    label.addClickHandler(clickEvent -> {
                        goTo(SALE_ORDER_CODE + "|summary/" + expenseReportData.getSaleOrder().getId(), expenseReportData.getSaleOrder().getName());
                    });
                    result.add(new FormGroup(Property.getShortName(SALE_ORDER, accountingStrings.relatedSO()), wrapWidgetToFormControl(label)));
                }
                if (expenseReportData.getPayableAccount() != null) {
                    result.add(new FormGroup(wfmStrings.accountsPayable(), getWidgetAsFormControl(expenseReportData.getPayableAccount().getCode() + " -> " + expenseReportData.getPayableAccount().getName())));
                }
                return result;
            }
        }, false);
    }

    private void initTotalsTable() {
        totalsTable = new ReceiptTable();
        totalsTable.getElement().addClassName("java-ExpenseSummaryView");

        subTotalLabel = new TotalHTML(wfmStrings.subtotal());
        totalTaxLabel = new TotalHTML(wfmStrings.taxTotal());
        baseTotalLabel = new TotalHTML(wfmStrings.total());
        totalLabel = new TotalHTML(wfmStrings.total());
        amountDueLabel = new TotalHTML(wfmStrings.dueAmount());

        subTotal = new TotalHTML(AccountingUtils.getZero());
        totalTax = new TotalHTML(AccountingUtils.getZero());
        total = new TotalHTML(AccountingUtils.getZero());
        baseTotal = new TotalHTML(AccountingUtils.getZero());
        amountDue = new TotalHTML(AccountingUtils.getZero());

        baseTotalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        totalLabel.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_LABEL);
        subTotal.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        total.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        baseTotal.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
    }


    private List<Widget> initButtons(ExpenseReportsListItem item) {
        List<Widget> buttonList = new LinkedList<>();
        /*if (Utils.getUserID().equals(item.getCurrentApproverEmployeeID()) || canApprove) {
            canApprove = true;
        }*/
        String statusCode = item.getStatusCode();
        boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(item.getProjectStatusCode()));

        if (canApprove && Utils.getUserID().equals(item.getCurrentApproverEmployeeID())) {
            if (hasAccessToChange && EXPENSE_SUBMITTED.equals(statusCode)) {
                SplitButton approveOrRejectButton = new SplitButton(100, WfmButton2.BTN_PRIMARY);
                SplitButtonItem approve = new SplitButtonItem("APPROVE", wfmStrings.approve(), () -> {
                    setButtonsEnabled(false);
                    changeStatus(EXPENSE_APPROVED);
                }, true);

                SplitButtonItem reject = new SplitButtonItem("REJECT", wfmStrings.reject(), () -> {
                    setButtonsEnabled(false);
                    Command declineCommand = () -> {
                        setButtonsEnabled(false);
                        changeStatus(EXPENSE_DECLINED);
                    };

                    declineNoteWidget.setNoteListener(declineCommand);
                    declineNoteWidget.setSaveIntoDatabase(null);    //Based on that tab will be closed
                    declineNoteWidget.setCloseListener(null);
                    declineNoteWidget.noteShell();
                });

                approveOrRejectButton.addItemList(Arrays.asList(approve, reject));
                buttonList.add(approveOrRejectButton);
            }
        }

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        printPdfSplitButton.ensureDebugId("printPdf_button");

        List<SplitButtonItem> pdfCommandSubItems = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (expenseReportData.getPdfTemplateList() != null && expenseReportData.getPdfTemplateList().getItems() != null) {
            for (SelectItem pdfItem : expenseReportData.getPdfTemplateList().getItems()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfCommandSubItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePdf(pdfItem.getId())));
            }
        }
        Integer finalDefaultTemplateId = defaultTemplateId;
        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePdf(finalDefaultTemplateId), true);
        pdfVersion.ensureDebugId("expensePdfVersionItem");
        pdfCommandSubItems.add(pdfVersion);

        if (Utils.hasRoles(Constants.ADMIN)) {
            pdfCommandSubItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), new Command() {
                @Override
                public void execute() {
                    Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.EXPENSE_REPORT.name());
                }
            }));
        }
        printPdfSplitButton.addItemList(pdfCommandSubItems);
        if (!printPdfSplitButton.getItemsMap().isEmpty() || printPdfSplitButton.getDefaultItem() != null) {
            buttonList.add(printPdfSplitButton);
        }

        boolean isSubmittedExpenseEditable = !expenseReportData.isDoubleApproverEnabled() && Utils.hasRole(ADMIN) && (EXPENSE_APPROVED.equals(statusCode) || EXPENSE_SUBMITTED.equals(statusCode));

        String editPermission = section.equals(PermissionConstants.ACCOUNTING_CONTEXT) ? PermissionConstants.ACCOUNTING_EXPENSE_REPORT_EDIT : PermissionConstants.HRMS_EXPENSE_REPORT_EDIT;
        String fullEditPermission = section.equals(PermissionConstants.ACCOUNTING_CONTEXT) ? PermissionConstants.ACCOUNTING_EXPENSE_REPORT_FULL_EDIT_ACCESS : PermissionConstants.HRMS_EXPENSE_REPORT_EDIT;

        boolean isAccessToEdit = ((Utils.hasPermission(editPermission) && item.getReporterId().equals(Utils.getUserID())) || Utils.hasPermission(fullEditPermission)) && !item.isAllocatedToPO();
        if (expenseReportData.isApproveProcessEnabled()) {
            isAccessToEdit = Utils.hasPermission(editPermission) && expenseReportData.isApprover() && !item.isAllocatedToPO();
        }

        if (hasAccessToChange && !hasAccountingBeforeBlockDate && (EXPENSE_DRAFT.equals(statusCode) || EXPENSE_DECLINED.equals(statusCode) || EXPENSE_SUBMITTED.equals(statusCode) || EXPENSE_APPROVED.equals(statusCode) || EXPENSE_PAID.equals(statusCode) || PARTIALLY_PAID.equals(statusCode)) && isAccessToEdit) {

            WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
            editButton.addClickHandler(clickEvent -> {
                setButtonsEnabled(false);
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|edit/" + item.getId(), item.getExpenseNumber(), item.getTitle());
            });
            buttonList.add(editButton);

        }
        if (hasAccessToChange && !hasAccountingBeforeBlockDate && (canApprove || Utils.getUserID().equals(item.getCurrentApproverEmployeeID()))
                && (EXPENSE_DRAFT.equals(statusCode) || EXPENSE_DECLINED.equals(statusCode) || EXPENSE_SUBMITTED.equals(statusCode) ||
                EXPENSE_APPROVED.equals(statusCode) || EXPENSE_PAID.equals(statusCode) || PARTIALLY_PAID.equals(statusCode)
                || isSubmittedExpenseEditable)) {
            if (canApprove) {
                WfmButton2 approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_PRIMARY);
                approveButton.addClickHandler(sender -> {
                    setButtonsEnabled(false);
                    changeStatus(EXPENSE_APPROVED);
                });

                if (!hasAccountingBeforeBlockDate && (EXPENSE_DRAFT.equals(statusCode) || EXPENSE_DECLINED.equals(statusCode))) {
                    buttonList.add(approveButton);
                }
            } else {
                WfmButton2 submitButton = new WfmButton2(EXPENSE_DRAFT.equals(statusCode) ? wfmStrings.submit() : wfmStrings.resubmit(), WfmButton2.BTN_PRIMARY);
                submitButton.addClickHandler(clickEvent -> {
                    setButtonsEnabled(false);
                    WfmMessageBox alert = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
                    alert.setTitle(wfmStrings.submit());
                    alert.setMessage(accountingStrings.doYouWantToSubmit() + " " + expenseReportData.getTitle() + " " + property.getPlural(wfmStrings.expenseClaims()));
                    alert.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            changeStatus(EXPENSE_SUBMITTED);
                        }

                        @Override
                        public void onCancel() {
                            setButtonsEnabled(true);
                        }
                    });
                    alert.open();
                });

                if (!hasAccountingBeforeBlockDate && (EXPENSE_DRAFT.equals(statusCode) || EXPENSE_DECLINED.equals(statusCode))) {
                    buttonList.add(submitButton);
                }
            }
        }

        return buttonList;
    }

    private void setButtonsEnabled(boolean enabled) {
        if (widgetsMap.get(SAVE_AND_APPROVE_BUTTON) != null) {
            ((WfmButton2) widgetsMap.get(SAVE_AND_APPROVE_BUTTON)).setEnabled(enabled);
        }
        if (widgetsMap.get(REJECT_BUTTON) != null) {
            ((WfmButton2) widgetsMap.get(REJECT_BUTTON)).setEnabled(enabled);
        }
        if (widgetsMap.get(SAVE_AS_DRAFT_BUTTON) != null) {
            ((WfmButton2) widgetsMap.get(SAVE_AS_DRAFT_BUTTON)).setEnabled(enabled);
        }
        if (widgetsMap.get(APPROVE_AND_SEND_BUTTON) != null) {
            ((WfmButton2) widgetsMap.get(APPROVE_AND_SEND_BUTTON)).setEnabled(enabled);
        }
        if (widgetsMap.get(DELETE_BUTTON) != null) {
            ((WfmButton2) widgetsMap.get(DELETE_BUTTON)).setEnabled(enabled);
        }
        if (widgetsMap.get(CLOSE_BUTTON) != null) {
            ((WfmButton2) widgetsMap.get(CLOSE_BUTTON)).setEnabled(enabled);
        }
    }

    private void initCustomFields() {

        if (expenseReportData.getCustomFieldItems() != null && expenseReportData.getCustomFieldItems().size() > 0) {
            MaterialPanel customFieldsWrapper = new InvoiceCustomFieldsSummaryView(expenseReportData.getCustomFieldItems()).getCustomsDataView();
            advancedOptions.initCustomFieldSummaryWidget(customFieldsWrapper);
        }
    }

    private void changeStatus(String status) {
        if (!validation()) {
            setButtonsEnabled(true);
            return;
        }
        String note = "";
        if (status.equals(EXPENSE_DECLINED)) {
            if (declineNoteWidget != null && declineNoteWidget.getLastHistoryItem() != null && declineNoteWidget.getLastHistoryItem().getComment() != null && !"".equals(declineNoteWidget.getLastHistoryItem().getComment().trim())) {
                note = wfmStrings.rejectionReason() + declineNoteWidget.getLastHistoryItem().getComment();
            }
        }
        String _note = note;
        if (EXPENSE_APPROVED.equals(status) && Utils.isDoubleMessageEnable()) {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouWantToSaveChanges(), new CloseHandler() {
                @Override
                public void onSubmit() {
                    updateData(status, _note);
                }

                @Override
                public void onCancel() {
                    setButtonsEnabled(true);
                }
            });
            wfmMessageBox.setTitle(wfmStrings.confirmation());
            wfmMessageBox.open();
        } else {
            updateData(status, note);
        }
    }

    public boolean validation() {
        if (Utils.isExpensesLocked() && DateUtils.getTransactionLockDate().after(expenseReportData.getStartDate().getNonConvertedDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate("Expense Claim", Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void updateData(String status, String note) {
        boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(expenseReportData.getProjectStatusCode()));
        boolean isDrAdmAccAndDoubleApproveDisabled = Utils.hasPermission(PermissionConstants.ACCOUNTING_CAN_APPROVE_EXPENSE_CLAIM);
        boolean hasAccountingBeforeBlockDate = (Utils.isExpensesLocked() && DateUtils.getTransactionLockDate().after(expenseReportData.getStartDate().getNonConvertedDate()));
        if (EXPENSE_APPROVED.equals(status) || EXPENSE_DECLINED.equals(status)) {
            if (hasAccessToChange && (isDrAdmAccAndDoubleApproveDisabled || expenseReportData.isApprover()) && !hasAccountingBeforeBlockDate) {
                if (EXPENSE_APPROVED.equals(status)) {
                    ArrayList<ExpenseListItem> lineItems = new ArrayList<>(itemsTable.getRowCount());
                    for (int i = 0; i < itemsTable.getRowCount(); i++) {
                        if (itemsTable.getColumnById(i, ACCOUNT_LIST) != null && itemsTable.getColumnById(i, ACCOUNT_LIST) instanceof AccountLookUpForExpense) {
                            SmartAccountLookUpForExpense category = (SmartAccountLookUpForExpense) itemsTable.getColumnById(i, ACCOUNT_LIST);
                            if (category.getSelectedItem() == null) {
                                itemsTable.notValid(i, ACCOUNT_LIST);
//                                new WfmMessageBox(IconEnum.ERROR, Action.OK, accountingStrings.plsChooseCategory()).open();
                                setButtonsEnabled(true);
                                return;
                            } else {
                                String id = category.getElement().getId();
                                ExpenseListItem item = new ExpenseListItem();
                                item.setId(Integer.valueOf(id));
                                item.setAccountId(category.getSelectedItemID());
                                lineItems.add(item);
                            }
                        } else if (itemsTable.getColumnById(i, ACCOUNT_LIST) != null && itemsTable.getColumnById(i, ACCOUNT_LIST) instanceof Label) {
                            Label label = (Label) itemsTable.getColumnById(i, ACCOUNT_LIST);
                            if (label == null || label.getText() == null || label.getText().isEmpty()) {
                                itemsTable.notValid(i, ACCOUNT_LIST);
//                                new WfmMessageBox(IconEnum.ERROR, Action.OK, accountingStrings.plsChooseCategory()).open();
                                setButtonsEnabled(true);
                                return;
                            } else {
                                String id = label.getElement().getId();
                                if (id != null && !id.isEmpty()) {
                                    String[] ids = id.split("_");
                                    ExpenseListItem item = new ExpenseListItem();
                                    item.setId(Integer.valueOf(ids[0]));
                                    item.setAccountId(Integer.valueOf(ids[1]));
                                    lineItems.add(item);
                                }
                            }
                        }
                    }
                    saveData(status, note, lineItems);
                } else {
                    saveData(status, note, null);
                }
            } else {
                String message = EXPENSE_APPROVED.equals(status) ? property.getSingular(accountingStrings.youCannotApprove(), wfmStrings.expenseClaim()) : property.getSingular(accountingStrings.youCannotDecline(), wfmStrings.expenseClaim());
                new WfmMessageBox(IconEnum.ERROR, Action.OK, message).open();
                setButtonsEnabled(true);
            }
        }
    }

    private void saveData(String status, String note, ArrayList<ExpenseListItem> lineItems) {
        LoadingPanel.loading(true);
        expenseService.changeExpenseStatus(objectId, status, note, null, lineItems, new AbstractAsyncCallback() {
            public void failure(Throwable caught) {
                setButtonsEnabled(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Object result) {
                setButtonsEnabled(true);
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSEREPORT_SAVED, result, ExpenseSummaryView.this);
                if (EXPENSE_APPROVED.equals(status)) {
                    Info.show(accountingMessages.approved(accountingStrings.expenseWasSuccessfuly()), Info.Type.INFO);

                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_APPROVED, result, ExpenseSummaryView.this);
                } else if (EXPENSE_DECLINED.equals(status)) {
                    Info.show(accountingMessages.declined(accountingStrings.expenseWasSuccessfuly()), Info.Type.INFO);
                } else if (EXPENSE_SUBMITTED.equals(status)) {
                    Info.show(accountingMessages.submitted(accountingStrings.expenseWasSuccessfuly()), Info.Type.INFO);

                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EXPENSE_SUBMITTED, result, ExpenseSummaryView.this);
                }
                closeTab();
            }
        });
    }

    private void fillDynamicTable(ExpenseListItem[] expenses) {
        if (expenses == null) {
            return;
        }
        itemsTable.removeAllRows();

        for (ExpenseListItem expenseItem : expenses) {
            Map<String, Widget> widgetsMap = new LinkedHashMap<>();

            for (String column : itemColumns) {
                switch (column) {
                    case ExpenseConstants.ACCOUNT_LIST:
                        if ((canApprove || canRelateToProject) && !(EXPENSE_APPROVED.equals(expenseReportData.getStatusCode()) || EXPENSE_DECLINED.equals(expenseReportData.getStatusCode()))
                                && expenseItem.getAccountId() == null) {
                            categoryLookUp = new SmartAccountLookUpForExpense(wfmStrings.expense(), () -> {
                                if (addAccountPopup == null) {
                                    addAccountPopup = new AddEditAccountView2(obj -> {
                                        AccountItem item = (AccountItem) obj;
                                        if (item != null && item.isCheckedForExpense()) {
                                            categoryLookUp.addAccountItem(item);
                                        }
                                    }, EXPENSES);
                                } else {
                                    addAccountPopup.showPopup();
                                }
                            }, Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_CLAIM_ADD_CATEGORY));

                            categoryLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> categoryLookUp.islink());
                            categoryLookUp.setSelected(expenseItem.getAccountName());
                            categoryLookUp.getElement().setId(expenseItem.getId().toString());
                            widgetsMap.put(ACCOUNT_LIST, categoryLookUp);
                        } else {
                            SmartAccountLookUpForExpense categoryLookUp = new SmartAccountLookUpForExpense(wfmStrings.expense());
                            categoryLookUp.setEnabled(false);
                            categoryLookUp.getSuggestBox().getTextBox().setEnabled(false);
                            categoryLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> categoryLookUp.islink());
                            if (expenseItem.getAccountId() != null) {
                                categoryLookUp.addItem(new SelectItem(expenseItem.getAccountId(), expenseItem.getAccountName()));
                            }
                            categoryLookUp.setSelected(expenseItem.getAccountName());
                            categoryLookUp.getElement().setId(expenseItem.getId().toString());
                            widgetsMap.put(ACCOUNT_LIST, categoryLookUp);
                        }
                        break;
                    case ExpenseConstants.DESCRIPTION:
                        Label description = new Label(expenseItem.getDescription());
                        description.setLayoutData(expenseItem.getId());
                        widgetsMap.put(DESCRIPTION, description);
                        break;
                    case ExpenseConstants.UNITS:

                        if (expenseItem.isProjectBase()) {
                            widgetsMap.put(UNITS, new Label(Utils.formatMinutes(expenseItem.getUnits()
                                    .multiply(new BigDecimal(60))
                                    .setScale(0, RoundingMode.HALF_UP)
                                    .intValue())));
                        } else {
                            widgetsMap.put(UNITS, new Label(AccountingUtils.get().format(expenseItem.getUnits())));
                        }
                        break;
                    case ExpenseConstants.COST:
                        widgetsMap.put(COST, new Label(AccountingUtils.get().formatUnitPrice(expenseItem.getCostPerUnit())));
                        break;
                    case ExpenseConstants.TAX_LIST:

                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            TaxLabel taxLabel = new TaxLabel();
                            taxLabel.setText(expenseItem.getTax() != null ? expenseItem.getTax().getName() : wfmStrings.noTax());
                            taxLabel.setTaxAmount(Optional.ofNullable(expenseItem.getTaxAmountInBase()).orElse(BigDecimal.ZERO));
                            if (expenseReportData.getExpenseCurrency() != null) {
                                taxLabel.setTaxAmount(taxLabel.getTaxAmount().multiply(expenseReportData.getExchangeRate()));
                            }
                            widgetsMap.put(TAX_LIST, taxLabel);
                        }
                        break;
                    case ExpenseConstants.DOUBLE_TAX:
                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            TaxLabel doubleTaxLabel = new TaxLabel();

                            doubleTaxLabel.setText(expenseItem.getDoubleTax() != null ? expenseItem.getDoubleTax().getName() : wfmStrings.noTax());
                            doubleTaxLabel.setTaxAmount(expenseItem.getDoubleTaxAmountInBase() != null ? expenseItem.getDoubleTaxAmountInBase() : ZERO);
                            if (expenseReportData.getExpenseCurrency() != null) {
                                doubleTaxLabel.setTaxAmount(doubleTaxLabel.getTaxAmount().multiply(expenseReportData.getExchangeRate()));
                            }
                            widgetsMap.put(DOUBLE_TAX, doubleTaxLabel);
                        }
                        break;
                    case ExpenseConstants.RECEIPTS_PANEL:
                        CustomHorizontalPanel attachmentPanel = new CustomHorizontalPanel();
                        FileResource[] attachments = expenseItem.getAttachments();

                        if (attachments != null && attachments.length > 0) {
                            SimpleLink viewLink = new SimpleLink(wfmStrings.summaryView());

                            viewLink.addClickHandler(clickEvent -> {
                                KpiModal dialogBox = new KpiModal();
                                GeneralAttachmentLinksComponent attachmentsPanel = new GeneralAttachmentLinksComponent(attachments,
                                        true,
                                        false);

                                dialogBox.add(attachmentsPanel);
                                dialogBox.open();
                            });
                            attachmentPanel.add(viewLink);
                        } else {
                            attachmentPanel.add(new Label("-"));
                        }
                        widgetsMap.put(RECEIPTS_PANEL, attachmentPanel);
                        break;
                    case ExpenseConstants.MARKUP_AMOUNT:

                        if (isCompanyExpense || canApprove || canRelateToProject) {
                            widgetsMap.put(ExpenseConstants.MARKUP_AMOUNT, new Label(expenseItem.getMarkupAmount() != null
                                    ? AccountingUtils.get().formatPrice(expenseItem.getMarkupAmount())
                                    : AccountingUtils.getZero()));
                        }
                        break;
                    case ExpenseConstants.DEPARTMENT_LIST:
                        Label departmentLabel = new Label();

                        if (expenseItem.getDepartment() != null) {
                            departmentLabel.setText(expenseItem.getDepartment().getName());
                        }
                        widgetsMap.put(ExpenseConstants.DEPARTMENT_LIST, departmentLabel);
                        break;
                    case ExpenseConstants.CUSTOMER_LIST:
                        String customerName = Optional.ofNullable(expenseItem.getClientName()).orElse("");
                        Label customerLabel = new Label(customerName);
                        if (expenseItem.getSaleInvoiceId() != null) {
                            customerLabel.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|7Csummary/" + expenseItem.getSaleInvoiceId()));
                        }
                        widgetsMap.put(CUSTOMER_LIST, customerLabel);
                        break;
                    case ExpenseConstants.PROJECT_LIST:
                        Label projectLabel = new Label();

                        if (expenseItem.getProject() != null) {
                            projectLabel.setText(expenseItem.getProject().getName());
                        }
                        widgetsMap.put(ExpenseConstants.PROJECT_LIST, projectLabel);
                        break;
                    case ExpenseConstants.PO_LIST:
                        Label purchaseOrderLabel = new Label();

                        if (expenseItem.getPurchaseOrder() != null) {
                            purchaseOrderLabel.setText(expenseItem.getPurchaseOrder().getName());
                        }
                        widgetsMap.put(ExpenseConstants.PO_LIST, purchaseOrderLabel);
                        break;
                    case ExpenseConstants.TOTAL:
                        widgetsMap.put(TOTAL, new Label(AccountingUtils.get().formatPrice(expenseItem.getSubtotal())));
                        break;
                    case ExpenseConstants.BASE_SUBTOTAL:
                        widgetsMap.put(BASE_SUBTOTAL, new Label(AccountingUtils.get().formatPrice(expenseItem.getBaseSubtotal())));
                        break;
                    default:
                        CompanyCustomFieldItem customFieldItem = expenseItem.getCustomFieldByCode(column);
                        Label label_ = new Label();

                        if (customFieldItem != null) {
                            if (DATA_TYPE_DATE.equals(customFieldItem.getDataType())) {
                                label_.setText(customFieldItem.getFieldDateNonConvertedValue() != null ? DateUtils.format(customFieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "");
                            } else {
                                label_.setText(customFieldItem.getFieldStringValue() != null ? customFieldItem.getFieldStringValue() : "");
                            }
                        } else {
                            label_.setText("");
                        }
                        widgetsMap.put(column, label_);
                        break;
                }
            }

            itemsTable.addRow(widgetsMap.values().toArray(new Widget[]{}));
        }
    }

    private class CustomHorizontalPanel extends HorizontalPanel implements CustomCellInterface {

        @Override
        public String getDisplayValue() {
            return getElement().getInnerHTML();
        }

        @Override
        public void setItemValue(Object value) {

        }

        @Override
        public void setItemFocus(boolean focused) {

        }
    }

    private BigDecimal loadExpenseReportTotals(ExpensePaymentData[] paymentItems, boolean calculateTotals) {
        totalsTable.clear();

        if (calculateTotals) {
            setTotalsData();
        }
        totalsTable.addItem(subTotalLabel, subTotal);
        totalsTable.addItem(totalTaxLabel, totalTax);

        if (expenseReportData.getBaseCurrency() == null || expenseReportData.getExpenseCurrency() == null || expenseReportData.getBaseCurrency().getId().equals(expenseReportData.getExpenseCurrency().getId())) {
            totalsTable.addGrossItem(totalLabel, total);
        } else {
            totalsTable.addGrossItem(totalLabel, total);
            totalsTable.addGrossItem(baseTotalLabel, baseTotal);
        }
        //Bu eski expense reportlada base currencyda payment bo'lganda due amount to'g'ri ishlashi uchun qo'shildi
        //exchangeRatePaneldan rateni olish keremas undagi data xato bo'lishi mumkin (old paymentlada rate teskari edi)

        BigDecimal totalAmount = AccountingUtils.get().parseToBigDecimal(total.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
        BigDecimal totalBaseAmount = AccountingUtils.get().parseToBigDecimal(baseTotal.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
        BigDecimal exchangeRate = expenseReportData.getExchangeRate();//totalAmount.compareTo(BigDecimal.ZERO) != 0 ? totalAmount.divide(totalBaseAmount, 12, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

        BigDecimal paidTotal = BigDecimal.ZERO;
        if (paymentItems != null) {
            for (ExpensePaymentData ep : paymentItems) {
                BigDecimal paymentAmount = ep.getPaymentAmount();

                //Agar paymentItemni exchangeRate parametri null bo'lsa, demak u base currencyda to'langan(old payment)
                if (ep.getExchangeRate() == null) {
                    paymentAmount = paymentAmount.multiply(exchangeRate);
                }
                paidTotal = paidTotal.add(paymentAmount);
                setPaymentInfoToTable(ep);
            }
        }

        BigDecimal dueAmount = totalAmount.subtract(paidTotal).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
        setDueAmount(dueAmount);
        totalsTable.setDueAmount(amountDueLabel, amountDue);
        return dueAmount;
    }

    private void setDueAmount(BigDecimal dueAmount) {
        amountDue.setText(AccountingUtils.get().formatPrice(dueAmount));
        expensePaymentPanel.setDueAmount(dueAmount);
    }

    private void setTotalsData() {
        BigDecimal subTotalValue = ZERO;
        for (int i = 0; i < itemsTable.getRowCount(); i++) {
            Label units = (Label) itemsTable.getColumnById(i, UNITS);
            Label cost = (Label) itemsTable.getColumnById(i, COST);
            BigDecimal net = getUnits(units.getText()).multiply(getUnits(cost.getText()));
            subTotalValue = subTotalValue.add(net.setScale(AccountingUtils.customQtyScale, RoundingMode.HALF_UP));
        }
        setTotalTaxValue(expenseReportData.getTaxTotal());
        subTotal.setText(AccountingUtils.get().formatPrice(subTotalValue));
        total.setText(AccountingUtils.get().formatPrice(expenseReportData.getTotal()));
        baseTotal.setText(AccountingUtils.get().formatPrice(expenseReportData.getBaseTotal()));
    }

    private void setTotalTaxValue(BigDecimal taxTotal) {
        totalTax.setText(AccountingUtils.get().formatPrice(taxTotal));
        totalTaxLabel.setVisible(taxTotal.compareTo(BigDecimal.ZERO) != 0);
        totalTax.setVisible(taxTotal.compareTo(BigDecimal.ZERO) != 0);
    }

    private void setPaymentInfoToTable(ExpensePaymentData ep) {
        MaterialPanel container = new MaterialPanel();
        container.add(new MaterialLink(accountingStrings.lessPayment(), (ep.isApplyCredit() ? "invoicepayment|paymentView/" : "expensepayment|summary/") + ep.getObjectID()));
        container.add(new Label(DateUtils.format(ep.getDate())));

        HTML valueHTML = new HTML(AccountingUtils.get().formatPrice(ep.getPaymentAmount()));
        valueHTML.setStyleName(AccountingCustomFormConstants.STYLE_TOTAL_VALUE);
        totalsTable.addPaidItem(container, valueHTML);
    }

    private BigDecimal getUnits(String text) {
        return AccountingUtils.get().parseToBigDecimal(getUnitsAsString(text));
    }

    private String getUnitsAsString(String text) {
        if (wfmStrings.notAvailable().equals(text)) {
            return accountingStrings.digit1();
        }
        return (text.indexOf(':') == -1 ? text : getHourValue(text));
    }

    private String getHourValue(String text) {
        String[] time = text.split(":");
        return String.valueOf(Double.parseDouble(time[0]) + Double.parseDouble(time[1]) / 60);
    }

    private boolean hasActiveSystemCustomField(String columnCode) {
        if (systemCustomFields == null || systemCustomFields.isEmpty()) {
            return false;
        }
        for (CompanyCustomFieldItem systemCustomField : systemCustomFields) {
            if (systemCustomField.isActive() && columnCode.equals(systemCustomField.getColumnCode())) {
                return true;
            }
        }
        return false;
    }

    private void generatePdf(Integer pdfTemplateId) {
        String pdfURL = CommandConstants.PDF_URL + "/expensesViewPDFHandler";
        ExpenseRequestObject requestObject = new ExpenseRequestObject();
        requestObject.setObjectID(objectId);
        requestObject.setTemplateId(pdfTemplateId);
        requestObject.setOnlyImageLinkShow(isOnlyShowLinks);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parameters, "_blank");
    }

    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public class TaxLabel extends Label {
        private BigDecimal taxAmount;

        public TaxLabel() {
        }

        public BigDecimal getTaxAmount() {
            return taxAmount;
        }

        public void setTaxAmount(BigDecimal taxAmount) {
            this.taxAmount = taxAmount;
        }
    }

    public class TotalHTML extends HTML {
        private final String text;

        public TotalHTML(String text) {
            this.text = text;
            setStyleName("totalBold");
            setText(text);
        }

        public void setCurrency(String currency) {
            setText(this.text + currency);
        }
    }

    @Override
    public String getPropertyCode() {
        return Constants.EXPENSES_CLAIM;
    }
}
