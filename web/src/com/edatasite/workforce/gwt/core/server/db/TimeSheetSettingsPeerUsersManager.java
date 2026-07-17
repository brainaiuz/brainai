package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTimeSheetSettingsPeerUsers;

import java.util.List;

/**
 * User: Ilhombek
 * Date: 20.07.2010
 * Time: 15:48:41
 */
public interface TimeSheetSettingsPeerUsersManager extends Manager<EdsTimeSheetSettingsPeerUsers> {

    EdsTimeSheetSettingsPeerUsers getTimeSheetSettingsPeerUsers(Integer timesheetSettingsId, Integer userId);

    List<EdsTimeSheetSettingsPeerUsers> getTimeSheetSettingsPeerUsers(Integer timesheetSettingsID);
}
