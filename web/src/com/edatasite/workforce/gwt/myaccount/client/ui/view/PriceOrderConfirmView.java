package com.edatasite.workforce.gwt.myaccount.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.KpiPaymentRequestObject;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.myaccount.client.PricingUtils;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountMessages;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountStrings;
import com.edatasite.workforce.gwt.myaccount.client.rpc.*;
import com.edatasite.workforce.gwt.myaccount.client.ui.stripe.StripeCheckoutToken;
import com.edatasite.workforce.gwt.myaccount.client.ui.stripe.StripeCompletePayment;
import com.edatasite.workforce.gwt.myaccount.client.ui.stripe.StripePaymentHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.*;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Dilsh0d Madrahimov on 8/31/2018.
 */
public class PriceOrderConfirmView extends View implements Constants, Colapse {

    private static final MyAccountStrings myAccountStrings = MyAccountStrings.App.get();
    private static final MyAccountMessages myAccountMessages = MyAccountMessages.App.get();
    private static final NumberFormat extendedNumberFormat = NumberFormat.getFormat(",##0.00");
    private static final MyAccountServiceAsync myAccountService = MyAccountService.App.get();

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
    DivElement pageTitle;
    @UiField
    ParagraphElement pageDescription;
    @UiField
    HeadingElement securePage;
    @UiField
    TableCellElement item;
    @UiField
    TableCellElement unitPrice;
    @UiField
    TableCellElement quantityLabel;
    @UiField
    TableCellElement totalYear;
    @UiField
    Element itemTableBody;
    @UiField
    ParagraphElement securePageDesc1;
    @UiField
    ParagraphElement securePageDesc2;
    @UiField
    Anchor updateLink;
    @UiField
    Anchor makePaymentLink;
    @UiField
    SpanElement updateLinkSpan;
    @UiField
    SpanElement makePaymentLinkSpan;
    @UiField
    Element totalReceiptTableBody;
    @UiField
    Element totalSumUpTableBody;
    @UiField
    Element totalTableFooter;
    @UiField
    HTMLPanel pdfPanel;


    private String type = null;
    private Integer usageplanid = null;
    private UsagePlanItem usagePlan;
    private UsagePlanPrice usagePlanPriceDataToServer;
    private UsagePlanItem prevUsagePlan = new UsagePlanItem();
    private final String currency = "USD";
    private KpiPaymentRequestObject kpiRequestObject;

    @UiConstructor
    public PriceOrderConfirmView() {
        super("pricingOrder", myAccountStrings.pricingOrderConfirm());
    }

    @UiConstructor
    public PriceOrderConfirmView(String type, Integer usageplanid) {
        this();
        this.type = type;
        this.usageplanid = usageplanid;
    }

    interface OrderSummaryViewUiBinder extends UiBinder<HTMLPanel, PriceOrderConfirmView> {
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        OrderSummaryViewUiBinder ourUiBinder = GWT.create(OrderSummaryViewUiBinder.class);
        add(ourUiBinder.createAndBindUi(this));
        initialize();
        return this;
    }

    private void initialize() {
        pageTitle.setInnerHTML(myAccountStrings.orderSummary());
        pageDescription.setInnerHTML(myAccountStrings.pageDescription());
        securePage.setInnerHTML(myAccountStrings.aboutYourSubscriptionSecurePage());
        securePageDesc1.setInnerHTML(myAccountMessages.securePageDesc1(/*Utils.getHostNameURL(),*/ Utils.getProductName()));
        securePageDesc2.setInnerHTML(myAccountStrings.securePageDesc2());
        updateLinkSpan.setInnerHTML(wfmStrings.back());
        makePaymentLinkSpan.setInnerHTML(wfmStrings.continueOnly());

        item.setInnerHTML(wfmStrings.item());
        unitPrice.setInnerHTML(wfmStrings.unitPrice());
        quantityLabel.setInnerHTML(wfmStrings.qty());
        totalYear.setInnerHTML(myAccountStrings.totalYear());

        updateLink.addClickHandler(event -> {
            myAccountService.deleteSubscriptionHistory(usagePlan, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(Boolean aBoolean) {
                    closeTab();
                }
            });
        });

        makePaymentLink.addClickHandler(event -> paymentDialogBox());
        kpiRequestObject = new KpiPaymentRequestObject();

        getDataToFillFields();

    }

    @Override
    public void reInitialize() {
        super.reInitialize();
        getDataToFillFields();
    }

    private void getDataToFillFields() {
        if (AllPricingMaterialView.dataForSend == null) {
            closeTab();
            return;
        }

        if (PRICING_ORDER.SUBSCRIPTION_ADD.equals(type)) {
            LoadingPanel.loading(true);
            myAccountService.getUsagePlanItem(usageplanid, new AbstractAsyncCallback<UsagePlanItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.error(), Info.Type.WARNING);
                }

                @Override
                public void success(UsagePlanItem result) {
                    LoadingPanel.loading(false);
                    usagePlan = result;
                    drawItemTable();
                    drawTotalTable();
                    sendEmailUserVisitToPage();
                }
            });
        } else {
            usagePlan = AllPricingMaterialView.usagePlanItem;
            prevUsagePlan = AllPricingMaterialView.prevUsagePlanItem;

            if (usagePlan != null) {
                drawItemTable();
                drawTotalTable();
                sendEmailUserVisitToPage();
            } else {
                closeTab();
                Utils.reloadPage();
            }
        }
    }

    /*
      Send email for statistics when user is visited to the page
     */
    private void sendEmailUserVisitToPage() {
        myAccountService.sendEmailUserVisitToPage(kpiRequestObject, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Void result) {

            }
        });
    }

    private void drawItemTable() {
        UsagePlanPrice prices = AllPricingMaterialView.dataForSend;

        itemTableBody.removeAllChildren();
        totalYear.setInnerHTML(usagePlan.getUsageMonth() == 1 ? myAccountStrings.totalMonth() : myAccountStrings.totalYear());
        String unitPriceTitle = " / " + wfmStrings.user() + " / " + wfmStrings.month();
        if (PP_BY_YEAR.equals(AllPricingMaterialView.type)) {
            unitPriceTitle = " / " + wfmStrings.user() + " / " + wfmStrings.year();
        } else if (PP_BY_HALF_YEAR.equals(AllPricingMaterialView.type)) {
            unitPriceTitle = " / " + wfmStrings.user() + " / " + wfmStrings.halfYearly();
        } else if (PP_BY_QUARTER.equals(AllPricingMaterialView.type)) {
            unitPriceTitle = " / " + wfmStrings.user() + " / " + wfmStrings.quarterly();
        }


        //Subscription reccuring title
        Element tr = DOM.createTR();
        tr.setAttribute("colspan", "4");

        Element td1 = DOM.createTD();
        td1.setInnerHTML(myAccountStrings.recurringSubscription());
        kpiRequestObject.setReccurSubscripTitle(myAccountStrings.recurringSubscription());
        Element td2 = DOM.createTD();
        Element td3 = DOM.createTD();
        Element td4 = DOM.createTD();
        td1.setAttribute("style", "text-transform:uppercase;color:black;");

        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        itemTableBody.appendChild(tr);

        //App Add
        tr = DOM.createTR();
        td1 = DOM.createTD();
        td2 = DOM.createTD();
        td3 = DOM.createTD();
        td4 = DOM.createTD();

        td1.setAttribute("style", "padding-left:70px");
        td3.setClassName(TEXT_RIGHT);
        td4.setClassName(TEXT_RIGHT);

        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        itemTableBody.appendChild(tr);


        int prevModuleCount = 0;
        if (prevUsagePlan.isAccountsModule()) prevModuleCount++;
        if (prevUsagePlan.isHumansModule()) prevModuleCount++;
        if (prevUsagePlan.isProjectModule()) prevModuleCount++;
        if (prevUsagePlan.isSalesModule()) prevModuleCount++;
        if (prevUsagePlan.isPayrollModule()) prevModuleCount++;

        UsagePlanPrice prevPrices = prevUsagePlan.isPaid() ? PricingUtils.getTotalPrice(prevUsagePlan.getUserCount(), prevUsagePlan.getEssUserCount(), prevModuleCount,
                prevUsagePlan.getNonAccessUserCount(), 0, prevUsagePlan, prevUsagePlan.getUsageMonth() != 12, AllPricingMaterialView.type) : new UsagePlanPrice();

        td1.setInnerHTML(myAccountStrings.usersApps().concat(getItemTitle()));
        kpiRequestObject.setUsersItem("User(s) / App(s) ".concat(getItemTitle()));

            td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getFullUsersPrice() / (usagePlan != null ? usagePlan.getUserCount() : 0)) + unitPriceTitle);
            kpiRequestObject.setUsersUnitPrice(getNumberFormatWithBigDecimal(prices.getFullUsersPrice() / usagePlan.getUserCount()) + unitPriceTitle);

            td3.setInnerHTML(usagePlan.getUserCount() + "");
            kpiRequestObject.setUsersQty(usagePlan.getUserCount() + "");


        td4.setInnerHTML(getNumberFormatWithBigDecimal(prices.getFullUsersPrice()));
            kpiRequestObject.setUsersTotalYear(getNumberFormatWithBigDecimal(prices.getFullUsersPrice()));


        //ESS
        if (usagePlan.getEssUserCount() != null && usagePlan.getEssUserCount() > 0) {
            tr = DOM.createTR();

            td1 = DOM.createTD();
            td2 = DOM.createTD();
            td3 = DOM.createTD();
            td4 = DOM.createTD();

            td1.setClassName("tblCell_1");
            td3.setClassName(TEXT_RIGHT);
            td4.setClassName(TEXT_RIGHT);

            tr.appendChild(td1);
            tr.appendChild(td2);
            tr.appendChild(td3);
            tr.appendChild(td4);
            itemTableBody.appendChild(tr);

            td1.setInnerHTML(myAccountStrings.essUsers());
            kpiRequestObject.setEssItem("ESS User(s)");

                td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getEssUsersPrice() / usagePlan.getEssUserCount()) + unitPriceTitle);
                kpiRequestObject.setEssUnitPrice(getNumberFormatWithBigDecimal(prices.getEssUsersPrice() / usagePlan.getEssUserCount()) + unitPriceTitle);

                td3.setInnerHTML(usagePlan.getEssUserCount() + "");
                kpiRequestObject.setEssQty(usagePlan.getEssUserCount() + "");

                td4.setInnerHTML(getNumberFormatWithBigDecimal(prices.getEssUsersPrice()));
                kpiRequestObject.setEssTotalYear(getNumberFormatWithBigDecimal(prices.getEssUsersPrice()));

        }

        //Non Access Users
        if (usagePlan.getNonAccessUserCount() != null && usagePlan.getNonAccessUserCount() > 0) {
            tr = DOM.createTR();

            td1 = DOM.createTD();
            td2 = DOM.createTD();
            td3 = DOM.createTD();
            td4 = DOM.createTD();

            td1.setClassName("tblCell_1");
            td3.setClassName(TEXT_RIGHT);
            td4.setClassName(TEXT_RIGHT);

            tr.appendChild(td1);
            tr.appendChild(td2);
            tr.appendChild(td3);
            tr.appendChild(td4);
            itemTableBody.appendChild(tr);

            td1.setInnerHTML(myAccountStrings.nonUsers());
            kpiRequestObject.setNonUserItem("Non User(s)");

            td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getNonUsersPrice() / usagePlan.getNonAccessUserCount()) + unitPriceTitle);
                kpiRequestObject.setNonUserUnitPrice(getNumberFormatWithBigDecimal(prices.getNonUsersPrice() / usagePlan.getNonAccessUserCount()) + unitPriceTitle);


            td3.setInnerHTML(usagePlan.getNonAccessUserCount() + "");
                kpiRequestObject.setNonUserQty(usagePlan.getNonAccessUserCount() + "");

                td4.setInnerHTML(getNumberFormatWithBigDecimal(prices.getNonUsersPrice()));
                kpiRequestObject.setNonUserTotalYear(getNumberFormatWithBigDecimal(prices.getNonUsersPrice()));

        }

        //Addons
        if ((usagePlan.getAddonOnlineTraining() != null && usagePlan.getAddonOnlineTraining() > 0d) || (usagePlan.getAddonInitialSetup() != null && usagePlan.getAddonInitialSetup() > 0d) ||
                (usagePlan.getAddonExtraStorage() != null && usagePlan.getAddonExtraStorage() > 0d) || (usagePlan.getAddonCustomPDFTemplate() != null && usagePlan.getAddonCustomPDFTemplate() > 0d) ||
                (usagePlan.getAddonDedicatedDeveloper() != null && usagePlan.getAddonDedicatedDeveloper() > 0d) || (usagePlan.getAddonDedicatedAccountManager() != null && usagePlan.getAddonDedicatedAccountManager() > 0d)) {

            AddOnsItem addOnsItem = AllPricingMaterialView.addOnsItem;
            usagePlan.setAddOnsItem(addOnsItem);
            //Addons title
            tr = DOM.createTR();
            tr.setAttribute("colspan", "3");

            td1 = DOM.createTD();
            td1.setInnerHTML(myAccountStrings.addonsTitle());
            kpiRequestObject.setAddonsTitle("Extra Add-on(s)");
            td2 = DOM.createTD();
            td3 = DOM.createTD();
            td4 = DOM.createTD();
            td1.setAttribute("style", "text-transform:uppercase;color:black;");

            tr.appendChild(td1);
            tr.appendChild(td2);
            tr.appendChild(td3);
            tr.appendChild(td4);
            itemTableBody.appendChild(tr);

            //Addons
            if (usagePlan.getAddonOnlineTraining() != null && usagePlan.getAddonOnlineTraining() > 0d) {
                tr = DOM.createTR();
                td1 = DOM.createTD();
                td2 = DOM.createTD();
                td3 = DOM.createTD();
                td4 = DOM.createTD();

                td3.setClassName(TEXT_RIGHT);
                td4.setClassName(TEXT_RIGHT);

                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
                itemTableBody.appendChild(tr);

                td1.setInnerHTML(myAccountStrings.onlineTraining());
                kpiRequestObject.setAddonsOnlineTrainingItem("Online Training");
                td1.setClassName("tblCell_1");
                td3.setInnerHTML(addOnsItem.getOnlineTraining().getDescription());
                kpiRequestObject.setAddonsOnlineTrainingQty(addOnsItem.getOnlineTraining().getDescription());
                td4.setInnerHTML(getNumberFormatWithBigDecimal(usagePlan.getAddonOnlineTraining()));
                kpiRequestObject.setAddonsOnlineTrainingTotalYear(getNumberFormatWithBigDecimal(usagePlan.getAddonOnlineTraining()));
            }

            if (usagePlan.getAddonInitialSetup() != null && usagePlan.getAddonInitialSetup() > 0d) {
                tr = DOM.createTR();
                td1 = DOM.createTD();
                td2 = DOM.createTD();
                td3 = DOM.createTD();
                td4 = DOM.createTD();

                td3.setClassName(TEXT_RIGHT);
                td4.setClassName(TEXT_RIGHT);

                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
                itemTableBody.appendChild(tr);

                td1.setInnerHTML(myAccountStrings.initialSetUpPackage());
                kpiRequestObject.setAddonsInitialSetUpPackageItem("Initial Set Up Package");
                td1.setClassName("tblCell_1");
                td3.setInnerHTML(addOnsItem.getInitialSetup().getDescription());
                kpiRequestObject.setAddonsInitialSetUpPackageQty(addOnsItem.getInitialSetup().getDescription());
                td4.setInnerHTML(getNumberFormatWithBigDecimal(usagePlan.getAddonInitialSetup()));
                kpiRequestObject.setAddonsInitialSetUpPackageTotalYear(getNumberFormatWithBigDecimal(usagePlan.getAddonInitialSetup()));

            }

            if (usagePlan.getAddonDedicatedAccountManager() != null && usagePlan.getAddonDedicatedAccountManager() > 0d) {
                tr = DOM.createTR();
                td1 = DOM.createTD();
                td2 = DOM.createTD();
                td3 = DOM.createTD();
                td4 = DOM.createTD();

                td3.setClassName(TEXT_RIGHT);
                td4.setClassName(TEXT_RIGHT);

                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
                itemTableBody.appendChild(tr);

                td1.setInnerHTML(wfmStrings.premiumSupport());
                kpiRequestObject.setAddonsPremiumSupportItem("Premium Support");
                td1.setClassName("tblCell_1");
                td3.setInnerHTML(addOnsItem.getDedicatedAccountManager().getDescription());
                kpiRequestObject.setAddonsPremiumSupportQty(addOnsItem.getDedicatedAccountManager().getDescription());
                td4.setInnerHTML(getNumberFormatWithBigDecimal(usagePlan.getAddonDedicatedAccountManager()));
                kpiRequestObject.setAddonsPremiumSupportTotalYear(getNumberFormatWithBigDecimal(usagePlan.getAddonDedicatedAccountManager()));
            }

            if (usagePlan.getAddonCustomPDFTemplate() != null && usagePlan.getAddonCustomPDFTemplate() > 0d) {
                tr = DOM.createTR();
                td1 = DOM.createTD();
                td2 = DOM.createTD();
                td3 = DOM.createTD();
                td4 = DOM.createTD();

                td3.setClassName(TEXT_RIGHT);
                td4.setClassName(TEXT_RIGHT);

                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
                itemTableBody.appendChild(tr);

                td1.setInnerHTML(myAccountStrings.customPDFTemplate());
                kpiRequestObject.setAddonsCustomPDFItem("Custom PDF Template");
                td1.setClassName("tblCell_1");
                td3.setInnerHTML(addOnsItem.getCustomPdfTemplate().getDescription());
                kpiRequestObject.setAddonsCustomPDFQty(addOnsItem.getCustomPdfTemplate().getDescription());
                td4.setInnerHTML(getNumberFormatWithBigDecimal(usagePlan.getAddonCustomPDFTemplate()));
                kpiRequestObject.setAddonsCustomPDFTotalYear(getNumberFormatWithBigDecimal(usagePlan.getAddonCustomPDFTemplate()));
            }

            if (usagePlan.getAddonExtraStorage() != null && usagePlan.getAddonExtraStorage() > 0d) {
                tr = DOM.createTR();
                td1 = DOM.createTD();
                td2 = DOM.createTD();
                td3 = DOM.createTD();
                td4 = DOM.createTD();

                td3.setClassName(TEXT_RIGHT);
                td4.setClassName(TEXT_RIGHT);

                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
                itemTableBody.appendChild(tr);

                td1.setInnerHTML(myAccountStrings.extraStorage());
                kpiRequestObject.setAddonsExtraStorageItem("Extra Storage");
                td1.setClassName("tblCell_1");
                td3.setInnerHTML(addOnsItem.getExtraStorage().getDescription());
                kpiRequestObject.setAddonsExtraStorageQty(addOnsItem.getExtraStorage().getDescription());
                td4.setInnerHTML(getNumberFormatWithBigDecimal(usagePlan.getAddonExtraStorage()));
                kpiRequestObject.setAddonsExtraStorageTotalYear(getNumberFormatWithBigDecimal(usagePlan.getAddonExtraStorage()));
            }

            if (usagePlan.getAddonDedicatedDeveloper() != null && usagePlan.getAddonDedicatedDeveloper() > 0d) {
                tr = DOM.createTR();
                td1 = DOM.createTD();
                td2 = DOM.createTD();
                td3 = DOM.createTD();
                td4 = DOM.createTD();

                td3.setClassName(TEXT_RIGHT);
                td4.setClassName(TEXT_RIGHT);

                tr.appendChild(td1);
                tr.appendChild(td2);
                tr.appendChild(td3);
                tr.appendChild(td4);
                itemTableBody.appendChild(tr);

                td1.setInnerHTML(wfmStrings.dedicatedDeveloper());
                kpiRequestObject.setAddonsDedicatedDeveloperItem("Dedicated Developer");
                td1.setClassName("tblCell_1");
                td3.setInnerHTML(addOnsItem.getDedicatedDeveloper().getDescription());
                kpiRequestObject.setAddonsDedicatedDeveloperQty(addOnsItem.getDedicatedDeveloper().getDescription());
                td4.setInnerHTML(getNumberFormatWithBigDecimal(usagePlan.getAddonDedicatedDeveloper()));
                kpiRequestObject.setAddonsDedicatedDeveloperTotalYear(getNumberFormatWithBigDecimal(usagePlan.getAddonDedicatedDeveloper()));
            }
        }
    }

    private String getItemTitle() {
        String itemTitle = "(";

        if (usagePlan.isAccountsModule()) {
            itemTitle += wfmStrings.accounts();
            itemTitle += ", ";
        }
        if (usagePlan.isSalesModule()) {
            itemTitle += wfmStrings.sales();
            itemTitle += ", ";
        }
        if (usagePlan.isHumansModule()) {
            itemTitle += wfmStrings.hrms();
            itemTitle += ", ";
        }
        if (usagePlan.isProjectModule()) {
            itemTitle += wfmStrings.projects();
            itemTitle += ", ";
        }
        if (usagePlan.isPayrollModule()) {
            itemTitle += wfmStrings.payroll();
            itemTitle += ", ";
        }
        if (itemTitle.endsWith(", ")) {
            itemTitle = itemTitle.substring(0, itemTitle.length() - 2);
        }

        itemTitle += ")";


        return itemTitle;
    }

    private void drawTotalTable() {

        UsagePlanPrice prices = AllPricingMaterialView.dataForSend;
        usagePlanPriceDataToServer = prices;

        int appCount = getAppCount();

        totalReceiptTableBody.removeAllChildren();
        totalSumUpTableBody.removeAllChildren();
        totalTableFooter.removeAllChildren();

        int prevModuleCount = 0;
        if (prevUsagePlan.isAccountsModule()) prevModuleCount++;
        if (prevUsagePlan.isHumansModule()) prevModuleCount++;
        if (prevUsagePlan.isProjectModule()) prevModuleCount++;
        if (prevUsagePlan.isSalesModule()) prevModuleCount++;
        if (prevUsagePlan.isPayrollModule()) prevModuleCount++;

        UsagePlanPrice prevPrices = prevUsagePlan.isPaid() ? PricingUtils.getTotalPrice(prevUsagePlan.getUserCount(), prevUsagePlan.getEssUserCount(), prevModuleCount,
                prevUsagePlan.getNonAccessUserCount(), 0, prevUsagePlan, prevUsagePlan.getUsageMonth() != 12, AllPricingMaterialView.type) : new UsagePlanPrice();


        //Users
        Element tr = DOM.createTR();
        Element td1 = DOM.createTD();
        Element td2 = DOM.createTD();
        tr.appendChild(td1);
        tr.appendChild(td2);
        if (prevUsagePlan.isPaid()) {
            td1.setInnerHTML(wfmStrings.users() + " (" + (usagePlan.getUserCount() - prevUsagePlan.getUserCount()) + ")" + " / " + wfmStrings.apps() + " (" + appCount + ")");

            td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getFullUsersPrice() - prevPrices.getFullUsersPrice()));
            usagePlanPriceDataToServer.setFullUsersPrice(prices.getFullUsersPrice() - prevPrices.getFullUsersPrice());
        } else {
            td1.setInnerHTML(wfmStrings.users() + " (" + usagePlan.getUserCount() + ")" + " / " + wfmStrings.apps() + " (" + appCount + ")");
            td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getFullUsersPrice()));
        }
        totalReceiptTableBody.appendChild(tr);

        //ESS Users
        if (usagePlan.getEssUserCount() != null && usagePlan.getEssUserCount() > 0) {
            tr = DOM.createTR();
            td1 = DOM.createTD();
            td2 = DOM.createTD();
            tr.appendChild(td1);
            tr.appendChild(td2);

            if (prevUsagePlan.isPaid()) {
                td1.setInnerHTML(wfmStrings.essUser() + " (" + (usagePlan.getEssUserCount() - prevUsagePlan.getEssUserCount()) + ")");
                td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getEssUsersPrice() - prevPrices.getEssUsersPrice()));
                usagePlanPriceDataToServer.setEssUsersPrice(prices.getEssUsersPrice() - prevPrices.getEssUsersPrice());
            } else {
                td1.setInnerHTML(wfmStrings.essUser() + " (" + usagePlan.getEssUserCount() + ")");
                td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getEssUsersPrice()));
            }
            totalReceiptTableBody.appendChild(tr);
        }
        //Non Access Users
        if (usagePlan.getNonAccessUserCount() != null && usagePlan.getNonAccessUserCount() > 0) {
            tr = DOM.createTR();
            td1 = DOM.createTD();
            td2 = DOM.createTD();
            tr.appendChild(td1);
            tr.appendChild(td2);

            if (prevUsagePlan.isPaid()) {
                td1.setInnerHTML(myAccountStrings.nonUser() + " (" + (usagePlan.getNonAccessUserCount() - prevUsagePlan.getNonAccessUserCount()) + ")");
                td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getNonUsersPrice() - prevPrices.getNonUsersPrice()));
                usagePlanPriceDataToServer.setNonUsersPrice(prices.getNonUsersPrice() - prevPrices.getNonUsersPrice());
            } else {
                td1.setInnerHTML(myAccountStrings.nonUser() + " (" + usagePlan.getNonAccessUserCount() + ")");
                td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getNonUsersPrice()));
            }
            totalReceiptTableBody.appendChild(tr);
        }

        //Users Discount
        if (prices.getTotalDiscount() > 0) {
            tr = DOM.createTR();
            td1 = DOM.createTD();
            td2 = DOM.createTD();
            td2.setClassName("cp_receipt__discount");
            tr.appendChild(td1);
            tr.appendChild(td2);
            td1.setInnerHTML(myAccountStrings.youSave());
            kpiRequestObject.setUsersDiscountTitle("Users Discount");
            if (prevUsagePlan.isPaid()) {
                td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getTotalDiscount() - prevPrices.getTotalDiscount()));
                kpiRequestObject.setUsersDiscountTotal(getNumberFormatWithBigDecimal(prices.getTotalDiscount() - prevPrices.getTotalDiscount()));
                usagePlanPriceDataToServer.setTotalDiscount(prices.getTotalDiscount() - prevPrices.getTotalDiscount());
            } else {
                td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getTotalDiscount()));
                kpiRequestObject.setUsersDiscountTotal(getNumberFormatWithBigDecimal(prices.getTotalDiscount()));
            }
            totalReceiptTableBody.appendChild(tr);
        }

        //Subscriptions
        tr = DOM.createTR();
        td1 = DOM.createTD();
        td2 = DOM.createTD();
        tr.appendChild(td1);
        tr.appendChild(td2);
        td1.setInnerHTML(wfmStrings.subtotalSubscription());
        kpiRequestObject.setTotalSubscriptionTitle(wfmStrings.subtotalSubscription());
        if (prevUsagePlan.isPaid()) {
            td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getTotalAmount() - prevPrices.getTotalAmount()));
            kpiRequestObject.setTotalSubscriptionTotal(getNumberFormatWithBigDecimal(prices.getTotalAmount() - prevPrices.getTotalAmount()));
            usagePlanPriceDataToServer.setTotalSubscription(prices.getTotalAmount() - prevPrices.getTotalAmount());
        } else {
            td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getTotalAmount()));
            kpiRequestObject.setTotalSubscriptionTotal(getNumberFormatWithBigDecimal(prices.getTotalAmount()));
        }
        totalSumUpTableBody.appendChild(tr);

        //Total Add-on
        tr = DOM.createTR();
        td1 = DOM.createTD();
        td2 = DOM.createTD();
        tr.appendChild(td1);
        tr.appendChild(td2);
        td1.setInnerHTML(myAccountStrings.subtotalAddOn());
        kpiRequestObject.setTotalAddonTitle(myAccountStrings.subtotalAddOn());
        td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getAddonPrice()));
        kpiRequestObject.setTotalAddonTotal(getNumberFormatWithBigDecimal(prices.getAddonPrice()));
        totalSumUpTableBody.appendChild(tr);

        //To be paid
        tr = DOM.createTR();
        td1 = DOM.createTD();
        td2 = DOM.createTD();
        tr.appendChild(td1);
        tr.appendChild(td2);
        td1.setInnerHTML(myAccountMessages.totalcurrency(currency));
        kpiRequestObject.setTobePaidTitle(myAccountMessages.totalcurrency(currency));
        td2.setInnerHTML(getNumberFormatWithBigDecimal(prices.getTotalAmount() + prices.getAddonPrice()));
        kpiRequestObject.setTobePaidTotal(getNumberFormatWithBigDecimal(prices.getTotalAmount() + prices.getAddonPrice()));
        totalTableFooter.appendChild(tr);

    }

    private int getAppCount() {
        int appCount = 0;
        if (usagePlan.isAccountsModule()) {
            appCount++;
        }
        if (usagePlan.isSalesModule()) {
            appCount++;
        }
        if (usagePlan.isHumansModule()) {
            appCount++;
        }
        if (usagePlan.isProjectModule()) {
            appCount++;
        }
        if (usagePlan.isPayrollModule()) {
            appCount++;
        }
        return appCount;
    }

    private void paymentDialogBox() {
        UsagePlanPrice prices = AllPricingMaterialView.dataForSend;

        if (prices.getTotalAmount() <= 0d && prices.getAddonPrice() <= 0d) {
            Info.show(myAccountStrings.minimumRequired(), Info.Type.WARNING);
        } else {

            KpiModal dialogBox = new KpiModal(false);
            dialogBox.getContent().getElement().getStyle().setPadding(0d, Style.Unit.PX);
            dialogBox.setWidth("823px");

            PaymentButtonsPopup paymentButtonsPopup = new PaymentButtonsPopup(prices);

            paymentButtonsPopup.setPaypalCommand(param -> {
                usagePlan.setPeriodConstant(AllPricingMaterialView.type);
                dialogBox.close();
                payWithRevolut(usagePlan);
            });


            dialogBox.add(paymentButtonsPopup);
            dialogBox.open();
        }
    }

    private void payWithRevolut(UsagePlanItem usagePlanItem) {
        UsagePlanPrice prices = AllPricingMaterialView.dataForSend;
        MyAccountService.App.get().getRedirectUrlForRevolut(usagePlanItem, prices, "USD", "KPI.com", new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(String url) {
                Utils.redirect(url);
            }
        });
    }

    private void payWithPaypal(String subscriptionOperation,
                               UsagePlanItem usagePlanItem, UsagePlanPrice usagePlanPrice,
                               String currencyValue,
                               Integer subscriptionHistoryID) {

        final String description = "Full Users " + usagePlanItem.getUserCount() + (usagePlanItem.getEssUserCount() != null && usagePlanItem.getEssUserCount() > 0 ? ", ESS Users " + usagePlanItem.getEssUserCount() : "")
                + (usagePlanItem.getNonAccessUserCount() != null && usagePlanItem.getNonAccessUserCount() > 0 ? ", Non Users " + usagePlanItem.getNonAccessUserCount() : "")
                + " / " + getAppCount() + " Apps";
        String paypalURL = null;

        if (usagePlanPrice.getTotalAmount() <= 0 && usagePlanPrice.getAddonPrice() > 0) {
            //IF there user choosed only Addons without upgrade of subscription plan
            paypalURL = "https://" + Utils.getPayPalLink() + "?" +
                    cmd + "=_xclick&" +
                    business + "=" + Utils.getPayPalAccount() + "&" +
                    currency_code + "=" + currencyValue + "&" +
                    amount + "=" + getNumberFormatPaypal(usagePlanPrice.getAddonPrice()) + "&" +
                    taxX + "=0&" +
                    item_name + "=KPI.com - One Time Payment&" +
                    item_number + "=1&" +
                    a3 + "=" + getNumberFormatPaypal(usagePlanPrice.getAddonPrice()) + "&" +
                    src + "=1&" +
                    custom + "=" + usagePlanItem.getCompanyID() /*+ " "SUBSCRIPTION_ADD + usagePlanItem.getObjectID()*/ + "&" +
                    returnT + "=" + GWT.getHostPageBaseURL() + "Myaccount.html&" +
                    cancel_return + "=" + GWT.getHostPageBaseURL() + "Myaccount.html";
        } else {

            //IF Subscription plan was changed we exclude Addon prices
            if (SUBSCRIPTION_ADD.equals(subscriptionOperation)) {
                paypalURL = "https://" + Utils.getPayPalLink() + "?" +
                        cmd + "=_xclick-subscriptions&" +
                        business + "=" + Utils.getPayPalAccount() + "&" +
                        currency_code + "=" + currencyValue + "&" +
                        amount + "=" + getNumberFormatPaypal(usagePlanItem.getTotalAmount()) + "&" +
                        taxX + "=0&" +
                        item_name + "=" + description + "&" +
                        item_number + "=1&" +
                        a3 + "=" + getNumberFormatPaypal(usagePlanItem.getTotalAmount()) + "&" +
                        p3 + "=" + usagePlanItem.getUsageMonth() + "&" +
                        t3 + "=M&" +//it is daily , change to "M" - monthly
                        src + "=1&" +
                        custom + "=" + usagePlanItem.getCompanyID() + SUBSCRIPTION_ADD + usagePlanItem.getUnique_guid()/*usagePlanItem.getObjectID()*/ + "&" +
                        returnT + "=" + GWT.getHostPageBaseURL() + "Myaccount.html" + "&" +
                        cancel_return + "=" + GWT.getHostPageBaseURL() + "Myaccount.html";
            } else if (SUBSCRIPTION_UPG.equals(subscriptionOperation)) {
                paypalURL = "https://" + Utils.getPayPalLink() + "?" +
                        cmd + "=_xclick-subscriptions&" +
                        business + "=" + Utils.getPayPalAccount() + "&" +
                        currency_code + "=" + currencyValue + "&" +
                        amount + "=" + getNumberFormatPaypal(usagePlanItem.getTotalAmount() + prevUsagePlan.getTotalAmount()) + "&" +
                        taxX + "=" + getNumberFormatPaypal(usagePlanItem.getTax()) + "&" +
                        item_name + "=" + description + "&" +
                        item_number + "=2&" +
                        a3 + "=" + getNumberFormatPaypal(usagePlanItem.getTotalAmount() + prevUsagePlan.getTotalAmount()) + "&" +
                        p3 + "=" + usagePlanItem.getUsageMonth() + "&" +
                        t3 + "=M&" +
                        modify + "=2&" +
                        src + "=1&" +
                        custom + "=" + usagePlanItem.getCompanyID() + SUBSCRIPTION_UPG + subscriptionHistoryID + SUBSCRIPTION_UPG + usagePlanItem.getUnique_guid() + "&" +
                        returnT + "=" + Utils.getHostURL() + "Myaccount.html" + "&" +
                        cancel_return + "=" + Utils.getHostURL() + "Myaccount.html";
            }
        }

        Utils.redirect(paypalURL);

    }

    private void payWithStripe(String subscriptionOperation, UsagePlanItem usagePlanItem, UsagePlanPrice usagePlanPrice, String currencyValue, Integer subscriptionHistoryID) {

        StripePaymentHandler.handlePaymentButtonClicked("Custom", "KPI.com", (usagePlanItem.getTotalAmount() + usagePlanPrice.getAddonPrice()) * 100d, new StripeCompletePayment() {
            @Override
            public void completePayment(StripeCheckoutToken stripeCheckoutToken) {
                if (stripeCheckoutToken == null || stripeCheckoutToken.getId() == null) {
                    return;
                }
                LoadingPanel.loading(true);

                MyAccountService.App.get().chargeForSubscriptionPaymentWithStripeNew(subscriptionOperation,
                        usagePlanItem,
                        subscriptionHistoryID,
                        stripeCheckoutToken.getId(),
                        currencyValue,
                        usagePlanPrice.getAddonPrice(), usagePlanPriceDataToServer, new AbstractAsyncCallback<Boolean>() {

                            public void failure(Throwable caught) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.error(), Info.Type.WARNING);
                            }

                            public void success(Boolean result) {
                                LoadingPanel.loading(false);
                                if (Boolean.TRUE.equals(result)) {
                                    Info.show(myAccountStrings.paymentReceived(), Info.Type.INFO, Info.Position.BOTTOM_RIGHT, 10000);
                                    Utils.reloadPage();
                                } else {
                                    Info.show(myAccountStrings.youAreNotHavingSufficientFunds(), Info.Type.WARNING);
                                }
                            }

                        });
            }
        });
    }

    private String getNumberFormatWithBigDecimal(double value) {
        return extendedNumberFormat.format(value);
    }

    private String getNumberFormatPaypal(double value) {
        return NumberFormat.getFormat("##0.00").format(value);
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
