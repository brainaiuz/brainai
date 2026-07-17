package com.edatasite.workforce.gwt.core.client.ui.laborPeriod;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityServiceAsync;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.leaveRequest.LaborPeriodRequest;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.util.*;

public class LaborPeriodWidget extends Composite implements Constants {

    private static final AvailabilityServiceAsync service = AvailabilityService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    public static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final NumberFormat doubleFormat = NumberFormat.getFormat(",##0.#");

    private final Div container = new Div();
    private DynamicTable periodListTable;
    private DynamicTable historyTable;
    private DynamicTable multiLeaveTable;
    private Integer employeeID;
    private String reasonCode;
    private Integer sickRequestID;
    private final Boolean isSummary;
    private final Boolean isRecalculate;
    private List<LaborPeriodRequest> periodList = new ArrayList<>();
    private final Label totalDays = new Label("0");
    private final FlexTable totalTable = new FlexTable();
    private final HashMap<String, ArrayList<MultiLeaveDTO>> multiLeaveValues = new HashMap<>();
    private CustomModal multiLeaveModal;
    private Double leftLeaveDays;
    private Double leftLeaveDaysBeforeChange;
    private Double minimumLeaveDays;
    private WfmButton2 saveToMap;
    private Integer clickedPeriodRowId;
    private Date startDate;
    private Double usedExperienceDays = 0d;

    public LaborPeriodWidget(Integer employeeID, Boolean isSummary, Boolean isRecalculate) {
        this.employeeID = employeeID;
        this.isSummary = isSummary;
        this.isRecalculate = isRecalculate;
        VerticalPanel vp = new VerticalPanel();
        vp.add(container);
        initWidget(vp);
        initPeriods();
        sinkEvents(Event.ONPASTE);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if (DOM.eventGetType(event) == Event.ONPASTE) {
            event.preventDefault();
        }
    }

    private void initPeriods() {
        if ("LR_TYPE_ANNUAL_LEAVE".equals(reasonCode)) {
            service.getEmployeeAdditonalAllowances(employeeID, sickRequestID, startDate, isSummary, isRecalculate ,new AsyncCallback<ArrayList<LaborPeriodRequest>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    Info.show(wfmStrings.hireDateOrReasonSettingsIsWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(ArrayList<LaborPeriodRequest> leaveRequestByPeriodsItems) {
                    if (leaveRequestByPeriodsItems != null) {
                        periodList = leaveRequestByPeriodsItems;
                        createPeriodTable();
                    } else {
                        Info.show(wfmStrings.hireDateOrReasonSettingsIsWrong(), Info.Type.WARNING);
                    }
                }
            });
        }
    }

    private void createPeriodTable() {
        container.clear();
        periodListTable = new DynamicTable(getColumns(), false);
        FlexTable.FlexCellFormatter formatter = periodListTable.getFlexCellFormatter();
        if (isSummary) {
            formatter.setVisible(0, 6, false);
        }
        initPeriodList();
        container.add(periodListTable);
    }

    private DynamicTableColumn[] getColumns() {
        LinkedList<DynamicTableColumn> columnsList = new LinkedList<>();


        DynamicTableColumn period = new DynamicTableColumn(wfmStrings.employmentPeriod(), "EMPLOYMENT_PERIOD", 0);
        period.setSaveWhiteSpace(true);

        DynamicTableColumn leaveAllowance = new DynamicTableColumn(wfmStrings.leaveAllowance(), "LEAVE_ALLOWANCE", 0);
        leaveAllowance.setStyle("text-align: center");

        DynamicTableColumn experienceDays = new DynamicTableColumn(wfmStrings.experienceDays(), "EXPERIENCE_DAYS", 0);
        experienceDays.setStyle("text-align: center");

        DynamicTableColumn currentBalance = new DynamicTableColumn(hrmsStrings.currentBalance(), "CURRENT_BALANCE", 0);
        currentBalance.setVisible(false);
        currentBalance.setStyle("text-align: center");

        DynamicTableColumn taken = new DynamicTableColumn(wfmStrings.taken(), "TAKEN", 0);
        taken.setStyle("text-align: center");

        DynamicTableColumn dayLeft = new DynamicTableColumn(wfmStrings.daysLeft(), "DAYS_LEFT", 0);
        dayLeft.setStyle("text-align: center");

        DynamicTableColumn inputBox = new DynamicTableColumn(wfmStrings.typeHere(), "TYPE_HERE", 0);
        inputBox.setStyle("text-align: center");

        DynamicTableColumn minValueBox = new DynamicTableColumn(wfmStrings.minValue(), "MIN_VALUE", 0);
        minValueBox.setVisible(false);

        DynamicTableColumn overAllSubmitted = new DynamicTableColumn(wfmStrings.submitted(), "OVERALL_SUBMITTED", 0);
        overAllSubmitted.setVisible(false);

        if (isSummary) {
            inputBox.setVisible(false);
        }

        columnsList.add(period);
        columnsList.add(leaveAllowance);
        columnsList.add(experienceDays);
        columnsList.add(currentBalance);
        columnsList.add(taken);
        columnsList.add(dayLeft);
        columnsList.add(inputBox);
        columnsList.add(minValueBox);
        columnsList.add(overAllSubmitted);

        return columnsList.toArray(new DynamicTableColumn[columnsList.size()]);
    }


    private void initPeriodList() {
        periodListTable.clear();
        if (periodList != null && !periodList.isEmpty()) {
            int i = 0;
            for (LaborPeriodRequest period : periodList) {
                Map<String, Widget> itemWidgetsMap = new LinkedHashMap<>();
                double leftLeaveDays = period.getAllowance() - period.getApprovedTakenDays() + period.getExperienceDays();

                MaterialLink leavePeriod = new MaterialLink();
                leavePeriod.setText(period.getLaborPeriod());
                if (Utils.hasPermission(PermissionConstants.HRMS_MULTI_LEAVE)) {
                    int finalI = i;
                    leavePeriod.addClickHandler(click -> {
                        if (isSummary || period.getApprovedTakenDays() < period.getAllowance()) {
                            getMultiLeaveModal(finalI, period.getObjectID(), period.getLaborPeriod(), leftLeaveDays, period.getMinLeaveDays());
                        } else {
                            Info.show(wfmStrings.leaveDays() + " " + wfmStrings.exceeded(), Info.Type.WARNING);
                        }
                    });
                }

                Span leaveAllowance = new Span();
                leaveAllowance.setText(period.getAllowance().toString());

                Span experienceDays = new Span();
                experienceDays.setText(period.getExperienceDays().toString());

                Span currentBalance = new Span();
                currentBalance.setText("CB");

                MaterialLink taken = new MaterialLink();
                taken.setText(period.getApprovedTakenDays().toString());
                taken.addClickHandler(handler -> getTakenDaysByPeriod(period.getObjectID()));

                Span daysLeft = new Span();
                daysLeft.setText(String.valueOf(leftLeaveDays));

                TextBox typeHere = new TextBox();
                typeHere.ensureDebugId("leave_days");
                typeHere.getElement().setPropertyString("style", "margin: 2px auto; text-align: center !important;");
                if (period.getCurrentLeaveDays() != null) {
                    typeHere.setText(String.valueOf(period.getCurrentLeaveDays()));
                }
                Validation.addPositiveNumericKeyboardListener(typeHere);
                typeHere.addKeyUpHandler(handler -> {
                            multiLeaveValues.remove(period.getLaborPeriod());
                            typeHere.removeStyleName(Constants.ERROR_FORM_STYLE);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVE_DAYS_INSERTED, null, LaborPeriodWidget.this);
                            if (!typeHere.getText().isEmpty()) {
                                validate();
                            }
                        }
                );
                if (period.getApprovedTakenDays() >= period.getAllowance()) {
                    typeHere.setEnabled(false);
                }

                Span minValue = new Span();
                minValue.setText(period.getMinLeaveDays().toString());

                Span overAllSubmitted = new Span();
                overAllSubmitted.setText(period.getOverAllSubmittedLeaveDays().toString());

                itemWidgetsMap.put("EMPLOYMENT_PERIOD", leavePeriod);
                itemWidgetsMap.put("LEAVE_ALLOWANCE", leaveAllowance);
                itemWidgetsMap.put("EXPERIENCE_DAYS", experienceDays);
                itemWidgetsMap.put("CURRENT_BALANCE", currentBalance);
                itemWidgetsMap.put("TAKEN", taken);
                itemWidgetsMap.put("DAYS_LEFT", daysLeft);
                itemWidgetsMap.put("TYPE_HERE", typeHere);
                itemWidgetsMap.put("MIN_VALUE", minValue);
                itemWidgetsMap.put("OVERALL_SUBMITTED", overAllSubmitted);
                i++;
                periodListTable.addRow(period.getPeriodID(), itemWidgetsMap.values().toArray(new Widget[]{}));
            }
        }
    }

    public void setEmployeeID(Integer employeeID, String reasonCode, Date startDate, Integer requestID) {
        this.employeeID = employeeID;
        this.reasonCode = reasonCode;
        this.startDate = startDate;
        this.sickRequestID = requestID;
        initPeriods();
    }

    public Integer getLeaveDays() {
        int overAllLeaveDays = 0;
        if (periodListTable != null) {
            int rowCount = periodListTable.getRowNumber();
            for (int i = 0; i < rowCount; i++) {
                DynamicTableItem tableItem = periodListTable.getItem(i);
                TextBox inputBox = (TextBox) tableItem.getColumnById("TYPE_HERE");
                overAllLeaveDays += !"".equals(inputBox.getText()) ? Integer.parseInt(inputBox.getText()) : 0;
            }
        }
        return overAllLeaveDays;
    }

    public ArrayList<LaborPeriodRequest> getValues() {
        ArrayList<LaborPeriodRequest> list = new ArrayList<>();
        int i = 0;
        usedExperienceDays = 0d;
        for (LaborPeriodRequest request : periodList) {
            DynamicTableItem tableItem = periodListTable.getItem(i);
            MaterialLink employmentPeriod = (MaterialLink) tableItem.getColumnById("EMPLOYMENT_PERIOD");
            TextBox inputBox = (TextBox) tableItem.getColumnById("TYPE_HERE");
            Double leftDays = Utils.universalParse(doubleFormat, ((Span) tableItem.getColumnById("DAYS_LEFT")).getText());
            Double experienceDays = Utils.universalParse(doubleFormat, ((Span) tableItem.getColumnById("EXPERIENCE_DAYS")).getText());

            if (!Objects.equals(inputBox.getText(), "")) {
                LaborPeriodRequest periodRequest = new LaborPeriodRequest();
                String key = employmentPeriod.getText();
                Double takenDays = Utils.universalParse(doubleFormat, inputBox.getText());
                if (multiLeaveValues.containsKey(key)) {
                    periodRequest.setPeriodID(request.getObjectID());
                    periodRequest.setMultiLeaveList(multiLeaveValues.get(employmentPeriod.getText()));
                    list.add(periodRequest);
                } else {
                    periodRequest.setPeriodID(request.getObjectID());
                    periodRequest.setTakenDays(takenDays);
                    list.add(periodRequest);
                }
                if (takenDays > (leftDays - experienceDays)) {
                    usedExperienceDays += takenDays - (leftDays - experienceDays);
                }
            }
            i++;
        }
        return list;
    }
    public Double getUsedExperienceDays() {
        return usedExperienceDays;
    }

    public boolean validate() {
        int invalidInput = 0;
        double tottalDays = 0d;
        int rowCount = periodListTable.getRowNumber();
        if (rowCount <= 1) {
            return false;
        }
        for (int i = 0; i < rowCount; i++) {
            DynamicTableItem tableItem = periodListTable.getItem(i);
            TextBox inputBox = (TextBox) tableItem.getColumnById("TYPE_HERE");
            Span minValue = (Span) tableItem.getColumnById("MIN_VALUE");
            MaterialLink taken = (MaterialLink) tableItem.getColumnById("TAKEN");

            double leaveAllowenceDays = Utils.universalParse(doubleFormat, ((Span) tableItem.getColumnById("LEAVE_ALLOWANCE")).getText());
            double experienceDays = Utils.universalParse(doubleFormat, ((Span) tableItem.getColumnById("EXPERIENCE_DAYS")).getText());
            double input = !Objects.equals(inputBox.getText(), "") ? Utils.universalParse(doubleFormat, inputBox.getText()) : 0;
            double minValueBox = !Objects.equals(minValue.getText(), "") ? Utils.universalParse(doubleFormat, minValue.getText()) : 0;
            double takenDays = !Objects.equals(taken.getText(), "") ? Utils.universalParse(doubleFormat, taken.getText()) : 0;

            tottalDays += input;
            leaveAllowenceDays += experienceDays;

            if (input >= 0 && input <= leaveAllowenceDays - takenDays) {
                if (takenDays >= minValueBox) {
                    if (!(input <= leaveAllowenceDays - takenDays)) {
                        inputBox.addStyleName(Constants.ERROR_FORM_STYLE);
                        Info.show(wfmMessages.youMustEnterMoreMinimumLeaveDays(String.valueOf(minValueBox)), Info.Type.WARNING, Info.Position.BOTTOM_RIGHT, 5000);
                        invalidInput++;
                    }
                } else if (input < minValueBox) {
                    if (!(minValueBox <= leaveAllowenceDays - takenDays - input)) {
                        inputBox.addStyleName(Constants.ERROR_FORM_STYLE);
                        Info.show(wfmMessages.youMustEnterMoreMinimumLeaveDays(String.valueOf(minValueBox)), Info.Type.WARNING, Info.Position.BOTTOM_RIGHT, 5000);
                        invalidInput++;
                    }
                }
            } else {
                inputBox.addStyleName(Constants.ERROR_FORM_STYLE);
                Info.show(wfmStrings.errorUsersLimitExceeded(), Info.Type.WARNING, Info.Position.BOTTOM_RIGHT, 5000);
                invalidInput++;
            }
        }
        if (tottalDays == 0) {
            Info.show(wfmStrings.pleaseEnterValue(), Info.Type.WARNING, Info.Position.BOTTOM_RIGHT, 5000);
            invalidInput++;
        }
        return invalidInput <= 0;
    }

    private void getLeaveHistoryModal(ArrayList<LaborPeriodRequest> laborPeriodRequests) {
        KpiSideNavBox historyModal = new KpiSideNavBox(true);
        historyModal.setStyle("width: 40%;");
        setStyleName(historyModal.getElement(), "quick-add", true);
        StringBuilder title = new StringBuilder();
        if ("uz".equals(Utils.getCurrentLocale())) {
            title.append(laborPeriodRequests.get(0).getLaborPeriod()).append(" - ").append(wfmStrings.dataFor());
        } else {
            title.append(wfmStrings.dataFor()).append(" - ").append(laborPeriodRequests.get(0).getLaborPeriod());
        }
        Heading h1 = new Heading(HeadingSize.H1);
        h1.setText(title.toString());
        historyModal.addHeader(h1);
        createHistoryTable(laborPeriodRequests);
        historyModal.addBody(historyTable);
        historyModal.open();
    }

    private void createHistoryTable(ArrayList<LaborPeriodRequest> laborPeriodRequests) {
        historyTable = new DynamicTable(getHistoryTableColumns(), false);
        historyTable.setStyleName("dataTable");
        historyTable.setHeaderTextToCenter(1, Style.TextAlign.CENTER);
        fillHistoryTable(laborPeriodRequests);
    }

    private DynamicTableColumn[] getHistoryTableColumns() {
        LinkedList<DynamicTableColumn> columnsList = new LinkedList<>();

        columnsList.add(new DynamicTableColumn(wfmStrings.code(), "LEAVE_REQUEST_CODE", 30));
        columnsList.add(new DynamicTableColumn(wfmStrings.leavePeriod(), "LEAVE_PERIOD", 30));
        columnsList.add(new DynamicTableColumn(wfmStrings.duration(), "LEAVE_DURATION", 30));
        columnsList.add(new DynamicTableColumn(wfmStrings.status(), "STATUS", 30));
        columnsList.add(new DynamicTableColumn(wfmStrings.createdDate(), "LEAVE_CREATED_DATE", 30));

        return columnsList.toArray(new DynamicTableColumn[columnsList.size()]);
    }

    private void fillHistoryTable(ArrayList<LaborPeriodRequest> laborPeriodRequests) {
        if (laborPeriodRequests != null && laborPeriodRequests.size() > 0) {
            int i = 1;
            HTMLTable.CellFormatter cellFormatter = historyTable.getCellFormatter();
            for (LaborPeriodRequest request : laborPeriodRequests) {
                MaterialLink linkToLeaveRequest = new MaterialLink();
                linkToLeaveRequest.setText(request.getLeaveRequestNumber());
                if (request.getLeaveRequestID() > 0) {
                    linkToLeaveRequest.addClickHandler(click -> SinksContainerFactory.entryPoint.onHistoryChanged("leaverequest/" + request.getLeaveRequestID(), request.getLeaveRequestNumber(), request.getLeaveRequestNumber()));
                }
                historyTable.setWidget(i, 0, linkToLeaveRequest);
                historyTable.setHTML(i, 1, "<span>" + request.getLeavePeriod() + "</span>");
                historyTable.setHTML(i, 2, "<span>" + request.getApprovedTakenDays() + "</span>");
                historyTable.setHTML(i, 3, "<span>" + request.getLeaveRequestStatus() + "</span>");
                historyTable.setHTML(i, 4, "<span>" + request.getCreatedDate() + "</span>");
                String bgColor = "background-color: #d2d2d2;";
                if (Constants.LR_STATUS_SS_APPROVED.equals(request.getLeaveRequestStatusCode()))
                    bgColor = "background-color: #72f100;";
                String alignCenter = "";
                for (int j = 0; j <= 4; j++) {
                    if (j == 2) alignCenter = "text-align: center;";
                    cellFormatter.getElement(i, j).setAttribute("style", bgColor + alignCenter);
                }
                i++;
            }
        }
    }

    private void getTakenDaysByPeriod(Integer periodID) {
        service.getTakenDaysByPeriod(periodID, new AsyncCallback<ArrayList<LaborPeriodRequest>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<LaborPeriodRequest> laborPeriodRequests) {
                if (laborPeriodRequests.size() > 0) {
                    getLeaveHistoryModal(laborPeriodRequests);
                }
            }
        });
    }

    private void formatTotalTable() {
        totalTable.removeAllRows();
        HTMLTable.CellFormatter cellFormatter = totalTable.getCellFormatter();
        cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
        cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
        cellFormatter.setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
        cellFormatter.setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_CENTER);
        cellFormatter.setWidth(0, 0, "50%");
        cellFormatter.setWidth(0, 1, "30%");
        cellFormatter.setWidth(0, 2, "15%");
        cellFormatter.setWidth(0, 3, "5%");
    }

    private Widget[] addNewRow(String leavePeriod) {
        final Map<String, Widget> itemWidgetsMap = new LinkedHashMap<>();
        Label leftDaysLabel = new Label();
        Label minValueLabel = new Label();
        minValueLabel.setText(String.valueOf(minimumLeaveDays));

        if (canAdd(leavePeriod) && multiLeaveTable.getRowNumber() < 2 && leftLeaveDaysBeforeChange != 0) {
            itemWidgetsMap.put("TYPE", getTypeListBox());
            itemWidgetsMap.put("TYPE_HERE", getTextBox());
            itemWidgetsMap.put("LEFT_DAYS", leftDaysLabel);
            itemWidgetsMap.put("MIN_VALUE", minValueLabel);
            onChangeEvent(multiLeaveTable);
        }
        return itemWidgetsMap.values().toArray(new Widget[]{});
    }

    private boolean canAdd(String leavePeriod) {
        double totalDays = 0;
        if (multiLeaveValues.size() > 0 && !multiLeaveValues.containsKey(leavePeriod) && multiLeaveTable.getRowCount() <= 1) {
            return true;
        }
        for (int i = 0; i < multiLeaveTable.getRowNumber(); i++) {
            DynamicTableItem item = multiLeaveTable.getItem(i);
            TextBox textBox = (TextBox) item.getColumnById("TYPE_HERE");
            if (textBox != null) {
                totalDays += !"".equals((textBox).getText()) ? Utils.universalParse(doubleFormat, textBox.getText()) : 0;
            }
        }
        return totalDays < leftLeaveDaysBeforeChange;
    }

    private void getTotalTable() {
        formatTotalTable();
        HTMLTable.CellFormatter formatter = totalTable.getCellFormatter();
        Div addContainer = new Div();
        addContainer.setWidth("100%");

        if (isSummary) {
            totalTable.setWidget(0, 0, addContainer);
            formatter.setVisible(0, 0, false);
        } else {
            totalTable.setWidget(0, 0, addContainer);
        }
        totalTable.setHTML(0, 1, "<b class=customTitle>" + wfmStrings.total() + ":" + "</b>");
        totalTable.setWidget(0, 2, totalDays);

    }

    private void getMultiLeaveModal(int periodListRowID, Integer objectID, String leavePeriod, Double leftLeaveDays, Double minimumLeaveDays) {
        this.leftLeaveDays = leftLeaveDays;
        this.leftLeaveDaysBeforeChange = leftLeaveDays;
        this.minimumLeaveDays = minimumLeaveDays;
        this.totalDays.setText("0");
        this.clickedPeriodRowId = periodListRowID;

        multiLeaveModal = new CustomModal(true);
        setStyleName(multiLeaveModal.getElement(), "quick-add", true);

        Heading h1 = new Heading(HeadingSize.H1);
        h1.setText(leavePeriod);
        multiLeaveModal.addHeader(h1);

        generateMultiLeaveTable(leavePeriod);

        Div totalContainer = new Div();
        totalContainer.setWidth("100%");
        totalContainer.setStyle("display: flex; flex-direction: row; align-items: flex-end; justify-content: space-between;width: 100%; margin-top: 10px;");

        Div dataWrapper = new Div();
        dataWrapper.setStyleName("dataWrapper");

        Div counterContainer = new Div();
        counterContainer.setStyleName("bill-total");
        counterContainer.add(totalTable);

        totalContainer.add(counterContainer);
        saveToMap = new WfmButton2();
        saveToMap.setStyleName(BTN_PRIMARY);
        saveToMap.setText(wfmStrings.save());
        saveToMap.addClickHandler(click -> saveValues(objectID, leavePeriod));

        dataWrapper.add(multiLeaveTable);

        multiLeaveModal.addBody(dataWrapper);
        multiLeaveModal.addBody(totalContainer);
        if (!isSummary) {
            multiLeaveModal.addFooter(saveToMap);
        }
        multiLeaveModal.open();
    }

    private void generateMultiLeaveTable(String leavePeriod) {
        multiLeaveTable = new DynamicTable(getMultiLeaveTableColumns());
        multiLeaveTable.setStyleName("dataTable");
        multiLeaveTable.addListener(new AddListener() {
            @Override
            public void plusClicked(int rowId) {
                multiLeaveTable.addRow(addNewRow(leavePeriod));
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {
                enableButton(true);
                totalDays.removeStyleName(ERROR_FORM_STYLE);
                int days = 0;
                for (int j = 0; j < multiLeaveTable.getRowNumber(); j++) {
                    DynamicTableItem item = multiLeaveTable.getItem(j);
                    String takenDays = ((TextBox) item.getColumnById("TYPE_HERE")).getText();
                    days += "".equals(takenDays) || "0".equals(takenDays) ? 0 : Integer.parseInt(takenDays);
                }
                totalDays.setText(String.valueOf(days));
                totalTable.setWidget(0, 2, totalDays);
            }
        });
        formatMultiLeaveTable();
        getTotalTable();
        initMultiLeaveTable(leavePeriod);
    }

    private void formatMultiLeaveTable() {
        FlexTable.FlexCellFormatter flexCellFormatter = multiLeaveTable.getFlexCellFormatter();
        if (isSummary) {
            flexCellFormatter.setVisible(0, 0, false);
            multiLeaveTable.setShowButtons(false);
        }
        flexCellFormatter.setVisible(0, 3, false);
        flexCellFormatter.setVisible(0, 4, false);
    }

    private DynamicTableColumn[] getMultiLeaveTableColumns() {
        LinkedList<DynamicTableColumn> columnsList = new LinkedList<>();

        DynamicTableColumn type = new DynamicTableColumn(wfmStrings.type(), "TYPE", 150);
        DynamicTableColumn typeHere = new DynamicTableColumn(wfmStrings.typeHere(), "TYPE_HERE", 150);
        DynamicTableColumn leftDays = new DynamicTableColumn(wfmStrings.leftDays(), "LEFT_DAYS", 30);
        leftDays.setVisible(false);

        DynamicTableColumn minValue = new DynamicTableColumn(wfmStrings.minValue(), "MIN_VALUE", 30);
        minValue.setVisible(false);

        columnsList.add(type);
        columnsList.add(typeHere);
        columnsList.add(leftDays);
        columnsList.add(minValue);

        return columnsList.toArray(new DynamicTableColumn[columnsList.size()]);
    }

    private void initMultiLeaveTable(String leavePeriod) {
        multiLeaveTable.clear();
        if (multiLeaveValues.size() > 0 && multiLeaveValues.get(leavePeriod) != null && multiLeaveValues.get(leavePeriod).size() > 0) {
            final Map<String, Widget> itemWidgetsMap = new LinkedHashMap<>();
            List<MultiLeaveDTO> list = multiLeaveValues.get(leavePeriod);
            if (list != null && list.size() > 0) {
                for (MultiLeaveDTO dto : list) {
                    DataListBox listBox = getTypeListBox();
                    listBox.setSelectedByDescription(dto.getSickRequestType());
                    TextBox inputBox = new TextBox();
                    inputBox.setAlignment(ValueBoxBase.TextAlignment.CENTER);
                    inputBox.setText(String.valueOf(dto.getSickRequestDuration()));
                    Validation.addPositiveNumericKeyboardListener(inputBox);
                    inputBox.addKeyUpHandler(keyUpEvent -> {
                        inputBox.removeStyleName(Constants.ERROR_FORM_STYLE);
                        totalTable.getWidget(0, 2).removeStyleName(Constants.ERROR_FORM_STYLE);
                        enableButton(true);
                        int days = 0;
                        for (int j = 0; j < multiLeaveTable.getRowNumber(); j++) {
                            DynamicTableItem item = multiLeaveTable.getItem(j);
                            String takenDays = ((TextBox) item.getColumnById("TYPE_HERE")).getText();
                            days += "".equals(takenDays) || "0".equals(takenDays) ? 0 : Integer.parseInt(takenDays);
                        }
                        totalDays.setText(String.valueOf(days));
                        totalTable.setWidget(0, 2, totalDays);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVE_DAYS_INSERTED, null, LaborPeriodWidget.this);
                    });
                    Label days = (Label) totalTable.getWidget(0, 2);
                    int totalDays = days != null && "".equals(days.getText()) || "0".equals(days.getText()) ? 0 : Integer.parseInt(days.getText());
                    if (!dto.getSickRequestType().equals(Constants.MONEY)) {
                        totalDays += dto.getSickRequestDuration();
                        totalTable.setWidget(0, 2, new Label(String.valueOf(totalDays)));
                        inputBox.setText(String.valueOf(dto.getSickRequestDuration()));
                    } else {
                        totalDays += dto.getSickRequestDuration();
                        totalTable.setWidget(0, 2, new Label(String.valueOf(totalDays)));
                    }

                    Label leftDays = new Label(String.valueOf(dto.getSickRequestLeftDays()));
                    Label minLeaveDays = new Label(String.valueOf(dto.getMinLeaveDays()));
                    itemWidgetsMap.put("TYPE", listBox);
                    itemWidgetsMap.put("TYPE_HERE", inputBox);
                    itemWidgetsMap.put("LEFT_DAYS", leftDays);
                    itemWidgetsMap.put("MIN_VALUE", minLeaveDays);
                    multiLeaveTable.addRow(dto.getPeriodId(), itemWidgetsMap.values().toArray(new Widget[]{}));
                }
            }
        } else {
            multiLeaveTable.addRow(addNewRow(leavePeriod));
        }
    }

    private TextBox getTextBox() {
        TextBox durationBox = new TextBox();
        durationBox.setAlignment(ValueBoxBase.TextAlignment.CENTER);
        Validation.addPositiveNumericKeyboardListener(durationBox);
        durationBox.addKeyUpHandler(keyUpEvent -> {
            durationBox.removeStyleName(Constants.ERROR_FORM_STYLE);
            totalTable.getWidget(0, 2).removeStyleName(Constants.ERROR_FORM_STYLE);
            enableButton(true);
            double days = 0;
            for (int i = 0; i < multiLeaveTable.getRowNumber(); i++) {
                DynamicTableItem item = multiLeaveTable.getItem(i);
                TextBox typeHere = (TextBox) item.getColumnById("TYPE_HERE");
                String takenDays = typeHere.getText();
                days += "".equals(takenDays) || "0.00".equals(takenDays) ? 0 : Double.parseDouble(takenDays);
            }
            totalDays.setText(String.valueOf(days));
            totalTable.setWidget(0, 2, totalDays);
            DynamicTableItem item = periodListTable.getItem(clickedPeriodRowId);
            TextBox typeHere = (TextBox) item.getColumnById("TYPE_HERE");
            typeHere.setText(String.valueOf(days));
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVE_DAYS_INSERTED, null, LaborPeriodWidget.this);
        });
        return durationBox;
    }

    private void saveValues(Integer objectID, String leavePeriod) {
        enableButton(false);
        if (!validateMultiLeaveTable()) {
            return;
        }
        setValuesToDto(objectID, leavePeriod);
        multiLeaveModal.hide();
    }

    private void setValuesToDto(Integer objectID, String leavePeriod) {
        ArrayList<MultiLeaveDTO> list = new ArrayList<>();
        if (multiLeaveTable.getRowCount() > 0) {
            for (int i = 0; i < multiLeaveTable.getRowNumber(); i++) {
                MultiLeaveDTO dto = new MultiLeaveDTO();

                DynamicTableItem item = multiLeaveTable.getItem(i);

                DataListBox type = (DataListBox) item.getColumnById("TYPE");
                String sickRequestType = type.getSelectedItem() != null ? type.getSelectedItem().getDescription() : null;

                TextBox inbox = (TextBox) item.getColumnById("TYPE_HERE");
                int sickRequestDuration = !inbox.getText().equals("") ? Integer.parseInt(inbox.getText()) : 0;

                Label leftDays = (Label) item.getColumnById("LEFT_DAYS");
                int sickRequestLeftDays = !leftDays.getText().equals("") ? Integer.parseInt(leftDays.getText()) : 0;

                Label minValue = (Label) item.getColumnById("MIN_VALUE");
                int minLeaveDays = !minValue.getText().equals("") ? Integer.parseInt(minValue.getText()) : 0;

                if (sickRequestDuration != 0) {
                    dto.setPeriodId(objectID);
                    dto.setSickRequestType(sickRequestType);
                    dto.setSickRequestDuration(sickRequestDuration);
                    dto.setSickRequestLeftDays(sickRequestLeftDays);
                    dto.setMinLeaveDays(minLeaveDays);
                    dto.setLaborPeriod(leavePeriod);
                }
                list.add(dto);
            }
            setDatesToBalance(list, leavePeriod);
        }
    }

    private void setDatesToBalance(ArrayList<MultiLeaveDTO> list, String leavePeriod) {
        if (list != null) {
            multiLeaveValues.put(leavePeriod, list);
            DynamicTableItem tableItem = periodListTable.getItem(clickedPeriodRowId);
            TextBox textBox = (TextBox) tableItem.getColumnById("TYPE_HERE");
            String totalValue = ((Label) totalTable.getWidget(0, 2)).getText();
            textBox.setText(!totalValue.isEmpty() ? totalValue : "");

            int totalDays = 0;
            for (MultiLeaveDTO leaveDTO : list) {
                totalDays += leaveDTO.getSickRequestDuration();
                textBox.setText(String.valueOf(totalDays));
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVE_DAYS_INSERTED, null, LaborPeriodWidget.this);
            }
        }
    }

    public void enableButton(boolean enable) {
        saveToMap.setEnabled(enable);
    }

    private boolean validateMultiLeaveTable() {
        int invalidInput = 0;
        double totalDays = !((Label) totalTable.getWidget(0, 2)).getText().equals("") ? Utils.universalParse(doubleFormat, ((Label) totalTable.getWidget(0, 2)).getText()) : (double) 0;
        String type = null;
        for (int i = 0; i < multiLeaveTable.getRowNumber(); i++) {
            DynamicTableItem item = multiLeaveTable.getItem(i);
            DataListBox typeListBox = (DataListBox) item.getColumnById("TYPE");
            String leaveType = typeListBox.getSelectedItem() != null ? typeListBox.getSelectedItem().getDescription() : null;

            TextBox typeHere = (TextBox) item.getColumnById("TYPE_HERE");
            Double input = !typeHere.getText().equals("") ? Utils.universalParse(doubleFormat, typeHere.getText()) : (double) 0;

            Label minValueBox = (Label) item.getColumnById("MIN_VALUE");
            Double minValue = !minValueBox.getText().equals("") ? Utils.universalParse(doubleFormat, minValueBox.getText()) : (double) 0;

            Label leftDays = (Label) item.getColumnById("LEFT_DAYS");

            if (leaveType == null) {
                typeListBox.addStyleName(Constants.ERROR_FORM_STYLE);
                Info.show(wfmStrings.pleaseSelect(), Info.Type.WARNING);
                invalidInput++;
            }

            if (type == null) {
                type = leaveType;
            } else {
                if (type.equals(leaveType)) {
                    typeListBox.addStyleName(Constants.ERROR_FORM_STYLE);
                    Info.show(wfmStrings.youCanNotAdd(), Info.Type.WARNING);
                    invalidInput++;
                }
            }

            assert leaveType != null;
            switch (leaveType) {
                case Constants.DAY:
                    if (input > 0 && input <= leftLeaveDays) {
                        if (leftLeaveDays >= minValue) {
                            if (input >= minValue) {
                                this.leftLeaveDays = leftLeaveDays - input;
                                leftDays.setText(String.valueOf(leftLeaveDays));
                            } else {
                                if (leftLeaveDays - input >= minValue) {
                                    this.leftLeaveDays = leftLeaveDays - input;
                                    leftDays.setText(String.valueOf(leftLeaveDays));
                                } else {
                                    typeHere.addStyleName(Constants.ERROR_FORM_STYLE);
                                    Info.show(wfmMessages.youMustEnterMoreMinimumLeaveDays(minValue.toString()), Info.Type.WARNING);
                                    invalidInput++;
                                }
                            }
                        } else {
                            this.leftLeaveDays = leftLeaveDays - input;
                            leftDays.setText(String.valueOf(leftLeaveDays));
                        }
                    } else {
                        typeHere.addStyleName(Constants.ERROR_FORM_STYLE);
                        Info.show(wfmMessages.youMustEnterMoreMinimumLeaveDays(minValue.toString()), Info.Type.WARNING);
                        invalidInput++;
                    }
                    break;
                case "MONEY":
                    if (input > 0 && input <= leftLeaveDays) {
                        if (leftLeaveDays >= minValue) {
                            if (input <= leftLeaveDays - minValue) {
                                this.leftLeaveDays = leftLeaveDays - input;
                                leftDays.setText(String.valueOf(leftLeaveDays));
                            } else {
                                typeHere.addStyleName(Constants.ERROR_FORM_STYLE);
                                Info.show(wfmMessages.youMustEnterMoreMinimumLeaveDays(minValue.toString()), Info.Type.WARNING);
                                invalidInput++;
                            }
                        } else {
                            this.leftLeaveDays = leftLeaveDays - input;
                            leftDays.setText(String.valueOf(leftLeaveDays));
                        }
                    } else {
                        typeHere.addStyleName(Constants.ERROR_FORM_STYLE);
                        Info.show(wfmMessages.youMustEnterMoreMinimumLeaveDays(minValue.toString()), Info.Type.WARNING);
                        invalidInput++;
                    }
                    break;
            }
        }

        if (totalDays > leftLeaveDaysBeforeChange) {
            totalTable.getWidget(0, 2).addStyleName(Constants.ERROR_FORM_STYLE);
            Info.show(wfmStrings.youCanNotAddMoreThan(), Info.Type.WARNING);
            invalidInput++;
        }

        if (invalidInput > 0) {
            this.leftLeaveDays = this.leftLeaveDaysBeforeChange;
        }

        return invalidInput <= 0;
    }

    private DataListBox getTypeListBox() {
        DataListBox leaveTypes = new DataListBox();
        leaveTypes.setWithoutNullLabel(false);
        leaveTypes.addListItem(new SelectItem(0, wfmStrings.day(), Constants.DAY));
        leaveTypes.addListItem(new SelectItem(1, wfmStrings.money(), Constants.MONEY));
        leaveTypes.addFocusHandler(focusEvent -> {
            leaveTypes.removeStyleName(Constants.ERROR_FORM_STYLE);
            enableButton(true);
        });
        return leaveTypes;
    }

    private void onChangeEvent(FlexTable table) {
        table.setVisible(table.getRowCount() > 0);
    }

    public HashMap<String, ArrayList<MultiLeaveDTO>> getMultiLeaveValues() {
        return multiLeaveValues;
    }

    public void setMultiLeaveItems(ArrayList<MultiLeaveDTO> multiLeaveItems) {
        ArrayList<MultiLeaveDTO> list = null;
        String period = null;
        for (MultiLeaveDTO dto : multiLeaveItems) {
            if (period == null) {
                period = dto.getLaborPeriod();
                list = new ArrayList<>();
                list.add(dto);
                multiLeaveValues.put(period, list);
            } else if (period.equals(dto.getLaborPeriod())) {
                list.add(dto);
                multiLeaveValues.put(period, list);
            } else {
                period = dto.getLaborPeriod();
                list = new ArrayList<>();
                list.add(dto);
                multiLeaveValues.put(period, list);
            }
        }
    }

    public void clearValues() {
        multiLeaveValues.clear();
        if (periodListTable != null && periodListTable.getRowNumber() > 0) {
            periodListTable.clear();
        }
    }

    public void setLabourPeriods(ArrayList<LaborPeriodRequest> labourPeriods) {
        this.periodList = labourPeriods;
        createPeriodTable();
    }

    class CustomModal extends KpiSideNavBox {

        CustomModal(boolean autoHide) {
            super(autoHide);
            sinkEvents(Event.ONPASTE);
        }

        @Override
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);
            if (DOM.eventGetType(event) == Event.ONPASTE) {
                event.preventDefault();
            }
        }
    }
}
