package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.*;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
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
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceCustomFieldsSummaryView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.PdfTemplatePanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.InvoiceAdvancedOptions;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.ReceiptTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.*;


/**
 * User: Dilshod Madrahimov
 * Date: 6/4/2015
 */
public class BankTransferSummaryView extends FooteredView implements AccountingConstants, AccountingCustomFormConstants, Colapse, Constants, FittedContent {
    private static String codeToDnmcClass(String code) {
        if (ItemTableConstants.AMOUNT.equals(code)) return "dnmctbl__amount";
        if (ItemTableConstants.TAX_RATE.equals(code)) return "dnmctbl__rate";
        if (ItemTableConstants.ACCOUNT.equals(code)) return "dnmctbl__account";
        if (ItemTableConstants.NAME.equals(code)) return "dnmctbl__name";
        if (ItemTableConstants.CLIENT.equals(code)) return "dnmctbl__bill";   // BILL TO
        if (ItemTableConstants.DESCRIPTION.equals(code)) return "dnmctbl__desc";
        if (ItemTableConstants.REFERENCE.equals(code)) return "dnmctbl__ref";
        return null;
    }

    private static void normalizeCol(DynamicTableColumn col, String code) {
        // 1) ширины не задаём вообще → не будет инлайновых width=".."
        col.setColumnWidth(null);
        col.setForceWidthInPercent(false);
        col.setPixel(true); // без разницы, когда width=null, но пусть будет явно

        // 2) навешиваем наш «семантический» класс
        String extra = codeToDnmcClass(code);
        if (extra != null) {
            String s = col.getStyle();
            col.setStyle((s != null && !s.isEmpty()) ? (s + " " + extra) : extra);
        }
    }

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingServiceAsync accountingService = AccountingService.App.get();
    BigDecimal subTotalAmount = ZERO;
    private Integer objectID;
    private WfmButton2 deleteButton, editButton;
    private SplitButton printPdfSplitButton;
    private NewManualTransaction transactionItem;
    private HTMLPanel htmlPanel;
    private final HashMap<String, Widget> widgetsMap = new HashMap<>();
    private String viewName;
    private Integer transferType;
    private PdfTemplatePanel pdfTemplatePanel;
    private NoteHistoryWidget noteHistoryWidget;
    private boolean hasAccountingBeforeBlockDate;
    private FooterUploadPanel footerUploadPanel;
    private KpiSwitcher postedDate;
    private InvoiceAdvancedOptions advancedOptions;
    private MaterialLink showMoreLink;
    private LinkedList<String> itemColumns;
    private DynamicTable itemsTable;
    // for Add form
    //[0] -  add, edit, view
    //[1] -  RECEIVE_MONEY, SPEND_MONEY, CASH_RECEIPT, CASH_PAYMENT
    //[2] -  relatedProject or relatedBankAccount

    //for Edit-View forms
    // [0]  - objectId
    // [1]  - RECEIVE_MONEY, SPEND_MONEY, CASH_RECEIPT, CASH_PAYMENT
    // [2] -  relatedProject or relatedBankAccount

    public BankTransferSummaryView(String[] params) {
        super("summary", Property.get(Constants.BANKACCOUNT, accountingStrings.bankAccountTransactions(), wfmStrings.bankAccount()));
        if (params.length > 1 && !"add".equals(params[0])) {
            objectID = Integer.valueOf(params[0]);
            viewName = params[1];
        }
    }

    @Override
    protected Widget onInitialize() {
        loadData();
        return null;
    }

    private void loadData() {
        if (objectID != null) {
            LoadingPanel.loading(true);
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setObjectId(objectID);
            accountingService.getBankTransferData(fp, new AsyncCallback<NewManualTransaction>() {
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void onSuccess(NewManualTransaction result) {
                    transactionItem = result;
                    transferType = result.getTransferType();
                    viewName = RECEIVE_MONEY.equals(transferType) ? accountingStrings.receiveMoney() : SPEND_MONEY.equals(transferType) ? wfmStrings.bankPayment() : CASH_RECEIPT.equals(transferType) ? wfmStrings.cashReceipt() : wfmStrings.cashPayment();
                    hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(transactionItem.getDate().getNonConvertedDate()));
                    initForm();
                    initCustomFields();
                    initPdfTemplates();
                    htmlPanel = new WftHTMLPanel(result.getLayoutHtml(), widgetsMap).getContainer();
                    htmlPanel.setStyleName("add-form invoice-form");
                    htmlPanel.add(createFooter());
                    add(htmlPanel);
                    LoadingPanel.loading(false);

                }
            });
        }
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return BankTransferSummaryView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return BankTransferSummaryView.this.getFooterRightSideWidgets();
            }
        });
    }


    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightSideWidgets = new ArrayList<>();

        boolean hasPermissionToEdit = (CASH_RECEIPT.equals(transferType) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT) ||
                CASH_PAYMENT.equals(transferType) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT) ||
                RECEIVE_MONEY.equals(transferType) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT) ||
                SPEND_MONEY.equals(transferType) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT));

        boolean hasPermissionToDelete = (CASH_RECEIPT.equals(transferType) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE) ||
                CASH_PAYMENT.equals(transferType) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE) ||
                RECEIVE_MONEY.equals(transferType) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE) ||
                SPEND_MONEY.equals(transferType) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE));

        editButton = new WfmButton2(wfmStrings.edit(), BTN_DEFAULT_OUTLINE);
        if (!hasAccountingBeforeBlockDate && hasPermissionToEdit) {
            editButton.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|edit/" + objectID + "/" + viewName));
            Div editWrapper = new Div();
            editWrapper.add(editButton);
            rightSideWidgets.add(editWrapper);
        }

        deleteButton = new WfmButton2(wfmStrings.delete(), BTN_DEFAULT_OUTLINE);
        if (!hasAccountingBeforeBlockDate && !transactionItem.isUsed() && hasPermissionToDelete) {
            deleteButton.setVisible(!Constants.PAYMENT_TRANSACTION.equals(transactionItem.getTransactionType()) && !transactionItem.isUsed());

            deleteButton.addClickHandler(clickEvent -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        accountingService.deleteBankTransfer(objectID, Constants.BANK_TRANSFER_TRANSACTION, new AbstractAsyncCallback<Void>() {
                            public void failure(Throwable caught) {
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            public void success(Void result) {
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.accountTransaction()), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_ACCOUNT_TRANSACTION_DELETED, result, BankTransferSummaryView.this);
                                closeTab();
                            }
                        });
                    }
                });
                messageBox.open();
            });
            Div deleteWrapper = new Div();
            deleteWrapper.add(deleteButton);
            rightSideWidgets.add(deleteWrapper);
        }
        Div pdfWrapper = new Div();
        pdfWrapper.add(printPdfSplitButton);
        rightSideWidgets.add(pdfWrapper);

//        closeButton = new WfmButton2(wfmStrings.close(), BTN_DEFAULT_OUTLINE);
//        closeButton.addClickHandler(event -> closeTab());
//        Div closeWrapper = new Div();
//        closeWrapper.add(closeButton);
//        rightSideWidgets.add(closeWrapper);

        return rightSideWidgets;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = new ArrayList<>();

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);


        informer.setInitialClasses("informer-item history-notes-container");
        footerUploadPanel.setInitialClasses("informer-item history-notes-container");
        leftSideWidgets.add(informer);
        leftSideWidgets.add(footerUploadPanel);

        if (transactionItem != null && transactionItem.getJournalID() != null && Utils.hasPermission(ACCOUNTING_JOURNAL_REPORT)) {
            FooterInformer showJournal = new FooterInformer(SvgEnum.wallet, wfmStrings.showJournal(), null);
            showJournal.addClickHandler(clickEvent -> {
                SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + transactionItem.getJournalID(), accountingStrings.reportView() + ": " + transactionItem.getNumber(), accountingStrings.reportView() + ": " + transactionItem.getNumber());
            });
            showJournal.setBadgeCount(1);

            leftSideWidgets.add(showJournal);
        }

        //add billable expese to footer
        return leftSideWidgets;
    }

    private void initForm() {
        itemsTable = new DynamicTable(getColumns(), false);
        itemsTable.addStyleName("dnmctbl dnmctbl--vatop truncTxtCatch");
        itemsTable.setBorderWidth(0);

        noteHistoryWidget = new NoteHistoryWidget(cb -> AccountingService.App.get()
                .getBankTransferHistoryNotes(objectID, BANK_TRANSFER, cb));
        setnotesPanelListeners();

        initWidgetsMap();
        initDynamicTable();
//        initAttachmentTable();
        Scheduler.get().scheduleDeferred(() -> {
            clearTdVerticalAlign(); // remove vertical-align: middle;
            addHeaderClasses();
//            applyHeaderClassesViaFlex();
//            TblDynamicSmartTxtCell.process(itemsTable);
        });

        new Timer() {
            int tries = 0;
            @Override public void run() {
                clearTdVerticalAlign();
                addHeaderClasses();
//                applyHeaderClassesViaFlex();
//                TblDynamicSmartTxtCell.process(itemsTable);
                if (++tries >= 5) cancel();
            }
        }.scheduleRepeating(150);

        initTotalTable();
    }

    private void addHeaderClasses() {
        if (itemsTable == null || itemColumns == null || itemColumns.isEmpty()) return;

        // 0-я строка таблицы у DynamicTable — «шапка» (первая строка tbody)
        int cols = itemColumns.size();
        for (int c = 0; c < cols; c++) {
            String klass = codeToDnmcClass(itemColumns.get(c));
            if (klass == null || klass.isEmpty()) continue;

            // Класс — на саму ячейку заголовка
            itemsTable.getFlexCellFormatter().addStyleName(0, c, klass);

            // (опционально) если хочешь убрать дубликаты с div — снимем класс с внутренних дивов
            com.google.gwt.dom.client.Element td = itemsTable.getFlexCellFormatter().getElement(0, c);
            if (td != null) {
                com.google.gwt.dom.client.NodeList<com.google.gwt.dom.client.Element> divs = td.getElementsByTagName("div");
                for (int i = 0; i < divs.getLength(); i++) {
                    com.google.gwt.dom.client.Element div = divs.getItem(i);
                    String divCls = div.getClassName() != null ? div.getClassName() : "";
                    // удаляем только наши dnmctbl__* классы, не трогая dynamictable-header
                    if (divCls.contains("dnmctbl__")) {
                        // простой способ снять: заменить на строку без dnmctbl__*
                        div.setClassName(divCls.replaceAll("\\bdnmctbl__\\S+\\b", "").trim());
                    }
                }
            }
        }
    }


    private void clearTdVerticalAlign() {
        Element root = itemsTable.getElement();

        NodeList<Element> tds = root.getElementsByTagName("td");
        for (int i = 0; i < tds.getLength(); i++) {
            Element e = tds.getItem(i);
            e.getStyle().clearProperty("verticalAlign");  // style="vertical-align: ..."
            e.removeAttribute("valign");                  // на всякий случай, если где-то используют старый valign
        }

        NodeList<Element> ths = root.getElementsByTagName("th");
        for (int i = 0; i < ths.getLength(); i++) {
            Element e = ths.getItem(i);
            e.getStyle().clearProperty("verticalAlign");
            e.removeAttribute("valign");
        }
    }

    private void initCustomFields() {
        if (transactionItem.getCustomFieldItems() != null && transactionItem.getCustomFieldItems().size() > 0) {
            MaterialPanel customFieldsWrap = new InvoiceCustomFieldsSummaryView(transactionItem.getCustomFieldItems()).getCustomsDataView();
            advancedOptions.initCustomFieldSummaryWidget(customFieldsWrap);

            FormGroup showMoreField = new FormGroup(showMoreLink);
            showMoreField.setLabel("&nbsp;");
            widgetsMap.put(INPUT_SHOW_MORE, showMoreField);
        }
    }

    private void setnotesPanelListeners() {
        noteHistoryWidget.setSaveIntoDatabase((historyListItem) -> {
            LoadingPanel.loading(true);
            AccountingService.App.get().createBankTransferNote(objectID, historyListItem, new AsyncCallback<Integer>() {
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
                accountingService.deleteBankTransferNote(historyListItem.getObjectID(), new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.warn(wfmStrings.errorOccuredWhileDeleting());
                    }

                    @Override
                    public void onSuccess(Boolean integer) {
                        LoadingPanel.loading(false);
                    }
                });
            }
        });
    }

    private void initPdfTemplates() {
        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);

        List<SplitButtonItem> pdfCommandSubItems = new ArrayList<>();
        if(Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_SPEND_PDF)){
            Integer defaultTemplateId = null;
            if (transactionItem.getPdfTemplateList() != null
                    && transactionItem.getPdfTemplateList().getItems() != null
                    && transactionItem.getPdfTemplateList().getItems().length > 0) {
                for (SelectItem pdfItem : transactionItem.getPdfTemplateList().getItems()) {
                    if (pdfItem.isDefaultSelected()) {
                        defaultTemplateId = pdfItem.getId();
                    }
                    pdfCommandSubItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(pdfItem.getId())));
                }
                pdfTemplatePanel = new PdfTemplatePanel(transactionItem);
                FormGroup pdfTemplateBox = new FormGroup(accountingStrings.pdfTemplate(), wrapWidgetToFormControl(pdfTemplatePanel));
                widgetsMap.put(INPUT_PDF_TEMPLATE, pdfTemplateBox);
            }
            Integer finalDefaultTemplateId = defaultTemplateId;
            SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(finalDefaultTemplateId), true);
            pdfVersion.ensureDebugId("pdfVersionItem");
            pdfCommandSubItems.add(pdfVersion);
        }
        if (Utils.hasRoles(Constants.ADMIN)) {
            pdfCommandSubItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", "Customize", new Command() {
                @Override
                public void execute() {
                    String pdfType = "";
                    if (CASH_RECEIPT.equals(transferType)) {
                        pdfType = PdfTemplateTypeEnum.CASH_RECEIPT.name();
                    } else if (CASH_PAYMENT.equals(transferType)) {
                        pdfType = PdfTemplateTypeEnum.CASH_PAYMENT.name();
                    } else if (RECEIVE_MONEY.equals(transferType)) {
                        pdfType = PdfTemplateTypeEnum.BANK_RECEIPT.name();
                    } else if (SPEND_MONEY.equals(transferType)) {
                        pdfType = PdfTemplateTypeEnum.BANK_PAYMENT.name();
                    }
                    Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + pdfType);
                }
            }));
        }
        printPdfSplitButton.addItemList(pdfCommandSubItems);
    }

    private void generatePDF(Integer pdfTemplateID) {
        TransactionPDFObject requestObject = new TransactionPDFObject(objectID, pdfTemplateID, viewName, transferType);
        String pdfURL = CommandConstants.PDF_URL + "/spendMoneyViewPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(htmlPanel, pdfURL, parametrs, "_blank");
    }

    private DynamicTableColumn[] getColumns() {
        LinkedList<DynamicTableColumn> columnsList = new LinkedList<>();
        itemColumns = new LinkedList<>();

        if (transactionItem != null && transactionItem.getCustomItemColumns() != null && transactionItem.getCustomItemColumns().length > 0) {
            DynamicTableColumn dynamicTableColumn;
            for (ColumnConfigs column : transactionItem.getCustomItemColumns()) {
                switch (column.getCode()) {

                    case ItemTableConstants.ACCOUNT: {
                        dynamicTableColumn = new DynamicTableColumn(
                                column.isChanged() ? column.getTitle() : wfmStrings.account(),
                                ItemTableConstants.ACCOUNT,
                                /* width */ null
                        );
                        normalizeCol(dynamicTableColumn, ItemTableConstants.ACCOUNT);
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(ItemTableConstants.ACCOUNT);
                        break;
                    }

                    case ItemTableConstants.TAX_RATE: {
                        dynamicTableColumn = new DynamicTableColumn(
                                column.isChanged() ? column.getTitle() : wfmStrings.taxRate(),
                                ItemTableConstants.TAX_RATE,
                                /* width */ null
                        );
                        normalizeCol(dynamicTableColumn, ItemTableConstants.TAX_RATE);
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(ItemTableConstants.TAX_RATE);
                        break;
                    }

                    case ItemTableConstants.DESCRIPTION: {
                        dynamicTableColumn = new DynamicTableColumn(
                                column.isChanged() ? column.getTitle() : wfmStrings.description(),
                                ItemTableConstants.DESCRIPTION,
                                /* width */ null
                        );
                        normalizeCol(dynamicTableColumn, ItemTableConstants.DESCRIPTION);
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(ItemTableConstants.DESCRIPTION);
                        break;
                    }

                    case ItemTableConstants.REFERENCE: {
                        dynamicTableColumn = new DynamicTableColumn(
                                column.isChanged() ? column.getTitle() : wfmStrings.reference(),
                                ItemTableConstants.REFERENCE,
                                /* width */ null
                        );
                        normalizeCol(dynamicTableColumn, ItemTableConstants.REFERENCE);
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(ItemTableConstants.REFERENCE);
                        break;
                    }

                    case ItemTableConstants.AMOUNT: {
                        dynamicTableColumn = new DynamicTableColumn(
                                column.isChanged() ? column.getTitle() : wfmStrings.amount(),
                                ItemTableConstants.AMOUNT,
                                /* width */ null
                        );
                        // добавим наш класс поверх имеющегося стиля
                        normalizeCol(dynamicTableColumn, ItemTableConstants.AMOUNT);
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(ItemTableConstants.AMOUNT);
                        break;
                    }

                    case ItemTableConstants.NAME: {
                        dynamicTableColumn = new DynamicTableColumn(
                                column.isChanged() ? column.getTitle() : wfmStrings.name(),
                                ItemTableConstants.NAME,
                                /* width */ null
                        );
                        normalizeCol(dynamicTableColumn, ItemTableConstants.NAME);
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(ItemTableConstants.NAME);
                        break;
                    }

                    case ItemTableConstants.CLIENT: {
                        if (SPEND_MONEY.equals(transferType) || CASH_PAYMENT.equals(transferType)) {
                            dynamicTableColumn = new DynamicTableColumn(
                                    column.isChanged() ? column.getTitle() : accountingStrings.billing(),
                                    ItemTableConstants.CLIENT,
                                    /* width */ null
                            );
                            normalizeCol(dynamicTableColumn, ItemTableConstants.CLIENT);
                            columnsList.add(dynamicTableColumn);
                            itemColumns.add(ItemTableConstants.CLIENT);
                        }
                        break;
                    }

                    case ItemTableConstants.PROJECT: {
                        if (Utils.isProjectInLineItemEnable()) {
                            dynamicTableColumn = new DynamicTableColumn(
                                    column.isChanged() ? column.getTitle() : Property.get(Constants.PROJECT, wfmStrings.project()),
                                    ItemTableConstants.PROJECT,
                                    /* width */ null
                            );
                            // можно дать общий класс, либо ничего не добавлять
                            normalizeCol(dynamicTableColumn, ItemTableConstants.PROJECT);
                            columnsList.add(dynamicTableColumn);
                            itemColumns.add(ItemTableConstants.PROJECT);
                        }
                        break;
                    }

                    case ItemTableConstants.DEPARTMENT: {
                        if (AccountingUtils.get().isEnableAccountingDepartmentRelation()) {
                            dynamicTableColumn = new DynamicTableColumn(
                                    column.isChanged() ? column.getTitle() : Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()),
                                    ItemTableConstants.DEPARTMENT,
                                    /* width */ null
                            );
                            normalizeCol(dynamicTableColumn, ItemTableConstants.DEPARTMENT);
                            columnsList.add(dynamicTableColumn);
                            itemColumns.add(ItemTableConstants.DEPARTMENT);
                        }
                        break;
                    }

                    default: {
                        dynamicTableColumn = new DynamicTableColumn(
                                column.getTitle(),
                                column.getCode(),
                                /* width */ null
                        );
                        // для любых других колонок просто не задаём ширину и при желании подвесим общий класс
                        normalizeCol(dynamicTableColumn, column.getCode());
                        columnsList.add(dynamicTableColumn);
                        itemColumns.add(column.getCode());
                        break;
                    }
                }
            }

        } else {
    DynamicTableColumn c;

    c = new DynamicTableColumn(wfmStrings.account(), ItemTableConstants.ACCOUNT, /*width*/ null);
    normalizeCol(c, ItemTableConstants.ACCOUNT);
    columnsList.add(c);
    itemColumns.add(ItemTableConstants.ACCOUNT);

    c = new DynamicTableColumn(wfmStrings.taxRate(), ItemTableConstants.TAX_RATE, /*width*/ null);
    normalizeCol(c, ItemTableConstants.TAX_RATE);
    columnsList.add(c);
    itemColumns.add(ItemTableConstants.TAX_RATE);

    c = new DynamicTableColumn(wfmStrings.description(), ItemTableConstants.DESCRIPTION, /*width*/ null);
    normalizeCol(c, ItemTableConstants.DESCRIPTION);
    columnsList.add(c);
    itemColumns.add(ItemTableConstants.DESCRIPTION);

    c = new DynamicTableColumn(wfmStrings.reference(), ItemTableConstants.REFERENCE, /*width*/ null);
    normalizeCol(c, ItemTableConstants.REFERENCE);
    columnsList.add(c);
    itemColumns.add(ItemTableConstants.REFERENCE);

    c = new DynamicTableColumn(wfmStrings.amount(), ItemTableConstants.AMOUNT, /*width*/ null);
    normalizeCol(c, ItemTableConstants.AMOUNT);
    columnsList.add(c);
    itemColumns.add(ItemTableConstants.AMOUNT);

    c = new DynamicTableColumn(wfmStrings.name(), ItemTableConstants.NAME, /*width*/ null);
    normalizeCol(c, ItemTableConstants.NAME);
    columnsList.add(c);
    itemColumns.add(ItemTableConstants.NAME);

    if (SPEND_MONEY.equals(transferType) || CASH_PAYMENT.equals(transferType)) {
        c = new DynamicTableColumn(accountingStrings.billing(), ItemTableConstants.CLIENT, /*width*/ null);
        normalizeCol(c, ItemTableConstants.CLIENT);
        columnsList.add(c);
        itemColumns.add(ItemTableConstants.CLIENT);
    }

    if (Utils.isProjectInLineItemEnable()) {
        c = new DynamicTableColumn(Property.get(Constants.PROJECT, wfmStrings.project()), ItemTableConstants.PROJECT, /*width*/ null);
        normalizeCol(c, ItemTableConstants.PROJECT);
        columnsList.add(c);
        itemColumns.add(ItemTableConstants.PROJECT);
    }
    if (AccountingUtils.get().isEnableAccountingDepartmentRelation()) {
        c = new DynamicTableColumn(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), ItemTableConstants.DEPARTMENT, /*width*/ null);
        normalizeCol(c, ItemTableConstants.DEPARTMENT);
        columnsList.add(c);
        itemColumns.add(ItemTableConstants.DEPARTMENT);
    }
}

        return columnsList.toArray(new DynamicTableColumn[columnsList.size()]);
    }

    private void initWidgetsMap() {

        widgetsMap.put(LABEL_TITLE, getWidgetAsFormControl(viewName));

        if (RECEIVE_MONEY.equals(transferType) || SPEND_MONEY.equals(transferType)) {
            FormGroup bankAccountItem = new FormGroup(Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccount()), getWidgetAsFormControl(transactionItem.getBankAccountItem() != null ? transactionItem.getBankAccountItem().getName() : ""));
            widgetsMap.put(INPUT_ACCOUNT, bankAccountItem);
        } else if (CASH_RECEIPT.equals(transferType) || CASH_PAYMENT.equals(transferType)) {
            FormGroup bankAccountItem = new FormGroup(accountingStrings.cashAccount(), getWidgetAsFormControl(transactionItem.getCashAccount() != null ? transactionItem.getCashAccount().getName() : ""));
            widgetsMap.put(INPUT_ACCOUNT, bankAccountItem);
        }

        FormGroup referenceField = new FormGroup(wfmStrings.reference(), getWidgetAsFormControl(transactionItem.getReference()));
        widgetsMap.put(INPUT_REFERENCE, referenceField);

        FormGroup narrationField = new FormGroup(wfmStrings.narration(), getWidgetAsFormControl(transactionItem.getNarration()));
        widgetsMap.put(INPUT_TO_FROM, narrationField);

        FormGroup dateField = new FormGroup(wfmStrings.date(), getWidgetAsFormControl(DateUtils.format(transactionItem.getDate())));
        widgetsMap.put(INPUT_DATE, dateField);


        if (!Utils.isProjectInLineItemEnable() && Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            FormGroup projectItem = new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), getWidgetAsFormControl(transactionItem.getProject() != null ? transactionItem.getProject().getName() : wfmStrings.notAvailable()));
            widgetsMap.put(INPUT_PROJECT, projectItem);
        }

        FormGroup checkNumberField = new FormGroup(accountingStrings.checkNumber(), getWidgetAsFormControl(transactionItem.getCheckNumber() != null ? transactionItem.getCheckNumber() : wfmStrings.notAvailable()));
        widgetsMap.put(INPUT_CHECK_NUMBER, checkNumberField);

        FormGroup numberField = new FormGroup(wfmStrings.number(), getWidgetAsFormControl(transactionItem.getNumber() != null ? transactionItem.getNumber() : ""));
        widgetsMap.put(INPUT_NUMBER, numberField);

        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
            HTML rateLabel = new HTML("1 " + transactionItem.getBaseCurrency().getName() + " = " +
                    AccountingUtils.get().formatExRate(transactionItem.getExchangeRate()) + " " + transactionItem.getCurrency().getName());
            FormGroup exchangeRateField = new FormGroup(wfmStrings.exchangeRate(), wrapWidgetToFormControl(rateLabel));
            widgetsMap.put(INPUT_EXCHANGE_RATE, exchangeRateField);
        }

        HTML amountType = new HTML();
        if (transactionItem.getTaxCalculationType() == 0) {
            amountType.setText("No Tax");
        } else if (transactionItem.getTaxCalculationType() == 1) {
            amountType.setText("Tax Inclusive");
        } else {
            amountType.setText("Tax Exclusive");
        }
        FormGroup amountField = new FormGroup(accountingStrings.amounts(), getWidgetAsFormControl(amountType.getText()));
        widgetsMap.put(INPUT_TAX_CALC_TYPE, amountField);

        postedDate = new KpiSwitcher();
        if (transactionItem.isEnabledPostDatedTransaction() && transactionItem.isPostDatedTransaction()) {
            postedDate.setValue(true);
        }
        postedDate.setEnabled(false);
        FormGroup postDateField = new FormGroup(wfmStrings.postDated(), postedDate);
        widgetsMap.put(INPUT_POST_DATED, postDateField);
        GRow gRow = new GRow();
        GColumn col = new GColumn();
        col.add(itemsTable);
        gRow.add(col);
        widgetsMap.put(INPUT_ITEM_TABLE, gRow);

        footerUploadPanel = new FooterUploadPanel(Constants.F_BANK_TRANSFER, transactionItem.getObjectId(), true);

        advancedOptions = createAdvancedOptions();
        showMoreLink = new MaterialLink(wfmStrings.showAdditionalFields());
        showMoreLink.addStyleName("btn-flat BankTransferSummaryView");
        showMoreLink.addClickHandler(ch -> showAdvancedOptions(wfmStrings.additionalFields(), advancedOptions));
    }

    private InvoiceAdvancedOptions createAdvancedOptions() {
        return new InvoiceAdvancedOptions(() -> {
            List<Widget> result = new ArrayList<>();
            return result;
        }, false);
    }

/*    private void applyHeaderClassesViaFlex() {
        if (itemsTable == null || itemColumns == null || itemColumns.isEmpty()) return;

        int cols = itemColumns.size();
        for (int c = 0; c < cols; c++) {
            String klass = codeToDnmcClass(itemColumns.get(c));
            if (klass == null || klass.isEmpty()) continue;

            // 1) класс на TD (ячейку заголовка)
            itemsTable.getFlexCellFormatter().addStyleName(0, c, klass);

            // 2) опционально: добавить класс и на внутренний div.dynamictable-header
//            com.google.gwt.dom.client.Element td = itemsTable.getFlexCellFormatter().getElement(0, c);
//            if (td != null) {
//                com.google.gwt.dom.client.NodeList<com.google.gwt.dom.client.Element> divs = td.getElementsByTagName("div");
//                if (divs != null && divs.getLength() > 0) {
//                    com.google.gwt.dom.client.Element div = divs.getItem(0);
//                    String divCls = div.getClassName() != null ? div.getClassName() : "";
//                    if (divCls.contains("dynamictable-header")) {
//                        div.addClassName(klass);
//                    }
//                }
//            }
        }
    }*/


    private void initDynamicTable() {
        itemsTable.clear();

        for (NewManualTransactionItem item : transactionItem.getItems()) {
            final Map<String, Widget> itemWidgetsMap = new LinkedHashMap<>();
            for (String column : itemColumns) {
                switch (column) {
                    case ItemTableConstants.ACCOUNT:
                        Label account = new Label();
                        account.setText(item.getAccountItem() != null ? item.getAccountItem().getName() : "");
                        itemWidgetsMap.put(ItemTableConstants.ACCOUNT, account);
                        break;
                    case ItemTableConstants.TAX_RATE:
                        Label taxRate = new Label();
                        taxRate.setText(item.getTaxItem() != null ? item.getTaxItem().getName() : "");
                        itemWidgetsMap.put(ItemTableConstants.TAX_RATE, taxRate);
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        Label description = new Label();
                        description.setText(item.getDescription());
                        itemWidgetsMap.put(ItemTableConstants.DESCRIPTION, description);
                        break;
                    case ItemTableConstants.REFERENCE:
                        Label reference = new Label();
                        reference.setText(item.getReference());
                        itemWidgetsMap.put(ItemTableConstants.REFERENCE, reference);
                        break;
                    case ItemTableConstants.AMOUNT:
                        Label amount = new Label();
                        amount.setText(AccountingUtils.get().format(item.getAmount()));
                        itemWidgetsMap.put(ItemTableConstants.AMOUNT, amount);
                        break;
                    case ItemTableConstants.NAME:
                        Label name;
                        if (SALARY_PAYABLE.equals(item.getAccountItem().getAccountKey())) {
                            name = new Label(item.getEmployee() != null ? item.getEmployee().getName() : "");
                        } else {
                            name = new Label(item.getCustomerOrSupplier() != null ? item.getCustomerOrSupplier().getName() : "");
                        }
                        itemWidgetsMap.put(ItemTableConstants.NAME, name);
                        break;
                    case ItemTableConstants.CLIENT:
                        if (SPEND_MONEY.equals(transferType) || CASH_PAYMENT.equals(transferType)) {
                            Widget clientWidget;
                            final String clientLabel = item.getClient() != null && item.getClient().getName() != null
                                    ? item.getClient().getName()
                                    : "";

                            if (item.getInvoiceId() != null) {
                                clientWidget = new SimpleLink(clientLabel);
                                ((SimpleLink) clientWidget).addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|7Csummary/" + item.getInvoiceId()));
                            } else {
                                clientWidget = new Label(clientLabel);
                            }
                            itemWidgetsMap.put(ItemTableConstants.CLIENT, clientWidget);
                        }
                        break;
                    case ItemTableConstants.PROJECT:
                        if (Utils.isProjectInLineItemEnable()) {
                            Label project = new Label();
                            project.setText(item.getProject() != null ? item.getProject().getName() : "");
                            itemWidgetsMap.put(ItemTableConstants.PROJECT, project);
                        }
                        break;
                    case ItemTableConstants.DEPARTMENT:
                        if (AccountingUtils.get().isEnableAccountingDepartmentRelation()) {
                            Label department = new Label();
                            department.setText(item.getDepartment() != null ? item.getDepartment().getName() : "");
                            itemWidgetsMap.put(ItemTableConstants.DEPARTMENT, department);
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
                        itemWidgetsMap.put(column, label_);
                        break;
                }
            }

            subTotalAmount = subTotalAmount.add(item.getAmount()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);

            itemsTable.addRow(item.getObjectId(), itemWidgetsMap.values().toArray(new Widget[]{}));
        } // <-- Цикл for заканчивается здесь
    }

    private void initTotalTable() {

        HTML subTotalLabel = new HTML(wfmStrings.subtotal());
        HTML baseSubTotalLabel = new HTML(wfmStrings.subtotal());
        HTML baseVatLabel = new HTML(accountingStrings.vat());
        HTML vatLabel = new HTML(accountingStrings.vat());
        HTML baseTotalLabel = new HTML(wfmStrings.total());
        HTML totalLabel = new HTML(wfmStrings.total());

        vatLabel.getElement().getStyle().setTextTransform(Style.TextTransform.UPPERCASE);
        baseVatLabel.getElement().getStyle().setTextTransform(Style.TextTransform.UPPERCASE);


        HTML baseSubTotal = new HTML(AccountingUtils.get().formatPrice(transactionItem.getSubtotal().divide(transactionItem.getExchangeRate(), RoundingMode.HALF_UP)));
        HTML subTotal = new HTML(AccountingUtils.get().formatPrice(transactionItem.getSubtotal()));
        HTML baseVatTotal = new HTML(AccountingUtils.get().formatPrice(transactionItem.getTaxTotal()));
        HTML vatTotal = new HTML(AccountingUtils.get().formatPrice(transactionItem.getTaxForeignTotal()));
        HTML baseTotalTotal = new HTML(AccountingUtils.get().formatPrice(transactionItem.getTotal().divide(transactionItem.getExchangeRate(), RoundingMode.HALF_UP)));
        HTML totalTotal = new HTML(AccountingUtils.get().formatPrice(transactionItem.getTotal()));

        boolean inBaseCurrency = transactionItem.getBaseCurrency().getName().equals(transactionItem.getCurrency().getName());

        totalLabel.setText(accountingMessages.dynamicTotal(transactionItem.getCurrency().getName()));
        baseTotalLabel.setText(accountingMessages.dynamicTotal(transactionItem.getBaseCurrency().getName()));
        subTotalLabel.setText(wfmStrings.subtotal());
        vatLabel.setText(accountingStrings.vat());


        ReceiptTable totalTable = new ReceiptTable();

        totalTable.clear();
        totalTable.removeShippingBody();

        totalTable.addItem(subTotalLabel, (!inBaseCurrency ? subTotal : baseSubTotal));
        totalTable.addItem(vatLabel, (!inBaseCurrency ? vatTotal : baseVatTotal));


        if (!inBaseCurrency) {
            totalTable.addGrossItem(totalLabel, totalTotal);
        }
        totalTable.addGrossItem(baseTotalLabel, baseTotalTotal);
        widgetsMap.put(INPUT_TOTALS_TABLE, totalTable);
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
}
