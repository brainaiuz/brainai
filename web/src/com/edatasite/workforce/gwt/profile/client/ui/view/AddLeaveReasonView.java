package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.LeaveReasonLocationDaysItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.Gender;
import com.edatasite.workforce.gwt.core.client.enums.LeaveReasonType;
import com.edatasite.workforce.gwt.core.client.enums.TypeOption;
import com.edatasite.workforce.gwt.core.client.enums.UnitType;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiAppendedRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ColorWidget;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.view.AddEditLocaleView;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.availability.client.ui.view.AttendanceReport.hrmsStrings;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_PRIMARY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.MAX_DEFAULT_WIDTH;

/**
 * @author Hurshid on 12/17/2018
 */
public class AddLeaveReasonView extends CustomForm implements Colapse {

    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final Integer objectID;
    private final boolean isSummary;
    private TextBox name;
    private WfmButton2 locale;

    private FlexTable localedNameBox;
    private ReferenceLocale localeItem;
    private AddEditLocaleView localeView;
    private TextBox leaveDays;
    private TextBox probationDays;
    private TextArea2 description;
    private KpiCheckBox leaveToAll;
    private KpiCheckBox probAll;
    private ReasonItem item;
    private VerticalPanel allowancPanel;
    private TextBox shortName;
    private KpiCheckBox prorataValidation;
    private KpiSwitcher attendancelr;
    private KpiSwitcher autoApprove;
    private KpiSwitcher markAsDraft;
    private HorizontalPanel lrTable;
    private DatePicker openingBalanceDate;

    private KpiSelect2 departmentList;
    private KpiSelect2 positionList;
    private KpiSelect2 locationList;
    private KpiSelect2 roleList;
    private MultiSelectEmployeeLookUp employeeLookUp;
    private DataListBox genderList;
    private DataListBox leaveUnit;
    private VerticalPanelDiv typeOptions;
    private KpiRadioButton notAllow, allowAsPaid, allowAsNonPaid;
    private KpiAppendedRadioButton active;
    private KpiAppendedRadioButton exceptionalTimeSlot;
    private KpiAppendedRadioButton includeDayOffs;
    private ColorWidget colorWidget;
    private TextBox duration;
    private DatePicker effectiveFrom;
    private TextBox minLeaveDays;
    private MaterialLink logHistroyTab;
    private FlexTable tableHeader;
    private FlexTable dataTable;
    private TextBox integrationCode;
    private TextBox redirectUrl;

    private FlexTable locationDaysTable;
    private VerticalPanel locationDaysPanel;
    private List<LocationDaysRow> locationDaysRows = new ArrayList<>();
    private List<SelectItem> allLocations = new ArrayList<>();

    private static class LocationDaysRow {
        DataListBox locationBox;
        TextBox leaveDaysBox;
        DatePicker effectiveFromPicker;
        KpiCheckBox applyToAllBox;
    }

    public AddLeaveReasonView(Integer id, boolean isSummary) {
        super("", isSummary ? wfmStrings.summaryView() : wfmStrings.edit() + " " + wfmStrings.reason());
        this.objectID = id;
        this.isSummary = isSummary;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.LEAVE_REASON_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
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
        tableHeader = new FlexTable();
        tableHeader.setStyleName("headerTable");
        setTableCellFormats(tableHeader);

        tableHeader.setHTML(0, 0, wfmStrings.leaveAllowance());
        tableHeader.setHTML(0, 1, wfmStrings.effectiveDate());
        tableHeader.setHTML(0, 2, wfmStrings.modifiedBy());
        tableHeader.setHTML(0, 3, wfmStrings.action());
        tableHeader.setHTML(0, 4, wfmStrings.history());

        dataTable = new FlexTable();
        dataTable.setStyleName("dataTable");
        setTableCellFormats(dataTable);

        name = new TextBox();
        // locale
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
        description = new TextArea2(wfmStrings.description());

        active = new KpiAppendedRadioButton();

        exceptionalTimeSlot = new KpiAppendedRadioButton();

        leaveToAll = new KpiCheckBox(settingsStrings.allowancedaysForAllEmployess());
        probAll = new KpiCheckBox(settingsStrings.probationDaysForAllEmployes());

        leaveDays = new TextBox();
        Validation.addNumericKeyboardListener(leaveDays, 2);

        probationDays = new TextBox();
        Validation.addNumericKeyboardListener(probationDays, 2);
        prorataValidation = new KpiCheckBox(wfmStrings.proRataBased());

        allowancPanel = new VerticalPanel();

        includeDayOffs = new KpiAppendedRadioButton(wfmStrings.countAsLeave(), wfmStrings.dontCountAsLeave());

        shortName = new TextBox();
        shortName.setMaxLength(3);

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(shortName);
        new KpiToolTip(shortName, settingsStrings.shortNameInformation());
        horizontalPanel.add(shortName);

        attendancelr = new KpiSwitcher();
        attendancelr.setOnLabel(settingsStrings.quickAddLR());
        new KpiToolTip(attendancelr, settingsStrings.quickLeaveRquest());

        autoApprove = new KpiSwitcher();
        autoApprove.setOnLabel(settingsStrings.automaticallyApprove());
        new KpiToolTip(autoApprove, settingsStrings.autoAddLRWithoutApproval());

        lrTable = new HorizontalPanel();
        lrTable.addStyleName(DEFAULT_WIDTH);
        lrTable.add(attendancelr);
        lrTable.add(autoApprove);

        markAsDraft = new KpiSwitcher();
        markAsDraft.setOnLabel(settingsStrings.makeAsDraft());
        lrTable.add(markAsDraft);
        lrTable.setCellWidth(attendancelr, "33%");
        lrTable.setCellWidth(autoApprove, "33%");
        lrTable.setCellWidth(markAsDraft, "33%");

        openingBalanceDate = new DatePicker();

        //Applicable items
        departmentList = new KpiSelect2(true);
        positionList = new KpiSelect2(true);
        locationList = new KpiSelect2(true);
        roleList = new KpiSelect2(true);

        employeeLookUp = new MultiSelectEmployeeLookUp();
        employeeLookUp.getFilterParametrs().setHRMS(true);

        genderList = new DataListBox();
        SelectItem[] genders = new SelectItem[Gender.values().length];
        for (Gender gender : Gender.values()) {
            genders[gender.ordinal()] = new SelectItem(gender.ordinal(), gender.getName());
        }
        for (int i = 0; i < genders.length; i++) {
            if (genders[i].getName().equals("Male"))
                genders[i].setName(wfmStrings.male());
            else if (genders[i].getName().equals("Female"))
                genders[i].setName(wfmStrings.female());
            else if (genders[i].getName().equals("Other"))
                genders[i].setName(wfmStrings.other());
        }
        genderList.setItems(genders);

        leaveUnit = new DataListBox();
        leaveUnit.setWithoutNullLabel(true);
        SelectItem[] unitItems = new SelectItem[UnitType.values().length];
        for (UnitType unitType : UnitType.values()) {
            unitItems[unitType.ordinal()] = new SelectItem(unitType.ordinal(), unitType.getName());
        }

        for (int i = 0; i < unitItems.length; i++) {
            if (unitItems[i].getName().equals("Daily"))
                unitItems[i].setName(wfmStrings.daily());
            else if (unitItems[i].getName().equals("Hourly"))
                unitItems[i].setName(wfmStrings.hourly());
        }

        leaveUnit.setItems(unitItems);

        notAllow = new KpiRadioButton("typeOption", settingsStrings.doNotAllow());
        notAllow.setValue(true);
        allowAsPaid = new KpiRadioButton("typeOption", settingsStrings.allowAsPaid());
        allowAsNonPaid = new KpiRadioButton("typeOption", settingsStrings.allowAsNonPaid());
        typeOptions = new VerticalPanelDiv();
        typeOptions.add(notAllow);
        typeOptions.add(allowAsPaid);
        typeOptions.add(allowAsNonPaid);

        colorWidget = new ColorWidget();
        colorWidget.setWidth("295px");

        duration = new TextBox();
        Validation.addNumericKeyboardListener(duration, 0);

        effectiveFrom = new DatePicker();
        effectiveFrom.setWidth(MAX_DEFAULT_WIDTH);
        effectiveFrom.setDate(new Date());
        effectiveFrom.addChangeHandler(changeEvent -> {
            if (objectID != null) {
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
        });

        minLeaveDays = new TextBox();
        Validation.addNumericKeyboardListener(minLeaveDays, 0);

        logHistroyTab = new MaterialLink(wfmStrings.logHistory());
        logHistroyTab.ensureDebugId("logHistory");
        logHistroyTab.addClickHandler(clickEvent -> {
            if (objectID != null) {
                fillDataTable();
            }
        });

        integrationCode = new TextBox();
        redirectUrl = new TextBox();

        locationDaysTable = new FlexTable();
        locationDaysTable.setStyleName("dataTable");

        locationDaysPanel = new VerticalPanel();
        locationDaysPanel.addStyleName(DEFAULT_WIDTH);

        WfmButton2 addRowBtn = new WfmButton2("+ " + wfmStrings.locations());
        addRowBtn.setStyleName(BTN_PRIMARY);
        addRowBtn.addClickHandler(event -> addLocationDaysRow(null, null, null, false));

        locationDaysPanel.add(locationDaysTable);
        locationDaysPanel.add(addRowBtn);

        addTitleField(DETAILS, getTitle(settingsStrings.leaveReasonDetails()));
        addTitleField(REASON_APPLICABLE_FOR, getTitle(settingsStrings.reasonApplicableFor()));
        addTitleField(LEAVE_ENTITLEMENT, getTitle(settingsStrings.leaveEntitlement()));
        addField(DEPARTMENT, departmentList, getTitle(Property.getPluralWithObjectCode(Constants.DEPARTMENT_LIST, wfmStrings.departments())));
        addField(POSITION, positionList, getTitle(wfmStrings.positions()));
        addField(LOCATIONS, locationList, getTitle(wfmStrings.locations()));
        addField(ROLE, roleList, getTitle(wfmStrings.roles()));
        addField(GENDER, genderList, getTitle(wfmStrings.gender()));
        addField(EMPLOYEES, employeeLookUp, getTitle(wfmStrings.employees()));
        addField(STATUS, active, getTitle(wfmStrings.active()));

        addField(CustomFormConstants.DATE_FIELD, effectiveFrom, getTitle(wfmStrings.effectiveDate(), false));
        addField(NAME, localedNameBox, wfmStrings.name());
        addField(DESCRIPTION, description, null);
        addField(LEAVE_UNIT, leaveUnit, settingsStrings.leaveUnit());
        addField(TYPE_OPTION, typeOptions, settingsStrings.whileApplyingLeaveExceedLeaveBalance());

        addField(DAYOFF_INCLUDED, includeDayOffs, getTitle(settingsStrings.dayOffsBetweenLeavePeriod()));
        addField(SHORT_NAME, horizontalPanel, getTitle(wfmStrings.shortName(), true));
        addField(COLOR_PICKER, colorWidget, getTitle(wfmStrings.color()));
        addField(EXCEPTIONAL_WORKINGDAY_TIMESLOT, exceptionalTimeSlot, getTitle(settingsStrings.countFromExceptionalTimeSlot()));
        addField(DURATION, duration, getTitle(wfmStrings.duration()));
        addField(MIN_LEAVE_DAYS, minLeaveDays, logHistroyTab, wfmStrings.minValue(), true);
        addField(INTEGRATION_CODE, integrationCode, getTitle(settingsStrings.integrationCode()));
        addField(REDIRECT_URL, redirectUrl, getTitle(settingsStrings.redirectUrl()));
        show();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> save());
    }

    @Override
    protected void getDataToFillFields() {
        service.getReason(objectID, new AbstractAsyncCallback<ReasonItem>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ReasonItem result) {
                item = result == null ? new ReasonItem() : result;
                if (objectID == null) {
                    Document.get().getElementById("summary_log").removeFromParent();
                }
                fillFieldsWithData();
                localeItem = result != null ? result.getLocaleItem() : new ReferenceLocale();
            }
        });

        service.getSelectOptions(objectID, new AbstractAsyncCallback<HashMap<LeaveReasonType, ArrayList<SelectItem>>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<LeaveReasonType, ArrayList<SelectItem>> result) {
                departmentList.setItems(result.get(LeaveReasonType.DEPARTMENT));
                positionList.setItems(result.get(LeaveReasonType.POSITION));
                for (int i = 0; i < result.get(LeaveReasonType.ROLE).size(); i++) {
                    SelectItem role = result.get(LeaveReasonType.ROLE).get(i);
                    if ("Administrator".equals(role.getName()))
                        role.setName(wfmStrings.administrator());
                    else if ("Member".equals(role.getName()))
                        role.setName(wfmStrings.member());
                    else if ("Director".equals(role.getName()))
                        role.setName(wfmStrings.director());
                    else if ("HR Manager".equals(role.getName()))
                        role.setName(wfmStrings.hrManager());
                    else if ("Accountant".equals(role.getName()))
                        role.setName(wfmStrings.accountant());
                    else if ("Sales Manager".equals(role.getName()))
                        role.setName(wfmStrings.salesManager());
                    else if ("Sales Person".equals(role.getName()))
                        role.setName(wfmStrings.salesPerson());
                    else if ("Department Leader".equals(role.getName()))
                        role.setName(wfmStrings.departmentLeader());
                    else if ("Project Manager".equals(role.getName()))
                        role.setName(wfmStrings.projectManager());
                }
                locationList.setItems(result.get(LeaveReasonType.LOCATION));
                allLocations = result.get(LeaveReasonType.LOCATION) != null
                        ? result.get(LeaveReasonType.LOCATION)
                        : new ArrayList<>();
                if (item != null) {
                    rebuildLocationDaysRows();
                }
                roleList.setItems(result.get(LeaveReasonType.ROLE));
                if (result.get(LeaveReasonType.EMPLOYEE) != null) {
                    employeeLookUp.setSelectedItems(result.get(LeaveReasonType.EMPLOYEE));
                }
            }
        });
    }

    private void fillFieldsWithData() {
        name.setText(item.getName());
        description.setText(item.getDescription());

        active.setActive(objectID == null || item.isActive());
        exceptionalTimeSlot.setActive(item.isExceptionalWorkingDay());
        leaveDays.setValue(!"0.0".equals(item.getLeaveDaysOld() + "") ? String.valueOf(item.getLeaveDaysOld()) : "");
        probationDays.setValue(!"0.0".equals(item.getProbationDays() + "") ? String.valueOf(item.getProbationDays()) : "");
        leaveToAll.setValue(item.isLeaveToAll());
        probAll.setValue(item.isProbationToAll());
        includeDayOffs.setActive(item.isIncludeDayOffs());
        shortName.setValue(item.getShortName());
        duration.setText(item.getDuration() != null ? String.valueOf(item.getDuration().intValue()) : "");
        if (item.getGender() != null) {
            genderList.setSelected(new SelectItem(item.getGender().ordinal(), item.getGender().getName()));
        }
        if (item.getUnitType() != null) {
            leaveUnit.setSelected(new SelectItem(item.getUnitType().ordinal(), item.getUnitType().getName()));
        }
//        if (!Constants.LR_TYPE_UNAUTHORIZED_LEAVE.equals(item.getCode())) {
        if (CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(item.getCode())) {
            allowancPanel.add(leaveDays);
            allowancPanel.add(prorataValidation);
        } else {
            allowancPanel.add(leaveDays);
        }
        allowancPanel.add(leaveToAll);
        if (CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(item.getCode())) {
            allowancPanel.add(new HTML("&nbsp;"));
            allowancPanel.add(probationDays);
            allowancPanel.add(probAll);

            openingBalanceDate.setDate(item.getOpeningBalanceDate() != null ? item.getOpeningBalanceDate().getNonConvertedDate() : null);
            addField(CustomFormConstants.OPENING_BALANCE_DATE, openingBalanceDate, getTitle(wfmStrings.openingBalanceDate()));
        }
        addField(POSITIONS.LEAVE_ALLOUNCE_PANEL, allowancPanel, wfmStrings.leaveDays() + ":");
        addField(CustomFormConstants.LEAVE_REASON_LOCATION_DAYS_PANEL, locationDaysPanel, settingsStrings.leaveDaysByLocation() + ":");
        if (!allLocations.isEmpty()) {
            rebuildLocationDaysRows();
        }
        if (item.getId() != null) {
            attendancelr.setValue(item.isAttendanceLR());
            autoApprove.setValue(item.isAutoApprove());
            autoApprove.setVisible(true);
        }
//        } else {
//            attendancelr.setValue(true);
//            attendancelr.setEnabled(false);
//            autoApprove.setValue(true);
//            autoApprove.setEnabled(false);
//        }
        if (!CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(item.getCode())) {
            addField(ATTENDANCE_LR, lrTable, null);
        }
        if (item.isHasProrata()) {
            prorataValidation.setValue(true);
        }

        if (item.getTypeOption() != null) {
            if (TypeOption.NOT_ALLOW_EXCEED_ALLOWANCE.equals(item.getTypeOption())) {
                notAllow.setValue(true);
            } else if (TypeOption.ALLOW_AS_PAID.equals(item.getTypeOption())) {
                allowAsPaid.setValue(true);
            } else if (TypeOption.ALLOW_AS_NON_PAID.equals(item.getTypeOption())) {
                allowAsNonPaid.setValue(true);
            }
        }

        if (item.getHexColor() != null && item.getHexColor().length() > 0)
            colorWidget.setColor(item.getHexColor());

        if (item.getEffectiveFrom() != null) {
            effectiveFrom.setDate(item.getEffectiveFrom().getDate());
        }
        if (item.getMinLeaveDays() != null) {
            minLeaveDays.setValue(item.getMinLeaveDays().toString());
        }
        integrationCode.setText(item.getIntegrationCode());
        redirectUrl.setText(item.getRedirectUrl());
        markAsDraft.setValue(item.isMarkAsDraft());
    }

    private void addLocationDaysRow(Integer locationId, Double leaveDays, Date effectiveDate, boolean applyToAll) {
        final LocationDaysRow row = new LocationDaysRow();

        row.locationBox = new DataListBox();
        if (!allLocations.isEmpty()) {
            row.locationBox.setItems(allLocations.toArray(new SelectItem[0]));
            if (locationId != null) {
                for (SelectItem loc : allLocations) {
                    if (locationId.equals(loc.getId())) {
                        row.locationBox.setSelected(loc);
                        break;
                    }
                }
            }
        }
        row.locationBox.getElement().getStyle().setWidth(160, Style.Unit.PX);

        row.leaveDaysBox = new TextBox();
        Validation.addNumericKeyboardListener(row.leaveDaysBox, 2);
        if (leaveDays != null) {
            row.leaveDaysBox.setValue(!"0.0".equals(leaveDays + "") ? String.valueOf(leaveDays) : "");
        }
        row.leaveDaysBox.getElement().getStyle().setWidth(80, Style.Unit.PX);

        row.effectiveFromPicker = new DatePicker();
        row.effectiveFromPicker.setWidth("130px");
        if (effectiveDate != null) {
            row.effectiveFromPicker.setDate(effectiveDate);
        } else {
            row.effectiveFromPicker.setDate(new Date());
        }

        row.applyToAllBox = new KpiCheckBox(settingsStrings.allowancedaysForAllEmployess());
        row.applyToAllBox.setValue(applyToAll);

        Icon removeIcon = new Icon();
        removeIcon.setStyleName("ficon--trash");
        removeIcon.setTooltip(wfmStrings.delete());
        removeIcon.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        int rowIndex = locationDaysRows.size();
        locationDaysRows.add(row);

        locationDaysTable.setWidget(rowIndex, 0, row.locationBox);
        locationDaysTable.setWidget(rowIndex, 1, row.leaveDaysBox);
        locationDaysTable.setWidget(rowIndex, 2, row.effectiveFromPicker);
        locationDaysTable.setWidget(rowIndex, 3, row.applyToAllBox);
        locationDaysTable.setWidget(rowIndex, 4, removeIcon);

        removeIcon.addClickHandler(click -> {
            int idx = locationDaysRows.indexOf(row);
            if (idx >= 0) {
                locationDaysRows.remove(idx);
                locationDaysTable.removeRow(idx);
            }
        });
    }

    private void rebuildLocationDaysRows() {
        locationDaysTable.removeAllRows();
        locationDaysRows.clear();
        if (item != null && item.getLocationDaysList() != null) {
            for (LeaveReasonLocationDaysItem ldItem : item.getLocationDaysList()) {
                addLocationDaysRow(
                        ldItem.getLocationId(),
                        ldItem.getLeaveDays(),
                        ldItem.getEffectiveFrom() != null ? ldItem.getEffectiveFrom().getDate() : null,
                        ldItem.isApplyToAll()
                );
            }
        }
    }

    private void setTableCellFormats(FlexTable table) {
        HTMLTable.CellFormatter cellFormatter = table.getCellFormatter();
        cellFormatter.setWidth(0, 0, "82px");
        cellFormatter.setWidth(0, 1, "150px");
        cellFormatter.setWidth(0, 2, "150px");
        cellFormatter.setWidth(0, 3, "80px");
        cellFormatter.setVisible(0, 4, false);
    }

    private void fillDataTable() {
        service.getReasonItemsByReasonId(objectID, new AsyncCallback<ArrayList<ReasonItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.show(wfmStrings.thereAreNoItemsToShow(), Info.Type.INFO);
            }

            @Override
            public void onSuccess(ArrayList<ReasonItem> reasonItems) {
                if (reasonItems.size() > 0) {
                    drawDataTable(reasonItems);
                    getReasonHistoryModal();
                } else {
                    Info.show(wfmStrings.thereAreNoItemsToShow(), Info.Type.WARNING);
                }
            }
        });
    }

    private void deleteHistory(Integer historyID) {
        service.deleteLeaveReasonHistoryById(historyID, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Void unused) {
                fillDataTable();
            }
        });
    }

    private void drawDataTable(List<ReasonItem> reasonItems) {
        dataTable.removeAllRows();
        HTMLTable.CellFormatter cellFormatter = dataTable.getCellFormatter();
        int i = 0;
        for (ReasonItem reasonItem : reasonItems) {
            Icon removeIcon = new Icon();
            removeIcon.setStyleName("ficon--trash");
            removeIcon.setTooltip(wfmStrings.delete());
            removeIcon.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            removeIcon.addClickHandler(click -> {
                deleteHistory(reasonItem.getId());
            });

            dataTable.setHTML(i, 0, reasonItem.getLeaveDaysNew().toString());
            cellFormatter.setWidth(i, 0, "82px");
            dataTable.setHTML(i, 1, reasonItem.getEffectiveDate());
            cellFormatter.setWidth(i, 1, "150px");
            dataTable.setHTML(i, 2, reasonItem.getModifiedBy() != null ? reasonItem.getModifiedBy() : "");
            cellFormatter.setWidth(i, 2, "150px");
            dataTable.setWidget(i, 3, removeIcon);
            cellFormatter.setWidth(i, 3, "80px");
            dataTable.setHTML(i, 4, reasonItem.getId().toString());
            cellFormatter.setVisible(i, 4, false);

            i++;
        }
    }

    private void getReasonHistoryModal() {
        KpiModal modal = new KpiModal();
        modal.setTitle(wfmStrings.logHistory());
        Div dataContainer = new Div();
        dataContainer.setStyleName("dataWrapper");
        Div headerContainer = new Div();
        headerContainer.setStyleName("headerWrapper");
        headerContainer.setStyle("border: 0.01rem solid #C4D0DA; border-radius: 3px;");

        WfmButton2 close = new WfmButton2(wfmStrings.close());
        close.setStyleName(BTN_PRIMARY);
        close.addClickHandler(click -> modal.close());

        headerContainer.add(tableHeader);
        dataContainer.add(dataTable);
        modal.add(headerContainer);
        modal.add(dataContainer);
        modal.addCloseHandler(handler -> modal.close());
        modal.addButton(close);
        modal.open();
    }


    private boolean validate() {
        int result = 0;
        if (!Validation.validateTextBoxRequired(shortName)) {
            result++;
        }
        if (result > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void prepareToSave() {
        item = item == null ? new ReasonItem() : item;
        item.setName(name.getText());
        item.setDescription(description.getText());
        item.setActive(active.isActive());
        item.setLeaveDaysNew((leaveDays.getValue() != null && !leaveDays.getValue().isEmpty()) ? Double.valueOf(leaveDays.getValue()) : null);
        item.setProbationDays((probationDays.getValue() != null && !probationDays.getValue().isEmpty()) ? Double.valueOf(probationDays.getValue()) : null);
        item.setLeaveToAll(leaveToAll.getValue());
        item.setProbationToAll(probAll.getValue());
        item.setIncludeDayOffs(includeDayOffs.isActive());
        item.setExceptionalWorkingDay(exceptionalTimeSlot.isActive());
        item.setShortName(shortName.getValue());
        item.addRelationType(LeaveReasonType.DEPARTMENT, departmentList.getSelectedItems());
        item.addRelationType(LeaveReasonType.POSITION, positionList.getSelectedItems());
        item.addRelationType(LeaveReasonType.LOCATION, locationList.getSelectedItems());
        item.addRelationType(LeaveReasonType.ROLE, roleList.getSelectedItems());
        item.addRelationType(LeaveReasonType.EMPLOYEE, employeeLookUp.getSelectedItems());
        item.setGender(genderList.getSelectedItem() != null ? Gender.values()[genderList.getSelectedId()] : null);
        item.setUnitType(leaveUnit.getSelectedItem() != null ? UnitType.values()[leaveUnit.getSelectedId()] : null);
        item.setHexColor(colorWidget.getColor());
        item.setDuration(duration.getText() != null && !"".equals(duration.getText()) ? new BigDecimal(duration.getText()) : null);
        localeItem = localeView != null ? localeView.getLocaleItem() : new ReferenceLocale();
        item.setReferenceLocale(localeItem);
        item.setEffectiveFrom(new DateNonConvertable(effectiveFrom.getDate()));
        item.setMinLeaveDays(minLeaveDays.getText() != null && !"".equals(minLeaveDays.getText()) ? Integer.parseInt(minLeaveDays.getText()) : null);

        if (attendancelr != null) {
            item.setAttendanceLR(attendancelr.getValue());
        }
        if (autoApprove != null) {
            item.setAutoApprove(autoApprove.getValue());
        }
        if (notAllow.getValue()) {
            item.setTypeOption(TypeOption.NOT_ALLOW_EXCEED_ALLOWANCE);
        }
        if (allowAsPaid.getValue()) {
            item.setTypeOption(TypeOption.ALLOW_AS_PAID);
        }
        if (allowAsNonPaid.getValue()) {
            item.setTypeOption(TypeOption.ALLOW_AS_NON_PAID);
        }
        if (CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(item.getCode())) {
            item.setOpeningBalanceDate(null);
            if (openingBalanceDate.getDate() != null) {
                item.setOpeningBalanceDate(new DateNonConvertable(openingBalanceDate.getDate()));
            }
        }
        item.setHasProrata(prorataValidation.getValue());
        item.setIntegrationCode(integrationCode.getText());
        item.setRedirectUrl(redirectUrl.getText());
        item.setMarkAsDraft(markAsDraft.getValue());

        final List<LeaveReasonLocationDaysItem> locationDaysList = new ArrayList<>();
        for (LocationDaysRow row : locationDaysRows) {
            SelectItem selectedLoc = row.locationBox.getSelectedItem();
            if (selectedLoc == null || selectedLoc.getId() == null) {
                continue;
            }
            final LeaveReasonLocationDaysItem ldItem = new LeaveReasonLocationDaysItem();
            ldItem.setLocationId(selectedLoc.getId());
            ldItem.setLocationName(selectedLoc.getName());
            final String daysVal = row.leaveDaysBox.getValue();
            ldItem.setLeaveDays(daysVal != null && !daysVal.isEmpty() ? Double.valueOf(daysVal) : null);
            ldItem.setApplyToAll(row.applyToAllBox.getValue());
            ldItem.setEffectiveFrom(row.effectiveFromPicker.getDate() != null
                    ? new DateNonConvertable(row.effectiveFromPicker.getDate()) : null);
            locationDaysList.add(ldItem);
        }
        item.setLocationDaysList(locationDaysList);
    }

    private void save() {
        if (!validate()) {
            return;
        }
        enableButton(false);
        prepareToSave();
        LoadingPanel.loading(true);
        service.saveLeaveReason(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                super.onFailure(caught);
                enableButton(true);
                closeTab();
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                enableButton(true);
                if (ReasonItem.NAME_EXISTS.equals(result)) {
                    Info.warn("Reason name is exist in the system");
                } else if (ReasonItem.SHORT_NAME_EXISTS.equals(result)) {
                    Info.warn("Short name is exist in the system");
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.changes()));
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVE_REASON_UPDATE, null, AddLeaveReasonView.this);
                    closeTab();
                }
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
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
