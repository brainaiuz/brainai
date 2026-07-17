package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsLeaveReasonHistory;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("leaveReasonHistoryManager")
public class LeaveReasonHistoryManagerImpl extends BaseManager<EdsLeaveReasonHistory> implements LeaveReasonHistoryManager {

    public LeaveReasonHistoryManagerImpl() {
        super(EdsLeaveReasonHistory.class);
    }

    @Override
    public List<EdsLeaveReasonHistory> leaveReasonHistoryListByReasonId(Integer reasonID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select lrh.* from").append(getCompanyId()).append(".leave_reason_history lrh join ")
                .append(getCompanyId()).append(".leave_reason lr on lr.id = lrh.reasonId ")
                .append("where lrh.deleted is not true and lr.id = ").append(reasonID);
        return (ArrayList<EdsLeaveReasonHistory>) findNative(sql.toString(), EdsLeaveReasonHistory.class);
    }

    @Override
    public EdsLeaveReasonHistory leaveReasonHistoryByReasonCode(String reasonCode, Date startDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("select lrh.* from ").append(getCompanyId()).append(".leave_reason_history lrh ")
                .append("where lrh.deleted is not true and lrh.reason_code = ").append("'").append(reasonCode).append("'");
        if (startDate != null) {
            sql.append(" and lrh.effective_date < ").append("'").append(startDate).append("'");
        }
        sql.append(" order by lrh.modified_date desc");
        return (EdsLeaveReasonHistory) findNativeSingle(sql.toString(), EdsLeaveReasonHistory.class);
    }

    @Override
    public void deleteLeaveReasonHistoryById(Integer reasonID) {
        updateNative("UPDATE " + getCompanyId() + ".leave_reason_history SET deleted = true where id = " + reasonID);
    }
}
