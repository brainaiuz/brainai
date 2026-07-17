package com.edatasite.workforce.gwt.pricing.client;

import java.io.Serializable;

public class SubscriptionPaymentItem implements Serializable {
    public float perUserCost = 0.00f;
    public float up = 0;
    public float usersCost = 0;
    public float storageCost = 0;
    public float discounts = 0;
    public float tot = 0;
    public Integer usageMonths = null;
    public int userCount;
    public int storage;
    public String usagePeriod;
    public int usagePeriodID;
    public String planType;
    public String service;
    public boolean serviceType = false;
    public boolean isUK = false;
    public String taxCost;
    public String serviceName;
    public float taxC = 0;
    public String category;
    public String categoryREAL;
    public boolean isGBP = false;
    public Integer supportPackage;
    public String supportPackageNAME;
    public float supportPackagePrice = 0;
    public String modules;
    public Integer moduleLimit;
}