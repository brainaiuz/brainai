package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.HtmlLabel;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CustomerCreditLimitExceedPopup extends KpiModal {
    protected static NumberFormat numberFormat = Utils.getCalculationNumberFormat();
    private Integer calculationScale = Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2;

    private String message;
    private Integer accountId;
    private NewInvoice invoiceData;
    private String invoiceStatus;
    private TextBox creditLimit;
    private GBoxItem creditLimitField;
    private boolean updatedCreditLimit = false;

    private WfmButton2 update;
    private WfmButton2 save;
    private WfmButton2 okButton;

    public CustomerCreditLimitExceedPopup(Integer accountId, String message, NewInvoice invoiceData, String invoiceStatus){
        setTitle(wfmStrings.exceeded() + " " + wfmStrings.creditLimit());
        this.message = message;
        this.accountId = accountId;
        this.invoiceData = invoiceData;
        this.invoiceStatus = invoiceStatus;
        setWidth(450);
        init();
    }

    private void init() {
        Command closeCommand = () -> {
            close();
        };

        HtmlLabel htmlLabel = new HtmlLabel();
        htmlLabel.setText(message);
        add(htmlLabel);

        creditLimit = new TextBox();
        creditLimit.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(creditLimit, calculationScale);

        creditLimitField = new GBoxItem(wfmStrings.creditLimit(), creditLimit);
        creditLimitField.ensureDebugId("credit_limit");


        okButton = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_DEFAULT, clickEvent -> {
            LoadingPanel.loading(false);
            close();
        });
        okButton.ensureDebugId("closeButton");

        update = new WfmButton2(wfmStrings.updateCreditLimit(), WfmButton2.BTN_PRIMARY);
        update.ensureDebugId("saveButton");

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.ensureDebugId("saveButton");

        update.addClickHandler(clickEvent -> {
            add(creditLimitField);
            update.setVisible(false);
            save.setVisible(true);
            creditLimitField.setVisible(true);
            addButton(save);

        });

        if (Utils.hasPermission(PermissionConstants.UPDATE_CUSTOMER_CREDIT_LIMIT)) {
            addButton(update);
        }
        addButton(okButton);
        open();
    }

    public boolean updateCreditLimit() {
        BigDecimal limit = new BigDecimal(BigInteger.ZERO);
        if (creditLimit.getText() != null && !"".equals(creditLimit.getText())) {
            updatedCreditLimit = true;
            limit = new BigDecimal(numberFormat.parse(creditLimit.getText()));

            CRMService.App.get().updateCreditLimit(accountId, limit,  new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(Void result) {
                    updatedCreditLimit = true;
                }
            });
        } else {
            save.setVisible(false);
            creditLimitField.setVisible(false);
            update.setVisible(true);
        }
        return updatedCreditLimit;
    }

    public WfmButton2 getSaveButton() {
        return save;
    }

}
