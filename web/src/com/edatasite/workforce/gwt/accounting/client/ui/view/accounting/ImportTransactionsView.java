package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.DateFormatParseException;
import com.edatasite.workforce.gwt.accounting.client.bundles.AccountingReportsImageBundles;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccountAttachment;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccountImportStatementData;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccountStatementTO;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccountTransactionMapper;
import com.edatasite.workforce.gwt.accounting.client.rpc.CsvTemplateItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.FindMatchFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.listener.DropdownListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDialogContent;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 12, 2010
 * Time: 4:54:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportTransactionsView extends FooteredView implements CommandConstants, Colapse {

    private final AccountingServiceAsync accountingService = AccountingService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingReportsImageBundles reportsImageBundle = AccountingReportsImageBundles.App.get();

    public static final String SAMPLE_IMPORT_TRANSACTION_CSV = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Import_Transaction_Template.csv";

    private WfmFormPanel form;
    private FlexTable filesTable;
    private WfmButton2 importButton;
    private WfmButton2 cancel;
    private FileUpload fileUpload;
    private FlexTable importedStatementsTable;
    private final Map<String, KpiCheckBox> checkboxs = new HashMap<>();

    private final Integer bankAccountID;
    private final String bankAccountName;

    private Integer loadedAttachmentID;

    private WfmDropdown patternDropdown;

    private String datePattern;

    //reconcile popup
    private FindMatchFilterData filterData;
    private KpiModal shell;
    private DynamicTable dynamicTable;
    private WfmButton2 reconcileButton;
    private ScrollPanel scrollPanel;
    private ArrayList<Integer> selected;
    private HTML amountMatchHTML;
    private HTML totalAmountHTML;
    //reconcile popup

    public ImportTransactionsView(Integer bankAccountID, String[] params) {
        super("importTransactions", accountingStrings.importTransactions());
        this.bankAccountID = bankAccountID;
        this.bankAccountName = params.length >= 2 ? params[1] : "";
    }

    protected Widget onInitialize() {
        initUploadForm();
        ScrollPanel filesContainer = new ScrollPanel();
        filesContainer.setHeight("200px");
        filesTable = new FlexTable();
        filesTable.setCellSpacing(5);
        filesContainer.add(filesTable);

        loadFiles();

        importedStatementsTable = new FlexTable();
        importedStatementsTable.setCellSpacing(0);
        importedStatementsTable.setStyleName("bankStatementLine");

        patternDropdown = new WfmDropdown(false, true);
        patternDropdown.setWidth("150px");
        patternDropdown.addItems(new SelectItem[]{
                new SelectItem(1, "dd MMM yyyy"),
                new SelectItem(2, "dd-MMM-yyyy"),
                new SelectItem(3, "dd/MMM/yyyy"),
                new SelectItem(4, "dd.MMM.yyyy"),
                new SelectItem(5, "MM/dd/yyyy"),
                new SelectItem(6, "dd/MM/yy"),
                new SelectItem(7, "dd-MM-yy"),
                new SelectItem(8, "dd.MM.yy"),
                new SelectItem(9, "dd MM yy"),
                new SelectItem(10, "dd-MMM-yy"),
                new SelectItem(11, "dd/MM/yyyy"),
                new SelectItem(12, "dd.MM.yyyy"),
                new SelectItem(13, "dd-MM-yyyy"),
                new SelectItem(14, "dd MM yyyy"),
                new SelectItem(15, "dd MMMM yyyy"),
                new SelectItem(16, "yyyy-MM-dd"),
                new SelectItem(17, "yyyy/MM/dd"),
                new SelectItem(18, "yyyy.MM.dd"),
                new SelectItem(19, "yyyy MM dd"),
                new SelectItem(20, "yy/MM/dd"),
                new SelectItem(21, "yy-MM-dd"),
                new SelectItem(22, "MM/dd/yyyy"),
                new SelectItem(23, "MM/dd/yy")
        });
        patternDropdown.setSelected(11);


        HorizontalPanel hp = new HorizontalPanel();
        hp.setStyleName("mod_table--cellpadding");
        hp.add(form);
        hp.add(filesContainer);


        FlowPanel panel = new FlowPanel();
        panel.setStyleName("section-box box-bg--1 file--ImportTransactionsView");
        VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(5);
        vp.setWidth("100%");
        vp.add(hp);
        vp.add(importedStatementsTable);
        panel.add(vp);
        panel.add(createFooter());
        add(panel);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, ImportTransactionsView.this, (sender, args) -> loadStatements(loadedAttachmentID));
        return null;
    }

    // TEST
    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return ImportTransactionsView.this.getButtonGroup();
            }
        });
    }

    private List<Widget> getButtonGroup() {
        List<Widget> rigthSideWidgets = new ArrayList<>();

        Div saveWrapper = new Div();
        Div cancelWrapper = new Div();

        saveWrapper.add(importButton);

        rigthSideWidgets.add(saveWrapper);
        rigthSideWidgets.add(cancelWrapper);
        return rigthSideWidgets;
    }

    public String getIconStyle() {
        return "accountMark manual-journals";  //To change body of implemented methods use File | Settings | File Templates.
    }


    private void initUploadForm() {
        form = new WfmFormPanel("/CreateBankAccountFileHandler");
        form.addFormHandler(new FormHandler() {
            public void onSubmit(FormSubmitEvent event) {
            }

            public void onSubmitComplete(FormSubmitCompleteEvent event) {
                LoadingPanel.loading(false);
                Info.show(form.getErrorString() != null ? wfmStrings.messParseErrorCompareFile() : Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.file()), Info.Type.INFO);
                loadFiles();
            }
        });
        importButton = new WfmButton2(wfmStrings.importString(), WfmButton2.BTN_PRIMARY);
        importButton.addClickHandler(event -> {
            if (fileUpload.getFilename() != null && !"".equals(fileUpload.getFilename())) {
                if (".csv".equals(fileUpload.getFilename().substring(fileUpload.getFilename().lastIndexOf(".")))) {
                    form.setParameter(BANK_ACCOUNT_ID, bankAccountID.toString());
                    form.setParameter(BANK_ACCOUNT_TYPE, "CSV");
                    form.setParameter(DESCRIPTION_PARAM_NAME, "");
                    form.setParameter(UPLOAD_TYPE_PARAM_NAME, Utils.getUploadTypeParam());
                    form.submit();
                    LoadingPanel.loading(true);
                } else {
                    Info.show(accountingStrings.wrongFileType(), Info.Type.WARNING);
                }
            }
        });
        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        fileUpload = new FileUpload();
        fileUpload.setWidth("350px");
        fileUpload.setName(ATTACHMENT_PARAM_BASE + 0);

        FlexTable table = new FlexTable();
        Anchor downloadLink = new Anchor(wfmStrings.downloadSample(), false, GWT.getHostPageBaseURL() + "", "_blank");
        downloadLink.setHref(SAMPLE_IMPORT_TRANSACTION_CSV);
        table.setWidget(0, 0, new HTML("<b class=customTitle>" + bankAccountName + "</b>"));
        table.setWidget(1, 0, new HTML("<b class=customTitle>" + wfmStrings.clickHereToImport() + "</b>"));
        table.setWidget(2, 0, fileUpload);
        table.setWidget(3, 0, new HTML(accountingMessages.csvOfxSupported(".CSV", ".OFX")));
        table.setWidget(4, 0, downloadLink);
        table.setCellSpacing(15);
        form.setWidget(table);
    }

    private void loadFiles() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                AccountingService.App.get().getBankAccountFilesList(bankAccountID, new AsyncCallback<BankAccountAttachment[]>() {
                    public void onFailure(Throwable caught) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public void onSuccess(BankAccountAttachment[] result) {
                        filesTable.removeAllRows();

                        if (result != null) {

                            for (int i = 0; i < result.length; i++) {
                                Icon icon = new Icon();
                                icon.setStyleName(WfmButton2.ICON_TRASH);
                                final String name = result[i].getName();
                                icon.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                                icon.getElement().setId("ba" + result[i].getObjectID().toString());
                                icon.addClickHandler(new ClickHandler() {
                                    @Override
                                    public void onClick(final ClickEvent clickEvent) {
                                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                                        messageBox.setTitle(wfmStrings.confirmation());
                                        messageBox.setMessage(wfmStrings.areYouSureWantToDeleteThe() + " " + name + " ?");
                                        Object source = clickEvent.getSource();
                                        final int rowIndex = ((FlexTable) ((Image) source).getParent()).getCellForEvent(clickEvent).getRowIndex();
                                        final String id = icon.getElement().getId().replace("ba", "");
                                        messageBox.addCloseHandler(new CloseHandler() {
                                            @Override
                                            public void onSubmit() {
                                                AccountingService.App.get().deleteBankAccountFile(Integer.valueOf(id), new AbstractAsyncCallback<Void>() {
                                                    @Override
                                                    public void failure(Throwable throwable) {
                                                        super.failure(throwable);
                                                    }

                                                    @Override
                                                    public void success(Void result) {
                                                        filesTable.removeRow(rowIndex);
                                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.file()), Info.Type.INFO);
                                                    }
                                                });
                                            }
                                        });
                                        messageBox.open();
                                    }
                                });
                                filesTable.setWidget(i, 0, icon);
                                if (!result[i].isImported()) {
                                    final Integer attID = result[i].getObjectID();
                                    ExtendedSimpleLink link = new ExtendedSimpleLink(result[i].getName(), attID);
                                    link.addClickHandler(event -> showMapperPopUp(attID));
                                    filesTable.setWidget(i, 1, link);
                                } else {
                                    final Integer attachmentID = result[i].getObjectID();
                                    ExtendedSimpleLink showStatementsLink = new ExtendedSimpleLink(result[i].getName(), attachmentID, result[i].getReconciled());
                                    showStatementsLink.addClickHandler(event -> {
                                        loadedAttachmentID = attachmentID;
                                        loadStatements(attachmentID);
                                    });
                                    filesTable.setWidget(i, 1, showStatementsLink);
                                }
                            }
                        }
                    }
                });
            }
        });

    }

    private void loadStatements(final Integer attID) {
        LoadingPanel.loading(true);
        AccountingService.App.get().getStatementItems(attID, new AsyncCallback<BankAccountStatementTO[]>() {
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void onSuccess(BankAccountStatementTO[] result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    importedStatementsTable.removeAllRows();
                    importedStatementsTable.setHTML(0, 0, "<b class=customTitle>" + accountingStrings.reviewImportedBankStatements() + "</b>");
                    importedStatementsTable.getFlexCellFormatter().setColSpan(0, 0, 4);
                    importedStatementsTable.setHTML(0, 1, "<b class=customTitle>" + wfmStrings.match() + "</b>");
                    importedStatementsTable.getFlexCellFormatter().setColSpan(0, 1, 3);

                    importedStatementsTable.setHTML(1, 0, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    importedStatementsTable.setHTML(1, 1, accountingStrings.spent());
                    importedStatementsTable.setHTML(1, 2, accountingStrings.received());
                    importedStatementsTable.setHTML(1, 3, "&nbsp;");
                    importedStatementsTable.setHTML(1, 4, "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                    importedStatementsTable.setHTML(1, 5, accountingStrings.spent());
                    importedStatementsTable.setHTML(1, 6, accountingStrings.received());
//                    // 1-qator: Ustun sarlavhalari (joriy ustun indekslari bo'yicha tartiblash)
//                    importedStatementsTable.setHTML(1, 0, "<b>" +accountingStrings.transactionDate() + "</b>"); // Ustun 0
//                    importedStatementsTable.setHTML(1, 1, "<b>" +accountingStrings.transactionDescription()+ "</b>");    // Ustun 1
//                    importedStatementsTable.setHTML(1, 2, "<b>" + accountingStrings.spent() + "</b>"); // Ustun 2 (Bank hisoboti Sarflangan)
//                    importedStatementsTable.setHTML(1, 3, "<b>" + accountingStrings.received() + "</b>"); // Ustun 3 (Bank hisoboti Qabul qilingan)
//                    importedStatementsTable.setHTML(1, 4, "<b>" + "&nbsp;" + "</b>"); // Ustun 4 (Harakatlar / Moslik belgisi)
//                    importedStatementsTable.setHTML(1, 5, "<b>" + accountingStrings.spent() + "</b>"); // Ustun 5 (Mos kelgan tranzaksiya Sarflangan)
//                    importedStatementsTable.setHTML(1, 6, "<b>" + accountingStrings.received() + "</b>"); // Ustun 6 (Mos kelgan tranzaksiya Qabul qilingan)


                    for (BankAccountStatementTO aResult : result) {
                        final int rCount = importedStatementsTable.getRowCount();
                        importedStatementsTable.setHTML(rCount, 0, DateUtils.format(aResult.getTransactionDate()) + (aResult.getDescription() != null ? "<br>" + aResult.getDescription() : " "));

                        if (aResult.getDebit() != null && aResult.getDebit().compareTo(BigDecimal.ZERO) > 0) {
                            importedStatementsTable.setHTML(rCount, 1, "&nbsp;");
                            importedStatementsTable.setText(rCount, 2, AccountingUtils.get().format(aResult.getDebit()));
                        } else {
                            importedStatementsTable.setText(rCount, 1, aResult.getCredit() != null ? AccountingUtils.get().format(aResult.getCredit()) : "&nbsp;");
                            importedStatementsTable.setHTML(rCount, 2, "&nbsp;");
                        }
                        final BankAccountStatementTO transaction = aResult;

                        VerticalPanel linkPanel = new VerticalPanel();

                        Button matchButton = new Button(wfmStrings.match());
                        matchButton.addClickHandler(new ClickHandler() {
                            public void onClick(ClickEvent event) {
                                ArrayList<Integer> selected = new ArrayList();
                                selected.add(transaction.getTransaction().getTransactionId());
                                AccountingService.App.get().reconcileStatement(transaction.getBankStatementItemID(), selected, transaction.getBankGlAccountID(), new AsyncCallback<Boolean>() {
                                    public void onFailure(Throwable caught) {
                                        //To change body of implemented methods use File | Settings | File Templates.
                                    }

                                    public void onSuccess(Boolean result) {
                                        loadStatements(attID);
                                    }
                                });
                            }
                        });

                        SimpleLink findAndMatch = new SimpleLink(accountingStrings.findAndMatch());
                        findAndMatch.addClickHandler(new ClickHandler() {
                            public void onClick(ClickEvent event) {
                                showReconcileView(transaction, true);
                            }
                        });

                        SimpleLink createNewTransactionLink = new SimpleLink(accountingStrings.createNewTransaction());
                        createNewTransactionLink.addClickHandler(new ClickHandler() {
                            public void onClick(ClickEvent event) {
                                new BankTransferAddEditView(transaction.getBankAccountID(), transaction.getAmount(),
                                        transaction.getTransactionDate(), transaction.isDebitCredit());
                            }
                        });
                        if (transaction.getTransaction() != null) {
                            linkPanel.add(matchButton);
                        }
                        linkPanel.add(findAndMatch);
                        linkPanel.add(createNewTransactionLink);

                        if (aResult.getMatchResult().equals(Constants.HAS_ENTRIES) && aResult.getTransaction() != null) {
                            //.getTransaction().getJournalId();
                            FlexTable resBox = new FlexTable();
                            resBox.setCellSpacing(0);
                            resBox.setText(0, 0, DateUtils.format(aResult.getTransaction().getJournalDate()));
                            resBox.setText(1, 0, aResult.getTransaction().getJournalName());
                            if (aResult.getTransaction().getMoreFound() > 0) {
                                SimpleLink otherFound = new SimpleLink(aResult.getTransaction().getMoreFound() + " " + accountingStrings.otherPossibleMatchFound());
                                otherFound.addClickHandler(new ClickHandler() {
                                    public void onClick(ClickEvent event) {
                                        showReconcileView(transaction, false);
                                    }
                                });
                                resBox.setWidget(2, 0, otherFound);
                            }

                            importedStatementsTable.setWidget(rCount, 4, resBox);
                            if (aResult.isDebitCredit()) {
                                importedStatementsTable.setHTML(rCount, 5, "&nbsp;");
                                importedStatementsTable.setText(rCount, 6, AccountingUtils.get().format(aResult.getTransaction().getTotalDebit()));
                            } else {
                                importedStatementsTable.setText(rCount, 5, AccountingUtils.get().format(aResult.getTransaction().getTotalCredit()));
                                importedStatementsTable.setHTML(rCount, 6, "&nbsp;");
                            }
                        } else if (Constants.FIND_AND_MATCH.equals(aResult.getMatchResult()) && aResult.getTransaction() != null) {
                            if (aResult.isDebitCredit()) {
                                importedStatementsTable.setHTML(rCount, 5, "&nbsp;");
                                importedStatementsTable.setText(rCount, 6, AccountingUtils.get().format(aResult.getTransaction().getTotalDebit()));
                            } else {
                                importedStatementsTable.setText(rCount, 5, AccountingUtils.get().format(aResult.getTransaction().getTotalCredit()));
                                importedStatementsTable.setHTML(rCount, 6, "&nbsp;");
                            }
                        }

                        if (aResult.getTransaction() != null && Constants.RECONCILED.equals(aResult.getTransaction().getReconcileStatus())) {
                            importedStatementsTable.getRowFormatter().setStyleName(rCount, "bankStatementReconcileBackground");
                            importedStatementsTable.setWidget(rCount, 3, new Image(reportsImageBundle.reconciled()));
                            FlexTable resBox = new FlexTable();
                            resBox.setCellSpacing(0);
                            resBox.setText(0, 0, DateUtils.format(aResult.getTransaction().getJournalDate()));
                            Element journal = DOM.createTD();
                            journal.appendChild(getAsLink(aResult.getTransaction().getJournalName(), "clickedreport|journalReport/" + aResult.getTransaction().getJournalId(), aResult.getTransaction().getJournalId().toString()));
                            resBox.setWidget(1, 0, new Span(journal));
                            importedStatementsTable.setWidget(rCount, 4, resBox);
                        } else if (aResult.getTransaction() != null && aResult.getMatchResult().equals(Constants.FIND_AND_MATCH)) {
                            FlexTable resBox = new FlexTable();
                            resBox.setCellSpacing(0);
                            resBox.setText(0, 0, DateUtils.format(aResult.getTransaction().getJournalDate()));
                            resBox.setText(1, 0, aResult.getTransaction().getJournalName());
                            if (aResult.getTransaction().getMoreFound() > 0) {
                                SimpleLink otherFound = new SimpleLink(aResult.getTransaction().getMoreFound() + " " + accountingStrings.otherPossibleMatchFound());
                                otherFound.addClickHandler(new ClickHandler() {
                                    public void onClick(ClickEvent event) {
                                        showReconcileView(transaction, false);
                                    }
                                });
                                resBox.setWidget(2, 0, otherFound);
                            }
                            importedStatementsTable.setWidget(rCount, 3, linkPanel);
                            importedStatementsTable.setWidget(rCount, 4, resBox);
                        } else {
                            importedStatementsTable.setWidget(rCount, 3, linkPanel);
                        }

                        if (aResult.getMatchResult().equals(Constants.FIND_AND_MATCH) && aResult.getTransaction() == null) {
                            importedStatementsTable.setHTML(rCount, 4, "&nbsp;");
                            importedStatementsTable.setHTML(rCount, 5, "&nbsp;");
                            importedStatementsTable.setHTML(rCount, 6, "&nbsp;");
                        } else if (aResult.getMatchResult().equals(Constants.NO_MATCH_FOUND)) {
//                            importedStatementsTable.setWidget(rCount, 3, new Label(""));
                            importedStatementsTable.setWidget(rCount, 4, new Label(accountingStrings.noMatchFound()));
                            importedStatementsTable.getFlexCellFormatter().setColSpan(rCount, 4, 3);
                        }
                    }
                    if (result.length == 0) {
                        refreshFileName(attID);
                    }
                }
            }
        });
    }

    private void refreshFileName(Integer attID) {
        for (int i = 0; i < filesTable.getRowCount(); i++) {
            ExtendedSimpleLink link = (ExtendedSimpleLink) filesTable.getWidget(i, 0);
            if (link.getAttachmentID() != null && link.getAttachmentID().equals(attID)) {
                link.setReconciled(true);
                link.changeFileName();
            }
        }
    }

    private void showReconcileView(final BankAccountStatementTO transaction, final boolean isFindAndMatch) {

        filterData = new FindMatchFilterData();
        filterData.setGlAccountID(transaction.getBankGlAccountID());
        filterData.setDebitCredit(transaction.isDebitCredit());

        shell = new KpiModal();
        shell.addStyleName("import-transactions-modal");
        shell.setTitle(accountingStrings.selectMatchingTransaction());

        selected = new ArrayList<>();
        amountMatchHTML = new HTML("Matched");
        amountMatchHTML.setStyleName("bankStatementReconcileBackground");
        amountMatchHTML.setVisible(false);

        totalAmountHTML = new HTML();

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(5);
        buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        final MaterialDialogContent mainPanel = shell.getContent();
        mainPanel.setWidth("100%");

        final KpiCheckBox unchecked = new KpiCheckBox(wfmStrings.deselect());
        unchecked.setValue(transaction.getTransaction() != null);
        unchecked.setEnabled(transaction.getTransaction() != null);
        unchecked.addClickHandler(event -> {
            for (Object o : checkboxs.entrySet()) {
                CustomCheckBox ck;
                Map.Entry pairs = (Map.Entry) o;
                //System.out.println(pairs.getKey() + " = " + pairs.getValue());
                ck = (CustomCheckBox) pairs.getValue();
                ck.setValue(unchecked.getValue());
            }
        });

        final TextBox searchTextBox = new TextBox();
        final TextBox startAmount = new TextBox();

        final TextBox endAmount = new TextBox();
//        startAmount.setWidth("100px");
//        endAmount.setWidth("100px");
        startAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        endAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(startAmount, 2);
        Validation.addNumericKeyboardListener(endAmount, 2);

        final DatePicker startDate = new DatePicker();
        final DatePicker endDate = new DatePicker();
        startDate.setWidth("110px");
        endDate.setWidth("110px");

        WfmButton2 applyFilterButton = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_PRIMARY);
        WfmButton2 resetButton = new WfmButton2(wfmStrings.reset());

        FlexTable topFilterTable = new FlexTable();
        topFilterTable.addStyleName("matching-transactions-table");
        topFilterTable.setCellSpacing(10);
        topFilterTable.setWidget(0, 0, new HTML(wfmStrings.search()));
        topFilterTable.setWidget(0, 1, new HTML(accountingStrings.amountMoreThan()));
        topFilterTable.setWidget(0, 2, new HTML(accountingStrings.amountLessThan()));
        topFilterTable.setWidget(0, 3, new HTML(wfmStrings.startDate()));
        topFilterTable.setWidget(0, 4, new HTML(wfmStrings.endDate()));
        topFilterTable.setWidget(1, 0, new HTML("<span class='cell-search'>" + searchTextBox + "</span>"));
        topFilterTable.setWidget(1, 1, new HTML("<span class='cell-amountMore'>" + startAmount + "</span>"));
        topFilterTable.setWidget(1, 2, new HTML("<span class='cell-amountLess'>" + endAmount + "</span>"));
        topFilterTable.setWidget(1, 3, startDate);
        topFilterTable.setWidget(1, 4, endDate);
        topFilterTable.setWidget(1, 5, applyFilterButton);
        topFilterTable.setWidget(1, 6, resetButton);
        topFilterTable.setWidget(2, 0, unchecked);
        topFilterTable.setWidget(2, 1, new HTML(accountingStrings.statementAmount()));
        topFilterTable.setWidget(2, 2, new HTML((transaction.isDebitCredit() ? transaction.getDebit() + "" : transaction.getCredit() + "")));
        topFilterTable.setWidget(2, 3, new HTML(accountingStrings.kpiAmount()));
        topFilterTable.setWidget(2, 4, totalAmountHTML);
        topFilterTable.setWidget(2, 5, amountMatchHTML);
        mainPanel.add(topFilterTable);

        DynamicTableColumn[] columns = new DynamicTableColumn[6];
        columns[0] = new DynamicTableColumn("", "checkBox", 20);
        columns[1] = new DynamicTableColumn(wfmStrings.date(), "date", 100);
        columns[2] = new DynamicTableColumn(wfmStrings.name(), "name", 250);
        columns[3] = new DynamicTableColumn(wfmStrings.number(), "refNumber", 130);
        columns[4] = new DynamicTableColumn(accountingStrings.spent(), "spent", 100);
        columns[5] = new DynamicTableColumn(accountingStrings.received(), "received", 100);

        dynamicTable = new DynamicTable(columns, false);
        dynamicTable.setWidth("100%");
        dynamicTable.setBorderWidth(0);
//        dynamicTable.setStyleName(AccountingCustomFormConstants.STYLE_PRODUCT_TABLE);

        reconcileButton = new WfmButton2(wfmStrings.reconcile());
        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        scrollPanel = new ScrollPanel();

        applyFilterButton.addClickHandler(clickEvent -> {
            filterData.setSearchKey(searchTextBox.getText());
            if (startAmount.getText() != null && !startAmount.getText().trim().isEmpty()) {
                filterData.setStartAmount(AccountingUtils.get().parseToBigDecimal(startAmount.getText().trim()));
            } else {
                filterData.setStartAmount(null);
            }
            if (endAmount.getText() != null && !endAmount.getText().trim().isEmpty()) {
                filterData.setEndAmount(AccountingUtils.get().parseToBigDecimal(endAmount.getText().trim()));
            } else {
                filterData.setEndAmount(null);
            }
            filterData.setStartDate(Utils.getStartDateNC(startDate.getDate()));
            filterData.setEndDate(Utils.getEndDateNC(endDate.getDate()));
            loadReconcileContentData(transaction, isFindAndMatch);
        });

        resetButton.addClickHandler(clickEvent -> {
            searchTextBox.setText("");
            startAmount.setText("");
            endAmount.setText("");
            startDate.clearSelected();
            endDate.clearSelected();

            filterData.setSearchKey(null);
            filterData.setStartAmount(null);
            filterData.setEndAmount(null);
            filterData.setStartDate(null);
            filterData.setEndDate(null);
            loadReconcileContentData(transaction, isFindAndMatch);
        });

        reconcileButton.setVisible(false);
        reconcileButton.addClickHandler(event -> {
            BigDecimal total = calculate(transaction);
            if (transaction.getAmount() != null && transaction.getAmount().setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP).compareTo(total) == 0) {
                AccountingService.App.get().reconcileStatement(transaction.getBankStatementItemID(), selected, transaction.getBankGlAccountID(), new AsyncCallback<Boolean>() {
                    public void onFailure(Throwable caught) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    public void onSuccess(Boolean result) {
                        if (result) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TRANSACTION_RECONCILED, result, ImportTransactionsView.this);
                            shell.close();
                            loadStatements(loadedAttachmentID);
                        }
                    }
                });
            } else {
                Info.show(accountingStrings.sumOfSelectedTransactionsMustMatch() + " " + AccountingUtils.get().formatPrice(transaction.getAmount()), Info.Type.WARNING);
            }
        });
        cancelButton.addClickHandler(event -> shell.close());

        loadReconcileContentData(transaction, isFindAndMatch);

        buttonPanel.add(reconcileButton);
        buttonPanel.add(cancel);
        mainPanel.add(scrollPanel);
        mainPanel.add(buttonPanel);
        shell.open();
    }


    private Element getAsLink(String text, final String linkHref, String... title) {
        Element link = DOM.createAnchor();
        link.setInnerHTML(text);
        DOM.sinkEvents(link.cast(), Event.ONCLICK);
        DOM.setEventListener(link.cast(), event -> {
            if (title.length > 0) {
                SinksContainerFactory.entryPoint.onHistoryChanged(linkHref, title[0]);
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged(linkHref);
            }
        });

        return link;
    }

    private BigDecimal calculate(final BankAccountStatementTO transaction) {
        selected.clear();
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
            DynamicTableItem item = dynamicTable.getItem(i);
            CustomCheckBox checkBox = (CustomCheckBox) item.getColumnById("checkBox");
            if (checkBox.getValue()) {
                Transaction checkedTransaction = checkBox.getTransaction();
                selected.add(checkedTransaction.getTransactionId());
                if (transaction.isDebitCredit()) {
                    if (checkedTransaction.getTotalDebit() != null) {
                        total = total.add(checkedTransaction.getTotalDebit()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    }
                    if (checkedTransaction.getTotalCredit() != null) {
                        total = total.subtract(checkedTransaction.getTotalCredit()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    }
                } else {
                    if (checkedTransaction.getTotalDebit() != null) {
                        total = total.subtract(checkedTransaction.getTotalDebit()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    }
                    if (checkedTransaction.getTotalCredit() != null) {
                        total = total.add(checkedTransaction.getTotalCredit()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
                    }
                }
            }
        }
        amountMatchHTML.setVisible(total.compareTo((transaction.isDebitCredit() ? transaction.getDebit() : transaction.getCredit())) == 0);
        totalAmountHTML.setHTML(total + "");
        return total;
    }

    private void loadReconcileContentData(final BankAccountStatementTO transaction, boolean isFindAndMatch) {
        LoadingPanel.loading(true);
        scrollPanel.clear();
        if (isFindAndMatch) {
            AccountingService.App.get().findAndMatchTransactions(filterData, new AsyncCallback<ArrayList<Transaction>>() {
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void onSuccess(ArrayList<Transaction> result) {
                    drawReconcileContent(transaction, result);
                    LoadingPanel.loading(false);
                }
            });
        } else {
            filterData.setTransactionAmount(transaction.getAmount());
            AccountingService.App.get().findOthers(filterData, new AsyncCallback<ArrayList<Transaction>>() {
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(accountingStrings.errorOccuredDuringTheLoad(), Info.Type.WARNING);
                }

                public void onSuccess(ArrayList<Transaction> result) {
                    drawReconcileContent(transaction, result);
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    private void drawReconcileContent(final BankAccountStatementTO transaction, List<Transaction> result) {
        if (result != null && result.size() > 0) {
            dynamicTable.clear();
            for (Transaction trans : result) {
                Widget[] widgets = new Widget[6];
                CustomCheckBox checkBox = new CustomCheckBox(trans);
                if (transaction.getTransaction() != null && trans.getTransactionId().equals(transaction.getTransaction().getTransactionId())) {
                    checkBox.setValue(true);
                    checkboxs.put(trans.getJournalId().toString(), checkBox);
                }
                widgets[0] = checkBox;
                widgets[1] = new Label(DateUtils.format(trans.getJournalDate()));
                widgets[2] = new Label(trans.getJournalName());
                widgets[3] = new Label(trans.getReference() != null ? trans.getReference() : " ");
                widgets[4] = new Label(trans.getTotalCredit() != null ? AccountingUtils.get().formatPrice(trans.getTotalCredit()) : "");
                widgets[5] = new Label(trans.getTotalDebit() != null ? AccountingUtils.get().formatPrice(trans.getTotalDebit()) : "");
                dynamicTable.addRow(widgets);
                checkBox.addClickHandler(clickEvent -> calculate(transaction));
            }

            scrollPanel.add(dynamicTable);
            scrollPanel.addStyleName("matching-transactions-results");
            reconcileButton.setVisible(true);
        } else {
            scrollPanel.add(new HTML("<b>" + accountingStrings.noMatchFound() + "</b>"));
            scrollPanel.addStyleName("matching-transactions-results matching-transactions-results--null");
            reconcileButton.setVisible(false);
        }
    }

    private KpiModal mappingShell;
    private KpiModal addTemplateDialogBox;
    private WfmDropdown csvTemplateDropdown;
    private TextBox name;
    private FlexTable statementLinesTable;

    private void showMapperPopUp(final Integer bankAccountAttachmentID) {
        LoadingPanel.loading(true);
        AccountingService.App.get().getTransactionMapping(bankAccountAttachmentID, new AsyncCallback<BankAccountImportStatementData>() {
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void onSuccess(final BankAccountImportStatementData importStatementData) {
                final BankAccountTransactionMapper[] formMappedData = importStatementData.getFormMappedData();
                if (formMappedData != null) {
                    mappingShell = new KpiModal();
                    mappingShell.setWidth(500);
                    mappingShell.setTitle(accountingStrings.statementLineImportedFromYourFile());

                    if (importStatementData.getCsvTemplates() != null && importStatementData.getCsvTemplates().length > 0) {
                        csvTemplateDropdown = new WfmDropdown(false, true);
                    } else {
                        csvTemplateDropdown = new WfmDropdown();
                    }
                    csvTemplateDropdown.setWidth("150px");

                    drawAddTemplatePanel(formMappedData);

                    statementLinesTable = new FlexTable();
                    statementLinesTable.setStyleName("mod_table--cellpadding");
                    statementLinesTable.setCellSpacing(0);

                    HorizontalPanel merchP = new HorizontalPanel();
                    merchP.setStyleName("mod_table--cellpadding");
                    Label label = new Label(wfmStrings.template());
                    merchP.add(label);
                    merchP.add(new HTML());
                    AdvancedInputGroup inputGroup = new AdvancedInputGroup(csvTemplateDropdown);
                    inputGroup.setFloat(Style.Float.RIGHT);
                    inputGroup.setWidth("auto");
                    inputGroup.setAppender("ficon--plus");
                    inputGroup.appenderClickHandler(() -> {
                        name.removeStyleName("error");
                        name.setText("");
                        addTemplateDialogBox.open();
                    });
                    merchP.add(inputGroup);
                    merchP.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);

                    statementLinesTable.setText(0, 0, accountingStrings.statementData());
                    statementLinesTable.getFlexCellFormatter().setColSpan(0, 0, 2);
                    statementLinesTable.setText(0, 1, wfmStrings.assignTo());
                    statementLinesTable.getFlexCellFormatter().setStyleName(0, 0, "bankStatementLineTransparentPadding");
                    statementLinesTable.getFlexCellFormatter().setStyleName(0, 1, "bankStatementLineTransparentPadding");

                    SelectItem[] items = new SelectItem[8];
                    items[0] = new SelectItem(Constants.TRANSACTION_DATE, Constants.TRANSACTION_DATE_STR);
                    items[1] = new SelectItem(Constants.TRANSACTION_DESCRIPTION, Constants.TRANSACTION_DESCRIPTION_STR);
                    items[2] = new SelectItem(Constants.TRANSACTION_DEBIT, Constants.TRANSACTION_DEBIT_STR);
                    items[3] = new SelectItem(Constants.TRANSACTION_CREDIT, Constants.TRANSACTION_CREDIT_STR);
                    items[4] = new SelectItem(Constants.TRANSACTION_BALANCE, Constants.TRANSACTION_BALANCE_STR);
                    items[5] = new SelectItem(Constants.TRANSACTION_ACCOUNT_CODE, Constants.TRANSACTION_ACCOUNT_CODE_STR);
                    items[6] = new SelectItem(Constants.TRANSACTION_NAME, Constants.TRANSACTION_NAME_STR);
                    items[7] = new SelectItem(Constants.TRANSACTION_EXCHANGE_RATE, Constants.TRANSACTION_EXCHANGE_RATE_STR);

                    for (int i = 0; i < formMappedData.length; i++) {
                        final int rCount = statementLinesTable.getRowCount();

                        final WfmDropdown transactionFields = new WfmDropdown();
                        transactionFields.setWidth("150px");

                        transactionFields.addItems(items);
                        statementLinesTable.setWidget(rCount, 0, new Label(formMappedData[i].getFileColumnName()));
                        statementLinesTable.setWidget(rCount, 1, new Label(formMappedData[i].getFileColumnValue()));
                        statementLinesTable.setWidget(rCount, 2, transactionFields);
                        if (i == (formMappedData.length - 1)) {
                            statementLinesTable.getFlexCellFormatter().setStyleName(rCount, 0, "bankStatementLineTransparentBtm");
                            statementLinesTable.getFlexCellFormatter().setStyleName(rCount, 1, "bankStatementLineTransparentBtm");
                            statementLinesTable.getFlexCellFormatter().setStyleName(rCount, 2, "bankStatementLineTransparentLeft");
                        } else {
                            statementLinesTable.getFlexCellFormatter().setStyleName(rCount, 0, "bankStatementLineTransparentTop");
                            statementLinesTable.getFlexCellFormatter().setStyleName(rCount, 1, "bankStatementLineTransparentTop");
                            statementLinesTable.getFlexCellFormatter().setStyleName(rCount, 2, "bankStatementLineTransparentLeft");
                        }
                    }
                    int lastRow = statementLinesTable.getRowCount();
                    statementLinesTable.setWidget(lastRow, 0, new Label(accountingStrings.pleaseSelectDatePattern()));
                    statementLinesTable.setWidget(lastRow, 2, patternDropdown);

                    if (importStatementData.getCsvTemplates() != null && importStatementData.getCsvTemplates().length > 0) {
                        csvTemplateDropdown.addItems(importStatementData.getCsvTemplates());
                        csvTemplateDropdown.setSelected(importStatementData.getCsvTemplateID());
                        applyCsvTemplateItemsData(importStatementData.getCsvTemplateData());
                    }

                    final WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
                    saveButton.addClickHandler(event -> {
                        saveButton.setEnabled(false);
                        if (!validateTransactionFields(statementLinesTable)) {
                            Info.show(wfmStrings.pleaseSelectRequiredFields(), Info.Type.WARNING);
                            saveButton.setEnabled(true);
                            return;
                        }

                        LoadingPanel.loading(true);

                        datePattern = patternDropdown.getSelectedItem() != null ? patternDropdown.getSelectedItem().getName() : "";

                        for (int i = 1; i < statementLinesTable.getRowCount() - 2; i++) {
                            formMappedData[i - 1].setTransactionField(((WfmDropdown) statementLinesTable.getWidget(i, 2)).getSelectedId());
                        }

                        BankAccountImportStatementData importStatementData1 = new BankAccountImportStatementData();
                        importStatementData1.setFormMappedData(formMappedData);
                        importStatementData1.setDatePattern(datePattern);
                        importStatementData1.setCsvTemplateID(csvTemplateDropdown.getSelectedId());
                        importStatementData1.setCsvTemplateData(getCsvTemplateItems(formMappedData));

                        AccountingService.App.get().saveStatements(importStatementData1, new AsyncCallback<Boolean>() {
                            public void onFailure(Throwable ex) {
                                saveButton.setEnabled(true);
                                LoadingPanel.loading(false);
                                if (ex instanceof DateFormatParseException) {
                                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
                                    messageBox.setTitle(wfmStrings.error());
                                    messageBox.setMessage(ex.getMessage());
                                    messageBox.addCloseHandler(new CloseHandler() {
                                        @Override
                                        public void onSubmit() {
                                            /*mappingShell.close();
                                            loadFiles();*/
                                        }
                                    });
                                    messageBox.open();
                                } else {
                                    Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                                }
                            }

                            public void onSuccess(Boolean result) {
                                saveButton.setEnabled(true);
                                LoadingPanel.loading(false);
                                if (result) {
                                    loadFiles();
                                    loadedAttachmentID = bankAccountAttachmentID;
                                    loadStatements(bankAccountAttachmentID);
                                    mappingShell.close();
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_STATEMENTS_SAVED, result, ImportTransactionsView.this);
                                } else {
                                    Info.show(accountingStrings.errorOccuredWhileParsingFile(), Info.Type.WARNING);
                                }
                            }
                        });
                    });
                    WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
                    cancelButton.addClickHandler(event -> mappingShell.close());

                    mappingShell.add(merchP);
                    mappingShell.add(statementLinesTable);
                    mappingShell.addButton(cancelButton);
                    mappingShell.addButton(saveButton);

                    LoadingPanel.loading(false);
                    mappingShell.open();
                }
            }
        });
    }

    private ArrayList<CsvTemplateItem> getCsvTemplateItems(BankAccountTransactionMapper[] result) {
        ArrayList<CsvTemplateItem> items = new ArrayList<>();
        for (int i = 1; i < statementLinesTable.getRowCount() - 2; i++) {
            WfmDropdown wfmDropdown = (WfmDropdown) statementLinesTable.getWidget(i, 2);
            if (wfmDropdown.getSelectedId() != null) {
                CsvTemplateItem item = new CsvTemplateItem();
                item.setValue(result[i - 1].getFileColumnName());
                item.setSystemField(wfmDropdown.getSelectedItem().getName());
                items.add(item);
            }
        }

        CsvTemplateItem item = new CsvTemplateItem();
        item.setSystemValue(true);
        item.setValue(patternDropdown.getSelectedItem().getName());
        item.setSystemField(Constants.TRANSACTION_DATE_FORMAT_STR);
        items.add(item);

        return items;
    }

    private void drawAddTemplatePanel(final BankAccountTransactionMapper[] result) {
        addTemplateDialogBox = new KpiModal();
        addTemplateDialogBox.setWidth(400);
        addTemplateDialogBox.setTitle(wfmStrings.add() + " " + wfmStrings.template());
        name = new TextBox();
        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        FlexTable merchTable = new FlexTable();
        merchTable.setHTML(1, 0, wfmStrings.name() + "<font color='red'>*</font>");
        merchTable.setWidget(1, 1, name);
        addTemplateDialogBox.add(merchTable);
        addTemplateDialogBox.addButton(cancel);
        addTemplateDialogBox.addButton(save);
        save.addClickHandler(clickEvent -> {
            if (name.getText() != null && !"".equals(name.getText())) {
                saveTemplateService(name.getText());
            } else {
                name.setStyleName("error");
            }
        });

        cancel.addClickHandler(clickEvent -> addTemplateDialogBox.close());

        csvTemplateDropdown.addEventHandler(new DropdownListener() {
            @Override
            public void itemSelected() {
                if (csvTemplateDropdown.getSelectedId() != null) {
                    applySelectedTemplate();
                }
            }

            @Override
            public void saveNewItem() {

            }
        });
    }

    private void applySelectedTemplate() {
        LoadingPanel.loading(true);
        accountingService.getCsvTemplateData(csvTemplateDropdown.getSelectedId(), new AsyncCallback<ArrayList<CsvTemplateItem>>() {

            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<CsvTemplateItem> csvTemplateItems) {
                LoadingPanel.loading(false);
                applyCsvTemplateItemsData(csvTemplateItems);
            }
        });
    }

    private void applyCsvTemplateItemsData(ArrayList<CsvTemplateItem> csvTemplateItems) {
        if (csvTemplateItems != null && csvTemplateItems.size() != 0) {
            boolean nextLine = false;
            for (int i = 1; i < statementLinesTable.getRowCount() - 2; i++) {
                nextLine = false;
                WfmDropdown transactionFields = (WfmDropdown) statementLinesTable.getWidget(i, 2);
                Label csvColumnName = (Label) statementLinesTable.getWidget(i, 0);
                for (int j = 1; j < transactionFields.getItemCount(); j++) {
                    for (int l = 0; l < csvTemplateItems.size(); l++) {
                        if (csvColumnName.getText().equals(csvTemplateItems.get(l).getValue()) && transactionFields.getValues().get(j).getName().equals(csvTemplateItems.get(l).getSystemField())) {
                            transactionFields.setSelectedIndex(j);
                            csvTemplateItems.remove(l);
                            nextLine = true;
                            break;
                        }
                    }
                    if (nextLine) {
                        break;
                    }
                }
            }
        }
    }

    private void saveTemplateService(String text) {
        accountingService.saveCsvTemplate(text, Constants.CSV_TEMPLATE_IMPORT_BANK_STATEMENT, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                addTemplateDialogBox.close();
            }

            @Override
            public void onSuccess(Integer templateID) {
                getCsvTemplate(templateID);
                addTemplateDialogBox.close();
            }
        });
    }

    private boolean validateTransactionFields(FlexTable statementLinesTable) {
        List<Integer> validationList = new LinkedList<>();
        for (int i = 1; i < statementLinesTable.getRowCount() - 2; i++) {
            WfmDropdown transactionFields = (WfmDropdown) statementLinesTable.getWidget(i, 2);
            if (transactionFields.getSelectedId() != null) {
                validationList.add(transactionFields.getSelectedId());
            }
        }
        return validationList.contains(Constants.TRANSACTION_DEBIT) && validationList.contains(Constants.TRANSACTION_CREDIT) && validationList.contains(Constants.TRANSACTION_DATE);
    }

    private void getCsvTemplate(final Integer selectedTemplateID) {
        accountingService.getCsvTemplates(Constants.CSV_TEMPLATE_IMPORT_BANK_STATEMENT, new AsyncCallback<SelectItem[]>() {

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] csvTemplates) {
                csvTemplateDropdown.addItems(csvTemplates);
                csvTemplateDropdown.setSelected(selectedTemplateID);
            }
        });
    }

    public class ExtendedSimpleLink extends SimpleLink {
        private final Integer attachmentID;
        private String fileName;
        private boolean isReconciled;

        public ExtendedSimpleLink(String fileName, Integer attachmentID) {
            super(fileName);
            this.attachmentID = attachmentID;
            this.fileName = fileName;
        }

        public ExtendedSimpleLink(String fileName, Integer attachmentID, Boolean isReconciled) {
            super(fileName + (isReconciled ? "(" + accountingStrings.reconciled() + ")" : "(" + wfmStrings.saved() + ")"));
            this.attachmentID = attachmentID;
            this.fileName = fileName;
            this.isReconciled = isReconciled;
        }

        public void changeFileName() {
            String htmlFileName = fileName + (isReconciled ? "(" + accountingStrings.reconciled() + ")" : "(" + wfmStrings.saved() + ")");
            setHTML("<a href='javascript:;'>" + htmlFileName + "</a>");
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Integer getAttachmentID() {
            return attachmentID;
        }

        public void setReconciled(boolean reconciled) {
            isReconciled = reconciled;
        }
    }

    private class CustomCheckBox extends KpiCheckBox {
        private final Transaction transaction;

        private CustomCheckBox(Transaction transaction) {
            this.transaction = transaction;
        }

        public Transaction getTransaction() {
            return transaction;
        }
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
