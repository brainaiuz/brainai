package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsLeaveReasonHistory;

import java.util.Date;
import java.util.List;

public interface LeaveReasonHistoryManager extends Manager<EdsLeaveReasonHistory> {

    List<EdsLeaveReasonHistory> leaveReasonHistoryListByReasonId(Integer reasonID);

    EdsLeaveReasonHistory leaveReasonHistoryByReasonCode(String reasonCode, Date startDate);

    void deleteLeaveReasonHistoryById(Integer reasonID);


}
