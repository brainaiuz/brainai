package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.CashFlow;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.CashFlow;
import com.edatasite.workforce.gwt.accounting.client.rpc.CashFlowItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceFilterData;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
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
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCustomToolTip;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxDatePeriodItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
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
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Sherzod on 1/11/2016.
 */
public class NewCashFlow extends Composite implements Constants {
    interface NewCashFlowUiBinder extends UiBinder<HTMLPanel, NewCashFlow> {
    }

    private static final NewCashFlowUiBinder ourUiBinder = GWT.create(NewCashFlowUiBinder.class);

    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static final boolean isDepartmentRelationEnabled = AccountingUtils.get().isEnableAccountingDepartmentRelation();

    private final String BALANCE = "BALANCE";

    private DataListBox departmentListBox;
    private final DatePicker fromValue;
    private final DatePicker toValue;
    private Date financialYearStart;
    private Date currentDate;
    private final String sortField;
    private final String sortDirection;
    private final DataListBox currencyListBox;
    private final KpiCustomToolTip currencyToolTip;
    private Integer baseCurrencyId;
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;

    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    HTMLPanel exportPanel;
    @UiField
    AnchorElement accountText;
    @UiField
    AnchorElement sortByAccount;
    @UiField
    DivElement balanceText;
    @UiField
    Element operatingTr;
    @UiField
    Element netProfit;
    @UiField
    Element currentAssets;
    @UiField
    Element prepayments;
    @UiField
    Element currentLability;
    @UiField
    Element operatingTotalTr;
    @UiField
    Element investingTr;
    @UiField
    Element deprication;
    @UiField
    Element fixedAsset;
    @UiField
    Element liability;
    @UiField
    Element nonCurrent;
    @UiField
    Element investingTotalTr;
    @UiField
    Element financingTr;
    @UiField
    Element longTermLability;
    @UiField
    Element equity;
    @UiField
    Element financingTotalTr;
    @UiField
    Element netIncreaseDecreaseForPeriod;
    @UiField
    Element cashAtTheBeginningOfPeriod;
    @UiField
    Element cashAtTheEndOfPeriod;
    @UiField
    Element tableHeader;


    public NewCashFlow() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);

        GBox groupBox = headerPanel.drawNewGroupBox();
        groupBox.setStyleUnited(true);
        groupBox.setStyleWidthFree(true);

        sortDirection = ASC_STR;
        sortField = ACC_CODE;

        GBoxDatePeriodItem datePeriodItem = new GBoxDatePeriodItem();
        fromValue = new DatePicker();
        fromValue.ensureDebugId("cashFlow-statement-fromDate");
        datePeriodItem.setStartBoxItem(wfmStrings.from(), fromValue);

        toValue = new DatePicker();
        toValue.ensureDebugId("cashFlow-statement-toDate");
        datePeriodItem.setDueBoxItem(wfmStrings.to(), toValue);
        headerPanel.addGroupBoxItem(datePeriodItem);

        //Currency Tooltip
        currencyToolTip = new KpiCustomToolTip("");
        currencyToolTip.setVisible(false);

        currencyListBox = new DataListBox();
        currencyListBox.setMaxWidth("8.46rem");
        currencyListBox.setWithoutNullLabel(true);
        currencyListBox.ensureDebugId("balance-currencyListBox");
        currencyListBox.addValueChangeHandler(changeEvent -> onCurrencyChange());
        GBoxItem currencyItem = headerPanel.addGroupBoxItem(0, wfmStrings.currency(), currencyToolTip, currencyListBox);
        currencyItem.setStyleSplitRight(!isDepartmentRelationEnabled);

        if (isDepartmentRelationEnabled) {
            departmentListBox = new DataListBox();
            ListingFilterParameter filter = new ListingFilterParameter();
            AccountingService.App.get().getDepartmentsForAccounting(filter, new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(SelectItem[] selectItems) {
                    departmentListBox.setItems(selectItems);
                }
            });
            GBoxItem departmentItem = headerPanel.addGroupBoxItem(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), departmentListBox);
            departmentItem.setStyleSplitRight(true);
        }

        accountText.setInnerHTML(wfmStrings.accountName());

        AccountingService.App.get().getTrialBalanceFilterData(new AsyncCallback<TrialBalanceFilterData>() {
            @Override
            public void onFailure(Throwable caught) {
                onUpdate();
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
                balanceText.setInnerHTML(accountingMessages.balance(currencyListBox.getSelectedItem(true).getName()));
                onUpdate();
            }
        });

        WfmButton2 updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(event -> onUpdate());
        GBoxItem updateItem = headerPanel.addGroupBoxItem(null, updateButton);
        updateItem.setStyleSplitRight(true);
        updateItem.setStyleWidthFree(true);
        updateItem.getComponent().getElement().addClassName("group-box__item-content--no-border");

        exportSection();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICE_VOID, NewCashFlow.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MONEY_TRANSFER, NewCashFlow.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, NewCashFlow.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, NewCashFlow.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, NewCashFlow.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FIXED_ASSET_SAVED, NewCashFlow.this, (sender, args) -> onUpdate());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MANUAL_TRANSACTION_SAVED, NewCashFlow.this, (sender, args) -> onUpdate());
    }

    private void onUpdate() {
        if (!validate()) {
            return;
        }
        initInternal();
    }

    private boolean busy;
    private void initInternal() {

        //if there is running process then ignore the request
        if (busy) {
            return;
        }

        //start the process and lock it
        busy = true;

        //cuncurrent request counter
        STEP_COUNTER = 0;

        //clear before old beginning balance
        beginningBalance = BigDecimal.ZERO;

        LoadingPanel.loading(true);

        //CALCULATION CASH FLOW FOR THIS PERIOD
        ListingFilterParameter filter = new ListingFilterParameter();
        {
            filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
            filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
            filter.setSortField(sortField);
            filter.setAscending(ASC_STR.equals(sortDirection));
            filter.setCurrencyID(currencyListBox.getSelectedId());

            if (isDepartmentRelationEnabled && financialYearStart != null) {
                filter.setDepartmentId(departmentListBox.getSelectedId());
            }


            AccountingService.App.get().getCashFlow(filter, new AbstractAsyncCallback<CashFlow>() {
                        public void failure(Throwable caught) {
                            busy = false;
                            LoadingPanel.loading(false);
                        }

                        public void success(CashFlow result) {
                            cashFlow = result;

                            ++STEP_COUNTER;
                            drawingCachFlow();
                        }
                    }
            );
        }

        //CALCULATION CASH FLOW BEGINNING BALANCE
        {
            filter.setStartDateNC(Utils.getStartDateNCForFilter(new Date(0))); //January 1, 1970
            filter.setEndDateNC(Utils.getStartDateNCForFilter(DateUtil.addDays(fromValue.getDate(), -1)));
            GWT.log("Beginning start date: " + filter.getStartDateNC());
            GWT.log("Beginning end date: " + filter.getEndDateNC());

            AccountingService.App.get().getCashFlow(filter, new AsyncCallback<CashFlow>() {
                @Override
                public void onFailure(Throwable throwable) {
                    busy = false;
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(CashFlow cashFlow) {
                    beginningBalance = cashFlow.getNetIncreaseDecreaseForPeriod();

                    ++STEP_COUNTER;
                    drawingCachFlow();
                }
            });
        }
        balanceText.setInnerText(accountingMessages.balance(currencyListBox.getSelectedItem() != null
                                                            ? "(" + currencyListBox.getSelectedItem().getName() + ")"
                : wfmStrings.balance()));
    }


    private final int STEP_COUNT = 2;
    private int STEP_COUNTER = 0;
    private CashFlow cashFlow;
    private BigDecimal beginningBalance = BigDecimal.ZERO;

    private void drawingCachFlow() {

        if (STEP_COUNT == STEP_COUNTER) {

            //calculate ending balance of the cach flow
            cashFlow.setCashAtTheBeginningOfPeriod(beginningBalance);
            cashFlow.setCashAtTheEndOfPeriod(cashFlow.getCashAtTheBeginningOfPeriod().add(cashFlow.getNetIncreaseDecreaseForPeriod()));

            clearElementChild(operatingTr);
            clearElementChild(netProfit);
            clearElementChild(currentAssets);
            clearElementChild(prepayments);
            clearElementChild(currentLability);
            clearElementChild(operatingTotalTr);

            clearElementChild(investingTr);
            clearElementChild(deprication);
            clearElementChild(fixedAsset);
            clearElementChild(liability);
            clearElementChild(nonCurrent);
            clearElementChild(investingTotalTr);

            clearElementChild(financingTr);
            clearElementChild(longTermLability);
            clearElementChild(equity);
            clearElementChild(financingTotalTr);

            clearElementChild(netIncreaseDecreaseForPeriod);
            clearElementChild(cashAtTheBeginningOfPeriod);
            clearElementChild(cashAtTheEndOfPeriod);

            createGroupHeader(operatingTr, wfmStrings.operatingActivities());
            createGroupNetTotal(netProfit, accountingStrings.netProfit(), cashFlow.getNetProfit());
            collectTreeData(currentAssets, wfmStrings.currentAsset(), cashFlow.getCurrentAssets());
            collectTreeData(prepayments, accountingStrings.prepayment(), cashFlow.getPrepayments());
            collectTreeData(currentLability, wfmStrings.currentLiability(), cashFlow.getCurrentLiabilities());
            createGroupNetTotal(operatingTotalTr, accountingStrings.netCashFromOperatingActivities(), cashFlow.getNetOperatingActivities());

            createGroupHeader(investingTr, accountingStrings.investingActivities());
            collectTreeData(deprication, accountingStrings.accumulatedDepreciation(), cashFlow.getAccumulatedDepreciations());
            collectTreeData(fixedAsset, wfmStrings.fixedAssetAccounts(), cashFlow.getFixedAssets());
            collectTreeData(liability, wfmStrings.liabilities(), cashFlow.getLiabilities());
            collectTreeData(nonCurrent, accountingStrings.nonCurrentAsset(), cashFlow.getNonCurrentAssets());
            createGroupNetTotal(investingTotalTr, accountingStrings.netCashFromInvestingActivities(), cashFlow.getNetInvestingActivities());

            createGroupHeader(financingTr, wfmStrings.financingActivities());
            collectTreeData(longTermLability, wfmStrings.longTermLiabilityAccounts(), cashFlow.getLongTermLiabilities());
            collectTreeData(equity, wfmStrings.equityAccounts(), cashFlow.getEquities());
            createGroupNetTotal(financingTotalTr, wfmStrings.netCashFromFinancingActivities(), cashFlow.getNetFinancingActivities());

            createGrandTotal(netIncreaseDecreaseForPeriod, accountingStrings.netIncreaseDecreaseForPeriod(), cashFlow.getNetIncreaseDecreaseForPeriod());
            createGrandTotal(cashAtTheBeginningOfPeriod, wfmStrings.cashAtTheBeginningOfPeriod(), cashFlow.getCashAtTheBeginningOfPeriod());
            createGrandTotal(cashAtTheEndOfPeriod, wfmStrings.cashAtTheEndOfPeriod(), cashFlow.getCashAtTheEndOfPeriod());

            Utils.table__frame_affix_init();
            LoadingPanel.loading(false);
            busy = false;
        }
    }

    private void collectTreeData(Element element, String header, LinkedList<CashFlowItem> items) {
        if (items != null && items.size() > 0) {
            Map<String, CashFlowItem> map1 = new HashMap<>(items.size());
            ArrayList<CashFlowItem> map2 = new ArrayList<>();
            BigDecimal totalInnerGroupBalance = BigDecimal.ZERO;
            for (CashFlowItem item : items) {
                if (item.getBalance() != null) {
                    totalInnerGroupBalance = totalInnerGroupBalance.add(item.getBalance());
                }
                map1.put(item.getCode(), item);
            }
            for (CashFlowItem item : items) {
                if (item.getParentCode() != null) {
                    if (map1.get(item.getParentCode()) == null) {
                        AccountItem accountCodeUnique = new AccountItem(item.getParentId(), item.getParentCode(), item.getParentName());
                        CashFlowItem cashFlowItem = new CashFlowItem(accountCodeUnique.getId(), accountCodeUnique.getCode(), accountCodeUnique.getName(), BigDecimal.ZERO);
                        cashFlowItem.getChilds().add(item);
                        map1.put(item.getParentCode(), cashFlowItem);
                        map2.add(cashFlowItem);
                    } else {
                        map1.get(item.getParentCode()).getChilds().add(item);
                    }
                } else {
                    map2.add(item);
                }
            }
            createInnerGroupHeader(element, header);
            map2.forEach(key -> {
                BigDecimal childTotal = new BigDecimal(0);
                createGroup1(element, key, childTotal);
            });
            createInnerGroupFooter(element, header, totalInnerGroupBalance);
        }
    }


    private void createGroup1(Element parentElement, CashFlowItem accItem1, BigDecimal childTotal) {
        Element element = create(parentElement, accItem1, !accItem1.getChilds().isEmpty());
        if (!accItem1.getChilds().isEmpty()) {
            for (CashFlowItem child : accItem1.getChilds()) {
                createGroup1(element, child, childTotal);

                if (!child.isCalculated()) {
                    childTotal = childTotal.add(child.getBalance());
                    child.setCalculated(true);
                }
            }

            if (!accItem1.isCalculated()) {
                childTotal = childTotal.add(accItem1.getBalance());
                accItem1.setCalculated(true);
            }
            if (accItem1.getAccount() != null) {
                createGroupTotalRow(element, NewCashFlow.wfmStrings.total() + " " + accItem1.getAccount().getName(), childTotal);
            }
        }
    }

    private Element create(Element element, CashFlowItem accItem1, boolean hasChilds) {
        Element mainTr = DOM.createTR();
        Element mainTd = DOM.createTD();
        mainTr.appendChild(mainTd);
        mainTr.addClassName("set_unit_2_row");
        if (hasChilds) {
            Element childTable = DOM.createTable();
            childTable.setClassName("table table_report");

            Element header = createTH();
            header.setAttribute("style", "display: none;");

            Element childTBody = DOM.createTBody();
            childTBody.setClassName("category_set collapsed");

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

            childTd.appendChild(icon);
            childTd.addClassName("text-left");
            Element nameElement = DOM.createElement("span");
            nameElement.setInnerHTML(accItem1.getAccount().getName().concat("<small>").concat("(").concat(accItem1.getCode()).concat(")").concat("</small>"));
            childTd.appendChild(nameElement);

            Element td2 = DOM.createTD();
            childTr.appendChild(td2);
            if (accItem1.getAccount().getId() != null) {
                final Integer accountId = accItem1.getAccount().getId();
                if (accItem1.getBalance() != null) {
                    td2.appendChild(getDOMLink(accItem1.getBalance(), accountId, BALANCE));
                    td2.addClassName("text-right");
                }
            }

            childTBody.appendChild(childTr);
            element.appendChild(mainTr);
            return childTBody;
        } else {
            Element tr = DOM.createTR();
            tr.addClassName("set_unit_2_row");
            Element td1 = DOM.createTD();
            td1.addClassName("text-left");
            td1.setInnerHTML(accItem1.getAccount().getName().concat("<small>").concat("(").concat(accItem1.getCode()).concat(")").concat("</small>"));
            tr.appendChild(td1);

            Element td2 = DOM.createTD();
            tr.appendChild(td2);

            if (accItem1.getAccount().getId() != null) {
                final Integer accountId = accItem1.getAccount().getId();
                if (accItem1.getBalance() != null) {
                    td2.appendChild(getDOMLink(accItem1.getBalance(), accountId, BALANCE));
                    td2.addClassName("text-right");
                    element.appendChild(tr);
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
        divElement.setInnerHTML(NewCashFlow.wfmStrings.accountName());
        th.appendChild(divElement);
        tr.appendChild(th);

        Element th1 = DOM.createTH();
        th1.addClassName("stickerCell text-right");

        Element divElement1 = DOM.createDiv();
        divElement1.setClassName("frame_affix_top");

        divElement1.getStyle().clearWidth();
        divElement1.getStyle().setProperty("minWidth", "80px");
        th1.appendChild(divElement1);

        tr.appendChild(th1);
        header.appendChild(tr);
        return header;
    }

    private void createGroupTotalRow(Element element, String groupName, BigDecimal itemsTotal) {
        if (element != null) {
            Element td = DOM.createTD();

            Element tr = DOM.createTR();
            tr.addClassName("total_row");
            td.setInnerHTML(groupName);
            td.setAttribute("style", "border-right: none;");
            tr.appendChild(td);
            element.appendChild(tr);
            Element td2 = DOM.createTD();
            tr.appendChild(td2);

            td2.setInnerHTML(getValueAsString(itemsTotal));
            td2.addClassName(RIGHT_ALIGN_CELL);

            element.appendChild(tr);
        }
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

    private void createGroupHeader(Element element, String text) {
        Element tr = DOM.createTR();

        Element tdHeader = DOM.createTD();
        tdHeader.addClassName("text-left");
        tdHeader.setAttribute("colspan", "2");
        Element span = DOM.createSpan();
        span.setInnerHTML(text);
        tdHeader.appendChild(span);

        tr.appendChild(tdHeader);
        element.appendChild(tr);
    }

    private void createGroupNetTotal(Element element, String text, BigDecimal netTotal) {
        Element tr = DOM.createTR();

        Element tdAccount = DOM.createTD();
        tdAccount.addClassName("text-left");
        Element span = DOM.createSpan();
        span.setInnerHTML(text);
        tdAccount.appendChild(span);

        Element tdBalance = DOM.createTD();
        tdBalance.addClassName("text-right");
        Element spanBalance = DOM.createSpan();
        spanBalance.setInnerHTML(getValueAsString(netTotal));
        tdBalance.appendChild(spanBalance);

        tr.appendChild(tdAccount);
        tr.appendChild(tdBalance);
        element.appendChild(tr);
    }

    private void createGrandTotal(Element element, String text, BigDecimal total) {
        Element tr = DOM.createTR();
        tr.addClassName("total_row double");

        Element tdHeader = DOM.createTD();
        tdHeader.addClassName("text-left");
        Element span = DOM.createSpan();
        span.setInnerHTML(text);
        tdHeader.appendChild(span);

        Element tdBalance = DOM.createTD();
        tdBalance.addClassName("text-right");
        Element spanBalance = DOM.createSpan();
        spanBalance.setInnerHTML(getValueAsString(total));
        tdBalance.appendChild(spanBalance);

        tr.appendChild(tdHeader);
        tr.appendChild(tdBalance);
        element.appendChild(tr);
    }

    private Element getDOMLink(BigDecimal value, final Integer accountId, final String type) {
        Element link = DOM.createAnchor();
        link.setInnerHTML(getValueAsString(value));
        DOM.sinkEvents(link.cast(), Event.ONCLICK);
        DOM.setEventListener(link.cast(), event -> SinksContainerFactory.entryPoint.onHistoryChanged("clickedreport|transactionsByPeriod/" + accountId + "/newCashFlow" +
                "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(fromValue.getDate()) +
                "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(toValue.getDate()) +
                "/" + type));

        return link;
    }

    private void createInnerGroupHeader(Element element, String headerName) {
        Element tr = DOM.createTR();
        tr.addClassName("heading_row");
        Element td = DOM.createTD();
        td.setAttribute("colspan", "2");

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
        nameElement.setInnerHTML(headerName);
        td.appendChild(icon);
        td.appendChild(nameElement);
        tr.appendChild(td);
        element.appendChild(tr);
    }

    private void createInnerGroupFooter(Element element, String headerName, BigDecimal totalInnerGroupBalance) {
        Element tr = DOM.createTR();
        tr.addClassName("total_row");

        Element tdAccount = DOM.createTD();
        tdAccount.addClassName("text-left");
        Element span = DOM.createSpan();
        span.setInnerHTML(wfmStrings.total().concat(" ").concat(headerName));
        tdAccount.appendChild(span);

        Element tdBalance = DOM.createTD();
        tdBalance.addClassName("text-right");
        Element spanBalance = DOM.createSpan();
        spanBalance.setInnerHTML(getValueAsString(totalInnerGroupBalance));
        tdBalance.appendChild(spanBalance);

        tr.appendChild(tdAccount);
        tr.appendChild(tdBalance);
        element.appendChild(tr);
    }

    private String getValueAsString(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return " " + AccountingUtils.get().formatPrice(value);
        } else {
            return "(" + AccountingUtils.get().formatPrice(value.abs()) + ")";
        }
    }

    private void clearElementChild(Element element) {
        element.removeAllChildren();
    }

    private void clearCurrencyRateHistory() {
        currencyToolTip.setMessage("");
        currencyToolTip.setVisible(false);
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

        mdp.add(NewCashFlow.this::getPortraitLink);
        mdp.add(NewCashFlow.this::getLandscapeLink);

        wrapper.add(mdp);

        setPDFListener();

        MaterialLink exportExl = new MaterialLink();
        exportExl.addStyleName("hasicon--left");
        Icon exlIcon = new Icon();
        exlIcon.setClass("ficon--file-excel");
        exportExl.add(exlIcon);
        exportExl.setText(wfmStrings.excel());
        exportExl.addClickHandler(ch -> {
            String URL = (CommandConstants.COMMON_URL + "/cashFlowExcelHandler");
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setPropertyCode("cashFlowStatement");
            filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
            filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
            filter.setSortField(sortField);
            filter.setAscending(ASC_STR.equals(sortDirection));
            if (isDepartmentRelationEnabled && financialYearStart != null) {
                filter.setDepartmentId(departmentListBox.getSelectedId());
            }
            filter.setCurrencyID(currencyListBox.getSelectedId());
            HashMap<String, String> parametrs = filter.getRequestParams();
            Utils.sendPDFOrExcelRequest(exportPanel, URL, parametrs, "_blank");

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
        String URL = (CommandConstants.PDF_URL + "/cashFlowPDFHandler");
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setLandscape(landscape);
        filter.setPropertyCode("cashFlowStatement");
        filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
        filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
        filter.setSortField(sortField);
        filter.setAscending(ASC_STR.equals(sortDirection));
        if (isDepartmentRelationEnabled && financialYearStart != null) {
            filter.setDepartmentId(departmentListBox.getSelectedId());
        }
        filter.setCurrencyID(currencyListBox.getSelectedId());
        HashMap<String, String> parametrs = filter.getRequestParams();
        Utils.sendPDFOrExcelRequest(exportPanel, URL, parametrs, "_blank");
    }
}
