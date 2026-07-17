package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.HashMap;

import static com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants.CHECK_NUMBER;

/**
 * Created by Dilshod on 1/22/15.
 */
public abstract class BankTransferListView extends BaseListView implements Constants, AccountingConstants, PermissionConstants {

    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingServiceAsync accountingService = AccountingService.App.get();

    private final Integer type;
    private String typeStr;
    private String viewName;
    private String emptyMEssage1;
    private String emptyMEssage2;
    private Boolean isFilterParametrs = false;

    private ListingPanel<NewManualTransaction> list;
    private ImportFilePopUp imp;

    public BankTransferListView(Integer type) {
        super(type.equals(RECEIVE_MONEY) ? "bankreceipt" :
                type.equals(SPEND_MONEY) ? "bankpayment" :
                        type.equals(CASH_RECEIPT) ? "cashreceipt" : "cashpayment");
        setDescription(
                type.equals(RECEIVE_MONEY) ? property.getPlural(accountingStrings.bankReceipts()) :
                        (type.equals(SPEND_MONEY) ? property.getPlural(accountingStrings.bankPayments()) :
                                (type.equals(CASH_RECEIPT) ? property.getPlural(wfmStrings.cashReceipt()) : property.getPlural(wfmStrings.cashPayment()))));
        this.type = type;
        findViewType(type);
        if (hasPermissionToAdd()) {
            setAddNew("spendreceivemoney|add/add/" + typeStr);
        }
    }

    private boolean hasPermissionToAdd() {
        return RECEIVE_MONEY.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD) ||
                SPEND_MONEY.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_SPEND_ADD) ||
                CASH_RECEIPT.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_ADD) ||
                CASH_PAYMENT.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_ADD);
    }

    private void findViewType(Integer type) {
        switch (type) {
            case 0:
                typeStr = RECEIVE_MONEY_STR;
                viewName = accountingStrings.bankReceipts();
                emptyMEssage1 = accountingStrings.currentlyYouDoNotHaveAnyBankReceipt();
                emptyMEssage2 = accountingStrings.noBankReceipt();
                break;
            case 1:
                typeStr = SPEND_MONEY_STR;
                viewName = accountingStrings.bankPayments();
                emptyMEssage1 = accountingStrings.currentlyYouDoNotHaveAnyBankPayment();
                emptyMEssage2 = accountingStrings.noBankPayment();
                break;
            case 2:
                typeStr = CASH_RECEIPT_STR;
                viewName = wfmStrings.cashReceipt();
                emptyMEssage1 = accountingStrings.currentlyYouDoNotHaveAnyCashReceipt();
                emptyMEssage2 = accountingStrings.noCashRecepient();
                break;
            case 3:
                typeStr = CASH_PAYMENT_STR;
                viewName = wfmStrings.cashPayment();
                emptyMEssage1 = accountingStrings.currentlyYouDoNotHaveAnyCashPayment();
                emptyMEssage2 = accountingStrings.noCashPayment();
                break;
        }
    }

    protected abstract ListPanelType getPanelType();

    @Override
    protected Widget onInitialize() {
        list = new GuideListingPanel(getPanelType(), getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());
        list.setPDFListener(clickEvent -> {
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/bankTransferListPDFHandler";
            ListingFilterParameter filterParameters = list.getFilterParametrs();
            filterParameters.setPropertyCode(getPropertyCode());
            setFilterValues(filterParameters);
            list.callListPDF(pdfURL, filterParameters);
        });

        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/bankTransferListExcelHandler";
            ListingFilterParameter filterParameters = list.getFilterParametrs();
            filterParameters.setPropertyCode(getPropertyCode());
            setFilterValues(filterParameters);
            list.callListExcel(excelURL, filterParameters);
        });

        add(list);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_TRANSFER_LIST_UPDATE, BankTransferListView.this, (sender, args) -> list.reloadPage());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_ACCOUNT_TRANSACTION_DELETED, BankTransferListView.this, (sender, args) -> list.reloadPage());

        return null;
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                return BankTransferListView.this::addNewBankTransfer;
            }

            @Override
            public Command getUploadButtonCommand() {
                return () -> imp.open();
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
                        ArrayList<String> fields = new ArrayList<>();
                        fields.add(ListingChooseFilter.RELATED_PROJECT);
                        fields.add(ListingChooseFilter.FROM_AMOUNT);
                        fields.add(ListingChooseFilter.TO_AMOUNT);
                        fields.add(ListingChooseFilter.FROM_DATE);
                        fields.add(ListingChooseFilter.TO_DATE);
                        fields.add(ListingChooseFilter.CREATOR);
                        return fields;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToAdd()) {

                    final ActionButton addNew = getAddNewButton();

                    addNew.addClickHandler(clickEvent -> addNewBankTransfer());
                    return addNew;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                imp = new ImportFilePopUp(ImportTypeEnum.BANK_TRANSFER_TRANSACTION, typeStr);
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo("importbanktransfer|add/add/" + imp.getObjectId() + "/" + typeStr);
                    }
                });

                ImportFileActionLink link = new ImportFileActionLink();
                link.addClickHandler(ch -> imp.open());
                menuContainer.add(link);

                exportOption.initExport(null);
            }
            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(emptyMEssage1);
                if (hasPermissionToAdd()) {
                    message.setTextBeforeLink(emptyMEssage2);
                    message.setHref("spendreceivemoney|add/add/" + typeStr);
                }
                emptyDataTable.initEmptyDataTable(message);
            }

        };
    }

    private SinksContainer addNewBankTransfer() {
        return SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|add/add/" + typeStr);
    }

    private void setFilterValues(ListingFilterParameter filterParametrs) {
        filterParametrs.setType(type);
        filterParametrs.setViewType(viewName);
        isFilterParametrs = true;
        filterParametrs.setStartDateNC(filterParametrs.getStartDate() != null ? Utils.getStartDateNCForFilter(filterParametrs.getStartDate()) : null);
        filterParametrs.setEndDateNC(filterParametrs.getEndDate() != null ? Utils.getEndDateNCForFilter(filterParametrs.getEndDate()) : null);
    }

    private ListingRequestProvider<NewManualTransaction> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            if (!isFilterParametrs) {
                setFilterValues(filterParametrs);
            }
            accountingService.getBankCashTransferList(filterParametrs, new AbstractAsyncCallback<ListResult<NewManualTransaction>>() {
                @Override
                public void failure(Throwable throwable) {
                    isFilterParametrs = false;
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<NewManualTransaction> result) {
                    isFilterParametrs = false;
                    callback.onSuccess(result);
                }
            });
        };
    }


    private int actionItemCount;

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ArrayList<ColumnDefinitionConfig> columnList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<NewManualTransaction, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final NewManualTransaction item) {
                actionItemCount = 0;
                boolean hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate()));
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem itemView = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                itemView.setCommand(() -> {
                    if (PAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|summary/" + item.getObjectId() + "/" + ((RECEIVE_MONEY.equals(type) || CASH_RECEIPT.equals(type)) ? Constants.RECEIVABLE : Constants.PAYABLE), item.getNumber());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|summary/" + item.getObjectId() + "/" + typeStr, item.getNumber());
                    }
                });
                if (CASH_RECEIPT.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_SUMMARY) ||
                        CASH_PAYMENT.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_SUMMARY) ||
                        RECEIVE_MONEY.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_SUMMARY) ||
                        SPEND_MONEY.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_SPEND_SUMMARY)) {
                    actionItemCount++;
                    menuBar.addItem(itemView);
                }

                if (!hasAccountingBeforeBlockDate && !item.isUsed()) {
                    MenuPopItem itemEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    itemEdit.setCommand(() -> {
                        if (PAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|edit/" + item.getObjectId() + "/" + ((RECEIVE_MONEY_STR.equals(typeStr) || CASH_RECEIPT_STR.equals(typeStr)) ? Constants.RECEIVABLE : Constants.PAYABLE), item.getNumber());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|edit/" + item.getObjectId() + "/" + typeStr, item.getNumber());
                        }
                    });
                    if (CASH_RECEIPT.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_EDIT) ||
                            CASH_PAYMENT.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_EDIT) ||
                            RECEIVE_MONEY.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT) ||
                            SPEND_MONEY.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_SPEND_EDIT)) {
                        actionItemCount++;
                        menuBar.addItem(itemEdit);
                    }
                }

                if (!hasAccountingBeforeBlockDate && !item.isUsed()) {
                    MenuPopItem itemDelete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    itemDelete.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());

                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());

                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                if (PAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                                    InvoiceService.App.get().deleteBatchPayment(item.getObjectId(), new AbstractAsyncCallback<TestRPC>() {
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
                                            Info.show("Payment deleted successfully", Info.Type.INFO);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, null, BankTransferListView.this);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_ACCOUNT_TRANSACTION_DELETED, null, BankTransferListView.this);
                                        }
                                    });
                                } else {
                                    accountingService.deleteBankTransfer(item.getObjectId(), BANK_TRANSFER_TRANSACTION, new AbstractAsyncCallback<Void>() {
                                        public void failure(Throwable caught) {
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        public void success(Void result) {
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.accountTransaction()), Info.Type.INFO);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_ACCOUNT_TRANSACTION_DELETED, null, BankTransferListView.this);
                                        }
                                    });
                                }
                            }
                        });
                        messageBox.open();
                    });
                    if (CASH_RECEIPT.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_RECEIPT_DELETE) ||
                            CASH_PAYMENT.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_CASH_PAYMENT_DELETE) ||
                            RECEIVE_MONEY.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE) ||
                            SPEND_MONEY.equals(type) && Utils.hasPermission(ACCOUNTING_BANK_ACCOUNT_SPEND_DELETE)) {
                        actionItemCount++;
                        menuBar.addItem(itemDelete);
                    }
                }

                if (RECEIVE_MONEY.equals(type) || SPEND_MONEY.equals(type)) {
                    MenuPopItem copy = new MenuPopItem(wfmStrings.copy(), "icon-edit");
                    copy.setCommand(() -> {
                        if (PAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|add/add/" + ((RECEIVE_MONEY_STR.equals(typeStr) || CASH_RECEIPT_STR.equals(typeStr)) ? Constants.RECEIVABLE : Constants.PAYABLE) + "/" + "copy" + "/" + item.getObjectId());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|add/add/" + typeStr + "/" + "copy" + "/" + item.getObjectId());
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(copy);
                }

                MenuPopItem generatePdf = new MenuPopItem(wfmStrings.pdf(), "icon-pdf");
                final HTMLPanel htmlPanel = new HTMLPanel("");

                if (!PAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                    generatePdf.setCommand(() -> new PDFTemplateSelector(typeStr, new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(htmlPanel, id, item.getObjectId(), false);
                        }
                    }));
                } else {
                    final String templateType = ((RECEIVE_MONEY.equals(type) || CASH_RECEIPT.equals(type)) ? BATCH_RECEIVE_PAYMENT : BATCH_PAY_BILL);
                    generatePdf.setCommand(() -> new PDFTemplateSelector(templateType, new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(htmlPanel, id, item.getObjectId(), true);
                        }
                    }));
                }
                add(htmlPanel);
                actionItemCount++;
                menuBar.addItem(generatePdf);

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();

            }
        };

        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnList.add(column);

        column = new ColumnDefinitionConfig<NewManualTransaction, SimpleLink>(wfmStrings.number(), NUMBER_COLUMN, 80) {
            @Override
            public SimpleLink getCellValue(NewManualTransaction item) {
                if (PAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                    return getLink(item.getNumber(), "receivepayment|summary/" + item.getObjectId() + "/" + ((RECEIVE_MONEY.equals(type) || CASH_RECEIPT.equals(type)) ? Constants.RECEIVABLE : Constants.PAYABLE), item.getNumber());
                } else {
                    return getLink(item.getNumber(), "spendreceivemoney|summary/" + item.getObjectId() + "/" + typeStr, item.getNumber());
                }
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(40);
        columnList.add(column);

        column = new ColumnDefinitionConfig<NewManualTransaction, String>(wfmStrings.account(), ACCOUNT_COLUMN, 100) {
            @Override
            public String getCellValue(NewManualTransaction item) {
                return item.getAccount() != null ? item.getAccount().getName() : "";
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(40);
        columnList.add(column);

        column = new ColumnDefinitionConfig<NewManualTransaction, String>(wfmStrings.date(), DATE_COLUMN, 80) {
            @Override
            public String getCellValue(NewManualTransaction item) {
                return item.getDate() != null ? DateUtils.format1(item.getDate().getNonConvertedDate()) : wfmStrings.notAvailable();
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(40);
        columnList.add(column);

        column = new ColumnDefinitionConfig<NewManualTransaction, String>(wfmStrings.amount(), AMOUNT_COLUMN, 80) {
            @Override
            public String getCellValue(NewManualTransaction item) {
                return AccountingUtils.get().formatPrice(item.getTotal());
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        column.setMinimumColumnWidth(40);
        columnList.add(column);

        column = new ColumnDefinitionConfig<NewManualTransaction, String>(wfmStrings.currency(), CURRENCY_COLUMN, 80) {
            @Override
            public String getCellValue(NewManualTransaction item) {
                return item.getCurrency() != null ? item.getCurrency().getName() : AccountingUtils.getBaseCurrencyCode();
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        column.setMinimumColumnWidth(40);
        columnList.add(column);

        column = new ColumnDefinitionConfig<NewManualTransaction, String>(wfmStrings.reference(), REFERENCE_COLUMN, 100) {
            @Override
            public String getCellValue(NewManualTransaction item) {
                return item.getReference();
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(40);
        columnList.add(column);

        column = new ColumnDefinitionConfig<NewManualTransaction, String>(Property.get(Constants.PROJECT, wfmStrings.project()), PROJECT_COLUMN, 100) {
            @Override
            public String getCellValue(NewManualTransaction item) {
                return item.getProject() != null ? item.getProject().getName() : wfmStrings.notAvailable();
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        column.setMinimumColumnWidth(40);
        column.setShow(type.equals(CASH_PAYMENT) || type.equals(CASH_RECEIPT));
        columnList.add(column);

        column = new ColumnDefinitionConfig<NewManualTransaction, String>(accountingStrings.checkNumber(), CHECK_NUMBER, 80) {
            @Override
            public String getCellValue(NewManualTransaction item) {
                return item.getCheckNumber() != null ? item.getCheckNumber() : wfmStrings.notAvailable();
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        column.setMinimumColumnWidth(40);
        columnList.add(column);

        column = new ColumnDefinitionConfig<NewManualTransaction, String>(wfmStrings.createdBy(), CREATOR, 80) {
            @Override
            public String getCellValue(NewManualTransaction item) {
                return item.getCreator() != null ? item.getCreator() : "";
            }
        };
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        column.setMinimumColumnWidth(40);
        columnList.add(column);

        return columnList.toArray(new ColumnDefinitionConfig[columnList.size()]);
    }


    private void generatePDF(HTMLPanel hp, Integer pdfTemplateID, Integer objectID, boolean isPaymentTransaction) {
        TransactionPDFObject requestObject = null;
        String pdfURL = null;
        if (isPaymentTransaction) {
            boolean isReceivable = (RECEIVE_MONEY.equals(type) || CASH_RECEIPT.equals(type));
            requestObject = new TransactionPDFObject(objectID, pdfTemplateID, isReceivable ? Constants.RECEIVABLE : Constants.PAYABLE, type);
            pdfURL = CommandConstants.PDF_URL + (isReceivable ? "/batchReceivePaymentViewPDFHandler" : "/batchPayBillViewPDFHandler");
        } else {
            requestObject = new TransactionPDFObject(objectID, pdfTemplateID, viewName, type);
            pdfURL = CommandConstants.PDF_URL + "/spendMoneyViewPDFHandler";
        }
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    @Override
    public String getIconStyle() {
        return type.equals(SPEND_MONEY) ? "accountMark manual-journals" : "accountMark purchase-order-list";
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

    @Override
    public String getPropertyCode() {
        return super.getPropertyCode();
    }
}
