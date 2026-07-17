package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccount;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * User: Dilshod  Madrahimov
 * Date: 7/22/14
 * Time: 12:18 AM
 */
public class BankAccountViewForm extends AddEditBankAccountForm implements Colapse {

    private final Integer objectID;
    private BankAccount bankAccountItem;
    private HTML bankName, accountCode, accountNumber, owner, bankBranch, active,
            bankAddress, accountName, swiftCode, ibanCode, sortCode, abaCode, agentID,
            streetAddress, city, country, state,
            postCode, phoneNumber, currency, openingBalanceDate, openingBalanceAmountCurrencyLabel;
    private Command popupCommand;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public BankAccountViewForm(Integer objectID) {
        super("summary", Property.get(BANKACCOUNT, wfmStrings.summaryView(), wfmStrings.bankAccount()));
        this.objectID = objectID;
    }

    public BankAccountViewForm(Integer objectID, Command command) {
        this(objectID);
        popupCommand = command;
        isPopup = true;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BANK_ACCOUNT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected void addButtons() {
        if (!isPopup) {
            addButton(wfmStrings.edit(), BTN_DEFAULT_OUTLINE, sender -> SinksContainerFactory.entryPoint.onHistoryChanged("bank|edit/" + bankAccountItem.getObjectId(), bankAccountItem.getCode(), bankAccountItem.getName()));
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        accountingService.getBankAccountForEdit(objectID, new AbstractAsyncCallback<BankAccount>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final BankAccount bankAccount) {
                LoadingPanel.loading(false);
                bankAccountItem = bankAccount;
                fillFormWithData();
            }
        });
    }

    protected void registerFields() {
        bankName = initHTML();
        accountCode = initHTML();
        accountNumber = initHTML();
        owner = initHTML();
        accountName = initHTML();
        bankBranch = initHTML();
        active = initHTML();
        bankAddress = initHTML();
        swiftCode = initHTML();
        ibanCode = initHTML();
        sortCode = initHTML();
        abaCode = initHTML();
        agentID = initHTML();
        streetAddress = initHTML();
        city = initHTML();
        country = initHTML();
        state = initHTML();
        postCode = initHTML();
        phoneNumber = initHTML();
        currency = initHTML();
        openingBalanceDate = initHTML();

        openingBalanceAmountCurrencyLabel = initHTML();
        fileUpload = new GeneralFileUpload(F_BANK_ACCOUNT, objectID, objectID);

        addTitleField(ACCOUNT_INFORMATION, wfmStrings.basicInfo());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BANK_NAME) != null) {
            addField(BANK_NAME, bankName, getTitle(formPropertyMap.get(BANK_NAME).isChanged() ? formPropertyMap.get(BANK_NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(BANK_NAME, bankName, getTitle(wfmStrings.name()));
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_NUMBER) != null) {
            addField(ACCOUNT_NUMBER, accountNumber, getTitle(formPropertyMap.get(ACCOUNT_NUMBER).isChanged() ? formPropertyMap.get(ACCOUNT_NUMBER).getTitle() : wfmStrings.accountNumber()));
        } else {
            addField(ACCOUNT_NUMBER, accountNumber, getTitle(wfmStrings.accountNumber()));
        }

        if (formPropertyMap != null && formPropertyMap.get(OWNER) != null) {
            addField(OWNER, owner, getTitle(formPropertyMap.get(OWNER).isChanged() ? formPropertyMap.get(OWNER).getTitle() : wfmStrings.owners()));
        } else {
            addField(OWNER, owner, getTitle(wfmStrings.owners()));
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_CODE) != null) {
            addField(ACCOUNT_CODE, accountCode, getTitle(formPropertyMap.get(ACCOUNT_CODE).isChanged() ? formPropertyMap.get(ACCOUNT_CODE).getTitle() : wfmStrings.accountCode()));
        } else {
            addField(ACCOUNT_CODE, accountCode, getTitle(wfmStrings.accountCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(ACCOUNT_NAME) != null) {
            addField(ACCOUNT_NAME, accountCode, getTitle(formPropertyMap.get(ACCOUNT_NAME).isChanged() ? formPropertyMap.get(ACCOUNT_NAME).getTitle() : wfmStrings.accountName()));
        } else {
            addField(ACCOUNT_NAME, accountName, getTitle(wfmStrings.accountName()));
        }

        if (formPropertyMap != null && formPropertyMap.get(BANK_BRANCH) != null) {
            addField(BANK_BRANCH, bankBranch, getTitle(formPropertyMap.get(BANK_BRANCH).isChanged() ? formPropertyMap.get(BANK_BRANCH).getTitle() : wfmStrings.bankBranch()));
        } else {
            addField(BANK_BRANCH, bankBranch, getTitle(wfmStrings.bankBranch()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACTIVE) != null) {
            addField(CustomFormConstants.ACTIVE, active, getTitle(formPropertyMap.get(CustomFormConstants.ACTIVE).isChanged() ? formPropertyMap.get(CustomFormConstants.ACTIVE).getTitle() : wfmStrings.active()));
        } else {
            addField(CustomFormConstants.ACTIVE, active, getTitle(wfmStrings.active()));
        }

        addTitleField(ADDRESS_INFORMATION, wfmStrings.addressInformation());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STREET_ADDRESS) != null) {
            addField(CustomFormConstants.STREET_ADDRESS, streetAddress, getTitle(formPropertyMap.get(CustomFormConstants.STREET_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.STREET_ADDRESS).getTitle() : wfmStrings.streetAddress()));
        } else {
            addField(STREET_ADDRESS, streetAddress, getTitle(wfmStrings.streetAddress()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CITY) != null) {
            addField(CustomFormConstants.CITY, city, getTitle(formPropertyMap.get(CustomFormConstants.CITY).isChanged() ? formPropertyMap.get(CustomFormConstants.CITY).getTitle() : wfmStrings.city()));
        } else {
            addField(CITY, city, getTitle(wfmStrings.city()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CITY) != null) {
            addField(CustomFormConstants.COUNTRY, country, getTitle(formPropertyMap.get(CustomFormConstants.COUNTRY).isChanged() ? formPropertyMap.get(CustomFormConstants.COUNTRY).getTitle() : wfmStrings.country()));
        } else {
            addField(COUNTRY, country, getTitle(wfmStrings.country()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATE) != null) {
            addField(CustomFormConstants.STATE, state, getTitle(formPropertyMap.get(CustomFormConstants.STATE).isChanged() ? formPropertyMap.get(CustomFormConstants.STATE).getTitle() : wfmStrings.state()));
        } else {
            addField(STATE, state, getTitle(wfmStrings.state()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.POST_CODE) != null) {
            addField(CustomFormConstants.POST_CODE, postCode, getTitle(formPropertyMap.get(CustomFormConstants.POST_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.POST_CODE).getTitle() : wfmStrings.postCode()));
        } else {
            addField(POST_CODE, postCode, getTitle(wfmStrings.postCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PHONE_NUMBER) != null) {
            addField(CustomFormConstants.PHONE_NUMBER, phoneNumber, getTitle(formPropertyMap.get(CustomFormConstants.PHONE_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.PHONE_NUMBER).getTitle() : wfmStrings.phone()));
        } else {
            addField(PHONE_NUMBER, phoneNumber, getTitle(wfmStrings.phone()));
        }

        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SWIFT_CODE) != null) {
            addField(CustomFormConstants.SWIFT_CODE, swiftCode, getTitle(formPropertyMap.get(CustomFormConstants.SWIFT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SWIFT_CODE).getTitle() : wfmStrings.swiftCode()));
        } else {
            addField(SWIFT_CODE, swiftCode, getTitle(wfmStrings.swiftCode()));
        }

//        if (CompanyConstants.C6506.equals(Utils.getEncryptedCompanyID())) {
//            //Company ID : 6506; Company Name: Purple Oranges Pty Ltd; E-Mail:troy@purpleoranges.com;
//            //Sort code changed to BSB code for this company
//            addField(SORT_CODE, sortCode, getTitle(accountingStrings.bsbCode()));
//        } else {
//            addField(SORT_CODE, sortCode, getTitle(accountingStrings.sortCode()));
//        }

//        if (CompanyConstants.C6506.equals(Utils.getEncryptedCompanyID())) {
//            //Company ID : 6506; Company Name: Purple Oranges Pty Ltd; E-Mail:troy@purpleoranges.com;
//            //Sort code changed to BSB code for this company
//            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SORT_CODE) != null) {
//                addField(CustomFormConstants.SORT_CODE, sortCode, getTitle(formPropertyMap.get(CustomFormConstants.SORT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SORT_CODE).getTitle() : accountingStrings.bsbCode()));
//            } else {
//                addField(SORT_CODE, sortCode, getTitle(accountingStrings.bsbCode()));
//            }
//        } else {

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SORT_CODE) != null) {
                addField(CustomFormConstants.SORT_CODE, sortCode, getTitle(formPropertyMap.get(CustomFormConstants.SORT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SORT_CODE).getTitle() : wfmStrings.sortCode()));
            } else {
                addField(SORT_CODE, sortCode, getTitle(wfmStrings.sortCode()));
            }
//        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.IBAN_CODE) != null) {
            addField(CustomFormConstants.IBAN_CODE, ibanCode, getTitle(formPropertyMap.get(CustomFormConstants.IBAN_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.IBAN_CODE).getTitle() : wfmStrings.ibanCode()));
        } else {
            addField(IBAN_CODE, ibanCode, getTitle(wfmStrings.ibanCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ABA_CODE) != null) {
            addField(CustomFormConstants.ABA_CODE, abaCode, getTitle(formPropertyMap.get(CustomFormConstants.ABA_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.ABA_CODE).getTitle() : wfmStrings.abaCode()));
        } else {
            addField(ABA_CODE, abaCode, getTitle(wfmStrings.abaCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.AGENT_ID) != null) {
            addField(CustomFormConstants.AGENT_ID, agentID, getTitle(formPropertyMap.get(CustomFormConstants.AGENT_ID).isChanged() ? formPropertyMap.get(CustomFormConstants.AGENT_ID).getTitle() : wfmStrings.agentID()));
        } else {
            addField(AGENT_ID, agentID, getTitle(wfmStrings.agentID()));
        }

        addTitleField(FINANCIAL_INFORMATION, wfmStrings.financialInformation());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CURRENCY) != null) {
            addField(CustomFormConstants.CURRENCY, currency, getTitle(formPropertyMap.get(CustomFormConstants.CURRENCY).isChanged() ? formPropertyMap.get(CustomFormConstants.CURRENCY).getTitle() : wfmStrings.currency()));
        } else {
            addField(CURRENCY, currency, getTitle(wfmStrings.currency()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.OPENING_BALANCE_DATE) != null) {
            addField(CustomFormConstants.OPENING_BALANCE_DATE, openingBalanceDate, getTitle(formPropertyMap.get(CustomFormConstants.OPENING_BALANCE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.OPENING_BALANCE_DATE).getTitle() : wfmStrings.openingBalanceAsOfDate()));
        } else {
            addField(OPENING_BALANCE_DATE, openingBalanceDate, getTitle(wfmStrings.openingBalanceAsOfDate()));
        }
        HorizontalPanel amountPanel = new HorizontalPanel();
        amountPanel.add(openingBalanceAmountCurrencyLabel);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.AMOUNT_TABLE) != null) {
            addField(CustomFormConstants.AMOUNT_TABLE, amountPanel, getTitle(formPropertyMap.get(CustomFormConstants.AMOUNT_TABLE).isChanged() ? formPropertyMap.get(CustomFormConstants.AMOUNT_TABLE).getTitle() : wfmStrings.amount()));
        } else {
            addField(AMOUNT_TABLE, amountPanel, getTitle(wfmStrings.amount()));
        }

        addTitleField(ATTACHMENTS, wfmStrings.attachments());
        addField(ATTACHMENTS, fileUpload, getTitle(wfmStrings.attachments(), false), true);

        //section Custom Field
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID, true);

        show();
    }

    protected void fillFormWithData() {
        bankName.setHTML(bankAccountItem.getName());
        accountCode.setHTML(bankAccountItem.getCode());
        accountNumber.setHTML(bankAccountItem.getAccountNumber());
        owner.setHTML(Optional.ofNullable(bankAccountItem.getOwnerNames()).orElse(""));
        accountName.setHTML(bankAccountItem.getAccauntName());
        bankBranch.setHTML(bankAccountItem.getBankBranch());
        bankAddress.setHTML(bankAccountItem.getBankAddress());
        swiftCode.setHTML(bankAccountItem.getSwiftCode());
        ibanCode.setHTML(bankAccountItem.getIbanCode());
        sortCode.setHTML(bankAccountItem.getSortCode());
        abaCode.setHTML(bankAccountItem.getAbaCode());
        agentID.setHTML(bankAccountItem.getAgentID());
        streetAddress.setHTML(bankAccountItem.getStreetAddress());
        city.setHTML(bankAccountItem.getCity());
        country.setHTML(bankAccountItem.getCountryName());
        state.setHTML(bankAccountItem.getStateName());
        postCode.setHTML(bankAccountItem.getPostCode());
        phoneNumber.setHTML(bankAccountItem.getPhoneNumber());
        currency.setHTML(bankAccountItem.getCurrency().getName());
        openingBalanceAmountCurrencyLabel.setHTML(bankAccountItem.getCurrency().getName());
        String openingBalanceAsOfDate = "";
        if (bankAccountItem.getOpeningAmount() != null) {
            openingBalanceAsOfDate = AccountingUtils.get().formatPrice(bankAccountItem.getOpeningAmount()) + " ";
        }
        if (bankAccountItem.getOpeningDate() != null && bankAccountItem.getOpeningDate().getDate() != null) {
            openingBalanceAsOfDate += DateUtils.format(bankAccountItem.getOpeningDate());
        }
        openingBalanceDate.setHTML(openingBalanceAsOfDate);
        if (bankAccountItem.isActive()) {
            active.setHTML("YES");
        } else {
            active.setHTML("NO");
        }
        getCustomFieldUtil().fillCustomFieldsWithData(bankAccountItem.getCustomFieldItems(), true);
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
