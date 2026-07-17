package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.EmployeeProfileConstans;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.BenefitItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Aziz on 08.09.14.
 */
public class AddEditBenefitView extends CustomForm2 implements Colapse, EmployeeProfileConstans {

    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private Integer objectID;
    private WfmButton2 saveButton;
    private TextBox name, allowance;
    private DataListBox type;
    private DataListBox qtyType;
    private DataListBox currency;
    private HTMLPanel transferrablePanel;
    private HTMLPanel qRestrictionPanel;
    private RadioButton tYes, tNo;
    private RadioButton qYes, qNo;
    private DatePicker expirationDate;
    private MultiSelectEmployeeLookUp employeeLookUp;
    private TextArea2 description;
    private KpiCheckBox active;
    private KpiCheckBox applyAll;
    private AccountsLookUp debitToAccount;
    private AccountsLookUp creditToAccount;
    private final String setting_benefit = "setting-benefit";

    public AddEditBenefitView() {
        super("addBenefit", settingsStrings.addBenefit());
    }

    public AddEditBenefitView(Integer objectID) {
        super("edit", settingsStrings.editBenefit());
        this.objectID = objectID;
    }

    @Override
    protected void addButtons() {
        saveButton = addButton(wfmStrings.save(), event -> save());

    }

    private void save() {
        if (validation()) {
            saveButton.setEnabled(false);
            BenefitItem benefitItem = new BenefitItem();
            benefitItem.setObjectId(objectID);
            benefitItem.setName(name.getText());
            benefitItem.setTypeID(type.getSelectedId());
            benefitItem.setQtytypeID(qtyType.getSelectedId());
            benefitItem.setCurrencyID(currency.getSelectedId());
            benefitItem.setTransferrable(tYes.getValue());
            benefitItem.setQtyRestriction(qYes.getValue());
            if (expirationDate.getDate() != null) {
                benefitItem.setExpireDate(new DateNonConvertable(expirationDate.getDate()));
            } else {
                benefitItem.setExpireDate(null);
            }
            benefitItem.setEmployees(employeeLookUp.getSelectedItems());
            benefitItem.setActive(active.getValue());
            benefitItem.setAllowance(!allowance.getValue().equals("") ? Double.valueOf(allowance.getValue()) : 0.0);
            benefitItem.setApplyAll(applyAll.getValue());
            benefitItem.setDescription(description.getText());
            benefitItem.setDebitToAccount(debitToAccount.getSelectedItem());
            benefitItem.setCreditToAccount(creditToAccount.getSelectedItem());
            HrmsService.App.get().saveBenefit(benefitItem, new AbstractAsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable caught) {
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                    saveButton.setEnabled(true);
                }

                @Override
                public void onSuccess(Integer result) {
                    saveButton.setEnabled(true);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.benefit()), Info.Type.INFO);
                    closeTab();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ADD_OR_EDIT_BENEFIT, result, AddEditBenefitView.this);
                }
            });
        }
    }

    private boolean validation() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(type, new HTML(), "")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(qtyType, new HTML(), "")) {
            errors++;
        }
//        if (!Validation.validateLookUpRequired(debitToAccount)) {
//            errors++;
//        }
//        if (!Validation.validateLookUpRequired(creditToAccount)) {
//            errors++;
//        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        HrmsService.App.get().getBenefitData(objectID, true, new AbstractAsyncCallback<BenefitItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(BenefitItem result) {
                LoadingPanel.loading(false);
                name.setText(result.getName());
                type.setItems(result.getTypes());
                if (result.getTypeID() != null) {
                    type.setSelected(result.getTypeID());
                }
                qtyType.setItems(result.getQtyTypes());
                if (result.getQtytypeID() != null) {
                    qtyType.setSelected(result.getQtytypeID());
                }
                currency.setItems(result.getCurrencys());
                if (result.getCurrencyID() != null) {
                    currency.setEnabled("Currency".equals(qtyType.getSelectedItem().getName()));
                    currency.setSelected(result.getCurrencyID());
                }
                tNo.setValue(true);
                if (result.getTransferrable() != null)
                    tYes.setValue(result.getTransferrable());
                qNo.setValue(true);
                if (result.getQtyRestriction() != null)
                    qYes.setValue(result.getQtyRestriction());
                if (result.getExpireDate() != null)
                    expirationDate.setDate(result.getExpireDate().getNonConvertedDate());
                if (result.getEmployees() != null) {
                    employeeLookUp.setSelectedItems(result.getEmployees());
                }
                allowance.setValue(!"0.0".equals(result.getAllowance() + "") ? String.valueOf(result.getAllowance()) : "");
                applyAll.setValue(result.isApplyAll());
                description.setText(result.getDescription());
                active.setValue(objectID == null || result.isActive());
                if (result.getDebitToAccount() != null) {
                    debitToAccount.addItem(result.getDebitToAccount());
                }
                if (result.getCreditToAccount() != null) {
                    creditToAccount.addItem(result.getCreditToAccount());
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BENEFIT_FORM;
    }

    @Override
    protected String getFormType() {
        if (objectID == null)
            return LayoutRPC.ADD;
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
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        name = new TextBox();
        name.ensureDebugId(setting_benefit + "nameTextBox");
        name.addStyleName(Constants.DEFAULT_WIDTH);

        type = new DataListBox();
        type.ensureDebugId(setting_benefit + "typeListBox");
        type.addStyleName(Constants.DEFAULT_WIDTH);

        qtyType = new DataListBox();
        qtyType.addStyleName(Constants.DEFAULT_WIDTH);
        qtyType.ensureDebugId(setting_benefit + "quantityType");

        currency = new DataListBox();
        currency.addStyleName(Constants.DEFAULT_WIDTH);
        currency.setEnabled(false);
        currency.ensureDebugId(setting_benefit + "currency");

        allowance = new TextBox();
        allowance.ensureDebugId(setting_benefit + "allowance");
        Validation.addNumericKeyboardListener(allowance, 0);
        allowance.setWidth("7.2em");

        applyAll = new KpiCheckBox();
        applyAll.ensureDebugId(setting_benefit + "apllyAll_checkBox");
        applyAll.addClickHandler(clickEvent -> {
//                applyAll.setVisible(applyAll.getValue());
        });
        new KpiToolTip(applyAll, settingsStrings.allowanceForAllEmployes());

        transferrablePanel = new HTMLPanel((SafeHtml) () -> "");
        transferrablePanel.addStyleName(Constants.DEFAULT_WIDTH);
        qRestrictionPanel = new HTMLPanel((SafeHtml) () -> "");
        tYes = new KpiRadioButton("type", wfmStrings.yes());
        tYes.ensureDebugId(setting_benefit + "yesRadioButton");
        tNo = new KpiRadioButton("type", wfmStrings.no());
        tNo.ensureDebugId(setting_benefit + "noRadioButton");
        transferrablePanel.add(tYes);
        transferrablePanel.add(tNo);

        qYes = new KpiRadioButton("qtype", wfmStrings.yes());
        qNo = new KpiRadioButton("qtype", wfmStrings.no());
        qRestrictionPanel.add(qYes);
        qRestrictionPanel.add(qNo);

        type.addStyleName(Constants.DEFAULT_WIDTH);
        qtyType.addStyleName(Constants.DEFAULT_WIDTH);

        qtyType.addValueChangeHandler(changeEvent -> {
            if (qtyType.getSelectedItem() != null) {
                if ("Currency".equals(qtyType.getSelectedItem().getName())) {
                    currency.setEnabled(true);
                } else {
                    currency.setEnabled(false);
                    currency.setSelectedNullLabel();
                }
            }
        });

        expirationDate = new DatePicker();
        expirationDate.addStyleName(Constants.DEFAULT_WIDTH);
        expirationDate.ensureDebugId(setting_benefit + "expirationDate");
        employeeLookUp = new MultiSelectEmployeeLookUp();
        employeeLookUp.getFilterParametrs().setHRMS(true);
        employeeLookUp.addStyleName(Constants.DEFAULT_WIDTH);
        employeeLookUp.getTextBox().addKeyPressHandler(keyPressEvent -> employeeLookUp.clearOracleItems());

        description = new TextArea2(wfmStrings.description());
        description.ensureDebugId("rich_text_description");
        description.setWidth(Constants.MAX_DEFAULT_WIDTH);
        description.setHeight(Constants.SHORT_WIDTH);

        active = new KpiCheckBox();
        active.setValue(true);

        debitToAccount = new AccountsLookUp();
        debitToAccount.addStyleName(Constants.DEFAULT_WIDTH);
        debitToAccount.ensureDebugId(setting_benefit + "debitToAccount");
        creditToAccount = new AccountsLookUp();
        creditToAccount.addStyleName(Constants.DEFAULT_WIDTH);
        creditToAccount.ensureDebugId(setting_benefit + "creditToAccount");

        addTitleField(BENEFIT.INFORMATION, wfmStrings.information());
        addField(BENEFIT.NAME, name, getTitle(wfmStrings.name(), true));
        addField(BENEFIT.DESCRIPION, description, null, false);
        addField(BENEFIT.TYPE, type, getTitle(wfmStrings.cashableOrNot(), true));
        addField(BENEFIT.QTY_TYPE, qtyType, getTitle(wfmStrings.quantityType(), true));
        addField(BENEFIT.CURRENCY, currency, getTitle(wfmStrings.currency(), false));
        addField(BENEFIT.LEAVE_ALLOUNCE_PANEL, new AdvancedInputGroup(allowance, applyAll), wfmStrings.allowance() + ":");
        addField(BENEFIT.TRANSFERRABLE, transferrablePanel, getTitle(wfmStrings.transferrable(), false), false);
        addField(BENEFIT.ACTIVE, active, getTitle(wfmStrings.active(), false), false);
        addField(BENEFIT.EXPIRE_DATE, expirationDate, getTitle(wfmStrings.expiryDate(), false), false);
        addField(BENEFIT.DEBIT_TO_ACCOUNT, debitToAccount, getTitle(wfmStrings.debitToAccount(), false), false);
        addField(BENEFIT.CREDIT_TO_ACCOUNT, creditToAccount, getTitle(wfmStrings.creditToAccount(), false), false);

        show();
    }

    @Override
    protected void initPredefinedValues() {

    }


    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            if (BENEFIT.INFORMATION.equalsIgnoreCase(fieldID)) {
                return wfmStrings.information();
            }
        }
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
