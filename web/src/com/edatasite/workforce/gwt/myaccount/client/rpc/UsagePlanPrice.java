package com.edatasite.workforce.gwt.myaccount.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Anvar Akramov
 * Date: 25.08.2018
 * Time: 13:07:36
 */
public class UsagePlanPrice implements IsSerializable {


    private double fullUsersPrice;
    private double fullUsersDiscountedPrice;
    private double essUsersPrice;
    private double essUsersDiscountedPrice;
    private double nonUsersPrice;
    private double addonPrice;
    private double totalDiscount;
    private double totalAmount;
    private double prevTotalAmount;
    private double totalSubscription;

    public double getFullUsersPrice() {
        return fullUsersPrice;
    }

    public void setFullUsersPrice(double fullUsersPrice) {
        this.fullUsersPrice = fullUsersPrice;
    }

    public double getEssUsersPrice() {
        return essUsersPrice;
    }

    public void setEssUsersPrice(double essUsersPrice) {
        this.essUsersPrice = essUsersPrice;
    }

    public double getNonUsersPrice() {
        return nonUsersPrice;
    }

    public void setNonUsersPrice(double nonUsersPrice) {
        this.nonUsersPrice = nonUsersPrice;
    }

    public double getAddonPrice() {
        return addonPrice;
    }

    public void setAddonPrice(double addonPrice) {
        this.addonPrice = addonPrice;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPrevTotalAmount() {
        return prevTotalAmount;
    }

    public void setPrevTotalAmount(double prevTotalAmount) {
        this.prevTotalAmount = prevTotalAmount;
    }

    public double getTotalSubscription() {
        return totalSubscription;
    }

    public void setTotalSubscription(double totalSubscription) {
        this.totalSubscription = totalSubscription;
    }

    public double getFullUsersDiscountedPrice() {
        return fullUsersDiscountedPrice;
    }

    public void setFullUsersDiscountedPrice(double fullUsersDiscountedPrice) {
        this.fullUsersDiscountedPrice = fullUsersDiscountedPrice;
    }

    public double getEssUsersDiscountedPrice() {
        return essUsersDiscountedPrice;
    }

    public void setEssUsersDiscountedPrice(double essUsersDiscountedPrice) {
        this.essUsersDiscountedPrice = essUsersDiscountedPrice;
    }
}