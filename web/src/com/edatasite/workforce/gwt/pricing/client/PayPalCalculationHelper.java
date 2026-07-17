package com.edatasite.workforce.gwt.pricing.client;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * User: Sherali
 * Date: 06.02.2010
 * Time: 15:53:07
 */
public class PayPalCalculationHelper implements Constants {

    public void calculateCostsNEW(SubscriptionPaymentItem subscriptionPaymentItem, final double userRate, final double supportPackagePrice, String hostURL, final double vat_rate, double[][] discount_per_monthly2) {
        subscriptionPaymentItem.storageCost = (/*1*/subscriptionPaymentItem.storage * perStorageCost - perStorageCost) * subscriptionPaymentItem.usageMonths;
        final UsagePlanItem usagePlan = calculateUsagePlanNEW(subscriptionPaymentItem.category,
                subscriptionPaymentItem.usagePeriodID, subscriptionPaymentItem.supportPackage, supportPackagePrice, subscriptionPaymentItem.userCount,
                subscriptionPaymentItem.isGBP, subscriptionPaymentItem.isUK, subscriptionPaymentItem.storageCost, userRate, hostURL, vat_rate, discount_per_monthly2);
        subscriptionPaymentItem.perUserCost = !UsagePlanItem.CUSTOM.equals(subscriptionPaymentItem.category) && !hostURL.contains("1erp.sa") ? (float) userRate / subscriptionPaymentItem.userCount / subscriptionPaymentItem.usageMonths : (float) (userRate);
        subscriptionPaymentItem.tot = usagePlan.getTotalAmount();
        subscriptionPaymentItem.usersCost = subscriptionPaymentItem.tot / subscriptionPaymentItem.userCount / subscriptionPaymentItem.usageMonths;
        subscriptionPaymentItem.taxC = usagePlan.getTax();
        subscriptionPaymentItem.categoryREAL = usagePlan.getCategoryREAL();
        subscriptionPaymentItem.supportPackage = usagePlan.getSupportPackage();
        subscriptionPaymentItem.supportPackageNAME = usagePlan.getSupportPackageNAME();
        subscriptionPaymentItem.supportPackagePrice = usagePlan.getSupportPackagePrice();
    }

    public static UsagePlanItem calculateUsagePlanNEW(String packageType,
                                                      Integer periodID, Integer supportPackageID, double supportPackagePrice,
                                                      Integer userCount, boolean isCurrencyGBP, boolean includeTax, double storageCost,
                                                      double userRate, String hostURL, double vat_rate, double[][] discount_per_monthly2) {
        double subTotalWithoutDiscount = 0d;
        double total = 0d;// get per user cost
        //calculate discount amount
        // get usage months and discount
        // 3 month
        final double discountRate = discount_per_monthly2[0][(periodID == 1) ?
                ((hostURL.contains("smebu.com") || hostURL.contains("tjilo.com") || hostURL.contains("localhost")) ? periodID : ((userCount >= 1 && userCount <= 2) ? 0 : periodID)) : periodID];

        //get pricing package name OR category
        final String pricingPackage = getPricingPackageNAME(packageType);
        //get usage months
        int usageMonth = 0;
        String planType = null;
        if (periodID == 0) {
            usageMonth = 1;
            planType = ONE_MONTH_0;
            // 1 month
        } else if (periodID == 1) {
            usageMonth = 3;
            planType = THREE_MONTH_15;
            // 3 month
        } else if (periodID == 2) {
            usageMonth = 6;
            planType = SIX_MONTH_20;
            // 6 month
        } else if (periodID == 3) {
            usageMonth = 12;
            planType = TWELVE_MONTH_TWENTY_30;
            // 12 month
        }
        String supportPackageN = null;
        if (hostURL.contains("aws.kpi.com") || hostURL.contains("mcloud.kpi.com") || hostURL.contains("app.kpi.com") || hostURL.contains("kpi.com")) {
            //get support package
            supportPackageN = getSupportPackageNAME2(supportPackageID);
        } else {
            supportPackageN = getSupportPackageNAME(supportPackageID);
        }

        double discountedStorageCost = 0d;
        double subTotalBeforeTax = 0d;
        double totalBeforeTax = 0d;
        double subTotal = 0d;
        if (!UsagePlanItem.CUSTOM.equals(packageType) && !hostURL.contains("1erp.sa")) {
            subTotalWithoutDiscount = userRate;
        } else {
            // calculate subtotal without discount
            subTotalWithoutDiscount = userRate * userCount * usageMonth;
            //calculate discount amount
            discountedStorageCost = storageCost * (1 - discountRate);
        }

        subTotalBeforeTax = (subTotalWithoutDiscount * (1 - discountRate)) + discountedStorageCost;
        totalBeforeTax = subTotalBeforeTax + supportPackagePrice;

        //calculate sub total
        subTotal = (includeTax ? subTotalBeforeTax * (1 + vat_rate) : subTotalBeforeTax);
        // calculate total
        // include tax
        total = (includeTax ? totalBeforeTax * (1 + vat_rate) : totalBeforeTax);

        // collect required data
        final UsagePlanItem planItem = new UsagePlanItem();
        planItem.setDiscount((float) (subTotalWithoutDiscount + storageCost - totalBeforeTax));
        planItem.setSubTotalAmount(round((float) subTotal, 2));
        planItem.setTotalAmount(round((float) total, 2));
        planItem.setUserCount(userCount);
        planItem.setTax((float) (total - totalBeforeTax));
        planItem.setUsageMonth(usageMonth);
        planItem.setCurrencyGBP(isCurrencyGBP);
        planItem.setPlanType(planType);
        planItem.setUserRate((float) userRate);
        planItem.setSupportPackageNAME(supportPackageN);
        planItem.setSupportPackage(supportPackageID);
        planItem.setSupportPackagePrice((float) supportPackagePrice);
        planItem.setCategoryREAL(pricingPackage);
        return planItem;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static UsagePlanItem calculateUsagePlan2NEW(Integer userCount, boolean isCurrencyGBP, boolean includeTax,
                                                       double storageCost, double support_package_price, double userRate, String hostURL,
                                                       double vat_rate, double[][] discount_per_monthly2) {
        double subTotalsWithoutDiscountMonthly[];
        double totalsMonthly[];// get per user cost

        // get usage months and discount
        // 3 month
        final double discountRateOneMonth = discount_per_monthly2[0][0];
        final double discountRateThreeMonth = discount_per_monthly2[0][(hostURL.contains("smebu.com") || hostURL.contains("tjilo.com") || hostURL.contains("localhost")) ? 1 : ((userCount >= 1 && userCount <= 2) ? 0 : 1)];//
        final double discountRateSixMonth = discount_per_monthly2[0][2];
        final double discountRateOneYear = discount_per_monthly2[0][3];

        int usageMonths[] = new int[]{1, 3, 6, 12};//1 -- one month, 3 -- three month, 6 -- six month, 12 -- twelve month/one year;
        String planTypesMonthly[] = new String[]{ONE_MONTH_0, THREE_MONTH_15, SIX_MONTH_20, TWELVE_MONTH_TWENTY_30};

        // calculate subtotals without discount
        subTotalsWithoutDiscountMonthly = new double[]{(userRate * userCount * usageMonths[0]), (userRate * userCount * usageMonths[1]),
                (userRate * userCount * usageMonths[2]), (userRate * userCount * usageMonths[3])};

        // calculate discount amount
        final double discountedStorageCostOneMonth = storageCost * (1 - discountRateOneMonth);
        final double discountedStorageCostThreeMonth = storageCost * (1 - discountRateThreeMonth);
        final double discountedStorageCostSixMonth = storageCost * (1 - discountRateSixMonth);
        final double discountedStorageCostOneYear = storageCost * (1 - discountRateOneYear);
        //calculate sub totals before tax per monthly
        final double subTotalBeforeTaxOneMonth = (subTotalsWithoutDiscountMonthly[0] * (1 - discountRateOneMonth)) + discountedStorageCostOneMonth;
        final double subTotalBeforeTaxThreeMonth = (subTotalsWithoutDiscountMonthly[1] * (1 - discountRateThreeMonth)) + discountedStorageCostThreeMonth;
        final double subTotalBeforeTaxSixMonth = (subTotalsWithoutDiscountMonthly[2] * (1 - discountRateSixMonth)) + discountedStorageCostSixMonth;
        final double subTotalBeforeTaxOneYear = (subTotalsWithoutDiscountMonthly[3] * (1 - discountRateOneYear)) + discountedStorageCostOneYear;
        //calculate totals before tax per monthly
        final double totalBeforeTaxOneMonth = subTotalBeforeTaxOneMonth + support_package_price;
        final double totalBeforeTaxThreeMonth = subTotalBeforeTaxThreeMonth + support_package_price;
        final double totalBeforeTaxSixMonth = subTotalBeforeTaxSixMonth + support_package_price;
        final double totalBeforeTaxOneYear = subTotalBeforeTaxOneYear + support_package_price;

        //calculate sub totals per monthly
        double subTotalsMonthly[] = new double[]{(includeTax ? subTotalBeforeTaxOneMonth * (1 + vat_rate) : subTotalBeforeTaxOneMonth),
                (includeTax ? subTotalBeforeTaxThreeMonth * (1 + vat_rate) : subTotalBeforeTaxThreeMonth),
                (includeTax ? subTotalBeforeTaxSixMonth * (1 + vat_rate) : subTotalBeforeTaxSixMonth),
                (includeTax ? subTotalBeforeTaxOneYear * (1 + vat_rate) : subTotalBeforeTaxOneYear)};

        // calculate totals per monthly
        // include tax
        totalsMonthly = new double[]{(includeTax ? totalBeforeTaxOneMonth * (1 + vat_rate) : totalBeforeTaxOneMonth),
                (includeTax ? totalBeforeTaxThreeMonth * (1 + vat_rate) : totalBeforeTaxThreeMonth),
                (includeTax ? totalBeforeTaxSixMonth * (1 + vat_rate) : totalBeforeTaxSixMonth),
                (includeTax ? totalBeforeTaxOneYear * (1 + vat_rate) : totalBeforeTaxOneYear)};

        // collect required data
        final UsagePlanItem planItem = new UsagePlanItem();
//		planItem.setDiscount((float) (subTotalWithoutDiscount + storageCost - totalBeforeTax));
        planItem.setDiscountsMonthly(new float[]{((float) (subTotalsWithoutDiscountMonthly[0] + storageCost - totalBeforeTaxOneMonth)),
                ((float) (subTotalsWithoutDiscountMonthly[1] + storageCost - totalBeforeTaxThreeMonth)), ((float) (subTotalsWithoutDiscountMonthly[2] + storageCost - totalBeforeTaxSixMonth)),
                ((float) (subTotalsWithoutDiscountMonthly[3] + storageCost - totalBeforeTaxOneYear))});
        planItem.setSubTotalAmountsMonthly(new float[]{((float) subTotalsMonthly[0]), ((float) subTotalsMonthly[1]), ((float) subTotalsMonthly[2]), ((float) subTotalsMonthly[3])});
        planItem.setTotalAmountsMonthly(new float[]{((float) totalsMonthly[0]), ((float) totalsMonthly[1]), ((float) totalsMonthly[2]), ((float) totalsMonthly[3])});
        planItem.setUserCount(userCount);
        planItem.setTaxMonthly(new float[]{((float) (totalsMonthly[0] - totalBeforeTaxOneMonth)), ((float) (totalsMonthly[1] - totalBeforeTaxThreeMonth)),
                ((float) (totalsMonthly[2] - totalBeforeTaxSixMonth)), ((float) (totalsMonthly[3] - totalBeforeTaxOneYear))});
        planItem.setUsageMonths(usageMonths);
        planItem.setCurrencyGBP(isCurrencyGBP);
        planItem.setPlanTypesMonthly(planTypesMonthly);
        planItem.setUserRate((float) userRate);
        planItem.setSupportPackagePrice((float) support_package_price);
        return planItem;
    }


    /**
     * Get support package name
     *
     * @param supportPackageID - support package ID //0, or 1, or 2, or 3, or 4, or 5, or 6
     * @return - support package name
     */
    public static String getSupportPackageNAME(Integer supportPackageID) {
        if (supportPackageID == null) {
            return null;
        }
        //get support package name
        String supportPackageN = null;
        if (supportPackageID == 0) {
            supportPackageN = SP_BASIC;
            //support package - basic
        } else if (supportPackageID == 1) {
            supportPackageN = SP_STANDARD;
            //support package - standard
        } else if (supportPackageID == 2) {
            supportPackageN = SP_BRONZE;
            //support package - bronze
        } else if (supportPackageID == 3) {
            supportPackageN = SP_SILVER;
            //support package - silver
        } else if (supportPackageID == 4) {
            supportPackageN = SP_GOLD;
            //support package - gold
        } else if (supportPackageID == 5) {
            supportPackageN = SP_PLATINUM;
            //support package - platinum
        } else if (supportPackageID == 6) {
            supportPackageN = SP_DIAMOND;
            //support package - diamond
        }
        return supportPackageN;
    }

    public static String getSupportPackageNAME2(Integer supportPackageID) {
        if (supportPackageID == null) {
            return null;
        }
        String supportPackageN = null;
        if (supportPackageID == 0) {
            supportPackageN = SP_CONTRACTOR;
        } else if (supportPackageID == 1) {
            supportPackageN = SP_SMALL_BUSINESS;
        } else if (supportPackageID == 2) {
            supportPackageN = SP_PROFESSIONAL;
        } else if (supportPackageID == 3) {
            supportPackageN = SP_ENTERPRISE;
        }
        return supportPackageN;
    }

    /**
     * Get support package ID
     *
     * @param supportPackageNAME - support package NAME //0, or 1, or 2, or 3, or 4, or 5, or 6
     * @return - support package ID
     */
    public static Integer getSupportPackageID(String supportPackageNAME) {
        //get support package name
        Integer supportPackageID = -1;
        if (SP_CONTRACTOR.equals(supportPackageNAME)) {
            supportPackageID = 0;
            //support package - CONTRACTOR
        } else if (SP_SMALL_BUSINESS.equals(supportPackageNAME)) {
            supportPackageID = 1;
            //support package - SMALL_BUSINESS
        } else if (SP_PROFESSIONAL.equals(supportPackageNAME)) {
            supportPackageID = 2;
            //support package - PROFESSIONAL
        } else if (SP_ENTERPRISE.equals(supportPackageNAME)) {
            supportPackageID = 3;
            //support package - silver
        }
        return supportPackageID;
    }

    /**
     * Get support package price
     *
     * @param selectedSupportPackageNAME - selected support package name
     * @param supportPackagePrices       - all support package prices
     * @return - selected support package price
     */
    public static double getSupportPackagePRICE(String selectedSupportPackageNAME, HashMap<String, Double> supportPackagePrices) {
        double supportPackagePrice = 0d;
        if (selectedSupportPackageNAME != null && supportPackagePrices != null && supportPackagePrices.containsKey(selectedSupportPackageNAME)) {
            supportPackagePrice = supportPackagePrices.get(selectedSupportPackageNAME);
        }
        return supportPackagePrice;
    }

    /**
     * Get pricing package name OR category
     *
     * @param packageType - category
     * @return pricing package name --> PP_SMALL_BUSINESS or PP_KPI_PRO or PP_ENTERPRISE
     */
    public static String getPricingPackageNAME(String packageType) {
        if (packageType == null) {
            return null;
        }
        String pricePackageN = null;
        if (PLUS_SIGN.equals(packageType)) {
            pricePackageN = PP_SMALL_BUSINESS;
        } else if (PREMIUM_SIGN.equals(packageType)) {
            pricePackageN = PP_KPI_PRO;
        } else if (MAX_SIGN.equals(packageType)) {
            pricePackageN = PP_ENTERPRISE;
        } else if (MINI_SIGN.equals(packageType)) {
            pricePackageN = PP_MINI;
        } else if (SMALL_SIGN.equals(packageType)) {
            pricePackageN = PP_SMALL;
        } else if (STANDARD_SIGN.equals(packageType)) {
            pricePackageN = PP_STANDART;
        } else if (SILVER_SIGN.equals(packageType)) {
            pricePackageN = PP_SILVER;
        } else if (ENTERPRISE_SIGN.equals(packageType)) {
            pricePackageN = PP_ENTERPRISE2;
        } else if (ESSENTIAL_SIGN.equals(packageType)) {
            pricePackageN = PP_ESENTIAL;
        }else if (MINI_SIGN_ANNUAL.equals(packageType)) {
            pricePackageN = PP_MINI_ANNUAL;
        } else if (SMALL_SIGN_ANNUAL.equals(packageType)) {
            pricePackageN = PP_SMALL_ANNUAL;
        } else if (STANDARD_SIGN_ANNUAL.equals(packageType)) {
            pricePackageN = PP_STANDART_ANNUAL;
        } else if (SILVER_SIGN_ANNUAL.equals(packageType)) {
            pricePackageN = PP_SILVER_ANNUAL;
        } else if (ENTERPRISE_SIGN_ANNUAL.equals(packageType)) {
            pricePackageN = PP_ENTERPRISE2_ANNUAL;
        } else if (PP_BRONZE_.equals(packageType) ||
                   PP_SILVER_.equals(packageType) ||
                   PP_GOLDEN.equals(packageType) ||
                   PP_CUSTOM.equals(packageType)) {
            return packageType;
        }
        return pricePackageN;
    }

    public static String getModuleNAME(String modules) {
        if (modules == null) {
            return null;
        }
        StringBuilder s = new StringBuilder();
        String[] module = modules.split(",");
        for (String m : module) {
            if (s.length() > 0) {
                s.append(',');
            }
            if (Constants.MODULE_PM.equals(m)) {
                s.append("'");
                s.append(PermissionConstants.PM_MODULE);
                s.append("'");
            } else if (Constants.MODULE_CRM.equals(m)) {
                s.append("'");
                s.append(PermissionConstants.CRM_MODULE);
                s.append("'");
            } else if (Constants.MODULE_HRMS.equals(m)) {
                s.append("'");
                s.append(PermissionConstants.HRMS_MODULE);
                s.append("'");
            } else if (Constants.MODULE_ACCOUNTING.equals(m)) {
                s.append("'");
                s.append(PermissionConstants.ACCOUNTING_MODULE);
                s.append("'");
            } else if (Constants.MODULE_PAYROLL.equals(m)) {
                s.append("'");
                s.append(PermissionConstants.PAYROLL);
                s.append("'");
            }
        }
        return s.toString();
    }

    /**
     * Get pricing category name
     *
     * @param packageNAME - pricing package name
     * @return pricing category name --> PLUS_SIGN or PREMIUM_SIGN or MAX_SIGN
     */
    public static String getPricingCategoryNAME(String packageNAME) {
        return PP_SMALL_BUSINESS.equals(packageNAME) ? PLUS_SIGN : PP_KPI_PRO.equals(packageNAME) ? PREMIUM_SIGN : MAX_SIGN;
    }

    public void calculateCosts(SubscriptionPaymentItem subscriptionPaymentItem, final double userRate, String hostURL, final double vat_rate, double[][] discount_per_monthly2) {
        subscriptionPaymentItem.storageCost = (/*1*/subscriptionPaymentItem.storage * perStorageCost - perStorageCost) * subscriptionPaymentItem.usageMonths;
        final UsagePlanItem usagePlan = calculateUsagePlan(subscriptionPaymentItem.usagePeriodID,
                subscriptionPaymentItem.userCount, subscriptionPaymentItem.isGBP, subscriptionPaymentItem.isUK, subscriptionPaymentItem.storageCost, userRate,
                hostURL, vat_rate, discount_per_monthly2);
        subscriptionPaymentItem.perUserCost = (float) (userRate);
        subscriptionPaymentItem.tot = usagePlan.getTotalAmount();
        subscriptionPaymentItem.usersCost = subscriptionPaymentItem.tot / subscriptionPaymentItem.userCount / subscriptionPaymentItem.usageMonths;
        subscriptionPaymentItem.taxC = usagePlan.getTax();
        subscriptionPaymentItem.discounts = usagePlan.getDiscount();
    }


    public static UsagePlanItem calculateUsagePlan(Integer periodID, Integer userCount,
                                                   boolean isCurrencyGBP, boolean includeTax, double storageCost,
                                                   double userRate, String hostURL, double vat_rate, double[][] discount_per_monthly2) {
        double subTotalWithoutDiscount;
        double total;// get per user cost

        // get usage months and discount
        // 3 month
        //final int packageIndex = BASIC_SIGN.equals(packageType) ? 0 : PLUS_SIGN.equals(packageType) ? 1 : 2;
        final double discountRate = discount_per_monthly2[0][(periodID == 1) ?
                ((hostURL.contains("smebu.com") || hostURL.contains("tjilo.com") || hostURL.contains("localhost")) ? periodID : ((userCount >= 1 && userCount <= 2) ? 0 : periodID)) : periodID];

        int usageMonth = 0;
        String planType = null;
        if (periodID == 0) {
            usageMonth = 1;
            planType = ONE_MONTH_0;
            // 1 month
        } else if (periodID == 1) {
            usageMonth = 3;
            planType = THREE_MONTH_15;
            // 3 month
        } else if (periodID == 2) {
            usageMonth = 6;
            planType = SIX_MONTH_20;
            // 6 month
        } else if (periodID == 3) {
            usageMonth = 12;
            planType = TWELVE_MONTH_TWENTY_30;
            // 12 month
        }

        // calculate subtotal without discount
        subTotalWithoutDiscount = userRate * userCount * usageMonth;

        // calculate discount amount
        final double discountedStorageCost = storageCost * (1 - discountRate);
        final double totalBeforeTax = (subTotalWithoutDiscount * (1 - discountRate)) + discountedStorageCost;

        // calculate total
        // include tax
        total = includeTax ? totalBeforeTax * (1 + vat_rate) : totalBeforeTax;

        // collect required data
        final UsagePlanItem planItem = new UsagePlanItem();
        planItem.setDiscount((float) (subTotalWithoutDiscount + storageCost - totalBeforeTax));
        planItem.setTotalAmount((float) total);
        planItem.setUserCount(userCount);
        planItem.setTax((float) (total - totalBeforeTax));
        planItem.setUsageMonth(usageMonth);
        planItem.setCurrencyGBP(isCurrencyGBP);
        planItem.setPlanType(planType);
        planItem.setUserRate((float) userRate);
        return planItem;
    }

    public static double getUserRate(Integer userCount) {
        double price_per_user = 0d;

        if (userCount == 1) {
            price_per_user = 15.99;
        } else if (userCount == 2) {
            price_per_user = 12.99;
        } else if (userCount == 3) {
            price_per_user = 10.99;
        } else if (userCount == 4 || userCount == 5) {
            price_per_user = 9.99;
        } else if (userCount >= 6 && userCount <= 10) {
            price_per_user = 8.99;
        } else if (userCount > 10 && userCount <= 15) {
            price_per_user = 7.99;
        } else if (userCount > 15 && userCount <= 25) {
            price_per_user = 6.99;
        } else if (userCount > 25 && userCount <= 100) {
            price_per_user = 5.99;
        }
        return price_per_user;
    }

    public static UsagePlanItem calculateUsagePlan2(Integer userCount, boolean isCurrencyGBP, boolean includeTax,
                                                    double storageCost, double userRate, String hostURL,
                                                    double vat_rate, double[][] discount_per_monthly2) {
        double subTotalsWithoutDiscountMonthly[];
        double totalsMonthly[];// get per user cost

        // get usage months and discount
        // 3 month
        final double discountRateOneMonth = discount_per_monthly2[0][0];
        final double discountRateThreeMonth = discount_per_monthly2[0][(hostURL.contains("smebu.com") || hostURL.contains("tjilo.com") || hostURL.contains("localhost")) ? 1 : ((userCount >= 1 && userCount <= 2) ? 0 : 1)];//
        final double discountRateSixMonth = discount_per_monthly2[0][2];
        final double discountRateOneYear = discount_per_monthly2[0][3];

        int usageMonths[] = new int[]{1, 3, 6, 12};//1 -- one month, 3 -- three month, 6 -- six month, 12 -- twelve month/one year;
        String planTypesMonthly[] = new String[]{ONE_MONTH_0, THREE_MONTH_15, SIX_MONTH_20, TWELVE_MONTH_TWENTY_30};

        // calculate subtotals
        subTotalsWithoutDiscountMonthly = new double[]{(userRate * userCount * usageMonths[0]), (userRate * userCount * usageMonths[1]),
                (userRate * userCount * usageMonths[2]), (userRate * userCount * usageMonths[3])};

        // calculate discount amount
        final double discountedStorageCostOneMonth = storageCost * (1 - discountRateOneMonth);
        final double discountedStorageCostThreeMonth = storageCost * (1 - discountRateThreeMonth);
        final double discountedStorageCostSixMonth = storageCost * (1 - discountRateSixMonth);
        final double discountedStorageCostOneYear = storageCost * (1 - discountRateOneYear);

        final double totalBeforeTaxOneMonth = (subTotalsWithoutDiscountMonthly[0] * (1 - discountRateOneMonth)) + discountedStorageCostOneMonth;
        final double totalBeforeTaxThreeMonth = (subTotalsWithoutDiscountMonthly[1] * (1 - discountRateThreeMonth)) + discountedStorageCostThreeMonth;
        final double totalBeforeTaxSixMonth = (subTotalsWithoutDiscountMonthly[2] * (1 - discountRateSixMonth)) + discountedStorageCostSixMonth;
        final double totalBeforeTaxOneYear = (subTotalsWithoutDiscountMonthly[3] * (1 - discountRateOneYear)) + discountedStorageCostOneYear;

        // calculate totals per monthly
        // include tax
        totalsMonthly = new double[]{(includeTax ? totalBeforeTaxOneMonth * (1 + vat_rate) : totalBeforeTaxOneMonth),
                (includeTax ? totalBeforeTaxThreeMonth * (1 + vat_rate) : totalBeforeTaxThreeMonth),
                (includeTax ? totalBeforeTaxSixMonth * (1 + vat_rate) : totalBeforeTaxSixMonth),
                (includeTax ? totalBeforeTaxOneYear * (1 + vat_rate) : totalBeforeTaxOneYear)};

        // collect required data
        final UsagePlanItem planItem = new UsagePlanItem();
        planItem.setDiscountsMonthly(new float[]{((float) (subTotalsWithoutDiscountMonthly[0] + storageCost - totalBeforeTaxOneMonth)),
                ((float) (subTotalsWithoutDiscountMonthly[1] + storageCost - totalBeforeTaxThreeMonth)), ((float) (subTotalsWithoutDiscountMonthly[2] + storageCost - totalBeforeTaxSixMonth)),
                ((float) (subTotalsWithoutDiscountMonthly[3] + storageCost - totalBeforeTaxOneYear))});
        planItem.setTotalAmountsMonthly(new float[]{((float) totalsMonthly[0]), ((float) totalsMonthly[1]), ((float) totalsMonthly[2]), ((float) totalsMonthly[3])});
        planItem.setUserCount(userCount);
        planItem.setTaxMonthly(new float[]{((float) (totalsMonthly[0] - totalBeforeTaxOneMonth)), ((float) (totalsMonthly[1] - totalBeforeTaxThreeMonth)),
                ((float) (totalsMonthly[2] - totalBeforeTaxSixMonth)), ((float) (totalsMonthly[3] - totalBeforeTaxOneYear))});
        planItem.setUsageMonths(usageMonths);
        planItem.setCurrencyGBP(isCurrencyGBP);
        planItem.setPlanTypesMonthly(planTypesMonthly);
        planItem.setUserRate((float) userRate);
        return planItem;
    }
    //----------- OLD VERSION -----------////----------- OLD VERSION -----------////----------- OLD VERSION -----------//
    //For 1erp.sa custom calculation.  We didn't create separate table in order to retrieve date from database.
    public double getTotalPrice(Integer periodID, Integer userCount, Integer moduleLimit) {
        double userRate = 0;
        if (2 == moduleLimit) {
            if (userCount > 0 && userCount < 10) {
                if (periodID == 1) {
                    userRate = 45;
                } else if (periodID == 3) {
                    userRate = 43;
                }
            } else if (userCount > 9 && userCount < 50) {
                if (periodID == 1) {
                    userRate = 42;
                } else if (periodID == 3) {
                    userRate = 40;
                }
            } else if (userCount > 49) {
                if (periodID == 1) {
                    userRate = 40;
                } else if (periodID == 3) {
                    userRate = 38;
                }
            }
        } else if (3 == moduleLimit) {
            if (userCount > 0 && userCount < 10) {
                if (periodID == 1) {
                    userRate = 60;
                } else if (periodID == 3) {
                    userRate = 57;
                }
            } else if (userCount > 9 && userCount < 50) {
                if (periodID == 1) {
                    userRate = 55;
                } else if (periodID == 3) {
                    userRate = 52;
                }
            } else if (userCount > 49) {
                if (periodID == 1) {
                    userRate = 50;
                } else if (periodID == 3) {
                    userRate = 47;
                }
            }
        } else {
            if (userCount > 0 && userCount < 10) {
                if (periodID == 1) {
                    userRate = 75;
                } else if (periodID == 3) {
                    userRate = 71;
                }
            } else if (userCount > 9 && userCount < 50) {
                if (periodID == 1) {
                    userRate = 65;
                } else if (periodID == 3) {
                    userRate = 62;
                }
            } else if (userCount > 49) {
                if (periodID == 1) {
                    userRate = 55;
                } else if (periodID == 3) {
                    userRate = 52;
                }
            }
        }
        return userRate;
    }
}
