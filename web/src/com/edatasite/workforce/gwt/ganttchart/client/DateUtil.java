package com.edatasite.workforce.gwt.ganttchart.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 2, 2011
 * Time: 9:32:12 AM
 * To change this template use File | Settings | File Templates.
 */

public class DateUtil implements com.edatasite.workforce.gwt.core.client.ui.Constants {

    private static final Integer secondsInDay = 86400000;
    public static final DateTimeFormat shortDateFormat = DateTimeFormat.getFormat(Utils.getShortDateFormat());
    public static final DateTimeFormat format = DateTimeFormat.getFormat("dd.MM.yyyy");
	public static final DateTimeFormat format1 = DateTimeFormat.getFormat("MMM dd, yyyy");
	public static final DateTimeFormat format2 = DateTimeFormat.getFormat("dd.MM.yyyy hh:mm:ss");
	public static final DateTimeFormat dayFormat = DateTimeFormat.getFormat("dd");
    public static final DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMM, yyyy");
    public static final String[] daysName = new String[]{"M", "T", "W", "T", "F", "S", "S"};
    public static final String[] daysName1 = new String[]{"S", "M", "T", "W", "T", "F", "S"};
    public static final String[] daysName2 = new String[]{ "S", "S", "M", "T", "W", "T", "F"};
    public DateUtil() {

    }

    public static Date getFirstDateOfWeek(Date date) {
        Date copyDate = (Date) date.clone();
        if (copyDate.getDay() != 1) {
            copyDate = addDays(copyDate, -1*(copyDate.getDay()-1));
        }
        return copyDate;
    }

    public static Date getLastDateOfWeek(Date date) {
		if (date.getDay() != 0) {
        	date = addDays(date, 7-date.getDay());
		}
		return date;
    }

    public static Date getCompanyFirstDateOfWeek(Date date) {
        Date copyDate = (Date) date.clone();

        if ("1".equals(Utils.userSettings.get(TIMESHEET_WEEK_START))) { //week tarts from Sunday
            if (copyDate.getDay() != 1) {
                copyDate = addDays(copyDate, -1*(copyDate.getDay()));
            }
        } else if ("7".equals(Utils.userSettings.get(TIMESHEET_WEEK_START))) { //week starts from Saturday
            if (copyDate.getDay() != 6) {
                copyDate = addDays(copyDate, -1*(copyDate.getDay()+1));
            }
        } else { //week starts from Monday
            if (copyDate.getDay() != 1) {
                copyDate = addDays(copyDate, -1*(copyDate.getDay()-1));
            }
        }
        return copyDate;
    }

    public static Date getCompanyLastDateOfWeek(Date date) {
        if ("1".equals(Utils.userSettings.get(TIMESHEET_WEEK_START))) { //if week starts from Sunday it must return Saturday
            if (date.getDay() != 1) {
                date = addDays(date, 6-date.getDay());
            }
        } else if ("7".equals(Utils.userSettings.get(TIMESHEET_WEEK_START))) { //if week starts from Saturday it must return Friday
            if (date.getDay() != 5) {
                date = addDays(date, date.getDay());
            }
        } else { //week starts from Monday it must return Sunday
            if (date.getDay() != 0) {
                date = addDays(date,  7-date.getDay());
            }
        }
		return date;
    }

    public static Date getFirstDateOfMonth(Date date) {
        Date copyDate = (Date) date.clone();
        copyDate.setDate(1);
        return copyDate;
    }

    public static Date getLastDateOfMonth(Date date) {
        Date copyDate = (Date) date.clone();
        copyDate.setMonth(copyDate.getMonth()+1);
        copyDate.setDate(1);
        return addDays(copyDate, -1);
    }

    public static Date addDays(Date date, int days) {
        return new Date(date.getYear(), date.getMonth(), date.getDate()+days, date.getHours(), date.getMinutes(), date.getSeconds());
    }

	public static Date addMonths(Date date, int month) {
		Date copyDate = (Date) date.clone();
		copyDate.setMonth(copyDate.getMonth()+1);
		return copyDate;
	}

    public static String getFormattedDate(Date date) {
        return format1.format(date);
    }

    public static int getDaysCount(Date start, Date end) {
		return getDaysCount(start, end, false);
	}

    public static int getDaysCount(Date start, Date end, boolean ignoreHours) {
		Date sDate = new Date(start.getYear(), start.getMonth(), start.getDate());
		Date eDate = new Date(end.getYear(), end.getMonth(), end.getDate());
        long startOffset = -((long) sDate.getTimezoneOffset() *60*1000);
        long endOffset = - ((long) eDate.getTimezoneOffset() *60*1000);
        Long value = ((eDate.getTime() + endOffset) - (sDate.getTime() + startOffset))/secondsInDay;
		int abs = Math.abs(value.intValue());
		if (!ignoreHours && (end.getTime() - start.getTime()) % secondsInDay >= 0) {
			abs += 1;
		}
		return abs;
    }

    public static String getQuarterName(Date date) {
        String quarter = (date.getYear() + 1900) + ", Quarter ";
        int month = date.getMonth();
        if (month>=0 && month <= 2) {
            quarter += 1;
        } else if (month>=3 && month <= 5) {
            quarter += 2;
        } else if (month>=6 && month <= 8) {
            quarter += 3;
        } else if (month>=9 && month <= 11) {
            quarter += 4;
        }
        return quarter;
    }

    public static int getQuarterDaysCount(Date date) {
        int days = 0;
        int month = date.getMonth();
        if (month>=0 && month <= 2) {
            if ((date.getYear()+1900)%4 == 0) {
                days = 91; //this is laep year
            } else {
                days = 90;
            }
        } else if (month>=3 && month <= 5) {
            days = 91;
        } else if (month>=6 && month <= 8) {
            days = 92;
        } else if (month>=9 && month <= 11) {
            days = 92;
        }
        return days;
    }

    public static int getMonthDaysCount(Date date) {
        int days = 0;
        int month = date.getMonth();
        switch (month) {
            case 0 : days = 31; break; //January
            case 1 : { //February
                if ((date.getYear()+1900)%4 == 0) {
                    days = 29; //this is laep year
                } else {
                    days = 28;
                }
            } break;
            case 2: days = 31;//March
            case 3: days = 30;//April
            case 4: days = 31;//May
            case 5: days = 30;//Juny
            case 6: days = 31;//July
            case 7: days = 31;//August
            case 8: days = 30;//September
            case 9: days = 31;//October
            case 10: days = 30;//November
            case 11: days = 31;//December
        }
        return days;
    }

    public static Date getFirstDateOfQuarter(Date date) {
        Date copyDate = (Date) date.clone();
        int month = copyDate.getMonth();
        if (month>=0 && month <= 2) {
            copyDate.setMonth(0);
        } else if (month>=3 && month <= 5) {
            copyDate.setMonth(3);
        } else if (month>=6 && month <= 8) {
            copyDate.setMonth(6);
        } else if (month>=9 && month <= 11) {
            copyDate.setMonth(9);
        }
        copyDate.setDate(1);
        return copyDate;
    }

	public static Date getLastDateOfQuarter(Date date) {
        Date copyDate = (Date) date.clone();
        int month = copyDate.getMonth();
        if (month>=0 && month <= 2) {
            copyDate.setMonth(2);
        } else if (month>=3 && month <= 5) {
            copyDate.setMonth(5);
        } else if (month>=6 && month <= 8) {
            copyDate.setMonth(8);
        } else if (month>=9 && month <= 11) {
            copyDate.setMonth(11);
        }
        copyDate.setDate(1);
        return getLastDateOfMonth(copyDate);
    }

	public static Date lastDateOfTheDay(Date date) {
		Date copyDate = (Date) date.clone();
		copyDate.setHours(23);
		copyDate.setMinutes(59);
		copyDate.setSeconds(59);
		return copyDate;
	}
}
