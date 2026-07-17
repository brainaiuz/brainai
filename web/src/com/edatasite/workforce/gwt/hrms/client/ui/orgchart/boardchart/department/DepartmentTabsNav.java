package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department;

import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTab;
import gwt.material.design.client.ui.MaterialTabItem;

import java.util.ArrayList;
import java.util.List;

public class DepartmentTabsNav {

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final DepartmentNode department;

    private final MaterialTab tabs = new MaterialTab();
    private final FlowPanel panes = new FlowPanel();

    private final List<Widget> paneList = new ArrayList<>();
    private final List<MaterialLink> linkList = new ArrayList<>();

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();


    public DepartmentTabsNav(DepartmentNode department) {
        this.department = department;

        tabs.addStyleName("tabs");
        tabs.addStyleName("tabs--underlined");

        panes.addStyleName("depTabs__panes");

        buildTabs();
        buildPanes();

    }

    public MaterialTab getTabsWidget() {
        return tabs;
    }

    public FlowPanel getPanesWidget() {
        return panes;
    }

    /**
     * Call after sidenav is opened (when final sizes are known).
     */
    public void forceTabsLayout() {
        Scheduler.get().scheduleDeferred(() ->
                Scheduler.get().scheduleDeferred(() -> tabs.reload())
        );
    }

    private void addTab(String text, String targetId) {
        MaterialTabItem item = new MaterialTabItem();
        MaterialLink link = new MaterialLink(text);

        link.setHref("#" + targetId);

        item.add(link);
        tabs.add(item);
        linkList.add(link);

        final int index = linkList.size() - 1;
        link.addClickHandler(e -> {
            setActivePane(index);
        });
    }


    private void buildTabs() {
        addTab(wfmStrings.employees(), "depTabEmployees");
        addTab(hrmsStrings.metricsWihoutDot(), "depTabMetrics");
        addTab(hrmsStrings.scripts(), "depTabScripts");
        addTab(hrmsStrings.jobDescription(), "depTabPosition");
        addTab(hrmsStrings.experiencesAndMistakes(), "depTabExperiences");
    }


    private void buildPanes() {
        addPane(new EmployeeTabContent(department), "depTabEmployees", "depTabs__pane--employees");
        addPane(new GoalsTabContent(department), "depTabMetrics", "depTabs__pane--metrics");
        addPane(new ScriptsTabContent(), "depTabScripts", "depTabs__pane--scripts");
        addPane(new PositionDescriptionTabContent(), "depTabPosition", "depTabs__pane--position");
        addPane(new ExperiencesTabContent(), "depTabExperiences", "depTabs__pane--experiences");
    }

    private void addPane(Widget pane, String targetId, String modifier) {
        pane.addStyleName("depTabs__pane");
        pane.addStyleName(modifier);
        pane.getElement().setId(targetId);
        pane.getElement().removeClassName("col");

        panes.add(pane);
        paneList.add(pane);
    }

    public void activateTab(String activeTab) {
        Scheduler.get().scheduleDeferred(() -> {
            int indexToActivate = 0; // По умолчанию Employees

            if ("GOAL".equals(activeTab)) indexToActivate = 1;
            else if ("SCRIPTS".equals(activeTab)) indexToActivate = 2;
            else if ("POSITION".equals(activeTab)) indexToActivate = 3;
            else if ("EXP".equals(activeTab)) indexToActivate = 4;

            setActivePane(indexToActivate);
            forceTabsLayout();
        });
    }

    private void setActivePane(int index) {
        for (int i = 0; i < paneList.size(); i++) {
            if (i == index) {
                paneList.get(i).addStyleName("active");

                String targetId = paneList.get(i).getElement().getId();
                tabs.selectTab(targetId);
            } else {
                paneList.get(i).removeStyleName("active");
            }
        }
    }
}
