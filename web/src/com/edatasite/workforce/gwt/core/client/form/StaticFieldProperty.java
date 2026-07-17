package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseCRMItemAndSearch;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CountryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.SmartCurrencyLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeByPermissionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceQuoteLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MeasurementsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.InvoiceTermsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollCategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.WarehouseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.TIME_TYPES;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_DATEPICKER;

public class StaticFieldProperty extends KpiModal {
    private final CustomizeFormItem field;
    private TextBox fieldName;
    private TextBox aliasName;
    private TextBox minChar;
    private DataListBox minCharCriteria;
    private KpiSwitcher mandatory;
    private KpiSwitcher disabled;
    private KpiSwitcher information;
    private KpiSwitcher approvalRelated;
    private HTML approvalAttribute;
    private FormGroup approvalForm;
    private TextBox informationText;
    private FormGroup infoTextForm;
    private KpiSelect2 roleList;
    private Widget widget;
    private Widget listBox;
    private final FormProperty formProperty;
    private WfmButton2 saveAndCloseButton;
    private ChooseCRMItemAndSearch reportedBy;
    private SelectItem[] dataItems;
    private FormGroup allowEditFormGroup;
    SelectItem[] PRODUCT_TYPES = {
            new SelectItem(1, Property.get(Constants.INVENTORY_ITEMS, KpiModal.wfmStrings.inventoryItem())),
            new SelectItem(2, Property.get(Constants.INVENTORY_ITEMS, KpiModal.wfmStrings.nonInventoryItem(), KpiModal.wfmStrings.inventoryItem())),
            new SelectItem(3, KpiModal.wfmStrings.service()),
            new SelectItem(4, Property.get(Constants.ASSEMBLY_PRODUCTS, KpiModal.wfmStrings.assemblyItem())),
            new SelectItem(5, KpiModal.wfmStrings.otherCharge()),
            new SelectItem(6, Property.get(Constants.PRODUCTS_OR_SERVICES, KpiModal.wfmStrings.product()) + " " + KpiModal.wfmStrings.group())
    };

    SelectItem[] DISCOUNT_TYPES = {
            new SelectItem(1, KpiModal.wfmStrings.fixedAmount()),
            new SelectItem(2, KpiModal.wfmStrings.percentage()),
    };
    private final SelectItem[] DATE_PICKER_TYPES = {
            new SelectItem(1, wfmStrings.equal(), "EQUAL"),
            new SelectItem(2, wfmStrings.today(), "TODAY"),
            new SelectItem(3, wfmStrings.tomorrow(), "TOMORROW"),
            new SelectItem(4, wfmStrings.yesterday(), "YESTERDAY")
    };

    StaticFieldProperty(CustomizeFormItem field) {
        modalHeader.setVisible(false);
        this.field = field;
        formProperty = field.getFormProperty();
        if ((LayoutRPC.CLIENT_FORM.equals(field.getFormID()) || LayoutRPC.SUPPLIER_FORM.equals(field.getFormID())) && CustomFormConstants.CRM_ACCOUNT_TAX_TREATMENT.equals(this.formProperty.getCode())) {
            CommonService.App.get().getTaxTreatmentItems(new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                    init();
                }

                @Override
                public void success(SelectItem[] result) {
                    super.success(result);
                    dataItems = result;
                    init();
                }
            });
        } else {
            init();
        }
    }

    private void init() {
        addStyleName("bznsGoalCustomDetModal");
        FlowPanel panel = new FlowPanel();
        panel.addStyleName("bznsGoalCustomDetModalContent");
        fieldName = new TextBox();
        aliasName = new TextBox();
        aliasName.setEnabled(false);
        mandatory = new KpiSwitcher();
        roleList = new KpiSelect2(true);
        minChar = new TextBox();
        Validation.addPhoneNumberKeyboardListener(minChar);
        minCharCriteria = new DataListBox();
        minCharCriteria.setEnabled(false);

        disabled = new KpiSwitcher();
        disabled.addValueChangeHandler(value -> {
            if (value.getValue()) {
                allowEditFormGroup.setVisible(true);
                roleList.setItems(formProperty.getAllRoles());
            } else {
                roleList.clear();
                allowEditFormGroup.setVisible(false);
            }
        });

        GColumn leftColumn = new GColumn(GColumnEnum.COL_6);
        GColumn rightColumn = new GColumn(GColumnEnum.COL_6);

        GRow mainRow = new GRow();
        mainRow.add(leftColumn);
        mainRow.add(rightColumn);

        leftColumn.add(new FormGroup(KpiModal.wfmStrings.fieldName(), fieldName));
        if ("CASE_MULTI_WIDGET".equals(formProperty.getWidget())) {
            reportedBy = new ChooseCRMItemAndSearch(true);
            leftColumn.add(new FormGroup(KpiModal.wfmStrings.defaultValue(), reportedBy));
            if (formProperty.getDefaultValue() != null && formProperty.getDefaultValue().contains("###")) {
                String[] values = formProperty.getDefaultValue().split("###");
                if (values != null && values.length > 0) {
                    int type = Integer.valueOf(values[0]);
                    String name = values[1];
                    if (LookUpConstants.CRM_LEAD_ID == type) {
                        reportedBy.setValues(LookUpConstants.CRM_LEAD, new SelectItem(formProperty.getSelectedId(), name));
                    } else if (LookUpConstants.CRM_ACCOUNT_ID == type) {
                        reportedBy.setValues(LookUpConstants.CRM_ACCOUNT, new SelectItem(formProperty.getSelectedId(), name));
                    } else if (LookUpConstants.CRM_CONTACT_ID == type) {
                        reportedBy.setValues(LookUpConstants.CRM_CONTACT, new SelectItem(formProperty.getSelectedId(), name));
                    }
                }
            }
        } else {
            if (UI_TYPE_DATEPICKER.equals(formProperty.getWidget())) {
                listBox = new DataListBox();
                ((DataListBox) listBox).setWithoutNullLabel(true);
                ((DataListBox) listBox).setItems(DATE_PICKER_TYPES);
                ((DataListBox) listBox).setStyle("display: inline-block; width: 45%");
                ((DataListBox) listBox).addValueChangeHandler(event -> {
                    if (!event.getValue().getId().equals(1)) {
                        ((DatePicker) widget).setEnabled(false);
                        ((DatePicker) widget).clearSelected();
                    } else {
                        ((DatePicker) widget).setEnabled(true);
                    }
                });
            }
            preview();
            if (widget != null) {
                if (UI_TYPE_DATEPICKER.equals(formProperty.getWidget())) {
                    FormGroup defaultFormGroup = new FormGroup();
                    defaultFormGroup.setVisible(true);
                    defaultFormGroup.setLabel(KpiModal.wfmStrings.defaultValue());
                    defaultFormGroup.add(listBox);
                    defaultFormGroup.add(widget);
                    leftColumn.add(defaultFormGroup);
                } else {
                    if (CustomFormConstants.REQUESTED_QUANTITY.equals(formProperty.getCode()) || CustomFormConstants.WAGE_RATE.equals(formProperty.getCode()) || CustomFormConstants.CLIENT_CHARGE_RATE.equals(formProperty.getCode()) || CustomFormConstants.PROBATION_DAYS.equals(formProperty.getCode())) {
                        Validation.addNumericKeyboardListener((TextBox) widget);
                        leftColumn.add(new FormGroup(KpiModal.wfmStrings.defaultValue(), widget));
                    } else
                        leftColumn.add(new FormGroup(KpiModal.wfmStrings.defaultValue(), widget));
                }
            }
        }

        rightColumn.add(new FormGroup(KpiModal.wfmStrings.aliasName(), aliasName));
        FlowPanel switchesPanel = new FlowPanel();
        switchesPanel.addStyleName("customField-switchers");
        if (!Constants.UI_TYPE_CHECKBOX.equals(formProperty.getWidget()) && !("NUMBER".equals(formProperty.getCode())) &&
                !("CODE".equals(formProperty.getCode()) && LayoutRPC.FIXED_ASSET_FORM.equals(field.getFormID())) &&
                !("RELATED_ITEM".equals(formProperty.getCode()) && LayoutRPC.FIXED_ASSET_FORM.equals(field.getFormID())) &&
                !("DEPARTMENT_NAME".equals(formProperty.getCode()) && LayoutRPC.DEPARTMENT_FORM.equals(field.getFormID())) &&
//                !("DEPARTMENT_PARENT".equals(formProperty.getCode()) && LayoutRPC.DEPARTMENT_FORM.equals(field.getFormID())) &&
                !("DEPARTMENT_LEADER".equals(formProperty.getCode()) && LayoutRPC.DEPARTMENT_FORM.equals(field.getFormID()))) {
            switchesPanel.add(new FormGroup(KpiModal.wfmStrings.mandatory(), mandatory));
        }
        switchesPanel.add(new FormGroup(KpiModal.wfmStrings.disabled(), disabled));
        rightColumn.add(switchesPanel );
        allowEditFormGroup = new FormGroup(KpiModal.wfmStrings.allowEdit(), roleList);
        allowEditFormGroup.setVisible(false);
        rightColumn.add(allowEditFormGroup);
        if (Constants.UI_TYPE_TEXTBOX.equals(formProperty.getWidget()) ||
                Constants.UI_TYPE_TEXTAREA.equals(formProperty.getWidget()) ||
                Constants.UI_TYPE_HTML_TEXTAREA.equals(formProperty.getWidget()) ||
                Constants.UI_TYPE_TEXTBOX_EMAIL.equals(formProperty.getWidget()) ||
                Constants.UI_TYPE_PHONENUMBER.equals(formProperty.getWidget()) ||
                Constants.UI_TYPE_AUTONUMBER.equals(formProperty.getWidget())) {
            FormGroup formGroup = new FormGroup(KpiModal.wfmStrings.charLimit(), new InputGroup(minCharCriteria, minChar));
            rightColumn.add(formGroup);
        }

        information = new KpiSwitcher();
        information.addValueChangeHandler(event -> {
            infoTextForm.setVisible(event.getValue());
            if (!event.getValue()) {
                informationText.setText(null);
            }
        });
        switchesPanel.add(new FormGroup(KpiModal.wfmStrings.information(), information));
        informationText = new TextBox();
        infoTextForm = new FormGroup(KpiModal.wfmStrings.text(), informationText);
        infoTextForm.setVisible(false);
        rightColumn.add(infoTextForm);

        approvalRelated = new KpiSwitcher();
        approvalRelated.addValueChangeHandler(event -> approvalForm.setVisible(event.getValue()));
        switchesPanel.add(new FormGroup(KpiModal.wfmStrings.approvalRelated(), approvalRelated));
        approvalAttribute = new HTML();
        approvalForm = new FormGroup(KpiModal.wfmStrings.attributes(), approvalAttribute);
        approvalForm.setVisible(false);
        rightColumn.add(approvalForm);

        panel.add(mainRow);
        setValues();

//        //init buttons
        saveAndCloseButton = new WfmButton2(KpiModal.wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        WfmButton2 closeButton = new WfmButton2(KpiModal.wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        saveAndCloseButton.addClickHandler(sender -> {
            saveAndCloseButton.setEnabled(false);
            save();
        });

        closeButton.addClickHandler(event -> close());
        add(panel);
        addButton(closeButton);
        addButton(saveAndCloseButton);
        open();
    }

    private void save() {
        if (validate()) {
            saveAndCloseButton.setEnabled(true);
            return;
        }
        formProperty.setTitle(fieldName.getText());
        formProperty.setRequired(mandatory.getValue());
        formProperty.setDisabled(disabled.getValue());
        ArrayList<Integer> roles = new ArrayList<>();
        if (disabled.getValue()) {
            if (roleList.getSelectedItems() != null && roleList.getSelectedItems().size() > 0) {
                roles.addAll(roleList.getSelectedItems()
                        .stream()
                        .map(SelectItem::getId)
                        .collect(Collectors.toList())
                );
            }
        }
        formProperty.setRoleEdit(roles);
        formProperty.setMinChar(minChar.getText());
        if (minChar.getText() != null && !minChar.getText().equals("")) {
            formProperty.setRequired(true);
        }

        getDefaultValue();
        if ("CASE_MULTI_WIDGET".equals(formProperty.getWidget()) && reportedBy != null && reportedBy.getReporter() != null && reportedBy.getReporter().getId() != null) {
            if (reportedBy.isLeadChecked()) {
                formProperty.setSelectedId(reportedBy.getReporter().getId());
                formProperty.setDefaultValue(LookUpConstants.CRM_LEAD_ID + "###" + reportedBy.getReporter().getName());
            } else if (reportedBy.isContactChecked()) {
                formProperty.setSelectedId(reportedBy.getReporter().getId());
                formProperty.setDefaultValue(LookUpConstants.CRM_CONTACT_ID + "###" + reportedBy.getReporter().getName());
            } else if (reportedBy.isAccountChecked()) {
                formProperty.setSelectedId(reportedBy.getReporter().getId());
                formProperty.setDefaultValue(LookUpConstants.CRM_ACCOUNT_ID + "###" + reportedBy.getReporter().getName());
            }
        }
        formProperty.setInformation(information.getValue());
        formProperty.setInformationText(informationText.getText());
        formProperty.setApprovalRelated(approvalRelated.getValue());

        CommonService.App.get().saveStaticFieldProperty(field.getFormID(), formProperty, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                saveAndCloseButton.setEnabled(true);
            }

            @Override
            public void success(Void result) {
                super.success(result);
                field.setFormProperty(formProperty);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.item()), Info.Type.INFO);
                close();
            }
        });
    }

    private void getDefaultValue() {

        if (widget != null) {

            if (widget instanceof TextBox) {
                formProperty.setDefaultValue(((TextBox) widget).getText());
            } else if (widget instanceof ReferenceLookUp) {
                ReferenceLookUp referenceLookUp = (ReferenceLookUp) widget;
                if (referenceLookUp.getSelectedItemID() != null) {
                    formProperty.setSelectedId(referenceLookUp.getSelectedItemID());
                    formProperty.setDefaultValue(referenceLookUp.getSelectedItem().getName());
                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }
            } else if (widget instanceof DataListBox && CustomFormConstants.CRM_ACCOUNT_TAX_TREATMENT.equals(formProperty.getCode())) {
                DataListBox dataListBox = (DataListBox) widget;
                if (dataListBox.getSelectedId() != null) {
                    formProperty.setSelectedId(dataListBox.getSelectedId());
                    formProperty.setDefaultValue(dataListBox.getSelectedItem().getCode() + "###" + dataListBox.getSelectedItem().getName());
                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }
            } else if (widget instanceof DataListBox && CustomFormConstants.BENEFIT_TYPE.equals(formProperty.getCode())) {
                DataListBox dataListBox = (DataListBox) widget;
                if (dataListBox.getSelectedId() != null) {
                    formProperty.setSelectedId(dataListBox.getSelectedId());
                    formProperty.setDefaultValue(dataListBox.getSelectedItem().getCode() + "###" + dataListBox.getSelectedItem().getName());

                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }
            } else if (widget instanceof DataListBox && (LayoutRPC.PRODUCT.equals(field.getFormID()) && ("FROM_PURCHASE_INVOICE".equals(formProperty.getCode()) || "SOLD_TO_CUSTOMERS".equals(formProperty.getCode())))) {
                DataListBox dataListBox = (DataListBox) widget;
                if (dataListBox.getSelectedId() != null) {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue(dataListBox.getSelectedItem().getDescription());
                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }
            } else if (widget instanceof DataListBox && (CustomFormConstants.TYPE.equals(formProperty.getCode()) || CustomFormConstants.CANDIDATE.LOCATION.equals(formProperty.getCode()) || CustomFormConstants.GOAL_VALIDITY_PERIOD.equals(formProperty.getCode()) || CustomFormConstants.COMPANY_GOAL.equals(formProperty.getCode()))) {
                DataListBox dataListBox = (DataListBox) widget;
                if (dataListBox.getSelectedId() != null) {
                    formProperty.setSelectedId(dataListBox.getSelectedId());
                    formProperty.setDefaultValue(dataListBox.getSelectedItem().getName());
                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }
            } else if (widget instanceof EmployeeLookUp) {
                EmployeeLookUp lookUp = (EmployeeLookUp) widget;
                if (lookUp.getSelectedItemID() != null) {
                    formProperty.setSelectedId(lookUp.getSelectedItemID());
                    formProperty.setDefaultValue(lookUp.getSelectedItem().getName());
                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }
            } else if (widget instanceof EmployeeLookUp && (CustomFormConstants.SUPERVISOR.equals(formProperty.getCode()))) {
                EmployeeLookUp lookUp = (EmployeeLookUp) widget;
                if (lookUp.getSelectedItemID() != null) {
                    formProperty.setSelectedId(lookUp.getSelectedItemID());
                    formProperty.setDefaultValue(lookUp.getSelectedItem().getName());
                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }

            } else if (widget instanceof EmployeeLookUp && (CustomFormConstants.POSITION.equals(formProperty.getCode()))) {
                EmployeeLookUp lookUp = (EmployeeLookUp) widget;
                if (lookUp.getSelectedItemID() != null) {
                    formProperty.setSelectedId(lookUp.getSelectedItemID());
                    formProperty.setDefaultValue(lookUp.getSelectedItem().getName());
                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }

            } else if (widget instanceof DepartmentLookUp && (CustomFormConstants.DEPARTMENT.equals(formProperty.getCode()))) {
                DepartmentLookUp departmentLookUp = (DepartmentLookUp) widget;
                if (departmentLookUp.getSelectedItemID() != null) {
                    formProperty.setSelectedId(departmentLookUp.getSelectedItemID());
                    formProperty.setDefaultValue(departmentLookUp.getSelectedItem().getName());
                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }
            } else if (widget instanceof CRMLookUp) {
                CRMLookUp lookUp = (CRMLookUp) widget;
                if (lookUp.getSelectedItemID() != null) {
                    formProperty.setSelectedId(lookUp.getSelectedItemID());
                    formProperty.setDefaultValue(lookUp.getSelectedItem().getName());
                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }
            } else if (widget instanceof ProjectLookUp) {
                ProjectLookUp lookUp = (ProjectLookUp) widget;
                if (lookUp.getSelectedItemID() != null) {
                    formProperty.setSelectedId(lookUp.getSelectedItemID());
                    formProperty.setDefaultValue(lookUp.getSelectedItem().getName());
                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }
            } else if (widget instanceof LookUp) {
                LookUp lookUp = (LookUp) widget;
                if (lookUp.getSelectedItemID() != null) {
                    formProperty.setSelectedId(lookUp.getSelectedItemID());
                    formProperty.setDefaultValue(lookUp.getSelectedItem().getName());
                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }
            } else if (widget instanceof TextArea) {
                formProperty.setDefaultValue(((TextArea) widget).getText());
            } else if (widget instanceof DatePicker) {
                Date date = ((DatePicker) widget).getDate();
                SelectItem type = ((DataListBox) listBox).getSelectedItem(true);
                if (type.getDescription().equals("EQUAL")) {
                    formProperty.setDefaultValue(date != null ? DateUtils.format(date) : null);
                } else {
                    formProperty.setDefaultValue(type.getDescription());
                }
            } else if (widget instanceof DateTimeWidget) {
                Date date = ((DateTimeWidget) widget).getDateTime();
                formProperty.setDefaultValue(date != null ? date.toString() : null);
            } else if (widget instanceof DataListBox) {
                DataListBox dataListBox = (DataListBox) widget;
                if (dataListBox.getSelectedId() != null) {
                    formProperty.setSelectedId(dataListBox.getSelectedId());
                    formProperty.setDefaultValue(dataListBox.getSelectedItem().getName());
                } else {
                    formProperty.setSelectedId(null);
                    formProperty.setDefaultValue("");
                }
            } else if (widget instanceof CurrencyWidget) {
                CurrencyWidget currencyWidget = (CurrencyWidget) widget;
                if (currencyWidget.getCurrencyID() != null) {
                    formProperty.setSelectedId(currencyWidget.getCurrencyID());
                    formProperty.setDefaultValue(currencyWidget.getCurrencyName());
                }
            }
        }
    }

    public boolean validate() {
        boolean errorFound = !Validation.validateTextBoxRequired(fieldName);

        if (minChar.getText() != null && !minChar.getText().equals("") && Integer.parseInt(minChar.getText()) == 0) {
            minChar.addStyleName(ERROR_FORM_STYLE);
            errorFound = true;
        }

        if (errorFound) {
            Info.show(KpiModal.wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
        }
        return errorFound;
    }

    private void setValues() {
        fieldName.setText(formProperty.getTitle());
        aliasName.setText(formProperty.getAliasName());
        mandatory.setValue(formProperty.isRequired());
        disabled.setValue(formProperty.isDisabled());
        minChar.setText(formProperty.getMinChar());
        minCharCriteria.setItems(new SelectItem[]{new SelectItem(1, wfmStrings.equal()), new SelectItem(2, wfmStrings.more()), new SelectItem(3, wfmStrings.less())});
        minCharCriteria.setSelected(new SelectItem(1));
        if (formProperty.isSystemRequired()) {
            mandatory.setEnabled(false);
        }

        if (disabled.getValue()) {
            allowEditFormGroup.setVisible(true);
            ArrayList<SelectItem> roles = new ArrayList<>();
            for (SelectItem role : formProperty.getAllRoles()) {
                final SelectItem roleItem = role;
                if (formProperty.getRoleEdit() != null && !formProperty.getRoleEdit().isEmpty() && formProperty.getRoleEdit().contains(role.getId())) {
                    roleItem.setSelected(true);
                }
                roles.add(roleItem);
            }
            roleList.setItems(roles);
        }
        information.setValue(formProperty.isInformation());
        if (formProperty.isInformation()) {
            informationText.setValue(formProperty.getInformationText());
            infoTextForm.setVisible(true);
        }
        if (formProperty.isApprovalRelated()) {
            approvalRelated.setValue(true);
            approvalForm.setVisible(true);
        }
        approvalAttribute.setHTML("${" + formProperty.getAliasName().toLowerCase() + "}");
    }

    private static SelectItem[] getLeavePaidTypes() {
        return new SelectItem[]{
                new SelectItem(0, "ST_PAID"),
                new SelectItem(1, "NON_PAID")
        };
    }

    private static SelectItem[] getvisibilityBox() {
        return new SelectItem[]{
                new SelectItem(0, wfmStrings.internal()),
                new SelectItem(1, wfmStrings.pub())
        };
    }

    private Widget getDropdownWidget() {
        String refereceCode = null;
        switch (field.getFormID()) {
            case LayoutRPC.CASE_FORM:
                if (CustomFormConstants.TYPE.equals(formProperty.getCode())) {
                    refereceCode = "_CASE_TYPE";
                } else if (CustomFormConstants.CASE_ORIGIN.equals(formProperty.getCode())) {
                    refereceCode = "_CASE_ORIGIN";
                } else if (CustomFormConstants.CASE_REASON.equals(formProperty.getCode())) {
                    refereceCode = "_CASE_REASON";
                } else if (CustomFormConstants.PRIORITY.equals(formProperty.getCode())) {
                    refereceCode = "_CASE_PRIORITY";
                } else if (CustomFormConstants.STATUS.equals(formProperty.getCode())) {
                    refereceCode = "_CASE_STATUS";
                } else if (CustomFormConstants.INTERNAL_STATUS.equals(formProperty.getCode())) {
                    refereceCode = "_CASE_INTERNAL_STATUS";
                }
                break;
            case LayoutRPC.OPPORTUNITY_FORM:
                if (CustomFormConstants.CRM_OPPORTUNITY_STAGE.equals(formProperty.getCode())) {
                    refereceCode = "_OPPORTUNITY_STAGE";
                } else if (CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE.equals(formProperty.getCode())) {
                    refereceCode = "_LEAD_SOURCE";
                } else if (CustomFormConstants.CRM_OPPORTUNITY_TYPE.equals(formProperty.getCode())) {
                    refereceCode = "_OPPORTUNITY_TYPE";
                } else if (CustomFormConstants.CURRENCY.equals(formProperty.getCode())) {
                    refereceCode = "CRM_ACCOUNT_CURRENCY";
                }
                break;
            case LayoutRPC.TASK_MAX_FORM:
                if (CustomFormConstants.STATUS.equals(formProperty.getCode())) {
                    refereceCode = "_TASK_STATUS";
                }
                break;
            case LayoutRPC.LEAD_FORM:
                if (CustomFormConstants.LEAD_SOURCE.equals(formProperty.getCode())) {
                    refereceCode = "_LEAD_SOURCE";
                } else if (CustomFormConstants.STATUS.equals(formProperty.getCode())) {
                    refereceCode = "_LEAD_STATUS";
                } else if (CustomFormConstants.RATING.equals(formProperty.getCode())) {
                    refereceCode = "_LEAD_RATING";
                } else if (CustomFormConstants.CRM_ACCOUNT_INDUSTRY.equals(formProperty.getCode())) {
                    refereceCode = "_COMPANY_WORKAREA";
                }
                break;
            case LayoutRPC.CONTACT_FORM:
                if (CustomFormConstants.CRM_ACCOUNT_INDUSTRY.equals(formProperty.getCode())) {
                    refereceCode = "_COMPANY_WORKAREA";
                }
                break;
            case LayoutRPC.CANDIDATE_FORM:
                if (CustomFormConstants.LEAD_SOURCE.equals(formProperty.getCode())) {
                    refereceCode = "_CANDIDATE_SOURCE";
                } else if (CustomFormConstants.STATUS.equals(formProperty.getCode())) {
                    refereceCode = "_CANDIDATE_STATUS";
                } else if (CustomFormConstants.MARTIAL_STATUS.equals(formProperty.getCode())) {
                    refereceCode = "_MARTIAL_STATUS";
                }
                break;
//            case LayoutRPC.VACANCY_FORM:
//                if (CustomFormConstants.VACANCY.VACANCY_TYPE.equals(formProperty.getCode()))
//                {
////                    refereceCode = ""
//                }
//                break;
            case LayoutRPC.CLIENT_FORM:
            case LayoutRPC.SUPPLIER_FORM:
            case LayoutRPC.ACCOUNT_FORM:
                if (CustomFormConstants.CRM_ACCOUNT_INDUSTRY.equals(formProperty.getCode())) {
                    refereceCode = "_COMPANY_WORKAREA";
                } else if (CustomFormConstants.PAYMENT_METHOD.equals(formProperty.getCode())) {
                    refereceCode = "CRM@PAYMENT_METHOD";
                } else if (CustomFormConstants.CLIENT_BANK_ACCOUNT.equals(formProperty.getCode())) {
                    refereceCode = "ACCOUNTING@BANK_ACCOUNT";
                } else if (CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE.equals(formProperty.getCode())) {
                    refereceCode = "ACCOUNTS_RECEIVABLE_PAYABLE";
                } else if (CustomFormConstants.CLIENT_TYPE.equals(formProperty.getCode())) {
                    refereceCode = "CLIENT_TYPES";
                } else if (CustomFormConstants.CURRENCY.equals(formProperty.getCode())) {
                    refereceCode = "CRM_ACCOUNT_CURRENCY";
                } else if (CustomFormConstants.CLIENT_INVOICE_TERM.equals(formProperty.getCode())) {
                    refereceCode = "ACCOUNTING@CLIENT_INVOICE_TERM";
                } else if (CustomFormConstants.CLIENT_VAT.equals(formProperty.getCode())) {
                    refereceCode = "CLIENT_VAT";
                } else if (CustomFormConstants.VAT_CATEGORIES.equals(formProperty.getCode())) {
                    refereceCode = "_VAT_CATEGORY";
                }

                break;
            case LayoutRPC.PRODUCT:
                if (CustomFormConstants.CATEGORY.equals(formProperty.getCode())) {
                    refereceCode = "PRODUCT_CATEGORY";
                } else if (CustomFormConstants.BRAND.equals(formProperty.getCode())) {
                    refereceCode = "PRODUCT_BRAND";
                } else if (CustomFormConstants.DISCOUNT_PANEL.equals(formProperty.getCode())) {
                    refereceCode = "PRODUCT_DISCOUNT";
                }
                break;
            case LayoutRPC.DEPARTMENT_GOAL_FORM:
            case LayoutRPC.BUSINESS_GOAL_FORM:
            case LayoutRPC.PROJECT_GOAL_FORM:
            case LayoutRPC.PERSONAL_GOAL_FORM:
            case LayoutRPC.COMPANY_GOAL_FORM:
                if (CustomFormConstants.GOAL_STATUS.equals(formProperty.getCode())) {
                    refereceCode = "_TASK_STATUS";
                } else if (CustomFormConstants.GOAL_SCORE_CALCULATION.equals(formProperty.getCode())) {
                    refereceCode = "_SCORE_CALCULATION";
                }
                break;
            case LayoutRPC.PM_EMPLOYEE_FORM:
                if (CustomFormConstants.MARTIAL_STATUS.equals(formProperty.getCode())) {
                    refereceCode = "_MARTIAL_STATUS";
                }
                break;
            case LayoutRPC.POSITION_FORM:
                if (CustomFormConstants.POSITIONS.STATUS.equals(formProperty.getCode())) {
                    refereceCode = "POS_STATUS";
                } else if (CustomFormConstants.POSITIONS.REG_TEMP.equals(formProperty.getCode())) {
                    refereceCode = "REG_TEMP";
                } else if (CustomFormConstants.POSITIONS.FULL_PART_TIME.equals(formProperty.getCode())) {
                    refereceCode = "TIME_TYPES";
                }
                break;
            case LayoutRPC.HRMS_EMPLOYEE_FORM:
                if (CustomFormConstants.EMPLOYMENT_MODE.equals(formProperty.getCode())) {
                    refereceCode = "_EMPLOYMENT_MODE";
                } else if (CustomFormConstants.MARTIAL_STATUS.equals(formProperty.getCode())) {
                    refereceCode = "_MARTIAL_STATUS";
                }
                break;
            case LayoutRPC.INCIDENT_FORM:
                if (CustomFormConstants.STATUS.equals(formProperty.getCode())) {
                    refereceCode = "_ISSUE_STATUS";
                } else if (CustomFormConstants.PRIORITY.equals(formProperty.getCode())) {
                    refereceCode = "_PERFORMANCE_NOTE_PRIORITIES";
                }
                break;
            case LayoutRPC.BRIGADA_FORM:
                if (CustomFormConstants.STATUS.equals(formProperty.getCode())) {
                    refereceCode = "_PROJECT_STATUS";
                }
                break;
        }
        Widget widget = null;
        if (refereceCode != null) {
            widget = new ReferenceLookUp(refereceCode);
        }

        return widget;
    }

    private static SelectItem[] getTakeLeaveTypes() {
        return new SelectItem[]{
                new SelectItem(0, Constants.DAY),
                new SelectItem(1, Constants.MONEY)
        };
    }


    private SelectItem[] getTaxCalcTypes() {
        return new SelectItem[]{
                new SelectItem(0, wfmStrings.noTax()),
                new SelectItem(1, wfmStrings.taxInclusive()),
                new SelectItem(2, wfmStrings.taxExclusive())
        };
    }

    private static SelectItem[] getGenderTable() {
        return new SelectItem[]{
                new SelectItem(1, wfmStrings.male()),
                new SelectItem(2, wfmStrings.female())
        };
    }

    private static SelectItem[] getPaymentMethods() {
        return new SelectItem[]{
                new SelectItem(1, wfmStrings.minSalary()),
                new SelectItem(2, wfmStrings.midSalary()),
                new SelectItem(3, wfmStrings.maxSalary())
        };
    }

    private static SelectItem[] getPaymentType() {
        return new SelectItem[]{
                new SelectItem(1, wfmStrings.fixedRate()),
                new SelectItem(2, wfmStrings.paymentTypeTimesheetOnly()),
                new SelectItem(3, wfmStrings.fixedHourlyRate()),
                new SelectItem(4, wfmStrings.fixedRateWithOvertime()),
                new SelectItem(5, wfmStrings.timesheet()),
                new SelectItem(6, wfmStrings.basedOnAttendanceReport())
        };
    }

    private static SelectItem[] getFamilyStatus() {
        return new SelectItem[]{
                new SelectItem(0, "Single"),
                new SelectItem(1, "Married")
        };
    }

    private static SelectItem[] getMonthItem() {
        return new SelectItem[]{
                new SelectItem(0, wfmStrings.january()),
                new SelectItem(1, wfmStrings.february()),
                new SelectItem(2, wfmStrings.march()),
                new SelectItem(3, wfmStrings.april()),
                new SelectItem(4, wfmStrings.may()),
                new SelectItem(5, wfmStrings.june()),
                new SelectItem(6, wfmStrings.july()),
                new SelectItem(7, wfmStrings.august()),
                new SelectItem(8, wfmStrings.september()),
                new SelectItem(9, wfmStrings.october()),
                new SelectItem(10, wfmStrings.november()),
                new SelectItem(11, wfmStrings.december())
        };
    }

    private static SelectItem[] getPaymentTypes() {
        return new SelectItem[]{
                new SelectItem(1, wfmStrings.fixedAmount(), "FIXED_AMOUNT"),
                new SelectItem(2, wfmStrings.basicSalary(), "BASIC_SALARY"),
                new SelectItem(3, wfmStrings.basicAllowancePay(), "BASIC_SALARY_ALLOWANCE")
        };
    }

    private static SelectItem[] getPaymentTerms() {
        return new SelectItem[]{
                new SelectItem(0, wfmStrings.fixed() + " " + wfmStrings.amount()),
                new SelectItem(1, wfmStrings.percentage())
        };
    }

    private static SelectItem[] getBooleanValue() {
        return new SelectItem[]{
                new SelectItem(0, wfmStrings.yes(), "true"),
                new SelectItem(1, wfmStrings.no(), "false`")
        };
    }

    private static SelectItem[] getSerialNumber() {
        return new SelectItem[]{
                new SelectItem(0, wfmStrings.none()),
                Utils.isInventoryTrackingEnable() ?
                        new SelectItem(1, "Track Serial Number") :
                        Utils.isBatchSerialEnable() ?
                                new SelectItem(1, "Track Batch Serials") : new SelectItem(1, wfmStrings.trackBatches())
        };
    }

    private static SelectItem[] getPayFrequencies(boolean isArabic) {
        final SelectItem[] frequencies = new SelectItem[isArabic ? 4 : Frequency.values().length - 1];
        int i = 0;
        for (Frequency frequency : Frequency.values()) {
            if (isArabic) {
                if (frequency.isForAllCountry()) {
                    frequencies[i] = new SelectItem(frequency.getId(), frequency.getName());
                    i++;
                }
            } else {
                if (Frequency.DAILY.equals(frequency)) {
                    //do not include Daily pay frequency
                    continue;
                }
                frequencies[i] = new SelectItem(frequency.getId(), frequency.getName());
                i++;
            }
        }
        return frequencies;
    }

    private static SelectItem[] getEmployeeAssignment() {
        SelectItem[] eaTypes = new SelectItem[]{new SelectItem(EmployeeAssignmentEnum.BY_POSITION.getId(), EmployeeAssignmentEnum.BY_POSITION.getTitle()),
                new SelectItem(EmployeeAssignmentEnum.BY_EMPLOYEE.getId(), EmployeeAssignmentEnum.BY_EMPLOYEE.getTitle())};
        return eaTypes;
    }

    private void preview() {
        widget = null;
        if (formProperty != null) {
            switch (formProperty.getWidget()) {
                case Constants.UI_TYPE_TEXTBOX: {
                    widget = new TextBox();
                    if ("NUMBER".equals(formProperty.getCode()) || "PURCHASE_PRICE".equals(formProperty.getCode()) || "SALES_PRICE".equals(formProperty.getCode())) {
                        Validation.addNumericKeyboardListener((TextBox) widget, Utils.getAccountingCalculationScale());
                    }

                    ((TextBox) widget).setText(formProperty.getDefaultValue() != null ? formProperty.getDefaultValue() : "");
                    break;
                }
                case Constants.UI_TYPE_AUTONUMBER:
                    widget = new TextBox();
                    Validation.addNumericKeyboardListener((TextBox) widget);
                    ((TextBox) widget).setText(formProperty.getDefaultValue() != null ? formProperty.getDefaultValue() : "");
                    break;
                case Constants.UI_TYPE_TEXTAREA: {
                    widget = new TextArea();
                    widget.addStyleName("custom-textarea");
                    if (formProperty.getDefaultValue() != null) {
                        ((TextArea) widget).setText(formProperty.getDefaultValue());
                    }
                    break;
                }
                case Constants.UI_TYPE_PERCENTAGE: {
                    widget = new TextBox();
                    ((TextBox) widget).setText(formProperty.getDefaultValue() != null ? formProperty.getDefaultValue() : "");
                    Validation.addPercentageNumericKeyboardListener((TextBox) widget, 100, (double) 100);
                    break;
                }
                case Constants.UI_TYPE_DROPDOWN: {
                    if ((LayoutRPC.CLIENT_FORM.equals(field.getFormID()) || LayoutRPC.SUPPLIER_FORM.equals(field.getFormID()) || LayoutRPC.ACCOUNT_FORM.equals(field.getFormID())) && CustomFormConstants.CRM_ACCOUNT_TAX_TREATMENT.equals(formProperty.getCode())) {

                        widget = new DataListBox();
                        if (dataItems != null && dataItems.length > 0) {
                            ((DataListBox) widget).setItems(dataItems);

                            String[] values = formProperty.getDefaultValue().split("###");
                            if (values != null && values.length > 0) {
                                String code = values[0];
                                String name = values[1];
                                SelectItem item = new SelectItem(formProperty.getSelectedId(), name);
                                item.setCode(code);
                                ((DataListBox) widget).setSelected(item);
                            }
                        }
                    } else {
                        widget = getDropdownWidget();
                        if (widget != null) {
                            if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                ((ReferenceLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                            }
                        } else {
                            widget = new DataListBox();

                            if (LayoutRPC.PRODUCT.equals(field.getFormID()) && CustomFormConstants.TYPE.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(PRODUCT_TYPES);

                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.PRODUCT.equals(field.getFormID()) && CustomFormConstants.DISCOUNT_TYPE.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(DISCOUNT_TYPES);

                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.CANDIDATE_FORM.equals(field.getFormID()) && CustomFormConstants.CANDIDATE.LOCATION.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getLocations(new AsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);

                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }
                                    }
                                });

                            } else if ((LayoutRPC.PERSONAL_GOAL_FORM.equals(field.getFormID()) || LayoutRPC.COMPANY_GOAL_FORM.equals(field.getFormID()) || LayoutRPC.PROJECT_GOAL_FORM.equals(field.getFormID()) || LayoutRPC.BUSINESS_GOAL_FORM.equals(field.getFormID()) || LayoutRPC.DEPARTMENT_GOAL_FORM.equals(field.getFormID()) || LayoutRPC.GROUP_GOAL_FORM.equals(field.getFormID())) && CustomFormConstants.GOAL_VALIDITY_PERIOD.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getValidityPeriods("VALIDITY_PERIOD_GOAL", new AsyncCallback<ArrayList<SelectItem>>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(ArrayList<SelectItem> items) {
                                        ((DataListBox) widget).setItems(items.toArray(new SelectItem[items.size()]));
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }
                                    }
                                });
                            } else if (LayoutRPC.CANDIDATE_FORM.equals(field.getFormID()) && CustomFormConstants.VACANCIES.equals(formProperty.getCode())) {
                                ListingFilterParameter fp = new ListingFilterParameter();
                                fp.setBriefly(false);
                                AllInOneService.App.get().getVacanciesList(fp, new AsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);

                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }
                                    }
                                });
                            } else if ((LayoutRPC.PROJECT_GOAL_FORM.equals(field.getFormID()) || LayoutRPC.BUSINESS_GOAL_FORM.equals(field.getFormID()) || LayoutRPC.DEPARTMENT_GOAL_FORM.equals(field.getFormID())) && CustomFormConstants.COMPANY_GOAL.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getCompanyGoals(new AsyncCallback<ArrayList<SelectItem>>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(ArrayList<SelectItem> result) {
                                        ((DataListBox) widget).setItems(result.toArray(new SelectItem[result.size()]));
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }

                                    }
                                });
                            } else if ((LayoutRPC.PAYROLL_STARTER_FORM.equals(field.getFormID()) && CustomFormConstants.PAYROLL_STARTER.PAY_METHOD.equals(formProperty.getCode()))) {
                                AllInOneService.App.get().getPaymentMethodList(new AsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] result) {
                                        ((DataListBox) widget).setItems(result);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }

                                    }
                                });
                            } else if ((LayoutRPC.BRAND_FORM.equals(field.getFormID()) && CustomFormConstants.PARENT.equals(formProperty.getCode()))) {
                                AllInOneService.App.get().getParentList(new AsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] result) {
                                        ((DataListBox) widget).setItems(result);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }

                                    }
                                });
                            } else if (LayoutRPC.FIXED_ASSET_FORM.equals(field.getFormID()) && CustomFormConstants.TAX_CALC_TYPE.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getTaxCalcTypes());

                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.HRMS_EMPLOYEE_FORM.equals(field.getFormID()) && CustomFormConstants.GENDER.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getGenderTable());

                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if ((LayoutRPC.OPPORTUNITY_FORM.equals(field.getFormID()) || LayoutRPC.RENTAL_ORDER_FORM.equals(field.getFormID())) && CustomFormConstants.TAX_CALC_TYPE.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getTaxCalcTypes());

                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.PAYROLL_STARTER_FORM.equals(field.getFormID()) && CustomFormConstants.GENDER.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getGenderTable());

                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.PAYROLL_STARTER_FORM.equals(field.getFormID()) && CustomFormConstants.PAYMENT_METHOD.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getPaymentMethods());

                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.PAYROLL_STARTER_FORM.equals(field.getFormID()) && CustomFormConstants.PAYROLL_STARTER.EMPLOYEE_IS_PAID.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getPaymentType());

                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.PAYROLL_STARTER_FORM.equals(field.getFormID()) && CustomFormConstants.PAYROLL_STARTER.FAMILY_STATUS.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getFamilyStatus());

                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.PAYROLL_STARTER_FORM.equals(field.getFormID()) && CustomFormConstants.PAYROLL_STARTER.PAY_PERIOD.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getPayFrequencies(Utils.isArabicCompany()));
                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.PROJECT_FORM.equals(field.getFormID()) && CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getEmployeeAssignment());
                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.ADDITIONAL_PAYMENT_FORM.equals(field.getFormID()) && CustomFormConstants.PERIOD.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getMonthItem());
                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.ADDITIONAL_PAYMENT_FORM.equals(field.getFormID()) && CustomFormConstants.ACCOUNTING.PAYMENT_TYPE.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getPaymentTypes());
                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.PAYROLL_CASH_ADVANCE_FORM.equals(field.getFormID()) && CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS.equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getPaymentTerms());
                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.PRODUCT.equals(field.getFormID()) && "FROM_PURCHASE_INVOICE".equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getBooleanValue());
                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.PRODUCT.equals(field.getFormID()) && "SOLD_TO_CUSTOMERS".equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getBooleanValue());
                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.PRODUCT.equals(field.getFormID()) && "SERIAL_NUMBER".equals(formProperty.getCode())) {
                                ((DataListBox) widget).setItems(getSerialNumber());
                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (LayoutRPC.LEAVE_REQUEST_FORM.equals(field.getFormID())) {
                                if (CustomFormConstants.REASON.equals(formProperty.getCode())) {
                                    AllInOneService.App.get().getReasons(Utils.getUserID(), new AsyncCallback<SelectItem[]>() {
                                        @Override
                                        public void onFailure(Throwable throwable) {

                                        }

                                        @Override
                                        public void onSuccess(SelectItem[] selectItems) {
                                            ((DataListBox) widget).setItems(selectItems);

                                            if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                                ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                            }
                                        }
                                    });
                                }
                                if (CustomFormConstants.TAKE_LIVE_TYPE.equals(formProperty.getCode())) {
                                    ((DataListBox) widget).setItems(getTakeLeaveTypes());

                                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                        ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                    }
                                }
                                if (CustomFormConstants.TYPE.equals(formProperty.getCode())) {
                                    ((DataListBox) widget).setItems(getLeavePaidTypes());

                                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                        ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                    }
                                }
                            } else if (LayoutRPC.HRMS_COMPANY_NEWS_FORM.equals(field.getFormID())) {
                                if (CustomFormConstants.NEWS_CATEGORIES.equals(formProperty.getCode())) {
                                    AllInOneService.App.get().getNewsCategories(new AbstractAsyncCallback<SelectItem[]>() {
                                        @Override
                                        public void onFailure(Throwable throwable) {

                                        }

                                        @Override
                                        public void onSuccess(SelectItem[] selectItems) {
                                            ((DataListBox) widget).setItems(selectItems);

                                            if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                                ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                            }
                                        }
                                    });
                                    if (CustomFormConstants.NEWS_VISIBILITY.equals(formProperty.getCode())) {
                                        ((DataListBox) widget).setItems(getvisibilityBox());

                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }
                                    }

                                }

                            } else if (LayoutRPC.BENEFIT_REQUEST_FORM.equals(field.getFormID()) && CustomFormConstants.BENEFIT_TYPE.equals(formProperty.getCode())) {
                                ListingFilterParameter fp = new ListingFilterParameter();
                                fp.setActive(true);
                                AllInOneService.App.get().getBenefitTypes(fp, new AsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }

                                    }
                                });

                            } else if (LayoutRPC.PROJECT_FORM.equals(field.getFormID()) && CustomFormConstants.PROJECT.LOCATION.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getLocationList(new AbstractAsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }

                                    }
                                });

                            } else if (LayoutRPC.PROJECT_FORM.equals(field.getFormID()) && CustomFormConstants.PARENT.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getParentIsNullProjects(null, new AbstractAsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }

                                    }
                                });

                            } else if (LayoutRPC.PROJECT_FORM.equals(field.getFormID()) && CustomFormConstants.MANAGER.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getManagers(new AbstractAsyncCallback<HashSet<SelectItem>>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(HashSet<SelectItem> selectItems) {
                                        ((DataListBox) widget).setItems((SelectItem[]) selectItems.toArray());
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }

                                    }
                                });

                            } else if (LayoutRPC.TASK_MAX_FORM.equals(field.getFormID()) && CustomFormConstants.PRIORITY.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getPrioritySelectItems(new AbstractAsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }

                                    }
                                });
                            } else if (LayoutRPC.HRMS_EMPLOYEE_FORM.equals(field.getFormID()) && CustomFormConstants.LOCATION_FIELD.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getLocationList(new AbstractAsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }

                                    }
                                });

                            } else if (LayoutRPC.POSITION_FORM.equals(field.getFormID()) && CustomFormConstants.POSITIONS.DEPARTMENT.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getTeamsList(new AbstractAsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }

                                    }
                                });

                            } else if (LayoutRPC.CERTIFICATE_OF_EMPLOYMENT_FORM.equals(field.getFormID()) && CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.CERTIFICATE_TYPE.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getCertificateeTypes(new AbstractAsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }

                                    }
                                });

                            } else if (LayoutRPC.INCIDENT_FORM.equals(field.getFormID()) && CustomFormConstants.RELATED_EMPLOYEES.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getDepartmentsEmployees(new AbstractAsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }

                                    }
                                });

                            } else if (LayoutRPC.RENTAL_PRODUCT_FORM.equals(field.getFormID()) && CustomFormConstants.CATEGORY.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getCategoriessAsSelectItem(new AbstractAsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }
                                    }
                                });

                            } else if (LayoutRPC.RENTAL_PRODUCT_FORM.equals(field.getFormID()) && CustomFormConstants.BRAND.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getBrandssAsSelectItem(new AbstractAsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }
                                    }
                                });
                            } else if (LayoutRPC.PAYROLL_CASH_ADVANCE_FORM.equals(field.getFormID()) && CustomFormConstants.PAYROLL_STARTER.PAYMENT_METHOD.equals(formProperty.getCode())) {
                                AllInOneService.App.get().getPaymentMethodList(new AbstractAsyncCallback<SelectItem[]>() {
                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }

                                    @Override
                                    public void onSuccess(SelectItem[] selectItems) {
                                        ((DataListBox) widget).setItems(selectItems);
                                        if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                            ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                        }
                                    }
                                });
                            } else if (CustomFormConstants.SALES_TYPE.equals(formProperty.getCode())) {
                                SelectItem[] items = {new SelectItem(1, "B2B"), new SelectItem(2, "B2C")};
                                ((DataListBox) widget).setItems(items);
                                if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                                }
                            } else if (CustomFormConstants.FEE_TYPE.equals(formProperty.getCode())) {
                                ((DataListBox) this.widget).setItems(new SelectItem[]{
                                        new SelectItem(0, "No Fee"),
                                        new SelectItem(1, "Fixed Amount"),
                                        new SelectItem(2, "Percentage")
                                });
                                if (formProperty.getSelectedId() != null) {
                                    ((DataListBox) widget).setSelected(formProperty.getSelectedId());
                                } else if (formProperty.getDefaultValue() != null) {
                                    ((DataListBox) widget).setSelectedByValue(formProperty.getDefaultValue(), true);
                                }
                            }
                        }
                    }
                    break;
                }
                case Constants.UI_TYPE_DATEPICKER: {
                    widget = new DatePicker();
                    widget.setWidth("50%");
                    widget.getElement().getStyle().setDisplay(Style.Display.INLINE_BLOCK);
                    widget.getElement().getStyle().setFloat(Style.Float.RIGHT);
                    if (formProperty.getDefaultValue() != null) {
                        if (("TODAY".equals(formProperty.getDefaultValue()) || "TOMORROW".equals(formProperty.getDefaultValue())
                                || "YESTERDAY".equals(formProperty.getDefaultValue()))) {
                            if ("TOMORROW".equals(formProperty.getDefaultValue())) {
                                ((DataListBox) listBox).setSelected(3);
                            } else if ("YESTERDAY".equals(formProperty.getDefaultValue())) {
                                ((DataListBox) listBox).setSelected(4);
                            } else {
                                ((DataListBox) listBox).setSelected(2);
                            }
                            ((DatePicker) widget).setEnabled(false);
                        } else {
                            try {
                                ((DatePicker) widget).setDate(DateUtils.parse(formProperty.getDefaultValue()));
                            } catch (DateFormatException e) {
                            }
                        }
                    }
                    break;
                }
                case Constants.UI_TYPE_DATEPICKER_TIME: {
                    widget = new DateTimeWidget(28);
                    if (formProperty.getDefaultValue() != null) {
                        try {
                            ((DateTimeWidget) widget).setDateTime(DateUtils.parseLongFormat(formProperty.getDefaultValue()));
                        } catch (DateFormatException e) {
                        }
                    }
                    break;
                }
                case Constants.UI_TYPE_LOOKUP: {

                    widget = getLookUpWidget();
                    if (widget != null) {
                        widget.addStyleName("custom-lookup");
                    }
                    break;
                }
            }
        }
    }

    private Widget getLookUpWidget() {
        Widget widget = null;
        switch (field.getFormID()) {
            case LayoutRPC.CASE_FORM:
                if (CustomFormConstants.RESOLVER.equals(formProperty.getCode()) || CustomFormConstants.ASSIGNEE.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, false, false);
                }
                break;
            case LayoutRPC.OPPORTUNITY_FORM:
                if (CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE.equals(formProperty.getCode()) || CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }

                } else if (CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.CRM_ACCOUNT_ID);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.CRM_CONTACT_ID, Constants.SUPPLIER);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.CRM_CAMPAIGN_ID);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.PROJECT_FIELD.equals(formProperty.getCode())) {
                    widget = new ProjectLookUp(null);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ProjectLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.LEAD_FORM:
            case LayoutRPC.CONTACT_FORM:
            case LayoutRPC.CANDIDATE_FORM:
                if (CustomFormConstants.ASSIGNEE.equals(formProperty.getCode()) || CustomFormConstants.BACKUP_ASSIGNEE.equals(formProperty.getCode()) || CustomFormConstants.LEAD_OWNER.equals(formProperty.getCode()) || CustomFormConstants.OWNER.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }

                } else if (CustomFormConstants.CRM_ACCOUNT_NAME.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.CRM_ACCOUNT, Constants.SUPPLIER);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.CRM_CAMPAIGN_NAME.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.CRM_CAMPAIGN_ID);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.CANDIDATE.CANDIDATE_PROJECT.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.PROJECT);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.VACANCY_FORM:
            case LayoutRPC.LOCATION_FORM:
                if (CustomFormConstants.VACANCY.JOB_TYPE.equals(formProperty.getCode())) {
                    widget = new ReferenceLookUp(TIME_TYPES);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ReferenceLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.VACANCY.VACANCY_TYPE.equals(formProperty.getCode())) {
                    widget = new ReferenceLookUp((VacancyItem._VACANCY_TYPE));
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ReferenceLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.VACANCY.REQUIRED_DEGREE.equals(formProperty.getCode())) {
                    widget = new ReferenceLookUp(VacancyItem.VACANCY_DEGREES);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ReferenceLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.VACANCY.POSITION.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.POSITION);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.VACANCY.LOCATION.equals(formProperty.getCode())) {
                    widget = new CRMLookUp("LOCATION");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.VACANCY.MANAGER.equals(formProperty.getCode())) {
                    ArrayList<Integer> roles = new ArrayList<>();
                    roles.add(Constants.ADMIN);
                    roles.add(Constants.DR);
                    roles.add(Constants.TL);
                    roles.add(Constants.PM);
                    roles.add(Constants.HR);
                    roles.add(Constants.SALESMAN);
                    widget = new EmployeeLookUp(true, roles);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
//                else if (CustomFormConstants.VACANCY.BACKUP_MANAGER.equals(formProperty.getCode())) {
//                    ArrayList<Integer> roles = new ArrayList<>();
//                    roles.add(Constants.ADMIN);
//                    roles.add(Constants.DR);
//                    roles.add(Constants.TL);
//                    roles.add(Constants.PM);
//                    roles.add(Constants.HR);
//                    roles.add(Constants.SALESMAN);
//                    widget = new EmployeeLookUp(true, roles);
//
//                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
//                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
//                    }
//                }

                else if (CustomFormConstants.VACANCY.STATUS.equals(formProperty.getCode())) {
                    widget = new ReferenceLookUp(VacancyItem.VACANCY_STATUSES);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ReferenceLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
//                } else if (CustomFormConstants.VACANCY.COUNTRY.equals(formProperty.getCode()) || CustomFormConstants.LOCATION.COUNTRY.equals(formProperty.getCode())) {
//                    widget = new CountryLookUp();
//                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
//                        ((CountryLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
//                    }
                } else if (CustomFormConstants.VACANCY.PROJECT.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.PROJECT);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.VACANCY.DEPARTMENT.equals(formProperty.getCode())) {
                    widget = new DepartmentLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((DepartmentLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
//                else if (CustomFormConstants.VACANCY.RELIGION.equals(formProperty.getCode())) {
//                    widget = new CRMLookUp(LookUpConstants.RELIGON_ID);
//                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
//                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
//                    }
//                }
                break;
            case LayoutRPC.CLIENT_FORM:
            case LayoutRPC.SUPPLIER_FORM:
            case LayoutRPC.ACCOUNT_FORM:
                if (CustomFormConstants.CRM_ACCOUNT_NAME.equals(formProperty.getCode()) || CustomFormConstants.CRM_ACCOUNT_PARENT.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.CRM_ACCOUNT_ID);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.PRIMARY_CONTACT.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.CRM_CONTACT_ID);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.CLIENT_INVOICE_TERM.equals(formProperty.getCode())) {
                    widget = new InvoiceTermsLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((InvoiceTermsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.WAREHOUSE.equals(formProperty.getCode())) {
                    widget = new WarehouseLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((WarehouseLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.DEPARTMENT.equals(formProperty.getCode())) {
                    widget = new DepartmentLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((DepartmentLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.CLIENT_VAT.equals(formProperty.getCode())) {
                    widget = new TaxLookUp(Constants.RECEIVABLE);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((TaxLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.LOGACALL_FORM:
                if (CustomFormConstants.SHARED_WITH.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, false);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.PRODUCT:
                if ("PURCHASE_ACCOUNT".equals(formProperty.getCode())) {
                    widget = new AccountsLookUp(Constants.EXPENSES);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((AccountsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if ("SALES_ACCOUNT".equals(formProperty.getCode())) {
                    widget = new AccountsLookUp(Constants.REVENUE, Constants.LIABILITIES);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((AccountsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (Constants.TAX.equals(formProperty.getCode())) {
                    widget = new TaxLookUp(Constants.RECEIVABLE);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((TaxLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if ((Constants.TAX + 2).equals(formProperty.getCode())) {
                    widget = new TaxLookUp(Constants.RECEIVABLE);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((TaxLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if ("SUPPLIERS".equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.SUPPLIER_ID);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if ("UNIT_MEASUREMENT".equals(formProperty.getCode())) {
                    widget = new MeasurementsLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((MeasurementsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if ("ASSET_ACCOUNT".equals(formProperty.getCode())) {
                    widget = new AccountsLookUp(Constants.ASSETS);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((AccountsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.PREPAYMENT_FORM:
            case LayoutRPC.SUPPLIER_CREDIT_FORM:
                if (CustomFormConstants.CRM_ACCOUNT_LOOKUP.equals(formProperty.getCode())) {
                    widget = new CrmAccountLookUp(CrmConstants.CUSTOMER, true);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CrmAccountLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.PAYMENT_ACCOUNT_LOOKUP.equals(formProperty.getCode())) {
                    widget = new PaymentAccountsLookUp(true);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((PaymentAccountsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE.equals(formProperty.getCode())) {
                    widget = new AccountsLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((AccountsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP.equals(formProperty.getCode())) {
                    widget = new InvoiceQuoteLookUp(Constants.PURCHASE_ORDER);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((InvoiceQuoteLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.PROJECT_.equals(formProperty.getCode())) {
                    widget = new ProjectLookUp(null);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ProjectLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.DEPARTMENT.equals(formProperty.getCode())) {
                    widget = new DepartmentLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((DepartmentLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.AMOUNT_PERCENTAGE.equals(formProperty.getCode())) {
                    widget = new TextBox();
                    if (formProperty.getDefaultValue() != null) {
                        ((TextBox) widget).setText(formProperty.getDefaultValue());
                    }
                } else if (CustomFormConstants.FEE_TYPE.equals(formProperty.getCode())) {
                    ((DataListBox) this.widget).setItems(new SelectItem[]{
                            new SelectItem(0, "Fixed Amount"),
                            new SelectItem(1, "Percentage")
                    });
                    if (formProperty.getSelectedId() != null) {
                        ((DataListBox) widget).setSelected(formProperty.getSelectedId());
                    } else if (formProperty.getDefaultValue() != null) {
                        ((DataListBox) widget).setSelectedByValue(formProperty.getDefaultValue(), true);
                    }
                } else if (CustomFormConstants.SALE_INVOICE_LOOKUP.equals(formProperty.getCode())) {
                    widget = new InvoiceQuoteLookUp(Constants.SALE_INVOICE);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((InvoiceQuoteLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.PLACEMENT_FORM:
                if (CustomFormConstants.PLACEMENT.DEPARTMENT.equals(formProperty.getCode())) {
                    widget = new DepartmentLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((DepartmentLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.PLACEMENT.POSITION.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.POSITION);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.PLACEMENT.PROJECT.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.PROJECT);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.PLACEMENT.CANDIDATE.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.CANDIDATE);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.PLACEMENT.LOCATION.equals(formProperty.getCode())) {
                    widget = new CRMLookUp("LOCATION");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.DEPARTMENT_GOAL_FORM:
            case LayoutRPC.BUSINESS_GOAL_FORM:
            case LayoutRPC.PROJECT_GOAL_FORM:
                if (CustomFormConstants.GOAL_PROORDEP.equals(formProperty.getCode())) {
                    widget = new DepartmentLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((DepartmentLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.GOAL_RESOLVER.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, false, false);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.DEPENDENT_FORM:
            case LayoutRPC.EDUCATION_FORM:
                if (CustomFormConstants.COUNTRY_.equals(formProperty.getCode())) {
                    widget = new CountryLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CountryLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.PERSONAL_GOAL_FORM:
                if (CustomFormConstants.GOAL_PERSONAL_ASSINESS.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, false, false);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.GROUP_GOAL_FORM:
                if (CustomFormConstants.EMPLOYEE.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUpWithCode();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUpWithCode) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.FIXED_ASSET_FORM:
                if (CustomFormConstants.OWNER.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, false, false);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.CATEGORY.equals(formProperty.getCode())) {
                    widget = new AccountsLookUp("FIXED_ASSET");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((AccountsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.TAX_VALUE.equals(formProperty.getCode())) {
                    widget = new TaxLookUp("PAYABLE");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((TaxLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.DEPARTMENT.equals(formProperty.getCode())) {
                    widget = new DepartmentLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((DepartmentLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.ACCOUNT_NAME.equals(formProperty.getCode())) {
                    widget = new AccountsLookUp("FINANCED_BY");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((AccountsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.FIXED_ASSET_ACCOUNT.equals(formProperty.getCode())) {
                    widget = new AccountsLookUp("FIXED_ASSET");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((AccountsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.EXPENSE_ACCOUNT.equals(formProperty.getCode())) {
                    widget = new AccountsLookUp("EXPENSES");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((AccountsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.ISSUE_FORM:
                if (CustomFormConstants.PROJECT_FIELD.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.PROJECT);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.PRIORITY.equals(formProperty.getCode())) {
                    widget = new ReferenceLookUp("_ISSUE_PRIORITY");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ReferenceLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.STATUS.equals(formProperty.getCode())) {
                    widget = new ReferenceLookUp("_ISSUE_STATUS");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ReferenceLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.REPORTED_BY.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, false);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.INCIDENT_FORM:
                if (CustomFormConstants.REPORTED_BY.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, false);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.RESOLVER.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, false, false);
                }
                break;
            case LayoutRPC.PAYROLL_STARTER_FORM:
                if (CustomFormConstants.ACCOUNT_STATUS.equals(formProperty.getCode())) {
                    widget = new ReferenceLookUp("_EMPLOYEE_STATUS");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ReferenceLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.APPROVER.equals(formProperty.getCode())) {
                    widget = new EmployeeByPermissionLookUp();
                    ((EmployeeByPermissionLookUp) widget).setPermissionCode(PermissionConstants.PAYROLL_EMPLOYEE_APPROVAL);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeByPermissionLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.SALARY_CURRENCY.equals(formProperty.getCode())) {
                    widget = new SmartCurrencyLookUp(true, false);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((SmartCurrencyLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.CITIZENSHIP.equals(formProperty.getCode())) {
                    widget = new CountryLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CountryLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.JOB_TITLE.equals(formProperty.getCode())) {
                    widget = new ReferenceLookUp("PAYROLL_JOB_TITLE");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ReferenceLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.SOLUTION_FORM:
                if (CustomFormConstants.ASSIGNEE.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.STATUS.equals(formProperty.getCode())) {
                    widget = new ReferenceLookUp("_SOLUTION_STATUS");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ReferenceLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.BANK_ACCOUNT_FORM:
                if (CustomFormConstants.OWNER.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, PermissionConstants.ACCOUNTING_BANKACCOUNT_ASSIGNEE_LIST_VALUE);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.COUNTRY.equals(formProperty.getCode())) {
                    widget = new CountryLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CountryLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.CURRENCY.equals(formProperty.getCode())) {
                    widget = new CurrencyWidget(true);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CurrencyWidget) widget).setCurrency(new CurrencyItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.PM_EMPLOYEE_FORM:
                if (CustomFormConstants.DEPARTMENT.equals(formProperty.getCode())) {
                    widget = new DepartmentLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((DepartmentLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.POSITION.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.POSITION);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.LOCATION_FIELD.equals(formProperty.getCode())) {
                    widget = new CRMLookUp("LOCATION");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.LEAVE_REQUEST_FORM:
                if (CustomFormConstants.EMPLOYEES.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUpWithCode();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUpWithCode) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.CHART_OF_ACCOUNT_FORM:
                if (CustomFormConstants.CHART_ACCOUNT_PARENT.equals(formProperty.getCode())) {
                    widget = new AccountsLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((AccountsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;

            case LayoutRPC.DEPARTMENT_FORM:
                if (CustomFormConstants.DEPARTMENT_CREATED_BY.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUpWithCode();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUpWithCode) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.DEPARTMENT_PARENT.equals(formProperty.getCode())) {
                    widget = new DepartmentLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((DepartmentLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.HRMS_COMPANY_NEWS_FORM:
                if (CustomFormConstants.LOCATION_FIELD.equals(formProperty.getCode())) {
                    widget = new CRMLookUp("LOCATION");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.NEWS_AUTHOR.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUp(true, false);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.BENEFIT_REQUEST_FORM:
                if (CustomFormConstants.APPROVER.equals(formProperty.getCode()) || CustomFormConstants.REQUESTER.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUpWithCode();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUpWithCode) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.PROJECT_FORM:
                if (CustomFormConstants.STATUS.equals(formProperty.getCode())) {
                    widget = new ReferenceLookUp("_PROJECT_STATUS");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ReferenceLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.TASK_MAX_FORM:
                if (CustomFormConstants.TASK.PROJECT.equals(formProperty.getCode())) {
                    widget = new CRMLookUp(LookUpConstants.PROJECT);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CRMLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.HRMS_EMPLOYEE_FORM:
                if (CustomFormConstants.SUPERVISOR.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUpWithCode();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUpWithCode) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                if (CustomFormConstants.DEPARTMENT.equals(formProperty.getCode())) {
                    widget = new DepartmentLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUpWithCode) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                if (CustomFormConstants.POSITION.equals(formProperty.getCode())) {
                    widget = new DepartmentLookUp();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUpWithCode) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;

            case LayoutRPC.CERTIFICATE_OF_EMPLOYMENT_FORM:
                if (CustomFormConstants.CERTIFICATE_OF_EMPLOYMENT.EMPLOYEE.equals(formProperty.getCode())) {
                    widget = new EmployeeLookUpWithCode();
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((EmployeeLookUpWithCode) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.RENTAL_PRODUCT_FORM:
                if ("SALES_ACCOUNT".equals(formProperty.getCode())) {
                    widget = new AccountsLookUp("REVENUE", "LIABILITIES");
                    if (formProperty.getDefaultValue() != null && formProperty.getDefaultValue() != null) {
                        ((AccountsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if ("PURCHASE_ACCOUNT".equals(formProperty.getCode())) {
                    widget = new AccountsLookUp("EXPENSES");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((AccountsLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), this.formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.ADDITIONAL_PAYMENT_FORM:
                if (CustomFormConstants.CATEGORY.equals(formProperty.getCode()) && (Utils.getLocationString().toString().contains("additionalDeduction") || Utils.getLocationString().toString().contains("additionalPayment"))) {
                    String type = Utils.getLocationString().equals("additionalDeduction") ? "Deduction" : "Payment";
                    widget = new PayrollCategoryLookUp(type);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((PayrollCategoryLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.PAYROLL_CASH_ADVANCE_FORM:
                if (CustomFormConstants.PAYROLL_STARTER.EMPLOYEE.equals(formProperty.getCode())) {
                    widget = new PayrollEmployeeLookUp(false);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((PayrollEmployeeLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                } else if (CustomFormConstants.CATEGORY.equals(formProperty.getCode())) {
                    String type = "Deduction";
                    widget = new CategoryLookUp(type, true);
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((CategoryLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
            case LayoutRPC.POSITION_FORM:
                if (CustomFormConstants.TYPE.equals(formProperty.getCode())) {
                    widget = new ReferenceLookUp("POSITION_TYPE");
                    if (formProperty.getDefaultValue() != null && formProperty.getSelectedId() != null) {
                        ((ReferenceLookUp) widget).setSelected(new SelectItem(formProperty.getSelectedId(), formProperty.getDefaultValue()));
                    }
                }
                break;
        }

        return widget;
    }

}
