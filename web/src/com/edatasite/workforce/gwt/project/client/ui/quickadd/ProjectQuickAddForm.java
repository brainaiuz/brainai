package com.edatasite.workforce.gwt.project.client.ui.quickadd;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
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

/**
 * Created by Hurshid on 12/17/2017.
 */
public class ProjectQuickAddForm extends Composite implements Constants {

    private static final ProjectQuickAddForm.ProjectQuickAddFormUiBinder ourUiBinder = GWT.create(ProjectQuickAddForm.ProjectQuickAddFormUiBinder.class);
    private final Date date = DateUtil.resetTime(new Date());
    private String relationType;
    private Integer relationID;
    private String relationName;

    interface ProjectQuickAddFormUiBinder extends UiBinder<Widget, ProjectQuickAddForm> {
    }

    private Integer parentProjectId;
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private final ProjectServiceAsync projectService = ProjectService.App.get();
    public ProjectQuickAddForm(String relationType, Integer relationID, Integer parentProjectId) {
        this();
        this.relationType = relationType;
        this.relationID = relationID;
        this.parentProjectId = parentProjectId;
        this.getRelationName(relationID, relationType);
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
    Label nameLabel;
    @UiField
    TextBox name;

    @UiField
    Label descriptionLabel;
    @UiField
    TextArea2 description;

    @UiField
    Label statusLabel;
    @UiField
    DataListBox status;

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

    @UiField
    Label managerLabel;
    @UiField
    DataListBox manager;

    private Numbering number;
    private NumberData numberData;
    private MultiSelectEmployeeLookUp assigneeLookUp;
    private ProjectSingleItem item;


    public ProjectQuickAddForm() {
        this.initWidget(ProjectQuickAddForm.ourUiBinder.createAndBindUi(this));

        this.initForm();
    }

    private void initForm() {
        this.details.setText(Property.get(Constants.PROJECT, ProjectQuickAddForm.wfmStrings.basicDetails(), ProjectQuickAddForm.wfmStrings.project()));
        this.involvedEmployeesTitle.setText(ProjectQuickAddForm.wfmStrings.involvedEmployees());
        this.numberLabel.setText(ProjectQuickAddForm.wfmStrings.number());
        this.nameLabel.setText(ProjectQuickAddForm.wfmStrings.name());
        this.descriptionLabel.setText(ProjectQuickAddForm.wfmStrings.description());
        this.statusLabel.setText(ProjectQuickAddForm.wfmStrings.status());
        this.startDateLabel.setText(ProjectQuickAddForm.wfmStrings.startDate());
        this.dueDateLabel.setText(ProjectQuickAddForm.wfmStrings.dueDate());
        this.assigneeLabel.setText(ProjectQuickAddForm.wfmStrings.assignees());
        this.managerLabel.setText(ProjectQuickAddForm.wfmStrings.manager());

        this.description.setMAX_LENGTH(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        this.description.counterLabel.setText("" + Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);

        this.number = new Numbering(false);
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_NUMBERING)) {
            this.number.setEnabled(true);
        }
        this.numberDiv.add(this.number);

        this.assigneeLookUp = new MultiSelectEmployeeLookUp();
        this.assigneeLookUp.getFilterParametrs().setHRMS(true);
        this.assigneeLookUp.getList().setWidth("100%");
        this.assigneeLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
            this.manager.clear();
            this.manager.setItems(this.assigneeLookUp.getSelectedItems().toArray(new SelectItem[]{}));
        });
        this.assignee.add(this.assigneeLookUp);


        this.startDate.setDate(this.date);
    }

    public void getProjectQuickData() {
        LoadingPanel.loading(true, this.panel);
        this.generateProjectNumber(new Date());
        this.projectService.getProjectStatuses(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false, ProjectQuickAddForm.this.panel);
            }

            public void success(SelectItem[] object) {
                LoadingPanel.loading(false, ProjectQuickAddForm.this.panel);
                ProjectQuickAddForm.this.status.setItems(object);
                if (object.length > 0) {
                    ProjectQuickAddForm.this.status.setSelected(object[0].getId());
                }
                SelectItem defaultUser = ProjectQuickAddForm.this.assigneeLookUp.selectCurrentUser();
                SelectItem user = defaultUser == null ? new SelectItem(Utils.getUserID(), Utils.getFullName()) : defaultUser;
                ProjectQuickAddForm.this.manager.setItems(new SelectItem[]{user});
                ProjectQuickAddForm.this.manager.setSelected(user);
            }
        });
    }

    private void generateProjectNumber(Date date) {
        this.projectService.generateProjectNumber(date, null, null, new AbstractAsyncCallback<NumberData>() {
            public void failure(Throwable caught) {
            }

            public void success(NumberData result) {
                ProjectQuickAddForm.this.numberData = result;
                ProjectQuickAddForm.this.number.setNumberData(ProjectQuickAddForm.this.numberData);
            }
        });
    }

    public void save() {
        LoadingPanel.loading(true, this.panel);
        this.setValues();
        this.projectService.saveProject(this.item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                ProjectQuickAddForm.this.command.execute(-1);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false);

                if (ProjectQuickAddForm.this.parentProjectId != null) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUB_PROJECT_ADD, result, ProjectQuickAddForm.this);
                } else {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_ADD, result, ProjectQuickAddForm.this);
                }
                Info.show(Property.get(Constants.PROJECT, ProjectQuickAddForm.wfmStrings.messSuccessfullyAdded(), ProjectQuickAddForm.wfmStrings.project()), Info.Type.INFO);
                if (ProjectQuickAddForm.this.command != null) {
                    ProjectQuickAddForm.this.command.execute(result);
                }
            }
        });
    }

    private void setValues() {
        this.item = new ProjectSingleItem();
        this.item.setName(this.name.getText());
        this.item.setDescription(this.description.getText());
        ProjectMember[] pMembers = new ProjectMember[this.assigneeLookUp.getSelectedItems().size()];
        for (int i = 0; i < this.assigneeLookUp.getSelectedItems().size(); i++) {
            pMembers[i] = new ProjectMember();
            pMembers[i].setId(this.assigneeLookUp.getSelectedItems().get(i).getId());
        }
        this.item.setProjectMembers(pMembers);
        this.item.setManagerId(this.manager.getSelectedId());
        this.item.setStartDate(this.startDate.getDate());
        this.item.setEndDate(this.dueDate.getDate());

        if (this.numberData != null) {
            this.numberData = this.number.getNumberData(false);
            this.item.setNumberData(this.numberData);
        }
        this.item.setStatusId(this.status.getSelectedId());
        if (this.relationID != null && RelationItem.TYPE_CRM_ACCOUNT.equalsIgnoreCase(this.relationType)) {
            this.item.setClientId(this.relationID);
        }
        this.item.setParentId(this.parentProjectId);
        if (this.relationType != null && this.relationID != null) {
            ArrayList<RelationItem> relationItems = new ArrayList<>();
            relationItems.add(RelationItem.newEventRelation(this.relationType, this.relationID, this.relationName));
            this.item.setRelations(relationItems);
        }
    }

    public boolean validate() {
        int errors = 0;
        this.removeErrorStyle();

        if (this.name.getText() == null || "".equals(this.name.getText().trim())) {
            this.name.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (!this.number.validate()) {
            this.number.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (!Validation.validateDate(this.startDate)) {
            this.startDate.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (!Validation.validateDate(this.dueDate)) {
            this.dueDate.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (!this.status.isSomethingSelected()) {
            this.status.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (this.assigneeLookUp.getSelectedItems().size() < 1) {
            this.assigneeLookUp.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (this.manager.getSelectedItem() == null) {
            this.manager.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }

        if (errors > 0) {
            Info.warn(ProjectQuickAddForm.wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    private void removeErrorStyle() {
        this.name.removeStyleName(Constants.ERROR_FORM_STYLE);
        this.number.removeStyleName(Constants.ERROR_FORM_STYLE);
        this.startDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        this.dueDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        this.status.removeStyleName(Constants.ERROR_FORM_STYLE);
        this.assigneeLookUp.removeStyleName(Constants.ERROR_FORM_STYLE);
        this.manager.removeStyleName(Constants.ERROR_FORM_STYLE);
    }

    public void clearForm() {
        this.name.setText("");
        this.description.setText("");
        this.startDate.setDate(this.date);
        this.dueDate.clearSelected();
        this.dueDate.setDate(null);
        this.assigneeLookUp.clear();
        this.assigneeLookUp.clearOracleItems();
        this.manager.clear();
        this.removeErrorStyle();
    }

    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }

    private void getRelationName(Integer relationID, String relType) {
        AllInOneService.App.get().getRelationName(relationID, relType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    ProjectQuickAddForm.this.relationName = result;
                }
            }
        });
    }
}
