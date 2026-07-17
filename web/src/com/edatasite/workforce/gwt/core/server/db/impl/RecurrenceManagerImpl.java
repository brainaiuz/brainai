package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.EdsSchemaUpdater;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Mar 4, 2010
 * Time: 4:46:59 AM
 * To change this template use File | Settings | File Templates.
 */

@Repository("recurrenceManager")
public class RecurrenceManagerImpl extends BaseManager<EdsRecurrence> implements RecurrenceManager, SchedulerConstant {

    public RecurrenceManagerImpl() {
        super(EdsRecurrence.class);
    }

    public String getCronExpression(EdsRecurrence recurrence) {
        Date startDate = recurrence.getStartDate();
        StringBuilder expression = new StringBuilder("0 " + startDate.getMinutes() + " " + startDate.getHours());
        switch (recurrence.getType()) {
            case RECURRENCE_TYPE_MINUTELY -> {
                return startDate.getSeconds() + " " + startDate.getMinutes() + "/" + recurrence.getInterval() + ALL + ALL + ALL + NO_SPECIFIC_VALUE;
            }
            case RECURRENCE_TYPE_HOURLY -> {
                return startDate.getSeconds() + " " + startDate.getMinutes() + ALL + ALL + ALL + NO_SPECIFIC_VALUE;
            }
            case RECURRENCE_TYPE_DAILY -> {
                //set daily interval
                if (recurrence.getDailyPatternOptions() != null && recurrence.getDailyPatternOptions() == DAILY_PATTERN_OPTION_INTERVAL) {
                    expression.append(" 1/").append(recurrence.getInterval());
                } else {
                    expression.append(NO_SPECIFIC_VALUE);
                }
                expression.append(ALL); //set months
                if (recurrence.getDailyPatternOptions() != null && recurrence.getDailyPatternOptions() == DAILY_PATTERN_OPTION_INTERVAL) {
                    expression.append(NO_SPECIFIC_VALUE); //set Days of Week
                } else {
                    expression.append(WEEKDAYS);
                } //set Days of Week
            }
            case RECURRENCE_TYPE_EVEN_DAILY, RECURRENCE_TYPE_ODD_DAILY -> {
                //set daily interval
                if (recurrence.getDailyPatternOptions() != null && recurrence.getDailyPatternOptions() == DAILY_PATTERN_OPTION_INTERVAL) {
                    expression.append(" 1/").append(recurrence.getInterval());
                } else {
                    expression.append(NO_SPECIFIC_VALUE);
                }
                expression.append(ALL); //set months
                if (recurrence.getDailyPatternOptions() != null && recurrence.getDailyPatternOptions() == DAILY_PATTERN_OPTION_INTERVAL) {
                    expression.append(NO_SPECIFIC_VALUE); //set Days of Week
                } else {
                    expression.append(WEEKDAYS);
                } //set Days of Week
            }
            case RECURRENCE_TYPE_WEEKLY -> {
                expression.append(" ? *"); //"no specific value" for <Day of month> & "every" <Month>
                //set Days of Week with interval
                StringBuilder daysOfWeek = new StringBuilder();
                if (recurrence.isSunday()) {
                    daysOfWeek.append("SUN,");
                }
                if (recurrence.isMonday()) {
                    daysOfWeek.append("MON,");
                }
                if (recurrence.isTuesday()) {
                    daysOfWeek.append("TUE,");
                }
                if (recurrence.isWednesday()) {
                    daysOfWeek.append("WED,");
                }
                if (recurrence.isThursday()) {
                    daysOfWeek.append("THU,");
                }
                if (recurrence.isFriday()) {
                    daysOfWeek.append("FRI,");
                }
                if (recurrence.isSaturday()) {
                    daysOfWeek.append("SAT,");
                }

                if (daysOfWeek.toString().length() > 3) {
                    expression.append(" " + daysOfWeek.substring(0, daysOfWeek.toString().lastIndexOf(',')));
                } else {
                    expression.append(" ?");
                }
            }
            case RECURRENCE_TYPE_MONTHLY -> {
                if (recurrence.getMonthlyOrYearlyPatternOption() != null && recurrence.getMonthlyOrYearlyPatternOption() == MONTHLY_OR_YEARLY_PATTERN_CUSTOM) {
                    expression.append(" " + recurrence.getMonthlyOrYearlyDay()); //set Day of month
                    expression.append(" " + startDate.getMonth() + "/" + recurrence.getInterval()); //set Month
                    expression.append(NO_SPECIFIC_VALUE); //set Days of Week
                } else {
                    String monthlyOrYearlyDay = switch (recurrence.getMonthlyOrYearlyDay()) {
                        case SUNDAY -> "SUN";
                        case MONDAY -> "MON";
                        case TUESDAY -> "TUE";
                        case WEDNESDAY -> "WED";
                        case THURSDAY -> "THU";
                        case FRIDAY -> "FRI";
                        case SATURDAY -> "SAT";
                        default -> "";
                    };
                    expression.append(NO_SPECIFIC_VALUE); // set day of month
                    expression.append(" " + startDate.getMonth() + "/" + recurrence.getInterval()); // set month
                    expression.append(Integer.valueOf(LAST).equals(recurrence.getCustomPatternDay()) ? " " + monthlyOrYearlyDay + "L" : " " + monthlyOrYearlyDay + "#" + recurrence.getCustomPatternDay()); //set Day of week
                }
            }
            case RECURRENCE_TYPE_YEARLY -> {
                if (recurrence.getMonthlyOrYearlyPatternOption() != null && recurrence.getMonthlyOrYearlyPatternOption() == MONTHLY_OR_YEARLY_PATTERN_CUSTOM) {
                    expression.append(" " + recurrence.getMonthlyOrYearlyDay()); //set Day of month
                    expression.append(" " + recurrence.getYearlyMonth()); //set Month
                    expression.append(NO_SPECIFIC_VALUE); //set Day of week
                } else {
                    String monthlyOrYearlyDay = switch (recurrence.getMonthlyOrYearlyDay()) {
                        case SUNDAY -> "SUN";
                        case MONDAY -> "MON";
                        case TUESDAY -> "TUE";
                        case WEDNESDAY -> "WED";
                        case THURSDAY -> "THU";
                        case FRIDAY -> "FRI";
                        case SATURDAY -> "SAT";
                        default -> "";
                    };
                    expression.append(NO_SPECIFIC_VALUE); // set day of month
                    expression.append(" " + recurrence.getYearlyMonth()); //set Month
                    expression.append(recurrence.getCustomPatternDay().equals(LAST) ? " " + monthlyOrYearlyDay + "L" : " " + monthlyOrYearlyDay + "#" + recurrence.getCustomPatternDay()); //set Day of week
                }
            }
        }
        return expression.toString();
    }

    public Date getTriggerEndDate(EdsRecurrence recurrence) {
        return getTriggerEndDate(recurrence, false);
    }

    public Date getTriggerEndDate(EdsRecurrence recurrence, boolean forInvoice) {
        Date occurenceDate = (Date) recurrence.getStartDate().clone();
        if (recurrence.getEndType() == NO_END_DATE) {
            occurenceDate.setYear(occurenceDate.getYear() + 5);
            return occurenceDate;
        } else if (recurrence.getEndType() == END_BY_DATE) {
            return recurrence.getEndDate();
        } else if (recurrence.getEndType() == END_AFTER_OCCURRENCES) {
            if (recurrence.getType() == RECURRENCE_TYPE_DAILY && recurrence.getDailyPatternOptions() != null) {
                switch (recurrence.getDailyPatternOptions()) {
                    case DAILY_PATTERN_OPTION_INTERVAL -> {
                        int occurs = recurrence.getOccurrence();
                        if (forInvoice) {
                            if (occurs > 0) {
                                occurs--;
                            }
                        }
                        occurenceDate = DateUtil.addDays(occurenceDate, occurs * recurrence.getInterval());
                    }
                    case DAILY_PATTERN_OPTION_WEEKDAYS -> {
                        int occurence = 0;
                        Date startDate = (Date) recurrence.getStartDate().clone();
                        startDate = DateUtil.addDays(startDate, -1);
                        while (occurence < recurrence.getOccurrence()) {
                            startDate = DateUtil.addDays(startDate, 1);
                            if (startDate.getDay() > 0 && startDate.getDay() < 6) {
                                occurenceDate = startDate;
                                occurence++;
                            }
                        }
                        setTime(occurenceDate, recurrence.getStartDate());
                    }
                }
            } else if (recurrence.getType() == RECURRENCE_TYPE_WEEKLY) {
                List<Integer> daysOfWeek = new ArrayList<>();
                if (recurrence.isSunday()) {
                    daysOfWeek.add(SUNDAY);
                }
                if (recurrence.isMonday()) {
                    daysOfWeek.add(MONDAY);
                }
                if (recurrence.isTuesday()) {
                    daysOfWeek.add(TUESDAY);
                }
                if (recurrence.isWednesday()) {
                    daysOfWeek.add(WEDNESDAY);
                }
                if (recurrence.isThursday()) {
                    daysOfWeek.add(THURSDAY);
                }
                if (recurrence.isFriday()) {
                    daysOfWeek.add(FRIDAY);
                }
                if (recurrence.isSaturday()) {
                    daysOfWeek.add(SATURDAY);
                }

                int occurence = 0;
                if (forInvoice) {
                    occurence = 1;
                }
                Date startDate = (Date) recurrence.getStartDate().clone();
                while (occurence <= recurrence.getOccurrence()) {
                    if (daysOfWeek.contains(startDate.getDay() + 1)) {
                        occurenceDate = (Date) startDate.clone();
                        occurence++;
                    }
                    startDate = DateUtil.addDays(startDate, 1);
                }
                setTime(occurenceDate, recurrence.getStartDate());
            } else if (recurrence.getType() == RECURRENCE_TYPE_MONTHLY) {
                switch (recurrence.getMonthlyOrYearlyPatternOption()) {
                    case MONTHLY_OR_YEARLY_PATTERN_CUSTOM -> {
                        if (forInvoice) {
                            occurenceDate = DateUtil.addMonths(occurenceDate, (recurrence.getOccurrence() - 1) * recurrence.getInterval());
                        } else {
                            occurenceDate = DateUtil.addMonths(occurenceDate, recurrence.getOccurrence() * recurrence.getInterval());
                        }
                        Date date = DateUtil.getMonthLastDate((Date) occurenceDate.clone());
                        occurenceDate.setDate(recurrence.getMonthlyOrYearlyDay() <= date.getDate() ? recurrence.getMonthlyOrYearlyDay() : date.getDate());
                    }
                    case MONTHLY_OR_YEARLY_PATTERN_SIMPLE -> {
                        Date monthFirstDay = DateUtil.getMonthFirstDay(recurrence.getStartDate());
                        int occurence = 0;
                        while (occurence < recurrence.getOccurrence()) {
                            int firstNDay = 0;
                            while (firstNDay < recurrence.getCustomPatternDay()) {
                                if (monthFirstDay.getDay() + 1 == recurrence.getMonthlyOrYearlyDay()) {
                                    occurenceDate = (Date) monthFirstDay.clone();
                                    firstNDay++;
                                }
                                if (firstNDay == 0) {
                                    monthFirstDay = DateUtil.addDays(monthFirstDay, 1);
                                } else {
                                    monthFirstDay = DateUtil.addDays(monthFirstDay, 7);
                                }
                            }
                            monthFirstDay = DateUtil.addMonths(occurenceDate, recurrence.getInterval());
                            monthFirstDay = DateUtil.getMonthFirstDay(monthFirstDay);
                            occurence++;
                        }
                    }
                }
                setTime(occurenceDate, recurrence.getStartDate());
            } else if (recurrence.getType() == RECURRENCE_TYPE_YEARLY) {
                switch (recurrence.getMonthlyOrYearlyPatternOption()) {
                    case MONTHLY_OR_YEARLY_PATTERN_CUSTOM -> {
                        Date date = new Date(occurenceDate.getYear() + recurrence.getOccurrence(), recurrence.getYearlyMonth() - 1, recurrence.getMonthlyOrYearlyDay(),
                                occurenceDate.getHours(), occurenceDate.getMinutes(), occurenceDate.getSeconds());
                        date = DateUtil.getMonthLastDate(date);
                        date.setDate(recurrence.getMonthlyOrYearlyDay() <= date.getDate() ? recurrence.getMonthlyOrYearlyDay() : date.getDate());
                        occurenceDate = date;
                    }
                    case MONTHLY_OR_YEARLY_PATTERN_SIMPLE -> {
                        Date monthFirstDay = DateUtil.getMonthFirstDay(recurrence.getStartDate());    // select the first day of this month
                        int occurence = 0;
                        while (occurence <= recurrence.getOccurrence()) {
                            int firstNDay = 0;
                            while (firstNDay < recurrence.getCustomPatternDay()) {
                                if (monthFirstDay.getDay() + 1 == recurrence.getMonthlyOrYearlyDay()) {
                                    occurenceDate = (Date) monthFirstDay.clone();
                                    firstNDay++;
                                }
                                if (firstNDay == 0) {
                                    monthFirstDay = DateUtil.addDays(monthFirstDay, 1);
                                }
                                if (firstNDay > 0 && firstNDay < recurrence.getCustomPatternDay()) {
                                    monthFirstDay = DateUtil.addDays(monthFirstDay, 7);
                                } else if (firstNDay >= recurrence.getCustomPatternDay()) {
                                    occurenceDate = (Date) monthFirstDay.clone();
                                    monthFirstDay = new Date(occurenceDate.getYear() + 1, 0, 1, 0, 0, 0);
                                    occurence++;
                                }
                            }
                        }
                    }
                }
                setTime(occurenceDate, recurrence.getStartDate());
            }
        }
        return occurenceDate;
    }

    private void setTime(Date occurenceDate, Date recStartDate) {
        occurenceDate.setHours(recStartDate.getHours());
        occurenceDate.setMinutes(recStartDate.getMinutes());
        occurenceDate.setSeconds(recStartDate.getSeconds());
    }

    public List<EdsRecurrence> getRecurrences(boolean isMailForceTrack) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> schemaList = new ArrayList<>();
        try {
            EdsSchemaUpdater.setDbUrl(WfmJpaTemplate.getDataBaseURL());
            schemaList = EdsSchemaUpdater.getSchemaList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (schemaList != null && !schemaList.isEmpty()) {
            map.put("schemalist", schemaList);
            return findByNamedParams("select rec from EdsRecurrence rec where rec.deleted <> true and rec.job.id not in (" + RECURRING_EVENT + "," + RECURRING_TASK + ") and " +
                    (isMailForceTrack ? "rec.job.id=" + MASS_MAILING_RECURRENCE + " and " : " ") +
                    " rec.companyID is not null and rec.companyID not in (select com.objectID from EdsCompany com where com.active <> true) and " +
                    "cast(rec.companyID as string) in (:schemalist) order by rec.objectID", map);
        } else {
            return find("select rec from EdsRecurrence rec where rec.deleted <> true and rec.job.id not in (" + RECURRING_EVENT + "," + RECURRING_TASK + ") and " +
                    (isMailForceTrack ? "rec.job.id=" + MASS_MAILING_RECURRENCE + " and " : " ") +
                    "rec.companyID is not null and rec.companyID not in " +
                    "(select com.objectID from EdsCompany com where com.active <> true)  order by rec.objectID");
        }
    }

    public List<EdsRecurrence> getRecurrencesByJobType(Integer jobID) {
        return find("select rec from EdsRecurrence rec where rec.job.objectID = ?", jobID);
    }

    public List<EdsRecurrence> getRecurrencesByBusObjectId(Integer busObjectId) {
        return find("select rec from EdsRecurrence rec where rec.busObjectId= ?", busObjectId);
    }

    public EdsRecurrence getRecurrencesByUser(Integer busObjectId, Integer jobID, EdsUser user) {
        if (user == null) {
            user = getUser();
        }
        if (busObjectId == null || jobID == null) {
            return null;
        }
        Integer companyID = (companyID = SecurityContext.getCompanyID()) != null ? companyID : user.getCompany().getObjectID();
        return (EdsRecurrence) findSingle("select rec from EdsRecurrence rec where rec.busObjectId = ? AND rec.job.objectID = ? AND rec.companyID = ?", busObjectId, jobID, companyID);
    }

    public List<EdsRecurrence> getChangedRecurrences() {
        return find("select rec from EdsRecurrence rec where rec.changed = true and rec.job.id not in (" + RECURRING_EVENT + "," + RECURRING_TASK + ") and (rec.status is null or rec.status <> '" + IN_PROGRESS + "')");
    }

    public EdsRecurrence getRecurrenceJob(Integer jobID, Integer busObjectId, Integer companyID) {
        if (jobID != null) {
            return (EdsRecurrence) findSingle("select rec from EdsRecurrence rec where deleted<>true and  rec.job.objectID = ? and rec.busObjectId = ? and rec.companyID=?", jobID, busObjectId, companyID);
        } else {
            return null;
        }
    }

    public List<EdsRecurrence> getRecurrencesJob(Integer jobID, Integer busObjectId, Integer companyID) {
        if (jobID != null) {
            return (List<EdsRecurrence>) find("select rec from EdsRecurrence rec where deleted<>true and  rec.job.objectID = ? and rec.busObjectId = ? and rec.companyID=?", jobID, busObjectId, companyID);
        } else {
            return null;
        }
    }

    public EdsRecurrence getRecurrenceJob(Integer jobID, Integer busObjectId, String ruleName, Integer companyID) {
        if (jobID != null) {
            return (EdsRecurrence) findSingle("select rec from EdsRecurrence rec where deleted<>true and  rec.job.objectID = ? and rec.busObjectId = ? and rec.companyID=? and rec.ruleName=?", jobID, busObjectId, companyID, ruleName);
        } else {
            return null;
        }
    }

    public EdsRecurrence getRecurrenceJob(Integer jobID, Integer companyID) {
        if (jobID != null) {
            return (EdsRecurrence) findSingle("select rec from EdsRecurrence rec where rec.job.objectID = ? and rec.busObjectId = null and rec.companyID=?", jobID, companyID);
        } else {
            return null;
        }
    }

    public List<EdsRecurrence> getRecurrenceJobList(Integer jobID, Integer busObjectID, Integer companyID) {
        if (jobID != null) {
            return find("select rec from EdsRecurrence rec where rec.job.objectID = ? and rec.busObjectId = ? and rec.companyID = ?", jobID, busObjectID, companyID);
        } else {
            return null;
        }
    }

    public ArrayList<EdsRecurrence> getFeaturedItemsRecurrences(int jobType) {
        return (ArrayList<EdsRecurrence>) find("select rec from EdsRecurrence rec where rec.deleted<>true and rec.job.objectID=" + jobType +
                " and rec.busObjectParams is not null and rec.extendDate is not null and (to_char(rec.extendDate, 'yyyy-MM-dd') = to_char(now(), 'yyyy-MM-dd'))");
    }

    @Override
    public void nativelyRemoveRecurrence(Integer recurrenceID) {
        updateNative("update " + getPublic() + ".recurrence set changed = true, deleted = true where id = " + recurrenceID);
    }
}
