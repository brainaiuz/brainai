package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskTransfer;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetReport;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TimeSheetPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    private TimesheetService timesheetService;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return true;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchType(Integer.parseInt(request.getParameter("weekoffset")));
        fp.setStartDate(ServerUtils.parseDate(request.getParameter("date")));
        fp.setLandscape(Boolean.parseBoolean(request.getParameter("IS_LANDSCAPE")));
        if (request.getParameter("userId") != null) {
            fp.setEmployeeId(Integer.parseInt(request.getParameter("userId")));
        }
        String date = request.getParameter("selectedDate");
        String[] dateToSplit = date.split("/");
        String year = dateToSplit[2];
        String month = dateToSplit[0];
        String day = dateToSplit[1];

        fp.setSelectedYear(Integer.parseInt(year));
        fp.setSelectedMonth(Integer.parseInt(month));
        fp.setSelectedDay(Integer.parseInt(day));

        if (request.getParameter("clientid") != null) {
            fp.setClientId(Integer.parseInt(request.getParameter("clientid")));
        }
        if (request.getParameter("projectid") != null) {
            fp.setProjectId(Integer.parseInt(request.getParameter("projectid")));
        }
        if (request.getParameter("workstreamid") != null) {
            fp.setWorkstreamID(Integer.parseInt(request.getParameter("workstreamid")));
        }
        return fp;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        if (fp.getEmployeeId() != null) {
            user = userManager.get(fp.getEmployeeId());
        }
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        Date clientsCurrentDate = fp.getStartDate();
        if (clientsCurrentDate == null) {
            clientsCurrentDate = new DateNonConvertable().getNonConvertedDate();
        }
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_TimeSheet_" + dateFormat(clientsCurrentDate));
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        Set<Integer> projectIDs = new HashSet<>();
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        Date clientsCurrentDate = fp.getStartDate();
        int weekOffset = fp.getSearchType();
        if (clientsCurrentDate == null) {
            clientsCurrentDate = new DateNonConvertable().getNonConvertedDate();
        }
        if (clientsCurrentDate == null) {
            clientsCurrentDate = new DateNonConvertable().getNonConvertedDate();
        }
        DateNonConvertable[] dates = timesheetService.getTimesheetWeeklyDates(new DateNonConvertable(clientsCurrentDate), weekOffset);
        EdsUser user = uploadManager.getUser();
        if (fp.getEmployeeId() != null) {
            user = userManager.get(fp.getEmployeeId());
        }
        TimesheetData timesheetData = timesheetService.getData(new DateNonConvertable(clientsCurrentDate), weekOffset, fp);
        TaskTransfer[] taskTransfers = timesheetData.getTransferTasks();
        TimesheetDataItem[] dataItems = timesheetData.getItems();
        SimpleDateFormat formatDateHeader = new SimpleDateFormat("dd/MM/yyyy", commonLocalizer.initializeUserLocale());

        for (TaskTransfer taskTransfer1 : taskTransfers) {
            projectIDs.add(taskTransfer1.getProjectId());
        }
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable viewTable = new CustomisedITextTable();
        viewTable.setName(commonLocalizer.localize(PdfLocalizationName.timesheet) + " " + commonLocalizer.localize(PdfLocalizationName.forLocalize) + ": "
                          + user.getFullName() + " (" + formatDateHeader.format(dates[0].getNonConvertedDate()) + " - "
                          + formatDateHeader.format(dates[6].getNonConvertedDate()) + ")");

        SimpleDateFormat format = new SimpleDateFormat("dd/MM", commonLocalizer.initializeUserLocale());
        Calendar c = Calendar.getInstance();
        c.setTime(dates[0].getNonConvertedDate());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String day1 = format.format(dates[0].getNonConvertedDate());
        String day2 = format.format(dates[1].getNonConvertedDate());
        String day3 = format.format(dates[2].getNonConvertedDate());
        String day4 = format.format(dates[3].getNonConvertedDate());
        String day5 = format.format(dates[4].getNonConvertedDate());
        String day6 = format.format(dates[5].getNonConvertedDate());
        String day7 = format.format(dates[6].getNonConvertedDate());
        if (dayOfWeek == 1) {
            viewTable.addColumn(PROJECT_NAME, commonLocalizer.localize(PdfLocalizationName.project));
            viewTable.addColumn(TASK_NAME, commonLocalizer.localize(PdfLocalizationName.task));
            viewTable.addColumn(SUNDAYSHORT, commonLocalizer.localize(PdfLocalizationName.sundayShort) + " \n" + day1);
            viewTable.addColumn(MONDAYSHORT, commonLocalizer.localize(PdfLocalizationName.mondayShort) + " \n" + day2);
            viewTable.addColumn(TUESDAYSHORT, commonLocalizer.localize(PdfLocalizationName.tuesdayShort) + " \n" + day3);
            viewTable.addColumn(WEDNESDAYSHORT, commonLocalizer.localize(PdfLocalizationName.wednesdayShort) + " \n" + day4);
            viewTable.addColumn(THURSDAYSHORT, commonLocalizer.localize(PdfLocalizationName.thursdayShort) + " \n" + day5);
            viewTable.addColumn(FRIDAYSHORT, commonLocalizer.localize(PdfLocalizationName.fridayShort) + " \n" + day6);
            viewTable.addColumn(SATURDAYSHORT, commonLocalizer.localize(PdfLocalizationName.saturdayShort) + " \n" + day7);
            viewTable.addColumn(PDFConstants.COMPLETED, commonLocalizer.localize(PdfLocalizationName.completed));
            viewTable.addColumn(ESTIMATEDTIME, commonLocalizer.localize(PdfLocalizationName.estimatedTime));
            viewTable.addColumn(TOTAL, commonLocalizer.localize(PdfLocalizationName.total));
            viewTable.addColumn(STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        } else if (dayOfWeek == 2) {
            viewTable.addColumn(PROJECT_NAME, commonLocalizer.localize(PdfLocalizationName.project));
            viewTable.addColumn(TASK_NAME, commonLocalizer.localize(PdfLocalizationName.task));
            viewTable.addColumn(MONDAYSHORT, commonLocalizer.localize(PdfLocalizationName.mondayShort) + " \n" + day1);
            viewTable.addColumn(TUESDAYSHORT, commonLocalizer.localize(PdfLocalizationName.tuesdayShort) + " \n" + day2);
            viewTable.addColumn(WEDNESDAYSHORT, commonLocalizer.localize(PdfLocalizationName.wednesdayShort) + " \n" + day3);
            viewTable.addColumn(THURSDAYSHORT, commonLocalizer.localize(PdfLocalizationName.thursdayShort) + " \n" + day4);
            viewTable.addColumn(FRIDAYSHORT, commonLocalizer.localize(PdfLocalizationName.fridayShort) + " \n" +   day5);
            viewTable.addColumn(SATURDAYSHORT, commonLocalizer.localize(PdfLocalizationName.saturdayShort) + " \n" +  day6);
            viewTable.addColumn(SUNDAYSHORT, commonLocalizer.localize(PdfLocalizationName.sundayShort) + " \n" + day7);
            viewTable.addColumn(PDFConstants.COMPLETED, commonLocalizer.localize(PdfLocalizationName.completed));
            viewTable.addColumn(ESTIMATEDTIME, commonLocalizer.localize(PdfLocalizationName.estimatedTime));
            viewTable.addColumn(TOTAL, commonLocalizer.localize(PdfLocalizationName.total));
            viewTable.addColumn(STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        } else {
            viewTable.addColumn(PROJECT_NAME, commonLocalizer.localize(PdfLocalizationName.project));
            viewTable.addColumn(TASK_NAME, commonLocalizer.localize(PdfLocalizationName.task));
            viewTable.addColumn(SATURDAYSHORT, commonLocalizer.localize(PdfLocalizationName.saturdayShort) + " \n" + day1);
            viewTable.addColumn(SUNDAYSHORT, commonLocalizer.localize(PdfLocalizationName.sundayShort) + " \n" +day2);
            viewTable.addColumn(MONDAYSHORT, commonLocalizer.localize(PdfLocalizationName.mondayShort) + " \n" + day3);
            viewTable.addColumn(TUESDAYSHORT, commonLocalizer.localize(PdfLocalizationName.tuesdayShort) + " \n" + day4);
            viewTable.addColumn(WEDNESDAYSHORT, commonLocalizer.localize(PdfLocalizationName.wednesdayShort) + " \n" + day5);
            viewTable.addColumn(THURSDAYSHORT, commonLocalizer.localize(PdfLocalizationName.thursdayShort) + " \n" + day6);
            viewTable.addColumn(FRIDAYSHORT, commonLocalizer.localize(PdfLocalizationName.fridayShort) + " \n" + day7);
            viewTable.addColumn(PDFConstants.COMPLETED, commonLocalizer.localize(PdfLocalizationName.completed));
            viewTable.addColumn(ESTIMATEDTIME, commonLocalizer.localize(PdfLocalizationName.estimatedTime));
            viewTable.addColumn(TOTAL, commonLocalizer.localize(PdfLocalizationName.total));
            viewTable.addColumn(STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        }
        // Generating main table
        List<String> columnsValue = Lists.newArrayList();
        Iterator<Integer> iterator = projectIDs.iterator();
        int[] dailyStatistics = new int[7];
        while (iterator.hasNext()) {
            Integer projectId = iterator.next();
            for (TaskTransfer taskTransfer : taskTransfers) {
                if (projectId.equals(taskTransfer.getProjectId())) {
                    String projectName = taskTransfer.getProjectName();
                    String taskName = taskTransfer.getEmplTaskName();
                    String forMonday = "00:00";
                    String forTuesday = "00:00";
                    String forWednesday = "00:00";
                    String forThursday = "00:00";
                    String forFriday = "00:00";
                    String forSaturday = "00:00";
                    String forSunday = "00:00";
                    for (TimesheetDataItem dataItem : dataItems) {
                        if (dataItem.getEmployeeTaskID() == taskTransfer.getEmplTaskId()) {
                            int minutes = dataItem.getMinutes();
                            switch (dataItem.getDate().getDay()) {
                                case 0 -> {
                                    forSunday = formatMinutes(minutes);
                                    dailyStatistics[6] += minutes;
                                }
                                case 1 -> {
                                    forMonday = formatMinutes(minutes);
                                    dailyStatistics[0] += minutes;
                                }
                                case 2 -> {
                                    forTuesday = formatMinutes(minutes);
                                    dailyStatistics[1] += minutes;
                                }
                                case 3 -> {
                                    forWednesday = formatMinutes(minutes);
                                    dailyStatistics[2] += minutes;
                                }
                                case 4 -> {
                                    forThursday = formatMinutes(minutes);
                                    dailyStatistics[3] += minutes;
                                }
                                case 5 -> {
                                    forFriday = formatMinutes(minutes);
                                    dailyStatistics[4] += minutes;
                                }
                                case 6 -> {
                                    forSaturday = formatMinutes(minutes);
                                    dailyStatistics[5] += minutes;
                                }
                                default -> {
                                }
                            }
                        }
                    }
                    String percentCompleted = taskTransfer.getPercentCompleted() + "%";

                    String estimatedTime = null;
                    if (taskTransfer.getEstimatedTime() != null) {
                        estimatedTime = formatMinutes(taskTransfer.getEstimatedTime());
                    } else {
                        estimatedTime = "00:00";
                    }

                    String totalTimeSpent = null;
                    if (taskTransfer.getTotalMinutes() != null) {
                        totalTimeSpent = formatMinutes(taskTransfer.getTotalMinutes());
                    } else {
                        totalTimeSpent = "00:00";
                    }
                    String status = taskTransfer.getTaskStatus().getStatusName() != null ? taskTransfer.getTaskStatus().getStatusName() : "";
                    if (dayOfWeek == 1) {
                        columnsValue.clear();
                        columnsValue.add(StringUtils.defaultString(projectName));
                        columnsValue.add(StringUtils.defaultString(taskName));
                        columnsValue.add(StringUtils.defaultString(forSunday));
                        columnsValue.add(StringUtils.defaultString(forMonday));
                        columnsValue.add(StringUtils.defaultString(forTuesday));
                        columnsValue.add(StringUtils.defaultString(forWednesday));
                        columnsValue.add(StringUtils.defaultString(forThursday));
                        columnsValue.add(StringUtils.defaultString(forFriday));
                        columnsValue.add(StringUtils.defaultString(forSaturday));
                        columnsValue.add(StringUtils.defaultString(percentCompleted));
                        columnsValue.add(StringUtils.defaultString(estimatedTime));
                        columnsValue.add(StringUtils.defaultString(totalTimeSpent));
                        columnsValue.add(StringUtils.defaultString(status));
                        viewTable.addRow(columnsValue.toArray(new String[]{}));
                    } else if (dayOfWeek == 2) {
                        columnsValue.clear();
                        columnsValue.add(StringUtils.defaultString(projectName));
                        columnsValue.add(StringUtils.defaultString(taskName));
                        columnsValue.add(StringUtils.defaultString(forMonday));
                        columnsValue.add(StringUtils.defaultString(forTuesday));
                        columnsValue.add(StringUtils.defaultString(forWednesday));
                        columnsValue.add(StringUtils.defaultString(forThursday));
                        columnsValue.add(StringUtils.defaultString(forFriday));
                        columnsValue.add(StringUtils.defaultString(forSaturday));
                        columnsValue.add(StringUtils.defaultString(forSunday));
                        columnsValue.add(StringUtils.defaultString(percentCompleted));
                        columnsValue.add(StringUtils.defaultString(estimatedTime));
                        columnsValue.add(StringUtils.defaultString(totalTimeSpent));
                        columnsValue.add(StringUtils.defaultString(status));
                        viewTable.addRow(columnsValue.toArray(new String[]{}));
                    } else {
                        columnsValue.clear();
                        columnsValue.add(StringUtils.defaultString(projectName));
                        columnsValue.add(StringUtils.defaultString(taskName));
                        columnsValue.add(StringUtils.defaultString(forSaturday));
                        columnsValue.add(StringUtils.defaultString(forSunday));
                        columnsValue.add(StringUtils.defaultString(forMonday));
                        columnsValue.add(StringUtils.defaultString(forTuesday));
                        columnsValue.add(StringUtils.defaultString(forWednesday));
                        columnsValue.add(StringUtils.defaultString(forThursday));
                        columnsValue.add(StringUtils.defaultString(forFriday));
                        columnsValue.add(StringUtils.defaultString(percentCompleted));
                        columnsValue.add(StringUtils.defaultString(estimatedTime));
                        columnsValue.add(StringUtils.defaultString(totalTimeSpent));
                        columnsValue.add(StringUtils.defaultString(status));
                        viewTable.addRow(columnsValue.toArray(new String[]{}));
                    }
                }
            }
        }
        customData.put("VIEW_TABLE", viewTable);

        Object[] values = new Object[8];
        for (int i = 0; i < dates.length; i++) {
            if (i != dates.length) {
                values[i + 1] = formatMinutes(timesheetData.getTimeslotItem().getActualWeekDaysPlannedTime()[i]);
            }
        }

        String monDaily = formatMinutes(dailyStatistics[0]);
        String tuDaily = formatMinutes(dailyStatistics[1]);
        String wedDaily = formatMinutes(dailyStatistics[2]);
        String thurDaily = formatMinutes(dailyStatistics[3]);
        String friDaily = formatMinutes(dailyStatistics[4]);
        String satDaily = formatMinutes(dailyStatistics[5]);
        String sunDaily = formatMinutes(dailyStatistics[6]);

        CustomisedITextTable dailyTable = new CustomisedITextTable();
        dailyTable.addColumnOrder("DAILY_TOTAL");
        dailyTable.addColumnOrder(MONDAYSHORT);
        dailyTable.addColumnOrder(TUESDAYSHORT);
        dailyTable.addColumnOrder(WEDNESDAYSHORT);
        dailyTable.addColumnOrder(THURSDAYSHORT);
        dailyTable.addColumnOrder(FRIDAYSHORT);
        dailyTable.addColumnOrder(SATURDAYSHORT);
        dailyTable.addColumnOrder(SUNDAYSHORT);
        columnsValue.clear();
        if (dayOfWeek == 1) {
            columnsValue.add(commonLocalizer.localize(PdfLocalizationName.dailyTotal));
            columnsValue.add(StringUtils.defaultString(sunDaily));
            columnsValue.add(StringUtils.defaultString(monDaily));
            columnsValue.add(StringUtils.defaultString(tuDaily));
            columnsValue.add(StringUtils.defaultString(wedDaily));
            columnsValue.add(StringUtils.defaultString(thurDaily));
            columnsValue.add(StringUtils.defaultString(friDaily));
            columnsValue.add(StringUtils.defaultString(satDaily));
            dailyTable.addRow(columnsValue.toArray(new String[]{}));
        } else if (dayOfWeek == 2) {
            columnsValue.add(commonLocalizer.localize(PdfLocalizationName.dailyTotal));
            columnsValue.add(StringUtils.defaultString(monDaily));
            columnsValue.add(StringUtils.defaultString(tuDaily));
            columnsValue.add(StringUtils.defaultString(wedDaily));
            columnsValue.add(StringUtils.defaultString(thurDaily));
            columnsValue.add(StringUtils.defaultString(friDaily));
            columnsValue.add(StringUtils.defaultString(satDaily));
            columnsValue.add(StringUtils.defaultString(sunDaily));
            dailyTable.addRow(columnsValue.toArray(new String[]{}));
        } else {
            columnsValue.add(commonLocalizer.localize(PdfLocalizationName.dailyTotal));
            columnsValue.add(StringUtils.defaultString(satDaily));
            columnsValue.add(StringUtils.defaultString(sunDaily));
            columnsValue.add(StringUtils.defaultString(monDaily));
            columnsValue.add(StringUtils.defaultString(tuDaily));
            columnsValue.add(StringUtils.defaultString(wedDaily));
            columnsValue.add(StringUtils.defaultString(thurDaily));
            columnsValue.add(StringUtils.defaultString(friDaily));
            dailyTable.addRow(columnsValue.toArray(new String[]{}));
        }
        String monDailyPlanned = values[1] == null ? "0" : values[1] + "";
        String tuDailyPlanned = values[2] + "";
        String wedDailyPlanned = values[3] + "";
        String thurDailyPlanned = values[4] + "";
        String friDailyPlanned = values[5] + "";
        String satDailyPlanned = values[6] + "";
        String sunDailyPlanned = values[7] + "";

        columnsValue.clear();
        columnsValue.add(commonLocalizer.localize(PdfLocalizationName.dailyTotalPlanned));
        columnsValue.add(StringUtils.defaultString(monDailyPlanned));
        columnsValue.add(StringUtils.defaultString(tuDailyPlanned));
        columnsValue.add(StringUtils.defaultString(wedDailyPlanned));
        columnsValue.add(StringUtils.defaultString(thurDailyPlanned));
        columnsValue.add(StringUtils.defaultString(friDailyPlanned));
        columnsValue.add(StringUtils.defaultString(satDailyPlanned));
        columnsValue.add(StringUtils.defaultString(sunDailyPlanned));
        dailyTable.addRow(columnsValue.toArray(new String[]{}));
        customData.put("DAILY_TABLE", dailyTable);

        TimesheetReport[] weeklyReport = timesheetData.getWeeklyStatistics();
        TimesheetReport[] monthlyReport = timesheetData.getMonthlyStatistices();
        int totalWeeklyActual = 0, totalMonthlyActual = 0;
        for (TimesheetReport aWeeklyReport : weeklyReport) {
            totalWeeklyActual += aWeeklyReport.getSum();
        }
        for (TimesheetReport aMonthlyReport : monthlyReport) {
            totalMonthlyActual += aMonthlyReport.getSum();
        }
        int totalWeeklyPlanned = 0, totalMonthlyPlanned = 0;
        for (int i = 0; i < timesheetData.getDates().length; i++) {
            totalWeeklyPlanned += timesheetData.getTimeslotItem().getActualWeekDaysPlannedTime()[i];
        }
        totalMonthlyPlanned = timesheetData.getTimeslotItem().getMonthlyPlannedTime();

        CustomisedITextTable totalTable = new CustomisedITextTable();
        totalTable.addColumnOrder(TOTAL);
        totalTable.addColumnOrder("WEEKLY");
        totalTable.addColumnOrder("MONTHLY");
        totalTable.addRow(commonLocalizer.localize(PdfLocalizationName.totals) + "(" + commonLocalizer.localize(PdfLocalizationName.actual) + ")",
                          formatMinutes(totalWeeklyActual),
                          formatMinutes(totalMonthlyActual));

        totalTable.addRow(commonLocalizer.localize(PdfLocalizationName.totals) + "(" + commonLocalizer.localize(PdfLocalizationName.planned) + ")",
                          formatMinutes(totalWeeklyPlanned),
                          formatMinutes(totalMonthlyPlanned));
        customData.put("TOTAL_TABLE", totalTable);

        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setCustomData(customData);
        return pdf;
    }

    public static String formatMinutes(Integer minutes) {
        if (minutes == null) {
            return "00:00";
        }
        int hours = minutes / 60;
        int mins = minutes % 60;

        String strHours = hours < 10 ? "0" + hours : "" + hours;
        String strMinutes = mins < 10 ? "0" + mins : "" + mins;

        return strHours + ":" + strMinutes;
    }

    public static String formatMinutes(int minutes) {
        return formatMinutes(Integer.valueOf(minutes));
    }

    public void setTimesheetService(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("timesheet");
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.TIMESHEET;
    }
}
