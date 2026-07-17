package com.edatasite.workforce.gwt.myaccount.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.landing.WestPanelHelp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.myaccount.client.bundles.MyaccountImageBundles;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountStrings;
import com.edatasite.workforce.gwt.myaccount.client.rpc.MyAccountService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.RequestQuoteItem;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.pricing.client.PayPalCalculationHelper;
import com.edatasite.workforce.gwt.pricing.client.SubscriptionPaymentItem;
import com.edatasite.workforce.gwt.pricing.client.UserRateItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 1/12/12
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class PricingView2 extends View implements Constants {
    interface PricingView2UiBinder extends UiBinder<HTMLPanel, PricingView2> {
    }

    //variables
    
    private static final MyAccountStrings myAccountStrings = MyAccountStrings.App.get();

    private UsagePlanItem afterCalculateUsagePlanItem_u;
    private String currencyValue = Utils.getCurrencyCODEbyHOST();
    private String currentCategory;
    private Integer currentUserCount;
    private Integer currentUsagePeriod = 0;
    private int currentUsageMonth = 0;
    private Integer registeredUsersCount;

    private InitHelpContainer initHelpContainer;
    private reCalculateUserRatePER_H reCalculateUserRatePER_h;
    private reCalculateUserRatePER_H2 reCalculateUserRatePER_h2;

    private boolean isGetQuote = false;

    private boolean isUKClient = false;

    private boolean isUpgrade = false;

    private final SubscriptionPaymentItem upgSubscriptionPaymentItem = new SubscriptionPaymentItem();
    private HTML serviceType;
    private Integer subHisID;
    private Button upgradePayBt;

    /*---------------------------------*/
    final String cmd = "cmd";                       //_xclick-subscriptions;
    final String business = "business";               //sales@workforcetrack.com
    final String currency_code = "currency_code";  //USD
    final String amount = "amount";                   //25$
    final String item_name = "item_name";           //
    final String item_number = "item_number";       //
    final String txn_type = "txn_type";               //
    final String custom = "custom";                   //
    final String taxX = "tax";                       //
    final String a3 = "a3";                           //5.00
    final String p3 = "p3";                           //1  (1,3,6,12 - Months)
    final String t3 = "t3";                           //M (Month)
    final String src = "src";                       //1,2,3 (Limit the number of billing cycles.)
    final String cancel_return = "cancel_return";  //
    final String returnT = "return";               //
    final String modify = "modify";                   //
    /*---------------------------------*/

    //UI fields
    @UiField
    HTMLPanel header;
    @UiField
    HTMLPanel headerDIV;
    @UiField
    SpanElement iWantToUseKPIDOTCOM;
    @UiField
    DataListBox usersSelectItem;
    @UiField
    SpanElement selectedUSERS;
    //
    @UiField
    TableCellElement oneMonth;
    @UiField
    TableCellElement threeMonth;
    @UiField
    TableCellElement sixMonth;
    @UiField
    TableCellElement oneYear;
    //
    @UiField
    HTML forUKBasedVAT;
    @UiField
    KpiCheckBox taxCheck;
    //
    @UiField
    Button buyNowOneMonth;
    @UiField
    Button buyNowThreeMonth;
    @UiField
    Button buyNowSixMonth;
    @UiField
    Button buyNowOneYear;
    //
    @UiField
    TableCellElement priceAfterDiscount;
    @UiField
    HTML priceAfterDiscountPerUserOneMonth;
    @UiField
    HTML priceAfterDiscountPerUserThreeMonth;
    @UiField
    HTML priceAfterDiscountPerUserSixMonth;
    @UiField
    HTML priceAfterDiscountPerUserOneYear;
    @UiField
    HTML totalPricePerUserOneMonth;
    @UiField
    HTML totalPricePerUserThreeMonth;
    @UiField
    HTML totalPricePerUserSixMonth;
    @UiField
    HTML totalPricePerUserOneYear;
    //
    @UiField
    TableCellElement pricePerUserMonth;
    @UiField
    HTML pricePerUserMonthOneMonth;
    @UiField
    HTML pricePerUserMonthThreeMonth;
    @UiField
    HTML pricePerUserMonthSixMonth;
    @UiField
    HTML pricePerUserMonthOneYear;
    //
    @UiField
    TableCellElement totalAmountPerUser;
    @UiField
    HTML totalAmountTotalUserPerOneMonth;
    @UiField
    HTML totalAmountTotalUserPerThreeMonth;
    @UiField
    HTML totalAmountTotalUserPerSixMonth;
    @UiField
    HTML totalAmountTotalUserPerOneYear;
    //
    @UiField
    TableCellElement discount;
    @UiField
    HTML discountPercentPerOneMonth;
    @UiField
    HTML discountPercentPerThreeMonth;
    @UiField
    HTML discountPercentPerSixMonth;
    @UiField
    HTML discountPercentPerOneYear;

    public PricingView2() {
        super("pricingView", wfmStrings.currentSubscription());
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public ImageResource getIconImage() {
        return MyaccountImageBundles.App.get().currentSubscription();
    }

    @Override
    protected Widget onInitialize() {
        PricingView2UiBinder ourUiBinder = GWT.create(PricingView2UiBinder.class);
        add(ourUiBinder.createAndBindUi(this));
        initialize();
        return null;
    }

    private void calculatedUserRatePerHOST(final DataListBox userBox) {
        if (userBox.getSelectedItem() == null) {
            return;
        }
        //get user count
        final Integer userCount;
        try {
            userCount = Integer.valueOf(userBox.getSelectedItem().getName());
        } catch (NumberFormatException e) {
            return;
        }
        MyAccountService.App.get().getUserDiscount(userCount, Utils.getHostName(), new AbstractAsyncCallback<UserRateItem>() {
            @Override
            public void success(UserRateItem result) {
                calculateAll(userCount, result);
            }
        });
    }

    private UsagePlanItem calculateClickedMonth(UserRateItem userRateItem, final Integer userCount, Integer clickedPeriodID) {
        //no calculation
        final boolean currencyGBR = isUKClient;

        final boolean includeTax = taxCheck.getValue();
        final float userRate = userRateItem.getUserRate().floatValue();
        final double vat_rateN = Utils.getVAT_RATE() != null ? Utils.getVAT_RATE() : VAT_RATE;//default UK based VAT RATE
        final double[][] discount_per_monthly2 = {{userRateItem.getDiscountOneMonth(), userRateItem.getDiscountThreeMonth(),
                userRateItem.getDiscountSixMonth(), userRateItem.getDiscountTwentyMonth()}}; // Example: (1 month - not discount), 3 month - 15%, 6 month - 25%, 12 month - 30%;
        return PayPalCalculationHelper.calculateUsagePlan(clickedPeriodID, userCount, currencyGBR, includeTax, 0, userRate, Utils.getHostName(), vat_rateN, discount_per_monthly2);
    }

    private String getNumberFormatWithBigDecimal(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toString();
    }

    private UsagePlanItem calculateAll(Integer userCount, UserRateItem userRateItem) {
        //no calculation
        final boolean currencyGBR = isUKClient;

        final boolean includeTax = taxCheck.getValue();
        final double vat_rateN = Utils.getVAT_RATE() != null ? Utils.getVAT_RATE() : VAT_RATE;//default UK based VAT RATE
        final double userRate = userRateItem.getUserRate();
        final double[][] discount_per_monthly2 = {{userRateItem.getDiscountOneMonth(), userRateItem.getDiscountThreeMonth(),
                userRateItem.getDiscountSixMonth(), userRateItem.getDiscountTwentyMonth()}}; // Example: (1 month - not discount), 3 month - 15%, 6 month - 25%, 12 month - 30%;
        final UsagePlanItem planItem = PayPalCalculationHelper.calculateUsagePlan2(userCount, currencyGBR, includeTax, 0, userRate, Utils.getHostName(), vat_rateN, discount_per_monthly2);
        // show total in view
        String includeVAT = "";
        if (includeTax) {
            includeVAT = "<br/><em style='font-size:9px;'> (" + wfmStrings.vatIncluded() + ")</em>";    //#007dc3
        }
        final String UKorUSD = Utils.getHTMLCODESForCurrency(currencyGBR ? "GBP" : Utils.getCurrencyCODEbyHOST());


        //discount percent calculate per user to three month
        if ((discount_per_monthly2[0][0] * 100) > 0) {
            discountPercentPerOneMonth.setHTML((int) (discount_per_monthly2[0][0] * 100) + "%");//percent for one month
        } else {
            discountPercentPerOneMonth.setHTML("");
        }
        if (Utils.getHostName().contains("smebu.com") || Utils.getHostName().contains("tjilo.com") || Utils.getHostName().contains("localhost")) {
            discountPercentPerThreeMonth.setHTML((int) (discount_per_monthly2[0][1] * 100) + "%");//15%
        } else {
            if (userCount >= 1 && userCount <= 2) {
                discountPercentPerThreeMonth.setHTML("");
            } else {
                discountPercentPerThreeMonth.setHTML((int) (discount_per_monthly2[0][1] * 100) + "%");//15%
            }
        }
        discountPercentPerSixMonth.setHTML((int) (discount_per_monthly2[0][2] * 100) + "%");//25%
        discountPercentPerOneYear.setHTML((int) (discount_per_monthly2[0][3] * 100) + "%");//30%
        totalAmountPerUser.setInnerHTML(wfmStrings.totalAmount() + " (" + userCount + " " + wfmStrings.users().toLowerCase() + ")");

        // no calculation
        final String totalSpan = wfmStrings.spanTotalSpan() + " ";
        final String perUserSpan = wfmStrings.spanPerUserSpan() + " ";
        if (userCount == 0) {
            return null;
        } else if (userCount >= 1 && userCount <= 5) {
            //price after discount per user
            priceAfterDiscountPerUserOneMonth.setHTML(wfmStrings.notAvailable());
            //total price after discount per user
            totalPricePerUserOneMonth.setHTML(wfmStrings.notAvailable());
            //price per user
            pricePerUserMonthOneMonth.setHTML(wfmStrings.notAvailable());
            //total amount
            totalAmountTotalUserPerOneMonth.setHTML(wfmStrings.notAvailable());
            //register disabled option
            registerDisabledOptionForBetween1to5();
            isGetQuote = false;
        } else if (userCount >= 6 && userCount <= userRateItem.getMaxPayableUserCount()/*30*/) {
            priceAfterDiscountPerUserOneMonth.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getTotalAmountsMonthly()[0] / planItem.getUserCount() / planItem.getUsageMonths()[0]) + perUserSpan + includeVAT);
            //total price after discount per user
            totalPricePerUserOneMonth.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getTotalAmountsMonthly()[0]) + totalSpan + includeVAT);
            //price per user
            pricePerUserMonthOneMonth.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getUserRate()));
            //total amount
            totalAmountTotalUserPerOneMonth.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getUserRate() * planItem.getUserCount() * planItem.getUsageMonths()[0]));
            //register disabled option
            registerDisabledOptionForBetween6to30();
            isGetQuote = false;
        } else if (userCount >= userRateItem.getMaxPayableUserCount() + 1/*31*/) {
            //register disabled option
            registerDisabledOptionForGreater31();
            isGetQuote = true;
        }

        //price after discount per user
        if (userCount >= 1 && userCount <= userRateItem.getMaxPayableUserCount()/*30*/) {
            priceAfterDiscountPerUserThreeMonth.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getTotalAmountsMonthly()[1] / planItem.getUserCount() / planItem.getUsageMonths()[1]) + perUserSpan + includeVAT);
            priceAfterDiscountPerUserSixMonth.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getTotalAmountsMonthly()[2] / planItem.getUserCount() / planItem.getUsageMonths()[2]) + perUserSpan + includeVAT);
            priceAfterDiscountPerUserOneYear.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getTotalAmountsMonthly()[3] / planItem.getUserCount() / planItem.getUsageMonths()[3]) + perUserSpan + includeVAT);
            //total price after discount per user
            totalPricePerUserThreeMonth.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getTotalAmountsMonthly()[1]) + totalSpan + includeVAT);
            totalPricePerUserSixMonth.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getTotalAmountsMonthly()[2]) + totalSpan + includeVAT);
            totalPricePerUserOneYear.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getTotalAmountsMonthly()[3]) + totalSpan + includeVAT);
            //price per user
            pricePerUserMonthThreeMonth.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getUserRate()));
            pricePerUserMonthSixMonth.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getUserRate()));
            pricePerUserMonthOneYear.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getUserRate()));
            //total amount
            totalAmountTotalUserPerThreeMonth.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getUserRate() * planItem.getUserCount() * planItem.getUsageMonths()[1]));
            totalAmountTotalUserPerSixMonth.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getUserRate() * planItem.getUserCount() * planItem.getUsageMonths()[2]));
            totalAmountTotalUserPerOneYear.setHTML(UKorUSD + getNumberFormatWithBigDecimal(planItem.getUserRate() * planItem.getUserCount() * planItem.getUsageMonths()[3]));
        } else {
            priceAfterDiscountPerUserOneMonth.setHTML(wfmStrings.negotiable());
            priceAfterDiscountPerUserThreeMonth.setHTML(wfmStrings.negotiable());
            priceAfterDiscountPerUserSixMonth.setHTML(wfmStrings.negotiable());
            priceAfterDiscountPerUserOneYear.setHTML(wfmStrings.negotiable());
            //total price after discount per user
            totalPricePerUserOneMonth.setHTML(wfmStrings.negotiable());
            totalPricePerUserThreeMonth.setHTML(wfmStrings.negotiable());
            totalPricePerUserSixMonth.setHTML(wfmStrings.negotiable());
            totalPricePerUserOneYear.setHTML(wfmStrings.negotiable());
            //price per user
            pricePerUserMonthOneMonth.setHTML(wfmStrings.negotiable());
            pricePerUserMonthThreeMonth.setHTML(wfmStrings.negotiable());
            pricePerUserMonthSixMonth.setHTML(wfmStrings.negotiable());
            pricePerUserMonthOneYear.setHTML(wfmStrings.negotiable());
            //total amount
            totalAmountTotalUserPerOneMonth.setHTML(wfmStrings.negotiable());
            totalAmountTotalUserPerThreeMonth.setHTML(wfmStrings.negotiable());
            totalAmountTotalUserPerSixMonth.setHTML(wfmStrings.negotiable());
            totalAmountTotalUserPerOneYear.setHTML(wfmStrings.negotiable());
        }

        return planItem;
    }

    private void drawUsagePlanItem(final UsagePlanItem result_usagePlanItem) {
        final SubscriptionPaymentItem subscriptionPaymentItem = new SubscriptionPaymentItem();
        final HTML upgHtml = new HTML();
        final HorizontalPanelDiv shp = new HorizontalPanelDiv();
        shp.setTextAlign("right");
        upgradePayBt = new Button(myAccountStrings.payNow());
        upgradePayBt.setStyleName("button");

        //
        currentUsageMonth = result_usagePlanItem.getUsageMonth();
        currentUserCount = result_usagePlanItem.getUserCount();
        isUKClient = result_usagePlanItem.isCurrencyGBP();
        registeredUsersCount = result_usagePlanItem.getRegisteredUsersCount();

        if (currentUsageMonth == 0) {
            currentUsagePeriod = -1;//      free trial usage month
        } else if (currentUsageMonth == 1) {
            currentUsagePeriod = 0;//       one month
        } else if (currentUsageMonth == 3) {
            currentUsagePeriod = 1;//0      three month
        } else if (currentUsageMonth == 6) {
            currentUsagePeriod = 2;//1      six month
        } else if (currentUsageMonth == 12) {
            currentUsagePeriod = 3;//2      12 month
        }

        //buy now buttons -/- get quote buttons -/- upgrade buttons -/
        registerBuyNowButtonListener(result_usagePlanItem, buyNowOneMonth, 0, "1 month");    //0 -> 1 month
        registerBuyNowButtonListener(result_usagePlanItem, buyNowThreeMonth, 1, "3 months");  //1 -> 3 month
        registerBuyNowButtonListener(result_usagePlanItem, buyNowSixMonth, 2, "6 months");    //2 -> 6 month
        registerBuyNowButtonListener(result_usagePlanItem, buyNowOneYear, 3, "12 months");     //3 -> 12 month/1 year

        //initialize isUpgrade
        if (!result_usagePlanItem.isFree() && !result_usagePlanItem.isPaid()) {
            isUpgrade = false;
        } else if (!result_usagePlanItem.isFree() && result_usagePlanItem.isShowUpgBt()) {
            isUpgrade = true;
        }

        if (currentUserCount >= 1 && currentUserCount <= 5) {
            //
            buyNowOneMonth.setEnabled(false);
        }

        //
        currencyValue = isUKClient ? "GBP" : Utils.getCurrencyCODEbyHOST();
        serviceType.setHTML(result_usagePlanItem.getService());

        if (!result_usagePlanItem.isUpgPayed()) {
            //
            buyNowOneMonth.setEnabled(false);
            buyNowThreeMonth.setEnabled(false);
            buyNowSixMonth.setEnabled(false);
            buyNowOneYear.setEnabled(false);
            //
            final PayPalCalculationHelper upgPayPalCalculationHelper = new PayPalCalculationHelper();
            upgSubscriptionPaymentItem.usageMonths = currentUsageMonth;
            upgSubscriptionPaymentItem.isGBP = isUKClient;
            upgSubscriptionPaymentItem.storage = result_usagePlanItem.getUpgStorageCount();
            upgSubscriptionPaymentItem.userCount = result_usagePlanItem.getUpgUserCount();
            upgSubscriptionPaymentItem.up = result_usagePlanItem.getCostDown();
            upgSubscriptionPaymentItem.serviceType = result_usagePlanItem.isAllService();

            subscriptionPaymentItem.usageMonths = currentUsageMonth;
            subscriptionPaymentItem.isGBP = isUKClient;
            subscriptionPaymentItem.storage = result_usagePlanItem.getStorageCount();
            subscriptionPaymentItem.userCount = result_usagePlanItem.getUserCount();
            subscriptionPaymentItem.up = result_usagePlanItem.getCostDown();
            subscriptionPaymentItem.serviceType = result_usagePlanItem.isAllService();
            subscriptionPaymentItem.usagePeriodID = currentUsagePeriod;
            upgSubscriptionPaymentItem.usagePeriodID = currentUsagePeriod;

            MyAccountService.App.get().getUserRateAndUpgUserRatePerHost(subscriptionPaymentItem.userCount, upgSubscriptionPaymentItem.userCount, Utils.getHostName(), new AbstractAsyncCallback<UserRateItem>() {
                @Override
                public void success(UserRateItem results) {
                    if (reCalculateUserRatePER_h != null) {
                        reCalculateUserRatePER_h.getReCalculatedUserRate(results);
                    }
                }
            });

            reCalculateUserRatePER_h = userRateItem -> {
                final double vat_rateN = Utils.getVAT_RATE() != null ? Utils.getVAT_RATE() : VAT_RATE;//default UK based VAT RATE
                final double[][] discount_per_monthly2 = {{userRateItem.getDiscountOneMonth(), userRateItem.getDiscountThreeMonth(),
                        userRateItem.getDiscountSixMonth(), userRateItem.getDiscountTwentyMonth()}}; // Example: (1 month - not discount), 3 month - 15%, 6 month - 25%, 12 month - 30%;
                upgPayPalCalculationHelper.calculateCosts(subscriptionPaymentItem, userRateItem.getUserRate(), Utils.getHostName(), vat_rateN, discount_per_monthly2);

                upgPayPalCalculationHelper.calculateCosts(upgSubscriptionPaymentItem, userRateItem.getUserRateUpg(), Utils.getHostName(), vat_rateN, discount_per_monthly2);

                reCalculateUserRatePER_HOST(result_usagePlanItem, upgHtml, shp);
            };
        }

        //
        currentCategory = FREE_TRIAL;
        if (result_usagePlanItem.isFree()) {
            currentCategory = FREE_TRIAL;
        } else if (result_usagePlanItem.isPaid()) {
            currentCategory = IS_PAID;//paid category (OR, old --> basic/plus/premium)
        }

        if (initHelpContainer != null) {
            initHelpContainer.initHelp(result_usagePlanItem);
        }

        //
        if (FREE_TRIAL.equals(currentCategory)) {

            if (currentUserCount >= 1 && currentUserCount <= 5) {
                buyNowOneMonth.setEnabled(false);
            }
        }

        //
        usersSelectItem.setSelected(0);
        usersSelectItem.setSelectedByValue(String.valueOf(result_usagePlanItem.getUserCount()));

        //tax check
        forUKBasedVAT.setHTML(wfmStrings.forUKBased());
        taxCheck.addValueChangeHandler(booleanValueChangeEvent -> calculatedUserRatePerHOST(usersSelectItem));
        if (Utils.getHostURL().contains(".ru")) {
            forUKBasedVAT.setVisible(false);
            taxCheck.setVisible(false);
        } else {
            forUKBasedVAT.setVisible(true);
            taxCheck.setVisible(true);
        }
        if (result_usagePlanItem.isCompanyUk() && !Utils.getHostURL().contains(".ru")) {
            taxCheck.setValue(true);
        }
        //
        buyNowOneMonth.setHTML(isUpgrade ? wfmStrings.upgradeOnly() : wfmStrings.buyNow());
        buyNowThreeMonth.setHTML(isUpgrade ? wfmStrings.upgradeOnly() : wfmStrings.buyNow());
        buyNowSixMonth.setHTML(isUpgrade ? wfmStrings.upgradeOnly() : wfmStrings.buyNow());
        buyNowOneYear.setHTML(isUpgrade ? wfmStrings.upgradeOnly() : wfmStrings.buyNow());


        if (currentUsageMonth == 1) {// one month

        }
        if (currentUsageMonth == 3) {//three month
            buyNowOneMonth.setEnabled(false);
            priceAfterDiscountPerUserOneMonth.addStyleName("disabled");
            totalPricePerUserOneMonth.addStyleName("disabled");
            pricePerUserMonthOneMonth.addStyleName("disabled");
            totalAmountTotalUserPerOneMonth.addStyleName("disabled");
        }
        if (currentUsageMonth == 6) {//six month
            buyNowOneMonth.setEnabled(false);
            priceAfterDiscountPerUserOneMonth.addStyleName("disabled");
            totalPricePerUserOneMonth.addStyleName("disabled");
            pricePerUserMonthOneMonth.addStyleName("disabled");
            totalAmountTotalUserPerOneMonth.addStyleName("disabled");

            buyNowThreeMonth.setEnabled(false);
            priceAfterDiscountPerUserThreeMonth.addStyleName("disabled");
            totalPricePerUserThreeMonth.addStyleName("disabled");
            pricePerUserMonthThreeMonth.addStyleName("disabled");
            totalAmountTotalUserPerThreeMonth.addStyleName("disabled");
        }
        if (currentUsageMonth == 12) {//one year / 12 month
            buyNowOneMonth.setEnabled(false);
            priceAfterDiscountPerUserOneMonth.addStyleName("disabled");
            totalPricePerUserOneMonth.addStyleName("disabled");
            pricePerUserMonthOneMonth.addStyleName("disabled");
            totalAmountTotalUserPerOneMonth.addStyleName("disabled");

            buyNowThreeMonth.setEnabled(false);
            priceAfterDiscountPerUserThreeMonth.addStyleName("disabled");
            totalPricePerUserThreeMonth.addStyleName("disabled");
            pricePerUserMonthThreeMonth.addStyleName("disabled");
            totalAmountTotalUserPerThreeMonth.addStyleName("disabled");

            buyNowSixMonth.setEnabled(false);
            priceAfterDiscountPerUserSixMonth.addStyleName("disabled");
            totalPricePerUserSixMonth.addStyleName("disabled");
            pricePerUserMonthSixMonth.addStyleName("disabled");
            totalAmountTotalUserPerSixMonth.addStyleName("disabled");
        }

        //initialize upgrade pay button
        upgradePayBt.addClickHandler(sender -> {
            float totalTax = 0;

            float perUserPerDay = upgSubscriptionPaymentItem.perUserCost / upgSubscriptionPaymentItem.usageMonths / upgSubscriptionPaymentItem.userCount / 30;
            float total = (upgSubscriptionPaymentItem.userCount - subscriptionPaymentItem.userCount) * result_usagePlanItem.getUpgDayCount() * perUserPerDay;

            String contextPath = "https://" + Utils.getPayPalLink() + "?";
            Utils.redirect(contextPath + cmd + "=_xclick-subscriptions&"
                    + business + "=" + Utils.getPayPalAccount() + "&"
                    + currency_code + "=" + currencyValue + "&"
                    + amount + "=" + getNumberFormatWithBigDecimal(total) + "&"
                    + "tax" + "=" + getNumberFormatWithBigDecimal(totalTax) + "&"
                    + item_name + "=" + Utils.getProductName() + " - ERP.&"
                    + item_number + "=3&"
                    + "a3" + "=" + getNumberFormatWithBigDecimal(total) + "&"
                    + "p3" + "=" + currentUsageMonth + "&"
                    + "t3" + "=M&"
                    + custom + "=" + result_usagePlanItem.getCompanyID() + Constants.SUBSCRIPTION_UPG + subHisID + "&"
                    + returnT + "=" + Utils.getHostURL() + "Myaccount.html" + "&"
                    + cancel_return + "=" + Utils.getHostURL() + "Myaccount.html");
            upgradePayBt.setEnabled(false);
        });
        //
        headerDIV.add(upgHtml);
        headerDIV.add(shp);

        calculatedUserRatePerHOST(usersSelectItem);
    }

    private void reCalculateUserRatePER_HOST(UsagePlanItem result_usagePlanItem, HTML upgHtml, HorizontalPanelDiv shp) {
        subHisID = result_usagePlanItem.getUpgSubHisId();
        upgHtml.setHTML("\n <b>You have just upgraded your subscription plan, in order to finalize\n" +
                "subscription upgrade process please click on the \"Pay Now\" button, " +
                "and you will be charged for the " + (result_usagePlanItem.getUpgUserCount() - result_usagePlanItem.getUserCount()) + " new employees/"
                + (result_usagePlanItem.getUpgStorageCount() - result_usagePlanItem.getStorageCount()) + "GB storage space.</b>");
        shp.add(upgradePayBt);
        header.setVisible(true);
    }

    private void draw() {
        LoadingPanel.loading(true);
        MyAccountService.App.get().getCurrentUsagePlan(new AbstractAsyncCallback<UsagePlanItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.error(), Info.Type.WARNING);
            }

            @Override
            public void success(final UsagePlanItem result_usagePlanItem) {
                LoadingPanel.loading(false);
                if (result_usagePlanItem != null) {
                    drawUsagePlanItem(result_usagePlanItem);
                }
            }
        });
    }

    private SelectItem[] getUsersSelectItem(int from, int to) {
        int count = to - from;
        SelectItem[] items = new SelectItem[count];
        for (int i = 0; i < count; i++) {
            items[i] = new SelectItem(i, String.valueOf(i + 1 + from));
        }
        return items;
    }

    private void initialize() {

        header.setVisible(false);
        serviceType = new HTML("");

        iWantToUseKPIDOTCOM.setInnerHTML(wfmStrings.iWantToUseKPIDOTCOM1() + " " + Utils.getHelpHost() + " " + wfmStrings.iWantToUseKPIDOTCOM2());
        registerUsersSelectItem(usersSelectItem);
        //register listener to pseudoSelect span
        selectedUSERS.setInnerHTML(wfmStrings.emUsersEm());

        //periods help
        oneMonth.setInnerHTML(wfmStrings.oneMonth());
        threeMonth.setInnerHTML(wfmStrings.threeMonth());
        sixMonth.setInnerHTML(wfmStrings.sixMonth());
        oneYear.setInnerHTML(wfmStrings.oneYear());
        //
        priceAfterDiscount.setInnerHTML(wfmStrings.priceAfterDiscount());
        pricePerUserMonth.setInnerHTML(wfmStrings.pricePerUserMonth());
        discount.setInnerHTML(wfmStrings.discount());

        //discount percent
        discountPercentPerOneMonth.setHTML("");

        draw();
    }

    private void registerBuyNowButtonListener(final UsagePlanItem result_usagePlanItem, final Button button, final int periodMonth, final String periodS) {
        button.setSize("110px", "37px");
        button.addClickHandler(event -> {
            //
            if (isGetQuote) {
                final Integer userCount = Integer.valueOf(usersSelectItem.getSelectedItem().getName());
                String cTotAmount = (Utils.getHTMLCODESForCurrency(isUKClient ? "GBP" : Utils.getCurrencyCODEbyHOST()) + getNumberFormatWithBigDecimal(result_usagePlanItem.getTotalAmount()));
                sendRequestQuoteMessage(result_usagePlanItem.getPeriod(), result_usagePlanItem.getUserCount(), result_usagePlanItem.getStatus(), cTotAmount, periodS, userCount);
            } else if (isUpgrade) {
                upgradePlanValidate(result_usagePlanItem, usersSelectItem, button, periodMonth);
            } else {
                createSubscriptionPayPalValidate(usersSelectItem, button, periodMonth);
            }
        });
    }

    private void registerDisabledOptionForBetween1to5() {
        if (FREE_TRIAL.equals(currentCategory)) {
            buyNowOneMonth.setEnabled(false);
        }

        priceAfterDiscountPerUserOneMonth.addStyleName("disabled");
        priceAfterDiscountPerUserThreeMonth.removeStyleName("disabled");
        priceAfterDiscountPerUserSixMonth.removeStyleName("disabled");
        priceAfterDiscountPerUserOneYear.removeStyleName("disabled");

        totalPricePerUserOneMonth.addStyleName("disabled");
        totalPricePerUserThreeMonth.removeStyleName("disabled");
        totalPricePerUserSixMonth.removeStyleName("disabled");
        totalPricePerUserOneYear.removeStyleName("disabled");

        pricePerUserMonthOneMonth.addStyleName("disabled");
        pricePerUserMonthThreeMonth.removeStyleName("disabled");
        pricePerUserMonthSixMonth.removeStyleName("disabled");
        pricePerUserMonthOneYear.removeStyleName("disabled");

        totalAmountTotalUserPerOneMonth.addStyleName("disabled");
        totalAmountTotalUserPerThreeMonth.removeStyleName("disabled");
        totalAmountTotalUserPerSixMonth.removeStyleName("disabled");
        totalAmountTotalUserPerOneYear.removeStyleName("disabled");

        //
        buyNowOneMonth.setHTML(isUpgrade ? wfmStrings.upgradeOnly() : wfmStrings.buyNow());
        buyNowThreeMonth.setHTML(isUpgrade ? wfmStrings.upgradeOnly() : wfmStrings.buyNow());
        buyNowSixMonth.setHTML(isUpgrade ? wfmStrings.upgradeOnly() : wfmStrings.buyNow());
        buyNowOneYear.setHTML(isUpgrade ? wfmStrings.upgradeOnly() : wfmStrings.buyNow());
    }

    private void registerDisabledOptionForBetween6to30() {
        if (FREE_TRIAL.equals(currentCategory)) {
            buyNowOneMonth.setEnabled(true);
        }

        priceAfterDiscountPerUserOneMonth.removeStyleName("disabled");
        priceAfterDiscountPerUserThreeMonth.removeStyleName("disabled");
        priceAfterDiscountPerUserSixMonth.removeStyleName("disabled");
        priceAfterDiscountPerUserOneYear.removeStyleName("disabled");

        totalPricePerUserOneMonth.removeStyleName("disabled");
        totalPricePerUserThreeMonth.removeStyleName("disabled");
        totalPricePerUserSixMonth.removeStyleName("disabled");
        totalPricePerUserOneYear.removeStyleName("disabled");

        pricePerUserMonthOneMonth.removeStyleName("disabled");
        pricePerUserMonthThreeMonth.removeStyleName("disabled");
        pricePerUserMonthSixMonth.removeStyleName("disabled");
        pricePerUserMonthOneYear.removeStyleName("disabled");

        totalAmountTotalUserPerOneMonth.removeStyleName("disabled");
        totalAmountTotalUserPerThreeMonth.removeStyleName("disabled");
        totalAmountTotalUserPerSixMonth.removeStyleName("disabled");
        totalAmountTotalUserPerOneYear.removeStyleName("disabled");
        //
        buyNowOneMonth.setHTML(isUpgrade ? wfmStrings.upgradeOnly() : wfmStrings.buyNow());
        buyNowThreeMonth.setHTML(isUpgrade ? wfmStrings.upgradeOnly() : wfmStrings.buyNow());
        buyNowSixMonth.setHTML(isUpgrade ? wfmStrings.upgradeOnly() : wfmStrings.buyNow());
        buyNowOneYear.setHTML(isUpgrade ? wfmStrings.upgradeOnly() : wfmStrings.buyNow());
    }

    private void registerDisabledOptionForGreater31() {
        if (FREE_TRIAL.equals(currentCategory)) {
            buyNowOneMonth.setEnabled(true);
        }

        priceAfterDiscountPerUserOneMonth.addStyleName("disabled");
        priceAfterDiscountPerUserThreeMonth.addStyleName("disabled");
        priceAfterDiscountPerUserSixMonth.addStyleName("disabled");
        priceAfterDiscountPerUserOneYear.addStyleName("disabled");

        totalPricePerUserOneMonth.addStyleName("disabled");
        totalPricePerUserThreeMonth.addStyleName("disabled");
        totalPricePerUserSixMonth.addStyleName("disabled");
        totalPricePerUserOneYear.addStyleName("disabled");

        pricePerUserMonthOneMonth.addStyleName("disabled");
        pricePerUserMonthThreeMonth.addStyleName("disabled");
        pricePerUserMonthSixMonth.addStyleName("disabled");
        pricePerUserMonthOneYear.addStyleName("disabled");

        totalAmountTotalUserPerOneMonth.addStyleName("disabled");
        totalAmountTotalUserPerThreeMonth.addStyleName("disabled");
        totalAmountTotalUserPerSixMonth.addStyleName("disabled");
        totalAmountTotalUserPerOneYear.addStyleName("disabled");

        buyNowOneMonth.setHTML(wfmStrings.getQuate());
        buyNowThreeMonth.setHTML(wfmStrings.getQuate());
        buyNowSixMonth.setHTML(wfmStrings.getQuate());
        buyNowOneYear.setHTML(wfmStrings.getQuate());
    }

    private void registerUsersSelectItem(final DataListBox usersSelectItem) {
        usersSelectItem.setNullLabel("0");
        usersSelectItem.setWithoutNullLabel(true);
        usersSelectItem.setItems(getUsersSelectItem(0, 100));
        usersSelectItem.addValueChangeHandler(event -> {
            if (usersSelectItem.getSelectedItem() != null) {
                //
                calculatedUserRatePerHOST(usersSelectItem);
            }
        });
    }

    private void createSubscriptionPayPalValidate(DataListBox userBox, final Button button, Integer clickedUsagePeriodID) {
        // validate
        boolean valid = validate(userBox);
        if (!valid) {
            Info.show(wfmStrings.pleaseSelectValue(), Info.Type.WARNING);
            return;
        }
        //validate for choosen pay items less than last
        try {
            boolean error = validateBuyAndUpgradeUsersCount(userBox, clickedUsagePeriodID);
            if (error) {
                Info.show(wfmStrings.pleaseChooseHigherUpgrade(), Info.Type.WARNING);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        createSubscriptionPayPal(userBox, button, clickedUsagePeriodID);
    }

    private void createSubscriptionPayPal(final DataListBox userBox, final Button button, final Integer clickedUsagePeriodID) {
        //collect upgrade data
        if (userBox.getSelectedItem() == null) {
            return;
        }
        //get user count
        final Integer userCount;
        try {
            userCount = Integer.valueOf(userBox.getSelectedItem().getName());
        } catch (NumberFormatException e) {
            return;
        }
        MyAccountService.App.get().getUserDiscount(userCount, Utils.getHostName(), new AbstractAsyncCallback<UserRateItem>() {
            @Override
            public void success(UserRateItem result) {
                if (reCalculateUserRatePER_h2 != null) {
                    reCalculateUserRatePER_h2.getReCalculatedUserRate2(result);
                }

            }
        });
        reCalculateUserRatePER_h2 = userRate -> {
            final UsagePlanItem usagePlanItem = calculateClickedMonth(userRate, userCount, clickedUsagePeriodID);
            usagePlanItem.setStorageCount(1);
            usagePlanItem.setService(ALL_SERVICES);
            usagePlanItem.setCompanyUk(taxCheck.getValue());

            final String currencyValue = isUKClient ? "GBP" : Utils.getCurrencyCODEbyHOST();

            button.setEnabled(false);
            LoadingPanel.loading(true);
            MyAccountService.App.get().saveUsagePlan(usagePlanItem, new AbstractAsyncCallback<UsagePlanItem>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    button.setEnabled(true);
                    Info.show(wfmStrings.error(), Info.Type.WARNING);
                }

                public void success(UsagePlanItem usagePlan) {
                    LoadingPanel.loading(false);
                    button.setEnabled(true);

                    if (usagePlan != null) {
                        String contextPath = "https://" + Utils.getPayPalLink() + "?";
                        Utils.redirect(contextPath + cmd + "=_xclick-subscriptions&"
                                + business + "=" + Utils.getPayPalAccount() + "&"
                                + currency_code + "=" + currencyValue + "&"
                                + amount + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount()) + "&"
                                + taxX + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTax()) + "&"
                                + item_name + "=" + Utils.getProductName() + " - ERP.&"
                                + item_number + "=1&"
                                + a3 + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount()) + "&"
                                + p3 + "=" + usagePlanItem.getUsageMonth() + "&"
                                + t3 + "=M&"//it si daily , change to "M" - monthly
                                + src + "=1&"
                                + custom + "=" + usagePlan.getCompanyID() + SUBSCRIPTION_ADD + usagePlan.getObjectID() + "&"
                                + returnT + "=" + GWT.getHostPageBaseURL() + "Myaccount.html" + "&"
                                + cancel_return + "=" + GWT.getHostPageBaseURL() + "Myaccount.html"
                        );
                    }
                }
            });
        };
    }

    private void sendRequestQuoteMessage(String cSPeriod, int cUsersCount, String cStatus, String cTotalAmount, String rSPeriod, int rUsersCount) {
        RequestQuoteItem requestQuoteItem = new RequestQuoteItem();
        requestQuoteItem.setCurrentSubscriptionPeriod(cSPeriod);
        requestQuoteItem.setCurrentUsersCount(cUsersCount);
        requestQuoteItem.setCurrentStatus(cStatus);
        requestQuoteItem.setCurrentTotalAmount(cTotalAmount);
        requestQuoteItem.setRequestedSubscriptionPeriod(rSPeriod);
        requestQuoteItem.setRequestedUsersCount(rUsersCount);
        LoadingPanel.loading(true);
        MyAccountService.App.get().sendRequestQuote(requestQuoteItem, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.error(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                LoadingPanel.loading(false);
                if (aBoolean) {
                    Info.show(myAccountStrings.yourRequestForQuoteHasBeenReceived(), Info.Type.INFO);
                } else {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }
            }
        });
    }

    private void upgradePlanValidate(final UsagePlanItem usagePlanItem, DataListBox userBox, Button button, Integer clickedUsagePeriodID) {
        //validate
        boolean valid = validate(userBox);
        if (!valid) {
            Info.show(wfmStrings.pleaseSelectValue(), Info.Type.WARNING);
            return;
        }
        try {
            boolean error = validateBuyAndUpgradeUsersCount(userBox, clickedUsagePeriodID);
            if (error) {
                Info.show(wfmStrings.pleaseChooseHigherUpgrade(), Info.Type.WARNING);
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        upgradePlan(usagePlanItem, userBox, button, clickedUsagePeriodID);
    }

    private void upgradePlan(final UsagePlanItem usagePlanItem, DataListBox userBox, Button button, final Integer clickedUsagePeriodID) {
        button.setEnabled(false);
        if (userBox.getSelectedItem() == null) {
            return;
        }
        //get user count
        final Integer userCount;
        try {
            userCount = Integer.valueOf(userBox.getSelectedItem().getName());
        } catch (NumberFormatException e) {
            return;
        }
        MyAccountService.App.get().getUserDiscount(userCount, Utils.getHostName(), new AbstractAsyncCallback<UserRateItem>() {
            @Override
            public void success(UserRateItem result) {
                if (reCalculateUserRatePER_h2 != null) {
                    reCalculateUserRatePER_h2.getReCalculatedUserRate2(result);
                }
            }
        });
        reCalculateUserRatePER_h2 = userRate -> {
            //collect upgrade data
            UsagePlanItem calculatedData = calculateClickedMonth(userRate, userCount, clickedUsagePeriodID);
            usagePlanItem.setDiscount(calculatedData.getDiscount());
            usagePlanItem.setTotalAmount(calculatedData.getTotalAmount());
            usagePlanItem.setUserCount(calculatedData.getUserCount());
            usagePlanItem.setTax(calculatedData.getTax());
            usagePlanItem.setUsageMonth(calculatedData.getUsageMonth());
            usagePlanItem.setCompanyUk(taxCheck.getValue());

            final String currencyValue = isUKClient ? "GBP" : Utils.getCurrencyCODEbyHOST();
            // save plan history
            LoadingPanel.loading(true);
            MyAccountService.App.get().createSubscriptionHistory(usagePlanItem, new AbstractAsyncCallback<Integer>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(Integer usagePlanID) {
                    LoadingPanel.loading(false);
                    // send to PayPal
                    String contextPath = "https://" + Utils.getPayPalLink() + "?";
                    Utils.redirect(contextPath + cmd + "=_xclick-subscriptions&"
                            + business + "=" + Utils.getPayPalAccount() + "&"         //      sales@workforcetrack.com
                            + currency_code + "=" + currencyValue + "&"
                            + amount + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount()) + "&"  ////test "tot"
                            + taxX + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTax()) + "&"  ////test "taxC"
                            + item_name + "=" + Utils.getProductName() + " - ERP.&"
                            + item_number + "=2&"
                            + a3 + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount()) + "&"
                            + p3 + "=" + usagePlanItem.getUsageMonth() + "&"
                            + t3 + "=M&" //it si daily , change to "M" - monthly
                            + modify + "=2&"
                            + src + "=1&"
                            + custom + "=" + usagePlanItem.getCompanyID() + SUBSCRIPTION_UPG + usagePlanID + "&"
                            + returnT + "=" + Utils.getHostURL() + "Myaccount.html" + "&"
                            + cancel_return + "=" + Utils.getHostURL() + "Myaccount.html"
                    );
                }
            });
        };
    }

    private boolean validateBuyAndUpgradeUsersCount(DataListBox userBox, Integer clickedButtonPeriod) {
        boolean userLess = false;
        boolean userEqual = false;
        boolean periodLess = false;
        boolean periodEqual = false;
        Integer selectedUsersCount = Integer.valueOf(userBox.getSelectedItem().getName());
        Integer currentUserCount_ = FREE_TRIAL.equals(currentCategory) ? registeredUsersCount : currentUserCount;
        if (selectedUsersCount < currentUserCount_) {
            userLess = true;
        }
        if (selectedUsersCount.intValue() == currentUserCount_.intValue()) {
            userEqual = true;
        }
        if (clickedButtonPeriod < currentUsagePeriod) {
            periodLess = true;
        }
        if (clickedButtonPeriod.intValue() == currentUsagePeriod.intValue()) {
            periodEqual = true;
        }

        boolean error = periodLess;
        if (periodEqual && userEqual) {
            error = true;
        }
        if (periodEqual && userLess) {
            error = true;
        }
        if (!periodLess && userLess) {
            error = true;
        }
        return error;
    }

    private boolean validate(DataListBox userBox) {
        return userBox.getSelectedItem() != null;
    }

    @Override
    public FlowPanel getHelpContainer() {
        final FlowPanel flowPanel = new FlowPanel();
        flowPanel.setVisible(false);
        if (afterCalculateUsagePlanItem_u == null) {
            this.initHelpContainer = result_u -> {
                afterCalculateUsagePlanItem_u = result_u;
                generateHelpContainer(result_u, flowPanel);
            };
        } else {
            generateHelpContainer(afterCalculateUsagePlanItem_u, flowPanel);
        }
        return flowPanel;
    }

    private void generateHelpContainer(UsagePlanItem result_u, FlowPanel flowPanel) {
        flowPanel.setVisible(true);
        final WestPanelHelp westPanel = new WestPanelHelp("<b>" + wfmStrings.yourCurrentPlan() + ":" + "</b>");
        String sb = "<div><b>" + wfmStrings.users() + ": </b>&nbsp;&nbsp;" + " " + result_u.getUserCount() + "</br>" +
                "<b>" + wfmStrings.period() + ": </b>&nbsp;" + result_u.getPeriodType() + "</br></br>" +
                "</div>";
        final HTML html = new HTML(sb);
        westPanel.addHtmlLine(html);

        VerticalPanelDiv vpDiv = new VerticalPanelDiv();
        vpDiv.add(westPanel);
        vpDiv.setSpacing(3);
        vpDiv.setWidth("100%");

        flowPanel.add(vpDiv);
    }

    private interface InitHelpContainer {
        void initHelp(UsagePlanItem result);
    }

    private interface reCalculateUserRatePER_H {
        void getReCalculatedUserRate(UserRateItem userRateItem);
    }

    private interface reCalculateUserRatePER_H2 {
        void getReCalculatedUserRate2(UserRateItem userRateItem);
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}