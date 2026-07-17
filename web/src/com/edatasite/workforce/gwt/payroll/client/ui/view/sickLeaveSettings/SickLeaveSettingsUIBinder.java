package com.edatasite.workforce.gwt.payroll.client.ui.view.sickLeaveSettings;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.listener.DropdownListener;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SickLeaveSettings;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

public class SickLeaveSettingsUIBinder {
    interface ISickLeaveSettingsUIBinder extends UiBinder<HTMLPanel, SickLeaveSettingsUIBinder> {
    }

    private static final ISickLeaveSettingsUIBinder ourUiBinder = GWT.create(ISickLeaveSettingsUIBinder.class);
    private final HTMLPanel rootElement;

    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    WfmButton2 saveButton;
    @UiField
    Span titleLabel;
    @UiField
    FormGroup fullSickDays;
    @UiField
    FormGroup halfSickDays;
    @UiField
    FormGroup noSickDdays;
    @UiField
    FormGroup serviceMinPeriod;
    @UiField
    FormGroup sickLeaveCategoy;
    @UiField
    FormGroup paymentType;
    @UiField
    FormGroup allowance;

    private TextBox fullPaidTextBox;
    private TextBox halfPaidTextBox;
    private TextBox unPaidTextBox;
    private TextBox minPeriodTextBox;
    private CategoryLookUp sickLCategoryLookUp;
    private WfmDropdown paymentTypeDropDown;

    private DynamicTable allowancesTable;

    private SickLeaveSettings localData;

    public SickLeaveSettingsUIBinder() {
        rootElement = ourUiBinder.createAndBindUi(this);
    }

    public void init() {

        fullPaidTextBox = new TextBox();
        halfPaidTextBox = new TextBox();
        unPaidTextBox = new TextBox();
        minPeriodTextBox = new TextBox();

        sickLCategoryLookUp = new CategoryLookUp("Payment");
        paymentTypeDropDown = new WfmDropdown(false, true);

        titleLabel.setText(wfmStrings.annualLeaveSettings());
        fullSickDays.setLabel(payrollStrings.fullPaidSickLeaveDays());
        fullSickDays.addToContent(fullPaidTextBox);
        halfSickDays.setLabel(payrollStrings.halfPaidSickLeaveDays());
        halfSickDays.addToContent(halfPaidTextBox);
        noSickDdays.setLabel(wfmStrings.unPaidDays());
        noSickDdays.addToContent(unPaidTextBox);
        serviceMinPeriod.setLabel(payrollStrings.minPeriodOfService());
        serviceMinPeriod.addToContent(minPeriodTextBox);
        sickLeaveCategoy.setLabel(payrollStrings.sickLCategory());
        sickLeaveCategoy.addToContent(sickLCategoryLookUp);
        paymentType.setLabel(wfmStrings.paymentType());
        paymentType.addToContent(paymentTypeDropDown);

        Validation.addNumericKeyboardListener(fullPaidTextBox, 0);
        Validation.addNumericKeyboardListener(halfPaidTextBox, 0);
        Validation.addNumericKeyboardListener(unPaidTextBox, 0);
        Validation.addNumericKeyboardListener(minPeriodTextBox, 0);

        initPaymentTypeDropDown();

        initAllowancesTable();

        PayrollService.App.get().getSickLeaveSettings(new AsyncCallback<SickLeaveSettings>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.show(wfmStrings.error(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(SickLeaveSettings sickLeaveSettings) {
                localData = sickLeaveSettings;
                setValues();
            }
        });

        saveButton.setText(wfmStrings.save());
        saveButton.setStyleName(WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(event -> save());
    }

    private void initAllowancesTable() {
        allowancesTable = new DynamicTable(getAllowanceTableColumns(), true);
        allowancesTable.addRow(getWidgets(null));
        allowancesTable.addListener(new AddListener() {
            public void plusClicked(int rowId) {
                allowancesTable.insertRow(rowId + 1, getWidgets(null));
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {

            }
        });
        allowancesTable.setBorderWidth(0);
        setAllowanceVisible(false);
        allowance.addToContent(allowancesTable);
    }

    private void setAllowanceVisible(boolean visible) {
        allowancesTable.setVisible(visible);
    }
    
    private void setValues() {
        if (localData != null) {
            fullPaidTextBox.setValue(String.valueOf(localData.getFullyPaidLeaveDays()));
            halfPaidTextBox.setValue(String.valueOf(localData.getHalfPaidLeaveDays()));
            unPaidTextBox.setValue(String.valueOf(localData.getUnPaidLeaveDays()));
            minPeriodTextBox.setValue(String.valueOf(localData.getUnPaidLeaveDays()));
            sickLCategoryLookUp.setSelected(localData.getSickLeaveCategory());
            if (localData.getAllowances().size() > 0) {
                allowancesTable.clear();
                setAllowanceVisible(true);
                paymentTypeDropDown.setSelected(1);
                for (PaymentDeductionSelectItem item : localData.getAllowances()) {
                    allowancesTable.addRow(getWidgets(item));
                }
            }
        }
    }

    private void initPaymentTypeDropDown() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(0, wfmStrings.basicSalary());
        items[1] = new SelectItem(1, wfmStrings.basicAllowancePay());
        paymentTypeDropDown.addItems(items);
        paymentTypeDropDown.setSelected(0);
        paymentTypeDropDown.addEventHandler(new DropdownListener() {
            @Override
            public void itemSelected() {
                setAllowanceVisible(paymentTypeDropDown.getSelectedId() == 1);
            }

            @Override
            public void saveNewItem() {

            }
        });
    }

    private DynamicTableColumn[] getAllowanceTableColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[1];
        columns[0] = new DynamicTableColumn(wfmStrings.allowance(), "allowance", 450);
        return columns;
    }

    private Widget[] getWidgets(PaymentDeductionSelectItem item) {
        Widget[] widgets = new Widget[1];
        CategoryLookUp allowanceLookUp = new CategoryLookUp(PayrollConstants.CATEGORY_PAYMENT);
//        allowanceLookUp.getSuggestBox().getElement().setAttribute("style", "width:250px !important");
        if (item != null) {
            allowanceLookUp.addCategoryItem(item);
        }
        widgets[0] = allowanceLookUp;
        return widgets;
    }

    private void save() {
        if (!validation()) {
            return;
        }
        LoadingPanel.loading(true);
        PayrollService.App.get().saveSickLeaveSettings(getData(), new AsyncCallback<TestRPC>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.error(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(TestRPC testRPC) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.leaveRequest()));
            }
        });
    }

    private boolean validation() {
        int error = 0;
        StringBuilder sb = new StringBuilder();
        if (!Validation.validateIntegerTextBoxRequired(fullPaidTextBox)) {
            sb.append(wfmMessages.notFilled(payrollStrings.fullPaidSickLeaveDays())).append("<br/>");
            error++;
        }
        if (!Validation.validateIntegerTextBoxRequired(halfPaidTextBox)) {
            sb.append(wfmMessages.notFilled(payrollStrings.halfPaidSickLeaveDays())).append("<br/>");
            error++;
        }
        if (!Validation.validateIntegerTextBoxRequired(unPaidTextBox)) {
            sb.append(wfmMessages.notFilled(wfmStrings.unPaidDays()));
            error++;
        }
        if (!Validation.validateIntegerTextBoxRequired(minPeriodTextBox)) {
            sb.append(wfmMessages.notFilled(payrollStrings.minPeriodOfService())).append("<br/>");
            error++;
        }
        if (!Validation.validateLookUpRequired(sickLCategoryLookUp)) {
            sb.append(wfmMessages.notSelected(payrollStrings.sickLCategory())).append("<br/>");
            error++;
        }
        if (!Validation.validateWfmDropdown(paymentTypeDropDown)) {
            sb.append(wfmMessages.notSelected(wfmStrings.paymentType())).append("<br/>");
            error++;
        }

        if (error > 0) {
            Info.show(sb.toString(), Info.Type.WARNING);
            return false;
        }

        int fullPaidLeaveDays = Integer.parseInt(fullPaidTextBox.getValue());
        int halfPaidLeaveDays = Integer.parseInt(halfPaidTextBox.getValue());
        int unPaidLeaveDays = Integer.parseInt(unPaidTextBox.getValue());
        if (fullPaidLeaveDays + halfPaidLeaveDays + unPaidLeaveDays > 364) {
            sb.append(payrollStrings.totalPaidLeavesSumFault()).append("<br/>");
            return false;
        }
        return true;
    }

    private SickLeaveSettings getData() {
        SickLeaveSettings sl = new SickLeaveSettings();
        sl.setFullyPaidLeaveDays(Integer.parseInt(fullPaidTextBox.getValue()));
        sl.setHalfPaidLeaveDays(Integer.parseInt(halfPaidTextBox.getValue()));
        sl.setUnPaidLeaveDays(Integer.parseInt(unPaidTextBox.getValue()));
        sl.setMinPeriodOfService(Integer.parseInt(minPeriodTextBox.getValue()));
        sl.setSickLeaveCategory(sickLCategoryLookUp.getSelectedItem());
        if (paymentTypeDropDown.getSelectedId() == 1) {
            for (int i = 0; i < allowancesTable.getRowNumber(); i++) {
                DynamicTableItem item = allowancesTable.getItem(i);
                CategoryLookUp allowanceLookUp = (CategoryLookUp) item.getColumnById("allowance");
                if (allowanceLookUp.getSelectedData() != null) {
                    sl.getAllowances().add(allowanceLookUp.getSelectedData());
                }
            }
        }
        return sl;
    }

    public HTMLPanel getRootElement() {
        return rootElement;
    }
}
