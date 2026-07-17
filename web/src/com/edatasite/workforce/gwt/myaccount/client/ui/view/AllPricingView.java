package com.edatasite.workforce.gwt.myaccount.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.myaccount.client.bundles.MyaccountImageBundles;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountStrings;
import com.edatasite.workforce.gwt.myaccount.client.rpc.MyAccountService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.client.ui.stripe.StripeCheckoutToken;
import com.edatasite.workforce.gwt.myaccount.client.ui.stripe.StripeCompletePayment;
import com.edatasite.workforce.gwt.myaccount.client.ui.stripe.StripePaymentHandler;
import com.edatasite.workforce.gwt.pricing.client.PayPalCalculationHelper;
import com.edatasite.workforce.gwt.pricing.client.SubscriptionPaymentItem;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTab;
import gwt.material.design.client.ui.MaterialTabItem;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Fathulla on 04.04.16.
 */
public class AllPricingView extends View implements Constants {

    private static final MyAccountStrings myAccountStrings = MyAccountStrings.App.get();
    private static final PricingStyle pricingStyle = GWT.create(PricingStyle.class);
    private final String cmd = "cmd";                      //_xclick-subscriptions;
    private final String business = "business";            //sales@workforcetrack.com
    private final String currency_code = "currency_code";  //USD
    private final String amount = "amount";                //25$
    private final String item_name = "item_name";
    private final String item_number = "item_number";
    private final String custom = "custom";
    private final String taxX = "tax";
    private final String a3 = "a3";                        //5.00
    private final String p3 = "p3";                        //1  (1,3,6,12 - Months)
    private final String t3 = "t3";                        //M (Month)
    private final String src = "src";                      //1,2,3 (Limit the number of billing cycles.)
    private final String cancel_return = "cancel_return";
    private final String returnT = "return";
    private final String modify = "modify";

    @UiField
    MaterialTab monthYearTab;
    @UiField
    SpanElement bronzePriceLabel;
    @UiField
    SpanElement bronzePeriod;
    @UiField
    DivElement customBuyNow;
    @UiField
    DivElement bronzeBuyNow;
    @UiField
    DivElement silverBuyNow;
    @UiField
    DivElement goldBuyNow;
    @UiField
    SpanElement silverPriceLabel;
    @UiField
    SpanElement silverPeriod;
    @UiField
    SpanElement goldPriceLabel;
    @UiField
    SpanElement goldPeriod;
    @UiField
    SpanElement customPriceLabel;
    @UiField
    SpanElement customPeriod;
    @UiField
    SpanElement customAccountsLabel;
    @UiField
    SpanElement customSalesLabel;
    @UiField
    SpanElement customHumansLabel;
    @UiField
    SpanElement customProjectsLabel;
    @UiField
    SpanElement payrollLabel;
    @UiField
    InputElement customDocuments;
    @UiField
    InputElement customProjects;
    @UiField
    InputElement customHumans;
    @UiField
    InputElement customSales;
    @UiField
    InputElement customAccounts;
    @UiField
    SpanElement customDecreaseUser;
    @UiField
    InputElement customUserCount;
    @UiField
    SpanElement customIncreaseUser;
    @UiField
    SpanElement essCustomIncreaseUser;
    @UiField
    SpanElement essCustomDecreaseUser;
    @UiField
    InputElement essCustomUserCount;
    @UiField
    DivElement infoHeader;
    @UiField
    DivElement plansHeader;
    @UiField
    TableCellElement reportsLabel;
    @UiField
    TableCellElement documentsLabel;

    private MaterialLink bronzeButton;
    private MaterialLink silverButton;
    private MaterialLink goldButton;
    private MaterialLink customButton;
    private MaterialLink bronzeStripeButton;
    private MaterialLink silverStripeButton;
    private MaterialLink goldStripeButton;
    private MaterialLink customStripeButton;

    private Button upgradePayBt;
    private UsagePlanItem usagePlan;
    private boolean isUpgrade = false;
    private final SubscriptionPaymentItem upgSubscriptionPaymentItem = new SubscriptionPaymentItem();
    private String currentCategory;

    private Integer currentUsagePeriod = 0;
    private int currentUsageMonth = 0;
    private Integer subHisId;
    private final int perUserPrice = 10;
    private final int perEssUserPrice = 1;
    private int customModuleCount = 0;
    private boolean isMonthly = true;

    public AllPricingView() {
        super("allPricingView", wfmStrings.currentSubscription());
    }

    private void draw() {
        documentsLabel.setInnerHTML("<b>" + wfmStrings.documents() + "</b> " + wfmStrings.free());
        reportsLabel.setInnerHTML("<b>" + wfmStrings.reports() + "</b> " + wfmStrings.free());
        pricingStyle.markupPricing().ensureInjected();
        MaterialTabItem monthItem = new MaterialTabItem();
        MaterialLink monthLink = new MaterialLink();
        monthLink.setText("Monthly");
        monthLink.addClickHandler(event -> {
            isMonthly = true;
            pricePeriod(isMonthly);
            setCustomPriceLabel();
            bronzePriceLabel.setInnerHTML(String.valueOf(this.getPriceLabel(5, 50, 5)));
            silverPriceLabel.setInnerHTML(String.valueOf(this.getPriceLabel(10, 100, 5)));
            goldPriceLabel.setInnerHTML(String.valueOf(this.getPriceLabel(25, 250, 5)));
        });
        monthItem.add(monthLink);
        monthYearTab.add(monthItem);

        MaterialTabItem yearItem = new MaterialTabItem();
        MaterialLink yearLink = new MaterialLink();
        yearLink.setText(wfmStrings.yearly());
        yearLink.addClickHandler(event -> {
            isMonthly = false;
            pricePeriod(isMonthly);
            setCustomPriceLabel();
            bronzePriceLabel.setInnerHTML(String.valueOf(this.getPriceLabel(5, 50, 5)));
            silverPriceLabel.setInnerHTML(String.valueOf(this.getPriceLabel(10, 100, 5)));
            goldPriceLabel.setInnerHTML(String.valueOf(this.getPriceLabel(25, 250, 5)));
        });
        yearItem.add(yearLink);
        monthYearTab.add(yearItem);

        bronzeButton = new MaterialLink();
        bronzeButton.setStyleName("btn btn--default");
        silverButton = new MaterialLink();
        silverButton.setStyleName("btn btn--default");
        goldButton = new MaterialLink();
        goldButton.setStyleName("btn btn--default");
        customButton = new MaterialLink();
        customButton.setStyleName("btn btn--default");
        bronzeStripeButton = new MaterialLink();
        bronzeStripeButton.setStyleName("btn btn--default");
        silverStripeButton = new MaterialLink();
        silverStripeButton.setStyleName("btn btn--default");
        goldStripeButton = new MaterialLink();
        goldStripeButton.setStyleName("btn btn--default");
        customStripeButton = new MaterialLink();
        customStripeButton.setStyleName("btn btn--default");

        DOM.sinkEvents(customBuyNow, Event.ONCLICK);
        DOM.setEventListener(customBuyNow, event -> paymentDialogBox(customButton, customStripeButton));
        DOM.sinkEvents(bronzeBuyNow, Event.ONCLICK);
        DOM.setEventListener(bronzeBuyNow, event -> paymentDialogBox(bronzeButton, bronzeStripeButton));
        DOM.sinkEvents(silverBuyNow, Event.ONCLICK);
        DOM.setEventListener(silverBuyNow, event -> paymentDialogBox(silverButton, silverStripeButton));
        DOM.sinkEvents(goldBuyNow, Event.ONCLICK);
        DOM.setEventListener(goldBuyNow, event -> paymentDialogBox(goldButton, goldStripeButton));

        pricePeriod(true);

        customAccountsLabel.setInnerHTML("<b>" + wfmStrings.accounts() + "</b>");
        customHumansLabel.setInnerHTML("<b>" + wfmStrings.hrms() + "</b>");
        customSalesLabel.setInnerHTML("<b>" + wfmStrings.sales() + "</b>");
        customProjectsLabel.setInnerHTML("<b>" + wfmStrings.projects() + "</b>");
        payrollLabel.setInnerHTML("<b>" + wfmStrings.payroll() + "</b>");

        bronzePriceLabel.setInnerHTML(String.valueOf(this.getPriceLabel(5, 50, 5)));
        silverPriceLabel.setInnerHTML(String.valueOf(this.getPriceLabel(10, 100, 5)));
        goldPriceLabel.setInnerHTML(String.valueOf(this.getPriceLabel(25, 250, 5)));
        this.setCustomPriceLabel();
        this.getData();
        this.registerEventHanlders();

        if (Utils.getStripePublicKey() != null && !Utils.getStripePublicKey().equals("") && !Utils.getStripePublicKey().equalsIgnoreCase("null")) {
            StripePaymentHandler.initializeStripe(new Callback<Void, Exception>() {
                @Override
                public void onFailure(Exception reason) {
                    GWT.log("Could not inject Stripe.js");
                }

                @Override
                public void onSuccess(Void result) {
                    bronzeStripeButton.setVisible(true);
                    silverStripeButton.setVisible(true);
                    goldStripeButton.setVisible(true);
                }
            });
        }
    }

    private void pricePeriod(boolean isMonthly) {
        customPeriod.setInnerHTML(isMonthly ? "/mo" : "/yr");
        bronzePeriod.setInnerHTML(isMonthly ? "/mo" : "/yr");
        silverPeriod.setInnerHTML(isMonthly ? "/mo" : "/yr");
        goldPeriod.setInnerHTML(isMonthly ? "/mo" : "/yr");
    }

    private void paymentDialogBox(MaterialLink payPalButton, MaterialLink stripeButton) {
        KpiModal dialogBox = new KpiModal();
        dialogBox.setCloseButton(true);
        dialogBox.setWidth(300);

        Div pnlContainer = new Div("payment-type--container");
        pnlContainer.add(payPalButton);
        pnlContainer.add(stripeButton);

        enableDisableButtons(true);
        dialogBox.add(pnlContainer);
        dialogBox.open();
    }

    private void registerEventHanlders() {
        DOM.sinkEvents(customAccounts, Event.ONCHANGE);
        DOM.setEventListener(customAccounts, event -> {
            if (customAccounts.isChecked()) {
                customAccounts.removeClassName("checkbox-checked");
                customModuleCount++;
            } else {
                customAccounts.setClassName("checkbox-checked");
                if (customModuleCount > 0) {
                    customModuleCount--;
                }
            }
            setCustomPriceLabel();
        });

        DOM.sinkEvents(customHumans, Event.ONCHANGE);
        DOM.setEventListener(customHumans, event -> {
            if (customHumans.isChecked()) {
                customHumans.removeClassName("checkbox-checked");
                customModuleCount++;
            } else {
                customHumans.addClassName("checkbox-checked");
                if (customModuleCount > 0) {
                    customModuleCount--;
                }
            }
            setCustomPriceLabel();
        });

        DOM.sinkEvents(customSales, Event.ONCHANGE);
        DOM.setEventListener(customSales, event -> {
            if (customSales.isChecked()) {
                customSales.removeClassName("checkbox-checked");
                customModuleCount++;
            } else {
                customSales.addClassName("checkbox-checked");
                if (customModuleCount > 0) {

                    customModuleCount--;
                }
            }
            setCustomPriceLabel();
        });

        DOM.sinkEvents(customProjects, Event.ONCHANGE);
        DOM.setEventListener(customProjects, event -> {
            if (customProjects.isChecked()) {
                customProjects.removeClassName("checkbox-checked");
                customModuleCount++;
            } else {
                customProjects.addClassName("checkbox-checked");
                if (customModuleCount > 0) {
                    customModuleCount--;
                }
            }
            setCustomPriceLabel();
        });

        DOM.sinkEvents(customDocuments, Event.ONCHANGE);
        DOM.setEventListener(customDocuments, event -> {
            if (customDocuments.isChecked()) {
                customDocuments.removeClassName("checkbox-checked");
                customModuleCount++;
            } else {
                customDocuments.addClassName("checkbox-checked");
                if (customModuleCount > 0) {
                    customModuleCount--;
                }
            }
            setCustomPriceLabel();
        });

        DOM.sinkEvents(customIncreaseUser, Event.ONCLICK);
        DOM.setEventListener(customIncreaseUser, event -> {
            setCustomUserCount(true, false);
        });

        DOM.sinkEvents(customDecreaseUser, Event.ONCLICK);
        DOM.setEventListener(customDecreaseUser, event -> {
            setCustomUserCount(false, false);
        });

        DOM.sinkEvents(customUserCount, Event.ONCHANGE);
        DOM.setEventListener(customUserCount, event -> {
            setCustomPriceLabel();
        });

        DOM.sinkEvents(essCustomIncreaseUser, Event.ONCLICK);
        DOM.setEventListener(essCustomIncreaseUser, event -> {
            setCustomUserCount(true, true);
        });

        DOM.sinkEvents(essCustomDecreaseUser, Event.ONCLICK);
        DOM.setEventListener(essCustomDecreaseUser, event -> {
            setCustomUserCount(false, true);
        });

        DOM.sinkEvents(essCustomUserCount, Event.ONCHANGE);
        DOM.setEventListener(essCustomUserCount, event -> {
            setCustomPriceLabel();
        });
    }

    private void getData() {
        LoadingPanel.loading(true);
        MyAccountService.App.get().getCurrentUsagePlan(new AbstractAsyncCallback<UsagePlanItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.error(), Info.Type.WARNING);
            }

            @Override
            public void success(final UsagePlanItem resultUsagePlanItem) {
                LoadingPanel.loading(false);
                setData(resultUsagePlanItem);
            }
        });
    }

    private void setData(final UsagePlanItem usagePlan) {
        this.usagePlan = usagePlan;
        final SubscriptionPaymentItem subscriptionPaymentItem = new SubscriptionPaymentItem();

        if (!usagePlan.isFree() && !usagePlan.isPaid()) {
            isUpgrade = false;
        } else if (!usagePlan.isFree() && usagePlan.isShowUpgBt()) {
            isUpgrade = true;
        }
        final String buttonPaypalLabel = (wfmStrings.buyNow());
        final String buttonStripeLabel = (wfmStrings.buyWithStripe());

        bronzeButton.setText(buttonPaypalLabel);
        silverButton.setText(buttonPaypalLabel);
        goldButton.setText(buttonPaypalLabel);
        customButton.setText(buttonPaypalLabel);

        bronzeStripeButton.setText(buttonStripeLabel);
        silverStripeButton.setText(buttonStripeLabel);
        goldStripeButton.setText(buttonStripeLabel);
        customStripeButton.setText(buttonStripeLabel);

        final HTML upgHtml = new HTML();
        final HorizontalPanelDiv shp = new HorizontalPanelDiv();
        shp.setTextAlign("right");
        upgradePayBt = new Button(myAccountStrings.payNow());
        upgradePayBt.setStyleName("button btn-2 btn-r18");

        currentUsageMonth = usagePlan.getUsageMonth();

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
        String usagePlanPricingPackage = usagePlan.getCategoryREAL();
        if (!usagePlan.isUpgPayed()) {
            upgSubscriptionPaymentItem.usageMonths = 12;
            upgSubscriptionPaymentItem.isGBP = false;
            upgSubscriptionPaymentItem.userCount = usagePlan.getUpgUserCount();
            upgSubscriptionPaymentItem.up = usagePlan.getCostDown();
            upgSubscriptionPaymentItem.serviceType = usagePlan.isAllService();
            upgSubscriptionPaymentItem.usagePeriodID = currentUsagePeriod;
            upgSubscriptionPaymentItem.supportPackageNAME = usagePlan.getUpgSupportPackageNAME();
            upgSubscriptionPaymentItem.categoryREAL = usagePlan.getUpgCategoryREAL();

            subscriptionPaymentItem.usageMonths = 12;
            subscriptionPaymentItem.isGBP = false;

            subscriptionPaymentItem.userCount = usagePlan.getUserCount();
            subscriptionPaymentItem.up = usagePlan.getCostDown();
            subscriptionPaymentItem.serviceType = usagePlan.isAllService();
            subscriptionPaymentItem.usagePeriodID = currentUsagePeriod;
            subscriptionPaymentItem.supportPackageNAME = usagePlan.getSupportPackageNAME();
            subscriptionPaymentItem.categoryREAL = usagePlanPricingPackage;

            this.reCalculateUserRatePER_HOST(usagePlan, upgHtml, shp);

            upgradePayBt.addClickHandler(sender -> {
                float totalTax = 0;
                float total = (float) getUpgTotal(usagePlan);
                final int currentUsageMonth = usagePlan.getUsageMonth();

                String contextPath = "https://" + Utils.getPayPalLink() + "?";
                String paypalURL = contextPath + cmd + "=_xclick-subscriptions&"
                        + business + "=" + Utils.getPayPalAccount() + "&"
                        + currency_code + "=USD&"
                        + amount + "=" + getNumberFormatWithBigDecimal(total) + "&"
                        + "tax" + "=" + getNumberFormatWithBigDecimal(totalTax) + "&"
                        + item_name + "=" + Utils.getProductName() + " - ERP.&"
                        + item_number + "=3&"
                        + "a3" + "=" + getNumberFormatWithBigDecimal(total) + "&"
                        + "p3" + "=" + currentUsageMonth + "&"
                        + "t3" + "=M&"
                        + custom + "=" + usagePlan.getCompanyID() + Constants.SUBSCRIPTION_UPG + subHisId + "&"
                        + returnT + "=" + Utils.getHostURL() + "Myaccount.html" + "&"
                        + cancel_return + "=" + Utils.getHostURL() + "Myaccount.html";
                Utils.redirect(paypalURL);
                upgradePayBt.setEnabled(false);
            });
        }

        currentCategory = FREE_TRIAL;
        if (usagePlan.isFree()) {
            currentCategory = FREE_TRIAL;
        } else if (usagePlan.isPaid()) {
            currentCategory = IS_PAID;//paid category (equally --> plus/premium/max)
        }
        bronzeButton.addClickHandler(this.getButtonClickHandler(PAYPAL_PAYMENT, usagePlan, Constants.PP_BRONZE_));
        bronzeStripeButton.addClickHandler(this.getButtonClickHandler(STRIPE_PAYMENT, usagePlan, Constants.PP_BRONZE_));
        silverButton.addClickHandler(this.getButtonClickHandler(PAYPAL_PAYMENT, usagePlan, Constants.PP_SILVER_));
        silverStripeButton.addClickHandler(this.getButtonClickHandler(STRIPE_PAYMENT, usagePlan, Constants.PP_SILVER_));
        goldButton.addClickHandler(this.getButtonClickHandler(PAYPAL_PAYMENT, usagePlan, Constants.PP_GOLDEN));
        goldStripeButton.addClickHandler(this.getButtonClickHandler(STRIPE_PAYMENT, usagePlan, Constants.PP_GOLDEN));

        customButton.addClickHandler(this.getButtonClickHandler(PAYPAL_PAYMENT, usagePlan, Constants.PP_CUSTOM));
        customStripeButton.addClickHandler(this.getButtonClickHandler(STRIPE_PAYMENT, usagePlan, Constants.PP_CUSTOM));

        if (isUpgrade && Constants.PP_CUSTOM.equals(usagePlanPricingPackage)) {
            customAccounts.setChecked(usagePlan.isAccountsModule());
            if (!customAccounts.isChecked()) {
                customAccounts.removeClassName("checkbox-checked");
            }
            customHumans.setChecked(usagePlan.isHumansModule());
            if (!customHumans.isChecked()) {
                customHumans.removeClassName("checkbox-checked");
            }
            customProjects.setChecked(usagePlan.isProjectModule());
            if (!customProjects.isChecked()) {
                customProjects.removeClassName("checkbox-checked");
            }
            customSales.setChecked(usagePlan.isSalesModule());
            if (!customSales.isChecked()) {
                customSales.removeClassName("checkbox-checked");
            }
        }
        if (usagePlanPricingPackage == null) {
        }
    }

    private double getUpgTotal(UsagePlanItem usagePlan) {
        final double oldPackagePrice = this.getTotalPrice(usagePlan.getUserCount(), usagePlan.getEssUserCount(), 5) / 360;
        final double newPackagePrice = this.getTotalPrice(usagePlan.getUpgUserCount(), usagePlan.getEssUserCount(), 5) / 360;
        final double oldSum = usagePlan.getUpgDayCount() * oldPackagePrice;
        final double upgSum = usagePlan.getUpgDayCount() * newPackagePrice;

        return (upgSum - oldSum);
    }

    // TODO: 9/30/17 fix logic
    private void reCalculateUserRatePER_HOST(UsagePlanItem usagePlan, HTML upgHtml, HorizontalPanelDiv horizontalPanelDiv) {
        subHisId = usagePlan.getUpgSubHisId();
        final Integer userDifference = usagePlan.getUpgUserCount() - usagePlan.getUserCount();
        final StringBuilder text = new StringBuilder();

        text.append("\n <b>You have just upgraded your subscription plan, in order to finalize\n")
                .append("subscription upgrade process please click on the \"Pay Now\" button, ")
                .append("and you will be charged for the ");
        if (userDifference > 0) {
            text.append((usagePlan.getUpgUserCount() - usagePlan.getUserCount())).append(" new employees/</b>");
        }
        final StringBuilder moduleText = new StringBuilder();
        int moduleCount = 0;
        if (!usagePlan.isAccountsModule() && usagePlan.isUpgAccountsModule()) {
            moduleText.append("Accounts");
            moduleCount++;
        }
        if (!usagePlan.isHumansModule() && usagePlan.isUpgHumansModule()) {
            if (moduleText.length() > 0) {
                moduleText.append(", ");
            }
            moduleText.append("Humans");
            moduleCount++;
        }
        if (!usagePlan.isSalesModule() && usagePlan.isUpgSalesModule()) {
            if (moduleText.length() > 0) {
                moduleText.append(", ");
            }
            moduleText.append("Sales");
            moduleCount++;
        }
        if (!usagePlan.isProjectModule() && usagePlan.isUpgProjectModule()) {
            if (moduleText.length() > 0) {
                moduleText.append(", ");
            }
            moduleText.append("Project");
            moduleCount++;
        }
        if (moduleText.length() > 0) {
            moduleText.append(" module");
            if (moduleCount++ > 1) {
                moduleText.append("s ");
            }
        }
        if (userDifference > 0 && moduleText.length() > 0) {
            text.append(" and ");
        }
        text.append(moduleText);

        upgHtml.setHTML(text.toString());
        horizontalPanelDiv.add(upgradePayBt);
    }

    private ClickHandler getButtonClickHandler(final String paymentMethod, final UsagePlanItem usagePlan, final String packageName) {
        return clickEvent -> {
            if (Constants.PP_CUSTOM.equals(packageName) &&
                    (this.getUserCount(packageName) == 0 ||
                            this.getModuleCount(packageName) == 0)) {
                Info.show(myAccountStrings.pleaseChooseModules(), Info.Type.WARNING);
            }
            if (isUpgrade) {
                this.upgradePlan(paymentMethod, usagePlan, packageName);
                return;
            }
            this.createSubscriptionPayPal(paymentMethod, packageName);
        };
    }

    private void upgradePlan(final String paymentMethod, final UsagePlanItem usagePlanItem, final String packageName) {
        final int userCount = this.getUserCount(packageName);
        final int essUserCount = this.getESSUserCount(packageName);
        final int moduleCount = this.getModuleCount(packageName);
        final double totalPrice = this.getTotalPrice(userCount, essUserCount, moduleCount);

        if (totalPrice <= 0d) {
            return;
        }
        if (this.validateBuyAndUpgradeUsersCount(userCount, packageName)) {
            Info.show(wfmStrings.pleaseChooseHigherUpgrade(), Info.Type.WARNING);
            return;
        }
        this.enableDisableButtons(false);
        usagePlan.setTotalAmount((float) totalPrice);
        usagePlanItem.setPlanType(TWELVE_MONTH_TWENTY_30);
        usagePlanItem.setCategoryREAL(PayPalCalculationHelper.getPricingPackageNAME(packageName));
        usagePlanItem.setUserCount(userCount);
        usagePlanItem.setEssUserCount(userCount == 1 ? 1 : userCount == 2 ? 10 : userCount == 5 ? 100 : 1);

        if (packageName.equals(PP_CUSTOM)) {
            usagePlan.setProjectModule(customProjects.isChecked());
            usagePlan.setAccountsModule(customAccounts.isChecked());
            usagePlan.setSalesModule(customSales.isChecked());
            usagePlan.setHumansModule(customHumans.isChecked());
        }
        final String currencyValue = "USD";

        LoadingPanel.loading(true);
        MyAccountService.App.get().createSubscriptionHistory(usagePlanItem, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                enableDisableButtons(true);
            }

            public void success(Integer subscriptionHistoryID) {
                LoadingPanel.loading(false);

                if (subscriptionHistoryID != null) {

                    if (PAYPAL_PAYMENT.equals(paymentMethod)) {
                        //Pay with PayPal
                        payWithPaypal(SUBSCRIPTION_UPG, usagePlanItem, packageName, currencyValue, subscriptionHistoryID);
                    } else if (STRIPE_PAYMENT.equals(paymentMethod)) {

                        payWithStripe(SUBSCRIPTION_UPG, usagePlan, currencyValue, packageName, subscriptionHistoryID);
                    }
                }
            }
        });
    }

    private void createSubscriptionPayPal(final String paymentMethod, final String packageName) {
        final int userCount = this.getUserCount(packageName);
        final int essUserCount = this.getESSUserCount(packageName);
        final int moduleCount = this.getModuleCount(packageName);

        if (userCount <= 0 || moduleCount <= 0) {
            return;
        }
        final double totalPrice = this.getTotalPrice(userCount, essUserCount, moduleCount);

        if (totalPrice <= 0d) {
            return;
        }
        final UsagePlanItem usagePlan = new UsagePlanItem();

        usagePlan.setStorageCount(1);
        usagePlan.setService(ALL_SERVICES);
        usagePlan.setUsageMonth(isMonthly ? 1 : 12);
        usagePlan.setTotalAmount((float) totalPrice);
        usagePlan.setPlanType(TWELVE_MONTH_TWENTY_30);
        String pricingPackageName = "Bronze";

        if (PP_BRONZE_.equals(packageName)) {
            pricingPackageName = "Bronze";
        } else if (PP_SILVER.equals(packageName)) {
            pricingPackageName = "Silver";
        } else if (PP_GOLDEN.equals(packageName)) {
            pricingPackageName = "Gold";
        } else {
            pricingPackageName = "Custom";
            usagePlan.setProjectModule(customProjects.isChecked());
            usagePlan.setAccountsModule(customAccounts.isChecked());
            usagePlan.setSalesModule(customSales.isChecked());
            usagePlan.setHumansModule(customHumans.isChecked());
        }
        usagePlan.setCategoryREAL(PayPalCalculationHelper.getPricingPackageNAME(packageName));
        usagePlan.setUserCount(userCount);
        usagePlan.setEssUserCount(essUserCount);
        this.savePackage(paymentMethod, usagePlan, pricingPackageName);
    }

    private void savePackage(final String paymentMethod, final UsagePlanItem usagePlanItem, final String packageName) {
        final String currencyValue = "USD";

        this.enableDisableButtons(false);
        LoadingPanel.loading(true);

        MyAccountService.App.get().saveUsagePlan(usagePlanItem, new AbstractAsyncCallback<UsagePlanItem>() {
            public void failure(Throwable caught) {
                enableDisableButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.error(), Info.Type.WARNING);
            }

            public void success(UsagePlanItem result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    if (PAYPAL_PAYMENT.equals(paymentMethod)) {
                        payWithPaypal(SUBSCRIPTION_ADD, result, packageName, currencyValue, null);
                    } else if (STRIPE_PAYMENT.equals(paymentMethod)) {
                        payWithStripe(SUBSCRIPTION_ADD, result, currencyValue, packageName, null);
                    }
                }
            }
        });
    }

    private void payWithPaypal(String subscriptionOperation,
                               UsagePlanItem usagePlanItem,
                               String packageName,
                               String currencyValue,
                               Integer subscriptionHistoryID) {
        final String description = Utils.getProductName() + ", " +
                packageName + ", " +
                usagePlanItem.getUserCount() +
                " Users  - Annual Contract";
        String paypalURL = null;

        if (SUBSCRIPTION_ADD.equals(subscriptionOperation)) {
            paypalURL = "https://" + Utils.getPayPalLink() + "?" +
                    cmd + "=_xclick-subscriptions&" +
                    business + "=" + Utils.getPayPalAccount() + "&" +
                    currency_code + "=" + currencyValue + "&" +
                    amount + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount()) + "&" +
                    taxX + "=0&" +
                    item_name + "=" + description + "&" +
                    item_number + "=1&" +
                    a3 + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount()) + "&" +
                    p3 + "=" + usagePlanItem.getUsageMonth() + "&" +
                    t3 + "=M&" +//it is daily , change to "M" - monthly
                    src + "=1&" +
                    custom + "=" + usagePlanItem.getCompanyID() + SUBSCRIPTION_ADD + usagePlanItem.getObjectID() + "&" +
                    returnT + "=" + GWT.getHostPageBaseURL() + "Myaccount.html" + "&" +
                    cancel_return + "=" + GWT.getHostPageBaseURL() + "Myaccount.html";
        } else if (SUBSCRIPTION_UPG.equals(subscriptionOperation)) {
            paypalURL = "https://" + Utils.getPayPalLink() + "?" +
                    cmd + "=_xclick-subscriptions&" +
                    business + "=" + Utils.getPayPalAccount() + "&" +
                    currency_code + "=" + currencyValue + "&" +
                    amount + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount()) + "&" +
                    taxX + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTax()) + "&" +
                    item_name + "=" + description + "&" +
                    item_number + "=2&" +
                    a3 + "=" + getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount()) + "&" +
                    p3 + "=" + usagePlanItem.getUsageMonth() + "&" +
                    t3 + "=M&" +
                    modify + "=2&" +
                    src + "=1&" +
                    custom + "=" + usagePlanItem.getCompanyID() + SUBSCRIPTION_UPG + subscriptionHistoryID + "&" +
                    returnT + "=" + Utils.getHostURL() + "Myaccount.html" + "&" +
                    cancel_return + "=" + Utils.getHostURL() + "Myaccount.html";
        }

        Utils.redirect(paypalURL);

    }

    private void payWithStripe(String subscriptionOperation, UsagePlanItem usagePlanItem, String currencyValue, final String pricingPackageName, Integer subscriptionHistoryID) {

        final String description = Utils.getProductName() + ", " +
                usagePlanItem.getUserCount() +
                " Users  - Annual Contract";
        enableDisableButtons(true);

        StripePaymentHandler.handlePaymentButtonClicked(pricingPackageName, description, usagePlanItem.getTotalAmount() * 100d, new StripeCompletePayment() {
            @Override
            public void completePayment(StripeCheckoutToken stripeCheckoutToken) {
                if (stripeCheckoutToken == null || stripeCheckoutToken.getId() == null) {
                    return;
                }
                enableDisableButtons(false);
                LoadingPanel.loading(true);
                MyAccountService.App.get().chargeForSubscriptionPaymentWithStripe(subscriptionOperation,
                        usagePlanItem,
                        subscriptionHistoryID,
                        stripeCheckoutToken.getId(),
                        currencyValue,
                        description, new AbstractAsyncCallback<Boolean>() {
                            public void failure(Throwable caught) {
                                enableDisableButtons(false);
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.error(), Info.Type.WARNING);
                            }

                            public void success(Boolean result) {
                                LoadingPanel.loading(false);
                                enableDisableButtons(true);
                                if (Boolean.TRUE.equals(result)) {
                                    Info.show("Successfully processed", Info.Type.INFO);
                                    Utils.reloadPage();
                                } else {
                                    Info.show(wfmStrings.error(), Info.Type.WARNING);
                                }
                            }
                        });
            }
        });
    }

    private boolean validateBuyAndUpgradeUsersCount(Integer userCount, String packageName) {
        boolean userLess = false;
        boolean userEqual = false;
        boolean pricingPackageLess = false;
        boolean pricingPackageEqual = false;

        if (this.usagePlan == null || this.usagePlan.getCategoryREAL() == null) {
            return false;
        }
        final Integer currentUserCount_ = FREE_TRIAL.equals(currentCategory)
                ? usagePlan.getRegisteredUsersCount()
                : usagePlan.getUserCount();

        final int selectedPackageId = PP_BRONZE_.equals(packageName)
                ? 1 : (PP_SILVER.equals(packageName)
                ? 2 : (PP_GOLDEN.equals(packageName)
                ? 3 : (PP_CUSTOM.equals(packageName) ? 4 : 0)));

        final int currentPackageId = PP_BRONZE_.equals(this.usagePlan.getCategoryREAL())
                ? 1 : (PP_SILVER.equals(this.usagePlan.getCategoryREAL())
                ? 2 : (PP_GOLDEN.equals(this.usagePlan.getCategoryREAL())
                ? 3 : (PP_CUSTOM.equals(this.usagePlan.getCategoryREAL()) ? 4 : 0)));
        boolean error = false;

        if (userCount < currentUserCount_) {
            userLess = true;
        }
        if (userCount.equals(currentUserCount_)) {
            userEqual = true;
        }
        if (selectedPackageId < currentPackageId) {
            pricingPackageLess = true;
        }
        if (selectedPackageId == currentPackageId) {
            pricingPackageEqual = true;
        }
        if (pricingPackageLess) {
            error = true;
        }
        if (userEqual && pricingPackageEqual && !this.checkForSelectedModules(packageName, this.usagePlan.getCategoryREAL())) {
            error = true;
        }
        if (userLess && (pricingPackageEqual || pricingPackageLess) && !this.checkForSelectedModules(packageName, this.usagePlan.getCategoryREAL())) {
            error = true;
        }
        if (userLess) {
            error = true;
        }
        return error;
    }

    private boolean checkForSelectedModules(String newPackageName, String oldPackageName) {
        if (newPackageName == null || this.usagePlan == null) {
            return false;
        }

        if (PP_CUSTOM.equals(newPackageName) && newPackageName.equals(oldPackageName)) {
            boolean salesModuleSelected = customSales.isChecked();
            boolean humansModuleSelected = customHumans.isChecked();
            boolean projectModuleSelected = customProjects.isChecked();
            boolean accontsModuleSelected = customAccounts.isChecked();
            boolean totalResult = !((usagePlan.isAccountsModule() == accontsModuleSelected) &&
                    (usagePlan.isProjectModule() == projectModuleSelected) &&
                    (usagePlan.isSalesModule() == salesModuleSelected) &&
                    (usagePlan.isHumansModule() == humansModuleSelected));
            return totalResult;

        }
        return true;
    }

    @Override
    protected Widget onInitialize() {
        AllPricingViewUiBinder ourUiBinder = GWT.create(AllPricingViewUiBinder.class);
        add(ourUiBinder.createAndBindUi(this));
//        stick_in_parent();
        draw();
        return null;
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

    @Override
    public ImageResource getIconImage() {
        return MyaccountImageBundles.App.get().currentSubscription();
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private void setCustomUserCount(boolean increase, boolean isEss) {
        Integer userCount = 0;
        try {
            userCount = Integer.parseInt(isEss ? essCustomUserCount.getValue() : customUserCount.getValue());
        } catch (NumberFormatException e) {
        }
        if (userCount == null || userCount <= 0) {
            userCount = 0;
        }
        if (increase) {
            userCount++;
        } else if (userCount > 0) {
            userCount--;
        }
        if (isEss) {
            essCustomUserCount.setValue(userCount + "");
        } else {
            customUserCount.setValue(userCount + "");
        }
        setCustomPriceLabel();

    }

    private void setCustomPriceLabel() {
        Integer userCount = 0;
        Integer essUserCount = 0;
        try {
            userCount = Integer.valueOf(customUserCount.getValue());
            essUserCount = Integer.valueOf(essCustomUserCount.getValue());
        } catch (Exception ignored) {
        }
        if (userCount == null) {
            userCount = 0;
        }
        if (essUserCount == null) {
            essUserCount = 0;
        }
        customPriceLabel.setInnerHTML(this.getPriceLabel(userCount, essUserCount, customModuleCount) + "");
    }

    private double getPerUserDiscount(int userCount) {
        if (userCount <= 0d) {
            return 0;
        }
        if (userCount >= 25) {
            return 15d;
        } else if (userCount >= 10) {
            return 10d;
        } else if (userCount >= 5) {
            return 5d;
        }
        return 0d;
    }

    private double getPerESSUserDiscount(int userCount) {
        if (userCount <= 0d) {
            return 0;
        }
        if (userCount >= 500) {
            return 20d;
        } else if (userCount >= 250) {
            return 15d;
        } else if (userCount >= 100) {
            return 10d;
        } else if (userCount >= 50) {
            return 5d;
        }
        return 0d;
    }

    private double getPerModuleDiscount(int moduleCount) {
        if (moduleCount <= 0) {
            return 0d;
        }
        if (moduleCount == 5) {
            return 20d;
        } else if (moduleCount == 4) {
            return 15d;
        } else if (moduleCount == 3) {
            return 10d;
        } else if (moduleCount == 2) {
            return 5;
        }
        return 0d;
    }

    private double getTotalPrice(int userCount, int essUserCount, int moduleCount) {
        if (userCount <= 0 || moduleCount <= 0) {
            return 0d;
        }
        double userDiscount = this.getPerUserDiscount(userCount);
        double essUserDiscount = this.getPerESSUserDiscount(essUserCount);
        double moduleDiscount = this.getPerModuleDiscount(moduleCount);

        double userDiscountedPrice = ((this.perUserPrice * userCount * moduleCount) * userDiscount) / 100d;
        double moduleDiscountedPrice = (((this.perUserPrice * userCount * moduleCount) - userDiscountedPrice) * moduleDiscount) / 100d;
        double payment = (this.perUserPrice * userCount * moduleCount) - (userDiscountedPrice + moduleDiscountedPrice);
        if (!isMonthly) {
            payment = ((payment * 12d) * 0.90);
        }
        double essUserDiscountedPrice = ((this.perEssUserPrice * essUserCount * moduleCount) * essUserDiscount) / 100d;
        double essModuleDiscountedPrice = (((this.perEssUserPrice * essUserCount * moduleCount) - essUserDiscountedPrice) * moduleDiscount) / 100d;
        double essPayment = (this.perEssUserPrice * essUserCount * moduleCount) - (essUserDiscountedPrice + essModuleDiscountedPrice);
        if (!isMonthly) {
            essPayment = ((essPayment * 12d) * 0.90);
        }
        BigDecimal totalPrice = new BigDecimal(payment + essPayment);
        totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP);
        return totalPrice.doubleValue();
    }

    private double getPriceLabel(int userCount, int essUserCount, int moduleCount) {
        if (userCount <= 0 || moduleCount <= 0) {
            return 0d;
        }
        return this.getTotalPrice(userCount, essUserCount, moduleCount);
    }

    private int getUserCount(String packageName) {
        if (packageName == null) {
            return 0;
        }
        switch (packageName) {
            case PP_BRONZE_:
                return 5;
            case PP_SILVER:
                return 10;
            case PP_GOLDEN:
                return 25;
            case PP_CUSTOM:
                Integer userCount = null;
                try {
                    userCount = Integer.parseInt(customUserCount.getValue());
                } catch (Exception ignored) {
                }
                if (userCount == null) {
                    userCount = 0;
                }
                return userCount;
        }
        return 0;
    }

    private int getESSUserCount(String packageName) {
        if (packageName == null) {
            return 0;
        }
        switch (packageName) {
            case PP_BRONZE_:
                return 50;
            case PP_SILVER:
                return 100;
            case PP_GOLDEN:
                return 250;
            case PP_CUSTOM:
                Integer essUserCount = null;
                try {
                    essUserCount = Integer.parseInt(essCustomUserCount.getValue());
                } catch (Exception ignored) {
                }
                if (essUserCount == null) {
                    essUserCount = 0;
                }
                return essUserCount;
        }
        return 0;
    }

    private int getModuleCount(String packageName) {
        if (packageName == null) {
            return 0;
        }
        switch (packageName) {
            case PP_BRONZE_:
            case PP_SILVER:
            case PP_GOLDEN:
                return 5;
            case PP_CUSTOM:
                return customModuleCount;
        }
        return 5;
    }

//    @Override
//    protected void onAttach() {
//        super.onAttach();
//        stick_in_parent();
//    }

//    @Override
//    protected void onDetach() {
//        stick_detach();
//        super.onDetach();
//    }

//    private native void stick_in_parent() /*-{
//        $wnd.jQuery(".priceTable-plansHeader").stick_in_parent();
//    }-*/;

//    private native void stick_detach() /*-{
//        $wnd.jQuery('.priceTable-plansHeader').trigger("sticky_kit:detach");
//    }-*/;

    private void enableDisableButtons(boolean enable) {
        silverButton.setEnabled(enable);
        silverStripeButton.setEnabled(enable);
        bronzeButton.setEnabled(enable);
        bronzeStripeButton.setEnabled(enable);
        goldButton.setEnabled(enable);
        goldStripeButton.setEnabled(enable);
    }

    private String getNumberFormatWithBigDecimal(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toString();
    }

    public interface PricingStyle extends ClientBundle {
        @CssResource.NotStrict
        @ClientBundle.Source("AllPricingView.css")
        CssResource markupPricing();
    }

    interface AllPricingViewUiBinder extends UiBinder<HTMLPanel, AllPricingView> {
    }
}
