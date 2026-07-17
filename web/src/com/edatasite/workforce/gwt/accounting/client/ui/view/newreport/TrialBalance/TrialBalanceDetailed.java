package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.TrialBalance;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalance;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.TrialBalanceFilter;
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
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCustomToolTip;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxDatePeriodItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.ALL_ACCOUNTS;

public class TrialBalanceDetailed extends Composite implements Constants {
    interface TrialBalanceDetailedUiBinder extends UiBinder<HTMLPanel, TrialBalanceDetailed> {
    }

    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    public static final boolean isDepartmentRelationEnabled = AccountingUtils.get().isEnableAccountingDepartmentRelation();

    private static final TrialBalanceDetailed.TrialBalanceDetailedUiBinder ourUiBinder = GWT.create(TrialBalanceDetailed.TrialBalanceDetailedUiBinder.class);
    private final String BEGINNING_BALANCE = "BEGINNING_BALANCE";
    private final String DEBIT = "DEBIT";
    private final String CREDIT = "CREDIT";

    private Date financialYearStart;
    private Date currentDate;

    private Integer baseCurrencyId;
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;
    private final String ENDING_BALANCE = "ENDING_BALANCE";

    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    DivElement accountCodeText;
    @UiField
    DivElement accountNameText;
//    @UiField
    DivElement beginningBalanceText;
    @UiField
    DivElement beginningDebitText;
    @UiField
    DivElement beginningCreditText;
    @UiField
    DivElement debitText;
    @UiField
    DivElement creditText;
//    @UiField
    DivElement endingBalanceText;
    @UiField
    DivElement endingDebitText;
    @UiField
    DivElement endingCreditText;
    @UiField
    Element revenueGroup;
    @UiField
    Element expensesGroup;
    @UiField
    Element assetsGroup;
    @UiField
    Element liabilitiesGroup;
    @UiField
    Element equityGroup;
    @UiField
    HTMLPanel noResultText;
    @UiField
    HTMLPanel exportExcelPanel;
    @UiField
    TableSectionElement header;
    @UiField
    Element totalField;

    private KpiModal filterDialog;
    private DataListBox showValues;
    private KpiCheckBox consolidation;
    private final DatePicker fromValue;
    private final DatePicker toValue;
    private final DataListBox currencyListBox;
    private DepartmentLookUp departmentLookUp;
    private final DataListBox sortTypeListBox;
    private final DataListBox sortByListBox;
    private final KpiCustomToolTip currencyToolTip;
    private StringBuilder exchangeRateInfoText;
    private StringBuilder beginningPeriodRateText;
    private StringBuilder currentPeriodRateText;
    private StringBuilder toolTipFullText;


    public TrialBalanceDetailed() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);

        GBox groupBox = headerPanel.drawNewGroupBox();
        groupBox.setStyleUnited(true);
        groupBox.setStyleWidthFree(true);

        //it is for fixed header
        header.getStyle().setDisplay(Style.Display.NONE);

        //Wide View
        LoadingPanel.loading(true);

        GBoxDatePeriodItem datePeriodItem = new GBoxDatePeriodItem();
        fromValue = new DatePicker();
        fromValue.ensureDebugId("trialBalance-fromValue-datePicker");
        fromValue.addChangeHandler(changeEvent -> {
            onCurrencyChange();
        });
        datePeriodItem.setStartBoxItem(wfmStrings.from(), fromValue);
        toValue = new DatePicker();
        toValue.ensureDebugId("trialBalance-toValue-datePicker");
        toValue.addChangeHandler(changeEvent -> {
            onCurrencyChange();
        });
        datePeriodItem.setDueBoxItem(wfmStrings.to(), toValue);
        headerPanel.addGroupBoxItem(datePeriodItem);

        //Currency Tooltip
        currencyToolTip = new KpiCustomToolTip("");
        currencyToolTip.setVisible(false);

        currencyListBox = new DataListBox();
        currencyListBox.setMaxWidth("8.46rem");
        currencyListBox.setWithoutNullLabel(true);
        currencyListBox.addValueChangeHandler(changeEvent -> onCurrencyChange());
        currencyListBox.ensureDebugId("trialBalance-currencyListBox");
        headerPanel.addGroupBoxItem(0, wfmStrings.currency(), currencyToolTip, currencyListBox);

        //Filter
        initFilterPopup();

        WfmButton2 filterButton = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--filter");
        filterButton.removeHasiconLeftStyle();
        filterButton.addClickHandler(event -> filterDialog.open());
        GBoxItem filterItem = headerPanel.addGroupBoxItem(null, filterButton);
        filterItem.setStyleSplitRight(true);
        filterItem.setStyleWidthFree(true);

        //Sorting
        sortByListBox = new DataListBox();
        sortByListBox.setWithoutNullLabel(true);
        sortByListBox.setMaxWidth("9.46rem");
        sortByListBox.setItems(getSortByItems());
        sortByListBox.setSelected(getSortByItems()[0]);
        sortByListBox.addValueChangeHandler(event -> initInternal());


        sortTypeListBox = new DataListBox();
        sortTypeListBox.setWithoutNullLabel(true);
        sortTypeListBox.setMaxWidth("5.46rem");
        sortTypeListBox.setItems(getSortItems());
        sortTypeListBox.setSelected(getSortItems()[0]);
        sortTypeListBox.addValueChangeHandler(event -> initInternal());

        headerPanel.addGroupBoxItem(wfmStrings.sortBy(), sortByListBox);

        GBoxItem sortItem = headerPanel.addGroupBoxItem("", sortTypeListBox);
        sortItem.setStyleSplitRight(true);
        sortItem.setStyleWidthFree(true);

        exchangeRateInfoText = new StringBuilder();
        beginningPeriodRateText = new StringBuilder();
        currentPeriodRateText = new StringBuilder();
        toolTipFullText = new StringBuilder();

        //Update
        WfmButton2 updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(event -> initInternal());
        GBoxItem updateItem = headerPanel.addGroupBoxItem(null, updateButton);
        updateItem.setStyleSplitRight(true);
        updateItem.setStyleWidthFree(true);
        updateItem.getComponent().getElement().addClassName("group-box__item-content--no-border");

        exportSection();

        accountNameText.setInnerHTML(wfmStrings.account());
//        beginningBalanceText.setInnerHTML(accountingStrings.beginningBalance());
        beginningDebitText.setInnerHTML(accountingStrings.beginningDebit());
        beginningCreditText.setInnerHTML(accountingStrings.beginningCredit());
        debitText.setInnerHTML(wfmStrings.debit());
        creditText.setInnerHTML(wfmStrings.credit());
//        endingBalanceText.setInnerHTML(accountingStrings.endingBalance());
        endingDebitText.setInnerHTML(accountingStrings.endingDebit());
        endingCreditText.setInnerHTML(accountingStrings.endingCredit());

        noResultText.getElement().getStyle().setDisplay(Style.Display.NONE);

        AccountingService.App.get().getTrialBalanceFilterData(new AsyncCallback<TrialBalanceFilterData>() {
            @Override
            public void onFailure(Throwable caught) {
                initInternal();
            }

            @Override
            public void onSuccess(TrialBalanceFilterData result) {
                financialYearStart = DateUtil.addDays(result.getFinancialYearEnd().getNonConvertedDate(), 1);
                currentDate = new Date();
                financialYearStart.setYear(currentDate.getYear());

                while (financialYearStart.after(currentDate)) {
                    financialYearStart.setYear(financialYearStart.getYear() - 1);
                }

                fromValue.setDate(financialYearStart);
                toValue.setDate(currentDate);

                setCurrency(result.getCurrencies());
                if (currencyListBox.getSelectedId() == null && result.getBaseCurrency() != null) {
                    currencyListBox.setSelected(result.getBaseCurrency());
                }
//                endingBalanceText.setInnerHTML(accountingMessages.endingBalance(currencyListBox.getSelectedItem(true).getName()));
                initInternal();
            }
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICE_VOID, TrialBalanceDetailed.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MONEY_TRANSFER, TrialBalanceDetailed.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, TrialBalanceDetailed.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, TrialBalanceDetailed.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, TrialBalanceDetailed.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FIXED_ASSET_SAVED, TrialBalanceDetailed.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, TrialBalanceDetailed.this, (sender, args) -> initInternal());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EXCHANGE_RATE_ADDED, TrialBalanceDetailed.this, (sender, args) -> onCurrencyChange());
    }

    private void initInternal() {
        if (!validate()) {
            return;
        }
        Integer departmentID = null;

        if (isDepartmentRelationEnabled) {
            departmentID = departmentLookUp.getSelectedItem() != null ? departmentLookUp.getSelectedItem().getId() : null;
        }

        LoadingPanel.loading(true);
        TrialBalanceFilter tbf = new TrialBalanceFilter();
        tbf.setStartDate(Utils.getStartDateNC(fromValue.getDate()));
        tbf.setToDate(Utils.getEndDateNC(toValue.getDate()));
        tbf.setSortField(sortByListBox.getSelectedItem().getDescription());
        tbf.setSortDirection(sortTypeListBox.getSelectedItem().getDescription());
        tbf.setConsolidation(consolidation.getValue());
        tbf.setShowValues(showValues.getSelectedId());
        tbf.setDepartmentID(departmentID);
        tbf.setCurrencyId(currencyListBox.getSelectedId());

        noResultText.getElement().getStyle().setDisplay(Style.Display.NONE);

        AccountingService.App.get().getTrialBalance(tbf, new AbstractAsyncCallback<TrialBalance>() {
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                    }

                    public void success(TrialBalance balance) {
                        collectTreeData(assetsGroup, balance.getAssets(), wfmStrings.assets(), true);
                        collectTreeData(liabilitiesGroup, balance.getLiabilities(), wfmStrings.liabilities(), true);
                        collectTreeData(equityGroup, balance.getEquity(), wfmStrings.equities(), true);
                        collectTreeData(revenueGroup, balance.getRevenue(), wfmStrings.revenue(), false);
                        collectTreeData(expensesGroup, balance.getExpenses(), wfmStrings.expenses(), false);
                        createTotalRow(totalField, balance);

                        if (balance.getRevenue().length == 0
                                && balance.getExpenses().length == 0
                                && balance.getAssets().length == 0
                                && balance.getLiabilities().length == 0
                                && balance.getEquity().length == 0) {
                            noResultText.getElement().setInnerHTML(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
                            noResultText.getElement().getStyle().setDisplay(Style.Display.BLOCK);
                            clearCurrencyRateHistory();
                        }
//                        endingBalanceText.setInnerText(accountingMessages.endingBalance(currencyListBox.getSelectedItem() != null ? "(" + currencyListBox.getSelectedItem().getName() + ")" : accountingStrings.endingBalance()));
                        header.getStyle().setDisplay(Style.Display.BLOCK);
                        Utils.table__frame_affix_init();
                        LoadingPanel.loading(false);
                    }
                }
        );
    }

    private boolean validate() {
        int errors = 0;
        fromValue.removeStyleName(ERROR_FORM_STYLE);
        toValue.removeStyleName(ERROR_FORM_STYLE);

        if (!Validation.validateDate(fromValue)) {
            errors++;
        }
        if (!Validation.validateDate(toValue)) {
            errors++;
        }

        if (!Validation.validateDateOrder(fromValue, toValue)) {
            return false;
        }

        return errors == 0;
    }

    private void setCurrency(CurrencyItem[] currencies) {
        currencyListBox.setItems(currencies);
        for (CurrencyItem currency : currencies) {
            if (currency.isCompanyCurrency()) {
                baseCurrencyId = currency.getId();
                currencyListBox.setSelected(currency.getId());
            }
        }
    }

    private void onCurrencyChange() {
        currentDate = fromValue.getDate();
        if (currencyListBox.getSelectedId().equals(baseCurrencyId)) {
            clearCurrencyRateHistory();
        } else {
            currencyToolTip.setVisible(true);
            exchangeRateInfoText = new StringBuilder(accountingMessages.figuresConvertedIntoCurrency(currencyListBox.getSelectedItemText()));

            //load exchange rate by beginning balance date{from date - 1 day}
            Date beginingBalanceDate = DateUtil.addDays(fromValue.getDate(), -1);
            CurrencyService.App.get().getCurrencyRateByDate(currencyListBox.getSelectedId(), new DateNonConvertable(beginingBalanceDate), new AbstractAsyncCallback<CurrencyListItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    clearCurrencyRateHistory();
                }

                @Override
                public void onSuccess(CurrencyListItem result) {
                    String text = "1 " + result.getBaseCurrency().getName() + " = ";
                    text = text.concat(AccountingUtils.get().formatExRate(result.getExchangeRate()) + " ");
                    text = text.concat(result.getCurrency().getName());
                    beginningPeriodRateText = new StringBuilder(accountingMessages.beginningPeriodRate(text, DateUtils.format(beginingBalanceDate)));
                }
            });


            //load exchange rate by to date
            Date cbDate = toValue.getDate();
            if (cbDate.compareTo(new Date()) > 0) {
                cbDate = new Date();
            }
            Date currentBalanceDate = cbDate;
            CurrencyService.App.get().getCurrencyRateByDate(currencyListBox.getSelectedId(), new DateNonConvertable(currentBalanceDate), new AbstractAsyncCallback<CurrencyListItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    clearCurrencyRateHistory();
                }

                @Override
                public void onSuccess(CurrencyListItem result) {
                    String text = "1 " + result.getBaseCurrency().getName() + " = ";
                    text = text.concat(AccountingUtils.get().formatExRate(result.getExchangeRate()) + " ");
                    text = text.concat(result.getCurrency().getName());

                    currentPeriodRateText = new StringBuilder(accountingMessages.currentPeriodRate(text, DateUtils.format(currentBalanceDate)));

                    toolTipFullText = new StringBuilder();

                    toolTipFullText.append(exchangeRateInfoText);
                    toolTipFullText.append("</br></br>");

                    toolTipFullText.append(beginningPeriodRateText);
                    toolTipFullText.append("</br></br>");

                    toolTipFullText.append(currentPeriodRateText);

                    currencyToolTip.setMessage(toolTipFullText.toString());
                }
            });
        }
    }

    private void collectTreeData(Element element, TrialBalanceItem[] items, String groupName, boolean showBalances) {
        element.addClassName("expanded");
        TrialBalanceItem itemsTotal = new TrialBalanceItem();
        itemsTotal.setBeginningDebit(BigDecimal.ZERO);
        itemsTotal.setBeginningCredit(BigDecimal.ZERO);
        itemsTotal.setDebit(BigDecimal.ZERO);
        itemsTotal.setCredit(BigDecimal.ZERO);
        itemsTotal.setEndingDebit(BigDecimal.ZERO);
        itemsTotal.setEndingCredit(BigDecimal.ZERO);
        clearElementChild(element);
        createGroupHeader(element, groupName);
        if (items != null && items.length > 0) {
            Map<String, TrialBalanceItem> map1 = new HashMap<>(items.length);
            ArrayList<TrialBalanceItem> map2 = new ArrayList<>();
            Arrays.stream(items).forEach(acc -> map1.put(acc.getCode(), acc));
            Arrays.stream(items).forEach(acc -> {
                itemsTotal.setBeginningDebit(itemsTotal.getBeginningDebit().add(acc.getBeginningDebit()));
                itemsTotal.setBeginningCredit(itemsTotal.getBeginningCredit().add(acc.getBeginningCredit()));
                itemsTotal.setDebit(itemsTotal.getDebit().add(acc.getDebit()));
                itemsTotal.setCredit(itemsTotal.getCredit().add(acc.getCredit()));
                itemsTotal.setEndingDebit(itemsTotal.getEndingDebit().add(acc.getEndingDebit()));
                itemsTotal.setEndingCredit((itemsTotal.getEndingCredit().add(acc.getEndingCredit())));
                if (acc.getParentCode() != null) {
                    if (map1.get(acc.getParentCode()) == null) {
                        AccountItem accountCodeUnique = new AccountItem(acc.getParentId(), acc.getParentCode(), acc.getParentName());
                        TrialBalanceItem balanceItem = new TrialBalanceItem(accountCodeUnique.getId(), accountCodeUnique.getCode(), accountCodeUnique.getName(),
                                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                        balanceItem.getChilds().add(acc);
                        map1.put(acc.getParentCode(), balanceItem);
                        map2.add(balanceItem);
                    } else {
                        map1.get(acc.getParentCode()).getChilds().add(acc);
                    }
                } else {
                    map2.add(acc);
                }
            });

            map2.forEach(key -> {
                TrialBalanceItem childTotal = new TrialBalanceItem();
                childTotal.setBeginningDebit(BigDecimal.ZERO);
                childTotal.setBeginningCredit(BigDecimal.ZERO);
                childTotal.setDebit(BigDecimal.ZERO);
                childTotal.setCredit(BigDecimal.ZERO);
                childTotal.setEndingBalance(BigDecimal.ZERO);
                createGroup1(element, key, childTotal, showBalances);
            });
        }
        createGroupTotalRow(element, wfmStrings.total() + " " + groupName, itemsTotal, showBalances);
    }

    private void createGroupTotalRow(Element element, String groupName, TrialBalanceItem itemsTotal, boolean showBalances) {
        if (element != null) {
            Element tr = DOM.createTR();
            tr.addClassName("total_row");
            element.appendChild(tr);

            Element nameTd = DOM.createTD();
            nameTd.setInnerHTML(groupName);
            nameTd.setAttribute("style", "border-right: none;");
            tr.appendChild(nameTd);

            Element emptyTd = DOM.createTD();
            tr.appendChild(emptyTd);

            Element beginDebitTd = DOM.createTD();
            tr.appendChild(beginDebitTd);
            Element beginCreditTd = DOM.createTD();
            tr.appendChild(beginCreditTd);
            Element debitTd = DOM.createTD();
            tr.appendChild(debitTd);
            Element creditTd = DOM.createTD();
            tr.appendChild(creditTd);
            Element endDebitTd = DOM.createTD();
            tr.appendChild(endDebitTd);
            Element endCreditTd = DOM.createTD();
            tr.appendChild(endCreditTd);

            if (showBalances && itemsTotal.getBeginningDebit() != null) {
                beginDebitTd.setInnerHTML(getValueAsString(itemsTotal.getBeginningDebit()));
                beginDebitTd.addClassName(RIGHT_ALIGN_CELL);
            }
            if (showBalances && itemsTotal.getBeginningCredit() != null) {
                beginCreditTd.setInnerHTML(getValueAsString(itemsTotal.getBeginningCredit()));
                beginCreditTd.addClassName(RIGHT_ALIGN_CELL);
            }
            if (itemsTotal.getDebit() != null) {
                debitTd.setInnerHTML(getValueAsString(itemsTotal.getDebit()));
                debitTd.addClassName(RIGHT_ALIGN_CELL);
            }
            if (itemsTotal.getCredit() != null) {
                creditTd.setInnerHTML(getValueAsString(itemsTotal.getCredit()));
                creditTd.addClassName(RIGHT_ALIGN_CELL);
            }
            if (showBalances && itemsTotal.getEndingDebit() != null) {
                endDebitTd.setInnerHTML(getValueAsString(itemsTotal.getEndingDebit()));
                endDebitTd.addClassName(RIGHT_ALIGN_CELL);
            }
            if (showBalances && itemsTotal.getEndingCredit() != null) {
                endCreditTd.setInnerHTML(getValueAsString(itemsTotal.getEndingCredit()));
                endCreditTd.addClassName(RIGHT_ALIGN_CELL);
            }
            element.appendChild(tr);
        }
    }

    private void createGroup1(Element parentElement, TrialBalanceItem accItem1, TrialBalanceItem childTotal, boolean showBalances) {
        Element element = create(parentElement, accItem1, !accItem1.getChilds().isEmpty(), showBalances);
        if (!accItem1.getChilds().isEmpty()) {
            for (TrialBalanceItem child : accItem1.getChilds()) {
                createGroup1(element, child, childTotal, showBalances);

                if (!child.isCalculated()){
                    childTotal.setBeginningDebit(childTotal.getBeginningDebit().add(child.getBeginningDebit()));
                    childTotal.setBeginningCredit(childTotal.getBeginningCredit().add(child.getBeginningCredit()));
                    childTotal.setDebit(childTotal.getDebit().add(child.getDebit()));
                    childTotal.setCredit(childTotal.getCredit().add(child.getCredit()));
                    childTotal.setEndingDebit(childTotal.getEndingDebit().add(child.getEndingDebit()));
                    childTotal.setEndingCredit(childTotal.getEndingCredit().add(child.getEndingCredit()));
                    child.setCalculated(true);
                }
            }

            if (!accItem1.isCalculated()) {
                childTotal.setBeginningDebit(childTotal.getBeginningDebit().add(accItem1.getBeginningDebit() != null ? accItem1.getBeginningDebit() : BigDecimal.ZERO));
                childTotal.setBeginningCredit(childTotal.getBeginningCredit().add(accItem1.getBeginningCredit() != null ? accItem1.getBeginningCredit() : BigDecimal.ZERO));
                childTotal.setDebit(childTotal.getDebit().add(accItem1.getDebit() != null ? accItem1.getDebit() : BigDecimal.ZERO));
                childTotal.setCredit(childTotal.getCredit().add(accItem1.getCredit() != null ? accItem1.getCredit() : BigDecimal.ZERO));
                childTotal.setEndingDebit(childTotal.getEndingDebit().add(accItem1.getEndingDebit() != null ? accItem1.getEndingDebit() : BigDecimal.ZERO));
                childTotal.setEndingCredit(childTotal.getEndingCredit().add(accItem1.getEndingCredit() != null ? accItem1.getEndingCredit() : BigDecimal.ZERO));
                accItem1.setCalculated(true);
            }
            if (accItem1.getName() != null) {
                createGroupTotalRow(element, wfmStrings.total() + " " + accItem1.getName(), childTotal, showBalances);
            }
        }
    }

    private Element create(Element element, TrialBalanceItem accItem1, boolean hasChilds, boolean showBalances) {

        if (accItem1.getName() != null && accItem1.getCode() != null) {
            if (hasChilds) {
                Element mainTr = DOM.createTR();
                Element mainTd = DOM.createTD();
                mainTr.appendChild(mainTd);

                Element childTable = DOM.createTable();
                childTable.setClassName("table table_report");

                Element header = createTH();
                header.setAttribute("style", "display: none;");

                Element childTBody = DOM.createTBody();
                childTBody.setClassName("category_set");
                childTBody.addClassName("collapsed");

                childTable.appendChild(header);
                childTable.appendChild(childTBody);

                Element childTr = DOM.createTR();
                childTr.addClassName("heading_row");
                Element childTd = DOM.createTD();
                childTr.appendChild(childTd);

                Element icon = DOM.createElement("i");
                icon.addClassName("btn--circle plusMinus");
                DOM.sinkEvents(icon.cast(), Event.ONCLICK);
                DOM.setEventListener(icon.cast(), event -> {
                    if (childTBody.getClassName().contains("collapsed")) {
                        childTBody.removeClassName("collapsed");
                        childTBody.addClassName("expanded");
                    } else {
                        childTBody.removeClassName("expanded");
                        childTBody.addClassName("collapsed");
                    }
                });

                mainTd.addClassName("second_level");
                mainTd.setAttribute("colspan", "5");
                mainTd.appendChild(childTable);
                element.appendChild(mainTr);

                Element codeTd = DOM.createTD();
                codeTd.appendChild(icon);
                codeTd.addClassName(LEFT_ALIGN_CELL);
                Span codeSpan = new Span(accItem1.getCode());
                codeTd.appendChild(codeSpan.getElement());
                childTr.appendChild(codeTd);

                Element nameTd = DOM.createTD();
                nameTd.addClassName(LEFT_ALIGN_CELL);
                Span nameSpan = new Span(accItem1.getName());
                nameTd.appendChild(nameSpan.getElement());
                childTr.appendChild(nameTd);

                Element beginDebitTd = DOM.createTD();
                childTr.appendChild(beginDebitTd);
                Element beginCreditTd = DOM.createTD();
                childTr.appendChild(beginCreditTd);
                Element debitTd = DOM.createTD();
                childTr.appendChild(debitTd);
                Element creditTd = DOM.createTD();
                childTr.appendChild(creditTd);
                Element endDebitTd = DOM.createTD();
                childTr.appendChild(endDebitTd);
                Element endCreditTd = DOM.createTD();
                childTr.appendChild(endCreditTd);

                if (accItem1.getAccountId() != null) {
                    final Integer accountId = accItem1.getAccountId();
                    if (showBalances && accItem1.getBeginningDebit() != null) {
                        beginDebitTd.appendChild(getDOMLink(accItem1.getBeginningDebit(), accountId, BEGINNING_BALANCE));
                        beginDebitTd.addClassName(RIGHT_ALIGN_CELL);
                    }
                    if (showBalances && accItem1.getBeginningCredit() != null) {
                        beginCreditTd.appendChild(getDOMLink(accItem1.getBeginningCredit(), accountId, BEGINNING_BALANCE));
                        beginCreditTd.addClassName(RIGHT_ALIGN_CELL);
                    }
                    if (accItem1.getDebit() != null) {
                        debitTd.appendChild(getDOMLink(accItem1.getDebit(), accountId, DEBIT));
                        debitTd.addClassName(RIGHT_ALIGN_CELL);
                    }
                    if (accItem1.getCredit() != null) {
                        creditTd.appendChild(getDOMLink(accItem1.getCredit(), accountId, CREDIT));
                        creditTd.addClassName(RIGHT_ALIGN_CELL);
                    }
                    if (showBalances && accItem1.getEndingDebit() != null) {
                        endDebitTd.appendChild(getDOMLink(accItem1.getEndingDebit(), accountId, ENDING_BALANCE));
                        endDebitTd.addClassName(RIGHT_ALIGN_CELL);
                    }
                    if (showBalances && accItem1.getEndingCredit() != null) {
                        endCreditTd.appendChild(getDOMLink(accItem1.getEndingCredit(), accountId, ENDING_BALANCE));
                        endCreditTd.addClassName(RIGHT_ALIGN_CELL);
                    }
                }

                return childTBody;

            } else {
                Element childTr = DOM.createTR();
                element.appendChild(childTr);

                Element codeTd = DOM.createTD();
                codeTd.addClassName(LEFT_ALIGN_CELL);
                Span codeSpan = new Span(accItem1.getCode());
                codeTd.appendChild(codeSpan.getElement());
                childTr.appendChild(codeTd);

                Element nameTd = DOM.createTD();
                nameTd.addClassName(LEFT_ALIGN_CELL);
                Span nameSpan = new Span(accItem1.getName());
                nameTd.appendChild(nameSpan.getElement());
                childTr.appendChild(nameTd);

                Element beginDebitTd = DOM.createTD();
                childTr.appendChild(beginDebitTd);
                Element beginCreditTd = DOM.createTD();
                childTr.appendChild(beginCreditTd);
                Element debitTd = DOM.createTD();
                childTr.appendChild(debitTd);
                Element creditTd = DOM.createTD();
                childTr.appendChild(creditTd);
                Element endDebitTd = DOM.createTD();
                childTr.appendChild(endDebitTd);
                Element endCreditTd = DOM.createTD();
                childTr.appendChild(endCreditTd);

                if (accItem1.getAccountId() != null) {
                    final Integer accountId = accItem1.getAccountId();
                    if (showBalances && accItem1.getBeginningDebit() != null) {
                        beginDebitTd.appendChild(getDOMLink(accItem1.getBeginningDebit(), accountId, BEGINNING_BALANCE));
                        beginDebitTd.addClassName(RIGHT_ALIGN_CELL);
                    }
                    if (showBalances && accItem1.getBeginningCredit() != null) {
                        beginCreditTd.appendChild(getDOMLink(accItem1.getBeginningCredit(), accountId, BEGINNING_BALANCE));
                        beginCreditTd.addClassName(RIGHT_ALIGN_CELL);
                    }
                    if (accItem1.getDebit() != null) {
                        debitTd.appendChild(getDOMLink(accItem1.getDebit(), accountId, DEBIT));
                        debitTd.addClassName(RIGHT_ALIGN_CELL);
                    }
                    if (accItem1.getCredit() != null) {
                        creditTd.appendChild(getDOMLink(accItem1.getCredit(), accountId, CREDIT));
                        creditTd.addClassName(RIGHT_ALIGN_CELL);
                    }
                    if (showBalances && accItem1.getEndingDebit() != null) {
                        endDebitTd.appendChild(getDOMLink(accItem1.getEndingDebit(), accountId, ENDING_BALANCE));
                        endDebitTd.addClassName(RIGHT_ALIGN_CELL);
                    }
                    if (showBalances && accItem1.getEndingCredit() != null) {
                        endCreditTd.appendChild(getDOMLink(accItem1.getEndingCredit(), accountId, ENDING_BALANCE));
                        endCreditTd.addClassName(RIGHT_ALIGN_CELL);
                    }
                }
            }
        }
        return element;
    }

    private Element createTH() {
        Element header = DOM.createTHead();
        header.setClassName("point_affix_top text-nowrap");
        Element tr = DOM.createTR();

        Element th = DOM.createTH();
        th.addClassName("stickerCell");
        th.addClassName(TEXT_LEFT);
        Element divElement = DOM.createDiv();
        divElement.setClassName("frame_affix_top");
        divElement.getStyle().clearWidth();
        divElement.getStyle().setProperty("minWidth", "300px");
        divElement.setInnerHTML(wfmStrings.accountName());
        th.appendChild(divElement);
        tr.appendChild(th);

        Element th1 = DOM.createTH();
        th1.addClassName("stickerCell text-right");

        Element divElement1 = DOM.createDiv();
        divElement1.setClassName("frame_affix_top");

        divElement1.getStyle().clearWidth();
        divElement1.getStyle().setProperty("minWidth", "80px");
        th1.appendChild(divElement1);
        for (int i = 2; i >= 0; i--) {
            Element comparedTh = DOM.createTH();
            comparedTh.addClassName("stickerCell text-right");
            Element divElement2 = DOM.createDiv();
            divElement2.getStyle().setProperty("minWidth", "80px");
            divElement2.setClassName("frame_affix_top");
            comparedTh.appendChild(divElement2);
            tr.appendChild(comparedTh);
        }

        tr.appendChild(th1);
        header.appendChild(tr);
        return header;
    }

    private void createGroupHeader(Element element, String groupName) {
        Element tr = DOM.createTR();
        tr.addClassName("heading_row");
        Element td = DOM.createTD();

        Element icon = DOM.createElement("i");
        icon.addClassName("btn--circle plusMinus");
        DOM.sinkEvents(icon.cast(), Event.ONCLICK);
        DOM.sinkEvents(icon.cast(), Event.ONCLICK);
        DOM.setEventListener(icon.cast(), event -> {
            if (element.getClassName().contains("collapsed")) {
                element.removeClassName("collapsed");
                element.addClassName("expanded");
            } else {
                element.removeClassName("expanded");
                element.addClassName("collapsed");
            }
        });

        Element nameElement = DOM.createElement("span");
        nameElement.setInnerHTML(groupName);
        td.appendChild(icon);
        td.appendChild(nameElement);

        td.setAttribute("colspan", "5");
        tr.appendChild(td);
        element.appendChild(tr);
    }

    private void createTotalRow(Element element, TrialBalance balance) {
        clearElementChild(element);
        Element tr = DOM.createTR();
        tr.addClassName("total_row total_row_last");
        element.appendChild(tr);

        Element totalTd = DOM.createTD();
        totalTd.setInnerHTML(wfmStrings.total());
        tr.appendChild(totalTd);

        Element emptyTd = DOM.createTD();
        tr.appendChild(emptyTd);

        Element beginDebitTd = DOM.createTD();
        beginDebitTd.setInnerHTML(getValueAsString(balance.getTotalBeginningDebit()));
        beginDebitTd.addClassName(RIGHT_ALIGN_CELL);
        tr.appendChild(beginDebitTd);

        Element beginCreditTd = DOM.createTD();
        beginCreditTd.setInnerHTML(getValueAsString(balance.getTotalBeginningCredit()));
        beginCreditTd.addClassName(RIGHT_ALIGN_CELL);
        tr.appendChild(beginCreditTd);

        Element debitTd = DOM.createTD();
        debitTd.setInnerHTML(getValueAsString(balance.getTotalDebit()));
        debitTd.addClassName(RIGHT_ALIGN_CELL);
        tr.appendChild(debitTd);

        Element creditTd = DOM.createTD();
        creditTd.setInnerHTML(getValueAsString(balance.getTotalCredit()));
        creditTd.addClassName(RIGHT_ALIGN_CELL);
        tr.appendChild(creditTd);

        Element endDebitTd = DOM.createTD();
        endDebitTd.setInnerHTML(getValueAsString(balance.getTotalEndingDebit()));
        endDebitTd.addClassName(RIGHT_ALIGN_CELL);
        tr.appendChild(endDebitTd);

        Element endCreditTd = DOM.createTD();
        endCreditTd.setInnerHTML(getValueAsString(balance.getTotalEndingCredit()));
        endCreditTd.addClassName(RIGHT_ALIGN_CELL);
        tr.appendChild(endCreditTd);
    }

    private Element getDOMLink(BigDecimal value, final Integer accountId, final String type) {
        Element link = DOM.createAnchor();
        link.setInnerHTML(getValueAsString(value));
        DOM.sinkEvents(link.cast(), Event.ONCLICK);
        DOM.setEventListener(link.cast(), event -> SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|transactionsByPeriod/" + accountId + "/trialBalance" +
                "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(fromValue.getDate()) +
                "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(toValue.getDate()) +
                "/" + type));

        return link;
    }

    private String getValueAsString(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return " " + AccountingUtils.get().formatPrice(value);
        } else {
            return "(" + AccountingUtils.get().formatPrice(value.abs()) + ")";
        }
    }

    private void clearElementChild(Element element) {
        Element child;
        while ((child = element.getFirstChildElement()) != null) {
            element.removeChild(child);
        }
    }

    private SelectItem[] getSortByItems() {
        SelectItem[] items = new SelectItem[2];

        items[0] = new SelectItem(0, wfmStrings.byAccount(), Constants.ACC_NAME, true);
        items[1] = new SelectItem(1, wfmStrings.byCode(), Constants.ACC_CODE);

        return items;
    }

    private SelectItem[] getSortItems() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(0, "A-Z", ASC_STR, true);
        items[1] = new SelectItem(1, "Z-A", DESC_STR);
        return items;
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

        mdp.add(TrialBalanceDetailed.this::getPortraitLink);
        mdp.add(TrialBalanceDetailed.this::getLandscapeLink);

        wrapper.add(mdp);

        setPDFListener();

        MaterialLink exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.excel());
        exportExl.addClickHandler(ch -> {
            String URL = (CommandConstants.COMMON_URL + "/trialBalanceDetailedExcelHandler");
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
            filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
            filter.setSortField(sortByListBox.getSelectedItem().getDescription());
            filter.setActualDue(consolidation.getValue());
            filter.setType(showValues.getSelectedId());
            filter.setAscending(ASC_STR.equals(sortTypeListBox.getSelectedItem().getDescription()));
            filter.setCurrencyID(currencyListBox.getSelectedId());
            if (isDepartmentRelationEnabled && financialYearStart != null && departmentLookUp.getSelectedItem() != null) {
                filter.setDepartmentId(departmentLookUp.getSelectedItem().getId());
            }
            HashMap<String, String> parametrs = filter.getRequestParams();
            Utils.sendPDFOrExcelRequest(exportExcelPanel, URL, parametrs, "_blank");
        });

        showMenuContainer.add(exportExl);

        showMenuBar.add(showLink);

        Div div = new Div();
        new KpiToolTip(showMenuBar, wfmStrings.export(), Position.TOP);
        div.add(showMenuBar);
        GBoxItem exportItem = headerPanel.addGroupBoxItem(0, null, div);
        exportItem.setStyleSplitRight(true);
        exportItem.setStyleWidthFree(true);
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
        String URL = (CommandConstants.PDF_URL + "/trialBalanceDetailedPDFHandler");
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setLandscape(landscape);
        filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
        filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
        filter.setSortField(sortByListBox.getSelectedItem().getDescription());
        filter.setActualDue(consolidation.getValue());
        filter.setType(showValues.getSelectedId());
        filter.setAscending(ASC_STR.equals(sortTypeListBox.getSelectedItem().getDescription()));
        filter.setCurrencyID(currencyListBox.getSelectedId());
        if (isDepartmentRelationEnabled && financialYearStart != null && departmentLookUp.getSelectedItem() != null) {
            filter.setDepartmentId(departmentLookUp.getSelectedItem().getId());
        }

        HashMap<String, String> parametrs = filter.getRequestParams();
        Utils.sendPDFOrExcelRequest(exportExcelPanel, URL, parametrs, "_blank");
    }

    private void clearCurrencyRateHistory() {
        exchangeRateInfoText = new StringBuilder();
        beginningPeriodRateText = new StringBuilder();
        currentPeriodRateText = new StringBuilder();
        toolTipFullText = new StringBuilder();
        currencyToolTip.setMessage("");
        currencyToolTip.setVisible(false);
    }

    private void initFilterPopup() {
        filterDialog = new KpiModal();
        filterDialog.setWidth(400);
        filterDialog.setCloseButton(true);
        filterDialog.setDismissible(false);

        MaterialPanel contentPanel = new MaterialPanel();

        if (isDepartmentRelationEnabled) {
            departmentLookUp = new DepartmentLookUp();
            departmentLookUp.setWidth("100%");
            contentPanel.add(new FormGroup(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), departmentLookUp));
        }

        showValues = new DataListBox();
        showValues.setWithoutNullLabel(true);
        showValues.ensureDebugId("trialBalance-showListBox");
        showValues.addListItem(new SelectItem(2, wfmStrings.nonZero()));
        showValues.addListItem(new SelectItem(ALL_ACCOUNTS, accountingStrings.allAccounts()));
        showValues.setSelected(2);
        contentPanel.add(new FormGroup(wfmStrings.show(), showValues));

        consolidation = new KpiCheckBox(accountingStrings.consolidation());
        consolidation.ensureDebugId("trialBalance-consolidation-checkBox");
        consolidation.addValueChangeHandler(valueChangeEvent -> {
            if (consolidation.getValue()) {
                currencyListBox.setSelected(baseCurrencyId);
                currencyListBox.setEnabled(false);
                clearCurrencyRateHistory();
            } else {
                currencyListBox.setEnabled(true);
            }
        });

        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP)) {
            contentPanel.add(new FormGroup(consolidation));

        }
        WfmButton2 resetButton = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_SECONDARY);
        resetButton.addClickHandler(clickEvent -> {
            if (departmentLookUp != null) {
                departmentLookUp.clear();
            }
            showValues.setSelected(2);
            consolidation.setValue(Boolean.FALSE);
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
}
