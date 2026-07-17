package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLeaveBalanceSettings;

import java.util.List;

public interface LeaveBalanceSettingsManager extends Manager<EdsLeaveBalanceSettings> {
    void deleteLeaveBalanceSettingsByReason(Integer reasonId);

    void deleteNotUpdatedLeaveBalanceSettings(List<Integer> ids, Integer reasonId);

}
