package com.edatasite.workforce.gwt.myaccount.client.ui.view;

import com.edatasite.workforce.gwt.core.client.NumberUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AnchorParam;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCustomToolTip;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.myaccount.client.PricingUtils;
import com.edatasite.workforce.gwt.myaccount.client.bundles.MyaccountImageBundles;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountMessages;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountStrings;
import com.edatasite.workforce.gwt.myaccount.client.rpc.AddOnsItem;
import com.edatasite.workforce.gwt.myaccount.client.rpc.MyAccountService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanPrice;
import com.edatasite.workforce.gwt.myaccount.client.ui.stripe.StripePaymentHandler;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by Anvar Akramov on 16.08.18.
 */
public class AllPricingMaterialView extends View implements Constants {

    private static final MyAccountStrings myAccountStrings = MyAccountStrings.App.get();
    private static final MyAccountMessages myAccountMessages = MyAccountMessages.App.get();

    private UsagePlanItem usagePlan;
    private boolean isUpgrade = false;
    private String currentCategory;

    private Integer currentUsagePeriod = 0;
    private int currentUsageMonth = 0;
    private int customModuleCount = 0;
    private final boolean isMonthly = false;

    @UiField
    InputElement customAccounts;
    @UiField
    InputElement customSales;
    @UiField
    InputElement customHumans;
    @UiField
    InputElement customProjects;
    @UiField
    InputElement customPayroll;
    @UiField
    DivElement reportsLabel;
    @UiField
    DivElement reportsFreeLabel;
    @UiField
    DivElement documentsLabel;
    @UiField
    DivElement documentsFreeLabel;
    @UiField
    Div buyNowButtonContainer;
    @UiField
    Span buyNowButton;
    @UiField
    DivElement fullUsersPerLabel;
    @UiField
    DivElement essUsersPerLabel;
    @UiField
    DivElement noAccessUsersPerLabel;
    @UiField
    InputElement fullUsersCount;
    @UiField
    InputElement essUsersCount;
    @UiField
    InputElement noAccessUsersCount;
    @UiField
    TableCellElement totalToBePaid;
    @UiField
    Span fullUsersIncrease;
    @UiField
    Span fullUsersDecrease;
    @UiField
    Span essUsersIncrease;
    @UiField
    Span essUsersDecrease;
    @UiField
    Span noAccessUsersIncrease;
    @UiField
    Span noAccessUsersDecrease;
    @UiField
    TableCellElement totalDiscount;
    @UiField
    TableCellElement essUsersPrice;
    @UiField
    TableCellElement noAccessUsersPrice;
    @UiField
    TableCellElement fullUsersPrice;
    @UiField
    TableCellElement subscriptionTotal;
    @UiField
    TableCellElement totalAddOnsPrice;
    @UiField
    DataListBox addonOnlineTraining;
    @UiField
    DataListBox addonInitialSetup;
    @UiField
    DataListBox addonExtraStorage;
    @UiField
    DataListBox addonCustomPDFTemplate;
    @UiField
    DataListBox addonDedicatedDeveloper;
    @UiField
    DataListBox addonDedicatedAccountManager;
    @UiField
    TableCellElement usersPerAppLabel;
    @UiField
    TableCellElement essUsersPerAppLabel;
    @UiField
    DivElement pricePerFullUser;
    @UiField
    DivElement pricePerEssUser;
    @UiField
    DivElement pricePerNoAccessUser;
    @UiField
    TableCellElement totalLabel;
    @UiField
    Label premiumSupportLabel;
    @UiField
    Label initialSetupLabel;
    @UiField
    HeadingElement addonsTitle;
    @UiField
    ParagraphElement addonsDescription;
    @UiField
    Div noAccessUsersPerAppLabel;
    @UiField
    Div discountLabel;
    @UiField
    Div fullUsersLabel;
    @UiField
    Div essUsersLabel;
    @UiField
    DivElement accountsWrapper;
    @UiField
    DivElement salesWrapper;
    @UiField
    DivElement humansWrapper;
    @UiField
    DivElement projectsWrapper;
    @UiField
    DivElement payrolWrapper;
    @UiField
    Div nonUserLabel;
    @UiField
    DivElement accountsLabel;
    @UiField
    HeadingElement chooseYourAppsLabel;
    @UiField
    DivElement salesLabel;
    @UiField
    DivElement hrmsLabel;
    @UiField
    DivElement projectManagementLabel;
    @UiField
    DivElement payrollLabel;
    @UiField
    HeadingElement selectUserTitle;
    @UiField
    ParagraphElement specifyNumberOfUsers;
    @UiField
    ParagraphElement ourDedicatedStandaloneLabel;
    @UiField
    TableCellElement subtotalSubscription;
    @UiField
    TableCellElement subtotalAddOn;
    @UiField
    LabelElement onlineTrainingLabel;
    @UiField
    LabelElement customPDFTemplate;
    @UiField
    LabelElement extraStorage;
    @UiField
    LabelElement dedicatedDeveloper;
    @UiField
    DivElement nonUserContainer;
    @UiField
    DivElement essUserContainer;
    @UiField
    TableRowElement noAccessUsersPerAppContainer;
    @UiField
    TableRowElement essUsersPerAppContainer;
    @UiField
    RadioButton byYear;
    @UiField
    RadioButton byHalfYear;
    @UiField
    RadioButton byQuarter;
    @UiField
    RadioButton byMonth;


    public static String type;
    public static UsagePlanPrice dataForSend;
    public static AddOnsItem addOnsItem;
    public static UsagePlanItem usagePlanItem;
    public static UsagePlanItem prevUsagePlanItem;

    private final SelectItem[] ONLINE_TRAININGS = new SelectItem[]{
            new SelectItem(1, "3 hrs / $399.00 — " + myAccountStrings.oneTimePayment(), "3hrs", 399d),
            new SelectItem(2, "6 hrs / $699.00 — " + myAccountStrings.oneTimePayment(), "6hrs", 699d),
            new SelectItem(3, "9 hrs / $999.00 — " + myAccountStrings.oneTimePayment(), "9hrs", 999d)
    };
    private final SelectItem[] INITIAL_SETUPS = new SelectItem[]{
            new SelectItem(1, "$999 — " + myAccountStrings.oneTimePayment(), "One time Payment", 999d)
    };
    private final SelectItem[] EXTRA_STORAGES = new SelectItem[]{
            new SelectItem(1, "5 GB / " + wfmStrings.free(), "5GB", 0d),
            new SelectItem(2, "10 GB / $48 — " + myAccountStrings.oneTimePayment(), "10GB", 48d),
            new SelectItem(3, "15 GB / $77 — " + myAccountStrings.oneTimePayment(), "15GB", 77d),
            new SelectItem(4, "20 GB / $86 — " + myAccountStrings.oneTimePayment(), "20GB", 86d),
            new SelectItem(5, "25 GB / $120 —" + myAccountStrings.oneTimePayment(), "25GB", 120d)
    };
    private final SelectItem[] CUSTOM_PDFTEMPLATES = new SelectItem[]{
            new SelectItem(1, "1 PDF / $299 — " + myAccountStrings.oneTimePayment(), "1 PDF", 299d),
            new SelectItem(2, "2 PDF / $499 — " + myAccountStrings.oneTimePayment(), "2 PDFs", 499d),
            new SelectItem(3, "3 PDF / $799 — " + myAccountStrings.oneTimePayment(), "3 PDFs", 799d)
    };
    private final SelectItem[] DEDICATED_DEVELOPERS = new SelectItem[]{
            new SelectItem(1, "5 hrs / $499 — " + myAccountStrings.oneTimePayment(), "5 hrs", 499d),
            new SelectItem(2, "10 hrs / $899 — " + myAccountStrings.oneTimePayment(), "10 hrs", 899d),
            new SelectItem(3, "15 hrs / $1399 — " + myAccountStrings.oneTimePayment(), "15 hrs", 1399d)
    };
    private final SelectItem[] DEDICATED_ACCOUNT_MANAGERS = new SelectItem[]{
            new SelectItem(1, "$999 — " + myAccountStrings.oneTimePayment(), "One time Payment", 999d)
    };

    private String nonUserMsg;

    public AllPricingMaterialView() {
        super("allPricingView", wfmStrings.currentSubscription());
    }

    private void draw() {


        nonUserMsg = myAccountMessages.nonUserTooltip(Utils.getHostNameURL(), Utils.getProductName());
        noAccessUsersCount.setId("pricingPage_nonUser");

        selectUserTitle.setInnerHTML(myAccountStrings.selectUser());
        specifyNumberOfUsers.setInnerHTML(myAccountStrings.specifyNumberOfUsers());
        fullUsersLabel.getElement().setInnerHTML(myAccountStrings.fullUsers() + " ");
        fullUsersLabel.add(new KpiCustomToolTip(myAccountStrings.fullUsersTooltip(), false, true));
        essUsersLabel.getElement().setInnerHTML(myAccountStrings.essUsers() + " ");
        essUsersLabel.add(new KpiCustomToolTip(myAccountStrings.essUsersTooltip(), false, true));
        nonUserLabel.getElement().setInnerHTML(myAccountStrings.nonUsers() + " ");
        nonUserLabel.add(new KpiCustomToolTip(myAccountMessages.nonUserTooltip(Utils.getHostNameURL(), Utils.getProductName()), false, true));

        chooseYourAppsLabel.setInnerHTML(myAccountStrings.chooseYourApps());
        ourDedicatedStandaloneLabel.setInnerHTML(myAccountStrings.ourDedicatedStandalone());
        accountsLabel.setInnerHTML(wfmStrings.accounts());
        salesLabel.setInnerHTML(wfmStrings.crm());
        hrmsLabel.setInnerHTML(wfmStrings.hrms());
        projectManagementLabel.setInnerHTML(wfmStrings.projects());
        payrollLabel.setInnerHTML(wfmStrings.payroll());
        documentsLabel.setInnerHTML(wfmStrings.documents());
        documentsFreeLabel.setInnerHTML(wfmStrings.free());
        reportsLabel.setInnerHTML(wfmStrings.reports());
        reportsFreeLabel.setInnerHTML(wfmStrings.free());

        subtotalSubscription.setInnerHTML(wfmStrings.subtotalSubscription());
        totalLabel.setInnerHTML(myAccountMessages.totalcurrency("USD"));
        subtotalAddOn.setInnerHTML(myAccountStrings.subtotalAddOn());
        addonsTitle.setInnerHTML(myAccountStrings.addonsTitle());
        addonsDescription.setInnerHTML(myAccountStrings.addonsDescription());
        onlineTrainingLabel.setInnerHTML(myAccountStrings.onlineTraining());
        customPDFTemplate.setInnerHTML(myAccountStrings.customPDFTemplate());
        extraStorage.setInnerHTML(myAccountStrings.extraStorage());
        dedicatedDeveloper.setInnerHTML(wfmStrings.dedicatedDeveloper());

        byYear.setText(wfmStrings.yearly());
        byHalfYear.setText(wfmStrings.halfYearly());
        byQuarter.setText(wfmStrings.quarterly());
        byMonth.setText(wfmStrings.monthly());

        discountLabel.getElement().setInnerHTML(myAccountStrings.youSave());
        discountLabel.add(new KpiCustomToolTip(myAccountStrings.discountTooltip(), true));

        noAccessUsersPerAppLabel.getElement().setInnerHTML(myAccountStrings.nonUser() + " ");
        noAccessUsersPerAppLabel.add(new KpiCustomToolTip(nonUserMsg, true));

        initialSetupLabel.setText(myAccountStrings.initialSetUpPackage() + " ");
        initialSetupLabel.add(new KpiCustomToolTip(myAccountStrings.initialSetUpPackageTooltip()));
        premiumSupportLabel.setText(wfmStrings.premiumSupport() + " ");
        premiumSupportLabel.add(new KpiCustomToolTip(myAccountStrings.premiumSupportTooltip()));

        addonOnlineTraining.setItems(ONLINE_TRAININGS);
        addonInitialSetup.setItems(INITIAL_SETUPS);
        addonExtraStorage.setItems(EXTRA_STORAGES);
        addonCustomPDFTemplate.setItems(CUSTOM_PDFTEMPLATES);
        addonDedicatedDeveloper.setItems(DEDICATED_DEVELOPERS);
        addonDedicatedAccountManager.setItems(DEDICATED_ACCOUNT_MANAGERS);

        ValueChangeHandler addOnValueChangeHandler = (ValueChangeHandler<SelectItem>) valueChangeEvent -> setCustomPriceLabel();
        addonOnlineTraining.addValueChangeHandler(addOnValueChangeHandler);
        addonInitialSetup.addValueChangeHandler(addOnValueChangeHandler);
        addonExtraStorage.addValueChangeHandler(addOnValueChangeHandler);
        addonCustomPDFTemplate.addValueChangeHandler(addOnValueChangeHandler);
        addonDedicatedDeveloper.addValueChangeHandler(addOnValueChangeHandler);
        addonDedicatedAccountManager.addValueChangeHandler(addOnValueChangeHandler);

        pricePerFullUser.setInnerHTML("$" + PricingUtils.perUserPrice);
        pricePerEssUser.setInnerHTML("$" + PricingUtils.perEssUserPrice);
        pricePerNoAccessUser.setInnerHTML("$" + PricingUtils.perNoAccessUserPrice);


        pricePeriodLabel(isMonthly);

        buyNowButton.setText(wfmStrings.continueOnly());

//        byYear.addValueChangeHandler(valueChangeEvent -> {
//            type = PP_BY_YEAR;
//            setCustomPriceLabel();
//        });
//
//        byHalfYear.addValueChangeHandler(valueChangeEvent -> {
//            type = PP_BY_HALF_YEAR;
//            setCustomPriceLabel();
//        });
//
//        byQuarter.addValueChangeHandler(valueChangeEvent -> {
//            type = PP_BY_QUARTER;
//            setCustomPriceLabel();
//        });
//
//        byMonth.addValueChangeHandler(valueChangeEvent -> {
//            type = PP_BY_MONTH;
//            setCustomPriceLabel();
//        });

        setupValueChangeHandler(byYear, PP_BY_YEAR);
        setupValueChangeHandler(byHalfYear, PP_BY_HALF_YEAR);
        setupValueChangeHandler(byQuarter, PP_BY_QUARTER);
        setupValueChangeHandler(byMonth, PP_BY_MONTH);


        buyNowButtonContainer.addClickHandler(clickEvent -> {

            final int userCount = getFullUsersCount();
            final int essUserCount = getESSUsersCount();
            final int nonAccessUserCount = getNonAccessUsersCount();
            double totalAddonsPrice = getTotalAddonsPrice();

            UsagePlanPrice prices = PricingUtils.getTotalPrice(userCount, essUserCount, customModuleCount, nonAccessUserCount, totalAddonsPrice, usagePlan, isMonthly, type);

            if(!isUpgrade && (userCount <= 0 || customModuleCount <= 0) ) {
                //T4430
                Info.show(myAccountStrings.minimumRequired(), Info.Type.WARNING);
            } else if (prices.getTotalAmount() <= 0d && prices.getAddonPrice() <= 0d) {
                Info.show(myAccountStrings.minimumRequired(), Info.Type.WARNING);
            } else {
                if (isUpgrade) {
                    prevUsagePlanItem = null;
                    upgradePlan();
                } else {
                    if (userCount > 0 && customModuleCount > 0) {
                        createSubscription();
                    } else {
                        Info.show(myAccountStrings.minimumRequired(), Info.Type.WARNING);
                    }
                }
            }
        });

        this.loadCurrentOrLastUsagePlan();
        this.registerEventHanlders();

        //Inject Strip.js
        if (Utils.getStripePublicKey() != null && !Utils.getStripePublicKey().equals("") && !Utils.getStripePublicKey().equalsIgnoreCase("null")) {
            StripePaymentHandler.initializeStripe(new Callback<Void, Exception>() {
                @Override
                public void onFailure(Exception reason) {
                    GWT.log("Could not inject Stripe.js");
                }

                @Override
                public void onSuccess(Void result) {
                    GWT.log("Stripe is Injected");
                }
            });
        } else {
            GWT.log("STRIPE KEYS ARE NOT CONFIGURED.");
        }
    }

    private void setupValueChangeHandler(RadioButton radioButton, String targetType) {
        radioButton.addValueChangeHandler(valueChangeEvent -> {
            type = targetType;
            setCustomPriceLabel();
        });
    }


    private void upgradePlan() {

        final int userCount = this.getFullUsersCount();
        final int essUserCount = this.getESSUsersCount();
        final int noAccessUserCount = this.getNonAccessUsersCount();
        final int moduleCount = customModuleCount;
        double totalAddonsPrice = getTotalAddonsPrice();

        UsagePlanPrice prices = PricingUtils.getTotalPrice(userCount, essUserCount, moduleCount, noAccessUserCount, totalAddonsPrice, usagePlan, isMonthly, type);

        if (this.validateBuyAndUpgradeUsersCount(userCount)) {
            Info.show(wfmStrings.pleaseChooseHigherUpgrade(), Info.Type.WARNING);
            return;
        }
        UsagePlanItem upgradeUsagePlan = new UsagePlanItem();
        upgradeUsagePlan.setObjectID(usagePlan.getObjectID());
        upgradeUsagePlan.setCompanyID(usagePlan.getCompanyID());
        upgradeUsagePlan.setUnique_guid(usagePlan.getUnique_guid());
        upgradeUsagePlan.setTotalAmount((float) prices.getTotalAmount());
        upgradeUsagePlan.setSubTotalAmount((float) prices.getTotalSubscription());
        if (PP_BY_QUARTER.equals(type)) {
            upgradeUsagePlan.setPlanType(THREE_MONTH_15);
        } else if (PP_BY_HALF_YEAR.equals(type)) {
            upgradeUsagePlan.setPlanType(SIX_MONTH_20);
        } else if (PP_BY_YEAR.equals(type)) {
            upgradeUsagePlan.setPlanType(TWO_YEARS_45);
        } else {
            upgradeUsagePlan.setPlanType(ONE_MONTH_0);
        }
        upgradeUsagePlan.setCategoryREAL(PP_CUSTOM);
        upgradeUsagePlan.setUserCount(userCount);
        upgradeUsagePlan.setEssUserCount(essUserCount);
        upgradeUsagePlan.setNonAccessUserCount(noAccessUserCount);

        upgradeUsagePlan.setProjectModule(customProjects.isChecked());
        upgradeUsagePlan.setAccountsModule(customAccounts.isChecked());
        upgradeUsagePlan.setSalesModule(customSales.isChecked());
        upgradeUsagePlan.setHumansModule(customHumans.isChecked());
        upgradeUsagePlan.setPayrollModule(customPayroll.isChecked());

        upgradeUsagePlan.setStorageCount(usagePlan.getStorageCount());
        upgradeUsagePlan.setService(usagePlan.getService());
        //We must set this here
        upgradeUsagePlan.setUsageMonth(isMonthly ? 1 : 12);
        upgradeUsagePlan.setPeriodConstant(type);

        upgradeUsagePlan.setModules(String.valueOf(moduls));


        //Set Addon prices
        if (addonOnlineTraining.getSelectedIndex() > 0) {
            upgradeUsagePlan.setAddonOnlineTraining(addonOnlineTraining.getSelectedItem(false).getTotalAmount());
        }
        if (addonInitialSetup.getSelectedIndex() > 0) {
            upgradeUsagePlan.setAddonInitialSetup(addonInitialSetup.getSelectedItem(false).getTotalAmount());
        }
        if (addonExtraStorage.getSelectedIndex() > 0) {
            upgradeUsagePlan.setAddonExtraStorage(addonExtraStorage.getSelectedItem(false).getTotalAmount());
        }
        if (addonCustomPDFTemplate.getSelectedIndex() > 0) {
            upgradeUsagePlan.setAddonCustomPDFTemplate(addonCustomPDFTemplate.getSelectedItem(false).getTotalAmount());
        }
        if (addonDedicatedDeveloper.getSelectedIndex() > 0) {
            upgradeUsagePlan.setAddonDedicatedDeveloper(addonDedicatedDeveloper.getSelectedItem(false).getTotalAmount());
        }
        if (addonDedicatedAccountManager.getSelectedIndex() > 0) {
            upgradeUsagePlan.setAddonDedicatedAccountManager(addonDedicatedAccountManager.getSelectedItem(false).getTotalAmount());
        }

        addOnsItem = getAddOnsItem();

        LoadingPanel.loading(true);
        MyAccountService.App.get().createSubscriptionHistory(upgradeUsagePlan, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(Integer subscriptionID) {
                LoadingPanel.loading(false);
                if (subscriptionID != null) {
                    dataForSend = prices;
                    usagePlanItem = upgradeUsagePlan;
                    prevUsagePlanItem = usagePlan;
                    SinksContainerFactory.entryPoint.onHistoryChanged("pricingOrder|summary/".concat(PRICING_ORDER.SUBSCRIPTION_UPGRADE).concat("/") + subscriptionID);
                }
            }
        });
    }


    private void createSubscription() {
        final int userCount = getFullUsersCount();
        final int essUserCount = getESSUsersCount();
        final int nonAccessUserCount = getNonAccessUsersCount();
        double totalAddonsPrice = getTotalAddonsPrice();

        UsagePlanPrice prices = PricingUtils.getTotalPrice(userCount, essUserCount, customModuleCount, nonAccessUserCount, totalAddonsPrice, usagePlan, isMonthly, type);

        final UsagePlanItem newUsagePlan = new UsagePlanItem();

        newUsagePlan.setStorageCount(1);
        newUsagePlan.setService(ALL_SERVICES);
        newUsagePlan.setUsageMonth(isMonthly ? 1 : 12);
        newUsagePlan.setTotalAmount((float) prices.getTotalAmount());
        if (PP_BY_QUARTER.equals(type)) {
            newUsagePlan.setPlanType(THREE_MONTH_15);
        } else if (PP_BY_HALF_YEAR.equals(type)) {
            newUsagePlan.setPlanType(SIX_MONTH_20);
        } else if (PP_BY_YEAR.equals(type)) {
            newUsagePlan.setPlanType(TWO_YEARS_45);
        } else {
            newUsagePlan.setPlanType(ONE_MONTH_0);
        }
        newUsagePlan.setProjectModule(customProjects.isChecked());
        newUsagePlan.setAccountsModule(customAccounts.isChecked());
        newUsagePlan.setSalesModule(customSales.isChecked());
        newUsagePlan.setHumansModule(customHumans.isChecked());
        newUsagePlan.setPayrollModule(customPayroll.isChecked());
        newUsagePlan.setPeriodType(type);
        //Set Addon prices
        if (addonOnlineTraining.getSelectedIndex() > 0) {
            newUsagePlan.setAddonOnlineTraining(addonOnlineTraining.getSelectedItem(false).getTotalAmount());
        }
        if (addonInitialSetup.getSelectedIndex() > 0) {
            newUsagePlan.setAddonInitialSetup(addonInitialSetup.getSelectedItem(false).getTotalAmount());
        }
        if (addonExtraStorage.getSelectedIndex() > 0) {
            newUsagePlan.setAddonExtraStorage(addonExtraStorage.getSelectedItem(false).getTotalAmount());
        }
        if (addonCustomPDFTemplate.getSelectedIndex() > 0) {
            newUsagePlan.setAddonCustomPDFTemplate(addonCustomPDFTemplate.getSelectedItem(false).getTotalAmount());
        }
        if (addonDedicatedDeveloper.getSelectedIndex() > 0) {
            newUsagePlan.setAddonDedicatedDeveloper(addonDedicatedDeveloper.getSelectedItem(false).getTotalAmount());
        }
        if (addonDedicatedAccountManager.getSelectedIndex() > 0) {
            newUsagePlan.setAddonDedicatedAccountManager(addonDedicatedAccountManager.getSelectedItem(false).getTotalAmount());
        }

        newUsagePlan.setCategoryREAL(PP_CUSTOM);
        newUsagePlan.setUserCount(userCount);
        newUsagePlan.setEssUserCount(essUserCount);
        newUsagePlan.setNonAccessUserCount(nonAccessUserCount);

        LoadingPanel.loading(true);
        MyAccountService.App.get().saveUsagePlan(newUsagePlan, new AbstractAsyncCallback<UsagePlanItem>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.error(), Info.Type.WARNING);
            }

            @Override
            public void success(UsagePlanItem usagePlanItem) {
                LoadingPanel.loading(false);
                if (usagePlanItem != null && usagePlanItem.getObjectID() != null) {
                    dataForSend = prices;
                    addOnsItem = getAddOnsItem();
                    SinksContainerFactory.entryPoint.onHistoryChanged("pricingOrder|summary/".concat(PRICING_ORDER.SUBSCRIPTION_ADD).concat("/") + usagePlanItem.getObjectID());
                }
            }
        });
    }

    private AddOnsItem getAddOnsItem() {

        AddOnsItem addOnsItem = new AddOnsItem();
        addOnsItem.setOnlineTraining(addonOnlineTraining.getSelectedItem());
        addOnsItem.setInitialSetup(addonInitialSetup.getSelectedItem());
        addOnsItem.setExtraStorage(addonExtraStorage.getSelectedItem());
        addOnsItem.setCustomPdfTemplate(addonCustomPDFTemplate.getSelectedItem());
        addOnsItem.setDedicatedAccountManager(addonDedicatedAccountManager.getSelectedItem());
        addOnsItem.setDedicatedDeveloper(addonDedicatedDeveloper.getSelectedItem());

        return addOnsItem;
    }

    private void pricePeriodLabel(boolean isMonthly) {
        fullUsersPerLabel.setInnerHTML(wfmStrings.user().toLowerCase() + " / " + wfmStrings.month().toLowerCase());
        essUsersPerLabel.setInnerHTML(wfmStrings.user().toLowerCase() + " / " + wfmStrings.month().toLowerCase());
        noAccessUsersPerLabel.setInnerHTML(wfmStrings.user().toLowerCase() + " / " + wfmStrings.month().toLowerCase());
    }

    private double getTotalAddonsPrice() {
        //Calculate AddOns
        double totalAddOns = 0d;
        if (addonOnlineTraining.getSelectedItem(false) != null) {
            totalAddOns += addonOnlineTraining.getSelectedItem(false).getTotalAmount();
        }
        if (addonInitialSetup.getSelectedItem(false) != null) {
            totalAddOns += addonInitialSetup.getSelectedItem(false).getTotalAmount();
        }
        if (addonExtraStorage.getSelectedItem(false) != null) {
            totalAddOns += addonExtraStorage.getSelectedItem(false).getTotalAmount();
        }
        if (addonCustomPDFTemplate.getSelectedItem(false) != null) {
            totalAddOns += addonCustomPDFTemplate.getSelectedItem(false).getTotalAmount();
        }
        if (addonDedicatedDeveloper.getSelectedItem(false) != null) {
            totalAddOns += addonDedicatedDeveloper.getSelectedItem(false).getTotalAmount();
        }
        if (addonDedicatedAccountManager.getSelectedItem(false) != null) {
            totalAddOns += addonDedicatedAccountManager.getSelectedItem(false).getTotalAmount();
        }
        return totalAddOns;
    }

    private void setCustomPriceLabel() {

        Integer userCount = getFullUsersCount();
        Integer essUserCount = getESSUsersCount();
        Integer noAccessUserCount = getNonAccessUsersCount();
        double totalAddonsPrice = getTotalAddonsPrice();

        int prevModuleCount = 0;
        if (usagePlan.isAccountsModule()) prevModuleCount++;
        if (usagePlan.isHumansModule()) prevModuleCount++;
        if (usagePlan.isProjectModule()) prevModuleCount++;
        if (usagePlan.isSalesModule()) prevModuleCount++;
        if (usagePlan.isPayrollModule()) prevModuleCount++;

        UsagePlanPrice prices = PricingUtils.getTotalPrice(userCount, essUserCount, customModuleCount, noAccessUserCount, totalAddonsPrice, usagePlan, isMonthly, type);


            usersPerAppLabel.setInnerHTML(wfmStrings.users() + "(" + userCount + ")" + " / " + wfmStrings.apps() + "(" + customModuleCount + ")");
            essUsersPerAppLabel.setInnerHTML(wfmStrings.essUser() + " " + "(" + essUserCount + ")");

        pricePerFullUser.setInnerHTML("$" + NumberUtils.getNumberFormatWithBigDecimal(userCount > 0 ? prices.getFullUsersDiscountedPrice() : 0));
        pricePerEssUser.setInnerHTML("$" + NumberUtils.getNumberFormatWithBigDecimal(essUserCount > 0 ? prices.getEssUsersDiscountedPrice() : 0));
            pricePerNoAccessUser.setInnerHTML("$" + NumberUtils.getNumberFormatWithBigDecimal(noAccessUserCount > 0 ? prices.getNonUsersPrice() / noAccessUserCount : 0));

        if(essUserCount > 0) {
            essUsersPerAppContainer.getStyle().clearDisplay();
        } else {
            essUsersPerAppContainer.getStyle().setDisplay(Style.Display.NONE);
        }
        if (noAccessUserCount > 0) {
            noAccessUsersPerAppContainer.getStyle().clearDisplay();

            if (usagePlan.isPaid() && usagePlan.getNonAccessUserCount() != null) {
                noAccessUsersPerAppLabel.getElement().setInnerHTML(myAccountStrings.nonUser() + " " + "(" + (noAccessUserCount - usagePlan.getNonAccessUserCount()) + ")" + " ");
            } else {
                noAccessUsersPerAppLabel.getElement().setInnerHTML(myAccountStrings.nonUser() + " " + "(" + noAccessUserCount + ")" + " ");
            }
            noAccessUsersPerAppLabel.add(new KpiCustomToolTip(nonUserMsg, true));
        } else {
            noAccessUsersPerAppContainer.getStyle().setDisplay(Style.Display.NONE);
        }


        fullUsersPrice.setInnerHTML(NumberUtils.getNumberFormatWithBigDecimal(prices.getFullUsersPrice()));
            essUsersPrice.setInnerHTML(NumberUtils.getNumberFormatWithBigDecimal(prices.getEssUsersPrice()));
            noAccessUsersPrice.setInnerHTML(NumberUtils.getNumberFormatWithBigDecimal(prices.getNonUsersPrice()));
            totalDiscount.setInnerHTML(NumberUtils.getNumberFormatWithBigDecimal(prices.getTotalDiscount()));
            totalAddOnsPrice.setInnerHTML(NumberUtils.getNumberFormatWithBigDecimal(totalAddonsPrice));
            subscriptionTotal.setInnerHTML(NumberUtils.getNumberFormatWithBigDecimal(prices.getTotalAmount()));
            totalToBePaid.setInnerHTML(NumberUtils.getNumberFormatWithBigDecimal(prices.getTotalAmount() + prices.getAddonPrice()));

        if (prices.getTotalAmount() > 0d || prices.getAddonPrice() > 0d) {
            if (usagePlan.isPaid()) {
                buyNowButton.setText(myAccountStrings.upgradeNow());
            }
        } else {
        }
    }


    private void registerEventHanlders() {
        //For PAID usage plans we allow only UPGRADE but they cant DOWNGRADE
        DOM.sinkEvents(customAccounts, Event.ONCHANGE);
        DOM.setEventListener(customAccounts, event -> {
            if (usagePlan.isFree()) {
                enableFreeTrialModule("accounts", customAccounts.isChecked());
            }
            if (usagePlan.isPaid() && usagePlan.isAccountsModule() && !customAccounts.isChecked()) {
                customAccounts.setChecked(true);
            } else {
                if (customAccounts.isChecked()) {
                    customModuleCount++;
                    accountsWrapper.addClassName("cp_modules-switch__item--active");
                } else {
                    accountsWrapper.removeClassName("cp_modules-switch__item--active");
                    if (customModuleCount > 0) {
                        customModuleCount--;
                    }
                }
                setCustomPriceLabel();
            }
        });

        DOM.sinkEvents(customSales, Event.ONCHANGE);
        DOM.setEventListener(customSales, event -> {
            if (usagePlan.isFree()) {
                enableFreeTrialModule("sales", customAccounts.isChecked());
            }
            if (usagePlan.isPaid() && usagePlan.isSalesModule() && !customSales.isChecked()) {
                customSales.setChecked(true);
            } else {
                if (customSales.isChecked()) {
                    customModuleCount++;
                    salesWrapper.addClassName("cp_modules-switch__item--active");
                } else {
                    if (customModuleCount > 0) {
                        customModuleCount--;
                        salesWrapper.removeClassName("cp_modules-switch__item--active");
                    }
                }
                setCustomPriceLabel();
            }
        });

        DOM.sinkEvents(customHumans, Event.ONCHANGE);
        DOM.setEventListener(customHumans, event -> {
            if (usagePlan.isFree()) {
                enableFreeTrialModule("humans", customAccounts.isChecked());
            }
            if (usagePlan.isPaid() && usagePlan.isHumansModule() && !customHumans.isChecked()) {
                customHumans.setChecked(true);
            } else {
                if (customHumans.isChecked()) {
                    customModuleCount++;
                    humansWrapper.addClassName("cp_modules-switch__item--active");
                } else {
                    if (customModuleCount > 0) {
                        customModuleCount--;
                        humansWrapper.removeClassName("cp_modules-switch__item--active");
                    }
                }
            }
            enableDisableESSandNoUsers();
            setCustomPriceLabel();
        });


        DOM.sinkEvents(customProjects, Event.ONCHANGE);
        DOM.setEventListener(customProjects, event -> {
            if (usagePlan.isFree()) {
                enableFreeTrialModule("projects", customAccounts.isChecked());
            }
            if (usagePlan.isPaid() && usagePlan.isProjectModule() && !customProjects.isChecked()) {
                customProjects.setChecked(true);
            } else {
                if (customProjects.isChecked()) {
                    customModuleCount++;
                    projectsWrapper.addClassName("cp_modules-switch__item--active");
                } else {
                    if (customModuleCount > 0) {
                        customModuleCount--;
                        projectsWrapper.removeClassName("cp_modules-switch__item--active");
                    }
                }
            }
            enableDisableESSandNoUsers();
            setCustomPriceLabel();
        });

        DOM.sinkEvents(customPayroll, Event.ONCHANGE);
        DOM.setEventListener(customPayroll, event -> {
            if (usagePlan.isFree()) {
                enableFreeTrialModule("payroll", customAccounts.isChecked());
            }
            if (usagePlan.isPaid() && usagePlan.isPayrollModule() && !customPayroll.isChecked()) {
                customPayroll.setChecked(true);
            } else {
                if (customPayroll.isChecked()) {
                    customModuleCount++;
                    payrolWrapper.addClassName("cp_modules-switch__item--active");
                } else {
                    if (customModuleCount > 0) {
                        customModuleCount--;
                        payrolWrapper.removeClassName("cp_modules-switch__item--active");
                    }
                }
            }
            enableDisableESSandNoUsers();
            setCustomPriceLabel();
        });


        fullUsersIncrease.addClickHandler(clickEvent -> {
            if (fullUsersDecrease.getElement().hasAttribute("data-disabled")) {
                fullUsersDecrease.getElement().removeAttribute("data-disabled");
            }
            setCustomUserCount(true, fullUsersCount);
        });

        fullUsersDecrease.addClickHandler(clickEvent -> {
            if (usagePlan.isCurrSub()) {
                if (usagePlan.isPaid() && getFullUsersCount() == usagePlan.getUserCount()) {
                    fullUsersDecrease.getElement().setAttribute("data-disabled", "true");
                } else {
                    setCustomUserCount(false, fullUsersCount);
                    if (usagePlan.isPaid() && getFullUsersCount() == usagePlan.getUserCount()) {
                        fullUsersDecrease.getElement().setAttribute("data-disabled", "true");
                    }
                }
            } else {
                setCustomUserCount(false, fullUsersCount);

                if (getFullUsersCount() < usagePlan.getActiveUserCount()) {
                    fullUsersCount.setValue(usagePlan.getActiveUserCount() + "");
                    fullUsersDecrease.getElement().setAttribute("data-disabled", "true");
                    Info.show(myAccountStrings.downgradeUsers(), Info.Type.WARNING);
                } else if (getFullUsersCount() == usagePlan.getActiveUserCount()) {
                    fullUsersDecrease.getElement().setAttribute("data-disabled", "true");
                }
            }
        });

        essUsersIncrease.addClickHandler(clickEvent -> {
            if (customHumans.isChecked() || customProjects.isChecked() || customPayroll.isChecked()) {
                if (essUsersDecrease.getElement().hasAttribute("data-disabled")) {
                    essUsersDecrease.getElement().removeAttribute("data-disabled");
                }

                setCustomUserCount(true, essUsersCount);
            }
        });

        essUsersDecrease.addClickHandler(clickEvent -> {
            if (usagePlan.isCurrSub()) {
                if (usagePlan.isPaid() && getESSUsersCount() == usagePlan.getEssUserCount()) {
                    essUsersDecrease.getElement().setAttribute("data-disabled", "true");
                } else {
                    setCustomUserCount(false, essUsersCount);
                    if ((usagePlan.isPaid() && getESSUsersCount() == usagePlan.getEssUserCount()) || getESSUsersCount() == 0) {
                        essUsersDecrease.getElement().setAttribute("data-disabled", "true");
                    }
                }
            } else {
                setCustomUserCount(false, essUsersCount);

                if (getESSUsersCount() < usagePlan.getActiveEssUserCount()) {
                    essUsersCount.setValue(usagePlan.getActiveEssUserCount() + "");
                    essUsersDecrease.getElement().setAttribute("data-disabled", "true");
                    Info.show(myAccountStrings.downgradeUsers(), Info.Type.WARNING);
                } else if (getESSUsersCount() == usagePlan.getActiveEssUserCount()) {
                    essUsersDecrease.getElement().setAttribute("data-disabled", "true");
                }
            }
        });

        noAccessUsersIncrease.addClickHandler(clickEvent -> {

            if (customHumans.isChecked() || customProjects.isChecked() || customPayroll.isChecked()) {
                if (noAccessUsersDecrease.getElement().hasAttribute("data-disabled")) {
                    noAccessUsersDecrease.getElement().removeAttribute("data-disabled");
                }
                setCustomUserCount(true, noAccessUsersCount);
            }
        });

        noAccessUsersDecrease.addClickHandler(clickEvent -> {
            if (usagePlan.isCurrSub()) {
                if (usagePlan.isPaid() && getNonAccessUsersCount() == usagePlan.getNonAccessUserCount()) {
                    noAccessUsersDecrease.getElement().setAttribute("data-disabled", "true");
                } else {
                    setCustomUserCount(false, noAccessUsersCount);
                    if ((usagePlan.isPaid() && getNonAccessUsersCount() == usagePlan.getNonAccessUserCount()) || getNonAccessUsersCount() == 0) {
                        noAccessUsersDecrease.getElement().setAttribute("data-disabled", "true");
                    }
                }
            } else {
                setCustomUserCount(false, noAccessUsersCount);

                if (getNonAccessUsersCount() < usagePlan.getActiveNonAccessUserCount()) {
                    noAccessUsersCount.setValue(usagePlan.getActiveNonAccessUserCount() + "");
                    noAccessUsersDecrease.getElement().setAttribute("data-disabled", "true");
                    Info.show(myAccountStrings.downgradeUsers(), Info.Type.WARNING);
                } else if (getNonAccessUsersCount() == usagePlan.getActiveNonAccessUserCount()) {
                    noAccessUsersDecrease.getElement().setAttribute("data-disabled", "true");
                }
            }
        });


        EventListener recalculatePricesListener = event -> {

            if (!customHumans.isChecked() && !customProjects.isChecked() && !customPayroll.isChecked()) {
                essUsersCount.setValue("0");
                noAccessUsersCount.setValue("0");
            }

            if (usagePlan.isCurrSub()) {
                if (usagePlan.isPaid()) {
                    if (getFullUsersCount() < usagePlan.getUserCount()) {
                        fullUsersCount.setValue(usagePlan.getUserCount() + "");
                    }
                    if (getESSUsersCount() < usagePlan.getEssUserCount()) {
                        essUsersCount.setValue(usagePlan.getEssUserCount() + "");
                    }
                    if (getNonAccessUsersCount() < usagePlan.getNonAccessUserCount()) {
                        noAccessUsersCount.setValue(usagePlan.getNonAccessUserCount() + "");
                    }
                } else {
                    //If negative number entered set it to zero
                    if (getFullUsersCount() <= 0) {
                        fullUsersCount.setValue(0 + "");
                    }
                    if (getESSUsersCount() <= 0) {
                        essUsersCount.setValue("0");
                    }
                    if (getNonAccessUsersCount() <= 0) {
                        noAccessUsersCount.setValue("0");
                    }
                }
            } else {
                if (getFullUsersCount() < usagePlan.getActiveUserCount()) {
                    fullUsersCount.setValue(usagePlan.getActiveUserCount() + "");
                    fullUsersDecrease.getElement().setAttribute("data-disabled", "true");
                    Info.show(myAccountStrings.downgradeUsers(), Info.Type.WARNING);
                } else if (getFullUsersCount() == usagePlan.getActiveUserCount()) {
                    fullUsersDecrease.getElement().setAttribute("data-disabled", "true");
                }
                if (getESSUsersCount() < usagePlan.getActiveEssUserCount()) {
                    essUsersCount.setValue(usagePlan.getActiveEssUserCount() + "");
                    essUsersDecrease.getElement().setAttribute("data-disabled", "true");
                    Info.show(myAccountStrings.downgradeUsers(), Info.Type.WARNING);
                } else if (getESSUsersCount() == usagePlan.getActiveEssUserCount()) {
                    essUsersDecrease.getElement().setAttribute("data-disabled", "true");
                }
                if (getNonAccessUsersCount() < usagePlan.getActiveNonAccessUserCount()) {
                    noAccessUsersCount.setValue(usagePlan.getActiveNonAccessUserCount() + "");
                    noAccessUsersDecrease.getElement().setAttribute("data-disabled", "true");
                    Info.show(myAccountStrings.downgradeUsers(), Info.Type.WARNING);
                } else if (getNonAccessUsersCount() == usagePlan.getActiveNonAccessUserCount()) {
                    noAccessUsersDecrease.getElement().setAttribute("data-disabled", "true");
                }
            }
            setCustomPriceLabel();
        };


        DOM.sinkEvents(fullUsersCount, Event.ONCHANGE);
        DOM.setEventListener(fullUsersCount, recalculatePricesListener);

        DOM.sinkEvents(essUsersCount, Event.ONCHANGE);
        DOM.setEventListener(essUsersCount, recalculatePricesListener);

        DOM.sinkEvents(noAccessUsersCount, Event.ONCHANGE);
        DOM.setEventListener(noAccessUsersCount, recalculatePricesListener);

    }

    private void setCustomUserCount(boolean increase, InputElement inputElement) {
        Integer userCount = 0;
        try {
            userCount = Integer.parseInt(inputElement.getValue());
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

        inputElement.setValue(userCount + "");

        setCustomPriceLabel();

    }

    private void loadCurrentOrLastUsagePlan() {
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
                //Handle URL if not empty
                handleAnchorString();
                setCustomPriceLabel();

            }
        });
    }

    private void enableFreeTrialModule(String module, boolean enable) {
        MyAccountService.App.get().enableOrDisableFreeTrialModule(module, enable, new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.error(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Boolean result) {
                LoadingPanel.loading(false);
                if(Boolean.TRUE.equals(result)) {
                    Info.show("Successfully " + (enable?"Enabled":"Disabled"), Info.Type.INFO);
                } else {
                    Info.show("Can not perform action.", Info.Type.WARNING);
                }
            }

        });
    }

    private void setData(final UsagePlanItem usagePlan) {
        this.usagePlan = usagePlan;
        customModuleCount = 0;

        if (!usagePlan.isFree() && !usagePlan.isPaid()) {
            isUpgrade = false;
        } else if (!usagePlan.isFree() && usagePlan.isShowUpgBt()) {
            isUpgrade = true;
        }

        if(usagePlan.isCurrSub()){
            fullUsersCount.setValue(usagePlan.getUserCount() + "");
            essUsersCount.setValue(usagePlan.getEssUserCount() + "");
            noAccessUsersCount.setValue(usagePlan.getNonAccessUserCount() + "");
        } else {
            fullUsersCount.setValue( Math.max(usagePlan.getUserCount(), usagePlan.getActiveUserCount()) + "");
            essUsersCount.setValue(Math.max(usagePlan.getEssUserCount(), usagePlan.getActiveEssUserCount()) + "");
            noAccessUsersCount.setValue(Math.max(usagePlan.getNonAccessUserCount(), usagePlan.getActiveNonAccessUserCount()) + "");
        }

        usersPerAppLabel.setInnerHTML("(" + usagePlan.getUserCount() + ") " + wfmStrings.users() + " / (" + customModuleCount + ") Apps");
        essUsersPerAppLabel.setInnerHTML(wfmStrings.essUser() + " (" + usagePlan.getEssUserCount() + ")");
        if (usagePlan.getNonAccessUserCount() > 0) {
            noAccessUsersPerAppContainer.getStyle().clearDisplay();
            noAccessUsersPerAppLabel.getElement().setInnerHTML(myAccountStrings.nonUser() + " (" + usagePlan.getNonAccessUserCount() + ") ");
            noAccessUsersPerAppLabel.add(new KpiCustomToolTip(nonUserMsg, true));
        } else {
            noAccessUsersPerAppContainer.getStyle().setDisplay(Style.Display.NONE);
        }

        currentUsageMonth = usagePlan.getUsageMonth();

        if (currentUsageMonth == 0) {
            currentUsagePeriod = -1;//      free trial usage month
        } else if (currentUsageMonth == 1) {
            currentUsagePeriod = 0;//       one month
        } else if (currentUsageMonth == 12) {
            currentUsagePeriod = 3;//2      12 month
        }
        String usagePlanPricingPackage = usagePlan.getCategoryREAL();
        //@TODO Decide to remove or leave (below 'if' block is from old code)

        currentCategory = FREE_TRIAL;
        if (usagePlan.isFree()) {
            currentCategory = FREE_TRIAL;
        } else if (usagePlan.isPaid()) {
            currentCategory = IS_PAID;//paid category (equally --> plus/premium/max)
        }

        customAccounts.setChecked(usagePlan.isAccountsModule());
        if (usagePlan.isAccountsModule()) {
            customModuleCount++;
            accountsWrapper.addClassName("cp_modules-switch__item--active");
        }

        customHumans.setChecked(usagePlan.isHumansModule());
        if (usagePlan.isHumansModule()) {
            customModuleCount++;
            humansWrapper.addClassName("cp_modules-switch__item--active");
        }
        customProjects.setChecked(usagePlan.isProjectModule());
        if (usagePlan.isProjectModule()) {
            customModuleCount++;
            projectsWrapper.addClassName("cp_modules-switch__item--active");
        }
        customSales.setChecked(usagePlan.isSalesModule());
        if (usagePlan.isSalesModule()) {
            customModuleCount++;
            salesWrapper.addClassName("cp_modules-switch__item--active");
        }
        customPayroll.setChecked(usagePlan.isPayrollModule());
        if (usagePlan.isPayrollModule()) {
            customModuleCount++;
            payrolWrapper.addClassName("cp_modules-switch__item--active");
        }

        enableDisableESSandNoUsers();


        if (usagePlan.isPaid()) {
            fullUsersDecrease.getElement().setAttribute("data-disabled", "true");
            essUsersDecrease.getElement().setAttribute("data-disabled", "true");
            noAccessUsersDecrease.getElement().setAttribute("data-disabled", "true");
        } else if(!usagePlan.isCurrSub()) {

            if(Math.max(usagePlan.getUserCount(), usagePlan.getActiveUserCount())==usagePlan.getActiveUserCount()) {
                fullUsersDecrease.getElement().setAttribute("data-disabled", "true");
            }
            if(Math.max(usagePlan.getEssUserCount(), usagePlan.getActiveEssUserCount())==usagePlan.getActiveEssUserCount()) {
                essUsersDecrease.getElement().setAttribute("data-disabled", "true");
            }
            if(Math.max(usagePlan.getNonAccessUserCount(), usagePlan.getActiveNonAccessUserCount())==usagePlan.getActiveNonAccessUserCount()) {
                noAccessUsersDecrease.getElement().setAttribute("data-disabled", "true");
            }
        }
    }

    private void enableDisableESSandNoUsers() {
        // Enable/Disable ESSUsers and NonUsers fields
        if (!customHumans.isChecked() && !customProjects.isChecked() && !customPayroll.isChecked()) {
            essUsersCount.setValue("0");
            noAccessUsersCount.setValue("0");
            essUsersIncrease.getElement().setAttribute("data-disabled", "true");
            essUsersDecrease.getElement().setAttribute("data-disabled", "true");
            noAccessUsersIncrease.getElement().setAttribute("data-disabled", "true");
            noAccessUsersDecrease.getElement().setAttribute("data-disabled", "true");
            nonUserContainer.getStyle().setDisplay(Style.Display.NONE);
            essUserContainer.getStyle().setDisplay(Style.Display.NONE);
        } else {
            int essUsersCount = getESSUsersCount();
            int noAccessUsersCount = getNonAccessUsersCount();

            nonUserContainer.getStyle().setDisplay(Style.Display.BLOCK);
            essUserContainer.getStyle().setDisplay(Style.Display.BLOCK);

            if (essUsersIncrease.getElement().hasAttribute("data-disabled")) {
                essUsersIncrease.getElement().removeAttribute("data-disabled");
            }
            if (essUsersDecrease.getElement().hasAttribute("data-disabled") && essUsersCount > 0) {
                essUsersDecrease.getElement().removeAttribute("data-disabled");
            }
            if (noAccessUsersIncrease.getElement().hasAttribute("data-disabled")) {
                noAccessUsersIncrease.getElement().removeAttribute("data-disabled");
            }
            if (noAccessUsersDecrease.getElement().hasAttribute("data-disabled") && noAccessUsersCount > 0) {
                noAccessUsersDecrease.getElement().removeAttribute("data-disabled");
            }
        }
    }

    private int getFullUsersCount() {

        Integer userCount = null;
        try {
            userCount = Integer.parseInt(fullUsersCount.getValue());
        } catch (Exception ignored) {
        }
        if (userCount == null || userCount <= 0) {
            userCount = 0;
        }
        return userCount;

    }

    private int getESSUsersCount() {
        Integer essUserCount = null;
        try {
            essUserCount = Integer.parseInt(essUsersCount.getValue());
        } catch (Exception ignored) {
        }

        if (essUserCount == null || essUserCount <= 0) {
            essUserCount = 0;
        }
        return essUserCount;

    }


    private int getNonAccessUsersCount() {
        Integer noAccessCount = null;
        try {
            noAccessCount = Integer.parseInt(noAccessUsersCount.getValue());
        } catch (Exception ignored) {
        }

        if (noAccessCount == null || noAccessCount <= 0) {
            noAccessCount = 0;
        }
        return noAccessCount;

    }

    private boolean validateBuyAndUpgradeUsersCount(Integer userCount) {
        boolean userLess = false;
        boolean pricingPackageLess = false;
        boolean pricingPackageEqual = false;

        if (this.usagePlan == null || this.usagePlan.getCategoryREAL() == null) {
            return false;
        }
        final Integer currentUserCount_ = FREE_TRIAL.equals(currentCategory)
                ? usagePlan.getRegisteredUsersCount()
                : usagePlan.getUserCount();

        final int selectedPackageId = 4;

        final int currentPackageId = 4;
        boolean error = false;

        if (userCount < currentUserCount_) {
            userLess = true;
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
        if (userLess && (pricingPackageEqual || pricingPackageLess) && !this.checkForSelectedModules(this.usagePlan.getCategoryREAL(), this.usagePlan.getCategoryREAL())) {
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

        if (newPackageName.equals(oldPackageName)) {
            boolean salesModuleSelected = customSales.isChecked();
            boolean humansModuleSelected = customHumans.isChecked();
            boolean projectModuleSelected = customProjects.isChecked();
            boolean accontsModuleSelected = customAccounts.isChecked();
            boolean payrollModuleSelected = customPayroll.isChecked();
            boolean totalResult = !((usagePlan.isAccountsModule() == accontsModuleSelected) &&
                    (usagePlan.isProjectModule() == projectModuleSelected) &&
                    (usagePlan.isSalesModule() == salesModuleSelected) &&
                    (usagePlan.isHumansModule() == humansModuleSelected) &&
                    (usagePlan.isPayrollModule() == payrollModuleSelected));
            return totalResult;

        }
        return true;
    }

    @Override
    protected Widget onInitialize() {
        AllPricingViewUiBinder ourUiBinder = GWT.create(AllPricingViewUiBinder.class);
        add(ourUiBinder.createAndBindUi(this));
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

    @Override
    public void reInitialize() {
        customModuleCount = 0;
        this.loadCurrentOrLastUsagePlan();
    }

    private void handleAnchorString() {
        String historyToken = getContainer().getHistoryToken();//Utils.getAnchorString().substring(1);

        if (historyToken != null && historyToken.contains("/")) {
            historyToken = historyToken.replace("%7C", "|");
            AnchorParam param = Utils.parseAnchorParam(historyToken);
            if(param!=null && param.getTokens()!=null) {

                DataListBox[] dataListBoxes = {addonOnlineTraining,
                addonInitialSetup,
                addonExtraStorage,
                addonCustomPDFTemplate,
                addonDedicatedDeveloper,
                addonDedicatedAccountManager};

                SelectItem[][] vals = {ONLINE_TRAININGS, INITIAL_SETUPS, EXTRA_STORAGES, CUSTOM_PDFTEMPLATES, DEDICATED_DEVELOPERS, DEDICATED_ACCOUNT_MANAGERS};

                if(param.getTokens().length>2) {
                    try {
                        dataListBoxes[Integer.valueOf(param.getTokens()[1])].setSelected(vals[Integer.valueOf(param.getTokens()[1])][Integer.valueOf(param.getTokens()[2])-1]);
                    } catch (NumberFormatException e) {
                        GWT.log("", e);
                    }
                    if(param.getTokens().length>3) {
                        try {
                            addonDedicatedAccountManager.setSelected(vals[5][0]);
                        } catch (NumberFormatException e) {
                            GWT.log("", e);
                        }
                    }
                    Utils.scrollIntoView(addonDedicatedDeveloper.getElement());
                }
            }
        }
    }

    interface AllPricingViewUiBinder extends UiBinder<HTMLPanel, AllPricingMaterialView> {
    }
}
