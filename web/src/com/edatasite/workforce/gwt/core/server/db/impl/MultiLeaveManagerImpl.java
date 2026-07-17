package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsMultiLeave;
import com.edatasite.workforce.gwt.core.server.db.MultiLeaveManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("multiLeaveManager")
public class MultiLeaveManagerImpl extends BaseManager<EdsMultiLeave> implements MultiLeaveManager {

    public MultiLeaveManagerImpl() {
        super(EdsMultiLeave.class);
    }


    @Override
    public void deleteBySickRequestForPeriodId(Integer id) {
        updateNative("DELETE FROM " + getCompanyId() + ".multi_leave " +
                "WHERE labour_period_for_sick = " + id);
    }

    @Override
    public List<EdsMultiLeave> getMultiLeaveListBySickForPeriodID(Integer periodID) {
        return (List<EdsMultiLeave>) find("select lr from EdsMultiLeave lr where lr.laborPeriod.objectID = ? ", periodID);
    }

    @Override
    public void deleteBySickRequestForSickId(Integer sickID) {
        updateNative("DELETE FROM " + getCompanyId() + ".multi_leave " +
                "WHERE childsickrequest_id = " + sickID);
    }

    @Override
    public EdsMultiLeave getBySickRequest(Integer sickID) {
        return (EdsMultiLeave) findSingle("select ml from EdsMultiLeave ml where ml.childSickRequest.objectID=?", sickID);
    }
}
