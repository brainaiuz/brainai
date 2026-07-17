package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;
import com.edatasite.workforce.gwt.core.client.rpc.GoalRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.ui.GoalAddEditView2;
import com.edatasite.workforce.gwt.hrms.client.ui.GoalAssigneeViewTab;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

import java.util.HashMap;


/**
 * User: Halim Kamolov
 * Date: 5/23/12
 * Time: 11:21 PM
 */
public class ViewGoalForm extends GoalAddEditView2 implements FormHasCustomFieldInterface, Constants, NoColapse {

    private HTML numberdata, selectedProjectGoal, companyGoal, assignees, title, actionSteps, startDate, toDate, status, goalCategory,
            progress, score, resolver, personalWeight, dataListBox, validityPeriod, measurementUnit, target, actual, scoreCalculation;
    private NoteWidget noteWidget;
    private TextArea2 description;
    private GoalAssigneeViewTab assigneeSelector;
    private final String goalSummaryView = "goal_summary_view_";
    private HasLinks linkingUtil;

    public ViewGoalForm(Integer objectId, String[] params) {
        super("summary", wfmStrings.summaryView());

        this.objectId = objectId;
        this.type = params[1];

        if (PERSONAL_GOAL.equals(type)) {
            isPersonGoal = true;
            this.viewName = hrmsStrings.personalGoal();
            folderType = F_PERS_GOAL;
        } else if (PROJECT_GOAL.equals(type)) {
            isProjectGoal = true;
            this.viewName = Property.get("projectgoal", hrmsStrings.projectgoal(), wfmStrings.project());
            folderType = F_PROJ_GOAL;
        } else if (BUSINESS_GOAL.equals(type)) {
            isBusinessGoal = true;
            this.viewName = hrmsStrings.businessGoal();
            folderType = F_BUSS_GOAL;
        }
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);
        MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
        if (Utils.hasPermission(deletePermission())) {
            if (Utils.hasRole(Constants.ADMIN)) {
                MaterialLink customize = new MaterialLink(wfmStrings.customize());
                customize.addClickHandler(click -> {
                    String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
                });
                options.add(customize);
            }

            if (Utils.hasPermission(deletePermission())) {
                MaterialLink deleteButton = new MaterialLink(wfmStrings.delete());
                deleteButton.addClickHandler(event -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            HrmsService.App.get().deleteGoal(objectId, item.getGoalCategory(), new AbstractAsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GOAL_DELETE, result, ViewGoalForm.this);
                                    closeTab();
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                options.add(deleteButton);
            }
        }

        MaterialLink appraisal = new MaterialLink(hrmsStrings.simpleAppraisal());
        appraisal.addClickHandler(click -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("initiate|add/" + item.getSelectedEmployeeID());
        });
        options.add(appraisal);

        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return "/goalViewPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                GoalRequestObject requestObject = new GoalRequestObject(objectId);
                requestObject.setType(type);
                HashMap<String, String> parametrs = requestObject.getRequestParams();
                return parametrs;
            }
        });
        addRightButton(pdf);

        if (Utils.hasPermission(editPermission())) {
            addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> SinksContainerFactory.entryPoint.onHistoryChanged("goaledit|editgoal/" + objectId + "/" + type, item.getTitle()));
        }

        ActionButton button = new ActionButton("");
        button.setVisible(false);
        addButton(button);
    }


    private String deletePermission() {
        String permission = null;
        if (isPersonGoal) {
            permission = PermissionConstants.HRMS_PERSONAL_GOAL_REMOVE;
        } else if (isProjectGoal) {
            permission = PermissionConstants.HRMS_PROJECT_GOAL_REMOVE;
        } else if (isBusinessGoal) {
            permission = PermissionConstants.HRMS_BUSINESS_GOAL_REMOVE;
        }
        return permission;
    }

    private String editPermission() {
        String permission = null;
        if (isPersonGoal) {
            permission = PermissionConstants.HRMS_EDIT_PERSONAL_GOAL;
        } else if (isProjectGoal) {
            permission = PermissionConstants.HRMS_EDIT_PROJECT_GOAL;
        } else if (isBusinessGoal) {
            permission = PermissionConstants.HRMS_EDIT_BUSINESS_GOAL;
        }
        return permission;
    }

    @Override
    public String getIconStyle() {
        return "hrms employees-goal-list";
    }

    public void initialize() {
        LoadingPanel.loading(true);

//goal notes
        String noteEntityName = isPersonGoal ? PERSONAL_GOAL : isProjectGoal ? PROJECT_GOAL : BUSINESS_GOAL;
        noteWidget = new NoteWidget(objectId, noteEntityName);
        noteWidget.getTextBox().getElement().setId(goalSummaryView + "notes");
        numberdata = initHTML();
        selectedProjectGoal = initHTML();
//goal company goal
        companyGoal = initHTML();
        companyGoal.getElement().setId(goalSummaryView + "company_goal");
//goal assignees
        assignees = initHTML();
        assignees.getElement().setId(goalSummaryView + "assignees");
//goal personal weight
        personalWeight = initHTML();
        personalWeight.getElement().setId(goalSummaryView + "personal_weight");
//goal title
        title = initHTML();
        title.getElement().setId(goalSummaryView + "title");
//goal description
        description = new TextArea2();
        description.hideCharacterLimitPanel();
        description.setReadOnly(true);
        description.addStyleName("GoalAddEditView2-description");
        description.setSize("100%", "150px");
        description.getElement().setId(goalSummaryView + "description");
//goal validity period
        validityPeriod = initHTML();
        validityPeriod.getElement().setId(goalSummaryView + "validity_period");
//goal measurement unit
        measurementUnit = initHTML();
        measurementUnit.getElement().setId(goalSummaryView + "measurement_unit");
//goal target
        target = initHTML();
        target.getElement().setId(goalSummaryView + "target");
//goal actual
        actual = initHTML();
        actual.getElement().setId(goalSummaryView + "actual");
//goal score calculation
        scoreCalculation = initHTML();
        scoreCalculation.getElement().setId(goalSummaryView + "score_calculation");
//goal
        dataListBox = initHTML();
        dataListBox.getElement().setId(goalSummaryView + "project");
        if (isProjectGoal) {
            dataListBoxString = Property.get(Constants.PROJECT, wfmStrings.project());
        }
//goal action steps
        actionSteps = initHTML();
        actionSteps.getElement().setId(goalSummaryView + "action_steps");
//goal start Date
        startDate = initHTML();
        startDate.getElement().setId(goalSummaryView + "start_date");
//goal to Date
        toDate = initHTML();
        toDate.getElement().setId(goalSummaryView + "to_date");
//goal status
        status = initHTML();
        status.getElement().setId(goalSummaryView + "status");
//goal category
        goalCategory = initHTML();
        goalCategory.getElement().setId(goalSummaryView + "goal_category");
//goal progress
        progress = initHTML();
        progress.getElement().setId(goalSummaryView + "progress");
//goal score
        score = initHTML();
        score.getElement().setId(goalSummaryView + "score");
//goal resolver
        resolver = initHTML();
        resolver.getElement().setId(goalSummaryView + "resolver");
//goal assign selector
        assigneeSelector = new GoalAssigneeViewTab(wfmStrings.thereAreNoAssigneesYet());
        assigneeSelector.setSize("100%", "200px");
        assigneeSelector.getElement().setId(goalSummaryView + "assign_selector");
//goal attachments
        attachment = new GeneralFileUpload(folderType, objectId, objectId);
        attachment.getPanel().getElement().setId(goalSummaryView + "attachments");

        LoadingPanel.loading(true);
        addFieldsToForm();
        drawFooter();
    }

    public void addFieldsToForm() {
        addTitleField(CustomFormConstants.GOAL_DETAILS, wfmStrings.basicDetails());
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        addField(CustomFormConstants.GOAL_NUMBER, numberdata, getTitle(wfmStrings.number()));
        addField(CustomFormConstants.PROJECT_GOAL_LOOKUP, selectedProjectGoal, getTitle(Property.get(Constants.PROJECT_GOAL, hrmsStrings.projectgoal())));
        addField(CustomFormConstants.GOAL_PERSONAL_ASSINESS, assignees, getTitle(wfmStrings.assignee()));
        addField(CustomFormConstants.GOAL_TITLE, title, getTitle(wfmStrings.title()));
        addField(CustomFormConstants.GOAL_DESCRIPTION, description, getTitle(wfmStrings.description()));
        GColumn column1 = new GColumn(GColumnEnum.COL_6, startDate);
        GColumn column2 = new GColumn(GColumnEnum.COL_6, toDate);

        addField(CustomFormConstants.GOAL_START_DATE, new GRow(column1, column2), getTitle(wfmStrings.period()));
        addField(CustomFormConstants.GOAL_PROORDEP, dataListBox, getTitle(dataListBoxString));
        addField(CustomFormConstants.COMPANY_GOAL, companyGoal, getTitle(hrmsStrings.companyGoal()));
        addField(CustomFormConstants.GOAL_ACTION_STEPS, actionSteps, getTitle(wfmStrings.actionSteps()));
        addField(CustomFormConstants.GOAL_RESOLVER, resolver, getTitle(wfmStrings.resolver()));
        addField(CustomFormConstants.GOAL_SCORE, score, getTitle(wfmStrings.score()));
        addField(CustomFormConstants.GOAL_PROGRESS, progress, getTitle(wfmStrings.progress()));//title progresga o'zgarishi kerak
        addField(CustomFormConstants.GOAL_STATUS, status, getTitle(wfmStrings.status()));
        addField(CustomFormConstants.GOAL_WEIGHT, personalWeight, getTitle(wfmStrings.weight()));
        addTitleField(CustomFormConstants.ASSIGNEES, wfmStrings.assignees());
        addField(CustomFormConstants.GOAL_ASSIGNEES, assigneeSelector, null);
        addTitleField(CustomFormConstants.ATTACHMENTS_TITLE, wfmStrings.attachments());
        addField(CustomFormConstants.ATTACHMENTS, attachment, null);
        addTitleField(CustomFormConstants.NOTES, wfmStrings.notes());
        addField(CustomFormConstants.CRM_NOTE, noteWidget, null);
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        addField(CustomFormConstants.GOAL_VALIDITY_PERIOD, validityPeriod, wfmStrings.validityPeriod());
        addField(CustomFormConstants.GOAL_MEASUREMENT_UNIT, measurementUnit, wfmStrings.measurementUnit());
        addField(CustomFormConstants.GOAL_TARGET, target, wfmStrings.target());
        addField(CustomFormConstants.GOAL_ACTUAL, actual, wfmStrings.actual());
        addField(CustomFormConstants.GOAL_SCORE_CALCULATION, scoreCalculation, wfmStrings.scoreCalculation());
        if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_LINKS)) {
            VerticalPanel addLinkAndLinks = new VerticalPanel();
            addLinkAndLinks.add(getLinkingUtil().getAddLink());
            addLinkAndLinks.add(getLinkingUtil().getLinksPanel());
            addLinkAndLinks.getElement().setId(goalSummaryView + "addLinkAndLinks");
            addTitleField(LINKS2, wfmStrings.links());
            showSection(LINKS2);
            addField(CustomFormConstants.LINKS, addLinkAndLinks, null);
        } else {
            hideSection(LINKS2);
        }
        show();
    }

    public void fillFieldWithValue() {

        GoalAssigneeItem assigneeItem = null;
        if (isPersonGoal && item.getGoalAssigneeItem() != null) {

            selectedEmployeeID = item.getSelectedEmployeeID();

            for (GoalAssigneeItem selectItem : item.getGoalAssigneeItem()) {
                if (selectItem.isAssignee()) {
                    assigneeItem = selectItem;
                    setInnerHTML(assignees, selectItem.getName());
                    break;
                }
            }
        }

        if (item.getGoalNumber() != null) {
            setInnerHTML(numberdata, item.getGoalNumber() != null ? item.getGoalNumber().getFirstNumberString() : "");
        }
        if (item.getSelectedProjectGoalId() != null) {
            selectedProjectGoal.setHTML("<a href=\"#goal%7Csummary/" + item.getSelectedProjectGoalId() + "/projectgoal\">" + item.getProjectGoalTitle() + "</a>");
        }
        setInnerHTML(companyGoal, item.getCompanyGoal());
        setInnerHTML(title, item.getTitle());
        if (!Utils.isNullOrEmpty(item.getDescription())) {
            description.setText(item.getDescription().replace("\r\n", "\\r\\n"));
        }
        if (isProjectGoal) {
            dataListBox.setHTML("<a href=\"#project%7Csummary/" + item.getProjectId() + "/null/false\">" + item.getProject() + "</a>");
        }
        setInnerHTML(actionSteps, item.getActionSteps());
        setInnerHTML(startDate, DateUtils.format(item.getFromDate()));
        setInnerHTML(toDate, DateUtils.format(item.getToDate()));
        setInnerHTML(status, item.getStatus());
        setInnerHTML(goalCategory, item.getGoalCategory());
        setInnerHTML(progress, item.getProgress().toString() + "%");
        setInnerHTML(score, (int) item.getWeight() + "");
        setInnerHTML(resolver, item.getResolver());
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields(), true);
        if (assigneeItem != null) {
            setInnerHTML(personalWeight, String.valueOf(assigneeItem.getWeight()));
        }

        if (item.getValidityPeriodItem() != null) {
            setInnerHTML(validityPeriod, item.getValidityPeriodItem().getName());
        }
        setInnerHTML(measurementUnit, item.getMeasurementUnit() != null ? item.getMeasurementUnit().getName() : "");
        if (assigneeItem != null) {
            setInnerHTML(target, String.valueOf(assigneeItem.getTarget()));
            setInnerHTML(actual, String.valueOf(assigneeItem.getActual()));
        }
        if (item.getScore() != null) {
            setInnerHTML(scoreCalculation, item.getScore().getName());
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_LINKS)) {
            getLinkingUtil().getTaggingView().setFromName(item.getTitle());
            getLinkingUtil().getTaggingView().setSelectedRelations(item.getRelations());
            getLinkingUtil().drawLinks();

        }
        assigneeSelector.setItem(item);
        assigneeSelector.draw();
    }

    private void drawFooter() {
        NoteHistoryWidget noteHistoryWidget = new NoteHistoryWidget(callback -> {
            if (objectId == null) {
                return;
            }
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

            noteHistoryWidget.setRemoveFromDatabase((hisItem) -> {
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
    }


    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(ViewGoalForm.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                protected Integer getRelationID() {
                    return objectId;
                }

                @Override
                protected String getRelationType() {
                    return isPersonGoal ? RelationItem.TYPE_PERSONAL_GOAL : isProjectGoal ? RelationItem.TYPE_PROJECT_GOAL : RelationItem.TYPE_BUSINESS_GOAL;
                }

                @Override
                protected String getRelationName() {
                    return item != null ? item.getTitle() : null;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
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