package com.edatasite.workforce.gwt.issue.client.ui.quickadd;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueItem;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueService;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueServiceAsync;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Hurshid on 1/6/2018.
 */
public class IssueQuickAddForm extends Composite implements Constants {
    interface IssueQuickAddFormUiBinder extends UiBinder<Widget, IssueQuickAddForm> {
    }

    private static IssueQuickAddFormUiBinder ourUiBinder = GWT.create(IssueQuickAddFormUiBinder.class);

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final IssueServiceAsync issueService = IssueService.App.get();
    private Date date = DateUtil.resetTime(new Date());
    private boolean billable = true;
    private HashMap<Integer, Integer> projectMembersMap = new HashMap<>();

    public IssueQuickAddForm() {
        initWidget(ourUiBinder.createAndBindUi(this));

        initForm();
    }

    protected ExtendedCommand command;

    @UiField
    HTMLPanel panel;
    @UiField
    Span details;
    @UiField
    Span involvedEmployeesTitle;

    @UiField
    Label numberLabel;
    @UiField
    HTMLPanel numberDiv;

    @UiField
    Label projectLabel;
    @UiField
    HTMLPanel projectDiv;

    @UiField
    Label nameLabel;
    @UiField
    TextBox name;

    @UiField
    Label descriptionLabel;
    @UiField
    TextArea2 description;

    @UiField
    Label startDateLabel;
    @UiField
    DatePicker startDate;

    @UiField
    Label dueDateLabel;
    @UiField
    DatePicker dueDate;

    @UiField
    Label assigneeLabel;
    @UiField
    HTMLPanel assignee;

    private Numbering number;
    private NumberData numberData;
    private MultiSelectEmployeeLookUp assigneeLookUp;
    private IssueItem item;
    private CRMLookUp projectLookUp;

    private Integer relationId;
    private String relationType;
    private String relationName;

    private void initForm() {
        details.setText(wfmStrings.issueDetails());
        involvedEmployeesTitle.setText(wfmStrings.assignees());
        numberLabel.setText(wfmStrings.number());
        nameLabel.setText(wfmStrings.name());
        descriptionLabel.setText(wfmStrings.description());
        startDateLabel.setText(wfmStrings.startDate());
        dueDateLabel.setText(wfmStrings.dueDate());
        assigneeLabel.setText(wfmStrings.assignees());
        projectLabel.setText(Property.get(Constants.PROJECT, wfmStrings.project()));


        description.getTextArea().ensureDebugId("issue_description");
        description.setMAX_LENGTH(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        description.counterLabel.setText("" + Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        number = new Numbering(false);
        numberDiv.add(number);

        projectLookUp = new CRMLookUp(LookUpConstants.PROJECT);
        projectLookUp.addStyleName(DEFAULT_WIDTH);
        projectLookUp.setFullSearch(true);
        projectLookUp.getTextBox().ensureDebugId("issue_project");
        projectLookUp.setWidth("100%");
        projectLookUp.getSuggestBox().setWidth("100%");
        projectLookUp.getSuggestBox().addSelectionHandler(event -> {
            assigneeLookUp.clear();
            assigneeLookUp.clearOracleItems();
            if (projectLookUp.isSelected()) {
                generateNumber();
                ProjectService.App.get().getProjectEmployees(projectLookUp.getSelectedItemID(), new AsyncCallback<ProjectMember[]>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                    }

                    @Override
                    public void onSuccess(ProjectMember[] projectMembers) {
                        projectMembersMap.clear();
                        for (ProjectMember member : projectMembers) {
                            projectMembersMap.put(member.getId(), member.getProjectEmployeeId());
                        }
                    }
                });
            }
        });
        projectDiv.add(projectLookUp);

        assigneeLookUp = new MultiSelectEmployeeLookUp();
        assigneeLookUp.getFilterParametrs().setHRMS(true);
        assigneeLookUp.setBeforeSearch(() -> {
            assigneeLookUp.getFilterParametrs().setProjectId(projectLookUp.getSelectedItemID());
            assigneeLookUp.getFilterParametrs().setIDsOnly(true);
        });
        assigneeLookUp.getList().setWidth("100%");
        assignee.add(assigneeLookUp);
        assigneeLookUp.getTextBox().ensureDebugId("issue_assignee");


        startDate.setDate(date);
        startDate.ensureDebugId("startDate");
        dueDate.setDate(date);
        dueDate.ensureDebugId("dueDate");
    }

    public void getIssueQuickData(Integer projectID) {
        LoadingPanel.loading(true, panel);
        if (projectID != null) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setProjectId(projectID);
            fp.setCategory(TASK);
            CommonService.App.get().getProjects(fp, false, Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_NAME_SHOW_WITH_PROJECT_NUMBER), new AbstractAsyncCallback<ProjectItem[]>() {
                @Override
                public void success(final ProjectItem[] object) {
                    if (object != null && object.length > 0) {
                        billable = object[0].isSelected();
                        projectLookUp.setSelected(new SelectItem(object[0].getId(), object[0].getName()));
                        generateNumber();
                    }
                }
            });
        } else {
            generateNumber();
        }
        issueService.editProjectItem(relationId, new AsyncCallback<ProjectItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ProjectItem projectItem) {
                    LoadingPanel.loading(false);
                if (projectItem != null) {
                    projectLookUp.setSelected(projectItem);
                }
            }
        });
    }

    private void generateNumber() {
        issueService.generateIssueNumber(new AbstractAsyncCallback<NumberData>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(NumberData result) {
                LoadingPanel.loading(false);
                numberData = result;
                number.setNumberData(numberData);
            }
        });
    }

    public void save() {
        LoadingPanel.loading(true, panel);
        setValues();
        issueService.createIssueItem(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                command.execute(-1);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ISSUE_ADD, result, IssueQuickAddForm.this);

                if (command != null) {
                    command.execute(result);
                }
            }
        });
    }

    private void setValues() {
        item = new IssueItem();
        item.setProjectID(projectLookUp.getSelectedItemID());
        item.setName(name.getText());
        item.setDescription(description.getText());
        if (assigneeLookUp.getSelectedItems() != null) {
            IdTime[] employees = new IdTime[assigneeLookUp.getSelectedItems().size()];
            for (int i = 0; i < assigneeLookUp.getSelectedItems().size(); i++) {
                employees[i] = new IdTime();
                employees[i].setId(projectMembersMap.get(assigneeLookUp.getSelectedItems().get(i).getId()));
            }
            item.setAssignees(employees);
        }
        item.setStartDate(startDate.getDate());
        item.setEndDate(dueDate.getDate());
        item.setBillable(billable);

        if (numberData != null) {
            numberData = number.getNumberData(false);
            item.setNumberData(numberData);
        }
        if (relationId != null && RelationItem.TYPE_TASK.equals(relationType)) {
            ArrayList<RelationItem> relations = new ArrayList<>();
            relations.add(new RelationItem(null, relationId, relationType, relationName, null, RelationItem.TYPE_ISSUE, null));
            item.setRelations(relations);
        } else if (projectLookUp.getSelectedItem() != null && RelationItem.TYPE_PROJECT.equals(relationType)) {
            SelectItem project = projectLookUp.getSelectedItem();
            ArrayList<RelationItem> relations = new ArrayList<>();
            relations.add(new RelationItem(null, project.getId(), relationType, project.getName(), null, RelationItem.TYPE_ISSUE, null));
            item.setRelations(relations);
        } else if (RelationItem.TYPE_DEPARTMENT.equals(relationType)) {
            ArrayList<RelationItem> relations = new ArrayList<>();
            relations.add(new RelationItem(null, relationId, relationType, null, null, RelationItem.TYPE_ISSUE, null));
            item.setRelations(relations);
        }
    }

    public boolean validate() {
        int errors = 0;
        removeErrorStyle();

        if (name.getText() == null || "".equals(name.getText().trim())) {
            name.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (!number.validate()) {
            number.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (!Validation.validateDate(startDate)) {
            startDate.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (!Validation.validateDate(dueDate)) {
            dueDate.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (projectLookUp.getSelectedItemID() == null) {
            projectLookUp.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }

        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    private void removeErrorStyle() {
        name.removeStyleName(Constants.ERROR_FORM_STYLE);
        number.removeStyleName(Constants.ERROR_FORM_STYLE);
        startDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        dueDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        projectLookUp.removeStyleName(Constants.ERROR_FORM_STYLE);
    }

    public void clearForm() {
        name.setText("");
        description.setText("");
        startDate.setDate(date);
        dueDate.setDate(date);
        projectLookUp.clear();
        projectLookUp.clearOracleItems();
        assigneeLookUp.clear();
        assigneeLookUp.clearOracleItems();
        removeErrorStyle();
    }

    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }
}