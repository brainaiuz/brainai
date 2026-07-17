package com.edatasite.workforce.gwt.profile.client.ui.view.accounting.uk;

import com.edatasite.workforce.gwt.accounting.client.rpc.HMRCAuthSettingsItem;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Div;

public class AuthModalContent extends Composite {

    interface AuthModalContentUiBinder extends UiBinder<HTMLPanel, AuthModalContent> {
    }

    private static AuthModalContent.AuthModalContentUiBinder uiBinder = GWT.create(AuthModalContent.AuthModalContentUiBinder.class);

    @UiField
    HTMLPanel adminContainer;
    @UiField
    HTMLPanel agentContainer;
    @UiField
    HTMLPanel ARNContainer;
    @UiField
    TextBox arnBox;
    @UiField
    KpiCheckBox vatConfirmationBox;
    @UiField
    KpiCheckBox termsAndConditionsBox;

    KpiRadioButton agentBox;
    KpiRadioButton adminBox;

    public AuthModalContent() {
        initWidget(uiBinder.createAndBindUi(this));
        initialize();
    }

    private void initialize() {
        adminBox = new KpiRadioButton("role", "Admin of company in KPI");
        adminBox.addValueChangeHandler(valueChangeEvent -> onSelect());

        agentBox = new KpiRadioButton("role", "Agent (on behalf of company)");
        agentBox.addValueChangeHandler(valueChangeEvent -> onSelect());

        adminContainer.add(adminBox);
        agentContainer.add(agentBox);
        ARNContainer.setVisible(false);
        vatConfirmationBox.setText("I have received the confirmation email from HMRC to file VAT returns for company using software solution");
        termsAndConditionsBox.setHTML("I understand that kpi will send data to HMRC for security purposes as per the <a href=\"https://www.kpi.com/en/hmrc/\">Terms & Conditions</a>.");
    }

    private void onSelect() {
        ARNContainer.setVisible(agentBox.getValue());
    }

    public boolean validate() {
        int errors = 0;
        if (agentBox.getValue()) {
            if (!Validation.validateTextBoxRequired(arnBox)) {
                errors++;
            }
        } else if (!Validation.validateRadioButtonRequired(adminBox)) {
            errors++;
        }
        if (!vatConfirmationBox.getValue()) {
            errors++;
        }
        if (!termsAndConditionsBox.getValue()) {
            errors++;
        }
        return errors == 0;
    }

    public HMRCAuthSettingsItem getData() {
        HMRCAuthSettingsItem hmrcAuthSettingsItem = new HMRCAuthSettingsItem();
        hmrcAuthSettingsItem.setSubmitVatManually(false);
        hmrcAuthSettingsItem.setAgent(agentBox.getValue());
        hmrcAuthSettingsItem.setAgentNumber(arnBox.getValue());

        return hmrcAuthSettingsItem;
    }
}
