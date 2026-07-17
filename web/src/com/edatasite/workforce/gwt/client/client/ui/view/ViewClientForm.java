package com.edatasite.workforce.gwt.client.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.ui.ViewCrmAccountForm;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 07.05.12
 * Time: 20:20
 */
public class ViewClientForm extends ViewCrmAccountForm {

    private HTML customerBalanceDate;
    private HTML customerBalanceCurrencyLabel;
    private HTML customerBalance;
    private SimpleLink nameBalanceLink; // Clickable balance shown next to the Name label (links to Statement of Account)

    protected HTML supplierBalanceDate;
    private HTML supplierBalanceCurrencyLabel;

    private HTML creditLimit;
    private HTML quoteCreditLimit;
    private HTML clientPriceLevels;
    private HTML clientDiscounts;
    private HTML subsidiary;
    private HTML clientTaxItem;
    private HTML accountReceivable;
    private HTML bankAccount;
    private HTML vatCategory;
    private HTML pasportNumber;
    private HTML warehouse;
    private HTML department;
    private Div itemTableWrap;
    private KpiSwitcher createGlAccountCheckBox;

    public ViewClientForm(String description, Integer id) {
        super("summary", description, false);
        this.objectId = id;
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_EDIT, this, (sender, args) -> getSelectedAccountItems());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, this, (sender, args) -> getSelectedAccountItems());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEQUOTE_ADDED, this, (sender, args) -> getSelectedAccountItems());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_ADD, this, (sender, args) -> getSelectedAccountItems());
    }

    public ViewClientForm(String description, Integer id, boolean isBlocked, String type) {
        super("summary", description, false, isBlocked);
        this.objectId = id;
        filterParametrs.setViewType(type);
        filterParametrs.setObjectId(objectId);
        if ("client".equals(type)) {
            filterParametrs.setHasAccessToChange(hasEditPermission());
        }
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_EDIT, this, (sender, args) -> getSelectedAccountItems());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, this, (sender, args) -> getSelectedAccountItems());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEQUOTE_ADDED, this, (sender, args) -> getSelectedAccountItems());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_ADD, this, (sender, args) -> getSelectedAccountItems());
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
                    getCustomFieldUtil().drawCustomFields(ViewClientForm.this, objectId, LayoutRPC.VIEW.equals(getFormType()));
                    show();
                    addItemTable(item);
                    setPluginParams(item);
                    fillFieldWithValue();
                    onTaxTreatmentChanged(item.getTaxTreatment());
                    if (item.getLogoUrl() != null && !item.getLogoUrl().isEmpty()) {
                        profilePicture.initialize(item.getLogoUrl(), "", "", true);
                    } else {
                        profilePicture.initialize(imageUrl, "", "", true);
                    }
                });
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CLIENT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    public void initialize() {
        super.initialize();
        customerBalanceDate = new HTML();
        customerBalanceCurrencyLabel = new HTML();
        customerBalance = new HTML();
        nameBalanceLink = new SimpleLink("");

        supplierBalanceDate = new HTML();
        supplierBalanceCurrencyLabel = new HTML();

        creditLimit = new HTML();
        quoteCreditLimit = new HTML();
        clientPriceLevels = new HTML();
        clientDiscounts = new HTML();
        subsidiary = new HTML();
        clientTaxItem = new HTML();
        accountReceivable = new HTML();
        bankAccount = new HTML();
        vatCategory = new HTML();
        pasportNumber = new HTML();
        warehouse = new HTML();
        department = new HTML();
        itemTableWrap = new Div();
        createGlAccountCheckBox = new KpiSwitcher();
    }

    @Override
    public void addFieldsToForm() {
        super.addFieldsToForm();
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_AS_OF_DATE) != null) {
            addField(CustomFormConstants.CLIENT_AS_OF_DATE, customerBalanceDate, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_AS_OF_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_AS_OF_DATE).getTitle() : wfmStrings.openingBalanceAsOfDate()));
        } else {
            addField(CLIENT_AS_OF_DATE, customerBalanceDate, wfmStrings.openingBalanceAsOfDate());
        }
        addField(CLIENT_AMOUNT_CURRENCY, customerBalanceCurrencyLabel, null);
        addField(CustomFormConstants.CLIENT_BALANCE, customerBalance, wfmStrings.balance());

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUPPLIER_AS_OF_DATE) != null) {
            addField(CustomFormConstants.SUPPLIER_AS_OF_DATE, supplierBalanceDate, getTitle(formPropertyMap.get(CustomFormConstants.SUPPLIER_AS_OF_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.SUPPLIER_AS_OF_DATE).getTitle() : wfmStrings.openingBalanceAsOfDate()));
        } else {
            addField(SUPPLIER_AS_OF_DATE, supplierBalanceDate, wfmStrings.openingBalanceAsOfDate());
        }
        addField(SUPPLIER_AMOUNT_CURRENCY, supplierBalanceCurrencyLabel, null);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WAREHOUSE) != null) {
            addField(CustomFormConstants.WAREHOUSE, warehouse, getTitle(formPropertyMap.get(CustomFormConstants.WAREHOUSE).isChanged() ? formPropertyMap.get(CustomFormConstants.WAREHOUSE).getTitle() : wfmStrings.defaultWarehouse()));
        } else {
            addField(CustomFormConstants.WAREHOUSE, warehouse, getTitle(wfmStrings.defaultWarehouse()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null) {
            addField(CustomFormConstants.DEPARTMENT, department, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT).getTitle() : wfmStrings.default2() + " " + Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
        } else {
            addField(CustomFormConstants.DEPARTMENT, department, getTitle(wfmStrings.default2() + " " + Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department())));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_CREDIT_LIMIT) != null) {
            addField(CustomFormConstants.CLIENT_CREDIT_LIMIT, creditLimit, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_CREDIT_LIMIT).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_CREDIT_LIMIT).getTitle() : wfmStrings.creditLimit()));
        } else {
            addField(CLIENT_CREDIT_LIMIT, creditLimit, getTitle(wfmStrings.creditLimit()));
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.CREDIT_LIMIT_FOR_QUOTE_ENABLED)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT) != null) {
                addField(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT, quoteCreditLimit, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_QUOTE_CREDIT_LIMIT).getTitle() : wfmStrings.quoteCreditLimit()));
            } else {
                addField(CLIENT_QUOTE_CREDIT_LIMIT, quoteCreditLimit, getTitle(wfmStrings.quoteCreditLimit()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRICE_LEVEL) != null) {
            addField(CustomFormConstants.PRICE_LEVEL, clientPriceLevels, getTitle(formPropertyMap.get(CustomFormConstants.PRICE_LEVEL).isChanged() ? formPropertyMap.get(CustomFormConstants.PRICE_LEVEL).getTitle() : wfmStrings.priceLevel()));
        } else {
            addField(PRICE_LEVEL, clientPriceLevels, getTitle(wfmStrings.priceLevel()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_DISCOUNT) != null) {
            addField(CustomFormConstants.CLIENT_DISCOUNT, clientDiscounts, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_DISCOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_DISCOUNT).getTitle() : wfmStrings.discount()));
        } else {
            addField(CLIENT_DISCOUNT, clientDiscounts, getTitle(wfmStrings.discount()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT) != null) {
            addField(CustomFormConstants.CLIENT_BANK_ACCOUNT, bankAccount, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_BANK_ACCOUNT).getTitle() : Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccount())));
        } else {
            addField(CLIENT_BANK_ACCOUNT, bankAccount, getTitle(Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccount())));
        }
        if (VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES) != null) {
                addField(CustomFormConstants.VAT_CATEGORIES, vatCategory, getTitle(formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES).isChanged() ? formPropertyMap.get(CustomFormConstants.VAT_CATEGORIES).getTitle() : Property.get(Constants.VAT_CATEGORY_LIST, wfmStrings.taxExemptionReason())));
            } else {
                addField(VAT_CATEGORIES, vatCategory, getTitle(Property.get(Constants.VAT_CATEGORY_LIST, wfmStrings.taxExemptionReason())));
            }
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTI_COMPANY_MANAGENT_SETUP) || Utils.isMultiCompanySubsidiary()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES) != null) {
                addField(CustomFormConstants.CLIENT_SUBSIDIARIES, subsidiary, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_SUBSIDIARIES).getTitle() : Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE) && Utils.isMultiCompanySubsidiary() ? wfmStrings.headOffice() : wfmStrings.subsidiary()));
            } else {
                addField(CLIENT_SUBSIDIARIES, subsidiary, getTitle(Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE) && Utils.isMultiCompanySubsidiary() ? wfmStrings.headOffice() : wfmStrings.subsidiary()));
            }

        }
        if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_VAT) != null) {
                addField(CustomFormConstants.CLIENT_VAT, clientTaxItem, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_VAT).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_VAT).getTitle() : wfmStrings.taxRate()));
            } else {
                addField(CLIENT_VAT, clientTaxItem, getTitle(wfmStrings.taxRate()));
            }
        } else if (!GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode())) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CLIENT_VAT) != null) {
                addField(CustomFormConstants.CLIENT_VAT, clientTaxItem, getTitle(formPropertyMap.get(CustomFormConstants.CLIENT_VAT).isChanged() ? formPropertyMap.get(CustomFormConstants.CLIENT_VAT).getTitle() : wfmStrings.taxRate()));
            } else {
                addField(CLIENT_VAT, clientTaxItem, getTitle(wfmStrings.taxRate()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE) != null) {
            addField(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE, accountReceivable, getTitle(formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).isChanged() ? formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).getTitle() : wfmStrings.accountsReceivable()));
        } else {
            addField(ACCOUNTS_RECEIVABLE_PAYABLE, accountReceivable, getTitle(wfmStrings.accountsReceivable()));
        }
        if (VAT_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()){
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PASSPORT_NUM) != null) {
                addField(CustomFormConstants.PASSPORT_NUM, pasportNumber, getTitle(formPropertyMap.get(CustomFormConstants.PASSPORT_NUM).isChanged() ? formPropertyMap.get(CustomFormConstants.PASSPORT_NUM).getTitle() : wfmStrings.passportNumber(), formPropertyMap.get(CustomFormConstants.PASSPORT_NUM).isRequired()));
            } else {
                addField(CustomFormConstants.PASSPORT_NUM, pasportNumber, getTitle(wfmStrings.passportNumber()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GL_ACCOUNT) != null) {
            addField(CustomFormConstants.GL_ACCOUNT, createGlAccountCheckBox, getTitle(formPropertyMap.get(CustomFormConstants.GL_ACCOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.GL_ACCOUNT).getTitle() : wfmStrings.createGLAccount()));
        } else {
            addField(CustomFormConstants.GL_ACCOUNT, createGlAccountCheckBox, getTitle(wfmStrings.createGLAccount()));
        }

        addField(CustomFormConstants.IMAGE_UPLOAD, profilePicture, null, true);
        if (panel != null) {
            if (panel.getElementById("customerOpeningBalance") != null) {
                panel.getElementById("customerOpeningBalance").getStyle().setDisplay(Style.Display.BLOCK);
            }
            if (panel.getElementById("supplierOpeningBalance") != null) {
                panel.getElementById("supplierOpeningBalance").getStyle().setDisplay(Style.Display.NONE);
            }
            if (panel.getElementById("customerOpeningBalanceTitle") != null) {
                panel.getElementById("customerOpeningBalanceTitle").getStyle().setDisplay(Style.Display.NONE);
            }
            if (panel.getElementById("supplierOpeningBalanceTitle") != null) {
                panel.getElementById("supplierOpeningBalanceTitle").getStyle().setDisplay(Style.Display.NONE);
            }
        }
    }

    /**
     * Renders the Customer balance next to the Name field's label, e.g. "Name:  Balance: 11.00".
     * When the user has access to the Statement of Account ({@link PermissionConstants#ACCOUNTING_CUSTOMER_SUMMARY})
     * the balance is a clickable link that opens the customer's Statement of Account; otherwise it is
     * shown as plain text.
     */
    @Override
    public void addField(String id, Widget widget, String title) {
        if (CustomFormConstants.CRM_ACCOUNT_NAME.equals(id) && widget == name) {
            FormGroup nameGroup = new FormGroup(name);
            Div groupLabel = nameGroup.getGroupLabel();
            groupLabel.addStyleName("label-group");
            groupLabel.add(new Span(title));

            if (item != null && item.getClientBalance() != null) {
                Span balanceWrap = new Span(wfmStrings.balance() + ": ");
                String priceStr = AccountingUtils.get().formatPrice(item.getClientBalance());
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_SUMMARY)) {
                    nameBalanceLink.setHTML("<a href=\"javascript:;\">" + priceStr + "</a>");
                    nameBalanceLink.addClickHandler(clickEvent ->
                            SinksContainerFactory.entryPoint.onHistoryChanged(
                                    "customerBalance|customerBalance/" + item.getObjectId() + "/" + CrmAccountItem.CUSTOMER,
                                    item.getNumber(), item.getName()));
                    balanceWrap.add(nameBalanceLink);
                } else {
                    balanceWrap.add(new Span(priceStr));
                }
                groupLabel.add(balanceWrap);
            }

            super.addField(id, nameGroup, null);
            return;
        }
        super.addField(id, widget, title);
    }

    @Override
    public void fillFieldWithValue() {
        super.fillFieldWithValue();
        String customerOpeningBalanceAsOfDate = "";
        if (item.getBalanceAmount() != null) {
            customerOpeningBalanceAsOfDate = numberFormat.format(item.getBalanceAmount()) + " ";
        }
        if (item.getBalanceDate() != null && item.getBalanceDate().getDate() != null) {
            customerOpeningBalanceAsOfDate += DateUtils.format(item.getBalanceDate());
        }
        customerBalanceDate.setHTML(customerOpeningBalanceAsOfDate);

        if (item.getClientBalance() != null) {
            customerBalance.setHTML(numberFormat.format(item.getClientBalance()));
        }

        String supplierOpeningBalanceAsOfDate = "";
        if (item.getSupplierBalanceAmount() != null) {
            supplierOpeningBalanceAsOfDate = numberFormat.format(item.getSupplierBalanceAmount()) + " ";
        }
        if (item.getSupplierBalanceDate() != null && item.getSupplierBalanceDate().getDate() != null) {
            supplierOpeningBalanceAsOfDate += DateUtils.format(item.getSupplierBalanceDate());
        }
        supplierBalanceDate.setHTML(supplierOpeningBalanceAsOfDate);

        if (item.getCreditLimit() != null) {
            creditLimit.setHTML(numberFormat.format(item.getCreditLimit()));
        }
        if (item.getQuoteCreditLimit() != null) {
            quoteCreditLimit.setHTML(numberFormat.format(item.getQuoteCreditLimit()));
        }
        if (item.getAppliedPriceLavel() != null && item.getAppliedPriceLavel().length > 0) {
            StringBuilder str = new StringBuilder();
            for (SelectItem price : item.getAppliedPriceLavel()) {
                if (str.toString() == "") {
                    str = new StringBuilder(price.getName());
                } else {
                    str.append(", ").append(price.getName());
                }
            }
            clientPriceLevels.setHTML(str.toString());
        }
        if (item.getAppliedDiscounts() != null && item.getAppliedDiscounts().length > 0) {
            StringBuilder str = new StringBuilder();
            for (SelectItem discount : item.getAppliedDiscounts()) {
                if (str.toString() == "") {
                    str = new StringBuilder(discount.getName());
                } else {
                    str.append(", ").append(discount.getName());
                }
            }
            clientDiscounts.setHTML(str.toString());
        }
        if (item.getSubsidiary() != null) {
            subsidiary.setHTML(item.getSubsidiary().getName());
        }
        if (item.getVat() != null) {
            clientTaxItem.setHTML(item.getVat().getName());
        }
        if (item.getAccountsReceivablePayable() != null) {
            accountReceivable.setHTML(item.getAccountsReceivablePayable().getName());
        }
        if (item.isCreateGlAccount()) {
            createGlAccountCheckBox.setValue(item.isCreateGlAccount());
        }
        createGlAccountCheckBox.setEnabled(false);
        if (item.getBankAccount() != null) {
            bankAccount.setHTML(item.getBankAccount());
        }

        if (item.getWarehouse() != null) {
            warehouse.setHTML(item.getWarehouse().getName());
        }
        if (item.getDepartment() != null) {
            department.setHTML(item.getDepartment().getName());
        }
        if (item.getVatCategory() != null) {
            vatCategory.setHTML(item.getVatCategory().getName());
        }
        if (item.getPassportNumber() != null) {
            pasportNumber.setHTML(item.getPassportNumber());
        }

        setVisible(CRM_ACCOUNT_TAX_TREATMENT, GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && isCustomerOrSupplier());
        //onTaxTreatmentChanged(taxTreatments != null ? taxTreatments.getSelectedItem() : null);
    }

    @Override
    protected void onSendToTargetClicked(boolean isClient) {
        super.onSendToTargetClicked(true);
    }

    @Override
    protected boolean hasEditPermission() {
        return Utils.isAccounting() ? Utils.hasPermission(ACCOUNTING_CUSTOMER_EDIT) : Utils.hasPermission(PermissionConstants.PM_CUSTOMER_EDIT);
    }

    @Override
    protected boolean hasDeletePermission() {
        return Utils.hasPermission(ACCOUNTING_CUSTOMER_DELETE);
    }

    @Override
    protected void onDeleteActionClicked() {
        if (Utils.isBankingLocked()) {
            if (!"".equals(customerBalanceDate.getText()) && DateUtils.getTransactionLockDate().after(item.getBalanceDate().getNonConvertedDate())) {
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
        SinksContainerFactory.entryPoint.onHistoryChanged("clientedit|addaccount/" + objectId, item.getNumber(), item.getName());
    }

    public String getIconStyle() {
        return "newClientList new-client-list";
    }
}
