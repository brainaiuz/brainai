package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.importfile.client.ImportAbstractView;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT;
import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT_LEAVE_ALLOWANCE_FORM;

public class ImportEmployeeAllowanceView extends ImportAbstractView implements Constants, FormHasCustomFieldInterface {

    private DataListBox pinfl;
    private DataListBox startDate;
    private DataListBox allowance;
    private DataListBox left;

    public ImportEmployeeAllowanceView(Integer objectId) {
        super("importemployeeallowanceadd", "Import Leave Allowance");
        this.objectId = objectId;
    }

    public void initialize() {
        initInternal();
        super.initialize();
    }

    private void initInternal() {
        pinfl = new DataListBox();
        String importPCView = "import_leave_allowance_view_";
        pinfl.ensureDebugId(importPCView + "pinfl");
        pinfl.addStyleName(DEFAULT_WIDTH);
        pinfl.addValueChangeHandler(handler -> pinfl.removeStyleName(Constants.ERROR_FORM_STYLE));

        startDate = new DataListBox();
        startDate.ensureDebugId(importPCView + "startDate");
        startDate.addStyleName(DEFAULT_WIDTH);
        startDate.addValueChangeHandler(handler -> startDate.removeStyleName(Constants.ERROR_FORM_STYLE));

        allowance = new DataListBox();
        allowance.ensureDebugId(importPCView + "allowance");
        allowance.addStyleName(DEFAULT_WIDTH);
        allowance.addValueChangeHandler(handler -> allowance.removeStyleName(Constants.ERROR_FORM_STYLE));

        left = new DataListBox();
        left.ensureDebugId(importPCView + "left");
        left.addStyleName(DEFAULT_WIDTH);
        left.addValueChangeHandler(handler -> left.removeStyleName(Constants.ERROR_FORM_STYLE));
    }

    @Override
    public void drawForm() {
        super.drawForm();
        addField(CustomFormConstants.EMPLOYEE_CODE, pinfl, getTitle(wfmStrings.employeeCode(), true));
        addField(CustomFormConstants.START_DATE, startDate, getTitle(wfmStrings.startDate(), true));
        addField(CustomFormConstants.ANNUAL_ALLOWANCE, allowance, getTitle(wfmStrings.allowance(), true));
        addField(CustomFormConstants.LEFT_DAYS, left, getTitle(wfmStrings.leftDays(), true));
    }

    @Override
    protected String getFormID() {
        return IMPORT_LEAVE_ALLOWANCE_FORM;
    }

    @Override
    protected String getFormType() {
        return IMPORT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.ImportLeaveAllowanceView;
    }

    @Override
    public void setItems(SelectItem[] items) {
        pinfl.setItems(items, wfmStrings.employeeCode());
        startDate.setItems(items, wfmStrings.startDate());
        allowance.setItems(items, wfmStrings.allowance());
        left.setItems(items, wfmStrings.leftDays());
    }

    private ImportFile createColumns(ProfileItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.EmployeeLeaveAllowanceFields.FIELD_PINFL, item.getPinflID() != null ? item.getPinflID() : -1);
        importFile.addColumn(ImportField.EmployeeLeaveAllowanceFields.FIELD_START_DATE, item.getStartDateID() != null ? item.getStartDateID() : -1);
        importFile.addColumn(ImportField.EmployeeLeaveAllowanceFields.FIELD_ALLOWANCE, item.getAllowanceID() != null ? item.getAllowanceID() : -1);
        importFile.addColumn(ImportField.EmployeeLeaveAllowanceFields.FIELD_LEFT, item.getLeftDaysID() != null ? item.getLeftDaysID() : -1);

        return importFile;
    }

    private ProfileItem getRPC() {
        ProfileItem item = new ProfileItem();
        item.setObjectId(objectId);
        item.setPinflID(getSelectedItem(pinfl));
        item.setStartDateID(getSelectedItem(startDate));
        item.setAllowanceID(getSelectedItem(allowance));
        item.setLeftDaysID(getSelectedItem(left));
        return item;
    }

    @Override
    protected ImportFile getImportFile() {
        ImportFile importFile = createColumns(getRPC());
        importFile.setFileID(objectId);
        return importFile;
    }

    @Override
    protected ImportTypeEnum getType() {
        return ImportTypeEnum.ANNUAL_ALLOWANCE;
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
