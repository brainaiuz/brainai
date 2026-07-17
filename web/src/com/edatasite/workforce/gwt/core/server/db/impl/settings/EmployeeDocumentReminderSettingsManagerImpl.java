package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.EdsEmployeeDocumentReminderSettings;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.EmployeeDocumentReminderSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Faxriddin Taslimov Date: 05-10-2015
 */
@Repository("employeeDocumentReminderSettingsManager")
public class EmployeeDocumentReminderSettingsManagerImpl extends BaseManager<EdsEmployeeDocumentReminderSettings> implements EmployeeDocumentReminderSettingsManager {

    public EmployeeDocumentReminderSettingsManagerImpl() {
        super(EdsEmployeeDocumentReminderSettings.class);
    }

    @Override
    public void deleteHrReminders(Integer itemId) {
        List<Integer> ids = find("select hr.objectID from EdsEmployeeDocumentReminderSettings hr where hr.itemId =?", itemId);
        for (Integer objid : ids) {
            updateNative("delete from " + getCompanyId() + ".hrremindertimeaction where employeedocumentreminderId = " + objid);
            updateNative("delete from " + getCompanyId() + ".employeedocumentreminder_role where employeedocumentreminder_id = " + objid);
            updateNative("delete from " + getCompanyId() + ".employeedocumentremindersettings where id = " + objid);
        }
    }

    @Override
    public List<EdsEmployeeDocumentReminderSettings> getReminders(Integer companyId) {
        return (List<EdsEmployeeDocumentReminderSettings>) findNative("select hr.id, hr.entityType, hr.fieldValue, hr.fieldcode,hr.itemId, hr.templateid from \"" + companyId + "\".employeedocumentremindersettings hr", EdsEmployeeDocumentReminderSettings.class);
    }

    @Override
    public List<Integer> getReminderRecipentRoles(Integer reminderSettingId, Integer companyId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select hrr.role_id  from \"" + companyId + "\".employeedocumentreminder_role hrr ");
        sql.append("where hrr.employeedocumentreminder_id=" + reminderSettingId);
        return (List<Integer>) findNative(sql.toString());
    }

    @Override
    public EdsFileHeader getReminderDateDocument(String columnCode, Integer itemId, String reminderTimeActions, String endReminderTimeAction) {
        StringBuilder stB = new StringBuilder();
        stB.append("select fh.*, 0 as clazz_ from " + getCompanyId() + ".fileheader fh ");
        stB.append("where fh.deleted is not true and fh.id=" + itemId + " and (");
        stB.append(columnCode + "='" + reminderTimeActions + "'");
        stB.append(" OR ");
        stB.append(columnCode + " between '" + reminderTimeActions + "' and '" + endReminderTimeAction + "'");
        stB.append(")");
        return (EdsFileHeader) findNativeSingle(stB.toString(), EdsFileHeader.class);
    }
}
