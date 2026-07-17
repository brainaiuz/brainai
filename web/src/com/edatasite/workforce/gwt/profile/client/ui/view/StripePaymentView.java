package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankAccountLookUp;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.IntegrationItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.ui.SettingsLogoBundle;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Abror Abdukadirov
 * Date: 16.05.2019 17:34
 */
public class StripePaymentView extends CustomForm implements CustomFormConstants, Constants, AccountingConstants, CommandConstants, Colapse {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private final SettingsLogoBundle settingsLogoBundle = GWT.create(SettingsLogoBundle.class);

    private BankAccountLookUp stripeBankAccountLookUp;

    private TextBox elavonMerchant;
    private TextBox elavonUser;
    private TextBox elavonPin;
    private BankAccountLookUp elavonBankAccountLookUp;

    private FormGroup elavonMerchantField;
    private FormGroup elavonUserField;
    private FormGroup elavonPinField;
    private FormGroup elavonBankAccountLookUpField;

    private TextBox mastercardMerchantTxtBox;
    private TextBox mastercardAccessCodeTxtBox;
    private PasswordTextBox mastercardSecretKeyTxtBox;
    private BankAccountLookUp mastercardBankAccountLookUp;

    private final String stripePayment = "payment_payment_";
    private IntegrationItem item;

    public StripePaymentView() {
        super("stripePayment", "Stripe");
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        mastercardMerchantTxtBox = new TextBox();
        mastercardAccessCodeTxtBox = new TextBox();
        mastercardSecretKeyTxtBox = new PasswordTextBox();
        mastercardBankAccountLookUp = new BankAccountLookUp();

        this.drawOnlinePaymentDetailForm();

        this.show();
    }

    private void drawDisabledForm() {
        if (item.isMastercardPaymentEnabled()) {
            addTitleField(MASTERCARD_DETAILS, "Mastercard Details");
            Image mcLogo = new Image(settingsLogoBundle.logoForMastercard());
            mcLogo.setSize("60px", "28px");
            addField(MASTERCARD_MERCHANT, new AdvancedInputGroup(mastercardMerchantTxtBox, mcLogo), settingsStrings.mastercardMerchant());
            addField(MASTERCARD_CODE, mastercardAccessCodeTxtBox, settingsStrings.mastercardAccessCode());
            addField(MASTERCARD_KEY, mastercardSecretKeyTxtBox, settingsStrings.mastercardSecretKey());
            addField(MASTERCARD_BANK_ACCOUNT, mastercardBankAccountLookUp, settingsStrings.mastercardBankAccount());
        }
        if (item.isElavonPaymentEnabled()) {
            addField(EVALON_MERCHANT, elavonMerchantField);
            addField(EVALON_USER, elavonUserField);
            addField(EVALON_PIN, elavonPinField);
            addField(EVALON_BANK_ACCOUNT, elavonBankAccountLookUpField);
        }
    }

    private void drawOnlinePaymentDetailForm() {

        stripeBankAccountLookUp = new BankAccountLookUp();
        stripeBankAccountLookUp.ensureDebugId("stripeBankAccount-searchBox");

        elavonMerchant = new TextBox();
        elavonMerchant.ensureDebugId(stripePayment.concat("elavonMerchant"));
        elavonMerchantField = new FormGroup("Elavon Merchant", elavonMerchant);

        elavonUser = new TextBox();
        elavonUser.ensureDebugId(stripePayment.concat("elavonUser"));
        elavonUserField = new FormGroup("Elavon User", elavonUser);

        elavonPin = new TextBox();
        elavonPin.ensureDebugId(stripePayment.concat("elavonPin"));
        elavonUserField = new FormGroup("Elavon PIN", elavonPin);

        elavonBankAccountLookUp = new BankAccountLookUp();
        elavonBankAccountLookUpField = new FormGroup("Elavon Bank Account", elavonBankAccountLookUp);

        Image logoForPayPal = new Image(settingsLogoBundle.logoForPayPal());
        logoForPayPal.setPixelSize(100, 50);

        addTitleField(PAYMENT_GATEWAYS, settingsStrings.onlinePaymentDetails());
        addField(LOGO_PAYPAL, logoForPayPal);

        addField(STRIPE_BANK_ACCOUNT, stripeBankAccountLookUp, "Stripe bank account");
    }

    @Override
    protected void addButtons() {
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.getElement().setId("integration_setting_save_button");
        saveButton.addClickHandler(sender -> save());
        addButton(saveButton);
    }

    @Override
    protected void getDataToFillFields() {
        ProfileService.App.get().getPaymentGatewayItem(new AbstractAsyncCallback<IntegrationItem>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(IntegrationItem result) {
                item = result;
                drawDisabledForm();
                fillFormWithData();
            }
        });
    }

    private void fillFormWithData() {

        if (item.getStripeBankAccount() != null) {
            stripeBankAccountLookUp.addItem(item.getStripeBankAccount());
        }
        if (item.getMastercardMerchantID() != null) {
            mastercardMerchantTxtBox.setText(item.getMastercardMerchantID());
        }
        if (item.getMastercardAccessCode() != null) {
            mastercardAccessCodeTxtBox.setText(item.getMastercardAccessCode());
        }
        if (item.getMastercardSecretKey() != null) {
            mastercardSecretKeyTxtBox.setText(item.getMastercardSecretKey());
        }
        if (item.getMastercardBankAccount() != null) {
            mastercardBankAccountLookUp.addItem(item.getMastercardBankAccount());
        }
        if (item.getElavonMerchantID() != null) {
            elavonMerchant.setText(item.getElavonMerchantID());
        }
        if (item.getElavonUserID() != null) {
            elavonUser.setText(item.getElavonUserID());
        }
        if (item.getElavonPIN() != null) {
            elavonPin.setText(item.getElavonPIN());
        }
        if (item.getElavonBankAccount() != null) {
            elavonBankAccountLookUp.addItem(item.getElavonBankAccount());
        }

        if (item.getStripeUserId() != null && !item.getStripeUserId().trim().equals("")) {
            WfmButton2 stripeDeauthorizeButton = new WfmButton2("Deauthorize Stripe", WfmButton2.BTN_PRIMARY);
            stripeDeauthorizeButton.getElement().setId("stripe_deauth_button");
            stripeDeauthorizeButton.addClickHandler(sender -> {
                if (Utils.getStripePublicKey() != null && !Utils.getStripePublicKey().equals("") && Utils.getStripePublicKey().startsWith("pk_live_")) {
                    Utils.openURLCurrentTab(Utils.getHostURL() + "stripe-deauthorize?client_id=ca_EVfkOl28v40YPw8EPxaxnYSm7z8Os6oo");
                } else {
                    Utils.openURLCurrentTab(Utils.getHostURL() + "stripe-deauthorize?client_id=ca_EVfk8GBFFAo0x4BPqKgtpv43csnCmgBa");
                }
            });
            addField(STRIPE_PUBLIC_KEY, stripeDeauthorizeButton, "Deauthorize Stripe Account");
        } else {
            Image stripeImage = new Image(settingsLogoBundle.logoForStripe());
            stripeImage.setPixelSize(100, 50);
            stripeImage.addClickHandler(sender -> {
                if (Utils.getStripePublicKey() != null && !Utils.getStripePublicKey().equals("") && Utils.getStripePublicKey().startsWith("pk_live_")) {
                    Utils.openURLCurrentTab("https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_EVfkOl28v40YPw8EPxaxnYSm7z8Os6oo&scope=read_write&redirect_uri=" + Utils.getHostURL() + "stripe-authorize");
                } else {
                    Utils.openURLCurrentTab("https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_EVfk8GBFFAo0x4BPqKgtpv43csnCmgBa&scope=read_write&redirect_uri=" + Utils.getHostURL() + "stripe-authorize");
                }
            });
            addField(STRIPE_PUBLIC_KEY, stripeImage);
        }
    }

    private void save() {

        IntegrationItem item = new IntegrationItem();
        item.setPayPal(false);
        //Stripe Settings
        item.setStripeBankAccount(stripeBankAccountLookUp.getSelectedItem());


        //Mastercard Parameters
        item.setMastercardMerchantID(mastercardMerchantTxtBox.getText());
        item.setMastercardAccessCode(mastercardAccessCodeTxtBox.getText());
        item.setMastercardSecretKey(mastercardSecretKeyTxtBox.getText());
        item.setMastercardBankAccount(mastercardBankAccountLookUp.getSelectedItem());

        //Elavon Parameters
        item.setElavonMerchantID(elavonMerchant.getText());
        item.setElavonUserID(elavonUser.getText());
        item.setElavonPIN(elavonPin.getText());
        item.setElavonBankAccount(elavonBankAccountLookUp.getSelectedItem());

        ProfileService.App.get().saveIntegrationItem(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.integrationSettings()), Info.Type.INFO);
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.STRIPE_PAYMENT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
