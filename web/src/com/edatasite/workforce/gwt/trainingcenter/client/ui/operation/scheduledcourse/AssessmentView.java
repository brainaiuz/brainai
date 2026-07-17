package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.AssessmentItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.QuestionarieResponseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

/**
 * Created with IntelliJ IDEA.
 * User: Abdullo
 * Date: 20.09.12
 * Time: 12:23
 */
public class AssessmentView extends CustomForm {

    private Integer objectID;
    private Integer stdQuestionarieId;
    private HTML questionName, totalPoints;
    private static TCStrings tcStrings = TCStrings.App.get();
    private static WfmStrings wfmStrings = WfmStrings.App.get();
    private static WfmMessages wfmMessages = WfmMessages.App.get();
    private AssessmentItem assessmentItem;
    private KpiDataGrid<QuestionarieResponseItem> dataGrid;

    public AssessmentView(Integer objectID) {
        super("assessmentview", "Assessment View");
        this.objectID = objectID;
    }

    public AssessmentView(Integer objectID, Integer stdQuestionarieID) {
        this(objectID);
        this.stdQuestionarieId = stdQuestionarieID;
    }

    public static final ProvidesKey<QuestionarieResponseItem> KEY_PROVIDER = item -> item.getId();

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setSize("100%", "100%");

        questionName = new HTML();
        totalPoints = new HTML();

        addTitleField(TRAINING_CENTER.INSTRUCTOR_REASSIGN.DETAIL, wfmStrings.assessment() + " " + wfmStrings.details());
        addTitleField(NAME, getTitle(wfmStrings.name()));
        addField(NAME, questionName);
        addTitleField(TRAINING_CENTER.TOTAL_POINT, getTitle(wfmStrings.total() + " Points"));
        addField(TRAINING_CENTER.TOTAL_POINT, totalPoints);
        addTitleField(TRAINING_CENTER.TC_RESPONSE, getTitle("Responses"));
        addField(TRAINING_CENTER.TC_RESPONSE, dataGrid);

        initTableColumn();
        show();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ASSESSMENT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        TCService.App.get().getAssessment(objectID, stdQuestionarieId, new AbstractAsyncCallback<AssessmentItem>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                GWT.log(caught.getMessage());
            }

            @Override
            public void success(AssessmentItem result) {
                LoadingPanel.loading(false);
                assessmentItem = result;
                fillFormWithData();
            }
        });
    }

    private void fillFormWithData() {
        dataGrid.supplyProvider(assessmentItem.getResponseItems());
        dataGrid.refresh();
        questionName.setText(assessmentItem.getName());
        totalPoints.setText(assessmentItem.getTotalPoints());
    }

    private void initTableColumn() {
        //Employee First Name
        Column<QuestionarieResponseItem, String> answer = new Column<QuestionarieResponseItem, String>(new TextCell()) {
            @Override
            public String getValue(QuestionarieResponseItem object) {
                return object.getAnswer();
            }
        };
        dataGrid.addColumn(answer, "Answer");
        dataGrid.setColumnWidth(answer, 50, com.google.gwt.dom.client.Style.Unit.PCT);
        //Employee Last Name
        Column<QuestionarieResponseItem, String> pointsEarnet = new Column<QuestionarieResponseItem, String>(new TextCell()) {
            @Override
            public String getValue(QuestionarieResponseItem object) {
                return object.getPointsEarnet() != null ? object.getPointsEarnet().toString() : "";
            }
        };
        dataGrid.addColumn(pointsEarnet, "Points Earnet");
        dataGrid.setColumnWidth(pointsEarnet, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        //Employee Email
        Column<QuestionarieResponseItem, String> questionNumber = new Column<QuestionarieResponseItem, String>(new TextCell()) {
            @Override
            public String getValue(QuestionarieResponseItem object) {
                return object.getQuestionNumber() != null ? object.getQuestionNumber().toString() : "";
            }
        };
        dataGrid.addColumn(questionNumber, "Question Number");
        dataGrid.setColumnWidth(questionNumber, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        //Activation Link
    }

    @Override
    protected void addButtons() {
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });

    }
}
