package com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 01/12/2018.
 */
public class SubscriptionInfo extends ResponseData {

    private String subscription_type;
    private Boolean is_active;
    private String expiration_date;
    private Integer days_left;

    public SubscriptionInfo() {
    }

    public String getSubscription_type() {
        return subscription_type;
    }

    public void setSubscription_type(String subscription_type) {
        this.subscription_type = subscription_type;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public Integer getDays_left() {
        return days_left;
    }

    public void setDays_left(Integer days_left) {
        this.days_left = days_left;
    }
}
