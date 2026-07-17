package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.localization.AvailabilityMessages;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ExceptionalTimeSlotItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.CallbackSynchronizer;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ColorWidget;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.view.AddEditLocaleView;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.edatasite.workforce.gwt.hrms.client.ui.tabpanels.TimeSlotHistoryTab;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class AddTimeslotView extends CustomForm implements FocusHandler, BlurHandler, KeyUpHandler, KeyPressHandler, KeyDownHandler, Constants, Colapse {

    private static final String EXCEPTIONAL_DAYS = "EXCEPTIONAL_DAYS";
    private static final String EXCEPTIONAL_REASONS = "EXCEPTIONAL_REASONS";
    protected SettingsData settingsData;
    protected final ProfileServiceAsync profileService = ProfileService.App.get();
    private final CallbackSynchronizer callbacksynchronizer = new CallbackSynchronizer();
    private static final AvailabilityMessages availabilityMessages = AvailabilityMessages.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private static final DisCloseIconBundle DEFAULT_IMAGES = GWT.create(DisCloseIconBundle.class);

    public interface DisCloseIconBundle extends ClientBundle {
        @Source("com/edatasite/workforce/gwt/core/client/bundles/icons/bottom-arrow.png")
        ImageResource openImage();

        @Source("com/edatasite/workforce/gwt/core/client/bundles/icons/right-arrow.png")
        ImageResource closedImage();
    }

    private KpiCellTree assignEmployees;
    private TextArea2 description;
    private Integer objectId;
    private boolean isDefaultTimeslot = false;
    private TextBox name;
    private WfmButton2 locale;
    private FlexTable localedNameBox;
    private AddEditLocaleView localeView;
    private ReferenceLocale localeItem;
    private TextBox shortName;
    private ColorWidget colorWidget;
    private TimeslotItem timeSlotItem;
    private TextBox[][] weekDays;
    private MultiSelectLookUp exceptionalWeekDays;
    private MultiSelectLookUp exceptionalLeaveReasons;
    private DynamicTable exceptionalDaysAndReasons;
    private final Div exceptionalWeekDaysPanel = new Div();
    private MultiTable exceptionalCases;
    private MultiTable dailyTimeslot;
    private VerticalPanel dailyTimeslotPanel;
    private DateTimePicker dateTimePicker;
    private DatePicker effectiveFrom;
    private DatePicker endDate;
    private DisclosurePanel exceptionalCasesPanel;
    private Image hideUnHide;
    private TimeSlotHistoryTab historyTab;
    private final FlexTable exceptionTable = new FlexTable();
    private final FlexTable ft = new FlexTable();
    private String timeEntryTitle = "";
    private SelectItem[] notWorkingDays;
    private TextBox lateMinutes;
    private TextBox earlyLeaveMinutes;
    private TextBox validCheckInStart;
    private TextBox validCheckInEnd;
    private TextBox validCheckOutStart;
    private TextBox validCheckOutEnd;
    private final FlexTable validIn = new FlexTable();
    private final FlexTable validOut = new FlexTable();
    private CheckBox autoInOutEnabled;

    private String test_code_ID_name = "add_timeSlot_view_";

    public AddTimeslotView() {
        super("add", wfmStrings.timeslot());
    }

    public AddTimeslotView(Integer objectId, boolean isDefaultTimeslot) {
        super("edit", hrmsStrings.editTimeslot());
        this.objectId = objectId;
        this.test_code_ID_name = "edit_timeSlot_view_";
        this.isDefaultTimeslot = isDefaultTimeslot;
    }

    public String getIconStyle() {
        return "availability add-timeslot";
    }

    @Override
    public void onBlur(BlurEvent event) {
        Object sender = event.getSource();
        /**/
        if (((TextBox) sender).getText().length() == 1) {
            ((TextBox) sender).setText("0" + ((TextBox) sender).getText() + ":00");
        }
        if (((TextBox) sender).getText().length() == 3) {
            ((TextBox) sender).setText(((TextBox) sender).getText() + "00");
        }
        if (((TextBox) sender).getText().length() == 4) {
            ((TextBox) sender).setText(((TextBox) sender).getText() + "0");
        }
        if (((TextBox) sender).getText().length() > 5) {
            ((TextBox) sender).setText(((TextBox) sender).getText().substring(0, 5));
        }
        String[] times = ((TextBox) sender).getText().split(":");
        try {
            int hours = Integer.parseInt(times[0]);
            int minutes = Integer.parseInt(times[1]);
            if (hours > 23) {
                Info.show(hrmsStrings.hoursExeedsLimit(), Info.Type.WARNING);
                ((TextBox) sender).setText("0");
                ((TextBox) sender).setFocus(true);
                return;
            }
            if (minutes > 59) {
                Info.show(hrmsStrings.minutesExeedsLimit(), Info.Type.WARNING);
                ((TextBox) sender).setText("0");
                ((TextBox) sender).setFocus(true);
            }
        } catch (NumberFormatException e) {

            if (!("".equals(((TextBox) sender).getText()))) {
                Info.show(availabilityMessages.timeNotValid(((TextBox) sender).getText()), Info.Type.WARNING);
                ((TextBox) sender).setText("00:00");
                ((TextBox) sender).setFocus(true);
            } else {
                ((TextBox) sender).setText("00:00");
            }
        }
    }

    @Override
    public void onFocus(FocusEvent event) {
        /* ((TextBox) sender).setText("");*/
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        //Window.alert("Keydown");
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        //Window.alert("KeyPress" + ((TextBox)sender).getText());
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        Object sender = event.getSource();
        if (((TextBox) sender).getText().length() == 2) {
            ((TextBox) sender).setText(((TextBox) sender).getText() + ":");
        }
    }

    @Override
    protected void addButtons() {
        if (objectId == null) {
            addButton(wfmStrings.save(), BTN_PRIMARY, null, (test_code_ID_name + "add_timeSlot_button"), clickEvent -> save());
        } else {
            addButton(wfmStrings.update(), BTN_PRIMARY, null, (test_code_ID_name + "update_timeSlot_button"), clickEvent -> update());
        }
    }

    private void getCompanySettings() {
        LoadingPanel.loading(true);
        profileService.getCompanySettings(false, callbacksynchronizer.registerCallback(new AbstractAsyncCallback<SettingsData>() {
            public void success(SettingsData result) {
                LoadingPanel.loading(false);
                settingsData = result;
                setWeekNames();
                initExceptionalDays(result);
            }
        }));
    }

    private void initExceptionalDays(SettingsData result) {
        exceptionalDaysAndReasons.clear();
        final Map<String, Widget> itemWidgetsMap = new LinkedHashMap<>();
        exceptionalWeekDays.setItems(
                wfmStrings.week(),
                getNotWorkingDay(ft, weekDays)
        );
        exceptionalLeaveReasons.setItems(hrmsStrings.leaveReason(), result.getReasons());
        itemWidgetsMap.put(EXCEPTIONAL_DAYS, exceptionalWeekDays);
        itemWidgetsMap.put(EXCEPTIONAL_REASONS, exceptionalLeaveReasons);
        exceptionalDaysAndReasons.addRow(result.getObjectID(), itemWidgetsMap.values().toArray(new Widget[]{}));
    }

    private void setWeekStart(FlexTable table, TextBox[][] week) {
        switch (settingsData.getOverallDatePickerWeekStart()) {
            case 1:
                ///sunday
                timeEntryTitle = hrmsStrings.timeEntryForEachSunday();
                table.setHTML(2, 0, "<b class=customTitle>" + wfmStrings.sunday() + ":</b>");
                table.setHTML(3, 0, "<b class=customTitle>" + wfmStrings.monday() + ":</b>");
                table.setHTML(4, 0, "<b class=customTitle>" + wfmStrings.tuesday() + ":</b>");
                table.setHTML(5, 0, "<b class=customTitle>" + wfmStrings.wednesday() + ":</b>");
                table.setHTML(6, 0, "<b class=customTitle>" + wfmStrings.thursday() + ":</b>");
                table.setHTML(7, 0, "<b class=customTitle>" + wfmStrings.friday() + ":</b>");
                table.setHTML(8, 0, "<b class=customTitle>" + wfmStrings.saturday() + ":</b>");
                break;
            case 2:
                //monday
                timeEntryTitle = hrmsStrings.timeEntryForEachMonday();
                table.setHTML(2, 0, "<b class=customTitle>" + wfmStrings.monday() + ":</b>");
                table.setHTML(3, 0, "<b class=customTitle>" + wfmStrings.tuesday() + ":</b>");
                table.setHTML(4, 0, "<b class=customTitle>" + wfmStrings.wednesday() + ":</b>");
                table.setHTML(5, 0, "<b class=customTitle>" + wfmStrings.thursday() + ":</b>");
                table.setHTML(6, 0, "<b class=customTitle>" + wfmStrings.friday() + ":</b>");
                table.setHTML(7, 0, "<b class=customTitle>" + wfmStrings.saturday() + ":</b>");
                table.setHTML(8, 0, "<b class=customTitle>" + wfmStrings.sunday() + ":</b>");
                break;
            case 7:
                //saturday
                timeEntryTitle = hrmsStrings.timeEntryForEachSaturday();
                table.setHTML(2, 0, "<b class=customTitle>" + wfmStrings.saturday() + ":</b>");
                table.setHTML(3, 0, "<b class=customTitle>" + wfmStrings.sunday() + ":</b>");
                table.setHTML(4, 0, "<b class=customTitle>" + wfmStrings.monday() + ":</b>");
                table.setHTML(5, 0, "<b class=customTitle>" + wfmStrings.tuesday() + ":</b>");
                table.setHTML(6, 0, "<b class=customTitle>" + wfmStrings.wednesday() + ":</b>");
                table.setHTML(7, 0, "<b class=customTitle>" + wfmStrings.thursday() + ":</b>");
                table.setHTML(8, 0, "<b class=customTitle>" + wfmStrings.friday() + ":</b>");
                break;
            default:
                break;
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                table.setWidget(i + 2, j + 1, week[i][j]);
            }
        }
        //remove icon
        Icon[] removeIcon = new Icon[7];
        for (int i = 0; i < 7; i++) {
            final int index = i;
            removeIcon[i] = new Icon();
            removeIcon[i].setStyleName("ficon--reset");
            removeIcon[i].getElement().getStyle().setCursor(Style.Cursor.POINTER);
            removeIcon[i].setTooltip(wfmStrings.reset());
            removeIcon[i].addClickHandler(ev -> clearValues(index, week));
        }
        //add to table remove icon
        table.setWidget(2, 7, removeIcon[0]);
        table.setWidget(3, 7, removeIcon[1]);
        table.setWidget(4, 7, removeIcon[2]);
        table.setWidget(5, 7, removeIcon[3]);
        table.setWidget(6, 7, removeIcon[4]);
        table.setWidget(7, 7, removeIcon[5]);
        table.setWidget(8, 7, removeIcon[6]);
        //copy to all link
        SimpleLink copyToAll = new SimpleLink(wfmStrings.copyToAll());
        copyToAll.addClickHandler(event -> {
            for (int i = 1; i < week.length; i++) {
                for (int j = 0; j < week[i].length; j++) {
                    week[i][j].setText(week[0][j].getText());
                }
            }
            exceptionalWeekDays.clear();
            exceptionalWeekDays.refreshOracle(true);
            exceptionalWeekDays.clearOracleItems();
            exceptionalWeekDays.setItems(
                    wfmStrings.week(),
                    getNotWorkingDay(ft, weekDays)
            );
        });
        table.setWidget(2, 8, copyToAll);
    }

    private void setWeekNames() {
        setWeekStart(ft, weekDays);

        exceptionalCases = new MultiTable(0, false, new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getExceptionalCasesRow(null, false);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        exceptionalCases.setSpacing();
        exceptionalCases.setOnLinesAdded(() -> exceptionalCases.setRemovableIconMarginTop(5));
        exceptionalCases.setStyleName("addFieldSet");

        dailyTimeslot = new MultiTable(0, false, new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getExceptionalCasesRow(null, true);
            }

            @Override
            public boolean isFilled() {
                return true;
            }
        });
        dailyTimeslot.setSpacing();
        dailyTimeslot.setOnLinesAdded(() -> {
            correctDailyRow();
            dailyTimeslot.setRemovableIconMarginTop(10);
        });
        dailyTimeslot.setStyleName("addFieldSet");
        dailyTimeslotPanel = new VerticalPanel();
        dailyTimeslotPanel.getElement().getStyle().setWidth(75, Style.Unit.PCT);
        dailyTimeslotPanel.add(getDailyExceptionalCasesRowTitles(true));
        dailyTimeslotPanel.add(dailyTimeslot);
        dailyTimeslotPanel.setVisible(false);


        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(getDailyExceptionalCasesRowTitles(false));
        verticalPanel.add(exceptionalCases);
        //
        hideUnHide = new Image();
        hideUnHide.setStyleName("pointer");
        hideUnHide.setResource(DEFAULT_IMAGES.closedImage());
        hideUnHide.getElement().getStyle().setPaddingRight(3, com.google.gwt.dom.client.Style.Unit.PX);
        hideUnHide.getElement().getStyle().setCursor(com.google.gwt.dom.client.Style.Cursor.POINTER);
        hideUnHide.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);

        //info tooltip
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

        exceptionalCasesPanel = new DisclosurePanel(DEFAULT_IMAGES.openImage(), DEFAULT_IMAGES.closedImage(), hrmsStrings.exceptionalCases());
        HTML exceptionalCasesHtml = new HTML(hrmsStrings.exceptionalCases());
        exceptionalCasesHtml.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        HorizontalPanelDiv headerWidget = new HorizontalPanelDiv(2, hideUnHide, exceptionalCasesHtml, tooltipWrapper);
        exceptionalCasesPanel.setHeader(headerWidget);
        exceptionalCasesPanel.setSize("100%", "30px");
        exceptionalCasesPanel.setOpen(false);
        exceptionalCasesPanel.setContent(verticalPanel);
        headerWidget.getParent().getElement().getStyle().setTextDecoration(Style.TextDecoration.NONE);
        verticalPanel.getElement().getStyle().setWidth(75, Style.Unit.PCT);
        exceptionalCasesPanel.addCloseHandler(disclosurePanelCloseEvent -> {
            //close logic
            hideUnHide.setResource(DEFAULT_IMAGES.closedImage());
        });
        exceptionalCasesPanel.addOpenHandler(disclosurePanelOpenEvent -> {
            hideUnHide.setResource(DEFAULT_IMAGES.openImage());
        });

        exceptionalWeekDays = new MultiSelectLookUp() {
            @Override
            public boolean onCondition(String text) {
                return false;
            }
        };

        exceptionalLeaveReasons = new MultiSelectLookUp() {
            @Override
            public boolean onCondition(String text) {
                return false;
            }
        };

        exceptionalDaysAndReasons = new DynamicTable(getColumns(), false);
        exceptionalDaysAndReasons.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        HTML title = new HTML("<b class=customTitle align=right>" + wfmStrings.countAsLeave() + ":" + "</b>");
        title.getElement().setPropertyString("style", "margin-bottom: 5px;");
        exceptionalWeekDaysPanel.add(title);
        exceptionalWeekDaysPanel.add(exceptionalDaysAndReasons);


        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.timeSlotDetails());
        addField(CustomFormConstants.NAME, localedNameBox, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.DESCRIPTION, description, null);

        new KpiToolTip(effectiveFrom, hrmsStrings.timeslotUpdatesWillEffectFromThisDate());
        addField(CustomFormConstants.DATE_FIELD, effectiveFrom, getTitle(wfmStrings.effectiveDate(), true));
        addField(CustomFormConstants.END_DATE, endDate, getTitle(wfmStrings.endDate()));
        addField(CustomFormConstants.SHORT_NAME, shortName, getTitle(wfmStrings.shortName()));
        addField(COLOR_PICKER, colorWidget, getTitle(wfmStrings.color()));
        addField(LATE_MINUTES, lateMinutes, wfmStrings.lateMinutes());
        addField(EARLY_LEAVE, earlyLeaveMinutes, wfmStrings.earlyLeaveMinutes());
        addField(VALID_CHECK_IN, validIn, wfmStrings.validCheckInTime());
        addField(VALID_CHECK_OUT, validOut, wfmStrings.validCheckOutTime());
        addField(AUTO_IN_OUT_ENABLED, autoInOutEnabled, wfmStrings.autoInOutEnabled());

        addField(CustomFormConstants.TIME_ENTRY_FOR_EACH, new HTML(timeEntryTitle), null);
        addField(CustomFormConstants.TIME_ENTRY, ft, null);
        addField(CustomFormConstants.EXCEPTIONAL_CASE_FOR_LEAVE, exceptionalWeekDaysPanel, null);
        addField(CustomFormConstants.DATE_PERIOD, dailyTimeslotPanel, null);
        addField(CustomFormConstants.PERIOD, exceptionalCasesPanel, null);
        //assignees -> 2
        addTitleField(CustomFormConstants.ASSIGNEE, wfmStrings.assignedEmployees());
        addField(CustomFormConstants.ASSIGNEES, assignEmployees, getTitle(wfmStrings.assignedEmployees()));
        if (objectId != null) {
            historyTab = new TimeSlotHistoryTab(objectId, false);
            addField(CustomFormConstants.TIMESLOT_HISTORY_LOG, historyTab, wfmStrings.historyLog(), true);
        }
        show();
    }

    private DynamicTableColumn[] getColumns() {
        LinkedList<DynamicTableColumn> columnsList = new LinkedList<>();
        DynamicTableColumn selectDays = new DynamicTableColumn(hrmsStrings.selectDays(), EXCEPTIONAL_DAYS, 130);
        selectDays.setStyle("vertical-align: top;");
        DynamicTableColumn selectReasons = new DynamicTableColumn(hrmsStrings.selectReasons(), EXCEPTIONAL_REASONS, 130);
        selectReasons.setStyle("vertical-align: top;");
        columnsList.add(selectDays);
        columnsList.add(selectReasons);

        return columnsList.toArray(new DynamicTableColumn[columnsList.size()]);
    }

    private SelectItem[] getNotWorkingDay(FlexTable table, TextBox[][] weekDays) {
        ArrayList<SelectItem> notWorkingDays = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < 7; i++) {
            String startTime = weekDays[i][0].getText();
            String endTime = weekDays[i][1].getText();
            if (startTime.equals(endTime) || startTime.equals("") || endTime.equals("")) {
                if (table.getText(i + 2, 0).replace(":", "").equals(wfmStrings.sunday())) {
                    notWorkingDays.add(new SelectItem(0, table.getText(i + 2, 0).replace(":", "")));
                } else if (table.getText(i + 2, 0).replace(":", "").equals(wfmStrings.monday())) {
                    notWorkingDays.add(new SelectItem(1, table.getText(i + 2, 0).replace(":", "")));
                } else if (table.getText(i + 2, 0).replace(":", "").equals(wfmStrings.tuesday())) {
                    notWorkingDays.add(new SelectItem(2, table.getText(i + 2, 0).replace(":", "")));
                } else if (table.getText(i + 2, 0).replace(":", "").equals(wfmStrings.wednesday())) {
                    notWorkingDays.add(new SelectItem(3, table.getText(i + 2, 0).replace(":", "")));
                } else if (table.getText(i + 2, 0).replace(":", "").equals(wfmStrings.thursday())) {
                    notWorkingDays.add(new SelectItem(4, table.getText(i + 2, 0).replace(":", "")));
                } else if (table.getText(i + 2, 0).replace(":", "").equals(wfmStrings.friday())) {
                    notWorkingDays.add(new SelectItem(5, table.getText(i + 2, 0).replace(":", "")));
                } else {
                    notWorkingDays.add(new SelectItem(6, table.getText(i + 2, 0).replace(":", "")));
                }
            }
        }
        return notWorkingDays.toArray(new SelectItem[]{});
    }

    private void correctDailyRow() {
        int i = 1;
        for (Map<String, Widget> row : dailyTimeslot.getWidgets()) {
            if (row != null) {
                FlexTable fte = (FlexTable) row.get(MultiTable.MULTI_TABLE);
                HTML dayno = new HTML("Day " + i++);
                dayno.setWidth("75px");
                fte.setWidget(0, 0, dayno);
            }
        }
    }

    @Override
    protected void getDataToFillFields() {
        if (objectId != null) {
            LoadingPanel.loading(true);
            AvailabilityService.App.get().getTimeslot(objectId, new AbstractAsyncCallback<TimeslotItem>() {
                @Override
                public void success(TimeslotItem item) {
                    LoadingPanel.loading(false);
                    timeSlotItem = item;
                    localeItem = item.getLocaleItem() != null ? item.getLocaleItem() : new ReferenceLocale();
                    fillFormWithData(item);
                }
            });
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.TIMESLOT_FORM;
    }

    @Override
    protected String getFormType() {
        return objectId == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
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

    private boolean allFieldsAreZero(TextBox[][] weekdays, int day) {
        for (int i = 0; i < weekdays[day].length; i++) {
            if (convertToInteger(weekdays[day][i].getText()) != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkTimeSlot(TextBox[][] week) {
        boolean isValid = true;
        for (int i = 0; i < week.length; i++) {
            isValid &= (allFieldsAreZero(week, i) || isTimeEntriesValid(week, i));
        }
        return isValid;
    }

    private void clearValues(int i, TextBox[][] week) {
        for (int j = 0; j < 6; j++) {
            week[i][j].setText("00:00");
        }
    }

    private int convertToInteger(String s) {
        String[] time = s.split(":");
        int hours = 0;
        int minutes = 0;
        try {
            hours = Integer.parseInt(time[0]);
            minutes = Integer.parseInt(time[1]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        return (hours * 60 + minutes);
    }

    private void setWeekStartDay(Integer i, TextBox[][] week, TimeslotItem item) {
        switch (i) {
            case 1:
                //sunday
                week[0][0].setText(getTimeSlot(item.getSunday()[0] / 60) + ":" + getTimeSlot(item.getSunday()[0] % 60));
                week[0][1].setText(getTimeSlot(item.getSunday()[1] / 60) + ":" + getTimeSlot(item.getSunday()[1] % 60));
                week[0][2].setText(getTimeSlot(item.getLunchSu()[0] / 60) + ":" + getTimeSlot(item.getLunchSu()[0] % 60));
                week[0][3].setText(getTimeSlot(item.getLunchSu()[1] / 60) + ":" + getTimeSlot(item.getLunchSu()[1] % 60));
                week[0][4].setText(getTimeSlot(item.getCoffeeSu()[0] / 60) + ":" + getTimeSlot(item.getCoffeeSu()[0] % 60));
                week[0][5].setText(getTimeSlot(item.getCoffeeSu()[1] / 60) + ":" + getTimeSlot(item.getCoffeeSu()[1] % 60));

                week[1][0].setText(getTimeSlot(item.getMonday()[0] / 60) + ":" + getTimeSlot(item.getMonday()[0] % 60));
                week[1][1].setText(getTimeSlot(item.getMonday()[1] / 60) + ":" + getTimeSlot(item.getMonday()[1] % 60));
                week[1][2].setText(getTimeSlot(item.getLunchMo()[0] / 60) + ":" + getTimeSlot(item.getLunchMo()[0] % 60));
                week[1][3].setText(getTimeSlot(item.getLunchMo()[1] / 60) + ":" + getTimeSlot(item.getLunchMo()[1] % 60));
                week[1][4].setText(getTimeSlot(item.getCoffeeMo()[0] / 60) + ":" + getTimeSlot(item.getCoffeeMo()[0] % 60));
                week[1][5].setText(getTimeSlot(item.getCoffeeMo()[1] / 60) + ":" + getTimeSlot(item.getCoffeeMo()[1] % 60));

                week[2][0].setText(getTimeSlot(item.getTuesday()[0] / 60) + ":" + getTimeSlot(item.getTuesday()[0] % 60));
                week[2][1].setText(getTimeSlot(item.getTuesday()[1] / 60) + ":" + getTimeSlot(item.getTuesday()[1] % 60));
                week[2][2].setText(getTimeSlot(item.getLunchTu()[0] / 60) + ":" + getTimeSlot(item.getLunchTu()[0] % 60));
                week[2][3].setText(getTimeSlot(item.getLunchTu()[1] / 60) + ":" + getTimeSlot(item.getLunchTu()[1] % 60));
                week[2][4].setText(getTimeSlot(item.getCoffeeTu()[0] / 60) + ":" + getTimeSlot(item.getCoffeeTu()[0] % 60));
                week[2][5].setText(getTimeSlot(item.getCoffeeTu()[1] / 60) + ":" + getTimeSlot(item.getCoffeeTu()[1] % 60));

                week[3][0].setText(getTimeSlot(item.getWednesday()[0] / 60) + ":" + getTimeSlot(item.getWednesday()[0] % 60));
                week[3][1].setText(getTimeSlot(item.getWednesday()[1] / 60) + ":" + getTimeSlot(item.getWednesday()[1] % 60));
                week[3][2].setText(getTimeSlot(item.getLunchWe()[0] / 60) + ":" + getTimeSlot(item.getLunchWe()[0] % 60));
                week[3][3].setText(getTimeSlot(item.getLunchWe()[1] / 60) + ":" + getTimeSlot(item.getLunchWe()[1] % 60));
                week[3][4].setText(getTimeSlot(item.getCoffeeWe()[0] / 60) + ":" + getTimeSlot(item.getCoffeeWe()[0] % 60));
                week[3][5].setText(getTimeSlot(item.getCoffeeWe()[1] / 60) + ":" + getTimeSlot(item.getCoffeeWe()[1] % 60));

                week[4][0].setText(getTimeSlot(item.getThursday()[0] / 60) + ":" + getTimeSlot(item.getThursday()[0] % 60));
                week[4][1].setText(getTimeSlot(item.getThursday()[1] / 60) + ":" + getTimeSlot(item.getThursday()[1] % 60));
                week[4][2].setText(getTimeSlot(item.getLunchTh()[0] / 60) + ":" + getTimeSlot(item.getLunchTh()[0] % 60));
                week[4][3].setText(getTimeSlot(item.getLunchTh()[1] / 60) + ":" + getTimeSlot(item.getLunchTh()[1] % 60));
                week[4][4].setText(getTimeSlot(item.getCoffeeTh()[0] / 60) + ":" + getTimeSlot(item.getCoffeeTh()[0] % 60));
                week[4][5].setText(getTimeSlot(item.getCoffeeTh()[1] / 60) + ":" + getTimeSlot(item.getCoffeeTh()[1] % 60));

                week[5][0].setText(getTimeSlot(item.getFriday()[0] / 60) + ":" + getTimeSlot(item.getFriday()[0] % 60));
                week[5][1].setText(getTimeSlot(item.getFriday()[1] / 60) + ":" + getTimeSlot(item.getFriday()[1] % 60));
                week[5][2].setText(getTimeSlot(item.getLunchFr()[0] / 60) + ":" + getTimeSlot(item.getLunchFr()[0] % 60));
                week[5][3].setText(getTimeSlot(item.getLunchFr()[1] / 60) + ":" + getTimeSlot(item.getLunchFr()[1] % 60));
                week[5][4].setText(getTimeSlot(item.getCoffeeFr()[0] / 60) + ":" + getTimeSlot(item.getCoffeeFr()[0] % 60));
                week[5][5].setText(getTimeSlot(item.getCoffeeFr()[1] / 60) + ":" + getTimeSlot(item.getCoffeeFr()[1] % 60));

                week[6][0].setText(getTimeSlot(item.getSaturday()[0] / 60) + ":" + getTimeSlot(item.getSaturday()[0] % 60));
                week[6][1].setText(getTimeSlot(item.getSaturday()[1] / 60) + ":" + getTimeSlot(item.getSaturday()[1] % 60));
                week[6][2].setText(getTimeSlot(item.getLunchSa()[0] / 60) + ":" + getTimeSlot(item.getLunchSa()[0] % 60));
                week[6][3].setText(getTimeSlot(item.getLunchSa()[1] / 60) + ":" + getTimeSlot(item.getLunchSa()[1] % 60));
                week[6][4].setText(getTimeSlot(item.getCoffeeSa()[0] / 60) + ":" + getTimeSlot(item.getCoffeeSa()[0] % 60));
                week[6][5].setText(getTimeSlot(item.getCoffeeSa()[1] / 60) + ":" + getTimeSlot(item.getCoffeeSa()[1] % 60));
                break;
            case 2:
                //monday
                week[0][0].setText(getTimeSlot(item.getMonday()[0] / 60) + ":" + getTimeSlot(item.getMonday()[0] % 60));
                week[0][1].setText(getTimeSlot(item.getMonday()[1] / 60) + ":" + getTimeSlot(item.getMonday()[1] % 60));
                week[0][2].setText(getTimeSlot(item.getLunchMo()[0] / 60) + ":" + getTimeSlot(item.getLunchMo()[0] % 60));
                week[0][3].setText(getTimeSlot(item.getLunchMo()[1] / 60) + ":" + getTimeSlot(item.getLunchMo()[1] % 60));
                week[0][4].setText(getTimeSlot(item.getCoffeeMo()[0] / 60) + ":" + getTimeSlot(item.getCoffeeMo()[0] % 60));
                week[0][5].setText(getTimeSlot(item.getCoffeeMo()[1] / 60) + ":" + getTimeSlot(item.getCoffeeMo()[1] % 60));

                week[1][0].setText(getTimeSlot(item.getTuesday()[0] / 60) + ":" + getTimeSlot(item.getTuesday()[0] % 60));
                week[1][1].setText(getTimeSlot(item.getTuesday()[1] / 60) + ":" + getTimeSlot(item.getTuesday()[1] % 60));
                week[1][2].setText(getTimeSlot(item.getLunchTu()[0] / 60) + ":" + getTimeSlot(item.getLunchTu()[0] % 60));
                week[1][3].setText(getTimeSlot(item.getLunchTu()[1] / 60) + ":" + getTimeSlot(item.getLunchTu()[1] % 60));
                week[1][4].setText(getTimeSlot(item.getCoffeeTu()[0] / 60) + ":" + getTimeSlot(item.getCoffeeTu()[0] % 60));
                week[1][5].setText(getTimeSlot(item.getCoffeeTu()[1] / 60) + ":" + getTimeSlot(item.getCoffeeTu()[1] % 60));

                week[2][0].setText(getTimeSlot(item.getWednesday()[0] / 60) + ":" + getTimeSlot(item.getWednesday()[0] % 60));
                week[2][1].setText(getTimeSlot(item.getWednesday()[1] / 60) + ":" + getTimeSlot(item.getWednesday()[1] % 60));
                week[2][2].setText(getTimeSlot(item.getLunchWe()[0] / 60) + ":" + getTimeSlot(item.getLunchWe()[0] % 60));
                week[2][3].setText(getTimeSlot(item.getLunchWe()[1] / 60) + ":" + getTimeSlot(item.getLunchWe()[1] % 60));
                week[2][4].setText(getTimeSlot(item.getCoffeeWe()[0] / 60) + ":" + getTimeSlot(item.getCoffeeWe()[0] % 60));
                week[2][5].setText(getTimeSlot(item.getCoffeeWe()[1] / 60) + ":" + getTimeSlot(item.getCoffeeWe()[1] % 60));

                week[3][0].setText(getTimeSlot(item.getThursday()[0] / 60) + ":" + getTimeSlot(item.getThursday()[0] % 60));
                week[3][1].setText(getTimeSlot(item.getThursday()[1] / 60) + ":" + getTimeSlot(item.getThursday()[1] % 60));
                week[3][2].setText(getTimeSlot(item.getLunchTh()[0] / 60) + ":" + getTimeSlot(item.getLunchTh()[0] % 60));
                week[3][3].setText(getTimeSlot(item.getLunchTh()[1] / 60) + ":" + getTimeSlot(item.getLunchTh()[1] % 60));
                week[3][4].setText(getTimeSlot(item.getCoffeeTh()[0] / 60) + ":" + getTimeSlot(item.getCoffeeTh()[0] % 60));
                week[3][5].setText(getTimeSlot(item.getCoffeeTh()[1] / 60) + ":" + getTimeSlot(item.getCoffeeTh()[1] % 60));

                week[4][0].setText(getTimeSlot(item.getFriday()[0] / 60) + ":" + getTimeSlot(item.getFriday()[0] % 60));
                week[4][1].setText(getTimeSlot(item.getFriday()[1] / 60) + ":" + getTimeSlot(item.getFriday()[1] % 60));
                week[4][2].setText(getTimeSlot(item.getLunchFr()[0] / 60) + ":" + getTimeSlot(item.getLunchFr()[0] % 60));
                week[4][3].setText(getTimeSlot(item.getLunchFr()[1] / 60) + ":" + getTimeSlot(item.getLunchFr()[1] % 60));
                week[4][4].setText(getTimeSlot(item.getCoffeeFr()[0] / 60) + ":" + getTimeSlot(item.getCoffeeFr()[0] % 60));
                week[4][5].setText(getTimeSlot(item.getCoffeeFr()[1] / 60) + ":" + getTimeSlot(item.getCoffeeFr()[1] % 60));

                week[5][0].setText(getTimeSlot(item.getSaturday()[0] / 60) + ":" + getTimeSlot(item.getSaturday()[0] % 60));
                week[5][1].setText(getTimeSlot(item.getSaturday()[1] / 60) + ":" + getTimeSlot(item.getSaturday()[1] % 60));
                week[5][2].setText(getTimeSlot(item.getLunchSa()[0] / 60) + ":" + getTimeSlot(item.getLunchSa()[0] % 60));
                week[5][3].setText(getTimeSlot(item.getLunchSa()[1] / 60) + ":" + getTimeSlot(item.getLunchSa()[1] % 60));
                week[5][4].setText(getTimeSlot(item.getCoffeeSa()[0] / 60) + ":" + getTimeSlot(item.getCoffeeSa()[0] % 60));
                week[5][5].setText(getTimeSlot(item.getCoffeeSa()[1] / 60) + ":" + getTimeSlot(item.getCoffeeSa()[1] % 60));

                week[6][0].setText(getTimeSlot(item.getSunday()[0] / 60) + ":" + getTimeSlot(item.getSunday()[0] % 60));
                week[6][1].setText(getTimeSlot(item.getSunday()[1] / 60) + ":" + getTimeSlot(item.getSunday()[1] % 60));
                week[6][2].setText(getTimeSlot(item.getLunchSu()[0] / 60) + ":" + getTimeSlot(item.getLunchSu()[0] % 60));
                week[6][3].setText(getTimeSlot(item.getLunchSu()[1] / 60) + ":" + getTimeSlot(item.getLunchSu()[1] % 60));
                week[6][4].setText(getTimeSlot(item.getCoffeeSu()[0] / 60) + ":" + getTimeSlot(item.getCoffeeSu()[0] % 60));
                week[6][5].setText(getTimeSlot(item.getCoffeeSu()[1] / 60) + ":" + getTimeSlot(item.getCoffeeSu()[1] % 60));
                break;
            case 7:
                // satruday
                week[0][0].setText(getTimeSlot(item.getSaturday()[0] / 60) + ":" + getTimeSlot(item.getSaturday()[0] % 60));
                week[0][1].setText(getTimeSlot(item.getSaturday()[1] / 60) + ":" + getTimeSlot(item.getSaturday()[1] % 60));
                week[0][2].setText(getTimeSlot(item.getLunchSa()[0] / 60) + ":" + getTimeSlot(item.getLunchSa()[0] % 60));
                week[0][3].setText(getTimeSlot(item.getLunchSa()[1] / 60) + ":" + getTimeSlot(item.getLunchSa()[1] % 60));
                week[0][4].setText(getTimeSlot(item.getCoffeeSa()[0] / 60) + ":" + getTimeSlot(item.getCoffeeSa()[0] % 60));
                week[0][5].setText(getTimeSlot(item.getCoffeeSa()[1] / 60) + ":" + getTimeSlot(item.getCoffeeSa()[1] % 60));

                week[1][0].setText(getTimeSlot(item.getSunday()[0] / 60) + ":" + getTimeSlot(item.getSunday()[0] % 60));
                week[1][1].setText(getTimeSlot(item.getSunday()[1] / 60) + ":" + getTimeSlot(item.getSunday()[1] % 60));
                week[1][2].setText(getTimeSlot(item.getLunchSu()[0] / 60) + ":" + getTimeSlot(item.getLunchSu()[0] % 60));
                week[1][3].setText(getTimeSlot(item.getLunchSu()[1] / 60) + ":" + getTimeSlot(item.getLunchSu()[1] % 60));
                week[1][4].setText(getTimeSlot(item.getCoffeeSu()[0] / 60) + ":" + getTimeSlot(item.getCoffeeSu()[0] % 60));
                week[1][5].setText(getTimeSlot(item.getCoffeeSu()[1] / 60) + ":" + getTimeSlot(item.getCoffeeSu()[1] % 60));

                week[2][0].setText(getTimeSlot(item.getMonday()[0] / 60) + ":" + getTimeSlot(item.getMonday()[0] % 60));
                week[2][1].setText(getTimeSlot(item.getMonday()[1] / 60) + ":" + getTimeSlot(item.getMonday()[1] % 60));
                week[2][2].setText(getTimeSlot(item.getLunchMo()[0] / 60) + ":" + getTimeSlot(item.getLunchMo()[0] % 60));
                week[2][3].setText(getTimeSlot(item.getLunchMo()[1] / 60) + ":" + getTimeSlot(item.getLunchMo()[1] % 60));
                week[2][4].setText(getTimeSlot(item.getCoffeeMo()[0] / 60) + ":" + getTimeSlot(item.getCoffeeMo()[0] % 60));
                week[2][5].setText(getTimeSlot(item.getCoffeeMo()[1] / 60) + ":" + getTimeSlot(item.getCoffeeMo()[1] % 60));

                week[3][0].setText(getTimeSlot(item.getTuesday()[0] / 60) + ":" + getTimeSlot(item.getTuesday()[0] % 60));
                week[3][1].setText(getTimeSlot(item.getTuesday()[1] / 60) + ":" + getTimeSlot(item.getTuesday()[1] % 60));
                week[3][2].setText(getTimeSlot(item.getLunchTu()[0] / 60) + ":" + getTimeSlot(item.getLunchTu()[0] % 60));
                week[3][3].setText(getTimeSlot(item.getLunchTu()[1] / 60) + ":" + getTimeSlot(item.getLunchTu()[1] % 60));
                week[3][4].setText(getTimeSlot(item.getCoffeeTu()[0] / 60) + ":" + getTimeSlot(item.getCoffeeTu()[0] % 60));
                week[3][5].setText(getTimeSlot(item.getCoffeeTu()[1] / 60) + ":" + getTimeSlot(item.getCoffeeTu()[1] % 60));

                week[4][0].setText(getTimeSlot(item.getWednesday()[0] / 60) + ":" + getTimeSlot(item.getWednesday()[0] % 60));
                week[4][1].setText(getTimeSlot(item.getWednesday()[1] / 60) + ":" + getTimeSlot(item.getWednesday()[1] % 60));
                week[4][2].setText(getTimeSlot(item.getLunchWe()[0] / 60) + ":" + getTimeSlot(item.getLunchWe()[0] % 60));
                week[4][3].setText(getTimeSlot(item.getLunchWe()[1] / 60) + ":" + getTimeSlot(item.getLunchWe()[1] % 60));
                week[4][4].setText(getTimeSlot(item.getCoffeeWe()[0] / 60) + ":" + getTimeSlot(item.getCoffeeWe()[0] % 60));
                week[4][5].setText(getTimeSlot(item.getCoffeeWe()[1] / 60) + ":" + getTimeSlot(item.getCoffeeWe()[1] % 60));

                week[5][0].setText(getTimeSlot(item.getThursday()[0] / 60) + ":" + getTimeSlot(item.getThursday()[0] % 60));
                week[5][1].setText(getTimeSlot(item.getThursday()[1] / 60) + ":" + getTimeSlot(item.getThursday()[1] % 60));
                week[5][2].setText(getTimeSlot(item.getLunchTh()[0] / 60) + ":" + getTimeSlot(item.getLunchTh()[0] % 60));
                week[5][3].setText(getTimeSlot(item.getLunchTh()[1] / 60) + ":" + getTimeSlot(item.getLunchTh()[1] % 60));
                week[5][4].setText(getTimeSlot(item.getCoffeeTh()[0] / 60) + ":" + getTimeSlot(item.getCoffeeTh()[0] % 60));
                week[5][5].setText(getTimeSlot(item.getCoffeeTh()[1] / 60) + ":" + getTimeSlot(item.getCoffeeTh()[1] % 60));

                week[6][0].setText(getTimeSlot(item.getFriday()[0] / 60) + ":" + getTimeSlot(item.getFriday()[0] % 60));
                week[6][1].setText(getTimeSlot(item.getFriday()[1] / 60) + ":" + getTimeSlot(item.getFriday()[1] % 60));
                week[6][2].setText(getTimeSlot(item.getLunchFr()[0] / 60) + ":" + getTimeSlot(item.getLunchFr()[0] % 60));
                week[6][3].setText(getTimeSlot(item.getLunchFr()[1] / 60) + ":" + getTimeSlot(item.getLunchFr()[1] % 60));
                week[6][4].setText(getTimeSlot(item.getCoffeeFr()[0] / 60) + ":" + getTimeSlot(item.getCoffeeFr()[0] % 60));
                week[6][5].setText(getTimeSlot(item.getCoffeeFr()[1] / 60) + ":" + getTimeSlot(item.getCoffeeFr()[1] % 60));
                break;
            default:
                break;
        }
    }

    private void fillFormWithData(TimeslotItem item) {
        //timeSlot name
        if (item.getName() != null) {
            name.setText(item.getName());
        }
        //description
        if (item.getDescription() != null) {
            description.setText(item.getDescription());
        }
        if (item.getEffectiveDate() != null /*&& (new Date()).compareTo(item.getEffectiveDate()) <= 0*/) {
            effectiveFrom.setDate(item.getEffectiveDate());
            endDate.setDate(DateUtils.addDays(item.getEffectiveDate(), 730));
        }
        if (item.getLateMinutes() != null) {
            lateMinutes.setText(item.getLateMinutes().toString());
        }
        if (item.getEarlyMinutes() != null) {
            earlyLeaveMinutes.setText(item.getEarlyMinutes().toString());
        }
        if (item.getValidInStart() != null) {
            validCheckInStart.setText(getTimeSlot(item.getValidInStart() / 60) + ":" + getTimeSlot(item.getValidInStart() % 60));
        }
        if (item.getValidInEnd() != null) {
            validCheckInEnd.setText(getTimeSlot(item.getValidInEnd() / 60) + ":" + getTimeSlot(item.getValidInEnd() % 60));
        }
        if (item.getValidOutStart() != null) {
            validCheckOutStart.setText(getTimeSlot(item.getValidOutStart() / 60) + ":" + getTimeSlot(item.getValidOutStart() % 60));
        }
        if (item.getValidOutEnd() != null) {
            validCheckOutEnd.setText(getTimeSlot(item.getValidOutEnd() / 60) + ":" + getTimeSlot(item.getValidOutEnd() % 60));
        }
        autoInOutEnabled.setValue(item.isAutoInOutEnabled());
        setWeekStartDay(settingsData.getOverallDatePickerWeekStart(), weekDays, item);
        ArrayList<ExceptionalTimeSlotItem> exceptionalTimeSlotItems = item.getExceptionalCases();

        if (exceptionalTimeSlotItems != null && exceptionalTimeSlotItems.size() > 0) {
            exceptionalCasesPanel.setOpen(true);
            hideUnHide.setResource(DEFAULT_IMAGES.openImage());
            exceptionalCases.removeAllRows();
            for (ExceptionalTimeSlotItem exceptionalTimeSlotItem : exceptionalTimeSlotItems) {
                exceptionalCases.addWidgets(getExceptionalCasesRow(exceptionalTimeSlotItem, false));
            }
        }
        exceptionalWeekDays.clear();
        exceptionalWeekDays.refreshOracle(true);
        exceptionalWeekDays.clearOracleItems();
        exceptionalWeekDays.setItems(
                wfmStrings.week(),
                getNotWorkingDay(ft, weekDays)
        );
        exceptionalLeaveReasons.clear();
        exceptionalLeaveReasons.refreshOracle(true);
        exceptionalLeaveReasons.clearOracleItems();
        exceptionalLeaveReasons.setItems(hrmsStrings.leaveReason(), item.getReasons());
        exceptionalLeaveReasons.setSelectedItems(item.getSelectedLeaveReasons());
        shortName.setText(item.getShortName());

        if (item.getHexColor() != null && item.getHexColor().length() > 0)
            colorWidget.setColor(item.getHexColor());

        setSelectedExceptionalLeaveDays(item.getAdditionalLeaveDays());

    }

    private void setSelectedExceptionalLeaveDays(ArrayList<Integer> selectedDays) {
        SelectItem[] selectItems = getNotWorkingDay(ft, weekDays);
        ArrayList<SelectItem> selectedItems = new ArrayList<>();
        for (Integer day : selectedDays) {
            for (int i = 0; i < selectItems.length; i++) {
                if (selectItems[i] != null && day.equals(selectItems[i].getId())) {
                    SelectItem item = selectItems[i];
                    selectedItems.add(item);
                }
            }
        }
        exceptionalWeekDays.setSelectedItems(selectedItems);
    }

    private void createWeek(TextBox[][] week) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                week[i][j].setText("00:00");
                week[i][j].addFocusHandler(this);
                week[i][j].addKeyDownHandler(this);
                week[i][j].addKeyPressHandler(this);
                week[i][j].addKeyUpHandler(this);
                week[i][j].setWidth("75");
            }
            week[0][0].setText("09:30");
            week[1][0].setText("09:30");
            week[2][0].setText("09:30");
            week[3][0].setText("09:30");
            week[4][0].setText("09:30");

            week[0][1].setText("18:00");
            week[1][1].setText("18:00");
            week[2][1].setText("18:00");
            week[3][1].setText("18:00");
            week[4][1].setText("18:00");
        }

    }

    private void initialize() {
        name = new TextBox();
        name.setName("name");
        name.addStyleName(DEFAULT_WIDTH);
        name.ensureDebugId(test_code_ID_name + "name");
        localeItem = new ReferenceLocale();

        locale = new WfmButton2(wfmStrings.vacancyLocale());
        locale.setStyleName("font-style: italic;", true);
        locale.addClickHandler(event -> {
            if (localeView == null) {
                localeView = new AddEditLocaleView(name.getText(), localeItem);
            } else {
                localeView.setLocaleItem(localeItem);
                localeView.setNameValue(name.getText());
                localeView.showView();
            }
        });
        localedNameBox = new FlexTable();
        localedNameBox.addStyleName("formLine-table");
        localedNameBox.setWidget(0, 0, name);
        localedNameBox.setWidget(0, 1, locale);
        localedNameBox.getCellFormatter().addStyleName(0, 1, "formLine-table__act");

        shortName = new TextBox();
        shortName.setName("name");
        shortName.setMaxLength(3);
        shortName.addStyleName(DEFAULT_WIDTH);
        shortName.ensureDebugId(test_code_ID_name + "shortName");

        colorWidget = new ColorWidget();
        colorWidget.setWidth("295px");

        description = new TextArea2(wfmStrings.description());
        description.addStyleName(MAX_DEFAULT_WIDTH);
        description.ensureDebugId(test_code_ID_name + "description");

        effectiveFrom = new DatePicker();
        effectiveFrom.addStyleName(DEFAULT_WIDTH);
        effectiveFrom.setDate(new Date());
        effectiveFrom.addChangeHandler(changeEvent -> {
            if (objectId != null) {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                messageBox.setTitle("Effective Date!");
                messageBox.setMessage(hrmsStrings.areYourSureChangeEffectDate());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onCancel() {
                        effectiveFrom.setDate(new Date());
                    }
                });
                messageBox.open();
            }
            endDate.setDate(DateUtils.addDays(effectiveFrom.getDate(), 730));
        });

        endDate = new DatePicker();
        endDate.addStyleName(DEFAULT_WIDTH);
        endDate.setEnabled(false);

        weekDays = new TextBox[7][6];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                weekDays[i][j] = new TextBox();
                weekDays[i][j].setWidth("80px");
                weekDays[i][j].addValueChangeHandler(val -> {
                    exceptionalWeekDays.clear();
                    exceptionalWeekDays.refreshOracle(true);
                    exceptionalWeekDays.clearOracleItems();
                    exceptionalWeekDays.setItems(
                            wfmStrings.week(),
                            getNotWorkingDay(ft, weekDays)
                    );
                });
            }
        }
        //assign employees
        assignEmployees = new KpiCellTree();
        assignEmployees.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                //employee name
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };
                employee.setSortable(true);
                sortHandler.setComparator(employee, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                selectedDataGrid.addColumn(employee, wfmStrings.employee());
                selectedDataGrid.setColumnWidth(employee, 50, com.google.gwt.dom.client.Style.Unit.PCT);
                if (!isDefaultTimeslot) {
                    //remove action
                    final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new SimpleLinkCell()) {
                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return wfmStrings.delete();
                        }
                    };
                    action.setFieldUpdater((index, object, value) -> {
                        List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                        contacts.remove(object);
                        object.setSelected(false);
                        selectionModel.setSelected(object, false);
                    });
                    selectedDataGrid.addColumn(action, wfmStrings.action());
                    selectedDataGrid.setColumnWidth(action, 20, com.google.gwt.dom.client.Style.Unit.PCT);
                }
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
            }
        });

        lateMinutes = new TextBox();
        Validation.addNumericKeyboardListener(lateMinutes);

        earlyLeaveMinutes = new TextBox();
        Validation.addNumericKeyboardListener(earlyLeaveMinutes);

        autoInOutEnabled = new CheckBox();

        validCheckInStart = new TextBox();
        Validation.addNumericKeyboardListener(validCheckInStart);

        validCheckInStart.setWidth("75px");
        validCheckInStart.setText("00:00");
        validCheckInStart.addFocusHandler(this);
        validCheckInStart.addKeyDownHandler(this);
        validCheckInStart.addKeyPressHandler(this);
        validCheckInStart.addKeyUpHandler(this);

        validCheckInEnd = new TextBox();
        Validation.addNumericKeyboardListener(validCheckInEnd);

        validCheckInEnd.setWidth("75px");
        validCheckInEnd.setText("00:00");
        validCheckInEnd.addFocusHandler(this);
        validCheckInEnd.addKeyDownHandler(this);
        validCheckInEnd.addKeyPressHandler(this);
        validCheckInEnd.addKeyUpHandler(this);


        validIn.setWidget(0, 0, new HTML(wfmStrings.from()));
        validIn.setWidget(0, 1, validCheckInStart);
        validIn.setWidget(0, 2, new HTML(wfmStrings.to()));
        validIn.setWidget(0, 3, validCheckInEnd);

        validIn.setCellPadding(15);
        validIn.setCellSpacing(15);
        validIn.setWidth("50%");


        validIn.setCellPadding(0);


        validCheckOutStart = new TextBox();
        Validation.addNumericKeyboardListener(validCheckOutStart);

        validCheckOutStart.setWidth("75px");
        validCheckOutStart.setText("00:00");
        validCheckOutStart.addFocusHandler(this);
        validCheckOutStart.addKeyDownHandler(this);
        validCheckOutStart.addKeyPressHandler(this);
        validCheckOutStart.addKeyUpHandler(this);

        validCheckOutEnd = new TextBox();
        Validation.addNumericKeyboardListener(validCheckOutEnd);

        validCheckOutEnd.setWidth("75px");
        validCheckOutEnd.setText("00:00");
        validCheckOutEnd.addFocusHandler(this);
        validCheckOutEnd.addKeyDownHandler(this);
        validCheckOutEnd.addKeyPressHandler(this);
        validCheckOutEnd.addKeyUpHandler(this);
        validOut.setWidget(0, 0, new HTML(wfmStrings.from()));
        validOut.setWidget(0, 1, validCheckOutStart);
        validOut.setWidget(0, 2, new HTML(wfmStrings.to()));
        validOut.setWidget(0, 3, validCheckOutEnd);

        validOut.setCellPadding(15);
        validOut.setCellSpacing(15);
        validOut.setWidth("50%");


        createWeek(weekDays);

        reloadAssignees();
        setTableFeatures(ft);
        setTableFeatures(exceptionTable);
        ////////////Week names
        getCompanySettings();
    }

    private void setTableFeatures(FlexTable table) {
        table.setCellPadding(15);
        table.setCellSpacing(15);
        //title 1
        table.setHTML(0, 0, "");
        table.setWidget(0, 1, new HTML("<b class=customTitle align=right style=\"margin-left: 3px\">" + wfmStrings.contactwork() + " " + wfmStrings.time().toLowerCase() + "</b>"));
        table.setWidget(0, 3, new HTML("<b class=customTitle align=right style=\"margin-left: 3px\">" + hrmsStrings.lunch() + " " + wfmStrings.time().toLowerCase() + "</b>"));
        table.setWidget(0, 5, new HTML("<b class=customTitle align=right style=\"white-space: nowrap; margin-left: 3px\">" + hrmsStrings.coffee() + " " + hrmsStrings.getPropertyBreak().toLowerCase() + "</b>"));
        table.setHTML(0, 7, "");
        //title 2
        table.setHTML(1, 0, "");
        table.setWidget(1, 1, new HTML("<div style=\"margin-left: 3px\">" + wfmStrings.start() + "</div>"));
        table.setWidget(1, 2, new HTML("<div style=\"margin-left: 3px\">" + wfmStrings.end() + "</div>"));
        table.setWidget(1, 3, new HTML("<div style=\"margin-left: 3px\">" + wfmStrings.start() + "</div>"));
        table.setWidget(1, 4, new HTML("<div style=\"margin-left: 3px\">" + wfmStrings.end() + "</div>"));
        table.setWidget(1, 5, new HTML("<div style=\"margin-left: 3px\">" + wfmStrings.start() + "</div>"));
        table.setWidget(1, 6, new HTML("<div style=\"margin-left: 3px\">" + wfmStrings.end() + "</div>"));

        //cell formatter to table
        HTMLTable.CellFormatter cellFormatter = table.getCellFormatter();
        cellFormatter.setWidth(0, 0, "8%");
        cellFormatter.setWidth(0, 1, "2%");
        cellFormatter.setWidth(0, 2, "10%");
        cellFormatter.setWidth(0, 3, "2%");
        cellFormatter.setWidth(0, 4, "10%");
        cellFormatter.setWidth(0, 5, "2%");
        cellFormatter.setWidth(0, 6, "15%");
        cellFormatter.setWidth(0, 7, "2%");
        cellFormatter.setWidth(0, 8, "40%");
        cellFormatter.setHorizontalAlignment(2, 8, HasHorizontalAlignment.ALIGN_LEFT);
    }

    private FlexTable getDailyExceptionalCasesRowTitles(boolean daily) {
        FlexTable flex = new FlexTable();
        flex.setCellPadding(15);
        flex.setCellSpacing(15);
        flex.setWidth("135%");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        HTMLTable.CellFormatter cellFormatter = flex.getCellFormatter();
        cellFormatter.setWidth(0, 0, "8%");
        cellFormatter.setWidth(0, 1, "2%");
        cellFormatter.setWidth(0, 2, "10%");
        cellFormatter.setWidth(0, 3, "2%");
        cellFormatter.setWidth(0, 4, "10%");
        cellFormatter.setWidth(0, 5, "2%");
        cellFormatter.setWidth(0, 6, "15%");
        cellFormatter.setWidth(0, 7, "2%");
        cellFormatter.setWidth(0, 8, "40%");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        HTML dateHTML = new HTML("<b class=customTitle align=right>" + (daily ? wfmStrings.day() : wfmStrings.date()) + "</b>");
        dateHTML.setWidth("140px");
        flex.setWidget(0, 0, dateHTML);                                      //exceptional date
        HTML workHTML = new HTML("<b class=customTitle align=right>" + wfmStrings.contactwork() + " " + wfmStrings.time().toLowerCase() + "</b>");
        workHTML.setWidth("75px");
        flex.setWidget(0, 1, workHTML);                              //work
        HTML lunchHTML = new HTML("<b class=customTitle align=right>" + hrmsStrings.lunch() + " " + wfmStrings.time().toLowerCase() + "</b>");
        lunchHTML.setWidth("75px");
        flex.setWidget(0, 3, lunchHTML);                             //lunch
        HTML coffeeHTML = new HTML("<b class=customTitle align=right style=\"white-space: nowrap\">" + hrmsStrings.coffee() + " " + hrmsStrings.getPropertyBreak().toLowerCase() + "</b>");
        coffeeHTML.setWidth("75px");
        flex.setWidget(0, 5, coffeeHTML);                            //coffee
        flex.setHTML(0, 7, "&nbsp;");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        flex.setHTML(1, 0, "&nbsp;");                          //exceptional date
        flex.setHTML(1, 1, wfmStrings.start());       //work
        flex.setHTML(1, 2, wfmStrings.end());         //time
        flex.setHTML(1, 3, wfmStrings.start());       //lunch
        flex.setHTML(1, 4, wfmStrings.end());         //time
        flex.setHTML(1, 5, wfmStrings.start());       //coffee
        flex.setHTML(1, 6, wfmStrings.end());         //break
        flex.setHTML(1, 7, "&nbsp;");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return flex;
    }

    private WidgetsMap getExceptionalCasesRow(ExceptionalTimeSlotItem exceptionalTimeSlotItem, boolean daily) {
        WidgetsMap widgetsMap = new WidgetsMap();
        FlexTable flexTable = new FlexTable();
        flexTable.setCellPadding(15);
        flexTable.setCellSpacing(15);
        flexTable.setWidth("133%");

        HTMLTable.CellFormatter cellFormatter = flexTable.getCellFormatter();
        cellFormatter.setWidth(0, 0, "8%");
        cellFormatter.setWidth(0, 1, "2%");
        cellFormatter.setWidth(0, 2, "10%");
        cellFormatter.setWidth(0, 3, "2%");
        cellFormatter.setWidth(0, 4, "10%");
        cellFormatter.setWidth(0, 5, "2%");
        cellFormatter.setWidth(0, 6, "15%");
        cellFormatter.setWidth(0, 7, "2%");
        cellFormatter.setWidth(0, 8, "40%");

        final DatePicker wfmDatePicker = new DatePicker();
        wfmDatePicker.setDate(null);
        wfmDatePicker.setWidth("130px");
        wfmDatePicker.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        wfmDatePicker.setTitle(hrmsStrings.exceptionalDate());

        flexTable.setWidget(0, 0, daily ? new HTML(wfmStrings.day() + " 1 ") : wfmDatePicker);
        flexTable.getWidget(0, 0).setWidth("130px");

        final TextBox[][] exceptionalCasesWeekDays = new TextBox[1][6];

        for (int j = 0; j < 6; j++) {
            exceptionalCasesWeekDays[0][j] = new TextBox();
            exceptionalCasesWeekDays[0][j].setWidth("75px");
            exceptionalCasesWeekDays[0][j].setText("00:00");
            exceptionalCasesWeekDays[0][j].addFocusHandler(this);
            exceptionalCasesWeekDays[0][j].addKeyDownHandler(this);
            exceptionalCasesWeekDays[0][j].addKeyPressHandler(this);
            exceptionalCasesWeekDays[0][j].addKeyUpHandler(this);

            flexTable.setWidget(0, j + 1, exceptionalCasesWeekDays[0][j]);
        }
        exceptionalCasesWeekDays[0][0].setText("09:30");
        exceptionalCasesWeekDays[0][1].setText("18:00");

        Icon removeIcon = new Icon();
        removeIcon.setStyleName("ficon--reset");
        removeIcon.setTooltip(wfmStrings.reset());
        removeIcon.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        removeIcon.addClickHandler(event -> {
            //register clear selected date
            for (int j = 0; j < 6; j++) {
                exceptionalCasesWeekDays[0][j].setText("00:00");
                wfmDatePicker.setDefaultValue();
            }
        });
        flexTable.setWidget(0, 7, removeIcon);

        if (exceptionalTimeSlotItem != null) {
            if (exceptionalTimeSlotItem.getExceptionalDate() != null)
                wfmDatePicker.setDate(exceptionalTimeSlotItem.getExceptionalDate().getNonConvertedDate());
            //
            exceptionalCasesWeekDays[0][0].setText(getTimeSlot(exceptionalTimeSlotItem.getWeekDay()[0] / 60) + ":" + getTimeSlot(exceptionalTimeSlotItem.getWeekDay()[0] % 60));
            exceptionalCasesWeekDays[0][1].setText(getTimeSlot(exceptionalTimeSlotItem.getWeekDay()[1] / 60) + ":" + getTimeSlot(exceptionalTimeSlotItem.getWeekDay()[1] % 60));
            //
            exceptionalCasesWeekDays[0][2].setText(getTimeSlot(exceptionalTimeSlotItem.getLunch()[0] / 60) + ":" + getTimeSlot(exceptionalTimeSlotItem.getLunch()[0] % 60));
            exceptionalCasesWeekDays[0][3].setText(getTimeSlot(exceptionalTimeSlotItem.getLunch()[1] / 60) + ":" + getTimeSlot(exceptionalTimeSlotItem.getLunch()[1] % 60));
            //
            exceptionalCasesWeekDays[0][4].setText(getTimeSlot(exceptionalTimeSlotItem.getCoffee()[0] / 60) + ":" + getTimeSlot(exceptionalTimeSlotItem.getCoffee()[0] % 60));
            exceptionalCasesWeekDays[0][5].setText(getTimeSlot(exceptionalTimeSlotItem.getCoffee()[1] / 60) + ":" + getTimeSlot(exceptionalTimeSlotItem.getCoffee()[1] % 60));
        } else {
            exceptionalCasesWeekDays[0][0].setText("09:30");
            exceptionalCasesWeekDays[0][1].setText("18:00");
        }

        widgetsMap.addWidgets(flexTable);
        widgetsMap.add(MultiTable.MULTI_TABLE, flexTable);       //multi table

        return widgetsMap;
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

    private boolean isNull(TextBox day1, TextBox day2) {
        return (convertToInteger(day1.getText()) == 0 && convertToInteger(day2.getText()) == 0);
    }

    private boolean isLower(TextBox day1, TextBox day2) {
        return convertToInteger(day1.getText()) < convertToInteger(day2.getText());
    }

    private boolean isLowerOrEqual(TextBox day1, TextBox day2) {
        return convertToInteger(day1.getText()) <= convertToInteger(day2.getText());
    }

    private boolean isTimeEntriesValid(TextBox[][] weekdays, int day) {
        boolean isValid = true;
        isValid = isValid & isLower(weekdays[day][0], weekdays[day][1]);

        if (!isNull(weekdays[day][2], weekdays[day][3])) {
            boolean isValidEntries = isLower(weekdays[day][2], weekdays[day][3]) && isLowerOrEqual(weekdays[day][0], weekdays[day][2]) && isLowerOrEqual(weekdays[day][3], weekdays[day][1]);
            isValid &= isValidEntries;
            if (!isValidEntries) {
                markAsError(weekdays[day][2], true);
                markAsError(weekdays[day][3], true);
            }
        }
        if (!isNull(weekdays[day][4], weekdays[day][5])) {
            boolean isValidEntries = isLower(weekdays[day][4], weekdays[day][5]) && isLowerOrEqual(weekdays[day][0], weekdays[day][4]) && isLowerOrEqual(weekdays[day][5], weekdays[day][1]);
            isValid &= isValidEntries;
            if (!isValidEntries) {
                markAsError(weekdays[day][4], true);
                markAsError(weekdays[day][5], true);
            }
        }
        return isValid;
    }

    private void reloadAssignees() {
        AvailabilityService.App.get().getEmployeesByTeamsList(objectId, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
            @Override
            public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                assignEmployees.setItems(result);
            }
        });
    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        setValues();
        Info.show(hrmsStrings.savingTimeslotWait(), Info.Type.INFO);
        LoadingPanel.loading(true);
        AvailabilityService.App.get().createTimeslot(timeSlotItem, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void object) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.timeslot()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMESLOT_ADD, object, AddTimeslotView.this);
                closeTab();
            }
        });
    }

    private void getWeekDay(Integer i, TextBox[][] week) {
        switch (i) {
            case 1:
                //sunday
                timeSlotItem.setSunday(new int[]{convertToInteger(week[0][0].getText()), convertToInteger(week[0][1].getText())});
                timeSlotItem.setLunchSu(new int[]{convertToInteger(week[0][2].getText()), convertToInteger(week[0][3].getText())});
                timeSlotItem.setCoffeeSu(new int[]{convertToInteger(week[0][4].getText()), convertToInteger(week[0][5].getText())});
                timeSlotItem.setMonday(new int[]{convertToInteger(week[1][0].getText()), convertToInteger(week[1][1].getText())});
                timeSlotItem.setLunchMo(new int[]{convertToInteger(week[1][2].getText()), convertToInteger(week[1][3].getText())});
                timeSlotItem.setCoffeeMo(new int[]{convertToInteger(week[1][4].getText()), convertToInteger(week[1][5].getText())});
                timeSlotItem.setTuesday(new int[]{convertToInteger(week[2][0].getText()), convertToInteger(week[2][1].getText())});
                timeSlotItem.setLunchTu(new int[]{convertToInteger(week[2][2].getText()), convertToInteger(week[2][3].getText())});
                timeSlotItem.setCoffeeTu(new int[]{convertToInteger(week[2][4].getText()), convertToInteger(week[2][5].getText())});
                timeSlotItem.setWednesday(new int[]{convertToInteger(week[3][0].getText()), convertToInteger(week[3][1].getText())});
                timeSlotItem.setLunchWe(new int[]{convertToInteger(week[3][2].getText()), convertToInteger(week[3][3].getText())});
                timeSlotItem.setCoffeeWe(new int[]{convertToInteger(week[3][4].getText()), convertToInteger(week[3][5].getText())});
                timeSlotItem.setThursday(new int[]{convertToInteger(week[4][0].getText()), convertToInteger(week[4][1].getText())});
                timeSlotItem.setLunchTh(new int[]{convertToInteger(week[4][2].getText()), convertToInteger(week[4][3].getText())});
                timeSlotItem.setCoffeeTh(new int[]{convertToInteger(week[4][4].getText()), convertToInteger(week[4][5].getText())});
                timeSlotItem.setFriday(new int[]{convertToInteger(week[5][0].getText()), convertToInteger(week[5][1].getText())});
                timeSlotItem.setLunchFr(new int[]{convertToInteger(week[5][2].getText()), convertToInteger(week[5][3].getText())});
                timeSlotItem.setCoffeeFr(new int[]{convertToInteger(week[5][4].getText()), convertToInteger(week[5][5].getText())});
                timeSlotItem.setSaturday(new int[]{convertToInteger(week[6][0].getText()), convertToInteger(week[6][1].getText())});
                timeSlotItem.setLunchSa(new int[]{convertToInteger(week[6][2].getText()), convertToInteger(week[6][3].getText())});
                timeSlotItem.setCoffeeSa(new int[]{convertToInteger(week[6][4].getText()), convertToInteger(week[6][5].getText())});
                break;
            case 2:
                //monday
                timeSlotItem.setMonday(new int[]{convertToInteger(week[0][0].getText()), convertToInteger(week[0][1].getText())});
                timeSlotItem.setLunchMo(new int[]{convertToInteger(week[0][2].getText()), convertToInteger(week[0][3].getText())});
                timeSlotItem.setCoffeeMo(new int[]{convertToInteger(week[0][4].getText()), convertToInteger(week[0][5].getText())});
                timeSlotItem.setTuesday(new int[]{convertToInteger(week[1][0].getText()), convertToInteger(week[1][1].getText())});
                timeSlotItem.setLunchTu(new int[]{convertToInteger(week[1][2].getText()), convertToInteger(week[1][3].getText())});
                timeSlotItem.setCoffeeTu(new int[]{convertToInteger(week[1][4].getText()), convertToInteger(week[1][5].getText())});
                timeSlotItem.setWednesday(new int[]{convertToInteger(week[2][0].getText()), convertToInteger(week[2][1].getText())});
                timeSlotItem.setLunchWe(new int[]{convertToInteger(week[2][2].getText()), convertToInteger(week[2][3].getText())});
                timeSlotItem.setCoffeeWe(new int[]{convertToInteger(week[2][4].getText()), convertToInteger(week[2][5].getText())});
                timeSlotItem.setThursday(new int[]{convertToInteger(week[3][0].getText()), convertToInteger(week[3][1].getText())});
                timeSlotItem.setLunchTh(new int[]{convertToInteger(week[3][2].getText()), convertToInteger(week[3][3].getText())});
                timeSlotItem.setCoffeeTh(new int[]{convertToInteger(week[3][4].getText()), convertToInteger(week[3][5].getText())});
                timeSlotItem.setFriday(new int[]{convertToInteger(week[4][0].getText()), convertToInteger(week[4][1].getText())});
                timeSlotItem.setLunchFr(new int[]{convertToInteger(week[4][2].getText()), convertToInteger(week[4][3].getText())});
                timeSlotItem.setCoffeeFr(new int[]{convertToInteger(week[4][4].getText()), convertToInteger(week[4][5].getText())});
                timeSlotItem.setSaturday(new int[]{convertToInteger(week[5][0].getText()), convertToInteger(week[5][1].getText())});
                timeSlotItem.setLunchSa(new int[]{convertToInteger(week[5][2].getText()), convertToInteger(week[5][3].getText())});
                timeSlotItem.setCoffeeSa(new int[]{convertToInteger(week[5][4].getText()), convertToInteger(week[5][5].getText())});
                timeSlotItem.setSunday(new int[]{convertToInteger(week[6][0].getText()), convertToInteger(week[6][1].getText())});
                timeSlotItem.setLunchSu(new int[]{convertToInteger(week[6][2].getText()), convertToInteger(week[6][3].getText())});
                timeSlotItem.setCoffeeSu(new int[]{convertToInteger(week[6][4].getText()), convertToInteger(week[6][5].getText())});
                break;
            case 7:
                //saturday
                timeSlotItem.setSaturday(new int[]{convertToInteger(week[0][0].getText()), convertToInteger(week[0][1].getText())});
                timeSlotItem.setLunchSa(new int[]{convertToInteger(week[0][2].getText()), convertToInteger(week[0][3].getText())});
                timeSlotItem.setCoffeeSa(new int[]{convertToInteger(week[0][4].getText()), convertToInteger(week[0][5].getText())});
                timeSlotItem.setSunday(new int[]{convertToInteger(week[1][0].getText()), convertToInteger(week[1][1].getText())});
                timeSlotItem.setLunchSu(new int[]{convertToInteger(week[1][2].getText()), convertToInteger(week[1][3].getText())});
                timeSlotItem.setCoffeeSu(new int[]{convertToInteger(week[1][4].getText()), convertToInteger(week[1][5].getText())});
                timeSlotItem.setMonday(new int[]{convertToInteger(week[2][0].getText()), convertToInteger(week[2][1].getText())});
                timeSlotItem.setLunchMo(new int[]{convertToInteger(week[2][2].getText()), convertToInteger(week[2][3].getText())});
                timeSlotItem.setCoffeeMo(new int[]{convertToInteger(week[2][4].getText()), convertToInteger(week[2][5].getText())});
                timeSlotItem.setTuesday(new int[]{convertToInteger(week[3][0].getText()), convertToInteger(week[3][1].getText())});
                timeSlotItem.setLunchTu(new int[]{convertToInteger(week[3][2].getText()), convertToInteger(week[3][3].getText())});
                timeSlotItem.setCoffeeTu(new int[]{convertToInteger(week[3][4].getText()), convertToInteger(week[3][5].getText())});
                timeSlotItem.setWednesday(new int[]{convertToInteger(week[4][0].getText()), convertToInteger(week[4][1].getText())});
                timeSlotItem.setLunchWe(new int[]{convertToInteger(week[4][2].getText()), convertToInteger(week[4][3].getText())});
                timeSlotItem.setCoffeeWe(new int[]{convertToInteger(week[4][4].getText()), convertToInteger(week[4][5].getText())});
                timeSlotItem.setThursday(new int[]{convertToInteger(week[5][0].getText()), convertToInteger(week[5][1].getText())});
                timeSlotItem.setLunchTh(new int[]{convertToInteger(week[5][2].getText()), convertToInteger(week[5][3].getText())});
                timeSlotItem.setCoffeeTh(new int[]{convertToInteger(week[5][4].getText()), convertToInteger(week[5][5].getText())});
                timeSlotItem.setFriday(new int[]{convertToInteger(week[6][0].getText()), convertToInteger(week[6][1].getText())});
                timeSlotItem.setLunchFr(new int[]{convertToInteger(week[6][2].getText()), convertToInteger(week[6][3].getText())});
                timeSlotItem.setCoffeeFr(new int[]{convertToInteger(week[6][4].getText()), convertToInteger(week[6][5].getText())});
                break;
            default:
                break;
        }
    }

    private void setValues() {
        timeSlotItem = new TimeslotItem();
        if (objectId != null) {
            timeSlotItem.setObjectID(objectId);
        }
        timeSlotItem.setName(name.getText());
        timeSlotItem.setShortName(shortName.getText());
        timeSlotItem.setDescription(description.getText());
        timeSlotItem.setLateMinutes((lateMinutes.getValue() != null && !lateMinutes.getValue().isEmpty()) ? Integer.parseInt(lateMinutes.getValue()) : 0);
        timeSlotItem.setEarlyMinutes((earlyLeaveMinutes.getValue() != null && !earlyLeaveMinutes.getValue().isEmpty()) ? Integer.parseInt(earlyLeaveMinutes.getValue()) : 0);
        timeSlotItem.setValidInStart(validCheckInStart.getValue() != null && !validCheckInStart.getValue().isEmpty() ? convertToInteger(validCheckInStart.getValue()) : 0);
        timeSlotItem.setValidInEnd(validCheckInEnd.getValue() != null && !validCheckInEnd.getValue().isEmpty() ? convertToInteger(validCheckInEnd.getValue()) : 0);
        timeSlotItem.setValidOutStart(validCheckOutStart.getValue() != null && !validCheckOutStart.getValue().isEmpty() ? convertToInteger(validCheckOutStart.getValue()) : 0);
        timeSlotItem.setValidOutEnd(validCheckOutEnd.getValue() != null && !validCheckOutEnd.getValue().isEmpty() ? convertToInteger(validCheckOutEnd.getValue()) : 0);
        timeSlotItem.setAutoInOutEnabled(autoInOutEnabled.getValue());

        getWeekDay(settingsData.getOverallDatePickerWeekStart(), weekDays);
        timeSlotItem.setAdditionalLeaveDays(exceptionalWeekDays.getSelectedItemIds());
        timeSlotItem.setEffectiveDate(effectiveFrom.getDate());
        //set daily
        ArrayList<ExceptionalTimeSlotItem> dailyItems = new ArrayList<>();
        int i = 1;
        for (Map<String, Widget> row : dailyTimeslot.getWidgets()) {
            if (row != null) {
                dailyItems.add(getDailyItem(row, i++));
            }
        }
        timeSlotItem.setDailyItems(dailyItems);

        //set exceptional cases
        ArrayList<ExceptionalTimeSlotItem> exceptionalCasesT = new ArrayList<>();
        for (Map<String, Widget> row : exceptionalCases.getWidgets()) {
            if (row != null) {
                setExceptionalCasesT(row, exceptionalCasesT);
            }
        }
        timeSlotItem.setExceptionalCases(exceptionalCasesT);
        //set selected employees
        timeSlotItem.setSelectedEmployees(assignEmployees.getSelectedIds());
        localeItem = localeView != null ? localeView.getLocaleItem() : new ReferenceLocale();
        timeSlotItem.setReferenceLocale(localeItem);
        timeSlotItem.setHexColor(colorWidget.getColor());
        timeSlotItem.setSelectedLeaveReasons(exceptionalLeaveReasons.getSelectedItems());
    }

    private void setExceptionalCasesT(Map<String, Widget> row, ArrayList<ExceptionalTimeSlotItem> exceptionalCasesT) {
        FlexTable fte = (FlexTable) row.get(MultiTable.MULTI_TABLE);             //exceptional date table
        DatePicker wdp = (DatePicker) fte.getWidget(0, 0);  //exceptional date
        if (wdp != null && wdp.getDate() != null) {
            TextBox weekDayST = (TextBox) fte.getWidget(0, 1);                    //work time start time
            TextBox weekDayET = (TextBox) fte.getWidget(0, 2);                    //work time end time
            TextBox lunchST = (TextBox) fte.getWidget(0, 3);                      //lunch time start time
            TextBox lunchET = (TextBox) fte.getWidget(0, 4);                      //lunch time end time
            TextBox coffeeST = (TextBox) fte.getWidget(0, 5);                     //coffee break start time
            TextBox coffeeET = (TextBox) fte.getWidget(0, 6);                     //coffee break end time

            ExceptionalTimeSlotItem exTSi = new ExceptionalTimeSlotItem();
            exTSi.setName(name.getText());
            exTSi.setDescription(description.getText());
            exTSi.setExceptionalDate(new DateNonConvertable(DateUtil.resetTime(wdp.getDate())));

            exTSi.setWeekDay(new int[]{convertToInteger(weekDayST.getText()), convertToInteger(weekDayET.getText())});
            exTSi.setLunch(new int[]{convertToInteger(lunchST.getText()), convertToInteger(lunchET.getText())});
            exTSi.setCoffee(new int[]{convertToInteger(coffeeST.getText()), convertToInteger(coffeeET.getText())});

            exceptionalCasesT.add(exTSi);
        }
    }

    private ExceptionalTimeSlotItem getDailyItem(Map<String, Widget> row, int i) {
        FlexTable fte = (FlexTable) row.get(MultiTable.MULTI_TABLE);             //exceptional date table

        TextBox weekDayST = (TextBox) fte.getWidget(0, 1);                    //work time start time
        TextBox weekDayET = (TextBox) fte.getWidget(0, 2);                    //work time end time
        TextBox lunchST = (TextBox) fte.getWidget(0, 3);                      //lunch time start time
        TextBox lunchET = (TextBox) fte.getWidget(0, 4);                      //lunch time end time
        TextBox coffeeST = (TextBox) fte.getWidget(0, 5);                     //coffee break start time
        TextBox coffeeET = (TextBox) fte.getWidget(0, 6);                     //coffee break end time

        ExceptionalTimeSlotItem daily = new ExceptionalTimeSlotItem();
        daily.setDayNo(i);

        daily.setWeekDay(new int[]{convertToInteger(weekDayST.getText()), convertToInteger(weekDayET.getText())});
        daily.setLunch(new int[]{convertToInteger(lunchST.getText()), convertToInteger(lunchET.getText())});
        daily.setCoffee(new int[]{convertToInteger(coffeeST.getText()), convertToInteger(coffeeET.getText())});

        return daily;

    }

    private void update() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        setValues();
        Info.show(hrmsStrings.updatingTimeslotWait());
        LoadingPanel.loading(true);
        AvailabilityService.App.get().updateTimeslot(timeSlotItem, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void object) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.timeslot()), Info.Type.INFO);
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMESLOT_EDIT, object, AddTimeslotView.this);
            }
        });
    }

    private boolean validate() {
        clearErrorStyle();
        int errors = 0;
        errors += markAsError(CustomFormConstants.NAME, name, !Validation.validateTextBoxRequired(name));
//        errors += markAsError(CustomFormConstants.DESCRIPTION, description, !Validation.validateTextAreaRequired(description));

        if (!checkTimeSlot(weekDays)) {
            errors++;
        }
//        errors += validateExceptionalWeekDays();
        HashMap<Date, DatePicker> exceptionalDates = new HashMap<>();
        ArrayList<DatePicker> existExceptionalDates = new ArrayList<>();
        HashMap<Date, TextBox[][]> exceptionalCaseTimeEntriesT = new LinkedHashMap<>();
        for (Map<String, Widget> row : exceptionalCases.getWidgets()) {
            if (row != null) {
                FlexTable fte = (FlexTable) row.get(MultiTable.MULTI_TABLE);             //exceptional date table
                DatePicker wdp = (DatePicker) /*row.get(MultiTable.DATE_PICKER)*/fte.getWidget(0, 0);  //exceptional date
                //
                TextBox weekDayST = (TextBox) fte.getWidget(0, 1);                    //work time start time
                TextBox weekDayET = (TextBox) fte.getWidget(0, 2);                    //work time end time
                TextBox lunchST = (TextBox) fte.getWidget(0, 3);                      //lunch time start time
                TextBox lunchET = (TextBox) fte.getWidget(0, 4);                      //lunch time end time
                TextBox coffeeST = (TextBox) fte.getWidget(0, 5);                     //coffee break start time
                TextBox coffeeET = (TextBox) fte.getWidget(0, 6);                     //coffee break end time

                if (wdp != null && wdp.getDate() != null) {
                    DateUtil.resetTime(wdp.getDate());
                    if (exceptionalDates.containsKey(wdp.getDate())) {
                        existExceptionalDates.add(wdp);
                    }
                    exceptionalDates.put(wdp.getDate(), wdp);
                    //
                    if (!existExceptionalDates.contains(wdp)) {
                        TextBox[] timeEntries = new TextBox[]{weekDayST, weekDayET, lunchST, lunchET, coffeeST, coffeeET};
                        exceptionalCaseTimeEntriesT.computeIfAbsent(wdp.getDate(), k -> new TextBox[][]{timeEntries});
                    }
                }
            }
        }
        if (existExceptionalDates.size() > 0) {
            for (DatePicker wdpR : existExceptionalDates) {
                errors += markAsError(wdpR, true);
            }
        }
        if (exceptionalCaseTimeEntriesT.size() > 0) {
            for (TextBox[][] textBoxes : exceptionalCaseTimeEntriesT.values()) {
                if (!checkTimeSlot(textBoxes)) {
                    errors++;
                }
            }
        }
//------------------------------------------------------------------------------//
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private int validateExceptionalWeekDays() {
        int err = 0;
        if (exceptionalWeekDays.getSelectedItems().size() == 0) {
            markAsError(exceptionalWeekDays, true);
            err += 1;
        }
        if (exceptionalLeaveReasons.getSelectedItems().size() == 0) {
            markAsError(exceptionalLeaveReasons, true);
            err += 1;
        }
        return err;
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
