package com.edatasite.workforce.gwt.core.client;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

/**
 * User: iskan
 * Date: Jan 21, 2008
 * Time: 4:33:44 PM
 */

public class DateUtils implements Constants {

    private static DateTimeFormat formatToParse = DateTimeFormat.getFormat(DATE_PATTERN);
    public static DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat()); //"MM/dd/yyyy"
    private static final DateTimeFormat formatInternal = DateTimeFormat.getFormat(Utils.getLongDateFormat()); //"yyyy-MM-dd hh:mm:ss"
    public static final DateTimeFormat formatInternalShort = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm aa");
    public static final DateTimeFormat formatInternalShort1 = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm");
    public static final DateTimeFormat formatInternalShort2 = DateTimeFormat.getFormat("dd-MM-yyyy, HH:mm");
    private static final DateTimeFormat previewFormat = DateTimeFormat.getFormat("d MMM, yyyy");
    public static final DateTimeFormat dateAndTimeFormatShort = DateTimeFormat.getFormat("MMM dd, HH:mm");
    public static final DateTimeFormat dateAndTimeFormatShort1 = DateTimeFormat.getFormat("MMM dd, yyyy");
    public static final DateTimeFormat dateAndTimeFormatShort2 = DateTimeFormat.getFormat("MMM dd yyyy, HH:mm");
    public static final DateTimeFormat fullDateFormat = DateTimeFormat.getFullDateFormat();
    public static final DateTimeFormat dateAndTimeFormatFull = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormat longDateFormat = DateTimeFormat.getFormat(LONG_DATE_FORMAT_19);
    public static final DateTimeFormat shortDateTimeFormat = DateTimeFormat.getFormat(SHORT_DATE_FORMAT_5);
    public static final DateTimeFormat dateAndTimeFormatWithDash = DateTimeFormat.getFormat("yyyy-MM-dd-HH-mm-ss");
    public static final DateTimeFormat dateFormatShort = DateTimeFormat.getFormat("yyyy-MM-dd");
    public static final DateTimeFormat timeFormatShort = DateTimeFormat.getFormat("h:mm aa");
    public static final DateTimeFormat timeInSecondsFormatShort = DateTimeFormat.getFormat("mm:ss");
    public static final DateTimeFormat yearMonthFormat = DateTimeFormat.getFormat("yyyy, MMMM");
    public static final DateTimeFormat monthYearFormat = DateTimeFormat.getFormat("MMM, yyyy");
    public static final DateTimeFormat dayMonthFormat = DateTimeFormat.getFormat("dd, MMMM");
    public static final DateTimeFormat dateCustomFormat = DateTimeFormat.getFormat("d-E, yyyy HH:mm");
    public static final DateTimeFormat dateFormatShort1 = DateTimeFormat.getFormat("dd-MM-yyyy");
    public static final DateTimeFormat dateFormatWithSlash = DateTimeFormat.getFormat("MM/dd/yyyy");
    public static final DateTimeFormat dayFormat = DateTimeFormat.getFormat("dd");
    public static final DateTimeFormat monthFullFormat = DateTimeFormat.getFormat("MMMM");
    public static final DateTimeFormat monthShortFormat = DateTimeFormat.getFormat("M");
    public static final DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
    public static final DateTimeFormat dateFormatWithHour = DateTimeFormat.getFormat("dd MMM, yyyy HH:mm");


    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    private static Date transactionLockDate;

    public static String format(Date date) {
        if (date != null) {
            return format.format(date);
        } else {
            return wfmStrings.notAvailable();
        }
    }

    public static String convertToUzbDateFormat(String dateFormat) {
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String date = dateFormat;
        String datePrefix = "";
        for (String month : months) {
            if (dateFormat.contains(month)) {
                for (int i = 0; i <= dateFormat.length() - 3; i++) {
                    if (dateFormat.substring(i, i + 3).equals("Jan")) {
                        datePrefix = dateFormat.substring(0, i);
                        date = datePrefix + "Yan" + dateFormat.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Feb")) {
                        datePrefix = dateFormat.substring(0, i);
                        date = datePrefix + "Fev" + dateFormat.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Mar")) {
                        datePrefix = dateFormat.substring(0, i);
                        date = datePrefix + "Mar" + dateFormat.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Apr")) {
                        datePrefix = dateFormat.substring(0, i);
                        date = datePrefix + "Apr" + dateFormat.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("May")) {
                        datePrefix = dateFormat.substring(0, i);
                        date = datePrefix + "May" + dateFormat.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Jun")) {
                        datePrefix = dateFormat.substring(0, i);
                        date = datePrefix + "Iyun" + dateFormat.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Jul")) {
                        datePrefix = dateFormat.substring(0, i);
                        date = datePrefix + "Iyul" + dateFormat.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Aug")) {
                        datePrefix = dateFormat.substring(0, i);
                        date = datePrefix + "Avg" + dateFormat.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Sep")) {
                        datePrefix = dateFormat.substring(0, i);
                        date = datePrefix + "Sen" + dateFormat.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Oct")) {
                        datePrefix = dateFormat.substring(0, i);
                        date = datePrefix + "Okt" + dateFormat.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Nov")) {
                        datePrefix = dateFormat.substring(0, i);
                        date = datePrefix + "Noya" + dateFormat.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Dec")) {
                        datePrefix = dateFormat.substring(0, i);
                        date = datePrefix + "Dek" + dateFormat.substring(i + 3);
                    }
                }
            }
        }

        String[] weekDays = new String[]{"Mon", "Tue", "Wed", "Thur", "May", " Fri", "Jul", "Sat", "Sun"};
        String days = date;
        String dayPrefix = "";
        for (String day : weekDays) {
            if (date.contains(day)) {
                for (int i = 0; i <= dateFormat.length() - 3; i++) {
                    if (dateFormat.substring(i, i + 3).equals("Mon")) {
                        dayPrefix = days.substring(0, i);
                        days = dayPrefix + "Dush" + days.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Tue")) {
                        dayPrefix = days.substring(0, i);
                        days = dayPrefix + "Sesh" + days.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Wed")) {
                        dayPrefix = days.substring(0, i);
                        days = dayPrefix + "Chor" + days.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Thur")) {
                        dayPrefix = days.substring(0, i);
                        days = dayPrefix + "Pay" + days.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Fri")) {
                        dayPrefix = days.substring(0, i);
                        days = dayPrefix + "Juma" + days.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Sat")) {
                        dayPrefix = days.substring(0, i);
                        days = dayPrefix + "Shan" + days.substring(i + 3);
                    } else if (dateFormat.substring(i, i + 3).equals("Sun")) {
                        dayPrefix = days.substring(0, i);
                        days = dayPrefix + "Yak" + days.substring(i + 3);
                    }
                }
            }
        }
        return days;
    }

    public static String dateAndTimeFormatShort2(DateNonConvertable dateNonConvertable) {
        if (dateNonConvertable != null && dateNonConvertable.getNonConvertedDate() != null) {
            return dateAndTimeFormatShort2.format(dateNonConvertable.getNonConvertedDate());
        }
        return "";
    }

    public static String format(DateNonConvertable dateNonConvertable) {
        if (dateNonConvertable != null && dateNonConvertable.getNonConvertedDate() != null) {
            return format.format(dateNonConvertable.getNonConvertedDate());
        }
        return "";
    }

    public static String format1(Date date) {
        if (date != null) {
            return format.format(date);
        }
        return null;
    }

    public static String formatWithOutYear(Date date) {
        if (date != null) {
            String[] formatArray = format.getPattern().split("yyyy");
            if (formatArray.length == 2) {
                String formatString = formatArray[0];
                if (formatArray[0].equals(" ")) {
                    formatString = formatArray[1];
                }
                String beginIndex = formatString.substring(0, 1);
                String endIndex = formatString.substring(formatString.length() - 1);
                if (beginIndex.equals(",") || beginIndex.equals(".") || beginIndex.equals("/") || beginIndex.equals("-")) {
                    formatString = formatString;
                } else if (endIndex.equals(",") || endIndex.equals(".") || endIndex.equals("/") || endIndex.equals("-")) {
                    formatString = formatString.substring(0, formatString.length() - 1);
                }

            }
            return format.format(date);
        } else {
            return wfmStrings.notAvailable();
        }
    }

    public static String formatToParse(Date date) {
        if (date != null) {
            return formatToParse.format(date);
        } else {
            return wfmStrings.notAvailable();
        }
    }

    public static String format(Date date, DateTimeFormat format) {
        if (date != null) {
            return format.format(date);
        } else {
            return wfmStrings.notAvailable();
        }
    }

    public static String formatInternal(Date date) {
        if (date == null) {
            return wfmStrings.notAvailable();
        }
        return formatInternal.format(date);
    }

    public static String formatInternal1(Date date) {
        if (date != null) {
            return formatInternal.format(date);
        }
        return null;
    }

    public static String formatInternalShort(Date date) {
        if (date == null) {
            return wfmStrings.notAvailable();
        }
        return formatInternalShort.format(date);
    }

    public static String formatInternalShort1(Date date) {
        if (date == null) {
            return wfmStrings.notAvailable();
        }
        return formatInternalShort1.format(date);
    }

    public static String formatInternalShort2(Date date) {
        if (date == null) {
            return wfmStrings.notAvailable();
        }
        return formatInternalShort2.format(date);
    }

    public static String preiewFormat(Date date) {
        if (date != null) {
            return previewFormat.format(date);
        } else {
            return wfmStrings.notAvailable();
        }
    }

    public static Date parse(String text) throws DateFormatException {
        try {
            return format.parse(text);
        } catch (IllegalArgumentException ex) {
            throw new DateFormatException(ex.getMessage(), ex);
        }
    }

    public static Date parseLongFormat(String text) throws DateFormatException {
        try {
            return formatInternal.parse(text);
        } catch (IllegalArgumentException ex) {
            throw new DateFormatException(ex.getMessage(), ex);
        }
    }

    public static Date parseFormatInternalShort2(String text, DateTimeFormat format) throws DateFormatException {
        try {
            return format.parse(text);
        } catch (IllegalArgumentException ex) {
            throw new DateFormatException(ex.getMessage(), ex);
        }
    }

    public static Date parse(String text, DateTimeFormat format) throws DateFormatException {
        try {
            return format.parse(text);
        } catch (IllegalArgumentException ex) {
            throw new DateFormatException(ex.getMessage(), ex);
        }
    }

    public static String parseFromOneToAnotherFormat(String text, DateTimeFormat originFormat, DateTimeFormat resultingFormat) throws DateFormatException {
        try {
            Date date = originFormat.parse(text);
            return resultingFormat.format(date);
        } catch (IllegalArgumentException e) {
            throw new DateFormatException(e.getMessage(), e);
        }
    }

    public static String getYear(Date date) {
        if (date == null) {
            date = new Date();
        }
        return yearFormat.format(date);
    }

    public static String dayMonthFormat(Date date) {
        if (date != null) {
            return dayMonthFormat.format(date);
        } else {
            return wfmStrings.notAvailable();
        }
    }

    public static String getDateAndTimeFormatShort(Date date) {
        return dateAndTimeFormatShort.format(date);
    }

    public static String getDateAndTimeFormatShort1(Date date) {
        return dateAndTimeFormatShort1.format(date);
    }

    public static String getDateAndTimeFormatShort2(Date date) {
        return dateAndTimeFormatShort2.format(date);
    }

    public static String getDateAndTimeFormatFull(Date date) {
        return dateAndTimeFormatFull.format(date);
    }

    public static DateTimeFormat getDateAndTimeFormatFull() {
        return dateAndTimeFormatFull;
    }

    public static String getDateAndTimeFormatWithDash(Date date) {
        return dateAndTimeFormatWithDash.format(date);
    }

    public static DateTimeFormat getDateAndTimeFormatWithDash() {
        return dateAndTimeFormatWithDash;
    }

    public static DateTimeFormat getFormat() {
        return format;
    }

    public static DateTimeFormat dateAndTimeFormatShort2() {
        return dateAndTimeFormatShort2;
    }

    public static DateTimeFormat getFormatInternal() {
        return formatInternal;
    }

    public static DateTimeFormat getTimeFormatInternal() {//to do
        final String tfp = getFormatInternal().getPattern();
        return DateTimeFormat.getFormat(tfp.contains("[") ? tfp.substring(tfp.indexOf("[") + 1, tfp.length() - 1) : tfp.substring(tfp.toLowerCase().indexOf("hh")));
    }

    public static DateTimeFormat getFormatInternalShort() {
        return formatInternalShort;
    }

    public static DateTimeFormat getPreviewFormat() {
        return previewFormat;
    }

    public static DateTimeFormat getYearFormat() {
        return yearFormat;
    }

    public static DateTimeFormat getDateAndTimeFormatShort() {
        return dateAndTimeFormatShort;
    }

    public static DateTimeFormat getFullDateFormat() {
        return fullDateFormat;
    }

    public static DateTimeFormat getDateFormatShort() {
        return dateFormatShort;
    }

    public static String getDateFormatShort(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormatShort.format(date);
    }

    public static DateTimeFormat getTimeFormatShort() {
        return timeFormatShort;
    }

    public static String getTimeFormatShort(Date date) {
        if (date == null) {
            return "";
        }
        return timeFormatShort.format(date);
    }

    public static String getTimeInSecondsFormatShort(Date date) {
        return timeInSecondsFormatShort.format(date);
    }

    public static String getYearMonthFormat(Date date) {
        return yearMonthFormat.format(date);
    }

    public static String getDateCustomFormat(Date date) {
        if (date != null) {
            return dateCustomFormat.format(date);
        } else {
            return wfmStrings.notAvailable();
        }
    }

    public static Date getTransactionLockDate() {
        if (transactionLockDate != null) {
            return transactionLockDate;
        }
        if (Utils.getTransactionLockDate() != null) {
            try {
                transactionLockDate = new DateNonConvertable(dateFormatShort.parse(Utils.getTransactionLockDate())).getNonConvertedDate();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return transactionLockDate;
    }

    public static void setTransactionLockDate(Date date) {
        if (date != null) {
            Utils.setTransactionLockDate(dateFormatShort.format(date));
            transactionLockDate = null;
        }
    }

    public static String formatFromTo(Date from, Date to, boolean isAllDay, boolean isSameDay) {
        try {
            String fromDate = isAllDay ? format(from) : formatInternal(from);
            String toDate = isAllDay ? format(to) : formatInternal(to);
            if (isSameDay) {
                if (isAllDay) {
                    toDate = "";
                } else {
                    String format = Utils.getLongDateFormat().contains("[") ? "[HH:mm]" : "HH:mm";
                    DateTimeFormat timeFormat = DateTimeFormat.getFormat(format);
                    toDate = timeFormat.format(to);
                }
            }
            return toDate != null && toDate.trim().length() > 0 ? (fromDate + " - " + toDate) : fromDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String dateFormatWithHour(Date date){
        return dateFormatWithHour.format(date);
    }

    public static boolean areOnTheSameDay(Date date1, Date date2) {
        return date1.getDate() == date2.getDate() && date1.getMonth() == date2.getMonth() && date1.getYear() == date2.getYear();
    }

    public static Date addDays(Date date, int days) {
        return new Date(date.getYear(), date.getMonth(), date.getDate() + days, date.getHours(), date.getMinutes(), date.getSeconds());
    }
}
