package com.edatasite.workforce.gwt.profile.server.app;

import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsRecurrenceHistory;
import com.edatasite.workforce.core.domain.EdsRecurrenceJob;
import com.edatasite.workforce.core.domain.EdsServerHistory;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsRecurringBill;
import com.edatasite.workforce.core.domain.accounting.EdsRecurringInvoice;
import com.edatasite.workforce.gwt.backend.client.rpc.RecurrenceLogItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import org.quartz.impl.triggers.AbstractTrigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 01.05.2010
 * Time: 16:13:12
 * To change this template use File | Settings | File Templates.
 */
public interface RecurrenceService {

    Integer saveRecurrenceJob(RecurrenceJobItem item);

    RecurrenceJobItem getJob(Integer jobType);

    RecurrenceJobItem getJob(Integer jobType, boolean defaultTimeSheetReminder);

    RecurrenceJobItem createRecurrenceItemByUser(Integer busObjectId, Integer jobID, EdsUser user);

    void updateRecurrence(EdsRecurrence recurrence, boolean changed, boolean deleted);

    void updateRecurrence(Integer recurrenceId, boolean changed, boolean deleted);

    RecurrenceJobItem getRecurringInvoiceRecurrenceItem(EdsRecurringInvoice saleInvoice);

    RecurrenceJobItem getRecurringBillRecurrenceItem(EdsRecurringBill edsRecurringBill);

    RecurrenceJobItem getRecurringManualJournalRecurrenceItem(EdsManualJournal edsManualJournal);

    void deleteInvoiceRecurrenceIfExists(Integer invoiceID);

    Map<Integer, Date> getNextFireTimesAsMap(Integer[] busObjectIDArray, Integer jobType);

    String getRecurrenceTemplateString(Integer busObjectId, Integer jobType);

    EdsRecurrence getRecurrence(Integer objectId);

    EdsRecurrence getRecurrenceByJobId(Integer busObjectID, Integer jobType);

    List<Date> getRecurringDates(EdsRecurrence recurrence);

    void wrapRecurrenceJobItemToEdsRecurrence(RecurrenceJobItem item, EdsRecurrence recurrence, EdsRecurrenceJob job);

    void deleteRecurrence(Integer busObjectID, Integer jobType);

    void createRecurrenceLog();

    void updateRecurrenceLog();

    Integer createLateRecurrence(EdsRecurrenceHistory recurrenceHistory);

    void updateLateRecurrence(String jobName, Date fireTime);

    void fireLateRecurrences();

    Integer registerRecurrence(EdsServerHistory serverHistory, String jobDetailName, Date normalFireTime, Date lateFireTime, String jobType, String cronExpression, Integer recurrenceID, Integer companyID, boolean isFired);

    EdsServerHistory getLastServerHistory();

    void updateRecurrenceHistory(Integer recurrenceHistoryID);

    ArrayList<RecurrenceLogItem> getRecurrenceJobItems();

    void reLoadTrigger(EdsRecurrence recurrence);

    void setRecurrenceStatus(Integer recurrenceID, String status);

    AbstractTrigger getRecurrenceCronTrigger(EdsRecurrence recurrence);

    void removeTriggerFromScheduler(Integer recurrenceID);

    EdsRecurrenceHistory getRecurrenceHistory(Integer objectID, String date);

    void nativelyRemoveRecurrence(Integer recurrenceID);

    RecurrenceJobItem getJob();

    RecurrenceJobItem createRecurrenceItemByRule(Integer recurrenceId, int jobType);

    ArrayList<String> deleteTelegramRecurrenceRule(Integer ruleId);

    void deleteRecurrences(Integer busObjectID, Integer jobType);

}
