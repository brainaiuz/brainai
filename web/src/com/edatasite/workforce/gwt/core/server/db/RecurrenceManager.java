package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Anvar Akramov
 * Date: Mar 4, 2010
 * Time: 4:45:36 AM
 */
public interface RecurrenceManager extends Manager<EdsRecurrence> {

    String getCronExpression(EdsRecurrence recurrence);

    List<EdsRecurrence> getRecurrences(boolean isMailForceTrack);

    ArrayList<EdsRecurrence> getFeaturedItemsRecurrences(int jobType);

    List<EdsRecurrence> getRecurrencesByJobType(Integer jobID);

    List<EdsRecurrence> getRecurrencesByBusObjectId(Integer busObjectID);

    EdsRecurrence getRecurrencesByUser(Integer busObjectID, Integer jobID, EdsUser user);

    EdsRecurrence getRecurrenceJob(Integer jobID, Integer busObjectID, Integer companyID);

    EdsRecurrence getRecurrenceJob(Integer jobID, Integer companyID);

    List<EdsRecurrence> getRecurrenceJobList(Integer jobID, Integer busObjectID, Integer companyID);

    List<EdsRecurrence> getChangedRecurrences();

    Date getTriggerEndDate(EdsRecurrence recurrence);

    Date getTriggerEndDate(EdsRecurrence recurrence, boolean forInvoice);

    void nativelyRemoveRecurrence(Integer recurrenceID);
}