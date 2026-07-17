package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionProviderData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/4/15
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionProviderSummaryView extends PensionProviderView {

    Integer objectID;
    //Provider details
    private HTML providerName;
    private HTML providerAccountRef;
    private HTML providerOtherRef;
    private HTML providerAddress;
    private HTML providerTownCity;
    private HTML providerCounty;
    private HTML providerPostCode;
    private HTML providerCountry;
    private HTML providerTelNo;
    private HTML providerFaxNo;
    private HTML providerEmail;
    private HTML providerCPName;
    private HTML providerCPMobile;

    //Provider Bank details
    private HTML bankName;
    private HTML branchName;
    private HTML bankAddress;
    private HTML bankTownCity;
    private HTML bankCounty;
    private HTML bankPostCode;
    private HTML bankCountry;
    private HTML bankCPName;
    private HTML bankTelNo;
    private HTML bankFaxNo;
    private HTML bankEmail;
    private HTML sortCode;
    private HTML accountNo;
    private HTML nameShownOnAccount;
    private HTML bankAccountRef;

    public PensionProviderSummaryView(Integer objectID) {
        super(objectID, "Pension Provider View", "Pension Provider View");
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        return super.onInitialize();
    }

    @Override
    protected void init() {
        providerName = new HTML();
        providerAccountRef = new HTML();
        providerOtherRef = new HTML();
        providerAddress = new HTML();
        providerTownCity = new HTML();
        providerCounty = new HTML();
        providerPostCode = new HTML();
        providerCountry = new HTML();
        providerTelNo = new HTML();
        providerFaxNo = new HTML();
        providerEmail = new HTML();
        providerCPName = new HTML();
        providerCPMobile = new HTML();

        bankName = new HTML();
        branchName = new HTML();
        bankAddress = new HTML();
        bankTownCity = new HTML();
        bankCounty = new HTML();
        bankPostCode = new HTML();
        bankCountry = new HTML();
        bankCPName = new HTML();
        bankTelNo = new HTML();
        bankFaxNo = new HTML();
        bankEmail = new HTML();
        sortCode = new HTML();
        accountNo = new HTML();
        nameShownOnAccount = new HTML();
        bankAccountRef = new HTML();

        addTitleField(PENSION_PROVIDER.PROVIDER_DETAILS, payrollStrings.pensionProviderDetails());
        addTitleField(PENSION_PROVIDER.PROVIDER_BANK_DETAILS, payrollStrings.pensionProviderBankDetails());
        FormGroup providerNameField = new FormGroup(payrollStrings.providersName(), providerName);
        addField(PENSION_PROVIDER.PROVIDER_NAME, providerNameField);
        FormGroup providerAccountRefField = new FormGroup(payrollStrings.providersAccountRef(), providerAccountRef);
        addField(PENSION_PROVIDER.PROVIDER_ACCOUNT_REF, providerAccountRefField);
        FormGroup providerOtherRefField = new FormGroup(payrollStrings.otherRef(), providerOtherRef);
        addField(PENSION_PROVIDER.OTHER_REF, providerOtherRefField);
        FormGroup providerAddressField = new FormGroup(payrollStrings.providersAddress(), providerAddress);
        addField(PENSION_PROVIDER.PROVIDER_ADDRESS, providerAddressField);
        FormGroup providerTownCityField = new FormGroup(wfmStrings.city(), providerTownCity);
        addField(PENSION_PROVIDER.PROVIDER_CITY, providerTownCityField);
        FormGroup providerPostCodeField = new FormGroup(wfmStrings.postCode(), providerPostCode);
        addField(PENSION_PROVIDER.PROVIDER_POST_CODE, providerPostCodeField);
        FormGroup providerCountryField = new FormGroup(wfmStrings.country(), providerCountry);
        addField(PENSION_PROVIDER.PROVIDER_COUNTRY, providerCountryField);
        FormGroup providerTelNoField = new FormGroup(payrollStrings.telNo(), providerTelNo);
        addField(PENSION_PROVIDER.PROVIDER_PHONE, providerTelNoField);
        FormGroup providerFaxNoField = new FormGroup(payrollStrings.faxNo(), providerFaxNo);
        addField(PENSION_PROVIDER.PROVIDER_FAX, providerFaxNoField);
        FormGroup providerEmailField = new FormGroup(wfmStrings.email(), providerEmail);
        addField(PENSION_PROVIDER.PROVIDER_EMAIL, providerEmailField);
        FormGroup providerCPNameField = new FormGroup(payrollStrings.contactPersonsName(), providerCPName);
        addField(PENSION_PROVIDER.PROVIDER_CONTACT_NAME, providerCPNameField);
        FormGroup providerCPMobileField = new FormGroup(payrollStrings.contactPersonsMobile(), providerCPMobile);
        addField(PENSION_PROVIDER.PROVIDER_CONTACT_MOBILE, providerCPMobileField);
        FormGroup bankNameField = new FormGroup(payrollStrings.providersBankName(), bankName);
        addField(PENSION_PROVIDER.PROVIDER_BANK_NAME, bankNameField);
        FormGroup branchNameField = new FormGroup(payrollStrings.providersBankBranchName(), branchName);
        addField(PENSION_PROVIDER.PROVIDER_BANK_BRANCH_NAME, branchNameField);
        FormGroup bankAddressField = new FormGroup(payrollStrings.providersBankAddress(), bankAddress);
        addField(PENSION_PROVIDER.PROVIDER_BANK_ADDRESS, bankAddressField);
        FormGroup bankTownCityField = new FormGroup(wfmStrings.city(), bankTownCity);
        addField(PENSION_PROVIDER.PROVIDER_BANK_CITY, bankTownCityField);
        FormGroup bankPostCodeField = new FormGroup(wfmStrings.postCode(), bankPostCode);
        addField(PENSION_PROVIDER.PROVIDER_BANK_POST_CODE, bankPostCodeField);
        FormGroup bankCountryField = new FormGroup(wfmStrings.country(), bankCountry);
        addField(PENSION_PROVIDER.PROVIDER_BANK_COUNTRY, bankCountryField);
        FormGroup bankCPNameField = new FormGroup(payrollStrings.bankContactPersonsName(), bankCPName);
        addField(PENSION_PROVIDER.PROVIDER_BANK_CONTACT_NAME, bankCPNameField);
        FormGroup bankTelNoField = new FormGroup(payrollStrings.bankTelNo(), bankTelNo);
        addField(PENSION_PROVIDER.PROVIDER_BANK_PHONE, bankTelNoField);
        FormGroup bankFaxNoField = new FormGroup(payrollStrings.bankFaxNo(), bankFaxNo);
        addField(PENSION_PROVIDER.PROVIDER_BANK_FAX, bankFaxNoField);
        FormGroup bankEmailField = new FormGroup(payrollStrings.bankEMail(), bankEmail);
        addField(PENSION_PROVIDER.PROVIDER_BANK_EMAIL, bankEmailField);
        FormGroup sortCodeField = new FormGroup(wfmStrings.sortCode(), sortCode);
        addField(PENSION_PROVIDER.PROVIDER_BANK_SORT_CODE, sortCodeField);
        FormGroup accountNoField = new FormGroup(wfmStrings.accountNo(), accountNo);
        addField(PENSION_PROVIDER.PROVIDER_BANK_ACCOUNT, accountNoField);
        FormGroup nameShownOnAccountField = new FormGroup(payrollStrings.nameShownOnTheAccount(), nameShownOnAccount);
        addField(PENSION_PROVIDER.PROVIDER_BANK_NAME_SHOW, nameShownOnAccountField);
        FormGroup bankAccountRefField = new FormGroup(payrollStrings.bankOnlineAccountRef(), bankAccountRef);
        addField(PENSION_PROVIDER.PROVIDER_BANK_ACCOUNT_REF, bankAccountRefField);
        show();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.edit(), BTN_PRIMARY, clickEvent -> {
            closeTab();
            goTo("pensionprovider|add/add/" + objectID);
        });
    }

    @Override
    protected void getDataToFillFields() {
        super.getDataToFillFields();
    }

    @Override
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
            providerCountry.setText(data.getProviderCountry().getName());
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
            bankCountry.setText(data.getBankCountry().getName());
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

    @Override
    protected String getFormID() {
        return super.getFormID();
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return super.getWikiCode();
    }

    @Override
    public String getIconStyle() {
        return super.getIconStyle();
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
