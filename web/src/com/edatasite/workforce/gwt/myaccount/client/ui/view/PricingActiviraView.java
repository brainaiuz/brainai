package com.edatasite.workforce.gwt.myaccount.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
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
import com.google.gwt.dom.client.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Fatxulla
 * Date: 11/20/12
 * Time: 1:51 PM
 */
public class PricingActiviraView extends View implements Constants {
    interface PricingActiviraViewUiBinder extends UiBinder<HTMLPanel, PricingActiviraView> {
    }

    
    private static final MyAccountStrings myAccountStrings = MyAccountStrings.App.get();

    private UsagePlanItem afterCalculateUsagePlanItem_u;
    private String currencyValue = Utils.getCurrencyCODEbyHOST();

    private String currentSupportPackageNAME;
    private Integer currentSupportPackageID;

    private String selectedPricingPackageNAME;
    private String selectedSupportPackageNAME;
    private Integer selectedSupportPackageID = -1;

    private Integer currentUsagePeriod = 0;
    private int currentUsageMonth = 0;

    private InitHelpContainer initHelpContainer;
    private reCalculateUserRatePER_H reCalculateUserRatePER_h;
    private reCalculateUserRatePER_H2 reCalculateUserRatePER_h2;

    private HashMap<String, Double> supportPackagePrices;

    private final boolean isGetQuote = false;

    private boolean isUKClient = false;

    private boolean isContractorRadioButton = false;
    private boolean isEnterpriseRadioButton = false;
    private boolean isProfessionalRadioButton = false;
    private boolean isSmallBusinessRadioButton = false;

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
    final String custom = "custom";                   //
    final String taxX = "tax";                       //
    final String a3 = "a3";                           //5.00
    final String p3 = "p3";                           //1  (1,3,6,12 - Months)
    final String uc = "userCount";                           //1  (1,3,6,12 - Months)
    final String t3 = "t3";                           //M (Month)
    final String src = "src";                       //1,2,3 (Limit the number of billing cycles.)
    final String cancel_return = "cancel_return";  //
    final String returnT = "return";               //
    final String modify = "modify";                   //
    /* ~~~~~~~~~~~~~~~~~~~~~~~ values of Algotirm ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
    private long countNumberOfUsers = 0;
    private long countCheckedModulesOfFeatures = 0;
    private double yourSubscription = 1d;
    private double totalPrice = 0d;

    @UiField
    HeadingElement pricingPackagesHeader;
    @UiField
    HeadingElement smartBundlesTitle;
    @UiField
    DivElement smartBundlesHeader1;
    @UiField
    HeadingElement smartBundlesRowCost1;
    @UiField
    HeadingElement smartBundlesRowNumber1;
    @UiField
    Anchor miniPackagePayPal;
    @UiField
    Anchor miniPackageWorldPay;
    @UiField
    ButtonElement miniPackage;
    @UiField
    DivElement smartBundlesHeader2;
    @UiField
    HeadingElement smartBundlesRowCost2;
    @UiField
    HeadingElement smartBundlesRowNumber2;
    @UiField
    Anchor smallPackagePayPal;
    @UiField
    Anchor smallPackageWorldPay;
    @UiField
    ButtonElement smallPackage;
    @UiField
    DivElement smartBundlesHeader3;
    @UiField
    HeadingElement smartBundlesRowCost3;
    @UiField
    HeadingElement smartBundlesRowNumber3;
    @UiField
    Anchor standartPackagePayPal;
    @UiField
    Anchor standartPackageWorldPay;
    @UiField
    ButtonElement standartPackage;
    @UiField
    DivElement smartBundlesHeader4;
    @UiField
    HeadingElement smartBundlesRowCost4;
    @UiField
    HeadingElement smartBundlesRowNumber4;
    @UiField
    Anchor silverPackagePayPal;
    @UiField
    Anchor silverPackageWorldPay;
    @UiField
    ButtonElement silverPackage;
    @UiField
    Anchor enterPricePackagePaypal;
    @UiField
    Anchor enterPricePackageWorldPay;
    @UiField
    ButtonElement enterPricePackage;
    @UiField
    DivElement smartBundlesHeader5;
    @UiField
    HeadingElement smartBundlesRowCost5;
    @UiField
    HeadingElement smartBundlesRowNumber5;
    // CUSTOMISE YOUR PLAN
    @UiField
    HTMLPanel customisePanel;
    @UiField
    DivElement numberOfUsersTitle;
    @UiField
    InputElement numberOfUsersInput;
    @UiField
    DivElement modulesOfFeature;
    @UiField
    KpiCheckBox projectsCheckBox;
    @UiField
    KpiCheckBox accountingCheckBox;
    @UiField
    KpiCheckBox crmCheckBox;
    @UiField
    KpiCheckBox hrmsCheckBox;
    @UiField
    SpanElement projectsPrice;
    @UiField
    SpanElement accountingPrice;
    @UiField
    SpanElement crmPrice;
    @UiField
    SpanElement hrmsPrice;
    @UiField
    TableCellElement messageMyWorspaceDocumentsEmailSupport;
    @UiField
    DivElement yourSubscriptionTitle;
    @UiField
    Anchor monthly;
    @UiField
    Anchor quarterly;
    @UiField
    Anchor semiannual;
    @UiField
    Anchor annual;
    @UiField
    DivElement selectedNumberOfUsersTitle;
    @UiField
    DivElement consultingServiceTitle;
    @UiField
    HeadingElement totalPriceTitle;
    @UiField
    SpanElement pointerUp;
    @UiField
    SpanElement pointerDown;
    @UiField
    ButtonElement buyNowTotalPrice;
    @UiField
    Anchor buyNowTotalPricePayPal;
    @UiField
    Anchor buyNowTotalPriceWorldPay;
    //OUR OPTIONAL CONSULTING SERVICES LET US ASSIST YOU!
    @UiField
    TableColElement contractorColColumn;
    @UiField
    TableColElement smallBusinessColColumn;
    @UiField
    TableColElement professionalColColumn;
    @UiField
    TableColElement enterpriseColColumn;
    @UiField
    TableCellElement contractorTableHeader;
    @UiField
    TableCellElement smallBusinessTableHeader;
    @UiField
    TableCellElement professionalTableHeader;
    @UiField
    TableCellElement enterpriseTableHeader;
    @UiField
    RadioButton contractorRadioButton;
    @UiField
    RadioButton smallBusinessRadioButton;
    @UiField
    RadioButton professionalRadioButton;
    @UiField
    RadioButton enterpriseRadioButton;
    @UiField
    TableCellElement oneOffPrice;
    @UiField
    SpanElement priceOne;
    @UiField
    SpanElement priceTwo;
    @UiField
    SpanElement priceThree;
    @UiField
    SpanElement priceFour;
    @UiField
    TableCellElement trainingSessions;
    @UiField
    TableCellElement onBoardingSupportPeriod;
    @UiField
    TableCellElement liveChatSupportDuringOnBoarding;
    @UiField
    TableCellElement addingNoAccessUsers;
    @UiField
    TableCellElement phoneIMSupport;
    @UiField
    TableCellElement customerAccess;
    @UiField
    TableCellElement scheduledBackup;
    @UiField
    TableCellElement additionalStorage;
    @UiField
    TableCellElement emailMarketing;
    @UiField
    TableCellElement customLoginSubdomain;
    @UiField
    TableCellElement importingDocuments;
    @UiField
    TableCellElement customisePDFLayout;
    @UiField
    TableCellElement website;
    @UiField
    TableCellElement ecommerceWebsite;
    @UiField
    TableCellElement customiseReport;
    @UiField
    TableCellElement additionalImplementationRate;
    @UiField
    TableCellElement onsiteSupport;
    @UiField
    TableCellElement message449USDPerDayGlobally;
    @UiField
    TableCellElement messagePleaseContactUsforMore;
    @UiField
    ParagraphElement messageWeWillHaveToDisableMassMailing;
    //UI fields
    @UiField
    HTMLPanel header;
    @UiField
    HTMLPanel headerDIV;
    @UiField
    HorizontalPanel testPanel;

    public PricingActiviraView() {
        super("pricingActiviraViewNEW", wfmStrings.currentSubscription());
    }

    @Override
    public String getIconStyle() {
        return null;
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

    @Override
    public ImageResource getIconImage() {
        return MyaccountImageBundles.App.get().currentSubscription();
    }

    @Override
    protected Widget onInitialize() {
        PricingActiviraViewUiBinder ourUiBinder = GWT.create(PricingActiviraViewUiBinder.class);
        add(ourUiBinder.createAndBindUi(this));
        initialize();
        return null;
    }

    private UsagePlanItem calculateClickedMonth(String categoryType, UserRateItem userRateItem, final Integer userCount, Integer clickedPeriodID) {
        //no calculation
        final boolean currencyGBR = isUKClient;

        final boolean includeTax = false;
        final float userRate = userRateItem.getUserRate().floatValue();

        final double vat_rateN = Utils.getVAT_RATE() != null
                ? Utils.getVAT_RATE()
                : VAT_RATE;//default UK based VAT RATE
        final double[][] discount_per_monthly2 = {{userRateItem.getDiscountOneMonth(), userRateItem.getDiscountThreeMonth(),
                userRateItem.getDiscountSixMonth(), userRateItem.getDiscountTwentyMonth()}}; // Example: (1 month - not discount), 3 month - 15%, 6 month - 25%, 12 month - 30%;
        return PayPalCalculationHelper.calculateUsagePlanNEW(categoryType, clickedPeriodID, selectedSupportPackageID, userRateItem.getSupportPackagePrice(),
                userCount, currencyGBR, includeTax, 0, userRate, Utils.getHostName(), vat_rateN, discount_per_monthly2);
    }

    private String getNumberFormatWithBigDecimal(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toString();
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
                CommonService.App.get().getSupportPackagePrices(Utils.getHostName(), new AbstractAsyncCallback<HashMap<String, Double>>() {
                    @Override
                    public void success(HashMap<String, Double> result) {
                        supportPackagePrices = result;
                        //draw currently usage plan
                        if (result_usagePlanItem != null) {
                            drawUsagePlanItem(result_usagePlanItem);
                        }
                    }
                });
            }
        });
    }

    private void drawUsagePlanItem(final UsagePlanItem result_usagePlanItem) {
        final SubscriptionPaymentItem subscriptionPaymentItem = new SubscriptionPaymentItem();
        final HTML upgHtml = new HTML();
        final HorizontalPanelDiv shp = new HorizontalPanelDiv();
        shp.setTextAlign("right");
        upgradePayBt = new Button(myAccountStrings.payNow());
        upgradePayBt.setStyleName("button btn-2 btn-r18");

        //
        currentSupportPackageNAME = result_usagePlanItem.getSupportPackageNAME();
        if (!"".equals(currentSupportPackageNAME) && currentSupportPackageNAME != null) {
            setCurrentSupportPackage();
        }

        currentSupportPackageID = PayPalCalculationHelper.getSupportPackageID(currentSupportPackageNAME);

        currentUsageMonth = result_usagePlanItem.getUsageMonth();
        isUKClient = result_usagePlanItem.isCurrencyGBP();

        if (UsagePlanItem.CUSTOM.equals(result_usagePlanItem.getCategoryREAL())) {
            countNumberOfUsers = result_usagePlanItem.getUserCount().longValue();
            numberOfUsersInput.setValue(countNumberOfUsers + "");
            selectedNumberOfUsersTitle.setInnerHTML("Number of users: <span>" + countNumberOfUsers + "</span>");
            setModules(result_usagePlanItem.getModules());
        }
        if (currentUsageMonth == 0) {
            currentUsagePeriod = -1;//      free trial usage month
        } else if (currentUsageMonth == 1) {
            currentUsagePeriod = 0;//       one month
            monthly.setStyleName("active");
            removeActiveClassFromAnchor(monthly);
            yourSubscription = 1;
        } else if (currentUsageMonth == 3) {
            currentUsagePeriod = 1;//0      three month
            quarterly.setStyleName("active");
            removeActiveClassFromAnchor(quarterly);
            yourSubscription = 0.1;
        } else if (currentUsageMonth == 6) {
            currentUsagePeriod = 2;//1      six month
            semiannual.setStyleName("active");
            removeActiveClassFromAnchor(semiannual);
            yourSubscription = (float) 0.15;
        } else if (currentUsageMonth == 12) {
            currentUsagePeriod = 3;//2      12 month
            annual.setStyleName("active");
            removeActiveClassFromAnchor(annual);
            yourSubscription = (float) 0.2;
        }
        calculateTotalPrice();

        //initialize isUpgrade
        if (!result_usagePlanItem.isFree() && !result_usagePlanItem.isPaid()) {
            isUpgrade = false;
        } else if (!result_usagePlanItem.isFree() && result_usagePlanItem.isShowUpgBt()) {
            isUpgrade = true;
        }

        //buy now buttons -/- get quote buttons -/- upgrade buttons -/
        registerBuyNowButtonListener(result_usagePlanItem, miniPackagePayPal, Constants.MINI_SIGN, 2, false, false, 0);
        registerBuyNowButtonListener(result_usagePlanItem, smallPackagePayPal, Constants.SMALL_SIGN, 5, false, false, 0);
        registerBuyNowButtonListener(result_usagePlanItem, standartPackagePayPal, Constants.STANDARD_SIGN, 10, false, false, 0);
        registerBuyNowButtonListener(result_usagePlanItem, silverPackagePayPal, Constants.SILVER_SIGN, 25, false, false, 0);
        registerBuyNowButtonListener(result_usagePlanItem, enterPricePackagePaypal, Constants.ENTERPRISE_SIGN, 50, false, false, 0);

        registerBuyNowButtonListener(result_usagePlanItem, miniPackageWorldPay, Constants.MINI_SIGN_ANNUAL, 2, false, false, 3);
        registerBuyNowButtonListener(result_usagePlanItem, smallPackageWorldPay, Constants.SMALL_SIGN_ANNUAL, 5, false, false, 3);
        registerBuyNowButtonListener(result_usagePlanItem, standartPackageWorldPay, Constants.STANDARD_SIGN_ANNUAL, 10, false, false, 3);
        registerBuyNowButtonListener(result_usagePlanItem, silverPackageWorldPay, Constants.SILVER_SIGN_ANNUAL, 25, false, false, 3);
        registerBuyNowButtonListener(result_usagePlanItem, enterPricePackageWorldPay, Constants.ENTERPRISE_SIGN_ANNUAL, 50, false, false, 3);

        if (isUpgrade) {
            miniPackagePayPal.setText("Monthly Upgrade");
            smallPackagePayPal.setText("Monthly Upgrade");
            standartPackagePayPal.setText("Monthly Upgrade");
            silverPackagePayPal.setText("Monthly Upgrade");
            enterPricePackagePaypal.setText("Monthly Upgrade");

            miniPackageWorldPay.setText("Annual Upgrade (15% Off)");
            smallPackageWorldPay.setText("Annual Upgrade (15% Off)");
            standartPackageWorldPay.setText("Annual Upgrade (15% Off)");
            silverPackageWorldPay.setText("Annual Upgrade (15% Off)");
            enterPricePackageWorldPay.setText("Annual Upgrade (15% Off)");
        } else {
            miniPackagePayPal.setText("Monthly Subscription");
            smallPackagePayPal.setText("Monthly Subscription");
            standartPackagePayPal.setText("Monthly Subscription");
            silverPackagePayPal.setText("Monthly Subscription");
            enterPricePackagePaypal.setText("Monthly Subscription");

            miniPackageWorldPay.setText("Annual Subscription (15% Off)");
            smallPackageWorldPay.setText("Annual Subscription (15% Off)");
            standartPackageWorldPay.setText("Annual Subscription (15% Off)");
            silverPackageWorldPay.setText("Annual Subscription (15% Off)");
            enterPricePackageWorldPay.setText("Annual Subscription (15% Off)");
        }

        setBuyNowOrUpgrade();
        String buttonLabel = (isUpgrade
                ? wfmStrings.upgradeOnly()
                : wfmStrings.buyNow()) + "<span class=\"caret\"></span>";
        miniPackage.setInnerHTML(buttonLabel);
        smallPackage.setInnerHTML(buttonLabel);
        standartPackage.setInnerHTML(buttonLabel);
        silverPackage.setInnerHTML(buttonLabel);
        enterPricePackage.setInnerHTML(buttonLabel);

        currencyValue = isUKClient ? "GBP" : Utils.getCurrencyCODEbyHOST();
        serviceType.setHTML(result_usagePlanItem.getService());

        if (!result_usagePlanItem.isUpgPayed()) {

            final PayPalCalculationHelper upgPayPalCalculationHelper = new PayPalCalculationHelper();
            upgSubscriptionPaymentItem.usageMonths = currentUsageMonth;
            upgSubscriptionPaymentItem.isGBP = isUKClient;
            upgSubscriptionPaymentItem.storage = result_usagePlanItem.getUpgStorageCount();
            upgSubscriptionPaymentItem.userCount = result_usagePlanItem.getUpgUserCount();
            upgSubscriptionPaymentItem.up = result_usagePlanItem.getCostDown();
            upgSubscriptionPaymentItem.serviceType = result_usagePlanItem.isAllService();
            upgSubscriptionPaymentItem.usagePeriodID = currentUsagePeriod;
            upgSubscriptionPaymentItem.supportPackageNAME = result_usagePlanItem.getUpgSupportPackageNAME();
            upgSubscriptionPaymentItem.categoryREAL = result_usagePlanItem.getUpgCategoryREAL();
            //
            subscriptionPaymentItem.usageMonths = currentUsageMonth;
            subscriptionPaymentItem.isGBP = isUKClient;
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

                reCalculateUserRatePER_HOST(result_usagePlanItem, upgHtml, shp);
            };
        }

        //
        if (result_usagePlanItem.isFree()) {
        } else if (result_usagePlanItem.isPaid()) {
        }

        if (initHelpContainer != null) {
            initHelpContainer.initHelp(result_usagePlanItem);
        }

        //initialize upgrade pay button
        upgradePayBt.addClickHandler(sender -> {
            float totalTax = 0;

            float perUserPerDay = upgSubscriptionPaymentItem.perUserCost / upgSubscriptionPaymentItem.usageMonths / upgSubscriptionPaymentItem.userCount / 30;
            float total = (upgSubscriptionPaymentItem.userCount - subscriptionPaymentItem.userCount) * result_usagePlanItem.getUpgDayCount() * perUserPerDay;

            String contextPath = "https://" + Utils.getPayPalLink() + "?";
            String paypalURL = contextPath + cmd + "=_xclick-subscriptions&"
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
                    + cancel_return + "=" + Utils.getHostURL() + "Myaccount.html";
            Utils.redirect(paypalURL);
            upgradePayBt.setEnabled(false);
        });
        //
        headerDIV.add(upgHtml);
        headerDIV.add(shp);

        calculateTotalPrice();
    }

    private void setCurrentSupportPackage() {
        //select support package radioButton
        if (SP_CONTRACTOR.equals(currentSupportPackageNAME)) {
            isContractorRadioButton = true;
            contractorRadioButton.setValue(isContractorRadioButton);
            selectedSupportPackageNAME = currentSupportPackageNAME;
            selectedSupportPackageID = currentSupportPackageID;
            setValueOfConsultingService("Contractor", isContractorRadioButton);
            setActiveClassFromColumns(isContractorRadioButton, contractorColColumn);
            removeValueFromRadioButtons(contractorRadioButton);
        } else if (SP_SMALL_BUSINESS.equals(currentSupportPackageNAME)) {
            isSmallBusinessRadioButton = true;
            smallBusinessRadioButton.setValue(isSmallBusinessRadioButton);
            selectedSupportPackageNAME = currentSupportPackageNAME;
            selectedSupportPackageID = currentSupportPackageID;
            setValueOfConsultingService("SmallBusiness", isSmallBusinessRadioButton);
            setActiveClassFromColumns(isSmallBusinessRadioButton, smallBusinessColColumn);
            removeValueFromRadioButtons(smallBusinessRadioButton);
        } else if (SP_PROFESSIONAL.equals(currentSupportPackageNAME)) {
            isProfessionalRadioButton = true;
            professionalRadioButton.setValue(isProfessionalRadioButton);
            selectedSupportPackageNAME = currentSupportPackageNAME;
            selectedSupportPackageID = currentSupportPackageID;
            setValueOfConsultingService("Professional", isProfessionalRadioButton);
            setActiveClassFromColumns(isProfessionalRadioButton, professionalColColumn);
            removeValueFromRadioButtons(professionalRadioButton);
        } else if (SP_ENTERPRISE.equals(currentSupportPackageNAME)) {
            isEnterpriseRadioButton = true;
            enterpriseRadioButton.setValue(isEnterpriseRadioButton);
            selectedSupportPackageNAME = currentSupportPackageNAME;
            selectedSupportPackageID = currentSupportPackageID;
            setValueOfConsultingService("Enterprise", isEnterpriseRadioButton);
            setActiveClassFromColumns(isEnterpriseRadioButton, enterpriseColColumn);
            removeValueFromRadioButtons(enterpriseRadioButton);
        }
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

    private void initialize() {

        header.setVisible(false);
        serviceType = new HTML("");

        draw();

        initializeSmartBundles();
        initializeCustomiseYourPlan();
        initializeOurOptioanlConsulting();
        initRadioButtonEvent();

        initializeInputNumberEvents();

        DOM.sinkEvents(pointerUp, Event.ONCLICK);
        DOM.setEventListener(pointerUp, event -> {
            countNumberOfUsers++;
            numberOfUsersInput.setValue(String.valueOf(countNumberOfUsers));
            selectedNumberOfUsersTitle.setInnerHTML("Number of users: <span>" + countNumberOfUsers + "</span>");
            calculateTotalPrice();
        });
        DOM.sinkEvents(pointerDown, Event.ONCLICK);
        DOM.setEventListener(pointerDown, event -> {
            if (countNumberOfUsers >= 1) {
                countNumberOfUsers--;
                numberOfUsersInput.setValue(String.valueOf(countNumberOfUsers));
                selectedNumberOfUsersTitle.setInnerHTML("Number of users: <span>" + countNumberOfUsers + "</span>");
            }
            calculateTotalPrice();
        });

    }

    private void setBuyNowOrUpgrade() {
        buyNowTotalPrice.setInnerHTML((isUpgrade
                ? wfmStrings.upgradeOnly()
                : wfmStrings.buyNow()) + "<span class=\"caret\"></span>");
    }

    private void initializeInputNumberEvents() {
        DOM.sinkEvents(numberOfUsersInput, Event.ONKEYUP);
        DOM.setEventListener(numberOfUsersInput, event -> {
            String inputValue = numberOfUsersInput.getValue();
            if (!"".equals(inputValue)) {
                if (inputValue.matches("[0-9]*")) {
                    long number = Integer.parseInt(inputValue);
                    countNumberOfUsers = number;
                    if (countCheckedModulesOfFeatures > 0) {
                        calculateTotalPrice();
                    }
                    selectedNumberOfUsersTitle.setInnerHTML("Number of users: <span>" + number + "</span>");
                } else {
                    numberOfUsersInput.setValue(inputValue.substring(0, inputValue.toCharArray().length - 1));
                }
            }
        });
    }

    @UiHandler("monthly")
    void clickMonthlyEvent(ClickEvent event) {
        monthly.setStyleName("active");
        removeActiveClassFromAnchor(monthly);
        yourSubscription = 1;
        if (countNumberOfUsers > 0 && countCheckedModulesOfFeatures > 0) {
            calculateTotalPrice();
        }
    }

    @UiHandler("quarterly")
    void clickQuarterlyEvent(ClickEvent event) {
        quarterly.setStyleName("active");
        removeActiveClassFromAnchor(quarterly);
        yourSubscription = (float) 0.1;
        if (countNumberOfUsers > 0 && countCheckedModulesOfFeatures > 0) {
            calculateTotalPrice();
        }
    }

    @UiHandler("semiannual")
    void clickSemiannualEvent(ClickEvent event) {
        semiannual.setStyleName("active");
        removeActiveClassFromAnchor(semiannual);
        yourSubscription = (float) 0.15;
        if (countNumberOfUsers > 0 && countCheckedModulesOfFeatures > 0) {
            calculateTotalPrice();
        }
    }

    @UiHandler("annual")
    void clickAnnualEvent(ClickEvent event) {
        annual.setStyleName("active");
        removeActiveClassFromAnchor(annual);
        yourSubscription = (float) 0.2;
        if (countNumberOfUsers > 0 && countCheckedModulesOfFeatures > 0) {
            calculateTotalPrice();
        }
    }


    private void removeActiveClassFromAnchor(Anchor element) {
        ArrayList<Anchor> list = new ArrayList<>();
        list.add(0, monthly);
        list.add(1, quarterly);
        list.add(2, annual);
        list.add(3, semiannual);
        for (Anchor item : list) {
            if (!item.equals(element)) {
                item.setStyleName("none");
            }
        }
    }

    private void initRadioButtonEvent() {
        contractorRadioButton.addClickHandler(clickEvent -> {
            isContractorRadioButton = !isContractorRadioButton;
            setSelectedSupportPackage(isContractorRadioButton, Constants.SP_CONTRACTOR);
            contractorRadioButton.setValue(isContractorRadioButton);
            setValueOfConsultingService("Contractor", isContractorRadioButton);
            setActiveClassFromColumns(isContractorRadioButton, contractorColColumn);
            removeValueFromRadioButtons(contractorRadioButton);
            calculateTotalPrice();
        });
        smallBusinessRadioButton.addClickHandler(clickEvent -> {
            isSmallBusinessRadioButton = !isSmallBusinessRadioButton;
            setSelectedSupportPackage(isSmallBusinessRadioButton, Constants.SP_SMALL_BUSINESS);
            smallBusinessRadioButton.setValue(isSmallBusinessRadioButton);
            setValueOfConsultingService("Small Business", isSmallBusinessRadioButton);
            setActiveClassFromColumns(isSmallBusinessRadioButton, smallBusinessColColumn);
            removeValueFromRadioButtons(smallBusinessRadioButton);
            calculateTotalPrice();
        });

        professionalRadioButton.addClickHandler(clickEvent -> {
            isProfessionalRadioButton = !isProfessionalRadioButton;
            setSelectedSupportPackage(isProfessionalRadioButton, Constants.SP_PROFESSIONAL);
            professionalRadioButton.setValue(isProfessionalRadioButton);
            setValueOfConsultingService("Professional", isProfessionalRadioButton);
            setActiveClassFromColumns(isProfessionalRadioButton, professionalColColumn);
            removeValueFromRadioButtons(professionalRadioButton);
            calculateTotalPrice();
        });
        enterpriseRadioButton.addClickHandler(clickEvent -> {
            isEnterpriseRadioButton = !isEnterpriseRadioButton;
            setSelectedSupportPackage(isEnterpriseRadioButton, Constants.SP_ENTERPRISE);
            enterpriseRadioButton.setValue(isEnterpriseRadioButton);
            setValueOfConsultingService("Enterprise", isEnterpriseRadioButton);
            setActiveClassFromColumns(isEnterpriseRadioButton, enterpriseColColumn);
            removeValueFromRadioButtons(enterpriseRadioButton);
            calculateTotalPrice();
        });
    }

    private void setSelectedSupportPackage(boolean isSelected, String spPackage) {
        String selectedPagkageName = null;
        Integer selectedPackageID = null;
        switch (spPackage) {
            case Constants.SP_CONTRACTOR:
                selectedPagkageName = Constants.SP_CONTRACTOR;
                selectedPackageID = 0;
                break;
            case Constants.SP_SMALL_BUSINESS:
                selectedPagkageName = Constants.SP_SMALL_BUSINESS;
                selectedPackageID = 1;
                break;
            case Constants.SP_PROFESSIONAL:
                selectedPagkageName = Constants.SP_PROFESSIONAL;
                selectedPackageID = 2;
                break;
            case Constants.SP_ENTERPRISE:
                selectedPagkageName = Constants.SP_ENTERPRISE;
                selectedPackageID = 3;
                break;
        }
        if (isSelected) {
            selectedSupportPackageNAME = selectedPagkageName;
            selectedSupportPackageID = selectedPackageID;
        } else {
            selectedSupportPackageNAME = null;
            selectedSupportPackageID = -1;
        }
    }

    private void setValueOfConsultingService(String selectedRadioButton, boolean isSelected) {
        if (isSelected) {
            consultingServiceTitle.setInnerHTML("Consulting Service: <span>" + selectedRadioButton + "</span>");
        } else {
            consultingServiceTitle.setInnerHTML("Consulting Service: <span>Not Selected</span>");
        }
    }

    private void setActiveClassFromColumns(boolean isActive, TableColElement element) {
        if (isActive) {
            element.setClassName("slim active");
            removeActiveClassFromColColumns(element);
        } else {
            element.setClassName("slim");
        }
    }

    private void removeActiveClassFromColColumns(TableColElement element) {
        ArrayList<TableColElement> list = new ArrayList<>();
        list.add(0, contractorColColumn);
        list.add(1, smallBusinessColColumn);
        list.add(2, professionalColColumn);
        list.add(3, enterpriseColColumn);
        for (TableColElement item : list) {
            if (!item.equals(element)) {
                item.setClassName("");
            }
        }
    }

    private void removeValueFromRadioButtons(RadioButton radioButton) {
        if (!contractorRadioButton.equals(radioButton)) {
            isContractorRadioButton = false;
        }
        if (!smallBusinessRadioButton.equals(radioButton)) {
            isSmallBusinessRadioButton = false;
        }
        if (!professionalRadioButton.equals(radioButton)) {
            isProfessionalRadioButton = false;
        }
        if (!enterpriseRadioButton.equals(radioButton)) {
            isEnterpriseRadioButton = false;
        }
    }

    private Boolean isAnyRadioButtonSelected() {
        ArrayList<Boolean> list = new ArrayList<>();
        list.add(0, isContractorRadioButton);
        list.add(1, isSmallBusinessRadioButton);
        list.add(2, isProfessionalRadioButton);
        list.add(3, isEnterpriseRadioButton);
        for (Boolean item : list) {
            if (item) {
                return true;
            }
        }
        return false;
    }

    private double getPriceOfSelectedService() {
        if (isAnyRadioButtonSelected()) {
            if (isContractorRadioButton) {
                return 599;
            }
            if (isSmallBusinessRadioButton) {
                return 799;
            }
            if (isProfessionalRadioButton) {
                return 1550;
            }
            if (isEnterpriseRadioButton) {
                return 2995;
            }

        }
        return 0;
    }

    private double getNumberOfUsers() {
        if (countNumberOfUsers > 0) {
            return (double) countNumberOfUsers;
        } else {
            return 0;
        }
    }

    private double getPriceModulesOfFeatures() {
        double resultPrice = 0;
        switch ((int) countCheckedModulesOfFeatures) {
            case 1:
                resultPrice = 9.99;
                break;
            case 2:
                resultPrice = 15.58;
                break;
            case 3:
                resultPrice = 18.37;
                break;
            case 4:
                resultPrice = 19.96;
                break;
        }
        return resultPrice;
    }

    private void setPriceOfYourSubscription() {
        if ((float) yourSubscription == (float) 0.1) {
            totalPrice = (totalPrice - (yourSubscription * totalPrice)) * 3;

        } else if ((float) yourSubscription == (float) 0.15) {
            totalPrice = (totalPrice - (yourSubscription * totalPrice)) * 6;

        } else if ((float) yourSubscription == (float) 0.2) {
            totalPrice = (totalPrice - (yourSubscription * totalPrice)) * 12;

        }
    }

    private void calculateTotalPrice() {
        double numberOfUser = getNumberOfUsers();
        double priceOfService = getPriceOfSelectedService();
        double priceModulesOfFeatures = getPriceModulesOfFeatures();

        totalPrice = numberOfUser * priceModulesOfFeatures;
        setPriceOfYourSubscription();
        totalPrice += priceOfService;
        BigDecimal totalPriceFormat = new BigDecimal(String.valueOf(totalPrice));
        totalPriceFormat.setScale(2, RoundingMode.HALF_UP);
        totalPriceTitle.setInnerHTML("US$ <span>" + totalPriceFormat.setScale(2, RoundingMode.HALF_UP) + "</span>");

        if (totalPrice > 300 && countCheckedModulesOfFeatures > 0 && countNumberOfUsers > 0) {
            setBuyNowOrUpgrade();
            buyNowTotalPrice.setDisabled(false);
        } else if (totalPrice == 0) {
            buyNowTotalPrice.setInnerHTML(wfmStrings.buyNow());
            buyNowTotalPrice.setDisabled(true);
        } else if (numberOfUser > 0 && priceOfService > 0) {
            buyNowTotalPrice.setInnerHTML("Select modules you want to use");
            buyNowTotalPrice.setDisabled(true);
        } else {
            buyNowTotalPrice.setInnerHTML("Consulting package required");
            buyNowTotalPrice.setDisabled(true);
        }
    }

    private void initializeCustomiseYourPlan() {
        // set initialize
        customisePanel.setVisible(false);
        numberOfUsersTitle.setInnerHTML("Number of users");
        modulesOfFeature.setInnerHTML("Modules");
        projectsCheckBox.setValue(false);
        projectsCheckBox.setStyleName("prices");
        accountingCheckBox.setValue(false);
        accountingCheckBox.setStyleName("prices");
        crmCheckBox.setValue(false);
        crmCheckBox.setStyleName("prices");
        hrmsCheckBox.setValue(false);
        hrmsCheckBox.setStyleName("prices");
        projectsPrice.setInnerHTML("US$ 9.99");
        accountingPrice.setInnerHTML("US$ 9.99");
        crmPrice.setInnerHTML("US$ 9.99");
        hrmsPrice.setInnerHTML("US$ 9.99");
        messageMyWorspaceDocumentsEmailSupport.setInnerHTML("My Worspace, Documents, Email Support, Custom Fields, Integration with Google, Reporting &amp; Dashboards included*");
        yourSubscriptionTitle.setInnerHTML("Your Subscription");
        monthly.setHTML("Monthly<br/>&nbsp;<span>&nbsp;</span>");
        monthly.setStyleName("none");
        quarterly.setHTML("Quarterly<br/>10%<span>(Discount)</span>");
        quarterly.setStyleName("none");
        semiannual.setHTML("Semiannual<br/>15%<span>(Discount)</span>");
        semiannual.setStyleName("none");
        annual.setHTML("Annual<br/>20%<span>(Discount)</span>");
        annual.setStyleName("active"); // set default annual
        yourSubscription = (float) 0.2;
        selectedNumberOfUsersTitle.setInnerHTML("Number of users: <span>0</span>");
        consultingServiceTitle.setInnerHTML("Consulting Service: <span>Not Selected</span>");
        totalPriceTitle.setInnerHTML("US$ <span>0.00</span>");
        buyNowTotalPrice.setInnerHTML("Consulting package required");

        projectsCheckBox.addValueChangeHandler(setCountCheckedFeatures());
        accountingCheckBox.addValueChangeHandler(setCountCheckedFeatures());
        crmCheckBox.addValueChangeHandler(setCountCheckedFeatures());
        hrmsCheckBox.addValueChangeHandler(setCountCheckedFeatures());
    }

    private ValueChangeHandler<Boolean> setCountCheckedFeatures() {
        return event -> {
            if (event.getValue()) {
                countCheckedModulesOfFeatures++;
            } else {
                countCheckedModulesOfFeatures--;
            }
            if (countNumberOfUsers != 0) {
                calculateTotalPrice();
            }
        };
    }

    private void initializeOurOptioanlConsulting() {
        contractorRadioButton.setStyleName("consulting");
        enterpriseRadioButton.setStyleName("consulting");
        professionalRadioButton.setStyleName("consulting");
        smallBusinessRadioButton.setStyleName("consulting");
        oneOffPrice.setInnerHTML("One-off Price");
        priceOne.setInnerHTML("US$ 599");
        priceTwo.setInnerHTML("US$ 799");
        priceThree.setInnerHTML("US$ 1,550");
        priceFour.setInnerHTML("US$ 2,995");
        trainingSessions.setInnerHTML("Training sessions");
        onBoardingSupportPeriod.setInnerHTML("On Boarding support period");
        liveChatSupportDuringOnBoarding.setInnerHTML("Live Chat Support During On Boarding");
        addingNoAccessUsers.setInnerHTML("Adding No Access Users");
        phoneIMSupport.setInnerHTML("Phone &amp; IM Support");
        customerAccess.setInnerHTML("Customer Access");
        scheduledBackup.setInnerHTML("Scheduled Backup");
        additionalStorage.setInnerHTML("Additional Storage");
        emailMarketing.setInnerHTML("Email Marketing");
        customLoginSubdomain.setInnerHTML("Custom Login &amp; Subdomain");
        importingDocuments.setInnerHTML("Importing Documents");
        customisePDFLayout.setInnerHTML("Customise PDF Layout");
        website.setInnerHTML("Website");
        ecommerceWebsite.setInnerHTML("Ecommerce Website");
        customiseReport.setInnerHTML("Customise Report");
        additionalImplementationRate.setInnerHTML("Additional Implementation Rate");
        onsiteSupport.setInnerHTML("Onsite Support");
        message449USDPerDayGlobally.setInnerHTML("499 USD Per Day Globally / 700 AED Per Day in UAE.GCC (Excluding Travel Costs)");
        messagePleaseContactUsforMore.setInnerHTML("Please Contact Us for More Details");
        messageWeWillHaveToDisableMassMailing.setInnerHTML("*We will have to disable Mass mailing feature, if any of our customers are reported as spammers");
    }

    private void initializeSmartBundles() {
        // row Pricing Package
        pricingPackagesHeader.setInnerHTML("Pricing Packages");
        smartBundlesTitle.setInnerHTML("SMART BUNDLES");

        smartBundlesHeader1.setInnerHTML("Mini");
        smartBundlesRowCost1.setInnerHTML("US$");
        smartBundlesRowNumber1.setInnerHTML("97");

        smartBundlesHeader2.setInnerHTML("Small");
        smartBundlesRowCost2.setInnerHTML("US$");
        smartBundlesRowNumber2.setInnerHTML("199");

        smartBundlesHeader3.setInnerHTML("Standard");
        smartBundlesRowCost3.setInnerHTML("US$");
        smartBundlesRowNumber3.setInnerHTML("379");

        smartBundlesHeader4.setInnerHTML("Silver");
        smartBundlesRowCost4.setInnerHTML("US$");
        smartBundlesRowNumber4.setInnerHTML("924");

        smartBundlesHeader5.setInnerHTML("Enterprise");
        smartBundlesRowCost5.setInnerHTML("US$");
        smartBundlesRowNumber5.setInnerHTML("1749");
    }

    private String getModules() {
        StringBuilder modules = new StringBuilder();
        if (projectsCheckBox.getValue()) {
            modules.append("'").append(PermissionConstants.PM_MODULE).append("'");
            modules.append(",");
        }
        if (accountingCheckBox.getValue()) {
            modules.append("'").append(PermissionConstants.ACCOUNTING_MODULE).append("'");
            modules.append(",");
        }
        if (crmCheckBox.getValue()) {
            modules.append("'").append(PermissionConstants.CRM_MODULE).append("'");
            modules.append(",");
        }
        if (hrmsCheckBox.getValue()) {
            modules.append("'").append(PermissionConstants.HRMS_MODULE).append("'");
            modules.append(",");
        }
        if (modules.length() > 2) {
            return modules.substring(0, modules.length() - 1);
        } else {
            return null;
        }
    }

    private void setModules(String modules) {
        if (modules != null) {
            String[] module = modules.split(",");
            countCheckedModulesOfFeatures = module.length;
            for (String m : module) {
                if (PermissionConstants.PM_MODULE.equals(m)) {
                    projectsCheckBox.setValue(true);
                    projectsCheckBox.setEnabled(false);
                }
                if (PermissionConstants.ACCOUNTING_MODULE.equals(m)) {
                    accountingCheckBox.setValue(true);
                    accountingCheckBox.setEnabled(false);
                }
                if (PermissionConstants.CRM_MODULE.equals(m)) {
                    crmCheckBox.setValue(true);
                    crmCheckBox.setEnabled(false);
                }
                if (PermissionConstants.HRMS_MODULE.equals(m)) {
                    hrmsCheckBox.setValue(true);
                    hrmsCheckBox.setEnabled(false);
                }
            }
        }
    }

    private void registerBuyNowButtonListener(final UsagePlanItem result_usagePlanItem, final Anchor button, final String pricingPackageN, final Integer userCount, final boolean isCustomPricing, final boolean isWorldPay, final int selectedUsagePeriodValue) {
        button.addClickHandler(event -> {
            //  0 -> 1 month  ||  1 -> 3 month  ||  2 -> 6 month  ||  3 -> 12 month/1 year
            final String periodS = selectedUsagePeriodValue == 0
                    ? "1 month"
                    : selectedUsagePeriodValue == 1
                    ? "3 months"
                    : selectedUsagePeriodValue == 2
                    ? "6 months"
                    : selectedUsagePeriodValue == 3 ? "12 months" : "";

            selectedPricingPackageNAME = pricingPackageN;
            //

            if (isGetQuote) {
                String cTotAmount = (Utils.getHTMLCODESForCurrency(isUKClient
                        ? "GBP"
                        : Utils.getCurrencyCODEbyHOST()) + getNumberFormatWithBigDecimal(result_usagePlanItem.getTotalAmount()));
                String cSupportPackageNAME = PayPalCalculationHelper.getSupportPackageNAME(currentSupportPackageID);
                String cSupportPackagePRICE = (Utils.getHTMLCODESForCurrency(isUKClient
                        ? "GBP"
                        : Utils.getCurrencyCODEbyHOST()) + getNumberFormatWithBigDecimal(PayPalCalculationHelper.getSupportPackagePRICE(currentSupportPackageNAME, supportPackagePrices)));
                String selectedSupportPackageNAME = PayPalCalculationHelper.getSupportPackageNAME(selectedSupportPackageID);
                String selectedSupportPackagePRICE = (Utils.getHTMLCODESForCurrency(isUKClient
                        ? "GBP"
                        : Utils.getCurrencyCODEbyHOST()) + getNumberFormatWithBigDecimal(PayPalCalculationHelper.getSupportPackagePRICE(selectedSupportPackageNAME, supportPackagePrices)));
                sendRequestQuoteMessage(result_usagePlanItem.getPeriod(), result_usagePlanItem.getUserCount(), result_usagePlanItem.getStatus(),
                        cTotAmount, cSupportPackageNAME, cSupportPackagePRICE,
                        periodS, userCount, selectedSupportPackageNAME, selectedSupportPackagePRICE);
            } else {
                boolean validate = true;
                if (validate) {
                    if (isUpgrade) {
                        if (isWorldPay) {
                            upgradePlanWithWorlPay(result_usagePlanItem, userCount, button, selectedUsagePeriodValue, selectedPricingPackageNAME, isCustomPricing);
                        } else {
                            upgradePlan(result_usagePlanItem, userCount, button, selectedUsagePeriodValue, selectedPricingPackageNAME, isCustomPricing);
                        }
                    } else {
                        if (isWorldPay) {
                            createSubscriptionWorldPay(userCount, button, selectedPricingPackageNAME, selectedUsagePeriodValue, isCustomPricing);
                        } else {
                            createSubscriptionPayPal(userCount, button, selectedPricingPackageNAME, selectedUsagePeriodValue, isCustomPricing);
                        }
                    }
                }
            }
        });
    }

    private void createSubscriptionPayPal(final Integer userCount, final Anchor button, final String currentCategoryT, final Integer clickedUsagePeriodID, boolean customPricing) {
        //collect upgrade data
        if (userCount == null) {
            return;
        }
        //get user count
        MyAccountService.App.get().getUserT(Utils.getHostName(), PayPalCalculationHelper.getPricingPackageNAME(currentCategoryT), selectedSupportPackageNAME, getModules(), customPricing, new AbstractAsyncCallback<UserRateItem>() {
            @Override
            public void success(UserRateItem result) {
                if (reCalculateUserRatePER_h2 != null) {
                    reCalculateUserRatePER_h2.getReCalculatedUserRate2(result);
                }

            }
        });
        reCalculateUserRatePER_h2 = userRate -> {
            final UsagePlanItem usagePlanItem = calculateClickedMonth(currentCategoryT, userRate, userCount, clickedUsagePeriodID);
            usagePlanItem.setStorageCount(1);
            usagePlanItem.setService(ALL_SERVICES);

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

                    if (usagePlan != null) {
                        String packageName = "Mini";
                        if (userCount == 5) {
                            packageName = "Small";
                        } else if (userCount == 10) {
                            packageName = "Standard";
                        } else if (userCount == 25) {
                            packageName = "Silver";
                        } else if (userCount == 50) {
                            packageName = "Enterprise";
                        }
                        String periodAsString = clickedUsagePeriodID == 0 ? "Monthly" : "Annual";
                        String description = "Activira ERP " + packageName + " Bundle - Up to " + userCount + " Users - " + periodAsString + " Contract";
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
    }

    private void sendRequestQuoteMessage(String cSPeriod, int cUsersCount, String cStatus,
                                         String cTotalAmount, String cSupportPackageNAME, String cSupportPackagePRICE, String periodS,
                                         int rUsersCount, String selectedSupportPackageNAME, String selectedSupportPackagePRICE) {
        RequestQuoteItem requestQuoteItem = new RequestQuoteItem();
        requestQuoteItem.setCurrentSubscriptionPeriod(cSPeriod);
        requestQuoteItem.setCurrentUsersCount(cUsersCount);
        requestQuoteItem.setCurrentStatus(cStatus);
        requestQuoteItem.setCurrentTotalAmount(cTotalAmount);
        requestQuoteItem.setCurrentSupportPackageNAME(cSupportPackageNAME);
        requestQuoteItem.setCurrentSupportPackagePrice(cSupportPackagePRICE);

        requestQuoteItem.setRequestedSubscriptionPeriod(periodS);
        requestQuoteItem.setRequestedUsersCount(rUsersCount);
        requestQuoteItem.setRequestedSupportPackageNAME(selectedSupportPackageNAME);
        requestQuoteItem.setRequestedSupportPackagePrice(selectedSupportPackagePRICE);
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


    private void upgradePlan(final UsagePlanItem usagePlanItem, final Integer userCount, final Anchor button, final Integer clickedUsagePeriodID, final String currentCategoryType, boolean customPricing) {
        button.setEnabled(false);
        if (userCount == null) {
            button.setEnabled(true);
            return;
        }
        MyAccountService.App.get().getUserT(Utils.getHostName(), PayPalCalculationHelper.getPricingPackageNAME(currentCategoryType), selectedSupportPackageNAME, getModules(), customPricing, new AbstractAsyncCallback<UserRateItem>() {
            @Override
            public void success(UserRateItem result) {
                if (reCalculateUserRatePER_h2 != null) {
                    reCalculateUserRatePER_h2.getReCalculatedUserRate2(result);
                }
            }
        });
        reCalculateUserRatePER_h2 = userRate -> {
            //collect upgrade data
            UsagePlanItem calculatedData = calculateClickedMonth(currentCategoryType, userRate, userCount, clickedUsagePeriodID);
            usagePlanItem.setDiscount(calculatedData.getDiscount());
            usagePlanItem.setTotalAmount(calculatedData.getTotalAmount());
            usagePlanItem.setUserCount(calculatedData.getUserCount());
            usagePlanItem.setTax(calculatedData.getTax());
            usagePlanItem.setUsageMonth(calculatedData.getUsageMonth());
            usagePlanItem.setCategoryREAL(calculatedData.getCategoryREAL());
            usagePlanItem.setSupportPackageNAME(calculatedData.getSupportPackageNAME());

            final String currencyValue = isUKClient ? "GBP" : Utils.getCurrencyCODEbyHOST();
            // save plan history
            LoadingPanel.loading(true);
            MyAccountService.App.get().createSubscriptionHistory(usagePlanItem, new AbstractAsyncCallback<Integer>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    button.setEnabled(true);
                }

                public void success(Integer usagePlanID) {
                    LoadingPanel.loading(false);
                    // send to PayPal

                    String packageName = "Mini";
                    if (userCount == 5) {
                        packageName = "Small";
                    } else if (userCount == 10) {
                        packageName = "Standard";
                    } else if (userCount == 25) {
                        packageName = "Silver";
                    } else if (userCount == 50) {
                        packageName = "Enterprise";
                    }
                    String periodAsString = clickedUsagePeriodID == 0 ? "Monthly" : "Annual";
                    String description = "Activira ERP " + packageName + " Bundle - Up to " + userCount + " Users - " + periodAsString + " Contract";

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

    private void createSubscriptionWorldPay(final Integer userCount, final Anchor button, final String currentCategoryT, final Integer clickedUsagePeriodID, boolean customPricing) {
        //collect upgrade data
        if (userCount == null) {
            return;
        }
        //get user count
        MyAccountService.App.get().getUserT(Utils.getHostName(), PayPalCalculationHelper.getPricingPackageNAME(currentCategoryT), selectedSupportPackageNAME, getModules(), customPricing, new AbstractAsyncCallback<UserRateItem>() {
            @Override
            public void success(UserRateItem result) {
                if (reCalculateUserRatePER_h2 != null) {
                    reCalculateUserRatePER_h2.getReCalculatedUserRate2(result);
                }

            }
        });
        reCalculateUserRatePER_h2 = userRate -> {
            final UsagePlanItem usagePlanItem = calculateClickedMonth(currentCategoryT, userRate, userCount, clickedUsagePeriodID);
            usagePlanItem.setStorageCount(1);
            usagePlanItem.setService(ALL_SERVICES);

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
                    if (usagePlan != null) {
                        FormPanel form = new FormPanel("_blank");
                        form.setAction(Utils.getWorldPayLink());
                        form.setMethod(FormPanel.METHOD_POST);
                        form.setEncoding(FormPanel.ENCODING_URLENCODED);
                        form.setVisible(false);

                        VerticalPanel verticalPanel = new VerticalPanel();
                        verticalPanel.add(new Hidden("instId", Utils.getWorldPayAccount()));
                        verticalPanel.add(new Hidden("desc", Utils.getProductName() + " - ERP."));
                        verticalPanel.add(new Hidden("cartId", String.valueOf(usagePlanItem.getObjectID())));
                        verticalPanel.add(new Hidden("currency", currencyValue));
                        verticalPanel.add(new Hidden("testMode", Utils.getWorldPayTestModeValue()));
                        verticalPanel.add(new Hidden("hideCurrency", "hideCurrency"));
                        verticalPanel.add(new Hidden("option", "0"));
                        verticalPanel.add(new Hidden("futurePayType", "regular"));
                        verticalPanel.add(new Hidden("noOfPayments", "0"));
                        verticalPanel.add(new Hidden("amountLimit", "0"));
                        verticalPanel.add(new Hidden("lengthMult", "1"));
                        verticalPanel.add(new Hidden("lengthUnit", "3"));
                        verticalPanel.add(new Hidden("intervalUnit", "3"));//1 -day, 2-week, 3-month, 4-year
                        verticalPanel.add(new Hidden("intervalMult", String.valueOf(usagePlanItem.getUsageMonth())));
                        verticalPanel.add(new Hidden("startDelayUnit", "1"));
                        verticalPanel.add(new Hidden("startDelayMult", "1"));
                        verticalPanel.add(new Hidden("normalAmount", getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount())));
                        verticalPanel.add(new Hidden("MC_usageplaceId", String.valueOf(usagePlan.getObjectID())));
                        verticalPanel.add(new Hidden("MC_custom", Utils.getEncryptedCompanyID() + SUBSCRIPTION_ADD + usagePlan.getObjectID()));
                        verticalPanel.add(new Hidden("MC_host", GWT.getHostPageBaseURL() + "Myaccount.html"));

                        form.add(verticalPanel);
                        testPanel.add(form);
                        form.submit();
                    }
                }
            });
        };
    }

    private void upgradePlanWithWorlPay(final UsagePlanItem usagePlanItem, final Integer userCount, final Anchor button, final Integer clickedUsagePeriodID, final String currentCategoryType, boolean customPricing) {
        button.setEnabled(false);
        if (userCount == null) {
            button.setEnabled(true);
            return;
        }
        MyAccountService.App.get().getUserT(Utils.getHostName(), PayPalCalculationHelper.getPricingPackageNAME(currentCategoryType), selectedSupportPackageNAME, getModules(), customPricing, new AbstractAsyncCallback<UserRateItem>() {
            @Override
            public void success(UserRateItem result) {
                if (reCalculateUserRatePER_h2 != null) {
                    reCalculateUserRatePER_h2.getReCalculatedUserRate2(result);
                }
            }
        });
        reCalculateUserRatePER_h2 = userRate -> {
            //collect upgrade data
            UsagePlanItem calculatedData = calculateClickedMonth(currentCategoryType, userRate, userCount, clickedUsagePeriodID);
            usagePlanItem.setDiscount(calculatedData.getDiscount());
            usagePlanItem.setTotalAmount(calculatedData.getTotalAmount());
            usagePlanItem.setUserCount(calculatedData.getUserCount());
            usagePlanItem.setTax(calculatedData.getTax());
            usagePlanItem.setUsageMonth(calculatedData.getUsageMonth());
            usagePlanItem.setCategoryREAL(calculatedData.getCategoryREAL());
            usagePlanItem.setSupportPackageNAME(calculatedData.getSupportPackageNAME());

            final String currencyValue = isUKClient ? "GBP" : Utils.getCurrencyCODEbyHOST();
            LoadingPanel.loading(true);
            MyAccountService.App.get().createSubscriptionHistory(usagePlanItem, new AbstractAsyncCallback<Integer>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    button.setEnabled(true);
                }

                public void success(Integer usagePlanID) {
                    LoadingPanel.loading(false);
                    FormPanel form = new FormPanel("_blank");
                    form.setAction(Utils.getWorldPayLink());
                    form.setMethod(FormPanel.METHOD_POST);
                    form.setEncoding(FormPanel.ENCODING_URLENCODED);
                    form.setVisible(false);

                    VerticalPanel verticalPanel = new VerticalPanel();
                    verticalPanel.add(new Hidden("instId", Utils.getWorldPayAccount()));
                    verticalPanel.add(new Hidden("desc", Utils.getProductName() + " - ERP."));
                    verticalPanel.add(new Hidden("cartId", String.valueOf(usagePlanItem.getObjectID())));
                    verticalPanel.add(new Hidden("currency", currencyValue));
                    verticalPanel.add(new Hidden("testMode", Utils.getWorldPayTestModeValue()));
                    verticalPanel.add(new Hidden("hideCurrency", "hideCurrency"));
                    verticalPanel.add(new Hidden("option", "0"));
                    verticalPanel.add(new Hidden("futurePayType", "regular"));
                    verticalPanel.add(new Hidden("noOfPayments", "0"));
                    verticalPanel.add(new Hidden("amountLimit", "0"));
                    verticalPanel.add(new Hidden("lengthMult", "1"));
                    verticalPanel.add(new Hidden("lengthUnit", "3"));
                    verticalPanel.add(new Hidden("intervalUnit", "3"));//1 -day, 2-week, 3-month, 4-year
                    verticalPanel.add(new Hidden("intervalMult", String.valueOf(usagePlanItem.getUsageMonth())));
                    verticalPanel.add(new Hidden("startDelayUnit", "1"));
                    verticalPanel.add(new Hidden("startDelayMult", "1"));
                    verticalPanel.add(new Hidden("normalAmount", getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount())));
                    verticalPanel.add(new Hidden("amountString", getNumberFormatWithBigDecimal(usagePlanItem.getTotalAmount())));
                    verticalPanel.add(new Hidden("MC_usageplaceId", String.valueOf(usagePlanID)));
                    verticalPanel.add(new Hidden("MC_custom", Utils.getEncryptedCompanyID() + SUBSCRIPTION_UPG + usagePlanID));
                    verticalPanel.add(new Hidden("MC_host", GWT.getHostPageBaseURL() + "Myaccount.html"));

                    form.add(verticalPanel);
                    testPanel.add(form);
                    form.submit();
                }
            });
        };
    }

    private void generateHelpContainer(UsagePlanItem result_u, FlowPanel flowPanel) {
        flowPanel.setVisible(true);
        final WestPanelHelp westPanel = new WestPanelHelp("<b>" + wfmStrings.yourCurrentPlan() + ":" + "</b>");
        final StringBuilder sb = new StringBuilder();
        sb.append("<div><b>").append(wfmStrings.users()).append(": </b>&nbsp;&nbsp;").append(" ").append(result_u.getUserCount()).append("</br>");
        sb.append("<b>").append(wfmStrings.period()).append(": </b>&nbsp;").append(result_u.getPeriodType()).append("</br></br>");
        String categoryREAL = result_u.getCategoryREAL();
        if (categoryREAL != null && !"".equals(categoryREAL)) {
            categoryREAL = PP_MINI.equals(categoryREAL)
                    ? "Mini"
                    : PP_SMALL.equals(categoryREAL)
                    ? "Small"
                    : PP_STANDART.equals(categoryREAL)
                    ? "Standard"
                    : PP_SILVER.equals(categoryREAL)
                    ? "Silver"
                    : PP_ENTERPRISE2.equals(categoryREAL) ? "Enterprice" : "";
            sb.append("<b>").append("Current Package").append(": </b>&nbsp;").append(categoryREAL).append("</br></br>");
        }
        String supportPackage = result_u.getSupportPackageNAME();
        if (supportPackage != null && !"".equals(supportPackage)) {
            supportPackage = SP_CONTRACTOR.equals(supportPackage)
                    ? "Contractor"
                    : SP_SMALL_BUSINESS.equals(supportPackage)
                    ? "Small Business"
                    : SP_PROFESSIONAL.equals(supportPackage)
                    ? "Professional"
                    : SP_ENTERPRISE.equals(supportPackage) ? "Enterprice" : "";
            sb.append("<b>").append("Consulting Service").append(": </b>&nbsp;").append(supportPackage).append("</br></br>");
        }

        sb.append("</div>");
        final HTML html = new HTML(sb.toString());
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