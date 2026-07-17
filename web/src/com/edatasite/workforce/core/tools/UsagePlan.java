package com.edatasite.workforce.core.tools;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 4/15/11
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class UsagePlan {
    private Integer subscriptionId;
    private Integer companyId;
    private String type;

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
