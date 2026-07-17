package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentsListElem;
import com.edatasite.workforce.gwt.assessment.client.rpc.DepartmentPeriodAppraisalItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.ScoreItem;
import com.edatasite.workforce.gwt.assessment.client.ui.AssessmentHelper;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * User: Sher(sherali.pirnafaosov@gmail.com)
 * Date: 9/3/12
 * Time: 12:17 PM
 */
public class AppraisalApprovalView extends CustomForm implements Colapse {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private final Integer int_objectID;
    private DepartmentPeriodAppraisalItem appraisalItem;

    private Label periodLabel;
    private Label departmentLabel;
    private Label leaderLabel;
    private Label assessedLabel;
    private Label notAssessedLabel;
    private Label numberEmployeesLabel;
    private Label errorMessageLabel;
    private FlexTable scoreTable;
    private KpiModal rejectionReasonBox;
    private WfmButton2 saveRejectionReasonButton;

    private DataGrid<AssessmentsListElem> dataGrid;
    private ListDataProvider<AssessmentsListElem> dataProvider;
    private ColumnSortEvent.ListHandler<AssessmentsListElem> listHandler;

    private final String test_code_ID_name = "appraisal_approval_summary_view_";

    public static final ProvidesKey<AssessmentsListElem> KEY_PROVIDER = item -> item == null ? null : item.getAssessmentId();

    public AppraisalApprovalView(Integer int_objectID) {
        super("summary", "Appraisal Approval Summary");
        setDescription("Appraisal Approval Summary");
        this.int_objectID = int_objectID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        //approve button
        addButton(wfmStrings.approve(), WfmButton2.BTN_SUCCESS, null, (test_code_ID_name + "approve_button"), event -> {
            //approve logic
            appraisalItem.setStatusCode(DepartmentPeriodAppraisalItem.PERIOD_APPROVED);
            update(appraisalItem);
        });
        //reject button
        addButton(wfmStrings.reject(), WfmButton2.BTN_REJECT, null, (test_code_ID_name + "reject_button"), event -> {
            //reject logic
            rejectionReason();
            /*appraisalItem.setStatusCode(DepartmentPeriodAppraisalItem.PERIOD_REJECTED);
            update(appraisalItem);*/
        });
    }

    @Override
    protected void getDataToFillFields() {
        AssessmentService.App.get().getDepartmentPeriodAppraisalItem(int_objectID, new AbstractAsyncCallback<DepartmentPeriodAppraisalItem>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(DepartmentPeriodAppraisalItem result) {
                appraisalItem = result;
                fillFieldWithValue();
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.APPRAISAL_APPROVAL_FORM;
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
        drawInitialize();
        return null;
    }

    private void addDataDisplay(HasData<AssessmentsListElem> display) {
        dataProvider.addDataDisplay(display);
    }

    private void addFieldsToForm() {
        addField(AppraisalApproval.VALIDITY_PERIOD, periodLabel, wfmStrings.appraisalCycle());
        addField(AppraisalApproval.DEPARTMENT, departmentLabel, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
        addField(AppraisalApproval.LEADER, leaderLabel, wfmStrings.leader());
        addField(AppraisalApproval.EMPLOYEE_ASSESSED, assessedLabel, hrmsStrings.employeeAssessed());
        addField(AppraisalApproval.EMPLOYEE_NOT_ASSESSED, notAssessedLabel, hrmsStrings.employeeNotAssessed());
        addField(AppraisalApproval.NUMBER_OF_EMPLOYEES, numberEmployeesLabel, Property.get(Constants.DEPARTMENT_LIST, hrmsStrings.numberOfEmployeesInDepartment(), wfmStrings.department()));
        addField(AppraisalApproval.TOTAL_SCORE, scoreTable, hrmsStrings.totalScores());
        addField(AppraisalApproval.ERROR_MESSAGE, errorMessageLabel);

        addField(AppraisalApproval.ASSESSMENT_LIST, dataGrid);

        show();
    }

    private void draw(AssessmentsListElem[] assessmentElements) {
        dataProvider.getList().clear();
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage("", null, null));
        if (assessmentElements != null) {
            List<AssessmentsListElem> dataProviderList = dataProvider.getList();
            Collections.addAll(dataProviderList, assessmentElements);
        }
        dataProvider.refresh();
    }

    private void drawInitialize() {
        //period (appraisal cycle)
        periodLabel = new Label();
        periodLabel.ensureDebugId(test_code_ID_name + "period");
        //department name
        departmentLabel = new Label();
        departmentLabel.ensureDebugId(test_code_ID_name + "department");
        //department leader name
        leaderLabel = new Label();
        leaderLabel.ensureDebugId(test_code_ID_name + "department_leader");
        //employee assessed
        assessedLabel = new Label();
        assessedLabel.ensureDebugId(test_code_ID_name + "employee_assessed");
        //employee not assessed
        notAssessedLabel = new Label();
        notAssessedLabel.ensureDebugId(test_code_ID_name + "employee_not_assessed");
        //number of employees in department
        numberEmployeesLabel = new Label();
        numberEmployeesLabel.ensureDebugId(test_code_ID_name + "number_of_employees_in_department");
        //
        errorMessageLabel = new Label();
        //score table
        scoreTable = new FlexTable();
        scoreTable.ensureDebugId(test_code_ID_name + "score_table");

        dataProvider = new ListDataProvider<>();
        dataGrid = new DataGrid<>(KEY_PROVIDER);
        dataGrid.setSize("700px", "200px");
        listHandler = new ColumnSortEvent.ListHandler<>(dataProvider.getList());
        dataGrid.addColumnSortHandler(listHandler);
        addDataDisplay(dataGrid);
        initTableColumns();

        addFieldsToForm();
    }

    private void fillFieldWithValue() {

        Integer employeeCount = appraisalItem.getMembersCount();

        errorMessageLabel.setVisible(false);

        scoreTable.setHTML(0, 0, getScoreItem("A", appraisalItem.getScoreMap(), employeeCount));
        scoreTable.setHTML(1, 0, getScoreItem("B", appraisalItem.getScoreMap(), employeeCount));
        scoreTable.setHTML(2, 0, getScoreItem("C", appraisalItem.getScoreMap(), employeeCount));
        scoreTable.setHTML(3, 0, getScoreItem("D", appraisalItem.getScoreMap(), employeeCount));

        draw(appraisalItem.getAssessmentsListElems().toArray(new AssessmentsListElem[]{}));

        periodLabel.setText(appraisalItem.getValidityPeriodItem().getName());
        departmentLabel.setText(appraisalItem.getDepartmentName());
        leaderLabel.setText(appraisalItem.getDepartmentLeaderName());
        assessedLabel.setText(String.valueOf(appraisalItem.getEmployeeAssessed()));
        notAssessedLabel.setText(String.valueOf(appraisalItem.getEmployeeNotAssessed()));
        numberEmployeesLabel.setText(String.valueOf(appraisalItem.getMembersCount()));
        errorMessageLabel.setText("The period appraisal does not fit to Forced Distribution Ranking Percentage.");
    }

    private String getScoreItem(String scoreName, HashMap<String, Integer> scoreMap, Integer employeeCount) {
        if (scoreMap.containsKey(scoreName) && employeeCount > 0) {
            ScoreItem scoreItem = appraisalItem.getBonusSettingsItem().getScoreItemHashMap().get(scoreName);
            double percent = (scoreMap.get(scoreName) * 100) / employeeCount;
            if (percent > scoreItem.getEmployeePercentage().doubleValue()) {
                errorMessageLabel.setVisible(true);
            }
            return scoreName + " - " + scoreMap.get(scoreName) + " - " + percent + "%";
        } else {
            return scoreName + " - 0 - 0%";
        }
    }

    private void initTableColumns() {
        //employee name
        final Column<AssessmentsListElem, String> employee = new Column<AssessmentsListElem, String>(new TextCell()) {
            @Override
            public String getValue(AssessmentsListElem object) {
                return object.getEmployeeName();
            }
        };
        dataGrid.addColumn(employee, wfmStrings.employee());
        dataGrid.setColumnWidth(employee, 20, Style.Unit.PCT);
        employee.setSortable(true);

        listHandler.setComparator(employee, (o1, o2) -> o1.getEmployeeName().compareToIgnoreCase(o2.getEmployeeName()));
        //overall score
        Column<AssessmentsListElem, String> overallScore = new Column<AssessmentsListElem, String>(new TextCell()) {
            @Override
            public String getValue(AssessmentsListElem object) {
                return object.getOverallScore() != null ? Utils.formatDouble(object.getOverallScore()) : "";
            }
        };
        dataGrid.addColumn(overallScore, hrmsStrings.overallScore());
        dataGrid.setColumnWidth(overallScore, 11, Style.Unit.PCT);
        overallScore.setSortable(true);
        listHandler.setComparator(overallScore, (o1, o2) -> o1.getOverallScore().compareTo(o2.getOverallScore()));
        //grade
        Column<AssessmentsListElem, String> grade = new Column<AssessmentsListElem, String>(new TextCell()) {
            @Override
            public String getValue(AssessmentsListElem object) {
                return object.getOverallScore() != null ? AssessmentHelper.getScoreGradeName(object.getOverallScore(), appraisalItem.getBonusSettingsItem()) : "";
            }
        };
        dataGrid.addColumn(grade, "Grade");
        dataGrid.setColumnWidth(grade, 11, Style.Unit.PCT);
        grade.setSortable(false);

        //Reviewer name
        Column<AssessmentsListElem, String> reviewer = new Column<AssessmentsListElem, String>(new TextCell()) {
            @Override
            public String getValue(AssessmentsListElem object) {
                return object.getReviewerName();
            }
        };
        dataGrid.addColumn(reviewer, wfmStrings.reviewer());
        dataGrid.setColumnWidth(reviewer, 20, Style.Unit.PCT);
        reviewer.setSortable(true);
        listHandler.setComparator(reviewer, (o1, o2) -> o1.getReviewerName().compareToIgnoreCase(o2.getReviewerName()));
        //initiate date
        Column<AssessmentsListElem, String> initiateDate = new Column<AssessmentsListElem, String>(new TextCell()) {
            @Override
            public String getValue(AssessmentsListElem object) {
                return object.getInitiationDate() != null ? DateUtils.format(object.getInitiationDate()) : wfmStrings.notAvailable();
            }
        };
        dataGrid.addColumn(initiateDate, hrmsStrings.initiatedDate());
        dataGrid.setColumnWidth(initiateDate, 20, Style.Unit.PCT);
        initiateDate.setSortable(true);
        listHandler.setComparator(initiateDate, (o1, o2) -> (o1.getInitiationDate() != null ? DateUtils.format(o1.getInitiationDate()) :
                wfmStrings.notAvailable()).compareToIgnoreCase(o2.getInitiationDate() != null ?
                DateUtils.format(o2.getInitiationDate()) : wfmStrings.notAvailable()));
    }

    private void rejectionReason() {
        rejectionReasonBox = new KpiModal();
        rejectionReasonBox.setTitle(wfmStrings.rejectionReason());
        rejectionReasonBox.addStyleName("file--AppraisalApprovalView");
        final TextArea2 rejectionReasonArea = new TextArea2();
        rejectionReasonArea.setSize(193, 50);
        rejectionReasonArea.ensureDebugId(test_code_ID_name + "rejection_reason_comment_area");
        rejectionReasonBox.add(rejectionReasonArea);
        saveRejectionReasonButton = new WfmButton2(wfmStrings.save(), event -> {
            //set rejection reason comment and update to reject
            saveRejectionReasonButton.setEnabled(false);
            appraisalItem.setRejectionReasonComment(rejectionReasonArea.getText());
            appraisalItem.setStatusCode(DepartmentPeriodAppraisalItem.PERIOD_REJECTED);
            update(appraisalItem);
        });
        saveRejectionReasonButton.ensureDebugId(test_code_ID_name + "rejection_reason_save_button");
        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, event -> {
            //
            rejectionReasonBox.close();
        });
        cancelButton.ensureDebugId(test_code_ID_name + "rejection_reason_cancel_button");
        rejectionReasonBox.addButton(saveRejectionReasonButton);
        rejectionReasonBox.addButton(cancelButton);
        rejectionReasonBox.setWidth("200px");

        rejectionReasonBox.open();
    }

    private void update(final DepartmentPeriodAppraisalItem item) {
        LoadingPanel.loading(true);
        AssessmentService.App.get().updateDepartmentPeriodAppraisal(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                if (saveRejectionReasonButton != null) {
                    saveRejectionReasonButton.setEnabled(true);
                }
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                if (item.getStatusCode().equals(DepartmentPeriodAppraisalItem.PERIOD_APPROVED)) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyApproved(), hrmsStrings.appraisalType()), Info.Type.INFO);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyRejected(), wfmStrings.appraisal()), Info.Type.INFO);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PERIOD_APPRAISAL_CHANGED, "", AppraisalApprovalView.this);
                if (rejectionReasonBox != null) {
                    rejectionReasonBox.close();
                }
                closeTab();
            }
        });
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