package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.EdsEmployeeDocumentReminderSettings;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * User: Faxriddin Taslimov  Date: 07.10.2015
 */
public interface EmployeeDocumentReminderSettingsManager extends Manager<EdsEmployeeDocumentReminderSettings> {

    void deleteHrReminders(Integer itemId);

    List<EdsEmployeeDocumentReminderSettings> getReminders(Integer companyId);

    List<Integer> getReminderRecipentRoles(Integer reminderSettingId, Integer companyId);

    EdsFileHeader getReminderDateDocument(String s, Integer itemId, String formatDate, String endformatDate);
}
