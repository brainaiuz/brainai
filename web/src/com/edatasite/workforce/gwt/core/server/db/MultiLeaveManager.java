package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsMultiLeave;

import java.util.List;

public interface MultiLeaveManager extends Manager<EdsMultiLeave> {

    void deleteBySickRequestForPeriodId(Integer id);

    List<EdsMultiLeave> getMultiLeaveListBySickForPeriodID(Integer sickID);

    void deleteBySickRequestForSickId(Integer sickID);

    EdsMultiLeave getBySickRequest(Integer sickID);
}
