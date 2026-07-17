package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jan 14, 2010
 * Time: 9:18:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class SubscriptionManagementItem implements IsSerializable {

    private Integer companyId;
    private String adminEmail;
    private String adminUsername;

    private String companyName;
    private String registrationDate;
    private Boolean active;
    private boolean projectPercentNewLogic;
    private Integer currentUsagePlan;


    public SubscriptionManagementItem() {
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setProjectPercentNewLogic(boolean projectPercentNewLogic) {
        this.projectPercentNewLogic = projectPercentNewLogic;
    }

    public boolean isProjectPercentNewLogic() {
        return projectPercentNewLogic;
    }

    public void setCurrentUsagePlan(Integer currentUsagePlan) {
        this.currentUsagePlan = currentUsagePlan;
    }

    public Integer getCurrentUsagePlan() {
        return currentUsagePlan;
    }
}
