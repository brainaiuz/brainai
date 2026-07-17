package com.edatasite.workforce.gwt.payroll.client.utils;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.enums.ReportDatesEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shohruh on 12-Jan-17.
 */
public class PayrollReportUtils {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    public static SelectItem[] getDatesListItems() {
        ArrayList<SelectItem> datesList = new ArrayList<>();
        datesList.add(new SelectItem(ReportDatesEnum.ThisMonth.getId(), wfmStrings.thisMonth()));
        datesList.add(new SelectItem(ReportDatesEnum.ThisFiscalQuarter.getId(), wfmStrings.thisFiscalQuarter()));
        datesList.add(new SelectItem(ReportDatesEnum.ThisFiscalYear.getId(), wfmStrings.thisFiscalYear()));
        datesList.add(new SelectItem(ReportDatesEnum.Custom.getId(), wfmStrings.custom()));
        datesList.add(new SelectItem(ReportDatesEnum.Today.getId(), wfmStrings.today()));
        datesList.add(new SelectItem(ReportDatesEnum.ThisWeek.getId(), wfmStrings.thisWeek()));
        datesList.add(new SelectItem(ReportDatesEnum.ThisWeekToDate.getId(), wfmStrings.thisWeekToDate()));
        datesList.add(new SelectItem(ReportDatesEnum.ThisMonth.getId(), wfmStrings.thisMonth()));
        datesList.add(new SelectItem(ReportDatesEnum.ThisMonthToDate.getId(), wfmStrings.thisMonthToDate()));
        datesList.add(new SelectItem(ReportDatesEnum.ThisFiscalQuarter.getId(), wfmStrings.thisFiscalQuarter()));
        datesList.add(new SelectItem(ReportDatesEnum.ThisFiscalQuarterToDate.getId(), wfmStrings.thisFiscalQuarterToDate()));
        datesList.add(new SelectItem(ReportDatesEnum.ThisFiscalYear.getId(), wfmStrings.thisFiscalYear()));
        datesList.add(new SelectItem(ReportDatesEnum.ThisFiscalYearToDate.getId(), wfmStrings.thisFiscalYearToDate()));
        datesList.add(new SelectItem(ReportDatesEnum.Yesterday.getId(), wfmStrings.yesterday()));
        datesList.add(new SelectItem(ReportDatesEnum.LastWeek.getId(), wfmStrings.lastWeek()));
        datesList.add(new SelectItem(ReportDatesEnum.LastWeekToDate.getId(), wfmStrings.lastWeekToDate()));
        datesList.add(new SelectItem(ReportDatesEnum.LastMonth.getId(), wfmStrings.lastMonth()));
        datesList.add(new SelectItem(ReportDatesEnum.LastMonthToDate.getId(), wfmStrings.lastMonthToDate()));
        datesList.add(new SelectItem(ReportDatesEnum.LastFiscalQuarter.getId(), wfmStrings.lastFiscalQuarter()));
        datesList.add(new SelectItem(ReportDatesEnum.LastFiscalQuarterToDate.getId(), wfmStrings.lastFiscalQuarterToDate()));
        datesList.add(new SelectItem(ReportDatesEnum.LastFiscalYear.getId(), wfmStrings.lastFiscalYear()));
        datesList.add(new SelectItem(ReportDatesEnum.LastFiscalYearToDate.getId(), wfmStrings.lastFiscalYearToDate()));
        datesList.add(new SelectItem(ReportDatesEnum.NextWeek.getId(), wfmStrings.nextWeek()));
        datesList.add(new SelectItem(ReportDatesEnum.Next4Weeks.getId(), wfmStrings.next4Week()));
        datesList.add(new SelectItem(ReportDatesEnum.NextMonth.getId(), wfmStrings.nextMonth()));
        datesList.add(new SelectItem(ReportDatesEnum.NextFiscalQuarter.getId(), wfmStrings.nextFiscalQuarter()));
        datesList.add(new SelectItem(ReportDatesEnum.NextFiscalYear.getId(), wfmStrings.nextFiscalYear()));
        datesList.add(new SelectItem(ReportDatesEnum.Custom.getId(), wfmStrings.custom()));
        return datesList.toArray(new SelectItem[]{});
    }

    public static void setFromAndToDates(DatePicker fromValue, DatePicker toValue, Integer selectedId, List<Date> financialQuartiesList, Date financialYearStart) {
        Date from = new Date(), to = new Date();
        Date currentDate = new Date();
        switch (ReportDatesEnum.getEnumById(selectedId)) {
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
                from = DateUtil.addDays(DateUtil.getWeekFirstDay(DateUtil.addDays(from, -7)), 1);
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
        fromValue.setDate(from);
        toValue.setDate(to);
    }
}
