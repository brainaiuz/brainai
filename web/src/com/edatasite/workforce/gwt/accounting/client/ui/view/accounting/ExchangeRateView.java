package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ExchangeRateItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.MultiCurrencyExchangeRateItem;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.ExchangeRateTableWidget;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 09/11/12
 * Time: 19:40
 * To change this template use File | Settings | File Templates.
 */


public class ExchangeRateView extends CustomForm implements Colapse {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM, yyyy");

    private static final String[] monthNames = new String[]{wfmStrings.january(), wfmStrings.february(),
            wfmStrings.march(), wfmStrings.april(), wfmStrings.may(),
            wfmStrings.june(), wfmStrings.july(), wfmStrings.august(),
            wfmStrings.september(), wfmStrings.october(), wfmStrings.november(),
            wfmStrings.december()};

    private DataListBox yearListBox;
    private DataListBox monthListBox;
    private HTML dateFormatHTML;
    private FlexTable topPanel;
    private ExchangeRateTableWidget exchangeRateTable;

    private Integer yearNum;
    private Integer monthNum;
    private Integer currentDate;
    private Integer currentMonth;
    private Integer currentYear;
    private Integer companySignUpMonth;
    private Integer companySignUpYear;



    public ExchangeRateView(){
        super("exchangeRateView", wfmStrings.exchangeRate());
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> saveCurrenciesExchangeRate());

        addButton(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, event -> {
            yearNum = currentYear;
            yearListBox.setSelected(currentYear);
            monthListBox.setItems(getMonthListItems(companySignUpMonth, companySignUpYear));
            monthNum = currentMonth;
            monthListBox.setSelected(monthNum);
            getMultiCurrencyExchangeRate(yearNum, monthNum, false);

        });
    }

    private void saveCurrenciesExchangeRate() {
        LoadingPanel.loading(true);

        ExchangeRateItem exchangeRateItem = new ExchangeRateItem();
        exchangeRateItem.setYearNum(yearNum);
        exchangeRateItem.setMonthNum(monthNum);
        exchangeRateItem.setCurrencyExchangeRateData(exchangeRateTable.getCurrencyExchangeRateData());

        AccountingService.App.get().saveCurrenciesExchangeRate(exchangeRateItem, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.exchangeRate()), Info.Type.INFO);
                exchangeRateTable.clearExchangerRateMap();
            }
        });
    }

    @Override
    protected void getDataToFillFields() {
        getMultiCurrencyExchangeRate(currentYear, currentMonth,true);
    }

    private void getMultiCurrencyExchangeRate(Integer yearNum, Integer monthNum, final boolean first) {
        this.yearNum = yearNum;
        this.monthNum = monthNum;
        changeNewDateHtml(yearNum,monthNum);
        LoadingPanel.loading(true);
        AccountingService.App.get().getMultiCurrencyExchangeRate(yearNum, monthNum, new AsyncCallback<MultiCurrencyExchangeRateItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(MultiCurrencyExchangeRateItem multiCurrencyExchangeRateItem) {
                LoadingPanel.loading(false);
                fillData(multiCurrencyExchangeRateItem, first);
            }
        });
    }

    private void changeNewDateHtml(Integer yearNum, Integer monthNum) {
        dateFormatHTML.setHTML(dateFormat.format(new Date(yearNum, monthNum, 1)));
    }

    private void fillData(MultiCurrencyExchangeRateItem multiCurrencyExchangeRateItem, boolean first) {
        companySignUpYear = multiCurrencyExchangeRateItem.getCompanySignUpYear();
        companySignUpMonth = multiCurrencyExchangeRateItem.getCompanySignUpMonth();
        exchangeRateTable.fillData(multiCurrencyExchangeRateItem, yearNum, monthNum);
        monthListBox.setItems(getMonthListItems(companySignUpMonth, companySignUpYear));
        if (first) {
            yearListBox.setItems(getYearListItems(companySignUpYear));
            yearListBox.setSelected(currentYear);
            monthListBox.setSelected(currentMonth);
        }
    }

    private SelectItem[] getMonthListItems(Integer companySignUpMonth, Integer companySignUpYear) {
        SelectItem[] monthItems = null;
        if (currentYear == yearNum && yearNum != companySignUpYear) {
            monthItems = new SelectItem[currentMonth + 1];
            for (int i = 0; i <= currentMonth; i++) {
                monthItems[i] = new SelectItem(i, monthNames[i]);
            }
            changeMonthNum(0, currentMonth);
        } else if (yearNum == companySignUpYear && yearNum == currentYear) {
            int j = 0;
            monthItems = new SelectItem[12 -companySignUpMonth];
            for (int i = companySignUpMonth; i < 12; i++) {
                monthItems[j++] = new SelectItem(i, monthNames[i]);
            }
            changeMonthNum(companySignUpMonth, currentMonth);
        } else if (yearNum == companySignUpYear) {
            monthItems = new SelectItem[12 - companySignUpMonth];
            int j = 0;
            for (int i = companySignUpMonth; i < 12; i++) {
                monthItems[j++] = new SelectItem(i, monthNames[i]);
            }
            changeMonthNum(companySignUpMonth, 11);
        } else {
            monthItems = new SelectItem[12];
            for (int i = 0; i < 12; i++) {
                monthItems[i] = new SelectItem(i, monthNames[i]);
            }
            changeMonthNum(0, 11);
        }
        return monthItems;
    }

    private void changeMonthNum(int monthFrom, int monthTo) {
        if (!(monthFrom <= monthNum && monthNum <= monthTo)) {
            monthNum = monthTo;
        }
    }

    private SelectItem[] getYearListItems(Integer companySignUpYear) {
        SelectItem[] yearItems = new SelectItem[currentYear - companySignUpYear + 1];
        int i = 0;
        for (int year = companySignUpYear; year <= currentYear; year++) {
            yearItems[i++] = new SelectItem(year, String.valueOf(1900 + year));
        }
        return yearItems;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EXCHANGE_RATE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initViewWidgets();
        return null;
    }

    private void initViewWidgets() {
        // Year list
        yearListBox = new DataListBox();
        yearListBox.addStyleName(Constants.DEFAULT_WIDTH);
        yearListBox.setWithoutNullLabel(true);
        // Month list
        monthListBox = new DataListBox();
        monthListBox.addStyleName(Constants.DEFAULT_WIDTH);
        monthListBox.setWithoutNullLabel(true);
        // Select date
        dateFormatHTML = new HTML("");

        topPanel = new FlexTable();
//        topPanel.setHeight("35px");
        topPanel.setWidth("100%");

        FlexTable topTableWidget = new FlexTable();
        topTableWidget.setCellSpacing(10);
        topTableWidget.setHTML(0, 0, "&nbsp;");
        topTableWidget.setHTML(0, 1, wfmStrings.month());
        topTableWidget.getFlexCellFormatter().setHorizontalAlignment(0, 1, HasAlignment.ALIGN_LEFT);
        topTableWidget.getFlexCellFormatter().setStyleName(0, 1, "boldCustomTitle");
        topTableWidget.getFlexCellFormatter().getElement(0, 1).getStyle().setFontSize(13, Style.Unit.PX);
        topTableWidget.setWidget(0, 2, monthListBox);
        topTableWidget.getFlexCellFormatter().setHorizontalAlignment(0, 2, HasAlignment.ALIGN_LEFT);
        topTableWidget.setHTML(0, 3, wfmStrings.year());
        topTableWidget.getFlexCellFormatter().setHorizontalAlignment(0, 3, HasAlignment.ALIGN_LEFT);
        topTableWidget.getFlexCellFormatter().setStyleName(0, 3, "boldCustomTitle");
        topTableWidget.getFlexCellFormatter().getElement(0, 3).getStyle().setFontSize(13, Style.Unit.PX);
        topTableWidget.setWidget(0, 4, yearListBox);
        topTableWidget.getFlexCellFormatter().setHorizontalAlignment(0, 4, HasAlignment.ALIGN_LEFT);
        topTableWidget.setWidget(0, 5, dateFormatHTML);
        topTableWidget.getFlexCellFormatter().setStyleName(0, 5, "boldCustomTitle");
        topTableWidget.getFlexCellFormatter().getElement(0, 5).getStyle().setFontSize(13, Style.Unit.PX);

        topPanel.setWidget(0, 0, topTableWidget);
        topPanel.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasAlignment.ALIGN_LEFT);
        topPanel.getFlexCellFormatter().setVerticalAlignment(0, 0, HasAlignment.ALIGN_MIDDLE);


        // exchange rate table
        Date date = new Date();
        currentYear = date.getYear();
        currentMonth = date.getMonth();
        currentDate = date.getDate();

        exchangeRateTable = new ExchangeRateTableWidget(currentYear, currentMonth, currentDate);

        addWidgetsToForms();
        addWidgetEventListeners();
        show();
    }

    private void addWidgetEventListeners() {

        yearListBox.addValueChangeHandler(changeEvent -> {
            if (yearListBox.getSelectedId() != null) {
                yearNum = yearListBox.getSelectedId();
                monthListBox.setItems(getMonthListItems(companySignUpMonth, companySignUpYear));
                monthListBox.setSelected(monthNum);
                getMultiCurrencyExchangeRate(yearNum, monthNum, false);
            }
        });

        monthListBox.addValueChangeHandler(changeEvent -> {
            if (monthListBox.getSelectedId() != null) {
                monthNum = monthListBox.getSelectedId();
                getMultiCurrencyExchangeRate(yearNum, monthNum, false);
            }
        });
    }

    private void addWidgetsToForms() {
        // 1
        addTitleField(ExchangeRate.CONSOLIDATION_EXCHANGE_RATE, accountingStrings.consolidationExchangeRates());
        // 1.1
        addField(ExchangeRate.CONSOLIDATION_MULTI_CURRENCY_TOP_PANEL, topPanel);
        addField(ExchangeRate.CONSOLIDATION_MULTI_CURRENCY, exchangeRateTable);
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
