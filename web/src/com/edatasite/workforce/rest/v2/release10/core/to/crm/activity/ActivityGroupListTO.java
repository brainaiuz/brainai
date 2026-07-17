package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 2/14/2018.
 */
public class ActivityGroupListTO extends ResponseData {

    private String date;
    private ArrayList<ActivityTO> activities;

    public ActivityGroupListTO() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ActivityTO> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<ActivityTO> activities) {
        this.activities = activities;
    }
}
