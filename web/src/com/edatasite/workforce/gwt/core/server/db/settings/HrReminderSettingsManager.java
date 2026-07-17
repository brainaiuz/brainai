package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.EdsHrReminderSettings;
import com.edatasite.workforce.core.domain.EdsHrReminderTimeAction;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Faxriddin Taslimov
 * Date: 08.07.2015
 */
public interface HrReminderSettingsManager extends Manager<EdsHrReminderSettings> {

    void deleteHrReminders();

    List<EdsHrReminderSettings> getReminders(Integer companyId);

    List<EdsHrReminderTimeAction> getReminderTimeActions(boolean isEmployeeDocReminder, Integer objectID, Integer companyId);

    List<Integer> getReminderRecipentRoles(Integer objectID, Integer companyId);

    List<Integer> getReminderWorkflowRoles(Integer objectID, Integer companyId);
}
