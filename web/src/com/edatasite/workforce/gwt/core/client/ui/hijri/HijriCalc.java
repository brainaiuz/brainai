package com.edatasite.workforce.gwt.core.client.ui.hijri;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 09.09.14
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */
public class HijriCalc {

    private static void error(String msg) {
		System.err.println("HijriCalc: " + msg);
	}

    public static SimpleHijriDate toHijri(Date date) {
//        GregorianCalendar gcal = new GregorianCalendar();
//        gcal.setTime(date);
//
//        if (gcal.get(Calendar.ERA) != GregorianCalendar.AD)
//            error("Function h_date doesn't provide era input, it should be AD");

        HijriCalculator.sDate mydate = new HijriCalculator.sDate();
        HijriCalculator.h_date(mydate, date.getDate(),
                date.getMonth() + 1, date.getYear() + 1900);
        return new SimpleHijriDate(mydate);
    }

	public static Date fromHijri(int year, int month, int day) {
		HijriCalculator.sDate mydate = new HijriCalculator.sDate();
		HijriCalculator.g_date(mydate, day, month + 1, year);
//		GregorianCalendar result = new GregorianCalendar(mydate.year, mydate.month - 1, mydate.day);
//		if (HijriCalculator.GREGORIAN_BC.equals(mydate.units))
//			result.set(Calendar.ERA, GregorianCalendar.BC);
		return new Date(mydate.year-1900, mydate.month - 1, mydate.day);
	}
}
