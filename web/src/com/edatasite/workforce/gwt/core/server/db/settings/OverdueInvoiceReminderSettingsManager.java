package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.settings.EdsOverdueInvoiceReminderSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 14.12.2016 13:04
 */
public interface OverdueInvoiceReminderSettingsManager extends Manager<EdsOverdueInvoiceReminderSettings> {
    void deleteReminderSettingsByRecurrenceId(Integer recurrenceId);

    List<EdsOverdueInvoiceReminderSettings> getReminderSettingsByRecurrenceId(Integer companyId, Integer recurrenceId);
}
