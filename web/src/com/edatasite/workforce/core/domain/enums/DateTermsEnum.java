package com.edatasite.workforce.core.domain.enums;

import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;

import java.util.Date;

public enum DateTermsEnum {
    CUSTOM(0),
    TODAY(1),
    YESTERDAY(2),
    THIS_WEEK(3),
    THIS_MONTH(4),
    SINCE_TODAY(5),
    SINCE_YESTERDAY(6),
    SINCE_THIS_WEEK(7),
    SINCE_THIS_MONTH(8);

    private final Integer id;

    DateTermsEnum(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static DateTermsEnum getById(Integer id) {
        if (id != null) {
            for (DateTermsEnum dateTermsEnum : values()) {
                if (dateTermsEnum.getId().equals(id)) {
                    return dateTermsEnum;
                }
            }
        }
        return null;
    }

    public static Date getStartDateByTerm(DateTermsEnum type) {
        return getDateByTerm(type, true);
    }

    public static Date getEndDateByTerm(DateTermsEnum type) {
        return getDateByTerm(type, false);
    }

    private static Date getDateByTerm(DateTermsEnum type, boolean isStart) {
        switch (type) {
            case YESTERDAY:
                return getStartOrEndDate(ServerUtils.addDays(new Date(), -1), isStart);
            case THIS_WEEK:
                return isStart ? getStartOrEndDate(DateUtil.getWeekFirstDay(), true) : getStartOrEndDate(DateUtil.getWeekLastDay(), false);
            case THIS_MONTH:
                return isStart ? getStartOrEndDate(DateUtil.getMonthFirstDay(new Date()), true) : getStartOrEndDate(DateUtil.getMonthLastDate(new Date()), false);
            case SINCE_TODAY:
                return isStart ? getStartOrEndDate(new Date(), true) : null;
            case SINCE_YESTERDAY:
                return isStart ? getStartOrEndDate(ServerUtils.addDays(new Date(), -1), true) : null;
            case SINCE_THIS_WEEK:
                return isStart ? getStartOrEndDate(DateUtil.getWeekFirstDay(new Date()), true) : null;
            case SINCE_THIS_MONTH:
                return isStart ? getStartOrEndDate(DateUtil.getMonthFirstDay(new Date()), true) : null;
            default:
                return getStartOrEndDate(new Date(), isStart);
        }
    }

    private static Date getStartOrEndDate(Date date, boolean isStart) {
        return isStart ? ServerUtils.getStartDate(date) : ServerUtils.getEndDate(date);
    }
}
