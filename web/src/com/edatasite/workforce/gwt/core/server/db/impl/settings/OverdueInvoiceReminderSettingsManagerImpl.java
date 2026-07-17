package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.settings.EdsOverdueInvoiceReminderSettings;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.OverdueInvoiceReminderSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 14.12.2016 13:04
 */
@Repository("overdueInvoiceReminderSettingsManager")
public class OverdueInvoiceReminderSettingsManagerImpl extends BaseManager<EdsOverdueInvoiceReminderSettings> implements OverdueInvoiceReminderSettingsManager {
    public OverdueInvoiceReminderSettingsManagerImpl() {
        super(EdsOverdueInvoiceReminderSettings.class);
    }

    @Override
    public void deleteReminderSettingsByRecurrenceId(Integer recurrenceId) {
        updateNative("delete from " + getCompanyId() + ".overdue_invoice_reminder_settings where recurrence_id = " + recurrenceId);
    }

    @Override
    public List<EdsOverdueInvoiceReminderSettings> getReminderSettingsByRecurrenceId(Integer companyId, Integer recurrenceId) {
        return (List<EdsOverdueInvoiceReminderSettings>) findNative("select s.* from \"" + companyId + "\".overdue_invoice_reminder_settings s " +
                                                                    "   where s.recurrence_id = " + recurrenceId, EdsOverdueInvoiceReminderSettings.class);
    }
}
