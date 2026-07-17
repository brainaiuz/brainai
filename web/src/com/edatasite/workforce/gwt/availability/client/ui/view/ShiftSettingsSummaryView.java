package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.ShiftSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.ui.tabpanels.TimeSlotHistoryTab;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

public class ShiftSettingsSummaryView extends CustomForm implements Constants, Colapse {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    protected final ProfileServiceAsync profileService = ProfileService.App.get();

    private final Integer objectId;
    private ShiftSettingsItem shiftItem;

    private HTML name;
    private HTML shortName;
    private HTML description;
    private HTML timeEntries;
    private HTML interval;

    public ShiftSettingsSummaryView(Integer objectId) {
        super("summary", hrmsStrings.shiftSettings());
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
                return requestObject.getRequestParams();
            }
        });
        addRightButton(pdf);

        if (Utils.hasPermission(PermissionConstants.SETTINGS_HRMS_SETTINGS_EDIT_TIMESLOT)) {
            addEditButton().addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("shiftsettings|edit/" + objectId));
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        AvailabilityService.App.get().getShiftSettings(objectId, new AsyncCallback<ShiftSettingsItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(final ShiftSettingsItem t) {
                LoadingPanel.loading(false);
                shiftItem = t;
                fillFormWithData();
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SHIFT_SETTINGS_FORM;
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
        name.setHTML(shiftItem.getName());
        shortName.setHTML(shiftItem.getShortName());
        description.setHTML(shiftItem.getDescription());
        interval.setHTML(shiftItem.getInterval());
        String str = "<table width='50%'>";
        try {
            str = str +
                    "<tr align=center><td></td>" +
                    "<td align=center colspan=2><b>" + hrmsStrings.workTime() + "</b></td>" +
                    "<td align=center colspan=2><b>" + hrmsStrings.lunchTime() + "</b></td>" +
                    "<td align=center colspan=2><b>" + hrmsStrings.coffeeBreak() + "</b></td></tr>";
            str = str + "<tr><td>" + hrmsStrings.shiftSettings() + "</td>" +
                    "<td align=center>" + getshift(shiftItem.getTimes()[0] / 60) + ":" + getshift(shiftItem.getTimes()[0] % 60) + "</td>" +
                    "<td align=center>" + getshift(shiftItem.getTimes()[1] / 60) + ":" + getshift(shiftItem.getTimes()[1] % 60) + "</td>" +
                    "<td align=center>" + getshift(shiftItem.getLunchTimes()[0] / 60) + ":" + getshift(shiftItem.getLunchTimes()[0] % 60) + "</td>" +
                    "<td align=center>" + getshift(shiftItem.getLunchTimes()[1] / 60) + ":" + getshift(shiftItem.getLunchTimes()[1] % 60) + "</td>" +
                    "<td align=center>" + getshift(shiftItem.getCoffeeTimes()[0] / 60) + ":" + getshift(shiftItem.getCoffeeTimes()[0] % 60) + "</td>" +
                    "<td align=center>" + getshift(shiftItem.getCoffeeTimes()[1] / 60) + ":" + getshift(shiftItem.getCoffeeTimes()[1] % 60) + "</td>" +
                    "</tr>";

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        str = str + "</table>";
        timeEntries.setHTML(str);
        addField(CustomFormConstants.TIME_ENTRY, timeEntries, null);
    }

    private void initialize() {
        name = initHTML();
        String test_code_ID_name = "shift_summary_view_";
        name.ensureDebugId(test_code_ID_name + "name");
        shortName = initHTML();
        description = initHTML();
        description.ensureDebugId(test_code_ID_name + "description");
        interval = initHTML();

        timeEntries = initHTML();
        timeEntries.ensureDebugId(test_code_ID_name + "time_entries");

        TimeSlotHistoryTab historyTab = new TimeSlotHistoryTab(objectId, true);

        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.shiftSettings());
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name()));
        addField(CustomFormConstants.SHORT_NAME, shortName, getTitle(wfmStrings.shortName()));
        addField(CustomFormConstants.DESCRIPTION, description, getTitle(wfmStrings.description()));
        addTitleField(CustomFormConstants.ASSIGNEE, wfmStrings.assignedEmployees());
        addField(CustomFormConstants.TIMESLOT_HISTORY_LOG, historyTab, wfmStrings.historyLog(), true);
        addField(COLOR_PICKER, interval, wfmStrings.interval());
        show();
    }

    private String getshift(int time) {
        if (String.valueOf(time).length() == 1) {
            return "0" + time;
        }
        if (String.valueOf(time).length() == 0) {
            return "00";
        }
        return String.valueOf(time);
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