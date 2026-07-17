package com.edatasite.workforce.gwt.project.client.ui.view.projectposition;

import com.edatasite.workforce.gwt.core.client.UUID;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.Tag;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.UL;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Normurod on 8/6/15.
 */
public class ProjectPositionWidget extends Composite {
    interface ProjectPositionWidgetUiBinder extends UiBinder<HTMLPanel, ProjectPositionWidget> {
    }

    private static final ProjectPositionWidgetUiBinder ourUiBinder = GWT.create(ProjectPositionWidgetUiBinder.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    private static final String POSITION = "POSITION";
    private static final String CONTRACT_START = "CONTRACT_START";
    private static final String CONTRACT_END = "CONTRACT_END";
    private static final String WAGE_RATE = "WAGE_RATE";
    private static final String CLIENT_CHARGE = "CLIENT_CHARGE";
    private static final String NUMBER_OF_WORKERS = "NUMBER_OF_WORKERS";
    private static final String PRICE_TYPE = "PRICE_TYPE";
    private static final String UNIT_PRICE = "UNIT_PRICE";
    private static final String UNIT_QTY = "UNIT_QTY";
    private static final String TOTAL_CHARGE = "TOTAL_CHARGE";
    private static final String EMPLOYEES = "EMPLOYEES";
    private static final String OVERTIME_RATE = "OVERTIME_RATE";
    private static final String WEEKEND_OVERTIME_RATE = "WEEKEND_OVERTIME_RATE";
    private static final String HOLIDAY_OVERTIME_RATE = "HOLIDAY_OVERTIME_RATE";
    private static final String PleaseAssignEmployeetotheProject = "Please_Assign_Employee_to_the_Project";


    Set<Integer> itemIds;
    private SelectItem[] positions;
    private final HashMap<Integer, ProjectPosition> selectedPositionMap;
    private SelectItem[] selectedMembers;
    @UiField
    FlowPanel pnlContainer;

    private DynamicTable table;
    private final EmployeeAssignmentPopup employeeAssignmentPopup;

    private Integer projectID;
    private final boolean isContract;

    public ProjectPositionWidget(Integer projectID, boolean isContract) {
        this.projectID = projectID;
        this.isContract = isContract;

        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);

        CommonService.App.get().getPositions(new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                positions = selectItems;
                initializeTable();
            }
        });

        employeeAssignmentPopup = new EmployeeAssignmentPopup(projectID);
        employeeAssignmentPopup.addStyleName("file--ProjectPositionWidget");
        itemIds = new HashSet<>();
        selectedPositionMap = new HashMap<>();
    }

    private void initializeTable() {
        table = new DynamicTable(getColumnArray(), null, null, true, "80px");
        table.setBorderWidth(0);
        table.setStyleName("positiontable");
        table.addListener(new AddListener() {
            public void plusClicked(int rowId) {
                //id plus clicked  - add another row
                Widget[] widgets = getWidgetArray(null);
                table.insertRow(rowId + 1, widgets);
            }

            //if minus clicked, try to delete database record
            public void minusClicked(int rowId, Integer objectId) {
                //if item exist in database then delete it.
                removePositionFromMap();
            }
        });
        table.addRow(getWidgetArray(null));
        table.getRowFormatter().setStyleName(0, "positiontable__thead");
        pnlContainer.add(table);
    }

    private DynamicTableColumn[] getColumnArray() {
        LinkedList<DynamicTableColumn> columnsList = new LinkedList<>();

        columnsList.add(new DynamicTableColumn(wfmStrings.position(), POSITION, 200));
        columnsList.add(new DynamicTableColumn(wfmStrings.contractStart(), CONTRACT_START, 120));
        columnsList.add(new DynamicTableColumn(wfmStrings.contractEnd(), CONTRACT_END, 120));
        if (!isContract) {
            columnsList.add(new DynamicTableColumn(wfmStrings.rate(), UNIT_PRICE, 120, Constants.RIGHT_ALIGN_CELL));
            columnsList.add(new DynamicTableColumn(wfmStrings.overtimeRate(), OVERTIME_RATE, 120, Constants.RIGHT_ALIGN_CELL));
            columnsList.add(new DynamicTableColumn(wfmStrings.weekendOvertimeRate(), WEEKEND_OVERTIME_RATE, 120, Constants.RIGHT_ALIGN_CELL));
            columnsList.add(new DynamicTableColumn(wfmStrings.holidayOvertimeRate(), HOLIDAY_OVERTIME_RATE, 120, Constants.RIGHT_ALIGN_CELL));
        }
        columnsList.add(new DynamicTableColumn(projectStrings.numberOfWorkers(), NUMBER_OF_WORKERS, 120));
        if (isContract) {
            columnsList.add(new DynamicTableColumn(projectStrings.priceType(), PRICE_TYPE, 150));
            columnsList.add(new DynamicTableColumn(wfmStrings.rate(), UNIT_PRICE, 100, Constants.RIGHT_ALIGN_CELL));
            columnsList.add(new DynamicTableColumn(projectStrings.unitQTY(), UNIT_QTY, 100));
        }
        if (!isContract) {
            columnsList.add(new DynamicTableColumn(wfmStrings.employees(), EMPLOYEES, 220));
        }
        return columnsList.toArray(new DynamicTableColumn[]{});
    }

    private Widget[] getWidgetArray(ProjectPosition pp) {
        LinkedHashMap<String, Widget> widgetsMap = getWidgetsMap(pp);
        return widgetsMap.values().toArray(new Widget[]{});
    }

    private LinkedHashMap<String, Widget> getWidgetsMap(ProjectPosition pp) {
        final LinkedHashMap<String, Widget> widgetsMap = new LinkedHashMap<>();

        final DataListBox dwPosition = new DataListBox();
        dwPosition.setItems(positions);

        DatePicker dpContractStart = new DatePicker();
        dpContractStart.setDate(DateUtil.resetTime(new Date()));
        registerDatePickerForUnitPrice(widgetsMap, dpContractStart);

        DatePicker dpContractEnd = new DatePicker();
        registerDatePickerForUnitPrice(widgetsMap, dpContractEnd);

        TextBox txtWageRate = new TextBox();
        Validation.addNumericKeyboardListener(txtWageRate, 2);

        TextBox txtClientCharge = new TextBox();
        Validation.addNumericKeyboardListener(txtClientCharge, 2);

        TextBox txtOvertimeRate = new TextBox();
        Validation.addNumericKeyboardListener(txtOvertimeRate, 2);

        TextBox txtWeekendOvertimeRate = new TextBox();
        Validation.addNumericKeyboardListener(txtWeekendOvertimeRate, 2);

        TextBox txtHolidayOvertimeRate = new TextBox();
        Validation.addNumericKeyboardListener(txtHolidayOvertimeRate, 2);

        TextBox txtNOWorkders = new TextBox();
        Validation.addNumericKeyboardListener(txtNOWorkders, 0);
        registerTexBoxForUnitPrice(widgetsMap, txtNOWorkders);
        registerTexBoxHandler(widgetsMap, txtNOWorkders);

        DataListBox priceType = getPriceTypes();
        registerListBoxForUnitPrice(widgetsMap, priceType);
        registerHandler(widgetsMap, priceType);

        TextBox unitPrice = new TextBox();
        Validation.addNumericKeyboardListener(unitPrice, 2);
        registerTexBoxHandler(widgetsMap, unitPrice);

        TextBox unitQTY = new TextBox();
        Validation.addNumericKeyboardListener(unitQTY, 2);
        registerTexBoxHandler(widgetsMap, unitQTY);

        widgetsMap.put(POSITION, dwPosition);
        widgetsMap.put(CONTRACT_START, dpContractStart);
        widgetsMap.put(CONTRACT_END, dpContractEnd);
        if (!isContract) {
            widgetsMap.put(UNIT_PRICE, unitPrice);
            widgetsMap.put(OVERTIME_RATE, txtOvertimeRate);
            widgetsMap.put(WEEKEND_OVERTIME_RATE, txtWeekendOvertimeRate);
            widgetsMap.put(HOLIDAY_OVERTIME_RATE, txtHolidayOvertimeRate);
        }
        widgetsMap.put(NUMBER_OF_WORKERS, txtNOWorkders);
        if (isContract) {
            widgetsMap.put(PRICE_TYPE, priceType);
            widgetsMap.put(UNIT_PRICE, unitPrice);
            widgetsMap.put(UNIT_QTY, unitQTY);
        }

        final EmployeeAssignmentWidget employees = new EmployeeAssignmentWidget();
        employees.setWidgetMap(widgetsMap);

        dwPosition.addValueChangeHandler(changeEvent -> {
            int existCount = 0;
            if (employeeAssignmentPopup.getMap() != null) {
                int count = 0;
                for (int i = 0; i < table.getRowNumber(); i++) {
                    DynamicTableItem tableItem = table.getItem(i);
                    DataListBox position = (DataListBox) tableItem.getColumnById(POSITION);

                    if (position != null && position.getSelectedId() != null && dwPosition.getPreviousSelectedItem() != null
                            && employeeAssignmentPopup.getMap().get(dwPosition.getPreviousSelectedItem().getId()) != null
                            && position.getSelectedId().equals(dwPosition.getPreviousSelectedItem().getId())) {
                        count++;
                    }
                    if (dwPosition.getSelectedId() != null && dwPosition.getSelectedId().equals(position.getSelectedId())) {
                        existCount++;
                    }
                }
                if (!(count >= 1) && dwPosition.getPreviousSelectedItem() != null
                        && employeeAssignmentPopup.getMap().get(dwPosition.getPreviousSelectedItem().getId()) != null) {
                    employeeAssignmentPopup.getMap().remove(dwPosition.getPreviousSelectedItem().getId());
                }
            }
            employees.setPositionID(dwPosition.getSelectedId());
            employees.clearList();

            if (existCount > 1) {
                String alert = projectStrings.positionIsAlreadyUsedInTheProject();
                Info.show(dwPosition.getSelectedItem() != null ? dwPosition.getSelectedItem().getName() + " " + alert
                        : "Position Name " + alert + ".", Info.Type.WARNING);
            }
        });

        if (!isContract) {
            widgetsMap.put(EMPLOYEES, employees);
        }

        if (pp != null) {
            dwPosition.setSelected(pp.getPositionId());
            dpContractStart.setDate(pp.getContractStart() != null ? pp.getContractStart().getNonConvertedDate() : DateUtil.resetTime(new Date()));
            dpContractEnd.setDate(pp.getContractEnd() != null ? pp.getContractEnd().getNonConvertedDate() : null);

            if (!isContract) {
                unitPrice.setText(Utils.getNumberFormat().format(pp.getUnitPrice() != null ? pp.getUnitPrice() : BigDecimal.ZERO));
                txtOvertimeRate.setText(Utils.getNumberFormat().format(pp.getOvertimeRate() != null ? pp.getOvertimeRate() : BigDecimal.ZERO));
                txtWeekendOvertimeRate.setText(Utils.getNumberFormat().format(pp.getWeekendOvertimeRate() != null ? pp.getWeekendOvertimeRate() : BigDecimal.ZERO));
                txtHolidayOvertimeRate.setText(Utils.getNumberFormat().format(pp.getHolidayOvertimeRate() != null ? pp.getHolidayOvertimeRate() : BigDecimal.ZERO));
            }
            txtNOWorkders.setText(pp.getNumberOfWorker() != null ? pp.getNumberOfWorker().toString() : null);
            if (isContract) {
                priceType.setSelected(pp.getPriceType());
                unitPrice.setText(Utils.getNumberFormat().format(pp.getUnitPrice() != null ? pp.getUnitPrice() : BigDecimal.ZERO));
                unitQTY.setText(Utils.getNumberFormat().format(pp.getUnitQTY() != null ? pp.getUnitQTY() : BigDecimal.ZERO));
            }
            employees.setPositionID(pp.getPositionId());
            if ((pp.getMembers() != null && pp.getMembers().length > 0) && !isContract) {
                employees.setMembers(pp.getMembers());
            }
        }
        return widgetsMap;
    }

    private void registerListBoxForUnitPrice(final LinkedHashMap<String, Widget> widgetsMap, DataListBox priceType) {
        priceType.addValueChangeHandler(changeEvent -> {
            if (isContract) {
                calculateForUnitPrice(widgetsMap);
            }
        });
    }

    private void calculateForUnitPrice(final LinkedHashMap<String, Widget> widgetsMap) {

        DatePicker contractStart = (DatePicker) widgetsMap.get(CONTRACT_START);
        DatePicker contractEnd = (DatePicker) widgetsMap.get(CONTRACT_END);
        TextBox numberOfWorkers = (TextBox) widgetsMap.get(NUMBER_OF_WORKERS);
        DataListBox priceType = (DataListBox) widgetsMap.get(PRICE_TYPE);

        final TextBox unitPrice = (TextBox) widgetsMap.get(UNIT_QTY);

        if (priceType == null || priceType.getSelectedItem() == null || Utils.isNullOrEmpty(numberOfWorkers.getText()) || contractStart.getDate() == null || contractEnd.getDate() == null) {
            unitPrice.setText(Utils.getNumberFormat().format(Double.parseDouble("0.00")));
            calculateTotalCharge(widgetsMap);
            return;
        }
        ProjectPosition po = new ProjectPosition();
        po.setContractStart(new DateNonConvertable(contractStart.getDate()));
        po.setContractEnd(contractEnd.getDate() != null ? new DateNonConvertable(contractEnd.getDate()) : null);
        po.setNumberOfWorker((numberOfWorkers.getText() != null && !numberOfWorkers.getText().isEmpty()) ? Integer.parseInt(numberOfWorkers.getText()) : null);
        po.setPriceType(priceType.getSelectedId());

        CommonService.App.get().getCalculateUnitPrice(po, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(String priceTotal) {
                unitPrice.setText(Utils.getNumberFormat().format(Double.parseDouble(priceTotal)));
                calculateTotalCharge(widgetsMap);
            }
        });
    }

    private void registerTexBoxForUnitPrice(final LinkedHashMap<String, Widget> widgetsMap, TextBox txtNOWorkders) {
        txtNOWorkders.addChangeHandler(changeEvent -> {
            if (isContract) {
                calculateForUnitPrice(widgetsMap);
            }
        });
    }

    private void registerDatePickerForUnitPrice(final LinkedHashMap<String, Widget> widgetsMap, DatePicker dpContractStart) {
        dpContractStart.addChangeHandler(changeEvent -> {
            if (isContract) {
                calculateForUnitPrice(widgetsMap);
            }
        });
    }

    private void registerTexBoxHandler(final LinkedHashMap<String, Widget> widgetsMap, TextBox txtBox) {
        txtBox.addChangeHandler(changeEvent -> calculateTotalCharge(widgetsMap));
    }

    private void registerHandler(final LinkedHashMap<String, Widget> widgetsMap, DataListBox priceType) {
        priceType.addValueChangeHandler(changeEvent -> calculateTotalCharge(widgetsMap));
    }

    private void calculateTotalCharge(LinkedHashMap<String, Widget> widgetsMap) {
        ProjectPosition po = new ProjectPosition();
        DataListBox priceType = (DataListBox) widgetsMap.get(PRICE_TYPE);
        TextBox numberOfWorkers = (TextBox) widgetsMap.get(NUMBER_OF_WORKERS);
        TextBox unitPrice = (TextBox) widgetsMap.get(UNIT_PRICE);
        TextBox unitQTY = (TextBox) widgetsMap.get(UNIT_QTY);

        if (priceType == null || priceType.getSelectedItem() == null) {
            return;
        }
        po.setPriceTypeString(priceType.getSelectedItem().getDescription());
        po.setNumberOfWorker((numberOfWorkers.getText() != null && !numberOfWorkers.getText().isEmpty()) ? Integer.parseInt(numberOfWorkers.getText()) : null);
        po.setUnitPrice(BigDecimal.valueOf(Utils.getNumberFormat().parse(unitPrice.getText() != null && !unitPrice.getText().isEmpty() ? unitPrice.getText() : "0")));
        po.setUnitQTY(BigDecimal.valueOf(Utils.getNumberFormat().parse(unitQTY.getText() != null && !unitQTY.getText().isEmpty() ? unitQTY.getText() : "0")));

    }

    public DataListBox getPriceTypes() {
        DataListBox daysStringListBox = new DataListBox();
        daysStringListBox.addListItem(new SelectItem(0, " " + projectStrings.byHour(), "formula_byHour"));        //byHour
        daysStringListBox.addListItem(new SelectItem(1, " " + projectStrings.byMonth(), "formula_byMonth"));       //byMonth
        daysStringListBox.addListItem(new SelectItem(2, " " + projectStrings.byMarkup(), "formula_byMarkup"));    //byMarkup
        daysStringListBox.addListItem(new SelectItem(3, " " + projectStrings.byMarkupAndOT(), "formula_byMarkupAndOT"));    //byMarkupAndOT
        daysStringListBox.addListItem(new SelectItem(4, " " + projectStrings.byDayAndOT(), "formula_byDayAndOT"));    //byDayAndOT
        daysStringListBox.addListItem(new SelectItem(5, " " + projectStrings.byDayAndOTSpec(), "formula_byDayAndOTSpec"));    //byDayAndOTSpec
        return daysStringListBox;
    }

    public void setValues(ProjectPosition[] projectPositions) {
        if (table == null) {
            return;
        }

        if (projectPositions != null && projectPositions.length > 0) {
            table.clear();

            for (ProjectPosition pp : projectPositions) {
                table.addRow(pp.getObjectID(), getWidgetArray(pp));
            }
        }
    }

    public ProjectPosition[] getProjectPositions() {
        ArrayList<ProjectPosition> list = new ArrayList<>();
        selectedPositionMap.clear();

        for (int i = 0; i < table.getRowNumber(); i++) {
            DynamicTableItem tableItem = table.getItem(i);
            DataListBox position = (DataListBox) tableItem.getColumnById(POSITION);
            DatePicker contractStart = (DatePicker) tableItem.getColumnById(CONTRACT_START);
            DatePicker contractEnd = (DatePicker) tableItem.getColumnById(CONTRACT_END);
            TextBox clientCharge = (TextBox) tableItem.getColumnById(CLIENT_CHARGE);
            TextBox overTimeRate = (TextBox) tableItem.getColumnById(OVERTIME_RATE);
            TextBox weekendOverTimeRate = (TextBox) tableItem.getColumnById(WEEKEND_OVERTIME_RATE);
            TextBox holidayOverTimeRate = (TextBox) tableItem.getColumnById(HOLIDAY_OVERTIME_RATE);

            TextBox numberOfWorkers = (TextBox) tableItem.getColumnById(NUMBER_OF_WORKERS);
            DataListBox priceType = (DataListBox) tableItem.getColumnById(PRICE_TYPE);
            TextBox unitPrice = (TextBox) tableItem.getColumnById(UNIT_PRICE);
            TextBox unitQTY = (TextBox) tableItem.getColumnById(UNIT_QTY);

            if (position.getSelectedId() != null) {
                ProjectPosition projectPosition = new ProjectPosition();
                projectPosition.setObjectID(tableItem.getObjectId());
                projectPosition.setPositionId(position.getSelectedId());
                projectPosition.setContractStart(new DateNonConvertable(DateUtil.resetTime(contractStart.getDate())));
                projectPosition.setContractEnd(contractEnd.getDate() != null ? new DateNonConvertable(DateUtil.resetTime(contractEnd.getDate())) : null);
                if (!isContract) {

                    projectPosition.setOvertimeRate(BigDecimal.valueOf(Utils.getNumberFormat().parse(overTimeRate.getText() != null && !overTimeRate.getText().isEmpty() ? overTimeRate.getText() : "0")));
                    projectPosition.setWeekendOvertimeRate(BigDecimal.valueOf(Utils.getNumberFormat().parse(weekendOverTimeRate.getText() != null && !weekendOverTimeRate.getText().isEmpty() ? weekendOverTimeRate.getText() : "0")));
                    projectPosition.setHolidayOvertimeRate(BigDecimal.valueOf(Utils.getNumberFormat().parse(holidayOverTimeRate.getText() != null && !holidayOverTimeRate.getText().isEmpty() ? holidayOverTimeRate.getText() : "0")));
                }
                projectPosition.setNumberOfWorker((numberOfWorkers.getText() != null && !numberOfWorkers.getText().isEmpty()) ? Integer.parseInt(numberOfWorkers.getText()) : null);
                projectPosition.setUnitPrice(BigDecimal.valueOf(Utils.getNumberFormat().parse(unitPrice.getText() != null && !unitPrice.getText().isEmpty() ? unitPrice.getText() : "0")));

                if (isContract) {
                    projectPosition.setPriceType(priceType.getSelectedId());
                    projectPosition.setUnitQTY(BigDecimal.valueOf(Utils.getNumberFormat().parse(unitQTY.getText() != null && !unitQTY.getText().isEmpty() ? unitQTY.getText() : "0")));
                }

                list.add(projectPosition);
                selectedPositionMap.put(position.getSelectedId(), projectPosition);
            }
        }
        return list.toArray(new ProjectPosition[]{});
    }

    public ArrayList<KpiTreeInfo> getProjectMembers() {
        ArrayList<KpiTreeInfo> list = new ArrayList<>();

        Map<Integer, HashMap<String, KpiTreeInfo>> map = employeeAssignmentPopup.getMap();

        for (Integer positionId : map.keySet()) {
            for (KpiTreeInfo member : map.get(positionId).values()) {

                if (selectedPositionMap.get(positionId) != null) {

                    if (member.getContractStart() == null) {
                        member.setContractStart(selectedPositionMap.get(positionId).getContractStart());
                    }
                    if (member.getContractEnd() == null) {
                        member.setContractEnd(selectedPositionMap.get(positionId).getContractEnd());
                    }
                    member.setPositionId(positionId);
                }
                list.add(member);
            }
        }
        return list;
    }

    public SelectItem[] getSelectedMemebers() {
        ArrayList<SelectItem> list = new ArrayList<>();
        Map<Integer, HashMap<String, KpiTreeInfo>> map = employeeAssignmentPopup.getMap();
        Map<Integer, KpiTreeInfo> duplicateMemberMap = new HashMap<>();

        for (Integer positionId : map.keySet()) {
            for (KpiTreeInfo member : map.get(positionId).values()) {

                if (duplicateMemberMap.get(member.getId()) == null) {
                    SelectItem item = new SelectItem(member.getId(), member.getName());

                    list.add(item);
                    duplicateMemberMap.put(member.getId(), member);
                }
            }
        }
        return list.toArray(new SelectItem[]{});
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String validateEndDateAfterStartDate() {

        for (int i = 0; i < table.getRowNumber(); i++) {
            DynamicTableItem tableItem = table.getItem(i);
            DataListBox position = (DataListBox) tableItem.getColumnById(POSITION);
            DatePicker contractStart = (DatePicker) tableItem.getColumnById(CONTRACT_START);
            DatePicker contractEnd = (DatePicker) tableItem.getColumnById(CONTRACT_END);

            position.removeStyleName("x-form-invalid");
            contractStart.removeStyleName("x-form-invalid");
            contractEnd.removeStyleName("x-form-invalid");

            if (contractStart.getDate() != null && contractEnd.getDate() != null && contractStart.getDate().after(contractEnd.getDate())) {
                contractEnd.addStyleName("x-form-invalid");
            } else {
                return null;
            }
        }
        return projectStrings.contractEndDateCanNotBeEarlier();
    }

    private void removePositionFromMap() {
        itemIds.clear();

        for (int i = 0; i < table.getRowNumber(); i++) {
            DynamicTableItem tableItem = table.getItem(i);
            DataListBox position = (DataListBox) tableItem.getColumnById(POSITION);
            itemIds.add(position.getSelectedId());
        }

        Set<Integer> itemsForDelete = new HashSet<>();
        for (Integer key : employeeAssignmentPopup.getMap().keySet()) {
            if (!itemIds.contains(key)) {
                itemsForDelete.add(key);
            }
        }

        for (Integer key : itemsForDelete) {
            employeeAssignmentPopup.getMap().remove(key);
        }

        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ASSIGN_EMPLOYEE_TO_PROJECT, null, null);
    }

    public FlowPanel getPnlContainer() {
        return pnlContainer;
    }

    public String validateEndDate() {

        for (int i = 0; i < table.getRowNumber(); i++) {
            DynamicTableItem tableItem = table.getItem(i);
            DataListBox position = (DataListBox) tableItem.getColumnById(POSITION);
            DatePicker contractEnd = (DatePicker) tableItem.getColumnById(CONTRACT_END);
            position.removeStyleName("x-form-invalid");
            contractEnd.removeStyleName("x-form-invalid");

            if (position.getSelectedItem() == null && contractEnd.getDate() == null) {
                position.addStyleName("x-form-invalid");
                contractEnd.addStyleName("x-form-invalid");
            }
            if (position.getSelectedItem() == null) {
                position.addStyleName("x-form-invalid");
            }
            if (contractEnd.getDate() == null) {
                contractEnd.addStyleName("x-form-invalid");
            }
        }
        return " ";
    }

    private class Panel extends FlowPanel implements HasClickHandlers {
        @Override
        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addDomHandler(handler, ClickEvent.getType());
        }
    }

    class EmployeeAssignmentWidget extends Composite {
        private final ArrayList<SelectItem> selectedItems;
        private final UL list;
        private Anchor moreLink, employeeAssignmentLink;
        private Integer positionID;
        Map<String, Widget> map;

        EmployeeAssignmentWidget() {
            selectedItems = new ArrayList<>();
            list = new UL();
//            list.setStyleName("token-list-fb token-list--projectPosition");
            list.setStyleName("token-list-fb token-list-fb--cell");
            init();

        }

        private void init() {
            final Panel panel = new Panel();
            panel.add(list);
            panel.addStyleName("employee-assign__token-list-wrapper");

            final UL.LI item = getAssignLabel();
            list.add(item);

            employeeAssignmentPopup.getBtnSaveAndClose().addClickHandler(clickEvent -> {
                employeeAssignmentPopup.close();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ASSIGN_EMPLOYEE_TO_PROJECT, null, null);

                if (employeeAssignmentPopup.getMap().get(positionID) != null) {
                    list.clear();
                    int index = 0;
                    selectedItems.clear();

                    for (KpiTreeInfo item1 : employeeAssignmentPopup.getMap().get(positionID).values()) {
                        if (item1.isRejected()) {
                            employeeAssignmentPopup.getMap().get(positionID).remove(item1.getKey());
                            continue;
                        }

                        add(new SelectItem(item1.getId(), item1.getName()));
                        index++;

                        if (index >= 3) {
                            moreLink.setVisible(true);
                            break;
                        }
                    }

                    if (selectedItems.isEmpty()) {
                        list.add(getAssignLabel());
                    }
                }
            });
            moreLink = new Anchor(wfmStrings.more() + "...");
            moreLink.addClickHandler(clickEvent -> {
                DatePicker contractEndPicker = (DatePicker) map.get(CONTRACT_END);
                if (contractEndPicker.getDate() != null) {
                    employeeAssignmentPopup.setContractEndDate(contractEndPicker.getDate());
                } else {
//                        employeeAssignmentPopup.setContractEndDate(DateUtil.addYears(((DatePicker) map.get(CONTRACT_START)).getDate(), 1));
                }

                employeeAssignmentPopup.open();
                employeeAssignmentPopup.setPositionID(positionID);
                employeeAssignmentPopup.loadPositionAssignedEmployee(positionID);
            });

            employeeAssignmentLink = new Anchor(wfmStrings.assignedEmployees());
            employeeAssignmentLink.addClickHandler(clickEvent -> {
                if (positionID == null) {
                    Validation.validateDataListBoxRequired((DataListBox) map.get(POSITION));
                } else {
                    employeeAssignmentPopup.setProjectID(projectID);
                    employeeAssignmentPopup.setPositionID(positionID);
                    employeeAssignmentPopup.setContractStartDate(((DatePicker) map.get(CONTRACT_START)).getDate());

                    DatePicker contractEndPicker = (DatePicker) map.get(CONTRACT_END);
                    if (contractEndPicker.getDate() != null) {
                        employeeAssignmentPopup.setContractEndDate(contractEndPicker.getDate());
                    } else {
//                            employeeAssignmentPopup.setContractEndDate(DateUtil.addYears(((DatePicker) map.get(CONTRACT_START)).getDate(), 1));
                    }

                    employeeAssignmentPopup.setAvailableFrom(((DatePicker) map.get(CONTRACT_START)).getDate());
                    String strNumberOfWorkder = ((TextBox) map.get(NUMBER_OF_WORKERS)).getText();
                    employeeAssignmentPopup.setNumberOfWorker(strNumberOfWorkder != null && !strNumberOfWorkder.isEmpty() ? Integer.parseInt(strNumberOfWorkder) : null);
                    employeeAssignmentPopup.open();
                    employeeAssignmentPopup.resetFilter();
                    employeeAssignmentPopup.loadData(null);
                }
            });

            HorizontalPanel pnlLinkContainer = new HorizontalPanel();
            pnlLinkContainer.addStyleName("employee-assign__actions");
            pnlLinkContainer.add(moreLink);
            pnlLinkContainer.add(employeeAssignmentLink);
            pnlLinkContainer.setCellHorizontalAlignment(moreLink, HasHorizontalAlignment.ALIGN_LEFT);
            pnlLinkContainer.setCellHorizontalAlignment(employeeAssignmentLink, HasHorizontalAlignment.ALIGN_RIGHT);
            pnlLinkContainer.setSpacing(5);


            VerticalPanel pnlContainer = new VerticalPanel();
            pnlContainer.add(panel);
            pnlContainer.add(pnlLinkContainer);
            pnlContainer.addStyleName("employee-assign");
            pnlLinkContainer.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_LEFT);

            initWidget(pnlContainer);
        }

        private void add(SelectItem item) {
            if (item == null) {
                return;
            }

            final UL.LI displayItem = new UL.LI(item);
            displayItem.setStyleName("token-fb");
            Tag p = new Tag("p", "", item.getName());

            displayItem.addClickHandler(clickEvent -> {
                if (displayItem.getStyleName() != null && displayItem.getStyleName().contains("selected")) {
                    displayItem.addStyleName("token-fb");
                } else {
                    displayItem.addStyleName("selected-token-fb");
                }
            });

            Tag span = new Tag("span", "close", "");
            span.addClickHandler(clickEvent -> removeListItem(displayItem, list));

            displayItem.add(p);
            displayItem.add(span);
            // hold the original value of the item selected
            list.insert(displayItem, (list.getWidgetCount() > 1 ? list.getWidgetCount() - 1 : 0));

            selectedItems.add(displayItem.getSelectItem());
            list.fireEvent(new ChangeEvent() {
                @Override
                protected void dispatch(ChangeHandler handler) {
                    super.dispatch(handler);
                }
            });
        }

        private void removeListItem(UL.LI displayItem, UL list) {
            selectedItems.remove(displayItem.getSelectItem());
            list.remove(displayItem);
            list.fireEvent(new ChangeEvent() {
                @Override
                protected void dispatch(ChangeHandler handler) {
                    super.dispatch(handler);
                }
            });
//            employeeAssignmentPopup.getMap().get(positionID).remove(displayItem.getSelectItem().getId());
            String needDeleteKey = "";
            for (String childKEy : employeeAssignmentPopup.getMap().get(positionID).keySet()) {
                if (employeeAssignmentPopup.getMap().get(positionID).get(childKEy).getId().equals(displayItem.getSelectItem().getId())) {
                    needDeleteKey = childKEy;
                    break;
                }
            }
            employeeAssignmentPopup.getMap().get(positionID).remove(needDeleteKey);

            if (selectedItems.isEmpty()) {
                list.add(getAssignLabel());
            }
        }

        private UL.LI getAssignLabel() {
            UL.LI item = new UL.LI();
            item.setStyleName("input-fb");
            Label label = new Label(projectStrings.PleaseAssignEmployeetotheProject());
            label.setStyleName("employee-assign-label");
            item.add(label);

            return item;
        }

        public void setPositionID(Integer positionID) {
            this.positionID = positionID;
        }

        public void setWidgetMap(Map<String, Widget> map) {
            this.map = map;
        }

        public void setMembers(ProjectMember[] members) {
            if (employeeAssignmentPopup.getMap().get(positionID) != null)
                employeeAssignmentPopup.getMap().get(positionID).clear();

            if (members != null && members.length > 0) {
                list.clear();
                selectedItems.clear();

                int index = 0;
                for (ProjectMember member : members) {
                    if (index < 3) {
                        add(new SelectItem(member.getId(), member.getName()));
                    } else {
                        moreLink.setVisible(true);
                    }

                    KpiTreeInfo kpiTreeInfo = new KpiTreeInfo();
                    kpiTreeInfo.setId(member.getId());
                    kpiTreeInfo.setName(member.getName());
                    kpiTreeInfo.setPositionId(member.getPositionId());
                    kpiTreeInfo.setPositionName(member.getPosititon());
                    kpiTreeInfo.setWageRate(member.getWageRate());
                    kpiTreeInfo.setClientChargeRate(member.getClientChargeRate());
                    kpiTreeInfo.setEmployeeNumber(member.getEmployeeNumber());
                    kpiTreeInfo.setContractStart(member.getContractStart());
                    kpiTreeInfo.setContractEnd(member.getContractEnd());
                    kpiTreeInfo.setProjectEmployeeId(member.getProjectEmployeeId());
                    kpiTreeInfo.setSelected(true);
                    kpiTreeInfo.setKey(UUID.uuid());
                    kpiTreeInfo.setCreatedDate(member.getCreateDate());
                    kpiTreeInfo.setUnit(member.getUnit());

                    if (employeeAssignmentPopup.getMap().get(member.getPositionId()) == null) {
                        HashMap<String, KpiTreeInfo> hashMap = new HashMap<>();
                        hashMap.put(kpiTreeInfo.getKey(), kpiTreeInfo);
                        employeeAssignmentPopup.getMap().put(member.getPositionId(), hashMap);
                    } else {
                        employeeAssignmentPopup.getMap().get(member.getPositionId()).put(kpiTreeInfo.getKey(), kpiTreeInfo);
                    }

                    index++;
                }
            }
        }

        public void clearList() {
            list.clear();
            list.add(getAssignLabel());
        }
    }
}