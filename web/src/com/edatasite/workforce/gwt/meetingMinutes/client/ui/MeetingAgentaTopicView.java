package com.edatasite.workforce.gwt.meetingMinutes.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AgendaTopicDiscussionItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: developer
 * Date: 5/1/12
 * Time: 8:33 PM
 */
public class MeetingAgentaTopicView extends Composite {

    public final static String ASSIGNEEDTO = "assignedto";

    private static final WfmStrings wfmStrings =WfmStrings .App.get();
    private final static String ACTIONPOINT = "actionpoints";
    private final static String DISCUSSIONPOINT = "discussionpoints";
    private final static String DUEDATE = "duedate";
    private final static String STARTDATE = "startdate";
    private final static int DEFAULT_ROW = 2;

    private EditableTable agendaTopicTable;
    private boolean editable = true;
    private SelectItem[] employeesList;
    private EditableGrid grid;
    private String name;
    private Integer objectID;

    public MeetingAgentaTopicView(SelectItem[] employeesList, boolean editable) {
        this.employeesList = employeesList;
        this.editable = editable;
        initialize();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EditableTable getAgendaTopicTable() {
        return this.agendaTopicTable;
    }

    public AgendaTopicDiscussionItem[] getData() {
        List<AgendaTopicDiscussionItem> items = new ArrayList<>();

        for (int rowID = 0; rowID < grid.getRowCount(); rowID++) {

            if (agendaTopicTable.getValidRows() != null && agendaTopicTable.isItemValid(rowID)) {
                AgendaTopicName txtName = (AgendaTopicName) agendaTopicTable.getColumnById(rowID, DISCUSSIONPOINT);
                AgendaTopicName txtQty = (AgendaTopicName) agendaTopicTable.getColumnById(rowID, ACTIONPOINT);
                EmployeeLookUp assigneedTo = (EmployeeLookUp) agendaTopicTable.getColumnById(rowID, ASSIGNEEDTO);
                DatePicker startDate = (DatePicker) agendaTopicTable.getColumnById(rowID, STARTDATE);
                DatePicker dueDate = (DatePicker) agendaTopicTable.getColumnById(rowID, DUEDATE);

                AgendaTopicDiscussionItem item = new AgendaTopicDiscussionItem();
                item.setObjectID(txtName.getObjectID());
                item.setDiscussionPoints(txtName.getDisplayValue());
                item.setActionPoints(txtQty.getDisplayValue());
                item.setAssignedTo(assigneedTo.getSelectedItem());
                item.setStartDate(startDate.getDate());
                item.setDueDate(dueDate.getDate());

                items.add(item);
            }
        }

        return items.toArray(new AgendaTopicDiscussionItem[]{});
    }

    public static ColumnConfig[] getColumn(boolean editable) {
        Integer index = 0;

        ColumnConfig[] columns = new ColumnConfig[5];

        columns[index] = new ColumnConfig(CustomCell.class, DISCUSSIONPOINT, 200, true);
        columns[index++].setTitle(wfmStrings.discussionPoints());

        columns[index] = new ColumnConfig(CustomCell.class, ACTIONPOINT, 200, false);
        columns[index++].setTitle(wfmStrings.actionPoints());

        columns[index] = new ColumnConfig(editable ? LookUpCell.class : CustomCell.class, ASSIGNEEDTO, 200, false);
        columns[index++].setTitle(wfmStrings.assignedTo());

        columns[index] = new ColumnConfig(CustomCell.class, STARTDATE, 150, false);
        columns[index++].setTitle(wfmStrings.startDate());

        columns[index] = new ColumnConfig(CustomCell.class, DUEDATE, 150, false);
        columns[index++].setTitle(wfmStrings.dueDate());

        return columns;
    }

    public Object[] getWidgets(AgendaTopicDiscussionItem ticketItem) {
        if (ticketItem == null) {
            ticketItem = new AgendaTopicDiscussionItem();
        }
        Integer index = 0;
        Object[] data = new Object[5];
        if (editable) {
            AgendaTopicName txtDiscussionPoints = new AgendaTopicName(false);
            txtDiscussionPoints.setItemValue(ticketItem.getDiscussionPoints());
            txtDiscussionPoints.setObjectID(ticketItem.getObjectID());

            AgendaTopicName txtActionPoints = new AgendaTopicName(true);
            txtActionPoints.setItemValue(ticketItem.getActionPoints());
            txtActionPoints.setObjectID(ticketItem.getObjectID());

            EmployeeLookUp lookUp = new EmployeeLookUp(true, false, false);
            if (ticketItem.getAssignedTo() != null) {
                lookUp.addItem(ticketItem.getAssignedTo());
                lookUp.setSelected(ticketItem.getAssignedTo().getId());
            }

            AgendaTopicDates startDate = new AgendaTopicDates();
            startDate.setItemValue(ticketItem.getStartDate());

            AgendaTopicDates endDate = new AgendaTopicDates();
            endDate.setItemValue(ticketItem.getDueDate());

            data[index++] = txtDiscussionPoints;
            data[index++] = txtActionPoints;
            data[index++] = lookUp;
            data[index++] = startDate;
            data[index++] = endDate;
        } else {
            data[index++] = new AgendaTopicLabel(ticketItem.getDiscussionPoints());
            data[index++] = new AgendaTopicLabel(ticketItem.getActionPoints());
            data[index++] = new AgendaTopicLabel(ticketItem.getAssignedTo() != null ? ticketItem.getAssignedTo().getName() : "");
            data[index++] = new AgendaTopicLabel(ticketItem.getStartDate() != null ? DateUtils.format(ticketItem.getStartDate()) : "");
            data[index++] = new AgendaTopicLabel(ticketItem.getDueDate() != null ? DateUtils.format(ticketItem.getDueDate()) : "");
        }

        return data;
    }

    public void setValues(AgendaTopicDiscussionItem[] items, boolean addNewRow) {
        if (items != null && items.length > 0) {
            agendaTopicTable.removeAllRows();

            for (AgendaTopicDiscussionItem item : items) {
                agendaTopicTable.addRow(getWidgets(item));
            }
            if (addNewRow) {
                agendaTopicTable.addRow(getWidgets(null));
            }
        }
    }

    public int[] validation() {
        int errors = 0;
        int errorsCount = 0;
        int[] result = new int[3];
        agendaTopicTable.setValidRows(0);

        int rowCount = grid.getRowCount();

        for (int rowID = 0; rowID < grid.getRowCount(); rowID++) {
            if (rowID == rowCount) {
                break;
            }
            agendaTopicTable.resetValidation(rowID);

            AgendaTopicName discussionPoint = (AgendaTopicName) agendaTopicTable.getColumnById(rowID, DISCUSSIONPOINT);
            AgendaTopicName actionPoint = (AgendaTopicName) agendaTopicTable.getColumnById(rowID, ACTIONPOINT);
            EmployeeLookUp assignees = ((EmployeeLookUp) agendaTopicTable.getColumnById(rowID, ASSIGNEEDTO));

            boolean needToValidate = false;
            if ((actionPoint.getDisplayValue() != null && !actionPoint.getDisplayValue().isEmpty()) ||
                    (assignees.getSelectedItem() != null && assignees.getSelectedItemID() > 0)) {
                needToValidate = true;
            }

            if (needToValidate && (discussionPoint.getDisplayValue() == null || discussionPoint.getDisplayValue().isEmpty() || discussionPoint.getDisplayValue().trim().isEmpty())) {
                agendaTopicTable.setColumnValid(DISCUSSIONPOINT);
                errors++;
            }

            /*if (actionPoint.getDisplayValue() == null || actionPoint.getDisplayValue().isEmpty()) {
                agendaTopicTable.setColumnValid(ACTIONPOINT);
                errors++;
            }*/

            if (errors > 0) {
                if (errors == agendaTopicTable.getRequiredFieldCount()) {
                    agendaTopicTable.setItemValid(rowID, false);
                    errors = 0;
                    errorsCount++;
                    agendaTopicTable.notValidFields(rowID);
                } else if (agendaTopicTable.validateFields(rowID)) {
                    agendaTopicTable.setItemValid(rowID, true);
                    agendaTopicTable.incValidRow();
                    errors = 0;
                    errorsCount++;
                } else {
                    agendaTopicTable.setItemValid(rowID, false);
                    errors = 0;
                    errorsCount++;
                }
            } else {
                agendaTopicTable.setItemValid(rowID, true);
                agendaTopicTable.incValidRow();
            }
        }

        if (agendaTopicTable.getValidRows() != null && agendaTopicTable.getValidRows() == 0) {
//			agendaTopicTable.notValid(0, DISCUSSIONPOINT);
//			agendaTopicTable.notValid(0, ACTIONPOINT);
            result[0] = 0;
            result[1] = 0;
            result[2] = errorsCount;
            return result;
        }
        result[0] = 1;  // 0 - false, 1- true;
        result[1] = agendaTopicTable.getValidRows();  // valid rows count;
        result[2] = errorsCount;  // errors count;
        return result;
    }

    private void addNewRow() {
        AgendaTopicDiscussionItem discussionItem = new AgendaTopicDiscussionItem();
        agendaTopicTable.addRow(getWidgets(discussionItem));
    }

    private void drawDefaultRows(EditableTable agendaTopicTable) {
        for (int i = 0; i < DEFAULT_ROW; i++) {
            agendaTopicTable.addRow(getWidgets(null));
        }
    }

    private void initialize() {
        agendaTopicTable = new EditableTable(getColumn(editable), editable);
        agendaTopicTable.getGrid().addSelectRowListener((editableGrid, i) -> {
            int j = 1;
        });
        agendaTopicTable.getElement().getStyle().setBackgroundColor("#fff");
        grid = agendaTopicTable.getGrid();


        agendaTopicTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                addNewRow();
            }

            @Override
            public void removeRow() {
            }
        });

        //drawing default rows
        drawDefaultRows(agendaTopicTable);

        initWidget(agendaTopicTable);
    }

    class AgendaTopicName extends TextArea2 implements CustomCellInterface {
        private Integer objectID;

        public AgendaTopicName(boolean needMoreCherecter) {
            super(needMoreCherecter ? 3000 : 255);
            super.hideCharacterLimitPanel();
            super.setHeight("50px");
            super.setStyleName("description-default-color");
        }

        @Override
        public String getDisplayValue() {
            return getText();
        }

        @Override
        public void setItemValue(Object value) {
            super.setText((String) value);
        }

        @Override
        public void setItemFocus(boolean focused) {
            super.setItemFocus(focused);
        }

        public Integer getObjectID() {
            return objectID;
        }

        public void setObjectID(Integer objectID) {
            this.objectID = objectID;
        }
    }

    class AgendaTopicLabel extends HTML implements CustomCellInterface {
        private Integer objectID;

        public AgendaTopicLabel(String value) {
            super.setHTML(value != null ? value : " ");
        }

        @Override
        public String getDisplayValue() {
            return getHTML();
        }

        @Override
        public void setItemValue(Object value) {
            super.setHTML((String) value);
        }

        @Override
        public void setItemFocus(boolean focused) {
        }

        public Integer getObjectID() {
            return objectID;
        }

        public void setObjectID(Integer objectID) {
            this.objectID = objectID;
        }
    }

    class AgendaTopicAssignees extends DataListBox implements CustomCellInterface {

        private Integer objectID;

        public AgendaTopicAssignees() {
            super();
            setWithoutNullLabel(true);
        }

        @Override
        public String getDisplayValue() {
            return getSelectedItem() != null ? getSelectedItem().getName() : getNullLabel();
        }

        public SelectItem getSelectedItem() {
            return getSelectedItem(true);
        }

        @Override
        public void setItemValue(Object value) {
            setSelected((SelectItem) value);
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }

        public Integer getObjectID() {
            return objectID;
        }

        public void setObjectID(Integer objectID) {
            this.objectID = objectID;
        }
    }

    class AgendaTopicDates extends DatePicker implements CustomCellInterface {

        @Override
        public String getDisplayValue() {
            return DateUtils.format(getDate());
        }

        @Override
        public void setItemValue(Object value) {
            setDate(value != null ? (Date) value : new Date());
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }
    }
}