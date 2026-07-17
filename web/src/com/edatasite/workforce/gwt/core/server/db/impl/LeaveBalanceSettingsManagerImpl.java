package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsLeaveBalanceSettings;
import com.edatasite.workforce.gwt.core.server.db.LeaveBalanceSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("leaveBalanceSettingsManager")
public class LeaveBalanceSettingsManagerImpl extends BaseManager<EdsLeaveBalanceSettings> implements LeaveBalanceSettingsManager {
    public LeaveBalanceSettingsManagerImpl() {
        super(EdsLeaveBalanceSettings.class);
    }

    @Override
    public void deleteLeaveBalanceSettingsByReason(Integer reasonId) {
        update("delete from EdsLeaveBalanceSettings lbs where lbs.leaveType.objectID = " + reasonId);
    }

    @Override
    public void deleteNotUpdatedLeaveBalanceSettings(List<Integer> ids, Integer reasonid) {
        Map params = new HashMap();
        params.put("settingsIds", ids);
        params.put("reasonid", reasonid);
        updateByNamedParams("delete from EdsLeaveBalanceSettings lbs where lbs.leaveType.objectID = :reasonid and lbs.objectID not in (:settingsIds)", params);
    }
}
