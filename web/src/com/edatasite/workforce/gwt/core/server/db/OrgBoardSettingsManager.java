package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsOrgBoardSettings;

public interface OrgBoardSettingsManager extends Manager<EdsOrgBoardSettings> {

    EdsOrgBoardSettings findSettingsByEmployee(Integer employeeId);
}
