package com.edatasite.workforce.gwt.issue.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.KpiDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueItem;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueService;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Ilhombek
 * Date: 10/17/12
 * Time: 3:13 PM
 */
public class AddIssueView extends CustomForm2 implements Constants, HasLinksInterface, Colapse {

    protected static final ProjectStrings projectStrings = ProjectStrings.App.get();

    protected GeneralFileUpload fileUpload;
    protected TextArea2 issue_description;
    private WfmButton2 addNewProject;
    protected NoteWidget noteWidget;
    protected HasLinks linkingUtil;
    protected IssueItem issue_item;
    protected ArrayList<RelationItem> deletedRelations = new ArrayList<>();
    private final LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assignees_to_selector = new LinkedHashMap<>();
    private KpiCellTree assignSelectorN;
    private KpiCheckBox billable;
    private DateTimePicker dateTime;
    private Integer int_objectID;
    private KpiCheckBox issue_enable_timeSheet;
    private Div issue_billable_table;
    private TextBox issue_name;
    private Numbering issue_number;
    private DataListBox issue_reported_by;
    private DataListBox issue_resolver;
    private DataListBox issue_priority;
    private CRMLookUp issue_project;
    private DataListBox issue_status;
    private RadioButton issue_visibility_private;
    private RadioButton issue_visibility_public;
    private RadioButton issue_visibility_internal;
    private Integer relationID;
    private String relationName;
    private String relationType;
    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    private String test_code_ID_name = "add_issue_view_";
    private final boolean isChangeIssueProject = Utils.hasGenericAccess(GenericSettingsEnum.IS_CHANGE_ISSUE_PROJECT);
    private String issue_success_message = property.getSingular(wfmStrings.messSuccessfullySaved(), wfmStrings.issue());

    public AddIssueView() {
        super("addissue");
        setDescription(property.getSingular(projectStrings.addIssue(), wfmStrings.issue()));
    }

    public AddIssueView(Integer relationID, String relationType, String relationName) {
        this();
        this.relationID = relationID;
        this.relationType = relationType;
        this.relationName = relationName;
    }

    public AddIssueView(String name, String test_code_ID_name, Integer int_objectID) {
        super(name);
        this.test_code_ID_name = test_code_ID_name;
        this.int_objectID = int_objectID;
    }

    public AddIssueView(String name, String description, String test_code_ID_name, Integer int_objectID) {
        super(name, description);
        this.test_code_ID_name = test_code_ID_name;
        this.int_objectID = int_objectID;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    public String getIconStyle() {
        return "bgMark pm-edit";
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(AddIssueView.this) {
                @Override
                protected boolean isActionEditing() {
                    return true;
                }

                @Override
                protected Integer getRelationID() {
                    return int_objectID;
                }

                @Override
                protected String getRelationType() {
                    return RelationItem.TYPE_ISSUE;
                }

                @Override
                protected String getRelationName() {
                    return issue_item != null && issue_item.getName() != null ? issue_item.getName() : null;
                }
            };
        }
        return linkingUtil;
    }

    @Override
    protected void addButtons() {
        if (int_objectID == null) {
            MaterialLink save = new MaterialLink(wfmStrings.save());
            MaterialSplitButton splitButton = new MaterialSplitButton(save);

            save.addClickHandler(event -> save(true));


            MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
            saveAdd.addClickHandler(event -> save(false));
            splitButton.addItem(saveAdd);
            addButton(splitButton);
        } else {
            addButton(wfmStrings.update(), null, (test_code_ID_name + "update_button"), event -> {
                //update logic
                save(true);
            }).addStyleName(WfmButton2.BTN_PRIMARY);
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        IssueService.App.get().editIssueItem(int_objectID, relationID, new AbstractAsyncCallback<IssueItem>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(IssueItem result) {
                LoadingPanel.loading(false);
                issue_item = result;

                if (int_objectID == null) {
                    setDefaultValues();
                }
                //issue related items
                if (relationID != null && !RelationItem.TYPE_PROJECT.equals(relationType) && (issue_item.getRelations() == null || issue_item.getRelations().size() == 0)) {
                    issue_item.getRelations().add(new RelationItem(null, relationID, relationType, relationName, null, RelationItem.TYPE_ISSUE, null));
                }
                fillFormWithData();
            }
        });
        IssueService.App.get().editProjectItem(relationID, new AsyncCallback<ProjectItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ProjectItem projectItem) {
                LoadingPanel.loading(false);
                if (projectItem != null) {
                    issue_project.setSelected(projectItem);
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.ISSUE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Issues, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                }
                initialize();
                initializeForms();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {

    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.PM_ISSUE_ADD;
    }

    protected void fillFormWithData() {
        if (int_objectID != null) {
            issue_success_message = property.getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.issue());
        }
        //issue project
        registerTimeAssignedToProject(issue_item);
        //issue number
        if (issue_item.getNumberData() == null || (issue_item.getNumberData() != null && issue_item.getNumberData().getNumberString() == null && issue_item.getNumberData().getIntNumber() == null)) {
            generateIssueNumber();
        } else {
            issue_number.setNumberData(issue_item.getNumberData());
        }
        //issue name
        if (issue_item.getName() != null) {
            issue_name.setText(issue_item.getName());
        }
        //issue description
        if (issue_item.getDescription() != null) {
            issue_description.setText(issue_item.getDescription());
        }
        //issue public show to type
        if (int_objectID == null) {
            if (Utils.hasRole(CLIENT)) {
                issue_visibility_public.setValue(true);
            } else {
                issue_visibility_internal.setValue(true);
            }
        } else {
            if (issue_item.isPublic() == null) {
                issue_visibility_internal.setValue(true);
            } else if (issue_item.isPublic()) {
                issue_visibility_public.setValue(true);
            } else {
                issue_visibility_private.setValue(true);
            }
        }
        //issue period From
        if (issue_item.getStartDate() != null) {
            dateTime.getStartDatePicker().setDate(issue_item.getStartDate());
        }
        //issue period To
        if (issue_item.getEndDate() != null) {
            dateTime.getDueDatePicker().setDate(issue_item.getEndDate());
        }
        //issue priority
        issue_priority.setItems(issue_item.getPriorities());
        if (issue_priority.getItems() != null) {
            for (SelectItem item : issue_priority.getItems()) {
                if (item.getName().trim().equals(wfmStrings.medium())) {
                    issue_priority.setSelected(item.getId());
                    break;
                }
            }
        }
        if (issue_item.getPriorityID() != null) {
            issue_priority.setSelected(issue_item.getPriorityID());
        }
        //issue status
        issue_status.setItems(issue_item.getStatuses());
        if (issue_status.getItems() != null) {
            for (SelectItem item : issue_status.getItems()) {
                if (item.getName().trim().equals(wfmStrings.New())) {
                    issue_status.setSelected(item.getId());
                    break;
                }
            }
        }
        if (issue_item.getStatusID() != null) {
            issue_status.setSelected(issue_item.getStatusID());
        }
        //issue reported by
        issue_reported_by.setItems(issue_item.getReportedByItems());
        if (issue_item.getReportedByID() != null) {
            issue_reported_by.setSelected(issue_item.getReportedByID());
        }
        //issue enable timeSheet
        issue_enable_timeSheet.setValue(issue_item.isTimeSheetEnabled());
        if (issue_item.isTimeSheetEnabled()) {
            //
            visibleEnableTimeSheetOptionFields(true);
            //billable
            billable.setValue(issue_item.isBillable());
        } else {
            visibleEnableTimeSheetOptionFields(false);
        }
        //issue attachments
        fileUpload = new GeneralFileUpload(F_PR_ISSUE, issue_item.getProjectID(), issue_item.getObjectID());
        fileUpload.ensureDebugId(test_code_ID_name + "attachments");
        addTitleField(CustomFormConstants.ATTACHMENTS_TITLE, wfmStrings.attachments());
        addField(CustomFormConstants.ATTACHMENTS, fileUpload, getTitle(wfmStrings.attachments(), false));
        //issue linkage
        if (!Utils.hasRole(CLIENT)) {
            VerticalPanel addLinkAndLinks = new VerticalPanel();
            addLinkAndLinks.add(getLinkingUtil().getAddLink());
            addLinkAndLinks.add(getLinkingUtil().getLinksPanel());
            addLinkAndLinks.ensureDebugId(test_code_ID_name + "link_and_link_panel");
            addTitleField(CustomFormConstants.LINKS2, wfmStrings.links());
            addField(CustomFormConstants.LINKS, addLinkAndLinks, getTitle(wfmStrings.links(), false));
            showSection(CustomFormConstants.LINKS2);
        } else {
            hideSection(CustomFormConstants.LINKS2);
        }
        getLinkingUtil().getTaggingView().setFromName(issue_item.getName());
        getLinkingUtil().getTaggingView().setSelectedRelations(issue_item.getRelations());
        getLinkingUtil().drawLinks();

        //issue custom fields
        getCustomFieldUtil().fillCustomFieldsWithData(issue_item.getCustomFields());
        if (issue_item.getObjectID() != null && !(issue_item.getPermission() == EDIT)) {
            issue_project.setEnabled(false);
            issue_number.setEnabled(false);
            issue_name.setEnabled(false);
            issue_description.setEnabled(false);
            issue_priority.setEnabled(false);
            billable.setEnabled(false);
            dateTime.startDate.setEnabled(false);
            dateTime.dueDate.setEnabled(false);
            dateTime.startTime.setEnabled(false);
            dateTime.endTime.setEnabled(false);
            dateTime.allDay.setEnabled(false);
            issue_reported_by.setEnabled(false);
            issue_resolver.setEnabled(false);
            issue_enable_timeSheet.setEnabled(false);
        }
        if (int_objectID == null) {
            setDefaultValuesByFormProperty();
        }
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT_FIELD) != null && formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).getDefaultValue() != null) {
            issue_project.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).getSelectedId(), formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null && formPropertyMap.get(CustomFormConstants.NUMBER).getDefaultValue() != null) {
            issue_number.getTxtNumber().setText(formPropertyMap.get(CustomFormConstants.NUMBER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue() != null) {
            issue_name.setText(formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(PERIOD) != null && formPropertyMap.get(PERIOD).getDefaultValue() != null) {
            dateTime.setStartTime(formPropertyMap.get(PERIOD).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null && formPropertyMap.get(CustomFormConstants.PRIORITY).getDefaultValue() != null) {
            issue_priority.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PRIORITY).getSelectedId(), formPropertyMap.get(CustomFormConstants.PRIORITY).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue() != null) {
            issue_status.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY) != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY).getDefaultValue() != null) {
            issue_reported_by.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.REPORTED_BY).getSelectedId(), formPropertyMap.get(CustomFormConstants.REPORTED_BY).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESOLVER) != null && formPropertyMap.get(CustomFormConstants.RESOLVER).getDefaultValue() != null) {
            issue_resolver.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.RESOLVER).getSelectedId(), formPropertyMap.get(CustomFormConstants.RESOLVER).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET) != null && formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET).getDefaultValue() != null) {
            issue_enable_timeSheet.setText(formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BILLABLE) != null && formPropertyMap.get(CustomFormConstants.BILLABLE).getDefaultValue() != null) {
            issue_billable_table.setStyleName(formPropertyMap.get(CustomFormConstants.BILLABLE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEES) != null && formPropertyMap.get(CustomFormConstants.ASSIGNEES).getDefaultValue() != null) {
            issue_enable_timeSheet.setText(formPropertyMap.get(CustomFormConstants.ASSIGNEES).getDefaultValue());
        }
    }

    protected void initialize() {
        //issue project
        issue_project = new CRMLookUp(LookUpConstants.PROJECT);
        issue_project.setFullSearch(true);
        issue_project.addStyleName(DEFAULT_WIDTH);
        issue_project.ensureDebugId(test_code_ID_name + "project");
        issue_project.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            projectChangeT(null);
        });

        //AddNewProject
        if (Utils.hasPermission(PM_PROJECT_ADD)) {
            addNewProject = new WfmButton2("", WfmButton2.BTN_WHITE);
            addNewProject.addStyleName("ficon--plus");
            addNewProject.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("project|add/add/pm"));
            new KpiToolTip(addNewProject, projectStrings.addProject());
        }

        //issue number
        issue_number = new Numbering();
        issue_number.addStyleName(DEFAULT_WIDTH);
        issue_number.ensureDebugId(test_code_ID_name + "number");
        //issue name
        issue_name = new TextBox();
        issue_name.addStyleName(DEFAULT_WIDTH);
        issue_name.ensureDebugId(test_code_ID_name + "name");
        //issue description
        issue_description = new TextArea2(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        issue_description.ensureDebugId(test_code_ID_name + "description");
        //show to type (private)
        issue_visibility_private = new KpiRadioButton("showTo", wfmStrings.priv(), true);
        issue_visibility_private.ensureDebugId(test_code_ID_name + "show_to_type_private");
        //show to type (public)
        issue_visibility_public = new KpiRadioButton("showTo", wfmStrings.pub(), true);
        issue_visibility_public.ensureDebugId(test_code_ID_name + "show_to_type_public");
        //show to type (internal)
        issue_visibility_internal = new KpiRadioButton("showTo", wfmStrings.internal(), true);
        issue_visibility_internal.ensureDebugId(test_code_ID_name + "show_to_type_internal");
        if (Utils.hasRole(CLIENT)) {
            issue_visibility_public.setValue(true);
        } else {
            issue_visibility_internal.setValue(true);
        }
        //issue period From/To
        dateTime = new DateTimePicker();
        dateTime.setAllDay(true);
        dateTime.setStartDate(DateUtil.resetTime(new Date()));
        dateTime.setDueDate(DateUtil.getDayLastTime(new Date()));
        dateTime.getStartTime().setVisible(false);
        dateTime.getEndTime().setVisible(false);
        dateTime.getStartDatePicker().addValueChangeHandler(event -> dateTime.getStartDatePicker().removeStyleName(ERROR_FORM_STYLE));
        dateTime.getStartDatePicker().ensureDebugId(test_code_ID_name + "period_from");
        dateTime.getDueDatePicker().ensureDebugId(test_code_ID_name + "period_to");
        //issue priority
        issue_priority = new DataListBox();
        issue_priority.setAllowFirstItem(true);
        issue_priority.addStyleName(DEFAULT_WIDTH);
        issue_priority.ensureDebugId(test_code_ID_name + "priority");
        //issue status
        issue_status = new DataListBox();
        issue_status.setAllowFirstItem(true);
        issue_status.addStyleName(DEFAULT_WIDTH);
        issue_status.ensureDebugId(test_code_ID_name + "status");
        //issue reported by
        issue_reported_by = new DataListBox();
        issue_reported_by.setAllowFirstItem(true);
        issue_reported_by.addStyleName(DEFAULT_WIDTH);
        issue_reported_by.ensureDebugId(test_code_ID_name + "reported_by");
        //issue resolver
        issue_resolver = new DataListBox();
        issue_resolver.setAllowFirstItem(true);
        issue_resolver.setEnabled(false);
        issue_resolver.addStyleName(DEFAULT_WIDTH);
        issue_resolver.ensureDebugId(test_code_ID_name + "resolver");
        //issue enable timeSheet
        issue_enable_timeSheet = new KpiCheckBox(wfmStrings.enableTimesheet(), true);
        issue_enable_timeSheet.ensureDebugId(test_code_ID_name + "enable_timeSheet");
        //enable timeSheet listener
        issue_enable_timeSheet.addValueChangeHandler(event -> {
            visibleEnableTimeSheetOptionFields(event.getValue());
        });
        //issue billable
        billable = new KpiCheckBox();
        billable.ensureDebugId(test_code_ID_name + "billable");
        //issue assignees
        assignSelectorN = new KpiCellTree();
        assignSelectorN.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                selectedDataGrid.addRowCountChangeHandler(event -> {
                    if (selectedDataGrid.getList().size() > 0) {
                        //
                    }
                });
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
                selectedDataGrid.setColumnWidth(employee, 40, com.google.gwt.dom.client.Style.Unit.PCT);
                //estimate time
                Column<KpiTreeInfo, String> time = null;
                if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLED_MANNUAL_RU_ESTIMATE)) {
                    final TextInputCell textInputCell = new TextInputCell();
                    time = new Column<KpiTreeInfo, String>(textInputCell) {
                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return Utils.formatMinutes(object.getTime());
                        }
                    };
                    time.setFieldUpdater((index, object, value) -> {
                        if (!"".equals(value) && !"00:00".equals(value)) {
                            object.setTime(Utils.parseMinutes(value));
                        }
                    });
                } else {
                    time = new Column<KpiTreeInfo, String>(new TextCell()) {
                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return Utils.formatMinutes(object.getTime());
                        }
                    };
                }
                selectedDataGrid.addColumn(time, wfmStrings.estimatedTime());
                selectedDataGrid.setColumnWidth(time, 20, com.google.gwt.dom.client.Style.Unit.PCT);
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

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
            }
        });
        assignSelectorN.ensureDebugId(test_code_ID_name + "assignees");

        //issue notes
        noteWidget = new NoteWidget(int_objectID, PM_ISSUE);
        noteWidget.ensureDebugId(test_code_ID_name + "notes");
    }

    protected void initializeForms() {
        FlexTable issueVisibilityTab = new FlexTable();
        issueVisibilityTab.addStyleName("options-row default-width");
        issueVisibilityTab.setWidget(0, 0, issue_visibility_public);
        issueVisibilityTab.setWidget(0, 1, issue_visibility_internal);
        issueVisibilityTab.setWidget(0, 2, issue_visibility_private);

        FlexTable periodTab = new FlexTable();
        periodTab.addStyleName(DEFAULT_WIDTH);
        KpiDatePicker startDatePicker = dateTime.getStartDatePicker();
        KpiDatePicker dueDatePicker = dateTime.getDueDatePicker();
        periodTab.setWidget(0, 0, startDatePicker);
        periodTab.setWidget(0, 1, dueDatePicker);
        periodTab.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        periodTab.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        periodTab.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);
        periodTab.getCellFormatter().setVerticalAlignment(0, 3, HasVerticalAlignment.ALIGN_TOP);

        issue_billable_table = new Div();
        issue_billable_table.add(billable);

        //issue details -> 1
        addTitleField(CustomFormConstants.DETAILS, property.getSingular(wfmStrings.issueDetails(), wfmStrings.issue()));
        hideSection(CustomFormConstants.UPDATES);  // for Summary
        if (int_objectID == null || (isChangeIssueProject && Utils.hasRole(ADMIN))) {
            addField(CustomFormConstants.PROJECT_FIELD, new AdvancedInputGroup(null, issue_project, addNewProject, true, false), getTitle(Property.get(Constants.PROJECT, wfmStrings.project()), true));
        } else {
            addField(CustomFormConstants.PROJECT_FIELD, issue_project, getTitle(Property.get(Constants.PROJECT, wfmStrings.project()), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT_FIELD) != null) {
            addField(CustomFormConstants.PROJECT_FIELD, issue_project, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).getTitle() : wfmStrings.project(), formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).isInformation()) {
                new KpiToolTip(issue_project, formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).getInformationText());
            }

            issue_project.setEnabled(!formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).isDisabled());
        } else {
            addField(CustomFormConstants.PROJECT_FIELD, issue_project, getTitle(Property.get(Constants.PROJECT, wfmStrings.project()), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, issue_number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.NUMBER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.NUMBER).isInformation()) {
                new KpiToolTip(issue_number, formPropertyMap.get(CustomFormConstants.NUMBER).getInformationText());
            }

            issue_number.setEnabled(!formPropertyMap.get(CustomFormConstants.NUMBER).isDisabled());
        } else {
            addField(CustomFormConstants.NUMBER, issue_number, getTitle(wfmStrings.number(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, issue_name, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(CustomFormConstants.NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.NAME).isInformation()) {
                new KpiToolTip(issue_name, formPropertyMap.get(CustomFormConstants.NAME).getInformationText());
            }

            issue_name.setEnabled(!formPropertyMap.get(CustomFormConstants.NAME).isDisabled());
        } else {
            addField(CustomFormConstants.NAME, issue_name, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, issue_description, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation());
            if (formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation()) {
                new KpiToolTip(issue_description, formPropertyMap.get(CustomFormConstants.DESCRIPTION).getInformationText());
            }

            issue_description.setEnabled(!formPropertyMap.get(CustomFormConstants.DESCRIPTION).isDisabled());
        } else {
            addField(CustomFormConstants.DESCRIPTION, issue_description, null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VISIBILITY) != null) {
            addField(CustomFormConstants.VISIBILITY, issueVisibilityTab, getTitle(formPropertyMap.get(CustomFormConstants.VISIBILITY).isChanged() ? formPropertyMap.get(CustomFormConstants.VISIBILITY).getTitle() : wfmStrings.visibility(), formPropertyMap.get(CustomFormConstants.VISIBILITY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.VISIBILITY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.VISIBILITY).isInformation()) {
                new KpiToolTip(issueVisibilityTab, formPropertyMap.get(CustomFormConstants.VISIBILITY).getInformationText());
            }
        } else {
            addField(CustomFormConstants.VISIBILITY, issueVisibilityTab, getTitle(wfmStrings.visibility(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PERIOD) != null) {
            addField(CustomFormConstants.PERIOD, periodTab, getTitle(formPropertyMap.get(CustomFormConstants.PERIOD).isChanged() ? formPropertyMap.get(CustomFormConstants.PERIOD).getTitle() : wfmStrings.period(), formPropertyMap.get(CustomFormConstants.PERIOD).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PERIOD).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PERIOD).isInformation()) {
                new KpiToolTip(periodTab, formPropertyMap.get(CustomFormConstants.PERIOD).getInformationText());
            }

            dateTime.setEnabled(!formPropertyMap.get(CustomFormConstants.PERIOD).isDisabled());
        } else {
            addField(CustomFormConstants.PERIOD, periodTab, getTitle(wfmStrings.period(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null) {
            addField(CustomFormConstants.PRIORITY, issue_priority, getTitle(formPropertyMap.get(CustomFormConstants.PRIORITY).isChanged() ? formPropertyMap.get(CustomFormConstants.PRIORITY).getTitle() : wfmStrings.priority(), formPropertyMap.get(CustomFormConstants.PRIORITY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PRIORITY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PRIORITY).isInformation()) {
                new KpiToolTip(issue_priority, formPropertyMap.get(CustomFormConstants.PRIORITY).getInformationText());
            }

            issue_priority.setEnabled(!formPropertyMap.get(CustomFormConstants.PRIORITY).isDisabled());
        } else {
            addField(CustomFormConstants.PRIORITY, issue_priority, getTitle(wfmStrings.priority(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, issue_status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status(), formPropertyMap.get(CustomFormConstants.STATUS).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.STATUS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.STATUS).isInformation()) {
                new KpiToolTip(issue_status, formPropertyMap.get(CustomFormConstants.STATUS).getInformationText());
            }

            issue_status.setEnabled(!formPropertyMap.get(CustomFormConstants.STATUS).isDisabled());
        } else {
            addField(CustomFormConstants.STATUS, issue_status, getTitle(wfmStrings.status(), true));
        }

        if (!Utils.hasRole(CLIENT)) {
            addField(CustomFormConstants.REPORTED_BY, issue_reported_by, getTitle(wfmStrings.reportedBy()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY) != null) {
            addField(CustomFormConstants.REPORTED_BY, issue_reported_by, getTitle(formPropertyMap.get(CustomFormConstants.REPORTED_BY).isChanged() ? formPropertyMap.get(CustomFormConstants.REPORTED_BY).getTitle() : wfmStrings.reportedBy(), formPropertyMap.get(CustomFormConstants.REPORTED_BY).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.REPORTED_BY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.REPORTED_BY).isInformation()) {
                new KpiToolTip(issue_reported_by, formPropertyMap.get(CustomFormConstants.REPORTED_BY).getInformationText());
            }

            issue_reported_by.setEnabled(!formPropertyMap.get(CustomFormConstants.REPORTED_BY).isDisabled());
        } else {
            addField(CustomFormConstants.REPORTED_BY, issue_reported_by, getTitle(wfmStrings.reportedBy()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESOLVER) != null) {
            addField(CustomFormConstants.RESOLVER, issue_resolver, getTitle(formPropertyMap.get(CustomFormConstants.RESOLVER).isChanged() ? formPropertyMap.get(CustomFormConstants.RESOLVER).getTitle() : wfmStrings.resolverOwner(), formPropertyMap.get(CustomFormConstants.RESOLVER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.RESOLVER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.RESOLVER).isInformation()) {
                new KpiToolTip(issue_resolver, formPropertyMap.get(CustomFormConstants.RESOLVER).getInformationText());
            }

            issue_resolver.setEnabled(!formPropertyMap.get(CustomFormConstants.RESOLVER).isDisabled());
        } else {
            addField(CustomFormConstants.RESOLVER, issue_resolver, getTitle(wfmStrings.resolverOwner()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET) != null) {
            addField(CustomFormConstants.ENABLE_TIME_SHEET, issue_enable_timeSheet, getTitle(formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET).isChanged() ? formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET).getTitle() : wfmStrings.enableTimesheet(), formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET).isInformation());
            if (formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET).isInformation()) {
                new KpiToolTip(issue_enable_timeSheet, formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET).getInformationText());
            }

            issue_enable_timeSheet.setEnabled(!formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET).isDisabled());
        } else {
            addField(CustomFormConstants.ENABLE_TIME_SHEET, issue_enable_timeSheet, getTitle(wfmStrings.enableTimesheet().replace("&nbsp;", "")));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.BILLABLE) != null) {
            addField(CustomFormConstants.BILLABLE, issue_billable_table, getTitle(formPropertyMap.get(CustomFormConstants.BILLABLE).isChanged() ? formPropertyMap.get(CustomFormConstants.BILLABLE).getTitle() : wfmStrings.billable(), formPropertyMap.get(CustomFormConstants.BILLABLE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.BILLABLE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.BILLABLE).isInformation()) {
                new KpiToolTip(issue_billable_table, formPropertyMap.get(CustomFormConstants.BILLABLE).getInformationText());
            }

            issue_billable_table.setEnabled(!formPropertyMap.get(CustomFormConstants.BILLABLE).isDisabled());
        } else {
            addField(CustomFormConstants.BILLABLE, issue_billable_table, getTitle(wfmStrings.billable()));
        }
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        //issue project assignees -> 3
        addTitleField(CustomFormConstants.ASSIGNEE, wfmStrings.assignees());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEES) != null) {
            addField(CustomFormConstants.ASSIGNEES, assignSelectorN, getTitle(formPropertyMap.get(CustomFormConstants.ASSIGNEES).isChanged() ? formPropertyMap.get(CustomFormConstants.ASSIGNEES).getTitle() : wfmStrings.assignees(), formPropertyMap.get(CustomFormConstants.ASSIGNEES).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.ASSIGNEES).isInformation());
            if (formPropertyMap.get(CustomFormConstants.ASSIGNEES).isInformation()) {
                new KpiToolTip(assignSelectorN, formPropertyMap.get(CustomFormConstants.ASSIGNEES).getInformationText());
            }
        } else {
            addField(CustomFormConstants.ASSIGNEES, assignSelectorN, getTitle(wfmStrings.assignees(), true));
        }
        //attachments -> 4
        //issue notes -> 5
        addTitleField(CustomFormConstants.NOTES_TITLE, wfmStrings.notes());
        addField(CustomFormConstants.NOTES, noteWidget, getTitle(wfmStrings.notes(), false));
        visibleEnableTimeSheetOptionFields(false);
        //custom fields -> 2
        getCustomFieldUtil().drawCustomFields(this, int_objectID);
        show();
    }

    private void generateIssueNumber() {
        //generate issue number
        IssueService.App.get().generateIssueNumber(new AbstractAsyncCallback<NumberData>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(NumberData result) {
                issue_number.setNumberData(result);
            }
        });
    }

    private IdTime[] getSelectedAssignees() {
        ArrayList<IdTime> selectedAssignees = new ArrayList<>();
        IdTime iTime;
        if (assignSelectorN.getSelectedItems() != null && assignSelectorN.getSelectedData().size() > 0) {
            for (KpiTreeInfo assign : assignSelectorN.getSelectedData()) {
                iTime = new IdTime(assign.getId(), assign.getTime());
                selectedAssignees.add(iTime);
            }
        }
        return selectedAssignees.toArray(new IdTime[]{});
    }

    private void onShellOk(boolean closeTabT) {
        if (closeTabT) {
            closeTab();
        } else {
            linkingUtil = null;
            reInit();
        }
    }

    private void projectChangeT(final Integer resolverID) {
        SelectItem pro_item = issue_project.getSelectedItem();
        ArrayList<RelationItem> relations = new ArrayList<>();
        if (pro_item != null && pro_item.getId() != null && pro_item.getId() != 0) {
            IssueService.App.get().getAssigneesWithPositionsForIssue(pro_item.getId(), int_objectID, new AbstractAsyncCallback<PositionsSelectItem[]>() {
                @Override
                public void failure(Throwable caught) {
                }

                @Override
                public void success(PositionsSelectItem[] items) {
                    setAssignees(items);
                }
            });
            if (relationID == null) {
                relationID = pro_item.getId();
            }
            if (relationID != null) {
                relations.addAll(issue_item.getRelations());
                if (!relations.isEmpty()) {
                    for (RelationItem item : relations) {
                        if (RelationItem.TYPE_PROJECT.equals(item.getToType()) && (relationID.equals(item.getToID()) || item.getObjectID() == null)) {
                            issue_item.getRelations().remove(item);
                            item.setRemove(true);
                            deletedRelations.add(item);
                        }
                    }
                }
                issue_item.getRelations().add(new RelationItem(null, pro_item.getId(), RelationItem.TYPE_PROJECT, pro_item.getName(), issue_item.getObjectID(), RelationItem.TYPE_ISSUE, issue_item.getName()));
                getLinkingUtil().getTaggingView().setFromName(issue_item.getName());
                issue_item.getRelations().addAll(deletedRelations);
                getLinkingUtil().getTaggingView().setSelectedRelations(issue_item.getRelations());
                getLinkingUtil().drawLinks();
            }
            reSelectResolver(pro_item.getId(), resolverID);
        }
    }

    private void reInit() {
        initForm();
        initialize();
        initializeForms();
    }

    private void registerTimeAssignedToProject(IssueItem issue_item) {
        IssueService.App.get().getProjectsNotStartedOngoing(int_objectID, Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_NAME_SHOW_WITH_PROJECT_NUMBER), new AbstractAsyncCallback<ProjectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(ProjectItem[] items) {
                if (issue_item.getProjectID() != null) {
                    issue_project.setSelected(issue_item.getProjectID(), issue_item.getProjectName());
                }
                if (RelationItem.TYPE_PROJECT.equals(relationType) && relationID != null && issue_item.getProjectID() == null) {
                    issue_project.setSelected(relationID, relationName);
                }
                projectChangeT(issue_item.getResolverID());

//                if (issue_project.getSelectedItemID() == null && issue_item.getDefaultProjectID() != null) {
//                    issue_project.setSelected(new SelectItem(issue_item.getDefaultProjectID(), issue_item.getDefaultProjectName()));
//                }
            }
        });
    }

    private void reSelectResolver(Integer pro_itemID, final Integer resolverID) {
        if (pro_itemID != null && pro_itemID != 0) {
            IssueService.App.get().getResolversRelatedTo(PROJECT_ISSUE, pro_itemID, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {
                }

                @Override
                public void success(SelectItem[] items) {
                    issue_resolver.setItems(items);
                    if (resolverID != null) {
                        issue_resolver.setSelected(resolverID);
                    }
                }
            });
            issue_resolver.setEnabled(true);
        } else {
            issue_resolver.clear();
            issue_resolver.setEnabled(false);
        }
    }

    private void save(final boolean closeTabT) {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        setValues();
        LoadingPanel.loading(true);
        IssueService.App.get().createIssueItem(issue_item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(issue_success_message, Info.Type.INFO);
                onShellOk(closeTabT);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ISSUE_ADD, result, AddIssueView.this);
            }
        });
    }

    private void setAssignees(PositionsSelectItem[] assignees) {
        if (assignees == null) {
            return;
        }
        assignees_to_selector.clear();
        Map<Integer, List<PositionsSelectItem>> departments = new HashMap<>();
        for (PositionsSelectItem assign : assignees) {
            List<PositionsSelectItem> departmentMembers = departments.computeIfAbsent(assign.getDepartmentId(), k -> new ArrayList<>());
            departmentMembers.add(assign);
        }

        for (Integer departmentID : departments.keySet()) {
            List<PositionsSelectItem> departmentI = departments.get(departmentID);
            ArrayList<KpiTreeInfo> kpiTreeInfoList = new ArrayList<>();

            String departmentName = null;
            for (PositionsSelectItem item : departmentI) {
                departmentName = item.getDepartmentName();
                KpiTreeInfo info = new KpiTreeInfo(item.getId(), item.getName());
                info.setSelected(item.isAssignee());
                info.setEmployeeId(item.getId());
                info.setDepartmentId(departmentID);
                info.setTime(item.getTime());

                kpiTreeInfoList.add(info);
            }
            if (departmentName != null) {
                KpiTreeInfo departmentItem = new KpiTreeInfo(departmentID, departmentName);
                assignees_to_selector.put(departmentItem, kpiTreeInfoList);
            }
        }
        assignSelectorN.setItems(assignees_to_selector);
    }

    private void setValues() {
        //issue ID
        issue_item.setObjectID(int_objectID);
        //issue project
        if (int_objectID == null || (isChangeIssueProject && Utils.hasRole(ADMIN))) {
            issue_item.setProjectID(issue_project.getSelectedItem().getId());
            issue_item.setProjectName(issue_project.getSelectedItem().getName());
        }
        //issue numbering
        issue_item.setNumberData(issue_number.getNumberData(false));
        //issue name
        issue_item.setName(issue_name.getText());
        //issue description
        if (issue_description.getText() != null) {
            issue_item.setDescription(issue_description.getText());
        }
        //issue visibility
        issue_item.setPublic(issue_visibility_internal.getValue() ? null : issue_visibility_public.getValue());
        //issue period -> From
        issue_item.setStartDate(DateTimePicker.getDateTime(dateTime.getStartDate(), Utils.getDefaultCurrentUserTimeSlotStartTIME()));
        //issue period -> To
        issue_item.setEndDate(dateTime.getDueDate());
        if (dateTime.isAllDay()) {
            Date dueDate = (Date) dateTime.getDueDate().clone();
            issue_item.setEndDate(DateTimePicker.getDateTime(DateUtil.getDayLastTime(dueDate), Utils.getDefaultCurrentUserTimeSlotEndTIME()));
        }
        //issue priority
        if (issue_priority.isSomethingSelected()) {
            issue_item.setPriorityID(issue_priority.getSelectedItem().getId());
        }
        //issue status
        if (issue_status.isSomethingSelected()) {
            issue_item.setStatusID(issue_status.getSelectedItem().getId());
        }
        //issue reported by
        if (issue_reported_by != null && issue_reported_by.getSelectedItem() != null) {
            issue_item.setReportedByID(issue_reported_by.getSelectedItem().getId());
        }
        //issue resolver
        if (issue_resolver.isSomethingSelected()) {
            issue_item.setResolverID(issue_resolver.getSelectedItem().getId());
        }
        //issue timeSheet enabled
        issue_item.setTimeSheetEnabled(issue_enable_timeSheet.getValue());
        //issue billable
        if (issue_enable_timeSheet.getValue()) {
            issue_item.setBillable(billable.getValue());
        }
        //issue assignees
        issue_item.setAssignees(getSelectedAssignees());
        //issue attachments
        if (fileUpload != null) {
            issue_item.setAttachments(fileUpload.getAttachedFiles());
        }
        //issue notes
        issue_item.setNotes(noteWidget.getNewNotesToSave());
        //issue related links
        issue_item.setRelations(getLinkingUtil().getTaggingView().getSelectedRelations());
        issue_item.getRelations().addAll(deletedRelations);
        issue_item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
    }

    private boolean validate() {
        clearErrorStyle();
        int errors = 0;

        if (int_objectID == null || (isChangeIssueProject && Utils.hasRole(ADMIN))) {
            errors += markAsError(CustomFormConstants.PROJECT_, issue_project, issue_project.getSelectedItem() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null && formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.NUMBER, issue_number, !issue_number.validate() && !Validation.validateTextBoxRequired(issue_number.getTxtPrefix()) && !Validation.validateTextBoxRequired(issue_number.getTxtNumber()) && !Validation.validateTextBoxRequired(issue_number.getLastTxt()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.NAME, issue_name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.NAME).isChanged()
                    ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.issueName(), issue_name, formPropertyMap.get(CustomFormConstants.NAME).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null && formPropertyMap.get(CustomFormConstants.PRIORITY).isRequired()) {
            errors += markAsError(CustomFormConstants.PRIORITY, issue_priority, !Validation.validateListBoxRequired(issue_priority, new HTML(), projectStrings.selectPriority()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.STATUS, issue_status, !Validation.validateListBoxRequired(issue_status, new HTML(), wfmStrings.pleaseSelectStatus()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY) != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY).isRequired()) {
            errors += markAsError(CustomFormConstants.REPORTED_BY, issue_reported_by, !Validation.validateListBoxRequired(issue_reported_by));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESOLVER) != null && formPropertyMap.get(CustomFormConstants.RESOLVER).isRequired()) {
            errors += markAsError(CustomFormConstants.RESOLVER, issue_resolver, !Validation.validateListBoxRequired(issue_resolver));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VISIBILITY) != null && formPropertyMap.get(CustomFormConstants.VISIBILITY).isRequired()) {
            errors += markAsError(CustomFormConstants.VISIBILITY, issue_visibility_public, !Validation.validateRadioButtonRequired(issue_visibility_public));
        }


        errors += markAsError(CustomFormConstants.PERIOD, dateTime.getStartDatePicker(), !Validation.validateDate(dateTime.getStartDatePicker(), new HTML(), true));
        errors += markAsError(CustomFormConstants.PERIOD, dateTime.getDueDatePicker(), !Validation.validateDate(dateTime.getDueDatePicker(), new HTML(), true));
        if (dateTime.getStartDatePicker().getDate() != null && dateTime.getDueDatePicker().getDate() != null) {
            errors += markAsError(CustomFormConstants.PERIOD, dateTime.getStartDatePicker(), !Validation.validateDateOrder(dateTime.getStartDatePicker().getDate(), dateTime.getDueDatePicker().getDate(), null, dateTime.isAllDay()));
        }

        errors += getCustomFieldUtil().validateCustomFields();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEES) != null && formPropertyMap.get(CustomFormConstants.ASSIGNEES).isRequired()) {
            if (assignSelectorN.getSelectedData() == null || assignSelectorN.getSelectedData().size() == 0) {
                errors += markAsError("", assignSelectorN, true);
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void visibleEnableTimeSheetOptionFields(boolean visible) {
        if (issue_billable_table != null) {
            issue_billable_table.setVisible(visible);
        }
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

    @Override
    public String getPropertyCode() {
        return Constants.ISSUE;
    }
}
