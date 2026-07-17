package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTimeSheetSettingsPeerUsers;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetSettingsPeerUsersManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * User: Ilhombek
 * Date: 20.07.2010
 * Time: 15:48:10
 */
@Repository("timesheetSettingsPeerUsersManager")
public class TimeSheetSettingsPeerUsersManagerImpl extends BaseManager<EdsTimeSheetSettingsPeerUsers> implements TimeSheetSettingsPeerUsersManager {

    public TimeSheetSettingsPeerUsersManagerImpl() {
        super(EdsTimeSheetSettingsPeerUsers.class);
    }

    public EdsTimeSheetSettingsPeerUsers getTimeSheetSettingsPeerUsers(Integer timesheetSettingsId, Integer userId) {
        EdsTimeSheetSettingsPeerUsers timeSheetSettingsPeerUsers =
                (EdsTimeSheetSettingsPeerUsers) findSingle("from EdsTimeSheetSettingsPeerUsers tssm " +
                        " where tssm.deleted <> true and tssm.timeSheetSettings.objectID = ? and tssm.user.objectID = ?", timesheetSettingsId, userId);
        return Objects.requireNonNullElseGet(timeSheetSettingsPeerUsers, EdsTimeSheetSettingsPeerUsers::new);
    }

    public List<EdsTimeSheetSettingsPeerUsers> getTimeSheetSettingsPeerUsers(Integer timesheetSettingsID) {
        return find("select timesheetMembers from EdsTimeSheetSettingsPeerUsers timesheetMembers " +
                " where timesheetMembers.deleted <> true and timesheetMembers.timeSheetSettings.objectID = ? ",
                timesheetSettingsID != null ? timesheetSettingsID : 0);
    }
}
