package com.edatasite.workforce.gwt.myaccount.client.ui.stripe;

/**
 * Created by Anvar Akramov on 9/26/17.
 */
public interface StripeCompletePayment {
    void completePayment(StripeCheckoutToken stripeCheckoutToken);
}
