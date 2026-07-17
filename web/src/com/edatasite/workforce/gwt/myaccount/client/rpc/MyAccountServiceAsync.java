package com.edatasite.workforce.gwt.myaccount.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.KpiPaymentRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.pricing.client.UserRateItem;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Dec 5, 2008
 * Time: 10:42:01 AM
 */
public interface MyAccountServiceAsync {

    void sendRequestQuote(RequestQuoteItem requestQuoteItem, AsyncCallback<Boolean> callback);

    Request getUsagePlans(ListingFilterParameter fp, AsyncCallback<ListResult<UsagePlanItem>> async);

    void saveUsagePlan(UsagePlanItem us, AsyncCallback<UsagePlanItem> async);

    void chargeForSubscriptionPaymentWithStripeNew(String subscriptionOperation, UsagePlanItem us, Integer subscriptionHistoryID,
                                                   String stripeCheckoutToken,
                                                   String currencyValue, double totalAddOns, UsagePlanPrice usagePlanPrice, AsyncCallback<Boolean> async);

    void chargeForSubscriptionPaymentWithStripe(String subscriptionOperation, UsagePlanItem us, Integer subscriptionHistoryID,
                                                String stripeCheckoutToken,
                                                String currencyValue, String description, AsyncCallback<Boolean> async);

    void getUsagePlan(Integer int_usagePlanID, AsyncCallback<UsagePlanItem> callback);

    void getUserRateAndUpgUserRatePerHost(Integer userCount, Integer upgUserCount, String hostName, AsyncCallback<UserRateItem> callback);

    void getUserDiscount(Integer userCount, String hostName, AsyncCallback<UserRateItem> callback);

    void getUserT(String hostName, String pricingPackageNAME, String supportPackageNAME, String modules, boolean pricingType, AsyncCallback<UserRateItem> callback);

    void getUserRateAndUpgUserRatePerHostT(String hostName, String pricingPackageNAME, String supportPackageNAME, String upgPricingPackageNAME, String upgSupportPackageNAME, AsyncCallback<UserRateItem> callback);

    void getCurrentUsagePlan(AsyncCallback<UsagePlanItem> callback);

    void createSubscriptionHistory(UsagePlanItem us, AsyncCallback<Integer> async);

    void deleteSubscriptionHistory(UsagePlanItem us, AsyncCallback<Boolean> async);

    void getUsagePlanItem(Integer id, AsyncCallback<UsagePlanItem> async);

    void sendEmailUserVisitToPage(KpiPaymentRequestObject kpiRequestObject, AsyncCallback<Void> async);

    void enableOrDisableFreeTrialModule(String module, boolean enable, AsyncCallback<Boolean> async);

    void getRedirectUrlForRevolut(UsagePlanItem usagePlanItem, UsagePlanPrice prices, String currency, String description, AsyncCallback<String> async);
}
