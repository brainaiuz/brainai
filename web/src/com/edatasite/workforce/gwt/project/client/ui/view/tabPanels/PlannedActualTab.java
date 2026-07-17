package com.edatasite.workforce.gwt.project.client.ui.view.tabPanels;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionField;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectViewItem;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 13-Feb-2010
 * Time: 21:06:39
 * To change this template use File | Settings | File Templates.
 */
public class PlannedActualTab extends CustomTabWidget {

    private final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private ProjectStrings projectStrings;
    private WfmStrings strings;
    private ProjectViewItem result;
    private final HTML estemitedTime = new HTML("&nbsp;");
    private final HTML actualTime = new HTML("&nbsp;");
    private final HTML estemitedCost = new HTML("&nbsp;");
    private final HTML actualCost = new HTML("&nbsp;");
    private boolean isFirstLoad = true;

    public PlannedActualTab(String tabName) {
        super(tabName);
    }

    public void initData() {

    }

    public void viewShow() {
        clear();

        PreviewSectionField firstField = new PreviewSectionField("25%", "25%");
        firstField.addField(strings.startDate(), "<b>" + getDateFormat(result.getStartDate()) + "</b>");
        firstField.addField(strings.endDate(), "<b>" + getDateFormat(result.getEndDate()) + "</b>");
        if (!Utils.hasRole(Constants.CLIENT)) {
            firstField.addField(strings.estimatedTime(), estemitedTime);
        }
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_COST)) {
            firstField.addField(strings.costEstimated(), estemitedCost);
        }

        PreviewSectionField secondField = new PreviewSectionField("30%", "20%");
        secondField.addField(strings.actualStartDate(), "<b>" + getDateFormat(result.getActualStartDate()) + "</b>");
        secondField.addField(strings.actualEndDate(), "<b>" + getDateFormat(result.getActualEndDate()) + "</b>");
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_ACTUAL_TIME_SPENT)) {
            secondField.addField(strings.actualTimeSpent(), actualTime);
        }
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_COST)) {
            secondField.addField(strings.actualCost(), actualCost);
        }

        Grid grid = new Grid(1, 2);
        grid.setSize("100%", "100%");
        grid.setWidget(0, 0, firstField);
        grid.getCellFormatter().setAlignment(0, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_TOP);
        grid.setWidget(0, 1, secondField);
        grid.getCellFormatter().setAlignment(0, 1, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_TOP);

        add(grid);
        initInternal();
    }

    private void initInternal() {
        if (Utils.hasPermission(PermissionConstants.PM_PROJECT_COST)) {
            if (!isFirstLoad) {
                ProjectService.App.get().getProjectCostItems(result.getObjectID(), new AbstractAsyncCallback<ProjectViewItem>() {
                    public void failure(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    public void success(ProjectViewItem result) {
                        estemitedTime.setHTML("<b>" + result.getEstimatedTime() + "</b>");
                        estemitedCost.setHTML("<b>" + result.getEstimatedCost() + "</b>");
                        actualTime.setHTML("<b>" + result.getHoursSpent() + "</b>");
                        actualCost.setHTML("<b>" + result.getActualCost() + "</b>");
                    }
                });
            } else {
                actualTime.setHTML("<b>" + result.getHoursSpent() + "</b>");
                estemitedTime.setHTML("<b>" + result.getEstimatedTime() + "</b>");
                estemitedCost.setHTML("<b>" + result.getEstimatedCost() + "</b>");
                actualCost.setHTML("<b>" + result.getActualCost() + "</b>");
                isFirstLoad = false;
            }
        } else if (!Utils.hasRole(Constants.CLIENT)) {
            actualTime.setHTML("<b>" + result.getHoursSpent() + "</b>");
        }
    }

    private String formatIntToTime(double totalActualTime) {
        int minute = (int) totalActualTime % 60;
        int hour = ((int) totalActualTime - minute) / 60;
        return "" + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute;  //To change body of created methods use File | Settings | File Templates.
    }

    private String formatToDouble(String text) {
        return numberFormat.format(parseToDouble(text));
    }

    private double parseToDouble(String text) {
        return Double.parseDouble(text.replace(",", ""));
    }

    private String getDateFormat(Date date) {
        return DateUtils.format(date);
    }

    public void setStrings(WfmStrings strings) {
        this.strings = strings;
    }

    public void setProjectStrings(ProjectStrings projectStrings) {
        this.projectStrings = projectStrings;
    }

    public void setResult(ProjectViewItem result) {
        this.result = result;
    }
}
