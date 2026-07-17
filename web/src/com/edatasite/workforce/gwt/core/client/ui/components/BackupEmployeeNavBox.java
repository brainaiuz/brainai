package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableForBackupNavbox;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_PRIMARY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;

public class BackupEmployeeNavBox extends KpiSideNavBox {

    private final LinkedHashMap<Integer, List<ApproverItemMini>> selectItemMap = new LinkedHashMap<>();
    private final LinkedHashMap<Integer, List<ApproverItemMini>> validateMap = new LinkedHashMap<>();
    public MultiTableForBackupNavbox popupForBackupEmployee;
    public Date leaveStartDate;
    public Date leaveDueDate;
    public Integer leaveRequestEmployee;
    private List<ApproverItemMini> backupEmployees;
    private Integer activeEmployeeId;
    private boolean isSummaryForm, isValidate;
    public int mapSizeAfterSave;
    public int mapSizeBeforeSave;

    public BackupEmployeeNavBox() {
        initValues();
    }

    public static boolean isDateInBetweenIncludingEndPoints(final DateNonConvertable min, final DateNonConvertable max, final Date date) {
        if (date != null) {
            return !(date.before(min.getDate()) || date.after(max.getDate()));
        }
        return false;
    }

    public void setSummaryForm(boolean summaryForm) {
        isSummaryForm = summaryForm;
    }

    public void isValidate(boolean isValidated) {
        isValidate = isValidated;
    }

    private void initValues() {

        setSize("600", "1000");

        popupForBackupEmployee = new MultiTableForBackupNavbox(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return widgetsToPopUp(null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> widgetsMap : popupForBackupEmployee.getWidgets()) {
                    EmployeeLookUpWithCode backupEmployee = (EmployeeLookUpWithCode) widgetsMap.get(MultiTable.LOOK_UP_BOX);
                    if (backupEmployee.getSelectedItem() != null) {
                        return true;
                    }
                }
                return false;
            }
        }, false, null);

        Heading h1 = new Heading(HeadingSize.H1);
        h1.setText(wfmStrings.backupEmployee());
        addHeader(h1);

        Div header = new Div();
        header.add(popupForBackupEmployee);

        Div container = new Div();
        container.add(header);


        WfmButton2 save = new WfmButton2();
        save.setStyleName(BTN_PRIMARY);
        save.setText(wfmStrings.save());
        save.setId("save_backup_employee");

        save.addClickHandler(click -> {
            collectItems(true);
            if (isValidate && validateFields(true)) {
                collectItems(false);
                hide();
                if (popupForBackupEmployee.saveButtonListener != null) {
                    popupForBackupEmployee.saveButtonListener.execute();
                }
            } else {
                if ((validateFields(true) && validatePeriodDate())) {
                    collectItems(false);
                    hide();
                    if (popupForBackupEmployee.saveButtonListener != null) {
                        popupForBackupEmployee.saveButtonListener.execute();
                    }
                }
            }

        });


        addBody(container);
        addFooter(save);
    }

    public void collectItems(boolean forValidate) {
        backupEmployees = new ArrayList<>();
        for (HashMap<String, Widget> backupEmployeesRow : popupForBackupEmployee.getWidgets()) {
            TextBox value = (TextBox) backupEmployeesRow.get("id");
            EmployeeLookUpWithCode lookUpWithCode = (EmployeeLookUpWithCode) backupEmployeesRow.get("backupEmployees");
            DatePicker fromDate = (DatePicker) backupEmployeesRow.get("fromDate");
            fromDate.addChangeHandler(e -> {
                fromDate.removeStyleName(ERROR_FORM_STYLE);
            });
            DatePicker dueDate = (DatePicker) backupEmployeesRow.get("dueDate");
            dueDate.addChangeHandler(e -> {
                fromDate.removeStyleName(ERROR_FORM_STYLE);
            });
            if (lookUpWithCode.getSelectedItem() != null) {
                ApproverItemMini emp = new ApproverItemMini();
                emp.setObjectID(value.getText() != null && !"".equals(value.getText()) ? Integer.parseInt(value.getText()) : null);
                emp.setExactEmployee(lookUpWithCode.getSelectedItem());
                emp.setVozlojeniya(true);
                emp.setFromBackupEmployeeDate(fromDate.getDate() != null ? new DateNonConvertable(fromDate.getDate()) : null);
                emp.setDueBackupEmployeeDate(dueDate.getDate() != null ? new DateNonConvertable(dueDate.getDate()) : null);
                backupEmployees.add(emp);
//                lookUpWithCode.clear();
//                fromDate.clearSelected();
//                dueDate.clearSelected();
            }
            if (forValidate) {
                validateMap.put(activeEmployeeId, backupEmployees);
            } else {
                selectItemMap.put(activeEmployeeId, backupEmployees);
            }
        }
        mapSizeAfterSave = backupEmployees.size();

    }

    public void show() {
        super.open();
    }

    private WidgetsMap widgetsToPopUp(ApproverItemMini employee) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final TextBox id = new TextBox();
        EmployeeLookUpWithCode lookUpWithCode = new EmployeeLookUpWithCode();
        new KpiToolTip(lookUpWithCode, wfmStrings.backupEmployee());
        id.setVisible(false);
        DatePicker fromDate = new DatePicker();
        new KpiToolTip(fromDate, wfmStrings.fromDate());
        DatePicker dueDate = new DatePicker();
        new KpiToolTip(dueDate, wfmStrings.dueDate());

        if (employee != null) {
            lookUpWithCode.setSelected(employee.getExactEmployee().getId());
            id.setText(employee.getObjectID().toString());
        }
        widgetsMap.add("backupEmployees", lookUpWithCode);
        widgetsMap.add("id", id);
        widgetsMap.add("fromDate", fromDate);
        widgetsMap.add("dueDate", dueDate);
        return widgetsMap;
    }

    public void setSelectedEmployee(SelectItem selectedEmployee, int index) {
        activeEmployeeId = index;
        if (selectedEmployee == null) {
            addDefaultPopupRow();
        } else {
            addDefaultPopupRow();
            EmployeeLookUpWithCode employeeLookUpWithCode = ((EmployeeLookUpWithCode) popupForBackupEmployee.getWidgetsMaps().get(0).getWidget("backupEmployees"));
            employeeLookUpWithCode.setSelected(selectedEmployee);
            employeeLookUpWithCode.setVisible(false);
            employeeLookUpWithCode.setEnabled(false);
        }
        fillNavBoxForm(activeEmployeeId);
    }

    private void fillNavBoxForm(Integer index) {
        if (index != null && index != -1 && selectItemMap.get(activeEmployeeId) != null) {
            popupForBackupEmployee.clear();
            for (ApproverItemMini backupEmployee : selectItemMap.get(activeEmployeeId)) {
                mapSizeBeforeSave = selectItemMap.get(activeEmployeeId).size();
                WidgetsMap widgetsMap = new WidgetsMap();
                final TextBox id = new TextBox();
                final EmployeeLookUpWithCode backupEmployees = new EmployeeLookUpWithCode();
                backupEmployees.setEnabled(false);
                id.setVisible(false);
                DatePicker picker = new DatePicker();
                DatePicker picker2 = new DatePicker();
                backupEmployees.setSelected(backupEmployee.getExactEmployee());
                picker.setDate(backupEmployee.getFromBackupEmployeeDate().getDate());
                picker2.setDate(backupEmployee.getDueBackupEmployeeDate() != null ? backupEmployee.getDueBackupEmployeeDate().getDate() : null);
                widgetsMap.add("backupEmployees", backupEmployees);
                widgetsMap.add("id", id);
                widgetsMap.add("fromDate", picker);
                widgetsMap.add("dueDate", picker2);
                widgetsMap.addWidgets(backupEmployees, picker, picker2);
                popupForBackupEmployee.addWidgets(widgetsMap);
                if (isSummaryForm) {
                    backupEmployees.setVisible(false);
                }
            }
        }
    }

    public ArrayList<BackupEmployeeItem> getAllBackupEmployeeData() {
        ArrayList<BackupEmployeeItem> items = new ArrayList<>();
        selectItemMap.forEach((k, v) -> {
            BackupEmployeeItem item = new BackupEmployeeItem();
            for (ApproverItemMini mini : v) {
                if (mini.getExactEmployee().getId().equals(k)) {
                    item.setParentBackupEmployee(mini);
                } else {
                    item.getChildList().add(mini);
                }

            }
            items.add(item);
        });
        return items;
    }


    private void addDefaultPopupRow() {
        popupForBackupEmployee.clear();
        WidgetsMap widgetsMap = new WidgetsMap();
        final TextBox id = new TextBox();
        final EmployeeLookUpWithCode backupEmployees = new EmployeeLookUpWithCode();
        popupForBackupEmployee.setMinusButtonClick(() -> {
            EmployeeLookUpWithCode backupEmployees1 = (EmployeeLookUpWithCode) popupForBackupEmployee.row.getMap().getWidget("backupEmployees");
            popupForBackupEmployee.isRemovableRow = backupEmployees1.isVisible();

        });
        new KpiToolTip(backupEmployees, wfmStrings.backupEmployee());
        id.setVisible(false);
        DatePicker picker = new DatePicker();
        if (leaveStartDate != null) {
            picker.setDate(leaveStartDate);
        }
        new KpiToolTip(picker, wfmStrings.fromDate());
        DatePicker picker2 = new DatePicker();
        if (leaveDueDate != null) {
            picker2.setDate(leaveDueDate);
        }
        new KpiToolTip(picker2, wfmStrings.dueDate());
        widgetsMap.add("backupEmployees", backupEmployees);
        widgetsMap.add("id", id);
        widgetsMap.add("fromDate", picker);
        widgetsMap.add("dueDate", picker2);
        widgetsMap.addWidgets(backupEmployees, picker, picker2);
        popupForBackupEmployee.addWidgets(widgetsMap);
    }

    public boolean validateFields(boolean isFromBackup) {
        AtomicBoolean isError = new AtomicBoolean(true);
        ArrayList<ApproverItemMini> items = new ArrayList<>();
        ArrayList<Integer> empIds = new ArrayList<>();
        for (HashMap<String, Widget> popup : popupForBackupEmployee.getWidgets()) {
            EmployeeLookUpWithCode lookUpWithCode = (EmployeeLookUpWithCode) popup.get("backupEmployees");
            DatePicker fromDate = (DatePicker) popup.get("fromDate");
            DatePicker dueDate = (DatePicker) popup.get("dueDate");

            ApproverItemMini approverItemMini1 = new ApproverItemMini();
            approverItemMini1.setExactEmployee(lookUpWithCode.getSelectedItem());
            approverItemMini1.setFromBackupEmployeeDate(fromDate.getDate() != null ? new DateNonConvertable(fromDate.getDate()) : null);
            approverItemMini1.setDueBackupEmployeeDate(dueDate.getDate() != null ? new DateNonConvertable(dueDate.getDate()) : null);
            items.add(approverItemMini1);
            empIds.clear();
            validateMap.values().forEach((v) -> {
                for (int i = 0; i < v.size(); i++) {
                    if (empIds.contains(v.get(i).getExactEmployee().getId())) {
                        Info.warn(wfmStrings.employee() + " " + wfmStrings.isAlreadySelected());
                        isError.set(false);
                        v.remove(v.get(i));
                    } else {
                        empIds.add(v.get(i).getExactEmployee().getId());
                    }
                }
            });


            if (items.size() > 1) {
                for (int i = 0; i < items.size() - 1; i++) {
                    if (isFromBackup) {
                        if (items.get(i).getDueBackupEmployeeDate().getDate().after(items.get(i + 1).getFromBackupEmployeeDate().getDate())) {
                            fromDate.addStyleName(ERROR_FORM_STYLE);
                            Info.warn("Please, insert the correct date for the backup employee");
                            isError.set(false);
                        }
                        if (items.get(i).getFromBackupEmployeeDate().getDate().equals(items.get(i + 1).getDueBackupEmployeeDate().getDate())) {
                            fromDate.addStyleName(ERROR_FORM_STYLE);
                            Info.warn("Please, insert the correct date for the backup employee");
                            isError.set(false);
                        }
                    }
                    if (items.get(i).getExactEmployee().equals(items.get(i + 1).getExactEmployee())) {
                        lookUpWithCode.addStyleName(ERROR_FORM_STYLE);
                        Info.warn(wfmStrings.employee() + " " + wfmStrings.isAlreadySelected());
                        isError.set(false);
                    }
                    if (items.get(i).getFromBackupEmployeeDate().getDate().equals(items.get(i + 1).getDueBackupEmployeeDate().getDate())) {
                        fromDate.addStyleName(ERROR_FORM_STYLE);
                        Info.warn("Please, insert the correct date for the backup employee");
                        isError.set(false);
                    }
                }
            }

            if (!Validation.validateLookUpRequired(lookUpWithCode)) {
                lookUpWithCode.addStyleName(ERROR_FORM_STYLE);
                isError.set(false);
                Info.warn(wfmStrings.sureEnteredAllData());
            }
            if (!Validation.validateDate(fromDate)) {
                fromDate.addStyleName(ERROR_FORM_STYLE);
                isError.set(false);
                Info.warn(wfmStrings.sureEnteredAllData());
            }
            if (isFromBackup) {
                if (!Validation.validateDate(dueDate)) {
                    dueDate.addStyleName(ERROR_FORM_STYLE);
                    isError.set(false);
                    Info.warn(wfmStrings.sureEnteredAllData());
                }

                if (fromDate.getDate().after(dueDate.getDate())) {
                    dueDate.addStyleName(ERROR_FORM_STYLE);
                    Info.warn("Please, insert the correct date for the backup employee");
                    isError.set(false);
                }
            }
            if (lookUpWithCode.getSelectedItemID().equals(leaveRequestEmployee)) {
                lookUpWithCode.addStyleName(ERROR_FORM_STYLE);
                Info.warn(wfmStrings.employee() + " " + wfmStrings.isAlreadySelected());
                isError.set(false);
            }

        }
        return isError.get();
    }

    public boolean validatePeriodDate() {
        int errors = 0;
        boolean isError = false;
        for (HashMap<String, Widget> popup : popupForBackupEmployee.getWidgets()) {
            EmployeeLookUpWithCode lookUpWithCode = (EmployeeLookUpWithCode) popup.get("backupEmployees");
            DatePicker fromDate = (DatePicker) popup.get("fromDate");
            DatePicker dueDate = (DatePicker) popup.get("dueDate");
            if (leaveStartDate == null || leaveDueDate == null) {
                return false;
            }

            if (!isDateInBetweenIncludingEndPoints(Utils.getStartDateNC(leaveStartDate), Utils.getStartDateNC(leaveDueDate), fromDate.getDate())) {
                fromDate.addStyleName(ERROR_FORM_STYLE);
                errors++;
            }
            if (!isDateInBetweenIncludingEndPoints(Utils.getStartDateNC(leaveStartDate), Utils.getStartDateNC(leaveDueDate), dueDate.getDate())) {
                dueDate.addStyleName(ERROR_FORM_STYLE);
                errors++;
            }
            if (errors > 0) {
                Info.warn("Please, insert the correct date for the backup employee");
                isError = false;
            } else {
                isError = true;
            }
        }
        return isError;

    }

    public void setLeaveRequestSummaryValues(List<BackupEmployeeItem> backupEmployee) {
        backupEmployees = new ArrayList<>();
        for (BackupEmployeeItem backupEmployeeItem : backupEmployee) {
            selectItemMap.put(backupEmployeeItem.getParentBackupEmployee().getExactEmployee().getId(), backupEmployeeItem.getChildList());
        }
    }

    public void setValues(BackupEmployeeItem backupEmployeeItem) {
        selectItemMap.put(backupEmployeeItem.getParentBackupEmployee().getExactEmployee().getId(), backupEmployeeItem.getChildList());
    }

    public LinkedHashMap<Integer, List<ApproverItemMini>> getSelectItemMap() {
        return selectItemMap;
    }

    public void removeItemFromMapByEmployeeId(Integer id) {
        selectItemMap.remove(id);
    }

    public void removeAllMapValues() {
        selectItemMap.clear();
    }

    public void getDateFromLeave(Date startDate, Date dueDate) {
        leaveStartDate = startDate;
        leaveDueDate = dueDate;
    }

    public void getEmployeeFromLeave(Integer employeeId) {
        leaveRequestEmployee = employeeId;
    }
}
