package com.edatasite.workforce.gwt.profile.server.app;

import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsRecurrenceJob;
import com.edatasite.workforce.core.domain.EdsTemporaryRecurrence;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceJobManager;
import com.edatasite.workforce.gwt.core.server.db.TemporaryRecurrenceManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * User: Ilxom Lutfullaev
 * Date: 01.05.2010
 * Time: 16:13:34
 */

@Transactional
@Service("temporaryRecurrenceService")
public class TemporaryRecurrenceServiceImpl implements TemporaryRecurrenceService, SchedulerConstant, Constants {
    private static final Logger logger = LoggerFactory.getLogger(TemporaryRecurrenceServiceImpl.class);
    private final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
    @Autowired
    private RecurrenceJobManager recurrenceJobManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private TemporaryRecurrenceManager temporaryRecurrenceManager;

    @Override
    public Integer saveRecurrenceJob(RecurrenceJobItem item) {
        EdsRecurrenceJob job = null;
        if (item.getJobType() != null) {
            job = recurrenceJobManager.get(item.getJobType());
        }
        EdsUser user = recurrenceJobManager.getUser();

        boolean defaultTimeSheetReminder = item.getJobType() != null && item.getJobType() == SchedulerConstant.TIMESHEET_REMINDER && item.getDefaultReminder() != null && item.getDefaultReminder();

        EdsTemporaryRecurrence recurrence = item.getObjectId() != null ? temporaryRecurrenceManager.get(item.getObjectId()) : new EdsTemporaryRecurrence();

        wrapRecurrenceJobItemToEdsRecurrence(item, recurrence, job);
        //Override wrap method based on specific business logic, BusObjs mostly
        recurrence.setType(item.getType());
        recurrence.setCompanyID(user != null && user.getCompany() != null ? user.getCompany().getObjectID() : Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
        recurrence.setUserID(user != null ? user.getObjectID() : null);
        if (recurrence.getType() == RECURRENCE_TYPE_EVEN_DAILY || recurrence.getType() == RECURRENCE_TYPE_ODD_DAILY) {
            recurrence.setInterval(1);
            recurrence.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);
        }
        if (item.getJobType() == TIMESHEET_REMINDER || item.getJobType() == OVERDUE_INVOICE_REMINDER || item.getJobType() == SYNCHRONIZE_MAGENTO_CATALOG ||
                item.getJobType() == SYNCHRONIZE_GOOGLE_CONTACT || item.getJobType() == SYNCHRONIZE_GOOGLE_CALENDAR || item.getJobType() == SYNCHRONIZE_OFFICE_CALENDAR) {
            if (!defaultTimeSheetReminder) {
                recurrence.setBusObjectId(user.getObjectID());
            }
        } else {
            if (!defaultTimeSheetReminder) {
                recurrence.setBusObjectId(item.getBusObjectId());
            }
        }

        if (item.getJobType() == SYNCHRONIZE_GOOGLE_CALENDAR || item.getJobType() == SYNCHRONIZE_OFFICE_CALENDAR) {
            EdsCompanySystemSettings settings = companySystemSettingsManager.findByCompanyID(recurrence.getCompanyID());
            if (settings != null) {
                recurrence.setInterval(settings.getGoogleCalendarAutoSyncInterval());
            } else {
                recurrence.setInterval(60); // This is default interval = 60 minutes
            }
        }

        if (item.getObjectId() == null) {
            temporaryRecurrenceManager.create(recurrence);
        } else {
            temporaryRecurrenceManager.update(recurrence);
        }

        return recurrence.getObjectID();
    }

    @Override
    public RecurrenceJobItem createRecurrenceItemByRule(Integer recurrenceId, int jobType) {
        EdsTemporaryRecurrence recurrence = temporaryRecurrenceManager.get(recurrenceId);
        if (recurrence == null) {
            recurrence = new EdsTemporaryRecurrence();
        }
        return recurrence.createRecurrenceItem(jobType);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void wrapRecurrenceJobItemToEdsRecurrence(RecurrenceJobItem item, EdsTemporaryRecurrence recurrence, EdsRecurrenceJob job) {
        if (recurrence != null) {
            recurrence.setBusObjectId(item.getBusObjectId());
            recurrence.setType(item.getType());
            recurrence.setBusObjectParams(item.getBusObjectParams());
            recurrence.setUserTimeZone(item.getUserTimeZone());
            recurrence.setJob(job);
            recurrence.setDailyPatternOptions(item.getDailyPatternOptions());
            recurrence.setInterval(item.getInterval());
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(item.getStartDate());
            startCalendar.set(Calendar.SECOND, 0);
            startCalendar.set(Calendar.MILLISECOND, 0);
            recurrence.setStartDate(startCalendar.getTime());
            if (item.getEndDate() != null) {
                Calendar endCalendar = new GregorianCalendar();
                endCalendar.setTime(item.getEndDate());
                endCalendar.set(Calendar.SECOND, 0);
                endCalendar.set(Calendar.MILLISECOND, 0);
                recurrence.setEndDate(endCalendar.getTime());
            } else {
                recurrence.setEndDate(null);
            }
            recurrence.setOccurrence(item.getOccurrence());
            recurrence.setSunday(item.isSunday() != null && item.isSunday());
            recurrence.setMonday(item.isMonday() != null && item.isMonday());
            recurrence.setTuesday(item.isTuesday() != null && item.isTuesday());
            recurrence.setWednesday(item.isWednesday() != null && item.isWednesday());
            recurrence.setThursday(item.isThursday() != null && item.isThursday());
            recurrence.setFriday(item.isFriday() != null && item.isFriday());
            recurrence.setSaturday(item.isSaturday() != null && item.isSaturday());
            recurrence.setEndType(item.getEndType());
            recurrence.setMonthlyOrYearlyDay(item.getMonthlyOrYearlyDay());
            recurrence.setCustomPatternDay(item.getCustomPatternDay());
            recurrence.setDailyPatternOptions(item.getDailyPatternOptions());
            recurrence.setMonthlyOrYearlyPatternOption(item.getMonthlyOrYearlyPatternOption());
            recurrence.setMonthlyOrYearlyDay(item.getMonthlyOrYearlyDay());
            recurrence.setYearlyMonth(item.getYearlyMonth());
            recurrence.setToMe(item.getToMe() != null ? item.getToMe() : false);
            recurrence.setToClient(item.getToClient() != null ? item.getToClient() : false);
            recurrence.setChanged(true);
            recurrence.setAttempts(0);
            recurrence.setStatus(null);
        }
    }
}
