package com.edatasite.workforce.gwt.availability.server.pojo;

import java.util.Calendar;

public class Holiday {
    private Calendar calendar;
    private boolean takenFromAllowance;

    public Holiday(Calendar calendar, boolean takenFromAllowance) {
        this.calendar = calendar;
        this.takenFromAllowance = takenFromAllowance;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public boolean isTakenFromAllowance() {
        return takenFromAllowance;
    }

    public void setTakenFromAllowance(boolean takenFromAllowance) {
        this.takenFromAllowance = takenFromAllowance;
    }
}
