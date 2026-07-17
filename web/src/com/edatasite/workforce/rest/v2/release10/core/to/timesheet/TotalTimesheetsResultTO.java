package com.edatasite.workforce.rest.v2.release10.core.to.timesheet;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

public class TotalTimesheetsResultTO extends ResponseData {
    private ArrayList<TotalTimesheetsTO> total_timesheets;

    public TotalTimesheetsResultTO() {
    }

    public TotalTimesheetsResultTO(ArrayList<TotalTimesheetsTO> total_timesheets) {
        this.total_timesheets = total_timesheets;
    }

    public ArrayList<TotalTimesheetsTO> getTotal_timesheets() {
        return total_timesheets;
    }

    public void setTotal_timesheets(ArrayList<TotalTimesheetsTO> total_timesheets) {
        this.total_timesheets = total_timesheets;
    }
}
