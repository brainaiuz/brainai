package com.edatasite.workforce.gwt.accounting.client;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.FinancialSettingsItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.DatesListEnum;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 23.03.2010
 * Time: 21:14:41
 * To change this template use File | Settings | File Templates.
 */

public class AccountingUtils implements AccountingConstants {

    static FinancialSettingsItem fsi = new FinancialSettingsItem();

    private static final String DATE = "date";
    private static final String SPLITTER = "_";

    public static Integer systemCalculationScale = getPriceScale();
    public static Integer customQtyScale = getQtyScale();
    public static Integer customUnitPriceScale = getUnitPriceScale();
    public static Integer customExRateScale = getExRateScale();
    public static Integer calculationScale = getPriceScale();
    public static Integer taxRateScale = getTaxRateScale();

    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingServiceAsync accountingService = AccountingService.App.get();
    private static NumberFormat qtyNumberFormat = NumberFormat.getFormat(",##0.00");
    private static NumberFormat unitPriceNumberFormat = NumberFormat.getFormat(",##0.0000");
    private static NumberFormat exRateNumberFormat = NumberFormat.getFormat(",##0.0000");
    private static NumberFormat priceFormat = NumberFormat.getFormat(",##0.00000");
    private static final NumberFormat defaultNumberFormat = Utils.getCalculationNumberFormat();
    private static final NumberFormat taxRateNumberFormat = NumberFormat.getFormat(",##0" + Utils.generateDecimalByScale(taxRateScale));
    public static NumberFormat discountNumberFormat = NumberFormat.getFormat(",##0" + Utils.generateDecimalByScale(getDiscountScale()));

    private static final boolean enableComission = Utils.hasGenericAccess(GenericSettingsEnum.INVENTORY_COMISSION_ENABLED);
    private static final boolean enableInvoiceCustomTypes = Utils.hasGenericAccess(GenericSettingsEnum.INVOICE_CUSTOM_TYPE_ENABLED);
    private static final boolean enableVariation = Utils.hasGenericAccess(GenericSettingsEnum.INVENTORY_VARIATION_ENABLED);
    private static final boolean enablePaymentPeriod = Utils.hasGenericAccess(GenericSettingsEnum.PAYMENT_PERIOD_ENABLED);
    private static NumberToWord numberToWordConverter;

    public static AccountingUtils instance;

    public static AccountingUtils get() {
        if (instance == null) {
            instance = new AccountingUtils();
            loadData();
        }
        return instance;
    }

    public BigDecimal parseToBigDecimal(String text) {
        try {
            if (text != null && !text.isEmpty()) {
                return BigDecimal.valueOf(Utils.universalParse(priceFormat, text));
            }
            return ZERO;
        } catch (NumberFormatException e) {
            return ZERO;
        }
    }

    public BigDecimal parseToBigDecimalWithValidate(String text) throws NumberFormatException {
        try {
            if (text != null && !text.isEmpty()) {
                return BigDecimal.valueOf(Utils.universalParse(priceFormat, text));
            }
            return ZERO;
        } catch (NumberFormatException e) {
            return ZERO;
        }
    }

    public BigDecimal parseToBigDecimal(String text, NumberFormat numberFormat) {
        try {
            if (text != null && !text.isEmpty()) {
                return BigDecimal.valueOf(Utils.universalParse(numberFormat, text));
            }
            return BigDecimal.ZERO;
        } catch (NumberFormatException e) {
            return ZERO;
        }
    }

    public String formatQty(BigDecimal bigDecimal) {
        return qtyNumberFormat.format(bigDecimal.setScale(customQtyScale, RoundingMode.HALF_UP).doubleValue());
    }

    public String formatUnitPrice(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            bigDecimal = BigDecimal.ZERO;
        }
        return unitPriceNumberFormat.format(bigDecimal.setScale(customUnitPriceScale, RoundingMode.HALF_UP).doubleValue());
    }

    public String formatExRate(BigDecimal bigDecimal) {
        return exRateNumberFormat.format(bigDecimal.setScale(customExRateScale, RoundingMode.HALF_UP).doubleValue());
    }

    public String formatPrice(BigDecimal bigDecimal) {
        if (bigDecimal != null) {
            return priceFormat.format(bigDecimal.setScale(calculationScale, RoundingMode.HALF_UP).doubleValue());
        }
        return "";
    }

    public String formatPrice(BigDecimal bigDecimal, Integer scale) {
        if (bigDecimal != null) {
            return priceFormat.format(bigDecimal.setScale(scale, RoundingMode.HALF_UP).doubleValue());
        }
        return "";
    }

    public String formatPriceForNegative(BigDecimal bigDecimal) {
        String result;
        if (bigDecimal.compareTo(BigDecimal.ZERO) < 0) {
            result = "(" + priceFormat.format(bigDecimal.abs().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue()) + ")";
        } else {
            result = priceFormat.format(bigDecimal.setScale(calculationScale, RoundingMode.HALF_UP).doubleValue());
        }
        return result;
    }

    public String format(BigDecimal bigDecimal) {
        return defaultNumberFormat.format(bigDecimal.setScale(calculationScale, RoundingMode.HALF_UP).doubleValue());
    }

    public String format(BigDecimal bigDecimal, Integer scale) {
        return defaultNumberFormat.format(bigDecimal.setScale(scale, RoundingMode.HALF_UP).doubleValue());
    }

    public String format(Double value, int fraction) {
        String fPart = "";
        for (int i = 0; i < fraction; i++) fPart += "0";
        return NumberFormat.getFormat(",##0" + (!fPart.isEmpty() ? "." + fPart : "")).format(value);
    }

    public String formatTaxRate(BigDecimal value) {
        return taxRateNumberFormat.format(value.setScale(taxRateScale, RoundingMode.HALF_UP));
    }

    public String formatDiscount(BigDecimal value) {
        return discountNumberFormat.format(value.setScale(getDiscountScale(), RoundingMode.HALF_UP));
    }

    public String formatQty(Double value) {
        return qtyNumberFormat.format(value);
    }

    public String formatUnitPrice(Double value) {
        return unitPriceNumberFormat.format(value);
    }

    public String formatExRate(Double value) {
        return exRateNumberFormat.format(value);
    }

    public String formatPrice(Double value) {
        return priceFormat.format(value);
    }

    public String format(Double value) {
        return defaultNumberFormat.format(value);
    }

    public static String getZero() {
        return priceFormat.format(0);
    }

    public static String getDefaultZero() {
        return defaultNumberFormat.format(0);
    }

    public static String getQtyZero() {
        return qtyNumberFormat.format(0);
    }

    public static String getDiscountZero() {
        return discountNumberFormat.format(0);
    }

    public static String getUnitPriceZero() {
        return unitPriceNumberFormat.format(0);
    }

    public static void loadData() {

        qtyNumberFormat = getNumberFormat(customQtyScale);
        unitPriceNumberFormat = getNumberFormat(customUnitPriceScale);
        exRateNumberFormat = getNumberFormat(customExRateScale);
        priceFormat = getNumberFormat(calculationScale);

        accountingService.getCompanyFinancialSettings(new AsyncCallback<FinancialSettingsItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(FinancialSettingsItem result) {
                fsi = result;
                setNumberToWordConverter(result.getLocaleCode());
            }
        });
    }

    private static NumberFormat getNumberFormat(Integer productPrice) {
        if (productPrice == 0) {
            return NumberFormat.getFormat(",##0");
        } else {
            String s = ".";
            for (int i = 0; i < productPrice; i++) {
                s = s.concat("0");
            }
            return NumberFormat.getFormat(",##0" + s);
        }
    }

    public static String getPriceValue(String text) {
        BigDecimal price = parsePriceToBigDecimal(getQuantityAsString(text));
        return formatCustomPrice(price);
    }

    private static String getQuantityAsString(String text) {
        return text.equals(wfmStrings.notAvailable()) || text.equals("") ? accountingStrings.digit0() : (text.indexOf(':') == -1 ? text : getHourValue(text));
    }

    private static String getHourValue(String text) {
        String[] time = text.split(":");
        return (Double.parseDouble(time[0]) + Double.parseDouble(time[1]) / 60) + "";
    }

    public static String formatCustomPrice(BigDecimal price) {
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_FORMAT_WITH_CALCULATION_SCALE)) {
            return get().format(price);
        }
        NumberFormat unitPriceNumberFormat = NumberFormat.getFormat(",##0.00##");
        return unitPriceNumberFormat.format(price.stripTrailingZeros().doubleValue());
    }

    public static String formatCustomPrice(BigDecimal price, Integer scale) {
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_FORMAT_WITH_CALCULATION_SCALE)) {
            return get().format(price);
        }
        if (scale != null) {
            return Utils.getCalculationNumberFormatWithCustomScale(scale).format(price.setScale(scale, RoundingMode.HALF_UP).doubleValue());
        }
        NumberFormat unitPriceNumberFormat = NumberFormat.getFormat(",##0.00##");
        return unitPriceNumberFormat.format(price.stripTrailingZeros().doubleValue());
    }

    public static BigDecimal parsePriceToBigDecimal(String text) {
        try {
            if (text != null && !text.isEmpty()) {
                NumberFormat priceFormat = NumberFormat.getFormat(",##0.00000");
                return BigDecimal.valueOf(Utils.universalParse(priceFormat, text));
            }
            return ZERO;
        } catch (NumberFormatException e) {
            return ZERO;
        }
    }

    public static void setNumberToWordConverter(String localeCode) {
        switch (localeCode) {
            case "en":
                numberToWordConverter = new NumberToWord_en();
                break;
            case "es":
                numberToWordConverter = new NumberToWord_es();
                break;
            case "nl":
                numberToWordConverter = new NumberToWord_nl();
                break;
            case "no":
                numberToWordConverter = new NumberToWord_no();
                break;
            case "ru":
                numberToWordConverter = new NumberToWord_ru();
                break;
            case "pt":
                numberToWordConverter = new NumberToWord_pt();
                break;
        }
    }

    public String numberToWord(BigDecimal bigDecimal) {
        return numberToWordConverter.toWord(bigDecimal);
    }

    public boolean enableComission() {
        return enableComission;
    }

    public boolean enableVariation() {
        return enableVariation;
    }

    public boolean enableInvoiceCustomTypes() {
        return enableInvoiceCustomTypes;
    }

    public boolean enablePaymentPeriod() {
        return enablePaymentPeriod;
    }

    public static int getQtyScale() {
        if (Utils.getAccountingCustomQtyScale() != null) {
            return Utils.getAccountingCustomQtyScale();
        }
        return 2;
    }

    public static int getUnitPriceScale() {
        if (Utils.getAccountingCustomPriceScale() != null) {
            return Utils.getAccountingCustomPriceScale();
        }
        if (Utils.getAccountingCalculationScale() != null) {
            return Utils.getAccountingCalculationScale();
        }
        return 2;
    }

    public static int getExRateScale() {
        if (Utils.getAccountingCustomExRateScale() != null) {
            return Utils.getAccountingCustomExRateScale();
        }
        return 4;
    }

    public static int getPriceScale() {
        if (Utils.getAccountingCalculationScale() != null) {
            return Utils.getAccountingCalculationScale();
        }
        return 2;
    }

    public static int getTaxRateScale() {
        if (Utils.getAccountingTaxRateScale() != null) {
            return Utils.getAccountingTaxRateScale();
        }
        return 2;
    }

    public static int getDiscountScale() {
        return Utils.getAccountingDiscountScale();
    }

    public static Integer getBaseCurrencyId() {

        if (fsi != null) {
            return fsi.getCurrencyId();
        }
        return null;
    }

    public static String getBaseCurrencySymbol() {
        if (fsi != null) {
            if (fsi.getCurrencySymbol() != null)
                return fsi.getCurrencySymbol();
            else if (fsi.getCurrencyName() != null)
                return fsi.getCurrencyName();
        }
        return Utils.getCurrencyCODEbyHOST();
    }

    public static String getBaseCurrencyCode() {
        if (fsi != null) {
            if (fsi.getCurrencyName() != null)
                return fsi.getCurrencyName();
        }
        return Utils.getCurrencyCODEbyHOST();
    }

    public static Date getFinancialYearEnd() {
        if (fsi != null) {
            if (fsi.getFinYearEnd() != null) {
                return fsi.getFinYearEnd().getNonConvertedDate();
            }
            return null;
        }
        return null;
    }

    public static Date getConversionDate() {
        if (fsi != null) {
            if (fsi.getConversionDate() != null) {
                return fsi.getConversionDate();
            }
            return null;
        }
        return null;
    }

    public static boolean isEnablePostDatedTransactions() {
        if (fsi != null) {
            return fsi.isEnablePostDatedTransactions();
        }
        return false;
    }

    public static boolean isEnableDepreciationDatePeriod() {
        if (fsi != null) {
            return fsi.getEnableDepreciationDatePeriod();
        }
        return false;
    }

    public static boolean isEnableBatchTrackingItems() {
        if (fsi != null) {
            return fsi.getEnableBatchTrackingItems();
        }
        return false;
    }

    public static boolean isMandatoryProjectForExpenseClaims() {
        if (fsi != null) {
            return fsi.getMandatoryProjectForExpenseClaims();
        }
        return false;
    }

    public static boolean isMandatoryProjectForPurchaseOrders() {
        if (fsi != null) {
            return fsi.getMandatoryProjectForPurchaseOrders();
        }
        return false;
    }

    public static boolean isEnableLandedCost() {
        if (fsi != null) {
            return fsi.getEnableLandedCost();
        }
        return false;
    }

    public static boolean isEnableAccountingDepartmentRelation() {
        if (fsi != null) {
            return fsi.getEnableAccountingDepartmentRelation();
        }
        return false;
    }


    public static boolean validateVoidDate(Date voidDate, Date paymentDate) {
        String voidString = voidDate.toLocaleString();
        String paymentString = paymentDate.toLocaleString();
        // I don't have anohter option, that's why I used str; java packeges are not working in GWT UI
        boolean expetioanlCase = false;
        if (voidString != null && paymentString != null && voidString.length() >= 10 && paymentString.length() >= 10) {
            String str1 = voidString.substring(0, 10);
            String str2 = paymentString.substring(0, 10);
            expetioanlCase = str1.equals(str2);
        }
        if (!expetioanlCase) {
            if (!(voidDate.compareTo(paymentDate) >= 0)) {
                Info.show(accountingStrings.voidDateShouldBeGreaterThanTransactionDate(), Info.Type.WARNING);
                return false;
            }
        }

        if (!(new Date().compareTo(voidDate) >= 0)) {
            Info.show(accountingStrings.voidDateShouldNotBeGreaterThanToday(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public static boolean validateExpanseVoidDate(Date voidDate, Date paymentDate) {
        if (!(voidDate.compareTo(paymentDate) >= 0)) {
            Info.show(accountingStrings.voidDateShouldBeGreaterThanTransactionDate(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public static String getNumberingFormat(TextBox prefixBox, TextBox cell1Box, TextBox cell2Box, TextBox cell3Box, TextBox cell4Box, Boolean dateBox) {
        String numberingFormat = "";
        if (!"".equals(prefixBox.getText())) {
            numberingFormat += prefixBox.getText() + SPLITTER;
        }
        numberingFormat += getNumCellValue(cell1Box);
        numberingFormat += getNumCellValue(cell2Box);
        numberingFormat += getNumCellValue(cell3Box);
        numberingFormat += getNumCellValue(cell4Box);
        if (dateBox != null && dateBox) {
            numberingFormat += "-" + DATE;
        }

        return numberingFormat;
    }

    public static void drawExampleNumber(TextBox cell1Box, TextBox cell2Box,
                                         TextBox cell3Box, TextBox cell4Box, Boolean dateBox, TextBox exampleBox, DateTimeFormat dateFormat) {
        String example = "";
        example += AccountingUtils.getNumCellValue(cell1Box);
        example += AccountingUtils.getNumCellValue(cell2Box);
        example += AccountingUtils.getNumCellValue(cell3Box);
        example += AccountingUtils.getNumCellValue(cell4Box);

        if (dateBox != null && dateBox) {
            example += "-" + dateFormat.format(new Date());
        }
//        dateBox.getOffLabel().setMarginLeft(10);
        exampleBox.setText(example);
    }

    public static String getNumCellValue(TextBox txtBox) {
        if (txtBox.getText().equals("")) {
            return "0";
        } else {
            return txtBox.getText();
        }
    }

    public static SelectItem[] getDatesListItems() {
        ArrayList<SelectItem> datesListItems = new ArrayList<>();

        datesListItems.add(new SelectItem(DatesListEnum.All.getId(), wfmStrings.all()));
        datesListItems.add(new SelectItem(DatesListEnum.Today.getId(), wfmStrings.today()));
        datesListItems.add(new SelectItem(DatesListEnum.ThisWeek.getId(), wfmStrings.thisWeek()));
        datesListItems.add(new SelectItem(DatesListEnum.ThisWeekToDate.getId(), wfmStrings.thisWeekToDate()));
        datesListItems.add(new SelectItem(DatesListEnum.ThisMonth.getId(), wfmStrings.thisMonth()));
        datesListItems.add(new SelectItem(DatesListEnum.ThisMonthToDate.getId(), wfmStrings.thisMonthToDate()));
        datesListItems.add(new SelectItem(DatesListEnum.ThisFiscalQuarter.getId(), wfmStrings.thisFiscalQuarter()));
        datesListItems.add(new SelectItem(DatesListEnum.ThisFiscalQuarterToDate.getId(), wfmStrings.thisFiscalQuarterToDate()));
        datesListItems.add(new SelectItem(DatesListEnum.ThisFiscalYear.getId(), wfmStrings.thisFiscalYear()));
        datesListItems.add(new SelectItem(DatesListEnum.ThisFiscalYearToDate.getId(), wfmStrings.thisFiscalYearToDate()));
        datesListItems.add(new SelectItem(DatesListEnum.Yesterday.getId(), wfmStrings.yesterday()));
        datesListItems.add(new SelectItem(DatesListEnum.LastWeek.getId(), wfmStrings.lastWeek()));
        datesListItems.add(new SelectItem(DatesListEnum.LastWeekToDate.getId(), wfmStrings.lastWeekToDate()));
        datesListItems.add(new SelectItem(DatesListEnum.LastMonth.getId(), wfmStrings.lastMonth()));
        datesListItems.add(new SelectItem(DatesListEnum.LastMonthToDate.getId(), wfmStrings.lastMonthToDate()));
        datesListItems.add(new SelectItem(DatesListEnum.LastFiscalQuarter.getId(), wfmStrings.lastFiscalQuarter()));
        datesListItems.add(new SelectItem(DatesListEnum.LastFiscalQuarterToDate.getId(), wfmStrings.lastFiscalQuarterToDate()));
        datesListItems.add(new SelectItem(DatesListEnum.LastFiscalYear.getId(), wfmStrings.lastFiscalYear()));
        datesListItems.add(new SelectItem(DatesListEnum.LastFiscalYearToDate.getId(), wfmStrings.lastFiscalYearToDate()));
        datesListItems.add(new SelectItem(DatesListEnum.NextWeek.getId(), wfmStrings.nextWeek()));
        datesListItems.add(new SelectItem(DatesListEnum.Next4Weeks.getId(), wfmStrings.next4Week()));
        datesListItems.add(new SelectItem(DatesListEnum.NextMonth.getId(), wfmStrings.nextMonth()));
        datesListItems.add(new SelectItem(DatesListEnum.NextFiscalQuarter.getId(), wfmStrings.nextFiscalQuarter()));
        datesListItems.add(new SelectItem(DatesListEnum.NextFiscalYear.getId(), wfmStrings.nextFiscalYear()));
        datesListItems.add(new SelectItem(DatesListEnum.Custom.getId(), wfmStrings.custom()));

        return datesListItems.toArray(new SelectItem[]{});
    }

    public static void setFromAndToDates(Date currentDate, Date financialYearStart, Date conversationDate, DataListBox datesValues, LinkedList<Date> financialQuartiesList,
                                         DatePicker startDateBox, DatePicker endDateBox) {
        if (datesValues.getSelectedId() == null) {
            return;
        }
        LoadingPanel.loading(true);
        Date from = new Date(), to = new Date();
        switch (DatesListEnum.getEnumById(datesValues.getSelectedId())) {
            case All: {
                from = conversationDate;
                to = currentDate;
                break;
            }
            case Today: {
                break;
            }
            case ThisWeek: {
                from = DateUtil.addDays(DateUtil.getWeekFirstDay(from), 1);
                to = DateUtil.addDays(DateUtil.getWeekLastDay(to), 1);
                break;
            }
            case ThisWeekToDate: {
                from = DateUtil.addDays(DateUtil.getWeekFirstDay(from), 1);
                break;
            }
            case ThisMonth: {
                from = DateUtil.getMonthFirstDay(from);
                to = DateUtil.getMonthLastDate(to);
                break;
            }
            case ThisMonthToDate: {
                from = DateUtil.getMonthFirstDay(from);
                break;
            }
            case ThisFiscalQuarter: {
                from = financialQuartiesList.get(2);
                to = financialQuartiesList.get(3);
                break;
            }
            case ThisFiscalQuarterToDate: {
                from = financialQuartiesList.get(2);
                to = currentDate;
                break;
            }
            case ThisFiscalYear: {
                from = financialYearStart;
                to = DateUtil.addDays(DateUtil.addYears(financialYearStart, 1), -1);
                break;
            }
            case ThisFiscalYearToDate: {
                from = financialYearStart;
                break;
            }
            case Yesterday: {
                from = DateUtil.addDays(from, -1);
                to = DateUtil.addDays(to, -1);
                break;
            }
            case LastWeek: {
                from = DateUtil.addDays(DateUtil.getWeekFirstDay(DateUtil.addDays(from, -7)), 1);
                to = DateUtil.addDays(DateUtil.getWeekLastDay(DateUtil.addDays(to, -7)), 1);
                break;
            }
            case LastWeekToDate: {
                from = DateUtil.addDays(DateUtil.getWeekFirstDay(DateUtil.addDays(from, -8)), 1);
                to = DateUtil.addDays(DateUtil.getWeekLastDay(DateUtil.addDays(to, -7)), 1);
                to = DateUtil.addDays(to, -((to.getDay() == 0 ? 7 : to.getDay()) - (currentDate.getDay() == 0 ? 7 : currentDate.getDay())));
                break;
            }
            case LastMonth: {
                from = DateUtil.getMonthFirstDay(DateUtil.addMonths(from, -1));
                to = DateUtil.getMonthLastDate(DateUtil.addMonths(to, -1));
                break;
            }
            case LastMonthToDate: {
                from = DateUtil.getMonthFirstDay(DateUtil.addMonths(from, -1));
                to = DateUtil.getMonthLastDate(DateUtil.addMonths(to, -1));
                if (DateUtil.getDateInMonth(to.getYear(), to.getMonth()) < currentDate.getDate()) {
                    to.setDate(DateUtil.getDateInMonth(to.getYear(), to.getMonth()));
                } else {
                    to.setDate(currentDate.getDate());
                }
                break;
            }
            case LastFiscalQuarter: {
                from = financialQuartiesList.get(0);
                to = financialQuartiesList.get(1);
                break;
            }
            case LastFiscalQuarterToDate: {
                from = financialQuartiesList.get(0);
                to = new Date(financialQuartiesList.get(1).getYear(), financialQuartiesList.get(1).getMonth(), financialQuartiesList.get(1).getDate(), financialQuartiesList.get(1).getHours(), financialQuartiesList.get(1).getMinutes(), financialQuartiesList.get(1).getSeconds());
                if (DateUtil.getDateInMonth(to.getYear(), to.getMonth()) < currentDate.getDate()) {
                    to.setDate(DateUtil.getDateInMonth(to.getYear(), to.getMonth()));
                } else {
                    to.setDate(currentDate.getDate());
                }
                break;
            }
            case LastFiscalYear: {
                from = DateUtil.addYears(financialYearStart, -1);
                to = DateUtil.addDays(financialYearStart, -1);
                break;
            }
            case LastFiscalYearToDate: {
                from = DateUtil.addYears(financialYearStart, -1);
                to = DateUtil.addDays(financialYearStart, -1);
                to.setMonth(currentDate.getMonth());
                if (DateUtil.getDateInMonth(to.getYear(), to.getMonth()) < currentDate.getDate()) {
                    to.setDate(DateUtil.getDateInMonth(to.getYear(), to.getMonth()));
                } else {
                    to.setDate(currentDate.getDate());
                }
                break;
            }
            case NextWeek: {
                from = DateUtil.addDays(DateUtil.getWeekFirstDay(DateUtil.addDays(from, 7)), 1);
                to = DateUtil.addDays(DateUtil.getWeekLastDay(DateUtil.addDays(to, 7)), 1);
                break;
            }
            case Next4Weeks: {
                from = DateUtil.addDays(DateUtil.getWeekFirstDay(DateUtil.addDays(from, 7)), 1);
                to = DateUtil.addDays(DateUtil.getWeekLastDay(DateUtil.addDays(to, 28)), 1);
                break;
            }
            case NextMonth: {
                from = DateUtil.getMonthFirstDay(DateUtil.addMonths(from, 1));
                to = DateUtil.getMonthLastDate(DateUtil.addMonths(to, 1));
                break;
            }
            case NextFiscalQuarter: {
                from = financialQuartiesList.get(4);
                to = financialQuartiesList.get(5);
                break;
            }
            case NextFiscalYear: {
                from = DateUtil.addYears(financialYearStart, 1);
                to = DateUtil.addDays(DateUtil.addYears(financialYearStart, 2), -1);
                break;
            }
            case Custom: {
                break;
            }
        }
        startDateBox.setDate(from);
        endDateBox.setDate(to);
        LoadingPanel.loading(false);
    }

    public static SelectItem[] getTaxCalcTypes() {
        return new SelectItem[]{
                new SelectItem(NO_TAX_CALCULATION, wfmStrings.noTax()),
                new SelectItem(TAX_CALCULATION_INCLUSIVE, wfmStrings.taxInclusive()),
                new SelectItem(TAX_CALCULATION_EXCLUSIVE, wfmStrings.taxExclusive())
        };
    }

    public static SelectItem getTaxCalcType(int type) {
        SelectItem[] items = getTaxCalcTypes();
        for (SelectItem item : items) {
            if (item.getId().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public static int getFractionLength(Double number) {
        if (number != null) {
            String[] fractions = number.toString().split("[.]");
            return fractions.length > 1 ? fractions[1].length() : 0;
        }
        return 0;
    }
}
