package com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class TrialInfoTO extends ResponseData {

    private Boolean is_active;
    private String trial_end_date;

    public TrialInfoTO() {
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public String getTrial_end_date() {
        return trial_end_date;
    }

    public void setTrial_end_date(String trial_end_date) {
        this.trial_end_date = trial_end_date;
    }
}
