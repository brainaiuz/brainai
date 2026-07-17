package com.edatasite.workforce.gwt.dashboardwidget.client.view.pm;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardPMItem;
import com.edatasite.workforce.gwt.dashboardwidget.client.rpc.DashboardWidgetService;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hurshid on 5/10/2018.
 */
public class DashboardTasksDueToday extends DashboardBaseWidget {
    private MaterialPanel panel = new MaterialPanel("gwt-wrapper");
    private MaterialPanel widgetContent = new MaterialPanel("widget-content widget-list");
    private CRMLookUp assigneeLookUp;
    boolean showAll = false;

    public DashboardTasksDueToday(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    @Override
    protected void initInternal() {
        setTitle(wfmStrings.tasksDueToday());

        showAll = Utils.hasPermission(PermissionConstants.PM_SHOW_ALL_TASKS);

        mainPanel.addStyleName("widget--task-due widget--row-links");
        mainPanel.add(new DashboardFooter());
        panel.add(widgetContent);
        contentPanel.add(panel);

        Div headerWidget = new Div("widget-heading--title");
        headerWidget.add(new HTML(DateUtils.format(new Date())));
        actionPanel.add(headerWidget);

        Date date = new Date();
        Date startDate = new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
        Date end = new Date(date.getYear(), date.getMonth(), date.getDate(), 23, 59, 59);

        assigneeLookUp = new CRMLookUp(LookUpConstants.TASK_ASSIGNEE);
        assigneeLookUp.addStyleName(Constants.DEFAULT_WIDTH);
        assigneeLookUp.setFullSearch(true);
        assigneeLookUp.showClearButton();
        assigneeLookUp.getClearButton().addStyleName("dashboard__reset");
        assigneeLookUp.ensureDebugId("task_assignee");
        assigneeLookUp.getFilterParametrs().setStartDate(startDate);
        assigneeLookUp.getFilterParametrs().setEndDate(end);
        assigneeLookUp.getFilterParametrs().setNewType(true);
        assigneeLookUp.getSuggestBox().addSelectionHandler(event -> {
            if (assigneeLookUp.isSelected()) {
                setStart(0);
                getData(assigneeLookUp.getSelectedItemID());
            }
        });
        assigneeLookUp.setClearCommand(this::loadComponentData);
        if (showAll) {
            filterPanel.add(assigneeLookUp);
        }

        initListeners();
    }

    private void initListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_ADD, DashboardTasksDueToday.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_EDIT, DashboardTasksDueToday.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_DELETE, DashboardTasksDueToday.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESHEET_TASK_STATUS_CHANGED, DashboardTasksDueToday.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DASHBOARD_TASK_REFRESH, DashboardTasksDueToday.this, (sender, args) -> loadComponentData());
    }

    protected void getData() {
        assigneeLookUp.clearOracleItems();
        assigneeLookUp.clearAndClearItems();
        getData(null);
    }

    protected void getData(Integer employeeID) {
        assigneeLookUp.setVisible(true);
        if (start == 0) {
            LoadingWidgets.get(getCode()).show();
        }
        Date date = new Date();
        Date startDate = new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
        Date end = new Date(date.getYear(), date.getMonth(), date.getDate(), 23, 59, 59);

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(startDate);
        fp.setEndDate(end);
        fp.setStart(getStart());
        fp.setLimit(getLimit());
        fp.setEmployeeId(employeeID);
        fp.setShowTasks(showAll);
        DashboardWidgetService.App.get().getTasksByDate(fp, new AbstractAsyncCallback<ArrayList<DashboardPMItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingWidgets.get(getCode()).hide();
                clearPanel();
            }

            @Override
            public void onSuccess(ArrayList<DashboardPMItem> result) {
                LoadingWidgets.get(getCode()).hide();
                if (result != null && result.size() > 0) {
                    if (start == 0) {
                        clearAddPanel(false);
                    }
                    initializeTable(result);
                } else if (start == 0) {
                    noData();
                }
                if (command != null) {
                    command.execute(result != null ? result.size() : 0);
                }
            }
        });
    }

    private void clearAddPanel(boolean noData) {
        widgetContent.clear();
        panel.clear();
        if (!noData && !fromSettings) {
            contentPanel.removeStyleName(noDataClass);
            contentPanel.clear();
        }
        panel.add(widgetContent);
        contentPanel.add(panel);
    }

    private void initializeTable(List<DashboardPMItem> result) {

        result.forEach(item -> {

            Div row = new Div("widget-row");
            row.addClickHandler(event -> {
                if (item.getObjectID() != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + item.getObjectID());
                }
            });

            Div number = new Div("widget-row__task-num");
            Div text = new Div("widget-row__text");
            Div rowEnd = new Div("widget-row__end");

            row.add(number);
            row.add(text);
            row.add(rowEnd);

            Element dl = DOM.createElement("dl");
            dl.setClassName("widget-row__text-dl");
            Element dt1 = DOM.createElement("dt");
            dt1.setInnerText(item.getStatus());
            Element dt2 = DOM.createElement("dd");
            dt2.setInnerText("#" + item.getNumber());
            dl.appendChild(dt1);
            dl.appendChild(dt2);
            number.getElement().appendChild(dl);

            Element nameDl = DOM.createElement("dl");
            nameDl.setClassName("widget-row__text-dl");
            Element dt = DOM.createElement("dt");
            Element dd = DOM.createElement("dd");
            dt.setInnerText(item.getName());
            dd.setInnerText(item.getProjectName());
            nameDl.appendChild(dt);
            nameDl.appendChild(dd);
            text.getElement().appendChild(nameDl);

            Div action = new Div("todo-action todo-cat--3");
            Element indicator = DOM.createSpan();
            indicator.setClassName("todo-indicator");
            indicator.setInnerHTML("<em>" + item.getPriority() + "</em>");
            action.getElement().appendChild(indicator);
            rowEnd.add(action);

            widgetContent.add(row);
        });
    }

    @Override
    public String getCode() {
        return gridItemConfig.getComponentCode();
    }

    @Override
    protected void getSampleData(boolean noData) {
        assigneeLookUp.setVisible(false);
        ArrayList<DashboardPMItem> items = new ArrayList<>();

        DashboardPMItem item1 = new DashboardPMItem(null);
        item1.setProjectName("Deepmind Software Development");
        item1.setName("Prepare for implementation");
        item1.setPriority("High");
        item1.setNumber("T0001");
        item1.setStatus("Not Started");
        items.add(item1);

        DashboardPMItem item2 = new DashboardPMItem(null);
        item2.setProjectName("Global Air Services");
        item2.setName("Install the product in the production environment");
        item2.setPriority("Medium");
        item2.setNumber("T0002");
        item2.setStatus("In progress");
        items.add(item2);

        DashboardPMItem item3 = new DashboardPMItem(null);
        item3.setProjectName("Deimos Constructions");
        item3.setName("Supply Construction Agreement");
        item3.setPriority("Medium");
        item3.setNumber("T0003");
        item3.setStatus("In progress");
        items.add(item3);

        DashboardPMItem item4 = new DashboardPMItem(null);
        item4.setProjectName("Kryptonite Sales Cycle");
        item4.setName("Supply Lot Sale Agreement");
        item4.setPriority("High");
        item4.setNumber("T0004");
        item4.setStatus("On Hold");
        items.add(item4);

        DashboardPMItem item5 = new DashboardPMItem(null);
        item5.setProjectName("Gemini security update procedure");
        item5.setName("Implement distributed data feeds");
        item5.setPriority("Low");
        item5.setNumber("T0005");
        item5.setStatus("Not Started");
        items.add(item5);

        clearAddPanel(noData);

        initializeTable(items);
    }
}
