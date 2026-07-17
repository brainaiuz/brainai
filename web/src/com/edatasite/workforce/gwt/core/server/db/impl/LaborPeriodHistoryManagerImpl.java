package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsLaborPeriodHistory;
import com.edatasite.workforce.gwt.core.server.db.LaborPeriodHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("laborPeriodHistoryManager")
public class LaborPeriodHistoryManagerImpl extends BaseManager<EdsLaborPeriodHistory> implements LaborPeriodHistoryManager {
    public LaborPeriodHistoryManagerImpl() {
        super(EdsLaborPeriodHistory.class);
    }

    @Override
    public List<EdsLaborPeriodHistory> getComments(Integer periodId) {
        if (periodId == null) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select lph.* from ").append(getCompanyId()).append(".labor_period_history lph ")
                .append("where lph.labourPeriodId = ").append(periodId)
                .append(" and (lph.deleted is null or lph.deleted != true) ")
                .append("order by lph.creationDate desc");
        return (List<EdsLaborPeriodHistory>) findNative(sql.toString(), EdsLaborPeriodHistory.class);
    }

    @Override
    public void delete(EdsLaborPeriodHistory obj) {
        if (obj != null) {
            updateNative("update " + getCompanyId() + ".labor_period_history set deleted=true where id = " + obj.getObjectID());
        }
    }

    @Override
    public void clearHistory(Integer employeeId) {
        updateNative("delete from " + getCompanyId() + ".labor_period_history where labourPeriodId in (select id from " + getCompanyId() + ".labour_period where employeeid = " + employeeId + ")");
    }
}
