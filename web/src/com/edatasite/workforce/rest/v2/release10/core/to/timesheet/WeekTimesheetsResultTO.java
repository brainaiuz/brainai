package com.edatasite.workforce.rest.v2.release10.core.to.timesheet;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

public class WeekTimesheetsResultTO extends ResponseData {
    private ArrayList<WeekTimesheetsTO> week_timesheets;

    public WeekTimesheetsResultTO() {
    }

    public WeekTimesheetsResultTO(ArrayList<WeekTimesheetsTO> week_timesheets) {
        this.week_timesheets = week_timesheets;
    }

    public ArrayList<WeekTimesheetsTO> getWeek_timesheets() {
        return week_timesheets;
    }

    public void setWeek_timesheets(ArrayList<WeekTimesheetsTO> week_timesheets) {
        this.week_timesheets = week_timesheets;
    }
}
