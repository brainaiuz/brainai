package com.edatasite.workforce.gwt.accounting.client.rpc.enums;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 10.06.14
 * Time: 17:35
 * To change this template use File | Settings | File Templates.
 */
public enum DatesListEnum {
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

    DatesListEnum(int id){
        this.id= id;
    }

    public int getId() {
        return id;
    }

    public static DatesListEnum getEnumById(Integer selectedId) {
        for (DatesListEnum datesListEnum : DatesListEnum.values()) {
            if (datesListEnum.getId() == selectedId) {
                return datesListEnum;
            }
        }
        return null;
    }
}
