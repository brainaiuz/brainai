package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SignupsRate implements IsSerializable {
    private String signups;
    private String newUsers;
    private String activated;
    private String activatedInPercentage;
    private String used;
    private String usedInPercentage;
    private String bounce;
    private String bounceInPercentage;
    private String inactive;
    private String inactiveInPercentage;

    public String getSignups() {
        return signups;
    }

    public void setSignups(String signups) {
        this.signups = signups;
    }

    public String getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(String newUsers) {
        this.newUsers = newUsers;
    }

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public String getActivatedInPercentage() {
        return activatedInPercentage;
    }

    public void setActivatedInPercentage(String activatedInPercentage) {
        this.activatedInPercentage = activatedInPercentage;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getUsedInPercentage() {
        return usedInPercentage;
    }

    public void setUsedInPercentage(String usedInPercentage) {
        this.usedInPercentage = usedInPercentage;
    }

    public String getBounce() {
        return bounce;
    }

    public void setBounce(String bounce) {
        this.bounce = bounce;
    }

    public String getBounceInPercentage() {
        return bounceInPercentage;
    }

    public void setBounceInPercentage(String bounceInPercentage) {
        this.bounceInPercentage = bounceInPercentage;
    }

    public String getInactive() {
        return inactive;
    }

    public void setInactive(String inactive) {
        this.inactive = inactive;
    }

    public String getInactiveInPercentage() {
        return inactiveInPercentage;
    }

    public void setInactiveInPercentage(String inactiveInPercentage) {
        this.inactiveInPercentage = inactiveInPercentage;
    }
}
