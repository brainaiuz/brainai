package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DeleteRPC;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrChartOfAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.FeatureConstants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Abror Abdukadirov
 * Date: 09.11.2017 16:50
 */
public class ChartOfAccountListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants, FeatureConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();


    private final AccountingServiceAsync accountingService = AccountingService.App.get();

    private ListingPanel<AccountListItem> listPanel;
    private boolean hideEmptyAccount;
    private MenuBar menuBar;
    protected HashSet<AccountListItem> selectedItems = new HashSet<>();
    private final ActionButton deleteSelectedButton = null;

    public ChartOfAccountListView() {
        super("accountList", wfmStrings.chartOfAccounts());
        if (Utils.hasPermission(ACCOUNTING_ACCOUNT_ADD)) {
            setAddNew("account|add/add");
        }
    }

    @Override
    public String getIconStyle() {
        return "accountMark account-list";
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel<>(ListPanelType.AccoundListPanel, getColumn(), getRequestProvider(), getPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        listPanel.setPDFListener(clickEvent -> {
            if (listPanel.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/chartOfAccountListPDFHandler";
            ListingFilterParameter listingFilterParameter = listPanel.getFilterParametrs();
            if (listingFilterParameter == null) {
                listingFilterParameter = new ListingFilterParameter();
            }
            listPanel.callListPDF(pdfURL, listingFilterParameter);
        });

        listPanel.setExcelListener(clickEvent -> {
            String excelURL;
            excelURL = CommandConstants.COMMON_URL + "/chartOfAccountListExcelHandler";
            ListingFilterParameter listingFilterParameter = listPanel.getFilterParametrs();
            if (listingFilterParameter == null) {
                listingFilterParameter = new ListingFilterParameter();
            }
            listPanel.callListExcel(excelURL, listingFilterParameter);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ACCOUNT_SAVED, ChartOfAccountListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_CHECK_SAVED, ChartOfAccountListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_CHECK_DELETED, ChartOfAccountListView.this, (sender, args) -> listPanel.reloadPage());

        listPanel.setOnReset(() -> {
            listPanel.getFilterParametrs().setCheckNumber(false);
            listPanel.getFilterParametrs().setAccountType(null);
        });

        listPanel.addSelectionRowHandler(selectedRows -> {
            if (selectedRows.size() > 0) {
                selectedItems = selectedRows;
                if (deleteSelectedButton != null) {
                    deleteSelectedButton.setVisible(true);
                }
            } else {
                if (deleteSelectedButton != null) {
                    deleteSelectedButton.setVisible(false);
                }
            }
        });

        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumn() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column;

        column = new ColumnDefinitionConfig<AccountListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            int actionItemCount = 0;

            @Override
            public Anchor getCellValue(AccountListItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                final AccountListItem item = rowValue;
                actionItemCount = 0;
                if (Utils.hasPermission(ACCOUNTING_ACCOUNT_SUMMARY)) {
                    MenuPopItem accountSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-sales-quote-small");
                    accountSummary.getElement().setId("Chart_of_account_summary");
                    accountSummary.setCommand(() -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("chartOfAccount|chartOfAccountSummary/" + item.getObjectID(), item.getCode(), item.getName());
                    });
                    actionItemCount++;
                    menuBar.addItem(accountSummary);
                }

                if (Utils.hasPermission(ACCOUNTING_ACCOUNT_EDIT)) {
                    MenuPopItem accountEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    accountEdit.getElement().setId("Chart_of_account_edit_button");
                    accountEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("account|edit/" + item.getObjectID(), item.getCode(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(accountEdit);
                }
                if (item.isEditable()) {
                    if (Utils.hasPermission(ACCOUNTING_ACCOUNT_DELETE)) {
                        MenuPopItem accountDelete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                        accountDelete.getElement().setId("Chart_of_account_delete");
                        accountDelete.setCommand(() -> {
                            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                            messageBox.setTitle(wfmStrings.warning());
                            messageBox.setMessage(wfmStrings.errorPayrollAccountInUse());
                            messageBox.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    LoadingPanel.loading(true);
                                    accountingService.removeGLAccount(item.getObjectID(), new AbstractAsyncCallback<DeleteRPC>() {
                                        public void failure(Throwable caught) {
                                            LoadingPanel.loading(false);
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        public void success(DeleteRPC result) {
                                            LoadingPanel.loading(false);
                                            if (result.getDeleted() != null && result.getDeleted()) {
                                                listPanel.reloadPage();
                                                Info.show(result.getMessage(), Info.Type.INFO);
                                            } else {
                                                Info.show(result.getMessage(), Info.Type.WARNING);
                                            }
                                        }
                                    });
                                }
                            });
                            messageBox.open();

                        });
                        actionItemCount++;
                        menuBar.addItem(accountDelete);
                    }
                }

                if ("ACCOUNTING".equalsIgnoreCase(GWT.getModuleName()) && ACCOUNT_TYPE_BANK.equals(item.getAccountTypeCode())) {
                    if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_SPEND_ADD)) {
                        //Spend Money
                        MenuPopItem spendMoney = new MenuPopItem(wfmStrings.bankPayment(), "icon-puchase-invoise-small");
                        spendMoney.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|add/add/" + AccountingConstants.SPEND_MONEY_STR + "/" + "relatedBankAccount" + "/" + item.getBankAccountId()));
                        actionItemCount++;
                        menuBar.addItem(spendMoney);
                    }

                    if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD)) {
                        //Receive Money
                        MenuPopItem receiveMoney = new MenuPopItem(accountingStrings.receiveMoney(), "icon-puchase-invoise-small");
                        receiveMoney.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|add/add/" + AccountingConstants.RECEIVE_MONEY_STR + "/" + "relatedBankAccount" + "/" + item.getBankAccountId()));
                        actionItemCount++;
                        menuBar.addItem(receiveMoney);
                    }

                    if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_TRANSFER)) {
                        //Transfer Money
                        MenuPopItem transferMoney = new MenuPopItem(accountingStrings.transferMoney(), "icon-puchase-invoise-small");
                        transferMoney.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("transfer|add/add/" + item.getBankAccountId()));
                        actionItemCount++;
                        menuBar.addItem(transferMoney);
                    }

                    if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_TRANSACTION_IMPORT)) {
                        //Import Transactions
                        MenuPopItem importTransactions = new MenuPopItem(accountingStrings.importTransactions(), "icon-attachment");
                        importTransactions.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("importbanktransactions|importTransactions/" + item.getBankAccountId()));
                        actionItemCount++;
                        menuBar.addItem(importTransactions);
                    }

                    if (Utils.hasPermission(ACCOUNTING_BANK_STATEMENT)) {
                        //View all Statements
                        MenuPopItem bankStatements = new MenuPopItem(accountingStrings.viewAllStatements(), "icon-task-small");
                        bankStatements.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("bankStatementList|bankStatements/" + item.getBankAccountId()));
                        actionItemCount++;
                        menuBar.addItem(bankStatements);
                    }
                }
                if (Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_TRANSACTIONS)) {
                    //Account  Transactions
                    MenuPopItem accountTransactions = new MenuPopItem(accountingStrings.transaction(), "icon-task-small");
                    accountTransactions.getElement().setId("Chart_of_accounts_transactions");
                    accountTransactions.setCommand(() -> {

                        if ("ACCOUNTING".equalsIgnoreCase(GWT.getModuleName())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("accountTransactionsList|accountTransactions/" + item.getObjectID() + (ACCOUNT_TYPE_BANK.equals(item.getAccountTypeCode()) ? "/" + BANK_ACCOUNT : ""));
                        } else {
                            String transactions = GWT.getHostPageBaseURL() + "Accounting.html#" + "accountTransactionsList|accountTransactions/" + item.getObjectID() + (ACCOUNT_TYPE_BANK.equals(item.getAccountTypeCode()) ? "/" + BANK_ACCOUNT : "");

                            Window.open(transactions, "_blank", "");
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(accountTransactions);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }

        };
        column.setColumnSortable(false);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        columns.add(column);

        column = new ColumnDefinitionConfig<AccountListItem, String>(wfmStrings.code(), AccountListItem.CODE, 60) {
            @Override
            public String getCellValue(AccountListItem rowValue) {
                return rowValue.getCode();
            }
        };
        column.setMinimumColumnWidth(30);
        columns.add(column);

        column = new ColumnDefinitionConfig<AccountListItem, SimpleLink>(wfmStrings.name(), AccountListItem.NAME, 120) {
            @Override
            public SimpleLink getCellValue(AccountListItem rowValue) {
                return getLink(rowValue.getName(), "chartOfAccount|chartOfAccountSummary/" + rowValue.getObjectID(), rowValue.getCode(), rowValue.getName());
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

        column = new ColumnDefinitionConfig<AccountListItem, String>(wfmStrings.parent(), AccountListItem.PARENT, 120) {
            @Override
            public String getCellValue(AccountListItem rowValue) {
                return rowValue.getParentName();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

        column = new ColumnDefinitionConfig<AccountListItem, String>(wfmStrings.type(), AccountListItem.TYPE, 100) {
            @Override
            public String getCellValue(AccountListItem rowValue) {
                return rowValue.getAccountType();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

        column = new ColumnDefinitionConfig<AccountListItem, String>(wfmStrings.currency(), AccountListItem.CURRENCY, 60) {
            @Override
            public String getCellValue(AccountListItem rowValue) {
                return rowValue.getCurrency();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<AccountListItem, SimpleLink>(wfmStrings.balance(), AccountListItem.BALANCE, 80) {
            @Override
            public SimpleLink getCellValue(AccountListItem rowValue) {
                String balance;
                if (rowValue.getBalance() != null) {
                    if (rowValue.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                        balance = "(" + AccountingUtils.get().formatPrice(new BigDecimal(-1).multiply(rowValue.getBalance())) + ")";
                    } else {
                        balance = AccountingUtils.get().formatPrice(rowValue.getBalance());
                    }
                } else {
                    balance = String.valueOf(0d);
                }
                SimpleLink slink = new SimpleLink(balance);
                slink.addClickHandler(e -> {
                    String blink = GWT.getHostPageBaseURL() + "Accounting.html#" + "clickedreport|transactionsByPeriod/" + rowValue.getObjectID();
                    Window.open(blink, "_blank", "");
                });
                return slink;
            }
        };
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(50);
        column.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        columns.add(column);


        column = new ColumnDefinitionConfig<AccountListItem, String>(wfmStrings.modifiedDate(), AccountListItem.LAST_UPDATED_DATE, 80) {
            @Override
            public String getCellValue(AccountListItem rowValue) {
                return rowValue.getLastUpdatedDate() != null ? DateUtils.format(rowValue.getLastUpdatedDate()) : "";
            }
        };
        column.setMinimumColumnWidth(60);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<AccountListItem, Integer>(wfmStrings.key(), AccountListItem.CHART_OF_ACCOUNT_KEY, 60) {
            @Override
            public Integer getCellValue(AccountListItem rowValue) {
                return rowValue.getChartOfAccountkey();
            }
        };

        column.setMinimumColumnWidth(30);
        columns.add(column);



        column = new ColumnDefinitionConfig<AccountListItem, String>(accountingStrings.showInExpenseClaim(), AccountListItem.SHOW_IN_EXPENSE_CLAIM, 80) {
            @Override
            public String getCellValue(AccountListItem rowValue) {
                return rowValue.getShowInExpense() !=null && rowValue.getShowInExpense() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(50);
        column.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<AccountListItem, String>(wfmStrings.active(), AccountListItem.ACTIVE, 80) {
            @Override
            public String getCellValue(AccountListItem rowValue) {
                return rowValue.isActive() !=null && rowValue.isActive() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(50);
        column.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        column.setShow(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<AccountListItem, String>(wfmStrings.enablePaymentsToThisAccount(), AccountListItem.ENABLE_PAYMENTS_TO_THIS_ACCOUNT, 80) {
            @Override
            public String getCellValue(AccountListItem rowValue) {
                return rowValue.getEnablePayments() !=null && rowValue.getEnablePayments() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(50);
        column.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        column.setShow(false);
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private ListingPanelDesign getPanelDesign() {
        return new ListingPanelDesign() {

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callBack) -> {
                            if (data.getStartDate() != null) {
                                data.setCustomDataPut(STARTDATE_NC, Utils.getStartDateNCForFilter(data.getStartDate()));
                            } else data.getCustomData().remove(STARTDATE_NC);
                            if (data.getEndDate() != null) {
                                data.setCustomDataPut(ENDDATE_NC, Utils.getEndDateNCForFilter(data.getEndDate()));
                            } else data.getCustomData().remove(ENDDATE_NC);
                            RbacService.App.get().getChartOfAccountFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    callBack.onFailure(throwable);
                                }

                                @Override
                                public void success(FacetFilterRpc result) {
                                    callBack.onSuccess(result);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = null;
                if (Utils.hasPermission(ACCOUNTING_ACCOUNT_ADD)) {
                    addNew = getAddNewButton();
                    addNew.ensureDebugId(CHART_OF_ACCOUNTS + "addNewButton");
                    addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("account|add/add"));
                }
                return addNew;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(ACCOUNTING_ACCOUNT_DELETE)) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.CHART_OF_ACCOUNTS, null);
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo("importchartofaccounts|add/add/" + imp.getObjectId());
                    }
                });

                ImportFileActionLink link = new ImportFileActionLink();
                link.addClickHandler(ch -> imp.open());
                menuContainer.add(link);

                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.noAccountsMessage());
                if (Utils.hasPermission(ACCOUNTING_ACCOUNT_ADD)) {
                    message.setTextBeforeLink(accountingStrings.noAccountsMessage2());
                    message.setHref("account|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<AccountListItem> getRequestProvider() {
        return (filterParametrs, listingCallback) -> accountingService.getAccountList(filterParametrs, new AbstractAsyncCallback<ListResult<AccountListItem>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ListResult<AccountListItem> result) {
                listingCallback.onSuccess(result);
            }
        });
    }

    /*private MenuBar getActions() {
        menuBar = new MenuBar(true);
        menuBar.addItem("<div style='margin-left:20px'>" + accountingStrings.all() + "</div>", true, (Command) () -> {
            listPanel.getFilterParametrs().setAccountType(null);
            listPanel.reloadPage();
        }).ensureDebugId(CHART_OF_ACCOUNTS + "all");
        menuBar.addItem("<div style='margin-left:20px'>" + accountingStrings.assets() + "</div>", true, (Command) () -> {
            listPanel.getFilterParametrs().setAccountType(ASSETS);
            listPanel.reloadPage();
        }).ensureDebugId(CHART_OF_ACCOUNTS + "assets");
        menuBar.addItem("<div style='margin-left:20px'>" + accountingStrings.liabilities() + "</div>", true, (Command) () -> {
            listPanel.getFilterParametrs().setAccountType(LIABILITIES);
            listPanel.reloadPage();
        }).ensureDebugId(CHART_OF_ACCOUNTS + "liabilities");
        menuBar.addItem("<div style='margin-left:20px'>" + accountingStrings.equity() + "</div>", true, (Command) () -> {
            listPanel.getFilterParametrs().setAccountType(EQUITY);
            listPanel.reloadPage();
        }).ensureDebugId(CHART_OF_ACCOUNTS + "equity");
        menuBar.addItem("<div style='margin-left:20px'>" + accountingStrings.expenses() + "</div>", true, (Command) () -> {
            listPanel.getFilterParametrs().setAccountType(EXPENSES);
            listPanel.reloadPage();
        }).ensureDebugId(CHART_OF_ACCOUNTS + "expenses");
        menuBar.addItem("<div style='margin-left:20px'>" + accountingStrings.revenue() + "</div>", true, (Command) () -> {
            listPanel.getFilterParametrs().setAccountType(REVENUE);
            listPanel.reloadPage();
        }).ensureDebugId(CHART_OF_ACCOUNTS + "revenue");
        String title = null;
        if (hideEmptyAccount) {
            title = accountingStrings.showAllAccounts();
        } else {
            title = accountingStrings.hideEmptyAccounts();
        }
        menuBar.addItem("<div style='margin-left:20px'>" + title + "</div>", true, (Command) () -> {
            hideEmptyAccount = !hideEmptyAccount;
            listPanel.getFilterParametrs().setCheckNumber(hideEmptyAccount);
            listPanel.reloadPage();
        }).ensureDebugId(CHART_OF_ACCOUNTS + title);

        return menuBar;
    }*/

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.chartOfAccounts());
        contentConfigure.addContentConfigure(FacetContentType.ChartOfAccountFacetFilter.getContentCode()[0], wfmStrings.parent(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrChartOfAccountRepresenter.FIELD_PARENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrChartOfAccountRepresenter.FIELD_PARENT_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ChartOfAccountFacetFilter.getContentCode()[1], wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrChartOfAccountRepresenter.FIELD_TYPE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrChartOfAccountRepresenter.FIELD_TYPE_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ChartOfAccountFacetFilter.getContentCode()[2], wfmStrings.category(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrChartOfAccountRepresenter.FIELD_TYPE_CATEGORY;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrChartOfAccountRepresenter.FIELD_TYPE_CATEGORY;
            }

            @Override
            public boolean isConditionItemId() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ChartOfAccountFacetFilter.getContentCode()[3], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrChartOfAccountRepresenter.FIELD_ACTIVE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrChartOfAccountRepresenter.FIELD_ACTIVE;
            }

            @Override
            public boolean isWithID() {
                return false;
            }
        });

        contentConfigure.setDatePeriodPanelEnabled(false);
        return contentConfigure;
    }

    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(accountingMessages.pleaseSelectOneRow(wfmStrings.account()), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        AccountListItem item = selectedItems.iterator().next();
        String message = wfmStrings.errorPayrollAccountInUse();
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    accountingService.deleteSelectedAccounts(ids, new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Boolean result) {
                            LoadingPanel.loading(false);
                            listPanel.reloadPage();
                            if (result == null) {
                                Info.show(accountingStrings.errorDeletingAccount(), Info.Type.WARNING);
                            } else if (result) {
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.accounts()), Info.Type.INFO);
                            } else {
                                Info.show(wfmStrings.youCannotDeleteUsedTransaction(), Info.Type.WARNING);
                            }
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    public static ArrayList<Integer> getIDsOnly(Set<AccountListItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (AccountListItem item : selectedItems) {
            ids.add(item.getObjectID());
        }
        return ids;
    }


    @Override
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
}
