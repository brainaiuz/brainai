package com.edatasite.workforce.gwt.hrms.client.ui.orgchart;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.PanelUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnTool;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ColumnColorForOrgChart;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ColumnColorSettings;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ColumnPanelWidget;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.location.client.rpc.LocationService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.HtmlLabel;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Label;

import java.util.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by dilsh0d on 27.03.16.
 */
public class HrTeamOrgChart extends Composite {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private boolean showKanbanView = true;
    private SvgIcon listKanbanSwitch;
    private ActionButton kanbanViewSwitcher;
    private MaterialLink landscape;
    private WfmButton2 save;
    private DepartmentLookUp departmentLookUp;
    @UiField
    HTMLPanel locationDBLClick;
    private DepartmentLookUp departmentDBLClickLookUp;
    private AbsolutePanel boundaryPanel;
    private PickupDragController showColumnDragController;
    private VerticalPanel showVerticalPanel;
    private KpiSideNavBox navBox;
    private boolean changeStyle;
    private boolean changeStyleForRightClick;
    private Integer oldIdForDblClick;
    private Integer showAllSubMembersId;
    private Integer showMembersId;
    private ColumnColorSettings colorSettings;
    private ListPanelToolRpc panelTools;
    private LinkedHashMap<String, ColumnTool> columnsTool;
    private boolean isFirstInit = true;
    @UiField
    Label externalEmployeeLabel;

    interface TeamOrgChartUiBinder extends UiBinder<HTMLPanel, HrTeamOrgChart> {
    }

    private static final TeamOrgChartUiBinder ourUiBinder = GWT.create(TeamOrgChartUiBinder.class);

    @UiField
    HTMLPanel switchDiv;
    @UiField
    MaterialButton refreshBtn;
    @UiField
    MaterialButton resetBtn;
    @UiField
    DivElement content;
    @UiField
    DataListBox levelOptionList;
    @UiField
    HTMLPanel exportBtn;
    @UiField
    HTMLPanel customizeBtn;
    @UiField
    HTMLPanel departmentDBLClick;
    @UiField
    Label locationLabel;
    @UiField
    Label departmentLabel;
    @UiField
    Label levelLabel;
    @UiField
    KpiSwitcher externalEmployee;
    private LocationLookUpWithCode location;
    private boolean isShowExternalEmployee = false;


    public HrTeamOrgChart() {
        onInit();
    }

    private void onInit() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        getMaxLevelData(levelOptionList);
        initOpenTeamOrgChartScriptMethod();
        initJScriptMethod();
        onClickEventHandler();
        LoadingPanel.loading(true);
        if (!Utils.hasPermission(PermissionConstants.SHOW_EXTERNAL_EMPLOYEE)) {
            externalEmployee.setVisible(false);
            externalEmployeeLabel.setVisible(false);
        }
        navBox = new KpiSideNavBox();
        String hrTeamOrgChart = "hr_team_org_chart_";
        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.customize());
        navBox.addHeader(header);
        departmentLabel.setText(wfmStrings.department());
        departmentDBLClickLookUp = new DepartmentLookUp();
        departmentDBLClickLookUp.ensureDebugId(hrTeamOrgChart + "departmentLookUp");
        departmentDBLClickLookUp.addStyleName(DEFAULT_WIDTH);
        departmentDBLClickLookUp.getSuggestBox().addSelectionHandler(ch -> {
            LoadingPanel.loading(true);
            getDepartments(departmentLookUp.getSelectedItemID());
            oldIdForDblClick = departmentDBLClickLookUp.getSelectedItemID();
            getData(showKanbanView, levelOptionList, departmentDBLClickLookUp.getSelectedItemID(), null, null, null, false);
            LoadingPanel.loading(false);
        });
        departmentDBLClick.add(departmentDBLClickLookUp);
        locationDBLClick.setVisible(false);
        location = new LocationLookUpWithCode();
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ORG_CHART_BY_LOCATION)) {
            locationLabel.setText(Property.getPluralWithObjectCode(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.locations()));
            location.addStyleName(DEFAULT_WIDTH);
            location.getSuggestBox().addSelectionHandler(ch -> {
                onLocationChange(location.getSelectedItemID());
            });
            locationDBLClick.add(location);
            locationDBLClick.setVisible(true);
        }
        HtmlLabel label = new HtmlLabel();
        label.setText(wfmStrings.customizeOrder());
        navBox.addBody(label);
        departmentLookUp = new DepartmentLookUp();
        departmentLookUp.ensureDebugId(hrTeamOrgChart + "departmentLookUp");
        departmentLookUp.addStyleName(DEFAULT_WIDTH);

        departmentLookUp.getSuggestBox().addSelectionHandler(ch -> {
            LoadingPanel.loading(true);
            getDepartments(departmentLookUp.getSelectedItemID());
        });

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.addClickHandler(clickEvent -> {
            save();
        });
        navBox.addFooter(save);
        navBox.addBody(departmentLookUp);

        //Customize Button
        ActionButton orgChartCustomize = new ActionButton("", "btn btn--icon");
        orgChartCustomize.ensureDebugId("customise_button");
        SvgIcon customizeIcon = new SvgIcon(SvgEnum.sliders);
        orgChartCustomize.add(customizeIcon);
        new MaterialTooltip(orgChartCustomize, wfmStrings.customize()).setPosition(Position.TOP);
        orgChartCustomize.addClickHandler(clickEvent -> {
            navBox.open();
        });
        customizeBtn.add(orgChartCustomize);

        //Export to PDF Panel
        FlowPanel flowPanelOrgChart = new FlowPanel();
        flowPanelOrgChart.setStyleName("operPanel__actions");

        MaterialMenuBar importExportMenu = new MaterialMenuBar();
        importExportMenu.setClass("btn-group dropdown-kit--arrow--below");
        flowPanelOrgChart.add(importExportMenu);

        MaterialLink ieLink = new MaterialLink();//import export button link for listing top panel
        ieLink.setTooltip(wfmStrings.importExport());
        ieLink.setTooltipPosition(Position.TOP);
        ieLink.setHref("#");
        ieLink.setClass("btn btn--icon btn--white");

        Icon ieIcon = new Icon();//import export icon for listing top panel
        ieIcon.setClass("ficon--download-cloud");
        ieLink.add(ieIcon);

        importExportMenu.add(ieLink);
        exportBtn.add(flowPanelOrgChart);
        ieLink.add(HrTeamOrgChart.this::getLandscapeLink);
        ieLink.addClickHandler(clickEvent -> {
            LoadingPanel.loading(true);
            sendPdfRequest();
            LoadingPanel.loading(false);
        });

        levelLabel.setText(wfmStrings.level());

        //Level Option List
        levelOptionList.addValueChangeHandler(clickEvent -> {
                    LoadingPanel.loading(true);
                    getMaxLevelData(levelOptionList);
                }
        );

        //Refresh Button
        refreshBtn.ensureDebugId("reload_button");
        refreshBtn.addStyleName("btn--icon");
        refreshBtn.addClickHandler(clickEvent -> {
                }
        );
        refreshBtn.add(new SvgIcon(SvgEnum.rotateCw));
        new MaterialTooltip(refreshBtn, wfmStrings.refresh()).setPosition(Position.TOP);

        //Reset Button
        resetBtn.ensureDebugId("reset_button");
        resetBtn.addStyleName("btn--icon");
        resetBtn.add(new SvgIcon(SvgEnum.repeat));
        new MaterialTooltip(resetBtn, wfmStrings.reset()).setPosition(Position.TOP);
        resetBtn.addClickHandler(clickEvent -> {
                    LoadingPanel.loading(true);
                    levelOptionList.resetSelectedItem();
                    getMaxLevelData(levelOptionList);
                    departmentDBLClickLookUp.clear();
                    location.clear();
                    oldIdForDblClick = null;
                    showAllSubMembersId = null;
                    showMembersId = null;

                }
        );

        //View Switcher
        kanbanViewSwitcher = new ActionButton("", "btn btn--icon");
        listKanbanSwitch = new SvgIcon(SvgEnum.barChart);
        setSwitchStyle(showKanbanView);
        kanbanViewSwitcher.add(listKanbanSwitch);
        kanbanViewSwitcher.addClickHandler(clickEvent -> {
            LoadingPanel.loading(true);
            showKanbanView = !showKanbanView;
            setSwitchStyle(showKanbanView);
            getData(showKanbanView, levelOptionList, departmentDBLClickLookUp.isSelected() ? departmentDBLClickLookUp.getSelectedItemID() : null, null, null, null, false);
        });


        externalEmployeeLabel.setText(wfmStrings.external() + " " + wfmStrings.employees());
        externalEmployee.addValueChangeHandler(clickEvent -> {
            this.isShowExternalEmployee = externalEmployee.getValue();
            LoadingPanel.loading(true);
            getData(showKanbanView, levelOptionList, departmentDBLClickLookUp.isSelected() ? departmentDBLClickLookUp.getSelectedItemID() : null, null, null, null, false);
        });


        switchDiv.add(kanbanViewSwitcher);
    }

    private void save() {

        if (showVerticalPanel != null && showVerticalPanel.getWidgetCount() > 0) {
            GWT.log("save ga kirdi");
            LinkedList<SelectItem> items = new LinkedList<>();
            LinkedList<String> colorItems = new LinkedList<>();
            for (int i = 0; i < showVerticalPanel.getWidgetCount(); i++) {
                Widget widget = showVerticalPanel.getWidget(i);
                ColumnTool columnTool = new ColumnTool();
                SelectItem item = (SelectItem) widget.getLayoutData();
                GWT.log("save Select item dan keyn");
                for (ColumnColor color : getDataForColor()) {
                    GWT.log("save ColumnColor loop ga kirdi");
                    columnTool.addColor(color);
                }
                items.add(item);
                colorItems.add(String.valueOf(columnTool));
                GWT.log(String.valueOf(columnTool));
            }

            DepartmentService.App.get().saveCustomizationOrgChart(items, colorItems, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(Void v) {
                    LoadingPanel.loading(false);
                    save.setEnabled(true);
                    getMaxLevelData(levelOptionList);
                    boundaryPanel.clear();
                    departmentLookUp.clear();
                    navBox.hide();

                }
            });
        } else {
            navBox.hide();
        }
    }

    private ArrayList<ColumnPanelWidget> getAllColumns() {
        ArrayList<ColumnPanelWidget> result = new ArrayList<>();
        for (int i = 0; i < showVerticalPanel.getWidgetCount(); i++) {
            if (showVerticalPanel.getWidget(i) instanceof ColumnPanelWidget) {
                result.add((ColumnPanelWidget) showVerticalPanel.getWidget(i));
            }
        }
        return result;
    }

    private void drawForm() {
        // draggable container
        boundaryPanel = new AbsolutePanel();

        // initialize vertical panel to hold our columns
        showVerticalPanel = new VerticalPanel();

        boundaryPanel.add(showVerticalPanel);

        // initialize our column drag controller
        showColumnDragController = new PickupDragController(boundaryPanel, false);
        showColumnDragController.setBehaviorMultipleSelection(false);

        // initialize our column drop controller
        VerticalPanelDropController columnDropController = new VerticalPanelDropController(showVerticalPanel);
        showColumnDragController.registerDropController(columnDropController);

        navBox.addBody(boundaryPanel);
    }

    private void createNewColumnPanel(SelectItem item, List<SelectItem> selectItems) {
        ColumnPanelWidget pColumn = new ColumnPanelWidget(item, selectItems);
        pColumn.setLayoutData(item);
        showVerticalPanel.add(pColumn);
        pColumn.makeDraggable(showColumnDragController);
        showColumnDragController.addDragHandler(new DragHandlerAdapter() {
            @Override
            public void onDragEnd(DragEndEvent event) {
            }

            @Override
            public void onDragStart(DragStartEvent event) {
                if (event.getSource() instanceof ColumnPanelWidget) {
                    ((ColumnPanelWidget) event.getSource()).closeColorPanel();
                }
            }
        });

    }


    private SelectItem setLocation() {
        LoadingPanel.loading(true);
        getDepartments(Utils.getUserDepartment());
        departmentDBLClickLookUp.setSelected(Utils.getUserDepartmentAsSelectItem());
        oldIdForDblClick = departmentDBLClickLookUp.getSelectedItemID();
        getData(showKanbanView, levelOptionList, departmentDBLClickLookUp.getSelectedItemID(), null, null, null, false);
        LoadingPanel.loading(false);
        return Utils.getUserlocationAsSelectItem();
    }

    private void onLocationChange(Integer locationId) {
        if (location.getSelectedItemID() != null || locationId != null) {
            AllInOneService.App.get().getDepartmentsByLocationAsSelectItem(location.getSelectedItemID() != null ? location.getSelectedItemID() : locationId, new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(SelectItem[] selectItems) {
                    departmentDBLClickLookUp.setItems(null, selectItems);
                    getDepartments(departmentDBLClickLookUp.getSelectedItemID());
                    oldIdForDblClick = departmentDBLClickLookUp.getSelectedItemID();
                    getData(showKanbanView, levelOptionList, null, null, null, locationId, false);
                }
            });

        }
    }

    private void getDepartments(Integer parentId) {
        DepartmentService.App.get().getDepartmentsForCustomization(parentId, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                if (boundaryPanel != null) {
                    boundaryPanel.clear();
                }
                drawForm();
                for (SelectItem item : selectItems) {
                    createNewColumnPanel(item, Arrays.asList(selectItems));
                }
                LoadingPanel.loading(false);

            }
        });
    }

    public List<ColumnColor> getColorDataForOrgChart() {
        GWT.log("getColorDataForOrgChart");
        return colorSettings.getDataForOrgChart();
    }

    public List<ColumnColor> getDataForColor() {
        List<ColumnColor> result = new ArrayList<>();
        for (int i = 0; i < showVerticalPanel.getWidgetCount(); i++) {
            Widget widget = showVerticalPanel.getWidget(i);
            if (widget instanceof ColumnColorForOrgChart) {
                ColumnColorForOrgChart panel = (ColumnColorForOrgChart) widget;
                if (panel.getData() != null) {
                    result.add(panel.getData());
                }
            }
        }
        return result;
    }

    public void setColorDataForOrgChart(List<ColumnColor> data) {
        colorSettings.setDataForOrgChart(data);
    }

    private void getDepartmentsForDBLClickEvent(Integer oldIdForDblClick) {
        getData(showKanbanView, levelOptionList, oldIdForDblClick, null, null, null, false);
    }

    private void getEmployeesForClickEvent(Integer nodeId) {
        getData(showKanbanView, levelOptionList, oldIdForDblClick, nodeId, null, null, false);
    }

    private void getEmployeesForClickEventForSubDepartments(Integer showAllId) {
        getData(showKanbanView, levelOptionList, oldIdForDblClick, null, showAllId, null, false);
    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
        }
        return landscape;
    }

    private void sendPdfRequest() {
        String URL = (CommandConstants.PDF_URL + "/OrganizationChart");
        final ListingFilterParameter filter = new ListingFilterParameter();
        Integer selectedLevel = levelOptionList.getSelectedId() != null ? levelOptionList.getSelectedId() + 1 : null;
        filter.setLandscape(true);
        filter.setShowMembersForOrgChart(showMembersId);
        filter.setShowAllSubMembersForOrgChart(showAllSubMembersId);
        filter.setDepartmentDoubleClickId(oldIdForDblClick);
        filter.setShowView(showKanbanView);
        filter.setLevelOptionList(selectedLevel);
        Utils.sendPDFOrExcelRequest(exportBtn, URL, filter.getRequestParams(), "_blank");
    }

    private void setSwitchStyle(boolean active) {
        if (active) {
            new MaterialTooltip(kanbanViewSwitcher, wfmStrings.horizontalView()).setPosition(Position.TOP);
            listKanbanSwitch.addClassName("active");
        } else {
            listKanbanSwitch.removeClassName("active");
            new MaterialTooltip(kanbanViewSwitcher, wfmStrings.verticalView()).setPosition(Position.TOP);
        }

    }


    private String oldElementID = null;

    private void getData(boolean isShowView, DataListBox levelOptionList, Integer parentId, Integer nodeId, Integer showAllId, Integer locationId, boolean fromClickEvents) {
        Integer selectedLevel = levelOptionList.getSelectedId() != null ? levelOptionList.getSelectedId() + 1 : null;
        LoadingWidgets.get("getData").show();
        DepartmentService.App.get().getTeamGraphChart(isShowView, selectedLevel, true, parentId, nodeId, showAllId, isShowExternalEmployee, locationId != null ? locationId : location.getSelectedItemID(), fromClickEvents, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                LoadingWidgets.get("getData").hide();
            }

            @Override
            public void success(String result) {
                LoadingWidgets.get("getData").hide();
                content.setInnerHTML(result);

                changeElementClass();
                changeElementClassForRightClick();
                LoadingPanel.loading(false);
            }
        });
    }

    private void changeElementClass() {
        if (changeStyle) {
            NodeList<Element> elements = content.getElementsByTagName("table");
            for (int i = 0; i < elements.getLength(); i++) {
                Element elm = elements.getItem(i);
                if (elm.getId() != null && !elm.getClassName().contains("has-data") && elm.getClassName().contains("has-subNodes")) {
                    elm.removeClassName("hide-data");
                    elm.addClassName("has-data");
                    elm.addClassName("show-data");
                } else if (elm.getId() != null && elm.getClassName().contains("show-data") && elm.getClassName().contains("has-subNodes")) {
                    elm.removeClassName("show-data");
                    elm.addClassName("hide-data");
                } else if ((elm.getId() != null && elm.getClassName().contains("hide-data") && elm.getClassName().contains("has-subNodes"))) {
                    elm.removeClassName("hide-data");
                    elm.addClassName("show-data");
                }
            }
            changeStyle = false;
        }
    }


    private void changeElementClassForRightClick() {
        if (changeStyleForRightClick) {
            NodeList<Element> elements = content.getElementsByTagName("table");
            for (int i = 0; i < elements.getLength(); i++) {
                Element elm = elements.getItem(i);
                if (elm.getId() != null && elm.getId().equals(oldElementID) && elm.getClassName().contains("has-subNodes")) {
                    changeStyleForRightClick = false;
                    elm.addClassName("show-data");
                    elm.removeClassName("hide-data");
                }
            }
        }
    }

    private void onClickEventHandler() {
        DOM.addEventPreview(event -> {
            Element element = DOM.eventGetTarget(event);
            if (DOM.eventGetType(event) == Event.ONCLICK && element.getId().startsWith("empShow-")) {
                Integer teamId = Integer.valueOf(element.getId().replace("empShow-", ""));
                oldElementID = element.getId();
                changeStyleForRightClick = true;
                Element element1 = DOM.getElementById("table-" + teamId);
                oldElementID = element1.getId();
                if (!element.getClassName().contains("has-data")) {
                    getEmployeesForClickEvent(teamId);
                    showMembersId = teamId;
                } else if (element1.getClassName().contains("show-data")) {
                    element1.removeClassName("show-data");
                    element1.addClassName("hide-data");
                } else {
                    element1.removeClassName("hide-data");
                    element1.addClassName("show-data");
                }
            } else if (DOM.eventGetType(event) == Event.ONCLICK && element.getId().startsWith("allShow-")) {
                Integer allId = Integer.valueOf(element.getId().replace("allShow-", ""));
                oldElementID = element.getId();
                changeStyle = true;
                Element element2 = DOM.getElementById("table-" + allId);
                oldElementID = element2.getId();
                if (!element.getClassName().contains("has-data")) {
                    getEmployeesForClickEventForSubDepartments(allId);
                    showAllSubMembersId = allId;
                } else if (changeStyle) {
                    changeElementClass();
                }
            }
            if (DOM.eventGetType(event) == Event.ONDBLCLICK && element.getId().startsWith("teamboard-:-")) {
                String[] teamName = element.getId().split("-:-");
                Integer teamId = Integer.valueOf(teamName[1]);
                String teamN = teamName[2];
                oldIdForDblClick = teamId;
                getDepartmentsForDBLClickEvent(oldIdForDblClick);
                departmentDBLClickLookUp.setSelected(teamId, teamN);
                LoadingPanel.loading(true);
            }
            return true;
        });
    }

    private void getMaxLevelData(DataListBox levelOptionList) {
        LoadingWidgets.get("getData").show();
        DepartmentService.App.get().maxChildLevels(new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                LoadingWidgets.get("getData").hide();
            }

            @Override
            public void success(Integer result) {
                ArrayList<SelectItem> items = new ArrayList<>();
                for (int i = 1; i < result; i++) {
                    items.add(new SelectItem(i, wfmStrings.level() + " " + i));
                }
                levelOptionList.setItems(items.toArray(new SelectItem[]{}), wfmStrings.options());
                if (result > 4 && isFirstInit) {
                    levelOptionList.setSelected(3);
                }
                getData(showKanbanView, levelOptionList, null, null, null, null, true);
            }
        });
        isFirstInit = false;
    }

    public void openPopupSetParentTeamFromTree(final String id, String teamName, String parentId, String parentName) {
        final KpiModal shell = new KpiModal();
        shell.setWidth(400);
        shell.addStyleName("file--HrTeamOrgChart");
        final DepartmentLookUp parentDepartment = new DepartmentLookUp(Integer.valueOf(id), true);
        if (!"-1".equals(parentId)) {
            parentDepartment.setSelected(Integer.valueOf(parentId), parentName);
        }
        final WfmButton2 saveAndClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveAndClose.addClickHandler(clickEvent -> {
            saveAndClose.setEnabled(false);
            saveTeamParent(Integer.valueOf(id), parentDepartment.getSelectedItemID());
            shell.close();
        });
        final WfmButton2 closeShell = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_GREY);
        closeShell.addClickHandler(clickEvent -> {
            shell.close();
        });
        shell.getModalHeader().removeFromParent();

        WfmForm table = new WfmForm(new String[]{"99%", "1%"});
        table.addTitleField("<span style=\"font-size:12px!important\">" + Property.get(Constants.DEPARTMENT_LIST, wfmStrings.selectParentDepartment(), wfmStrings.department()) + "<i>" + teamName + "</i></span>");
        table.addField(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.parentDepartment(), wfmStrings.department()), parentDepartment);
        shell.add(table);
        shell.center();
        table.addStyleName("single-formGroup");
        shell.addButton(saveAndClose);
        shell.addButton(closeShell);
    }

    public void openTeamOrgChart(Integer locationId) {
        LoadingPanel.loading(true);
        LocationService.App.get().getLocationAsSelectItem(locationId, new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem result) {
                location.setSelected(result);
                onLocationChange(locationId);
            }
        });


    }

    private void saveTeamParent(Integer id, Integer parentId) {
        LoadingWidgets.get("saveData").show();
        DepartmentService.App.get().saveTeamParent(id, parentId, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingWidgets.get("saveData").hide();
            }

            @Override
            public void success(Void result) {
                LoadingWidgets.get("saveData").hide();
                getData(showKanbanView, levelOptionList, parentId, null, null, null, false);
            }
        });
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        PanelUtils.getBody().addClassName("hasOrgChartHeader");
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        PanelUtils.getBody().removeClassName("hasOrgChartHeader");
    }

    public native void initJScriptMethod() /*-{
        var that = this;
        $wnd.openPopupSetParentTeamFromTree = $entry(function (_id, team_name, parent_id, parent_name) {
            that.@com.edatasite.workforce.gwt.hrms.client.ui.orgchart.HrTeamOrgChart::openPopupSetParentTeamFromTree(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(_id, team_name, parent_id, parent_name);
        });
    }-*/;

    public native void initOpenTeamOrgChartScriptMethod() /*-{
        var that = this;
        $wnd.openTeamOrgChart = $entry(function (id) {
            that.@com.edatasite.workforce.gwt.hrms.client.ui.orgchart.HrTeamOrgChart::openTeamOrgChart(Ljava/lang/Integer;)(@java.lang.Integer::valueOf(I)(id));
        });
    }-*/;
}
