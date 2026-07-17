package com.edatasite.workforce.gwt.invoice.client.ui.view.manual;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualTransactionData;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.accounting.client.rpc.manualEntry.ManualEntryService;
import com.edatasite.workforce.gwt.accounting.client.rpc.manualEntry.ManualEntryServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.TotalTable;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.PdfTemplatePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_JOURNAL_REPORT;


/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jul 15, 2009
 * Time: 5:05:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManualEntrySummaryView extends FooteredView implements Colapse, FittedContent, Constants, AccountingCustomFormConstants {

    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final ManualEntryServiceAsync manualEntryService = ManualEntryService.App.get();
    private final boolean isDepartmentRelationEnabled = AccountingUtils.get().isEnableAccountingDepartmentRelation();
    private final boolean isProjectEnabled = Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && !Utils.hasGenericAccess(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED);
    private final boolean isProject_To_Head_Enabled = Utils.hasPermission(PermissionConstants.PM_MAIN_MENU) && Utils.hasGenericAccess(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED);
    private final boolean isEnabledManualEntryRole = Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MANUAL_ENTRY_ROLE);

    private final Integer objectId;
    private WfmButton2 postButton, editButton, voidButton, deleteButton;
    private WfmButton2 rejectButton;
    private WfmButton2 approveButton;
    private WfmButton2 submitButton;
    public SplitButton printPdfSplitButton;
    private HTMLPanel htmlPanel;
    private PdfTemplatePanel pdfTemplatePanel;
    private NewManualTransaction manualTransaction;

    private NoteHistoryWidget noteHistoryWidget;
    private boolean hasAccountingBeforeBlockDate;

    private HashMap<String, Widget> widgetsMap;
    private FooterUploadPanel footerUploadPanel;
    List<Widget> rightWidgets = new ArrayList<>();
    private LinkedList<String> itemColumns;
    private DynamicTable itemsTable;

    public ManualEntrySummaryView(Integer objectId) {
        super("summary", accountingStrings.manualEntryView());
        this.objectId = objectId;
    }

    @Override
    protected Widget onInitialize() {
        widgetsMap = new HashMap<>();

        postButton = new WfmButton2(wfmStrings.post(), WfmButton2.BTN_PRIMARY);
        postButton.addClickHandler(clickEvent -> postManualTransaction(POST));

        approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_SUCCESS);
        approveButton.addClickHandler(clickEvent -> postManualTransaction(APPROVED));

        rejectButton = new WfmButton2(wfmStrings.reject(), WfmButton2.BTN_REJECT);
        rejectButton.addClickHandler(clickEvent -> postManualTransaction(REJECTED));

        submitButton = new WfmButton2(wfmStrings.submit(), BTN_PRIMARY);
        submitButton.addClickHandler(clickEvent -> postManualTransaction(SUBMITTED));

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        printPdfSplitButton.ensureDebugId("printPdf_button");

        editButton = new WfmButton2(wfmStrings.edit(), BTN_DEFAULT_OUTLINE);
        editButton.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("manual|edit/" + objectId));

        voidButton = new WfmButton2(accountingStrings.voide(), BTN_DEFAULT_OUTLINE);
        voidButton.addClickHandler(clickEvent -> {
            final WfmMessageBox confirmBox = new WfmMessageBox(IconEnum.INFO, Action.YesNo);
            confirmBox.setTitle(wfmStrings.confirmation());
            confirmBox.setMessage(accountingStrings.areYouSureYouWantToVoidThe() + " " + wfmStrings.manualEntry().toLowerCase() + " ?");
            confirmBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    final KpiModal dialogBox = new KpiModal();
                    dialogBox.setWidth(400);
                    final DatePicker datePicker = new DatePicker(manualTransaction.getDate().getNonConvertedDate());
                    dialogBox.setTitle(wfmStrings.selectVoidDate());
                    dialogBox.add(datePicker);
                    final WfmButton2 voidButton = new WfmButton2(accountingStrings.voide(), WfmButton2.BTN_PRIMARY);
                    dialogBox.addButton(voidButton);
                    voidButton.addClickHandler(clickEvent1 -> {
                        voidButton.setEnabled(false);
                        if (AccountingUtils.validateVoidDate(datePicker.getDate(), manualTransaction.getDate().getNonConvertedDate())) {
                            voidButton.setEnabled(false);
                            manualEntryService.voidManualJournal(objectId, new DateNonConvertable(datePicker.getDate()), new AbstractAsyncCallback<Boolean>() {
                                public void failure(Throwable caught) {
                                    voidButton.setEnabled(true);
                                    dialogBox.close();
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void success(Boolean result) {
                                    voidButton.setEnabled(true);
                                    if (result) {
                                        dialogBox.close();
                                        Info.show(accountingStrings.infoMessage51(), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, result, ManualEntrySummaryView.this);
                                        closeTab();
                                    } else {
                                        Info.show(wfmStrings.errorOccurredUpdate(), Info.Type.WARNING);
                                    }
                                }
                            });
                        }
                    });
                    dialogBox.open();
                }
            });
            confirmBox.open();
        });

        deleteButton = new WfmButton2(wfmStrings.delete(), BTN_DEFAULT_OUTLINE);
        deleteButton.addClickHandler(clickEvent -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.warning());
            messageBox.setMessage(wfmStrings.sureYouWantToDelete());
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    manualEntryService.deleteManualJournal(objectId, new AbstractAsyncCallback<Boolean>() {
                        public void failure(Throwable caught) {
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        public void success(Boolean result) {
                            if (result) {
                                Info.show(wfmMessages.deletedSuccessfully(wfmStrings.manualEntry()), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MANUAL_TRANSACTION_DELETED, result, ManualEntrySummaryView.this);
                                closeTab();
                            } else {
                                Info.show(wfmStrings.errorOccuredWhileDeleting(), Info.Type.WARNING);
                            }
                        }
                    });
                }
            });
            messageBox.open();
        });

        LoadingPanel.loading(true);
        //InvoiceService.App.get().getCompanyTaxList(new AbstractAsyncCallback<TaxList>() {
        //    public void failure(Throwable caught) {
        //        LoadingPanel.loading(false);
        //   }

        //    public void success(TaxList result) {
        manualEntryService.getManualJournalsData(objectId, new AbstractAsyncCallback<ManualTransactionData>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(ManualTransactionData result) {
                LoadingPanel.loading(false);
                manualTransaction = result.getManualTransactionItem();
                hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(manualTransaction.getDate().getNonConvertedDate()));
                drawForm(result);
                initPdfTemplates();
                htmlPanel = new WftHTMLPanel(result.getLayoutHtml(), widgetsMap).getContainer();
                htmlPanel.setStyleName("add-form");
                htmlPanel.add(createFooter());
                add(htmlPanel);
                if (POST.equals(result.getManualTransactionItem().getStatus())) {
                    approveButton.setVisible(false);
                }
            }
        });
        //}
        //});

        return null;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return ManualEntrySummaryView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return ManualEntrySummaryView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();
        footerUploadPanel = new FooterUploadPanel(F_MANUAL_TRANSACTION, objectId);
        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);

        leftSideWidgets.add(informer);
        informer.setInitialClasses("informer-item history-notes-container");
        footerUploadPanel.setInitialClasses("informer-item history-notes-container");
        leftSideWidgets.add(informer);
        leftSideWidgets.add(footerUploadPanel);

        if (manualTransaction != null && manualTransaction.getJournalID() != null && Utils.hasPermission(ACCOUNTING_JOURNAL_REPORT)) {
            FooterInformer showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
            showJournal.addClickHandler(clickEvent -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + manualTransaction.getJournalID(), accountingStrings.reportView() + ": " + manualTransaction.getNumber(), accountingStrings.reportView() + ": " + manualTransaction.getNumber());
            });
            showJournal.setBadgeCount(1);

            leftSideWidgets.add(showJournal);
        }

        return leftSideWidgets;
    }

    private List<Widget> getFooterRightSideWidgets() {
        return rightWidgets;
    }

    private void initPdfTemplates() {
        List<SplitButtonItem> pdfCommandSubItems = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (manualTransaction.getPdfTemplateList() != null
                && manualTransaction.getPdfTemplateList().getItems() != null
                && manualTransaction.getPdfTemplateList().getItems().length > 0) {
            for (SelectItem pdfItem : manualTransaction.getPdfTemplateList().getItems()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfCommandSubItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(pdfItem.getId())));
            }
            pdfTemplatePanel = new PdfTemplatePanel(manualTransaction);
            FormGroup pdfTemplateBox = new FormGroup(accountingStrings.pdfTemplate(), pdfTemplatePanel);
            widgetsMap.put(INPUT_PDF_TEMPLATE, pdfTemplateBox);
        }
        Integer finalDefaultTemplateId = defaultTemplateId;
        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(finalDefaultTemplateId), true);
        pdfVersion.ensureDebugId("pdfVersionItem");
        pdfCommandSubItems.add(pdfVersion);

        if (Utils.hasRoles(Constants.ADMIN)) {
            pdfCommandSubItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", "Customize", new Command() {
                @Override
                public void execute() {
                    Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.MANUAL_ENTRY.name());
                }
            }));
        }
        printPdfSplitButton.addItemList(pdfCommandSubItems);
    }

    private void postManualTransaction(String status) {
        if (!validation()) {
            return;
        }
        LoadingPanel.loading(true);
        manualEntryService.updateManualTransaction(objectId, status, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Boolean result) {
                LoadingPanel.loading(false);
                if (result) {
                    Info.show(accountingStrings.manualEntryPostedSuccessfully(), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, result, ManualEntrySummaryView.this);
                    closeTab();
                } else {
                    Info.show(wfmStrings.errorOccurredUpdate(), Info.Type.WARNING);
                }
            }
        });
    }

    public boolean validation() {
        if (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(manualTransaction.getDate().getNonConvertedDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate("Manual Entry", Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void drawForm(ManualTransactionData data) {
        NewManualTransaction result = data.getManualTransactionItem();
        itemsTable = new DynamicTable(drawColumns(), false);
        itemsTable.setStyleName("invoice__summery-table");


        widgetsMap.put(INPUT_DATE, new FormGroup(wfmStrings.date(), getWidgetAsFormControl(DateUtils.format(result.getDate()))));
        widgetsMap.put(INPUT_INTRODUCTION, new FormGroup(wfmStrings.narration(), getWidgetAsFormControl(result.getNarration())));
        FormGroup approverBox = new FormGroup(wfmStrings.approver(), getWidgetAsFormControl(result.getApprover() != null ? result.getApprover().getName() : ""));
        widgetsMap.put(INPUT_MANAGER, new GColumn(GColumnEnum.COL_4, approverBox));

        widgetsMap.put(INPUT_ITEM_TABLE, itemsTable);
        widgetsMap.put(INPUT_TOTALS_TABLE, createTotalTable(result));

        FormGroup referenceBox = new FormGroup(accountingStrings.referenceNumber(), getWidgetAsFormControl(result.getReference()));
        widgetsMap.put(INPUT_REFERENCE, referenceBox);

        if (isProject_To_Head_Enabled) {
            FormGroup projectBox = new FormGroup(wfmStrings.project(), getWidgetAsFormControl(result.getProject() != null ? result.getProject().getName() : ""));
            widgetsMap.put(INPUT_PROJECT, projectBox);
        }
        if (isEnabledManualEntryRole) {
            FormGroup roleBox = new FormGroup(wfmStrings.role(), getWidgetAsFormControl(result.getRole() != null ? result.getRole().getName() : ""));
            widgetsMap.put(INPUT_ROLE, roleBox);
        }

        FormGroup numberBox = new FormGroup(wfmStrings.number(), getWidgetAsFormControl(result.getNumber()));
        widgetsMap.put(INPUT_NUMBER, numberBox);

        String currencyString = accountingMessages.dynamicCurrencyView(result.getBaseCurrency().getName()) +
                " " + AccountingUtils.get().formatExRate(result.getExchangeRate().doubleValue()) + " " + result.getCurrency().getName();
        widgetsMap.put(INPUT_EXCHANGE_RATE, new FormGroup(wfmStrings.currency(), getWidgetAsFormControl(currencyString)));

        fillItemTable(result.getItems());
        Div postButtonWrapper = new Div();
        postButtonWrapper.add(postButton);

        Div pdfButtonWrapper = new Div();
        pdfButtonWrapper.add(printPdfSplitButton);

        Div editButtonWrapper = new Div();
        editButtonWrapper.add(editButton);

        Div voidButtonWrapper = new Div();
        voidButtonWrapper.add(voidButton);

        Div deleteButtonWrapper = new Div();
        deleteButtonWrapper.add(deleteButton);

        Div rejectButtonWrapper = new Div();
        rejectButtonWrapper.add(rejectButton);

        Div approveButtonWrapper = new Div();
        approveButtonWrapper.add(approveButton);

        Div submitButtonWrapper = new Div();
        submitButtonWrapper.add(submitButton);

        if (!result.isUsed() && !hasAccountingBeforeBlockDate) {
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_MANUAL_JOURNAL_DELETE)) {
                rightWidgets.add(deleteButtonWrapper);
            }
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_MANUAL_JOURNAL_VOID) && POST.equals(result.getStatus())) {
                rightWidgets.add(voidButtonWrapper);
            }

            boolean canEdit = Utils.hasPermission(PermissionConstants.ACCOUNTING_MANUAL_JOURNAL_EDIT) && (!APPROVED.equals(result.getStatus()) && !POST.equals(result.getStatus()) || !data.isSetUpAP());
            if (manualTransaction.getApprover() != null) {
                canEdit = Utils.hasPermission(PermissionConstants.ACCOUNTING_MANUAL_JOURNAL_EDIT) && ((manualTransaction.getApprover() != null && (APPROVED.equals(result.getStatus()) || SUBMITTED.equals(result.getStatus()) || REJECTED.equals(result.getStatus()) || POST.equals(result.getStatus())) && manualTransaction.getApprover().getId().equals(Utils.getUserID()))
                        || manualTransaction.getCreatorItem() != null && manualTransaction.getCreatorItem().getId().equals(Utils.getUserID()) && (SUBMITTED.equals(result.getStatus()) || REJECTED.equals(result.getStatus())));
            }

            if (canEdit) {
                rightWidgets.add(editButtonWrapper);
            }
        }

        rightWidgets.add(pdfButtonWrapper);

        if (manualTransaction.getApprover() != null && manualTransaction.getApprover().getId() != null && manualTransaction.getApprover().getId().equals(Utils.getUserID()) && SUBMITTED.equals(result.getStatus())) {
            rightWidgets.add(rejectButtonWrapper);
            rightWidgets.add(approveButtonWrapper);
        }

        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_MANUAL_JOURNAL_EDIT) && !hasAccountingBeforeBlockDate) {
            if ((data.isApprover() && APPROVED.equals(result.getStatus()))) {
                rightWidgets.add(postButtonWrapper);
            } else if (!data.isApprover() && DRAFT.equals(result.getStatus())) {
                rightWidgets.add(postButtonWrapper);
            }
        }
        initNotesPanel(data);

    }

    private void fillItemTable(NewManualTransactionItem[] items) {
        if (items == null) {
            return;
        }
        itemsTable.clear();
        for (NewManualTransactionItem item : items) {
            Map<String, Widget> widgetsMap = new LinkedHashMap<>();
            for (String column : itemColumns) {
                switch (column) {
                    case ItemTableConstants.ACCOUNT:
                        Label account = new Label();
                        account.setText(item.getAccountItem() != null ? item.getAccountItem().getName() : "");
                        widgetsMap.put(ItemTableConstants.ACCOUNT, account);
                        break;
                    case ItemTableConstants.DEBIT:
                        Label debit = new Label(item.getDebit() != null ? AccountingUtils.get().formatPrice(item.getDebit()) : "");
                        widgetsMap.put(ItemTableConstants.DEBIT, debit);
                        break;
                    case ItemTableConstants.CREDIT:
                        Label credit = new Label(item.getCredit() != null ? AccountingUtils.get().formatPrice(item.getCredit()) : "");
                        widgetsMap.put(ItemTableConstants.CREDIT, credit);
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        Label description = new Label(item.getDescription());
                        widgetsMap.put(ItemTableConstants.DESCRIPTION, description);
                        break;
                    case ItemTableConstants.NAME:
                        Label commonLabel;
                        if (AccountingConstants.SALARY_PAYABLE.equals(item.getAccountItem().getAccountKey())) {
                            commonLabel = new Label(item.getEmployee() != null ? item.getEmployee().getName() : "");
                        } else {
                            commonLabel = new Label(item.getCustomerOrSupplier() != null ? item.getCustomerOrSupplier().getName() : "");
                        }
                        widgetsMap.put(ItemTableConstants.NAME, commonLabel);
                        break;
                    case ItemTableConstants.BILLING:
                        Label onlyCustomerName = new Label(item.getClient() != null ? item.getClient().getName() : "");
                        widgetsMap.put(ItemTableConstants.BILLING, onlyCustomerName);
                        break;
                    case ItemTableConstants.PROJECT:
                        if (isProjectEnabled) {
                            Label project = new Label(item.getProject() != null ? item.getProject().getNumber() != null ? item.getProject().getNumber() + " - >" + item.getProject().getName() : item.getProject().getName() : "");
                            widgetsMap.put(ItemTableConstants.PROJECT, project);
                        }
                        break;
                    case ItemTableConstants.DEPARTMENT:
                        if (isDepartmentRelationEnabled) {
                            Label department = new Label(item.getDepartment() != null ? item.getDepartment().getName() : "");
                            widgetsMap.put(ItemTableConstants.DEPARTMENT, department);
                        }
                        break;
                    default:
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldByCode(column);
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
            itemsTable.addRow(item.getObjectId(), widgetsMap.values().toArray(new Widget[]{}));
        }
    }

    private void initNotesPanel(ManualTransactionData data) {
        noteHistoryWidget = new NoteHistoryWidget(callback -> manualEntryService.getManualJournalHistoryNote(objectId, callback));
    }

    private void setnotesPanelListeners() {
        noteHistoryWidget.setSaveIntoDatabase((historyListItem) -> {
            LoadingPanel.loading(true);
            ManualEntryService.App.get().createManualJournalNote(objectId, historyListItem, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Integer savedObjectId) {
                    historyListItem.setObjectID(savedObjectId);
                    LoadingPanel.loading(false);
                }
            });
        });
        noteHistoryWidget.setRemoveFromDatabase((historyListItem) -> {
            if (historyListItem != null && historyListItem.getObjectID() != null) {
                LoadingPanel.loading(true);
                manualEntryService.deleteManualJournal(historyListItem.getObjectID(), new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void onSuccess(Boolean integer) {
                        LoadingPanel.loading(false);
                    }
                });
            }
        });
    }

    private TotalTable createTotalTable(NewManualTransaction result) {

        boolean inBaseCurrency = result.getBaseCurrency().getName().equals(result.getCurrency().getName());

        HTML debitLabel = new HTML(wfmStrings.debit());
        HTML creditLabel = new HTML(wfmStrings.credit());
        HTML totalLabel = new HTML(accountingMessages.dynamicTotal(result.getCurrency().getName()));
        HTML baseTotalLabel = new HTML(accountingMessages.dynamicTotal(result.getBaseCurrency().getName()));

        debitLabel.setStyleName(STYLE_TOTAL_LABEL);
        creditLabel.setStyleName(STYLE_TOTAL_LABEL);
        totalLabel.setStyleName(STYLE_TOTAL_LABEL);
        baseTotalLabel.setStyleName(STYLE_TOTAL_LABEL);

        HTML debitTotalHTML = new HTML(AccountingUtils.get().formatPrice(result.getDebitTotal()));
        HTML creditTotalHTML = new HTML(AccountingUtils.get().formatPrice(result.getCreditTotal()));
        HTML debitsBaseTotalHTML = new HTML(AccountingUtils.get().formatPrice(result.getDebitTotal().divide(result.getExchangeRate(), AccountingUtils.calculationScale, RoundingMode.HALF_UP)));
        HTML creditsBaseTotalHTML = new HTML(AccountingUtils.get().formatPrice(result.getCreditTotal().divide(result.getExchangeRate(), AccountingUtils.calculationScale, RoundingMode.HALF_UP)));

        debitTotalHTML.setStyleName(STYLE_TOTAL_VALUE);
        creditTotalHTML.setStyleName(STYLE_TOTAL_VALUE);
        debitsBaseTotalHTML.setStyleName(STYLE_TOTAL_VALUE);
        creditsBaseTotalHTML.setStyleName(STYLE_TOTAL_VALUE);

        TotalTable totalsTable = new TotalTable();
        totalsTable.addWidgetsInARow(new HTML(""), debitLabel, creditLabel);
        if (!inBaseCurrency) {
            totalsTable.addWidgetsInARow(totalLabel, debitTotalHTML, creditTotalHTML);
        }
        totalsTable.addWidgetsInARow(baseTotalLabel, debitsBaseTotalHTML, creditsBaseTotalHTML);

        return totalsTable;
    }

    private DynamicTableColumn[] drawColumns() {
        itemColumns = new LinkedList<>();
        LinkedList<DynamicTableColumn> columnsList = new LinkedList<>();
        if (manualTransaction != null && manualTransaction.getCustomItemColumns() != null && manualTransaction.getCustomItemColumns().length > 0) {
            DynamicTableColumn dynamicTableColumn;
            for (ColumnConfigs column : manualTransaction.getCustomItemColumns()) {
                switch (column.getCode()) {
                    case ItemTableConstants.ACCOUNT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.account(), ItemTableConstants.ACCOUNT, Utils.getColumnWidth(column.getWidth(), 200));
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(ItemTableConstants.ACCOUNT);
                        break;
                    case ItemTableConstants.DEBIT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.debit(), ItemTableConstants.DEBIT, Utils.getColumnWidth(column.getWidth(), 80), Constants.RIGHT_ALIGN_CELL);
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(ItemTableConstants.DEBIT);
                        break;
                    case ItemTableConstants.CREDIT:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.credit(), ItemTableConstants.CREDIT, Utils.getColumnWidth(column.getWidth(), 80), Constants.RIGHT_ALIGN_CELL);
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(ItemTableConstants.CREDIT);
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.description(), ItemTableConstants.DESCRIPTION, Utils.getColumnWidth(column.getWidth(), 250));
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(ItemTableConstants.DESCRIPTION);
                        break;
                    case ItemTableConstants.NAME:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.name(), ItemTableConstants.NAME, Utils.getColumnWidth(column.getWidth(), 120));
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(ItemTableConstants.NAME);
                        break;
                    case ItemTableConstants.BILLING:
                        dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : accountingStrings.billing(), ItemTableConstants.BILLING, Utils.getColumnWidth(column.getWidth(), 120));
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(ItemTableConstants.BILLING);
                        break;
                    case ItemTableConstants.PROJECT:
                        if (isProjectEnabled) {
                            dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.project(), ItemTableConstants.PROJECT, Utils.getColumnWidth(column.getWidth(), 120));
                            columnsList.add(dynamicTableColumn);
                            itemColumns.add(ItemTableConstants.PROJECT);
                        }
                        break;
                    case ItemTableConstants.DEPARTMENT:
                        if (isDepartmentRelationEnabled) {
                            dynamicTableColumn = new DynamicTableColumn(column.isChanged() ? column.getTitle() : wfmStrings.department(), ItemTableConstants.DEPARTMENT, Utils.getColumnWidth(column.getWidth(), 138));
                            columnsList.add(dynamicTableColumn);
                            itemColumns.add(ItemTableConstants.DEPARTMENT);
                        }
                        break;
                    default:
                        dynamicTableColumn = new DynamicTableColumn(column.getTitle(), column.getCode(), Utils.getColumnWidth(column.getWidth(), 165));
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(column.getCode());
                        break;

                }
            }
        } else {
            columnsList.add(new DynamicTableColumn(wfmStrings.account(), ItemTableConstants.ACCOUNT, 200));
            itemColumns.add(ItemTableConstants.ACCOUNT);

            columnsList.add(new DynamicTableColumn(wfmStrings.debit(), ItemTableConstants.DEBIT, 80, Constants.RIGHT_ALIGN_CELL));
            itemColumns.add(ItemTableConstants.DEBIT);

            columnsList.add(new DynamicTableColumn(wfmStrings.credit(), ItemTableConstants.CREDIT, 80, Constants.RIGHT_ALIGN_CELL));
            itemColumns.add(ItemTableConstants.CREDIT);

            columnsList.add(new DynamicTableColumn(wfmStrings.description(), ItemTableConstants.DESCRIPTION, 250));
            itemColumns.add(ItemTableConstants.DESCRIPTION);

            columnsList.add(new DynamicTableColumn(wfmStrings.name(), ItemTableConstants.NAME, 120));
            itemColumns.add(ItemTableConstants.NAME);

            columnsList.add(new DynamicTableColumn(accountingStrings.billing(), ItemTableConstants.BILLING, 120));
            itemColumns.add(ItemTableConstants.BILLING);

            if (isProjectEnabled) {
                columnsList.add(new DynamicTableColumn(wfmStrings.project(), ItemTableConstants.PROJECT, 120));
                itemColumns.add(ItemTableConstants.PROJECT);
            }

            if (isDepartmentRelationEnabled) {
                columnsList.add(new DynamicTableColumn(wfmStrings.department(), ItemTableConstants.DEPARTMENT, 138));
                itemColumns.add(ItemTableConstants.DEPARTMENT);
            }
        }
        return columnsList.toArray(new DynamicTableColumn[]{});
    }

    private HTML getHTMLTitle(String text) {
        return new HTML(text);
    }

    public void show() {

    }

    public String getIconStyle() {
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

    private void generatePDF(Integer pdfTemplateID) {
        String pdfURL = null;
        TransactionPDFObject requestObject = new TransactionPDFObject(objectId, pdfTemplateID);
        pdfURL = CommandConstants.PDF_URL + "/manualJournalViewPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
    }

}
