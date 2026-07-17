package com.edatasite.workforce.gwt.accounting.client.rpc.enums;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 10.06.14
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 */
public enum CompareWithEnum {
    None(0, 0, CompareCategoryEnum.None),
    PreviousDay(1, 1, CompareCategoryEnum.Day),
    PreviousWeek(2, 1, CompareCategoryEnum.Week),
    PreviousMonth(3, 1, CompareCategoryEnum.Month),
    Previous2Months(4, 2, CompareCategoryEnum.Month),
    Previous3Months(5, 3, CompareCategoryEnum.Month),
    Previous4Months(6, 4, CompareCategoryEnum.Month),
    Previous5Months(7, 5, CompareCategoryEnum.Month),
    Previous6Months(8, 6, CompareCategoryEnum.Month),
    Previous7Months(9, 7, CompareCategoryEnum.Month),
    Previous8Months(10, 8, CompareCategoryEnum.Month),
    Previous9Months(11, 9, CompareCategoryEnum.Month),
    Previous10Months(12, 10, CompareCategoryEnum.Month),
    Previous11Months(13, 11, CompareCategoryEnum.Month),
    Previous12Months(14, 12, CompareCategoryEnum.Month),
    PreviousYear(15, 1, CompareCategoryEnum.Year),
    Previous2Years(16, 2, CompareCategoryEnum.Year),
    Previous3Years(17, 3, CompareCategoryEnum.Year),
    Previous4Years(18, 4, CompareCategoryEnum.Year),
    Previous5Years(19, 5, CompareCategoryEnum.Year);

    private int id;
    private int length;
    private CompareCategoryEnum compareCategoryEnum;

    CompareWithEnum(int id, int length, CompareCategoryEnum compareCategoryEnum) {
        this.id = id;
        this.length = length;
        this.compareCategoryEnum = compareCategoryEnum;
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public CompareCategoryEnum getCompareCategoryEnum() {
        return compareCategoryEnum;
    }

    public static CompareWithEnum getEnumyById(Integer compareWithId) {
        for (CompareWithEnum compareWithEnum : CompareWithEnum.values()) {
            if (compareWithEnum.getId() == compareWithId) {
                return compareWithEnum;
            }
        }
        return CompareWithEnum.None;
    }
}
