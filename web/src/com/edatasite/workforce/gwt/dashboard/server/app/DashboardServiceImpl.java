package com.edatasite.workforce.gwt.dashboard.server.app;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDate;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsTimeTrack;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.InOutItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.DateManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.dashboard.client.rpc.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

@Transactional
@Service("dashboardService")
public class DashboardServiceImpl implements DashboardService, Constants, DashboardServiceLocal {

    private static final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private static final DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat hourFormat = new SimpleDateFormat("HH:mm");

    // TimeTrack statuses
    private static final String AVAILABLE = "AVAILABLE";
    private static final String NOT_AVAILABLE = "NOT_AVAILABLE";
    private static final String BREAK = "BREAK";


    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TimeSlotManager timeSlotManager;
    @Autowired
    private DateManager dateManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private CompanyManager companyManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public InOutItem[] getInOutReport(Integer clientId, Integer projectId, Integer departmentId, Integer employeeId, Integer viewAsId,
                                      String groupByName, Date fromDate, Date toDate, boolean showDate, boolean showCheckIn, boolean showCheckOut,
                                      boolean showActualIn, boolean showLeaveReq, boolean showLauchHour,
                                      boolean showTimesheetHour, boolean showBudgetHour, boolean showMissingHours, boolean showFinImpcat) {

        Boolean isCustomfingerPrint = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FOR_CUSTOM_FINGER_PRINT);
        EdsCrmAccount client = null;
        if (clientId != null) {
            client = clientManager.get(clientId);
        }
        EdsProject project = null;
        if (projectId != null) {
            project = projectManager.get(projectId);
        }
        EdsDepartment department = null;
        if (departmentId != null) {
            department = departmentManager.get(departmentId);
        }
        EdsEmployee employee = null;
        if (employeeId != null) {
            employee = employeeManager.get(employeeId);
        }

        List<EdsSickRequest> sickRequests = sickRequestManager.getRequestListByStartDate(client, project, department, employee, viewAsId, groupByName, fromDate, toDate);

        Integer teamID = department != null ? department.getObjectID() : null;
        Integer employeeID = employee != null ? employee.getObjectID() : null;

        List<Object[]> values = timeSlotManager.getResult(teamID, employeeID, viewAsId, fromDate, toDate);

        Set<InOutKey> keys = new HashSet<>();
        for (Object[] value : values) {
            if (value[2] != null && value[3] != null) {
                keys.add(new InOutKey(value[2].toString(), value[3].toString()));
            }
        }

        Iterator<InOutKey> iterator = keys.iterator();
        List<Object[]> list;
        Map<InOutKey, List<Object[]>> resultMap = new HashMap<>();
        String emplId = "";
        String date;

        while (iterator.hasNext()) {
            InOutKey key = iterator.next();
            emplId = key.getEmployeeId();
            date = key.getDate();
            list = new ArrayList<>();
            for (Object[] value : values) {
                if ((value[2] != null && value[3] != null) && (value[2].toString().equals(emplId)) && value[3].toString().equals(date)) {
                    list.add(value);
                }
            }
            resultMap.put(key, list);
        }

        List<InOutItem> items = new ArrayList<>();
        Iterator<InOutKey> keyIterator = resultMap.keySet().iterator();
        InOutItem item;
        Iterator<Object[]> valueIterator;


        // result columns
        // 0 - startDate
        // 1 - endDate
        // 2 - emplId
        // 3 - date
        // 5- statusCode
        // 6- sum
        // 7- timeslotStartTime
        // 8- timeslotEndTime
        // 9- emplName
        // 10- timespent
        // 11- departmentId
        // 12- departmentName
        // 14 - inOutDifference

        String actualInHour;
        String launchHour = null;
        String timeslotStartTime = null;
        String timeslotEndTime = null;
        String emplName = null;
        String timeSpent = null;
        String depId = null;
        String depName = null;

        boolean firstLoop;

        ArrayList<String> inHours;
        ArrayList<String> outHours;
        ArrayList<String> duration;
        ArrayList<Integer> timeTrackIdList;

        EdsUser user = employeeManager.getUser();
        while (keyIterator.hasNext()) {

            InOutKey key = keyIterator.next();

            if (resultMap.get(key).size() == 1) {
                Object[] result = resultMap.get(key).get(0);
                if (result != null && result[5] != null && result[5].toString().equals(NOT_AVAILABLE)) {
                    continue;
                }
            }


            valueIterator = resultMap.get(key).iterator();
            inHours = new ArrayList<>();
            outHours = new ArrayList<>();
            duration = new ArrayList<>();
            timeTrackIdList = new ArrayList<>();
            actualInHour = null;
            firstLoop = true;

            while (valueIterator.hasNext()) {
                Object[] result = valueIterator.next();
                if (result[5] != null && result[5].toString().equals(AVAILABLE)) {
                    inHours.add(result[0] != null ? result[0].toString() : "");
                    outHours.add(result[1] != null ? result[1].toString() : "");
                    duration.add(result[14] != null ? result[14].toString() : "");
                    timeTrackIdList.add(Integer.parseInt(result[13] != null ? result[13].toString() : "0"));
                    actualInHour = result[6] != null ? result[6].toString() : null;
                } else {
                    if (result[5] != null && result[5].toString().equals(BREAK)) {
                        launchHour = result[6] != null ? result[6].toString() : null;
                    }
                }
                if (firstLoop) {
                    timeslotStartTime = result[7] != null ? result[7].toString() : null;
                    timeslotEndTime = result[8] != null ? result[8].toString() : null;
                    emplName = result[9] != null ? result[9].toString() : "";
                    timeSpent = result[10] != null ? result[10].toString() : null;
                    depId = result[11] != null ? result[11].toString() : "0";
                    depName = result[12] != null ? result[12].toString() : "";
                    emplId = result[2] != null ? result[2].toString() : "";
                }

                firstLoop = false;
            }
            item = new InOutItem();
            item.setEmployeeName(emplName);
            item.setEmployeeId(Integer.parseInt(emplId));
            item.setTimeTrackList(timeTrackIdList);

            if (showDate) {
                try {
                    item.setDateName(key.getDate());
                    item.setDateFormat(format2.parse(key.getDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (showBudgetHour) {
                item.setBudgetHour(getBudgetHour(timeslotStartTime, timeslotEndTime));
            }
            if (isHoliday(timeslotStartTime, timeslotEndTime)) {
                item.setHoliday(true);
            }
            if ("Department".equals(groupByName)) {
                item.setGroupById(Integer.valueOf(depId));
                item.setDepartmentName(depName);
            } else {
                if ("Employee".equals(groupByName)) {
                    item.setGroupById(Integer.valueOf(key.getEmployeeId()));
                } else {
                    if ("Date".equals(groupByName)) {
                        item.setGroupById(getDateId(key.getDate()));
                    }
                }
            }

            if (showLauchHour && launchHour != null) {
                item.setLaunchHour(getHourFromDate(launchHour));
            }
            if (showActualIn && actualInHour != null) {
                item.setActualInHour(isCustomfingerPrint != null && isCustomfingerPrint ? getFormattedHour(actualInHour) : getHourFromDate(actualInHour));
            }

            int maxInOutSize = Math.max(inHours.size(), outHours.size());
            if (showCheckIn && inHours.size() > 0) {
                item.setInHours(getHours(inHours, maxInOutSize, user));
            }
            if (showCheckOut && outHours.size() > 0) {
                item.setOutHours(getHours(outHours, maxInOutSize, user));
            }
            if (duration.size() > 0) {
                item.setDuration(getDurationArray(duration, maxInOutSize));
            }
            if (showMissingHours) {
                if (userManager.getUser().getCompany().getObjectID() == 21390) {
                    item.setMissingHour(getAvailableInTimeMissingHours(inHours, outHours, maxInOutSize, user, timeslotStartTime, timeslotEndTime));
                } else {
                    item.setMissingHour(getMissingHours(item.getBudgetHour(), item.getActualInHour()));
                }
            }
            if (showTimesheetHour && timeSpent != null) {
                item.setTimeSheetHour(timeSpent);
            }
            if (showFinImpcat) {
                String missingHour = "";
                Integer bool = 0;
                HashMap<Double, Integer> finImp = new HashMap<>();
                for (Map.Entry<String, Integer> temp : item.getMissingHour().entrySet()) {
                    missingHour = temp.getKey();
                    bool = temp.getValue();
                }
                if (bool == 0) {
                    finImp.put(getFinImpact(missingHour, bool, Integer.parseInt(emplId)), 0);
                    item.setFinImpact(finImp);
                } else {
                    finImp.put(getFinImpact(missingHour, bool, Integer.parseInt(emplId)), 1);
                    item.setFinImpact(finImp);
                }
            }

            ArrayList<HashMap<String, String>> reasons = new ArrayList<>();
            for (EdsSickRequest sickRequest : sickRequests) {
                Date userDate = null;
                try {
                    userDate = format2.parse(key.getDate())/*))*/;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (userDate != null) {
                    if (DateUtil.compareByDate(userDate, sickRequest.getStartDate()) &&
                            DateUtil.compareByDate(sickRequest.getEndDate(), userDate)) {
                        if (sickRequest.getEmployee().getObjectID().equals(item.getEmployeeId())) {
                            HashMap<String, String> reason = new HashMap<>();
                            item.setHasLeaveRequest(true);
                            if (sickRequest.getLeaveReason() != null) {
                                reason.put("name", referenceWfmMessageSource.localizeRef(sickRequest.getLeaveReason()));
                            } else {
                                reason.put("name", sickRequest.getOtherReason());
                            }
                            reason.put("description", sickRequest.getDescription());
                            reasons.add(reason);
                        }
                    }
                }
            }
            item.setLeaveRequest(reasons);
            items.add(item);
        }

        TreeMap<String, List<InOutItem>> sortMap = new TreeMap<>();
        List<InOutItem> sorted;

        Iterator<InOutItem> iter;
        while (DateUtil.compareByDate(toDate, fromDate)) {
            String dateStr = format2.format(fromDate);
            iter = items.iterator();
            sorted = new ArrayList<>();
            while (iter.hasNext()) {
                InOutItem itm = iter.next();
                if (itm != null && itm.getDateName() != null && itm.getDateName().equals(dateStr)) {
                    sorted.add(itm);
                }
            }

            if (sorted.size() > 0) {
                sorted.sort((o1, o2) -> ((Comparable) o1.getEmployeeName()).compareTo(o2.getEmployeeName()));
                sortMap.put(dateStr, sorted);
            }
            fromDate.setDate(fromDate.getDate() + 1);
        }


        List<InOutItem> sortedItems = new ArrayList<>();


        ArrayList<String> valueKeys = new ArrayList<String>(sortMap.keySet()); // Sorting in out report in reverse order (order by date desc)
        for (int k = valueKeys.size() - 1; k >= 0; k--) {
            String sortDate = valueKeys.get(k);
            sortedItems.addAll(sortMap.get(sortDate));
        }


        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsTimeTrack.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get In/Out report");
        return sortedItems.toArray(new InOutItem[0]);

    }

    private String getHourFromDate(String dateString) {
        String hour = null;
        try {
            Date date = hourFormat.parse(dateString);
            int i = date.getHours() * 60 + date.getMinutes();
            hour = String.valueOf(i);
        } catch (ParseException ignored) {
        }
        return hour;
    }

    private Integer getDateId(String dates) {
        Integer id = null;
        try {
            Date date = format2.parse(dates);
            id = getDateId(date);
        } catch (ParseException p) {
            p.printStackTrace();
        }

        return id;
    }

    private Integer getDateId(Date date) {
        Integer id = null;
        StringBuilder buffer = new StringBuilder();
        buffer.append(Integer.valueOf(date.getDate()));
        buffer.append(Integer.valueOf(date.getMonth()));
        buffer.append(Integer.valueOf(date.getYear()));
        String str = buffer.toString();
        id = Integer.valueOf(str);

        return id;
    }

    private String[] getHours(List<String> list, int maxSize, EdsUser user) {
        String[] hours = new String[maxSize];
        Collections.sort(list);
        int i = 0;
        for (String hour : list) {
            if (hour != null && !hour.equals("")) {
                hours[i] = getDateHourWithoutTimeZone(hour, user);
                i++;
            }
        }
        return hours;
    }

    private String[] getDurationArray(List<String> list, int maxSize) {
        String[] durations = new String[maxSize];
        int i = 0;
        for (String duration : list) {
            if (duration != null && !duration.equals("")) {
                durations[i] = duration.substring(0, 5);
                i++;
            }
        }
        return durations;
    }

    private String[] getHoursArray(ArrayList<String> hoursStrings, int maxSize, EdsUser user) {
        String[] hours = new String[maxSize];
        Collections.sort(hoursStrings);
        int i = 0;
        for (String h : hoursStrings) {
            if (h != null && !"".equals(h)) {
                hours[i] = getHourFromDate(getDateHour(h, user));
                i++;
            }
        }
        return hours;
    }

    private HashMap<String, Integer> getAvailableInTimeMissingHours(ArrayList<String> allInHours, ArrayList<String> allOutHours,
                                                                    int maxInOutSize, EdsUser user, String startTimeSlotTime, String endTimeSlotTime) {
        String[] allInHours_ = getHoursArray(allInHours, maxInOutSize, user);
        String[] allOutHours_ = getHoursArray(allOutHours, maxInOutSize, user);

        String firstStartInHour = "0";
        String lastEndOutHour = "0";
        int countSETT = 0;
        int countIOT = 0;
        for (String inHour : allInHours_) {
            if (inHour != null && !"".equals(inHour)) {
                firstStartInHour = inHour;
                break;
            }
        }
        for (String outHour : allOutHours_) {
            lastEndOutHour = outHour;
        }
        int stTST = Integer.valueOf(startTimeSlotTime) + 5;
        int etTST = Integer.valueOf(endTimeSlotTime);

        int stT = Integer.valueOf(firstStartInHour);
        int etT = Integer.valueOf((lastEndOutHour == null || "".equals(lastEndOutHour)) ? "0" : lastEndOutHour);

        allInHours_[0] = String.valueOf(Math.max(stTST, stT));
        int minEndTime = Math.min(etTST, etT);
        allOutHours_[maxInOutSize - 1] = String.valueOf(minEndTime == 0 ? etTST : minEndTime);

        countSETT = etTST - stTST;
        countIOT = countActualHour(allInHours_, allOutHours_, maxInOutSize);

        HashMap<String, Integer> answer = new HashMap<>();
        int time = 0;
        if (countSETT + 5 == 0) {
            time = 0;
            answer.put(String.valueOf(time), 0);
        } else {
            if (countSETT > countIOT) {
                time = (countSETT - countIOT);
                answer.put(String.valueOf(time), 0);
            } else {
                time = (countIOT - countSETT);
                answer.put(String.valueOf(time), 0);
            }
        }

        return answer;
    }

    private int countActualHour(String[] allInHours, String[] allOutHours, int maxInOutSize) {
        int countTime = 0;
        for (int i = 0; i < maxInOutSize; i++) {
            countTime += Integer.valueOf(allOutHours[i]) - Integer.valueOf(allInHours[i]);
        }
        return countTime;
    }

    private String getDateHour(String hour, EdsUser user) throws RuntimeException {
        try {
            Date datea = getDateHourWithTimeZone(hour, user);
            return hourFormat.format(datea);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    private String getDateHourWithoutTimeZone(String hour, EdsUser user) {
        try {
            DateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd k:mm");
            Date date = fullFormat.parse(hour);
            return hourFormat.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Date getDateHourWithTimeZone(String hour, EdsUser user) throws ParseException {
        DateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd k:mm");
        Date date = fullFormat.parse(hour);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd k:mm");
        TimeZone tz = user.getUserTimezone();
        dateFormat1.setTimeZone(tz);
        String s = dateFormat1.format(date);
        return fullFormat.parse(s);
    }

    private String getBudgetHour(String start, String end) {

        int time = 0;
        if (start != null && end != null) {
            try {
                time = Integer.valueOf(end) - Integer.valueOf(start);
            } catch (NumberFormatException ne) {
                ne.printStackTrace();
            }

        }
        return String.valueOf(time);
    }

    private HashMap<String, Integer> getMissingHours(String budgetHours, String actualHours) {
        int time = 0;
        HashMap<String, Integer> answer = new HashMap<>();
        if (budgetHours != null) {
            try {
                if (actualHours == null) {
                    actualHours = "0";
                }
                if (userManager.getUser().getCompany().getObjectID() != 21390) {
                    if (Integer.valueOf(budgetHours) > Integer.valueOf(actualHours)) {
                        time = Integer.valueOf(budgetHours) - Integer.valueOf(actualHours);
                        answer.put(String.valueOf(time), 1);
                    } else {
                        time = Integer.valueOf(actualHours) - Integer.valueOf(budgetHours);
                        answer.put(String.valueOf(time), 0);
                    }
                } else {
                    if (Integer.valueOf(budgetHours) > Integer.valueOf(actualHours)) {
                        time = (Integer.valueOf(budgetHours) - Integer.valueOf(actualHours)) + 5;
                        answer.put(String.valueOf(time), 1);
                    } else {
                        time = (Integer.valueOf(actualHours) - Integer.valueOf(budgetHours)) + 5;
                        answer.put(String.valueOf(time), 0);
                    }
                }
            } catch (NumberFormatException ne) {
                ne.printStackTrace();
            }
        }
        return answer;
    }

    private Double getFinImpact(String missingHour, Integer bool, Integer emplID) {
        if (bool == 1) {
            return castTimeToInt(missingHour) * Double.parseDouble(getSalaryGradeItem(emplID)) * (-1);
        } else {
            return castTimeToInt(missingHour) * Double.parseDouble(getSalaryGradeItem(emplID));
        }
    }

    private Double castTimeToInt(String time) {

        String[] temp = getFormattedHour(time).split(":");


        return Double.parseDouble(temp[0]) + Double.parseDouble(temp[1]) * 100 / 60 * 0.01;
    }

    private String getFormattedHour(String hour) {
        int hr;
        int mn;
        String formattedHour = "00:00";
        if (hour != null) {
            try {
                hr = Integer.valueOf(hour) / 60;
                mn = Integer.valueOf(hour) % 60;
                String hourStr = Integer.toString(hr);
                if (hourStr.length() < 2) {
                    hourStr = "0" + hourStr;
                }
                String minutesStr = Integer.toString(mn);
                if (minutesStr.length() < 2) {
                    minutesStr = "0" + minutesStr;
                }
                formattedHour = hourStr + ":" + minutesStr;

            } catch (NumberFormatException e) {
                e.printStackTrace();
                return hour;
            }
        }
        return formattedHour;
    }

    private boolean isHoliday(String start, String end) {
        if (start != null && end != null) {
            try {
                if (Integer.valueOf(start).equals(0) && Integer.valueOf(end).equals(0)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public void lastEnteredDate() {
        lastEnteredDate(0);
    }


    public void lastEnteredDate(Integer yearDifference) {
        Integer companyID = userManager.getUser().getCompany().getObjectID();
        lastEnteredDate(yearDifference, companyID);
    }

    /**
     * @param yearDifference mustn't be less then 0
     */
    public void lastEnteredDate(Integer yearDifference, Integer companyID) {
        EdsCompany company = companyManager.get(companyID);
        Date lastDate = dateManager.lastEnteredDate();
        Calendar endDateToCompare = Calendar.getInstance();
        endDateToCompare.setTime(ServerUtils.getEndDate(company, -1));
        if (yearDifference != null && yearDifference > 0) {
            endDateToCompare.add(Calendar.YEAR, yearDifference);
        }

        if (lastDate == null || !lastDate.after(endDateToCompare.getTime())) {
            Calendar fromDate = Calendar.getInstance();
            Calendar toDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            if (lastDate != null) {
                fromDate.setTime(lastDate);
                fromDate.add(Calendar.DAY_OF_YEAR, 1);
            } else {
                fromDate.setTime(ServerUtils.getStartDate(company, 0, false));
            }
            toDate.setTime(fromDate.getTime());
            endDate.setTime(ServerUtils.getEndDate(company, 4));
            if (yearDifference != null) {
                endDate.add(Calendar.YEAR, yearDifference);
            }
            toDate.add(Calendar.DAY_OF_YEAR, 1);
            while (format2.format(endDate.getTime()).compareTo(format2.format(fromDate.getTime())) >= 0) {
                fromDate.set(Calendar.AM_PM, 0);
                fromDate.set(Calendar.HOUR_OF_DAY, 0);
                fromDate.set(Calendar.MINUTE, 0);
                fromDate.set(Calendar.SECOND, 0);
                fromDate.set(Calendar.MILLISECOND, 0);

                toDate.set(Calendar.AM_PM, 0);
                toDate.set(Calendar.HOUR_OF_DAY, 0);
                toDate.set(Calendar.MINUTE, 0);
                toDate.set(Calendar.SECOND, 0);
                toDate.set(Calendar.MILLISECOND, 0);

                EdsDate newDate = new EdsDate();
                newDate.setToDate(toDate.getTime());
                newDate.setFromDate(fromDate.getTime());
                dateManager.create(newDate);

                fromDate.add(Calendar.DAY_OF_YEAR, 1);
                toDate.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getSalaryGradeItem(Integer employeeID) {
        EdsUser emp = userManager.get(employeeID);
        Double grade = employeeManager.get(emp.getObjectID()).getWageRate();
        return grade.toString();
    }
}
