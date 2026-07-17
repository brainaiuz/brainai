package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ula
 * Date: Jul 21, 2010
 * Time: 3:35:42 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Frequency {
    DAILY(5, "Daily", "Day", 6/*Calendar.DAY_OF_YEAR*/, 1, 365, true, "DAILY"),
    WEEKLY(0, "Weekly", "Week", 3/*Calendar.WEEK_OF_YEAR*/, 1, 52, true, "WEEKLY"),
    WEEKLYx2(3, "2Weekly", "Week", 3/*Calendar.WEEK_OF_YEAR*/, 2, 52, true, "WEEKLYx2"),
    WEEKLYx4(4, "4Weekly", "Week", 3/*Calendar.WEEK_OF_YEAR*/, 4, 52, false, "WEEKLYx4"),
    MONTHLY(1, "Monthly", "Month", 2/*Calendar.MONTH*/, 1, 12, true, "MONTHLY"),
    ANNUAL(2, "Annual", "Year", 1/*Calendar.YEAR*/, 1, 1, false, "ANNUAL");

    private final int id;
    private final String name;
    private final int cycle;
    private final String cycleName;
    private final int numberOfCycles;
    private final int numberOfCyclesInYear;
    private final boolean forAllCountry;
    private final String code;

    Frequency(int id, String name, String cycleName, int cycle, int numberOfCycles, int numberOfCyclesInYear, boolean forAllCountry, String code) {
        this.id = id;
        this.name = name;
        this.cycle = cycle;
        this.cycleName = cycleName;
        this.numberOfCycles = numberOfCycles;
        this.numberOfCyclesInYear = numberOfCyclesInYear;
        this.forAllCountry = forAllCountry;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCycle() {
        return cycle;
    }

    public String getCycleName() {
        return cycleName;
    }

    public int getNumberOfCycles() {
        return numberOfCycles;
    }

    public int getNumberOfCyclesInYear() {
        return numberOfCyclesInYear;
    }

    public boolean isForAllCountry() {
        return forAllCountry;
    }

    public String getCode() {
        return code;
    }

    public static Frequency getByName(String name) {
        for (Frequency frequency : values()) {
            if (frequency.getName().equals(name)) {
                return frequency;
            }
        }
        return null;
    }

    public static Frequency getByID(int id) {
        for (Frequency frequency : values()) {
            if (frequency.getId() == id) {
                return frequency;
            }
        }
        return null;
    }

    public static SelectItem[] asSelectItem(boolean all) {
        ArrayList<SelectItem> list = new ArrayList<>();
        int i = 0;
        String locale = null;
        for (Frequency item : values()) {
            switch (item.getName()) {
                case "Daily":
                    locale = WfmStrings.App.get().daily();
                    break;
                case "Weekly":
                    locale = WfmStrings.App.get().weekly();
                    break;
                case "2Weekly":
                    locale = WfmStrings.App.get().twoWeekly();
                    break;
                case "4Weekly":
                    locale = WfmStrings.App.get().fourWeekly();
                    break;
                case "Monthly":
                    locale = WfmStrings.App.get().monthly();
                    break;
                case "Annual":
                    locale = WfmStrings.App.get().annual();
                    break;
            }
            SelectItem selectItem = new SelectItem(i++, locale, item.name());
            list.add(selectItem);
        }
        return list.toArray(new SelectItem[list.size()]);
    }


    /*  frequency name comes in and the word corresponding to that name is returned in the desired language   */
    public static String returnCompatibleWord(String frequencyName) {
        switch (frequencyName) {
            case "Daily":
                return WfmStrings.App.get().daily();
            case "Weekly":
                return WfmStrings.App.get().weekly();
            case "2Weekly":
                return WfmStrings.App.get().twoWeekly();
            case "4Weekly":
                return WfmStrings.App.get().fourWeekly();
            case "Monthly":
                return WfmStrings.App.get().monthly();
            case "Annual":
                return WfmStrings.App.get().annual();
        }
        return frequencyName;
    }
}
