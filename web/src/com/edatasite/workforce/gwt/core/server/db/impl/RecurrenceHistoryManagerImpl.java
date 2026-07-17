package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsRecurrenceHistory;
import com.edatasite.workforce.core.domain.EdsServerHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 29.03.11
 * Time: 14:43
 * To change this template use File | Settings | File Templates.
 */

@Repository("recurrenceHistoryManager")
public class RecurrenceHistoryManagerImpl extends BaseManager<EdsRecurrenceHistory> implements RecurrenceHistoryManager, SchedulerConstant {

    public RecurrenceHistoryManagerImpl() {
        super(EdsRecurrenceHistory.class);
    }

    public EdsRecurrenceHistory getRecurrenceHistory(String jobName, Date fireTime) {
        if (jobName != null && fireTime != null) {
            return (EdsRecurrenceHistory) findSingle("select rec from EdsRecurrenceHistory rec where rec.jobName=? and rec.lateFireTime=?", jobName, fireTime);
        }
        return null;
    }

    public Long getLateRecurrencesInThisSeries(EdsServerHistory serverHistory) {
        if (serverHistory != null) {
            return (Long) findSingle("select count(rec.objectID) from EdsRecurrenceHistory rec where rec.serverHistory=? and rec.isFired=false and rec.lateFireTime is not null", serverHistory);
        }
        return null;
    }

    public List<EdsRecurrenceHistory> getLateRecurrences() {
        return find("select rec from EdsRecurrenceHistory rec where rec.isFired=false and rec.lateFireTime is not null");
    }

    public List<EdsRecurrenceHistory> list(ListingFilterParameter filterParametrs) {
        StringBuilder sqlQuery = new StringBuilder("select rec from EdsRecurrenceHistory rec where ");

        if("firedRecurrenceLog".equals(filterParametrs.getFacetFilterJson())) {
            sqlQuery.append("(rec.normalFireTime between '" + filterParametrs.getStartDate() + "' and '" + filterParametrs.getEndDate() + "') and rec.lateFireTime is null and rec.isFired = true ");
        } else if("lateRecurrenceLog".equals(filterParametrs.getFacetFilterJson())) {
            sqlQuery.append("rec.lateFireTime is not null and (rec.lateFireTime between '" + filterParametrs.getStartDate() + "' and '" + filterParametrs.getEndDate() + "') and rec.isFired = true ");
        }

        if (filterParametrs.getSearchKey() != null && !"".equals(filterParametrs.getSearchKey())) {
            sqlQuery.append(" and (rec.jobType like '%" + filterParametrs.getSearchKey() + "%'");
            sqlQuery.append(" or cast(rec.companyID as string) like '%" + filterParametrs.getSearchKey()+"%'");
            sqlQuery.append(" or cast(rec.recurrenceID as string) like '%" + filterParametrs.getSearchKey()+"%')");
        }

        final String direction = filterParametrs.isAscending() ? " ASC " : " DESC ";
        final String orderBy = filterParametrs.getSortField();
        if (orderBy != null) {
            sqlQuery.append(" ORDER BY ").append(orderBy).append(direction);
        } else {
            sqlQuery.append(" ORDER BY rec.normalFireTime ").append(direction);
        }
        return find(sqlQuery.toString());
    }

    public EdsRecurrenceHistory getRecurrenceHistory(Integer recurrenceID, String date) {
        return (EdsRecurrenceHistory) findSingle("select rh from EdsRecurrenceHistory rh where rh.recurrenceID=? and " +
                "(to_char(rh.normalFireTime, 'yyyy-MM-dd')='"+date+"') or " +
                "to_char(rh.lateFireTime, 'yyyy-MM-dd')='"+date+"'))", recurrenceID);
    }
}
