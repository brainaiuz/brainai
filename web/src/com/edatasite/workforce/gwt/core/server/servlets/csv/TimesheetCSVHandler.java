package com.edatasite.workforce.gwt.core.server.servlets.csv;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskTransfer;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetReport;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 16, 2010
 * Time: 3:46:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimesheetCSVHandler extends AbstractBaseCSVHandler {

    @Autowired
    private TimesheetService timesheetService;
    @Autowired
    private UserManager userManager;

    private ListingFilterParameter filterParameter = new ListingFilterParameter();

    protected Object prepareRequest(HttpServletRequest request) {
        filterParameter = new ListingFilterParameter();
        filterParameter.setSearchType(Integer.parseInt(request.getParameter("weekoffset")));
        filterParameter.setStartDate(ServerUtils.parseDate(request.getParameter("date")));
        if (request.getParameter("userId") != null) {
            filterParameter.setEmployeeId(Integer.parseInt(request.getParameter("userId")));
        }

        String date = request.getParameter("selectedDate");
        String[] dateToSplit = date.split("/");
        String year = dateToSplit[2];
        String month = dateToSplit[0];
        String day = dateToSplit[1];

        filterParameter.setSelectedYear(Integer.parseInt(year));
        filterParameter.setSelectedMonth(Integer.parseInt(month));
        filterParameter.setSelectedDay(Integer.parseInt(day));

        return filterParameter;
    }

    @Override
    protected CSVTransferObject buildCSV(CSVTransferObject transferObject,Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        Set<Integer> projectIDs = new HashSet<>();
        Date clientsCurrentDate = fp.getStartDate();
        int weekOffset = fp.getSearchType();
        if (clientsCurrentDate == null) {
            clientsCurrentDate = new DateNonConvertable().getNonConvertedDate();
        }
        TimesheetData timesheetData = timesheetService.getData(new DateNonConvertable(clientsCurrentDate), weekOffset, fp);
        TaskTransfer[] taskTransfers = timesheetData.getTransferTasks();
        TimesheetDataItem[] dataItems = timesheetData.getItems();

        for (TaskTransfer taskTransfer1 : taskTransfers) {
            projectIDs.add(taskTransfer1.getProjectId());
        }

        DateNonConvertable[] dates = timesheetService.getTimesheetWeeklyDates(new DateNonConvertable(clientsCurrentDate), weekOffset);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM", Locale.ENGLISH);
        transferObject = new CSVTransferObject();
        //transferObject.setTitles("Project", "Task", "Mon " + format.format(dates[0].getNonConvertedDate()), "Tue " + format.format(dates[1].getNonConvertedDate()), "Wed " + format.format(dates[2].getNonConvertedDate()), "Thu " + format.format(dates[3].getNonConvertedDate()), "Fri " + format.format(dates[4].getNonConvertedDate()), "Sat " + format.format(dates[5].getNonConvertedDate()), "Sun " + format.format(dates[6].getNonConvertedDate()), "Total", "Status");
        int[] daylyStatistics = new int[7];
        Iterator<Integer> iterator = projectIDs.iterator();

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
        if (dayOfWeek == 1){
            transferObject.getRows().add(new String[]{
                    commonLocalizer.localize(PdfLocalizationName.project),
                    commonLocalizer.localize(PdfLocalizationName.task),
                    commonLocalizer.localize(PdfLocalizationName.sundayShort) + " " + day1,
                    commonLocalizer.localize(PdfLocalizationName.mondayShort) + " " + day2,
                    commonLocalizer.localize(PdfLocalizationName.tuesdayShort)+ " " + day3,
                    commonLocalizer.localize(PdfLocalizationName.wednesdayShort) + " " + day4,
                    commonLocalizer.localize(PdfLocalizationName.thursdayShort) +" " +  day5,
                    commonLocalizer.localize(PdfLocalizationName.fridayShort) + " " + day6,
                    commonLocalizer.localize(PdfLocalizationName.saturdayShort) + " " + day7,
                    commonLocalizer.localize(PdfLocalizationName.completed),
                    commonLocalizer.localize(PdfLocalizationName.estimatedTime),
                    commonLocalizer.localize(PdfLocalizationName.total),
                    commonLocalizer.localize(PdfLocalizationName.status)
            });
        } else if (dayOfWeek == 2) {
            transferObject.getRows().add(new String[]{
                    commonLocalizer.localize(PdfLocalizationName.project),
                    commonLocalizer.localize(PdfLocalizationName.task),
                    commonLocalizer.localize(PdfLocalizationName.mondayShort) + " " + day1,
                    commonLocalizer.localize(PdfLocalizationName.tuesdayShort)+ " " + day2,
                    commonLocalizer.localize(PdfLocalizationName.wednesdayShort) + " " + day3,
                    commonLocalizer.localize(PdfLocalizationName.thursdayShort) + " " + day4,
                    commonLocalizer.localize(PdfLocalizationName.fridayShort) + " " + day5,
                    commonLocalizer.localize(PdfLocalizationName.saturdayShort) + " " + day6 ,
                    commonLocalizer.localize(PdfLocalizationName.sundayShort) + " " + day7,
                    commonLocalizer.localize(PdfLocalizationName.completed),
                    commonLocalizer.localize(PdfLocalizationName.estimatedTime),
                    commonLocalizer.localize(PdfLocalizationName.total),
                    commonLocalizer.localize(PdfLocalizationName.status)
            });
        } else {
            transferObject.getRows().add(new String[]{
                    commonLocalizer.localize(PdfLocalizationName.project),
                    commonLocalizer.localize(PdfLocalizationName.task),
                    commonLocalizer.localize(PdfLocalizationName.saturdayShort) + " " + day1,
                    commonLocalizer.localize(PdfLocalizationName.sundayShort) + " " + day2,
                    commonLocalizer.localize(PdfLocalizationName.mondayShort) + " " + day3,
                    commonLocalizer.localize(PdfLocalizationName.tuesdayShort)+ " " + day4,
                    commonLocalizer.localize(PdfLocalizationName.wednesdayShort) + " " + day5,
                    commonLocalizer.localize(PdfLocalizationName.thursdayShort) + " " + day6,
                    commonLocalizer.localize(PdfLocalizationName.fridayShort) + " " + day7,
                    commonLocalizer.localize(PdfLocalizationName.completed),
                    commonLocalizer.localize(PdfLocalizationName.estimatedTime),
                    commonLocalizer.localize(PdfLocalizationName.total),
                    commonLocalizer.localize(PdfLocalizationName.status)
            });
        }

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
                                    daylyStatistics[6] += minutes;
                                }
                                case 1 -> {
                                    forMonday = formatMinutes(minutes);
                                    daylyStatistics[0] += minutes;
                                }
                                case 2 -> {
                                    forTuesday = formatMinutes(minutes);
                                    daylyStatistics[1] += minutes;
                                }
                                case 3 -> {
                                    forWednesday = formatMinutes(minutes);
                                    daylyStatistics[2] += minutes;
                                }
                                case 4 -> {
                                    forThursday = formatMinutes(minutes);
                                    daylyStatistics[3] += minutes;
                                }
                                case 5 -> {
                                    forFriday = formatMinutes(minutes);
                                    daylyStatistics[4] += minutes;
                                }
                                case 6 -> {
                                    forSaturday = formatMinutes(minutes);
                                    daylyStatistics[5] += minutes;
                                }
                                default -> {
                                }
                            }
                        }
                    }

                    String percentCompleted = String.valueOf(taskTransfer.getPercentCompleted()) + "%";

                    String estimatedTime = null;
                    if (taskTransfer.getTotalMinutes() != null) {
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
                        transferObject.getRows().add(new String[]{projectName, taskName, forSunday, forMonday, forTuesday, forWednesday, forThursday, forFriday, forSaturday, percentCompleted, estimatedTime, totalTimeSpent, status});
                    } else if (dayOfWeek == 2) {
                        transferObject.getRows().add(new String[]{projectName, taskName, forMonday, forTuesday, forWednesday, forThursday, forFriday, forSaturday, forSunday, percentCompleted, estimatedTime, totalTimeSpent, status});
                    } else {
                        transferObject.getRows().add(new String[]{projectName, taskName, forSaturday, forSunday, forMonday, forTuesday, forWednesday, forThursday, forFriday, percentCompleted, estimatedTime, totalTimeSpent, status});
                    }
                }
            }
        }

        //-----------------------Daily Total (Actual)- row
        Object[] values = new Object[8];
        for (int i = 0; i < dates.length; i++) {
            if (i != dates.length) {
                values[i + 1] = formatMinutes(timesheetData.getTimeslotItem().getActualWeekDaysPlannedTime()[i]);
            }
        }
        String monDaily = formatMinutes(daylyStatistics[0]);
        String tuDaily = formatMinutes(daylyStatistics[1]);
        String wedDaily = formatMinutes(daylyStatistics[2]);
        String thurDaily = formatMinutes(daylyStatistics[3]);
        String friDaily = formatMinutes(daylyStatistics[4]);
        String satDaily = formatMinutes(daylyStatistics[5]);
        String sunDaily = formatMinutes(daylyStatistics[6]);
        transferObject.getRows().add(new String[]{"", "", "", "", "", "", "", "", "", "", ""});
        if (dayOfWeek == 1){
            transferObject.getRows().add(new String[]{"", commonLocalizer.localize(PdfLocalizationName.dailyTotal),sunDaily, monDaily, tuDaily, wedDaily, thurDaily, friDaily, satDaily, "", ""});
        } else if (dayOfWeek == 2) {
            transferObject.getRows().add(new String[]{"", commonLocalizer.localize(PdfLocalizationName.dailyTotal), monDaily, tuDaily, wedDaily, thurDaily, friDaily, satDaily, sunDaily, "", ""});
        } else {
            transferObject.getRows().add(new String[]{"", commonLocalizer.localize(PdfLocalizationName.dailyTotal), satDaily, sunDaily, monDaily, tuDaily, wedDaily, thurDaily, friDaily, "", ""});
        }

        //-----------------------Daily Total (Planned) - row
        String monDailyPlanned = values[1] == null ? "0" : values[1] + "";
        String tuDailyPlanned = values[2] + "";
        String wedDailyPlanned = values[3] + "";
        String thurDailyPlanned = values[4] + "";
        String friDailyPlanned = values[5] + "";
        String satDailyPlanned = values[6] + "";
        String sunDailyPlanned = values[7] + "";

        transferObject.getRows().add(new String[]{"", commonLocalizer.localize(PdfLocalizationName.dailyTotalPlanned), monDailyPlanned, tuDailyPlanned, wedDailyPlanned, thurDailyPlanned, friDailyPlanned, satDailyPlanned, sunDailyPlanned, "", ""});

        //------------------Totals(Actual) - row
        transferObject.getRows().add(new String[]{"", "", "", "", "", "", "", "", "", "", ""});

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


        String totalWeeklyActual_ = formatMinutes(totalWeeklyActual);
        String totalWeeklyPlanned_ = formatMinutes(totalWeeklyPlanned);
        String totalMonthlyActual_ = formatMinutes(totalMonthlyActual);
        String totalMonthlyPlanned_ = formatMinutes(totalMonthlyPlanned);
        transferObject.getRows().add(new String[]{"", commonLocalizer.localize(PdfLocalizationName.totals) + "(" + commonLocalizer.localize(PdfLocalizationName.actual) + ")", totalWeeklyActual_, totalMonthlyActual_});

        //------------------Totals(Planned) - row
        transferObject.getRows().add(new String[]{"", commonLocalizer.localize(PdfLocalizationName.totals) + "(" + commonLocalizer.localize(PdfLocalizationName.planned) + ")", totalWeeklyPlanned_, totalMonthlyPlanned_});

        return transferObject;
    }

    @Override
    public String getFileName() {
        EdsUser user = userManager.getUser();
        if (filterParameter.getEmployeeId() != null) {
            user = userManager.get(filterParameter.getEmployeeId());
        }
        return user.getFirstName() + "_" + user.getLastName() + "_TimeSheet";
    }

    public static Date getMonthFirstDay(Date date) {
        Date current = date;
        while (current.getDate() != 1) {
            current = new Date(current.getYear(), current.getMonth(), current.getDate() - 1);
        }
        return current;
    }

    public static Date addDays(Date date, int days) {
        return new Date(date.getYear(), date.getMonth(), date.getDate() + days);
    }

    public static String formatMinutes(int minutes) {
        return formatMinutes(Integer.valueOf(minutes));
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

}
