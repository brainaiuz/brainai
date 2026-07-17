package com.edatasite.workforce.gwt.invoice.client.ui.view.manual;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualTransactionData;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.manualEntry.ManualEntryService;
import com.edatasite.workforce.gwt.accounting.client.rpc.manualEntry.ManualEntryServiceAsync;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.BankAccountTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.RecurringWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CommonLookup;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ExtendedTextArea;
import com.edatasite.workforce.gwt.invoice.client.ui.view.PdfTemplatePanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.*;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Div;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.edatasite.workforce.core.domain.accounting.EdsRFP.DRAFT;
import static com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction.POST;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.*;
import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.VALIDATE_PROJECT_ON_MANUAL_ENTRY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;
import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.EMPLOYEE;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/20/12
 * Time: 6:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddManualEntryView extends FooteredView implements Colapse, FittedContent {

    public static final int DEFAULT_ROWS = 3;
    public static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/yyyy");
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private final boolean hasPermissionToSkipDepartment = Utils.hasPermission(PermissionConstants.SKIP_DEPARTMENT_ITEM_VALIDATION);
    private final ManualEntryServiceAsync manualEntryService = ManualEntryService.App.get();
    private final boolean isDepartmentRelationEnabled = AccountingUtils.get().isEnableAccountingDepartmentRelation();
    private final boolean isProjectEnabled = Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && !Utils.hasGenericAccess(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED);
    private final boolean isProject_To_Head_Enabled = Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && Utils.hasGenericAccess(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED);
    private final boolean isEnabledManualEntryRole = Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MANUAL_ENTRY_ROLE);
    private Integer objectID;
    private DatePicker date;
    private TextBox narration;
    private TextBox reference;
    private TextBox numberTxtBox;
    private BankTransferNumberData transferNumberData;
    private KpiCheckBox memorizedTransaction;
    private TransactionsLookUp memorizedTransactionsLookUp;
    private ProjectLookUp project;

    private CurrencyWidget currencyWidget;
    private HTML debitTotalHTML, creditTotalHTML;
    private HTML debitsBaseTotalHTML, creditsBaseTotalHTML;

    private final HTML totalLabel = new HTML(wfmStrings.total());
    private final HTML baseTotalLabel = new HTML(wfmStrings.total());

    private final boolean isMultiCurrencyEnabled = Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED);

    private KpiCheckBox enableRecurringCheckBox;
    private RecurringWidget recurringWidget;

    private WfmButton2 saveButton, postButton;
    private WfmButton2 submitButton;
    private WfmButton2 approveButton;

    private String status;

    private HashMap<String, Widget> widgetsMap;

    private NewManualTransaction transactionItem;
    private CurrencyItem baseCurrency;

    private FooterUploadPanel footerUploadPanel;

    private EditableTable itemsTable;
    private EditableGrid grid;
    private TotalTable totalsTable;
    private Date conversionDate;
    private PdfTemplatePanel pdfTemplatePanel;
    private NoteHistoryWidget noteHistoryWidget;
    private ChosenApproversWidget approver;
    private ManualTransactionData item;
    private SelectItem selectedProject;
    private final String addManualJournalsView = "add_manual_journals_view_";
    private HashMap<String, CompanyCustomFieldItem> customFieldsMap;
    private LinkedList<String> itemColumns;
    private ColumnConfig[] columnConfigs;
    List<Widget> rightWidgets = new ArrayList<>();
    private DataListBox roles;

    public AddManualEntryView() {
        super("manualadd", accountingStrings.addManualEntry());

    }

    public AddManualEntryView(Integer objectID) {
        super("edit", accountingStrings.editManualEntry());
        this.objectID = objectID;
    }

    public AddManualEntryView(String[] params) {
        super("manualadd", accountingStrings.addManualEntry());
        if (params.length > 1 && "relatedProject".equals(params[1])) {
            ProjectService.App.get().getProjectAsLookupItem(Integer.parseInt(params[2]), new AsyncCallback<SelectItem>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(SelectItem item) {
                    selectedProject = item;
                }
            });

        }
    }

    @Override
    protected Widget onInitialize() {
        currencyWidget = new CurrencyWidget(objectID == null);

        currencyWidget.addListener(() -> {
            totalLabel.setText(accountingMessages.dynamicTotal(currencyWidget.getCurrencyName()));
            if (!baseCurrency.getName().equals(currencyWidget.getCurrencyName())) {
                baseTotalLabel.setVisible(true);
                debitsBaseTotalHTML.setVisible(true);
                creditsBaseTotalHTML.setVisible(true);
            } else {
                baseTotalLabel.setVisible(false);
                debitsBaseTotalHTML.setVisible(false);
                creditsBaseTotalHTML.setVisible(false);
            }
            calculateTotal();
            clearAccountSuggestBoxes();
        });
        currencyWidget.setValidator(() -> {
            if (!isCurrencyRequirementsValid()) {
                Info.show(accountingMessages.theCurrencyOfTheManualEntryMust(), Info.Type.WARNING);
                currencyWidget.setCurrency(baseCurrency.getId());
                return false;
            }
            setCurrencyToAccounts();
            return true;
        });
        currencyWidget.setDatePicker(date);
        currencyWidget.ensureDebugId("add_manual_journals_view_currency");

        loadData(objectID, true, false);

        return null;
    }

    private void initRoles() {
        // get all roles from database.
        AllInOneService.App.get().getAllRoles(new AsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<SelectItem> selectItems) {
                roles.setItems(selectItems.toArray(new SelectItem[]{}));
            }
        });
    }

    private void clearAccountSuggestBoxes() {
        for (int i = 0; i < grid.getRowCount(); i++) {
            AccountsLookUp account = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
            if (account != null) {
                AccountItem selectedData = account.getSelectedData();
                if (selectedData == null ||
                        (!baseCurrency.getId().equals(currencyWidget.getCurrencyID())
                                && !baseCurrency.getId().equals(selectedData.getCurrencyID())
                                && !currencyWidget.getCurrencyID().equals(selectedData.getCurrencyID()))) {
                    account.clearAndClearItems();
                }
            }
        }
    }

    private void createWidgets() {
        widgetsMap = new HashMap<>();
        narration = new TextBox(true);
        narration.ensureDebugId(addManualJournalsView + "narration");
        date = new DatePicker(true);
        date.ensureDebugId(addManualJournalsView + "date");
        date.addChangeHandler(changeEvent -> {
            if (transferNumberData != null && transferNumberData.isWithDate()) {
                transferNumberData.setDate(dateFormat.format(date.getDate()));
                String[] numberParts = numberTxtBox.getText().split("-"); //MT0001 or MT0001-05/2015
                numberTxtBox.setText(numberParts[0] + "-" + transferNumberData.getDate());
            }
        });
        noteHistoryWidget = new NoteHistoryWidget(callback -> manualEntryService.getManualJournalHistoryNote(objectID, callback));
        numberTxtBox = new TextBox();
        numberTxtBox.ensureDebugId(addManualJournalsView + "number");
//        reference = new TextBox();
        reference = new TextBox(true);
        reference.ensureDebugId(addManualJournalsView + "reference");
        memorizedTransaction = new KpiCheckBox();
        memorizedTransaction.ensureDebugId(addManualJournalsView + "memorizedTransaction");
        memorizedTransactionsLookUp = new TransactionsLookUp();
        memorizedTransactionsLookUp.ensureDebugId(addManualJournalsView + "memorizedTransaction-lookUp");
        memorizedTransactionsLookUp.setAutocompleteOff();
//        memorizedTransactionsLookUp.getSuggestBox().setSize("220px", "30px");
        memorizedTransactionsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            clear();
            rightWidgets.clear();
            loadData(memorizedTransactionsLookUp.getSelectedItemID(), true, true);
        });

        // project
        project = new ProjectLookUp(RECEIVABLE);
        project.ensureDebugId(addManualJournalsView + "project");
        roles = new DataListBox();
        roles.ensureDebugId(addManualJournalsView + "roles");

        enableRecurringCheckBox = new KpiCheckBox();
        enableRecurringCheckBox.ensureDebugId(addManualJournalsView + "reccuring-checkBox");
        recurringWidget = new RecurringWidget(SchedulerConstant.RECURRING_MANUAL_JOURNAL_FORM);
        recurringWidget.ensureDebugId(addManualJournalsView + "recurringFormView");
        recurringWidget.setVisible(false);

        enableRecurringCheckBox.addValueChangeHandler(booleanValueChangeEvent -> onRecurringEnableOrDisable());

        approver = new ChosenApproversWidget(MANUAL_JOURNAL, objectID);

        saveButton = new WfmButton2(wfmStrings.draft(), BTN_DEFAULT_OUTLINE);
        saveButton.ensureDebugId(addManualJournalsView + "save");
        saveButton.addClickHandler(clickEvent -> {
            setEnabledButtons(false);
            status = DRAFT;
            save();
        });

        postButton = new WfmButton2(wfmStrings.post(), WfmButton2.BTN_PRIMARY);
        postButton.ensureDebugId(addManualJournalsView + "post");
        postButton.setVisible(false);
        postButton.addClickHandler(clickEvent -> {
            setEnabledButtons(false);
            status = POST;
            save();
        });

        submitButton = new WfmButton2(wfmStrings.submit(), WfmButton2.BTN_PRIMARY);
        submitButton.ensureDebugId(addManualJournalsView + "save");
        submitButton.addClickHandler(clickEvent -> {
            setEnabledButtons(false);
            status = SUBMITTED;
            save();
        });

        approveButton = new WfmButton2(wfmStrings.approve(), BTN_PRIMARY);
        approveButton.ensureDebugId(addManualJournalsView + "post");
        approveButton.addClickHandler(clickEvent -> {
            setEnabledButtons(false);
            status = APPROVED;
            save();
        });

        if (Utils.isDemoAccount()) {
            saveButton.setEnabled(false);
            postButton.setEnabled(false);
        }

        createTotalTable();
        footerUploadPanel = new FooterUploadPanel(F_MANUAL_TRANSACTION, objectID);
        createItemTable();
        initWidgetsMap();
        initHandlers();
        initRoles();
    }

    private void initHandlers() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddManualEntryView.this, (sender, args) -> {
            if (approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        submitButton.setVisible(true);
                        approveButton.setVisible(false);
                    }
                });
                if (approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        approveButton.setVisible(true);
                        submitButton.setVisible(false);
                    } else {
                        approveButton.setVisible(false);
                        submitButton.setVisible(true);
                    }
                }
            } else {
                approveButton.setVisible(false);
                submitButton.setVisible(true);
            }
        });
    }

    private void onRecurringEnableOrDisable() {
        boolean isRecurringEnabled = enableRecurringCheckBox.getValue();
        recurringWidget.setVisible(isRecurringEnabled);
        numberTxtBox.setEnabled(!isRecurringEnabled);
    }


    private void loadData(final Integer objectID, final boolean addFormAndValidateButtons, boolean isMemorized) {
        LoadingPanel.loading(true);
        manualEntryService.getManualJournalsData(objectID, isMemorized, new AbstractAsyncCallback<ManualTransactionData>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ManualTransactionData result) {
                LoadingPanel.loading(false);
                item = result;
                if (item.getManualTransactionItem() != null && item.getManualTransactionItem().getItemCustomFields() != null) {
                    setItemCustomFields(item.getManualTransactionItem().getItemCustomFields());
                }

                conversionDate = result.getConversionDate();
                baseCurrency = result.getBaseCurrency();
                transactionItem = result.getManualTransactionItem();
                transferNumberData = transactionItem.getTransferNumberData();

                createWidgets();
                Div saveButtonWrapper = new Div();
                saveButtonWrapper.add(saveButton);

                Div postButtonWrapper = new Div();
                postButtonWrapper.add(postButton);

                Div submitButtonWrapper = new Div();
                submitButtonWrapper.add(submitButton);

                Div approveButtonWrapper = new Div();
                approveButtonWrapper.add(approveButton);

                enableRecurringCheckBox.setValue(transactionItem.isRecurringTemplate());
                if (transactionItem.getRecurrenceJobItem() != null) {
                    recurringWidget.setData(transactionItem.getRecurrenceJobItem());
                }
                onRecurringEnableOrDisable();

                totalLabel.setText(accountingMessages.dynamicTotal(transactionItem.getCurrency().getName()));
                baseTotalLabel.setText(accountingMessages.dynamicTotal(baseCurrency.getName()));

                currencyWidget.setCurrency(transactionItem.getCurrency().getId(), transactionItem.getExchangeRate());

                if (!baseCurrency.getName().equals(currencyWidget.getCurrencyName())) {
                    baseTotalLabel.setVisible(true);
                    debitsBaseTotalHTML.setVisible(true);
                    creditsBaseTotalHTML.setVisible(true);
                }

                setFormData(addFormAndValidateButtons);

                initPdfTemplates();

                if (item.isApprover()) {
                    FormGroup approverBox = new FormGroup(wfmStrings.approver(), approver);
                    widgetsMap.put(INPUT_MANAGER, new GColumn(GColumnEnum.COL_4, approverBox));
                }

                if (item.isApprover()) {
                    widgetsMap.put(INPUT_MANAGER, new GColumn(GColumnEnum.COL_4, new FormGroup(wfmStrings.manager(), approver)));
                    if (objectID != null && !item.isApproverSaved()) {
                        approver.reloadApproverWidgets(MANUAL_JOURNAL, null);
                    }
                }

                if (objectID != null) {
                    if (transactionItem.getApprover() != null) {
                        if (transactionItem.getApprover().getId().equals(Utils.getUserID()) && (SUBMITTED.equals(transactionItem.getStatus()) || APPROVED.equals(transactionItem.getStatus()) || REJECTED.equals(transactionItem.getStatus()) || DRAFT.equals(transactionItem.getStatus()))) {
                            approveButton.setVisible(true);
                            rightWidgets.add(approveButtonWrapper);
                        } else if (transactionItem.getApprover().getId().equals(Utils.getUserID()) && POST.equals(transactionItem.getStatus())) {
                            postButton.setVisible(true);
                            rightWidgets.add(postButtonWrapper);
                        } else if (transactionItem.getCreatorItem() != null && transactionItem.getCreatorItem().getId().equals(Utils.getUserID()) && (SUBMITTED.equals(transactionItem.getStatus()) || REJECTED.equals(transactionItem.getStatus()) || DRAFT.equals(transactionItem.getStatus()))) {
                            submitButton.setVisible(true);
                            rightWidgets.add(submitButtonWrapper);
                        }
                    } else {
                        if (APPROVED.equals(result.getManualTransactionItem().getStatus())) {
                            postButton.setVisible(true);
                        }
                        if (POST.equals(result.getManualTransactionItem().getStatus()) || APPROVED.equals(result.getManualTransactionItem().getStatus())) {
                            approveButton.setVisible(false);
                            postButton.setVisible(true);
                            rightWidgets.add(postButtonWrapper);
                        }
                        if (DRAFT.equals(result.getManualTransactionItem().getStatus())&&transactionItem.getCreatorItem().getId().equals(Utils.getUserID())) {
                            postButton.setVisible(true);
                            rightWidgets.add(postButtonWrapper);
                        }
                    }
                    if (DRAFT.equals(result.getManualTransactionItem().getStatus())) {
                        rightWidgets.add(saveButtonWrapper);
                    }
                } else {
                    rightWidgets.add(saveButtonWrapper);
                    if (item.isApprover()) {
                        rightWidgets.add(submitButtonWrapper);
                        rightWidgets.add(approveButtonWrapper);
                    } else {
                        rightWidgets.add(postButtonWrapper);
                        postButton.setVisible(true);
                    }
                }

                if ((status != null && status.equals(POST))) {
                    approveButton.setVisible(false);
                }
                HTMLPanel container = new WftHTMLPanel(result.getLayoutHtml(), widgetsMap).getContainer();
                container.add(createFooter());
                container.setStyleName("add-form ");
                add(container);
                memorizedTransaction.setValue(result.getManualTransactionItem().isMemorizedTransaction());
            }
        });
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return AddManualEntryView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return AddManualEntryView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();
        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);

        informer.setInitialClasses("informer-item history-notes-container");
        footerUploadPanel.setInitialClasses("informer-item history-notes-container");

        leftSideWidgets.add(informer);
        leftSideWidgets.add(footerUploadPanel);

        return leftSideWidgets;
    }

    private List<Widget> getFooterRightSideWidgets() {
        return rightWidgets;
    }

    public void setFormData(boolean setEntryNumber) {

        if (transactionItem.getNarration() != null) {
            narration.setText(transactionItem.getNarration());
        }
        if (transactionItem.getDate() != null) {
            date.setDate(transactionItem.getDate().getNonConvertedDate());
        }
        reference.setText(transactionItem.getReference());
        if (transactionItem.getProject() != null) {
            project.setSelected(transactionItem.getProject());
        } else if (selectedProject != null) {
            project.setSelected(selectedProject);
        }
        if (transactionItem.getRole() != null) {
            roles.setSelected(transactionItem.getRole());
        }
        boolean isRecurringEnabled = enableRecurringCheckBox.getValue();
        if (setEntryNumber) {
            if (transactionItem.getIntNumber() != null) {
                numberTxtBox.setText(transactionItem.getNumber());
            } else if (transactionItem.getObjectId() != null && !isRecurringEnabled) {
                numberTxtBox.setText(transferNumberData.getTransferNumber());
            } else {
                numberTxtBox.setText(transactionItem.getNumber());

            }
            if (objectID != null && transferNumberData != null) {
                String dateString = transactionItem.getDate() != null ? dateFormat.format(transactionItem.getDate().getDate()) : "";
                transferNumberData.setWithDate(transactionItem.getNumber() != null && transactionItem.getNumber().contains(dateString));
                transferNumberData.setDate(transferNumberData.isWithDate() ? dateString : "");
            }
        }
        memorizedTransaction.setValue(transactionItem.isMemorizedTransaction());

        itemsTable.removeAllRows();
        if (transactionItem.getItems() != null && transactionItem.getItems().length > 0) {
            for (int i = 0; i < transactionItem.getItems().length; i++) {
                itemsTable.addRow(getWidgetArray(transactionItem.getItems()[i]));
            }
            if (transactionItem.getItems().length < DEFAULT_ROWS) {
                for (int i = transactionItem.getItems().length; i < DEFAULT_ROWS; i++) {
                    itemsTable.addRow(getWidgetArray(null));
                }
            }
        } else {
            for (int i = 0; i < DEFAULT_ROWS; i++) {
                Widget[] widgetArray = getWidgetArray(null);
                if (widgetArray != null && widgetArray.length == columnConfigs.length) {
                    itemsTable.addRow(getWidgetArray(null));
                } else {
                    Info.warn("Please, fill out \"Width\" fields in Settings -> Customization -> Item Table -> Manual Entry !", 5500);
                }
            }
        }
        calculateTotal();
    }

    private void initPdfTemplates() {
        if (transactionItem.getPdfTemplateList() != null && transactionItem.getPdfTemplateList().getItems() != null && transactionItem.getPdfTemplateList().getItems().length > 0) {
            pdfTemplatePanel = new PdfTemplatePanel(transactionItem);
            FormGroup pdfTemplateBox = new FormGroup(accountingStrings.pdfTemplate(), pdfTemplatePanel);
            widgetsMap.put(INPUT_PDF_TEMPLATE, pdfTemplateBox);
        }
    }

    private void setItemCustomFields(List<CompanyCustomFieldItem> customFields) {
        if (customFields != null && !customFields.isEmpty()) {
            customFieldsMap = new HashMap<>();

            for (CompanyCustomFieldItem field : customFields) {
                customFieldsMap.put(field.getColumnCode(), field);
            }
        }
    }

    private void createItemTable() {
        columnConfigs = getColumns();
        itemsTable = new EditableTable(columnConfigs, true, true);
        itemsTable.setDraggable(true);
        itemsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                itemsTable.addRow(getWidgetArray(null));
            }

            @Override
            public void removeRow() {
                calculateTotal();
            }
        });
        grid = itemsTable.getGrid();
    }

    private Widget[] getWidgetArray(Object object) {
        LinkedHashMap<String, Widget> widgetsMap = getWidgetsMap(object);
        return widgetsMap.values().toArray(new Widget[]{});
    }

    private LinkedHashMap<String, Widget> getWidgetsMap(Object object) {
        LinkedHashMap<String, Widget> widgetsMap = new LinkedHashMap<>();
        NewManualTransactionItem manualItem = object != null ? (NewManualTransactionItem) object : new NewManualTransactionItem();

        AccountsLookUp accountsLookUp = new AccountsLookUp();

        String typeCode = null;
        if (manualItem.getAccountItem() != null && SALARY_PAYABLE.equals(manualItem.getAccountItem().getAccountKey())) {
            typeCode = EMPLOYEE;
        }
        CommonLookup commonLookup = new CommonLookup(typeCode, true);
        commonLookup.setAutocompleteOff();

        CommonLookup clientLookUp = new CommonLookup(CommonLookup.CUSTOMER, true);

        ProjectLookUp projectLookUp = new ProjectLookUp(RECEIVABLE);
        projectLookUp.setAutocompleteOff();

        CustomCellTextBox debit = new CustomCellTextBox();
        CustomCellTextBox credit = new CustomCellTextBox();

        for (String column : itemColumns) {
            switch (column) {
                case ItemTableConstants.ACCOUNT:
                    accountsLookUp.setCurrencyID(currencyWidget.getCurrencyID());
                    accountsLookUp.setFormId("MANUAL_ENTRY");
                    accountsLookUp.ensureDebugId(addManualJournalsView + "account");

                    accountsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                        AccountItem selectedData = accountsLookUp.getSelectedData();

                        if (!isCurrencyRequirementsValid()) {
                            accountsLookUp.clear();
                            Info.show(accountingMessages.onlyOneForeignCurrencyAllowed(), Info.Type.INFO);
                            return;
                        }
                        String accountTypeCode = selectedData.getAccountTypeCode();
                        if (BankAccountTypeEnum.COST_OF_SALES.getCode().equals(accountTypeCode) ||
                                BankAccountTypeEnum.DIRECT_EXPENSES.getCode().equals(accountTypeCode) ||
                                BankAccountTypeEnum.CURRENT_ASSET.getCode().equals(accountTypeCode) ||
                                BankAccountTypeEnum.OVERHEAD.getCode().equals(accountTypeCode)) {
                            clientLookUp.setEnabled(true);
                        } else {
                            clientLookUp.clear();
                            clientLookUp.setEnabled(false);
                        }

                        Integer accountKey = selectedData.getAccountKey();
                        if (ACCOUNTS_RECEIVABLE_KEY.equals(accountKey)) {
                            commonLookup.setTypeCode(CommonLookup.CUSTOMER);
                            projectLookUp.setType(RECEIVABLE);
                        } else if (ACCOUNTS_PAYABLE_KEY.equals(accountKey)) {
                            commonLookup.setTypeCode(CommonLookup.SUPPLIER);
                            projectLookUp.setType(PAYABLE);
                        } else if (SALARY_PAYABLE.equals(accountKey)) {
                            commonLookup.setTypeCode(CommonLookup.EMPLOYEE);
                            projectLookUp.setType(null);
                        } else if (VAT_LIABILITY_KEY.equals(accountKey)) {
                            commonLookup.setTypeCode(null);
                            projectLookUp.setType(null);
                        } else {
                            commonLookup.setTypeCode(null);
                            projectLookUp.setType(null);
                        }
                        if (isProjectEnabled) {
                            projectLookUp.clearOracleItems();
                            projectLookUp.clearAndClearItems();
                            projectLookUp.clearLaters();
                            LookUpCell projectCell = (LookUpCell) itemsTable.getColumnCellWidgetById(grid.getCurrentRow(), ItemTableConstants.PROJECT);
                            projectCell.InActive();
                        }

                        commonLookup.clearOracleItems();
                        commonLookup.clearAndClearItems();
                        commonLookup.clearLaters();
                        LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(grid.getCurrentRow(), ItemTableConstants.NAME);
                        lookUpCell.InActive();
                    });

                    if (manualItem.getAccountItem() != null) {
                        accountsLookUp.addAccountItem(manualItem.getAccountItem());
                    }
                    accountsLookUp.setAutocompleteOff();
                    widgetsMap.put(ItemTableConstants.ACCOUNT, accountsLookUp);
                    break;
                case ItemTableConstants.DEBIT:
                    debit.ensureDebugId(addManualJournalsView + "debit");
                    debit.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                    debit.setMaxLength(16);
                    debit.setText(AccountingUtils.getZero());
                    Validation.addNumericKeyboardListener(debit, AccountingUtils.calculationScale, false, true);
                    addFocusListener(debit, AccountingUtils.getZero());

                    if (manualItem.getDebit() != null) {
                        debit.setText(AccountingUtils.get().formatPrice(manualItem.getDebit()));
                    }
                    debit.addKeyDownHandler(keyDownEvent -> {
                        credit.setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
                        itemsTable.refreshCustomCellDisplayValue(itemsTable.getGrid().getCurrentRow(), ItemTableConstants.CREDIT);
                    });
                    widgetsMap.put(ItemTableConstants.DEBIT, debit);
                    break;
                case ItemTableConstants.CREDIT:
                    credit.ensureDebugId(addManualJournalsView + "credit");
                    credit.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                    credit.setText(AccountingUtils.getZero());
                    credit.setMaxLength(16);
                    Validation.addNumericKeyboardListener(credit, AccountingUtils.calculationScale, false, true);
                    addFocusListener(credit, AccountingUtils.getZero());


                    if (manualItem.getCredit() != null) {
                        credit.setText(AccountingUtils.get().formatPrice(manualItem.getCredit()));
                    }
                    credit.addKeyDownHandler(keyDownEvent -> {
                        debit.setText(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
                        itemsTable.refreshCustomCellDisplayValue(itemsTable.getGrid().getCurrentRow(), ItemTableConstants.DEBIT);
                    });
                    widgetsMap.put(ItemTableConstants.CREDIT, credit);
                    break;
                case ItemTableConstants.DESCRIPTION:
                    ExtendedTextArea description = new ExtendedTextArea();
                    description.setAlignment(TextBoxBase.TextAlignment.LEFT);
                    description.setText(manualItem.getDescription());
                    widgetsMap.put(ItemTableConstants.DESCRIPTION, description);
                    break;
                case ItemTableConstants.NAME:
                    commonLookup.ensureDebugId(addManualJournalsView + "crmAccountLookUp");

                    if (EMPLOYEE.equals(typeCode)) {
                        if (manualItem.getEmployee() != null) {
                            commonLookup.setSelected(manualItem.getEmployee());
                            commonLookup.setEnabled(true);
                        }
                    } else {
                        if (manualItem.getCustomerOrSupplier() != null) {
                            commonLookup.setSelected(manualItem.getCustomerOrSupplier());
                            commonLookup.setEnabled(true);
                        }
                    }
                    widgetsMap.put(ItemTableConstants.NAME, commonLookup);
                    break;
                case ItemTableConstants.BILLING:
                    clientLookUp.ensureDebugId(addManualJournalsView + "clientLookUp");
                    clientLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
                        AccountItem selectedData = accountsLookUp.getSelectedData();
                        Integer accountKey = selectedData.getAccountKey();

                        if (ACCOUNTS_PAYABLE_KEY.equals(accountKey)) {
                            projectLookUp.setType(PAYABLE);
                            projectLookUp.clear();
                        } else if (ACCOUNTS_RECEIVABLE_KEY.equals(accountKey)) {
                            projectLookUp.setType(RECEIVABLE);
                            projectLookUp.setClientSupplierID(commonLookup.getSelectedItemID());
                            projectLookUp.clear();
                        }
                    });
                    if (manualItem.getClient() != null) {
                        clientLookUp.setSelected(manualItem.getClient());
                        projectLookUp.setClientSupplierID(clientLookUp.getSelectedItemID());
                    }
                    widgetsMap.put(ItemTableConstants.BILLING, clientLookUp);
                    break;
                case ItemTableConstants.PROJECT:
                    if (isProjectEnabled) {
                        if (manualItem.getProject() != null) {
                            projectLookUp.setSelected(manualItem.getProject());
                        }
                        if (selectedProject != null) {
                            projectLookUp.setSelected(selectedProject);
                        }
                        projectLookUp.ensureDebugId(addManualJournalsView + "projectLookUp");
                        widgetsMap.put(ItemTableConstants.PROJECT, projectLookUp);
                    }
                    break;
                case ItemTableConstants.DEPARTMENT:
                    if (isDepartmentRelationEnabled) {
                        DepartmentLookUp departmentLookUp = new DepartmentLookUp();
                        departmentLookUp.ensureDebugId(addManualJournalsView + "departmentLookUp");
                        if (manualItem.getDepartment() != null) {
                            departmentLookUp.setSelected(manualItem.getDepartment());
                        }
                        widgetsMap.put(ItemTableConstants.DEPARTMENT, departmentLookUp);
                    }
                    break;
                default:
                    if (customFieldsMap != null && customFieldsMap.get(column) != null) {
                        CompanyCustomFieldItem fieldItem = customFieldsMap.get(column).cloneObject();


                        if (Constants.UI_TYPE_TEXTBOX.equals(fieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType()) || Constants.UI_TYPE_URL.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomTextBoxField(fieldItem));
                        } else if (Constants.UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomPercentageField(fieldItem));
                        } else if (Constants.UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomDropDownField(fieldItem));
                        } else if (Constants.UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomDatePicker(fieldItem));
                        } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomDateTime(fieldItem));
                        } else if (Constants.UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomTextAreaField(fieldItem));
                        } else if (Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                            widgetsMap.put(column, new CustomFieldLookUpField(fieldItem));
                        }

                        if (item != null && item.getManualTransactionItem() != null && item.getManualTransactionItem().getItemCustomFields() != null && !item.getManualTransactionItem().getItemCustomFields().isEmpty()) {
                            for (String field : customFieldsMap.keySet()) {
                                if (widgetsMap.get(field) != null && manualItem.getCustomFieldByCode(field) != null) {
                                    ((CustomFieldInterface) widgetsMap.get(field)).setFieldItem(manualItem.getCustomFieldByCode(field));
                                }
                            }
                        }
                    }
                    break;
            }
        }
        return widgetsMap;
    }

    private ColumnConfig[] getColumns() {
        itemColumns = new LinkedList<>();
        LinkedList<ColumnConfig> columnsList = new LinkedList<>();
        if (item.getManualTransactionItem() != null && item.getManualTransactionItem().getCustomItemColumns() != null && item.getManualTransactionItem().getCustomItemColumns().length > 0) {
            ColumnConfig columnConfig;
            for (ColumnConfigs column : item.getManualTransactionItem().getCustomItemColumns()) {
                boolean isPixel = (column.getWidth() == null || column.getWidth() == 0);
                switch (column.getCode()) {
                    case ItemTableConstants.ACCOUNT:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.ACCOUNT, column.isChanged() ? column.getTitle() : wfmStrings.account(), Utils.getColumnWidth(column.getWidth(), 200), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.ACCOUNT);
                        break;
                    case ItemTableConstants.DEBIT:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DEBIT, column.isChanged() ? column.getTitle() : wfmStrings.debit(), Utils.getColumnWidth(column.getWidth(), 80), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.DEBIT);
                        break;
                    case ItemTableConstants.CREDIT:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.CREDIT, column.isChanged() ? column.getTitle() : wfmStrings.credit(), Utils.getColumnWidth(column.getWidth(), 80), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.CREDIT);
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        columnConfig = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, column.isChanged() ? column.getTitle() : wfmStrings.description(), Utils.getColumnWidth(column.getWidth(), 250), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.DESCRIPTION);
                        break;
                    case ItemTableConstants.NAME:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.NAME, column.isChanged() ? column.getTitle() : wfmStrings.name(), Utils.getColumnWidth(column.getWidth(), 120), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.NAME);
                        break;
                    case ItemTableConstants.BILLING:
                        columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.BILLING, column.isChanged() ? column.getTitle() : accountingStrings.billing(), Utils.getColumnWidth(column.getWidth(), 120), column.isRequired());
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnConfig.setChanged(column.isChanged());
                        columnsList.add(columnConfig);
                        itemColumns.add(ItemTableConstants.BILLING);
                        break;
                    case ItemTableConstants.PROJECT:
                        if (isProjectEnabled) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.PROJECT, column.isChanged() ? column.getTitle() : wfmStrings.project(), Utils.getColumnWidth(column.getWidth(), 120), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnConfig.setChanged(column.isChanged());
                            columnsList.add(columnConfig);
                            itemColumns.add(ItemTableConstants.PROJECT);
                        }
                        break;
                    case ItemTableConstants.DEPARTMENT:
                        if (isDepartmentRelationEnabled) {
                            columnConfig = new ColumnConfig(LookUpCell.class, ItemTableConstants.DEPARTMENT, column.isChanged() ? column.getTitle() : wfmStrings.department(), Utils.getColumnWidth(column.getWidth(), 138), column.isRequired());
                            columnConfig.setPixel(isPixel);
                            columnConfig.setForceWidthInPercent(!isPixel);
                            columnConfig.setChanged(column.isChanged());
                            columnsList.add(columnConfig);
                            itemColumns.add(ItemTableConstants.DEPARTMENT);
                        }
                        break;
                    default:
                        if (column.getCode() != null && column.getCode().contains("date_value")) {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 165));
                        } else {
                            columnConfig = new ColumnConfig(CustomCell.class, column.getCode(), column.getTitle(), Utils.getColumnWidth(column.getWidth(), 100));
                        }
                        columnConfig.setPixel(isPixel);
                        columnConfig.setForceWidthInPercent(!isPixel);
                        columnsList.add(columnConfig);
                        itemColumns.add(column.getCode());
                        break;

                }
            }
        } else {
            columnsList.add(new ColumnConfig(LookUpCell.class, ItemTableConstants.ACCOUNT, wfmStrings.account(), 200, true));
            itemColumns.add(ItemTableConstants.ACCOUNT);

            columnsList.add(new ColumnConfig(CustomCell.class, ItemTableConstants.DEBIT, wfmStrings.debit(), 80, true));
            itemColumns.add(ItemTableConstants.DEBIT);

            columnsList.add(new ColumnConfig(CustomCell.class, ItemTableConstants.CREDIT, wfmStrings.credit(), 80, true));
            itemColumns.add(ItemTableConstants.CREDIT);

            columnsList.add(new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, wfmStrings.description(), 250, false));
            itemColumns.add(ItemTableConstants.DESCRIPTION);

            columnsList.add(new ColumnConfig(LookUpCell.class, ItemTableConstants.NAME, wfmStrings.name(), 120, false));
            itemColumns.add(ItemTableConstants.NAME);

            columnsList.add(new ColumnConfig(LookUpCell.class, ItemTableConstants.BILLING, accountingStrings.billing(), 120, false));
            itemColumns.add(ItemTableConstants.BILLING);

            if (isProjectEnabled) {
                columnsList.add(new ColumnConfig(LookUpCell.class, ItemTableConstants.PROJECT, wfmStrings.project(), 120, false));
                itemColumns.add(ItemTableConstants.PROJECT);
            }

            if (isDepartmentRelationEnabled) {
                columnsList.add(new ColumnConfig(LookUpCell.class, ItemTableConstants.DEPARTMENT, wfmStrings.department(), 138, !hasPermissionToSkipDepartment));
                itemColumns.add(ItemTableConstants.DEPARTMENT);
            }
        }
        return columnsList.toArray(new ColumnConfig[]{});
    }

    private void createTotalTable() {
        HTML debitLabel = new HTML(wfmStrings.debit());
        HTML creditLabel = new HTML(wfmStrings.credit());

        debitLabel.setStyleName(STYLE_TOTAL_LABEL);
        creditLabel.setStyleName(STYLE_TOTAL_LABEL);
        totalLabel.setStyleName(STYLE_TOTAL_LABEL);
        baseTotalLabel.setStyleName(STYLE_TOTAL_LABEL);

        debitTotalHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        debitTotalHTML.ensureDebugId(addManualJournalsView + "debitTotal");
        creditTotalHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        creditTotalHTML.ensureDebugId(addManualJournalsView + "creditTotal");
        debitsBaseTotalHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        debitsBaseTotalHTML.ensureDebugId(addManualJournalsView + "debitBaseTotal");
        creditsBaseTotalHTML = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        creditsBaseTotalHTML.ensureDebugId(addManualJournalsView + "creditBaseTotal");

        baseTotalLabel.setVisible(false);
        debitsBaseTotalHTML.setVisible(false);
        creditsBaseTotalHTML.setVisible(false);

        debitTotalHTML.setStyleName(STYLE_TOTAL_VALUE);
        creditTotalHTML.setStyleName(STYLE_TOTAL_VALUE);
        debitsBaseTotalHTML.setStyleName(STYLE_TOTAL_VALUE);
        creditsBaseTotalHTML.setStyleName(STYLE_TOTAL_VALUE);

        totalsTable = new TotalTable();
        totalsTable.addWidgetsInARow(new HTML(""), debitLabel, creditLabel);
        totalsTable.addWidgetsInARow(totalLabel, debitTotalHTML, creditTotalHTML);
        totalsTable.addWidgetsInARow(baseTotalLabel, debitsBaseTotalHTML, creditsBaseTotalHTML);
    }

    private void calculateTotal() {
        BigDecimal debitTotal = ZERO, creditTotal = ZERO;

        //Debit
        for (int i = 0; i < grid.getRowCount(); i++) {
            CustomCellTextBox debit = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.DEBIT);
            if (!"".equals(debit.getText())) {
                BigDecimal debitValue = AccountingUtils.get().parseToBigDecimal(debit.getText());
                debitTotal = debitTotal.add(debitValue);
            }
        }
        //Credit
        for (int i = 0; i < grid.getRowCount(); i++) {
            CustomCellTextBox credit = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.CREDIT);
            if (!"".equals(credit.getText())) {
                BigDecimal creditValue = AccountingUtils.get().parseToBigDecimal(credit.getText());
                creditTotal = creditTotal.add(creditValue);
            }
        }
        debitTotalHTML.setHTML(AccountingUtils.get().formatPrice(debitTotal));
        creditTotalHTML.setHTML(AccountingUtils.get().formatPrice(creditTotal));

        BigDecimal exchangeRate = currencyWidget.getExchangeRate();

        debitsBaseTotalHTML.setHTML(AccountingUtils.get().formatPrice(debitTotal.divide(exchangeRate, AccountingUtils.calculationScale, RoundingMode.HALF_UP)));
        creditsBaseTotalHTML.setHTML(AccountingUtils.get().formatPrice(creditTotal.divide(exchangeRate, AccountingUtils.calculationScale, RoundingMode.HALF_UP)));

//        totalsTable.setWidget(1, 1, debitTotalHTML);  //TODO test if totals value are updated on calcaulateTotal method call
//        totalsTable.setWidget(1, 2, creditTotalHTML); //TODO test if totals value are updated on calcaulateTotal method call
    }

    private void initWidgetsMap() {
        widgetsMap.put(LABEL_TITLE, new HTML(wfmStrings.manualEntry()));

        widgetsMap.put(INPUT_DATE, new FormGroup(wfmStrings.date(), wrapWidgetToFormControl(date)));
        widgetsMap.put(INPUT_INTRODUCTION, new FormGroup(wfmStrings.narration(), wrapWidgetToFormControl(narration)));
        widgetsMap.put(INPUT_ITEM_TABLE, itemsTable);
        widgetsMap.put(INPUT_TOTALS_TABLE, totalsTable);
        widgetsMap.put(INPUT_REFERENCE, new FormGroup(wfmStrings.reference(), reference));
        widgetsMap.put(INPUT_NUMBER, new FormGroup(wfmStrings.number(), numberTxtBox, true));
        if (isProject_To_Head_Enabled) {
            widgetsMap.put(INPUT_PROJECT, new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), project));
        }
        if (isEnabledManualEntryRole) {
            widgetsMap.put(INPUT_ROLE, new FormGroup(wfmStrings.role(), roles));
        }

        Div inputGroup = new Div("input-group");
        Div prepend = new Div("input-group-prepend");
        inputGroup.add(prepend);

        Div prependedContent = new Div("input-group-text");
        prependedContent.add(memorizedTransaction);
        prepend.add(prependedContent);

        inputGroup.add(memorizedTransactionsLookUp);

        FormGroup memorizedTsctnField = new FormGroup(wfmStrings.memorizedTransaction(), inputGroup);
        widgetsMap.put(INPUT_MEMORIZED_TRANSACTION_LIST, memorizedTsctnField);

        widgetsMap.put(INPUT_EXCHANGE_RATE, new FormGroup(wfmStrings.currency(), currencyWidget));

        Div inputGroup2 = new Div("input-group");
        Div prepend2 = new Div();
        inputGroup2.add(prepend2);

        Div prependedContent2 = new Div("input-group-text");
        prependedContent2.add(enableRecurringCheckBox);
        prepend2.add(prependedContent2);

        FormGroup recurringField = new FormGroup(wfmStrings.recurring(), inputGroup2);
        widgetsMap.put(INPUT_ENABLE_RECURRING, recurringField);

        widgetsMap.put(INPUT_RECURRING_VIEW, recurringWidget);

        HTML notesLabel = new HTML(wfmStrings.notes());
        notesLabel.setStyleName(STYLE_OPTION_BAR_TITLE);
        widgetsMap.put(LABEL_NOTES, notesLabel);
    }

    private void setEnabledButtons(boolean b) {
        if (saveButton != null) {
            saveButton.setEnabled(b);
        }
        if (postButton != null) {
            postButton.setEnabled(b);
        }
        if (approveButton != null) {
            approveButton.setEnabled(b);
        }
        if (submitButton != null) {
            submitButton.setEnabled(b);
        }
    }

    private boolean validate() {
        int tableErrors = 0;
        int errors = 0;
        if (date.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(date.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.manualEntry(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }
        if (!Validation.validateTextBoxRequired(narration)) {
            errors++;
        }
        if (!Validation.validateDate(date, new HTML(), true)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(numberTxtBox)) {
            errors++;
        }

        if (enableRecurringCheckBox.getValue() && recurringWidget != null && !recurringWidget.validate()) {
            errors++;
        }
        if (item.isApprover()) {
            if (!approver.isValid()) {
                errors++;
            }
        }

        if (!validateItemsTable()) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }

        if (!isCurrencyRequirementsValid()) {
            WfmWindow.alert(accountingMessages.onlyOneForeignCurrencyAllowed());
            return false;
        }

        BigDecimal debitTotal = AccountingUtils.get().parseToBigDecimal(debitTotalHTML.getText());
        BigDecimal creditTotal = AccountingUtils.get().parseToBigDecimal(creditTotalHTML.getText());
        if (debitTotal.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP).compareTo(creditTotal.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP)) != 0) {
            Info.show(accountingStrings.totalDebitsMustEqualTotalCredits(), Info.Type.WARNING);
            return false;
        }
        if (conversionDate != null && date != null && date.getDate() != null && conversionDate.after(date.getDate())) {
            Info.show("Transaction date should be after conversion date.", Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private boolean validateItemsTable() {
        itemsTable.setValidRows(0);
        List<CompanyCustomFieldItem> requiredAndEmailCFs = new ArrayList<>();

        for (ColumnConfig config : columnConfigs) {
            if (customFieldsMap != null && customFieldsMap.containsKey(config.getName()) && (customFieldsMap.get(config.getName()).isRequired() ||
                    (UI_TYPE_TEXTBOX_EMAIL.equals(customFieldsMap.get(config.getName()).getUiType())) ||
                    (UI_TYPE_PERCENTAGE.equals(customFieldsMap.get(config.getName()).getUiType())))) {
                requiredAndEmailCFs.add(customFieldsMap.get(config.getName()));
            }
        }


        boolean errorFound = false;
        ArrayList<String> requiredColumnCodes = new ArrayList<>();
        int requiredRow = 0;
        if (item.getManualTransactionItem() != null && item.getManualTransactionItem().getCustomItemColumns() != null && item.getManualTransactionItem().getCustomItemColumns().length > 0) {
            for (ColumnConfigs columnConfigs : item.getManualTransactionItem().getCustomItemColumns()) {
                if (columnConfigs != null && columnConfigs.isRequired() && columnConfigs.getCompanyCustomFieldID() == null) {
                    requiredRow++;
                    requiredColumnCodes.add(columnConfigs.getCode());
                }
            }
        } else {
            requiredRow = isDepartmentRelationEnabled && !hasPermissionToSkipDepartment ? 4 : 3;
        }

        for (int i = 0; i < grid.getRowCount(); i++) {

            int rowError = 0;
            itemsTable.resetValidation(i);
            rowError = validateRequiredItems(i, requiredAndEmailCFs, requiredColumnCodes)[0];

            if (rowError == 0) {
                itemsTable.setItemValid(i, true);
                itemsTable.incValidRow();
            } else if (rowError == requiredRow + requiredAndEmailCFs.size()) {
                if (!areOtherRowsAffected(i)) {
                    itemsTable.setItemValid(i, false);
                } else {
                    colorizeErrorField(i, requiredAndEmailCFs, requiredColumnCodes);
                    errorFound = true;
                }
            } else {
                colorizeErrorField(i, requiredAndEmailCFs, requiredColumnCodes);
                errorFound = true;
            }
            if (itemsTable.getValidRows() == 0) {
                colorizeErrorField(0, requiredAndEmailCFs, requiredColumnCodes);
                errorFound = true;
            }

            if (Utils.hasGenericAccess(GenericSettingsEnum.VALIDATE_PROJECT_ON_MANUAL_ENTRY)) {
                if (((ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT)).getSelectedItemID() == null) {
                    colorizeErrorField(i, requiredAndEmailCFs, requiredColumnCodes);
                }
            }
        }
        if (customFieldsMap != null && customFieldsMap.values().size() > 0) {
            return !errorFound && Validation.itemTableNumericCFMinValueValidate(itemsTable, customFieldsMap.values());
        } else {
            return !errorFound;
        }
    }

    private boolean areOtherRowsAffected(int i) {
        boolean result = false;

        AccountsLookUp accountsLookUp = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
        CustomCellTextBox debit = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.DEBIT);
        CustomCellTextBox credit = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.CREDIT);

        ExtendedTextArea description = (ExtendedTextArea) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
        CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
        CommonLookup clientLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.BILLING);
        ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);
        DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemsTable.getColumnById(i, ItemTableConstants.DEPARTMENT);

        result |= accountsLookUp != null && (accountsLookUp.getSelectedItem() != null && accountsLookUp.getSelectedItem().getId() != null);
        result |= !("".equals(debit.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(debit.getText())) == 0);
        result |= !("".equals(credit.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(credit.getText())) == 0);
        result |= description != null && (description.getText() != null && !"".equals(description.getText().trim()));
        result |= commonLookup != null && (commonLookup.getSelectedItem() != null && commonLookup.getSelectedItem().getId() != null);
        result |= clientLookup != null && (clientLookup.getSelectedItem() != null && clientLookup.getSelectedItem().getId() != null);
        result |= projectLookUp != null && (projectLookUp.getSelectedItem() != null && projectLookUp.getSelectedItem().getId() != null);
        result |= departmentLookUp != null && (departmentLookUp.getSelectedItem() != null && departmentLookUp.getSelectedItem().getId() != null);

        return result;
    }

    private void colorizeErrorField(int i, List<CompanyCustomFieldItem> requiredAndEmailCFs, ArrayList<String> requiredColumnCodes) {

        AccountsLookUp accountsLookUp = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
        CustomCellTextBox debit = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.DEBIT);
        CustomCellTextBox credit = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.CREDIT);

        if (requiredColumnCodes.isEmpty()) {
            if (!Validation.validateLookUpRequired(accountsLookUp)) {
                itemsTable.notValid(i, ItemTableConstants.ACCOUNT);
            }

            AccountItem accountItem = accountsLookUp.getSelectedData();
            if (accountItem != null && (ACCOUNTS_RECEIVABLE_KEY.equals(accountItem.getAccountKey()) || ACCOUNTS_PAYABLE_KEY.equals(accountItem.getAccountKey()))) {
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);

                if (!Validation.validateLookUpRequired(commonLookup)) {
                    itemsTable.notValid(i, ItemTableConstants.NAME);
                }
            }
            if (isDepartmentRelationEnabled && !hasPermissionToSkipDepartment) {
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemsTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
                if (!Validation.validateLookUpRequired(departmentLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.DEPARTMENT);
                    LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.DEPARTMENT);
                    if (lookUpCell != null) {
                        lookUpCell.InActive();
                    }
                }
            }

            boolean db = ("".equals(debit.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(debit.getText())) == 0);
            boolean cr = ("".equals(credit.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(credit.getText())) == 0);
            if (db && cr) {
                itemsTable.notValid(i, ItemTableConstants.CREDIT);
                itemsTable.notValid(i, ItemTableConstants.DEBIT);
            }
            if (Utils.hasGenericAccess(VALIDATE_PROJECT_ON_MANUAL_ENTRY) && accountItem != null && EXPENSES.equals(accountItem.getAccountTypeCategory())) {
                ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);
                if (!Validation.validateLookUpRequired(projectLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.PROJECT);
                }
            }
        } else {
            if (!Validation.validateLookUpRequired(accountsLookUp) && requiredColumnCodes.contains(ItemTableConstants.ACCOUNT)) {
                itemsTable.notValid(i, ItemTableConstants.ACCOUNT);
            }

            AccountItem accountItem = accountsLookUp.getSelectedData();
            if (accountItem != null && (ACCOUNTS_RECEIVABLE_KEY.equals(accountItem.getAccountKey()) || ACCOUNTS_PAYABLE_KEY.equals(accountItem.getAccountKey())) && requiredColumnCodes.contains(ItemTableConstants.NAME)) {
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
                if (!Validation.validateLookUpRequired(commonLookup)) {
                    itemsTable.notValid(i, ItemTableConstants.NAME);
                }
            }

            if (isDepartmentRelationEnabled && !hasPermissionToSkipDepartment && requiredColumnCodes.contains(ItemTableConstants.DEPARTMENT)) {
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemsTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
                if (!Validation.validateLookUpRequired(departmentLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.DEPARTMENT);
                    LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.DEPARTMENT);
                    if (lookUpCell != null) {
                        lookUpCell.InActive();
                    }
                }
            }

            boolean db = ("".equals(debit.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(debit.getText())) == 0);
            boolean cr = ("".equals(credit.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(credit.getText())) == 0);
            if (db && cr && requiredColumnCodes.contains(ItemTableConstants.DEBIT) && requiredColumnCodes.contains(ItemTableConstants.CREDIT)) {
                itemsTable.notValid(i, ItemTableConstants.DEBIT);
                itemsTable.notValid(i, ItemTableConstants.CREDIT);
            }

            if (requiredColumnCodes.contains(ItemTableConstants.DESCRIPTION)) {
                ExtendedTextArea description = (ExtendedTextArea) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
                if (!Validation.validateTextAreaRequired(description)) {
                    itemsTable.notValid(i, ItemTableConstants.DESCRIPTION);
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.NAME)) {
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
                if (!Validation.validateLookUpRequired(commonLookup)) {
                    itemsTable.notValid(i, ItemTableConstants.NAME);
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.BILLING)) {
                CommonLookup clientLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.BILLING);
                if (!Validation.validateLookUpRequired(clientLookup)) {
                    itemsTable.notValid(i, ItemTableConstants.BILLING);
                }

            }

            if ((isProjectEnabled && requiredColumnCodes.contains(ItemTableConstants.PROJECT)) || (Utils.hasGenericAccess(VALIDATE_PROJECT_ON_MANUAL_ENTRY) && accountItem != null && EXPENSES.equals(accountItem.getAccountTypeCategory()))) {
                ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);
                if (!Validation.validateLookUpRequired(projectLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.PROJECT);
                }
            }
        }

        for (CompanyCustomFieldItem fieldItem : requiredAndEmailCFs) {
            if (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateTextBoxRequired(t)) {
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                CustomPercentageField t = (CustomPercentageField) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateIntegerTextBoxRequired(t)) {
                        itemsTable.notValid(i, fieldItem.getColumnCode());
                    }
                } else {
                    if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                        itemsTable.notValid(i, fieldItem.getColumnCode());
                    }
                }
            } else if (UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateEmailRequired(t)) {
                        itemsTable.notValid(i, fieldItem.getColumnCode());
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText().length() > 0) {
                        if (!Validation.validateEmailRequired(t)) {
                            itemsTable.notValid(i, fieldItem.getColumnCode());
                        }
                    }
                }
            } else if (UI_TYPE_URL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateUrl(t, null)) {
                        itemsTable.notValid(i, fieldItem.getColumnCode());
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText().length() > 0) {
                        if (!Validation.validateUrl(t, null)) {
                            itemsTable.notValid(i, fieldItem.getColumnCode());
                        }
                    }
                }
            } else if (UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                DataListBox t = (DataListBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (t.getSelectedItem() == null) {
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                TextArea t = (TextArea) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (t.getText() == null) {
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                DatePicker t = (DatePicker) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateDate(t)) {
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                DateTimeWidget t = (DateTimeWidget) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateDateTime(t)) {
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldLookUpField t = (CustomFieldLookUpField) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateLookUpRequired(t)) {
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            } else if (UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(ERROR_FORM_STYLE);
                    Utils.scrollIntoView(t.getElement());
                    itemsTable.notValid(i, fieldItem.getColumnCode());
                }
            }
        }
    }

    private int[] validateRequiredItems(int i, List<CompanyCustomFieldItem> requiredAndEmailCFs, ArrayList<String> requiredColumnCodes) {
        int errors = 0;
        int nonRequired = 0;
        int[] error = new int[3];
        LookUpCell accountCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.ACCOUNT);
        AccountsLookUp accountsLookUp = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
        CustomCellTextBox debit = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.DEBIT);
        CustomCellTextBox credit = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.CREDIT);


        if (requiredColumnCodes.isEmpty()) {
            if (accountsLookUp.getSelectedData() == null) {
                accountCell.addStyleName("x-form-invalid");
                accountCell.InActive();
            }
            if (!Validation.validateLookUpRequired(accountsLookUp)) {
                itemsTable.setColumnValid(ItemTableConstants.ACCOUNT);
                errors++;
            }

            AccountItem accountItem = accountsLookUp.getSelectedData();
            if (accountItem != null && (ACCOUNTS_RECEIVABLE_KEY.equals(accountItem.getAccountKey()) || ACCOUNTS_PAYABLE_KEY.equals(accountItem.getAccountKey()))) {
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
                if (!Validation.validateLookUpRequired(commonLookup)) {
                    itemsTable.notValid(i, ItemTableConstants.NAME);
                    errors++;
                }
            }
            if (isDepartmentRelationEnabled && !hasPermissionToSkipDepartment) {
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemsTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
                if (!Validation.validateLookUpRequired(departmentLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.DEPARTMENT);
                    LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.DEPARTMENT);
                    if (lookUpCell != null) {
                        lookUpCell.InActive();
                    }
                    errors++;
                }
            }

            boolean db = ("".equals(debit.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(debit.getText())) == 0);
            boolean cr = ("".equals(credit.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(credit.getText())) == 0);
            if (db && cr) {
                itemsTable.notValid(i, ItemTableConstants.CREDIT);
                itemsTable.notValid(i, ItemTableConstants.DEBIT);
                errors += 2;
            }

            if (Utils.hasGenericAccess(VALIDATE_PROJECT_ON_MANUAL_ENTRY) && accountItem != null && EXPENSES.equals(accountItem.getAccountTypeCategory())) {
                ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);
                if (!Validation.validateLookUpRequired(projectLookUp)) {
                    itemsTable.setColumnValid(ItemTableConstants.PROJECT);
                    errors++;
                }
            }
        } else {
            if (accountsLookUp.getSelectedData() == null && requiredColumnCodes.contains(ItemTableConstants.ACCOUNT)) {
                accountCell.addStyleName("x-form-invalid");
                accountCell.InActive();
            }
            if (!Validation.validateLookUpRequired(accountsLookUp) && requiredColumnCodes.contains(ItemTableConstants.ACCOUNT)) {
                itemsTable.setColumnValid(ItemTableConstants.ACCOUNT);
                errors++;
            }

            AccountItem accountItem = accountsLookUp.getSelectedData();
            if (accountItem != null && (ACCOUNTS_RECEIVABLE_KEY.equals(accountItem.getAccountKey()) || ACCOUNTS_PAYABLE_KEY.equals(accountItem.getAccountKey()))) {
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
                if (!Validation.validateLookUpRequired(commonLookup)) {
                    itemsTable.notValid(i, ItemTableConstants.NAME);
                    errors++;
                }
            }

            if (isDepartmentRelationEnabled && !hasPermissionToSkipDepartment && requiredColumnCodes.contains(ItemTableConstants.DEPARTMENT)) {
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemsTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
                if (!Validation.validateLookUpRequired(departmentLookUp)) {
                    itemsTable.notValid(i, ItemTableConstants.DEPARTMENT);
                    LookUpCell lookUpCell = (LookUpCell) itemsTable.getColumnCellWidgetById(i, ItemTableConstants.DEPARTMENT);
                    if (lookUpCell != null) {
                        lookUpCell.InActive();
                    }
                    errors++;
                }
            }

            boolean db = ("".equals(debit.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(debit.getText())) == 0);
            boolean cr = ("".equals(credit.getText()) || ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(credit.getText())) == 0);
            if (db && cr && requiredColumnCodes.contains(ItemTableConstants.DEBIT) && requiredColumnCodes.contains(ItemTableConstants.CREDIT)) {
                itemsTable.notValid(i, ItemTableConstants.DEBIT);
                itemsTable.notValid(i, ItemTableConstants.CREDIT);
                errors += 2;
            }

            if (requiredColumnCodes.contains(ItemTableConstants.DESCRIPTION)) {
                ExtendedTextArea description = (ExtendedTextArea) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
                if (!Validation.validateTextAreaRequired(description)) {
                    itemsTable.setColumnValid(ItemTableConstants.DESCRIPTION);
                    errors++;
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.NAME)) {
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
                if (!Validation.validateLookUpRequired(commonLookup)) {
                    itemsTable.setColumnValid(ItemTableConstants.NAME);
                    errors++;
                }
            }

            if (requiredColumnCodes.contains(ItemTableConstants.BILLING)) {
                CommonLookup clientLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.BILLING);
                if (!Validation.validateLookUpRequired(clientLookup)) {
                    itemsTable.setColumnValid(ItemTableConstants.BILLING);
                    errors++;
                }

            }

            if ((isProjectEnabled && requiredColumnCodes.contains(ItemTableConstants.PROJECT)) || (Utils.hasGenericAccess(VALIDATE_PROJECT_ON_MANUAL_ENTRY) && accountItem != null && EXPENSES.equals(accountItem.getAccountTypeCategory()))) {
                ProjectLookUp projectLookUp = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);
                if (!Validation.validateLookUpRequired(projectLookUp)) {
                    itemsTable.setColumnValid(ItemTableConstants.PROJECT);
                    errors++;
                }
            }
        }

        for (CompanyCustomFieldItem fieldItem : requiredAndEmailCFs) {
            if (UI_TYPE_TEXTBOX.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateTextBoxRequired(t)) {
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_PERCENTAGE.equals(fieldItem.getUiType())) {
                CustomPercentageField t = (CustomPercentageField) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateIntegerTextBoxRequired(t)) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (t.getText() != null && t.getText().length() > 0 && Double.valueOf(t.getText()).compareTo((double) 100) > 0) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    } else {
                        nonRequired++;
                    }
                }
            } else if (UI_TYPE_TEXTBOX_EMAIL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateEmailRequired(t)) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText().length() > 0) {
                        if (!Validation.validateEmailRequired(t)) {
                            itemsTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    } else {
                        nonRequired++;
                    }
                }
            } else if (UI_TYPE_URL.equals(fieldItem.getUiType())) {
                TextBox t = (TextBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (fieldItem.isRequired()) {
                    if (!Validation.validateUrl(t, null)) {
                        itemsTable.setColumnValid(fieldItem.getColumnCode());
                        errors++;
                    }
                } else {
                    if (!fieldItem.isRequired() && t.getText().length() > 0) {
                        if (!Validation.validateUrl(t, null)) {
                            itemsTable.setColumnValid(fieldItem.getColumnCode());
                            errors++;
                        }
                    } else {
                        nonRequired++;
                    }
                }
            } else if (UI_TYPE_DROPDOWN.equals(fieldItem.getUiType())) {
                DataListBox t = (DataListBox) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (t.getSelectedItem() == null) {
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_TEXTAREA.equals(fieldItem.getUiType())) {
                TextArea t = (TextArea) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (t.getText() == null) {
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_DATEPICKER.equals(fieldItem.getUiType())) {
                DatePicker t = (DatePicker) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateDate(t)) {
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_DATEPICKER_TIME.equals(fieldItem.getUiType())) {
                DateTimeWidget t = (DateTimeWidget) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateDateTime(t)) {
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldLookUpField t = (CustomFieldLookUpField) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (!Validation.validateLookUpRequired(t)) {
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            } else if (UI_TYPE_MULTI_LOOKUP.equals(fieldItem.getUiType())) {
                CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) itemsTable.getColumnById(i, fieldItem.getColumnCode());
                if (t.getSelectedItems() == null || (t.getSelectedItems() != null && t.getSelectedItems().size() == 0)) {
                    t.addStyleName(ERROR_FORM_STYLE);
                    Utils.scrollIntoView(t.getElement());
                    itemsTable.setColumnValid(fieldItem.getColumnCode());
                    errors++;
                }
            }
        }

        error[0] = errors;
        error[1] = nonRequired;
        return error;

    }

    private boolean isCurrencyRequirementsValid() {
        if (isMultiCurrencyEnabled && !baseCurrency.getId().equals(currencyWidget.getCurrencyID())) {
            List<Integer> currencyIDs = new LinkedList<Integer>();
            currencyIDs.add(baseCurrency.getId());
            if (currencyWidget.getCurrencyID() != null && !currencyIDs.contains(currencyWidget.getCurrencyID())) {
                currencyIDs.add(currencyWidget.getCurrencyID());
            }
            int allowedCurrencyLimit = currencyIDs.size();
            for (int i = 0; i < grid.getRowCount(); i++) {
                AccountsLookUp account = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
                AccountItem selectedData = account.getSelectedData();
                if (selectedData != null && selectedData.getCurrencyID() != null) {
                    if (!currencyIDs.contains(selectedData.getCurrencyID())) {
                        currencyIDs.add(selectedData.getCurrencyID());
                    }
                }
            }

            return currencyIDs.size() <= allowedCurrencyLimit;
        }
        return true;
    }

    private void setCurrencyToAccounts() {
        for (int i = 0; i < grid.getRowCount(); i++) {
            AccountsLookUp account = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
            account.setCurrencyID(currencyWidget.getCurrencyID());
        }
    }

    private void save() {
        setEnabledButtons(false);
        if (!validate()) {
            setEnabledButtons(true);
            return;
        }

        saveManualTransaction(true);
    }

    private void saveManualTransaction(boolean checkExistingReference) {
        LoadingPanel.loading(true);

        NewManualTransaction manualTransaction = new NewManualTransaction();
        manualTransaction.setObjectId(objectID);
        manualTransaction.setNarration(narration.getText());
        manualTransaction.setDate(new DateNonConvertable(date.getDate()));

        manualTransaction.setRecurringTemplate(enableRecurringCheckBox.getValue());
        if (manualTransaction.isRecurringTemplate()) {
            manualTransaction.setRecurrenceJobItem(recurringWidget.getData());
        } else {
            if (objectID == null && !"".equals(numberTxtBox.getText()) && numberTxtBox.getText() != null) {
                String prefix = transferNumberData.getPrefix();
                String[] numberParts = numberTxtBox.getText().split("-"); //MT0001 or MT0001-05/2015
                String fourDigitNumber = prefix.contains("-") && numberParts.length > 1 ? numberParts[1] : numberParts[0].substring(prefix.length());
                manualTransaction.setIntNumber(Integer.valueOf(fourDigitNumber));
            }
            manualTransaction.setNumber(numberTxtBox.getText());
            //manualTransaction.setNumberData(numberWidget.getNumberData(false));
        }
        manualTransaction.setReference(reference.getText());
        manualTransaction.setValidateReference(checkExistingReference);
        manualTransaction.setStatus(status);
        manualTransaction.setMemorizedTransaction(memorizedTransaction.getValue());
        manualTransaction.setProjectId(project.getSelectedItemID());
        manualTransaction.setRoleId(roles.getSelectedId());

        manualTransaction.setCurrency(currencyWidget.getCurrency());
        manualTransaction.setExchangeRate(currencyWidget.getExchangeRate());
        if (pdfTemplatePanel != null) {
            manualTransaction.setPdfTemplateID(pdfTemplatePanel.getSelectedTemplateID());
        }
        //Selected Accounts Transaction Items
        NewManualTransactionItem[] items = new NewManualTransactionItem[itemsTable.getValidRows()];
        int j = -1;
        for (int i = 0; i < grid.getRowCount(); i++) {

            if (itemsTable.isItemValid(i)) {
                j++;
                items[j] = new NewManualTransactionItem();
                AccountsLookUp account = (AccountsLookUp) itemsTable.getColumnById(i, ItemTableConstants.ACCOUNT);
                CustomCellTextBox debit = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.DEBIT);
                CustomCellTextBox credit = (CustomCellTextBox) itemsTable.getColumnById(i, ItemTableConstants.CREDIT);
                ExtendedTextArea description = (ExtendedTextArea) itemsTable.getColumnById(i, ItemTableConstants.DESCRIPTION);
                CommonLookup commonLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.NAME);
                CommonLookup clientLookup = (CommonLookup) itemsTable.getColumnById(i, ItemTableConstants.BILLING);
                ProjectLookUp project = (ProjectLookUp) itemsTable.getColumnById(i, ItemTableConstants.PROJECT);
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) itemsTable.getColumnById(i, ItemTableConstants.DEPARTMENT);
                if (description != null) {
                    items[j].setDescription(description.getText());
                }
                if (account != null) {
                    items[j].setAccountItem(account.getSelectedData());
                }
                if (ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(debit.getText())) < 0)
                    items[j].setDebit(AccountingUtils.get().parseToBigDecimal(debit.getText()));
                if (ZERO.compareTo(AccountingUtils.get().parseToBigDecimal(credit.getText())) < 0)
                    items[j].setCredit(AccountingUtils.get().parseToBigDecimal(credit.getText()));
                if (account.getSelectedData() != null && SALARY_PAYABLE.equals(account.getSelectedData().getAccountKey())) {
                    items[j].setEmployee(commonLookup.getSelectedItem());
                } else {
                    items[j].setCustomerOrSupplier(commonLookup.getSelectedItem());
                }
                items[j].setClient(clientLookup != null ? clientLookup.getSelectedItem() : null);
                if (isProjectEnabled && project != null) {
                    items[j].setProject(project.getSelectedItem());
                }
                if (departmentLookUp != null) {
                    items[j].setDepartment(departmentLookUp.getSelectedItem());
                }
                if (customFieldsMap != null && !customFieldsMap.isEmpty()) {
                    ArrayList<CompanyCustomFieldItem> fieldItems = new ArrayList<>();
                    for (String key : customFieldsMap.keySet()) {
                        CustomFieldInterface customField = (CustomFieldInterface) itemsTable.getColumnById(i, key);
                        if (customField != null) {
                            fieldItems.add(customField.getFieldItem());
                        }
                    }

                    if (!fieldItems.isEmpty()) {
                        items[j].setItemCustomFields(fieldItems);
                    }
                }
            }
        }
        manualTransaction.setItems(items);
        manualTransaction.setAttachments(footerUploadPanel.getAttachedFiles());
        if (item.isApprover()) {
            manualTransaction.setApprovers(approver.getChosenApprovers());
        }
        manualEntryService.saveManualJournal(manualTransaction, new AbstractAsyncCallback<Integer>() {

            public void failure(Throwable caught) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Integer result) {
                setEnabledButtons(true);
                if (result != null && result.equals(NewManualTransaction.REFERENCE_EXIST)) {
                    LoadingPanel.loading(false);
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
                    messageBox.setTitle(wfmStrings.information());
                    messageBox.setMessage(accountingMessages.mtNumberExists(numberTxtBox.getText()));
                    messageBox.open();
                } else {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, result, AddManualEntryView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_TO_BANK_ACCOUNT, result, AddManualEntryView.this);
                    showInfoMessage(status);
                }
            }
        });
    }

    public void addFocusListener(final TextBox textBox, final String text) {

        textBox.addFocusListener(new FocusListener() {
            public void onFocus(Widget sender) {
                TextBox textbox = (TextBox) sender;
                if (textbox.getText().equals(text)) {
                    textbox.setText("");
                }
            }

            public void onLostFocus(Widget sender) {
                TextBox textbox = (TextBox) sender;
                if (textbox.getText().equals("")) {
                    textbox.setText(text);
                } else {
                    textBox.setText(AccountingUtils.get().format(AccountingUtils.get().parseToBigDecimal(textbox.getText())));
                }
                calculateTotal();
            }
        });
        textBox.addKeyUpHandler(c -> calculateTotal());
    }

    private void showInfoMessage(String status) {
        LoadingPanel.loading(false);
        if (DRAFT.equals(status)) {
            Info.show(wfmMessages.savedSuccessfully(wfmStrings.manualEntry()), Info.Type.INFO);
        } else if (POST.equals(status)) {
            Info.show(accountingStrings.manualEntryPostedSuccessfully(), Info.Type.INFO);
        }
        closeTab();
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
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
