package com.edatasite.workforce.gwt.client.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTML;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 09.05.12
 * Time: 15:35
 */
public class ViewSupplierForm extends ViewClientForm {
    private HTML bankName;
    private HTML accountName;
    private HTML accountNo;
    private HTML swiftCode;
    private HTML sortCode;
    private HTML ibanCode;
    private HTML branch;
    private HTML bankAddress;
    private HTML reverseChargeApplicable;
    private HTML accountPayable;

    public ViewSupplierForm(Integer objectId) {
        super(wfmStrings.summaryView(), objectId, true, "supplier");
        setSupplierAddView(true);
        filterParametrs.setHasAccessToChange(hasEditPermission());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_EDIT, this, (sender, args) -> getDataToFillFields());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, this, (sender, args) -> getDataToFillFields());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEORDER_ADDED, this, (sender, args) -> getDataToFillFields());
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SUPPLIER_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    public void initialize() {
        super.initialize();
        bankName = new HTML();
        accountName = new HTML();
        accountNo = new HTML();
        swiftCode = new HTML();
        sortCode = new HTML();
        ibanCode = new HTML();
        branch = new HTML();
        bankAddress = new HTML();
        reverseChargeApplicable = new HTML();
        accountPayable = new HTML();
    }

    @Override
    protected void registerFields() {
        LoadingPanel.loading(true);
        CRMService.App.get().getAccount(objectId, getFormName(), new AbstractAsyncCallback<CrmAccountItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(final CrmAccountItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;
                    formPropertyMap = item.getFormProperty();
                    initialize();
                    addFieldsToForm();
                    getCustomFieldUtil().drawCustomFields(ViewSupplierForm.this, objectId, LayoutRPC.VIEW.equals(getFormType()));
                    show();
                    addItemTable(item);
                    setPluginParams(item);
                    fillFieldWithValue();
                    onTaxTreatmentChanged(item.getTaxTreatment());
                    if (o.getLogoUrl() != null && o.getLogoUrl().length() > 0) {
                        profilePicture.initialize(o.getLogoUrl(), "", "", true);
                    } else {
                        profilePicture.initialize(imageUrl, "", "", true);
                    }
                });
            }
        });
    }

    @Override
    public void addFieldsToForm() {
        super.addFieldsToForm();
        addTitleField(SUPPLIER_BANK_ACCOUNT_DETAILS, Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccountDetails(), wfmStrings.bankAccount()));

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME) != null) {
            addField(CustomFormConstants.SUPPLIER_BANK_NAME, bankName, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_NAME).getTitle() : wfmStrings.bankName()));
        } else {
            addField(SUPPLIER_BANK_NAME, bankName, getTitle(wfmStrings.bankName()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME) != null) {
            addField(CustomFormConstants.SUPPLIER_ACCOUNT_NAME, accountName, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NAME).getTitle() : wfmStrings.accountName()));
        } else {
            addField(SUPPLIER_ACCOUNT_NAME, accountName, getTitle(wfmStrings.accountName()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER) != null) {
            addField(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER, accountNo, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_ACCOUNT_NUMBER).getTitle() : wfmStrings.accountNo()));
        } else {
            addField(SUPPLIER_ACCOUNT_NUMBER, accountNo, getTitle(wfmStrings.accountNo()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE) != null) {
            addField(CustomFormConstants.SUPPLIER_SWIFT_CODE, swiftCode, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_SWIFT_CODE).getTitle() : wfmStrings.swiftCode()));
        } else {
            addField(SUPPLIER_SWIFT_CODE, swiftCode, getTitle(wfmStrings.swiftCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE) != null) {
            addField(CustomFormConstants.SUPPLIER_SORT_CODE, sortCode, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_SORT_CODE).getTitle() : wfmStrings.sortCode()));
        } else {
            addField(SUPPLIER_SORT_CODE, sortCode, getTitle(wfmStrings.sortCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE) != null) {
            addField(CustomFormConstants.SUPPLIER_IBAN_CODE, ibanCode, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_IBAN_CODE).getTitle() : wfmStrings.ibanCode()));
        } else {
            addField(SUPPLIER_IBAN_CODE, ibanCode, getTitle(wfmStrings.ibanCode()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH) != null) {
            addField(CustomFormConstants.SUPPLIER_BRANCH, branch, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_BRANCH).getTitle() : wfmStrings.branch()));
        } else {
            addField(SUPPLIER_BRANCH, branch, getTitle(wfmStrings.branch()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS) != null) {
            addField(CustomFormConstants.SUPPLIER_BANK_ADDRESS, bankAddress, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_BANK_ADDRESS).getTitle() : wfmStrings.bankAddress()));
        } else {
            addField(SUPPLIER_BANK_ADDRESS, bankAddress, getTitle(wfmStrings.bankAddress()));
        }

        addField(REVERSE_CHARGE_APPLICABLE, reverseChargeApplicable, getTitle(wfmStrings.reverseChargeApplicable()));
        addField(ACCOUNTS_RECEIVABLE_PAYABLE, accountPayable, getTitle(wfmStrings.accountsPayable()));
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
        reverseChargeApplicable.setText(item.isReverseChargeApplicable() ? wfmStrings.yes() : wfmStrings.no());
        if (item.getAccountsReceivablePayable() != null) {
            accountPayable.setHTML(item.getAccountsReceivablePayable().getName());
        }
        if (panel != null) {
            if (panel.getElementById("customerOpeningBalance") != null && panel.getElementById("supplierOpeningBalance") != null) {
                panel.getElementById("customerOpeningBalance").getStyle().setDisplay(Style.Display.NONE);
                panel.getElementById("supplierOpeningBalance").getStyle().setDisplay(Style.Display.BLOCK);
            }
            if (panel.getElementById("customerOpeningBalanceTitle") != null && panel.getElementById("supplierOpeningBalanceTitle") != null) {
                panel.getElementById("customerOpeningBalanceTitle").getStyle().setDisplay(Style.Display.NONE);
                panel.getElementById("supplierOpeningBalanceTitle").getStyle().setDisplay(Style.Display.NONE);
            }
        }
    }


    @Override
    protected void onSendToTargetClicked(boolean isClient) {
        super.onSendToTargetClicked(false);

    }

    @Override
    protected boolean hasEditPermission() {
        return Utils.hasPermission(Utils.isAccounting() ? ACCOUNTING_SUPPLIER_EDIT : LOGISTICS_SUPPLIER_EDIT);
    }

    @Override
    protected boolean hasDeletePermission() {
        return Utils.hasPermission(Utils.isAccounting() ? ACCOUNTING_SUPPLIER_DELETE : LOGISTICS_SUPPLIER_DELETE);
    }

    @Override
    protected void onDeleteActionClicked() {
        if (Utils.isBankingLocked()) {
            if (!"".equals(supplierBalanceDate.getText()) && DateUtils.getTransactionLockDate().after(item.getSupplierBalanceDate().getNonConvertedDate())) {
                Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(Property.get(Constants.CLIENT_LIST, wfmStrings.customerOpeningBalance(), wfmStrings.customer()), Utils.getTransactionLockDate()), Info.Type.WARNING);
            } else {
                super.onDeleteActionClicked();
            }
        } else {
            super.onDeleteActionClicked();
        }
    }

    @Override
    protected void onEditActionClicked() {
        SinksContainerFactory.entryPoint.onHistoryChanged("supplier|addSupplier/" + objectId, item.getNumber(), item.getName());
    }

    public String getIconStyle() {
        return "newClientList new-client-list";
    }
}
