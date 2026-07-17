package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.AccountTransaction;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionsBetweenDatesInAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceFilterData;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCustomToolTip;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxDatePeriodItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.paging.PagingWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by admin on 15.09.2014.
 */
public class NewAccountTransaction extends Composite implements Constants, AccountingConstants {
    interface NewAccountTransactionUiBinder extends UiBinder<HTMLPanel, NewAccountTransaction> {
    }

    private static final NewAccountTransactionUiBinder ourUiBinder = GWT.create(NewAccountTransactionUiBinder.class);
    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final DateTimeFormat urlDateFormat = DateTimeFormat.getFormat("dd_MM_yyyy");
    private static final boolean isDepartmentRelationEnabled = AccountingUtils.get().isEnableAccountingDepartmentRelation();

    private Date externalStartDate;
    private Date externalEndDate;
    private Integer accountID;
    private String accountType = "";
    private String sortField;
    private String sortDirection;

    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    TableSectionElement tableHead;
    @UiField
    Element tableBody;
    @UiField
    HTMLPanel noResultMessage;
    @UiField
    HTMLPanel exportPanel;

    private Element balanceDiv;
    private KpiCheckBox showInBase;
    private KpiCheckBox cashBasis;
    private TextBox searchBox;
    private KpiModal filterDialog;
    private AccountsLookUp accountsLookUp;
    private DatePicker fromValue;
    private DatePicker toValue;
    private ProjectLookUp projectLookUp;
    private DepartmentLookUp departmentLookUp;
    private PagingWidget pagingWidget;
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;
    private String departmentIdFromProfitAndLoss = "";
    private String departmentNameFromProfitAndLoss = "";
    private String projectId = "";
    private String projectName = "";
    private KpiCustomToolTip currencyToolTip;
    private DataListBox currencyListBox;
    private Date currentDate;
    private Integer baseCurrencyId;
    private final boolean isEnableCurrency = Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CURRENCY_FIELD_IN_ACCOUNT_TRANSACTIONS);

    public NewAccountTransaction() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        onInitialize();
    }

    public NewAccountTransaction(Integer accountID, String[] params) {
        this();
        this.accountID = accountID;
        initFormParameters(params);
        if (departmentIdFromProfitAndLoss != null && departmentIdFromProfitAndLoss.trim().length() > 0) {
            departmentLookUp.setSelected(new SelectItem(Integer.parseInt(departmentIdFromProfitAndLoss), departmentNameFromProfitAndLoss));
        }
        if (projectId != null && projectId.trim().length() > 0) {
            projectLookUp.setSelected(new SelectItem(Integer.parseInt(projectId), projectName));
        }
        if (accountID != null) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setAccountID(accountID);
            filterParameter.setAllByFilter(true);
            AccountingService.App.get().getAccountsForInvoice(filterParameter, null, new AsyncCallback<AccountItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                    initInternal();
                }

                @Override
                public void onSuccess(AccountItem[] accountItems) {
                    if (accountItems != null && accountItems.length > 0) {
                        accountsLookUp.addAccountItem(accountItems[0]);
                        initInternal();
                    }
                }
            });
        }
    }

    private void setCurrency(CurrencyItem[] currencies) {
        currencyListBox.setItems(currencies);
        for (CurrencyItem currency : currencies) {
            if (currency.isCompanyCurrency()) {
                baseCurrencyId = currency.getId();
                currencyListBox.setSelected(currency);
            }
        }
    }

    private void initFormParameters(String[] params) {
        if (params.length >= 4 &&
                ("trialBalance".equals(params[1]) ||
                        "profitAndLoss".equals(params[1]) ||
                        "balanceSheet".equals(params[1]) ||
                        "newCashFlow".equals(params[1]) ||
                        "payment".equals(params[1]))) {

            if (("profitAndLoss".equals(params[1]) || "balanceSheet".equals(params[1])) && params[4] != null && params[5] != null) {
                departmentIdFromProfitAndLoss = params[4];
                departmentNameFromProfitAndLoss = params[5];
            }
            if (("profitAndLoss".equals(params[1]) || "balanceSheet".equals(params[1])) && params[6] != null && params[7] != null) {
                projectId = params[6];
                projectName = params[7];
            }

            if (params[2] != null) {
                externalStartDate = urlDateFormat.parse(params[2]);
                if (fromValue != null) {
                    fromValue.setDate(externalStartDate);
                }
            }
            if (params[3] != null) {
                externalEndDate = urlDateFormat.parse(params[3]);
                if (toValue != null) {
                    toValue.setDate(externalEndDate);
                }
            }
        }
    }

    public void onInitialize() {
//        tableHead.setAttribute("point_affix_top_when", "134");

        GBox groupBox = headerPanel.drawNewGroupBox();
        groupBox.setStyleUnited(true);
        groupBox.setStyleWidthFree(true);

        noResultMessage.getElement().setInnerHTML(accountingStrings.selectAnAccountToSeeResults());

        accountsLookUp = new AccountsLookUp();
        accountsLookUp.setShowAll(true);
        headerPanel.addGroupBoxItem(wfmStrings.account(), accountsLookUp).setWidth("275px");

        GBoxDatePeriodItem datePeriodItem = new GBoxDatePeriodItem();
        fromValue = new DatePicker();
        Date currentDate = new Date();
        fromValue.setDate(DateUtil.getMonthFirstDay(currentDate));
        fromValue.ensureDebugId("accountTransaction-fromDatePicker");
        datePeriodItem.setStartBoxItem(wfmStrings.from(), fromValue);
        toValue = new DatePicker();
        toValue.setDate(currentDate);
        toValue.ensureDebugId("accountTransaction-toDatePicker");
        datePeriodItem.setDueBoxItem(wfmStrings.to(), toValue);
        headerPanel.addGroupBoxItem(datePeriodItem);


        if (externalStartDate != null && externalEndDate != null) {
            fromValue.setDate(externalStartDate);
            toValue.setDate(externalEndDate);
        }
        fromValue.getDate().setHours(0);
        fromValue.getDate().setMinutes(0);
        fromValue.getDate().setSeconds(0);

        toValue.getDate().setHours(23);
        toValue.getDate().setMinutes(59);
        toValue.getDate().setSeconds(59);

        if (isEnableCurrency) {
            initCurrencyBox();
            headerPanel.addGroupBoxItem(0, wfmStrings.currency(), currencyToolTip, currencyListBox);
        }

        initFilterPopup();
        WfmButton2 filterButton = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--filter");
        filterButton.removeHasiconLeftStyle();
        filterButton.addClickHandler(event -> filterDialog.open());
        GBoxItem fileteItem = headerPanel.addGroupBoxItem(null, filterButton);
        fileteItem.setStyleSplitRight(true);

        WfmButton2 updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(event -> initInternal());
        GBoxItem updateItem = headerPanel.addGroupBoxItem(null, updateButton);
        updateItem.setStyleSplitRight(true);
        updateItem.getComponent().getElement().addClassName("group-box__item-content--no-border");
        exportSection();

        pagingWidget = new PagingWidget();
        pagingWidget.setLimit(50);
        pagingWidget.setPaging(getPagingLoader());

        GBoxItem paging = headerPanel.addGroupBoxItem(null, pagingWidget);
        paging.addStyleToComponent("paging-group__wrapper");
        paging.setStyleSplitRight(true);
        paging.addStyleName("ml-auto");


        createTableHeader();

        sortDirection = DESC_STR;
        sortField = DATE_COLUMN;

        fireChanges();


        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BANK_ACCOUNT_TRANSACTION_DELETED, NewAccountTransaction.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MANUAL_TRANSACTION_DELETED, NewAccountTransaction.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, NewAccountTransaction.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_OPENNING_BALANCE_TRANSACTION_DELETE, NewAccountTransaction.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TRANSACTION_RECONCILED, NewAccountTransaction.this, (sender, args) -> initInternal());
    }

    private void initCurrencyBox() {
        currencyToolTip = new KpiCustomToolTip("");
        currencyToolTip.setVisible(false);
        currencyListBox = new DataListBox();
        currencyListBox.setWithoutNullLabel(true);
        currencyListBox.ensureDebugId("profitAndLoss-currencyListBox");
        currencyListBox.addValueChangeHandler(changeEvent -> onCurrencyChange());
        currencyListBox.setMaxWidth("8.46rem");

        AccountingService.App.get().getTrialBalanceFilterData(new AsyncCallback<TrialBalanceFilterData>() {
            @Override
            public void onFailure(Throwable caught) {
                onInitialize();
            }

            @Override
            public void onSuccess(TrialBalanceFilterData result) {
                currentDate = new Date();
                setCurrency(result.getCurrencies());
                if (currencyListBox.getSelectedId() == null && result.getBaseCurrency() != null) {
                    currencyListBox.setSelected(result.getBaseCurrency());
                }
            }
        });
    }

    private void onCurrencyChange() {
        currentDate = toValue.getDate();

        if (currencyListBox.getSelectedId() == null) {
            clearCurrencyRateHistory();
            return;
        }
        if (currencyListBox.getSelectedId().equals(baseCurrencyId)) {
            CurrencyItem item = (CurrencyItem) currencyListBox.getSelectedItem();
            clearCurrencyRateHistory();
        } else {
            CurrencyService.App.get().getCurrencyRateByDate(currencyListBox.getSelectedId(), new DateNonConvertable(currentDate), new AbstractAsyncCallback<CurrencyListItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    clearCurrencyRateHistory();
                }

                @Override
                public void onSuccess(CurrencyListItem result) {
                    String text = "1 " + result.getBaseCurrency().getName() + " = ";
                    text = text.concat(AccountingUtils.get().formatExRate(result.getExchangeRate()) + " ");
                    text = text.concat(result.getCurrency().getName());
                    text = text.concat(" (" + DateUtils.getDateAndTimeFormatShort1(currentDate) + ")");

                    currencyToolTip.setMessage(text);
                    currencyToolTip.setVisible(true);
                }
            });
        }
    }

    private void clearCurrencyRateHistory() {
        currencyToolTip.setMessage("");
        currencyToolTip.setVisible(false);
    }

    private void initInternal() {
        if (validate()) {
            pagingWidget.resetAndReload();
        }
    }

    private void createTableHeader() {
//        tableHead.setAttribute("point_affix_top_when", "134");

        Element tr = DOM.createTR();

        //Date column
        Element th = DOM.createTH();
        th.addClassName("stickerCell");

        Element div = DOM.createDiv();
        div.setInnerHTML(wfmStrings.date());
        div.addClassName("frame_affix_top");
        div.getStyle().setProperty("minWidth", "90px");
        th.appendChild(div);
        tr.appendChild(th);

        //Transaction Description column
        th = DOM.createTH();
        th.addClassName("stickerCell text-left");

        div = DOM.createDiv();
        div.addClassName("frame_affix_top");
        div.setInnerHTML(accountingStrings.transactionDescription());
        div.getStyle().setProperty("minWidth", "200px"); // Account transaction issue //https://prntscr.com/rf7sq5
        th.appendChild(div);
        tr.appendChild(th);

        //Number column
        th = DOM.createTH();
        th.addClassName("stickerCell text-left");

        div = DOM.createDiv();
        div.addClassName("frame_affix_top");
        div.setInnerHTML(wfmStrings.number());
        div.getStyle().setProperty("minWidth", "65px");
        th.appendChild(div);
        tr.appendChild(th);

        //Journal Id column
        th = DOM.createTH();
        th.addClassName("stickerCell text-left");

        div = DOM.createDiv();
        div.addClassName("frame_affix_top");
        div.setInnerHTML(accountingStrings.journalID());
        div.getStyle().setProperty("minWidth", "70px");
        th.appendChild(div);
        tr.appendChild(th);

        //Reference column
        th = DOM.createTH();
        th.addClassName("stickerCell text-left");

        div = DOM.createDiv();
        div.addClassName("frame_affix_top");
        div.setInnerHTML(wfmStrings.reference());
        div.getStyle().setProperty("minWidth", "150px");
        th.appendChild(div);
        tr.appendChild(th);

        //Debit column
        th = DOM.createTH();
        th.addClassName("stickerCell text-right");

        div = DOM.createDiv();
        div.addClassName("frame_affix_top");
        div.setInnerHTML(wfmStrings.debit());
        div.getStyle().setProperty("minWidth", "90px");
        th.appendChild(div);
        tr.appendChild(th);

        //Credit column
        th = DOM.createTH();
        th.addClassName("stickerCell text-right");

        div = DOM.createDiv();
        div.addClassName("frame_affix_top");
        div.setInnerHTML(wfmStrings.credit());
        div.getStyle().setProperty("minWidth", "90px");
        th.appendChild(div);
        tr.appendChild(th);

        //Balance column
        th = DOM.createTH();
        th.addClassName("stickerCell text-right");

        balanceDiv = DOM.createDiv();
        balanceDiv.addClassName("frame_affix_top");
        balanceDiv.getStyle().setProperty("minWidth", "90px");
        balanceDiv.setInnerHTML(wfmStrings.balance());
        th.appendChild(balanceDiv);
        tr.appendChild(th);

        tableHead.appendChild(tr);
    }

    private PagingWidget.Paging getPagingLoader() {
        return (start, limit) -> {
            noResultMessage.setVisible(false);

            if (accountsLookUp.getSelectedItem() == null && accountID == null) {
                tableBody.removeAllChildren();
                noResultMessage.setVisible(true);
                return;
            }
            LoadingPanel.loading(true);
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setAccountID(Optional.ofNullable(accountsLookUp.getSelectedItemID()).orElse(accountID));
            filter.setProjectId(projectLookUp.getSelectedItemID());
            filter.setStartDate(fromValue.getDate());
            filter.setEndDate(toValue.getDate());
            filter.setShowBudget(cashBasis.getValue());
            filter.setShowInBase(showInBase.getValue());
            filter.setShowActive(true);
            filter.setSearchKey(searchBox.getText());
            filter.setSortField(sortField);
            filter.setSortDir(ASC_STR.equals(sortDirection) ? 1 : 2);
            filter.setStart(start - 1);
            filter.setLimit(50);
            if (isEnableCurrency) {
                filter.setCurrencyID(currencyListBox.getSelectedId());
            }

            if (departmentLookUp != null) {
                filter.setDepartmentId(departmentLookUp.getSelectedItemID());
            }
            final DateNonConvertable fromDate = new DateNonConvertable(DateUtil.resetTime(fromValue.getDate()));
            final DateNonConvertable toDate = new DateNonConvertable(DateUtil.getDayLastTime(toValue.getDate()));

            AccountingService.App.get().findTransactionsByAccountAndJournalDate(filter, fromDate, toDate, new AbstractAsyncCallback<TransactionsBetweenDatesInAccount>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(TransactionsBetweenDatesInAccount balance) {
                    showInBase.setVisible(balance.isForeignAccount());
                    accountType = balance.getAccountType();
                    pagingWidget.setTotalCount(balance.getTotalCount());
                    updateTableHeader(tableHead, balance);
                    createGroup(balance);
                    createTotalRow(tableBody, wfmStrings.total(), balance);
                    createBalanceTR(tableBody, balance, false);
                    LoadingPanel.loading(false);
                    Utils.table__frame_affix_init();
                }
            });
        };
    }

    private void updateTableHeader(Element element, TransactionsBetweenDatesInAccount balance) {
        if (element != null) {
            balanceDiv.setInnerHTML(accountingMessages.balance("(" + balance.getCurrency().getName() + ")"));
        }
    }

    private void createTotalRow(Element element, String name, TransactionsBetweenDatesInAccount balance) {
        if (element == null) {
            return;
        }
        Element tr = DOM.createTR();
        tr.addClassName("total_row");
        element.appendChild(tr);

        Element td1 = DOM.createTD();
        td1.setInnerHTML(name);
        td1.setAttribute("colspan", "5");
        tr.appendChild(td1);

        Element td6 = DOM.createTD();
        td6.setInnerHTML(getValueAsString(balance.getTotalDebit()));
        td6.addClassName("text-right");
        tr.appendChild(td6);

        Element td7 = DOM.createTD();
        td7.setInnerHTML(getValueAsString(balance.getTotalCredit()));
        td7.addClassName("text-right");
        tr.appendChild(td7);

        Element td8 = DOM.createTD();
        td8.setInnerHTML(getValueAsString(balance.getTotalDebit().subtract(balance.getTotalCredit())));
        td8.addClassName("text-right");
        tr.appendChild(td8);
    }

    private String getValueAsString(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return AccountingUtils.get().formatPrice(value);
        }
        return "(" + AccountingUtils.get().formatPrice(value.abs()) + ")";
    }

    private void clearElementChild(Element element) {
        Element child;
        while ((child = element.getFirstChildElement()) != null) {
            element.removeChild(child);
        }
    }

    private void createBalanceTR(Element element, TransactionsBetweenDatesInAccount balance, boolean top) {
        Element tr = DOM.createTR();
        tr.addClassName("total_row");
        tr.addClassName("td_background_white");
        Element td = DOM.createTD();
        td.setAttribute("colspan", "5");
        tr.appendChild(td);

        Element td1 = DOM.createTD();
        tr.appendChild(td1);
        Element td2 = DOM.createTD();
        tr.appendChild(td2);

        Element td3 = DOM.createTD();
        td3.addClassName("text-right");
        tr.appendChild(td3);

        if (!EXPENSES.equals(accountType) && !REVENUE.equals(accountType)) {
            element.appendChild(tr);
            if (top) {
                td.setInnerHTML(wfmStrings.beginningBalance());
                td3.setInnerHTML(getValueAsString(balance.getTotalBeginningBalance()));
            } else {
                tr.addClassName("double");
                td.setInnerHTML(wfmStrings.endingBalance());
                td3.setInnerHTML(getValueAsString(balance.getTotalBalance()));
                // munir:  we need extra row after ending balance.
                tr = DOM.createTR();
                td = DOM.createTD();
                td.setAttribute("colspan", "8");
                tr.appendChild(td);
                element.appendChild(tr);
            }
        }
    }

    private void createGroup(TransactionsBetweenDatesInAccount balance) {
        List<Transaction> items = balance.getTransactions();
        clearElementChild(tableBody);
        createBalanceTR(tableBody, balance, true);
        BigDecimal beginning = balance.getTotalBeginningBalance() != null ? balance.getTotalBeginningBalance().add(balance.getBalanceStart()) : balance.getBalanceStart();
        String journalName;
        String number;
        for (final Transaction item : items) {
            EventListener eventListener = null;
            journalName = item.getJournalName();
            number = item.getNumber();
            if (INVOICE_TRANSACTION.equals(item.getTransactionType())) {
                if (item.getKeyId() != null) {
                    number = item.getNumber();
                    journalName = item.getJournalName();
                    if (RECEIVABLE.equals(item.getInvoiceOrPaymentType())) {
                        eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged((item.isCreditNote() ? "receivablecreditnote/" : "saleinvoice/") + item.getKeyId(), item.getNumber());

                    } else {
                        eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged((item.isCreditNote() ? "payablecreditnote/" : "purchaseinvoice/") + item.getKeyId(), item.getNumber());
                    }
                }
            } else if (GOODS_RECEIVED_TRANSACTION.equals(item.getTransactionType())) {
                number = item.getNumber();
                journalName = wfmStrings.purchaseorder() + ": " + item.getJournalName();
                if (item.getPurchaseOrderId() != null) {
                    eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|summary/" + item.getPurchaseOrderId(), item.getNumber());
                } else {
                    eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("grn|summary/" + item.getKeyId());
                }
            } else if (GOODS_DELIVERED_TRANSACTION.equals(item.getTransactionType())) {
                number = item.getNumber();
                journalName = wfmStrings.saleorder() + ": " + item.getJournalName();
                if (item.getSaleOrderId() != null) {
                    eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("saleorder|summary/" + item.getSaleOrderId(), item.getNumber());
                } else {
                    eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("gdn|summary/" + item.getKeyId());
                }
            } else if (INVOICEPAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                number = item.getInvoicePaymentNumber();
                journalName = item.getJournalName();
                if (item.getPaymentType() != null && RECEIVABLE_PREPAYMENT.equals(item.getPaymentType())) {
                    journalName = "Prepayment: " + journalName + " Client:" + item.getClientName();
                } else if (item.getPaymentType() != null && PAYABLE_SUPPLIER_CREDIT.equals(item.getPaymentType())) {
                    journalName = "Supplier Credit: " + journalName + " Supplier: " + item.getSupplierName();

                }
                eventListener = event -> {
                    if (item.isCreditNote()) {
                        if (item.getPaymentType() != null && RECEIVABLE_PREPAYMENT.equals(item.getPaymentType())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment|paymentView/" + item.getKeyId() + "/prepayment", item.getNumber());
                        } else if (item.getPaymentType() != null && PAYABLE_SUPPLIER_CREDIT.equals(item.getPaymentType())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment|paymentView/" + item.getKeyId() + "/supplierCredit", item.getNumber());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment|paymentView/" + item.getKeyId() + "/cashRefund", item.getNumber());
                        }
                    } else if (item.getBatchPaymentId() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|summary/" + item.getBatchPaymentId(), item.getNumber());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment|paymentView/" + item.getKeyId(), item.getNumber());
                    }
                };
            } else if (INVENTORY_TRANSACTION.equals(item.getTransactionType())) {
                number = item.getProductNumber();
                journalName = item.getJournalName();
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + item.getKeyId(), item.getNumber());
            } else if (MANUAL_TRANSACTION.equals(item.getTransactionType())) {
                number = item.getManualJournalNumber();
                if (item.getReversedJournalId() != null) {
                    journalName = wfmStrings.manualEntry() + " " + accountingStrings.reversed() + " " + wfmStrings.manualEntry();
                } else {
                    journalName = wfmStrings.manualEntry() + ": " + item.getNarration();
                }
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("manual|summary/" + item.getKeyId(), item.getNumber());
            } else if (CASH_ADVANCE_TRANSACTION.equals(item.getTransactionType())) {
                number = item.getCashAdvanceNumber();
                eventListener = event -> {
                    String cashAdvanceUrl = GWT.getHostPageBaseURL() + "Payroll.html#cashAdvance|summary/view/" + item.getKeyId() + "/" + item.getCashAdvanceStatus();
                    Window.open(cashAdvanceUrl, "_blank", "");
                };
            } else if (EXPENSE_TRANSACTION.equals(item.getTransactionType())) {
                number = item.getExpenseNumber();
                String description = !Utils.isNullOrEmpty(item.getDescription()) ? item.getDescription() : item.getExpenseTitle();
                journalName = Property.getPluralWithObjectCode(EXPENSES_CLAIM, wfmStrings.expenseClaims()) + ": " + description;
                eventListener = event -> {
                    if (item.getStatus() != null && EXPENSE_PAID.equals(item.getStatus())) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("expensepayment|summary/" + item.getKeyId(), item.getNumber());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|previewReport/" + item.getKeyId() + "/" + Constants.EXPENSE_VIEW + "/" + PermissionConstants.ACCOUNTING_CONTEXT, item.getNumber());
                    }
                };
            } else if (EXPENSEPAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                journalName = accountingStrings.expensePaymentTransaction();
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("expensepayment|summary/" + item.getKeyId(), item.getNumber());
            } else if (FIXED_ASSET_TRANSACTION.equals(item.getTransactionType())) {
                number = item.getFixedAssetNumber();
                journalName = wfmStrings.fixedAsset() + ": " + item.getFixedAssetName();
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("fixedasset|summary/" + item.getKeyId(), item.getNumber());
            } else if (DISPOSAL_TRANSACTION.equals(item.getTransactionType())) {
                number = item.getFixedAssetNumber();
                journalName = wfmStrings.fixedAsset() + ": " + item.getFixedAssetName();
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("fixedasset|summary/" + item.getKeyId(), item.getNumber());
            } else if (CUSTOMER_SUPPLIER_PAYMENT_TRANSACTION.equals(item.getTransactionType())) {
                journalName = item.getJournalName();
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("transactionItemView|summary/" + item.getTransactionId(), item.getNumber());
            } else if (BANK_CHECK_TRANSACTION.equals(item.getTransactionType())) {
                number = item.getCheckNumber();
                journalName = wfmStrings.check();
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("check|summary/" + item.getKeyId(), item.getNumber());
            } else if (BANK_TRANSFER_TRANSACTION.equals(item.getTransactionType())) {
                String type = "", name = "";
                if (RECEIVE_MONEY.equals(item.getSpendReceiveMoneyType())) {
                    type = RECEIVE_MONEY_STR;
                    name = accountingStrings.bankReceipts();
                } else if (SPEND_MONEY.equals(item.getSpendReceiveMoneyType())) {
                    type = SPEND_MONEY_STR;
                    name = accountingStrings.bankPayments();
                } else if (CASH_RECEIPT.equals(item.getSpendReceiveMoneyType())) {
                    type = CASH_RECEIPT_STR;
                    name = wfmStrings.cashReceipt();
                } else if (CASH_PAYMENT.equals(item.getSpendReceiveMoneyType())) {
                    type = CASH_PAYMENT_STR;
                    name = wfmStrings.cashPayment();
                }
                journalName = name + ": " + item.getSpendReceiveMoneyNarration();
                number = item.getSpendReceiveMoneyNumber();
                String finalType = type;
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|summary/" + item.getKeyId() + "/" + finalType, item.getSpendReceiveMoneyNumber());
            } else if (BANK_MONEY_TRANSFER_TRANSACTION.equals(item.getTransactionType())) {
                journalName = item.getJournalName();
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("transfer|summary/" + item.getKeyId() + "/" + VIEW_FORM);
            } else if (ADJUSTMENT_TRANSACTION.equals(item.getTransactionType())) {
                journalName = accountingStrings.stockAdjustment();
                number = item.getStockAdjustmentNumber();
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("stockadjustment|summary/" + item.getKeyId());
            } else if (STOCK_TRANSFER_TRANSACTION.equals(item.getTransactionType())) {
                journalName = accountingStrings.stockTransfer();
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("stocktransfer|summary/" + item.getKeyId());
            } else if (RETAINED_EARNINGS_TRANSACTION.equals(item.getTransactionType())) {
                journalName = item.getJournalName();
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|newprofitLoss/" + item.getKeyId() + "/accountTransaction" +
                        "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(item.getFromDate().getNonConvertedDate()) +
                        "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(item.getToDate().getNonConvertedDate()));
            }
            Element tr = DOM.createTR();

            Element tdDate = DOM.createTD();
            tdDate.setInnerHTML(DateUtils.format(item.getJournalDate()));
            tr.appendChild(tdDate);

            Element tdDescription = DOM.createTD();
            tr.appendChild(tdDescription);
            tdDescription.addClassName("text-left");
            journalName = journalName != null ? journalName : item.getJournalName();
            if (journalName != null) {
                tdDescription.setInnerHTML(journalName.length() > 40 ? journalName.substring(0, 40) + "..." : journalName);
                tdDescription.setTitle(journalName);
            }
            Element tdNumber = DOM.createTD();
            tr.appendChild(tdNumber);
            if (number != null && eventListener != null) {
                Element numberLink = DOM.createAnchor();
                numberLink.getStyle().setWhiteSpace(Style.WhiteSpace.PRE_LINE);
                DOM.sinkEvents(numberLink.cast(), Event.ONCLICK);
                DOM.setEventListener(numberLink.cast(), eventListener);
                numberLink.setTitle(number);
                numberLink.setInnerHTML(number.length() > 25 ? number.substring(0, 25) + "..." : number);
                tdNumber.appendChild(numberLink);
            }

            Element tdJournalId = DOM.createTD();
            tr.appendChild(tdJournalId);
            Element journalLink = DOM.createAnchor();
            DOM.sinkEvents(journalLink.cast(), Event.ONCLICK);
            EventListener journalEventListener = event ->
                    SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|journalReport/" + item.getJournalId());
            DOM.setEventListener(journalLink.cast(), journalEventListener);

            if (item.getJournalId() != null) {
                journalLink.setInnerHTML(String.valueOf(item.getJournalId()));
                journalLink.setTitle(String.valueOf(item.getJournalId()));
            }
            tdJournalId.appendChild(journalLink);

            Element tdReference = DOM.createTD();
            tr.appendChild(tdReference);
            String reference = "";
            if (MANUAL_TRANSACTION.equals(item.getTransactionType())) {
                reference = !Utils.isNullOrEmpty(item.getDescription()) ? item.getDescription() : "";
            } else {
                reference = !Utils.isNullOrEmpty(item.getReference()) ? item.getReference() : "";
            }
            if (!Utils.isNullOrEmpty(reference)) {
                Element link = DOM.createAnchor();
                DOM.sinkEvents(link.cast(), Event.ONCLICK);
                if (eventListener != null)
                    DOM.setEventListener(link.cast(), eventListener);
                link.setInnerHTML(reference.length() > 23 ? reference.substring(0, 23) + " ..." : reference);
                link.setTitle(reference);
                tdReference.appendChild(link);
            }

            Element tdDebit = DOM.createTD();
            tr.appendChild(tdDebit);
            tdDebit.setInnerHTML(item.getTotalDebit() != null ? getValueAsString(item.getTotalDebit()) : "");
            tdDebit.addClassName("text-right");

            Element tdCredit = DOM.createTD();
            tr.appendChild(tdCredit);
            tdCredit.setInnerHTML(item.getTotalCredit() != null ? getValueAsString(item.getTotalCredit()) : "");
            tdCredit.addClassName("text-right");

            Element tdBalance = DOM.createTD();
            tdBalance.addClassName("text-right");
            tr.appendChild(tdBalance);
            BigDecimal totalDebit = item.getTotalDebit() != null ? item.getTotalDebit() : BigDecimal.ZERO;
            BigDecimal totalCredit = item.getTotalCredit() != null ? item.getTotalCredit() : BigDecimal.ZERO;

            if (Constants.ASSETS.equals(accountType) || EXPENSES.equals(accountType)) {
                beginning = beginning.add(totalDebit.subtract(totalCredit));
            } else {
                beginning = beginning.add(totalCredit.subtract(totalDebit));
            }
            tdBalance.setInnerHTML(getValueAsString(beginning));
            tableBody.appendChild(tr);
        }
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateDate(fromValue)) {
            errors++;
        }
        if (!Validation.validateDate(toValue)) {
            errors++;
        }
        if (!Validation.validateDateOrder(fromValue, toValue)) {
            errors++;
        }
        return errors == 0;
    }

    private void fireChanges() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_OPENNING_BALANCE_TRANSACTION_DELETE, NewAccountTransaction.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PREPAYMENT_SAVE, NewAccountTransaction.this, (sender, args) -> initInternal());
    }

    private void initFilterPopup() {
        filterDialog = new KpiModal();
        filterDialog.setWidth(400);
        filterDialog.setCloseButton(true);
        filterDialog.setDismissible(false);

        MaterialPanel contentPanel = new MaterialPanel();

        searchBox = new TextBox();
        searchBox.ensureDebugId("accountTransactions-searchBox");
        contentPanel.add(new FormGroup(wfmStrings.search(), searchBox));

        if (isDepartmentRelationEnabled) {
            departmentLookUp = new DepartmentLookUp();
            contentPanel.add(new FormGroup(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), departmentLookUp));
        }
        projectLookUp = new ProjectLookUp(null);
        contentPanel.add(new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp));

        showInBase = new KpiCheckBox(accountingStrings.showInBase());
        contentPanel.add(new FormGroup(showInBase));
        showInBase.setValue(Boolean.FALSE);

        cashBasis = new KpiCheckBox(accountingStrings.cashBase());
        contentPanel.add(new FormGroup(cashBasis));

        WfmButton2 resetButton = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_DEFAULT);
        resetButton.addClickHandler(clickEvent -> {
            searchBox.setText("");

            if (departmentLookUp != null) {
                departmentLookUp.clear();
            }
            projectLookUp.clear();
            showInBase.setValue(Boolean.TRUE);
            cashBasis.setValue(Boolean.FALSE);
        });

        WfmButton2 applyFilterButton = new WfmButton2(wfmStrings.apply(), WfmButton2.BTN_SUCCESS);
        applyFilterButton.addClickHandler(clickEvent -> {
            filterDialog.close();
            initInternal();
        });

        filterDialog.add(contentPanel);
        filterDialog.addButton(resetButton);
        filterDialog.addButton(applyFilterButton);
    }

    private void exportSection() {
        MaterialMenuBar showMenuBar = new MaterialMenuBar();
        showMenuBar.setClass("dropdown-kit--arrow--below");

        MaterialLink showLink = new MaterialLink();
        showLink.addStyleName("btn btn--white btn--icon");

        Icon ieIcon = new Icon();//import/export icon for listing top panel
        ieIcon.setClass("ficon--download-cloud");
        showLink.add(ieIcon);

        MaterialDropDown showMenuContainer = new MaterialDropDown(showLink);
        showMenuContainer.setClass("dropdown-content--2 dropdown-content--export");
        showMenuContainer.setBelowOrigin(true);
        showLink.add(showMenuContainer);

        pdfVersion = getPdfVersion();
        pdfVersion.ensureDebugId("pdf_button");

        Div wrapper = new Div("java-wrap");
        showMenuContainer.add(wrapper);

        MaterialLink pdfVersion = getPdfVersion();
        wrapper.add(pdfVersion);

        MaterialDropDown mdp = new MaterialDropDown(pdfVersion);
        mdp.setHover(true);
        mdp.setHoverable(true);

        mdp.add(NewAccountTransaction.this::getPortraitLink);
        mdp.add(NewAccountTransaction.this::getLandscapeLink);

        wrapper.add(mdp);

        setPDFListener();

        MaterialLink exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.excel());
        exportExl.addClickHandler(ch -> {
            String URL = (CommandConstants.COMMON_URL + "/transactionByPeriodExcelHandler");
            if (accountsLookUp.getSelectedItem() != null || (accountID != null && accountID > 0)) {
                final ListingFilterParameter filter = new ListingFilterParameter();
                filter.setPropertyCode("transactionsByPeriod");
                filter.setAccountID(accountsLookUp.getSelectedItem() != null ? accountsLookUp.getSelectedItem().getId() : accountID);
                filter.setProjectId(projectLookUp.getSelectedItemID());
                filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
                filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
                filter.setShowBudget(cashBasis.getValue());
                filter.setShowActive(true);
                filter.setSearchKey(searchBox.getText());
                filter.setName(accountsLookUp.getSelectedItem() != null ? accountsLookUp.getSelectedItem().getName() : "");
                if (departmentLookUp != null) {
                    filter.setDepartmentId(departmentLookUp.getSelectedItemID());
                }
                filter.setSortField(sortField);
                filter.setSortDir(ASC_STR.equals(sortDirection) ? 1 : 2);
                filter.setShowInBase(showInBase.getValue());
                if (isEnableCurrency) {
                    filter.setCurrencyID(currencyListBox.getSelectedId());
                }

                Utils.sendPDFOrExcelRequest(exportPanel, URL, filter.getRequestParams(), "_blank");
            } else {
                Info.warn(wfmStrings.pleaseSelectAccount());
            }
        });
        showMenuContainer.add(exportExl);

        showMenuBar.add(showLink);
        Div div = new Div();
        new KpiToolTip(showMenuBar, wfmStrings.export(), Position.TOP);
        div.add(showMenuBar);
        GBoxItem exportItem = headerPanel.addGroupBoxItem(0, null, div);
        //exportItem.addStyleName("ml-auto");
    }

    public MaterialLink getPdfVersion() {

        if (pdfVersion == null) {
            pdfVersion = new MaterialLink();
            MaterialIcon pdfIcon = new MaterialIcon();
            pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
            pdfVersion.add(pdfIcon);
            pdfVersion.setText(wfmStrings.pdf());
        }
        return pdfVersion;
    }

    private MaterialLink getPortraitLink() {
        if (portrait == null) {
            portrait = new MaterialLink();
            portrait.setText(wfmStrings.portrait());
        }
        return portrait;
    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
            landscape.setText(wfmStrings.landscape());
        }
        return landscape;
    }

    public void setPDFListener() {
        getPortraitLink().addClickHandler((event) -> {
            sendPdfRequest(false);
        });
        getLandscapeLink().addClickHandler((event) -> {
            sendPdfRequest(true);
        });
    }

    private void sendPdfRequest(boolean landscape) {
        String URL = (CommandConstants.PDF_URL + "/transactionByPeriodPdfHandler");
        if (accountsLookUp.getSelectedItem() != null || (accountID != null && accountID > 0)) {
            final ListingFilterParameter filter = new ListingFilterParameter();
            filter.setLandscape(landscape);
            filter.setPropertyCode("transactionsByPeriod");
            filter.setAccountID(accountsLookUp.getSelectedItem() != null ? accountsLookUp.getSelectedItem().getId() : accountID);
            filter.setProjectId(projectLookUp.getSelectedItemID());
            filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
            filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
            filter.setShowBudget(cashBasis.getValue());
            filter.setShowActive(true);
            filter.setSearchKey(searchBox.getText());
            filter.setName(accountsLookUp.getSelectedItem() != null ? accountsLookUp.getSelectedItem().getName() : "");
            if (departmentLookUp != null) {
                filter.setDepartmentId(departmentLookUp.getSelectedItemID());
            }
            filter.setSortField(sortField);
            filter.setSortDir(ASC_STR.equals(sortDirection) ? 1 : 2);
            filter.setShowInBase(showInBase.getValue());
            if (isEnableCurrency) {
                filter.setCurrencyID(currencyListBox.getSelectedId());
            }

            Utils.sendPDFOrExcelRequest(exportPanel, URL, filter.getRequestParams(), "_blank");
        } else {
            Info.warn(wfmStrings.pleaseSelectAccount());
        }
    }
}
