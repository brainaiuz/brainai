package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.ui.view.DepartmentGoalChart;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.ui.DepartmentGoalAddEditView;
import com.edatasite.workforce.gwt.hrms.client.ui.DepartmentGoalAssigneeViewTab;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.Date;


public class DepartmentGoalView extends DepartmentGoalAddEditView implements FormHasCustomFieldInterface, Constants, NoColapse {

    private final String formView = "goal_summary_view_";

    private TextArea2 description;
    private DepartmentGoalAssigneeViewTab assigneeSelector;

    private HTML title, startDate, toDate,
            resolver, department,
            targetAndGoal, location, weightWidget;

    private DepartmentGoalChart chart;

    public DepartmentGoalView(Integer objectId) {
        super("summary", wfmStrings.summaryView());
        this.objectId = objectId;
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

    public void initialize() {

        LoadingPanel.loading(true);

        title = initHTML();
        title.getElement().setId(formView + "title");

        location = initHTML();
        location.getElement().setId(formView + "location");

        department = initHTML();
        department.getElement().setId(formView + "project_or_department");

        startDate = initHTML();
        startDate.getElement().setId(formView + "start_date");

        toDate = initHTML();
        toDate.getElement().setId(formView + "to_date");

        targetAndGoal = initHTML();
        targetAndGoal.getElement().setId(formView + "target_and_goal");



        weightWidget = initHTML();
        weightWidget.getElement().setId(formView + "weightWidget");


        description = new TextArea2();
        description.hideCharacterLimitPanel();
        description.setReadOnly(true);
        description.setSize("100%", "150px");
        description.getElement().setId(formView + "description");

        resolver = initHTML();
        resolver.getElement().setId(formView + "resolver");

        assigneeSelector = new DepartmentGoalAssigneeViewTab(wfmStrings.thereAreNoAssigneesYet());
        assigneeSelector.setSize("100%", "200px");
        assigneeSelector.getElement().setId(formView + "assign_selector");

        attachment = new GeneralFileUpload(F_DEP_GOAL, objectId, objectId);
        attachment.getPanel().getElement().setId(formView + "attachments");

        MaterialPanel chartTabBar = new MaterialPanel("pg_leave__calendar box-bg--1 box-radius");
        chart = new DepartmentGoalChart();
        chartTabBar.add(chart);


        LoadingPanel.loading(true);
        addFieldsToForm();
        drawFooter();
    }

        public void addFieldsToForm() {

        addTitleField(CustomFormConstants.GOAL_DETAILS, wfmStrings.basicDetails());
        addTitleField(CustomFormConstants.ASSIGNEES, wfmStrings.assignees());
        addTitleField(CustomFormConstants.ATTACHMENTS_TITLE, wfmStrings.attachments());

        addField(CustomFormConstants.GOAL_TITLE, title, getTitle(wfmStrings.title()));
        addField(CustomFormConstants.LOCATIONS, location, getTitle(wfmStrings.location()));
        addField(CustomFormConstants.GOAL_PROORDEP, department, getTitle(wfmStrings.department()));

        addField(CustomFormConstants.GOAL_START_DATE, new GRow(new GColumn(GColumnEnum.COL_3, startDate), new GColumn(GColumnEnum.COL_3, toDate)), getTitle(wfmStrings.period()));

        addField(CustomFormConstants.DEPARTMENT_TARGET_GOAL_WIDGET,targetAndGoal, getTitle(wfmStrings.targetAndActual()));

        addField(CustomFormConstants.DEPARTMENT_GOAL_WEIGHT_WIDGET, weightWidget, getTitle(wfmStrings.weight()));

        addField(CustomFormConstants.GOAL_DESCRIPTION, description, getTitle(wfmStrings.description()));


        addField(CustomFormConstants.GOAL_RESOLVER, resolver, getTitle(wfmStrings.resolver()));
        addField(CustomFormConstants.GOAL_ASSIGNEES, assigneeSelector, null);

        addField(CustomFormConstants.ATTACHMENTS, attachment, null);

        addField(CustomFormConstants.GOAL_CHART, chart, null);

        getCustomFieldUtil().drawCustomFields(this, objectId, true);

        show();
    }

    public void fillFieldWithValue() {
        setInnerHTML(title, item.getTitle());
        setInnerHTML(location, item.getLocation() != null ? item.getLocation() : "");
        setInnerHTML(department, item.getDepartment());

        setInnerHTML(startDate, DateUtils.format(item.getFromDate()));
        setInnerHTML(toDate, DateUtils.format(item.getToDate()));
        totalActual = 0.d;
        if (item.getGoalAssigneeItem() != null) {
            for (GoalAssigneeItem item : item.getGoalAssigneeItem()) {
                totalActual += item.getActual();
            }
        }
        setInnerHTML(targetAndGoal, item.getTargetGoal() != null ? item.getTargetGoal() + "/" + totalActual : "0/0");
        setInnerHTML(weightWidget, item.getAvialableWeight() != null ? item.getAvialableWeight() + "/" + item.getDepartmentGoalWeight() : "");


        if (!Utils.isNullOrEmpty(item.getDescription())) {
            description.setText(item.getDescription().replace("\r\n", "\\r\\n"));
        }

        setInnerHTML(resolver, item.getResolver());

        assigneeSelector.setItem(item);
        assigneeSelector.draw();

        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields(), true);

        chart.drawChart(item);

    }

    private void drawFooter() {
        NoteHistoryWidget noteHistoryWidget = new NoteHistoryWidget(callback -> {
            if (objectId == null) return;
            AvailabilityService.App.get().loadGoalHistory(objectId, callback);
        });
        if (objectId != null) {
            noteHistoryWidget.setSaveIntoDatabase(historyItem -> {
                if (historyItem != null) {
                    LoadingPanel.loading(true);
                    AvailabilityService.App.get().createGoalHistory(objectId, historyItem, new AsyncCallback<Integer>() {
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

        }

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.setInitialClasses("informer-item history-notes-container");
        footer.addToLeftSide(informer);
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);
        if (Utils.hasRole(Constants.ADMIN)) {
            WfmButton2 customize = new WfmButton2(wfmStrings.customize(), WfmButton2.BTN_WHITE);
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
            });
            addButton(customize);
        }

        WfmButton2 dataEntry = new WfmButton2(wfmStrings.enterManually(), WfmButton2.BTN_SUCCESS);
        dataEntry.setTitle(wfmStrings.save());
        dataEntry.addClickHandler(clickEvent -> {
            Date from = item != null && item.getFromDate() != null ? item.getFromDate().getNonConvertedDate() : null;
            Date to = item != null && item.getToDate() != null ? item.getToDate().getNonConvertedDate() : null;
            new DepartmentGoalDataLogPopUp(objectId, null, from, to, DepartmentGoalView.super::getDataToFillFields);
        });
        addButton(dataEntry);

        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_DEPARTMENT_GOAL)) {
            addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged("goaledit|editgoal/" + objectId + "/" + DEPARTMENT_GOAL, item.getTitle());
            });
        }
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    public String getIconStyle() {
        return "hrms employees-goal-list";
    }
}
