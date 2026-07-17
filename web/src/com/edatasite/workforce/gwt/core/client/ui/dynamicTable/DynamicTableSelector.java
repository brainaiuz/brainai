//package com.edatasite.workforce.gwt.core.client.ui.dynamicTable;
//
//import com.edatasite.workforce.gwt.core.client.Utils;
//
//import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
//import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
//import com.edatasite.workforce.gwt.task.client.rpc.PositionProjectEmployeeIdTime;
//import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
//import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
//import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
//import com.edatasite.workforce.gwt.core.client.ui.Clearable;
//import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
//import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.Command;
//import com.google.gwt.user.client.ui.*;
//import com.google.gwt.view.client.ProvidesKey;
//import java.util.*;
//import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
//
//
///**
// * Created by IntelliJ IDEA.
// * User: Admin
// * Date: 13.11.2008
// * Time: 18:33:29
// * To change this template use File | Settings | File Templates.
// */
//@Deprecated
//public class DynamicTableSelector extends Composite implements Clearable {
//
//    private static final WfmStrings wfmStrings = WfmStrings.App.get();
//    private Widget[] sources;//The data source
//    private KpiDataGrid dataGrid;
//    private Table table;//The data pool
//    private FlexTable topTable;
//
//    private Map<Integer, PositionsSelectItem> positionEmployeesMap = new HashMap<Integer, PositionsSelectItem>();
//    private Map<Integer, TaskInvolvedMember> taskInvolvedMemberMap = new HashMap<Integer, TaskInvolvedMember>();
//
//    private DataListBox employeeListBox;
//    private SelectItem[] taskStatusItems = null;
//
//    public static final String EMPLOYEE = "employee";
//    private boolean isTaskMembersInvolved = false;
//
//    private String dynamicTableSelector = "dynamic_table_selector_";
//
//    public DynamicTableSelector() {
//        createTable();
//    }
//
//    public DynamicTableSelector(int membersViewFlag) {
//        if (membersViewFlag == 1) {
//            isTaskMembersInvolved = true;
//        }
//        createTable();
//    }
//
//    private void createTable() {
//
//        employeeListBox = new DataListBox();
//        employeeListBox.ensureDebugId(dynamicTableSelector+"employeeListBox");
//
//        topTable = new FlexTable();
//        VerticalPanel verticalPanel = new VerticalPanel();
//        verticalPanel.add(locateSources(topTable));
////        dataGrid = new KpiDataGrid(KEY_PROVIDER);
//        table = new Table(Style.SINGLE | Style.FOCUSABLE, new TableColumnModel(getColumnsArray()));
//        if (isTaskMembersInvolved) {
//            table.setSize("690px", "200px");
//        } else {
//            table.setSize("430px", "200px");
//        }
//        registerListeners();
//        verticalPanel.add(table);
//        initWidget(verticalPanel);
//    }
//
//    public void registerListeners() {
//        employeeListBox.setChangeEvent(new Command() {
//            public void execute() {
//                addAssigneeToTable();
//            }
//        });
//
//    }
//
//    private void addAssigneeToTable() {
//        table.add(getTableItem(EMPLOYEE, employeeListBox.getSelectedItem()));
//        table.recalculate();
//        table.sort(0, 0);
//    }
//
//    private void addAssigneeToTable(SelectItem item) {
//        table.add(getTableItem(EMPLOYEE, item));
//        table.recalculate();
//        table.sort(0, 0);
//    }
//
//    public void setAssignees(IdTime[] assignees) {
//        for (int i = 0; i < assignees.length; i++) {
//            if (assignees[i] != null) {
//                employeeListBox.setSelected(assignees[i].getId());
//                if (employeeListBox.getSelectedItem() != null) {
//                    addAssigneeToTable();
//                }
//            }
//        }
//    }
//
//
//    public void setEmployeeData(PositionsSelectItem[] data, TaskInvolvedMember[] members) {
//        if (positionEmployeesMap != null) {
//            positionEmployeesMap.clear();
//        }
//        for (int i = 0; i < data.length; i++) {
//            positionEmployeesMap.put(data[i].getId(), data[i]);
//        }
//        employeeListBox.setItems(data);
//        employeeListBox.setSelectedIndex(0);
//        for (TaskInvolvedMember member : members) {
//            taskInvolvedMemberMap.put(member.getEmployeeID(), member);
//            SelectItem si = new SelectItem(member.getEmployeeID(), member.getEmployee());
//            addAssigneeToTable(si);
//        }
//    }
//
//    public void setEmployeeData(PositionsSelectItem[] data) {
//        if (positionEmployeesMap != null) {
//            positionEmployeesMap.clear();
//        }
//        for (int i = 0; i < data.length; i++) {
//            positionEmployeesMap.put(data[i].getId(), data[i]);
//        }
//        employeeListBox.setItems(data);
//        employeeListBox.setSelectedIndex(0);
//    }
//
//    public void hideEmployeeListBox() {
//        employeeListBox.setVisible(false);
//    }
//
//    public void showEmployeeListBox() {
//        employeeListBox.setVisible(true);
//    }
//
//    public DataListBox getEmployeeListBox() {
//        return employeeListBox;
//    }
//
//    private TableColumn action;
//
//    private TableColumn[] getColumnsArray() {
//        TableColumn[] columns = null;
//        TableColumn employee = new TableColumn("employee", wfmStrings.employee());
//        TableColumn position = new TableColumn("position", coreStrings.position());
//        TableColumn time = new TableColumn("time", wfmStrings.estimatedTime());
//        action = new TableColumn("action", coreStrings.action());
//
//        employee.setWidth(125);
//        position.setWidth(70);
//        time.setWidth(75);
//        action.setWidth(50);
//
//        action.setSortable(false);
//        if (isTaskMembersInvolved) {
//            TableColumn status = new TableColumn("status", coreStrings.taskStatus());
//            TableColumn actualTime = new TableColumn("actualTime", coreStrings.actualTime());
//            TableColumn percent = new TableColumn("percent", coreStrings.percentCompleted());
//            status.setWidth(120);
//            actualTime.setWidth(65);
//            percent.setWidth(70);
//            columns = new TableColumn[]{employee, position, status, time, actualTime, percent, action};
//        } else {
//            columns = new TableColumn[]{employee, position, time, action};
//        }
//        return columns;
//    }
//
//    private TableItem getTableItem(final String resourceType, final SelectItem selectedItem) {
//        final TableItem tableItem;
//        String name = "";
//        TextBox timeBox = new TextBox();
//        timeBox.setText("00:00");
//        SimpleLink removeLink;
//        Integer employeeId = Integer.valueOf(0);
//        Integer positionId = Integer.valueOf(0);
//        PositionsSelectItem positionSelectItem = null;
//        positionSelectItem = positionEmployeesMap.get(selectedItem.getId());
//        TaskInvolvedMember member = taskInvolvedMemberMap.get(selectedItem.getId());
//        if (member != null && member.getEstimatedTime() != null) {
//            timeBox.setText(Utils.formatMinutes(member.getEstimatedTime()));
//        }
//
//        name = positionSelectItem.getName();
//        String position = positionSelectItem.getPositionName();
//        employeeId = positionSelectItem.getId();
//
//        if (isTaskMembersInvolved && positionSelectItem != null) {
//            final DataListBox status = new DataListBox();
//            status.setWidth("110px");
//            String actualTime = Utils.formatMinutes(positionSelectItem.getActualTime() != null ? positionSelectItem.getActualTime().intValue() : 0);
//            String percent = String.valueOf(positionSelectItem.getPercent() != null ? positionSelectItem.getPercent().floatValue() : 0);
//
//            TextBox txtPercent = new TextBox();
//            txtPercent.setText(Utils.formatDouble(positionSelectItem.getPercent() != null ? positionSelectItem.getPercent().doubleValue() : 0));
//
//            if (taskStatusItems != null && taskStatusItems.length > 0) {
//                status.setItems(taskStatusItems);
//                status.setSelected(taskStatusItems[0].getName().equals("Not Started") ? taskStatusItems[0].getId() : taskStatusItems.length > 1 ? taskStatusItems[1].getId() : taskStatusItems[0].getId());
//            }
//            tableItem = new TableItem(new Object[]{
//                    name,//0
//                    position,//1
//                    status,//2
//                    timeBox,//3
//                    actualTime,//4
//                    txtPercent,/*percent*///5
//                    removeLink = new SimpleLink(coreStrings.remove()),//6
//                    resourceType,//7
//                    employeeId,//index = 8
//                    positionId});// index = 9;
//            timeBox.setWidth("65px");
//        } else {
//            tableItem = new TableItem(new Object[]{
//                    name,//0
//                    position,//1
//                    timeBox,//2
//                    removeLink = new SimpleLink(coreStrings.remove()),//3
//                    resourceType,//4
//                    employeeId,//index = 5
//                    positionId});// index = 6;
//            timeBox.setWidth("55");
//        }
//
//        removeFromEmployeeList(employeeId);
//
//        removeLink.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                addToEmployeeList(selectedItem.getId());
//                table.remove(tableItem);
//            }
//        });
//        return tableItem;
//    }
//
//    private void removeFromEmployeeList(Integer id) {
//        if (employeeListBox.getSelectedIndex() > 0) {
//            employeeListBox.removeBySelectItemId((PositionsSelectItem) positionEmployeesMap.get(id));
//        }
//    }
//
//    private void addToEmployeeList(Integer id) {
//        if (positionEmployeesMap.size() > 0) {
//            employeeListBox.addListItem((PositionsSelectItem) positionEmployeesMap.get(id));
//        }
//    }
//
//    /**
//     * @return the result of the table content
//     */
//    public PositionProjectEmployeeIdTime getSelectedResult() throws NumberFormatException, StringIndexOutOfBoundsException {
//        PositionProjectEmployeeIdTime result = new PositionProjectEmployeeIdTime();
//        List projectEmployeeList = new ArrayList();
//
//        Iterator iterator = table.getItems().iterator();
//
//        while (iterator.hasNext()) {
//            TableItem item = (TableItem) iterator.next();
//            if (isTaskMembersInvolved) {
//                String resourceType = (String) item.getValue(7);
//                DataListBox status = (DataListBox) item.getValue(2);
//                TextBox estime = (TextBox) item.getValue(3);
//                TextBox txtPercent = (TextBox) item.getValue(5);
//
//                Integer statusId = status.getSelectedItem().getId();
//                if (isNotValidTime(estime.getText())) {
//                    return null;
//                }
//                Integer estimTime = Utils.parseMinutes(estime.getText());
//
//                if (txtPercent.getText() == null && txtPercent.getText().isEmpty()) {
//                    return null;
//                }
//                Float percent = Float.valueOf(Utils.parseFormatted(txtPercent.getText()));
//
//                Integer employeeId = (Integer) item.getValue(8);
//                projectEmployeeList.add(new IdTime(employeeId, estimTime, percent, statusId));
//
//            } else {
//                String resourceType = (String) item.getValue(4);
//                TextBox time = (TextBox) item.getValue(2);
//                Integer timeInt = null;
//                timeInt = Utils.parseMinutes(time.getText());
//                    Integer employeeId = (Integer) item.getValue(5);
//                    projectEmployeeList.add(new IdTime(employeeId, timeInt));
//            }
//
//        }
//        result.setProjectEmployee((IdTime[]) projectEmployeeList.toArray(new IdTime[]{}));
//        return result;
//    }
//
//    private Widget locateSources(FlexTable table) {
//        table.setWidget(0, 0, employeeListBox);
//        return table;
//    }
//
//    private boolean isNotValidTime(String number) {
//        boolean isNotValid = false;
//        try {
//            Utils.parseMinutes(number);
//        } catch (NumberFormatException exc) {
//            isNotValid = true;
//        }
//        return isNotValid;
//    }
//
//    public void clearSelected() {
//        table.removeAll();
//        employeeListBox.clearSelected();
//        employeeListBox.setVisible(false);
//    }
//
//    public void setVisible(boolean visible) {
//        table.removeAll();
//        employeeListBox.clearSelected();
//        employeeListBox.setVisible(visible);
//        table.setVisible(visible);
//    }
//}
//
//
