package com.edatasite.workforce.gwt.accounting.client.ui.view.widgets;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.MultiCurrencyExchangeRateItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.ui.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 12/11/12
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class ExchangeRateTableWidget extends HTMLPanel implements AccountingConstants {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingUtils utils = AccountingUtils.get();

    public static final String CURRENCY_ID = "currencyId";

    private FlexTable content;

    private Integer monthNum;
    private Integer yearNum;
    private Integer currentDate;
    private Integer currentMonth;
    private Integer currentYear;
    private MultiCurrencyExchangeRateItem multiCurrencyExchangeRateItem;
    private LinkedHashMap<Integer, BigDecimal> currencyLastExchangeRateMap;
    private LinkedHashMap<Integer, LinkedHashMap<Integer, BigDecimal>> exchangeRateMap;
    private LinkedHashMap<Integer, LinkedHashMap<Integer, TextBox>> exchangeRateMapWidget;


    public ExchangeRateTableWidget(Integer currentYear, Integer currentMonth, Integer currentDate) {
        super("");
        this.currentYear = currentYear;
        this.currentMonth = currentMonth;
        this.currentDate = currentDate;

        initWidgets();
    }

    private void initWidgets() {
        // content
        content = new FlexTable();
        content.setStyleName("exchange-rate");
        content.setCellPadding(0);
        content.setCellSpacing(0);

        currencyLastExchangeRateMap = new LinkedHashMap<>();
        exchangeRateMap = new LinkedHashMap<>();
        exchangeRateMapWidget = new LinkedHashMap<>();
        add(content);
    }

    public void add(Widget widget) {
        add(widget, this.getElement());
    }

    public void fillData(MultiCurrencyExchangeRateItem multiCurrencyExchangeRateItem, Integer yearNum, Integer monthNum) {
        this.yearNum = yearNum;
        this.monthNum = monthNum;
        this.multiCurrencyExchangeRateItem = multiCurrencyExchangeRateItem;
        content.removeAllRows();
        content.clear();
        exchangeRateMap.clear();
        exchangeRateMapWidget.clear();
        fillZeroValueCurrencyMap(multiCurrencyExchangeRateItem);
        createTableHeader(multiCurrencyExchangeRateItem);
        createTableBody(multiCurrencyExchangeRateItem);
        createTableFooter(multiCurrencyExchangeRateItem);
    }

    private void fillZeroValueCurrencyMap(MultiCurrencyExchangeRateItem multiCurrencyExchangeRateItem) {
        currencyLastExchangeRateMap.clear();
        for (SelectItem curencyItem : multiCurrencyExchangeRateItem.getSubsidiariesMultiCurrencyList()) {
            currencyLastExchangeRateMap.put(curencyItem.getId(), ZERO);
        }
    }

    private void createTableHeader(MultiCurrencyExchangeRateItem multiCurrencyExchangeRateItem) {
        content.setHTML(0, 0, wfmStrings.date());
        content.getFlexCellFormatter().setStyleName(0, 0, "header");
        content.setHTML(0, 1, accountingStrings.homeCurrency() + "-" + multiCurrencyExchangeRateItem.getParentBaseCurrency().getName());
        content.getFlexCellFormatter().setStyleName(0, 1, "header");
        for (int i = 0; i < multiCurrencyExchangeRateItem.getSubsidiariesMultiCurrencyList().size(); i++) {
            content.setHTML(0, i + 2, multiCurrencyExchangeRateItem.getSubsidiariesMultiCurrencyList().get(i).getName());
            content.getFlexCellFormatter().setStyleName(0, i + 2, "header");
            content.getFlexCellFormatter().setHorizontalAlignment(0, i + 2, HasAlignment.ALIGN_CENTER);
        }
    }

    private void createTableBody(MultiCurrencyExchangeRateItem multiCurrencyExchangeRateItem) {
        int month_max_day = getDaysInMonth(yearNum, monthNum);
        int row_day = (currentYear == yearNum && currentMonth == monthNum ? currentDate : month_max_day);
        for (int i = 1; i <= row_day; i++) {
            content.setHTML(i, 0, String.valueOf(i));
            content.getFlexCellFormatter().setStyleName(i, 0, "date");
            content.setHTML(i, 1, "1 " + multiCurrencyExchangeRateItem.getParentBaseCurrency().getName() + " =");
            for (int j = 0; j < multiCurrencyExchangeRateItem.getSubsidiariesMultiCurrencyList().size(); j++) {
                content.setWidget(i, j + 2, createNewExchangeRateBox(multiCurrencyExchangeRateItem.getSubsidiariesMultiCurrencyList().get(j), i, multiCurrencyExchangeRateItem));
            }
        }
    }

    private TextBox createNewExchangeRateBox(SelectItem currencyItem, int day_of_month, MultiCurrencyExchangeRateItem multiCurrencyExchangeRateItem) {
        TextBox exchangeRateBox = new TextBox();
        exchangeRateBox.setTitle(currencyItem.getName());
        exchangeRateBox.setWidth("80px");
        if (multiCurrencyExchangeRateItem.getExhangeRateMap().containsKey(currencyItem.getId())
                && multiCurrencyExchangeRateItem.getExhangeRateMap().get(currencyItem.getId()).containsKey(day_of_month)) {
            exchangeRateBox.setText(utils.formatQty(multiCurrencyExchangeRateItem.getExhangeRateMap().get(currencyItem.getId()).get(day_of_month)));
            exchangeRateBox.setEnabled(false);
            currencyLastExchangeRateMap.put(currencyItem.getId(), multiCurrencyExchangeRateItem.getExhangeRateMap().get(currencyItem.getId()).get(day_of_month));
        } else {
            if (!currencyLastExchangeRateMap.get(currencyItem.getId()).equals(ZERO)) {/*currentYear == yearNum && currentMonth == monthNum && day_of_month == currentDate*/
                exchangeRateBox.setText(utils.formatQty(currencyLastExchangeRateMap.get(currencyItem.getId())));
                exchangeRateBox.setStyleName("x-form-invalid");
            } else {
                exchangeRateBox.setText(utils.formatQty(ZERO));
            }
            Validation.checkToFocusTextBox(exchangeRateBox, AccountingUtils.getQtyZero());
            validationExchangeRateBox(exchangeRateBox);
            if (!exchangeRateMapWidget.containsKey(currencyItem.getId())) {
                exchangeRateMapWidget.put(currencyItem.getId(), new LinkedHashMap<>());
            }
            exchangeRateMapWidget.get(currencyItem.getId()).put(day_of_month, exchangeRateBox);
        }
        return exchangeRateBox;
    }

    private void validationExchangeRateBox(TextBox exchangeRateBox) {
        Validation.addNumericKeyboardListener(exchangeRateBox, AccountingUtils.customQtyScale);
    }

    private void createTableFooter(MultiCurrencyExchangeRateItem multiCurrencyExchangeRateItem) {
        int month_max_day = getDaysInMonth(yearNum, monthNum);
        int row_day = (currentYear == yearNum && currentMonth == monthNum ? currentDate : month_max_day);
        content.setHTML(row_day + 1, 0, wfmStrings.date());
        content.getFlexCellFormatter().setStyleName(row_day + 1, 0, "header");
        content.setHTML(row_day + 1, 1, accountingStrings.homeCurrency() + "-" + multiCurrencyExchangeRateItem.getParentBaseCurrency().getName());
        content.getFlexCellFormatter().setStyleName(row_day + 1, 1, "header");
        for (int i = 0; i < multiCurrencyExchangeRateItem.getSubsidiariesMultiCurrencyList().size(); i++) {
            content.setHTML(row_day + 1, i + 2, multiCurrencyExchangeRateItem.getSubsidiariesMultiCurrencyList().get(i).getName());
            content.getFlexCellFormatter().setStyleName(row_day + 1, i + 2, "header");
            content.getFlexCellFormatter().setHorizontalAlignment(row_day + 1, i + 2, HasAlignment.ALIGN_CENTER);
        }
    }

    private int getDaysInMonth(int year, int month) {
        switch (month) {
            case 1:
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    return 29; // leap year
                } else {
                    return 28;
                }
            case 3:
                return 30;
            case 5:
                return 30;
            case 8:
                return 30;
            case 10:
                return 30;
            default:
                return 31;
        }
    }

    public LinkedHashMap<Integer, LinkedHashMap<Integer, BigDecimal>> getCurrencyExchangeRateData() {
        for (Integer currencyId : exchangeRateMapWidget.keySet()){
            LinkedHashMap<Integer, TextBox> dateWidgetMap = exchangeRateMapWidget.get(currencyId);
            List<Integer> day_of_MonthList = new ArrayList<>();
            for (Integer day_of_month : dateWidgetMap.keySet()) {
                TextBox currencyExchRateBox = dateWidgetMap.get(day_of_month);
                BigDecimal currentValue = utils.parseToBigDecimal(currencyExchRateBox.getText());
                if (BigDecimal.ZERO.compareTo(currentValue) != 0) {
                    currencyExchRateBox.setEnabled(false);
                    currencyExchRateBox.removeStyleName("x-form-invalid");
                    if (!exchangeRateMap.containsKey(currencyId)) {
                        exchangeRateMap.put(currencyId, new LinkedHashMap<>());
                    }
                    day_of_MonthList.add(day_of_month);
                    exchangeRateMap.get(currencyId).put(day_of_month, currentValue);
                }
            }
            for (Integer day_of_month : day_of_MonthList) {
                dateWidgetMap.remove(day_of_month);
            }
        }
        return exchangeRateMap;
    }

    public void clearExchangerRateMap() {
        exchangeRateMap.clear();
    }
}
