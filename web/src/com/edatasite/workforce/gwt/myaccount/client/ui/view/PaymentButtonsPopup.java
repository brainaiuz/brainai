package com.edatasite.workforce.gwt.myaccount.client.ui.view;

import com.edatasite.workforce.gwt.core.client.NumberUtils;
import com.edatasite.workforce.gwt.core.client.enums.PaymentTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.CustomCommand;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountMessages;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountStrings;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanPrice;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialRadioButton;
import gwt.material.design.client.ui.html.Text;

/**
 * Created by Anvar Akramov on 16.08.18.
 */
public class PaymentButtonsPopup extends Composite {

    private static final MyAccountStrings myAccountStrings = MyAccountStrings.App.get();
    private static final MyAccountMessages myAccountMessages = MyAccountMessages.App.get();
    private static PaymentButtonsPopupUiBinder ourUiBinder = GWT.create(PaymentButtonsPopupUiBinder.class);

    @UiField
    DivElement payWithLabel;
    @UiField
    DivElement shortDescLabel;
    /*@UiField
    MaterialRadioButton radioStripe;*/
    @UiField
    MaterialRadioButton radioPaypal;
    //    @UiField
//    MaterialRadioButton radioWire;
    @UiField
    HTMLPanel rightPanelWrapper;
    /*@UiField
    ParagraphElement stripeShortDesc;
    @UiField
    DivElement stripeToBePaid;
    @UiField
    DivElement stripeToBePaidLabel;
    @UiField
    MaterialLink stripePayLink;
    @UiField
    Text stripeGuarantedLabel;*/
    @UiField
    ParagraphElement paypalShortDesc;
    @UiField
    DivElement paypalToBePaid;
    @UiField
    DivElement paypalToBePaidLabel;
    @UiField
    MaterialLink paypalPayLink;
    @UiField
    Text paypalGuarantedLabel;
//    @UiField
//    ParagraphElement wireShortDesc;
//    @UiField
//    DivElement wireToBePaid;
//    @UiField
//    DivElement wireToBePaidLabel;
//    @UiField
//    MaterialLink wirePayLink;
//    @UiField
//    Text wireGuarantedLabel;
//    @UiField
//    Text wireDownloadPDFLabel;

    private CustomCommand<Object> stripeCommand;
    private CustomCommand<Object> paypalCommand;
    private CustomCommand<Object> revolutlCommand;
    private CustomCommand<Object> wireCommand;
    private UsagePlanPrice prices;

    public PaymentButtonsPopup(UsagePlanPrice prices) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.prices = prices;
        init();
    }

    private void init() {
        payWithLabel.setInnerText(myAccountStrings.payWith());
        shortDescLabel.setInnerText(myAccountStrings.acceptAllGlobal());
        /*stripeShortDesc.setInnerHTML(myAccountMessages.clickPayNowToProceed("Stripe","Stripe"));
        stripeToBePaidLabel.setInnerHTML(myAccountStrings.amountToBePaid());
        stripePayLink.setText(myAccountStrings.payNow());
        stripeGuarantedLabel.setText(myAccountStrings.guaranteedSafeAndSecure());*/
        paypalShortDesc.setInnerHTML(myAccountMessages.clickPayNowToProceed("Revolut", "Revolut"));
        paypalToBePaidLabel.setInnerHTML(myAccountStrings.amountToBePaid());
        paypalGuarantedLabel.setText(myAccountStrings.guaranteedSafeAndSecure());
        paypalPayLink.setText(myAccountStrings.payNow());
//        wireShortDesc.setInnerHTML(myAccountStrings.traditionalWireTransfer());
//        wireToBePaidLabel.setInnerHTML(myAccountStrings.amountToBePaid());
//        wireGuarantedLabel.setText(myAccountStrings.guaranteedSafeAndSecure());
//        wireDownloadPDFLabel.setText(myAccountStrings.downloadPDF());

        /*radioStripe.setText(myAccountStrings.creditCard());
        radioStripe.addClickHandler(e -> rightPanelWrapper.setStyleName("payment-gateway-panel__side payment-gateway-panel__side--stripe"));*/

        rightPanelWrapper.setStyleName("payment-gateway-panel__side payment-gateway-panel__side--paypal");
//        radioWire.addClickHandler(e -> rightPanelWrapper.setStyleName("payment-gateway-panel__side payment-gateway-panel__side--wire"));
        /*stripePayLink.addClickHandler(e -> {
            if (stripeCommand != null) {
                stripeCommand.execute(PaymentTypeEnum.STRIPE.getCode());
            }
        });*/
        paypalPayLink.addClickHandler(e -> {
            if (paypalCommand != null) {
                paypalCommand.execute(PaymentTypeEnum.REVOLUT.getCode());
            }
        });
//        wirePayLink.addClickHandler(e -> {
//            if (wireCommand != null) {
//                wireCommand.execute(PaymentTypeEnum.WIRE.getCode());
//            }
//        });
        if (prices != null) {
//            stripeToBePaid.setInnerHTML("$&thinsp;" + NumberUtils.getNumberFormatWithBigDecimal(prices.getTotalAmount() + prices.getAddonPrice()));
            if (prices.getTotalAmount() <= 0 && prices.getAddonPrice() > 0) {
                paypalToBePaid.setInnerHTML("$&thinsp;" + NumberUtils.getNumberFormatWithBigDecimal(prices.getAddonPrice() /*+ prices.getAddonPrice()*/));
            } else {
                paypalToBePaid.setInnerHTML("$&thinsp;" + NumberUtils.getNumberFormatWithBigDecimal(prices.getTotalAmount() + prices.getAddonPrice() /*+ prices.getAddonPrice()*/));
            }
//            wireToBePaid.setInnerHTML("$&thinsp;" + NumberUtils.getNumberFormatWithBigDecimal(prices.getTotalAmount() + prices.getAddonPrice()));
        }
    }

    public void setStripeCommand(CustomCommand stripeCommand) {
        this.stripeCommand = stripeCommand;
    }

    public void setPaypalCommand(CustomCommand paypalCommand) {
        this.paypalCommand = paypalCommand;
    }

    public void setWireCommand(CustomCommand wireCommand) {
        this.wireCommand = wireCommand;
    }

    public interface PricingStyle extends ClientBundle {
        @CssResource.NotStrict
        @Source("AllPricingView.css")
        CssResource markupPricing();
    }

    interface PaymentButtonsPopupUiBinder extends UiBinder<HTMLPanel, PaymentButtonsPopup> {
    }
}
