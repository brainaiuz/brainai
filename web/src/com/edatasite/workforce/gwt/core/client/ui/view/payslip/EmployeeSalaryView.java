package com.edatasite.workforce.gwt.core.client.ui.view.payslip;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SalaryHistoryService;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

import java.math.BigDecimal;

public class EmployeeSalaryView  extends KpiModal {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final Integer employeeID;
    private final Integer objectID;
    private SalaryHistory item;

    private TextBox salary;
    private DatePicker effectiveDate;
    private TextBox relationType;
    private WfmButton2 saveButton;

    public EmployeeSalaryView(Integer employeeID) {
        this(employeeID, null);
    }

    public EmployeeSalaryView(Integer employeeID, Integer objectID) {
        this.employeeID = employeeID;
        this.objectID = objectID;
        setTitle(objectID != null ? wfmStrings.addLocation() : wfmStrings.location());
        setWidth(400);
        init();
        loadData();
        open();
    }

    private void init() {
        salary = new TextBox();
        salary.ensureDebugId("add_salary_salaryBox");

        effectiveDate = new DatePicker();
        effectiveDate.ensureDebugId("add_salary_effectiveDate");

        relationType = new TextBox();
        relationType.ensureDebugId("add_salary_relationType");

        addWidget(salary, wfmStrings.basicSalary());
        addWidget(effectiveDate, wfmStrings.effectiveDate());
        addWidget(relationType, wfmStrings.related());

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(saveButton);
    }

    private void loadData() {
        if (objectID != null) {
            SalaryHistoryService.App.get().get(objectID, new AsyncCallback<SalaryHistory>() {
                public void onFailure(Throwable caught) {

                }

                public void onSuccess(SalaryHistory result) {
                    item = result;
                    salary.setText(Utils.getCalculationNumberFormat().format(item.getSalary()));
                    effectiveDate.setDate(item.getEffectiveDate() != null ? item.getEffectiveDate().getNonConvertedDate() : null);
                    relationType.setText(result.getRelationType());
                }
            });
        }
    }

    public void save() {
        if(!validate()){
            return;
        }
        if (item == null) {
            item = new SalaryHistory();
            item.setEmployeeId(employeeID);
        }
        item.setSalary(BigDecimal.valueOf(Utils.getCalculationNumberFormat().parse(salary.getText())));
        item.setEffectiveDate(new DateNonConvertable(effectiveDate.getDate()));
        item.setRelationType(relationType.getText());
        saveButton.setEnabled(false);
        LoadingPanel.loading(true, EmployeeSalaryView.this);
        SalaryHistoryService.App.get().save(item, new AsyncCallback<Integer>() {
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(true, EmployeeSalaryView.this);
                saveButton.setEnabled(true);
            }

            public void onSuccess(Integer result) {
                LoadingPanel.loading(true, EmployeeSalaryView.this);
                Info.show(wfmStrings.messSuccessfullySaved(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALARY_ADD, null, null);
                close();
            }
        });
    }

    public boolean validate() {
        int errors = 0;

        if (!Validation.validateTextBoxRequired(salary)) {
            errors++;
        }
        if (!Validation.validateDate(effectiveDate)) {
            errors++;
        }

        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        }
        return true;
    }
}
