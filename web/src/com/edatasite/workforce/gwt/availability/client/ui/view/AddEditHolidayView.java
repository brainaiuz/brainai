package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityServiceAsync;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiAppendedRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.ui.tabpanels.HolidayHistoryTab;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.Map;

public class AddEditHolidayView extends CustomForm implements Constants, SchedulerConstant, Colapse {

    private static final AvailabilityServiceAsync availabilityService = AvailabilityService.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private boolean saveAndClose = false;
    private KpiEditor area;
    private DatePicker endDate;
    private Integer holidayID;

    private TextBox name;
    private KpiAppendedRadioButton dayOff, takenFromAllowance;
    private final MaterialPanel takenFromPanel = new MaterialPanel();
    private MultiTableNewUI locationTable;
    private SelectItem[] locationItems;
    private WfmButton2 saveButton;
    private DatePicker startDate;

    private KpiCheckBox recurringChb;
    private RadioButton yearlyRB;
    private RadioButton monthlyRB;
    private FlexPanel recurringPanel;
    private HolidayHistoryTab historyTab;

    public AddEditHolidayView() {
        super("add", hrmsStrings.addPublicHoliday());
    }

    public AddEditHolidayView(Integer id) {
        super("edit", hrmsStrings.editHoliday());
        this.holidayID = id;
    }

    public String getIconStyle() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.HOLIDAY_FORM;
    }

    @Override
    protected String getFormType() {
        return holidayID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private WidgetsMap getLocationsMap(Integer locationID) {
        WidgetsMap widgetsMap = new WidgetsMap();
        DataListBox locationsBox = new DataListBox();
        locationsBox.addStyleName(DEFAULT_WIDTH);
        widgetsMap.addWidgets(locationsBox);
        if (locationItems != null) {
            locationsBox.setItems(locationItems);
        }
        if (locationID != null) {
            locationsBox.setSelected(locationID);
        }
        widgetsMap.addToCenter(MultiTable.LIST_BOX, locationsBox);


        return widgetsMap;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        name = new TextBox();
        startDate = new DatePicker();
        endDate = new DatePicker();
        addField("DETAILS", null, hrmsStrings.holidayDetails());
        addField(CustomFormConstants.HOLIDAY.NAME, name, getTitle(wfmStrings.name(), true));

        area = new KpiEditor(true);
        area.setWidth("100%");
        addField(CustomFormConstants.HOLIDAY.DESCRIPTION, area, getTitle(wfmStrings.description(), false));

        recurringPanel = new FlexPanel();
        recurringPanel.addStyleName(DEFAULT_WIDTH);
        recurringPanel.setStyleName("row");
        FlexPanel childPanel = new FlexPanel();
        childPanel.setStyleName("field");
        recurringPanel.add(childPanel);
        recurringChb = new KpiCheckBox();
        recurringChb.addValueChangeHandler(booleanValueChangeEvent -> {
            recurringPanel.setVisible(recurringChb.getValue());
        });

        yearlyRB = new KpiRadioButton("recType", wfmStrings.yearly());
        yearlyRB.setValue(true);
        monthlyRB = new KpiRadioButton("recType", wfmStrings.monthly());

        FlexTable recTypeTable = new FlexTable();
        recTypeTable.setWidget(0, 0, monthlyRB);
        recTypeTable.setWidget(0, 1, yearlyRB);
        childPanel.add(recTypeTable);
        recurringPanel.setVisible(false);

        dayOff = new KpiAppendedRadioButton();
        addField(CustomFormConstants.HOLIDAY.DAY_OFF, dayOff, wfmStrings.dayOff());
        dayOff.setActive();
        dayOff.addSelectionHandler(event -> {
            takenFromAllowance.setActive(false);
            takenFromPanel.setVisible(dayOff.isActive());
        });

        takenFromAllowance = new KpiAppendedRadioButton();
        takenFromPanel.add(new FormGroup(hrmsStrings.takenFromAllowance(), takenFromAllowance));
        addField(CustomFormConstants.HOLIDAY.ANNUAL_ALLOWANCE, takenFromPanel, null);

        startDate.addStyleName(DEFAULT_WIDTH);
        addField(CustomFormConstants.HOLIDAY.START_DATE, startDate, getTitle(wfmStrings.startDate(), true));
        endDate.addStyleName(DEFAULT_WIDTH);
        addField(CustomFormConstants.HOLIDAY.END_DATE, endDate, getTitle(wfmStrings.endDate(), true));
        addField(CustomFormConstants.HOLIDAY.RECCURING, recurringChb, wfmStrings.recurring());
        addField(CustomFormConstants.HOLIDAY.MONTHLY_YEARLY, recurringPanel);

        locationTable = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getLocationsMap(null);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        }, false);
        refreshLocationDropDowns(locationTable);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCATION_ADD, AddEditHolidayView.this, (sender, args) -> refreshLocationDropDowns(locationTable));

        addField(CustomFormConstants.HOLIDAY.LOCATION, locationTable, Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()));

        if (holidayID != null) {
            historyTab = new HolidayHistoryTab(holidayID);
            addField(CustomFormConstants.HOLIDAY.HOLIDAY_HISTORY_LOG, historyTab, wfmStrings.historyLog(), true);
        }
        show();
        return null;
    }

    @Override
    protected void addButtons() {
        saveButton = addButton(holidayID != null ? wfmStrings.update() : wfmStrings.save());
        saveButton.addClickHandler(sender -> {
            saveAndClose = true;
            save();
        });
    }

    @Override
    protected void getDataToFillFields() {
        if (holidayID != null) {
            availabilityService.getHoliday(holidayID, new AbstractAsyncCallback<HolidayItem>() {
                public void success(final HolidayItem holiday) {
                    DeferredCommand.addCommand(() -> {
                        name.setText(holiday.getName());
                        startDate.setDate(holiday.getFrom().getNonConvertedDate());
                        takenFromAllowance.setActive(holiday.isTakenFromAnnual());
                        if (holiday.isAllDay()) {
                            endDate.setDate(holiday.getFrom().getNonConvertedDate());
                        } else {
                            endDate.setDate(holiday.getTo().getNonConvertedDate());
                        }
                        dayOff.setActive(holiday.isDayOff());
                        if (holiday.isRepeat()) {
                            if (holiday.getRepeatId() != null) {
                                if (holiday.getRepeatId() == RECURRENCE_TYPE_MONTHLY) {
                                    monthlyRB.setValue(true);
                                } else if (holiday.getRepeatId() == RECURRENCE_TYPE_YEARLY) {
                                    yearlyRB.setValue(true);
                                }
                            }
                            recurringChb.setValue(true, true);
                        }
                        area.setData(holiday.getDescription());
                        if (holiday.getLocationIds() != null && holiday.getLocationIds().size() > 0) {
                            locationTable.removeAllRows();
                            for (Integer locationID : holiday.getLocationIds()) {
                                locationTable.addWidgets(getLocationsMap(locationID));
                            }
                        }
                    });
                }
            });
        } else {
            dayOff.setActive();
            takenFromAllowance.setActive(false);
        }
    }

    private void refreshLocationDropDowns(final MultiTableNewUI locationTable) {
        ReportService.App.get().getLocationList(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] selectItems) {
                if (selectItems != null) {
                    locationItems = selectItems;
                    for (Map<String, Widget> row : locationTable.getWidgets()) {
                        if (row != null) {
                            DataListBox db = (DataListBox) row.get(MultiTable.LIST_BOX);
                            if (db != null) {
                                db.setItems(selectItems);
                            }
                        }
                    }
                }
            }
        });
    }

    private void onShellOk() {
        if (saveAndClose) {
            closeTab();
        }
    }

    protected void save() {
        if (!validate()) {
            return;
        }
        HolidayItem newHoliday = new HolidayItem();
        newHoliday.setObjectID(holidayID);
        newHoliday.setName(name.getText());
        newHoliday.setDescription(area.getData());
        newHoliday.setDayOff(dayOff.isActive());
        newHoliday.setFrom(new DateNonConvertable(DateUtil.resetTime(startDate.getDate())));
        newHoliday.setTo(new DateNonConvertable(DateUtil.getDayLastTime(endDate.getDate())));
        newHoliday.setAllDay(true);
        newHoliday.setTakenFromAnnual(takenFromAllowance.isActive());
        if (recurringChb.getValue()) {
            if (yearlyRB.getValue()) {
                newHoliday.setRepeatId(RECURRENCE_TYPE_YEARLY);
            } else if (monthlyRB.getValue()) {
                newHoliday.setRepeatId(RECURRENCE_TYPE_MONTHLY);
            }
            newHoliday.setRepeat(true);
        } else {
            newHoliday.setRepeat(false);
        }

        ArrayList<Integer> locationIds = new ArrayList<>();
        for (Map<String, Widget> row : locationTable.getWidgets()) {
            if (row != null) {
                DataListBox db = (DataListBox) row.get(MultiTable.LIST_BOX);
                if (db != null && db.getSelectedItem() != null && db.getSelectedItem().getId() != null) {
                    if (!locationIds.contains(db.getSelectedItem().getId())) {
                        locationIds.add(db.getSelectedItem().getId());
                    }
                }
            }
        }
        newHoliday.setLocationIds(locationIds);
        saveButton.setEnabled(false);
        LoadingPanel.loading(true);
        availabilityService.createOrUpdateHoliday(newHoliday, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void object) {
                if (holidayID != null) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.holiday()));
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_HOLIDAY_EDIT, object, AddEditHolidayView.this);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), Utils.textFormat(wfmStrings.addNew(), wfmStrings.holiday())));
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_HOLIDAY_ADD, object, AddEditHolidayView.this);
                }
                saveButton.setEnabled(true);
                LoadingPanel.loading(false);
                onShellOk();
            }
        });
    }

    private boolean validate() {
        clearErrorStyle();
        int errors = 0;
        errors += markAsError(CustomFormConstants.HOLIDAY.NAME, name, !Validation.validateTextBoxRequired(name));
        errors += markAsError(CustomFormConstants.HOLIDAY.START_DATE, startDate, !Validation.validateDate(startDate, new HTML(), true));
        errors += markAsError(CustomFormConstants.HOLIDAY.END_DATE, endDate, !Validation.validateDate(endDate, new HTML(), true));
        errors += markAsError(CustomFormConstants.HOLIDAY.START_DATE, startDate, !Validation.validateDateOrder(null, startDate, endDate));

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

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
