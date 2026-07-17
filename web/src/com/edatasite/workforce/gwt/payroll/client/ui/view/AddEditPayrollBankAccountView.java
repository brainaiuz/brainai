package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.UserBankAccountData;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionField;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionLabel;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: Oct 31, 2009
 * Time: 3:54:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddEditPayrollBankAccountView extends View {

    private TextBox accountName;
    private TextBox accountNumber;
    private WfmForm.Field accountNumberField;
    private TextArea2 bankAddress;
    private TextBox bankName;
    private WfmForm.Field bankNameField;
    private Integer employeeId;
    private TextBox ibanCode;
    private TextBox agentID;
    private TextBox sortCode;
    private TextBox swiftCode;
    private WfmForm table;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();


    public AddEditPayrollBankAccountView(Integer employeeId) {
        super("payrollbankedit", wfmStrings.bankDetails());
        this.employeeId = employeeId;
    }


    public String getIconStyle() {
        return "payroll bank-details";
    }

   /* public AbstractImagePrototype getIconImage() {
        return PayrollImageBundle.App.get().bank_16();
    }*/



    @Override
    protected Widget onInitialize() {
        refreshDetail();
        return null;
    }

    private void initializeAddBankDetail(UserBankAccountData userBankAccountData) {
        clear();
        table = new WfmForm(new String[]{"40%", "15%", "5%"});
        table.addTitleField(wfmStrings.bankDetails());
        table.addHorizontalLine();
        //bank name
        bankName = new TextBox();
        bankName.addStyleName(DEFAULT_WIDTH);
        bankName.setMaxLength(30);
        //bank address
        bankAddress = new TextArea2(wfmStrings.bankAddress());
        bankAddress.addStyleName(DEFAULT_WIDTH);
        //account number
        accountNumber = new TextBox();
        accountNumber.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(accountNumber);
        //account name
        accountName = new TextBox();
        accountName.addStyleName(DEFAULT_WIDTH);
        //swift/BIC code
        swiftCode = new TextBox();
        swiftCode.addStyleName(DEFAULT_WIDTH);
        //sort code
        sortCode = new TextBox();
        sortCode.addStyleName(DEFAULT_WIDTH);
        //IBAN code
        ibanCode = new TextBox();
        ibanCode.addStyleName(DEFAULT_WIDTH);

        //IBAN code
        agentID = new TextBox();
        agentID.addStyleName(DEFAULT_WIDTH);

        bankNameField = table.addField(wfmStrings.bankName(), bankName, wfmStrings.bankNameAppearLimitedTo30Characters(), true);
        accountNumberField = table.addField(wfmStrings.accountNumber(), accountNumber, wfmStrings.accountNumberFieldDescription(), true);
        table.addField(wfmStrings.accountName(), accountName);
        table.addField(null, bankAddress);
        table.addField(wfmStrings.swiftCode(), swiftCode);
        table.addField(wfmStrings.sortCode(), sortCode);
        table.addField(wfmStrings.ibanCode(), ibanCode);
        table.addField(wfmStrings.agentID(), agentID);

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(clickEvent -> {
            if (!validate()) {
                return;
            }
            saveBankAccount();
        });
        if (Utils.isDemoAccount()) {
            saveButton.setEnabled(false);
        }
        table.addStyleName("file--AddEditPayrollBankAccountVIew");
        table.addHorizontalLine();
        table.addButton(saveButton);

        //set bank details
        if (userBankAccountData != null) {
            if (userBankAccountData.getBankName() != null) {
                bankName.setText(userBankAccountData.getBankName());
            }
            if (userBankAccountData.getAccountNumber() != null) {
                accountNumber.setText(userBankAccountData.getAccountNumber());
            }
            if (userBankAccountData.getAccountName() != null) {
                accountName.setText(userBankAccountData.getAccountName());
            }
            if (userBankAccountData.getBankAddress() != null) {
                bankAddress.setText(userBankAccountData.getBankAddress());
            }
            if (userBankAccountData.getSwiftCode() != null) {
                swiftCode.setText(userBankAccountData.getSwiftCode());
            }
            if (userBankAccountData.getSortCode() != null) {
                sortCode.setText(userBankAccountData.getSortCode());
            }
            if (userBankAccountData.getIbanCode() != null) {
                ibanCode.setText(userBankAccountData.getIbanCode());
            }
            if (userBankAccountData.getAgentID() != null) {
                agentID.setText(userBankAccountData.getAgentID());
            }
        }
        add(table);
    }

    private void initializeBankDetail(final UserBankAccountData userBankAccountData) {
        clear();

        PreviewSectionContainer container = new PreviewSectionContainer();
        PreviewSectionField preview;
        PreviewSectionLabel label;
        preview = new PreviewSectionField();

        preview.addField(wfmStrings.bankName(), userBankAccountData.getBankName());
        if (userBankAccountData.getBankAddress() != null && !"".equals(userBankAccountData.getBankAddress())) {
            preview.addField(wfmStrings.bankAddress(), userBankAccountData.getBankAddress());
        }
        preview.addField(wfmStrings.accountNumber(), userBankAccountData.getAccountNumber());
        if (userBankAccountData.getAccountName() != null && !"".equals(userBankAccountData.getAccountName().trim())) {
            preview.addField(wfmStrings.accountName(), userBankAccountData.getAccountName());
        }
        if (userBankAccountData.getSwiftCode() != null && !"".equals(userBankAccountData.getSwiftCode().trim())) {
            preview.addField(wfmStrings.swiftCode(), userBankAccountData.getSwiftCode());
        }
        if (userBankAccountData.getSortCode() != null && !"".equals(userBankAccountData.getSortCode().trim())) {
            preview.addField(wfmStrings.sortCode(), userBankAccountData.getSortCode());
        }
        if (userBankAccountData.getIbanCode() != null && !"".equals(userBankAccountData.getIbanCode().trim())) {
            preview.addField(wfmStrings.ibanCode(), userBankAccountData.getIbanCode());
        }

        if (userBankAccountData.getAgentID() != null && !"".equals(userBankAccountData.getAgentID().trim())) {
            preview.addField(wfmStrings.agentID(), userBankAccountData.getAgentID());
        }

        preview.addSpace();
        WfmButton2 editButton = new WfmButton2(wfmStrings.edit());
        editButton.addClickHandler(clickEvent -> initializeAddBankDetail(userBankAccountData));
        if (Utils.isDemoAccount()) {
            editButton.setEnabled(false);
        }
        preview.addWidget(editButton);

        label = new PreviewSectionLabel(wfmStrings.bankDetails(), "");
        container.addSection(label, preview);
        add(container);
    }

    private void refreshDetail() {
        AllInOneService.App.get().getBankDetail(employeeId, new AbstractAsyncCallback<UserBankAccountData>() {
            @Override
            public void failure(Throwable throwable) {
                initializeAddBankDetail(null);
            }

            @Override
            public void success(UserBankAccountData userBankAccountData) {
                if (userBankAccountData != null) {
                    initializeBankDetail(userBankAccountData);
                } else {
                    initializeAddBankDetail(null);
                }
            }
        });
    }

    private void saveBankAccount() {
        UserBankAccountData saveBankAccountData = new UserBankAccountData();
        saveBankAccountData.setObjectID(employeeId);
        saveBankAccountData.setBankName(bankName.getText());
        saveBankAccountData.setAccountNumber(accountNumber.getText());
        saveBankAccountData.setAccountName(accountName.getText());
        saveBankAccountData.setBankAddress(bankAddress.getText());
        saveBankAccountData.setSwiftCode(swiftCode.getText());
        saveBankAccountData.setSortCode(sortCode.getText());
        saveBankAccountData.setIbanCode(ibanCode.getText());
        saveBankAccountData.setAgentID(agentID.getText());

        LoadingPanel.loading(true);
        PayrollService.App.get().createBankAccount(saveBankAccountData, new AbstractAsyncCallback<Void>() {

            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                refreshDetail();
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void o) {
                LoadingPanel.loading(false);
                refreshDetail();
                Info.show(Property.get(Constants.BANKACCOUNT, wfmStrings.messSuccessfullySaved(), wfmStrings.bankAccount()), Info.Type.INFO);
            }
        });
    }

    public boolean validate() {
        int error = 0;
        table.cleanupErrors();

        if (!Validation.validateTextBoxRequired(bankName, bankNameField)) {
            error++;
        }
        if (!Validation.validateTextBoxRequired(accountNumber, accountNumberField)) {
            error++;
        }
        if (error > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
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