package com.edatasite.workforce.gwt.myaccount.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.KpiPaymentRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.pricing.client.UserRateItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Dec 5, 2008
 * Time: 10:38:50 AM
 */
public interface MyAccountService extends RemoteService {

    Boolean sendRequestQuote(RequestQuoteItem requestQuoteItem);

    ListResult<UsagePlanItem> getUsagePlans(ListingFilterParameter fp);

    UsagePlanItem saveUsagePlan(UsagePlanItem us);

    Boolean chargeForSubscriptionPaymentWithStripeNew(String subscriptionOperation, UsagePlanItem us, Integer subscriptionHistoryID,
                                                      String stripeCheckoutToken,
                                                      String currencyValue, double totalAddOns, UsagePlanPrice usagePlanPrice);

    Boolean chargeForSubscriptionPaymentWithStripe(String subscriptionOperation, UsagePlanItem us, Integer subscriptionHistoryID,
                                                   String stripeCheckoutToken,
                                                   String currencyValue, String description);

    UsagePlanItem getUsagePlan(Integer int_usagePlanID);

    UserRateItem getUserRateAndUpgUserRatePerHost(Integer userCount, Integer upgUserCount, String hostName);

    UserRateItem getUserDiscount(Integer userCount, String hostName);

    UserRateItem getUserT(String hostName, String pricingPackageNAME, String supportPackageNAME, String modules, boolean pricingType);

    UserRateItem getUserRateAndUpgUserRatePerHostT(String hostName, String pricingPackageNAME, String supportPackageNAME, String upgPricingPackageNAME, String upgSupportPackageNAME);

    UsagePlanItem getCurrentUsagePlan();

    Integer createSubscriptionHistory(UsagePlanItem us);

    Boolean deleteSubscriptionHistory(UsagePlanItem us);

    UsagePlanItem getUsagePlanItem(Integer id);

    void sendEmailUserVisitToPage(KpiPaymentRequestObject kpiRequestObject);

    Boolean enableOrDisableFreeTrialModule(String module, boolean enable);

    String getRedirectUrlForRevolut(UsagePlanItem usagePlanItem, UsagePlanPrice prices, String currency, String description);

    class App {
        public static MyAccountServiceAsync get() {
            ServiceDefTarget target = GWT.create(MyAccountService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/myaccount");
            return (MyAccountServiceAsync) target;
        }
    }

}
