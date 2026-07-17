package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CountryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionProviderData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/4/15
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionProviderView extends CustomForm implements CustomFormConstants, Constants, Colapse {

    protected static final PayrollStrings payrollStrings = PayrollStrings.App.get();
//    protected static final InfoStrings infoStrings = wfmStrings.App.get();

    Integer objectID;
    //Provider details
    private TextBox providerName;
    private TextBox providerAccountRef;
    private TextBox providerOtherRef;
    private TextBox providerAddress;
    private TextBox providerTownCity;
    private TextBox providerCounty;
    private TextBox providerPostCode;
    private CountryLookUp providerCountry;
    private TextBox providerTelNo;
    private TextBox providerFaxNo;
    private TextBox providerEmail;
    private TextBox providerCPName;
    private TextBox providerCPMobile;
    private DatePicker lastPayment;
    private DatePicker nextPayment;

    //Provider Bank details
    private TextBox bankName;
    private TextBox branchName;
    private TextBox bankAddress;
    private TextBox bankTownCity;
    private TextBox bankCounty;
    private TextBox bankPostCode;
    private CountryLookUp bankCountry;
    private TextBox bankCPName;
    private TextBox bankTelNo;
    private TextBox bankFaxNo;
    private TextBox bankEmail;
    private TextBox sortCode;
    private TextBox accountNo;
    private TextBox nameShownOnAccount;
    private TextBox bankAccountRef;

    public PensionProviderView() {
        super("addpensionprovider", payrollStrings.pensionProvider());
    }

    public PensionProviderView(Integer objectID, String name, String description) {
        super(name, description);
        this.objectID = objectID;
    }

    public PensionProviderView(Integer objectID) {
        super("addpensionprovider", payrollStrings.pensionProvider());
        this.objectID = objectID;
    }


    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        init();
        return null;
    }

    protected void init() {
        providerName = new TextBox();
        providerAccountRef = new TextBox();
        providerOtherRef = new TextBox();
        providerAddress = new TextBox();
        providerTownCity = new TextBox();
        providerCounty = new TextBox();
        providerPostCode = new TextBox();
        providerCountry = new CountryLookUp();
        providerTelNo = new TextBox();
        providerFaxNo = new TextBox();
        providerEmail = new TextBox();
        providerCPName = new TextBox();
        providerCPMobile = new TextBox();
        lastPayment = new DatePicker(true);
        nextPayment = new DatePicker(true);
        bankName = new TextBox();
        branchName = new TextBox();
        bankAddress = new TextBox();
        bankTownCity = new TextBox();
        bankCounty = new TextBox();
        bankPostCode = new TextBox();
        bankCountry = new CountryLookUp();
        bankCPName = new TextBox();
        bankTelNo = new TextBox();
        bankFaxNo = new TextBox();
        bankEmail = new TextBox();
        sortCode = new TextBox();
        accountNo = new TextBox();
        nameShownOnAccount = new TextBox();
        bankAccountRef = new TextBox();

        providerName.addStyleName(DEFAULT_WIDTH);
        providerAccountRef.addStyleName(DEFAULT_WIDTH);
        providerOtherRef.addStyleName(DEFAULT_WIDTH);
        providerAddress.addStyleName(DEFAULT_WIDTH);
        providerTownCity.addStyleName(DEFAULT_WIDTH);
        providerCounty.addStyleName(DEFAULT_WIDTH);
        providerPostCode.addStyleName(DEFAULT_WIDTH);
        providerCountry.addStyleName(DEFAULT_WIDTH);
        providerTelNo.addStyleName(DEFAULT_WIDTH);
        providerFaxNo.addStyleName(DEFAULT_WIDTH);
        providerEmail.addStyleName(DEFAULT_WIDTH);
        providerCPName.addStyleName(DEFAULT_WIDTH);
        providerCPMobile.addStyleName(DEFAULT_WIDTH);
        lastPayment.addStyleName(DEFAULT_WIDTH);
        nextPayment.addStyleName(DEFAULT_WIDTH);
        bankName.addStyleName(DEFAULT_WIDTH);
        branchName.addStyleName(DEFAULT_WIDTH);
        bankAddress.addStyleName(DEFAULT_WIDTH);
        bankTownCity.addStyleName(DEFAULT_WIDTH);
        bankCounty.addStyleName(DEFAULT_WIDTH);
        bankPostCode.addStyleName(DEFAULT_WIDTH);
        bankCountry.addStyleName(DEFAULT_WIDTH);
        bankCPName.addStyleName(DEFAULT_WIDTH);
        bankTelNo.addStyleName(DEFAULT_WIDTH);
        bankFaxNo.addStyleName(DEFAULT_WIDTH);
        bankEmail.addStyleName(DEFAULT_WIDTH);
        sortCode.addStyleName(DEFAULT_WIDTH);
        accountNo.addStyleName(DEFAULT_WIDTH);
        nameShownOnAccount.addStyleName(DEFAULT_WIDTH);
        bankAccountRef.addStyleName(DEFAULT_WIDTH);

        FormGroup providerNameField = new FormGroup(payrollStrings.providersName(), providerName);
        FormGroup providerAccountRefField = new FormGroup(payrollStrings.providersAccountRef(), providerAccountRef);
        FormGroup providerOtherRefField = new FormGroup(payrollStrings.otherRef(), providerOtherRef);
        FormGroup providerAddressField = new FormGroup(payrollStrings.providersAddress(), providerAddress);
        FormGroup providerTownCityField = new FormGroup(wfmStrings.city(), providerTownCity);
        FormGroup providerPostCodeField = new FormGroup(wfmStrings.postCode(), providerPostCode);
        FormGroup providerCountryField = new FormGroup(wfmStrings.country(), providerCountry);
        FormGroup providerTelNoField = new FormGroup(payrollStrings.telNo(), providerTelNo);
        FormGroup providerFaxNoField = new FormGroup(payrollStrings.faxNo(), providerFaxNo);
        FormGroup providerEmailField = new FormGroup(wfmStrings.email(), providerEmail);
        FormGroup providerCPNameField = new FormGroup(payrollStrings.contactPersonsName(), providerCPName);
        FormGroup providerCPMobileField = new FormGroup(payrollStrings.contactPersonsMobile(), providerCPMobile);
        FormGroup bankNameField = new FormGroup(payrollStrings.providersBankName(), bankName);
        FormGroup branchNameField = new FormGroup(payrollStrings.providersBankBranchName(), branchName);
        FormGroup bankAddressField = new FormGroup(payrollStrings.providersBankAddress(), bankAddress);
        FormGroup bankTownCityField = new FormGroup(wfmStrings.city(), bankTownCity);
        FormGroup bankPostCodeField = new FormGroup(wfmStrings.postCode(), bankPostCode);
        FormGroup bankCountryField = new FormGroup(wfmStrings.country(), bankCountry);
        FormGroup bankCPNameField = new FormGroup(payrollStrings.bankContactPersonsName(), bankCPName);
        FormGroup bankTelNoField = new FormGroup(payrollStrings.bankTelNo(), bankTelNo);
        FormGroup bankFaxNoField = new FormGroup(payrollStrings.bankFaxNo(), bankFaxNo);
        FormGroup bankEmailField = new FormGroup(payrollStrings.bankEMail(), bankEmail);
        FormGroup sortCodeField = new FormGroup(wfmStrings.sortCode(), sortCode);
        FormGroup accountNoField = new FormGroup(wfmStrings.accountNo(), accountNo);
        FormGroup nameShownOnAccountField = new FormGroup(payrollStrings.nameShownOnTheAccount(), nameShownOnAccount);
        FormGroup bankAccountRefField = new FormGroup(payrollStrings.bankOnlineAccountRef(), bankAccountRef);

        addTitleField(PENSION_PROVIDER.PROVIDER_DETAILS, payrollStrings.pensionProviderDetails());
        addTitleField(PENSION_PROVIDER.PROVIDER_BANK_DETAILS, payrollStrings.pensionProviderBankDetails());
        addField(PENSION_PROVIDER.PROVIDER_NAME, providerNameField);
        addField(PENSION_PROVIDER.PROVIDER_ACCOUNT_REF, providerAccountRefField);
        addField(PENSION_PROVIDER.OTHER_REF, providerOtherRefField);
        addField(PENSION_PROVIDER.PROVIDER_ADDRESS, providerAddressField);
        addField(PENSION_PROVIDER.PROVIDER_CITY, providerTownCityField);
        addField(PENSION_PROVIDER.PROVIDER_POST_CODE, providerPostCodeField);
        addField(PENSION_PROVIDER.PROVIDER_COUNTRY, providerCountryField);
        addField(PENSION_PROVIDER.PROVIDER_PHONE, providerTelNoField);
        addField(PENSION_PROVIDER.PROVIDER_FAX, providerFaxNoField);
        addField(PENSION_PROVIDER.PROVIDER_EMAIL, providerEmailField);
        addField(PENSION_PROVIDER.PROVIDER_CONTACT_NAME, providerCPNameField);
        addField(PENSION_PROVIDER.PROVIDER_CONTACT_MOBILE, providerCPMobileField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_NAME, bankNameField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_BRANCH_NAME, branchNameField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_ADDRESS, bankAddressField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_CITY, bankTownCityField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_POST_CODE, bankPostCodeField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_COUNTRY, bankCountryField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_CONTACT_NAME, bankCPNameField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_PHONE, bankTelNoField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_FAX, bankFaxNoField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_EMAIL, bankEmailField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_SORT_CODE, sortCodeField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_ACCOUNT, accountNoField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_NAME_SHOW, nameShownOnAccountField);
        addField(PENSION_PROVIDER.PROVIDER_BANK_ACCOUNT_REF, bankAccountRefField);
        show();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), BTN_PRIMARY, clickEvent -> save());
    }

    @Override
    protected void getDataToFillFields() {
        if (objectID != null) {
            LoadingPanel.loading(true);
            PayrollService.App.get().getPensionProvider(objectID, new AbstractAsyncCallback<PensionProviderData>() {
                @Override
                public void failure(Throwable throwable) {
                    throwable.printStackTrace();
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(PensionProviderData result) {
                    setValues(result);
                    LoadingPanel.loading(false);
                }
            });
        }
    }


    protected void setValues(PensionProviderData data) {
        if (data.getProviderName() != null) {
            providerName.setText(data.getProviderName());
        }
        if (data.getProviderAccountRef() != null) {
            providerAccountRef.setText(data.getProviderAccountRef());
        }
        if (data.getProviderOtherRef() != null) {
            providerOtherRef.setText(data.getProviderOtherRef());
        }
        if (data.getProviderAddress() != null) {
            providerAddress.setText(data.getProviderAddress());
        }
        if (data.getProviderTownCity() != null) {
            providerTownCity.setText(data.getProviderTownCity());
        }
        if (data.getProviderPostCode() != null) {
            providerPostCode.setText(data.getProviderPostCode());
        }
        if (data.getProviderCountry() != null) {
            providerCountry.addItem(data.getProviderCountry());
        }
        if (data.getProviderTelNo() != null) {
            providerTelNo.setText(data.getProviderTelNo());
        }
        if (data.getProviderFaxNo() != null) {
            providerFaxNo.setText(data.getProviderFaxNo());
        }
        if (data.getProviderEmail() != null) {
            providerEmail.setText(data.getProviderEmail());
        }
        if (data.getProviderCPName() != null) {
            providerCPName.setText(data.getProviderCPName());
        }
        if (data.getProviderCPMobile() != null) {
            providerCPMobile.setText(data.getProviderCPMobile());
        }
        if (data.getBankName() != null) {
            bankName.setText(data.getBankName());
        }
        if (data.getBranchName() != null) {
            branchName.setText(data.getBranchName());
        }
        if (data.getBankAddress() != null) {
            bankAddress.setText(data.getBankAddress());
        }
        if (data.getBankTownCity() != null) {
            bankTownCity.setText(data.getBankTownCity());
        }
        if (data.getBankCountry() != null) {
            bankCountry.addItem(data.getBankCountry());
        }
        if (data.getBankCPName() != null) {
            bankCPName.setText(data.getBankCPName());
        }
        if (data.getBankTelNo() != null) {
            bankTelNo.setText(data.getBankTelNo());
        }
        if (data.getBankFaxNo() != null) {
            bankFaxNo.setText(data.getBankFaxNo());
        }
        if (data.getBankEmail() != null) {
            bankEmail.setText(data.getBankEmail());
        }
        if (data.getSortCode() != null) {
            sortCode.setText(data.getSortCode());
        }
        if (data.getAccountNo() != null) {
            accountNo.setText(data.getAccountNo());
        }
        if (data.getNameShownOnAccount() != null) {
            nameShownOnAccount.setText(data.getNameShownOnAccount());
        }
        if (data.getBankAccountRef() != null) {
            bankAccountRef.setText(data.getBankAccountRef());
        }
    }


    private void save() {
        if (!validate()) {
            //save.setEnabled(true);
            return;
        }
        PensionProviderData data = new PensionProviderData();
        data.setObjectID(objectID);
        data.setProviderName(providerName.getText());
        data.setProviderAccountRef(providerAccountRef.getText());
        data.setProviderOtherRef(providerOtherRef.getText());
        data.setProviderAddress(providerAddress.getText());
        data.setProviderTownCity(providerTownCity.getText());
        data.setProviderCounty(providerCounty.getText());
        data.setProviderPostCode(providerPostCode.getText());
        data.setProviderCountry(providerCountry.getSelectedItem());
        data.setProviderTelNo(providerTelNo.getText());
        data.setProviderFaxNo(providerFaxNo.getText());
        data.setProviderEmail(providerEmail.getText());
        data.setProviderCPName(providerCPName.getText());
        data.setProviderCPMobile(providerCPMobile.getText());
        data.setLastPayment(lastPayment.getDate());
        data.setNextPayment(nextPayment.getDate());

        data.setBankName(bankName.getText());
        data.setBranchName(branchName.getText());
        data.setBankAddress(bankAddress.getText());
        data.setBankTownCity(bankTownCity.getText());
        data.setBankCounty(bankCounty.getText());
        data.setBankPostCode(bankPostCode.getText());
        data.setBankCountry(bankCountry.getSelectedItem());
        data.setBankCPName(bankCPName.getText());
        data.setBankTelNo(bankTelNo.getText());
        data.setBankFaxNo(bankFaxNo.getText());
        data.setBankEmail(bankEmail.getText());
        data.setSortCode(sortCode.getText());
        data.setAccountNo(accountNo.getText());
        data.setNameShownOnAccount(nameShownOnAccount.getText());
        data.setBankAccountRef(bankAccountRef.getText());

        LoadingPanel.loading(true);
        PayrollService.App.get().savePensionProvider(data, new AbstractAsyncCallback() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Object o) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_PENSION_PROVIDER, o, PensionProviderView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), payrollStrings.pensionProvider()), Info.Type.INFO);
                closeTab();
            }
        });
    }


    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(providerName)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_PENSION_PROVIDER_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
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
