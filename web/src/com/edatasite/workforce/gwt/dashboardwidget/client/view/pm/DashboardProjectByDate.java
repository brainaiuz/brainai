package com.edatasite.workforce.gwt.dashboardwidget.client.view.pm;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingWidgets;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
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
public class DashboardProjectByDate extends DashboardBaseWidget {

    private MaterialPanel panel = new MaterialPanel("gwt-wrapper");
    private MaterialPanel widgetContent = new MaterialPanel("widget-content widget-list");
    private boolean showAll;

    public DashboardProjectByDate(DashboardComponentItem gridItemConfig) {
        this.gridItemConfig = gridItemConfig;
    }

    @Override
    protected void initInternal() {
        showAll = Utils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS);
        setTitle(wfmStrings.projectDueThisMonth());
        mainPanel.addStyleName("widget--task-due widget--row-links");
        mainPanel.add(new DashboardFooter());

        panel.add(widgetContent);
        contentPanel.add(panel);

        Div headerWidget = new Div("widget-heading--title");
        headerWidget.add(new HTML(DateUtils.monthYearFormat.format(new Date())));
        actionPanel.add(headerWidget);

        initListeners();
    }

    private void initListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_ADD, DashboardProjectByDate.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_EDIT, DashboardProjectByDate.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PROJECT_DELETE, DashboardProjectByDate.this, (sender, args) -> loadComponentData());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DASHBOARD_PROJECT_REFRESH, DashboardProjectByDate.this, (sender, args) -> loadComponentData());
    }

    @Override
    protected void getData() {
        if (start == 0) {
            LoadingWidgets.get(getCode()).show();
        }

        Date startTime = DateUtil.getMonthFirstDay(new Date());
        Date end = DateUtil.getMonthLastDate(new Date());

        Date startDate = new Date(startTime.getYear(), startTime.getMonth(), startTime.getDate(), 0, 0, 0);
        Date endDate = new Date(end.getYear(), end.getMonth(), end.getDate(), 23, 59, 59);

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(startDate);
        fp.setEndDate(endDate);
        fp.setStart(getStart());
        fp.setLimit(getLimit());
        fp.setShowProject(true);

        DashboardWidgetService.App.get().getProjectsByDate(fp, new AbstractAsyncCallback<ArrayList<DashboardPMItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                clearPanel();
                LoadingWidgets.get(getCode()).hide();
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
                    SinksContainerFactory.entryPoint.onHistoryChanged("project|summary/" + item.getObjectID());
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
            dt1.setInnerText(DateUtils.format(item.getDeadLine()));
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
            dd.setInnerText(item.getManager());
            nameDl.appendChild(dt);
            nameDl.appendChild(dd);
            text.getElement().appendChild(nameDl);

            Div action = new Div("todo-action");
            Element indicator = DOM.createSpan();
            indicator.setClassName("todo-indicator todo-indicator--custom-circle");
            indicator.getStyle().setColor(item.getReferenecColor());
            indicator.setInnerHTML("<em>" + item.getStatus() + "</em>" + "<span class='todo-indicator__circle' style='background-color: " + item.getReferenecColor() + "'></span>");
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
    public void getSampleData(boolean noData) {

        List<DashboardPMItem> items = new ArrayList<>();

        DashboardPMItem item1 = new DashboardPMItem(null);
        item1.setProjectName("Deepmind Software Development");
        item1.setName("Deepmind Software Development");
        item1.setNumber("P0001");
        item1.setStatus("Not Started");
        item1.setManager("John Smith");
        item1.setDeadLine(new Date());
        items.add(item1);

        DashboardPMItem item2 = new DashboardPMItem(null);
        item2.setProjectName("Global Air Services");
        item2.setName("Global Air Services");
        item2.setNumber("P0002");
        item2.setStatus("In progress");
        item2.setManager("Helena Johnson");
        item2.setDeadLine(new Date());
        items.add(item2);

        DashboardPMItem item3 = new DashboardPMItem(null);
        item3.setProjectName("Deimos Constructions");
        item3.setName("Deimos Constructions");
        item3.setNumber("P0003");
        item3.setStatus("In progress");
        item3.setManager("Monica Sandres");
        item3.setDeadLine(new Date());
        items.add(item3);

        DashboardPMItem item4 = new DashboardPMItem(null);
        item4.setProjectName("Kryptonite Sales Cycle");
        item4.setName("Kryptonite Sales Cycle");
        item4.setNumber("P0004");
        item4.setStatus("On Hold");
        item4.setManager("Chris Schroeder");
        item4.setDeadLine(new Date());
        items.add(item4);

        DashboardPMItem item5 = new DashboardPMItem(null);
        item5.setProjectName("Gemini security update procedure");
        item5.setName("Gemini security update procedure");
        item5.setNumber("P0005");
        item5.setStatus("Not Started");
        item5.setManager("Vincent Ricci");
        item5.setDeadLine(new Date());
        items.add(item5);

        clearAddPanel(noData);

        initializeTable(items);
    }
}
