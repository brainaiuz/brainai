package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.CrmAccountBalance;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.TrialBalanceFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.DatesListEnum;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.CrmAccountBalance;
import com.edatasite.workforce.gwt.client.client.rpc.CrmAccountBalanceItem;
import com.edatasite.workforce.gwt.client.client.rpc.CrmAccountCurrencyBalance;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCustomToolTip;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxDatePeriodItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by admin on 10/9/2014.
 */
public class NewCrmAccountBalance extends Composite implements AccountingConstants, Constants {
    interface NewCrmAccountBalanceUiBinder extends UiBinder<HTMLPanel, NewCrmAccountBalance> {
    }

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final NewCrmAccountBalanceUiBinder ourUiBinder = GWT.create(NewCrmAccountBalanceUiBinder.class);

    private final String crmAccountType;

    @UiField
    Element tableBody;
    @UiField
    HTMLPanel noResultText;
    @UiField
    TableSectionElement tableHeader;
    @UiField
    HTMLPanel exportPanel;
    @UiField
    SectionBoxPanel headerPanel;

    private DatePicker startDateValue;
    private DatePicker endDateValue;

    private int nowPosition;
    private int allCount;
    private int step;

    private KpiModal filterDialog;
    private final MaterialLink pagingResultText;

    private MaterialSplitButton exportButtons;
    private final MaterialLink prevLink;
    private final MaterialLink nextLink;
    private final TextBox current;
    private KpiCheckBox includeSubAccount;

    private final WfmButton2 sendReportButton;
    private final WfmButton2 filterButton;
    private final WfmButton2 updateButton;

    private CrmAccountLookUp crmAccountLookUp;
    private final DataListBox datesValues;
    private Date financialYearStart;
    private Date conversationDate;
    private Date currentDate;
    private LinkedList<Date> financialQuartiesList = new LinkedList<>();

    private final ListingFilterParameter parameter;

    private KpiCustomToolTip currencyToolTip;
    private DataListBox currencyListBox;
    private Integer baseCurrencyId;
    private CustomFormItemPdfTemplateList templateList;
    private MaterialLink pdfVersion;
    private MaterialLink xlsVersion;

    public NewCrmAccountBalance(Integer crmAccountID, String crmAccountType) {
        parameter = new ListingFilterParameter();
        this.crmAccountType = crmAccountType;

        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);

        GBox groupBox = headerPanel.drawNewGroupBox();
        groupBox.setStyleUnited(true);
        groupBox.setStyleWidthFree(true);

        datesValues = new DataListBox();
        datesValues.setItems(AccountingUtils.getDatesListItems());
        datesValues.ensureDebugId("profitAndLoss-dateListBox");
        datesValues.setSelected(DatesListEnum.ThisFiscalYear.getId());
        datesValues.addValueChangeHandler(changeEvent -> AccountingUtils.setFromAndToDates(getCurrentDate(), getFinancialYearStart(), getConversationDate(), datesValues, getFinancialQuartiesList(), startDateValue, endDateValue));
        headerPanel.addGroupBoxItem(wfmStrings.dates(), datesValues);

        GBoxDatePeriodItem datePeriodItem = new GBoxDatePeriodItem();
        startDateValue = new DatePicker();
        startDateValue.setDate(DateUtil.getYearFirstDay(new Date()));
        startDateValue.addChangeHandler(changeEvent -> initReportData(crmAccountLookUp.getSelectedItemID()));
        datePeriodItem.setStartBoxItem(wfmStrings.from(), startDateValue);

        endDateValue = new DatePicker();
        endDateValue.setDate(new Date());
        datePeriodItem.setDueBoxItem(wfmStrings.to(), endDateValue);
        endDateValue.addChangeHandler(changeEvent -> initReportData(crmAccountLookUp.getSelectedItemID()));
        headerPanel.addGroupBoxItem(datePeriodItem);

        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTIPLE_CURRENCY_CRM_ACCOUNT_BALANCE)) {
            initCurrencyBox();
            headerPanel.addGroupBoxItem(0, wfmStrings.currency(), currencyToolTip, currencyListBox);
        }

        exportSection();
        initFilterPopup();

        filterButton = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--filter");
        filterButton.removeHasiconLeftStyle();
        filterButton.addClickHandler(event -> filterDialog.open());
        GBoxItem filterItem = headerPanel.addGroupBoxItem(0, null, filterButton);
        filterItem.setStyleSplitRight(true);

        updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(ch -> {
            parameter.setLimit(50);
            parameter.setStart(0);
            initReportData(crmAccountLookUp.getSelectedItemID());
        });
        GBoxItem updateItem = headerPanel.addGroupBoxItem(0, null, updateButton);
        updateItem.setStyleSplitRight(true);

        sendReportButton = new WfmButton2(wfmStrings.send());
        sendReportButton.addClickHandler(ch -> {
            /*new AccountingComposeView(CrmAccountItem.CUSTOMER.equals(getCrmAccountType()) ? Constants.CUSTOMER_BALANCE_CATEGORY : Constants.SUPPLIER_BALANCE_CATEGORY,
                    crmAccountLookUp.getSelectedItemID(), startDateValue.getDate(), endDateValue.getDate(), includeSubAccount.getValue());*/
            String type = CrmAccountItem.CUSTOMER.equals(getCrmAccountType()) ? Constants.CUSTOMER_BALANCE_CATEGORY : Constants.SUPPLIER_BALANCE_CATEGORY;
            SinksContainerFactory.entryPoint.onHistoryChanged("accountingemailcompose|add/add/" + type + "/" + crmAccountLookUp.getSelectedItemID() + "/" + startDateValue.getDate().getTime() + "/" + endDateValue.getDate().getTime() + "/" + includeSubAccount.getValue());
        });
        if (!Utils.hasRole(CLIENT)) {
            GBoxItem sendReportItem = headerPanel.addGroupBoxItem(0, null, sendReportButton);
            sendReportItem.setStyleSplitRight(true);
        }

        pagingResultText = new MaterialLink();
        pagingResultText.setHref("javascript:void(0)");
        pagingResultText.setClass("btn btn--white");
        GBoxItem pagingResutlItem = headerPanel.addGroupBoxItem(null, pagingResultText);
        pagingResutlItem.getElement().setAttribute("style", "margin-left: auto;");

        Icon prevIcon = new Icon();
        prevIcon.setClass("ficon--chevron-left");
        prevLink = new MaterialLink();
        prevLink.setStyleName("btn btn--white btn--icon");
        prevLink.add(prevIcon);
        headerPanel.addGroupBoxItem(null, prevLink);

        current = new TextBox();
        current.setStyleName("currLoc");
        GBoxItem currentItem = headerPanel.addGroupBoxItem(null, current);
        currentItem.addStyleToComponent("paging__currentpage");

        Icon nextIcon = new Icon();
        nextIcon.setClass("ficon--chevron-right");
        nextLink = new MaterialLink();
        nextLink.setStyleName("btn btn--white btn--icon");
        nextLink.add(nextIcon);
        GBoxItem nextItem = headerPanel.addGroupBoxItem(null, nextLink);
        nextItem.setStyleSplitRight(true);

        prevLink.addClickHandler(ch -> {
            if (nowPosition > 0) {
                pageEvent(nowPosition - step);
            }
        });
        nextLink.addClickHandler(ch -> {
            if (nowPosition + step < allCount) {
                pageEvent(nowPosition + step);
            }
        });
        current.addKeyUpHandler(keyUpEvent -> {
            int key = keyUpEvent.getNativeKeyCode();

            if (key == KeyCodes.KEY_ENTER) {
                try {
                    int begin = Integer.parseInt(current.getValue().trim());
                    if (begin > 0 && begin <= (int) Math.ceil(((double) allCount / (double) (step)))) {
                        pageEvent(begin * step - step);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        initReportData(crmAccountID);
    }

    private void pageEvent(int beganPositon) {
        parameter.setStart(beganPositon);
        initReportData(crmAccountLookUp.getSelectedItemID());
    }

    private void initReportData(Integer crmAccountID) {

        LoadingPanel.loading(true);
        noResultText.getElement().setInnerHTML(" ");
        parameter.setCrmAccountId(crmAccountID);
        parameter.setAccountType(crmAccountType);
        parameter.setShowSubAccountTransaction(includeSubAccount.getValue());
        if (currencyListBox != null) {
            parameter.setCurrencyID(currencyListBox.getSelectedId());
        }

        if (parameter.getStart() == 0 && parameter.getLimit() == 0) {
            parameter.setStart(0);
            parameter.setLimit(50);
        }
        ClientService.App.get().getCrmAccountBalanceReport(Utils.getStartDateNC(startDateValue.getDate()), Utils.getEndDateNC(endDateValue.getDate()), parameter, new AsyncCallback<CrmAccountBalance>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CrmAccountBalance result) {
                LoadingPanel.loading(false);
                financialYearStart = DateUtil.addDays(AccountingUtils.getFinancialYearEnd(), 1);
                conversationDate = AccountingUtils.getConversionDate();
                currentDate = new Date();
                financialYearStart.setYear(currentDate.getYear());

                while (financialYearStart.after(currentDate)) {
                    financialYearStart.setYear(financialYearStart.getYear() - 1);
                }
                financialQuartiesList = Utils.setupFinancialQuarties(currentDate, financialYearStart);
                if (result == null || result.getCurrencyBalances() == null || result.getCurrencyBalances().isEmpty()) {
                    noResultText.getElement().setInnerHTML(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
                }
                if (result != null && result.getCrmAccountItem() != null && crmAccountLookUp.getSelectedItemID() == null) {
                    crmAccountLookUp.addItem(result.getCrmAccountItem());
                }
                tableHeader.removeAllChildren();
                createHeaderRow();

                tableBody.removeAllChildren();
                ArrayList<CrmAccountCurrencyBalance> currencyBalanceArrayList = result != null ? result.getCurrencyBalances() : new ArrayList<>();
                BigDecimal totalEndingBalance = BigDecimal.ZERO;
                for (CrmAccountCurrencyBalance balanceData : currencyBalanceArrayList) {
                    String baseCurrencyTest = balanceData.getBaseCurrency().getName();
                    String currencyTest = wfmStrings.figuresIn() + (balanceData.getCurrency().getSymbol() == null ? "" : " " + balanceData.getCurrency().getSymbol()) + " (" + balanceData.getCurrency().getName() + ")";
                    createCurrencyRow(tableBody, result.getCrmAccountItem().getName(), baseCurrencyTest, currencyTest);
                    createBeginningEnddingBalance(tableBody, wfmStrings.beginningBalance(), balanceData.getEarlyBalanceInBase(), balanceData.getEarlyBalance(), false);

                    if (balanceData.getItems().length > 0) {
                        CrmAccountBalanceItem[] items = balanceData.getItems();
                        for (CrmAccountBalanceItem item : items) {
                            createItemRow(item);
                        }
                    }
                    totalEndingBalance = totalEndingBalance.add(balanceData.getEndingBalanceInBase());
                    createBeginningEnddingBalance(tableBody, accountingMessages.endingBalance(balanceData.getCurrency().getName()), balanceData.getEndingBalanceInBase(), balanceData.getEndingBalance(), false);
                }
                if (Utils.hasGenericAccess(GenericSettingsEnum.MULTIPLE_CURRENCY_CRM_ACCOUNT_BALANCE)) {
                    createBeginningEnddingBalance(tableBody, wfmStrings.endingBalance(), totalEndingBalance, BigDecimal.ZERO, true);
                }

                step = parameter.getLimit();
                nowPosition = parameter.getStart();
                allCount = result != null ? result.getTotalCount() : 0;

                current.setValue("" + (nowPosition / step + 1));
                pagingResultText.setText((nowPosition + 1) + " - " + (Math.min((nowPosition + step), allCount)) + " " + wfmStrings.of() + " " + allCount);

                Utils.table__frame_affix_init();
                //Wide View should be used here otherwise in "Standard View" the table will not shrink
            }
        });
    }

    private void initCurrencyBox() {
        currencyToolTip = new KpiCustomToolTip("Test", true);
        currencyToolTip.setVisible(false);
        currencyListBox = new DataListBox();
        currencyListBox.setWithoutNullLabel(true);
        currencyListBox.ensureDebugId("CrmAccountBalance-currencyListBox");
        currencyListBox.addValueChangeHandler(changeEvent -> onCurrencyChange());
        currencyListBox.setMaxWidth("8.46rem");

        AccountingService.App.get().getTrialBalanceFilterData(new AsyncCallback<TrialBalanceFilterData>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(TrialBalanceFilterData result) {
                setCurrency(result.getCurrencies());
                if (currencyListBox.getSelectedId() == null && result.getBaseCurrency() != null) {
                    currencyListBox.setSelected(result.getBaseCurrency());
                }
            }
        });
    }

    private void onCurrencyChange() {
        currentDate = endDateValue.getDate();

        if (currencyListBox.getSelectedId() == null) {
            clearCurrencyRateHistory();
            return;
        }
        if (currencyListBox.getSelectedId().equals(baseCurrencyId)) {
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
//        currencyToolTip.setMessage("");
        currencyToolTip.setVisible(false);
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

    private void createBeginningEnddingBalance(Element element, String text, BigDecimal valueInBase, BigDecimal value, Boolean finalTotal) {
        Element tr = DOM.createTR();
        tr.addClassName("set_head_2_row");
        Element td = DOM.createTD();
        td.setAttribute("colspan", includeSubAccount.getValue() ? "6" : "5");
        td.setInnerHTML("<strong>" + text + "</strong");
        tr.appendChild(td);

        Element td3 = DOM.createTD();
        if (!finalTotal) {
            td3.addClassName("text-right");
            if (value != null) {
                td3.setTitle(value.setScale(5, RoundingMode.HALF_UP).toString());
            }
            td3.setInnerHTML("<strong>" + getFormattedAmount(value, true) + "</strong");
        }
        tr.appendChild(td3);

        Element td4 = DOM.createTD();
        td4.addClassName("text-right");
        if (valueInBase != null) {
            td4.setTitle(valueInBase.setScale(5, RoundingMode.HALF_UP).toString());
        }
        td4.setInnerHTML("<strong>" + getFormattedAmount(valueInBase, true) + "</strong");
        tr.appendChild(td4);

        element.appendChild(tr);
    }

    private void createHeaderRow() {
        Element tr = DOM.createTR();

        Element th = DOM.createTH();
        th.setClassName("stickerCell");
        Element div = DOM.createDiv();
        div.setClassName("frame_affix_top");
        div.setInnerText(wfmStrings.date());
        div.getStyle().setProperty("minWidth", "100px");
        th.appendChild(div);
        tr.appendChild(th);

        if (includeSubAccount.getValue()) {
            th = DOM.createTH();
            th.setClassName("stickerCell");
            div = DOM.createDiv();
            div.setClassName("frame_affix_top text-left");
            div.setInnerText(CrmAccountItem.CUSTOMER.equals(crmAccountType) ? Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) : wfmStrings.supplier());
            div.getStyle().setProperty("minWidth", "100px");
            th.appendChild(div);
            tr.appendChild(th);
        }

        th = DOM.createTH();
        th.setClassName("stickerCell");
        div = DOM.createDiv();
        div.setClassName("frame_affix_top text-left");
        div.setInnerText(accountingStrings.transaction());
        div.getStyle().setProperty("minWidth", "100px");
        th.appendChild(div);
        tr.appendChild(th);

        th = DOM.createTH();
        th.setClassName("stickerCell");
        div = DOM.createDiv();
        div.setClassName("frame_affix_top text-left");
        div.setInnerText(wfmStrings.reference());
        div.getStyle().setProperty("minWidth", "100px");
        th.appendChild(div);
        tr.appendChild(th);

        th = DOM.createTH();
        th.setClassName("stickerCell");
        div = DOM.createDiv();
        div.setClassName("frame_affix_top text-right");
        div.setInnerText(wfmStrings.debit());
        div.getStyle().setProperty("minWidth", "100px");
        th.appendChild(div);
        tr.appendChild(th);

        th = DOM.createTH();
        th.setClassName("stickerCell");
        div = DOM.createDiv();
        div.setClassName("frame_affix_top text-right");
        div.setInnerText(wfmStrings.credit());
        div.getStyle().setProperty("minWidth", "100px");
        th.appendChild(div);
        tr.appendChild(th);

        th = DOM.createTH();
        th.setClassName("stickerCell");
        div = DOM.createDiv();
        div.setClassName("frame_affix_top text-right");
        div.setInnerText(wfmStrings.balance());
        div.getStyle().setProperty("minWidth", "100px");
        th.appendChild(div);
        tr.appendChild(th);

        th = DOM.createTH();
        th.setClassName("stickerCell");
        div = DOM.createDiv();
        div.setClassName("frame_affix_top text-right");
        div.setInnerText(wfmStrings.amountBaseCurrency());
        div.getStyle().setProperty("minWidth", "100px");
        th.appendChild(div);
        tr.appendChild(th);

        tableHeader.appendChild(tr);
    }

    private void createItemRow(CrmAccountBalanceItem item) {

        Element tr = DOM.createTR();
        tr.setClassName("set_unit_2_row");

        Element td = DOM.createTD();
        td.setInnerHTML(DateUtils.format(item.getDate_nc().getNonConvertedDate()));
        tr.appendChild(td);

        if (includeSubAccount.getValue()) {
            td = DOM.createTD();
            td.setInnerHTML(item.getClientSupplierName());
            td.setClassName("text-left");
            tr.appendChild(td);
        }

        td = DOM.createTD();
        td.appendChild(getDetailedTransactionLink(item));
        td.setClassName("text-left");
        tr.appendChild(td);

        td = DOM.createTD();
        td.setInnerHTML(item.getReference());
        td.setClassName("text-left");
        tr.appendChild(td);

        td = DOM.createTD();
        if (item.getDebit() != null) {
            td.setTitle(item.getDebit().setScale(5, RoundingMode.HALF_UP).toString());
        }
        td.setInnerHTML(getFormattedAmount(item.getDebit()));
        td.addClassName("text-right");
        tr.appendChild(td);

        td = DOM.createTD();
        if (item.getCredit() != null) {
            td.setTitle(item.getCredit().setScale(5, RoundingMode.HALF_UP).toString());
        }
        td.setInnerHTML(getFormattedAmount(item.getCredit()));
        td.addClassName("text-right");
        tr.appendChild(td);

        td = DOM.createTD();
        if (item.getBalance() != null) {
            td.setTitle(item.getBalance().setScale(5, RoundingMode.HALF_UP).toString());
        }
        td.setInnerHTML(getFormattedAmount(item.getBalance()));
        td.addClassName("text-right");
        tr.appendChild(td);


        td = DOM.createTD();
        if (item.getAmountInBase() != null) {
            td.setTitle(item.getAmountInBase().setScale(5, RoundingMode.HALF_UP).toString());
        }
        td.setInnerHTML(getFormattedAmount(item.getAmountInBase()));
        td.addClassName("text-right");
        tr.appendChild(td);

        tableBody.appendChild(tr);
    }

    private void createCurrencyRow(Element element, String customerName, String baseCurrencyText, String currencyText) {
        Element tr = DOM.createTR();
        tr.addClassName("set_head_row");

        Element td1 = DOM.createTD();
        td1.setInnerHTML("<strong>" + customerName + "</strong");
        td1.setAttribute("colspan", includeSubAccount.getValue() ? "6" : "5");
        tr.appendChild(td1);

        Element td3 = DOM.createTD();
        td3.setInnerHTML("<strong>" + currencyText + "</strong");
        td3.addClassName("text-right");
        tr.appendChild(td3);

        Element td4 = DOM.createTD();
        td4.setInnerHTML("<strong>" + baseCurrencyText + "</strong");
        td4.addClassName("text-right");
        tr.appendChild(td4);
        element.appendChild(tr);
    }


    private Element getDetailedTransactionLink(final CrmAccountBalanceItem item) {

        String str = item.getTransactionLabel();
        EventListener eventListener = null;
        Element link = DOM.createAnchor();
        DOM.sinkEvents(link.cast(), Event.ONCLICK);
        link.setInnerHTML(str);

        if (Constants.INVOICE_TRANSACTION.equals(item.getTransactionType())) {
            if (CrmAccountItem.CUSTOMER.equals(crmAccountType)) {
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged((item.isCreditNote() ? "receivablecreditnote/" : "saleinvoice/") + item.getInvoiceID(), item.getNumber());
            } else {
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged((item.isCreditNote() ? "payablecreditnote/" : "purchaseinvoice/") + item.getInvoiceID(), item.getNumber());
            }
        } else if (Constants.INVOICEPAYMENT_TRANSACTION.equals(item.getTransactionType())) {
            if (item.getPaymentType() != null) {
                if (AccountingConstants.RECEIVABLE_PREPAYMENT.equals(item.getPaymentType())) {
                    eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment/" + item.getPaymentID() + "/prepayment", item.getNumber());
                } else {
                    if (item.getBatchPaymentId() != null) {
                        eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("receivepayment|summary/" + item.getBatchPaymentId() + "/" + item.getPaymentType(), item.getNumber());
                    } else {
                        eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment/" + item.getPaymentID() + (item.isRefund() ? "/cashRefund" : "/supplierCredit"), item.getNumber());
                    }
                }
            } else {
                Element label = DOM.createLabel();
                label.setInnerHTML(str);
                return label;
            }
        } else if (Constants.MANUAL_TRANSACTION.equals(item.getTransactionType())) {
            if (item.getNarration() != null) {
                eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("manual|summary/" + item.getManualJournalId(), item.getNumber());
            }
        } else if (Constants.CUSTOMER_SUPPLIER_PAYMENT_TRANSACTION.equals(item.getTransactionType())) {
            eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("transactionItemView|summary/" + item.getObjectID(), item.getNumber());
        } else if ("EdsBankTransferTransaction".equals(item.getTransactionType())) {
            eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("spendreceivemoney|summary/" + item.getItemId()
                    + "/" + (item.getNarration().startsWith("Cash Receipt") ? CASH_RECEIPT_STR : item.getNarration().startsWith("Cash Payment") ? CASH_PAYMENT_STR : item.getNarration().startsWith("Bank Receipt") ? SPEND_MONEY_STR : RECEIVE_MONEY_STR), item.getNarration().replace(":", " "));
        } else if (Constants.EXPENSE_TRANSACTION.equals(item.getTransactionType())) {
            eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("expenseReports|previewReport/" + item.getItemId() + "/EXPENSE_VIEW/ACCOUNTING", item.getNumber());
        } else if (Constants.EXPENSEPAYMENT_TRANSACTION.equals(item.getTransactionType())) {
            eventListener = event -> SinksContainerFactory.entryPoint.onHistoryChanged("expensepayment|summary/" + item.getItemId(), item.getNumber());
        } else {
            Element label = DOM.createLabel();
            label.setInnerHTML(str);
            return label;
        }
        if (eventListener != null)
            DOM.setEventListener(link.cast(), eventListener);

        return link;
    }

    private String getFormattedAmount(BigDecimal amount) {
        return getFormattedAmount(amount, false);
    }

    private String getFormattedAmount(BigDecimal amount, boolean showZero) {
        if (amount != null) {
            if (amount.compareTo(ZERO) == 0 || AccountingUtils.get().formatPrice(amount).equals("0.00")) {
                return showZero ? AccountingUtils.get().formatPrice(amount) : "";
            } else if (amount.compareTo(ZERO) > 0) {
                return AccountingUtils.get().formatPrice(amount);
            } else {
                return "(" + AccountingUtils.get().formatPrice(amount.abs()) + ")";
            }
        } else {
            return "";
        }
    }

    public MaterialLink getXlsVersion() {
        if (xlsVersion == null) {
            xlsVersion = new MaterialLink();
            MaterialIcon xlsIcon = new MaterialIcon();
            xlsIcon.setStylePrimaryName("ficon--file-excel hasicon--left");
            xlsVersion.add(xlsIcon);
            xlsVersion.setText(wfmStrings.excel());
            xlsVersion.addClickHandler(event -> {
                String excelUrl = CommandConstants.COMMON_URL + "/crmAccountBalanceExcelHandler";
                ListingFilterParameter filter = new ListingFilterParameter();
                filter.setCrmAccountId(crmAccountLookUp.getSelectedItemID());
                filter.setAccountType(crmAccountType);
                if (currencyListBox != null) {
                    filter.setCurrencyID(currencyListBox.getSelectedId());
                }
                filter.setStartDateNC(Utils.getStartDateNCForFilter(startDateValue.getDate()));
                filter.setEndDateNC(Utils.getEndDateNCForFilter(endDateValue.getDate()));
                filter.setShowSubAccountTransaction(includeSubAccount.getValue());
                HashMap<String, String> parametrs = filter.getRequestParams();
                Utils.sendPDFOrExcelRequest(exportPanel, excelUrl, parametrs, "_blank");
            });
        }
        return xlsVersion;
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

    public void pdfTool(MaterialDropDown mdp) {
        Integer defaultTemplateId = templateList.getDefaultTemplateID();
        if (templateList == null) {
            return;
        }
        if (templateList != null && templateList.getItems() != null) {
            templateList.getItems();
            for (SelectItem pdfItem : templateList.getItems()) {
                MaterialLink widgets = new MaterialLink(pdfItem.getName());
                widgets.addClickHandler(event -> generatePDF(exportPanel, pdfItem.getId(), true));
                mdp.add(widgets);
            }
        }

        MaterialLink widgets = new MaterialLink(wfmStrings.pdfVersion());
        widgets.addClickHandler(event -> generatePDF(exportPanel, defaultTemplateId, true));
        mdp.add(widgets);

    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        String pdfURL = CommandConstants.PDF_URL + "/crmAccountBalancePDFHandler";
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setCrmAccountId(crmAccountLookUp.getSelectedItemID());
        if (currencyListBox != null) {
            filter.setCurrencyID(currencyListBox.getSelectedId());
        }
        filter.setAccountType(crmAccountType);
        filter.setStartDateNC(Utils.getStartDateNCForFilter(startDateValue.getDate()));
        filter.setEndDateNC(Utils.getEndDateNCForFilter(endDateValue.getDate()));
        filter.setShowSubAccountTransaction(includeSubAccount.getValue());
        filter.setTemplateID(templateID);
        HashMap<String, String> parameters = filter.getRequestParams();
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    private void getPdfTemplates(Div wrapper) {
        AccountingService.App.get().getCrmAccountBalancePDFTemplates(new AsyncCallback<CustomFormItemPdfTemplateList>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(CustomFormItemPdfTemplateList result) {
                templateList = result;
                if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLING_CUSTOM_PDF_TEMPLATES) && result != null && result.getItems() != null && result.getItems().length != 0) {
                    MaterialDropDown mdp = new MaterialDropDown(pdfVersion);
                    mdp.setHover(true);
                    mdp.setHoverable(true);
                    pdfTool(mdp);
                    wrapper.add(mdp);
                } else {
                    pdfVersion.addClickHandler(event -> generatePDF(exportPanel, null, true));
                    wrapper.add(pdfVersion);
                }
            }
        });
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

        xlsVersion = getXlsVersion();
        xlsVersion.ensureDebugId("excel_button");

        pdfVersion = getPdfVersion();
        pdfVersion.ensureDebugId("pdf_button");
        showMenuContainer.add(xlsVersion);

        Div wrapper = new Div("java-wrap");
        showMenuContainer.add(wrapper);
        pdfVersion = getPdfVersion();
        wrapper.add(pdfVersion);
        pdfVersion.ensureDebugId("pdf_button");
        getPdfTemplates(wrapper);

        showMenuBar.add(showLink);

        Div div = new Div();
        new KpiToolTip(showMenuBar, wfmStrings.export(), Position.TOP);
        div.add(showMenuBar);
        GBoxItem exportItem = headerPanel.addGroupBoxItem(null, div);
    }

    private final String getCrmAccountType() {
        return this.crmAccountType;
    }

    public Date getFinancialYearStart() {
        return financialYearStart;
    }

    public Date getConversationDate() {
        return conversationDate;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public LinkedList<Date> getFinancialQuartiesList() {
        return financialQuartiesList;
    }

    private void initFilterPopup() {
        filterDialog = new KpiModal();
        filterDialog.setWidth(400);
        filterDialog.setCloseButton(true);
        filterDialog.setDismissible(false);

        MaterialPanel contentPanel = new MaterialPanel();

        crmAccountLookUp = new CrmAccountLookUp(crmAccountType, true);
        crmAccountLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> initReportData(crmAccountLookUp.getSelectedItemID()));
        contentPanel.add(new GBoxItem((CrmAccountItem.CUSTOMER.equals(crmAccountType) ? Property.get(Constants.CLIENT_LIST, wfmStrings.customer()) : wfmStrings.supplier()), crmAccountLookUp));

        includeSubAccount = new KpiCheckBox(accountingStrings.includeSubAccountTransactions());
        includeSubAccount.addValueChangeHandler(valueChangeEvent -> initReportData(crmAccountLookUp.getSelectedItemID()));
        GBoxItem includeSubAccountField = new GBoxItem(includeSubAccount);
        includeSubAccountField.setStyleNoBorder(true);
        includeSubAccountField.removeBoxItemLabel();
        contentPanel.add(includeSubAccountField);

        WfmButton2 resetButton = new WfmButton2(wfmStrings.reset());
        resetButton.addClickHandler(clickEvent -> {
            includeSubAccount.setValue(Boolean.FALSE);
        });

        WfmButton2 updateButton = new WfmButton2(wfmStrings.update());
        updateButton.addClickHandler(clickEvent -> {
            filterDialog.close();
            initReportData(crmAccountLookUp.getSelectedItemID());
        });

        filterDialog.add(contentPanel);
        filterDialog.addButton(resetButton);
        filterDialog.addButton(updateButton);
    }

}