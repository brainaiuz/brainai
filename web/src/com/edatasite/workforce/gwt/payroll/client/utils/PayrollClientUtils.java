package com.edatasite.workforce.gwt.payroll.client.utils;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * User: Ula
 * Date: Apr 16, 2010
 * Time: 5:15:15 PM
 */
public class PayrollClientUtils {

    private static final NumberFormat numberFormat = Utils.getNumberFormat();
    private static final NumberFormat totalNumberFormat = Utils.getCalculationNumberFormat();

    public static SelectItem[] getPayFrequencies(boolean isArabic) {
        final SelectItem[] frequencies = new SelectItem[isArabic ? 4 : Frequency.values().length - 1];
        int i = 0;
        for (Frequency frequency : Frequency.values()) {
            if (isArabic) {
                if (frequency.isForAllCountry()) {
                    frequencies[i] = new SelectItem(frequency.getId(), Frequency.returnCompatibleWord(frequency.getName()));
                    i++;
                }
            } else {
                if (Frequency.DAILY.equals(frequency)) {
                    //do not include Daily pay frequency
                    continue;
                }
                frequencies[i] = new SelectItem(frequency.getId(), Frequency.returnCompatibleWord(frequency.getName()));
                i++;
            }
        }
        return frequencies;
    }


    public static SelectItem[] getWeeksOrMonthForWeekMonthNumber(String week_month_type) {
        ArrayList<SelectItem> weeks_or_months = new ArrayList<>();
        if ("MONTHLY".equalsIgnoreCase(week_month_type)) {
            SelectItem[] months = new SelectItem[12];
            for (int i = 1; i <= months.length; i++) {
                months[i - 1] = new SelectItem(i, "M" + i);
                weeks_or_months.add(months[i - 1]);
            }
        } else {
            SelectItem[] weeks = new SelectItem[53];
            for (int i = 1; i <= weeks.length; i++) {
                weeks[i - 1] = new SelectItem(i, "W" + i);
                weeks_or_months.add(weeks[i - 1]);
            }
        }

        return weeks_or_months.toArray(new SelectItem[]{});
    }

    public static SelectItem[] getPayPeriods(String payFrequency) {
        if (Constants.PAY_FREQUENCY_MONTHLY.equalsIgnoreCase(payFrequency)) {
            return getMonths();
        } else if (Constants.PAY_FREQUENCY_WEEKLY.equalsIgnoreCase(payFrequency)) {
            return getWeeks();
        } else if (Constants.PAY_FREQUENCY_2_WEEKLY.equalsIgnoreCase(payFrequency)) {
            return getWeeks(2);
        } else if (Constants.PAY_FREQUENCY_4_WEEKLY.equalsIgnoreCase(payFrequency)) {
            return getWeeks(4);
        } else if (Constants.PAY_FREQUENCY_ANNUAL.equalsIgnoreCase(payFrequency)) {
            return new SelectItem[]{new SelectItem(1, "Annual")};
        }
        return null;
    }

    public static SelectItem[] getWeeks() {
        return getWeeks(1);
    }

    public static SelectItem[] getWeeks(int mergeWeeks) {
        final int length;
        switch (mergeWeeks) {
            case 2:
                length = 27; //=54-weeks/2;
                break;
            case 4:
                length = 14; //=56-weeks/4;
                break;
            default:
                length = 53;
        }
        final SelectItem[] weeks = new SelectItem[length];
        for (int w = 1, i = 0; i < weeks.length; w += mergeWeeks, i++) {
            weeks[i] = new SelectItem(w + mergeWeeks - 1, "Week" + (mergeWeeks > 1 ? "s " : " ") + w + (mergeWeeks > 1 ? "-" + (w + mergeWeeks - 1) : ""));
        }
        return weeks;
    }

    public static SelectItem[] getMonths() {
        final SelectItem[] months = new SelectItem[12];
        for (int m = 1; m <= months.length; m++) {
            months[m - 1] = new SelectItem(m, "Month " + m);
        }
        return months;
    }

    public static SelectItem[] getLastYearsAsSelectItem() {
        final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy");
        final Date start = new Date();
        if (!(start.getMonth() > 3 || (start.getMonth() == 3 && start.getDate() > 5))) {
            start.setYear(start.getYear() - 1);
        }
        final Date end = new Date(start.getTime());
        end.setYear(end.getYear() + 1);
        final SelectItem[] items = new SelectItem[4];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(Integer.valueOf(dateFormat.format(end)), dateFormat.format(start) + " - " + dateFormat.format(end));
            start.setYear(start.getYear() - 1);
            end.setYear(end.getYear() - 1);
        }
        return items;
    }

    public static BigDecimal parseToBigDecimal(String text) {
        if (text != null && text.length() > 0) {
            String currencyCode = Utils.getParam(Utils.BASE_CURRENCY);
            if (currencyCode != null && text.startsWith(currencyCode)) {
                return BigDecimal.valueOf(numberFormat.parse(text.replace(currencyCode, "")));
            }
            return BigDecimal.valueOf(numberFormat.parse(text));
        }
        return BigDecimal.ZERO;
    }

    public static String format(BigDecimal payAmount) {
        return totalNumberFormat.format(payAmount);
    }

    public static BigDecimal parse(String text) {
        return BigDecimal.valueOf(totalNumberFormat.parse(text));
    }

    public static String numberFormat(BigDecimal payAmount) {
        return numberFormat.format(payAmount);
    }
}