package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.BrigadaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TimeSlotShortNameLookUp;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.view.ExtendedHTML;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItems;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftTeamsItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellLabel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SHIFT_APPROVED;


public class ShiftSummaryView extends CustomForm2 implements Colapse {
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final Integer objectId;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private EditableTable brigadaEmployeesTable;
    private ShiftItem data;
    //    private FlexTable monthPicker;
    private SplitButton printPdfSplitButton;
    private HTML approvers, manager, backUpManager, owner, type,department,monthPicker,brigada;
    private String statusCode;
    private WfmButton2 submitButton, approveButton, declineButton, editButton;
    private int monthMaxDay = 0;
    private FormHasCustomField customFieldUtil;
    private HashMap<Integer, ArrayList<SelectItem>> teamsEmployees;
    private boolean isCustom = false;
    private String periodType = "month";

    public ShiftSummaryView(Integer objectId) {
        super("summaryShift", "shiftSummary");
        this.objectId = objectId;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.ShiftList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (getCustomFieldUtil() != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                }
                ShiftSummaryView.super.onInitialize();
            }
        });
        return null;
    }


    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    protected void addButtons() {

        approveButton = addButton(wfmStrings.approve(), WfmButton2.BTN_SUCCESS, clickEvent -> save(SHIFT_APPROVED));
        approveButton.setVisible(false);

        declineButton = addButton(wfmStrings.reject(), WfmButton2.BTN_REJECT, clickEvent -> save(Constants.SHIFT_REJECTED));
        declineButton.setVisible(false);

        submitButton = addButton(Constants.SHIFT_REJECTED.equals(statusCode) ? wfmStrings.resubmitForApproval() : wfmStrings.submitForApproval(), wfmStrings.submitForApproval(), Constants.BTN_DEFAULT_OUTLINE, clickEvent -> {
            submitButton.setEnabled(false);
            save(Constants.SHIFT_SUBMITTED);
        });
        submitButton.setVisible(false);

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        if (Utils.hasPermission(PermissionConstants.HRMS_SHIFT_PDF)) {
            addRightButton(printPdfSplitButton);
        }

        editButton = addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            closeTab();
            String suffix = "week".equals(periodType) ? "/week/true" : "";
            SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/edit/" + data.getId() + suffix);
        });
        editButton.setVisible(false);
    }

    private void save(String statusCode) {
        data.setStatusCode(statusCode);
        LoadingPanel.loading(true);
        HrmsService.App.get().updateApprove(data, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void objectId) {
                closeTab();
                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    protected void getDataToFillFields() {
        if (objectId == null) {
            return;
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(objectId);
        HrmsService.App.get().getShiftItem(objectId, true, new AsyncCallback<ShiftItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ShiftItem result) {
                data = result;
                if (result.getPeriodType() != null) {
                    periodType = result.getPeriodType();
                }
                if ("week".equals(periodType)) {
                    monthMaxDay = getISOWeeksInYear(result.getPeriod().getYear() + 1900);
                } else {
                    isCustom = result.getEndDate() != null;
                    if (data.getEndDate() == null) {
                        monthMaxDay = DateUtil.countDays(result.getPeriod());
                    } else {
                        monthMaxDay = DateUtil.countDays(result.getPeriod(), result.getEndDate().getDate());
                    }
                }

                setDataToFields();
                initButtons();
                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems(), true);
                pdfTool(result);
            }
        });


    }

    private void initButtons() {
        if (data.isApprover()) {
            Integer currentApproverId = data.getApproverEmployee() != null ? data.getApproverEmployee().getId() : null;
            Integer currentUserId = Utils.getUserID();
            if (Constants.SHIFT_SUBMITTED.equals(statusCode) && currentUserId.equals(currentApproverId)) {
                approveButton.setVisible(true);
                declineButton.setVisible(true);
                submitButton.setVisible(false);
            }
            editButton.setVisible(
                    Utils.hasPermission(PermissionConstants.HRMS_SHIFT_EDIT)
                            && !(SHIFT_APPROVED.equals(statusCode)) && (currentUserId.equals(data.getCreator().getId())));


            if (Constants.SHIFT_REJECTED.equals(statusCode) && data.getCreator() != null && currentUserId.equals(data.getCreator().getId())) {
                submitButton.setVisible(true);
            }
        } else {
            editButton.setVisible(Utils.hasPermission(PermissionConstants.HRMS_SHIFT_EDIT)
                    && !(SHIFT_APPROVED.equals(statusCode)));

            if (Constants.SHIFT_SUBMITTED.equals(statusCode) || Constants.SHIFT_DRAFT.equals(statusCode)) {
                approveButton.setVisible(Utils.hasPermission(PermissionConstants.HRMS_SHIFT_APPROVE));
            }
        }

    }

    public void pdfTool(ShiftItem result) {
        if (printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        LeaveRequestObject requestObject = new LeaveRequestObject(objectId);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("templateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        if (data.getPeriod() != null) {
            parameters.put("startDate_nc", String.valueOf(data.getPeriod().getTime()));
        }
        parameters.put("SHIFT_TYPE", data.getLookUpType().equals(LookUpConstants.BRIGADA_ID) ? "TEAM" : "DUTY");
        String pdfURL = CommandConstants.PDF_URL + "/shiftViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }


    private void setDataToFields() {
        EditableTable editableTable = new EditableTable(getColumns(false), false, false);
        editableTable.addStyleName("brigada_table");
        editableTable.addStyleName("tbl-StickyHead brigada_table_summary_view");

        EditableTable competenciesTable = new EditableTable(getColumns(true),false,false);
        competenciesTable.addStyleName("brigada_table");
        competenciesTable.addStyleName("tbl-StickyHead brigada_table_summary_view");

        GRow firstRow = new GRow(new GColumn(GColumnEnum.COL_12, new FormGroup("", getPeriodAsLink())));
//        monthPicker.setWidget(0, 0, firstRow);
        addField(SHIFT, editableTable, null);
        if (SHIFT_APPROVED.equals(data.getStatusCode())) {
            addField(COMPETENCIES, competenciesTable, null);
        }
        editableTable.removeAllRows();
        competenciesTable.removeAllRows();
        if (data != null && data.getShiftItems() != null) {
            for (ShiftItems shiftItem : data.getShiftItems()) {
                editableTable.addRow(getWidgetsWithData(shiftItem, false));
                if (SHIFT_APPROVED.equals(data.getStatusCode())) {
                    competenciesTable.addRow(getWidgetsWithData(shiftItem, true));
                }
            }
            brigadaEmployeesTable = new EditableTable(getBrigadaColumns(), false, false);
            Div brigadaTableContainer = new Div();
            brigadaTableContainer.setStyleName("brigada_employees_container");
            brigadaTableContainer.add(brigadaEmployeesTable);
//            addField("shiftContainer", brigadaTableContainer, null);
            getGroupsEmployees(data.getShiftTeams());
            if (data.getApproverEmployee() != null) {
                approvers.setHTML(data.getApproverEmployee().getName());
            }
            if (data.getOverallStatus() != null) {
                statusCode = data.getOverallStatus().getCode();
            }
            if (data.getManager() != null) {
                manager.setHTML(data.getManager());
            }
            if (data.getDepartment() != null) {
                department.setHTML(data.getDepartment().getName());
            }
            if (data.getBirgada() != null) {
                brigada.setHTML(data.getBirgada().getName());
            }
            if (data.getBackupManager() != null) {
                backUpManager.setHTML(data.getBackupManager());
            }
            if (data.getPeriod() != null) {
                DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
                DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MMM yyyy");
                DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd-MMM yyyy");
                String type = data.getLookUpType() == null || data.getLookUpType().equals(LookUpConstants.BRIGADA_ID) ? "B" : "E";
                if ("week".equals(periodType)) {
                    monthPicker.setHTML(yearFormat.format(data.getPeriod()));
                } else if (data.getOverallStatus() != null && data.getOverallStatus().getCode() != null && data.getOverallStatus().getCode().equals(SHIFT_APPROVED)) {
                    String date = dateTimeFormat.format(data.getPeriod());
                    if (data.getEndDate() == null) {
                        monthPicker.setHTML("<a href=\"attendanceReport/shift/" + (!data.getGroupsId().isEmpty() ? data.getId() + type : "") + "\">" + date + "</a>");
                    } else {
                        monthPicker.setHTML("<a href=\"attendanceReport/shift/" + (!data.getGroupsId().isEmpty() ? data.getId() + type : "") + "\">" + dateFormat.format(data.getPeriod()) + " -> " + dateFormat.format(data.getEndDate().getDate()) + "</a>");
                    }
                } else {
                    if (data.getEndDate() != null) {
                        monthPicker.setHTML(dateFormat.format(data.getPeriod()) + " -> " + dateFormat.format(data.getEndDate().getDate()));
                    } else {
                        monthPicker.setHTML(dateTimeFormat.format(data.getPeriod()));
                    }
                }
            }
            if (data.getOwnersSelectItem() != null) {
                StringBuilder owners = new StringBuilder();
                for (SelectItem selectItem : data.getOwnersSelectItem()) {
                    owners.append(selectItem.getName() + ",");
                }
                owner.setHTML(owners.toString());
            }
            if (data.getLookUpType() != null) {
                String lookupName = null;
                if (data.getLookUpType().equals(LookUpConstants.BRIGADA_ID)) {
                    lookupName = wfmStrings.team();
                } else if (data.getLookUpType().equals(LookUpConstants.EMPLOYEE_ID)) {
                    lookupName = wfmStrings.duty();
                } else if (data.getLookUpType().equals(LookUpConstants.OVERTIME)) {
                    lookupName = wfmStrings.overtime();
                }
                type.setHTML(lookupName);
            }

        }


    }

    private HTML getPeriodAsLink() {
        DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MMM yyyy");
        HTML label = new HTML();
        String type = data.getLookUpType() == null || data.getLookUpType().equals(LookUpConstants.BRIGADA_ID) ? "B" : "E";
        String date = data.getPeriod() != null ? dateTimeFormat.format(data.getPeriod()) : null;
        if (data.getOverallStatus() != null && data.getOverallStatus().getCode() != null && data.getOverallStatus().getCode().equals(SHIFT_APPROVED)) {
            label.setStyleName("uploadLinkStyle2");
            label.addClickHandler(e -> {
                goTo("attendanceReport/shift/" + (!data.getGroupsId().isEmpty() ? data.getId() + type : "") + "/" + date);
            });
        }
        label.setHTML("date");
        return label;
    }

    protected Widget[] getWidgetsWithData(ShiftItems shiftItems,boolean isCompetencies) {
        ArrayList<Widget> rowWidgets = new ArrayList<>();
        if (shiftItems != null) {
            String positionName = shiftItems.getPositionName() != null ? "<br/>"+shiftItems.getPositionName() : "";
            GWT.log(positionName);
            ExtendedHTML label = new ExtendedHTML(shiftItems.getSelectedGroup().getName()+positionName);
            boolean access = data.getLookUpType() == null || data.getLookUpType().equals(LookUpConstants.BRIGADA_ID) ?
                    Utils.hasPermission(PermissionConstants.HRMS_BRIGADA_SUMMARY) : Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE);
            String link = data.getLookUpType() == null || data.getLookUpType().equals(LookUpConstants.BRIGADA_ID) ?
                    "brigada|summary/" : "employeeProfile|employeeProfileView/";
            if (access) {
                label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged(link + shiftItems.getSelectedGroup().getId()));
            }
            label.setStyleName("uploadLinkStyle2");
            rowWidgets.add(label);
            HashMap<String, SelectItem> dayAndSelectedTimeSlot = shiftItems.getDayAndSelectedTimeSlotS();
            HashMap<String, Integer> dayAndShiftItemId = shiftItems.getDayAndShiftItemId();
            HashMap<Integer, String> leaveDays = shiftItems.getLeaveDays();
            HashMap<String, Double> rates = shiftItems.getRates();
            HashMap<String, Integer> assessments = shiftItems.getAssesments();
            double totalRate = 0.0;
            Date currentDate = new Date();
            Date period = data.getPeriod();
            int counter = 0;
            if ("week".equals(periodType)) {
                for (int i = 1; i <= monthMaxDay; i++) {
                    ExtendedHTML timeSlotShortName = new ExtendedHTML();
                    SelectItem selectedTimeSlot = dayAndSelectedTimeSlot.get("" + i);
                    if (selectedTimeSlot != null) {
                        timeSlotShortName.setText(selectedTimeSlot.getName());
                    }
                    rowWidgets.add(timeSlotShortName);
                }
            } else if (!isCustom) {
                for (int j = 1; j <= monthMaxDay; j++) {
                    ExtendedHTML timeSlotShortName = new ExtendedHTML();
                    SelectItem selectedTimeSlot = dayAndSelectedTimeSlot.get("" + j);
                    Integer shiftItemId = dayAndShiftItemId.get("" + j);
                    Double rate = rates.get("" +j);
                    Integer assessmentId = assessments.get("" +j);
                    if (isCompetencies) {
                        if (assessmentId != null) {
                            timeSlotShortName.setText(rate != null ? NumberFormat.getFormat("0.0").format(rate) : "");
                            totalRate += rate != null ? rate : 0;
                            counter++;
                            timeSlotShortName.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("assessment/" + assessmentId+"/APPROVED/ASSESSMENT_SIMPLE"));
                            timeSlotShortName.setStyleName("uploadLinkStyle2");
                        } else if (new Date(period.getYear(), period.getMonth(), j).before(currentDate) && selectedTimeSlot != null) {
                            timeSlotShortName.setText(0+"");
                            counter++;
                        }
                    } else if (leaveDays.get(j) != null) {
                        timeSlotShortName.setStyleName("uploadLinkStyle2");
                        timeSlotShortName.setText(leaveDays.get(j));
                    } else if (selectedTimeSlot != null) {
                        timeSlotShortName.setText(selectedTimeSlot.getName());
                        if (assessmentId == null && Utils.hasPermission(PermissionConstants.HRMS_APPRAISAL_ADD_FROM_SHIFT)) {
                            if (new Date(period.getYear(), period.getMonth(), j).before(currentDate)) {
                                timeSlotShortName.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("initiate|add/add/"+shiftItems.getSelectedGroup()+"/"+shiftItemId));
                                timeSlotShortName.setStyleName("uploadLinkStyle2");
                            } else {
                                timeSlotShortName.setTextAsHtml("<span style= \"opacity:0.5\"><i>" + selectedTimeSlot.getName() + "</i></span>");
                            }
                        } else if (assessmentId != null) {
                            timeSlotShortName.setStyleName("uploadLinkStyle2");
                            timeSlotShortName.setTextAsHtml("<b>" + selectedTimeSlot.getName() + "</b>");
                            timeSlotShortName.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("assessment/" + assessmentId + "/APPROVED/ASSESSMENT_SIMPLE"));
                            timeSlotShortName.setStyleName("uploadLinkStyle2");
                        }
                    }
                    rowWidgets.add(timeSlotShortName);
                }
            } else {

                Date tempDate = new Date(data.getPeriod().getTime());
                Date endDate = new Date(data.getEndDate().getDate().getTime());
                int i = 1;
                while (!tempDate.after(endDate)) {
//                    ExtendedHTML timeSlotShortName = new ExtendedHTML();
//                    SelectItem selectedTimeSlot = dayAndSelectedTimeSlot.get(DateUtils.getDateFormatShort(tempDate));
//                    timeSlotShortName.setText(selectedTimeSlot != null ? selectedTimeSlot.getName() : "");
////


                    TimeSlotShortNameLookUp timeSlotShortNameLookUp = new TimeSlotShortNameLookUp(new Date(tempDate.getTime()));
                    timeSlotShortNameLookUp.setSelected(dayAndSelectedTimeSlot.get(DateUtils.getDateFormatShort(tempDate)));
                    timeSlotShortNameLookUp.setEnabled(false);


                    rowWidgets.add(timeSlotShortNameLookUp);
                    tempDate.setDate(tempDate.getDate() + 1);
                }
            }


            if (isCompetencies) {
                rowWidgets.add(new CustomCellLabel(totalRate > 0 && counter > 0 ? NumberFormat.getFormat("0.0").format(totalRate / counter) : ""));
            }

        }
        return rowWidgets.toArray(new Widget[]{});

    }

    protected ColumnConfig[] getColumns(boolean isCompetencies) {
        String lookupName = null;
        if (data.getLookUpType() == null || data.getLookUpType().equals(LookUpConstants.BRIGADA_ID)) {
            lookupName = wfmStrings.team();
        } else if (data.getLookUpType().equals(LookUpConstants.EMPLOYEE_ID)) {
            lookupName = wfmStrings.employee();
        } else if (data.getLookUpType().equals(LookUpConstants.OVERTIME)) {
            lookupName = wfmStrings.overtime();
        }

        ColumnConfig[] columns = new ColumnConfig[isCompetencies ? monthMaxDay + 2 : monthMaxDay + 1];
        columns[0] = new ColumnConfig(CustomCell.class, "group", "<span class=\"frame_affix_top\">" + lookupName + "</span>", 200, false);
        DateTimeFormat weekDayFormat = DateTimeFormat.getFormat("E");
        DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMM");
        DateTimeFormat dayFormat = DateTimeFormat.getFormat("dd");
        if ("week".equals(periodType)) {
            for (int i = 1; i <= monthMaxDay; i++) {
                columns[i] = new ColumnConfig(CustomCell.class, "" + i, "W" + i, 50, false);
            }
        } else if (!isCustom) {
            for (int i = 1; i <= monthMaxDay; i++) {
                Date tempDate = data.getPeriod();
                tempDate.setDate(i);

                String monthString = monthFormat.format(tempDate);
                String dateString = dayFormat.format(tempDate);
                String weekDay = weekDayFormat.format(tempDate);

                monthString = monthString.substring(0, 3);

                columns[i] = new ColumnConfig(CustomCell.class,
                        monthString + ".-" + dateString + "<span  class='attendance-report-header-weekday-frame_affix_top'>" + weekDay + "</span>",
                        monthString + ".-" + dateString + "  <br/> <span class='attendance-report-header-weekday-frame_affix_top'>" + weekDay + "</span>", 50, false);
            }
        } else {
            Date tempDate = new Date(data.getPeriod().getTime());
            Date endDate = new Date(data.getEndDate().getDate().getTime());

            int counter = 1;
            while (!tempDate.after(endDate)) {
                String monthString = monthFormat.format(tempDate);
                String dateString = dayFormat.format(tempDate);
                String weekDay = weekDayFormat.format(tempDate);

                String span = "<span class='attendance-report-header-weekday-frame_affix_top'" + (weekDay.equals("Sat") || weekDay.equals("Sun") ? " style = \"color:red\"" : "") + ">";
                columns[counter] = new ColumnConfig(LookUpCell.class,
                        "" + counter,
                        monthString + ".-" + dateString + "</br>" + span + weekDay + "</span>", 50, false);

                counter++;
                tempDate.setDate(tempDate.getDate() + 1);
            }
        }

        if (isCompetencies) {
            columns[monthMaxDay+1] = new ColumnConfig(CustomCell.class, "Total",  wfmStrings.total(), 50, false);
        }
        return columns;
    }

    protected ColumnConfig[] getBrigadaColumns() {
        ArrayList<ColumnConfig> columnConfigs = new ArrayList<>();

        ColumnConfig columnConfig1 = new ColumnConfig(LookUpCell.class, "team", "<b>" + wfmStrings.team() + "</b>", 50);
        ColumnConfig columnConfig2 = new ColumnConfig(CustomCell.class, "employeeCode", "<b>" + wfmStrings.employeeCode() + "</b>", 50);
        ColumnConfig columnConfig3 = new ColumnConfig(LookUpCell.class, "fullName", "<b>" + wfmStrings.fullName() + "</b>", 50);
        ColumnConfig columnConfig4 = new ColumnConfig(CustomCell.class, "positon", "<b>" + wfmStrings.position() + "</b>", 50);
        ColumnConfig columnConfig5 = new ColumnConfig(CustomCell.class, "department", "<b>" + wfmStrings.department() + "</b>", 50);
        ColumnConfig columnConfig6 = new ColumnConfig(CustomCell.class, "label", "<b>" + wfmStrings.note() + "</b>", 50);

        columnConfig1.setForceWidthInPercent(true);
        columnConfig2.setForceWidthInPercent(true);
        columnConfig3.setForceWidthInPercent(true);
        columnConfig4.setForceWidthInPercent(true);
        columnConfig5.setForceWidthInPercent(true);
        columnConfig6.setForceWidthInPercent(true);

        columnConfigs.add(columnConfig1);
        columnConfigs.add(columnConfig2);
        columnConfigs.add(columnConfig3);
        columnConfigs.add(columnConfig4);
        columnConfigs.add(columnConfig5);
        columnConfigs.add(columnConfig6);

        return columnConfigs.toArray(new ColumnConfig[]{});
    }

    protected Widget[] getWidgetForEmployeeTable(ShiftTeamsItem employee) {
        ArrayList<Widget> rowWidgets = new ArrayList<>();
        BrigadaLookUp team = new BrigadaLookUp();
        CustomCellTextBox employeeCode = new CustomCellTextBox();
        EmployeeLookUp employeeFullName = new EmployeeLookUp(true, false);
        CustomCellTextBox employeePosition = new CustomCellTextBox();
        CustomCellTextBox employeeDepartment = new CustomCellTextBox();
        CustomCellTextBox employeeLabel = new CustomCellTextBox();
        team.setSelected(employee.getTeam());
        employeeCode.setText(employee.getEmployeeCode());
        employeeFullName.setSelected(employee.getFullName());
        employeePosition.setText(employee.getDepartment());
        employeeDepartment.setText(employee.getPosition());
        employeeLabel.setText(employee.getLabel());
        team.setEnabled(false);
        employeeCode.setEnabled(false);
        employeeFullName.setEnabled(false);
        employeeDepartment.setEnabled(false);
        employeePosition.setEnabled(false);
        employeeLabel.setEnabled(false);
        rowWidgets.add(team);
        rowWidgets.add(employeeCode);
        rowWidgets.add(employeeFullName);
        rowWidgets.add(employeeDepartment);
        rowWidgets.add(employeePosition);
        rowWidgets.add(employeeLabel);
        return rowWidgets.toArray(new Widget[]{});
    }

    private void getGroupsEmployees(LinkedHashMap<Integer, List<ShiftTeamsItem>> shiftTeamsItems) {
        shiftTeamsItems.forEach((k, v) -> {
            for (ShiftTeamsItem teamsItem : v) {
                brigadaEmployeesTable.addRow(getWidgetForEmployeeTable(teamsItem));
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SHIFT_FORM;
    }

    @Override
    protected String getFormType() {
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected void registerFields() {
        monthPicker = initHTML();
//        monthPicker.setWidth("200px");
        approvers = initHTML();
        manager = initHTML();
        backUpManager = initHTML();
        owner = initHTML();
        type = initHTML();
        department = initHTML();
        brigada = initHTML();
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        drawFields();
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void drawFields() {

        if (formPropertyMap != null && formPropertyMap.get("monthPicker") != null) {
            addField("monthPicker", monthPicker, getTitle(formPropertyMap.get("monthPicker").isChanged() ? formPropertyMap.get("monthPicker").getTitle() : wfmStrings.month(), formPropertyMap.get("monthPicker").isRequired()));
        } else {
            addField("monthPicker", monthPicker, wfmStrings.month());
        }

        if (formPropertyMap != null && formPropertyMap.get(APPROVERS) != null) {
            addField(APPROVERS, approvers, getTitle(formPropertyMap.get(APPROVERS).isChanged() ? formPropertyMap.get(APPROVERS).getTitle() : wfmStrings.approvers(), formPropertyMap.get(APPROVERS).isRequired()));
        } else {
            addField(APPROVERS, approvers, wfmStrings.approvers());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.MANAGER, manager, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getTitle() : wfmStrings.manager()));
        } else {
            addField(CustomFormConstants.PROJECT.MANAGER, manager, getTitle(wfmStrings.manager()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, backUpManager, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).getTitle() : wfmStrings.backupManagers()));
        } else {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, backUpManager, getTitle(wfmStrings.backupManagers()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TYPE) != null) {
            addField(CustomFormConstants.TYPE, type, getTitle(formPropertyMap.get(CustomFormConstants.TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.TYPE).getTitle() : wfmStrings.type(), formPropertyMap.get(CustomFormConstants.TYPE).isRequired()));
        } else {
            addField(CustomFormConstants.TYPE, type, wfmStrings.type());
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null) {
            addField(CustomFormConstants.DEPARTMENT, department, getTitle(formPropertyMap.get(CustomFormConstants.DEPARTMENT).isChanged() ? formPropertyMap.get(CustomFormConstants.DEPARTMENT).getTitle() : wfmStrings.department(), formPropertyMap.get(CustomFormConstants.DEPARTMENT).isRequired()));
        } else {
            addField(CustomFormConstants.DEPARTMENT, department, wfmStrings.department());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BRIGADA) != null) {
            addField(CustomFormConstants.BRIGADA, brigada, getTitle(formPropertyMap.get(CustomFormConstants.BRIGADA).isChanged() ? formPropertyMap.get(CustomFormConstants.BRIGADA).getTitle() : wfmStrings.brigadas(), formPropertyMap.get(CustomFormConstants.BRIGADA).isRequired()));
        } else {
            addField(CustomFormConstants.BRIGADA, brigada, wfmStrings.brigadas());
        }



        addField(CustomFormConstants.OWNER, owner, wfmStrings.owners());
    }

    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private SelectItem[] getSelectItemTypes() {
        return new SelectItem[]{
                new SelectItem(LookUpConstants.BRIGADA_ID, wfmStrings.team()),
                new SelectItem(LookUpConstants.EMPLOYEE_ID, wfmStrings.duty()),
        };
    }

    private int getISOWeeksInYear(int year) {
        Date jan1 = new Date(year - 1900, 0, 1);
        int dayOfWeek = jan1.getDay();
        int isoDay = dayOfWeek == 0 ? 7 : dayOfWeek;
        if (isoDay == 4) return 53;
        if (isoDay == 3 && isLeapYear(year)) return 53;
        return 52;
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
}
