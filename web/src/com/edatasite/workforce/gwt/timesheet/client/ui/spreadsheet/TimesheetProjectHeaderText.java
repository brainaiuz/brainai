package com.edatasite.workforce.gwt.timesheet.client.ui.spreadsheet;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.ui.HTML;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 22.11.2008
 * Time: 16:45:18
 * To change this template use File | Settings | File Templates.
 */
public class TimesheetProjectHeaderText extends HTML {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private int weeklySum;
    private int monthlySum;
    private int projectId;
    private String projectName;
    private String projectNameTitle;
    private String clientName;
    private String clientNameTitle;

    public void setWeeklyReportSum(int weeklySum) {
        this.weeklySum = weeklySum;
    }

    public int getWeeklyReportSum() {
        return weeklySum;
    }

    public void setMonthlyReportSum(int monthlySum) {
        this.monthlySum = monthlySum;
    }

    public int getMonthlyReportSum() {
        return monthlySum;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectNameTitle() {
        return projectNameTitle;
    }

    public String getClientNameTitle() {
        return clientNameTitle;
    }

    public void setProjectNameTitle(String projectNameTitle) {
        this.projectNameTitle = projectNameTitle;
    }

    public void setClientNameTitle(String clientNameTitle) {
        this.clientNameTitle = clientNameTitle;
    }

    public void drawHeaderText() {
        StringBuilder sb = new StringBuilder();

        if (projectName != null) {
            String titleProject = Property.get(Constants.PROJECT, wfmStrings.project()) + ":";
            sb.append("<table><tr><td style='width:205px;' title='").append(projectNameTitle).append("'><b>").append(titleProject).append("</b> ").append(projectName).append("</td>");
        }

        if (clientName != null) {
            sb.append("<td style='width:245px;' title='").append(clientNameTitle).append("'><b>").append(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())).append(":</b> ").append(clientName).append("</td>");
        }

        sb.append(weeklySum != 0 ? "<td style='width:120px;'><b>" + wfmStrings.weekly() + ":</b> " + Utils.formatMinutes(weeklySum) + "</td>" : "<td style='width:120px;'><b>" + wfmStrings.weekly() + ": </b>00:00</td>");

        sb.append(monthlySum != 0 ? "<td style='width:100px;'><b>" + wfmStrings.monthly() + ":</b> " + Utils.formatMinutes(monthlySum) + "</td>" : "<td style='width:100px;'><b>" + wfmStrings.monthly() + ": </b>00:00</td>");

        sb.append("</tr></table>");
        setHTML(sb.toString());
    }

    public void drawNonHeaderText(String text) {

        String sb = "<table><tr>" +
                (weeklySum != 0 ? "<td style='width:120px;'><b>" + wfmStrings.weekly() + ": </b>" + Utils.formatMinutes(weeklySum) + "</td>" : "<td style='width:120px;'><b>" + wfmStrings.weekly() + ": </b>00:00</td>") +
                (monthlySum != 0 ? "<td style='width:120px;'><b>" + wfmStrings.monthly() + ": </b>" + Utils.formatMinutes(monthlySum) + "</td>" : "<td style='width:120px;'><b>" + wfmStrings.monthly() + ": </b>00:00</td>") +
                "</tr></table>";
        setHTML(sb);
    }

    public void setHTML() {
        setHTML(Utils.formatMinutes(weeklySum));
    }
}
