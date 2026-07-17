package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ExceptionalTimeSlotItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.hrms.client.ui.tabpanels.TimeSlotHistoryTab;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: HaveANiceDay
 * Date: 29.09.11
 * Time: 15:06
 */
public class TimeslotSummaryView extends CustomForm implements Constants, Colapse {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    protected final ProfileServiceAsync profileService = ProfileService.App.get();

    private final Integer objectId;
    private TimeslotItem timeSlotItem;

    private HTML name, shortName, description, timeEntries, effectiveFrom, timeEntryEachTitle,lateMinutes,earlyLeaveMinutes,autoInOutEnabled;

    private KpiDataGrid<TimeslotEmployeeItem> dataGrid;
    private TimeSlotHistoryTab historyTab;

    private final String test_code_ID_name = "timeSlot_summary_view_";

    public static final ProvidesKey<TimeslotEmployeeItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectID();

    public TimeslotSummaryView(Integer objectId) {
        super("summary", hrmsStrings.timeslotSummary());
        this.objectId = objectId;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {

        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return "/timeslotSummaryViewPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                RequestObject requestObject = new RequestObject(objectId);
                HashMap<String, String> params = requestObject.getRequestParams();
                return params;
            }
        });
        addRightButton(pdf);

        if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_EDIT_TIMESLOT)) {
            addEditButton().addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("timeslot|edit/" + objectId));
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        AvailabilityService.App.get().getTimeslot(objectId, new AsyncCallback<TimeslotItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(final TimeslotItem t) {
                LoadingPanel.loading(false);
                timeSlotItem = t;
                fillFormWithData();
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.TIMESLOT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void fillFormWithData() {
        name.setHTML(timeSlotItem.getName());
        shortName.setHTML(timeSlotItem.getShortName());
        description.setHTML(timeSlotItem.getDescription());
        effectiveFrom.setHTML(DateUtils.format(timeSlotItem.getEffectiveDate()));
        lateMinutes.setHTML(timeSlotItem.getLateMinutes() != null ? timeSlotItem.getLateMinutes().toString() : wfmStrings.na());
        earlyLeaveMinutes.setHTML(timeSlotItem.getEarlyMinutes() != null ? timeSlotItem.getEarlyMinutes().toString() : wfmStrings.na());
        autoInOutEnabled.setHTML(timeSlotItem.isAutoInOutEnabled() ? wfmStrings.yes() : wfmStrings.no());
        String str = "<table width='50%'>";
        try {
            str = str +
                    "<tr align=center><td></td>" +
                    "<td align=center colspan=2><b>" + hrmsStrings.workTime() + "</b></td>" +
                    "<td align=center colspan=2><b>" + hrmsStrings.lunchTime() + "</b></td>" +
                    "<td align=center colspan=2><b>" + hrmsStrings.coffeeBreak() + "</b></td></tr>";
            switch (timeSlotItem.getWeekStart()) {
                case 1:
                    //sunday
                    timeEntryEachTitle.setHTML(hrmsStrings.timeEntryForEachSunday());
                    str = str + "<tr><td>" + wfmStrings.sunday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getSunday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getSunday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getSunday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getSunday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchSu()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchSu()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchSu()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchSu()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeSu()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeSu()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeSu()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeSu()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.monday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getMonday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getMonday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getMonday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getMonday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchMo()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchMo()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchMo()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchMo()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeMo()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeMo()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeMo()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeMo()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.tuesday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getTuesday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getTuesday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getTuesday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getTuesday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchTu()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchTu()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchTu()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchTu()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeTu()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeTu()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeTu()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeTu()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.wednesday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getWednesday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getWednesday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getWednesday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getWednesday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchWe()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchWe()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchWe()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchWe()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeWe()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeWe()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeWe()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeWe()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.thursday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getThursday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getThursday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getThursday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getThursday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchTh()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchTh()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchTh()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchTh()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeTh()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeTh()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeTh()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeTh()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.friday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getFriday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getFriday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getFriday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getFriday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchFr()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchFr()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchFr()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchFr()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeFr()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeFr()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeFr()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeFr()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.saturday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getSaturday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getSaturday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getSaturday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getSaturday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchSa()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchSa()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchSa()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchSa()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeSa()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeSa()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeSa()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeSa()[1] % 60) + "</td>" +
                            "</tr>";
                    break;
                case 2:
                    //monday
                    timeEntryEachTitle.setHTML(hrmsStrings.timeEntryForEachMonday());
                    str = str + "<tr><td>" + wfmStrings.monday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getMonday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getMonday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getMonday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getMonday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchMo()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchMo()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchMo()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchMo()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeMo()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeMo()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeMo()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeMo()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.tuesday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getTuesday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getTuesday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getTuesday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getTuesday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchTu()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchTu()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchTu()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchTu()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeTu()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeTu()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeTu()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeTu()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.wednesday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getWednesday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getWednesday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getWednesday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getWednesday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchWe()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchWe()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchWe()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchWe()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeWe()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeWe()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeWe()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeWe()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.thursday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getThursday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getThursday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getThursday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getThursday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchTh()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchTh()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchTh()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchTh()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeTh()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeTh()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeTh()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeTh()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.friday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getFriday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getFriday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getFriday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getFriday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchFr()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchFr()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchFr()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchFr()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeFr()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeFr()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeFr()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeFr()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.saturday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getSaturday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getSaturday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getSaturday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getSaturday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchSa()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchSa()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchSa()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchSa()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeSa()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeSa()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeSa()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeSa()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.sunday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getSunday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getSunday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getSunday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getSunday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchSu()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchSu()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchSu()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchSu()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeSu()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeSu()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeSu()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeSu()[1] % 60) + "</td>" +
                            "</tr>";
                    break;
                case 7:
                    //saturday
                    timeEntryEachTitle.setHTML(hrmsStrings.timeEntryForEachSaturday());
                    str = str + "<tr><td>" + wfmStrings.saturday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getSaturday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getSaturday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getSaturday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getSaturday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchSa()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchSa()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchSa()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchSa()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeSa()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeSa()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeSa()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeSa()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.sunday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getSunday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getSunday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getSunday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getSunday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchSu()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchSu()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchSu()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchSu()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeSu()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeSu()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeSu()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeSu()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.monday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getMonday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getMonday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getMonday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getMonday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchMo()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchMo()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchMo()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchMo()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeMo()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeMo()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeMo()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeMo()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.tuesday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getTuesday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getTuesday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getTuesday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getTuesday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchTu()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchTu()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchTu()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchTu()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeTu()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeTu()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeTu()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeTu()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.wednesday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getWednesday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getWednesday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getWednesday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getWednesday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchWe()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchWe()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchWe()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchWe()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeWe()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeWe()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeWe()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeWe()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.thursday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getThursday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getThursday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getThursday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getThursday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchTh()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchTh()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchTh()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchTh()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeTh()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeTh()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeTh()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeTh()[1] % 60) + "</td>" +
                            "</tr>" +
                            "<tr><td>" + wfmStrings.friday() + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getFriday()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getFriday()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getFriday()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getFriday()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchFr()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchFr()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getLunchFr()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getLunchFr()[1] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeFr()[0] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeFr()[0] % 60) + "</td>" +
                            "<td align=center>" + getTimeSlot(timeSlotItem.getCoffeeFr()[1] / 60) + ":" + getTimeSlot(timeSlotItem.getCoffeeFr()[1] % 60) + "</td>" +
                            "</tr>";
                    break;
                default:
                    break;
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        str = str + "</table>";
        timeEntries.setHTML(str);
        ArrayList<ExceptionalTimeSlotItem> timeSlotItemExceptionalCases = timeSlotItem.getExceptionalCases();
        if (timeSlotItemExceptionalCases != null && timeSlotItemExceptionalCases.size() > 0) {
            StringBuilder excStr = new StringBuilder("<table width='100%'>");
            try {
                excStr.append("<tr align=center><td></td>" + "<td align=center colspan=2><b>").append(hrmsStrings.workTime()).append("</b></td>").append("<td align=center colspan=2><b>").append(hrmsStrings.lunchTime()).append("</b></td>").append("<td align=center colspan=2><b>").append(hrmsStrings.coffeeBreak()).append("</b></td>").append("</tr>");
                for (ExceptionalTimeSlotItem eTsi : timeSlotItemExceptionalCases) {
                    excStr.append("<tr>" + "<td>").append(DateUtils.format(eTsi.getExceptionalDate().getNonConvertedDate())).append("</td>").append("<td align=center>").append(getTimeSlot(eTsi.getWeekDay()[0] / 60)).append(":").append(getTimeSlot(eTsi.getWeekDay()[0] % 60)).append("</td>").append("<td align=center>").append(getTimeSlot(eTsi.getWeekDay()[1] / 60)).append(":").append(getTimeSlot(eTsi.getWeekDay()[1] % 60)).append("</td>").append("<td align=center>").append(getTimeSlot(eTsi.getLunch()[0] / 60)).append(":").append(getTimeSlot(eTsi.getLunch()[0] % 60)).append("</td>").append("<td align=center>").append(getTimeSlot(eTsi.getLunch()[1] / 60)).append(":").append(getTimeSlot(eTsi.getLunch()[1] % 60)).append("</td>").append("<td align=center>").append(getTimeSlot(eTsi.getCoffee()[0] / 60)).append(":").append(getTimeSlot(eTsi.getCoffee()[0] % 60)).append("</td>").append("<td align=center>").append(getTimeSlot(eTsi.getCoffee()[1] / 60)).append(":").append(getTimeSlot(eTsi.getCoffee()[1] % 60)).append("</td>").append("</tr>");
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            excStr.append("</table>");

            Span tooltipWrapper = new Span();

            Icon iInfo = new Icon();
            iInfo.setClass("ficon--info");
            MaterialLink iconLink = new MaterialLink();
            iconLink.add(iInfo);
            String activation = "infoDropDown";
            iconLink.setActivates(activation);

            MaterialDropDown dropDown = new MaterialDropDown(activation);
            dropDown.addStyleName("dropdown-content dropdown-content-tooltip");
            dropDown.getElement().setInnerHTML(hrmsStrings.timeslotUpdatesWillNotAffectPreviouslyRegisteredLeaveRequests());
            dropDown.setHover(true);

            tooltipWrapper.add(iconLink);
            tooltipWrapper.add(dropDown);

            //exceptional date time entries
            FlexTable exceptionalDateTimeEntriesTable = new FlexTable();
            exceptionalDateTimeEntriesTable.setWidth("50%");
            exceptionalDateTimeEntriesTable.setWidget(0, 0, new HorizontalPanelDiv(2, new HTML(hrmsStrings.exceptionalCases()), tooltipWrapper));
            exceptionalDateTimeEntriesTable.setHTML(1, 0, excStr.toString());
            exceptionalDateTimeEntriesTable.ensureDebugId(test_code_ID_name + "exceptional_date_time_entries");
            addField(CustomFormConstants.PERIOD, exceptionalDateTimeEntriesTable, null);
        }
        addField(CustomFormConstants.TIME_ENTRY, timeEntries, null);
        dataGrid.supplyProvider(timeSlotItem.getTimeslotEmployeeItems());
        dataGrid.refresh();
        addField(CustomFormConstants.ASSIGNEES, dataGrid, wfmStrings.assignedEmployees());
    }

    private void initialize() {
        name = initHTML();
        name.ensureDebugId(test_code_ID_name + "name");
        shortName = initHTML();
        description = initHTML();
        description.ensureDebugId(test_code_ID_name + "description");
        effectiveFrom = initHTML();

        lateMinutes = initHTML();
        earlyLeaveMinutes = initHTML();
        autoInOutEnabled = initHTML();

        timeEntryEachTitle = initHTML();
        timeEntries = initHTML();
        timeEntries.ensureDebugId(test_code_ID_name + "time_entries");

        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        historyTab = new TimeSlotHistoryTab(objectId, false);
        initTableColumns();

        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.timeslotSummary());
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name()));
        addField(CustomFormConstants.SHORT_NAME, shortName, getTitle(wfmStrings.shortName()));
        addField(LATE_MINUTES, lateMinutes, getTitle(wfmStrings.lateMinutes()));
        addField(EARLY_LEAVE, earlyLeaveMinutes, getTitle(wfmStrings.earlyLeaveMinutes()));
        addField(AUTO_IN_OUT_ENABLED, autoInOutEnabled, getTitle(wfmStrings.autoInOutEnabled()));
        addField(CustomFormConstants.DESCRIPTION, description, getTitle(wfmStrings.description()));
        addField(CustomFormConstants.DATE_FIELD, effectiveFrom, getTitle(wfmStrings.effectiveDate()));
        addField(CustomFormConstants.TIME_ENTRY_FOR_EACH, timeEntryEachTitle, null);
        addTitleField(CustomFormConstants.ASSIGNEE, wfmStrings.assignedEmployees());
        addField(CustomFormConstants.TIMESLOT_HISTORY_LOG, historyTab, wfmStrings.historyLog(), true);
        show();
    }

    private String getTimeSlot(int time) {
        if (String.valueOf(time).length() == 1) {
            return "0" + time;
        }
        if (String.valueOf(time).length() == 0) {
            return "00";
        }
        return String.valueOf(time);
    }

    private void initTableColumns() {
        //employee Name
        Column<TimeslotEmployeeItem, String> employee = new Column<TimeslotEmployeeItem, String>(new TextCell()) {
            @Override
            public String getValue(TimeslotEmployeeItem object) {
                return object.getEmployeeFullName();
            }
        };
        dataGrid.addColumn(employee, wfmStrings.employee());
        dataGrid.setColumnWidth(employee, 100, com.google.gwt.dom.client.Style.Unit.PCT);
        // department Name
        Column<TimeslotEmployeeItem, String> department = new Column<TimeslotEmployeeItem, String>(new TextCell()) {
            @Override
            public String getValue(TimeslotEmployeeItem object) {
                return object.getEmployeeDepartment();
            }
        };
        dataGrid.addColumn(department, hrmsStrings.teamName());
        dataGrid.setColumnWidth(department, 100, com.google.gwt.dom.client.Style.Unit.PCT);
        //employee position
        Column<TimeslotEmployeeItem, String> position = new Column<TimeslotEmployeeItem, String>(new TextCell()) {
            @Override
            public String getValue(TimeslotEmployeeItem object) {
                return object.getEmployeePosition();
            }
        };
        dataGrid.addColumn(position, wfmStrings.position());
        dataGrid.setColumnWidth(position, 50, com.google.gwt.dom.client.Style.Unit.PCT);
        //employee roles
        Column<TimeslotEmployeeItem, String> role = new Column<TimeslotEmployeeItem, String>(new TextCell()) {
            @Override
            public String getValue(TimeslotEmployeeItem object) {
                return object.getEmployeeStatus();
            }
        };
        dataGrid.addColumn(role, wfmStrings.role());
        dataGrid.setColumnWidth(role, 50, com.google.gwt.dom.client.Style.Unit.PCT);
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
}