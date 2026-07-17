package com.edatasite.workforce.gwt.profile.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 01.05.2010
 * Time: 16:13:12
 * To change this template use File | Settings | File Templates.
 */
public interface TemporaryRecurrenceService {

    Integer saveRecurrenceJob(RecurrenceJobItem item);

    RecurrenceJobItem createRecurrenceItemByRule(Integer recurrenceId, int recurringReport);
}
