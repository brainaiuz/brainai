package com.edatasite.workforce.gwt.myaccount.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Ilhombek
 * Date: Mar 23, 2011
 * Time: 5:31:07 PM
 */
public class RequestQuoteItem implements IsSerializable {

    private String currentSubscriptionPeriod;
    private Integer currentUsersCount;
    private String currentStatus;
    private String currentTotalAmount;
    private String currentSupportPackageNAME;
    private String currentSupportPackagePrice;

    private String requestedSubscriptionPeriod;
    private Integer requestedUsersCount;
    private String requestedSupportPackageNAME;
    private String requestedSupportPackagePrice;

    public String getCurrentSubscriptionPeriod() {
        return currentSubscriptionPeriod;
    }

    public void setCurrentSubscriptionPeriod(String currentSubscriptionPeriod) {
        this.currentSubscriptionPeriod = currentSubscriptionPeriod;
    }

    public Integer getCurrentUsersCount() {
        return currentUsersCount;
    }

    public void setCurrentUsersCount(Integer currentUsersCount) {
        this.currentUsersCount = currentUsersCount;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getCurrentTotalAmount() {
        return currentTotalAmount;
    }

    public void setCurrentTotalAmount(String currentTotalAmount) {
        this.currentTotalAmount = currentTotalAmount;
    }

    public String getCurrentSupportPackageNAME() {
        return currentSupportPackageNAME != null ? currentSupportPackageNAME : "N/A";
    }

    public void setCurrentSupportPackageNAME(String currentSupportPackageNAME) {
        this.currentSupportPackageNAME = currentSupportPackageNAME;
    }

    public String getCurrentSupportPackagePrice() {
        return currentSupportPackagePrice != null ? currentSupportPackagePrice : "N/A";
    }

    public void setCurrentSupportPackagePrice(String currentSupportPackagePrice) {
        this.currentSupportPackagePrice = currentSupportPackagePrice;
    }

    public String getRequestedSubscriptionPeriod() {
        return requestedSubscriptionPeriod;
    }

    public void setRequestedSubscriptionPeriod(String requestedSubscriptionPeriod) {
        this.requestedSubscriptionPeriod = requestedSubscriptionPeriod;
    }

    public Integer getRequestedUsersCount() {
        return requestedUsersCount;
    }

    public void setRequestedUsersCount(Integer requestedUsersCount) {
        this.requestedUsersCount = requestedUsersCount;
    }

    public String getRequestedSupportPackageNAME() {
        return requestedSupportPackageNAME != null ? requestedSupportPackageNAME : "N/A";
    }

    public void setRequestedSupportPackageNAME(String requestedSupportPackageNAME) {
        this.requestedSupportPackageNAME = requestedSupportPackageNAME;
    }

    public String getRequestedSupportPackagePrice() {
        return requestedSupportPackagePrice != null ? requestedSupportPackagePrice : "N/A";
    }

    public void setRequestedSupportPackagePrice(String requestedSupportPackagePrice) {
        this.requestedSupportPackagePrice = requestedSupportPackagePrice;
    }
}