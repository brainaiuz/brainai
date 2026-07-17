package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.bundles.AccountingReportsImageBundles;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionsBetweenDatesInAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.PrepaymentService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 14.05.2010
 * Time: 21:57:07
 * To change this template use File | Settings | File Templates.
 */
public class AccountTransactionsListView extends BaseListView implements Constants, AccountingConstants {

    private static final AccountingReportsImageBundles reportsImageBundle = AccountingReportsImageBundles.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingServiceAsync accountingService = AccountingService.App.get();

    private ListingPanel<Transaction> list;

    private final Integer accountID;
    private final boolean isBankAccount;

    public AccountTransactionsListView(Integer accountID, boolean isBankAccount) {
        super("accountTransactions", isBankAccount ? Property.get(Constants.BANKACCOUNT, accountingStrings.bankAccountTransactions(), wfmStrings.bankAccount()) : accountingStrings.accountTransactions());
        this.accountID = accountID;
        this.isBankAccount = isBankAccount;
    }

    public AccountTransactionsListView(Integer accountID, boolean isBankAccount, String view) {
        super("accountTransactions", view);
        this.accountID = accountID;
        this.isBankAccount = isBankAccount;
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.AccountTransactionsListPanel, drawColumns(), getDataList(), getDesign());

        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/accountTransactionsListPDFHandler";
            setFilterValues(list.getFilterParametrs());
            list.callListPDF(pdfURL, list.getFilterParametrs());
        });
        list.setExcelListener(clickEvent -> {
            String excelUrl = CommandConstants.COMMON_URL + "/downloadAccountTransactionsListExcel";
            setFilterValues(list.getFilterParametrs());
            list.callListExcel(excelUrl, list.getFilterParametrs());
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MONEY_TRANSFER, AccountTransactionsListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TRANSACTION_RECONCILED, AccountTransactionsListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, AccountTransactionsListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PREPAYMENT_SAVE, AccountTransactionsListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
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
                        ArrayList<String> fields = new ArrayList<>(3);
                        fields.add(ListingChooseFilter.STATUS);
                        fields.add(ListingChooseFilter.FROM_DATE);
                        fields.add(ListingChooseFilter.TO_DATE);
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.AccountTransactions;
                    }
                };
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyYouDoNotHaveAnyManualEntry());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void setFilterValues(ListingFilterParameter filterParametrs) {
        filterParametrs.setAccountID(accountID);
        filterParametrs.setShowBudget(false);
        if (filterParametrs.getStartDate() != null) {
            filterParametrs.setStartDateNC(Utils.getStartDateNCForFilter(filterParametrs.getStartDate()));
        }
        if (filterParametrs.getEndDate() != null) {
            filterParametrs.setEndDateNC(Utils.getEndDateNCForFilter(filterParametrs.getEndDate()));
        }
    }

    private ListingRequestProvider<Transaction> getDataList() {
        return (filterParametrs, callback) -> {
            setFilterValues(filterParametrs);
            accountingService.findTransactionsByAccountAndJournalDate(filterParametrs, null, null, new AbstractAsyncCallback<TransactionsBetweenDatesInAccount>() {
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                public void success(TransactionsBetweenDatesInAccount result) {
                    callback.onSuccess(new ListResult<>(new ArrayList<>(result.getTransactions()), result.getTotalCount()));
                }
            });
        };
    }

    private ColumnDefinitionConfig[] drawColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[isBankAccount ? 10 : 6];

        int index = 0;

        columns[index] = new ColumnDefinitionConfig<Transaction, Object>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Object getCellValue(final Transaction transaction) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (INVOICEPAYMENT_TRANSACTION.equals(transaction.getTransactionType()) && RECEIVABLE_PREPAYMENT.equals(transaction.getInvoiceOrPaymentType())) {
                    MenuPopItem editPrePayment = new MenuPopItem(wfmStrings.edit());
                    editPrePayment.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("prepayment|edit/" + transaction.getKeyId()));
                    actionItemCount++;
                    menuBar.addItem(editPrePayment);

                    if (!RECONCILED.equals(transaction.getReconcileStatus())) {
                        MenuPopItem deletePrePayment = new MenuPopItem(wfmStrings.delete());
                        deletePrePayment.setCommand(() -> PrepaymentService.App.get().deletePrePayment(transaction.getKeyId(), new AsyncCallback<Integer>() {
                            @Override
                            public void onFailure(Throwable caught) {

                            }

                            @Override
                            public void onSuccess(Integer result) {
                                if (result == -1) {
                                    Info.show(accountingStrings.totalPrepaymentLessThanAmountAppliedErrorMessage(), Info.Type.WARNING);
                                } else {
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.prepayment()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PREPAYMENT_SAVE, null, AccountTransactionsListView.this);
                                    list.reloadPage();
                                }
                            }
                        }));
                        actionItemCount++;
                        menuBar.addItem(deletePrePayment);
                    }
                }

                if (isBankAccount) {
                    if (transaction.getReconcileStatus() == null || UNRECONCILED.equals(transaction.getReconcileStatus())) {
                        MenuPopItem markAsReconciled = new MenuPopItem(wfmStrings.markAsReconciled());
                        markAsReconciled.setCommand(() -> changeReconsileStatus(transaction.getTransactionId(), MARKED_AS_RECONCILED, transaction.getJournalName()));
                        actionItemCount++;
                        menuBar.addItem(markAsReconciled);
                    }
                    if (BANK_TRANSFER_TRANSACTION.equals(transaction.getTransactionType()) || BANK_MONEY_TRANSFER_TRANSACTION.equals(transaction.getTransactionType())) {
                        MenuPopItem viewBankTransfer;
                        if (BANK_MONEY_TRANSFER_TRANSACTION.equals(transaction.getTransactionType())) {
                            viewBankTransfer = new MenuPopItem(wfmStrings.summaryView() + " " + accountingStrings.transferMoney());
                        } else {
                            viewBankTransfer = new MenuPopItem(wfmStrings.summaryView() + " " + (RECEIVE_MONEY.equals(transaction.getSpendReceiveMoneyType()) ? accountingStrings.receiveMoney() : accountingStrings.bankPayments()));
                        }

                        viewBankTransfer.setCommand(() -> {
                            if (BANK_MONEY_TRANSFER_TRANSACTION.equals(transaction.getTransactionType())) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("transfer|summary/" + transaction.getKeyId() + "/" + VIEW_FORM);
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|summary/" + transaction.getKeyId()
                                        + "/" + (RECEIVE_MONEY.equals(transaction.getSpendReceiveMoneyType()) ? RECEIVE_MONEY_STR : SPEND_MONEY_STR));
                            }
                        });
                        actionItemCount++;
                        menuBar.addItem(viewBankTransfer);

                        MenuPopItem editBankTransfer;

                        if (BANK_MONEY_TRANSFER_TRANSACTION.equals(transaction.getTransactionType())) {
                            editBankTransfer = new MenuPopItem(wfmStrings.edit() + " " + accountingStrings.transferMoney());
                        } else {
                            editBankTransfer = new MenuPopItem(wfmStrings.edit() + " " + (RECEIVE_MONEY.equals(transaction.getSpendReceiveMoneyType()) ? accountingStrings.receiveMoney() : wfmStrings.bankPayment()));
                        }
                        editBankTransfer.setCommand(() -> {
                            if (BANK_MONEY_TRANSFER_TRANSACTION.equals(transaction.getTransactionType())) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("transfer|edit/" + transaction.getKeyId() + "/" + EDIT_FORM);
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|edit/" + transaction.getKeyId()
                                        + "/" + (RECEIVE_MONEY.equals(transaction.getSpendReceiveMoneyType()) ? RECEIVE_MONEY_STR : SPEND_MONEY_STR));
                            }
                        });
                        actionItemCount++;
                        menuBar.addItem(editBankTransfer);

                        if (!RECONCILED.equals(transaction.getReconcileStatus())) {
                            MenuPopItem deleteBankTransfer = new MenuPopItem(wfmStrings.delete());
                            deleteBankTransfer.setCommand(() -> {
                                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                                messageBox.setTitle(wfmStrings.confirmation());
                                messageBox.setMessage(wfmStrings.areYouSureWantToDeleteThe() + " " + accountingStrings.accountTransaction().toLowerCase() + "?");
                                messageBox.addCloseHandler(new CloseHandler() {
                                    @Override
                                    public void onSubmit() {
                                        deleteBankTransfer(transaction.getKeyId(), transaction.getTransactionType());
                                    }
                                });
                                messageBox.open();
                            });
                            actionItemCount++;
                            menuBar.addItem(deleteBankTransfer);
                        }
                    }

                }
                if (actionItemCount > 0) {
                    ToolItem toolItem = new ToolItem(actionItemCount);
                    toolItem.setWidget(menuBar);
                    return toolItem.getAction();
                }
                return null;
            }
        };
        columns[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index].setColumnSortable(false);
        index++;

        if (isBankAccount) {
            columns[index] = new ColumnDefinitionConfig<Transaction, Image>("", "icon", 40) {
                @Override
                public Image getCellValue(final Transaction transaction) {
                    Image image = null;

                    if (RECONCILED.equals(transaction.getReconcileStatus())) {
                        image = new Image(reportsImageBundle.reconciled());
                        image.setTitle(accountingStrings.reconciledReconciledUsingBankReconciliation());
                        image.addClickHandler(event -> changeReconsileStatus(transaction.getTransactionId(), UNRECONCILED, transaction.getJournalName()));
                    } else if (MARKED_AS_RECONCILED.equals(transaction.getReconcileStatus())) {
                        image = new Image(reportsImageBundle.markedAsReconsiled());
                        image.setTitle(accountingStrings.markedAsReconciled());
                        image.addClickHandler(event -> changeReconsileStatus(transaction.getTransactionId(), UNRECONCILED, transaction.getJournalName()));
                    } else {
                        image = new Image(reportsImageBundle.notReconciled());
                        image.setTitle(accountingStrings.notYetReconciled());
                    }
                    return image;
                }
            };
            columns[index].setMinimumColumnWidth(40);
            columns[index].setMaximumColumnWidth(40);
            index++;

            columns[index] = new ColumnDefinitionConfig<Transaction, String>(wfmStrings.status(), STATUS_COLUMN, 100) {
                @Override
                public String getCellValue(Transaction transaction) {
                    String status = null;

                    if (RECONCILED.equals(transaction.getReconcileStatus())) {
                        status = accountingStrings.reconciled();
                    } else if (MARKED_AS_RECONCILED.equals(transaction.getReconcileStatus())) {
                        status = accountingStrings.markedAsReconciled();
                    } else {
                        status = accountingStrings.notReconciled();
                    }
                    return status;
                }
            };
            columns[index].setMinimumColumnWidth(40);
            index++;

            columns[index] = new ColumnDefinitionConfig<Transaction, String>(accountingStrings.bankTransferDesc(), BANK_TRANSFER_DESCRIPTION_COLUMN, 350) {
                @Override
                public String getCellValue(Transaction transaction) {
                    return transaction.getDescription() != null ? transaction.getDescription() : "";
                }
            };
            columns[index].setMinimumColumnWidth(40);
            columns[index].setShow(false);
            index++;

            columns[index] = new ColumnDefinitionConfig<Transaction, String>(wfmStrings.number(), NUMBER_COLUMN, 100) {
                @Override
                public String getCellValue(Transaction transaction) {
                    if (!Utils.isNullOrEmpty(transaction.getSpendReceiveMoneyNumber())) {
                        return transaction.getSpendReceiveMoneyNumber();
                    }
                    if (!Utils.isNullOrEmpty(transaction.getManualJournalNumber())) {
                        return transaction.getManualJournalNumber();
                    }
                    if (!Utils.isNullOrEmpty(transaction.getInvoicePaymentNumber())) {
                        return transaction.getInvoicePaymentNumber();
                    }
                    if (!Utils.isNullOrEmpty(transaction.getCheckNumber())) {
                        return transaction.getCheckNumber();
                    }
                    return Utils.isNullOrEmpty(transaction.getNumber()) ? "" : transaction.getNumber();
                }
            };
            columns[index].setMinimumColumnWidth(40);
            columns[index].setShow(false);
            index++;
        }

        columns[index] = new ColumnDefinitionConfig<Transaction, String>(wfmStrings.date(), DATE_COLUMN, 100) {
            @Override
            public String getCellValue(Transaction transaction) {
                return DateUtils.format(transaction.getJournalDate());
            }
        };
        columns[index].setMinimumColumnWidth(40);
        index++;

        columns[index] = new ColumnDefinitionConfig<Transaction, String>(wfmStrings.description(), DESCRIPTION_COLUMN, 350) {
            @Override
            public String getCellValue(Transaction transaction) {
                return transaction.getJournalName();
            }
        };
        columns[index].setMinimumColumnWidth(40);
        index++;

        columns[index] = new ColumnDefinitionConfig<Transaction, String>(accountingStrings.referenceNumber(), REFERENCE_COLUMN, 100) {
            @Override
            public String getCellValue(Transaction transaction) {
                return transaction.getReference() != null ? transaction.getReference() : "";
            }
        };
        columns[index].setColumnSortable(false); // Sorting By Reference creates problems
        columns[index].setMinimumColumnWidth(40);
        index++;

        columns[index] = new ColumnDefinitionConfig<Transaction, String>(accountingStrings.spent(), SPENT_COLUMN, 100) {
            @Override
            public String getCellValue(Transaction transaction) {
                return transaction.getTotalCredit() != null ? AccountingUtils.get().formatPrice(transaction.getTotalCredit()) : "";
            }
        };
        columns[index].setMinimumColumnWidth(40);
        index++;

        columns[index] = new ColumnDefinitionConfig<Transaction, String>(accountingStrings.received(), RECEIVED_COLUMN, 100) {
            @Override
            public String getCellValue(Transaction transaction) {
                return transaction.getTotalDebit() != null ? AccountingUtils.get().formatPrice(transaction.getTotalDebit()) : "";
            }
        };
        columns[index].setMinimumColumnWidth(40);

        return columns;
    }

    private void changeReconsileStatus(final Integer transactionID, final String status, String name) {
        if (UNRECONCILED.equals(status)) {
            final WfmMessageBox confirmBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
            confirmBox.setTitle(wfmStrings.information());
            confirmBox.setMessage(accountingStrings.areYouSureWantUnreconcile() + "\"" + name + "\"?");
            confirmBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    reconcileOrUnReconcileTransaction(transactionID, status);
                }
            });
            confirmBox.open();
        } else {
            reconcileOrUnReconcileTransaction(transactionID, status);
        }
    }

    private void reconcileOrUnReconcileTransaction(Integer transactionID, final String status) {
        accountingService.changeReconcileStatus(transactionID, status, accountID, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(Void result) {
                list.reloadPage();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_RECONCILESTATUS_CHANGED, result, AccountTransactionsListView.this);
                String text = null;
                if (RECONCILED.equals(status)) {
                    text = accountingStrings.transactionReconciledSuccessfully();
                } else if (UNRECONCILED.equals(status)) {
                    text = accountingStrings.transactionUnreconciledSuccessfully();
                } else if (MARKED_AS_RECONCILED.equals(status)) {
                    text = accountingStrings.transactionMarkedReconciled();
                }
                if (text != null) {
                    Info.show(text, Info.Type.INFO);
                }
            }
        });
    }

    private void deleteBankTransfer(Integer objectId, String type) {
        accountingService.deleteBankTransfer(objectId, type, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.accountTransaction()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_ACCOUNT_TRANSACTION_DELETED, null, AccountTransactionsListView.this);
                list.reloadPage();
            }
        });
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
