package com.edatasite.workforce.gwt.myaccount.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

public interface MyAccountStrings extends Constants {

    String aboutYourSubscriptionSecurePage();
    String acceptAllGlobal();
    String addonsDescription();
    String addonsTitle();
    String amountToBePaid();
    String chooseYourApps();
    String customPDFTemplate();
    String discountTooltip();
    String downgradeUsers();
    String downloadPDF();
    String essUsers();
    String essUsersTooltip();
    String extraStorage();
    String fullUsers();
    String fullUsersTooltip();
    String guaranteedSafeAndSecure();
    String initialSetUpPackage();
    String initialSetUpPackageTooltip();
    String minimumRequired();
    String nonUser();
    String nonUsers();
    String oneTimePayment();
    String onlineTraining();
    String orderSummary();
    String ourDedicatedStandalone();
    String pageDescription();
    String paymentHistory();
    String paymentReceived();
    String payNow();
    String payWith();
    String pleaseChooseModules();
    String premiumSupportTooltip();
    String pricingOrderConfirm();
    String recurringSubscription();
    String securePageDesc2();
    String selectUser();
    String specifyNumberOfUsers();
    String subtotalAddOn();
    String totalMonth();
    String totalYear();
    String traditionalWireTransfer();
    String upgradeNow();
    String usagePlanSummary();
    String usersApps();
    String youAreNotHavingSufficientFunds();
    String yourRequestForQuoteHasBeenReceived();
    String youSave();

    class App {
        public static MyAccountStrings get() {
            return GWT.create(MyAccountStrings.class);
        }
    }
}
