package com.edatasite.workforce.gwt.issue.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Icon;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Sherzod
 * Date: May 22, 2009
 * Time: 1:51:43 PM
 */
public class IssueSummaryView extends AddIssueView implements Colapse, HasLinksInterface {

    private final Integer int_objectID;
    private HTML issue_project;
    private HTML issue_name;
    private HTML issue_numbering;
    private HTML issue_period;
    private HTML issue_priority;
    private HTML issue_reported_by;
    private HTML issue_resolver;
    private HTML issue_status;
    private HTML issue_timeSheet_enabled;
    private HTML issue_billable;
    private HTML issue_visibility;
    private HTML issue_created_by;
    private HTML issue_created_date;
    private HTML issue_last_updated_by;
    private HTML issue_last_updated_date;
    private Icon timerIcon;
    private FormHasCustomField customFieldUtil;
    private HTML description;
    private KpiDataGrid<PositionsSelectItem> dataGrid;
    private ListDataProvider<PositionsSelectItem> dataProvider;
    private ColumnSortEvent.ListHandler<PositionsSelectItem> listHandler;
    public static final ProvidesKey<PositionsSelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId();
    private final String test_code_ID_name = "summary_issue_view_";
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    public IssueSummaryView(Integer int_objectID) {
        super("summary", "summary_issue_view_", int_objectID);
        setDescription(property.getSingular(wfmStrings.summaryView(), wfmStrings.issue()));
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
        return "issues issue-list";
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(IssueSummaryView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
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

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);
        if (Utils.hasPermission(PermissionConstants.PM_ISSUE_REMOVE) || (issue_item != null && issue_item.getCreatedID() != null && Utils.getUserID().equals(issue_item.getCreatedID()))) {
            MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
            if (Utils.hasRole(Constants.ADMIN)) {
                MaterialLink customize = new MaterialLink(wfmStrings.customize());
                customize.addClickHandler(click -> {
                    String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
                });
                options.add(customize);
            }

            //delete button
            if (Utils.hasPermission(PermissionConstants.PM_ISSUE_REMOVE) || (issue_item != null && issue_item.getCreatedID() != null && Utils.getUserID().equals(issue_item.getCreatedID()))) {
                MaterialLink deleteButton = new MaterialLink(wfmStrings.delete());
                deleteButton.addClickHandler(clickEvent -> {

                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            IssueService.App.get().deleteIssue(issue_item.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                @Override
                                public void failure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Boolean result) {
                                    LoadingPanel.loading(false);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ISSUE_DELETE, result, IssueSummaryView.this);
                                    Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.issue()), Info.Type.INFO);
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

        ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
            @Override
            public String getUrl() {
                return "/issueListViewPDFHandler";
            }

            @Override
            public boolean isLandscapeOptionEnabled() {
                return true;
            }

            @Override
            public HashMap<String, String> getParameters() {
                RequestObject requestObject = new RequestObject(int_objectID);
                HashMap<String, String> params = requestObject.getRequestParams();
                return params;
            }
        });
        addRightButton(pdf);

        //edit button
        if (Utils.hasPermission(PermissionConstants.PM_ISSUE_EDIT)) {
            addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> SinksContainerFactory.entryPoint.onHistoryChanged("issue|edit/" + int_objectID, issue_item.getNumberData().getNumberString(), issue_item.getName()));
        }


    }

    @Override
    protected void getDataToFillFields() {
        super.getDataToFillFields();
    }

    @Override
    protected String getFormID() {
        return super.getFormID();
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    @Override
    protected void fillFormWithData() {
        //container name
        if (container != null) {
            String taskName = issue_item.getName();
            if (taskName.length() > 25) {
                taskName = taskName.substring(0, 25) + "...";
            }
            container.setDescription(taskName);
        }
        //issue project
        issue_project.setHTML(issue_item.getProjectName() != null ? issue_item.getProjectName() : "");
        //issue numbering
        issue_numbering.setHTML(issue_item.getNumberData() != null && issue_item.getNumberData().getNumberString() != null ? issue_item.getNumberData().getNumberString() : "");
        //issue name
        issue_name.setHTML(issue_item.getName() != null ? issue_item.getName() : "");
        //issue description
        description.setHTML(!Utils.isNullOrEmpty(issue_item.getDescription()) ? issue_item.getDescription().replace("\n", "<br/>") : "");
        //issue visibility
        issue_visibility.setHTML(issue_item.isPublic() == null ? wfmStrings.internal() : (issue_item.isPublic() ? wfmStrings.pub() : wfmStrings.priv()));
        //issue period
        issue_period.setHTML(DateUtils.format(issue_item.getStartDate()) + " - " + DateUtils.format(issue_item.getEndDate()));
        //issue priority
        issue_priority.setHTML(issue_item.getPriorityName() != null ? issue_item.getPriorityName() : "");
        //issue status
        issue_status.setHTML(issue_item.getStatusName() != null ? issue_item.getStatusName() : "");
        //issue reported by
        issue_reported_by.setHTML(issue_item.getReportedByName() != null ? issue_item.getReportedByName() : "");
        //issue resolver
        issue_resolver.setHTML(issue_item.getResolverName() != null ? issue_item.getResolverName() : "");
        //issue timeSheet enabled
        issue_timeSheet_enabled.setHTML(issue_item.isTimeSheetEnabled() ? wfmStrings.enabled() : wfmStrings.disabled());
        //issue timeSheet billable
        issue_billable.setHTML(issue_item.isBillable() ? wfmStrings.billable() : wfmStrings.nonBillable());


        if (issue_item.isTimeSheetEnabled()) {
            addField(CustomFormConstants.BILLABLE, issue_billable, getTitle(wfmStrings.billable()));
        }
        //issue created by
        issue_created_by.setHTML(issue_item.getCreatedBy() != null ? issue_item.getCreatedBy() : "");
        //issue created date
        issue_created_date.setHTML(issue_item.getCreatedFrom() != null ? DateUtils.format(issue_item.getCreatedFrom()) : "");
        //issue last updated by
        issue_last_updated_by.setHTML(issue_item.getLastUpdatedBy() != null ? issue_item.getLastUpdatedBy() : "");
        //issue last updated date
        issue_last_updated_date.setHTML(issue_item.getLastUpdatedDate() != null ? DateUtils.format(issue_item.getLastUpdatedDate()) : "");
        //issue attachments
        fileUpload = new GeneralFileUpload(F_PR_ISSUE, issue_item.getProjectID(), issue_item.getObjectID());
        fileUpload.ensureDebugId(test_code_ID_name + "attachments");

        if (issue_item.isShowTimer()) {
            timerIcon = new Icon();
            timerIcon.setStyleName("ficon--trial");
            if (issue_item.isTimerIsStarted()) {
                timerIcon.addStyleName("ficon--trial-active");
            }
            timerIcon.setTitle(wfmStrings.timer());
            timerIcon.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("issueTimer|summary/" + issue_item.getObjectID().toString() + "/" + issue_item.getProjectID().toString(), issue_item.getNumberData().getNumberString(), issue_item.getName()));
            addField(CustomFormConstants.PM_ISSUE_TIMER, timerIcon, getTitle(wfmStrings.timer()));
        }

        if (!issue_item.isSupplier()) {
            addField(CustomFormConstants.ATTACHMENTS, fileUpload, null);
            addBottomWidgets();
        }

        //issue linkage
        if (!(Utils.hasRole(CLIENT) || issue_item.isSupplier())) {
            addField(CustomFormConstants.LINKS, getLinkingUtil().getLinkAndLinksPanelInVerticalPanel(), wfmStrings.links(), true);
            addField(CustomFormConstants.LINKS2, getLinkingUtil().getAddLink(), getTitle(wfmStrings.relatedTo()));
        }
//        getLinkingUtil().getTaggingView().setFromName(issue_item.getName());
        getLinkingUtil().getTaggingView().setSelectedRelations(issue_item.getRelations());
        getLinkingUtil().drawLinks();

        PositionsSelectItem[] issueEmployees = issue_item.getIssueEmployees();
        if (issueEmployees != null && issueEmployees.length > 0) {
            initDataProviderApply(issueEmployees);
        }
        initIssueAssigneesTableColumns();
        dataProvider.refresh();

        //issue custom fields
        getCustomFieldUtil().fillCustomFieldsWithData(issue_item.getCustomFields(), true);
    }

    @Override
    protected void initialize() {
        //issue project
        issue_project = new HTML();
        issue_project.addStyleName(DEFAULT_WIDTH);
        issue_project.ensureDebugId(test_code_ID_name + "project");
        //issue numbering
        issue_numbering = new HTML();
        issue_numbering.addStyleName(DEFAULT_WIDTH);
        issue_numbering.ensureDebugId(test_code_ID_name + "numbering");
        //issue name
        issue_name = new HTML();
        issue_name.addStyleName(DEFAULT_WIDTH);
        issue_name.ensureDebugId(test_code_ID_name + "name");
        //issue description
        description = new HTML();
        description.addStyleName(DEFAULT_WIDTH);
        description.ensureDebugId(test_code_ID_name + "description");
        //issue visibility
        issue_visibility = new HTML();
        issue_visibility.addStyleName(DEFAULT_WIDTH);
        issue_visibility.ensureDebugId(test_code_ID_name + "visibility");
        //issue period
        issue_period = new HTML();
        issue_period.addStyleName(DEFAULT_WIDTH);
        issue_period.ensureDebugId(test_code_ID_name + "period");
        //issue priority
        issue_priority = new HTML();
        issue_priority.addStyleName(DEFAULT_WIDTH);
        issue_priority.ensureDebugId(test_code_ID_name + "priority");
        //issue status
        issue_status = new HTML();
        issue_status.addStyleName(DEFAULT_WIDTH);
        issue_status.ensureDebugId(test_code_ID_name + "status");
        //issue reported by
        issue_reported_by = new HTML();
        issue_reported_by.addStyleName(DEFAULT_WIDTH);
        issue_reported_by.ensureDebugId(test_code_ID_name + "reported_by");
        //issue resolver
        issue_resolver = new HTML();
        issue_resolver.addStyleName(DEFAULT_WIDTH);
        issue_resolver.ensureDebugId(test_code_ID_name + "resolver");
        //issue timeSheet enabled
        issue_timeSheet_enabled = new HTML();
        issue_timeSheet_enabled.addStyleName(DEFAULT_WIDTH);
        issue_timeSheet_enabled.ensureDebugId(test_code_ID_name + "timeSheet");
        //issue timeSheet billable
        issue_billable = new HTML();
        issue_billable.addStyleName(DEFAULT_WIDTH);
        issue_billable.ensureDebugId(test_code_ID_name + "billable");
        //issue notes
        noteWidget = new NoteWidget(int_objectID, PM_ISSUE);
        noteWidget.ensureDebugId(test_code_ID_name + "notes");

        //issue created by
        issue_created_by = new HTML();
        issue_created_by.addStyleName(DEFAULT_WIDTH);
        issue_created_by.ensureDebugId(test_code_ID_name + "created_by");
        //issue created date
        issue_created_date = new HTML();
        issue_created_date.addStyleName(DEFAULT_WIDTH);
        issue_created_date.ensureDebugId(test_code_ID_name + "created_date");
        //issue last updated by
        issue_last_updated_by = new HTML();
        issue_last_updated_by.addStyleName(DEFAULT_WIDTH);
        issue_last_updated_by.ensureDebugId(test_code_ID_name + "last_updated_by");
        //issue last updated date
        issue_last_updated_date = new HTML();
        issue_last_updated_date.addStyleName(DEFAULT_WIDTH);
        issue_last_updated_date.ensureDebugId(test_code_ID_name + "last_updated_date");

        //issue assignees
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("200px");
        dataGrid.addStyleName("cellBasedWidget-mod cellBasedWidget-mod--static-body");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataGrid.ensureDebugId(test_code_ID_name + "assignees");
        listHandler = new ColumnSortEvent.ListHandler<>(dataProvider.getList());
        dataGrid.addColumnSortHandler(listHandler);
        dataProvider.addDataDisplay(dataGrid);

    }

    @Override
    protected void initializeForms() {
        //add fields
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //issue details -> 1
        addTitleField(CustomFormConstants.DETAILS, property.getSingular(wfmStrings.issueDetails(), wfmStrings.issue()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT_FIELD) != null) {
            addField(CustomFormConstants.PROJECT_FIELD, issue_project, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT_FIELD).getTitle() : wfmStrings.project()));
        } else {
            addField(CustomFormConstants.PROJECT_FIELD, issue_project, Property.get(Constants.PROJECT, wfmStrings.project()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, issue_numbering, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(CustomFormConstants.NUMBER, issue_numbering, getTitle(wfmStrings.number()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, issue_name, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(CustomFormConstants.NAME, issue_name, getTitle(wfmStrings.name()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, description, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description()));
        } else {
            addField(CustomFormConstants.DESCRIPTION, description, getTitle(wfmStrings.description()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VISIBILITY) != null) {
            addField(CustomFormConstants.VISIBILITY, issue_visibility, getTitle(formPropertyMap.get(CustomFormConstants.VISIBILITY).isChanged() ? formPropertyMap.get(CustomFormConstants.VISIBILITY).getTitle() : wfmStrings.visibility()));
        } else {
            addField(CustomFormConstants.VISIBILITY, issue_visibility, getTitle(wfmStrings.visibility()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PERIOD) != null) {
            addField(CustomFormConstants.PERIOD, issue_period, getTitle(formPropertyMap.get(CustomFormConstants.PERIOD).isChanged() ? formPropertyMap.get(CustomFormConstants.PERIOD).getTitle() : wfmStrings.period()));
        } else {
            addField(CustomFormConstants.PERIOD, issue_period, getTitle(wfmStrings.period()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null) {
            addField(CustomFormConstants.PRIORITY, issue_priority, getTitle(formPropertyMap.get(CustomFormConstants.PRIORITY).isChanged() ? formPropertyMap.get(CustomFormConstants.PRIORITY).getTitle() : wfmStrings.priority()));
        } else {
            addField(CustomFormConstants.PRIORITY, issue_priority, getTitle(wfmStrings.priority()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, issue_status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status()));
        } else {
            addField(CustomFormConstants.STATUS, issue_status, getTitle(wfmStrings.status()));
        }

        if (!Utils.hasRole(CLIENT)) {
            addField(CustomFormConstants.REPORTED_BY, issue_reported_by, getTitle(wfmStrings.reportedBy()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY) != null) {
            addField(CustomFormConstants.REPORTED_BY, issue_reported_by, getTitle(formPropertyMap.get(CustomFormConstants.REPORTED_BY).isChanged() ? formPropertyMap.get(CustomFormConstants.REPORTED_BY).getTitle() : wfmStrings.reportedBy()));
        } else {
            addField(CustomFormConstants.REPORTED_BY, issue_reported_by, getTitle(wfmStrings.reportedBy()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESOLVER) != null) {
            addField(CustomFormConstants.RESOLVER, issue_resolver, getTitle(formPropertyMap.get(CustomFormConstants.RESOLVER).isChanged() ? formPropertyMap.get(CustomFormConstants.RESOLVER).getTitle() : wfmStrings.resolverOwner()));
        } else {
            addField(CustomFormConstants.RESOLVER, issue_resolver, getTitle(wfmStrings.resolverOwner()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET) != null) {
            addField(CustomFormConstants.ENABLE_TIME_SHEET, issue_timeSheet_enabled, getTitle(formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET).isChanged() ? formPropertyMap.get(CustomFormConstants.ENABLE_TIME_SHEET).getTitle() : wfmStrings.timesheet()));
        } else {
            addField(CustomFormConstants.ENABLE_TIME_SHEET, issue_timeSheet_enabled, getTitle(Property.get(Constants.TIMESHEET, wfmStrings.timesheet())));
        }
        show();
    }

    private void addBottomWidgets() {
        //issue updates -> 2
        addTitleField(CustomFormConstants.UPDATES, wfmStrings.updates());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CREATED_BY) != null) {
            addField(CustomFormConstants.CREATED_BY, issue_created_by, getTitle(formPropertyMap.get(CustomFormConstants.CREATED_BY).isChanged() ? formPropertyMap.get(CustomFormConstants.CREATED_BY).getTitle() : wfmStrings.createdBy()));
        } else {
            addField(CustomFormConstants.CREATED_BY, issue_created_by, getTitle(wfmStrings.createdBy()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CREATED_DATE) != null) {
            addField(CustomFormConstants.CREATED_DATE, issue_created_date, getTitle(formPropertyMap.get(CustomFormConstants.CREATED_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.CREATED_DATE).getTitle() : wfmStrings.createdDate()));
        } else {
            addField(CustomFormConstants.CREATED_DATE, issue_created_date, getTitle(wfmStrings.createdDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.UPDATED_BY) != null) {
            addField(CustomFormConstants.UPDATED_BY, issue_last_updated_by, getTitle(formPropertyMap.get(CustomFormConstants.UPDATED_BY).isChanged() ? formPropertyMap.get(CustomFormConstants.UPDATED_BY).getTitle() : wfmStrings.modifiedBy()));
        } else {
            addField(CustomFormConstants.UPDATED_BY, issue_last_updated_by, getTitle(wfmStrings.modifiedBy()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.UPDATED_DATE) != null) {
            addField(CustomFormConstants.UPDATED_DATE, issue_last_updated_date, getTitle(formPropertyMap.get(CustomFormConstants.UPDATED_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.UPDATED_DATE).getTitle() : wfmStrings.modifiedDate()));
        } else {
            addField(CustomFormConstants.UPDATED_DATE, issue_last_updated_date, getTitle(wfmStrings.modifiedDate()));
        }
        //issue custom fields -> 3
        getCustomFieldUtil().drawCustomFields(this, int_objectID, true);
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        //issue assignees -> 4
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ASSIGNEES) != null) {
            addField(CustomFormConstants.ASSIGNEES, dataGrid, getTitle(formPropertyMap.get(CustomFormConstants.ASSIGNEES).isChanged() ? formPropertyMap.get(CustomFormConstants.ASSIGNEES).getTitle() : wfmStrings.assignees()));
        } else {
            addField(CustomFormConstants.ASSIGNEES, dataGrid, wfmStrings.assignees(), true);
        }

        addField(CustomFormConstants.NOTES, noteWidget, null);
    }

    private void initDataProviderApply(PositionsSelectItem[] issueEmployees) {
        List<PositionsSelectItem> employeeItems = dataProvider.getList();
        employeeItems.clear();
        Collections.addAll(employeeItems, issueEmployees);
    }

    private void initIssueAssigneesTableColumns() {
        //employee name
        Column<PositionsSelectItem, String> employee = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return object.getName() != null ? object.getName() : "";
            }
        };
        employee.setSortable(true);
        dataGrid.addColumn(employee, wfmStrings.employee());
        dataGrid.setColumnWidth(employee, 30, Style.Unit.PCT);
        listHandler.setComparator(employee, (o1, o2) -> o2.getName().compareToIgnoreCase(o1.getName()));
        // department Name
        Column<PositionsSelectItem, String> department = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return object.getDepartmentName();
            }
        };
        dataGrid.addColumn(department, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department(), wfmStrings.department()));
        dataGrid.setColumnWidth(department, 25, com.google.gwt.dom.client.Style.Unit.PCT);
        //employee position
        Column<PositionsSelectItem, String> position = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return object.getPositionName();
            }
        };
        dataGrid.addColumn(position, wfmStrings.position());
        dataGrid.setColumnWidth(position, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        //estimate time
        Column<PositionsSelectItem, String> estimatedTime = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return Utils.formatMinutes(object.getTime());
            }
        };
        estimatedTime.setSortable(true);
        dataGrid.addColumn(estimatedTime, wfmStrings.estimatedTime());
        dataGrid.setColumnWidth(estimatedTime, issue_item.isTimeSheetEnabled() ? 15 : 20, com.google.gwt.dom.client.Style.Unit.PCT);
        listHandler.setComparator(estimatedTime, (o1, o2) -> Utils.formatMinutes(o1.getTime()).compareToIgnoreCase(Utils.formatMinutes(o2.getTime())));
        //actual time
        if (issue_item.isTimeSheetEnabled()) {
            Column<PositionsSelectItem, String> actualTime = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(PositionsSelectItem object) {
                    return Utils.formatMinutes(object.getActualTime());
                }
            };
            dataGrid.addColumn(actualTime, wfmStrings.actualTime());
            dataGrid.setColumnWidth(actualTime, 10, com.google.gwt.dom.client.Style.Unit.PCT);
        }


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

    @Override
    public String getPropertyCode() {
        return Constants.ISSUE;
    }
}
