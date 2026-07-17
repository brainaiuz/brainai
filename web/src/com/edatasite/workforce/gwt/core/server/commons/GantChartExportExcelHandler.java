package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.AbstractComparator;
import com.edatasite.workforce.gwt.core.server.utils.ComparatorFactory;
import com.edatasite.workforce.gwt.ganttchart.client.GanttChartService;
import com.edatasite.workforce.gwt.ganttchart.client.rpc.GanttItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.WorkstreamAssigneeItem;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceImpl;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ${Dilsh0d}
 * Date: 21.10.2009
 * Time: 20:51:06
 * To change this template use File | Settings | File Templates.
 */
public class GantChartExportExcelHandler implements HttpRequestHandler {

    private final XSSFColor defaultColor = new XSSFColor(new byte[]{(byte) 192, (byte) 192, (byte) 192});
    private final XSSFColor workstreamColor = new XSSFColor(new byte[]{(byte) 0, (byte) 0, (byte) 0});
    private final XSSFColor finishedColor = new XSSFColor(new byte[]{(byte) 51, (byte) 255, (byte) 0});
    private final XSSFColor plannedColor = new XSSFColor(new byte[]{(byte) 0, (byte) 102, (byte) 255});
    private final XSSFColor actualColor = new XSSFColor(new byte[]{(byte) 255, (byte) 204, (byte) 0});
    private final String padding = "----";
    @Autowired
    private GanttChartService ganttChartService;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private WorkStreamManager workStreamManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    TimeSheetManager timeSheetManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;

    Font font8 = null;
    Font font9 = null;
    Font font11 = null;
    CellStyle styleblue;
    CellStyle styleyellow;
    CellStyle stylegreen = null;
    CellStyle styleblack = null;
    CellStyle styleWS = null;
    CellStyle stylewhite = null;
    CellStyle stylewhitet = null;
    CellStyle stylewhiteb = null;
    CellStyle styletitle = null;
    CellStyle styletitler = null;
    CellStyle styleleft = null;

    protected String excel2007MimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int level = 0;
        String filename = "GanttChartReport";
        response.setHeader("content-disposition", "attachment; filename=" + filename + ".xlsx");
        response.setContentType(excel2007MimeType);

        Integer projectId = null, periodDays = 0, employeeID = null;
        String sessionId = "", columnNames = "", showActualStr = "false";
        boolean showActual = false;
        StringBuilder subWSSpace = new StringBuilder();
        Date from = null, to = null;
        String sortBy = "startDate asc";
        if (request.getParameter("projectID") != null) {
            projectId = Integer.valueOf(request.getParameter("projectID"));
        }

        if (request.getParameter("sessionId") != null) {
            sessionId = request.getParameter("sessionId");
            ServerSecurityContext.getInstance().setSessionId(sessionId);
        }
        EdsUser user = projectManager.getUser();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        if (request.getParameter("from") != null) {
            try {
                from = dateFormat.parse(request.getParameter("from"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (request.getParameter("to") != null) {
            try {
                to = dateFormat.parse(request.getParameter("to"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (request.getParameter("showActual") != null) {
            showActualStr = request.getParameter("showActual");
            if ("true".equals(showActualStr)) {
                showActual = true;
            }
        }
        Calendar fromCal = new GregorianCalendar();
        fromCal.setTime(from);
        fromCal.set(Calendar.HOUR, 0);
        fromCal.set(Calendar.MINUTE, 0);
        fromCal.set(Calendar.SECOND, 0);
        fromCal.set(Calendar.MILLISECOND, 0);

        Calendar toCal = new GregorianCalendar();
        toCal.setTime(to);
        toCal.set(Calendar.HOUR, 23);
        toCal.set(Calendar.MINUTE, 59);
        toCal.set(Calendar.SECOND, 59);
        toCal.set(Calendar.MILLISECOND, 999);
        if (request.getParameter("sortBy") != null) {
            sortBy = request.getParameter("sortBy");
        }
        if (request.getParameter("employeeID") != null && !"".equals(request.getParameter("employeeID"))) {
            employeeID = Integer.valueOf(request.getParameter("employeeID"));
        }
        EdsCompany company = user.getCompany();

        GanttItem projectTasks = ganttChartService.getProjectDetailsForGanttChart(projectId);
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        if (userSettings != null && userSettings.getGanttChartColumns() != null && !"".equals(userSettings.getGanttChartColumns())) {
            columnNames = userSettings.getGanttChartColumns();
        }
        periodDays = getPeriodDays(from, to);

        String[] columns = columnNames.split(",");
        int columnsCount = columnNames != null && !columnNames.isEmpty() ? columns.length + 1 : 1;

        SXSSFWorkbook workBook = new SXSSFWorkbook();
        Sheet sheet = workBook.createSheet(filename);
        sheet.createFreezePane(columnsCount, 9);
        sheet.setDefaultRowHeight((short) 30);
        sheet.setDefaultColumnWidth(13);
        createXslStyle(workBook);

        // Report Name
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("  " + company.getName() + " Gantt Chart Report ");
        cell.setCellStyle(getNewStyle(16, (short) 8, (short) 22, workBook));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnsCount - 1));

        /*Excel Title  names */
        row = sheet.createRow(8);
        /* task name */
        cell = row.createCell(0);
        cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.name));
        sheet.setColumnWidth(0, (25 * 256));
        cell.setCellStyle(styletitle);

        int index = 1;
        for (String column : columns) {
            column = column.replace(" ", "");
            if (TaskListItem.START_DATE.equals(column)) {
                // start date
                cell = row.createCell(index);
                cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.startDateField));
                sheet.setColumnWidth(index, 12 * 256);
                cell.setCellStyle(styletitle);
                index++;
            } else if (TaskListItem.END_DATE.equals(column)) {
                // end date
                cell = row.createCell(index);
                cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.dueDate));
                sheet.setColumnWidth(index, (12 * 256));
                cell.setCellStyle(styletitle);
                index++;
            } else if (TaskListItem.COMPLETE.equals(column)) {
                // % completed
                cell = row.createCell(index);
                cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.percent));
                sheet.setColumnWidth(index, (6 * 256));
                cell.setCellStyle(styletitler);
                index++;
            } else if (TaskListItem.ASSIGNED_TO.equals(column)) {
                // % completed
                cell = row.createCell(index);
                cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.assignedTo));
                sheet.setColumnWidth(index, (20 * 256));
                cell.setCellStyle(styletitle);
                index++;
            } else if (TaskListItem.OVERALL_STATUS_NAME.equals(column)) {
                // merged overallStatus column
                cell = row.createCell(index);
                cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.overAllStatus));
                sheet.setColumnWidth(index, (13 * 256));
                cell.setCellStyle(styletitle);
                index++;
            } else if (TaskListItem.PRIORITY_NAME.equals(column)) {
                // merged priority column
                cell = row.createCell(index);
                cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.priority));
                sheet.setColumnWidth(index, (12 * 256));
                cell.setCellStyle(styletitle);
                index++;
            } else if (TaskListItem.ESTIMATED.equals(column)) {
                // merged estimatedTime column
                cell = row.createCell(index);
                cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.estimatedTime));
                sheet.setColumnWidth(index, (6 * 256));
                cell.setCellStyle(styletitler);
                index++;
            } else if (TaskListItem.ACTUAL_TIME.equals(column)) {
                // merged actualTime column
                cell = row.createCell(index);
                cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.actualTimeSpent));
                sheet.setColumnWidth(index, (6 * 256));
                cell.setCellStyle(styletitler);
                index++;
            } else if (TaskListItem.ACTUAL_START_DATE.equals(column)) {
                // merged actualStartDate column
                cell = row.createCell(index);
                cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.actualStartDate));
                sheet.setColumnWidth(index, (12 * 256));
                cell.setCellStyle(styletitle);
                index++;
            } else if (TaskListItem.ACTUAL_END_DATE.equals(column)) {
                // merged actualEndDate column
                cell = row.createCell(index);
                cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.actualEndDate));
                sheet.setColumnWidth(index, (12 * 256));
                cell.setCellStyle(styletitle);
                index++;
            } else if (TaskListItem.BILLABLE.equals(column)) {
                // merged billable column
                cell = row.createCell(index);
                cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.billable));
                sheet.setColumnWidth(index, (6 * 256));
                cell.setCellStyle(styletitler);
                index++;
            }
        }

        font11.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);
        /* date period */

        for (int i = columnsCount; i < periodDays + columnsCount; i++) {
            cell = row.createCell(i);
            cell.setCellValue(ServerUtils.shortDateFormat(calendar.getTime(), company));
            sheet.setColumnWidth(i, (3 * 256));
            cell.setCellStyle(styletitler);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        /* set task list */
        if (projectTasks != null) {
            List<EdsWorkStream> wss = workStreamManager.findOrphanWorkstreams(projectId, fromCal.getTime(), toCal.getTime(), sortBy);
            if (wss != null && !wss.isEmpty()) {
                if (sortBy.contains("Date")) {
                    wss.sort(getComparatorFactoryForWorkstreamDates().createComparator(sortBy.contains("asc") ? Constants.ASC : Constants.DESC));
                } else {
                    wss.sort(getComparatorFactoryForWorkstreamOrder().createComparator(sortBy.contains("asc") ? Constants.ASC : Constants.DESC));
                }
                setListWorkStream(wss, level, fromCal.getTime(), toCal.getTime(), employeeID, sortBy, periodDays, user, company, cell, row, sheet, columnNames, showActual);
                projectManager.flushAndClear();
            }

            List<EdsTask> orphanTasks = taskManager.findOrphanTasksForGanttChart(projectId, employeeID, fromCal.getTime(), toCal.getTime(), sortBy);
            if (orphanTasks != null && !orphanTasks.isEmpty()) {
                if ("objectID asc".equals(sortBy)) {
                    orphanTasks.sort(getComparatorFactoryForTaskID().createComparator(Constants.ASC));
                } else if ("objectID desc".equals(sortBy)) {
                    orphanTasks.sort(getComparatorFactoryForTaskID().createComparator(Constants.DESC));
                } else if ("startDate asc".equals(sortBy)) {
                    orphanTasks.sort(getComparatorFactoryForTaskDates().createComparator(Constants.ASC));
                } else if ("startDate desc".equals(sortBy)) {
                    orphanTasks.sort(getComparatorFactoryForTaskDates().createComparator(Constants.DESC));
                }
                setListTask(orphanTasks, level, from, to, employeeID, periodDays, user, company, row, sheet, cell, columnNames, showActual);
                projectManager.flushAndClear();
            }
            row = sheet.createRow(4);
            cell = row.createCell(0);
            cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.projectName) + ":");

            cell = row.createCell(1);
            cell.setCellValue(projectTasks.getName());

            //Legends --------------------------------------------------------------------------------------------------
            // Workstream
            row = sheet.createRow(1);
            CellStyle workstreamStyle = getLegendsStyle(workBook, (short) 0);
            cell = row.createCell(columnsCount + 1);
            cell.setCellValue("");
            cell.setCellStyle(workstreamStyle);

            cell = row.createCell(columnsCount + 2);
            cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.workStream));
            cell.setCellStyle(stylewhite);
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 4));

            // Finished
            CellStyle finishedStyle = getLegendsStyle(workBook, (short) 3);
            cell = row.createCell(columnsCount + 7);
            cell.setCellValue("");
            cell.setCellStyle(finishedStyle);

            cell = row.createCell(columnsCount + 8);
            cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.finished));
            cell.setCellStyle(stylewhite);
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 4));
            //----------------------------------------------------------------------------------------------------------

            row = sheet.createRow(5);
            cell = row.createCell(0);
            cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.projectManager) + ":");

            cell = row.createCell(1);
            cell.setCellValue(projectTasks.getManager());

            row = sheet.createRow(6);
            cell = row.createCell(0);
            cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.startDateField) + ":");

            cell = row.createCell(1);
            cell.setCellValue(projectTasks.getStartDate() != null ? ServerUtils.shortDateFormat(projectTasks.getStartDate(), company) : "");

            //Legends --------------------------------------------------------------------------------------------------
            //Planned
            row = sheet.createRow(2);
            CellStyle plannedStyle = getLegendsStyle(workBook, (short) 4);
            cell = row.createCell(columnsCount + 1);
            cell.setCellValue("");
            cell.setCellStyle(plannedStyle);

            cell = row.createCell(columnsCount + 2);
            cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.planned));
            cell.setCellStyle(stylewhite);
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 4));

            // Actual
            CellStyle actualStyle = getLegendsStyle(workBook, (short) 5);
            cell = row.createCell(columnsCount + 7);
            cell.setCellValue("");
            cell.setCellStyle(actualStyle);

            cell = row.createCell(columnsCount + 8);
            cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.actual));
            cell.setCellStyle(stylewhite);
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), cell.getColumnIndex(), cell.getColumnIndex() + 4));
            //----------------------------------------------------------------------------------------------------------

            row = sheet.createRow(7);
            cell = row.createCell(0);
            cell.setCellValue(commonLocalizer.localize(PdfLocalizationName.endDateField) + ":");

            cell = row.createCell(1);
            cell.setCellValue(projectTasks.getEndDate() != null ? ServerUtils.shortDateFormat(projectTasks.getEndDate(), company, true) : "");
        }

        OutputStream stream = response.getOutputStream();
        if (workBook != null) {
            workBook.write(stream);
        }
        stream.flush();
        stream.close();
    }

    private ComparatorFactory<EdsTask> getComparatorFactoryForTaskID() {
        return sortOrder -> new AbstractComparator<EdsTask>() {
            public int compare(EdsTask o1, EdsTask o2) {
                return internalCompare(o1.getObjectID(), o2.getObjectID(), sortOrder);
            }
        };
    }

    private ComparatorFactory<EdsTask> getComparatorFactoryForTaskDates() {
        return sortOrder -> new AbstractComparator<EdsTask>() {
            public int compare(EdsTask o1, EdsTask o2) {
                return internalCompare(o1.getStartDate() != null ? o1.getStartDate() : new Date(), o2.getStartDate() != null ? o2.getStartDate() : new Date(), sortOrder);
            }
        };
    }

    private ComparatorFactory<EdsWorkStream> getComparatorFactoryForWorkstreamDates() {
        return sortOrder -> new AbstractComparator<EdsWorkStream>() {
            public int compare(EdsWorkStream o1, EdsWorkStream o2) {
                return internalCompare(o1.getStartDate() != null ? o1.getStartDate() : new Date(), o2.getStartDate() != null ? o2.getStartDate() : new Date(), sortOrder);
            }
        };
    }

    private ComparatorFactory<EdsWorkStream> getComparatorFactoryForWorkstreamOrder() {
        return sortOrder -> new AbstractComparator<EdsWorkStream>() {
            public int compare(EdsWorkStream o1, EdsWorkStream o2) {
                return internalCompare(o1.getObjectID(), o2.getObjectID(), sortOrder);
            }
        };
    }

    // Creating legends color style
    private CellStyle getLegendsStyle(SXSSFWorkbook workBook, short color) {
        CellStyle style = workBook.createCellStyle();
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(color);
        return style;
    }

    public int getPeriodDays(Date from, Date to) {
        try {
            Date start = new Date(from.getYear(), from.getMonth(), from.getDate(), 0, 0, 0);
            Date end = new Date(to.getYear(), to.getMonth(), to.getDate(), 0, 0, 0);
            long num = end.getTime() - start.getTime();
            return (int) (num / (1000 * 60 * 60 * 24));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public CellStyle getNewStyle(int fontSize, short fontcolor, short bgcolor, SXSSFWorkbook workBook) {
        CellStyle style = workBook.createCellStyle();
        Font font = workBook.createFont();

        style.setFont(font);
        style.setFillForegroundColor(bgcolor);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        font.setFontHeightInPoints((short) fontSize);
        font.setColor(fontcolor);

        return style;
    }

    private void setListTask(List<EdsTask> taskvoList, int wsLevel, Date from, Date to, Integer employeeID, Integer periodDays, EdsUser user, EdsCompany company, Row row, Sheet sheet, Cell cell, String columnNames, boolean showActual) {
        TimeZone userTimeZone = user.getUserTimezone();
        int timeZoneOffset = userTimeZone.getRawOffset() / 60000;
        for (EdsTask taskvo : taskvoList) {
            // merged columns
            row = addRow(sheet);
            // task name column merged
            cell = row.createCell(0);
            String taskName = getLeftPadding(wsLevel) + taskvo.getName();
            cell.setCellValue(taskName);
            System.out.println(taskName);
            cell.setCellStyle(styleleft);
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + 1, 0, 0));

            int index = 1;
            String[] columns = columnNames.split(",");
            Date startDate = (Date) taskvo.getStartDate().clone();
            startDate.setMinutes(startDate.getMinutes() + timeZoneOffset);

            Date dueDate = (Date) taskvo.getDueDate().clone();
            dueDate.setMinutes(dueDate.getMinutes() + timeZoneOffset);

            Double[] taskCostAndTime = timeSheetManager.getTaskCostAndTimeSpent(taskvo.getObjectID());
            for (String column : columns) {
                column = column.replace(" ", "");
                if (TaskListItem.START_DATE.equals(column)) {
                    // merged starddate column
                    cell = createMergedCells(sheet, row, row.getRowNum(), row.getRowNum() + 1, index, index, styleleft);
                    cell.setCellValue(startDate != null ? ServerUtils.shortDateFormat(startDate, company) : "");
                    index++;
                } else if (TaskListItem.END_DATE.equals(column)) {
                    // merged enddate column
                    cell = createMergedCells(sheet, row, row.getRowNum(), row.getRowNum() + 1, index, index, styleleft);
                    cell.setCellValue(dueDate != null ? ServerUtils.shortDateFormat(dueDate, company) : "");
                    index++;
                } else if (TaskListItem.COMPLETE.equals(column)) {
                    // merged completed column
                    cell = createMergedCells(sheet, row, row.getRowNum(), row.getRowNum() + 1, index, index, styleleft);
                    cell.setCellValue(taskvo.getPercent() != null ? taskvo.getPercent() + "%" : "");
                    index++;
                } else if (TaskListItem.ASSIGNED_TO.equals(column)) {
                    // merged assign column
                    cell = createMergedCells(sheet, row, row.getRowNum(), row.getRowNum() + 1, index, index, styleleft);
                    cell.setCellValue(getAssignToNames(taskvo.getUnDeletedAssignments()));
                    index++;
                } else if (TaskListItem.OVERALL_STATUS_NAME.equals(column)) {
                    // merged overallStatus column
                    cell = createMergedCells(sheet, row, row.getRowNum(), row.getRowNum() + 1, index, index, styleleft);
                    cell.setCellValue(taskvo.getStatus() != null ? (taskvo.getStatus().getCode() != null ? referenceWfmMessageSource.localize(taskvo.getStatus().getCode(), taskvo.getStatus().getName()) : taskvo.getStatus().getName()) : "");
                    index++;
                } else if (TaskListItem.PRIORITY_NAME.equals(column)) {
                    // merged priority column
                    cell = createMergedCells(sheet, row, row.getRowNum(), row.getRowNum() + 1, index, index, styleleft);
                    cell.setCellValue(taskvo.getPriority() != null ? (taskvo.getPriority().getCode() != null ? referenceWfmMessageSource.localize(taskvo.getPriority().getCode(), taskvo.getPriority().getName()) : taskvo.getPriority().getName()) : "");
                    index++;
                } else if (TaskListItem.ESTIMATED.equals(column)) {
                    // merged estimatedTime column
                    cell = createMergedCells(sheet, row, row.getRowNum(), row.getRowNum() + 1, index, index, styleleft);
                    cell.setCellValue(taskCostAndTime != null ? (taskCostAndTime[TaskServiceImpl.TASK_ESTIMATED_TIME_SPENT] != null ? ServerUtils.timeSpentToString(taskCostAndTime[TaskServiceImpl.TASK_ESTIMATED_TIME_SPENT].intValue()) : ServerUtils.timeSpentToString(0)) : ServerUtils.timeSpentToString(0));
                    index++;
                } else if (TaskListItem.ACTUAL_TIME.equals(column)) {
                    // merged actualTime column
                    cell = createMergedCells(sheet, row, row.getRowNum(), row.getRowNum() + 1, index, index, styleleft);
                    cell.setCellValue(taskCostAndTime != null ? (taskCostAndTime[TaskServiceImpl.TASK_ACTUAL_TIME_SPENT] != null ? ServerUtils.timeSpentToString(taskCostAndTime[TaskServiceImpl.TASK_ACTUAL_TIME_SPENT].intValue()) : ServerUtils.timeSpentToString(0)) : ServerUtils.timeSpentToString(0));
                    index++;
                } else if (TaskListItem.ACTUAL_START_DATE.equals(column)) {
                    String dateStr = "";
                    if (taskvo.getActualStartDate() != null) {
                        Date actualStartDate = (Date) taskvo.getActualStartDate().clone();
                        actualStartDate.setMinutes(actualStartDate.getMinutes() + timeZoneOffset);
                        dateStr = ServerUtils.shortDateFormat(actualStartDate, company);
                    }
                    // merged actualStartDate column
                    cell = createMergedCells(sheet, row, row.getRowNum(), row.getRowNum() + 1, index, index, styleleft);
                    cell.setCellValue(dateStr);
                    index++;
                } else if (TaskListItem.ACTUAL_END_DATE.equals(column)) {
                    String dateStr = "";
                    if (taskvo.getActualEndDate() != null) {
                        Date actualEndDate = (Date) taskvo.getActualEndDate().clone();
                        actualEndDate.setMinutes(actualEndDate.getMinutes() + timeZoneOffset);
                        dateStr = ServerUtils.shortDateFormat(actualEndDate, company);
                    }
                    // merged actualEndDate column
                    cell = createMergedCells(sheet, row, row.getRowNum(), row.getRowNum() + 1, index, index, styleleft);
                    cell.setCellValue(dateStr);
                    index++;
                } else if (TaskListItem.BILLABLE.equals(column)) {
                    // merged billable column
                    cell = createMergedCells(sheet, row, row.getRowNum(), row.getRowNum() + 1, index, index, styleleft);
                    cell.setCellValue(taskvo.getBillable() ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no));
                    index++;
                }
            }

            int columnsCount = columnNames != null && !columnNames.isEmpty() ? columns.length + 1 : 1;
            int start = -1, end = -1;

            if (startDate.after(from)) {
                start = getPeriodDays(from, startDate) + columnsCount;
            } else {
                start = columnsCount;
            }

            if (to.after(dueDate)) {
                end = getPeriodDays(from, dueDate) + columnsCount;
            } else {
                end = periodDays + columnsCount - 1;
            }

            int complete = -1;
            if (start != -1 && end != -1) {
                // have to object percent completed property
                if (taskvo.getPercent() != null && taskvo.getPercent() > 0) {
                    complete = getCompletedDay(start, end, taskvo.getPercent());
                }
                // begin
//				createMergedCells(sheet, row, columnsCount, start - 1, stylewhitet);
                // completed
                if (complete != -1) {
                    if (complete == 0 && taskvo.getPercent() != null && taskvo.getPercent() > 0) {
                        createMergedCells(sheet, row, start, start + complete, stylegreen);
                        complete++;
                    } else {
                        createMergedCells(sheet, row, start, start + complete - 1, stylegreen);
                    }
                    if (complete < getPeriodDays(startDate, dueDate) + 1) {
                        createMergedCells(sheet, row, start + complete, end, styleblue);
                    }
                } else {
                    complete = 0;
                    createMergedCells(sheet, row, start, end, styleblue);
                }
            }

            // new row
            row = addRow(sheet);
            for (int i = 0; i < columnsCount; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(styleleft);
            }

            start = -1;
            end = -1;
            if (taskvo.getActualStartDate() != null) {
                if (taskvo.getActualStartDate().after(from)) {
                    Date actualStart = (Date) taskvo.getActualStartDate().clone();
                    actualStart.setMinutes(actualStart.getMinutes() + timeZoneOffset);
                    start = getPeriodDays(from, actualStart) + columnsCount;
                } else {
                    start = columnsCount;
                }
            }

            if (taskvo.getActualEndDate() != null) {
                if (to.after(taskvo.getActualEndDate())) {
                    if (taskvo.getActualEndDate().after(from)) {
                        Date actualEnd = (Date) taskvo.getActualEndDate().clone();
                        actualEnd.setMinutes(actualEnd.getMinutes() + timeZoneOffset);
                        end = getPeriodDays(from, actualEnd) + columnsCount;
                    } else {
                        end = -1;
                    }
                } else {
                    end = periodDays + 5;
                }
            }
            // set color actual time
            if (showActual && start != -1 && end != -1) {
                createMergedCells(sheet, row, start, end, styleyellow);
            }
        }
    }

    private Cell createMergedCells(Sheet sheet, Row row, Integer start, Integer end, CellStyle style) {
        return createMergedCells(sheet, row, row.getRowNum(), row.getRowNum(), start, end, style);
    }

    private Cell createMergedCells(Sheet sheet, Row row, Integer startRow, Integer endRow, Integer startCol, Integer endCol, CellStyle style) {
        Cell cell = row.createCell(startCol);
        sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol, endCol));
        cell.setCellStyle(style);
        return cell;
    }

    private String getAssignToNames(Set<EdsEmployeeTask> assignedTo) {
        if (assignedTo != null && !assignedTo.isEmpty()) {
            StringBuilder names = new StringBuilder();
            for (EdsEmployeeTask employeeTask : assignedTo) {
                names.append(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getFullName()).append(",");
            }
            return names.deleteCharAt(names.lastIndexOf(",")).toString();
        }
        return "";
    }

    private int getCompletedDay(int start, int end, float persent) {
        return (int) ((end + 1 - start) * persent / 100);
    }


    private void setListWorkStream(List<EdsWorkStream> workStreams, int wsLevel, Date from, Date to, Integer employeeID, String sortBy, Integer periodDays, EdsUser user, EdsCompany company, Cell cell, Row row, Sheet sheet, String columnNames, boolean showActual) {
        for (EdsWorkStream workStream : workStreams) {
            createWorkStreamsChart(user, company, workStream, wsLevel, from, to, employeeID, periodDays, cell, row, sheet, columnNames, showActual, sortBy);
            wsLevel = 0;
            projectManager.flushAndClear();
        }
    }

    private void createWorkStreamsChart(EdsUser user, EdsCompany company, EdsWorkStream workstreamvo, int wsLevel, Date from, Date to, Integer employeeID, Integer periodDays, Cell cell, Row row, Sheet sheet, String columnNames, boolean showActual, String sortBy) {
        TimeZone userTimeZone = user.getUserTimezone();
        int timeZoneOffset = userTimeZone.getRawOffset() / 60000;

        Date startDate = (Date) workstreamvo.getStartDate().clone();
        startDate.setMinutes(startDate.getMinutes() + timeZoneOffset);

        Date dueDate = (Date) workstreamvo.getEndDate().clone();
        dueDate.setMinutes(dueDate.getMinutes() + timeZoneOffset);

        row = addRow(sheet);
        // task name column merged
        cell = row.createCell(0);
        String wsName = getLeftPadding(wsLevel) + workstreamvo.getName();
        cell.setCellStyle(styleWS);
        cell.setCellValue(wsName);
        System.out.println("WS name: " + wsName);

        int index = 1;
        String[] columns = columnNames.split(",");
        for (String column : columns) {
            column = column.replace(" ", "");
            if (TaskListItem.START_DATE.equals(column)) {
                // merged starddate column
                cell = row.createCell(index);
                cell.setCellValue(ServerUtils.shortDateFormat(startDate, company));
                cell.setCellStyle(styleWS);
                index++;
            } else if (TaskListItem.END_DATE.equals(column)) {
                // merged enddate column
                cell = row.createCell(index);
                cell.setCellValue(ServerUtils.shortDateFormat(dueDate, company));
                cell.setCellStyle(styleWS);
                index++;
            } else if (TaskListItem.COMPLETE.equals(column)) {
                // merged completed column
                cell = row.createCell(index);
                cell.setCellValue(workstreamvo.getPercent() != null ? workstreamvo.getPercent() + "%" : "");
                cell.setCellStyle(styleWS);
                index++;
            } else if (TaskListItem.ASSIGNED_TO.equals(column)) {
                // merged assign column
                cell = row.createCell(index);
                cell.setCellValue(getWorkstreamAssignees(workstreamvo.getAssigneeEmployee()));
                cell.setCellStyle(styleWS);
                index++;
            } else if (TaskListItem.OVERALL_STATUS_NAME.equals(column)) {
                // merged overallStatus column
                cell = row.createCell(index);
                cell.setCellValue("");
                cell.setCellStyle(styleWS);
                index++;
            } else if (TaskListItem.PRIORITY_NAME.equals(column)) {
                // merged priority column
                cell = row.createCell(index);
                cell.setCellValue("");
                cell.setCellStyle(styleWS);
                index++;
            } else if (TaskListItem.ESTIMATED.equals(column)) {
                // merged estimatedTime column
                cell = row.createCell(index);
                cell.setCellValue(ServerUtils.timeSpentToString(workstreamvo.getEstimatedTime() != null ? workstreamvo.getEstimatedTime() : 0));
                cell.setCellStyle(styleWS);
                index++;
            } else if (TaskListItem.ACTUAL_TIME.equals(column)) {
                // merged actualTime column
                cell = row.createCell(index);
                cell.setCellValue(ServerUtils.timeSpentToString(workstreamvo.getActualTime() != null ? workstreamvo.getActualTime() : 0));
                cell.setCellStyle(styleWS);
                index++;
            } else if (TaskListItem.ACTUAL_START_DATE.equals(column)) {
                // merged actualStartDate column
                cell = row.createCell(index);
                cell.setCellValue("");
                cell.setCellStyle(styleWS);
                index++;
            } else if (TaskListItem.ACTUAL_END_DATE.equals(column)) {
                // merged actualEndDate column
                cell = row.createCell(index);
                cell.setCellValue("");
                cell.setCellStyle(styleWS);
                index++;
            } else if (TaskListItem.BILLABLE.equals(column)) {
                // merged billable column
                cell = row.createCell(index);
                cell.setCellValue("");
                cell.setCellStyle(styleWS);
                index++;
            }
        }

        int columnsCount = columnNames != null && !columnNames.isEmpty() ? columns.length + 1 : 1;
        int start = -1, end = -1;
        if (workstreamvo.getStartDate().after(from)) {
            start = getPeriodDays(from, startDate) + columnsCount;
        } else {
            start = columnsCount;
        }
        if (start < 0) {
            start = 0;
        }

        if (to.after(workstreamvo.getEndDate())) {
            dueDate.setMinutes(dueDate.getMinutes() + timeZoneOffset + 1);
            end = getPeriodDays(from, dueDate) + columnsCount;
        } else {
            end = periodDays + columnsCount;
        }
        if (end < 0) {
            end = 0;
        }

        if (start != -1 && end != -1) {
            createMergedCells(sheet, row, start, end, styleblack);
        }

        EdsWorkStream edsWorkStream = workStreamManager.get(workstreamvo.getObjectID());
        ArrayList<EdsTask> tasks = new ArrayList<>();
        if (employeeID == null) {
            tasks.addAll(edsWorkStream.getTasks());
        } else {
            List<EdsTask> taskList = taskManager.getWorkStreamTasksByEmployee(workstreamvo.getObjectID(), employeeID, sortBy);
            if (taskList != null) {
                tasks.addAll(taskList);
            }
        }
        if (!tasks.isEmpty()) {
            wsLevel++;
            if ("objectID asc".equals(sortBy)) {
                tasks.sort(getComparatorFactoryForTaskID().createComparator(Constants.ASC));
            } else if ("objectID desc".equals(sortBy)) {
                tasks.sort(getComparatorFactoryForTaskID().createComparator(Constants.DESC));
            } else if ("startDate asc".equals(sortBy)) {
                tasks.sort(getComparatorFactoryForTaskDates().createComparator(Constants.ASC));
            } else if ("startDate desc".equals(sortBy)) {
                tasks.sort(getComparatorFactoryForTaskDates().createComparator(Constants.DESC));
            }
            setListTask(tasks, wsLevel, from, to, employeeID, periodDays, user, company, row, sheet, cell, columnNames, showActual);
            wsLevel--;
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setWorkstreamID(edsWorkStream.getObjectID());
        List<EdsWorkStream> subWorkStreams = workStreamManager.getOrderByWorkStream(filterParameter);//edsWorkStream.getSubWorkStreams();

        if (subWorkStreams != null && !subWorkStreams.isEmpty()) {
            wsLevel++;
            for (EdsWorkStream subworkstreamvo : subWorkStreams) {
                createWorkStreamsChart(user, company, subworkstreamvo, wsLevel, from, to, employeeID, periodDays, cell, row, sheet, columnNames, showActual, sortBy);
            }
            wsLevel--;
        }
    }

    private String getWorkstreamAssignees(List<WorkstreamAssigneeItem> assignedTo) {
        if (assignedTo != null && !assignedTo.isEmpty()) {
            StringBuilder names = new StringBuilder();
            for (WorkstreamAssigneeItem employeeTask : assignedTo) {
                names.append(employeeTask.getName()).append(",");
            }
            return names.deleteCharAt(names.lastIndexOf(",")).toString();
        }
        return "";
    }

    private String getLeftPadding(int level) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(padding.repeat(Math.max(0, level)));
        return buffer.toString();
    }

    public Row addRow(Sheet sheet) {
        return addRow(sheet, sheet.getLastRowNum() + 1);
    }

    public Row addRow(Sheet sheet, int number) {
        Row row = sheet.createRow(number);
        row.setHeight((short) 230);
        return row;
    }

    public void createXslStyle(SXSSFWorkbook workBook) {

        XSSFColor xssfColor = new XSSFColor();
        font8 = workBook.createFont();
        font8.setFontHeightInPoints((short) 9);
        font8.setColor((short) 8);

        font9 = workBook.createFont();
        font9.setFontHeightInPoints((short) 9);
        font9.setColor((short) 8);
        font9.setBoldweight(Font.BOLDWEIGHT_BOLD);

        font11 = workBook.createFont();
        font11.setFontHeightInPoints((short) 11);
        font11.setColor((short) 8);
        font11.setBoldweight(Font.BOLDWEIGHT_BOLD);

        styleblue = workBook.createCellStyle();
        styleblue.setFillForegroundColor((short)4);
        styleblue.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleblue.setFont(font8);

        stylegreen = workBook.createCellStyle();
        stylegreen.setFillForegroundColor((short)3);
        stylegreen.setFillPattern(CellStyle.SOLID_FOREGROUND);
        stylegreen.setFont(font8);

        styleblack = workBook.createCellStyle();
        styleblack.setFillForegroundColor((short) 0);
        styleblack.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleblack.setFont(font8);

        styleWS = workBook.createCellStyle();
        styleWS.setFillForegroundColor((short)9);
        styleWS.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleWS.setBorderTop(CellStyle.BORDER_THIN);
        styleWS.setTopBorderColor(defaultColor.getIndexed());
        styleWS.setBorderBottom(CellStyle.BORDER_THIN);
        styleWS.setBottomBorderColor(defaultColor.getIndexed());
        styleWS.setBorderRight(CellStyle.BORDER_THIN);
        styleWS.setRightBorderColor(defaultColor.getIndexed());
        styleWS.setFont(font9);

        styleyellow = workBook.createCellStyle();
        styleyellow.setFillForegroundColor((short)5);
        styleyellow.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleyellow.setFont(font8);

        stylewhite = workBook.createCellStyle();
        stylewhite.setFillForegroundColor((short) 9);
        stylewhite.setFillPattern(CellStyle.SOLID_FOREGROUND);
        stylewhite.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        stylewhite.setAlignment(CellStyle.ALIGN_CENTER);
        stylewhite.setFont(font8);

        stylewhitet = workBook.createCellStyle();
        stylewhitet.setFillForegroundColor((short) 9);
        stylewhitet.setFillPattern(CellStyle.SOLID_FOREGROUND);
        stylewhitet.setAlignment(CellStyle.ALIGN_CENTER);
        stylewhitet.setFont(font8);

        styleleft = workBook.createCellStyle();
        styleleft.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        styleleft.setFillForegroundColor((short) 9);
        styleleft.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styleleft.setBorderTop(CellStyle.BORDER_THIN);
        styleleft.setTopBorderColor(defaultColor.getIndexed());
        styleleft.setBorderBottom(CellStyle.BORDER_THIN);
        styleleft.setBottomBorderColor(defaultColor.getIndexed());
        styleleft.setBorderRight(CellStyle.BORDER_THIN);
        styleleft.setRightBorderColor(defaultColor.getIndexed());
        styleleft.setFont(font8);

        stylewhiteb = workBook.createCellStyle();
        stylewhiteb.setFillForegroundColor((short)9);
        stylewhiteb.setFillPattern(CellStyle.SOLID_FOREGROUND);
        stylewhiteb.setFont(font8);

        styletitler = workBook.createCellStyle();
        styletitler.setFillForegroundColor((short)9);
        styletitler.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styletitler.setAlignment(CellStyle.ALIGN_CENTER);
        styletitler.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        styletitler.setRotation((short) 90);
        styletitler.setBorderTop(CellStyle.BORDER_THIN);
        styletitler.setTopBorderColor(defaultColor.getIndexed());
        styletitler.setBorderLeft(CellStyle.BORDER_THIN);
        styletitler.setLeftBorderColor(defaultColor.getIndexed());
        styletitler.setBorderBottom(CellStyle.BORDER_THIN);
        styletitler.setBottomBorderColor(defaultColor.getIndexed());
        styletitler.setBorderRight(CellStyle.BORDER_THIN);
        styletitler.setRightBorderColor(defaultColor.getIndexed());
        styletitler.setFont(font11);

        styletitle = workBook.createCellStyle();
        styletitle.setFillForegroundColor((short)9);
        styletitle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styletitle.setAlignment(CellStyle.ALIGN_CENTER);
        styletitle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        styletitle.setBorderTop(CellStyle.BORDER_THIN);
        styletitle.setTopBorderColor(defaultColor.getIndexed());
        styletitle.setBorderLeft(CellStyle.BORDER_THIN);
        styletitle.setLeftBorderColor(defaultColor.getIndexed());
        styletitle.setBorderBottom(CellStyle.BORDER_THIN);
        styletitle.setBottomBorderColor(defaultColor.getIndexed());
        styletitle.setBorderRight(CellStyle.BORDER_THIN);
        styletitle.setRightBorderColor(defaultColor.getIndexed());
        styletitle.setFont(font11);
    }

}
