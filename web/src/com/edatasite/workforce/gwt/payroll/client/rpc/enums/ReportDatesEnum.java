package com.edatasite.workforce.gwt.payroll.client.rpc.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/6/16
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ReportDatesEnum {
    All(0),
    Today(1),
    ThisWeek(2),
    ThisWeekToDate(3),
    ThisMonth(4),
    ThisMonthToDate(5),
    ThisFiscalQuarter(6),
    ThisFiscalQuarterToDate(7),
    ThisFiscalYear(8),
    ThisFiscalYearToDate(9),
    Yesterday(10),
    LastWeek(11),
    LastWeekToDate(12),
    LastMonth(13),
    LastMonthToDate(14),
    LastFiscalQuarter(15),
    LastFiscalQuarterToDate(16),
    LastFiscalYear(17),
    LastFiscalYearToDate(18),
    NextWeek(19),
    Next4Weeks(20),
    NextMonth(21),
    NextFiscalQuarter(22),
    NextFiscalYear(23),
    Custom(24);

    private int id;

    ReportDatesEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ReportDatesEnum getEnumById(Integer selectedId) {
        for (ReportDatesEnum datesListEnum : ReportDatesEnum.values()) {
            if (datesListEnum.getId() == selectedId) {
                return datesListEnum;
            }
        }
        return null;
    }
}
