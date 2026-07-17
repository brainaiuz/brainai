package com.edatasite.workforce.gwt.pricing.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.CoreMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.myaccount.client.rpc.MyAccountService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

//import com.edatasite.workforce.gwt.core.client.ui.popupdropdown.PopUpDropDownBox;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 12/25/11
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class PricingSubscribeView2 extends Composite {

    
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final CoreMessages coreMessages = CoreMessages.App.get();
    private NumberFormat numberFormat;
    private boolean isUKClient = false;

    SpanElement headerPricingPage;
    Anchor features;
    Anchor comparePrices;
    Anchor prices;

    LabelElement helpContentMessage;

    @UiField
    SpanElement iWantToUseKPIDOTCOM;
    @UiField
    DataListBox usersSelectItem;
    @UiField
    SpanElement selectedUSERS;
    @UiField
    TableCellElement oneMonth;
    @UiField
    TableCellElement threeMonth;
    @UiField
    TableCellElement sixMonth;
    @UiField
    TableCellElement oneYear;
    @UiField
    TableCellElement priceAfterDiscount;
    @UiField
    TableCellElement pricePerUserMonth;
    @UiField
    TableCellElement totalAmountPerUser;
    @UiField
    TableCellElement discount;
    @UiField
    HeadingElement whatInMyPackage;
    @UiField
    HTML packageProjectManagement;
    @UiField
    HTML packageHRMS;
    @UiField
    HTML packageCRM;
    @UiField
    HTML packageAccountingAndFinance;
    @UiField
    HTML packagePayroll;
    @UiField
    HTML packageDocuments;
    @UiField
    HTML packageDashboard;
    //taxCheck
    @UiField
    HTML forUKBasedVAT;
    @UiField
    KpiCheckBox taxCheck;
    //Buy Now Buttons
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
    HTML priceAfterDiscountPerUserOneMonth;
    @UiField
    HTML priceAfterDiscountPerUserThreeMonth;
    @UiField
    HTML priceAfterDiscountPerUserSixMonth;
    @UiField
    HTML priceAfterDiscountPerUserOneYear;
    //
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
    HTML pricePerUserMonthOneMonth;
    @UiField
    HTML pricePerUserMonthThreeMonth;
    @UiField
    HTML pricePerUserMonthSixMonth;
    @UiField
    HTML pricePerUserMonthOneYear;
    //
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
    HTML discountPercentPerOneMonth;
    @UiField
    HTML discountPercentPerThreeMonth;
    @UiField
    HTML discountPercentPerSixMonth;
    @UiField
    HTML discountPercentPerOneYear;
    //try now for free trial help
    @UiField
    LabelElement youCanTasteKPIDOTCOM;
    //	@UiField LabelElement letUsShowYouWhatKPIDOTCOM;
    @UiField
    AnchorElement tryNOW;
//	@UiField AnchorElement scheduleADEMO;
    //

    interface PricingSubscribeView2UiBinder extends UiBinder<HTMLPanel, PricingSubscribeView2> {
    }

    public PricingSubscribeView2() {
        PricingSubscribeView2UiBinder ourUiBinder = GWT.create(PricingSubscribeView2UiBinder.class);
        initWidget(ourUiBinder.createAndBindUi(this));
        init();
    }

    private void calculateAll(DataListBox userBox) {
        if (Utils.getHostURL().contains(".ru")) {
            forUKBasedVAT.setVisible(false);
            taxCheck.setVisible(false);
        } else {
            forUKBasedVAT.setVisible(true);
            taxCheck.setVisible(true);
        }


        final boolean currencyGBR = isUKClient;
        // get user count
        final Integer userCount;
        try {
            userCount = Integer.valueOf(userBox.getSelectedItem().getName());
        } catch (NumberFormatException e) {
            return;
        }
        final boolean includeTax = taxCheck.getValue();
//		final float userRate = (float) PayPalCalculationHelper.getUserRate2(userCount);
        String hostName = Constants.HOST_LIVE;//default UK based VAT RATE
        if (DOM.getElementById("hostName") != null) {
            hostName = DOM.getElementProperty(DOM.getElementById("hostName"), "value");
        }
        MyAccountService.App.get().getUserDiscount(userCount, hostName, new AbstractAsyncCallback<UserRateItem>() {
            @Override
            public void success(UserRateItem result) {
                if (reCalculateUserRATE != null) {
                    reCalculateUserRATE.getCalculatedUserRATE(result);
                }
            }
        });
        this.reCalculateUserRATE = userRateItem -> {
            maxPayableUserCount = userRateItem.getMaxPayableUserCount();
            reCalculateAfterCalculateUserRate(currencyGBR, userCount, includeTax, userRateItem);
        };
    }

    private Integer maxPayableUserCount;
    private reCalculateUserRATE reCalculateUserRATE;

    private interface reCalculateUserRATE {
        void getCalculatedUserRATE(UserRateItem userRATEItem);
    }

    private void reCalculateAfterCalculateUserRate(boolean currencyGBR, Integer userCount, boolean includeTax, UserRateItem userRateItem) {
        String vatRateN = "0.20";//default UK based VAT RATE
        if (DOM.getElementById("vatN") != null) {
            vatRateN = DOM.getElementProperty(DOM.getElementById("vatN"), "value");
        }
        String hostName = Constants.HOST_LIVE;//default UK based VAT RATE
        if (DOM.getElementById("hostName") != null) {
            hostName = DOM.getElementProperty(DOM.getElementById("hostName"), "value");
        }

        final double userRate = userRateItem.getUserRate();
        final double[][] discount_per_monthly2 = {{userRateItem.getDiscountOneMonth(), userRateItem.getDiscountThreeMonth(),
                userRateItem.getDiscountSixMonth(), userRateItem.getDiscountTwentyMonth()}}; // Example: (1 month - not discount), 3 month - 15%, 6 month - 25%, 12 month - 30%;

        final UsagePlanItem planItem = PayPalCalculationHelper.calculateUsagePlan2(userCount, currencyGBR, includeTax, 0, userRate, hostName, Double.valueOf(vatRateN), discount_per_monthly2);
        // show total in view
        String includeVAT = "";
        if (includeTax) {
            includeVAT = "<br/><em style='font-size:9px;color:gray;'> (" + wfmStrings.vatIncluded() + ")</em>";    //#007dc3
        }
        final String UKorUSD = Utils.getHTMLCODESForCurrency(currencyGBR ? "GBP" : getCurrencyCODE());

        //discount percent calculate per user to three month
        if ((discount_per_monthly2[0][0] * 100) > 0) {
            discountPercentPerOneMonth.setHTML((int) (discount_per_monthly2[0][0] * 100) + "%");//percent for one month
        } else {
            discountPercentPerOneMonth.setHTML("");
        }
        if (hostName.contains("smebu.com") || hostName.contains("tjilo.com") || hostName.contains("localhost")) {
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
            return;
        } else {
            if (userCount >= 1 && userCount <= 5) {
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
            } else {
                if (userCount >= 6 && userCount <= /*30*/userRateItem.getMaxPayableUserCount()) {
                    priceAfterDiscountPerUserOneMonth.setHTML(UKorUSD + numberFormat.format(planItem.getTotalAmountsMonthly()[0] / planItem.getUserCount() / planItem.getUsageMonths()[0]) + perUserSpan + includeVAT);
                    //total price after discount per user
                    totalPricePerUserOneMonth.setHTML(UKorUSD + numberFormat.format(planItem.getTotalAmountsMonthly()[0]) + totalSpan + includeVAT);
                    //price per user
                    pricePerUserMonthOneMonth.setHTML(UKorUSD + numberFormat.format(planItem.getUserRate()));
                    //total amount
                    totalAmountTotalUserPerOneMonth.setHTML(UKorUSD + numberFormat.format(planItem.getUserRate() * planItem.getUserCount() * planItem.getUsageMonths()[0]));
                    //register disabled option
                    registerDisabledOptionForBetween6to30();
                } else {
                    if (userCount >= /*31*/userRateItem.getMaxPayableUserCount() + 1) {
                        //register disabled option
                        registerDisabledOptionForGreater31();
                    }
                }
            }
        }
        //price after discount per user
        if (userCount >= 1 && userCount <= /*30*/userRateItem.getMaxPayableUserCount()) {
            priceAfterDiscountPerUserThreeMonth.setHTML(UKorUSD + numberFormat.format(planItem.getTotalAmountsMonthly()[1] / planItem.getUserCount() / planItem.getUsageMonths()[1]) + perUserSpan + includeVAT);
            priceAfterDiscountPerUserSixMonth.setHTML(UKorUSD + numberFormat.format(planItem.getTotalAmountsMonthly()[2] / planItem.getUserCount() / planItem.getUsageMonths()[2]) + perUserSpan + includeVAT);
            priceAfterDiscountPerUserOneYear.setHTML(UKorUSD + numberFormat.format(planItem.getTotalAmountsMonthly()[3] / planItem.getUserCount() / planItem.getUsageMonths()[3]) + perUserSpan + includeVAT);
            //total price after discount per user
            totalPricePerUserThreeMonth.setHTML(UKorUSD + numberFormat.format(planItem.getTotalAmountsMonthly()[1]) + totalSpan + includeVAT);
            totalPricePerUserSixMonth.setHTML(UKorUSD + numberFormat.format(planItem.getTotalAmountsMonthly()[2]) + totalSpan + includeVAT);
            totalPricePerUserOneYear.setHTML(UKorUSD + numberFormat.format(planItem.getTotalAmountsMonthly()[3]) + totalSpan + includeVAT);
            //price per user
            pricePerUserMonthThreeMonth.setHTML(UKorUSD + numberFormat.format(planItem.getUserRate()));
            pricePerUserMonthSixMonth.setHTML(UKorUSD + numberFormat.format(planItem.getUserRate()));
            pricePerUserMonthOneYear.setHTML(UKorUSD + numberFormat.format(planItem.getUserRate()));
            //total amount
            totalAmountTotalUserPerThreeMonth.setHTML(UKorUSD + numberFormat.format(planItem.getUserRate() * planItem.getUserCount() * planItem.getUsageMonths()[1]));
            totalAmountTotalUserPerSixMonth.setHTML(UKorUSD + numberFormat.format(planItem.getUserRate() * planItem.getUserCount() * planItem.getUsageMonths()[2]));
            totalAmountTotalUserPerOneYear.setHTML(UKorUSD + numberFormat.format(planItem.getUserRate() * planItem.getUserCount() * planItem.getUsageMonths()[3]));
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
    }

    private String gettrialDate() {
        String trialDate = "7";
        if (DOM.getElementById("freeTrialDays") != null) {
            trialDate = DOM.getElementProperty(DOM.getElementById("freeTrialDays"), "value");
        }
        return trialDate;
    }

    private String getCurrencyCODE() {
        String currencyCODE = "USD";
        if (DOM.getElementById("currencyCODE") != null) {
            currencyCODE = DOM.getElementProperty(DOM.getElementById("currencyCODE"), "value");
        }
        return currencyCODE;
    }

    private void init() {
        //check for UK client
        String isUk = null;
        if (DOM.getElementById("isukclient") != null) {
            isUk = DOM.getElementProperty(DOM.getElementById("isukclient"), "value");
        }
        if ("true".equals(isUk)) {
            isUKClient = true;
        }

        numberFormat = NumberFormat.getFormat("#.##");

        String productName = null;
        if (DOM.getElementById("productname") != null) {
            productName = DOM.getElementProperty(DOM.getElementById("productname"), "value");
        }
        iWantToUseKPIDOTCOM.setInnerHTML(wfmStrings.iWantToUseKPIDOTCOM1() + " " + (Utils.getHelpHost() != null ? Utils.getHelpHost() : productName) + " " + wfmStrings.iWantToUseKPIDOTCOM2());
        registerUsersSelectItem(usersSelectItem);
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

        //my package
        whatInMyPackage.setInnerHTML(wfmStrings.whatInMyPackage());
        packageProjectManagement.setHTML(wfmStrings.projects());
        packageHRMS.setHTML(wfmStrings.hrms());
        packageCRM.setHTML(wfmStrings.crm());
        packageAccountingAndFinance.setHTML(wfmStrings.accounts());
        packagePayroll.setHTML(wfmStrings.payroll());
        packageDocuments.setHTML(wfmStrings.documents());
        packageDashboard.setHTML(wfmStrings.dashboard());

        //tax check
        forUKBasedVAT.setHTML(wfmStrings.forUKBased());
        taxCheck.addClickHandler(sender -> calculateAll(usersSelectItem/*popupDropDown.getSelectBox()*/));
        taxCheck.setValue(isUKClient);
        //buy now buttons
        registerBuyNowButtonListener(buyNowOneMonth, 0);   //0 -> 1 month
        registerBuyNowButtonListener(buyNowThreeMonth, 1); //1 -> 3 month
        registerBuyNowButtonListener(buyNowSixMonth, 2);   //2 -> 6 month
        registerBuyNowButtonListener(buyNowOneYear, 3);    //3 -> 12 month/1 year

        //discount percent
        discountPercentPerOneMonth.setHTML("");
        //free trial sign up help messages

        youCanTasteKPIDOTCOM.setInnerHTML(coreMessages.youCanTasteKPIDOTCOM(gettrialDate()));
        registerTryNowFreeTrialSignUp(tryNOW);
    }

    private SelectItem[] getUsersSelectItem(int from, int to) {
        int count = to - from;
        SelectItem[] items = new SelectItem[count];
        for (int i = 0; i < count; i++) {
            items[i] = new SelectItem(i, String.valueOf(i + 1 + from));
        }
        return items;
    }

    private void registerAnchorClickListener(Anchor anchor, String anchorText, final String action) {
        anchor.setHTML(anchorText);
        anchor.addClickHandler(event -> {
            //register action handler
        });
    }

    private void registerBuyNowButtonListener(final Button button, final int periodMonth) {
        button.setSize("110px", "37px");
        button.addClickHandler(event -> {
            if (Integer.valueOf(usersSelectItem.getSelectedItem().getName()/*popupDropDown.getSelectBox().getSelectedItem().getName()*/) >= /*31*/maxPayableUserCount) {
                sendRequestQuoteMessage(usersSelectItem/*popupDropDown.getSelectBox()*/, periodMonth);
            } else {
                sentToPaymentServlet("", usersSelectItem/*popupDropDown.getSelectBox()*/, periodMonth);
            }
        });
    }

    private void registerDisabledOptionForBetween1to5() {
        //
        buyNowOneMonth.setEnabled(false);

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
        buyNowOneMonth.setHTML(wfmStrings.buyNow());
        buyNowThreeMonth.setHTML(wfmStrings.buyNow());
        buyNowSixMonth.setHTML(wfmStrings.buyNow());
        buyNowOneYear.setHTML(wfmStrings.buyNow());
    }

    private void registerDisabledOptionForBetween6to30() {
        buyNowOneMonth.setEnabled(true);

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
        buyNowOneMonth.setHTML(wfmStrings.buyNow());
        buyNowThreeMonth.setHTML(wfmStrings.buyNow());
        buyNowSixMonth.setHTML(wfmStrings.buyNow());
        buyNowOneYear.setHTML(wfmStrings.buyNow());
    }

    private void registerDisabledOptionForGreater31() {
        buyNowOneMonth.setEnabled(true);

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

    private void registerTryNowFreeTrialSignUp(AnchorElement tryNOW) {
        boolean equals14Days = "14".equals(gettrialDate());
        String s = coreMessages.tryNowEMmarkNoteNoCreditCardsRequired(gettrialDate(), equals14Days ? "14" : "");
        if (!"7".equals(gettrialDate()) && !equals14Days) {
            s = s.split("<em")[0];
        }
        tryNOW.setInnerHTML(s);
        DOM.sinkEvents(tryNOW.cast(), Event.ONCLICK);
        DOM.setEventListener(tryNOW.cast(), event -> {
            if (event.getTypeInt() == Event.ONCLICK) {
                sendToFreeTrialServlet(usersSelectItem/*popupDropDown.getSelectBox()*/);
            }
        });
    }

    private void registerUsersSelectItem(final DataListBox usersListBox) {
        usersListBox.setNullLabel("0");
        usersListBox.setWithoutNullLabel(true);
        usersListBox.setItems(getUsersSelectItem(0, 100));
        usersListBox.setSelected(/*0*/3);
        usersListBox.addValueChangeHandler(event -> {
            //register change listener
            calculateAll(usersListBox);
        });
        calculateAll(usersListBox);
    }

    private void sentToPaymentServlet(String type, DataListBox userBox, int usagePeriodID) {
        // no calculation
        Integer userCount;
        try {
            userCount = Integer.valueOf(userBox.getSelectedItem().getName());
        } catch (NumberFormatException e) {
            Info.show(wfmStrings.pleaseSelectValue(), Info.Type.WARNING);
            return;
        }
        if (userCount == 0) {
            Info.show(wfmStrings.pleaseSelectValue(), Info.Type.WARNING);
            return;
        }

        String buffer = GWT.getHostPageBaseURL() + "signup.html?" +
                "users=" +
                userCount +
                "&storage=1" +  // storage 5
                "&service=pm" +
                "&type=true" +
                "&isGBP=" +
                isUKClient +
                "&category=" +
                type +
                "&tax=" +
                taxCheck.getValue() +
                "&usagePeriodID=" +
                usagePeriodID;

        Utils.redirect(buffer);
    }

    private void sendRequestQuoteMessage(DataListBox userBox, int month) {
        // no calculation
        Integer userCount;
        try {
            userCount = Integer.valueOf(userBox.getSelectedItem().getName());
        } catch (NumberFormatException e) {
            Info.show(wfmStrings.pleaseSelectValue(), Info.Type.WARNING);
            return;
        }
        if (userCount == 0) {
            Info.show(wfmStrings.pleaseSelectValue(), Info.Type.WARNING);
            return;
        }
        //send request e quote !!!
        sendToFreeTrialServlet(userBox, month);
    }

    private void sendToFreeTrialServlet(DataListBox userBox) {
        sendToFreeTrialServlet(userBox, null);
    }

    private void sendToFreeTrialServlet(DataListBox userBox, Integer usagePeriodID) {
        // no calculation
        Integer userCount;
        try {
            userCount = Integer.valueOf(userBox.getSelectedItem().getName());
        } catch (NumberFormatException e) {
            Info.show(wfmStrings.pleaseSelectValue(), Info.Type.WARNING);
            return;
        }
        if (userCount == 0) {
            Info.show(wfmStrings.pleaseSelectValue(), Info.Type.WARNING);
            return;
        }
        StringBuilder buffer = new StringBuilder(GWT.getHostPageBaseURL());
        buffer.append("signup/freeSignup.html?");
        buffer.append("users=");
        buffer.append(userCount);
        buffer.append("&tax=");
        buffer.append(taxCheck.getValue());
        if (usagePeriodID != null) {
            buffer.append("&usagePeriodID=");
            buffer.append(usagePeriodID);
        }

        Utils.redirect(buffer.toString());
    }
}