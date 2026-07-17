package com.edatasite.workforce.gwt.timesheet.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.WestPanelHelp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTaskTransfer;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTimesheetData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTimesheetModel;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetSettings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 9/15/11
 * Time: 9:21 PM
 */
public class FastTimesheet extends View implements Constants {

    private final ProjectStrings projectStrings = ProjectStrings.App.get();
    private final ListingFilterParameter filterParameters = new ListingFilterParameter();
    private MaterialPanel emptyMessage;
    private GBoxItem workstreamBox;

    public FastTimesheet() {
        super(TIMESHEET);
        setDescription(property.getPlural(wfmStrings.timesheet()));
    }

    @UiField
    GBox toolbar;
    @UiField(provided = true)
    KpiDataGrid<FastTaskTransfer> dataGrid;

    interface Binder extends UiBinder<Widget, FastTimesheet> {
    }

    private DateNonConvertable clientsDate;
    private TimesheetSettings timesheetSettings;
    private FastTimesheetModel model;
    private int weekOffset;
    private boolean refreshCalendar = false;
    private SelectItem selectedClient;

    private CRMLookUp project;
    private CRMLookUp clientDropdown;
    private DataListBox workstreamDropdown;
    private EmployeeLookUpWithCode employeeDropdown;

    private DatePicker dateBox;
    private WfmButton2 prewItem;
    private WfmButton2 nextItem;

    private GBoxRow toolbarRow;
    protected ListingPanel listingPanel;
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;
    private HTMLPanel exportPanel;


    /**
     * The key provider that provides the unique ID of a task.
     */
    public static final ProvidesKey<FastTaskTransfer> KEY_PROVIDER = item -> item == null ? null : item.getId();

    public Widget onInitialize() {

        // Create a DataGrid.

        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setPageSize(1000);

        // Add the CellList to the adapter in the database.
        TaskDatabase.get().addDataDisplay(dataGrid);

        // Create the UiBinder.
        Binder uiBinder = GWT.create(Binder.class);
        add(uiBinder.createAndBindUi(this));

        clientsDate = new DateNonConvertable();
        createToolBar();
        TimesheetService.App.get().getTimesheetSettings(new AbstractAsyncCallback<TimesheetSettings>() {
            public void success(TimesheetSettings settings) {
                TaskDatabase.get().setTimesheetSettings(settings);
                timesheetSettings = settings;
                if (model != null) {
                    model.setSettings(timesheetSettings);
                }
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_ADD, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_EDIT, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_DELETE, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ISSUE_ADD, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ISSUE_DELETE, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESHEET_APPROVAL, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESHEET_SUBMIT_FOR_APPROVAL, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKSTREAM_ADD, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKSTREAM_DELETED, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_ADD, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_DELETE, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_ADD, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_DELETED, FastTimesheet.this, (sender, args) -> loadList());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DEPARTMENT_EDIT, FastTimesheet.this, (sender, args) -> loadList());

        refreshView();
        return null;
    }

    private void loadList() {
        LoadingPanel.loading(true);
        clientsDate = new DateNonConvertable();
        if ("true".equals(Utils.userSettings.get(SHOW_COMPLETED_TASKS))) {
            filterParameters.setShowCompletedTasks(true);
        }
        TimesheetService.App.get().getFastTimesheetData(new DateNonConvertable(dateBox.getDate()), clientsDate, weekOffset, filterParameters, new AbstractAsyncCallback<FastTimesheetData>() {
            @Override
            public void failure(Throwable throwable) {
                filterParameters.setUseSelectedDate(false);
                createEmptyWidget();
                LoadingPanel.loading(false);
            }

            public void success(final FastTimesheetData result) {
                filterParameters.setUseSelectedDate(false);
                checkDateButtons(result);
                model = new FastTimesheetModel(result);
                TaskDatabase.get().setModel(model);
                setDropDownItems();
                mainView();
                createEmptyWidget();
                LoadingPanel.loading(false);
            }
        });
    }

    private void createEmptyWidget() {
        if (emptyMessage != null) {
            return;
        }
        // Set the message to display when the table is empty.
        VerticalPanelDiv vp = new VerticalPanelDiv();
        vp.setStyleName("center");
        HorizontalPanelDiv hp = new HorizontalPanelDiv();
        SimpleLink link = new SimpleLink(" " + wfmStrings.here());
        link.addClickHandler(clickEvent -> new TaskQuickAddView());
        link.setStyleName("addLinkStyle");
        hp.add(new Label(Property.get(Constants.TASK, wfmStrings.noTasksLink(), wfmStrings.tasks())));
        hp.add(link);
        vp.add(new Label(Property.get(Constants.TASK, wfmStrings.noTasksText(), wfmStrings.tasks())));
        vp.add(hp);

        emptyMessage = new MaterialPanel("timesheet__empty-message");
        emptyMessage.add(vp);

        dataGrid.setEmptyTableWidget(emptyMessage);
    }

    private void mainView() {

        model.setSettings(timesheetSettings);

        TaskDatabase.get().generateTasks(model);
    }

    private void createToolBar() {

        boolean enabled = true;
        if (employeeDropdown != null && !Utils.getUserID().equals(employeeDropdown.getSelectedItemID())) {
            enabled = false;
        }
        toolbar.addStyleName("group-box--united group-box--no-padding");
        toolbarRow = new GBoxRow();

        GBoxItem navigationButtonPanel = generateNavigationButtons();
        navigationButtonPanel.addStyleName("group-box__item--width-free group-box__item--split-right");
        navigationButtonPanel.getgBoxItemLabel().removeFromParent();
        toolbarRow.add(navigationButtonPanel);

        boolean isAuditorToWissamStouhi = Utils.hasRole(AUDITOR) && CompanyConstants.C28492.equals(Utils.getEncryptedCompanyID());//companyID = 28492, Company Name: ITS DEPARTMENT - PROJECT MANAGEMENT & COLLABORATION PORTAL
        if (!isAuditorToWissamStouhi) {
            if (!"true".equals(getModuleParam("isMediacom")) || !SinksContainerFactory.entryPoint.moduleSetting.isCustomise()) {
                WfmButton2 submitButton = new WfmButton2(wfmStrings.submit(), WfmButton2.BTN_PRIMARY);
                submitButton.ensureDebugId("timeSheet_list_view_submit_for_approval_button");
                submitButton.addClickHandler(event -> {
                    if (employeeDropdown != null && employeeDropdown.getSelectedItemID() != null && employeeDropdown.getSelectedItemID() > 0) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("timesheet|add/add/" + employeeDropdown.getSelectedItemID());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("timesheet|add/add");
                    }
                });

                KpiToolTip kpiToolTip = new KpiToolTip(submitButton, wfmStrings.submitForApproval(), Position.TOP);
                Div div = new Div();
                div.add(submitButton);
                GBoxItem submitButtonPanel = new GBoxItem(div);
                submitButtonPanel.addStyleName("group-box__item--split-right group-box__item--width-free");
                submitButtonPanel.getgBoxItemLabel().removeFromParent();
                toolbarRow.add(submitButtonPanel);
            }
        }

        if (enabled) {
            FlowPanel pnlOperActions = new FlowPanel();
            pnlOperActions.setStyleName("operPanel__actions");

            MaterialMenuBar importExportMenu = new MaterialMenuBar();
            importExportMenu.setClass("btn-group dropdown-kit--arrow--below");
            pnlOperActions.add(importExportMenu);

            MaterialLink ieLink = new MaterialLink();//import/export button link for listing top panel
            ieLink.setTooltip(wfmStrings.importExport());
            ieLink.setTooltipPosition(Position.TOP);
            ieLink.setHref("#");
            ieLink.setClass("btn btn--icon btn--white");

            Icon ieIcon = new Icon();//import/export icon for listing top panel
            ieIcon.setClass("ficon--download-cloud");
            ieLink.add(ieIcon);

            MaterialDropDown menuContainer = new MaterialDropDown(ieLink);
            menuContainer.setClass("dropdown-content--2");
            menuContainer.setBelowOrigin(true);
            ieLink.add(menuContainer);
            importExportMenu.add(ieLink);

            pdfVersion = getPdfVersion();
            pdfVersion.ensureDebugId("pdf_button");
            if (pdfVersion.isVisible()) {

                Div wrapper = new Div("java-wrap");
                menuContainer.add(wrapper);

                MaterialLink pdfVersion = getPdfVersion();
                wrapper.add(pdfVersion);

                MaterialDropDown mdp = new MaterialDropDown(pdfVersion);
                mdp.setHover(true);
                mdp.setHoverable(true);

                mdp.add(FastTimesheet.this::getPortraitLink);
                mdp.add(FastTimesheet.this::getLandscapeLink);

                wrapper.add(mdp);


                exportPanel = new HTMLPanel("");
                exportPanel.getElement().getStyle().setDisplay(Style.Display.NONE);
                pnlOperActions.add(exportPanel);

                setPDFListener();

                MaterialLink xlsVersion = new MaterialLink();
                MaterialIcon xlsIcon = new MaterialIcon();
                xlsIcon.setStylePrimaryName("ficon--file-excel hasicon--left");
                xlsVersion.add(xlsIcon);
                xlsVersion.setText(wfmStrings.excel());
                menuContainer.add(xlsVersion);
                xlsVersion.addClickHandler(event -> {
                    String csvURL = GWT.getHostPageBaseURL() + CommandConstants.COMMON_URL + "/downloadTimesheetCsv";
                    if (filterParameters != null) {
                        filterParameters.setPropertyCode(getPropertyCode());
                    }
                    csvURL += ("?date=" + DateUtils.formatToParse(clientsDate.getNonConvertedDate()));
                    csvURL += ("&weekoffset=" + weekOffset);
                    csvURL += ("&selectedDate=" + DateUtils.formatToParse(dateBox.getDate()));
                    csvURL += ("&userId=" + employeeDropdown.getSelectedItemID());
                    Window.open(csvURL, "_blank", "");
                });
            }
            GBoxItem actionPanel = new GBoxItem(pnlOperActions);
            actionPanel.addStyleName("group-box__item--split-right group-box__item--width-free");
            actionPanel.getgBoxItemLabel().removeFromParent();
            toolbarRow.add(actionPanel);
        }

        if (Utils.hasRole(Utils.TIMESHEET_EDITOR)) {
            toolbarRow.add(generateEmployeeDropdown());
        }
        toolbar.add(toolbarRow);
    }

    public MaterialLink getPdfVersion() {

        if (pdfVersion == null) {
            pdfVersion = new MaterialLink();
            MaterialIcon pdfIcon = new MaterialIcon();
            pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
            pdfVersion.add(pdfIcon);
            pdfVersion.setText(wfmStrings.pdf());
        }
        return pdfVersion;
    }

    private MaterialLink getPortraitLink() {
        if (portrait == null) {
            portrait = new MaterialLink();
            portrait.setText(wfmStrings.portrait());
        }
        return portrait;
    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
            landscape.setText(wfmStrings.landscape());
        }
        return landscape;
    }

    public void setPDFListener() {
        getPortraitLink().addClickHandler((event) -> sendPdfRequest(false));
        getLandscapeLink().addClickHandler((event) -> sendPdfRequest(true));
    }

    private void sendPdfRequest(boolean landscape) {
        String pdfUrl = CommandConstants.PDF_URL + "/timeSheetPDFHandler";
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setPropertyCode(Constants.TIMESHEET);
        HashMap<String, String> params = fp.getRequestParams();
        params.put("date", DateUtils.formatToParse(dateBox.getDate()));
        params.put("selectedDate", DateUtils.formatToParse(dateBox.getDate()));
        params.put("IS_LANDSCAPE", "" + landscape);
        params.put("weekoffset", "" + weekOffset);
        if (employeeDropdown != null && employeeDropdown.getSelectedItemID() != null) {
            params.put("userId", "" + employeeDropdown.getSelectedItemID());
        }
        if (clientDropdown != null && clientDropdown.getSelectedItemID() != null) {
            params.put("clientid", "" + clientDropdown.getSelectedItemID());
        }
        if (project != null && project.getSelectedItemID() != null) {
            params.put("projectid", "" + project.getSelectedItemID());
        }
        if (workstreamDropdown != null && workstreamDropdown.getSelectedId() != null) {
            params.put("workstreamid", "" + workstreamDropdown.getSelectedId());
        }
        Utils.sendPDFOrExcelRequest(exportPanel, pdfUrl, params, "_blank");
    }


    private GBoxItem generateNavigationButtons() {
        MaterialPanel btnGroup = new MaterialPanel("btn-group");

        prewItem = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--chevron-left");
        prewItem.removeStyleName("hasicon--left");
        prewItem.addStyleName("btn--icon");
        prewItem.addClickHandler(event -> {
            weekOffset--;
            refreshCalendar = true;
            refreshView();
        });
        btnGroup.add(prewItem);

        dateBox = new DatePicker(null, null, false, null, TIMESHEET_WEEK_START, false, null);

        dateBox.addChangeHandler(event -> {
            TimesheetService.App.get().getWeekOffset(clientsDate, new DateNonConvertable(dateBox.getDate()), new AbstractAsyncCallback<Integer>() {
                public void success(Integer offset) {
                    weekOffset = offset;
                    filterParameters.setUseSelectedDate(true);
                    refreshView();
                }
            });
            TaskDatabase.get().setDateboxDate(dateBox.getDate());
        });
        if (model != null) {
            dateBox.setDate(model.getDate(0).getNonConvertedDate());
            TaskDatabase.get().setDateboxDate(model.getDate(0).getNonConvertedDate());
        } else {
            dateBox.setDate(new Date());
            TaskDatabase.get().setDateboxDate(new Date());
        }
        GBoxItem gBoxPanel = new GBoxItem(dateBox);
        gBoxPanel.addStyleName("invoice__date group-box__item--split-right");
        gBoxPanel.getgBoxItemLabel().removeFromParent();

        btnGroup.add(dateBox);

        nextItem = new WfmButton2(null, WfmButton2.BTN_WHITE, "ficon--chevron-right");
        nextItem.removeStyleName("hasicon--left");
        nextItem.addStyleName("btn--icon");
        nextItem.addClickHandler(event -> {
            weekOffset++;
            refreshCalendar = true;
            refreshView();
        });
        btnGroup.add(nextItem);

        return new GBoxItem(btnGroup);
    }

    private void generateProjectDropdown() {
        project = new CRMLookUp(LookUpConstants.PROJECT);
        project.setDefaultText(Property.get(Constants.PROJECT, wfmStrings.selectProject(), wfmStrings.project()));
        project.setFullSearch(true);
        project.showClearButton();
        project.ensureDebugId("project_dropdown");
        project.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (project.getSelectedItemID() == null) {
                filterParameters.setProjectId(null);
            } else {
                filterParameters.setProjectId(project.getSelectedItemID());
            }
            if (workstreamDropdown != null && workstreamDropdown.getSelectedItem() != null) {
                filterParameters.setWorkstreamID(workstreamDropdown.getSelectedItem().getId());
                filterParameters.setWorkstreamName(workstreamDropdown.getSelectedItem().getName());
            } else {
                filterParameters.setWorkstreamID(null);
                filterParameters.setWorkstreamName(null);
            }
            refreshView();
        });

        project.setClearCommand(() -> {
            project.refreshOracle(true);
            filterParameters.setProjectId(null);
            refreshView();
        });

        GBoxItem projectItemBox = new GBoxItem(project);
        projectItemBox.getgBoxItemLabel().removeFromParent();

        toolbarRow.add(projectItemBox);
    }

    private void generateClientDropDown() {
        clientDropdown = new CRMLookUp(LookUpConstants.CLIENT_ID);
        clientDropdown.setDefaultText(wfmStrings.select() + " " + Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        clientDropdown.showClearButton();
        clientDropdown.setClearCommand(() -> {
            filterParameters.setClientId(null);
            refreshView();
        });
        clientDropdown.ensureDebugId("clientDropdown");
        if (selectedClient != null) {
            clientDropdown.setSelected(selectedClient);
        }
        clientDropdown.getSuggestBox().addSelectionHandler(event -> {
            selectedClient = clientDropdown.getSelectedItem();
            if (selectedClient != null) {
                filterParameters.setProjectId(null);
                project.clearAndClearItems();
                project.refreshOracle(true);
                setClientIdToProjectLookUp(clientDropdown.getSelectedItemID());
                filterParameters.setClientId(clientDropdown.getSelectedItemID());
            } else {
                filterParameters.setClientId(null);
                filterParameters.setProjectId(null);
                project.clearAndClearItems();
                project.refreshOracle(true);
                setClientIdToProjectLookUp(null);
            }
            refreshView();
        });
        GBoxItem clientBox = new GBoxItem(clientDropdown);
        clientBox.getgBoxItemLabel().removeFromParent();

        toolbarRow.add(clientBox);
    }

    private void setClientIdToProjectLookUp(final Integer clientId) {
        project.setBeforeSearch(() -> {
            project.getFilterParametrs().setDoNotSearch(false);
            project.getFilterParametrs().setClientId(clientId);
        });
    }

    private void refreshView() {
        loadList();
    }


    private void setDropDownItems() {
        if (project == null) {
            generateClientDropDown();
            generateProjectDropdown();
            generateWorkstreamDropdown();
        } else {
            setClientIdToProjectLookUp(clientDropdown.getSelectedItemID());
            workstreamDropdown.setItems(model.getWorkstreams());
        }
        if (workstreamBox != null) {
            workstreamBox.setVisible(model.getWorkstreams() != null && model.getWorkstreams().length > 1);
        }
    }

    private void checkDateButtons(FastTimesheetData result) {
        prewItem.setEnabled(!result.isLastWeek());
        nextItem.setEnabled(weekOffset < 0);
        if (refreshCalendar) {
            dateBox.setDate(result.getDates()[0].getNonConvertedDate());
            refreshCalendar = false;
        }
    }

    private void generateWorkstreamDropdown() {
        workstreamDropdown = new DataListBox();
        workstreamDropdown.ensureDebugId("workstreamDropdown");
        workstreamDropdown.setNullLabel(projectStrings.selectWorkstream());
        workstreamDropdown.setItems(model.getWorkstreams());
        workstreamDropdown.setAllowFirstItem(true);
        workstreamDropdown.addValueChangeHandler(event -> {
            if (workstreamDropdown.getSelectedItem().getName().equals(workstreamDropdown.getNullLabel()) ||
                    workstreamDropdown.getSelectedItem().getName().toLowerCase().equals(wfmStrings.pleaseSelect().toLowerCase())) {
                filterParameters.setWorkstreamID(null);
            } else {
                filterParameters.setWorkstreamID(workstreamDropdown.getSelectedItem().getId());
                filterParameters.setWorkstreamName(workstreamDropdown.getSelectedItem().getName());
            }

            refreshView();
        });

        workstreamBox = new GBoxItem(workstreamDropdown);
        workstreamBox.getgBoxItemLabel().removeFromParent();

        toolbarRow.add(workstreamBox);
    }

    private GBoxItem generateEmployeeDropdown() {
        employeeDropdown = new EmployeeLookUpWithCode();
        employeeDropdown.setDefaultText(wfmStrings.select() + " " + wfmStrings.employee());
        employeeDropdown.showClearButton();
        employeeDropdown.selectCurrentUser();
        employeeDropdown.getSuggestBox().addSelectionHandler(event -> {
            filterParameters.setEmployeeId(employeeDropdown.getSelectedItemID());
            filterParameters.setClientId(null);
            filterParameters.setProjectId(null);
            filterParameters.setWorkstreamID(null);

            clientDropdown.clearAndClearItems();
            project.clearAndClearItems();
            project.refreshOracle(true);
            workstreamDropdown.setSelectedNullLabel();
            refreshView();
        });
        employeeDropdown.setClearCommand(() -> {
            filterParameters.setEmployeeId(Utils.getUserID());
            refreshView();
        });

        GBoxItem employeeItem = new GBoxItem(employeeDropdown);
        employeeItem.getgBoxItemLabel().removeFromParent();
        return employeeItem;
    }


//    private MenuBar createActionMenu(MenuBar menuBar) {
//        boolean enabled = true;
//        if (employeeDropdown != null && !Utils.getUserID().equals(employeeDropdown.getSelectedId())) {
//            enabled = false;
//        }
//        boolean isAuditorToWissamStouhi = Utils.hasRole(AUDITOR) && CompanyConstants.C28492.equals(Utils.getEncryptedCompanyID());//companyID = 28492, Company Name: ITS DEPARTMENT - PROJECT MANAGEMENT & COLLABORATION PORTAL
//        if (!isAuditorToWissamStouhi) {
//            if (!"true".equals(getModuleParam("isMediacom")) || !SinksContainerFactory.entryPoint.moduleSetting.isCustomise()) {
//                MenuPopItem submitForApproval = new MenuPopItem(wfmStrings.submitForApproval());
//                submitForApproval.ensureDebugId("timeSheet_list_view_submit_for_approval_button");
//                submitForApproval.setScheduledCommand(() -> {
//                    if (employeeDropdown != null && employeeDropdown.getSelectedId() != null && employeeDropdown.getSelectedId() > 0) {
//                        SinksContainerFactory.entryPoint.onHistoryChanged("timesheet|add/add/" + employeeDropdown.getSelectedId());
//                    } else {
//                        SinksContainerFactory.entryPoint.onHistoryChanged("timesheet|add/add");
//                    }
//                });
//                menuBar.addItem(submitForApproval);
//            }
//        }
//        MenuPopItem pdfVersion = new MenuPopItem(wfmStrings.pdfVersion());
//        pdfVersion.setEnabled(enabled);
//        pdfVersion.ensureDebugId("timeSheet_list_view_pdf_version_button");
//        pdfVersion.setScheduledCommand(() -> {
//            String pdfUrl = CommandConstants.PDF_URL + "/timeSheetPDFHandler";
//            HashMap<String, String> params = new HashMap<>();
//            params.put("date", DateUtils.formatToParse(clientsDate.getNonConvertedDate()));
//            params.put("selectedDate", DateUtils.formatToParse(dateBox.getDate()));
//            params.put("weekoffset", "" + weekOffset);
//            if (clientDropdown != null && clientDropdown.getSelectedId() != null) {
//                params.put("clientid", "" + clientDropdown.getSelectedId());
//            }
//            if (project != null && project.getSelectedItemID() != null) {
//                params.put("projectid", "" + project.getSelectedItemID());
//            }
//            if (workstreamDropdown != null && workstreamDropdown.getSelectedId() != null) {
//                params.put("workstreamid", "" + workstreamDropdown.getSelectedId());
//            }
//            Utils.sendPDFOrExcelRequest(FastTimesheet.this, pdfUrl, params, "_blank");
//        });
//        menuBar.addItem(pdfVersion);
//        MenuPopItem csvVersion = new MenuPopItem(wfmStrings.excelVersion());
//        csvVersion.setEnabled(enabled);
//        csvVersion.ensureDebugId("timeSheet_list_view_excel_version_button");
//        csvVersion.setScheduledCommand(() -> {
//            String csvURL = GWT.getHostPageBaseURL() + CommandConstants.COMMON_URL + "/downloadTimesheetCsv";
//            csvURL += ("?date=" + DateUtils.formatToParse(clientsDate.getNonConvertedDate()));
//            csvURL += ("&weekoffset=" + weekOffset);
//            csvURL += ("&selectedDate=" + DateUtils.formatToParse(dateBox.getDate()));
//        });
//        menuBar.addItem(csvVersion);
//        return menuBar;
//    }

    @Override
    public String getIconStyle() {
        return "bgMark icon-fasttimesheet";
    }

    @Override
    public FlowPanel getHelpContainer() {
        final WestPanelHelp westPanel = new WestPanelHelp("<b>" + property.getSingular(projectStrings.timesheetStatus(),wfmStrings.timesheet()) + "</b>");
        final StringBuilder sb = new StringBuilder();
        sb.append("<div><b>12:00</b>&nbsp;").append(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.time())).append("<br/><br/>");
        sb.append("<font color='#008000'><b>12:00</b></font>&nbsp;").append(projectStrings.approvedTimeByManager()).append("<br/><br/>");
        sb.append("<font color='#cc6e00'><b>12:00</b></font>&nbsp;").append(wfmStrings.waitingForApproval()).append("<br/><br/>");
        sb.append("<font color='#ff0000'><b>12:00</b></font>&nbsp;").append(projectStrings.rejectedTime()).append("<br/><br/></div>");
        sb.append("<i>12:00</i>&nbsp;&nbsp;").append(property.getSingular(projectStrings.failedToSaveTimesheet(), wfmStrings.timesheet())).append("<br/><br/>");
        final HTML html = new HTML(sb.toString());
        westPanel.addHtmlLine(html);
        final FlowPanel wc = new FlowPanel();
        final VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(3);
        vp.add(westPanel);
        vp.setWidth("100%");
        wc.add(vp);

        return wc;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
                LoadingPanel.loading(true);
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return Constants.TIMESHEET;
    }
}
