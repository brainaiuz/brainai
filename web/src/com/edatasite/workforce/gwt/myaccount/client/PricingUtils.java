package com.edatasite.workforce.gwt.myaccount.client;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanPrice;

//import com.google.gwt.core.client.GWT;

public class PricingUtils implements Constants {

    public static double perUserPrice = 19.99d;
    public static double perEssUserPrice = 1d;
    public static double perNoAccessUserPrice = 0.25d;
    public static double yearlyDiscount = 20d;
    public static double semiAnnually = 15d;

    public static double quarterly = 10d;


    public static UsagePlanPrice getTotalPrice(int fullUsersCount, int essUsersCount, int moduleCount, int nonAccessUsersCount, double totalAddOns,
                                               UsagePlanItem prevUsagePlan, boolean isMonthly, String type) {


        if (moduleCount == 1) {
            perUserPrice = 19.99;
        } else if (moduleCount == 2) {
            perUserPrice = 29.99;
        } else if (moduleCount == 3) {
            perUserPrice = 34.99;
        } else if (moduleCount == 4) {
            perUserPrice = 39.99;
        } else if (moduleCount == 5) {
            perUserPrice = 44.99;
        }


        double totalUserPrice = perUserPrice * fullUsersCount;
        double totalEssUserPrice = perEssUserPrice * essUsersCount;
        double noAccessUsersTotalPrice = (perNoAccessUserPrice * nonAccessUsersCount);
        double totalDiscountPrice = 0;


        UsagePlanPrice prices = new UsagePlanPrice();


        if (PP_BY_YEAR.equals(type)) {
            totalUserPrice = totalUserPrice * 12;
            totalEssUserPrice = totalEssUserPrice * 12;
            noAccessUsersTotalPrice = noAccessUsersTotalPrice * 12;
            totalDiscountPrice = ((totalUserPrice + totalEssUserPrice + noAccessUsersTotalPrice) * yearlyDiscount) / 100;
            prices.setTotalDiscount(yearlyDiscount);
        } else if (PP_BY_HALF_YEAR.equals(type)) {
            totalUserPrice = totalUserPrice * 6;
            totalEssUserPrice = totalEssUserPrice * 6;
            noAccessUsersTotalPrice = noAccessUsersTotalPrice * 6;
            totalDiscountPrice = ((totalUserPrice + totalEssUserPrice + noAccessUsersTotalPrice) * semiAnnually) / 100;
            prices.setTotalDiscount(semiAnnually);
        } else if (PP_BY_QUARTER.equals(type)) {
            totalUserPrice = totalUserPrice * 3;
            totalEssUserPrice = totalEssUserPrice * 3;
            noAccessUsersTotalPrice = noAccessUsersTotalPrice * 3;
            totalDiscountPrice = ((totalUserPrice + totalEssUserPrice + noAccessUsersTotalPrice) * quarterly) / 100;
            prices.setTotalDiscount(quarterly);
        }
        double subscriptionTotalPrice = totalUserPrice + totalEssUserPrice + noAccessUsersTotalPrice;
        subscriptionTotalPrice = subscriptionTotalPrice - totalDiscountPrice;


        prices.setFullUsersPrice(totalUserPrice);
        prices.setFullUsersDiscountedPrice(perUserPrice);
        prices.setEssUsersPrice(totalEssUserPrice);
        prices.setEssUsersDiscountedPrice(perEssUserPrice);
        prices.setNonUsersPrice(noAccessUsersTotalPrice);
        prices.setTotalDiscount(totalDiscountPrice);
        prices.setAddonPrice(totalAddOns);
        prices.setTotalSubscription(subscriptionTotalPrice);


        prices.setTotalAmount(subscriptionTotalPrice);

        return prices;
    }


}
