package com.edatasite.workforce.gwt.profile.client.ui.view.leaveRequestApproversView;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.profile.ActionTimesTO;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.hrms.client.rpc.RemindersDataRpc;
import com.edatasite.workforce.gwt.profile.client.rpc.HrReminderEntities;
import com.edatasite.workforce.gwt.profile.client.rpc.HrReminderItem;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class ApproversTable extends FlexTable {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final String IS_CUSTOMFIELD = "IS_CUSTOMFIELD";
    private ArrayList<SelectItem> rolesList;
    private ArrayList<SelectItem> emailTemplateData;
    private ArrayList<SelectItem> workflowList;

    private ArrayList<SelectItem> listReminders;

    private DataListBox typeReminderlistBox;
    private MultiTable daysMultiTable;
    private DataListBox emailListBox;
    private MultiTable sendMultiTable;
    private MultiTable workflowListBox;
    private SimpleLink removeLink;
    private final boolean isFirst = true;

    public ApproversTable() {
    }

    public void getData(RemindersDataRpc result, List<HrReminderItem> itemlist) {
        emailTemplateData = new ArrayList<>();
        emailTemplateData = result.getEmailTemplate();
        rolesList = new ArrayList<>();
        rolesList = result.getRole();
        workflowList = new ArrayList<>();
        workflowList = result.getWorkflowRule();
        listReminders = new ArrayList<>();
        if (result.getCustomField() != null && result.getCustomField().length > 0) {
            int index = 0;
            SelectItem[] customItem = new SelectItem[result.getCustomField().length];
            for (SelectItem item : result.getCustomField()) {
                customItem[index] = new SelectItem(item.getId(), item.getName());
                index++;
                listReminders.add(new SelectItem(item.getId(), item.getName(), item.getDescription(), IS_CUSTOMFIELD));
            }
        }
        setDefaultReminderType();
        draw(itemlist);
    }

    public void getOnbData(RemindersDataRpc result, EmployeeStepItem item, List<HrReminderItem> itemlist) {
        emailTemplateData = new ArrayList<>();
        emailTemplateData = result.getEmailTemplate();
        rolesList = new ArrayList<>();
        rolesList = result.getRole();
        workflowList = new ArrayList<>();
        workflowList = result.getWorkflowRule();
        listReminders = new ArrayList<>();
        int index = 0;
        SelectItem[] customItem = new SelectItem[item.getOnboardingCustomFieldItems().length];
        for (SelectItem onItem : item.getOnboardingCustomFieldItems()) {
            customItem[index] = new SelectItem(onItem.getId(), onItem.getName());
            index++;
            listReminders.add(new SelectItem(onItem.getId(), onItem.getName(), onItem.getDescription(), IS_CUSTOMFIELD));
        }
        drawOnboarding(getRowCount(), item.getStepID(), null, itemlist);
    }

    private void draw(List<HrReminderItem> itemList) {
        boolean isEmpty = true;
        for (HrReminderItem item : itemList) {
            if (item.getEntityType().equals(HrReminderEntities.EMPLOYEE.ordinal())) {
                isEmpty = false;
                setDefaultRows(getRowCount(), item);
            }
        }
        if (isEmpty) {
            setDefaultRows(getRowCount(), null);
        }
    }

    private void drawOnboarding(Integer index, Integer stepID, Anchor link, List<HrReminderItem> itemList) {
        boolean isEmpty = true;
        for (HrReminderItem item : itemList) {
            if (item.getEntityType().equals(HrReminderEntities.ONBOARDING_STEP.ordinal())) {
                if (stepID.equals(item.getOnboardingStepId())) {
                    isEmpty = false;
                    setOnboardingRows(getRowCount(), stepID, null, item);
                }
            }
        }
        if (isEmpty) {
            setOnboardingRows(getRowCount(), stepID, null, null);
        }
    }

    private void setOnboardingRows(Integer index, Integer stepID, Anchor link, HrReminderItem item) {

        VerticalPanel vp = new VerticalPanel();
        if (stepID != null) {
            vp.setTitle(stepID.toString());
        }
        typeReminderlistBox = getTypeBox(false, item != null ? item.getFieldValue() : null);
//        vp.add(onboardingLabel);
        vp.add(typeReminderlistBox);
        daysMultiTable = createReminderDays(item != null ? item.getActionTimes() : null);
        emailListBox = createEmailBox(item != null ? item.getEmailTemplateId() : null);
        sendMultiTable = createSendMultiTable(item != null ? item.getRoleItems() : null);
        workflowListBox = createWorkflowMultiTable(item != null ? item.getWorkFlowItems() : null);
        removeLink = removeAction(false);
        if (link != null) {
            index = index - 1;
        }
        setWidget(index, 0, vp);
        setWidget(index, 1, daysMultiTable);
        setWidget(index, 2, emailListBox);
        setWidget(index, 3, sendMultiTable);
        setWidget(index, 4, workflowListBox);
        setWidget(index, 5, removeLink);
        if (link != null) {
            setWidget(index + 1, 0, link);
        }
    }

    private void setDefaultRows(Integer index, HrReminderItem item) {
        VerticalPanel vp = new VerticalPanel();
//        vp.add(employeeLabel);
        typeReminderlistBox = getTypeBox(true, item != null ? item.getFieldCode() : null);
        vp.add(typeReminderlistBox);
        daysMultiTable = createReminderDays(item != null ? item.getActionTimes() : null);
        emailListBox = createEmailBox(item != null ? item.getEmailTemplateId() : null);
        sendMultiTable = createSendMultiTable(item != null ? item.getRoleItems() : null);
        workflowListBox = createWorkflowMultiTable(item != null ? item.getWorkFlowItems() : null);
        removeLink = removeAction(true);

        setWidget(index, 0, vp);
        setWidget(index, 1, daysMultiTable);
        setWidget(index, 2, emailListBox);
        setWidget(index, 3, sendMultiTable);
        setWidget(index, 4, workflowListBox);
        setWidget(index, 5, removeLink);
    }

    private SimpleLink removeAction(final boolean isEmployeeItem) {
        SimpleLink link = new SimpleLink(wfmStrings.delete());
        link.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        link.addClickHandler(clickEvent -> {
            boolean islastRow = getIsLastrow(isEmployeeItem);
            if (islastRow) {
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, wfmStrings.youCannotDeleteTheLastRow());
                messageBox.setWidth("300px");
                messageBox.open();
                return;
            }
//                removeSelectedList(getCellForEvent(clickEvent).getRowIndex(), isEmployeeItem);
            removeRow(getCellForEvent(clickEvent).getRowIndex());
        });
        return link;
    }

    private boolean getIsLastrow(boolean isEmployeeItem) {
        int rowCount = isEmployeeItem ? getRowCount() : getRowCount() - 1;
        return rowCount <= 2;
    }

    private void removeSelectedList(int rowIndex, boolean isEmployeeItem) {
        DataListBox dataListBox = (DataListBox) ((VerticalPanel) getWidget(rowIndex, 0)).getWidget(1);
        if (dataListBox != null && dataListBox.getSelectedItem() != null) {
            listReminders.add(dataListBox.getSelectedItem());
            int tebleRowCount = isEmployeeItem ? getRowCount() : getRowCount() - 1;
            for (int i = 0; i < tebleRowCount; i++) {
                if (getWidget(i, 0) != null) {
                    DataListBox box = (DataListBox) ((VerticalPanel) getWidget(i, 0)).getWidget(1);
                    if (i != rowIndex) {
                        SelectItem selectItem = box.getSelectedItem();
                        box.addListItem(dataListBox.getSelectedItem());
                        if (selectItem != null) {
                            box.setSelected(selectItem);
                        }
                    }
                }
            }
        }
    }


    private DataListBox getTypeBox(final boolean isEmployeeItem, String code) {
//        final Integer[] index = {null};
        typeReminderlistBox = new DataListBox();
        typeReminderlistBox.setStyleName("form-control");
        typeReminderlistBox.setWidth("175px");
        typeReminderlistBox.setItems(listReminders.toArray(new SelectItem[listReminders.size()]));
//        typeReminderlistBox.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent clickEvent) {
//                index[0] = getCellForEvent(clickEvent).getRowIndex();
//            }
//        });
//        typeReminderlistBox.addValueChangeHandler(new ChangeHandler() {
//            @Override
//            public void onChange(ChangeEvent changeEvent) {
//                if (((DataListBox) changeEvent.getSource()).getSelectedItem() != null) {
//                    if (((DataListBox) changeEvent.getSource()).getPreviousSelectedItem() != null) {
//                        setOldSelectedItem(((DataListBox) changeEvent.getSource()), index[0], isEmployeeItem);
//                    } else {
//                        updateListBox(((DataListBox) changeEvent.getSource()), index[0], isEmployeeItem);
//                    }
//                } else {
//                    setOldSelectedItem(((DataListBox) changeEvent.getSource()), index[0], isEmployeeItem);
//                }
//            }
//        });
        if (code != null) {
            typeReminderlistBox.setSelected(getSelectItemByKey(listReminders, code));
        }
        return typeReminderlistBox;
    }

    private MultiTable createReminderDays(final ArrayList<ActionTimesTO> timesTOs) {
        final MultiTable table = new MultiTable(getRemindersDaysMultiTableWidgets());
        table.setOnLinesAdded(() -> setRemindersDaysItemsToListBox(table, null));
        table.setSpacing(5);
        if (timesTOs == null || timesTOs.size() < 1) {
            setRemindersDaysItemsToListBox(table, null);
            table.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
            table.getElement().getStyle().setPaddingTop(15, Style.Unit.PX);
            table.getElement().getStyle().setWidth(280, Style.Unit.PX);
            return table;
        }
        for (ActionTimesTO timesTO : timesTOs) {
            setRemindersDaysItemsToListBox(table, timesTO);
            table.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
            table.getElement().getStyle().setPaddingTop(15, Style.Unit.PX);
            table.getElement().getStyle().setWidth(280, Style.Unit.PX);
            table.cloneWidgetsRow();
        }
        table.removeRowTable(timesTOs.size());
        return table;
    }

    private MultiTableWidgets getRemindersDaysMultiTableWidgets() {
        return new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                WidgetsMap map = new WidgetsMap();
                DataListBox aftAndBefListBox = new DataListBox();
                aftAndBefListBox.setStyleName("form-control");
                aftAndBefListBox.setWidth("100px");
                aftAndBefListBox.addListItem(new SelectItem(1, wfmStrings.before(), "BEFORE"));
                aftAndBefListBox.addListItem(new SelectItem(2, wfmStrings.after(), "AFTER"));

                TextBox days = new TextBox();
                days.setStyleName("form-control");
                days.setWidth("20px");
                days.setHeight("16px");
                days.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                Validation.addNumericKeyboardListener(days);

                DataListBox daysStringListBox = new DataListBox();
                daysStringListBox.setStyleName("form-control");
                daysStringListBox.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                daysStringListBox.setWidth("105px");
                daysStringListBox.addListItem(new SelectItem(60 * 24 * 1, " " + wfmStrings.days()));        //one day    //1
                daysStringListBox.addListItem(new SelectItem(60 * 24 * 7, " " + wfmStrings.weeks()));       //one week   //2
                daysStringListBox.addListItem(new SelectItem(60 * 24 * 30, " " + wfmStrings.months()));    //one month  //3

                map.addWidgetToMap("REMINDERS_LIST_BOX", aftAndBefListBox);
                map.addWidgetToMap("REMINDERS_TEXT_BOX", days);
                map.addWidgetToMap("REMINDER_DAYS_LIST_BOX", daysStringListBox);

                HorizontalPanel p = new HorizontalPanel();
                p.add(aftAndBefListBox);
                p.add(days);
                p.add(daysStringListBox);

                map.addWidgets(p);
                return map;
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        };
    }

    private void setRemindersDaysItemsToListBox(MultiTable table, ActionTimesTO timesTO) {
        if (timesTO != null) {
            int i = table.getWidgetsMaps().size() - 1;
            WidgetsMap widgetsMap = table.getWidgetMapByRowID(i);
            DataListBox aftAndBefListBox = (DataListBox) widgetsMap.getWidget("REMINDERS_LIST_BOX");
            TextBox days = (TextBox) widgetsMap.getWidget("REMINDERS_TEXT_BOX");
            DataListBox daysStringListBox = (DataListBox) widgetsMap.getWidget("REMINDER_DAYS_LIST_BOX");

            aftAndBefListBox.setSelected(timesTO.getActiontype().equalsIgnoreCase("BEFORE") ? 1 : (timesTO.getActiontype().equalsIgnoreCase("AFTER") ? 2 : 0));
            days.setText(timesTO.getActionNumber());
            daysStringListBox.setSelected(Integer.valueOf(timesTO.getActionPeriod()));

            for (Widget widget : widgetsMap.getWidgets()) {
                HorizontalPanel hPanel = (HorizontalPanel) widget;
                hPanel.clear();
                hPanel.add(aftAndBefListBox);
                hPanel.add(days);
                hPanel.add(daysStringListBox);
            }
        }
    }

    private DataListBox createEmailBox(Integer emailTemplateId) {
        DataListBox emailListBox = new DataListBox();
        emailListBox.setStyleName("form-control");
        emailListBox.setItems(emailTemplateData.toArray(new SelectItem[emailTemplateData.size()]));
        emailListBox.setWidth("130px");
        emailListBox.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        if (emailTemplateId != null) {
            emailListBox.setSelected(emailTemplateId);
        }
        return emailListBox;
    }

    private MultiTable createSendMultiTable(ArrayList<SelectItem> roleItems) {
        final MultiTable sendNotif = new MultiTable(getMultiTableWidgets());
        sendNotif.setOnLinesAdded(() -> setRoleItemsToListBox(sendNotif, null));
        sendNotif.setSpacing(5);
        if (roleItems == null || roleItems.size() < 1) {
            setRoleItemsToListBox(sendNotif, null);
            sendNotif.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
            sendNotif.getElement().getStyle().setPaddingTop(15, Style.Unit.PX);
            sendNotif.getElement().getStyle().setWidth(150, Style.Unit.PX);
            return sendNotif;
        }
        for (SelectItem role : roleItems) {
            setRoleItemsToListBox(sendNotif, role);
            sendNotif.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
            sendNotif.getElement().getStyle().setPaddingTop(15, Style.Unit.PX);
            sendNotif.getElement().getStyle().setWidth(150, Style.Unit.PX);
            sendNotif.cloneWidgetsRow();
        }
        sendNotif.removeRowTable(roleItems.size());
        return sendNotif;
    }

    private MultiTable createWorkflowMultiTable(ArrayList<SelectItem> workflowList) {
        final MultiTable workflow = new MultiTable(getWorkflowWidgets());
        workflow.setOnLinesAdded(() -> setWorkflowItemsToListBox(workflow, null));
        workflow.setSpacing(5);
        if (workflowList == null || workflowList.size() < 1) {
            setWorkflowItemsToListBox(workflow, null);
            workflow.getElement().getStyle().setMarginLeft(25, Style.Unit.PX);
            workflow.getElement().getStyle().setPaddingTop(15, Style.Unit.PX);
            workflow.getElement().getStyle().setWidth(190, Style.Unit.PX);
            return workflow;
        }
        for (SelectItem wfItem : workflowList) {
            setWorkflowItemsToListBox(workflow, wfItem);
            workflow.getElement().getStyle().setMarginLeft(25, Style.Unit.PX);
            workflow.getElement().getStyle().setPaddingTop(15, Style.Unit.PX);
            workflow.getElement().getStyle().setWidth(190, Style.Unit.PX);
            workflow.cloneWidgetsRow();
        }
        workflow.removeRowTable(workflowList.size());
        return workflow;
    }

    private MultiTableWidgets getMultiTableWidgets() {
        return new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                WidgetsMap widgetsMap = new WidgetsMap();
                DataListBox rolesListBox = new DataListBox();
                rolesListBox.setStyleName("form-control");
                rolesListBox.setWidth("145px");
                widgetsMap.addWidgets(rolesListBox);
                return widgetsMap;
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        };
    }

    private MultiTableWidgets getWorkflowWidgets() {
        return new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                WidgetsMap widgetsMap = new WidgetsMap();
                DataListBox workflowListBox = new DataListBox();
                workflowListBox.setStyleName("form-control");
                workflowListBox.setWidth("175px");
                widgetsMap.addWidgets(workflowListBox);
                return widgetsMap;
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        };
    }

    private void updateListBox(DataListBox selectBox, Integer index, boolean isEmployeeItems) {
        listReminders.remove(selectBox.getSelectedItem());
        int tableRowCount = isEmployeeItems ? getRowCount() : getRowCount() - 1;
        for (int i = 0; i < tableRowCount; i++) {
            if (getWidget(i, 0) != null) {
                DataListBox box = (DataListBox) ((VerticalPanel) getWidget(i, 0)).getWidget(1);
                if (i != index) {
                    if (box != null && box.getSelectedItem() != null) {
                        box.removeListItem(selectBox.getSelectedItem());
                    } else {
                        box.clear();
                        box.setItems(listReminders.toArray(new SelectItem[listReminders.size()]));
                    }
                }
            }
        }
    }

    private void setOldSelectedItem(DataListBox selectBox, Integer index, boolean isEmployeeItems) {
        listReminders.add(selectBox.getPreviousSelectedItem());
        if (selectBox.getSelectedItem() != null) {
            listReminders.remove(selectBox.getSelectedItem());
        }
        int tableRowCount = isEmployeeItems ? getRowCount() : getRowCount() - 1;
        for (int i = 0; i < tableRowCount; i++) {
            DataListBox box = (DataListBox) ((VerticalPanel) getWidget(i, 0)).getWidget(1);
            if (i != index) {
                if (selectBox.getSelectedItem() == null) {
                    SelectItem selectItem = box.getSelectedItem();
                    box.addListItem(selectBox.getPreviousSelectedItem());
                    if (selectItem != null) {
                        box.setSelected(selectItem);
                    }
                } else if (box != null && box.getSelectedItem() != null) {
                    box.removeListItem(selectBox.getSelectedItem());
                    SelectItem selectItem = box.getSelectedItem();
                    box.addListItem(selectBox.getPreviousSelectedItem());
                    if (selectItem != null) {
                        box.setSelected(selectItem);
                    }
                } else {
                    box.clear();
                    box.setItems(listReminders.toArray(new SelectItem[listReminders.size()]));
                }
            } else if (selectBox.getPreviousSelectedItem() != null) {
                SelectItem selectItem = box.getSelectedItem();
                box.addListItem(selectBox.getPreviousSelectedItem());
                if (selectItem != null) {
                    box.setSelected(selectItem);
                }
            }
        }
    }

    public void addNewLine() {
        setDefaultRows(getRowCount(), null);
    }

    public void addNewOnboardingLine(Integer stepId, Anchor link) {
        setOnboardingRows(getRowCount(), stepId, link, null);
    }

    private void setRoleItemsToListBox(MultiTable multiTable, SelectItem selectRole) {
        int i = multiTable.getWidgetsMaps().size() - 1;
        for (Widget widget : multiTable.getWidgetsMaps().get(i).getWidgets()) {
            DataListBox listBox = (DataListBox) widget;
            listBox.clear();
            listBox.setItems(rolesList.toArray(new SelectItem[]{}));
            if (selectRole != null) {
                listBox.setSelected(selectRole);
            }
        }
    }

    private void setWorkflowItemsToListBox(MultiTable multiTable, SelectItem selectWorkFlow) {
        int i = multiTable.getWidgetsMaps().size() - 1;
        for (Widget widget : multiTable.getWidgetsMaps().get(i).getWidgets()) {
            DataListBox listBox = (DataListBox) widget;
            listBox.clear();
            listBox.setItems(workflowList.toArray(new SelectItem[]{}));
            if (selectWorkFlow != null) {
                listBox.setSelected(selectWorkFlow);
            }
        }
    }

    private void setDefaultReminderType() {
        listReminders.add(new SelectItem(123456, wfmStrings.passportIssueDate(), "passportIssueDate", "false"));
        listReminders.add(new SelectItem(132457, wfmStrings.passportExpireDate(), "passportExpireDate", "false"));
        listReminders.add(new SelectItem(123458, wfmStrings.visaExpirationDate(), "visaExpirationDate", "false"));
        listReminders.add(new SelectItem(123459, wfmStrings.visaIssueDate(), "visaIssueDate", "false"));
        listReminders.add(new SelectItem(123460, wfmStrings.insuranceExpiryDate(), "insuranceExpiryDate", "false"));
    }

    private SelectItem getSelectItemByKey(List<SelectItem> list, String key) {
        for (SelectItem item : list) {
            if (item.getName().equals(key) || item.getDescription().equals(key) || item.getCategory().equals(key)) {
                return item;
            }
        }
        return new SelectItem();
    }
}
