package com.edatasite.workforce.gwt.myaccount.server.app;

import com.edatasite.workforce.core.domain.EdsSubscriptionPayment;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsWorldPayHistory;
import com.edatasite.workforce.gwt.core.client.enums.PaymentTypeEnum;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.stripe.model.Invoice;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 25.08.2010
 * Time: 14:24:46
 */
public interface MyAccountServiceLocal {

    UsagePlanItem getCurrentUsagePlan();

    UsagePlanItem getParametr(EdsUsagePlan usagePlan);

    String payFromPaypal(EdsSubscriptionPayment newSubscriptionPayment, String mes) throws ParseException;

    String payFromWorld(EdsWorldPayHistory domain, String mes) throws ParseException;

    void sendPayPalNotification(String mes, String subject);

    void sendWorldPayNotification(String mes, String subject);

    Integer createSubscriptionHistory(UsagePlanItem us);

    UsagePlanItem getCompanyLastUsagePlan(Integer companyID);

    void updateCompanyLastUsagePlan(UsagePlanItem item);

    UsagePlanItem usagePlanSaveAndGet(UsagePlanItem usagePlan);

    void stripeSubscriptionInvoicePaid(Invoice invoiceLineItem);

    void sendPaidStripeWebhookNotification(Map<String, String> invoiceLineItem, PaymentTypeEnum paymentTypeEnum);

}
