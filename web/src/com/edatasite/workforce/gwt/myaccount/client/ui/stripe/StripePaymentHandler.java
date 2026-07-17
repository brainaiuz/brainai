package com.edatasite.workforce.gwt.myaccount.client.ui.stripe;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;

/**
 * Created by Anvar Akramov on 9/26/17.
 */
public class StripePaymentHandler {
    private static String STRIPE_API_KEY_TEST = "pk_test_1TTzcqsxivyda68MwsgYd28g";

    public static void initializeStripe(final Callback<Void, Exception> callback) {
        if (!isInjected()) {
            ScriptInjector.fromUrl("https://checkout.stripe.com/checkout.js")
                    .setWindow(ScriptInjector.TOP_WINDOW)
                    .setCallback(new Callback<Void, Exception>() {
                        @Override
                        public void onFailure(Exception e) {
                            callback.onFailure(e);
//                            Window.alert("Failed to load stripe");
                        }

                        @Override
                        public void onSuccess(Void result) {
                            doInitializeStripe(Utils.getStripePublicKey()/*STRIPE_API_KEY_TEST*/);
                            callback.onSuccess(result);
                        }
                    }).inject();
        } else {
            GWT.log("Stripe already initialized");
        }
    }

    public static native void doInitializeStripe(String apiKey) /*-{
//        $wnd.stripecallback = function(token) {
//            console.log(token);
//            alert(token);
//        }

        $wnd.stripehandler = $wnd.StripeCheckout.configure(apiKey,
            {
                key: apiKey,
                image: 'https://workforcetrack.s3.amazonaws.com/000000000000/public/65159/7689b4d4-7d84-46e3-bf48-45935d55f8c9?AWSAccessKeyId=AKIAIROQMC77E5UKWBWQ',
                locale: 'auto'
//                ,
//                token: $wnd.stripecallback
            });
        console.log('Initialized Stripe: ');
    }-*/;

    public static native void handlePaymentButtonClicked(String name, String description, double price, StripeCompletePayment paymentHandler) /*-{

    $wnd.stripehandler.open({
            name: name,
            description: description,
            amount: price,
            token: function(response) {
                paymentHandler.@com.edatasite.workforce.gwt.myaccount.client.ui.stripe.StripeCompletePayment::completePayment(Lcom/edatasite/workforce/gwt/myaccount/client/ui/stripe/StripeCheckoutToken;)(response);
            }
        });
        console.log("handlePaymentButton done");
    }-*/;

    public static native boolean isInjected() /*-{
        return typeof $wnd.Stripe !== "undefined";
    }-*/;
}
