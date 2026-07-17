package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemGrid;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.project.CheckInLocationItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.ExportPdfButton;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.ProjectViewSinksContainer;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectViewItem;
import com.edatasite.workforce.gwt.project.client.ui.CheckInLocationWidget;
import com.edatasite.workforce.gwt.project.client.ui.view.customWidgets.LoggedTimeWidget;
import com.edatasite.workforce.gwt.project.client.ui.view.customWidgets.ProjectInvoicesWidget;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: Dilshod
 * Date: 13-Feb-2010
 * Time: 20:57:11
 */
public class NewProjectSummaryView extends CustomForm2 implements Constants, HasLinksInterface {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    private ProjectViewItem item;
    private final Integer projectID;
    private HasLinks linkingUtil;
    private NoteWidget noteWidget;
    private FlowPanel chartPanel, contractName;
    private FormHasCustomField customFieldUtil;
    private final boolean showCustomFields;

    private Widget richText;
    private HTML description;
    private HTML number, projectName, startDate, dueDate, status, completed, pManager, bManager, actualStartDate, actualEndDate, estimatedTime, timeSpent, actualTimeSpent, estimatedCost,
            actualCost, createdBy, createdDate, updatedBy, updatedDate, location, notStartedTasks, inProgressTasks, completedTasks, cencelledTasks, waitingSomeone, closedTasks, waitingHours, rejectedHours, employeeAssignment, clientBalance, clientRetainers,
            billable;

    private KpiDataGrid<PositionsSelectItem> dataGrid;
    private ListDataProvider<PositionsSelectItem> dataProvider;
    private ColumnSortEvent.ListHandler<PositionsSelectItem> listHandler;
    public static final ProvidesKey<PositionsSelectItem> KEY_PROVIDER = item -> item == null ? null : item.getId();
    public MultiTableNewUI checkInLocations;

    private FlexPanel projectInvoicePanel;
    private LoggedTimeWidget loggedTimeWidget;
    private WfmButton2 timeEntryButton;
    private final ProjectViewSinksContainer projectViewSinksContainer;
    private FooterInformer link;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    AtomicBoolean firstClick = new AtomicBoolean(true);

    public NewProjectSummaryView(Integer projectID, ProjectViewSinksContainer projectViewSinksContainer) {
        super("summary", wfmStrings.summaryView());
        this.projectViewSinksContainer = projectViewSinksContainer;
        this.projectID = projectID;
        showCustomFields = true;
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_MEMBER_ADD, NewProjectSummaryView.this, (sender, args) -> {
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_EDIT, NewProjectSummaryView.this, (sender, args) -> {
        });
    }

    @Override
    public String getIconStyle() {
        return "bgMark pm-welcome";
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(NewProjectSummaryView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                protected Integer getRelationID() {
                    return projectID;
                }

                @Override
                protected String getRelationType() {
                    return RelationItem.TYPE_PROJECT;
                }

                @Override
                protected String getRelationName() {
                    return item != null ? item.getName() : null;
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
    public Widget onInitialize() {
        if (Utils.hasRole(ADMIN)) {
            onAccessGranted();
            return null;
        }

        if (!projectViewSinksContainer.getViewByName().containsKey("edit")
                && Utils.hasPermission(PermissionConstants.PM_PROJECT_EDIT)) {
            ProjectEditView projectEditView = new ProjectEditView(projectID);
            projectEditView.ensureDebugId("projectEditView");
            projectViewSinksContainer.addView(projectEditView);
            View view = (View) projectViewSinksContainer.getViewByName().get(projectEditView.getName());
            if (view != null) {
                projectViewSinksContainer.initShortcutItem(view);
            }
        }
        onAccessGranted();
        return null;
    }

    private void onAccessGranted() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Project, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                initialize();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                formPropertyMap = result.getFormPropertyMap();
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                }
                NewProjectSummaryView.super.onInitialize();
            }
        });
    }

    public void initialize() {
        super.onInitialize();
    }

    protected void registerFields() {
        number = new HTML();
        number.addStyleName(DEFAULT_WIDTH);
        String project_summary = "project_sumary";
        number.ensureDebugId(project_summary + "number");

        projectName = new HTML();
        projectName.addStyleName(DEFAULT_WIDTH);
        projectName.ensureDebugId(project_summary + "projectName");

        richText = createRichText();
        richText.addStyleName(DEFAULT_WIDTH);

        startDate = new HTML();
        startDate.addStyleName(DEFAULT_WIDTH);
        startDate.ensureDebugId(project_summary + "startDate");

        dueDate = new HTML();
        dueDate.addStyleName(DEFAULT_WIDTH);
        dueDate.ensureDebugId(project_summary + "dueDate");

        status = new HTML();
        status.addStyleName(DEFAULT_WIDTH);
        status.ensureDebugId(project_summary + "status");

        billable = new HTML();
        billable.addStyleName(DEFAULT_WIDTH);
        billable.ensureDebugId(project_summary + "billable");

        completed = new HTML();
        completed.addStyleName(DEFAULT_WIDTH);
        completed.ensureDebugId(project_summary + "completed");

        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
//        dataGrid.setHeight("190px");
        dataGrid.addStyleName("cellBasedWidget-mod cellBasedWidget-mod--static-body");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataGrid.ensureDebugId(project_summary + "assignees");
        listHandler = new ColumnSortEvent.ListHandler<>(dataProvider.getList());
        dataGrid.addColumnSortHandler(listHandler);
        dataProvider.addDataDisplay(dataGrid);

        pManager = new HTML();
        pManager.addStyleName(DEFAULT_WIDTH);
        pManager.ensureDebugId(project_summary + "pManager");

        bManager = new HTML();
        bManager.addStyleName(DEFAULT_WIDTH);
        bManager.ensureDebugId(project_summary + "bManager");

        estimatedTime = new HTML();
        estimatedTime.addStyleName(DEFAULT_WIDTH);
        estimatedTime.ensureDebugId(project_summary + "estimatedTime");

        timeSpent = new HTML();
        timeSpent.addStyleName(DEFAULT_WIDTH);
        timeSpent.ensureDebugId(project_summary + "timeSpent");

        estimatedCost = new HTML();
        estimatedCost.addStyleName(DEFAULT_WIDTH);
        estimatedCost.ensureDebugId(project_summary + "estimatedCost");

        actualCost = new HTML();
        actualCost.addStyleName(DEFAULT_WIDTH);
        actualCost.ensureDebugId(project_summary + "actualCost");

        actualStartDate = new HTML();
        actualStartDate.addStyleName(DEFAULT_WIDTH);
        actualStartDate.ensureDebugId(project_summary + "actualStartDate");

        actualEndDate = new HTML();
        actualEndDate.addStyleName(DEFAULT_WIDTH);
        actualEndDate.ensureDebugId(project_summary + "actualEndDate");

        actualTimeSpent = new HTML();
        actualTimeSpent.addStyleName(DEFAULT_WIDTH);
        actualTimeSpent.ensureDebugId(project_summary + "actualTimeSpent");

        waitingHours = new HTML();
        waitingHours.addStyleName(DEFAULT_WIDTH);
        waitingHours.ensureDebugId(project_summary + "waitingHours");

        rejectedHours = new HTML();
        rejectedHours.addStyleName(DEFAULT_WIDTH);
        rejectedHours.ensureDebugId(project_summary + "rejectedHours");

        employeeAssignment = new HTML();
        employeeAssignment.addStyleName(DEFAULT_WIDTH);
        employeeAssignment.ensureDebugId(project_summary + "employeeAssignment");

        clientBalance = new HTML();
        clientBalance.addStyleName(DEFAULT_WIDTH);
        clientBalance.ensureDebugId(project_summary + "clientBalance");

        clientRetainers = new HTML();
        clientRetainers.addStyleName(DEFAULT_WIDTH);
        clientRetainers.ensureDebugId(project_summary + "clientRetainers");

        createdBy = new HTML();
        createdBy.addStyleName(DEFAULT_WIDTH);
        createdBy.ensureDebugId(project_summary + "createdBy");

        createdDate = new HTML();
        createdDate.addStyleName(DEFAULT_WIDTH);
        createdDate.ensureDebugId(project_summary + "createdDate");

        updatedBy = new HTML();
        updatedBy.addStyleName(DEFAULT_WIDTH);
        updatedBy.ensureDebugId(project_summary + "updatedBy");

        updatedDate = new HTML();
        updatedDate.addStyleName(DEFAULT_WIDTH);
        updatedDate.ensureDebugId(project_summary + "updatedDate");

        location = new HTML();
        location.addStyleName(DEFAULT_WIDTH);
        location.ensureDebugId(project_summary + "location");

        contractName = new FlowPanel();

        notStartedTasks = new HTML();
        notStartedTasks.addStyleName(DEFAULT_WIDTH);
        notStartedTasks.ensureDebugId(project_summary + "notStartedTasks");

        inProgressTasks = new HTML();
        inProgressTasks.addStyleName(DEFAULT_WIDTH);
        inProgressTasks.ensureDebugId(project_summary + "inProgressTasks");

        completedTasks = new HTML();
        completedTasks.addStyleName(DEFAULT_WIDTH);
        completedTasks.ensureDebugId(project_summary + "completedTasks");

        cencelledTasks = new HTML();
        cencelledTasks.addStyleName(DEFAULT_WIDTH);
        cencelledTasks.ensureDebugId(project_summary + "cencelledTasks");

        waitingSomeone = new HTML();
        waitingSomeone.addStyleName(DEFAULT_WIDTH);
        waitingSomeone.ensureDebugId(project_summary + "waitingSomeone");

        closedTasks = new HTML();
        closedTasks.addStyleName(DEFAULT_WIDTH);
        closedTasks.ensureDebugId(project_summary + "closedTasks");

        if (showCustomFields) {
            getCustomFieldUtil().drawCustomFields(NewProjectSummaryView.this, projectID, true);
        }
        checkInLocations = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getCheckInLocations(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });

        drawItemTable();

        projectInvoicePanel = new FlexPanel();
        VerticalPanelDiv panelDiv = new VerticalPanelDiv();
        ProjectInvoicesWidget projectInvoices = new ProjectInvoicesWidget(projectID, panelDiv);
        projectInvoices.loadData();
        projectInvoicePanel.add(projectInvoices);
        projectInvoicePanel.add(panelDiv);

        loggedTimeWidget = new LoggedTimeWidget(projectID);

        noteWidget = new NoteWidget(projectID, RelationItem.TYPE_PROJECT);
        chartPanel = new FlowPanel();
        addFields();
    }

    private Widget createRichText() {
        description = new HTML();
        description.setWidth("100%");
        return description;
    }

    private void addFields() {
        //Project Details
        addTitleField(CustomFormConstants.DETAILS, getTitle(property.getSingular(wfmStrings.basicDetails(), wfmStrings.project())));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, projectName, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(CustomFormConstants.NAME, projectName, wfmStrings.name());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, richText, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description()));
        } else {
            addField(CustomFormConstants.DESCRIPTION, richText, getTitle(wfmStrings.description()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null) {
            addField(CustomFormConstants.START_DATE, startDate, getTitle(formPropertyMap.get(CustomFormConstants.START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.START_DATE).getTitle() : wfmStrings.startDate()));
        } else {
            addField(CustomFormConstants.START_DATE, startDate, getTitle(wfmStrings.startDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null) {
            addField(CustomFormConstants.DUE_DATE, dueDate, getTitle(formPropertyMap.get(CustomFormConstants.DUE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DUE_DATE).getTitle() : wfmStrings.dueDate()));
        } else {
            addField(CustomFormConstants.DUE_DATE, dueDate, getTitle(wfmStrings.dueDate()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status()));
        } else {
            addField(CustomFormConstants.STATUS, status, getTitle(wfmStrings.status()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE) != null) {
            addField(CustomFormConstants.PROJECT.BILLIBLE, billable, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE).getTitle() : wfmStrings.billable()));
        } else {
            addField(CustomFormConstants.PROJECT.BILLIBLE, billable, getTitle(wfmStrings.billable()));
        }

        addField(CustomFormConstants.PROJECT.COMPLETED, completed, getTitle(wfmStrings.percent()));

        if (CompanyConstants.C10520.equals(Utils.getEncryptedCompanyID()) || Utils.getHostURL().contains(HOST_AWS)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION) != null) {
                addField(CustomFormConstants.PROJECT.LOCATION, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION).isRequired()));
            } else {
                addField(CustomFormConstants.PROJECT.LOCATION, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
            }
        }

        if (Utils.isEmployeeAssignmentEnable()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.CONTRACT) != null) {
                addField(CustomFormConstants.PROJECT.CONTRACT, contractName, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.CONTRACT).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.CONTRACT).getTitle() : wfmStrings.contract()));
            } else {
                addField(CustomFormConstants.PROJECT.CONTRACT, contractName, getTitle(wfmStrings.contract()));
            }
        }

        //More details
        addTitleField(CustomFormConstants.MORE_DETAILS, getTitle(wfmStrings.moreDetails()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MANAGER) != null) {
            addField(CustomFormConstants.MANAGER, pManager, getTitle(formPropertyMap.get(CustomFormConstants.MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.MANAGER).getTitle() : wfmStrings.manager()));
        } else {
            addField(CustomFormConstants.MANAGER, pManager, getTitle(wfmStrings.manager()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, bManager, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).getTitle() : wfmStrings.backupManagers()));
        } else {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, bManager, getTitle(wfmStrings.backupManagers()));
        }

        if (!Utils.hasRole(Constants.CLIENT)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.ESTIMATED_TIME) != null) {
                addField(CustomFormConstants.PROJECT.ESTIMATED_TIME, estimatedTime, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.ESTIMATED_TIME).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.ESTIMATED_TIME).getTitle() : wfmStrings.estimatedTime()));
            } else {
                addField(CustomFormConstants.PROJECT.ESTIMATED_TIME, estimatedTime, getTitle(wfmStrings.estimatedTime()));
            }

            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.TIME_SPENT) != null) {
                addField(CustomFormConstants.PROJECT.TIME_SPENT, timeSpent, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.TIME_SPENT).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.TIME_SPENT).getTitle() : wfmStrings.timeSpentOnly()));
            } else {
                addField(CustomFormConstants.PROJECT.TIME_SPENT, timeSpent, getTitle(wfmStrings.timeSpentOnly()));
            }

            addField(CustomFormConstants.PROJECT.WAITING_HOURS, waitingHours, getTitle(wfmStrings.waitingForApproval()));
            addField(CustomFormConstants.PROJECT.REJECTED_HOURS, rejectedHours, getTitle(projectStrings.rejectedHours()));
        }
        if (Utils.hasRole(Constants.DR) || Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.PM)) {
            if (Utils.hasPermission(PermissionConstants.PM_PROJECT_COST)) {
                addField(CustomFormConstants.PROJECT.ESTIMATED_COST, estimatedCost, getTitle(wfmStrings.costEstimated()));
                addField(CustomFormConstants.PROJECT.ACTUAL_COST, actualCost, getTitle(wfmStrings.actualCost()));
            }
        }
        addField(CustomFormConstants.PROJECT.ACTUAL_START_DATE, actualStartDate, getTitle(wfmStrings.actualStartDate()));
        addField(CustomFormConstants.PROJECT.ACTUAL_END_DATE, actualEndDate, getTitle(wfmStrings.actualEndDate()));
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_ACTUAL_TIME_SPENT)) {
            addField(CustomFormConstants.PROJECT.ACTUAL_TIME_SPENT, actualTimeSpent, getTitle(wfmStrings.actualTimeSpent()));
        }


//        TaskDetails
        addTitleField(CustomFormConstants.PROJECT.TASK_DETAILS, Property.get(Constants.TASK, wfmStrings.taskDetails(), wfmStrings.task()));
        addField(CustomFormConstants.PROJECT.NOT_STARTED_TASKS, notStartedTasks, getTitle(wfmStrings.notStartedTasks()));
        addField(CustomFormConstants.PROJECT.IN_PORGRESS_TASKS, inProgressTasks, getTitle(wfmStrings.inProgressTasks()));
        addField(CustomFormConstants.PROJECT.COMPLETED_TASKS, completedTasks, getTitle(wfmStrings.completedTasks()));
        addField(CustomFormConstants.PROJECT.CANCELLED_TASKS, cencelledTasks, getTitle(wfmStrings.cancelledTasks()));
        addField(CustomFormConstants.PROJECT.WAITING_FOR_SOMEONE_ELSE, waitingSomeone, getTitle(wfmStrings.waitingForSomeone()));
        addField(CustomFormConstants.PROJECT.CLOSED_TASKS, closedTasks, getTitle(wfmStrings.closedTasks()));

        if (Utils.isEmployeeAssignmentEnable()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT) != null) {
                addField(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT, employeeAssignment, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).getTitle() : wfmStrings.employeeAssignment()));
            } else {
                addField(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT, employeeAssignment, getTitle(wfmStrings.employeeAssignment()));
            }
        }

        addField(CustomFormConstants.PROJECT.CREATED_BY, createdBy, getTitle(wfmStrings.createdBy()));
        addField(CustomFormConstants.PROJECT.CREATED_DATE, createdDate, getTitle(wfmStrings.createdDate()));
        addField(CustomFormConstants.PROJECT.UPDATED_BY, updatedBy, getTitle(wfmStrings.modifiedBy()));
        addField(CustomFormConstants.PROJECT.UPDATED_DATE, updatedDate, getTitle(wfmStrings.modifiedDate()));

        addField(CustomFormConstants.PROJECT.PROJECT_NOTE, noteWidget, wfmStrings.notes(), true);

        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

        addTitleField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES, wfmStrings.involvedEmployees());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE) != null) {
            addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, dataGrid, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE).getTitle() : wfmStrings.members()));
        } else {
            addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, dataGrid, wfmStrings.members());
        }

        addTitleField(CustomFormConstants.PROJECT.PROJECT_INVOICES_DETAILS, "Account Details");
        addField(CustomFormConstants.PROJECT.PROJECT_INVOICES, projectInvoicePanel, wfmStrings.invoices());

        addTitleField(CustomFormConstants.PROJECT.LOGGED_TIME_DETAILS, "Logged Time Details");
        addField(CustomFormConstants.PROJECT.LOGGED_TIMES, loggedTimeWidget, "", true);
        addField(CustomFormConstants.PROJECT.CLIENT_BALANCE, clientBalance, wfmStrings.clientBalance());
        addField(CustomFormConstants.PROJECT.CLIENT_RETAINER, clientRetainers, wfmStrings.clientRetainer());
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_CHART)) {
            addTitleField(CustomFormConstants.PROJECT.PROJECT_CHARTS, wfmStrings.chartOnly());
            addField(CustomFormConstants.PROJECT.PROJECT_CHART, chartPanel, wfmStrings.chartOnly(), true);
        }
        GeneralFileUpload fileUpload = new GeneralFileUpload(F_PROJECT, projectID, projectID);
        addField(ATTACHMENTS, fileUpload, wfmStrings.attachments(), true);

        addTitleField(CustomFormConstants.PROJECT.CHECK_IN_LOCATIONS, wfmStrings.checkInLocations());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.CHECK_IN_LOCATION) != null) {
            addField(CustomFormConstants.PROJECT.CHECK_IN_LOCATION, checkInLocations, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.CHECK_IN_LOCATION).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.CHECK_IN_LOCATION).getTitle() : null, formPropertyMap.get(CustomFormConstants.PROJECT.CHECK_IN_LOCATION).isRequired()));
        } else {
            addField(CustomFormConstants.PROJECT.CHECK_IN_LOCATION, checkInLocations,null);
        }

        show();
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(item.getRelations(), false);

                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });

        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_REMOVE)) {
            MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
            if (Utils.hasRole(Constants.ADMIN)) {
                MaterialLink customize = new MaterialLink(wfmStrings.customize());
                customize.addClickHandler(click -> {
                    String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
                });
                options.add(customize);
            }

            //Remove
            if (Utils.hasPermission(PermissionConstants.PM_PROJECT_REMOVE)) {
                MaterialLink deleteButton = new MaterialLink(wfmStrings.delete());
                deleteButton.addClickHandler(event -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            boolean booleanRemoveIcon = Utils.hasPermission(PermissionConstants.PM_PROJECT_REMOVE) && !projectID.equals(item.getDefaultProjectID()) && !projectID.equals(item.getCrmProjectID());
                            if (booleanRemoveIcon) {
                                ProjectService.App.get().deleteProject(projectID, new AbstractAsyncCallback<Void>() {
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_DELETE, result, NewProjectSummaryView.this);
                                        Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.project()), Info.Type.INFO);
                                        closeTab();
                                    }
                                });
                            } else {
                                LoadingPanel.loading(false);
                                if (projectID.equals(item.getDefaultProjectID()) || projectID.equals(item.getCrmProjectID())) {
                                    Info.show("System projects can not be deleted", Info.Type.WARNING);
                                } else {
                                    Info.show("You do not have permission to delete this project", Info.Type.WARNING);
                                }
                            }
                        }
                    });
                    message.open();
                });
                options.add(deleteButton);
            }
        }
        if (!(Utils.hasRole(CLIENT) || Utils.hasUserMaxRoleID(MEM))) {
            ExportPdfButton pdf = new ExportPdfButton(new ExportPdfButton.PdfRequestInterface() {
                @Override
                public String getUrl() {
                    return "/projectViewPDFHandler";
                }

                @Override
                public boolean isLandscapeOptionEnabled() {
                    return true;
                }

                @Override
                public HashMap<String, String> getParameters() {
                    RequestObject requestObject = new RequestObject(projectID);
                    HashMap<String, String> parametrs = requestObject.getRequestParams();
                    return parametrs;
                }
            });
            addRightButton(pdf);
        }

        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_EDIT)) {
            addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> goTo("project|edit/" + projectID));
        }


        if (Utils.isEnableBonnardCustomization()) {
            timeEntryButton = new WfmButton2(projectStrings.timeEntry());
            timeEntryButton.setTitle(wfmStrings.save());
            timeEntryButton.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("task|add/add" + "/" + projectID));
            timeEntryButton.setVisible(Utils.hasRole(Constants.TIMESHEET_EDITOR));
            addButton("ADD_TASK", timeEntryButton);
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ProjectService.App.get().viewProject(projectID, new AbstractAsyncCallback<ProjectViewItem>() {
            @Override
            public void failure(Throwable caught) {
                caught.getMessage();
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ProjectViewItem o) {
                item = o;
                drawProjectView();
                initProjectMembersTableColumns();
                drawItemTable();
                link.setBadgeCount(item.getRelations().size());
                LoadingPanel.loading(false);
            }
        });
    }

    private void drawProjectView() {
        if (item.getObjectID() != null) {
            Utils.registrRelation(item);
        }
        String pName = item.getName() != null ? item.getName() : "";
        if (pName.length() > 45) {
            pName = pName.substring(0, 45) + "...";
        }
        if (item.getNumberData() != null) {
            number.setHTML(item.getNumberData() != null ? item.getNumberData().getNumberString() : "");
        } else {
            number.setHTML("N/A");
        }
        projectName.setHTML(pName);

        pManager.setHTML(item.getManager() != null ? item.getManager() : "N/A");

        FlexTable backupManagerLinks = new FlexTable();
        int i = 0;
        for (final SelectItem backupManager : item.getBackupManagers()) {
            HTML backupManagerHTML = new HTML();
            backupManagerHTML.setHTML(backupManager.getName());
            backupManagerLinks.setWidget(i, 0, backupManagerHTML);
            i++;
        }
        bManager.setHTML(item.getBackupManagers().size() > 0 ? backupManagerLinks.toString() : "N/A");

        description.setHTML(!Utils.isNullOrEmpty(item.getDescription()) ? item.getDescription().replace("\n", "<br/>") : "");

        billable.setHTML(item.isBillable() ? wfmStrings.yes() : wfmStrings.no());

        HTML clientHtml = new HTML("N/A");

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT)) {
            if (item.getClients() != null && item.getClients().length > 0) {
                VerticalPanel pnlClients = new VerticalPanel();
                pnlClients.setSpacing(5);

                for (final SelectItem client : item.getClients()) {
                    if (Utils.hasPermission(PermissionConstants.CLIENT_NAME_CLICKABLE)) {
                        SimpleLink clientLink = new SimpleLink(client.getName());
                        clientLink.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + client.getId(), client.getNumber(), client.getName()));
                        pnlClients.add(clientLink);
                    } else {
                        pnlClients.add(new HTML(client.getName()));
                    }
                }
                addField(CustomFormConstants.PROJECT.CLIENT, pnlClients, getTitle(wfmStrings.customers()));
            } else {
                addField(CustomFormConstants.PROJECT.CLIENT, clientHtml, getTitle(wfmStrings.customers()));
            }
        } else {
            SimpleLink clientLink = null;

            if (item.getClient() != null && !"N/A".equals(item.getClient())) {
                clientLink = new SimpleLink(item.getClient());
                clientLink.setStyleName("NewProjectSummaryView-clientLink");
                clientLink.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + item.getClientId(), item.getClient(), item.getClient()));
                clientHtml.setHTML(item.getClient());
            }
            if (Utils.hasPermission(PermissionConstants.CLIENT_NAME_CLICKABLE) && clientLink != null) {
                addField(CustomFormConstants.PROJECT.CLIENT, clientLink, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
            } else {
                addField(CustomFormConstants.PROJECT.CLIENT, clientHtml, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
            }
        }

        status.setHTML(item.getStatus());
        employeeAssignment.setHTML(item.getEmployeeAssignment() != null ? item.getEmployeeAssignment().getTitle() : EmployeeAssignmentEnum.BY_EMPLOYEE.getTitle());
        if (item.getComplete() == null || item.getComplete().equals("0.0")) {
            completed.setHTML("0.00%");
        } else if (Double.valueOf(item.getComplete()) > 100) {
            completed.setHTML("<p style='color: red'>" + item.getComplete() + "%</p>");
        } else {
            completed.setHTML(formatToDouble(item.getComplete()) + "%");
        }

        if (CompanyConstants.C10520.equals(Utils.getEncryptedCompanyID()) || Utils.getHostURL().contains(HOST_AWS)) {
            location.setHTML(item.getProjectLocation());
        }
        if (!item.getCheckInLocations().isEmpty()) {
            checkInLocations.clear();
            for (CheckInLocationItem checkInLocation : item.getCheckInLocations()) {
                checkInLocations.addWidgets(getCheckInLocations(checkInLocation));
            }
        }

        if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_SUMMARY) && item.getContractID() != null) {
            Widget projectLink = new SimpleLink(item.getContractName().isEmpty() ? "N/A" : item.getContractName(), "contract|summary/" + item.getContractID(), item.getContractName(), item.getNumberData().getFirstNumberString());
            contractName.add(projectLink);
        } else {
            contractName.add(new HTML(item.getContractName()));
        }

        PositionsSelectItem[] projectEmployees = item.getProjectEmployees();
        if (projectEmployees != null && projectEmployees.length > 0) {
//            if (projectEmployees.length == 1) {
//                dataGrid.setHeight("125px");
//            } else if (projectEmployees.length == 2) {
//                dataGrid.setHeight("190px");
//            } else if (projectEmployees.length == 3) {
//                dataGrid.setHeight("260px");
//            } else if (projectEmployees.length == 4) {
//                dataGrid.setHeight("335px");
//            } else if (projectEmployees.length == 5) {
//                dataGrid.setHeight("410px");
//            } else {
//                dataGrid.setHeight("400px");
//            }
            if (projectEmployees.length > 10){
                dataGrid.setHeight("400px");
            }
            initDataProviderApply(projectEmployees);

            if (Utils.isEnableBonnardCustomization())
                for (PositionsSelectItem item : projectEmployees) {
                    if (Utils.getUserID().equals(item.getId()) && timeEntryButton != null) {
                        timeEntryButton.setVisible(true);
                        break;
                    }
                }
        }

        moreDetails();

        dataProvider.refresh();

        getTaskDetails();

        createdBy.setHTML(item.getCreator());
        createdDate.setHTML(DateUtils.format(item.getCreationDate()));
        updatedBy.setHTML(item.getLastUpdaterName());
        updatedDate.setHTML(DateUtils.format(item.getLastUpdateTime()));
        clientBalance.setHTML(Utils.getNumberFormat().format(item.getClientBalance()));
        clientRetainers.setHTML(Utils.getNumberFormat().format(item.getClientRetainers()));

        //task custom fields
        if (showCustomFields) {
            if (projectID != null) {
                getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields(), true);
            }
        }

    }

    private void getTaskDetails() {
        notStartedTasks.setHTML(String.valueOf(item.getNotStartedTasks()));
        inProgressTasks.setHTML(String.valueOf(item.getInProgressTasks()));
        completedTasks.setHTML(String.valueOf(item.getCompletedTasks()));
        cencelledTasks.setHTML(String.valueOf(item.getCancelledTasks()));
        waitingSomeone.setHTML(String.valueOf(item.getWaitingTasks()));
        closedTasks.setHTML(String.valueOf(item.getClosedTasks()));
    }

    private WidgetsMap getCheckInLocations(CheckInLocationItem item) {
        final WidgetsMap widgetsMap = new WidgetsMap();
        CheckInLocationWidget checkInLocationWidget = item != null ? new CheckInLocationWidget(item.getId(), item.getLatitude(), item.getLongitude(), item.getRadius(),true) : new CheckInLocationWidget();
        widgetsMap.addWidgetToMap("LOCATION", checkInLocationWidget);
        widgetsMap.add("latitude", checkInLocationWidget.latitude);
        widgetsMap.add("longitude", checkInLocationWidget.longitude);
        widgetsMap.add("radius", checkInLocationWidget.radius);
        return widgetsMap;
    }

    private void initDataProviderApply(PositionsSelectItem[] projectEmployees) {
        List<PositionsSelectItem> employeeItems = dataProvider.getList();
        employeeItems.clear();
        Collections.addAll(employeeItems, projectEmployees);
    }

    private void moreDetails() {
        String start = DateUtils.format(item.getStartDate());
        String due = DateUtils.format(item.getDueDate());
        startDate.setHTML(start);
        dueDate.setHTML(due);

        estimatedTime.setHTML(item.getEstimatedTime());
        timeSpent.setHTML(item.getTimeSpent());
        estimatedCost.setHTML(item.getEstimatedCost());
        waitingHours.setHTML(item.getWaitingHours());
        rejectedHours.setHTML(item.getRejectedHours());

        if (item.getActualStartDate() != null) {
            actualStartDate.setHTML(DateUtils.format(item.getActualStartDate()));
        } else {
            actualStartDate.setHTML("N/A");
        }

        if (item.getActualEndDate() != null) {
            actualEndDate.setHTML(DateUtils.format(item.getActualEndDate()));
        } else {
            actualEndDate.setHTML("N/A");
        }

        if (!Utils.hasRole(Constants.CLIENT)) {
            actualTimeSpent.setHTML(item.getHoursSpent());
        }
        actualCost.setHTML(item.getActualCost());
    }

    private void initProjectMembersTableColumns() {
        //employee name
        Column<PositionsSelectItem, String> employee = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return (object.getEmployeeNumber() != null ? object.getEmployeeNumber() + " - " : "") + (object.getName() != null ? object.getName() : "");
            }
        };
        dataGrid.addColumn(employee, wfmStrings.employee());
        dataGrid.setColumnWidth(employee, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        if (!EmployeeAssignmentEnum.BY_POSITION.equals(item.getEmployeeAssignment())) {
            // department Name
            Column<PositionsSelectItem, String> department = new Column<PositionsSelectItem, String>(new TextCell()) {
                @Override
                public String getValue(PositionsSelectItem object) {
                    return object.getDepartmentName();
                }
            };

            dataGrid.addColumn(department, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department(), wfmStrings.department()));
            dataGrid.setColumnWidth(department, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        }

        //employee position
        Column<PositionsSelectItem, String> position = new Column<PositionsSelectItem, String>(new TextCell()) {
            @Override
            public String getValue(PositionsSelectItem object) {
                return object.getPositionName();
            }
        };
        dataGrid.addColumn(position, wfmStrings.position());
        dataGrid.setColumnWidth(position, 15, com.google.gwt.dom.client.Style.Unit.PCT);

        if (!Utils.hasRole(CLIENT)) {
            if (!EmployeeAssignmentEnum.BY_POSITION.equals(item.getEmployeeAssignment())) {
                //estimate time
                Column<PositionsSelectItem, String> estimatedTime = new Column<PositionsSelectItem, String>(new TextCell()) {
                    @Override
                    public String getValue(PositionsSelectItem object) {
                        return Utils.formatMinutes(object.getTime());
                    }
                };
                estimatedTime.setSortable(true);
                dataGrid.addColumn(estimatedTime, wfmStrings.estimatedTime());
                dataGrid.setColumnWidth(estimatedTime, 15, com.google.gwt.dom.client.Style.Unit.PCT);
                listHandler.setComparator(estimatedTime, (o1, o2) -> Utils.formatMinutes(o1.getTime()).compareToIgnoreCase(Utils.formatMinutes(o2.getTime())));

                Column<PositionsSelectItem, String> timeSpent = new Column<PositionsSelectItem, String>(new TextCell()) {
                    @Override
                    public String getValue(PositionsSelectItem object) {
                        return Utils.formatMinutes(object.getTimeSpent());
                    }
                };
                dataGrid.addColumn(timeSpent, wfmStrings.timeSpentOnly());
                dataGrid.setColumnWidth(timeSpent, 15, com.google.gwt.dom.client.Style.Unit.PCT);

                //actual time
                Column<PositionsSelectItem, String> actualTime = new Column<PositionsSelectItem, String>(new TextCell()) {
                    @Override
                    public String getValue(PositionsSelectItem object) {
                        return Utils.formatMinutes(object.getActualTime());
                    }
                };
                dataGrid.addColumn(actualTime, wfmStrings.actualTime());
                dataGrid.setColumnWidth(actualTime, 15, com.google.gwt.dom.client.Style.Unit.PCT);

                //percentage
                Column<PositionsSelectItem, SafeHtml> percent = new Column<PositionsSelectItem, SafeHtml>(new SafeHtmlCell()) {
                    @Override
                    public SafeHtml getValue(final PositionsSelectItem object) {
                        return SafeHtmlUtils.fromTrustedString(getCompletedPercent(object.getPercent() != null ? object.getPercent().toString() : "0").toString());
                    }
                };
                dataGrid.addColumn(percent, wfmStrings.percentCompleted());
                dataGrid.setColumnWidth(percent, 15, com.google.gwt.dom.client.Style.Unit.PCT);
            } else {
                Column<PositionsSelectItem, String> contractStart = new Column<PositionsSelectItem, String>(new TextCell()) {
                    @Override
                    public String getValue(PositionsSelectItem object) {
                        return object.getStartDate() != null ? DateUtils.format(object.getStartDate().getNonConvertedDate()) : "N/A";
                    }
                };
                dataGrid.addColumn(contractStart, wfmStrings.contractStart());
                dataGrid.setColumnWidth(contractStart, 15, com.google.gwt.dom.client.Style.Unit.PCT);

                Column<PositionsSelectItem, String> contractEnd = new Column<PositionsSelectItem, String>(new TextCell()) {
                    @Override
                    public String getValue(PositionsSelectItem object) {
                        return object.getEndDate() != null ? DateUtils.format(object.getEndDate().getNonConvertedDate()) : "N/A";
                    }
                };
                dataGrid.addColumn(contractEnd, wfmStrings.contractEnd());
                dataGrid.setColumnWidth(contractEnd, 15, com.google.gwt.dom.client.Style.Unit.PCT);

                Column<PositionsSelectItem, String> type = new Column<PositionsSelectItem, String>(new TextCell()) {
                    @Override
                    public String getValue(PositionsSelectItem object) {
                        if (object.getProjectPosition() != null) {
                            ProjectPosition pp = object.getProjectPosition();

                            if (pp.getPriceType() != null)
                                if (pp.getPriceType() == 0) {
                                    return projectStrings.byHour();
                                } else if (pp.getPriceType() == 1) {
                                    return projectStrings.byMonth();
                                } else if (pp.getPriceType() == 2) {
                                    return projectStrings.byMarkup();
                                } else if (pp.getPriceType() == 3) {
                                    return projectStrings.byMarkupAndOT();
                                } else if (pp.getPriceType() == 4) {
                                    return projectStrings.byDayAndOT();
                                } else if (pp.getPriceType() == 5) {
                                    return projectStrings.byDayAndOTSpec();
                                }
                        }

                        return projectStrings.byHour();
                    }
                };
                dataGrid.addColumn(type, projectStrings.priceType());
                dataGrid.setColumnWidth(type, 15, com.google.gwt.dom.client.Style.Unit.PCT);

                Column<PositionsSelectItem, String> rate = new Column<PositionsSelectItem, String>(new TextCell()) {
                    @Override
                    public String getValue(PositionsSelectItem object) {
                        return object.getProjectPosition() != null ? Utils.getNumberFormat().format(object.getProjectPosition().getUnitPrice()) : Utils.getNumberFormat().format(BigDecimal.ZERO);
                    }
                };
                dataGrid.addColumn(rate, wfmStrings.rate());
                dataGrid.setColumnWidth(rate, 15, com.google.gwt.dom.client.Style.Unit.PCT);

                Column<PositionsSelectItem, String> overtimeRate = new Column<PositionsSelectItem, String>(new TextCell()) {
                    @Override
                    public String getValue(PositionsSelectItem object) {
                        return object.getProjectPosition() != null ? Utils.getNumberFormat().format(object.getProjectPosition().getOvertimeRate()) : Utils.getNumberFormat().format(BigDecimal.ZERO);
                    }
                };
                dataGrid.addColumn(overtimeRate, wfmStrings.overtimeRate());
                dataGrid.setColumnWidth(overtimeRate, 15, com.google.gwt.dom.client.Style.Unit.PCT);

                Column<PositionsSelectItem, String> weekendOvertimeRate = new Column<PositionsSelectItem, String>(new TextCell()) {
                    @Override
                    public String getValue(PositionsSelectItem object) {
                        return object.getProjectPosition() != null ? Utils.getNumberFormat().format(object.getProjectPosition().getWeekendOvertimeRate()) : Utils.getNumberFormat().format(BigDecimal.ZERO);
                    }
                };
                dataGrid.addColumn(weekendOvertimeRate, wfmStrings.weekendOvertimeRate());
                dataGrid.setColumnWidth(weekendOvertimeRate, 15, com.google.gwt.dom.client.Style.Unit.PCT);

                Column<PositionsSelectItem, String> holidayOvertimeRate = new Column<PositionsSelectItem, String>(new TextCell()) {
                    @Override
                    public String getValue(PositionsSelectItem object) {
                        return object.getProjectPosition() != null ? Utils.getNumberFormat().format(object.getProjectPosition().getHolidayOvertimeRate()) : Utils.getNumberFormat().format(BigDecimal.ZERO);
                    }
                };
                dataGrid.addColumn(holidayOvertimeRate, wfmStrings.holidayOvertimeRate());
                dataGrid.setColumnWidth(holidayOvertimeRate, 15, com.google.gwt.dom.client.Style.Unit.PCT);
            }
        }
    }

    private String formatToDouble(String text) {
        return numberFormat.format(parseToDouble(text));
    }

    private double parseToDouble(String text) {
        return Double.parseDouble(text.replace(",", ""));
    }

    private HorizontalPanel getCompletedPercent(String percent) {
        HorizontalPanel panelBackground = new HorizontalPanel();
        HorizontalPanel panelPercent = new HorizontalPanel();
        panelBackground.setStyleName("completed_back");
        panelBackground.setWidth("104px");
        panelBackground.setHeight("18px");
        panelPercent.setStyleName("completed_percent");
        panelPercent.setHeight("15px");
        panelPercent.setWidth(percent + "px");

        if (percent == null || !percent.equals("0.0")) {
            panelPercent.add(new HTML("&nbsp;&nbsp;" + formatToDouble(percent) + "%"));
            panelPercent.setTitle(formatToDouble(percent) + "% compele");
        } else {
            panelBackground.add(new HTML("&nbsp;&nbsp;" + "0" + "%"));
            panelBackground.setTitle("0" + "% compele");
        }
        panelPercent.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        if (percent != null && Double.valueOf(percent) > 100) {
            panelPercent.getElement().getStyle().setBackgroundColor("#DA3611");
        }
        panelBackground.add(panelPercent);
        return panelBackground;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PROJECT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.PM_PROJECT_ADD;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }


    protected void drawItemTable() {
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.PROJECT_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {
                        CustomFormItemGrid itemView = new CustomFormItemGrid(projectID, configMap.getKey(), LayoutRPC.PROJECT_FORM, configMap.getValue(), 1000);
                        addField(configMap.getKey(), itemView, null, true);
                    }
                }
            }
        });
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            Localize.getInstance().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    @Override
    protected void initPredefinedValues() {
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
        return PROJECT;
    }
}
