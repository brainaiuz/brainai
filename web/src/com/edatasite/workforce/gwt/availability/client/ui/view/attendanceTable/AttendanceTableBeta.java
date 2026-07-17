package com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable;

import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.AttendanceTableHtmlTags.TD_END;

/**
 * @author: Dilshod
 * @uses : Attendance tables
 */
public class AttendanceTableBeta extends Composite {

    private final HTML table;
    private int monthMaxDay = 0;
    private int currentDay;
    private String monthName = "";
    private AttendanceTableDataBeta tableData;
    private EmployeeAttendanceReport employeeAttendance;
    private HashMap<Integer, HashMap<String, LREmployee>> leaveTotalData = new HashMap<>();
    private HashMap<Integer, HashMap<String, Integer>> leaveTotalDataByEmployee = new HashMap<>();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private String monthYear;
    private Date date;
    private String orderBy;
    private boolean isFromTerminalReport = false;
    private Date startDate;
    private Date endDate;

    protected static WfmStrings wfmStrings = WfmStrings.App.get();

    public AttendanceTableBeta(Date date,boolean isFromTerminalReport) {
        table = new HTML();
        initWidget(table);
        initEmplpoyeeTypesPopup();
        this.date = date;
        this.isFromTerminalReport = isFromTerminalReport;

        ScriptInjector.fromString("document.addEventListener('click', (e) => {\n" +
                "    if (e.target.classList.contains(\"attendance-report-collapsible-button\")) {\n" +
                "        const targetTable = e.target.parentNode.parentNode.parentNode;\n" +
                "        targetTable.classList.toggle(\"active-table\");\n" +
                "    }\n" +
                "});").setWindow(ScriptInjector.TOP_WINDOW).inject();
    }

    public void setEmployeeAttendance(EmployeeAttendanceReport employeeAttendance) {
        this.employeeAttendance = employeeAttendance;
    }

    public EmployeeAttendanceReport getEmployeeAttendance() {
        return employeeAttendance;
    }

    public void generateTable() {
        tableData = new AttendanceTableDataBeta(getMonthMaxDay(), monthYear, date, employeeAttendance.getReasons(), employeeAttendance.isHolidayIncluded());
    }

    public void generateTerminalTable(Date startDate, Date endDate) {
        tableData = new AttendanceTableDataBeta(getMonthMaxDay(), monthYear, date, startDate, endDate, employeeAttendance.getReasons(), employeeAttendance.isHolidayIncluded());
    }

    public void drawTableBodyHtml(boolean isFromTerminalReport,Date startDate, Date endDate) {
        this.isFromTerminalReport = isFromTerminalReport;
        table.setHTML(getTableBodyHtmlText(startDate, endDate));
    }

    private String setEmployeeReport() {
        StringBuilder html = new StringBuilder();
        int plTotal = 0;
        int workDayTotal = 0;
        int monthPlTotal = 0;
        int monthActualTotal = 0;

        // Удаляем старые метки, если они были
        this.removeStyleName("report-with-data");
        this.removeStyleName("report-empty-stub");

        if (orderBy != null) {
            Map<String, ArrayList<EmployeeReport>> employeeData = employeeAttendance.getEmplReports();
            if (employeeData != null && employeeData.size() > 0) {

                // МЕТКА: Данные есть (группировка по департаментам)
                this.addStyleName("report-with-data");

                for (String department : employeeData.keySet()) {
                    boolean isFirst = true;
                    html.append("<tbody class='attendance-report-collapsible-table'>");
                    for (EmployeeReport anEmployeeData : employeeData.get(department)) {
                        html.append("<tr>");
                        if (isFirst) {
                            ActionButton button = new ActionButton("");
                            button.setStyleName("attendance-report-collapsible-button");
                            html.append("<td style='width:150px; vertical-align: top;' class='attendance-report-collapsible-table-grouped-column' rowspan='" + (employeeData.get(department).size() + 1) + "'>")
                                    .append(department).append(button.asWidget()).append(TD_END);
                        }
                        if (isFromTerminalReport) {
                            html.append(tableData.getTerminalEmployeesAttendanceReport(anEmployeeData, employeeAttendance.getLeaveTypes(), leaveTotalData, leaveTotalDataByEmployee,employeeAttendance.getFingerprintTimeDtoMap(),startDate,endDate));
                        } else {
                            html.append(tableData.getEmployeesAttendanceReport(anEmployeeData, employeeAttendance.getLeaveTypes(), leaveTotalData, leaveTotalDataByEmployee));
                        }
                        html.append("</tr>");
                        isFirst = false;
//                        long plannedHour = anEmployeeData.getPlannedHours();
//                        int totalHour = anEmployeeData.getInhour();
//
//                        String plannedHours = (Math.abs(plannedHour) / 60 > 9 ? (Math.abs(plannedHour) / 60) + "" : "0" + (Math.abs(plannedHour) / 60));
//                        String actualHours = (Math.abs(totalHour) / 60 > 9 ? (Math.abs(totalHour) / 60) + "" : "0" + (Math.abs(totalHour) / 60));
//
//                        plTotal += anEmployeeData.getPlannedDays();
//                        workDayTotal += anEmployeeData.getWorkedDays();
//                        monthPlTotal += Integer.valueOf(plannedHours);
//                        monthActualTotal += Integer.valueOf(actualHours);

                    }

//                    html.append("<td class=\"grandTotalTitle\">");
//                    html.append(wfmStrings.grandTotal());
//                    html.append("</td>");

//                    for (int i = 0; i < tableData.getCurrentMonth(); i++) {
//                        html.append("<td></td>");
//                    }

//                    html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(plTotal < 0 ? "-" : "").append(plTotal).append(TD_END);
//
//                    html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(workDayTotal < 0 ? "-" : "").append(workDayTotal).append(TD_END);
//                    html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(monthPlTotal).append(TD_END);
//                    html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(monthActualTotal).append(TD_END);

                    html.append("</tbody>");
//                    html.append("<tr ><td colspan=\"").append(monthMaxDay + orderBy != null ? 3 : 2).append("\" style=\"border-left: none; border-right: none;\"></td></tr>");

//                    plTotal = 0;
//                    workDayTotal = 0;
//                    monthPlTotal = 0;
//                    monthActualTotal = 0;
                }
            } else {
                // no data - header remains visible
            }
        } else {
            EmployeeReport[] employeeData = employeeAttendance.getEmployeeReports();
            if (employeeData != null && employeeData.length > 0) {

                // МЕТКА: Данные есть (обычный список)
                this.addStyleName("report-with-data");

                for (EmployeeReport anEmployeeData : employeeData) {
                    html.append("<tr>");
                    if (isFromTerminalReport) {
                        html.append(tableData.getTerminalEmployeesAttendanceReport(anEmployeeData, employeeAttendance.getLeaveTypes(), leaveTotalData, leaveTotalDataByEmployee,employeeAttendance.getFingerprintTimeDtoMap(),startDate,endDate));
                    } else {
                        html.append(tableData.getEmployeesAttendanceReport(anEmployeeData, employeeAttendance.getLeaveTypes(), leaveTotalData, leaveTotalDataByEmployee));
                    }
//                    long plannedHour = anEmployeeData.getPlannedHours();
//                    int totalHour = anEmployeeData.getInhour();
//
//                    String plannedHours = (Math.abs(plannedHour) / 60 > 9 ? (Math.abs(plannedHour) / 60) + "" : "0" + (Math.abs(plannedHour) / 60));
//                    String actualHours = (Math.abs(totalHour) / 60 > 9 ? (Math.abs(totalHour) / 60) + "" : "0" + (Math.abs(totalHour) / 60));
//
//                    plTotal += anEmployeeData.getPlannedDays();
//                    workDayTotal += anEmployeeData.getWorkedDays();
//                    monthPlTotal += Integer.valueOf(plannedHours);
//                    monthActualTotal += Integer.valueOf(actualHours);

                    html.append("</tr>");
                }
//                html.append("<td class=\"grandTotalTitle\">");
//                html.append(wfmStrings.grandTotal());
//                html.append("</td>");
//                for (int i = 0; i < tableData.getCurrentMonth(); i++) {
//                    html.append("<td></td>");
//                }
//                html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(plTotal < 0 ? "-" : "").append(plTotal).append(TD_END);
//                html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(workDayTotal < 0 ? "-" : "").append(workDayTotal).append(TD_END);
//                html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(monthPlTotal).append(TD_END);
//                html.append(TD_BEGIN.replaceAll(CLASS_NAME, IN_HOUR_OVERTIME_STYLE)).append(monthActualTotal).append(TD_END);
//                html.append("<tr ><td colspan=\"").append(monthMaxDay + orderBy != null ? 3 : 2).append("\" style=\"border-left: none; border-right: none;\"></td></tr>");

            } else {
                // МЕТКА: Тот самый пустой блок
//                this.addStyleName("report-empty-stub");
                html.append("<div style=\"width: 100%; overflow: hidden; border: 0px; margin: 0px; padding: 0px;\"><div class=\"emptyTable-content\"><div class=\"gwt-HTML\">" + wfmStrings.noResultsFoundForTheProvidedSearchCriteria() + "</div></div></div>");
            }
        }
        return html.toString();
    }


    private int getMonthMaxDay() {
        return monthMaxDay;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public void setMonthMaxDay(int monthMaxDay) {
        this.monthMaxDay = monthMaxDay;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    private String getTableBodyHtmlText(Date startDate, Date endDate) {
        StringBuilder table = new StringBuilder();
        this.startDate = startDate;
        this.endDate = endDate;
        String height = "auto";
        table.append("<table id='attendanceTableBeta' class='attRepTbl trmnlAtt-tbl' height='").append(height).append("' cellspacing='0' cellpadding='0'>");
        table.append("<tbody id='attendanceReportScrollTableTbody'>");
        leaveTotalData = new HashMap<>();
        leaveTotalDataByEmployee = new HashMap<>();
        table.append(setEmployeeReport());
        table.append("</tbody>");
//        table.append("<tbody id=\"grandTableBody\">");
//        table.append(getUnavilableTotalTable());
//        table.append("</tbody>");
//        table.append("<tbody id=\"totalLeaveType\">");
//        table.append(getLeaveTypesTable(employeeAttendance));
//        table.append("</tbody>");
        table.append("</table>");

        return table.toString();
    }

    public String getTableHeaderHtml(String orderBy) {
        this.orderBy = orderBy;
        StringBuilder table = new StringBuilder();
        String height = "auto";
        table.append("<table id='attendanceTableBeta' class='attRepTbl trmnlAtt-tbl' height='").append(height).append("' cellspacing='0' cellpadding='0'>");
        table.append("<thead><tr>");
        if (orderBy != null) {
            table.append("<th class='stickerCell isOrderBy' style='width:150px;text-align:left'> ").append(orderBy).append("</th>");
        }
        table.append("          <th class='firstColCell stickerCell th-y--lg'><span class=\"frame_affix_top\">").append(wfmStrings.employee()).append("</span></th>").append(tableData.getEmployeeTitle());
        table.append("          <th class='inhour-overtime stickerCell th-x--total'><span class=\"frame_affix_top\">" + wfmStrings.normalDays() + "</span></th>");
//        table.append("          <th class='inhour-overtime stickerCell' style='width:50px'><span class=\"frame_affix_top\">" + wfmStrings.workedDaysBasedOnTheNorm() + "</span></th>");
//        table.append("          <th class='inhour-overtime stickerCell' style='width:50px'><span class=\"frame_affix_top\">" + wfmStrings.overtimeWorkedDays() + "</span></th>");
        table.append("          <th class='inhour-overtime stickerCell th-x--total'><span class=\"frame_affix_top\">" + wfmStrings.actualDays() + "</span></th>");
        table.append("          <th class='inhour-overtime stickerCell th-x--total'><span class=\"frame_affix_top\">" + wfmStrings.monthlyPlanned() + "</span></th>");
        table.append("          <th class='inhour-overtime stickerCell th-x--total'><span class=\"frame_affix_top\">" + wfmStrings.monthlyActual() + "</span></th>");
        table.append("  </thead>");
        table.append("</table>");

        return table.toString();
    }

    public String getTerminalTableHeaderHtml(String orderBy,Date startDate, Date endDate) {
        this.orderBy = orderBy;
        StringBuilder table = new StringBuilder();
        String height = "auto";
        table.append("<table id='attendanceTableBeta' class='attRepTbl trmnlAtt-tbl' height='").append(height).append("' cellspacing='' cellpadding=''>");
        table.append("<thead>");
        table.append("<tr>");
        if (orderBy != null) {
            table.append("<th rowspan='2' class='stickerCell isOrderBy' style='width:150px;text-align:left'> ").append(orderBy).append("</th>");
        }
        table.append("          <th rowspan='2' class='firstColCell stickerCell th-y--lg'><span class=\"frame_affix_top\">").append(wfmStrings.employee()).append("</span></th>");
        table.append(tableData.getTerminalEmployeeTitle(startDate,endDate));
        table.append("          <th rowspan='2' style=\"border-left: 1px solid #8c8282;\" class='inhour-overtime stickerCell th-x--total'><span class=\"frame_affix_top\">" + wfmStrings.lateRate() + "</span></th>");
        table.append("          <th rowspan='2' class='inhour-overtime stickerCell th-x--total'><span class=\"frame_affix_top\">" + wfmStrings.earlyRate() + "</span></th>");
        table.append("</tr>");

        table.append(tableData.getSubTitleRow(startDate,endDate));
        table.append("  </thead>");
        table.append("</table>");

        return table.toString();
    }

    private String getUnavilableTotalTable() {
        String boldStyle = " style=\"font-weight:bold;\"";
        StringBuilder lTable = new StringBuilder();
        int total = 0;
        lTable.append("<tr class=\"grandTotal\">");
        if (orderBy != null) {
            lTable.append("<td></td>");
        }
        lTable.append("<td class=\"grandTotalTitle\">");
        lTable.append(wfmStrings.unavailableEmployees());
        lTable.append("</td>");

        for (int i = 1; i <= monthMaxDay; i++) {
            int temp = 0;
            for (String key : employeeAttendance.getLeaveTypes().keySet()) {
                temp += getTotalByType(key, i);
            }
            lTable.append("<td").append(temp > 0 ? boldStyle : "").append(">");
            total += temp;
            lTable.append(temp);
            lTable.append("</td>");
        }
        lTable.append("<td class=\"grandTotalTotal\">");
        lTable.append(total);
        lTable.append("</td>");
        lTable.append("</tr>");
        //Padding at the bottom of the table
        lTable.append("<tr ><td colspan=\"").append(monthMaxDay + orderBy != null ? 3 : 2).append("\" style=\"border-left: none; border-right: none;\"></td></tr>");
        return lTable.toString();
    }

    private String getLeaveTypesTable(EmployeeAttendanceReport empAttend) {
        StringBuilder lTable = new StringBuilder();
        SortedSet<String> sortedset = new TreeSet<>(empAttend.getLeaveTypes().keySet());
        for (String key : sortedset) {
            StringBuilder cell = new StringBuilder();
            cell.append("<tr>");
            if (orderBy != null) {
                cell.append("<td></td>");
            }
            cell.append(getLeaveTypeCell(empAttend.getLeaveTypes().get(key)));
            int total = 0;
            for (int i = 1; i <= monthMaxDay; i++) {
                int temp = getTotalByType(key, i);
                total += temp;
                if (temp > 0) {
                    cell.append("<td class=\"emppopUp\" style=\"font-weight: bold;\" onclick=\"window.showLeaveTypeEmployees('").append(key).append("', '").append(i).append("');\">").append(temp).append("</td>");
                } else {
                    cell.append("<td>").append(temp).append("</td>");
                }
            }
            cell.append("<td style=\"font-weight: bold;\">").append(total).append("</td>");
            cell.append("</tr>");
            if (total != 0) {
                lTable.append(cell);
            }
        }
        return lTable.toString();
    }

    private int getTotalByType(String key, int day) {
        HashMap<String, LREmployee> data = leaveTotalData.get(day);
        if (data != null && data.get(key) != null) {
            return data.get(key).getTotal();
        }
        return 0;
    }

    private int getTotalByEmployeeId(String key, int empId) {
        HashMap<String, Integer> data = leaveTotalDataByEmployee.get(empId);
        if (data != null && data.get(key) != null) {
            return data.get(key);
        }
        return 0;
    }

    private SortedSet<EmployeeReport> getLeaveTypeEmployeesByCodeAndDay(String key, int day) {
        HashMap<String, LREmployee> data = leaveTotalData.get(day);
        if (data != null && data.get(key) != null) {
            return data.get(key).getEmps();
        }
        return null;
    }

    private String getLeaveTypeCell(ReasonItem lt) {
        String color = lt != null && lt.getHexColor() != null ? lt.getHexColor() : "ffffff";
        String invertColor = Utils.invertColor(color);
        String shortName = (lt != null && lt.getShortName() != null) ? lt.getShortName() : "(L)";
        String name = (lt != null && lt.getName() != null) ? shortName.concat(" - ").concat(lt.getName()) : shortName;
        return "<td><span style=\"border-left: 2px solid #" + color + "\">" + name + "</span></td>";
    }

    public void removeAttendanceTable() {
        Element elem = DOM.getElementById("attendanceTableBeta");
        if (elem != null) {
            elem.removeFromParent();
        }
    }

    private native void initEmplpoyeeTypesPopup() /*-{
        var that = this;
        $wnd.showLeaveTypeEmployees = $entry(function (type, date__) {
            that.@com.edatasite.workforce.gwt.availability.client.ui.view.attendanceTable.AttendanceTableBeta::showLeaveTypeEmployees(Ljava/lang/String;Ljava/lang/String;)(type, date__);
        });
    }-*/;

    private void showLeaveTypeEmployees(final String type, final String date__) {
        Set<EmployeeReport> emps = getLeaveTypeEmployeesByCodeAndDay(type, Integer.parseInt(date__));
        if (emps != null && emps.size() > 0) {
            final KpiModal box = new KpiModal();
            ReasonItem ri = employeeAttendance.getLeaveTypes().get(type);
            String title = ri != null ? ri.getName() != null ? ri.getName() : "" : "";
            box.setTitle(title);
            box.setWidth(400);
            VerticalPanel vp = new VerticalPanel();
            vp.setWidth("100%");
            for (EmployeeReport emp : emps) {
                Anchor a = new Anchor();
                a.setText(emp.getName());
                a.setHref("#leaverequest/" + emp.getIdsOfLeaveRequests());
                a.addClickHandler(clickEvent -> box.close());
                vp.add(a);
            }
            box.add(vp);
            box.open();
        }
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setDate(Date date) {
        this.date = date;
        tableData.setDate(date);
    }

    public Date setTerminalDate(Date date, Date startDate, Date endDate) {
        this.date = date;
        tableData.setDate(date);
        tableData.setStartDate(startDate);
        tableData.setEndDate(endDate);
        return this.date;

    }

    public static class LREmployee {
        public LREmployee() {

        }

        private int total = 0;
        private SortedSet<EmployeeReport> emps = new TreeSet<>(Comparator.comparing(EmployeeReport::getId));

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public SortedSet<EmployeeReport> getEmps() {
            return emps;
        }

        public void setEmps(SortedSet<EmployeeReport> emps) {
            this.emps = emps;
        }
    }

}
