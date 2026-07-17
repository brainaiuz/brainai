package com.edatasite.workforce.gwt.myaccount.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.myaccount.client.bundles.MyaccountImageBundles;
import com.edatasite.workforce.gwt.myaccount.client.rpc.MyAccountService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.pricing.client.PayPalCalculationHelper;
import com.edatasite.workforce.gwt.pricing.client.SubscriptionPaymentItem;
import com.edatasite.workforce.gwt.pricing.client.UserRateItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;

/**
 * Created by Fathulla on 04.04.16.
 */
public class AEPricingPageView extends View implements Constants {

    @UiField
    InputElement activeUser;
    @UiField
    InputElement essUsers;
    @UiField
    Anchor basicBtn;
    @UiField
    Anchor proBtn;
    @UiField
    Anchor enterpriseBtn;
    @UiField
    Anchor customizeBtn;
    @UiField
    Label essTotal;
    @UiField
    Label activeTotal;
    @UiField
    Label totalPrice;

    private reCalculateUserRatePER_H reCalculateUserRatePER_h;
    private reCalculateUserRatePER_H2 reCalculateUserRatePER_h2;
    

    private UsagePlanItem resultUsagePlanItem;
    private boolean isUpgrade = false;
    private SubscriptionPaymentItem upgSubscriptionPaymentItem = new SubscriptionPaymentItem();

    private String selectedPricingPackageNAME;

    private Integer currentUsagePeriod = 0;
    private int currentUsageMonth = 0;


    final String cmd = "cmd";                       //_xclick-subscriptions;
    final String business = "business";               //sales@workforcetrack.com
    final String currency_code = "currency_code";  //USD
    final String amount = "amount";                   //25$
    final String item_name = "item_name";           //
    final String item_number = "item_number";       //
    final String custom = "custom";                   //
    final String taxX = "tax";                       //
    final String a3 = "a3";                           //5.00
    final String p3 = "p3";                           //1  (1,3,6,12 - Months)
    final String uc = "userCount";                           //1  (1,3,6,12 - Months)
    final String t3 = "t3";                           //M (Month)
    final String srt = "srt";                       //1,2,3 (Limit the number of billing cycles.)
    final String src = "src";                       //1,2,3 (Limit the number of billing cycles.)
    final String cancel_return = "cancel_return";  //
    final String returnT = "return";               //
    final String modify = "modify";


    interface AEPricingPageViewUiBinder extends UiBinder<HTMLPanel, AEPricingPageView> {
    }

    public AEPricingPageView() {
        super("pricingAECountryView", wfmStrings.currentSubscription());
    }


    @Override
    public ImageResource getIconImage() {
        return MyaccountImageBundles.App.get().currentSubscription();
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        AEPricingPageViewUiBinder ourUiBinder = GWT.create(AEPricingPageViewUiBinder.class);
        add(ourUiBinder.createAndBindUi(this));
        draw();
        return null;
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
                drawUsagePlanItem(result_usagePlanItem);
            }
        });
    }


    private void drawUsagePlanItem(final UsagePlanItem result_usagePlanItem) {
        resultUsagePlanItem = result_usagePlanItem;
        final SubscriptionPaymentItem subscriptionPaymentItem = new SubscriptionPaymentItem();
        //initialize isUpgrade
        if (!result_usagePlanItem.isFree() && !result_usagePlanItem.isPaid()) {
            isUpgrade = false;
        } else if (!result_usagePlanItem.isFree() && result_usagePlanItem.isShowUpgBt()) {
            isUpgrade = true;
        }

        if (isUpgrade){
            basicBtn.setText("Upgrade");
            proBtn.setText("Upgrade");
            enterpriseBtn.setText("Upgrade");
            customizeBtn.setText("Upgrade");
        }
        //buy now buttons -/- get quote buttons -/- upgrade buttons -/
        registerBuyNowButtonListener(result_usagePlanItem, basicBtn, Constants.MINI_SIGN, 1, false, false, 0);
        registerBuyNowButtonListener(result_usagePlanItem, proBtn, Constants.SMALL_SIGN, 2, false, false, 0);
        registerBuyNowButtonListener(result_usagePlanItem, enterpriseBtn, Constants.ENTERPRISE_SIGN, 5, false, false, 0);
        registerBuyNowButtonListener(result_usagePlanItem, customizeBtn, UsagePlanItem.CUSTOM, null, true, false,0);


        Event.sinkEvents(activeUser, Event.ONKEYUP | Event.ONMOUSEWHEEL | Event.ONMOUSEUP);
        Event.setEventListener(activeUser, event -> {
            if (DOM.eventGetType(event) == Event.ONMOUSEWHEEL) {
                int dir = event.getMouseWheelVelocityY() > 0 ? -1 : 1;
                int users = 0;
                if (!"".equals(activeUser.getValue())) {
                    users = Integer.parseInt(activeUser.getValue());
                }
                if (users + dir >= 0) {
                    activeUser.setValue(String.valueOf(users + dir));
                    double price = calculateActive(users + dir);
                    activeTotal.setText(Utils.formatDouble(price));
                    if (!"".equals(essTotal.getText())) {
                        totalPrice.setText("Total: AED " + Utils.formatDouble(price + calculateEss()));
                    } else {
                        totalPrice.setText("Total: AED " + Utils.formatDouble(price));
                    }
                }

                event.stopPropagation();
                event.preventDefault();
            } else {
                Double price = calculateActive();
                activeTotal.setText(Utils.formatDouble(price));
                if (!"".equals(essTotal.getText())) {
                    totalPrice.setText("Total: AED " + Utils.formatDouble(price + calculateEss()));
                } else {
                    totalPrice.setText("Total: AED " + Utils.formatDouble(price));
                }
            }
        });

        Event.sinkEvents(essUsers, Event.ONKEYUP | Event.ONMOUSEWHEEL | Event.ONMOUSEUP);
        Event.setEventListener(essUsers, event -> {
            if (DOM.eventGetType(event) == Event.ONMOUSEWHEEL) {
                int dir = event.getMouseWheelVelocityY() > 0 ? -1 : 1;
                int users = 0;
                if (!"".equals(activeUser.getValue())) {
                    users = Integer.parseInt(essUsers.getValue());
                }
                if (users + dir >= 0) {
                    essUsers.setValue(String.valueOf(users + dir));
                    double price = calculateEss(users + dir);
                    essTotal.setText(Utils.formatDouble(price));
                    if (!"".equals(activeTotal.getText())) {
                        totalPrice.setText("Total: AED " + Utils.formatDouble((price + calculateActive())));
                    } else {
                        totalPrice.setText("Total: AED " + Utils.formatDouble(price));
                    }
                }

                event.stopPropagation();
                event.preventDefault();
            } else {
                Double price = calculateEss();
                essTotal.setText(Utils.formatDouble(price));
                if (!"".equals(activeTotal.getText())) {
                    totalPrice.setText("Total: AED " + Utils.formatDouble((price + calculateActive())));
                } else {
                    totalPrice.setText("Total: AED " + Utils.formatDouble(price));
                }
            }
        });

        String buttonLabel = (isUpgrade
                ? wfmStrings.upgradeOnly()
                : wfmStrings.buyNow());
        basicBtn.setText(buttonLabel);
        proBtn.setText(buttonLabel);
        enterpriseBtn.setText(buttonLabel);

        basicBtn.getElement().setClassName("btn btn-primary btn-contact-trial text-uppercase");
        proBtn.getElement().setClassName("btn btn-primary btn-contact-trial text-uppercase");
        enterpriseBtn.getElement().setClassName("btn btn-primary btn-contact-trial text-uppercase");
        customizeBtn.getElement().setClassName("btn btn-primary btn-block btn-contact-buynow text-uppercase");
        customizeBtn.getElement().getStyle().setWidth(700, Style.Unit.PX);
        customizeBtn.getElement().getStyle().setPadding(12, Style.Unit.PX);

        if (!result_usagePlanItem.isUpgPayed()) {

            final PayPalCalculationHelper upgPayPalCalculationHelper = new PayPalCalculationHelper();
            upgSubscriptionPaymentItem.usageMonths = currentUsageMonth;
            upgSubscriptionPaymentItem.isGBP = false;
            upgSubscriptionPaymentItem.storage = result_usagePlanItem.getUpgStorageCount();
            upgSubscriptionPaymentItem.userCount = result_usagePlanItem.getUpgUserCount();
            upgSubscriptionPaymentItem.up = result_usagePlanItem.getCostDown();
            upgSubscriptionPaymentItem.serviceType = result_usagePlanItem.isAllService();
            upgSubscriptionPaymentItem.usagePeriodID = currentUsagePeriod;
            upgSubscriptionPaymentItem.supportPackageNAME = result_usagePlanItem.getUpgSupportPackageNAME();
            upgSubscriptionPaymentItem.categoryREAL = result_usagePlanItem.getUpgCategoryREAL();
            //
            subscriptionPaymentItem.usageMonths = currentUsageMonth;
            subscriptionPaymentItem.isGBP = false;
            subscriptionPaymentItem.storage = result_usagePlanItem.getStorageCount();
            subscriptionPaymentItem.userCount = result_usagePlanItem.getUserCount();
            subscriptionPaymentItem.up = result_usagePlanItem.getCostDown();
            subscriptionPaymentItem.serviceType = result_usagePlanItem.isAllService();
            subscriptionPaymentItem.usagePeriodID = currentUsagePeriod;
            subscriptionPaymentItem.supportPackageNAME = result_usagePlanItem.getSupportPackageNAME();
            subscriptionPaymentItem.categoryREAL = result_usagePlanItem.getCategoryREAL();
            MyAccountService.App.get().getUserRateAndUpgUserRatePerHostT(Utils.getHostName(), subscriptionPaymentItem.categoryREAL, subscriptionPaymentItem.supportPackageNAME,
                    upgSubscriptionPaymentItem.categoryREAL, upgSubscriptionPaymentItem.supportPackageNAME, new AbstractAsyncCallback<UserRateItem>() {
                @Override
                public void success(UserRateItem results) {
                    if (reCalculateUserRatePER_h != null) {
                        reCalculateUserRatePER_h.getReCalculatedUserRate(results);
                    }
                }
            });

            reCalculateUserRatePER_h = userRateItem -> {
                final double vat_rateN = Utils.getVAT_RATE() != null
                        ? Utils.getVAT_RATE()
                        : VAT_RATE;//default UK based VAT RATE
                final double[][] discount_per_monthly2 = {{userRateItem.getDiscountOneMonth(), userRateItem.getDiscountThreeMonth(),
                        userRateItem.getDiscountSixMonth(), userRateItem.getDiscountTwentyMonth()}}; // Example: (1 month - not discount), 3 month - 15%, 6 month - 25%, 12 month - 30%;
                final double supportPackagePrice = userRateItem.getSupportPackagePrice() != null
                        ? userRateItem.getSupportPackagePrice()
                        : 0d;
                final double supportPackagePriceUpg = userRateItem.getSupportPackagePriceUpg() != null
                        ? userRateItem.getSupportPackagePriceUpg()
                        : 0d;
                upgPayPalCalculationHelper.calculateCostsNEW(subscriptionPaymentItem, userRateItem.getUserRate(), supportPackagePrice, Utils.getHostName(), vat_rateN, discount_per_monthly2);
                upgPayPalCalculationHelper.calculateCostsNEW(upgSubscriptionPaymentItem, userRateItem.getUserRateUpg(), supportPackagePriceUpg, Utils.getHostName(), vat_rateN, discount_per_monthly2);

            };
        }

        //
        if (result_usagePlanItem.isFree()) {
        } else if (result_usagePlanItem.isPaid()) {
        }
    }

    private double calculateActive(){
        try{
            double USDtoUAE = 3.67;
            double perActiveUserUSD = 29.99;

            double perAnnualActiveUserAED = USDtoUAE * perActiveUserUSD;
            return  (12 * (Integer.parseInt(activeUser.getValue()) * perAnnualActiveUserAED));
        }catch(Exception e){
            return 0;
        }
    }

    private double calculateActive(int userCount){
        try{
            double USDtoUAE = 3.67;
            double perActiveUserUSD = 29.99;

            double perAnnualActiveUserAED = USDtoUAE * perActiveUserUSD;
            return  (12 * (userCount * perAnnualActiveUserAED));
        }catch(Exception e){
            return 0;
        }


    }

    private double calculateEss(){
        try{

            double perAnnualESSUserAED = 50;
            return (12 * (Integer.parseInt(essUsers.getValue()) * perAnnualESSUserAED));
        }catch(Exception e){
            return 0;
        }
    }

    private double calculateEss(int usersCount){
        try{
            double perAnnualESSUserAED = 50;
            return (12 * (usersCount * perAnnualESSUserAED));
        }catch(Exception e){
            return 0;
        }
    }

    private void registerBuyNowButtonListener(final UsagePlanItem result_usagePlanItem, final Anchor button, final String pricingPackageN, final Integer userCount, final boolean isCustomPricing, final boolean isWorldPay, final int selectedUsagePeriodValue) {
        button.addClickHandler(event -> {
            //  0 -> 1 month  ||  1 -> 3 month  ||  2 -> 6 month  ||  3 -> 12 month/1 year

            selectedPricingPackageNAME = pricingPackageN;
            //
            Integer usersCount = userCount;
            if (!isCustomPricing) {
            } else {
                usersCount = Integer.valueOf(activeUser.getValue());
            }
            boolean validate = true;
            if (validate) {
                if (isUpgrade) {
                    upgradePlan(result_usagePlanItem, usersCount, button, selectedUsagePeriodValue, selectedPricingPackageNAME, isCustomPricing);

                } else {
                    createSubscriptionPayPal(usersCount, button, selectedPricingPackageNAME, selectedUsagePeriodValue, isCustomPricing);
                }
            }

        });
    }

    private void upgradePlan(final UsagePlanItem usagePlanItem, final Integer userCount, final Anchor button, final Integer clickedUsagePeriodID, final String currentCategoryType, final boolean customPricing) {
        button.setEnabled(false);
        if (userCount == null) {
            button.setEnabled(true);
            return;
        }
        reCalculateUserRatePER_h2 = userRate -> {
            //collect upgrade data
            usagePlanItem.setPlanType(ONE_MONTH_0);
            usagePlanItem.setTotalAmount(Float.parseFloat((calculateActive() + calculateEss()) + ""));
            if (customPricing) {
                usagePlanItem.setEssUserCount(Integer.parseInt(essUsers.getValue()));
                usagePlanItem.setUserCount(Integer.parseInt(activeUser.getValue()));

            } else {
                usagePlanItem.setCategoryREAL(PayPalCalculationHelper.getPricingPackageNAME(currentCategoryType));
                usagePlanItem.setUserCount(userCount);
                usagePlanItem.setEssUserCount(userCount == 1 ? 1 : userCount == 2 ? 10 : userCount == 5 ? 100 : 1);
            }
            final String currencyValue = Utils.getCurrencyCODEbyHOST();
            // save plan history

            LoadingPanel.loading(true);
            MyAccountService.App.get().createSubscriptionHistory(usagePlanItem, new AbstractAsyncCallback<Integer>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    button.setEnabled(true);
                }

                public void success(Integer usagePlanID) {
                    LoadingPanel.loading(false);
                    String description = "AE";//packageName + " Bundle - Up to " + userCount + " Users - " + periodAsString + " Contract";

                    String contextPath = "https://" + Utils.getPayPalLink() + "?";
                    Utils.redirect(contextPath + cmd + "=_xclick-subscriptions&"
                            + business + "=" + Utils.getPayPalAccount() + "&"         //      sales@workforcetrack.com
                            + currency_code + "=" + currencyValue + "&"
                            + amount + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount()) + "&"  ////test "tot"
                            + taxX + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTax()) + "&"  ////test "taxC"
                            + item_name + "=" + description + "&"
                            + item_number + "=2&"
                            + a3 + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount()) + "&"
                            + p3 + "=" + usagePlanItem.getUsageMonth() + "&"
                            + t3 + "=M&" // it si daily , change to "M" - monthly
                            + modify + "=2&"
                            + src + "=1&"
                            + custom + "=" + usagePlanItem.getCompanyID() + SUBSCRIPTION_UPG + usagePlanID + "&"
                            + returnT + "=" + Utils.getHostURL() + "Myaccount.html" + "&"
                            + cancel_return + "=" + Utils.getHostURL() + "Myaccount.html"
                    );
                }
            });
        };
        reCalculateUserRatePER_h2.getReCalculatedUserRate2(new UserRateItem());
    }

    private String getNumberFormatWithBigDecimal(double value) {
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    private void createSubscriptionPayPal(final Integer userCount, final Anchor button, final String currentCategoryT, final Integer clickedUsagePeriodID, final boolean customPricing) {
        //collect upgrade data
        if (userCount == null) {
            return;
        }
        reCalculateUserRatePER_h2 = userRate -> {
            final UsagePlanItem usagePlanItem;
            usagePlanItem = new UsagePlanItem();
            usagePlanItem.setStorageCount(1);
            usagePlanItem.setService(ALL_SERVICES);

            final String currencyValue = Utils.getCurrencyCODEbyHOST();

            button.setEnabled(false);
            LoadingPanel.loading(true);
            usagePlanItem.setTotalAmount(Float.parseFloat((calculateActive() + calculateEss()) + ""));
            usagePlanItem.setPlanType(ONE_MONTH_0);
            if (customPricing) {
                usagePlanItem.setEssUserCount(Integer.parseInt(essUsers.getValue()));
                usagePlanItem.setUserCount(Integer.parseInt(activeUser.getValue()));
                usagePlanItem.setStatus(resultUsagePlanItem != null ? resultUsagePlanItem.getStatus() : null);
            } else {
                usagePlanItem.setCategoryREAL(PayPalCalculationHelper.getPricingPackageNAME(currentCategoryT));
                usagePlanItem.setUserCount(userCount);
                usagePlanItem.setEssUserCount(userCount == 1 ? 1 : userCount == 2 ? 10 : userCount == 5 ? 100 : 1);
            }
            MyAccountService.App.get().saveUsagePlan(usagePlanItem, new AbstractAsyncCallback<UsagePlanItem>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    button.setEnabled(true);
                    Info.show(wfmStrings.error(), Info.Type.WARNING);
                }

                public void success(UsagePlanItem usagePlan) {
                    LoadingPanel.loading(false);

                    if (usagePlan != null) {

                        String description = "AE";
                        String contextPath = "https://" + Utils.getPayPalLink() + "?";
                        Utils.redirect(contextPath + cmd + "=_xclick-subscriptions&"
                                + business + "=" + Utils.getPayPalAccount() + "&"
                                + currency_code + "=" + currencyValue + "&"
                                + amount + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount()) + "&"
                                + taxX + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTax()) + "&"
                                + item_name + "=" + description + "&"
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

        reCalculateUserRatePER_h2.getReCalculatedUserRate2(new UserRateItem());
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
