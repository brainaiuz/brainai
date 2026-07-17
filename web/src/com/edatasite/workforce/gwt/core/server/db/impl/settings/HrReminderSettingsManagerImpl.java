package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.EdsHrReminderSettings;
import com.edatasite.workforce.core.domain.EdsHrReminderTimeAction;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.HrReminderSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 18-Aug-2010
 * Time: 14:32:02
 */
@Repository("hrReminderSettingsManager")
public class HrReminderSettingsManagerImpl extends BaseManager<EdsHrReminderSettings> implements HrReminderSettingsManager {

    public HrReminderSettingsManagerImpl() {
        super(EdsHrReminderSettings.class);
    }

    @Override
    public void deleteHrReminders() {
        List<Integer> ids = find("select hr.objectID from EdsHrReminderSettings hr");
        for (Integer objid : ids) {
            updateNative("delete from " + getCompanyId() + ".hrremindertimeaction where hrreminderid = " + objid);
            updateNative("delete from " + getCompanyId() + ".hrreminder_workflows where hrreminder_id = " + objid);
            updateNative("delete from " + getCompanyId() + ".hrreminder_role where hrreminder_id = " + objid);
            updateNative("delete from " + getCompanyId() + ".hrremindersettings where id = " + objid);
        }
    }

    @Override
    public List<EdsHrReminderSettings> getReminders(Integer companyId) {
        return (List<EdsHrReminderSettings>) findNative("select hr.id, hr.entityType, hr.fieldValue, hr.fieldcode,hr.onboardingStepId, hr.templateid from \"" + companyId + "\".hrremindersettings hr", EdsHrReminderSettings.class);
    }

    @Override
    public List<EdsHrReminderTimeAction> getReminderTimeActions(boolean isEmployeeDocReminder, Integer hrReminderId, Integer companyId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select hrac.id, hrac.actionnumber, hrac.actionperiod, hrac.actiontype  from \"" + companyId + "\".hrremindertimeaction hrac ");
        sql.append("where " + (isEmployeeDocReminder ? "hrac.employeedocumentreminderId=" : "hrac.hrreminderid=") + hrReminderId);
        return (List<EdsHrReminderTimeAction>) findNative(sql.toString(), EdsHrReminderTimeAction.class);
    }

    @Override
    public List<Integer> getReminderRecipentRoles(Integer hrReminderId, Integer companyId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select hrr.role_id  from \"" + companyId + "\".hrreminder_role hrr ");
        sql.append("where hrr.hrreminder_id=" + hrReminderId);
        return (List<Integer>) findNative(sql.toString());
    }

    @Override
    public List<Integer> getReminderWorkflowRoles(Integer hrReminderId, Integer companyId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select hrw.workflowrule_id  from \"" + companyId + "\".hrreminder_workflows hrw ");
        sql.append("where hrw.hrreminder_id=" + hrReminderId);
        return (List<Integer>) findNative(sql.toString());

    }
}
