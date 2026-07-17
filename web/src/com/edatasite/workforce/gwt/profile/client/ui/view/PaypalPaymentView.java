package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankAccountLookUp;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
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

import java.math.BigDecimal;
import java.util.Optional;

/**
 * User: Abror Abdukadirov
 * Date: 16.05.2019 17:34
 */
public class PaypalPaymentView extends CustomForm implements CustomFormConstants, Constants, AccountingConstants, CommandConstants, Colapse {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private final SettingsLogoBundle settingsLogoBundle = GWT.create(SettingsLogoBundle.class);

    private TextBox payPalMerchantTxtBox;
    private BankAccountLookUp paypalBankAccountLookUp;

    private BankAccountLookUp stripeBankAccountLookUp;

    private TextBox elavonMerchant;
    private TextBox elavonUser;
    private TextBox elavonPin;
    private BankAccountLookUp elavonBankAccountLookUp;

    private TextBox stripeElavonMerchant;
    private TextBox stripeElavonUser;
    private TextBox stripeElavonPin;
    private BankAccountLookUp stripeElavonBankAccountLookUp;

    private FormGroup elavonMerchantField;
    private FormGroup elavonUserField;
    private FormGroup elavonPinField;
    private FormGroup elavonBankAccountLookUpField;

    private FormGroup stripeElavonMerchantField;
    private FormGroup stripeElavonUserField;
    private FormGroup stripeElavonPinField;
    private FormGroup stripeElavonBankAccountLookUpField;

    private TextBox mastercardMerchantTxtBox;
    private TextBox mastercardAccessCodeTxtBox;
    private PasswordTextBox mastercardSecretKeyTxtBox;
    private BankAccountLookUp mastercardBankAccountLookUp;

    private BankAccountLookUp paymeBankAccountLookUp;
    private AccountsLookUp paymeExpenseAccountLookUp;
    private TextBox paymeMerchantIdBox;
    private TextBox paymeServiceFeeBox;
    private TextBox paymeServiceIdBox;

    private final String paypalPayment = "paypal_payment_";
    private final String stripePayment = "payment_payment_";

    private BankAccountLookUp revolutBankAccountLookUp;
    private AccountsLookUp revolutExpenseAccountLookUp;
    private TextBox revolutEmail;
    private TextBox revolutSecretApiKey;

    private TextBox clickMerchantIdBox;
    private BankAccountLookUp clickBankAccountLookUp;
    private TextBox clickServiceIdBox;

    private IntegrationItem item;

    public PaypalPaymentView() {
        super("paypalPaymentAndStripePayment", settingsStrings.onlinePaymentDetails());
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

            addField(STRIPE_EVALON_MERCHANT, stripeElavonMerchantField);
            addField(STRIPE_EVALON_USER, stripeElavonUserField);
            addField(STRIPE_EVALON_PIN, stripeElavonPinField);
            addField(STRIPE_EVALON_BANK_ACCOUNT, stripeElavonBankAccountLookUpField);
        }
    }

    private void drawOnlinePaymentDetailForm() {
        payPalMerchantTxtBox = new TextBox();
        payPalMerchantTxtBox.ensureDebugId(paypalPayment.concat("payPalMerchantTxtBox"));

        paypalBankAccountLookUp = new BankAccountLookUp();
        paypalBankAccountLookUp.ensureDebugId("paypalBankAccount-searchBox");

        elavonMerchant = new TextBox();
        elavonMerchant.ensureDebugId(paypalPayment.concat("elavonMerchant"));
        elavonMerchantField = new FormGroup("Elavon Merchant", elavonMerchant);

        elavonUser = new TextBox();
        elavonUser.ensureDebugId(paypalPayment.concat("elavonUser"));
        elavonUserField = new FormGroup("Elavon User", elavonUser);

        elavonPin = new TextBox();
        elavonPin.ensureDebugId(paypalPayment.concat("elavonPin"));
        elavonUserField = new FormGroup("Elavon PIN", elavonPin);

        elavonBankAccountLookUp = new BankAccountLookUp();
        elavonBankAccountLookUpField = new FormGroup("Elavon Bank Account", elavonBankAccountLookUp);

        Image logoForPayPal = new Image(settingsLogoBundle.logoForPayPal());
        logoForPayPal.setPixelSize(100, 50);

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(sender -> save(true));

        addTitleField(PAYMENT_GATEWAYS, settingsStrings.onlinePaymentDetails());
        addField(PAYPAL_ACCOUNT, payPalMerchantTxtBox, settingsStrings.enterPayPalAccount());
        addField(PAYPAL_BANK_ACCOUNT, paypalBankAccountLookUp, settingsStrings.paypalBankAccount());
        addField(PAYPAL_SAVE_BUTTON, saveButton);

        stripeBankAccountLookUp = new BankAccountLookUp();
        stripeBankAccountLookUp.ensureDebugId("stripeBankAccount-searchBox");

        stripeElavonMerchant = new TextBox();
        stripeElavonMerchant.ensureDebugId(stripePayment.concat("elavonMerchant"));
        stripeElavonMerchantField = new FormGroup("Elavon Merchant", stripeElavonMerchant);

        stripeElavonUser = new TextBox();
        stripeElavonUser.ensureDebugId(stripePayment.concat("elavonUser"));
        stripeElavonUserField = new FormGroup("Elavon User", stripeElavonUser);

        stripeElavonPin = new TextBox();
        stripeElavonPin.ensureDebugId(stripePayment.concat("elavonPin"));
        stripeElavonUserField = new FormGroup("Elavon PIN", stripeElavonPin);

        stripeElavonBankAccountLookUp = new BankAccountLookUp();
        stripeElavonBankAccountLookUpField = new FormGroup("Elavon Bank Account", stripeElavonBankAccountLookUp);

        WfmButton2 stripeSaveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        stripeSaveButton.addClickHandler(sender -> save(false));

        WfmButton2 stripeSetUpButton = new WfmButton2(wfmStrings.setUp(), WfmButton2.BTN_PRIMARY);
        stripeSetUpButton.addClickHandler(sender -> {
            if (Utils.getStripePublicKey() != null && !Utils.getStripePublicKey().equals("") && Utils.getStripePublicKey().startsWith("pk_live_")) {
                Utils.openURLCurrentTab("https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_EVfkOl28v40YPw8EPxaxnYSm7z8Os6oo&scope=read_write&redirect_uri=" + Utils.getHostURL() + "stripe-authorize");
            } else {
                Utils.openURLCurrentTab("https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_EVfk8GBFFAo0x4BPqKgtpv43csnCmgBa&scope=read_write&redirect_uri=" + Utils.getHostURL() + "stripe-authorize");
            }
        });

        addField(STRIPE_BANK_ACCOUNT, stripeBankAccountLookUp, "Stripe bank account");
        addField(STRIPE_SETUP_BUTTON, stripeSetUpButton);
        addField(STRIPE_SAVE_BUTTON, stripeSaveButton);

        paymeMerchantIdBox = new TextBox();
        paymeBankAccountLookUp = new BankAccountLookUp();
        paymeServiceIdBox = new TextBox();
        paymeServiceFeeBox = new TextBox();
        paymeExpenseAccountLookUp = new AccountsLookUp(Constants.EXPENSES);
        addField(PAYME_MERCHANT_ID, new FormGroup(wfmStrings.merchantId(), paymeMerchantIdBox));
        addField(PAYME_BANK_ACCOUNT, new FormGroup(wfmStrings.bankAccount(), paymeBankAccountLookUp));
        addField(PAYME_EXPENSE_BANK_ACCOUNT, new FormGroup("Expense Account", paymeExpenseAccountLookUp));
        addField(PAYME_SERVICE_ID, new FormGroup(wfmStrings.key(), paymeServiceIdBox));
        addField(PAYME_SERVICE_FEE, new FormGroup(accountingStrings.bankFeeDiscount()+"(%)", paymeServiceFeeBox));
        addField(PAYME_SAVE_BUTTON, new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (validatePayme()) {
                save(false);
            }
        }));

        clickMerchantIdBox = new TextBox();
        clickBankAccountLookUp = new BankAccountLookUp();
        clickServiceIdBox = new TextBox();
        addField(CLICK_MERCHANT_ID, new FormGroup(wfmStrings.merchantId(), clickMerchantIdBox));
        addField(CLICK_BANK_ACCOUNT, new FormGroup(wfmStrings.bankAccount(), clickBankAccountLookUp));
        addField(CLICK_SERVICE_ID, new FormGroup(wfmStrings.service(), clickServiceIdBox));
        addField(CLICK_SAVE_BUTTON, new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (validateClick()) {
                save(false);
            }
        }));

        revolutSecretApiKey = new TextBox();
        revolutEmail = new TextBox();
        revolutBankAccountLookUp = new BankAccountLookUp();
        revolutExpenseAccountLookUp = new AccountsLookUp(Constants.EXPENSES);
        addField(REVOLUT_EMAIL, new FormGroup("Revolut Email", revolutEmail));
        addField(REVOLUT_BANK_ACCOUNT, new FormGroup(wfmStrings.bankAccount(), revolutBankAccountLookUp));
        addField(REVOLUT_EXPENSE_BANK_ACCOUNT, new FormGroup("Expense Account", revolutExpenseAccountLookUp));
        addField(REVOLUT_SECRET_API_KEY, new FormGroup("Secret API Key", revolutSecretApiKey));
        addField(REVOLUT_SAVE_BUTTON, new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, revolutEvent -> {
            if (validateRevolut()) {
                save(false);
            }
        }));


    }

    @Override
    protected void addButtons() {
//        WfmButton2 saveButton = new WfmButton2(settingsStrings.save(), WfmButton2.BTN_PRIMARY);
//        saveButton.getElement().setId("integration_setting_save_button");
//        saveButton.addClickHandler(sender -> save());
//        addButton(saveButton);
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
        if (item.getPayPalMerchant() != null) {
            payPalMerchantTxtBox.setText(item.getPayPalMerchant());
        }
        if (item.getPayPalBankAccount() != null) {
            paypalBankAccountLookUp.addItem(item.getPayPalBankAccount());
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

        if (item.getStripeBankAccount() != null) {
            stripeBankAccountLookUp.addItem(item.getStripeBankAccount());
        }

        if (item.getElavonMerchantID() != null) {
            stripeElavonMerchant.setText(item.getElavonMerchantID());
        }
        if (item.getElavonUserID() != null) {
            stripeElavonUser.setText(item.getElavonUserID());
        }
        if (item.getElavonPIN() != null) {
            stripeElavonPin.setText(item.getElavonPIN());
        }
        if (item.getElavonBankAccount() != null) {
            stripeElavonBankAccountLookUp.addItem(item.getElavonBankAccount());
        }

        Optional.ofNullable(item.getPayMeMerchantId()).ifPresent(paymeMerchantIdBox::setText);
        Optional.ofNullable(item.getPaymeBankAccount()).ifPresent(paymeBankAccountLookUp::setSelected);
        Optional.ofNullable(item.getPaymeExpenseAccount()).ifPresent(paymeExpenseAccountLookUp::setSelected);
        Optional.ofNullable(item.getPaymeServiceId()).ifPresent(paymeServiceIdBox::setText);
        Optional.ofNullable(item.getPaymeServiceFee()).ifPresent(value -> paymeServiceFeeBox.setText(value.toString()));

        Optional.ofNullable(item.getClickMerchantId()).ifPresent(clickMerchantIdBox::setText);
        Optional.ofNullable(item.getClickBankAccount()).ifPresent(clickBankAccountLookUp::addItem);
        Optional.ofNullable(item.getClickServiceId()).ifPresent(clickServiceIdBox::setText);

        Optional.ofNullable(item.getRevolutEmail()).ifPresent(revolutEmail::setText);
        Optional.ofNullable(item.getRevolutBankAccount()).ifPresent(revolutBankAccountLookUp::setSelected);
        Optional.ofNullable(item.getRevolutExpenseAccount()).ifPresent(revolutExpenseAccountLookUp::setSelected);
        Optional.ofNullable(item.getRevolutSecretApiKey()).ifPresent(revolutSecretApiKey::setText);

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
        }


    }

    private boolean validatePayme() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(paymeMerchantIdBox)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(paymeBankAccountLookUp)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(paymeExpenseAccountLookUp)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(paymeServiceIdBox)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(paymeServiceFeeBox)) {
            errors++;
        }
        return errors == 0 || errors == 5;
    }

    private boolean validateClick() {
        int errors = 0;

        if (!Validation.validateTextBoxRequired(clickMerchantIdBox)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(clickBankAccountLookUp)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired((clickServiceIdBox))) {
            errors++;
        }
        return errors == 0 || errors == 3;
    }

    private boolean validateRevolut() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(revolutEmail)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(revolutBankAccountLookUp)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(revolutExpenseAccountLookUp)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(revolutSecretApiKey)) {
            errors++;
        }

        return errors == 0 || errors == 3;
    }

    private void save(boolean isPaypal) {

        IntegrationItem item = new IntegrationItem();
        if (isPaypal) {
            if (!validateFields()) {
                Info.show(wfmStrings.validationFailed(), Info.Type.WARNING);
                return;
            }
            if (payPalMerchantTxtBox.getText() != null && !payPalMerchantTxtBox.getText().equals("")) {
                item.setPayPalMerchant(payPalMerchantTxtBox.getText());
            }
            item.setPayPalBankAccount(paypalBankAccountLookUp.getSelectedItem());
        } else {
            item.setStripeBankAccount(stripeBankAccountLookUp.getSelectedItem());
        }
        item.setPayPal(isPaypal);

        item.setPayMeMerchantId(paymeMerchantIdBox.getText());
        item.setPaymeBankAccount(paymeBankAccountLookUp.getSelectedItem());
        item.setPaymeExpenseAccount(paymeExpenseAccountLookUp.getSelectedItem());
        item.setPaymeServiceId(paymeServiceIdBox.getText());
        item.setPaymeServiceFee(new BigDecimal(paymeServiceFeeBox.getText()));

        item.setClickMerchantId(clickMerchantIdBox.getText());
        item.setClickBankAccount(clickBankAccountLookUp.getSelectedItem());
        item.setClickServiceId(clickServiceIdBox.getText());

        item.setRevolutEmail(revolutEmail.getText());
        item.setRevolutBankAccount(revolutBankAccountLookUp.getSelectedItem());
        item.setRevolutExpenseAccount(revolutExpenseAccountLookUp.getSelectedItem());
        item.setRevolutSecretApiKey(revolutSecretApiKey.getText());

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


    private boolean validateFields() {
        if (!Utils.isNullOrEmpty(payPalMerchantTxtBox.getText()) && !Utils.validateEmail(payPalMerchantTxtBox.getText(), false)) {
            payPalMerchantTxtBox.addStyleName(ERROR_FORM_STYLE);
            payPalMerchantTxtBox.addKeyPressHandler(valueChangeEvent -> payPalMerchantTxtBox.removeStyleName(ERROR_FORM_STYLE));
            return false;
        }
        return true;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYPAL_PAYMENT_FORM;
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
