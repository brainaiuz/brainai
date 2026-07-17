package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.importfile.client.ImportAbstractView;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;


public class ImportGroupPayrunView extends ImportAbstractView implements Constants, FormHasCustomFieldInterface {

    protected static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    private DataListBox employeeID;
    private DataListBox date;
    private DataListBox basicSalary;

    private String importGrPayrunView = "import_group_payrun_view_";

    public ImportGroupPayrunView(Integer objectId) {
        super("importgrouppayrunadd", "Import Group Payrun");
        this.objectId = objectId;
    }

    public void initialize() {
        initInternal();
        super.initialize();
    }

    private void initInternal() {
        employeeID = new DataListBox();
        employeeID.ensureDebugId(importGrPayrunView + "EmpID");
        employeeID.addStyleName(DEFAULT_WIDTH);

        date = new DataListBox();
        date.ensureDebugId(importGrPayrunView + "date");
        date.addStyleName(DEFAULT_WIDTH);

        basicSalary = new DataListBox();
        basicSalary.ensureDebugId(importGrPayrunView + "basicSalary");
        basicSalary.addStyleName(DEFAULT_WIDTH);

    }

    @Override
    public void drawForm() {
        super.drawForm();
        addTitleField(BASIC_DETAILS, wfmStrings.basicDetails());
        addField(CustomFormConstants.EMPLOYEE, employeeID, getTitle(wfmStrings.employeeId(), true));
        addField(CustomFormConstants.DATE, date, getTitle(wfmStrings.processDate(), true));
        addField(CustomFormConstants.SALARY_AMOUNT, basicSalary, getTitle(wfmStrings.basicSalary(), false));
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Project;
    }

    @Override
    public void setItems(SelectItem[] items) {
        employeeID.setItems(items, wfmStrings.employeeId());
        date.setItems(items, wfmStrings.processDate());
        basicSalary.setItems(items, wfmStrings.basicSalary());
        LoadingPanel.loading(false);
    }

    @Override
    protected ImportFile getImportFile() {
        ImportFile importFile = createColumns(getRPC());
        importFile.setFileID(objectId);
        return importFile;
    }

    private ImportFile createColumns(SinglePayrunItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.GroupPayrunFields.FIELD_EMPLOYEE, item.getEmployeeID() != null ? item.getEmployeeID() : -1);
        importFile.addColumn(ImportField.GroupPayrunFields.FIELD_DATE, item.getProcessDateId() != null ? item.getProcessDateId() : -1);
        importFile.addColumn(ImportField.GroupPayrunFields.FIELD_BASIC_SALARY, item.getBasicSalaryId() != null ? item.getBasicSalaryId() : -1);

        return importFile;
    }

    private SinglePayrunItem getRPC() {
        SinglePayrunItem item = new SinglePayrunItem();
        item.setObjectID(objectId);
        item.setEmployeeID(getSelectedItem(employeeID));
        item.setProcessDateId(getSelectedItem(date));
        item.setBasicSalaryId(getSelectedItem(basicSalary));
        return item;
    }

    public boolean validate() {
        int error = 0;

        if (!Validation.validateListBoxRequired(employeeID, new HTML(), "")) {
            error++;
        }
        if (!Validation.validateListBoxRequired(date, new HTML(), "")) {
            error++;
        }

        if (error > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        return true;
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_GROUP_PAYRUN_FORM;
    }

    @Override
    protected ImportTypeEnum getType() {
        return ImportTypeEnum.GROUP_PAYRUN;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.IMPORT;
    }

    @Override
    protected String getWikiCode() {
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