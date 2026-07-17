package com.edatasite.workforce.gwt.task.client.ui.view.tabpanels;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionField;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamSingleItem;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import gwt.material.design.client.ui.MaterialPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 6, 2011
 * Time: 4:59:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkstreamPlannedActualTab extends CustomTabWidget {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final WorkstreamSingleItem workstream;

    public WorkstreamPlannedActualTab(String tabName, WorkstreamSingleItem workstream) {
        super(tabName);
        this.workstream = workstream;
    }

    @Override
    public void initData() {
    }

    @Override
    public void viewShow() {
        PreviewSectionField plannedFieldFirst = new PreviewSectionField("25%", "30%", 8, 8);
        plannedFieldFirst.addField(wfmStrings.startDate(), DateUtils.format(workstream.getStartDate()));
        if (!Utils.hasRole(Constants.CLIENT)) {
            plannedFieldFirst.addField(wfmStrings.estimatedTime(), Utils.formatMinutes(workstream.getEstimatedTime()));
            plannedFieldFirst.addField(wfmStrings.costEstimated(), Utils.formatDouble(workstream.getEstimatedCost()));
        }

        PreviewSectionField plannedFieldSecond = new PreviewSectionField("25%", "30%", 8, 8);
        plannedFieldSecond.addField(wfmStrings.dueDate(), DateUtils.format(workstream.getEndDate()));
        if (!Utils.hasRole(Constants.CLIENT)) {
            plannedFieldSecond.addField(wfmStrings.actualTimeSpent(), Utils.formatMinutes(workstream.getActualTime()));
            plannedFieldSecond.addField(wfmStrings.actualCost(), Utils.formatDouble(workstream.getActualCost()));
        }
        MaterialPanel firstPanel = new MaterialPanel("preview_section--field");
        MaterialPanel secondPanel = new MaterialPanel("preview_section--field");

        firstPanel.add(plannedFieldFirst);
        secondPanel.add(plannedFieldSecond);

        Grid grid = new Grid(1, 2);
        grid.setSize("100%", "100%");
        grid.setCellPadding(3);
        grid.setCellSpacing(3);
        grid.setWidget(0, 0, firstPanel);
        grid.setWidget(0, 1, secondPanel);
        grid.getCellFormatter().setWidth(0, 0, "50%");
        grid.getCellFormatter().setWidth(0, 1, "50%");
        grid.getCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
        grid.getCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
        clear();
        add(grid);
    }
}
