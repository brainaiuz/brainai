package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTimeSheetApprovalSession;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 04.05.2009
 * Time: 16:17:43
 * To change this template use File | Settings | File Templates.
 */
public interface TimeSheetApprovalSessionManager extends Manager<EdsTimeSheetApprovalSession> {
    List<EdsTimeSheetApprovalSession> getList(ListingFilterParameter fp, Integer rejectedId);
    Object[] getListTimesheetApprovalList(Integer timesheetID);
    BigInteger getApprovedTimesheetHours(Integer timesheetApprovalSessionID, Integer statusId);

    Integer getTotalCount(ListingFilterParameter fp, Integer objectID);
    void deleteTimesheetApprovalSession(Integer timesheetId);

    List<EdsTimeSheetApprovalSession> getListByProjectAndEmployeeId(ListingFilterParameter fp);
}
