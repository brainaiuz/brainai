package com.edatasite.workforce.gwt.profile.client.ui;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 18.12.2008
 * Time: 17:24:50
 * To change this template use File | Settings | File Templates.
 */
public interface SettingsLogoBundle extends ClientBundle {

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/settings-image-for-employees.jpg")
    ImageResource imageForEmployees();

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/settings-image-for-pdf.jpg")
    ImageResource imageForPDF();

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/paypal.png")
    ImageResource logoForPayPal();

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/stripe.png")
    ImageResource logoForStripe();

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/google-checkout.gif")
    ImageResource logoForGoogleCheckout();

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/mastercard.gif")
    ImageResource logoForMastercard();

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/twilio.png")
    ImageResource logoTwilio();

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/asterisk.png")
    ImageResource logoAsterisk();

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/telegram.png")
    ImageResource logoTelegram();

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/helper_bot.png")
    ImageResource logoHelperBot();

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/whatsapp_logo.png")
    ImageResource whatsappLogo();

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/sipuni_logo.png")
    ImageResource logoSipuni();

    @ClientBundle.Source("com/edatasite/workforce/gwt/profile/resource/mycalls_logo.png")
    ImageResource logoMyCalls();
}
