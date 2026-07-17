package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: java
 * Date: 27.02.2009
 * Time: 18:30:36
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountingListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingServiceAsync accountingService = AccountingService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private ListingPanel<BankAccount> list;
    private KpiCheckBox showAll;
    private final boolean summaryPermission = Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_SUMMARY);

    public BankAccountingListView() {
        super(BANKACCOUNT);
        setDescription(property.getPlural(wfmStrings.bankAccounts()));
        if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_ADD)) {
            setAddNew("bank|add/add");
        }
    }

    private int actionItemCount;

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[6];

        columns[0] = new ColumnDefinitionConfig<BankAccount, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH - 20) {

            @Override
            public Anchor getCellValue(final BankAccount item) {
                actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                //View Bank Account
                if (summaryPermission) {
                    MenuPopItem viewAccount = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                    viewAccount.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bank|summary/" + item.getObjectId(), item.getCode(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(viewAccount);
                }
                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_EDIT)) {
                    //Edit bank Account
                    MenuPopItem accountEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                    accountEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bank|edit/" + item.getObjectId(), item.getCode(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(accountEdit);
                }

                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_SPEND_ADD) && item.isActive()) {
                    //Spend Money
                    MenuPopItem spendMoney = new MenuPopItem(wfmStrings.bankPayment(), "icon-sales-quote-small");
                    spendMoney.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|add/add/" + AccountingConstants.SPEND_MONEY_STR + "/" + "relatedBankAccount" + "/" + item.getObjectId()));
                    actionItemCount++;
                    menuBar.addItem(spendMoney);
                }
                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD) && item.isActive()) {
                    //Receive Money
                    MenuPopItem receiveMoney = new MenuPopItem(accountingStrings.receiveMoney(), "icon-sales-quote-small");
                    receiveMoney.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|add/add/" + AccountingConstants.RECEIVE_MONEY_STR + "/" + "relatedBankAccount" + "/" + item.getObjectId()));
                    actionItemCount++;
                    menuBar.addItem(receiveMoney);
                }

                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_TRANSFER) && item.isActive()) {
                    //Transfer Money
                    MenuPopItem transferMoney = new MenuPopItem(accountingStrings.transferMoney(), "icon-move-to");
                    transferMoney.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("transfer|add/add/" + item.getObjectId()));
                    actionItemCount++;
                    menuBar.addItem(transferMoney);
                }

                if (Utils.hasPermission(PermissionConstants.CUSTOMER_PREPAYMENT_REFUND_ADD)) {
                    MenuPopItem customerRefund = new MenuPopItem(accountingStrings.customerRefund(), "icon-edit");
                    customerRefund.setCommand(() -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("customerRefund|add/add/" + "Account/" + item.getObjectId());
                    });
                    actionItemCount++;
                    menuBar.addItem(customerRefund);
                }

                if (Utils.hasPermission(PermissionConstants.SUPPLIER_PREPAYMENT_REFUND_ADD)) {
                    MenuPopItem supplierRefund = new MenuPopItem(accountingStrings.supplierRefund(), "icon-edit");
                    supplierRefund.setCommand(() -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("supplierRefund|add/add/" + "Account/" + item.getObjectId());
                    });
                    actionItemCount++;
                    menuBar.addItem(supplierRefund);
                }

                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_DELETE)) {
                    //Delete Bank Account
                    MenuPopItem accountDelete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    accountDelete.setCommand(() -> {
                        final WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
                        confirm.setTitle(wfmStrings.warning());
                        confirm.setMessage(wfmStrings.sureYouWantToDelete());
                        confirm.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                accountingService.deleteBankAccount(item.getObjectId(), new AbstractAsyncCallback<String>() {
                                    public void failure(Throwable caught) {
                                        showFailureMessage();
                                    }

                                    public void success(String status) {
                                        if ("DELETED".equals(status)) {
                                            showSuccessMessage();
                                            list.reloadPage();
                                        } else if ("USEDINSYSTEM".equals(status)) {
                                            showFailureMessage();
                                        } else if ("USEDININVOICESETTINGS".equals(status)) {
                                            showFailureMessage2();
                                        }
                                    }
                                });
                            }
                        });
                        confirm.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(accountDelete);
                }

                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_TRANSACTION_IMPORT) && item.isActive()) {
                    //Import Transactions
                    MenuPopItem importTransactions = new MenuPopItem(accountingStrings.importTransactions(), "icon-attachment");
                    importTransactions.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("importbanktransactions|importTransactions/" + item.getObjectId() + "/" + item.getName()));
                    actionItemCount++;
                    menuBar.addItem(importTransactions);

                    if (Utils.hasGenericAccess(GenericSettingsEnum.IMPORT_STATEMT_ENABLED)) {
                        MenuPopItem importStatement = new MenuPopItem(accountingStrings.importStatements(), "icon-attachment");
                        importStatement.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("importbankstatement|importStatements/" + item.getObjectId() + "/" + item.getName()));
                        actionItemCount++;
                        menuBar.addItem(importStatement);
                    }
                }

                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_TRANSACTIONS) && item.isActive()) {
                    //Account  Transactions
                    MenuPopItem bankAccountTransactions = new MenuPopItem(accountingStrings.accountTransactions(), "icon-task-small");
                    bankAccountTransactions.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("accountTransactionsList|accountTransactions/" + item.getAccountId() + "/" + BANK_ACCOUNT + "/" + item.getName()));
                    actionItemCount++;
                    menuBar.addItem(bankAccountTransactions);
                }

                //Reconcilation Report
//                if (item.isReconcilationReportEnabled()) {
                if (item.isActive() && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECONCILATION_REPORT)) {
                    MenuPopItem reconcilationReport = new MenuPopItem(accountingStrings.reconcilationReport(), "icon-add-convert");
                    reconcilationReport.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("reconcilationReport|report/" + item.getObjectId()
                            + "/" + item.getName()));
                    actionItemCount++;
                    menuBar.addItem(reconcilationReport);
                }
//                }

                if (Utils.hasPermission(ACCOUNTING_BANK_STATEMENT) && item.isActive()) {
                    //View all Statements
                    MenuPopItem bankStatements = new MenuPopItem(accountingStrings.viewAllStatements(), "icon-task-small");
                    bankStatements.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bankStatementList|bankStatements/" + item.getObjectId() + "/" + item.getName()));
                    actionItemCount++;
                    menuBar.addItem(bankStatements);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH - 20);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH - 20);

        columns[1] = new ColumnDefinitionConfig<BankAccount, Widget>(wfmStrings.code(), BankAccount.CODE_COLUMN, 120) {

            @Override
            public Widget getCellValue(BankAccount item) {
                Label label = new Label(item.getCode());
                if (summaryPermission) {
                    label.setStyleName("uploadLinkStyle2");
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("bank|summary/" + item.getObjectId(), item.getCode(), item.getName()));
                }
                return label;
            }
        };
        columns[1].addStyleAttribute("paddingLeft", "5px");
        columns[1].setMinimumColumnWidth(100);

        columns[2] = new ColumnDefinitionConfig<BankAccount, Widget>(wfmStrings.name(), BankAccount.NAME_COLUMN, 140) {
            @Override
            public Widget getCellValue(BankAccount item) {
                StringBuilder str = new StringBuilder();
                if (!item.isActive()) {
                    str.append("<b style='margin:0 5px'>X</b>");
                }
                str.append(item.getName());
                HTML label = new HTML(str.toString());
                if (item.isActive() && summaryPermission) {
                    label.setStyleName("uploadLinkStyle2");
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("bank|summary/" + item.getObjectId(), item.getCode(), item.getName()));
                }
                return label;
            }
        };
        columns[2].addStyleAttribute("paddingLeft", "5px");
        columns[2].setMinimumColumnWidth(120);

        columns[3] = new ColumnDefinitionConfig<BankAccount, String>(wfmStrings.accountNumber(), BankAccount.NUMBER_COLUMN, 140) {

            @Override
            public String getCellValue(BankAccount item) {
                return item.getAccountNumber();
            }
        };
        columns[3].addStyleAttribute("paddingLeft", "5px");
        columns[3].setMinimumColumnWidth(120);

        columns[4] = new ColumnDefinitionConfig<BankAccount, SimpleLink>(wfmStrings.balance(), BankAccount.AMOUNT_COLUMN, 100) {

            @Override
            public SimpleLink getCellValue(BankAccount item) {
                String balance = item.getBalance() != null ? (item.getBalance().compareTo(BigDecimal.ZERO) < 0 ? "(" + AccountingUtils.get().formatPrice(new BigDecimal(-1).multiply(item.getBalance())) + ")" : AccountingUtils.get().formatPrice(item.getBalance())) : String.valueOf(0d);
                return getLink(balance, "clickedreport|transactionsByPeriod/" + item.getAccountId(), item.getName(), item.getName());
//                if (item.getBalance() < 0) {
//                    return "(" + AccountingUtils.get().getTotalMoneyFormat(new BigDecimal(item.getBalance()).abs()) + ")";
//                } else {
//                    return AccountingUtils.get().getTotalMoneyFormat(item.getBalance());
//                }
            }
        };
        columns[4].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columns[4].addStyleAttribute("paddingRight", "5px");
        columns[4].setMinimumColumnWidth(80);

        columns[5] = new ColumnDefinitionConfig<BankAccount, String>(wfmStrings.currency(), BankAccount.CURRENCY_COLUMN, 100) {

            @Override
            public String getCellValue(BankAccount item) {
                return (item.getCurrency() != null && item.getCurrency().getName() != null) ? item.getCurrency().getName() : "";
            }
        };
        columns[5].addStyleAttribute("paddingLeft", "5px");
        columns[5].setMinimumColumnWidth(50);
        columns[5].setMaximumColumnWidth(100);
        return columns;
    }

    protected Widget onInitialize() {
        list = new GuideListingPanel(getPanelType(), getColumnConfigs(), getListData(), getPanelDesign());

        list.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveBankAccountCellValue((BankAccount) rowValue, columnCodeName));

        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadBankAccountListExcel";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            if (filterParametr != null) {
                list.callListExcel(excelURL, filterParametr);
            } else {

                list.callListExcel(excelURL, filterParametrs);
            }
        });
        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/bankAccountsListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            if (filterParametr != null) {
                list.callListPDF(pdfURL, filterParametr);
            } else {

                list.callListPDF(pdfURL, filterParametrs);
            }
        });
        add(list);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANKACCOUNT_SAVED, BankAccountingListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MONEY_TRANSFER, BankAccountingListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_STATEMENTS_SAVED, BankAccountingListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, BankAccountingListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_CHECK_SAVED, BankAccountingListView.this, (sender, args) -> list.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_CHECK_DELETED, BankAccountingListView.this, (sender, args) -> list.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_ACCOUNT_TRANSACTION_DELETED, BankAccountingListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PAYMENT_TO_BANK_ACCOUNT, BankAccountingListView.this, (sender, args) -> list.reloadPage());

        return null;
    }

    private void saveBankAccountCellValue(BankAccount rowValue, String columnCodeName) {
        accountingService.saveBankAccountCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
        });
    }

    private GuideListingPanelDesign getPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_ADD) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("bank|add/add") : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return ChooseFilter.INVOICE_FILTER;
                    }

                    @Override
                    public ArrayList<String> getCustomFacetFilterFields() {
                        ArrayList<String> fields = new ArrayList<>(5);
                        fields.add(ListingChooseFilter.BANK_ACCOUNT_CODE);
                        fields.add(ListingChooseFilter.BANK_ACCOUNT_NAME);
                        fields.add(ListingChooseFilter.BANK_ACCOUNT_NUMBER);
                        fields.add(ListingChooseFilter.BANK_ACCOUNT_CURRENCY);
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.BankAccountList;
                    }
                };
            }


            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
                MenuBar menu = new MenuBar(true);
                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_ADD)/*Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT)*/) {
                    MenuPopItem addNew = new MenuPopItem(wfmStrings.bankAccount());
                    addNew.ensureDebugId(BANK_ACCOUNT + "addNewButton");
                    addNew.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bank|add/add"));
                    menu.addItem(addNew);
                }
                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_SPEND_ADD)) {
                    //Spend Money
                    MenuPopItem spendMoney = new MenuPopItem(wfmStrings.bankPayment());
                    spendMoney.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|add/add/" + AccountingConstants.SPEND_MONEY_STR + "/" + "relatedBankAccount"));
                    menu.addItem(spendMoney);
                }
                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD)) {
                    //Receive Money
                    MenuPopItem receiveMoney = new MenuPopItem(accountingStrings.receiveMoney());
                    receiveMoney.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|add/add/" + AccountingConstants.RECEIVE_MONEY_STR + "/" + "relatedBankAccount"));
                    menu.addItem(receiveMoney);
                }

                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_TRANSFER)) {
                    //Transfer Money
                    MenuPopItem transferMoney = new MenuPopItem(accountingStrings.transferMoney());
                    transferMoney.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("transfer|add/add"));
                    menu.addItem(transferMoney);
                }

                if (Utils.hasPermission(PermissionConstants.CUSTOMER_PREPAYMENT_REFUND_ADD)) {
                    MenuPopItem customerRefund = new MenuPopItem(accountingStrings.customerRefund());
                    customerRefund.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("customerRefund|add/add"));
                    menu.addItem(customerRefund);
                }

                if (Utils.hasPermission(PermissionConstants.SUPPLIER_PREPAYMENT_REFUND_ADD)) {
                    MenuPopItem supplierRefund = new MenuPopItem(accountingStrings.supplierRefund());
                    supplierRefund.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierRefund|add/add"));
                    menu.addItem(supplierRefund);
                }

                newItem.setMenu(menu);
                return newItem;
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                showAll = new KpiCheckBox(/*"&nbsp;" +*/accountingStrings.showAll(), true);
                showAll.ensureDebugId("showAll");
                showAll.addValueChangeHandler(event -> {
                    list.getFilterParametrs().setShowActive(!showAll.getValue());
                    list.reloadPage();
                });
                HorizontalPanel divPanel = new HorizontalPanel();
                divPanel.add(showAll);
                divPanel.setStyleName("showAllCheckBox file--BankAccountingListView");
                return divPanel;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getSingular(accountingStrings.noBankAccountMessage1(), wfmStrings.bankAccount()));
                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_ADD)/*Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT)*/) {
                    message.setTextBeforeLink(property.getPlural(accountingStrings.noBankAccountMessageBeforeLink(), wfmStrings.bankAccounts()));
                    message.setHref("bank|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_EDIT);
            }
        };
    }

    private ListingRequestProvider<BankAccount> getListData() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setShowActive(!showAll.getValue());
            filterParametr = filterParametrs;
            accountingService.getBankAccounts(filterParametrs, new AsyncCallback<ListResult<BankAccount>>() {
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void onSuccess(ListResult<BankAccount> list) {
                    callback.onSuccess(list);
                }
            });
        };
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.BankAccountPanel;
    }

    private ListingFilterParameter filterParametr;

    public void showSuccessMessage() {
        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.bankAccount()), Info.Type.INFO);
    }

    public void showFailureMessage() {
        Info.show(accountingStrings.infoMessage10(), Info.Type.WARNING);
    }

    public void showFailureMessage2() {
        Info.show(accountingStrings.infoMessage53(), Info.Type.WARNING);
    }

    public String getIconStyle() {
        return "accountMark  bank-accounts";
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
        return BANKACCOUNT;
    }
}
