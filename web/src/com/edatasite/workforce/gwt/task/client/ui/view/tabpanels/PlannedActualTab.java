package com.edatasite.workforce.gwt.task.client.ui.view.tabpanels;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionField;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 12-Feb-2010
 * Time: 17:05:25
 * To change this template use File | Settings | File Templates.
 */
public class PlannedActualTab extends CustomTabWidget {

    private WfmStrings wfmStrings;
    private ProjectStrings projectStrings;
    private TaskSingleItem result;
    
    private final HTML estemitedCost = new HTML("&nbsp;");
    private final HTML actualCost = new HTML("&nbsp;");
    private final int count1 = 0;
    private final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");


    DateTimeFormat dateFormat = DateUtils.getFormatInternal();

    public PlannedActualTab(String tabName) {
        super(tabName);
    }

    public void initData() {

    }

    public void viewShow() {
        String start;
        String due;
        String actualStart = DateUtils.format(result.getActualStartDate());
        String actualEnd = DateUtils.format(result.getActualEndDate());
        if (result.isAllDay() != null && result.isAllDay()) {
            start = DateUtils.format(result.getStartDate());
            Date dueDate = result.getDueDate();
            due = DateUtils.format(dueDate);
        } else {
            start = DateUtils.formatInternal(result.getStartDate());
            due = DateUtils.formatInternal(result.getDueDate());
        }
        if (Utils.hasRole(Constants.DR) || Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.PM)) {
            estemitedCost.setHTML("<b>" + result.getEstimatedCost() + "</b>");
            actualCost.setHTML("<b>" + result.getActualCost() + "</b>");
        }

        PreviewSectionField plannedField = new PreviewSectionField("25%", "30%");

        plannedField.addField(wfmStrings.startDate(), "<b>" + start + "</b>");
        plannedField.addField(wfmStrings.dueDate(), "<b>" + due + "</b>");
        if (!Utils.hasRole(Constants.CLIENT)) {
            plannedField.addField(wfmStrings.estimatedTime(), "<b>" + Utils.formatMinutes(result.getEstimatedTime()) + "</b>");
        }
        if (Utils.hasPermission(PermissionConstants.PM_TASKS_VIEW_PROJECT_COST)) {
            plannedField.addField(wfmStrings.costEstimated(), estemitedCost);
        }

        PreviewSectionField plannedField1 = new PreviewSectionField("25%", "25%");
        plannedField1.addField(wfmStrings.startDate(), "<b>" + actualStart + "</b>");
        plannedField1.addField(projectStrings.completedDateActual(), "<b>" + actualEnd + "</b>");
        if (!Utils.hasRole(Constants.CLIENT)) {
            plannedField1.addField(wfmStrings.actualTimeSpent(), "<b>" + Utils.formatMinutes(result.getActualTime()) + "</b>");
        }
        if (Utils.hasPermission(PermissionConstants.PM_TASKS_VIEW_PROJECT_COST)) {
            plannedField1.addField(wfmStrings.actualCost(), actualCost);
        }

        Grid grid = new Grid(1, 2);
        grid.setSize("100%", "100%");
        grid.setCellPadding(5);
        grid.setCellSpacing(5);
        grid.setWidget(0, 0, plannedField);
        grid.getCellFormatter().setWidth(0, 0, "50%");
        grid.getCellFormatter().setAlignment(0, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_TOP);
        grid.setWidget(0, 1, plannedField1);
        grid.getCellFormatter().setWidth(0, 1, "50%");
        grid.getCellFormatter().setAlignment(0, 1, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_TOP);
        grid.getElement().getStyle().setMarginTop(-12, Style.Unit.PX);
        add(grid);
    }

    private String formatToDouble(String text) {
        return numberFormat.format(parseToDouble(text));
    }

    private double parseToDouble(String text) {
        return Double.parseDouble(text.replace(",", ""));
    }

    public void setResult(TaskSingleItem result) {
        this.result = result;
    }

    public void setWfmStrings(WfmStrings wfmStrings) {
        this.wfmStrings = wfmStrings;
    }

    public void setProjectStrings(ProjectStrings projectStrings) {
        this.projectStrings = projectStrings;
    }
}
