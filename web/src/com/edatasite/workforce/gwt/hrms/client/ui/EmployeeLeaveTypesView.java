package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.AnnualLeaveItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.leaveRequest.LaborPeriodRequest;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.*;

/**
 * User: Ilhom Lutfullaev
 * Date: 25.12.2009
 * Time: 16:32:13
 */

public class EmployeeLeaveTypesView extends CustomForm implements Constants, Colapse {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final NumberFormat doubleFormat = NumberFormat.getFormat(",##0.#");
    private final NumberFormat numberFormat = NumberFormat.getFormat("#0.0");
    private DynamicTable tblAnualItem;
    private DynamicTable periodTable;
    private HTML employeeName;
    private final Integer int_objectID;
    private ProfileItem profileItem;
    private final Integer selectedYear;
    private boolean currentYear = true;

    public EmployeeLeaveTypesView(Integer int_objectID, Integer selectedYear) {
        super("editpositions", hrmsStrings.editPosition());
        this.selectedYear = selectedYear;
        this.int_objectID = int_objectID;
        sinkEvents(Event.ONPASTE);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if (DOM.eventGetType(event) == Event.ONPASTE) {
            event.preventDefault();
        }
    }

    @Override
    public String getIconStyle() {
        return "icon-edit";
    }

    @Override
    protected void addButtons() {
        if (currentYear) {
            addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, null, ("employee_leave_allowence_save_and_close_button"), event -> {
                save();
            });
        }
    }

    @Override
    protected void getDataToFillFields() {
        if (int_objectID != null) {
            LoadingPanel.loading(true);
            HrmsService.App.get().getEmployeeLeaveTypes(int_objectID, selectedYear, new AbstractAsyncCallback<ProfileItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(ProfileItem result) {
                    profileItem = result;
                    if (Utils.hasGenericAccess(GenericSettingsEnum.SICK_LEAVE_SETTINGS_CALCULATION)) {

                        if ((selectedYear == null || result.getCurrentYear().getNonConvertedDate().getYear() + 1900 != selectedYear)) {
                            currentYear = false;
                        }
                    }
                    LoadingPanel.loading(false);
                    fillTable(result);
                }
            });

        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EMPLOYEE_LEAVE_TYPES_FORM;
    }

    @Override
    protected String getFormType() {
        return int_objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    private void initialize() {

        //position job family
        employeeName = new HTML();
//        tblAnualItem.removeAllRows();
        tblAnualItem = new DynamicTable(getColumns(), "productstable-header", "", false);
        tblAnualItem.setBorderWidth(0);
        tblAnualItem.setStyleName("bulletin-GWTCode addRemoveData");
//        getColumns();
        //position details -> 1
        addTitleField(POSITIONS.LEAVE_ALLOUNCE_INFORMATION, hrmsStrings.leaveInformation());
        addField(EMPLOYEE_CODE, employeeName, getTitle(wfmStrings.employee(), false));
        addField(POSITIONS.LEAVE_ALLOUNCE_PANEL, tblAnualItem, getTitle(hrmsStrings.manageLeaveReasons(), false));
        show();
    }

    private DynamicTableColumn[] getColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[6];
        columns[0] = new DynamicTableColumn(wfmStrings.reasons(), "reasons", 100);
        columns[1] = new DynamicTableColumn("", "reasonsid", 1);
        columns[2] = new DynamicTableColumn("", "reasonsCode", 1);
        columns[3] = new DynamicTableColumn("Allowance Days", "allowanceDays", 25);
        columns[4] = new DynamicTableColumn("Last Year Remaining Days", "lastyearAllowanceDays", 15);
        columns[5] = new DynamicTableColumn("Add Last Year Remaining", "addLastYear", 15);
        columns[5].setStyle(CENTER_ALIGN_CELL);
        return columns;
    }

    private void fillTable(ProfileItem item) {
        employeeName.setText(item.getName());
        int rowID = 1;
        for (Integer key : item.getLeaveitems().keySet()) {
            tblAnualItem.addRow(getWidgets(key, item.getLeaveitems(), rowID));
            if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && "LR_TYPE_ANNUAL_LEAVE".equals(item.getLeaveitems().get(key).getReasonCode())) {
                tblAnualItem.getFlexCellFormatter().setColSpan(rowID, 3, 4);
            }
            rowID++;
        }
    }

    private Widget[] getWidgets(final Integer key, final HashMap<Integer, AnnualLeaveItem> leaveAllounces, int rowID) {
        int index = 0;
        final Widget[] widgets = new Widget[tblAnualItem.getCellCount(0)];

        Label reason = new Label(leaveAllounces.get(key).getReasonName());
        reason.getElement().getStyle().setTextAlign(Style.TextAlign.LEFT);
        widgets[index++] = reason;

        Hidden reasonId = new Hidden();
        reasonId.setValue(String.valueOf(key));
        widgets[index++] = reasonId;

        Hidden reasonCode = new Hidden();
        reasonCode.setValue(leaveAllounces.get(key).getReasonCode());
        widgets[index++] = reasonCode;

        Span working_periods = new Span();
        if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && "LR_TYPE_ANNUAL_LEAVE".equals(leaveAllounces.get(key).getReasonCode())) {
            widgets[index++] = createPeriodTable(leaveAllounces.get(key));
            return widgets;
        }

        TextBox allowanceDaysTextBox = new TextBox();
        allowanceDaysTextBox.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
        double annualAllowanceDays = leaveAllounces.get(key).getAnnualallowancedays();
        allowanceDaysTextBox.setValue(numberFormat.format(annualAllowanceDays));
        if (currentYear) {
            allowanceDaysTextBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            Validation.addNumericKeyboardListener(allowanceDaysTextBox, 2);
            allowanceDaysTextBox.addFocusHandler(event -> {
                if (("0.0".equals(allowanceDaysTextBox.getValue()) || "0".equals(allowanceDaysTextBox.getValue()))) {
                    allowanceDaysTextBox.setValue("");
                }
            });
            allowanceDaysTextBox.addBlurHandler(event -> {
                if ("".equals(allowanceDaysTextBox.getValue().trim())) {
                    allowanceDaysTextBox.setValue("0.0");
                }
            });
        } else {
            allowanceDaysTextBox.setEnabled(false);
        }
        widgets[index++] = allowanceDaysTextBox;

        Double lastYearDays = Optional.ofNullable(leaveAllounces.get(key).getLastAllowanceDays()).orElse(0d);
        Label lastYearAllowanceHoursLabel = new Label(String.valueOf(lastYearDays));
        lastYearAllowanceHoursLabel.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        widgets[index++] = lastYearAllowanceHoursLabel;

        KpiCheckBox addLastYearCheckBox = new KpiCheckBox();
        addLastYearCheckBox.setValue(leaveAllounces.get(key).getAddPrevious());
        if (!currentYear) {
            addLastYearCheckBox.setEnabled(false);
        }
        addLastYearCheckBox.addValueChangeHandler(valueChangeEvent -> {
            if (addLastYearCheckBox.getValue()) {
                allowanceDaysTextBox.setValue(numberFormat.format(numberFormat.parse(allowanceDaysTextBox.getValue()) + numberFormat.parse(lastYearAllowanceHoursLabel.getText())));
            } else {
                allowanceDaysTextBox.setValue(numberFormat.format(numberFormat.parse(allowanceDaysTextBox.getValue()) - numberFormat.parse(lastYearAllowanceHoursLabel.getText())));
            }
        });
        widgets[index++] = addLastYearCheckBox;
        return widgets;
    }

    private DynamicTable createPeriodTable(AnnualLeaveItem item) {
        periodTable = new DynamicTable(getTableColumns(), false);
        periodTable.setStyleName("dataTable");
        periodTable.setHeaderTextToCenter(1, Style.TextAlign.CENTER);
        initPeriodTable(item);
        return periodTable;
    }

    private DynamicTableColumn[] getTableColumns() {
        LinkedList<DynamicTableColumn> columnsList = new LinkedList<>();

        DynamicTableColumn employmentPeriod = new DynamicTableColumn(wfmStrings.employmentPeriod(), "EMPLOYMENT_PERIOD", 30);
        employmentPeriod.setSaveWhiteSpace(true);

        DynamicTableColumn leaveAllowance = new DynamicTableColumn(wfmStrings.leaveAllowance() + "\nA", "LEAVE_ALLOWANCE", 30);
        leaveAllowance.setSaveWhiteSpace(true);

        DynamicTableColumn taken = new DynamicTableColumn(wfmStrings.taken() + "\nB", "TAKEN", 30);
        taken.setSaveWhiteSpace(true);

        DynamicTableColumn historyAndNotes = new DynamicTableColumn(wfmStrings.historyAndNotes(), "HISTORY", 30);
        historyAndNotes.setSaveWhiteSpace(true);

        DynamicTableColumn currentBalance = new DynamicTableColumn(hrmsStrings.currentBalance() + "\n(D = A - B - C)", "BALANS", 30);
        currentBalance.setSaveWhiteSpace(true);

        DynamicTableColumn adjust = new DynamicTableColumn(wfmStrings.adjusted() + "\nC", "ADJUST", 30);
        adjust.setSaveWhiteSpace(true);

        DynamicTableColumn periodID = new DynamicTableColumn(wfmStrings.caseID(), "PERIOD_ID", 30);
        periodID.setVisible(false);

        columnsList.add(employmentPeriod);
        columnsList.add(leaveAllowance);
        columnsList.add(taken);
        columnsList.add(currentBalance);
        columnsList.add(adjust);
        columnsList.add(periodID);
        columnsList.add(historyAndNotes);

        return columnsList.toArray(new DynamicTableColumn[columnsList.size()]);
    }

    private void initPeriodTable(AnnualLeaveItem item) {
        if (item.getRequestList() != null && item.getRequestList().size() > 0) {
            int i = 0;
            for (LaborPeriodRequest period : item.getRequestList()) {
                final Map<String, Widget> itemWidgetsMap = new LinkedHashMap<>();

                MaterialLink multiLeaveModal = new MaterialLink();
                multiLeaveModal.setText(period.getLaborPeriod());

                Span periodID = new Span();
                periodID.setText(period.getPeriodID().toString());

                TextBox leaveAllowanceBox = new TextBox();
                leaveAllowanceBox.getElement().setPropertyString("style", "margin: 2px auto");
                leaveAllowanceBox.setAlignment(ValueBoxBase.TextAlignment.CENTER);
                leaveAllowanceBox.setText(period.getAllowance().toString());
                Validation.addPositiveNumericKeyboardListener(leaveAllowanceBox);

                Label takenDaysBox = new Label();
                takenDaysBox.getElement().setPropertyString("style", "margin: 2px auto");
                takenDaysBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
                takenDaysBox.setText(String.valueOf(period.getApprovedTakenDays()));

                TextBox adjustBox = new TextBox();
                adjustBox.getElement().setPropertyString("style", "margin: 2px auto");
                adjustBox.setAlignment(ValueBoxBase.TextAlignment.CENTER);
                adjustBox.setText(String.valueOf((period.getOutOfSystemDays())));
                Validation.addNumericKeyboardListener(adjustBox);
                int finalI = i;
                adjustBox.addKeyUpHandler(handler -> {
                    if (validate(finalI)) {
                        adjustBox.removeStyleName(Constants.ERROR_FORM_STYLE);
                            }
                        }
                );
                int row = i;
                leaveAllowanceBox.addKeyUpHandler(handler -> {
                            if (validate(row)) {
                                leaveAllowanceBox.removeStyleName(Constants.ERROR_FORM_STYLE);
                            }
                        }
                );

                IconFigure historyNotes = initHistory(period.getPeriodID());

                Label balance = new Label();
                balance.getElement().setPropertyString("style", "margin: 2px auto");
                balance.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
                balance.setText(String.valueOf(period.getAllowance() - period.getApprovedTakenDays() - period.getOutOfSystemDays()));

                itemWidgetsMap.put("EMPLOYMENT_PERIOD", multiLeaveModal);
                itemWidgetsMap.put("LEAVE_ALLOWANCE", leaveAllowanceBox);
                itemWidgetsMap.put("TAKEN", takenDaysBox);
                itemWidgetsMap.put("BALANS", balance);
                itemWidgetsMap.put("ADJUST", adjustBox);
                itemWidgetsMap.put("PERIOD_ID", periodID);
                itemWidgetsMap.put("HISTORY", historyNotes);
                i++;
                periodTable.addRow(period.getPeriodID(), itemWidgetsMap.values().toArray(new Widget[]{}));
            }
        }
    }

    private void save() {
        enableButton(false);
        if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && !validatePeriodTable()) {
            enableButton(true);
            return;
        }
        setValues();

        LoadingPanel.loading(true);

        HrmsService.App.get().saveEmployeeLeaveTypes(profileItem, selectedYear, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.errorOccurredSavingChanges(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer employeeID) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show("Employee Leave types saved succesfully", Info.Type.INFO);
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OVERTIME_CHANGE, employeeID, EmployeeLeaveTypesView.this);
            }
        });
    }

    private void setValues() {
        HashMap<Integer, AnnualLeaveItem> leaveAllounces = new HashMap<>();
        for (int i = 1; i < tblAnualItem.getRowCount(); i++) {
            AnnualLeaveItem leaveItem = new AnnualLeaveItem();

            Hidden reasonID = (Hidden) tblAnualItem.getWidget(i, 1);
            leaveItem.setReasonId(Integer.valueOf(reasonID.getValue()));

            String reasonCode = ((Hidden) tblAnualItem.getWidget(i, 2)).getValue();

            if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && "LR_TYPE_ANNUAL_LEAVE".equals(reasonCode)) {
                leaveItem.setRequestList(setPeriodValues());
            } else {
                String allowanceDays = ((TextBox) tblAnualItem.getWidget(i, 3)).getText();
                leaveItem.setAnnualallowancedays("0,0".equals(allowanceDays) || "0".equals(allowanceDays) || "".equals(allowanceDays) ? 0d : Utils.universalParse(doubleFormat, allowanceDays));

                KpiCheckBox addLastYear = (KpiCheckBox) tblAnualItem.getWidget(i, 5);
                leaveItem.setAddPrevious(addLastYear.getValue());
            }
            leaveAllounces.put(Integer.valueOf(reasonID.getValue()), leaveItem);
        }
        profileItem.setLeaveitems(leaveAllounces);
    }

    private ArrayList<LaborPeriodRequest> setPeriodValues() {
        ArrayList<LaborPeriodRequest> list = new ArrayList<>();
        for (int i = 0; i < periodTable.getRowNumber(); i++) {
            LaborPeriodRequest request = new LaborPeriodRequest();

            DynamicTableItem item = periodTable.getItem(i);
            Span periodId = (Span) item.getColumnById("PERIOD_ID");
            TextBox leaveAllowence = (TextBox) item.getColumnById("LEAVE_ALLOWANCE");
            TextBox adjustBox = (TextBox) item.getColumnById("ADJUST");

            Integer periodID = Integer.valueOf(periodId.getText());
            Double annualAllowance = "".equals(leaveAllowence.getText()) ? null : Utils.universalParse(doubleFormat, leaveAllowence.getText());
            Double adjust = "".equals(adjustBox.getText()) ? null : Utils.universalParse(doubleFormat, adjustBox.getText());

            request.setPeriodID(periodID);
            request.setAllowance(annualAllowance);
            request.setOutOfSystemDays(adjust);
            list.add(request);
        }
        return list;
    }

    private boolean validatePeriodTable() {
        int invalidInput = 0;
        for (int i = 0; i < periodTable.getRowNumber(); i++) {
            if (!validate(i)) {
                invalidInput++;
            }
        }
        return invalidInput <= 0;
    }

    private boolean validate(Integer rowID) {
        int invalidInput = 0;

        DynamicTableItem item = periodTable.getItem(rowID);
        TextBox leaveAllowence = (TextBox) item.getColumnById("LEAVE_ALLOWANCE");
        Label takenBox = (Label) item.getColumnById("TAKEN");
        TextBox adjustBox = (TextBox) item.getColumnById("ADJUST");

        double annualAllowance = !"".equals(leaveAllowence.getText()) ? Utils.universalParse(doubleFormat, leaveAllowence.getText()) : 0;
        double taken = !"".equals(takenBox.getText()) ? Utils.universalParse(doubleFormat, takenBox.getText()) : 0;
        double adjust = !"".equals(adjustBox.getText()) ? Utils.universalParse(doubleFormat, adjustBox.getText()) : 0;

        if (annualAllowance < taken + adjust) {
            leaveAllowence.addStyleName(Constants.ERROR_FORM_STYLE);
            Info.show(hrmsStrings.youCantSetLess(), Info.Type.WARNING);
            invalidInput++;
        }

        if (adjust > 0) {
            if (taken + adjust > annualAllowance) {
                adjustBox.addStyleName(Constants.ERROR_FORM_STYLE);
                Info.show(hrmsStrings.youCantAdjustForThisPeriod(), Info.Type.WARNING);
                invalidInput++;
            }
        }
        return invalidInput <= 0;
    }

    private NoteHistoryWidget drawHistoryNotes(Integer periodID) {
        NoteHistoryWidget noteHistoryWidget = new NoteHistoryWidget(callback -> {
            if (periodID == null) {
                return;
            }
            HrmsService.App.get().loadLaborPeriodHistory(periodID, callback);
        });
        if (periodID != null) {
            noteHistoryWidget.setSaveIntoDatabase(historyItem -> {
                if (historyItem != null) {
                    LoadingPanel.loading(true);
                    HrmsService.App.get().createlaborPeriodHistory(periodID, historyItem, new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Integer hisItemId) {
                            historyItem.setObjectID(hisItemId);
                            LoadingPanel.loading(false);
                        }
                    });
                }
            });

            noteHistoryWidget.setRemoveFromDatabase((hisItem) -> {
                if (hisItem != null && hisItem.getObjectID() != null) {
                    LoadingPanel.loading(true);
                    HrmsService.App.get().deleteLaborPeriodHistory(hisItem.getObjectID(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Void aVoid) {
                            LoadingPanel.loading(false);
                        }
                    });
                }
            });
        }
        return noteHistoryWidget;
    }

    private IconFigure initHistory(Integer periodID) {
        SvgIcon docSvg = new SvgIcon(SvgEnum.docHistory);
        IconFigure historyLink = new IconFigure(docSvg);
        historyLink.setTextAlign(TextAlign.CENTER);
        historyLink.setTooltip(wfmStrings.historyAndNotes());
        historyLink.setTooltipPosition(Position.TOP);
        historyLink.addClickHandler(event -> {
            getHistoryPopup(historyLink, periodID);
        });
        return historyLink;
    }

    private void getHistoryPopup(IconFigure historyLink, Integer periodID) {
        final NoteHistoryWidget historyPopup = drawHistoryNotes(periodID);
        PopupPanel historyPanel = new PopupPanel(true);
        historyPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            int historyLinkAbsoluteTop = historyLink.getAbsoluteTop();
            int historyLinkAbsoluteLeft = historyLink.getAbsoluteLeft();

            public void setPosition(int offsetWidth, int offsetHeight) {
                if (offsetHeight + historyLink.getOffsetHeight() < Window.getClientHeight() - historyLinkAbsoluteTop) {
                    if (offsetWidth + historyLink.getOffsetWidth() < Window.getClientWidth() - historyLinkAbsoluteLeft) {
                        historyPanel.setPopupPosition(historyLink.getAbsoluteLeft() + 85, historyLinkAbsoluteTop - 295);
                    } else {
                        historyPanel.setPopupPosition(historyLink.getAbsoluteLeft() - 338, historyLinkAbsoluteTop - 295);
                    }
                } else {
                    if (offsetWidth + historyLink.getOffsetWidth() < Window.getClientWidth() - historyLinkAbsoluteLeft) {
                        historyPanel.setPopupPosition(historyLink.getAbsoluteLeft(), historyLinkAbsoluteTop - offsetHeight);
                    } else {
                        historyPanel.setPopupPosition(historyLink.getAbsoluteLeft() - 338, historyLinkAbsoluteTop - offsetHeight);
                    }
                }
            }
        });
        historyPanel.add(historyPopup);
        historyPanel.getElement().getStyle().setPadding(0, Style.Unit.PX);
        historyPanel.show();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    private class IconFigure extends Div {
        private Span badge;

        public IconFigure(SvgIcon icon) {
            super("user-menu-item");
            MaterialLink link = new MaterialLink();
            link.addStyleName("button-collapse");
            link.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            add(link);
            Span umicon = new Span();
            umicon.addStyleName("user-menu-item__icon");
            umicon.add(icon);
            badge = createBadge();
            umicon.add(badge);
            link.add(umicon);
        }

        public Span createBadge() {
            Span badge = new Span();
            badge.addStyleName("badge");
            badge.setVisible(false);
            return badge;
        }

        public void setBadge(Integer count) {
            if (count == null || count == 0) {
                badge.setText("");
                badge.setVisible(false);
                return;
            }
            badge.setText("" + count);
            badge.setVisible(true);
        }

        public void setBadge(Long count) {
            if (count == null || count == 0L) {
                badge.setText("");
                badge.setVisible(false);
                return;
            }
            String prefix = "";
            String suffix = "";
            if (count >= 1000L) {
                count /= 1000L;
                prefix = "+";
                suffix = "k";
            } else if (count > 99L) {
                count = 99L;
                prefix = "+";
            }
            badge.setText(prefix + "" + count);
            badge.setVisible(true);
        }

    }
}