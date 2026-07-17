package com.edatasite.workforce.gwt.core.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 09.09.14
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public interface HijriStrings extends ConstantsWithLookup {
    String hijriDay0();

    String hijriDay1();

    String hijriDay2();

    String hijriDay3();

    String hijriDay4();

    String hijriDay5();

    String hijriDay6();

    String hijriDayShort0();

    String hijriDayShort1();

    String hijriDayShort2();

    String hijriDayShort3();

    String hijriDayShort4();

    String hijriDayShort5();

    String hijriDayShort6();

    String hijriMonth0();

    String hijriMonth1();

    String hijriMonth2();

    String hijriMonth3();

    String hijriMonth4();

    String hijriMonth5();

    String hijriMonth6();

    String hijriMonth7();

    String hijriMonth8();

    String hijriMonth9();

    String hijriMonth10();

    String hijriMonth11();

    String hijriMonthShort0();

    String hijriMonthShort1();

    String hijriMonthShort2();

    String hijriMonthShort3();

    String hijriMonthShort4();

    String hijriMonthShort5();

    String hijriMonthShort6();

    String hijriMonthShort7();

    String hijriMonthShort8();

    String hijriMonthShort9();

    String hijriMonthShort10();

    String hijriMonthShort11();

    String eraBH();

    String eraAH();

    class App {
        private static HijriStrings instance;

        public static HijriStrings get() {
            if (instance == null) {
                instance = GWT.create(HijriStrings.class);
            }
            return instance;
        }
    }
}
