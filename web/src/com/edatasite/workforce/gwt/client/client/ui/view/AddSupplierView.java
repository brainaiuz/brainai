package com.edatasite.workforce.gwt.client.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.ui.view.crmsubitemtable.CrmSubItemTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 5/31/11
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddSupplierView extends AddClientView {
    private CheckBox reverseChargeApplicable;
    private TextBox bankName;
    private TextBox accountName;
    private TextBox accountNo;
    private TextBox swiftCode;
    private TextBox sortCode;
    private TextBox ibanCode;
    private TextBox branch;
    private TextArea bankAddress;
    private CrmSubItemTable itemTable;

    public AddSupplierView(Integer objectId) {
        super("addSupplier");
        property = new Property(getPropertyCode());
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.supplier()));
        if (objectId != null) {
            setDescription(property.getSingular(wfmStrings.editSupplier(), wfmStrings.supplier()));
            super.viewName = property.getSingular(wfmStrings.editSupplier(), wfmStrings.supplier());
            super.objectId = objectId;
        }
        setSupplierAddView(true);
    }

    @Override
    protected boolean cityDistrictEnabledForAddress() {
        return false;
    }

    @Override
    protected void registerFields() {
        LoadingPanel.loading(true);
        ClientService.App.get().editAccount(objectId, SUPPLIER, new AbstractAsyncCallback<CrmAccountItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(CrmAccountItem clientData) {
                LoadingPanel.loading(false);
                formPropertyMap = clientData.getFormProperty();
                initialize();
                addFieldsToForm();
                getCustomFieldUtil().drawCustomFields(AddSupplierView.this, objectId, LayoutRPC.VIEW.equals(getFormType()));
                show();

                if (clientData.getLogoUrl() != null && clientData.getLogoUrl().length() > 0) {
                    profilePicture.initialize(clientData.getLogoUrl(), "", "", true);
                } else {
                    profilePicture.initialize(imageUrl, "", "", true);
                }

                setItem(clientData);
                fillFieldWithValue();
                fillPriceLavel(clientData.getCurrencyId());

                itemTable = new CrmSubItemTable(clientData);
                itemTableWrap.add(itemTable);

                if (objectId == null) {
                    setDefaultValues();
                    setDefaultValuesByFormProperty();
                }
            }
        });
    }

    @Override
    public boolean isSupplierAddView() {
        return true;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SUPPLIER_FORM;
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    public AddSupplierView(Integer objectId, String[] params) {
        this(objectId);
        this.isAddView = "add".equals(params[0]);
        if (params != null && params.length > 1 && params[1] != null) {
            FORM_NAME = params[1];
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        bankName = new TextBox(true);
        bankName.ensureDebugId("bankName-textBox");
        accountName = new TextBox(true);
        accountName.ensureDebugId("accountName-textBox");
        accountNo = new TextBox(true);
        accountNo.ensureDebugId("accountNo-textBox");
        swiftCode = new TextBox(true);
        swiftCode.ensureDebugId("swiftcode-textBox");
        sortCode = new TextBox(true);
        sortCode.ensureDebugId("sortCode-textBox");
        ibanCode = new TextBox(true);
        ibanCode.ensureDebugId("ibanCode-textBox");
        branch = new TextBox(true);
        branch.ensureDebugId("branch-textBox");
        bankAddress = new TextArea();
        bankAddress.ensureDebugId("bankAddress-textArea");
        reverseChargeApplicable = new CheckBox();
    }

    @Override
    public void addFieldsToForm() {
        super.addFieldsToForm();
        addTitleField(CustomFormConstants.SUPPLIER_BANK_ACCOUNT_DETAILS, Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccountDetails(), wfmStrings.bankAccount()));

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME) != null) {
            addField(CustomFormConstants.SUPPLIER_BANK_NAME, bankName, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).getTitle() : wfmStrings.bankName(), formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).isInformation());
            bankName.setEnabled(!formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).isInformation()){
                new KpiToolTip(bankName,formPropertyMap.get(SUPPLIER_BANK_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SUPPLIER_BANK_NAME, bankName, getTitle(wfmStrings.bankName()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME) != null) {
            addField(CustomFormConstants.SUPPLIER_ACCOUNT_NAME, accountName, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).getTitle() : wfmStrings.accountName(), formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).isInformation());
            accountName.setEnabled(!formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).isInformation()){
                new KpiToolTip(accountName,formPropertyMap.get(SUPPLIER_ACCOUNT_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SUPPLIER_ACCOUNT_NAME, accountName, getTitle(wfmStrings.accountName()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER) != null) {
            addField(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER, accountNo, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).getTitle() : wfmStrings.accountNo(), formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).isInformation());
            accountNo.setEnabled(!formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).isDisabled());
            if(formPropertyMap.get(SUPPLIER_ACCOUNT_NUMBER).isInformation()){
                new KpiToolTip(accountNo,formPropertyMap.get(SUPPLIER_ACCOUNT_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER, accountNo, getTitle(wfmStrings.accountNo()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE) != null) {
            addField(CustomFormConstants.SUPPLIER_SWIFT_CODE, swiftCode, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).getTitle() : wfmStrings.swiftCode(), formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).isInformation());
            swiftCode.setEnabled(!formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).isDisabled());
            if(formPropertyMap.get(SUPPLIER_SWIFT_CODE).isInformation()){
                new KpiToolTip(swiftCode,formPropertyMap.get(SUPPLIER_SWIFT_CODE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SUPPLIER_SWIFT_CODE, swiftCode, getTitle(wfmStrings.swiftCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE) != null) {
            addField(CustomFormConstants.SUPPLIER_SORT_CODE, sortCode, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).getTitle() : wfmStrings.sortCode(), formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).isInformation());
            sortCode.setEnabled(!formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).isDisabled());
            if (formPropertyMap.get(SUPPLIER_SORT_CODE).isInformation()){
                new KpiToolTip(sortCode,formPropertyMap.get(SUPPLIER_SORT_CODE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SUPPLIER_SORT_CODE, sortCode, getTitle(wfmStrings.sortCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE) != null) {
            addField(CustomFormConstants.SUPPLIER_IBAN_CODE, ibanCode, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).getTitle() : wfmStrings.ibanCode(), formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).isInformation());
            ibanCode.setEnabled(!formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).isDisabled());
            if (formPropertyMap.get(SUPPLIER_IBAN_CODE).isInformation()){
                new KpiToolTip(ibanCode,formPropertyMap.get(SUPPLIER_IBAN_CODE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SUPPLIER_IBAN_CODE, ibanCode, getTitle(wfmStrings.ibanCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH) != null) {
            addField(CustomFormConstants.SUPPLIER_BRANCH, branch, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).getTitle() : wfmStrings.branch(), formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).isInformation());
            branch.setEnabled(!formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).isDisabled());
            if (formPropertyMap.get(SUPPLIER_BRANCH).isInformation()){
                new KpiToolTip(branch,formPropertyMap.get(SUPPLIER_BRANCH).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SUPPLIER_BRANCH, branch, getTitle(wfmStrings.branch()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS) != null) {
            addField(CustomFormConstants.SUPPLIER_BANK_ADDRESS, bankAddress, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).getTitle() : wfmStrings.bankAddress(), formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).isInformation());
            bankAddress.setEnabled(!formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).isDisabled());
            if (formPropertyMap.get(SUPPLIER_BANK_ADDRESS).isInformation()){
                new KpiToolTip(bankAddress,formPropertyMap.get(SUPPLIER_BANK_ADDRESS).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SUPPLIER_BANK_ADDRESS, bankAddress, getTitle(wfmStrings.bankAddress()));
        }

        addField(CustomFormConstants.SUPPLIER_ITEM_TABLE, itemTableWrap, null, true);
    }

    public void fillPriceLavel(Integer currenceID) {
        if (currenceID != null) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setCurrencyID(currenceID);
            fp.setCorporate(true);
            fp.setSpecialOffer(true);
            fp.setShowHidden(true);
            PriceLevelService.App.get().getPriceLevelList(fp, new AsyncCallback<ListResult<PriceLevelItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ListResult<PriceLevelItem> result) {
                    SelectItem all = new SelectItem(0, "<b>" + wfmStrings.selectAll() + "</b>");
                    initClientListWidget(all, result, appliedPriceLavel);
                }
            });
        }
    }

    @Override
    protected void getDataToFillFields() {
    }

    @Override
    public void fillFieldWithValue() {
        super.fillFieldWithValue();
        bankName.setText(item.getBankName());
        accountName.setText(item.getAccountName());
        accountNo.setText(item.getAccountNo());
        swiftCode.setText(item.getSwiftCode());
        if (item.getSortCode() != null) {
            sortCode.setText(item.getSortCode());
        }
        ibanCode.setText(item.getIbanCode());
        branch.setText(item.getBranch());
        bankAddress.setText(item.getBankAddress());
        reverseChargeApplicable.setValue(item.isReverseChargeApplicable());
    }

    public void setItem(CrmAccountItem item) {
        super.setItem(item);
        this.item = item;
    }

    @Override
    public void save() {
        if (!validate()) {
            enableButton(true);
            return;
        }
        setValuesToRPC();
        LoadingPanel.loading(false);
        enableButton(false);
        CRMService.App.get().saveAccount(item, CrmAccountItem.SUPPLIER, null, false, false, false, true, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer accountID) {
                LoadingPanel.loading(false);
                enableButton(true);
                if (accountID != null) {
                    if (accountID > 0) {
                        item.setObjectId(accountID);
                        Info.show(getSuccessMessage(), Info.Type.INFO);
                        onShellOk(accountID);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUPPLIER_ADD, item.getObjectId(), AddSupplierView.this);
                    } else {
                        if (accountID == -1) {
                            name.getTextBox().setFocus(true);
                            name.addStyleName(ERROR_FORM_STYLE);
                            Info.show(wfmStrings.accountWithThisCompanyNameAlreadyExists(), Info.Type.WARNING);
                        }
                        if (accountID == -2) {
                            number.setFocus(true);
                            number.addStyleName(ERROR_FORM_STYLE);
                            Info.show(wfmStrings.accountWithThisCompanyNumberAlreadyExists(), Info.Type.WARNING);
                        }
                    }
                }

            }
        });
    }

    @Override
    public void onShellOk(Integer accountID) {
        if (saveAndClose && !saveAndAddContact) {
            closeTab();
            if (objectId != null && !INVOICE_QUOTE_FORM.equals(FORM_NAME)) {
                SinksContainerFactory.entryPoint.onHistoryChanged("suppliersummary|summary/" + objectId, item.getNumber(), item.getName());
            }
        } else if (saveAndAddContact && accountID != null) {
            closeTab("contact|add/add" + "/" + accountID);
        } else {
            closeTab("supplier|add/add");
        }
    }

    @Override
    public void setValuesToRPC() {
        super.setValuesToRPC();
        item.setBankName(bankName.getText());
        item.setAccountName(accountName.getText());
        item.setAccountNo(accountNo.getText());
        item.setSwiftCode(swiftCode.getText());
        item.setSortCode(sortCode.getText());
        item.setIbanCode(ibanCode.getText());
        item.setBranch(branch.getText());
        item.setBankAddress(bankAddress.getText());
        item.setReverseChargeApplicable(reverseChargeApplicable.getValue() != null && reverseChargeApplicable.getValue());
        item.setItems(itemTable.getItemsData());
    }

    @Override
    public String getPropertyCode() {
        return Constants.SUPPLIER_LIST;
    }

    protected void setDefaultValuesByFormProperty() {
        super.setDefaultValuesByFormProperty();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).getDefaultValue() != null && bankName != null) {
            bankName.setText(formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).getDefaultValue() != null && accountName != null) {
            accountName.setText(formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).getDefaultValue() != null && accountNo != null) {
            accountNo.setText(formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).getDefaultValue() != null && accountNo != null) {
            swiftCode.setText(formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).getDefaultValue() != null && sortCode != null) {
            sortCode.setText(formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).getDefaultValue() != null && ibanCode != null) {
            ibanCode.setText(formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).getDefaultValue() != null && branch != null) {
            branch.setText(formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).getDefaultValue() != null && bankAddress != null) {
            bankAddress.setText(formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).getDefaultValue());
        }
    }

    @Override
    protected boolean validate() {
        clearErrorStyle();
        errors = 0;
        errors = super.customValidate();
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).isRequired()) {
            errors += markAsError(ibanCode, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).getTitle() : wfmStrings.ibanCode(), ibanCode, formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).isRequired()) {
            errors += markAsError(accountNo, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).getTitle() : wfmStrings.accountNo(), accountNo, formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).isRequired()) {
            errors += markAsError(bankName, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).getTitle() : wfmStrings.bankName(), bankName, formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).isRequired()) {
            errors += markAsError(accountName, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).getTitle() : wfmStrings.accountName(), accountName, formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).isRequired()) {
            errors += markAsError(swiftCode, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).getTitle() : wfmStrings.swiftCode(), swiftCode, formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).isRequired()) {
            errors += markAsError(sortCode, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).getTitle() : wfmStrings.sortCode(), sortCode, formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).isRequired()) {
            errors += markAsError(branch, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).getTitle() : wfmStrings.branch(), branch, formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS) != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).isRequired()) {
            errors += markAsError(bankAddress, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).getTitle() : wfmStrings.bankAddress(), bankAddress, formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).getMinChar()));
        }

        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return super.validate();
    }

    protected void getSelectedAccountItems() {
        LoadingPanel.loading(true);
        ClientService.App.get().editAccount(objectId, CrmAccountItem.SUPPLIER, new AbstractAsyncCallback<CrmAccountItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(CrmAccountItem clientData) {
                LoadingPanel.loading(false);

                if (objectId == null) {
                    setDefaultValues();
                }
                setItem(clientData);
                fillFieldWithValue();
                fillPriceLavel(clientData.getCurrencyId());
                itemTable = new CrmSubItemTable(clientData);
                addField(CustomFormConstants.SUPPLIER_ITEM_TABLE, itemTable, null, true);
            }
        });
    }
}
