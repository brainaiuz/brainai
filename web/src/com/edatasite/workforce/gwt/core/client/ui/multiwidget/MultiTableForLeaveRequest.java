package com.edatasite.workforce.gwt.core.client.ui.multiwidget;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.BackupEmployeeNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;

public class MultiTableForLeaveRequest extends MultiTableNewUI {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private Command command;
    private Command deleteBtnListener;
    private BackupEmployeeNavBox backupEmployeeNavBox;
    private boolean isViewMode;
    private boolean isAddForm, isValidate;
    private boolean isSummaryForm;
    private WfmButton2 trashButton;
    private WfmButton2 plusButton;

    public MultiTableForLeaveRequest(int i, boolean canRemoveSingleRow, MultiTableWidgets multiTableWidgets, Command command) {
        super(i, canRemoveSingleRow, multiTableWidgets);
        this.command = command;
    }

    public MultiTableForLeaveRequest(MultiTableWidgets multiTableWidgets, boolean b, BackupEmployeeNavBox backupEmployeeNavBox) {
        super(multiTableWidgets, b);
        this.backupEmployeeNavBox = backupEmployeeNavBox;
        this.command = command;
    }

    @Override
    public void setViewMode(boolean viewMode) {
        isViewMode = viewMode;
    }

    public void setAddForm(boolean addForm) {
        isAddForm = addForm;
    }

    public void isAddValidate(boolean addValidate) {
        isValidate = addValidate;
    }

    public void setSummaryForm(boolean summaryForm) {
        isSummaryForm = summaryForm;
    }

    @Override
    protected void addMinusButton(MultiTableRow tableRow) {
        WfmButton2 button1 = new WfmButton2(wfmStrings.period());
        button1.addClickHandler(event -> {
            EmployeeLookUpWithCode employee = (EmployeeLookUpWithCode) tableRow.getMap().getWidget("employee");
            SelectItem selectedItem = employee.getSelectedItem();
            backupEmployeeNavBox.setSelectedEmployee(selectedItem, selectedItem != null ? selectedItem.getId() : -1);
            if (isValidate) {
                if (!employee.isSelected()) {
                    Info.warn(wfmStrings.pleaseSelect() + " " + wfmStrings.employee());
                } else {
                    backupEmployeeNavBox.show();
                }
            } else {
                if ((isAddForm && backupEmployeeNavBox.leaveStartDate == null || backupEmployeeNavBox.leaveDueDate == null)) {
                    Info.warn("Please insert the end date for the leave period");
                } else if (!employee.isSelected()) {
                    Info.warn(wfmStrings.pleaseSelect() + " " + wfmStrings.employee());
                } else {
                    backupEmployeeNavBox.show();
                }
            }

        });
        new KpiToolTip(button1, wfmStrings.backupEmployee());
        tableRow.addToRight(button1);

    }

    @Override
    protected void addPlusButton(MultiTableRow tableRow) {
        trashButton = new WfmButton2("", "btn btn--icon", WfmButton2.ICON_TRASH);
        trashButton.addClickHandler(event -> delete(tableRow));
        new KpiToolTip(trashButton, wfmStrings.delete());
        tableRow.addToRight(trashButton);
    }

    protected void delete(MultiTableRow tableRow) {
        removeFromTableRow(tableRow);
        EmployeeLookUpWithCode employee = (EmployeeLookUpWithCode) tableRow.getMap().getWidget("employee");
        Integer id = employee.getSelectedItem().getId();
        backupEmployeeNavBox.removeItemFromMapByEmployeeId(id);
        deleteBtnListener.execute();
    }

    @Override
    protected void addAdditionalButton(MultiTableRow tableRow) {
        if (!isViewMode) {
            return;
        }
        plusButton = new WfmButton2("", WfmButton2.BTN_WHITE);
        plusButton.addStyleName("btn--icon");
        plusButton.add(new SvgIcon(SvgEnum.plus));
        plusButton.addClickHandler(event -> onAddLinkClicked(getWidgetIndex(tableRow) + 1));
        new KpiToolTip(plusButton, wfmStrings.add());
        tableRow.addToRight(plusButton);
    }

    public void setDeleteBtnListener(final Command deleteBtnListener) {
        this.deleteBtnListener = deleteBtnListener;
    }

    public void setVisibleForButtons(boolean plButton, boolean trbutton) {
        trashButton.setVisible(trbutton);
        plusButton.setVisible(plButton);
    }
}
