package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.ui.view.customTabs.EmployeeLeaveRequestChart;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.KpiDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.BackupEmployeeNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.laborPeriod.LaborPeriodWidget;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableForLeaveRequest;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CalendarTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.view.CalendarView;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.FlexAlignContent;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LeaveRequestView extends CustomForm2 implements Constants, Colapse {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private static final String EMPLOYEE = "employee";
    WfmButton2 approveButton;
    WfmButton2 rejectButton;
    WfmButton2 deleteButton;
    WfmButton2 recallEmployeeButton;
    WfmButton2 submitButton;
    EmployeeLeaveRequestChart leaveRequestChart;
    private HTML employeeName;
    private HTML reason;
    private HTML datePeriod;
    private HTML type;
    private HTML takenLaveBy;
    private BackupEmployeeNavBox backupEmployeeNavBox;
    private TextArea description;
    private final Integer objectId;
    private Integer employeeID;
    private FormHasCustomField customFieldUtil;
    private HTML approver;
    private HTML numberData;
    private LaborPeriodWidget leavePeriodBox;
    private FormGroup leavePeriodForm;
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy");
    private final Span balance = new Span(wfmStrings.leaveDays() + ": 0.00");
    private final Span originalBalance = new Span(wfmStrings.leaveDays() + ": 0.00");
    private MultiTableForLeaveRequest backupEmployeeTable;
    private MaterialPanel chartTabBar;
    private StatisticsLeaveRequest leaveRequest;
    private int index = 0;
    private MaterialPanel calendarTabBar;

    public LeaveRequestView(Integer objectId) {
        super("leaverequest", (hrmsStrings.leaveRequestView()));
        this.objectId = objectId;

    }

    public String getIconStyle() {
        return "icon-leaveRequest-comments";
    }

    @Override
    protected void initPredefinedValues() {
// To do
    }

    private void deleteAction(Integer objectID, Integer employeeId) {
        if (Utils.hasPermission(PermissionConstants.HRMS_REMOVE_REQUEST) || (Utils.getUserID().equals(employeeId))) {
            final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            message.setTitle(wfmStrings.warning());
            message.setMessage(hrmsStrings.areYouSureYouWanttoDeleteThisLeaveRequest());
            message.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    AvailabilityService.App.get().deleteRequest(objectID, new AbstractAsyncCallback<Void>() {

                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            LoadingPanel.loading(false);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVER_REQUEST_DELETE, null, LeaveRequestView.this);
                            Info.show((Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.leaveRequest())), Info.Type.INFO);
                            closeTab();
                        }
                    });
                }
            });
            message.open();
        } else {
            Info.show(wfmStrings.youDontHavePermission());
        }
    }

    private void getLeaveDuration() {
        if (leaveRequest.getStartDDate() != null && leaveRequest.getEndDDate() != null) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setIncludeDayOff(false);
            fp.setEmployeeId(employeeID);
            fp.setAllDay("LR_TYPE_ANNUAL_LEAVE".equals(leaveRequest.getReasonCode()));
            if (leaveRequest.getReasonCode() != null) {
                fp.setReasonCode(leaveRequest.getReasonCode());
            }

            AvailabilityService.App.get().getLeaveDaysCount(fp, leaveRequest.getStartDDate(), leaveRequest.getEndDDate(), new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {
                    GWT.log("Got error on :" + throwable.getMessage());
                }

                @Override
                public void onSuccess(String duration) {
                    originalBalance.setText(wfmStrings.leaveDays() + ": " + duration);
                }
            });

        }
    }

    private void getRecallEmployeeModal(String leavePeriod) {
        KpiSideNavBox recallEmployeeModal = new KpiSideNavBox(true);
        setStyleName(recallEmployeeModal.getElement(), "quick-add", true);
        Heading h1 = new Heading(HeadingSize.H1);
        h1.setText(leavePeriod + " - " + employeeName.getText());
        recallEmployeeModal.addHeader(h1);

        Span period = new Span();
        period.setText(wfmStrings.leavePeriod() + ": " + DateUtils.format(leaveRequest.getStartDDate().getNonConvertedDate()) + " - " + DateUtils.format(leaveRequest.getEndDDate().getNonConvertedDate())
                + " (" + originalBalance.getText() + ")");
        Div sickPeriod = new Div();
        sickPeriod.setStyle("margin: 10px 0;");
        sickPeriod.add(period);

        Span headerTitle = new Span();
        headerTitle.setText(wfmStrings.recallDate());
        Div header = new Div();
        header.add(sickPeriod);
        header.add(headerTitle);

        KpiDatePicker recallDate = new KpiDatePicker();
        recallDate.setWidth("30%");
        recallDate.addValueChangeHandler(changeEvent -> recallDate.removeStyleName(Constants.ERROR_FORM_STYLE));
        Icon resetIcon = new Icon();
        resetIcon.setStyle("margin: 0 10px;");
        resetIcon.setStyleName("ficon--trash");
        resetIcon.setTooltip(wfmStrings.reset());
        resetIcon.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        resetIcon.addClickHandler(click -> recallDate.setDefaultValue());
        Div body = new Div();
        body.setWidth("100%");
        body.setStyle("display: flex; flex-direction: row; align-items: center; width: 100%; margin: 10px 0;");
        body.add(recallDate);
        body.add(resetIcon);

        HTML note = new HTML();
        note.setHTML("<b>" + wfmStrings.pleaseNoteThat() + "</b> " + wfmStrings.recallEmployeeNote());
        Div footer = new Div();
        footer.add(note);
        Div container = new Div();
        container.add(header);
        container.add(body);
        container.add(footer);

        WfmButton2 save = new WfmButton2();
        save.setStyleName(BTN_PRIMARY);
        save.setText(wfmStrings.save());
        save.addClickHandler(click -> getEnsure(leaveRequest, recallDate, recallEmployeeModal));

        recallEmployeeModal.addBody(container);
        recallEmployeeModal.addFooter(save);
        recallEmployeeModal.show();
    }

    private void getEnsure(StatisticsLeaveRequest leaveRequest, KpiDatePicker recallDate, KpiSideNavBox recallEmployeeModal) {
        KpiModal modal = new KpiModal();
        modal.setTitle(wfmStrings.sureYouWantToApprove() + "?");
        WfmButton2 save = new WfmButton2(wfmStrings.yes(),
                WfmButton2.BTN_PRIMARY,
                clickEvent -> createRecallLeave(leaveRequest.getStartDDate(), leaveRequest.getRecallDDate() != null ? leaveRequest.getRecallDDate() : leaveRequest.getEndDDate(),
                        recallDate, recallEmployeeModal));
        WfmButton2 close = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_PRIMARY, clickEvent -> modal.close());
        modal.addButton(close);
        modal.addButton(save);
        modal.open();
        modal.overlayCloseHandler();
    }

    public void createRecallLeave(DateNonConvertable startDate, DateNonConvertable dueDate, KpiDatePicker recallDate, KpiSideNavBox recallEmployeeModal) {
        if (!validateRecallDate(startDate.getNonConvertedDate(), dueDate.getNonConvertedDate(), recallDate)) {
            return;
        }
        AvailabilityService.App.get().restoreLeave(employeeID, objectId, new DateNonConvertable(recallDate.getDate()), dueDate, leaveRequest.getReasonCode(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log("Error catched: ", throwable.getCause());
            }

            @Override
            public void onSuccess(Void unused) {
                LoadingPanel.loading(true);
                recallEmployeeModal.hide();
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.request()), Info.Type.INFO);
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVER_REQUEST_ADD, null, LeaveRequestView.this);
            }
        });
    }

    public boolean validateRecallDate(Date startDate, Date dueDate, KpiDatePicker recallDate) {
        if (recallDate.getDate() != null && DateUtil.compareByDate(recallDate.getDate(), startDate) && DateUtil.compareByDate(dueDate, recallDate.getDate())) {
            return true;
        } else {
            Info.show(hrmsStrings.dateValidationForRecall(), Info.Type.WARNING);
            recallDate.setStyleName(Constants.ERROR_FORM_STYLE, true);
            return false;
        }
    }

    @Override
    protected void addButtons() {
        if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && Utils.hasPermission(PermissionConstants.HRMS_RECALL_EMPLOYEE)) {
            recallEmployeeButton = addButton(hrmsStrings.recallEmployee(), BTN_DEFAULT_OUTLINE, click -> getRecallEmployeeModal(numberData.getText()));
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_LEAVE_REQUEST_PDF_BUTTON)) {
            addPdfButton().addClickHandler(click -> new PDFTemplateSelector(AccountingConstants.LEAVE_REQUEST, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    LeaveRequestObject requestObject = new LeaveRequestObject(objectId, employeeID, id);
                    String pdfUrl = CommandConstants.PDF_URL + "/employeeLeaveRequestViewPDFHandler";
                    HashMap<String, String> requestParams = requestObject.getRequestParams();
                    if (leaveRequestChart.getSVG() != null) {
                        requestParams.put("svg", leaveRequestChart.getSVG());
                    }
                    Utils.sendPDFOrExcelRequest(panel, pdfUrl, requestParams, "_blank");
                }
            }));
        }


        if (Utils.hasPermission(PermissionConstants.HRMS_REMOVE_REQUEST)) {
            deleteButton = addButton(wfmStrings.delete(), BTN_DEFAULT_OUTLINE, clickEvent -> {
                deleteAction(objectId, employeeID);
                deleteButton.ensureDebugId("delete-leave-request");
            });
        }
        submitButton = addButton(wfmStrings.submit(), BTN_PRIMARY, clickEvent -> {
            submitButton.setEnabled(false);
            LoadingPanel.loading(true);
            ArrayList<BackupEmployeeItem> allBackupEmployeeData = backupEmployeeNavBox.getAllBackupEmployeeData();

            AvailabilityService.App.get().saveBackupEmployeesFromSummary(objectId, allBackupEmployeeData, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(Void unused) {
                    LoadingPanel.loading(false);
                    closeTab();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVER_REQUEST_APPROVED, unused, LeaveRequestView.this);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.backupEmployee()));
                }
            });
        });
        submitButton.setVisible(false);

        rejectButton = addButton(wfmStrings.reject(), WfmButton2.BTN_REJECT, click -> {
            enabledButtons(false);
            KpiModal reasonBox = new KpiModal();
            reasonBox.setTitle(wfmStrings.pleaseSpecifyRejectionReason());
            reasonBox.setFlexAlignContent(FlexAlignContent.CENTER);
            final TextArea txtReason = new TextArea();
            txtReason.setHeight("120px");
            txtReason.setStyleName("form-control file--SaleQuoteSummaryVIew");
            reasonBox.add(txtReason);
            reasonBox.addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, event -> {
                reasonBox.close();
                enabledButtons(true);
            }));
            reasonBox.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
                String rejectionReason = txtReason.getText();
                if (rejectionReason == null || "".equals(rejectionReason)) {
                    txtReason.addStyleName(ERROR_FORM_STYLE);
                    Info.warn(wfmStrings.pleaseSpecifyRejectionReason());
                    return;
                }
                reasonBox.close();
                initApproveOrReject(Constants.LR_STATUS_SS_DENIED, false, rejectionReason);
            }));
            reasonBox.setWidth("400px");
            reasonBox.center();
        });
        rejectButton.setVisible(false);

        approveButton = addButton(wfmStrings.approve(), WfmButton2.BTN_SUCCESS, click -> {
            enabledButtons(false);
            initApproveOrReject(Constants.LR_STATUS_SS_APPROVED, false, null);
        });
        approveButton.setVisible(false);

    }

    private void initApproveOrReject(final String approveType, boolean approveForAll, String rejectionReason) {
        AvailabilityService.App.get().updateApprove(approveType, objectId, approveForAll, rejectionReason, new AbstractAsyncCallback<Void>() {

            @Override
            public void failure(Throwable caught) {
                enabledButtons(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                enabledButtons(true);
                hideButtons();
                closeTab();
                if (Constants.LR_STATUS_SS_APPROVED.equals(approveType)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVER_REQUEST_APPROVED, result, LeaveRequestView.this);
                    Info.show(hrmsStrings.infoMessage42part1(), Info.Type.INFO);
                } else {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVER_REQUEST_REJECTED, result, LeaveRequestView.this);
                    Info.show(hrmsStrings.infoMessage42part2(), Info.Type.INFO);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTIFICATION_MSG_LIST_UPDATE, null, null);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTIFICATION_MSG_CHANGE_ENTITY, null, null);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LEAVE_REQUEST_STATUS_CHANGED, null, null);

            }
        });
    }

    private void enabledButtons(boolean b) {
        if (approveButton != null) {
            approveButton.setEnabled(b);
        }

        if (rejectButton != null) {
            rejectButton.setEnabled(b);
        }
    }

    @Override
    protected void getDataToFillFields() {
        getFillDataComments();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.LEAVE_REQUEST_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.LeaveRequest, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                GWT.log("", throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                LeaveRequestView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected void registerFields() {
        String leaveRequestCommentsView = "leave_request_view_";

        employeeName = new HTML();
        employeeName.addStyleName(DEFAULT_WIDTH);
        employeeName.ensureDebugId(leaveRequestCommentsView + "employeeName");

        numberData = new HTML();
        numberData.addStyleName(DEFAULT_WIDTH);
        numberData.ensureDebugId(leaveRequestCommentsView + "leaveRequestCode");

        reason = new HTML();
        reason.addStyleName(DEFAULT_WIDTH);
        reason.ensureDebugId(leaveRequestCommentsView + "reason");

        description = new TextArea();
        description.setEnabled(false);
        description.setReadOnly(true);
        description.addStyleName(DEFAULT_WIDTH);
        description.ensureDebugId(leaveRequestCommentsView + "description");

        datePeriod = new HTML();
        datePeriod.addStyleName(DEFAULT_WIDTH);
        datePeriod.ensureDebugId(leaveRequestCommentsView + "datePeriod");

        FormGroup formGroup = new FormGroup(wfmStrings.leavePeriod().toLowerCase() + ":", datePeriod);
        Div dateLabel = formGroup.getGroupLabel();
        dateLabel.addStyleName("label-group");
        dateLabel.add(balance);

        type = new HTML();
        type.addStyleName(DEFAULT_WIDTH);
        type.ensureDebugId(leaveRequestCommentsView + "type");

        takenLaveBy = new HTML();
        takenLaveBy.addStyleName(DEFAULT_WIDTH);
        takenLaveBy.ensureDebugId(leaveRequestCommentsView + "takenLaveBy");

        approver = new HTML();
        approver.addStyleName(DEFAULT_WIDTH);
        approver.ensureDebugId(leaveRequestCommentsView + "approver");

        backupEmployeeNavBox = new BackupEmployeeNavBox();

        backupEmployeeTable = new MultiTableForLeaveRequest(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getWidgets(null);

            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> widgetsMap : backupEmployeeTable.getWidgets()) {
                    EmployeeLookUpWithCode backupEmployee = (EmployeeLookUpWithCode) widgetsMap.get(MultiTable.LOOK_UP_BOX);
                    if (backupEmployee.getSelectedItem() != null) {
                        return true;
                    }
                }
                return false;
            }


        }, false, backupEmployeeNavBox);
        backupEmployeeTable.setViewMode(true);
        backupEmployeeTable.setSummaryForm(true);
        backupEmployeeNavBox.setSummaryForm(true);
        backupEmployeeNavBox.popupForBackupEmployee.setSaveButtonListener(() -> {
            if (backupEmployeeNavBox.mapSizeAfterSave > backupEmployeeNavBox.mapSizeBeforeSave) {
                approveButton.setVisible(false);
                rejectButton.setVisible(false);
                submitButton.setVisible(true);
            }
        });

        chartTabBar = new MaterialPanel("pg_leave__calendar box-bg--1 box-radius");
        calendarTabBar = new MaterialPanel("pg_leave__calendar box-bg--1 box-radius");

        leavePeriodBox = new LaborPeriodWidget(employeeID, true, false);

        MaterialPanel leavePeriodPanel = new MaterialPanel("form-row");
        Div periodHeader = new Div("col-12");
        periodHeader.add(new InputGroup(leavePeriodBox));
        leavePeriodPanel.add(periodHeader);

        leavePeriodForm = new FormGroup(wfmStrings.employmentPeriod() + ":", leavePeriodPanel);
        leavePeriodForm.setVisible(false);

        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.requestDetails());
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION)) {
            addField(CustomFormConstants.LEAVE_FOR_PERIOD, leavePeriodForm, null);
        }
        addField(CustomFormConstants.EMPLOYEES, employeeName, wfmStrings.employee());
        addField(CustomFormConstants.LEAVE_REQUEST_NUMBER, numberData, getTitle(wfmStrings.number()));
        addField(CustomFormConstants.REASON, reason, wfmStrings.reason());
        addField(CustomFormConstants.DESCRIPTION, description, wfmStrings.description());
        addField(CustomFormConstants.DATE_PERIOD, formGroup, null);
        addField(CustomFormConstants.CHART, chartTabBar, null);
        addField(CustomFormConstants.CALENDAR, calendarTabBar, null);
        addField(CustomFormConstants.APPROVER, approver, wfmStrings.approver());
        addField(CustomFormConstants.BACKUP_EMPLOYEE, backupEmployeeTable, wfmStrings.backupEmployee());

        FormGroup paidGroup = new FormGroup(wfmStrings.type() + ":", type);
        paidGroup.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        GColumn column1 = new GColumn(GColumnEnum.COL_6, paidGroup);

        FormGroup leaveBy = new FormGroup(hrmsStrings.takeLeaveBy() + ":", takenLaveBy);
        leaveBy.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        GColumn column2 = new GColumn(GColumnEnum.COL_6, leaveBy);
        addField(CustomFormConstants.TAKE_LIVE_TYPE, new GRow(column1, column2), null);

        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        show();
    }

    private void getFillDataComments() {
        LoadingPanel.loading(true);

        AvailabilityService.App.get().getLeaveRequest(objectId, new AbstractAsyncCallback<StatisticsLeaveRequest>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(StatisticsLeaveRequest result) {
                LoadingPanel.loading(false);
                LeaveRequestView.this.leaveRequest = result;
                LeaveRequestView.this.employeeID = result.getEmployeeId();
                drawInitialize(result);
                getLeaveDuration();

                if (result.getOverallStatus() != null && LR_STATUS_NOT_DEFINED.equals(result.getOverallStatus().getCode())) {
                    if (Utils.getUserID().equals(result.getCurrentApproverEmployeeID())) {
                        approveButton.setVisible(true);
                        rejectButton.setVisible(true);
                    } else {
                        hideButtons();
                    }
                } else {
                    hideButtons();
                }

                drawFooter();
            }
        });
    }

    private void hideButtons() {
        approveButton.removeFromParent();
        rejectButton.removeFromParent();
    }

    private void drawFooter() {
        NoteHistoryWidget noteHistoryWidget = new NoteHistoryWidget(callback -> {
            if (objectId == null) {
                return;
            }
            AvailabilityService.App.get().loadLeaveRequestHistory(objectId, callback);
        });
        if (objectId != null) {
            noteHistoryWidget.setSaveIntoDatabase(historyItem -> {
                if (historyItem != null) {
                    LoadingPanel.loading(true);
                    AvailabilityService.App.get().createLeaveRequestHistory(objectId, historyItem, new AsyncCallback<Integer>() {
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

            noteHistoryWidget.setRemoveFromDatabase(hisItem -> {
                if (hisItem != null && hisItem.getObjectID() != null) {
                    LoadingPanel.loading(true);
                    AvailabilityService.App.get().deleteLeaveRequestComment(hisItem.getObjectID(), new AsyncCallback<Void>() {
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

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.setInitialClasses("informer-item history-notes-container");
        footer.addToLeftSide(informer);

        FooterUploadPanel footerUploadPanel = new FooterUploadPanel(Constants.F_LEAVE_REQUEST, objectId, true);
        footer.addToLeftSide(footerUploadPanel);
    }

    private void drawInitialize(StatisticsLeaveRequest leaveRequest) {
        int year = Integer.parseInt(dateFormat.format(leaveRequest.getStartDDate().getNonConvertedDate()));
        CalendarTabWidget calendarTabWidget = new CalendarTabWidget(leaveRequest.getEmployeeId(), year, null, leaveRequest.getStartDDate().getNonConvertedDate(), leaveRequest.getReasonCode());
        calendarTabWidget.getCalendarView().setDisplayedMonth(leaveRequest.getStartDDate().getNonConvertedDate());
        setupCalendarView(calendarTabWidget.getCalendarView(), leaveRequest);

        setInnerHTML(numberData, leaveRequest.getNumberData() != null ? leaveRequest.getNumberData().getNumberString() : "");

        calendarTabBar.add(calendarTabWidget);

        leaveRequestChart = new EmployeeLeaveRequestChart();
        setupEmployeeLeaveRequestChart(leaveRequestChart, leaveRequest, year);
        leaveRequestChart.init();
        chartTabBar.add(leaveRequestChart);

        backupEmployeeNavBox.getDateFromLeave(leaveRequest.getStartDDate().getNonConvertedDate(), leaveRequest.getEndDDate().getNonConvertedDate());

        setLeaveRequestDetails(leaveRequest);

        if (leaveRequest.getBackupEmployee() != null) {
            if (leaveRequest.getBackupEmployee().isEmpty()) {
                createEmployeeLookUp();
                backupEmployeeTable.setVisibleForButtons(true, true);
            } else {
                for (BackupEmployeeItem item : leaveRequest.getBackupEmployee()) {
                    ApproverItemMini parent = item.getParentBackupEmployee();
                    createEmployeeLookUp();
                    backupEmployeeTable.setVisibleForButtons(false, false);
                    EmployeeLookUpWithCode c = (EmployeeLookUpWithCode) backupEmployeeTable.getWidgetsMaps().get(index).getWidget(EMPLOYEE);
                    c.setSelected(parent.getExactEmployee());
                    c.setEnabled(false);
                    index++;
                }
            }
        }

        setLeaveRequestSummary(leaveRequest);

        if (Utils.hasGenericAccess(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION) && leaveRequest.getReasonCode() != null && "LR_TYPE_ANNUAL_LEAVE".equals(leaveRequest.getReasonCode())) {
            setupLeavePeriodBox(leaveRequest);
        }

        getCustomFieldUtil().fillCustomFieldsWithData(leaveRequest.getCustomFields(), true);
    }

    private void setupCalendarView(CalendarView calendarView, StatisticsLeaveRequest leaveRequest) {
        if (leaveRequest.getOverallStatus() != null && LR_STATUS_NOT_DEFINED.equals(leaveRequest.getOverallStatus().getCode())) {
            calendarView.setLrPeriod1(leaveRequest.getStartDDate().getNonConvertedDate());
            calendarView.setLrPeriod2(leaveRequest.getEndDDate().getNonConvertedDate());
            calendarView.setIncludeDaysOff(leaveRequest.isIncludeDayOffs());
            calendarView.displayMonth();
        }
    }

    private void setupEmployeeLeaveRequestChart(EmployeeLeaveRequestChart chart, StatisticsLeaveRequest leaveRequest, int year) {
        chart.setEmployeeID(leaveRequest.getEmployeeId());
        chart.setSelectedYear(year);
        chart.setReasonID(leaveRequest.getReasonId());
        chart.setStartDate(leaveRequest.getStartDDate().getNonConvertedDate());
    }

    private void setLeaveRequestDetails(StatisticsLeaveRequest leaveRequest) {
        Date startDate = leaveRequest.getStartDDate().getNonConvertedDate();
        Date dueDate = leaveRequest.getRecallDDate() != null ? leaveRequest.getRecallDDate().getNonConvertedDate() : leaveRequest.getEndDDate().getNonConvertedDate();

        backupEmployeeNavBox.setLeaveRequestSummaryValues(leaveRequest.getBackupEmployee());
        backupEmployeeTable.removeAllRows();

        createEmployeeLookUp();
        backupEmployeeTable.setVisibleForButtons(!leaveRequest.getBackupEmployee().isEmpty(), !leaveRequest.getBackupEmployee().isEmpty());

        approver.setHTML(leaveRequest.getApproverName());
        if (leaveRequest.getApprovers() != null && leaveRequest.getApprovers().size() > 0 && leaveRequest.getCurrentApprover() != null && leaveRequest.getCurrentApprover().getExactEmployee() != null) {
            StringBuilder buildApprover = new StringBuilder();
            for (ApproverItemMini approver : leaveRequest.getApprovers()) {
                buildApprover.append(approver.getExactEmployee().getName());
                buildApprover.append("<br>");
            }
            approver.setHTML(buildApprover.toString());
        }



        type.setHTML(leaveRequest.getType());
        takenLaveBy.setHTML(Boolean.TRUE.equals(leaveRequest.getTakeByMoney()) ? wfmStrings.money() : wfmStrings.day());
        if (Utils.hasPermission(PermissionConstants.HRMS_EMPLOYEE_PROFILE) || Utils.isSettings()) {
            employeeName.setHTML("<a href=\"#employeeProfile%7CemployeeProfileView/" + leaveRequest.getEmployeeId() + "\">" + leaveRequest.getEmployee() + "</a>");
        } else {
            employeeName.setHTML(leaveRequest.getEmployee());
        }
        reason.setHTML(leaveRequest.getReason() != null ? leaveRequest.getReason() : "");
        description.setText(leaveRequest.getDescription() != null ? leaveRequest.getDescription() : "");
        datePeriod.setHTML(DateUtils.formatFromTo(startDate, dueDate, leaveRequest.isHideTime(), DateUtil.isSameDay(startDate, dueDate)));
        balance.setText(wfmStrings.leaveDays() + ": " + leaveRequest.getDuration());
    }

    private void setupLeavePeriodBox(StatisticsLeaveRequest leaveRequest) {
        leavePeriodBox.setEmployeeID(employeeID, leaveRequest.getReasonCode(), leaveRequest.getStartDDate().getNonConvertedDate(), leaveRequest.getObjectID());
        leavePeriodBox.setMultiLeaveItems(leaveRequest.getMultiLeaveList());
        leavePeriodForm.setVisible(true);
    }

    private void setLeaveRequestSummary(StatisticsLeaveRequest leaveRequest) {
        if (leaveRequest.getBackupEmployee() == null || leaveRequest.getBackupEmployee().isEmpty()) {
            createEmployeeLookUp();
            backupEmployeeTable.setVisibleForButtons(true, true);
        }
    }


    private void createEmployeeLookUp() {
        EmployeeLookUpWithCode withCode = new EmployeeLookUpWithCode();
        WidgetsMap widgetsMap = new WidgetsMap();
        widgetsMap.addWidgets(withCode);
        widgetsMap.add(EMPLOYEE, withCode);
        backupEmployeeTable.addWidgets(widgetsMap);
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private WidgetsMap getWidgets(ApproverItemMini employee) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final TextBox id = new TextBox();
        final EmployeeLookUpWithCode backupEmployees = new EmployeeLookUpWithCode();
        backupEmployees.getSuggestBox().addSelectionHandler(selectionEvent -> {
            approveButton.setVisible(false);
            rejectButton.setVisible(false);
            submitButton.setVisible(true);
        });
        id.setVisible(false);
        if (employee != null) {
            backupEmployees.setSelected(employee.getExactEmployee().getId());
            id.setText(employee.getObjectID().toString());
        }
        widgetsMap.add("id", id);
        widgetsMap.add(EMPLOYEE, backupEmployees);
        return widgetsMap;
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

    public void setEmployeeName(HTML employeeName) {
        this.employeeName = employeeName;
    }
}