package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityServiceAsync;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.hrms.client.ui.tabpanels.HolidayHistoryTab;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class HolidaySummaryView extends CustomForm implements Constants, SchedulerConstant, Colapse {
    private static final AvailabilityServiceAsync availabilityService = AvailabilityService.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final Integer holidayID;
    private HolidayHistoryTab historyTab;

    public HolidaySummaryView(Integer id) {
        super("view", hrmsStrings.summaryHoliday());
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
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    protected Widget onInitialize() {
        super.onInitialize();
        availabilityService.getHoliday(holidayID, new AbstractAsyncCallback<HolidayItem>() {
            public void success(final HolidayItem holiday) {
                addField(CustomFormConstants.HOLIDAY.NAME, new HTML(holiday.getName()), wfmStrings.name());
                TextArea2 area = new TextArea2();
//                area.addStyleName(SHORT_WIDTH);
                area.setWidth("450px");
                area.setHeight(NORMAL_WIDTH);
                area.setText(holiday.getDescription());
                area.setReadOnly(true);
                addField("DETAILS", null, hrmsStrings.holidayDetails());
                addField(CustomFormConstants.HOLIDAY.DESCRIPTION, area, wfmStrings.description());
                addField(CustomFormConstants.HOLIDAY.DAY_OFF, new HTML(holiday.isDayOff()
                        ? "Yes"
                        : "No"), wfmStrings.dayOff());

                addField(CustomFormConstants.HOLIDAY.START_DATE, new HTML(DateUtils.format(holiday.getFrom().getNonConvertedDate())), wfmStrings.startDate());
                addField(CustomFormConstants.HOLIDAY.END_DATE, new HTML(holiday.isAllDay()
                        ? DateUtils.format(holiday.getFrom().getNonConvertedDate())
                        : DateUtils.format(holiday.getTo().getNonConvertedDate())), wfmStrings.endDate());
                addField(CustomFormConstants.HOLIDAY.RECCURING, new HTML(holiday.isRepeat()
                        ? holiday.getRepeatId() == RECURRENCE_TYPE_YEARLY
                        ? wfmStrings.yearly()
                        : wfmStrings.monthly()
                        : "No"), wfmStrings.recurring());
                addField(CustomFormConstants.HOLIDAY.LOCATION, new HTML(holiday.getLocationName().replace(",", ",<br/>")), Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()));

            }
        });
        historyTab = new HolidayHistoryTab(holidayID);
        addField(CustomFormConstants.HOLIDAY.HOLIDAY_HISTORY_LOG, historyTab, wfmStrings.historyLog(), true);
        show();
        return null;
    }

    @Override
    protected void addButtons() {

        if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_EDIT_HOLIDAY)) {
            addEditButton().addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("holiday|edit/" + holidayID));
        }
    }

    @Override
    protected void getDataToFillFields() {
        //To change body of implemented methods use File | Settings | File Templates.
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
