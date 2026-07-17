package com.edatasite.workforce.gwt.invoice.client.ui.view.payment;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
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
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
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
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.IntroductionPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.PdfTemplatePanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedFields;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Dilshod Madrahimov on 10/20/15 7:56 PM
 */
public class BatchPaymentSummaryView extends FooteredView implements Constants, AccountingCustomFormConstants, Colapse, FittedContent, PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private HashMap<String, Widget> widgetsMap;
    private PdfTemplatePanel pdfTemplatePanel;
    private HTMLPanel htmlPanel;
    private WfmButton2 editButton;
    private WfmButton2 voidButton;
    private WfmButton2 deleteButton;
    private SplitButton printPdfSplitButton;
    List<SplitButtonItem> approveCommandSubItems = new ArrayList<>();
    private HTML totalAmountValue, totalPaymentAmountValue;
    private HTML totalPayInBaseLabel, totalPayInTransactionLabel;
    private HTML underpaymentAccountLabel, underpaymentAccount;
    private FlexTable allHistoryTable;
    private DisclosurePanel allHistoryPanel;
    private FlexTable allTable;
    private NoteHistoryWidget noteHistoryWidget;

    private final Integer objectID;
    private final boolean isReceivable;
    private boolean hasAccountingBeforeBlockDate;
    private FooterUploadPanel footerUploadPanel;
    private IntroductionPanel description;

    private ReceivePaymentData paymentData;
    private MaterialLink showMoreLink;
    public InvoiceAdvancedOptions advancedOptions;
    private SplitButton sendEmails;
    private Integer clientId;
    private final boolean hasAccessDelete;
    private final boolean hasAccessEdit;
    private final boolean hasAccessVoid;
    private final boolean hasAccessPdf;

    public BatchPaymentSummaryView(Integer objectID, boolean isReceivable) {
        super("summary");
        setDescription(isReceivable ? accountingStrings.receivePayment() : property.getSingular(accountingStrings.payBill()));
        this.objectID = objectID;
        this.isReceivable = isReceivable;
        hasAccessDelete = isReceivable ? Utils.hasPermission(RECEIVE_PAYMENT_DELETE) : Utils.hasPermission(PAY_INVOICE_DELETE);
        hasAccessEdit = isReceivable ? Utils.hasPermission(RECEIVE_PAYMENT_EDIT) : Utils.hasPermission(PAY_INVOICE_EDIT);
        hasAccessVoid = isReceivable ? Utils.hasPermission(RECEIVE_PAYMENT_VOID) : Utils.hasPermission(PAY_INVOICE_VOID);
        hasAccessPdf = isReceivable ? Utils.hasPermission(RECEIVE_PAYMENT_PDF) : Utils.hasPermission(PAY_INVOICE_PDF);
    }

    @Override
    protected Widget onInitialize() {
        initForm();
        loadFormData();
        return null;
    }

    private void initForm() {
        widgetsMap = new HashMap<>();

        totalPayInBaseLabel = new HTML(wfmStrings.paymentAmount());
        totalPayInTransactionLabel = new HTML(wfmStrings.paymentAmount());

        totalAmountValue = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        totalPaymentAmountValue = new HTML(AccountingUtils.get().formatPrice(BigDecimal.ZERO));

        drawHistoryPanel();

        editButton = new WfmButton2(wfmStrings.edit(), BTN_DEFAULT_OUTLINE);
        voidButton = new WfmButton2(accountingStrings.voide(), BTN_DEFAULT_OUTLINE);
        deleteButton = new WfmButton2(wfmStrings.delete(), BTN_DEFAULT_OUTLINE);
        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        sendEmails = new SplitButton(100, WfmButton2.BTN_PRIMARY);

        initHandler();
    }

    protected void sendToClient() {
        Integer pdfTemplateID = pdfTemplatePanel != null ? pdfTemplatePanel.getSelectedTemplateID() : null;
        SinksContainerFactory.entryPoint.onHistoryChanged("accountingemailcompose|add/add/" + RECEIVE_PAYMENT_CATEGORY + "/" + clientId + "/" + objectID + "/" + pdfTemplateID);
    }

    private void drawHistoryPanel() {
        drawHistoryTab();

        initializeAllHistoryTable();

        allHistoryPanel.setContent(allTable);

        if (allHistoryPanel != null) {
            allHistoryPanel.setStyleName(STYLE_PAYMENT_HISTORY_PANEL);
            widgetsMap.put(INPUT_ALL_HISTORY_PANEL, allHistoryPanel);
        }
    }

    private void drawHistoryTab() {

        allTable = new FlexTable();
        allTable.setCellSpacing(15);
        allTable.getElement().getStyle().setBackgroundColor("#F5F5F5");
        noteHistoryWidget = new NoteHistoryWidget(callback -> InvoiceService.App.get().getHistoryByHistoryNote(objectID, BATCH_PAYMENT, callback));

        InvoiceService.App.get().getAllHistory(objectID, BATCH_PAYMENT, new AbstractAsyncCallback<ArrayList<MyUpdateItem>>() {

            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ArrayList<MyUpdateItem> result) {
                int row = 0;
                for (MyUpdateItem item : result) {
                    allTable.setWidget(row, 0, new HTML(DateUtils.getDateAndTimeFormatFull(item.getEventDate())));
                    allTable.setWidget(row, 1, new HTML(item.getMessage()));
                    row++;
                }

            }
        });

    }

    private void initializeAllHistoryTable() {
        allHistoryTable = new FlexTable();

        allHistoryPanel = new DisclosurePanel(accountingStrings.showAllHistory());

        allHistoryPanel.addOpenHandler(disclosurePanelOpenEvent -> {
            allHistoryPanel.getHeaderTextAccessor().setText(accountingStrings.hideAllHistory());
        });
        allHistoryPanel.addCloseHandler(disclosurePanelCloseEvent -> {
            allHistoryTable.clear();
            allHistoryPanel.getHeaderTextAccessor().setText(accountingStrings.showAllHistory());
        });

        allHistoryTable.setWidget(0, 0, new Label(accountingStrings.noHistory()));

        allHistoryPanel.setContent(allHistoryTable);

    }

    private void initHandler() {
        editButton.addClickHandler(clickEvent -> goTo("receivepayment|edit/" + objectID + "/" + (isReceivable ? Constants.RECEIVABLE : Constants.PAYABLE)));
        deleteButton.addClickHandler(clickEvent -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.warning());
            messageBox.setMessage(wfmStrings.sureYouWantToDelete());
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    InvoiceService.App.get().deleteBatchPayment(objectID, new AbstractAsyncCallback<TestRPC>() {

                        @Override
                        public void failure(Throwable throwable) {
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(TestRPC result) {
                            if (result != null && result.isError()) {
                                Info.show(result.getMessage(), Info.Type.WARNING);
                                return;
                            }
                            Info.show(accountingStrings.paymentDeletedSuccessfully(), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, null, BatchPaymentSummaryView.this);
                            closeSummaryUI();
                        }
                    });
                }
            });
            messageBox.open();
        });

        voidButton.addClickHandler(clickEvent -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.confirmation());
            messageBox.setMessage(wfmStrings.doYoureallyWantTovoidThisPayment());
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    final KpiModal dialogBox = new KpiModal();
                    dialogBox.setCloseButton(true);
                    dialogBox.setWidth(400);
                    dialogBox.setTitle(wfmStrings.selectVoidDate());
                    final DatePicker datePicker = new DatePicker(paymentData.getDate().getNonConvertedDate());
                    datePicker.setWidth("180px");
                    datePicker.getElement().getStyle().setMargin(10, Style.Unit.PX);
                    WfmButton2 voidButton = new WfmButton2(accountingStrings.voide(), WfmButton2.BTN_PRIMARY);
                    dialogBox.add(datePicker);
                    dialogBox.addButton(voidButton);
                    voidButton.addClickHandler(clickEvent1 -> {
                        if (AccountingUtils.validateVoidDate(datePicker.getDate(), paymentData.getDate().getNonConvertedDate())) {
                            voidButton.setEnabled(false);
                            InvoiceService.App.get().voidBatchPayment(objectID, new DateNonConvertable(datePicker.getDate()), new AsyncCallback<Integer>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    dialogBox.close();
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Integer result) {
                                    dialogBox.close();
                                    Info.show(wfmStrings.paymentVoidSuccessfully(), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, null, BatchPaymentSummaryView.this);
                                    closeSummaryUI();
                                }
                            });
                        }
                    });
                    dialogBox.open();
                }
            });
            messageBox.open();
        });

//        closeButton.addClickHandler(clickEvent -> closeTab());

    }

    private void closeSummaryUI() {
        if (isReceivable) {
            closeTab("transaction|receivePaymentsList");
        } else {
            closeTab("transaction|payBillsList");
        }
    }

    private void loadFormData() {
        if (objectID != null) {
            LoadingPanel.loading(true);
            InvoiceService.App.get().getBatchPaymentData(objectID, new AsyncCallback<ReceivePaymentData>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ReceivePaymentData data) {
                    hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(data.getDate().getNonConvertedDate()));
                    paymentData = data;
                    systemCustomFields = data.getSystemCustomFields();
                    clientId = paymentData.getCrmAccount() != null ? paymentData.getCrmAccount().getId() : null;
                    initWidgetsMap();
                    initCustomFields();
                    htmlPanel = new WftHTMLPanel(paymentData.getLayoutHtml(), widgetsMap).getContainer();
                    htmlPanel.setStyleName("add-form");
                    htmlPanel.add(createFooter());
                    add(htmlPanel);
                    totalPayInBaseLabel.setHTML(wfmStrings.paymentAmount() + "(" + paymentData.getBaseCurrency().getName() + ")");
                    totalPayInTransactionLabel.setHTML(wfmStrings.paymentAmount() + "(" + paymentData.getCurrency().getName() + ")");
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return BatchPaymentSummaryView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return BatchPaymentSummaryView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightSideWidgets = new ArrayList<>();

        if (hasAccessPdf) {
            Div pdfWrapper = new Div();
            pdfWrapper.add(printPdfSplitButton);
            rightSideWidgets.add(pdfWrapper);
        }
        if (!hasAccountingBeforeBlockDate) {
            if (hasAccessDelete) {
                Div deleteWrapper = new Div();
                deleteWrapper.add(deleteButton);
                rightSideWidgets.add(deleteWrapper);
            }
            if (!paymentData.isReversed() && hasAccessVoid) {
                Div voidWrapper = new Div();
                voidWrapper.add(voidButton);
                rightSideWidgets.add(voidWrapper);
            }
            if (hasAccessEdit && !Objects.equals(AccountingConstants.VOID, paymentData.getPaymentStatus())) {
                Div editWrapper = new Div();
                editWrapper.add(editButton);
                rightSideWidgets.add(editWrapper);
            }
        }
        Div sendWrapper = new Div();
        sendWrapper.add(sendEmails);
        rightSideWidgets.add(sendWrapper);

        return rightSideWidgets;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();
        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);

        informer.setInitialClasses("informer-item history-notes-container");
        footerUploadPanel = new FooterUploadPanel(Constants.F_BATCH_PAYMENT, paymentData.getObjectID(), true);

        footerUploadPanel.setInitialClasses("informer-item history-notes-container");
        leftSideWidgets.add(informer);
        leftSideWidgets.add(footerUploadPanel);

        if (Utils.hasPermission(ACCOUNTING_JOURNAL_REPORT) && (paymentData.getJournalID() != null || paymentData.isHasMultiTransaction() || (paymentData.getPaymentTarget().contains(AccountingConstants.PAYMENT_TARGET_EXPENSE) || paymentData.getPaymentTarget().contains(Constants.EXPENSES)))) {
            FooterInformer showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
            if (paymentData.getJournalID() != null) {
                showJournal.addClickHandler(clickEvent -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + paymentData.getJournalID(), accountingStrings.reportView() + ": " + paymentData.getNumber(), accountingStrings.reportView() + ": " + paymentData.getNumber());
                });
            } else { // if there is multi transaction
                showJournal.addClickHandler(clickEvent -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + objectID + "/" + true, accountingStrings.reportView() + ": " + paymentData.getNumber(), accountingStrings.reportView() + ": " + paymentData.getNumber());
                });
            }
            showJournal.setBadgeCount(1);
            leftSideWidgets.add(showJournal);
        }
        FooterInformer footerIntroduction = new FooterInformer(SvgEnum.docTitle, wfmStrings.description(), description.getIntroduction());
        footerIntroduction.setInitialClasses("informer-item");
        leftSideWidgets.add(footerIntroduction);
        return leftSideWidgets;
    }


    private void initWidgetsMap() {

        advancedOptions = createAdvancedOptions();
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addStyleName("btn-flat BatchPaymentSummaryView");
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions));

        BigDecimal totalPayInTrancactionCurrency = BigDecimal.ZERO, totalPaymentAmountInBaseCurrency = BigDecimal.ZERO;

        boolean paymentIsForeignCurrency = !paymentData.getBaseCurrency().getId().equals(paymentData.getCurrency().getId());

        HTML rateLabel = new HTML("1 " + paymentData.getBaseCurrency().getName() + " = " +
                AccountingUtils.get().formatExRate(paymentData.getExRate()) + " " + paymentData.getCurrency().getName());

        description = new IntroductionPanel();
        description.getIntroduction().setText(paymentData.getDescription());

        SelectItem crmAccount = paymentData.getCrmAccount();


        if (isReceivable) {

            Widget customerName = null;

            if (Utils.hasPermission(PermissionConstants.CUSTOMER_CLICKABLE)) {
                customerName = new SimpleLink(crmAccount.getName(), "client|summary/" + crmAccount.getId(), crmAccount.getName(), crmAccount.getNumber());
            } else {
                customerName = new HTML(crmAccount.getName());
            }

            FormGroup clientField = new FormGroup(customerName);
            clientField.ensureDebugId(InvoiceFormFields.CUSTOMER);
            clientField.getGroupContent().addStyleName("form-control");
            Div clientFieldLabel = clientField.getGroupLabel();
            clientFieldLabel.addStyleName("label-group");
            clientFieldLabel.add(new Span(wfmStrings.name()));

           /* if (Utils.hasPermission(PermissionConstants.CUSTOMER_CLICKABLE)) {
                Span balance = new Span(accountingStrings.balance() + ": ");
                balance.add(new SimpleLink(crmAccount. >= 0 ? utils.formatPrice(invoiceData.getTypeItem().getSupplierCustomerBalance()) : "(" + utils.formatPrice((-1) * invoiceData.getTypeItem().getSupplierCustomerBalance()) + ")",
                        "customerBalance|customerBalance/" + invoiceData.getClientID() + "/" + CrmAccountItem.CUSTOMER, "", accountingStrings.balance() + ":" + invoiceData.getClientName()));
                clientFieldLabel.add(balance);
            } else {
                Span balance = new Span(accountingStrings.balance() + ": " + (invoiceData.getTypeItem().getSupplierCustomerBalance() >= 0 ? utils.formatPrice(invoiceData.getTypeItem().getSupplierCustomerBalance()) : "(" + utils.formatPrice((-1) * invoiceData.getTypeItem().getSupplierCustomerBalance()) + ")"));
                clientFieldLabel.add(balance);
            }*/

            widgetsMap.put(INPUT_CUSTOMER, clientField);
        } else {
            widgetsMap.put(INPUT_CUSTOMER, new FormGroup(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), getWidgetAsFormControl(crmAccount != null ? crmAccount.getName() : wfmStrings.notAvailable())));
        }


        HTML paymentTarget = new HTML();
        List<String> paymentTargets = new ArrayList<>();
        if (paymentData.getPaymentTarget().contains(AccountingConstants.PAYMENT_TARGET_INVOICE) || paymentData.getPaymentTarget().contains(AccountingConstants.PAYMENT_TARGET_INVOICE_AND_MANUAL_JOURNAL)) {
            paymentTargets.add(accountingStrings.invoice());
        }
        if (paymentData.getPaymentTarget().contains(AccountingConstants.PAYMENT_TARGET_MANUAL_JOURNAL) || paymentData.getPaymentTarget().contains(AccountingConstants.PAYMENT_TARGET_INVOICE_AND_MANUAL_JOURNAL)) {
            paymentTargets.add(wfmStrings.manualEntry());
        }
        if (paymentData.getPaymentTarget().contains(AccountingConstants.PAYMENT_TARGET_EXPENSE) || paymentData.getPaymentTarget().contains(Constants.EXPENSES)) {
            paymentTargets.add(wfmStrings.expense());
        }
        paymentTarget.setText(String.join(", ", paymentTargets));
        widgetsMap.put(INPUT_INVOICE_TYPE, new FormGroup(wfmStrings.type(), wrapWidgetToFormControl(paymentTarget)));
        widgetsMap.put(INPUT_ACCOUNT, new FormGroup(wfmStrings.account(), getWidgetAsFormControl(paymentData.getAccount().getName())));
        widgetsMap.put(INPUT_PAYMENT_DATE, new FormGroup(wfmStrings.paymentDate(), getWidgetAsFormControl(DateUtils.format(paymentData.getDate()))));
        widgetsMap.put(INPUT_AMOUNT, new FormGroup(wfmStrings.amount(), getWidgetAsFormControl(AccountingUtils.get().format(paymentData.getTotalAmount()))));

        systemCustomFieldsMap.put(INPUT_REFERENCE, getWidgetAsFormControl(paymentData.getReference()));
        systemCustomFieldsMap.put(INPUT_EXCHANGE_RATE, wrapWidgetToFormControl(rateLabel));

        if (paymentData.isEnableDepartment()) {
            FormGroup departmentItem = new FormGroup(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), getWidgetAsFormControl(paymentData.getDepartment() != null ? paymentData.getDepartment().getName() : ""));
            widgetsMap.put(INPUT_DEPARTMENT, departmentItem);
        }
        widgetsMap.put(INPUT_NUMBER, new FormGroup(wfmStrings.number(), getWidgetAsFormControl(paymentData.getNumber())));
        if (paymentData.getOverPayment() != null) {


            widgetsMap.put(INPUT_OVERPAYMENT_ACCOUNT, new FormGroup(wfmStrings.overpayment(), getWidgetAsFormControl(paymentData.getOverPayment().getOverPaymentAccount() != null ? paymentData.getOverPayment().getOverPaymentAccount().getName() : "")));
            widgetsMap.put(INPUT_OVERPAYMENT_AMOUNT, new FormGroup(wfmStrings.account(), getWidgetAsFormControl(AccountingUtils.get().formatPrice(paymentData.getOverPayment().getOverPaymentAmount()))));
        }

        underpaymentAccountLabel = new HTML(accountingStrings.bankFeeDiscountAccount());
        underpaymentAccount = new HTML();

        widgetsMap.put(INPUT_UNDERPAYMENT_ACCOUNT, new FormGroup(underpaymentAccountLabel, wrapWidgetToFormControl(underpaymentAccount)));

        if (paymentData.getPayments() != null && paymentData.getPayments().length > 0) {
            DynamicTable dynamicTable = new DynamicTable(getColumns(paymentIsForeignCurrency), false);
            dynamicTable.addStyleName("file--BatchPaymentSummaryView");
            for (PaymentData item : paymentData.getPayments()) {
                dynamicTable.addRow(getWidgets(item, paymentIsForeignCurrency));
                totalPaymentAmountInBaseCurrency = totalPaymentAmountInBaseCurrency.add(item.getPaymentAmount());
                totalPayInTrancactionCurrency = totalPayInTrancactionCurrency.add(item.getPaymentAmountInInvoiceCurrency() != null ? item.getPaymentAmountInInvoiceCurrency() : BigDecimal.ZERO);
            }
            widgetsMap.put(INPUT_ITEM_TABLE, dynamicTable);
        }

        totalAmountValue.setHTML(AccountingUtils.get().formatPrice(totalPaymentAmountInBaseCurrency));
        totalPaymentAmountValue.setHTML(AccountingUtils.get().formatPrice(totalPayInTrancactionCurrency));

        ReceiptTable totalTable = new ReceiptTable();
        totalTable.getElement().addClassName("java-AditionalPaymentSummaryView");
        totalTable.addItem(totalPayInBaseLabel, totalAmountValue);
        if (!paymentData.getBaseCurrency().getId().equals(paymentData.getCurrency().getId())) {
            totalTable.addItem(totalPayInTransactionLabel, totalPaymentAmountValue);
        }
        totalTable.clearTotalItems();
        widgetsMap.put(INPUT_TOTALS_TABLE, totalTable);

        List<SplitButtonItem> pdfCommandSubItems = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (paymentData.getPdfTemplateList() != null
                && paymentData.getPdfTemplateList().getItems() != null
                && paymentData.getPdfTemplateList().getItems().length > 0) {
            for (SelectItem pdfItem : paymentData.getPdfTemplateList().getItems()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfCommandSubItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(pdfItem.getId())));
            }
            pdfTemplatePanel = new PdfTemplatePanel(paymentData);
        }
        Integer finalDefaultTemplateId = defaultTemplateId;
        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(finalDefaultTemplateId), true);
        pdfVersion.ensureDebugId("pdfVersionItem");
        pdfCommandSubItems.add(pdfVersion);

        if (Utils.hasRoles(Constants.ADMIN)) {
            pdfCommandSubItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", "Customize", () -> {
                String pdfType = PdfTemplateTypeEnum.BATCH_PAY_BILL.name();
                if (isReceivable) {
                    pdfType = PdfTemplateTypeEnum.BATCH_RECEIVE_PAYMENT.name();
                }
                Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + pdfType);
            }));
        }
        printPdfSplitButton.addItemList(pdfCommandSubItems);

        approveCommandSubItems.add(new SplitButtonItem(EMAIL, wfmStrings.sendEmail(),
                () -> {
                    sendToClient();
                }, true));
        sendEmails.addItemList(approveCommandSubItems);

        FormGroup showMoreField = new FormGroup(showMoreLink);
        showMoreField.setLabel("&nbsp;");
        widgetsMap.put(INPUT_SHOW_MORE, showMoreField);
    }

    protected InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(new InvoiceAdvancedFields() {
            @Override
            public List<Widget> getOptionWidgets() {
                List<Widget> result = new ArrayList<>();

                FormGroup project = new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), getWidgetAsFormControl(paymentData.getProject() != null ? paymentData.getProject().getName() : wfmStrings.notAvailable()));
                result.add(project);

                FormGroup paymentType = new FormGroup(wfmStrings.paymentType(), getWidgetAsFormControl(paymentData.getPaymentMethod() != null ? paymentData.getPaymentMethod().getName() : wfmStrings.notAvailable()));
                result.add(paymentType);


                return result;
            }
        }, false);
    }

    private void initCustomFields() {
        if (paymentData.getCustomFieldItems() != null && paymentData.getCustomFieldItems().size() > 0) {
            MaterialPanel customFieldsWrap = new InvoiceCustomFieldsSummaryView(paymentData.getCustomFieldItems()).getCustomsDataView();
            advancedOptions.initCustomFieldSummaryWidget(customFieldsWrap);
        }
        addSystemCustomFields(widgetsMap);
    }

    private void generatePDF(Integer pdfTemplateID) {
        TransactionPDFObject requestObject = new TransactionPDFObject(objectID, pdfTemplateID, isReceivable ? Constants.RECEIVABLE : Constants.PAYABLE, null);
        String pdfURL = CommandConstants.PDF_URL + (isReceivable ? "/batchReceivePaymentViewPDFHandler" : "/batchPayBillViewPDFHandler");
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
    }

    private Widget[] getWidgets(final PaymentData data, boolean paymentIsForeignCurrency) {
        Label customerSupplier = new Label();
        Label description = new Label();
        Label reference = new Label();
        Label invoiceDate = new Label();
        Label invoiceAmount = new Label();
        Label paymentAmountInBase = new Label();
        Label paymentAmountInTr = new Label();
        Label underAmount = new Label();
        Label bankFeeTaxRate = new Label();
        Label bankFeeTaxAmount = new Label();

        paymentAmountInBase.setText(AccountingUtils.getZero());
        paymentAmountInTr.setText(AccountingUtils.getZero());

        description.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        description.getElement().getStyle().setColor("#2C5FE1");
        description.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        if (data.getCrmAccount() != null) {
            customerSupplier.setText(data.getCrmAccount().getName());
        }
        if (data.getInvoiceNumber() != null) {
            description.setText(data.getInvoiceNumber());
            description.addClickHandler(clickEvent -> {
                if (AccountingConstants.RECEIVABLE_PREPAYMENT.equals(data.getType()) || AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(data.getType())) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment|paymentView/" + data.getObjectID());
                } else {
                    if (data.isExpensePayment()) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|previewReport/" + data.getInvoiceID() + "/EXPENSE_VIEW/ACCOUNTING", data.getInvoiceNumber());
                    } else if (data.isManualJournal()) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("manual|summary/" + data.getInvoiceID(), data.getInvoiceNumber());
                    } else if (data.isOpeningBalance()) {
                        //no link
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged((isReceivable ? SALE_INVOICE : PURCHASE_INVOICE) + "|summary/" + data.getInvoiceID());
                    }
                }
            });
        }
        if (data.getReferenceNumber() != null) {
            reference.setText(data.getReferenceNumber());
        }
        if (data.getInvoiceDate() != null) {
            invoiceDate.setText(DateUtils.format(data.getInvoiceDate()));
        }
        if (data.getTotal() != null) {
            invoiceAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
            invoiceAmount.setText(AccountingUtils.get().formatPrice(data.getTotal()));
        }
        if (data.getPaymentAmount() != null) {
            paymentAmountInBase.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
            paymentAmountInBase.setText(AccountingUtils.get().formatPrice(data.getPaymentAmount()));
        }
        if (paymentIsForeignCurrency) {
            paymentAmountInTr.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
            paymentAmountInTr.setText(AccountingUtils.get().formatPrice(data.getPaymentAmountInInvoiceCurrency() != null ? data.getPaymentAmountInInvoiceCurrency() : data.getPaymentAmount()));
        }
        if (data.getUnderPaymentAmount() != null && data.getUnderPaymentAccount() != null) {
            underAmount.setText(AccountingUtils.get().formatPrice(data.getUnderPaymentAmount()));
            underpaymentAccount.setHTML(data.getUnderPaymentAccount().getName());
            underpaymentAccountLabel.setVisible(true);
            underpaymentAccount.setVisible(true);
        }
        if (data.getUnderPaymentTaxRate() != null && data.getUnderPaymentTaxRate().compareTo(BigDecimal.ZERO) > 0) {
            bankFeeTaxRate.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
            bankFeeTaxRate.setText(AccountingUtils.get().formatQty(data.getUnderPaymentTaxRate()) + "%");
        }
        if (data.getUnderPaymentTaxAmount() != null && data.getUnderPaymentTaxAmount().compareTo(BigDecimal.ZERO) > 0) {
            bankFeeTaxAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
            bankFeeTaxAmount.setText(AccountingUtils.get().formatPrice(data.getUnderPaymentTaxAmount()));
        }
        ArrayList<Widget> widgets = new ArrayList<>();
        if (paymentData.isIncludeSubAccountTransaction()) {
            widgets.add(customerSupplier);
        }
        widgets.add(description);
        widgets.add(reference);
        widgets.add(invoiceDate);
        widgets.add(invoiceAmount);
        widgets.add(paymentAmountInBase);
        if (paymentIsForeignCurrency) {
            widgets.add(paymentAmountInTr);
        }
        widgets.add(underAmount);
        widgets.add(bankFeeTaxRate);
        widgets.add(bankFeeTaxAmount);
        return widgets.toArray(new Widget[0]);
    }

    private DynamicTableColumn[] getColumns(boolean paymentIsForeignCurrency) {
        ArrayList<DynamicTableColumn> columns = new ArrayList<>();

        if (paymentData.isIncludeSubAccountTransaction()) {
            columns.add(new DynamicTableColumn(isReceivable ? Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), "clientSupplier", 200));
        }
        columns.add(new DynamicTableColumn(wfmStrings.description(), "description", 200));
        columns.add(new DynamicTableColumn(wfmStrings.reference(), "reference", 150));
        columns.add(new DynamicTableColumn(wfmStrings.date(), "date", 120));
        columns.add(new DynamicTableColumn(wfmStrings.amount(), "invoiceAmount", 150, RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn(wfmStrings.paymentAmount() + "(" + paymentData.getBaseCurrency().getName() + ")", "paymentAmountBC", 150, RIGHT_ALIGN_CELL));
        if (paymentIsForeignCurrency) {
            columns.add(new DynamicTableColumn(wfmStrings.paymentAmount() + "(" + paymentData.getCurrency().getName() + ")", "paymentAmountTC", 150, RIGHT_ALIGN_CELL));
        }
        columns.add(new DynamicTableColumn(accountingStrings.bankFeeDiscount(), "underpaymentAmount", 100));
        columns.add(new DynamicTableColumn(accountingStrings.bankFeeTaxRate(), "bankFeeTaxRate", 80, RIGHT_ALIGN_CELL));
        columns.add(new DynamicTableColumn(accountingStrings.bankFeeTaxAmount(), "bankFeeTaxAmount", 120, RIGHT_ALIGN_CELL));
        return columns.toArray(new DynamicTableColumn[0]);
    }

    @Override
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

    @Override
    public String getPropertyCode() {
        return Constants.PAYBILLS_LIST;
    }
}
