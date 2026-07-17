package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsPerformanceNote;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PerformanceNoteManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Ilhombek
 * Date: 10/24/12
 * Time: 6:47 PM
 */
@Repository("performanceNoteManager")
public class PerformanceNoteManagerImpl extends BaseManager<EdsPerformanceNote> implements PerformanceNoteManager {

    public PerformanceNoteManagerImpl() {
        super(EdsPerformanceNote.class);
    }

    public List<EdsPerformanceNote> getList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pn.id, pn.*");
        if (fp.getSortField() != null && PerformanceNoteItem.RELATED_TO.equals(fp.getSortField())) {
            sql.append(" , relE.firstName \n");
        }
        if (fp.getSortField() != null && PerformanceNoteItem.REPORTED_BY.equals(fp.getSortField())) {
            sql.append(" , repBy.firstName \n");
        }
        if (fp.getSortField() != null && PerformanceNoteItem.RESOLVER.equals(fp.getSortField())) {
            sql.append(" , res.firstName \n");
        }
        sql.append("FROM").append(getCompanyId()).append(".performanceNote pn \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".myUser relE ON (relE.id = pn.relatedTo_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".teamEmployee ed ON (relE.id = ed.employeeid and ed.isdeleted is not true ) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".team tm ON (ed.teamid = tm.id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".myUser repBy ON (repBy.id = pn.reportedBy_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".myUser res ON (res.id = pn.resolver_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".reference st ON (st.id = pn.status_id) \n");
        sql.append("WHERE pn.deleted is not true \n");

        if (!ServerUtils.hasPermission(PermissionConstants.HRMS_SEE_ALL_INCIDENT)) {
            if (ServerUtils.hasPermission(PermissionConstants.HRMS_SEE_OWN_INCIDENT)) {
                sql.append(" AND (relE.id=").append(getUser().getObjectID());
                sql.append(" OR repBy.id=").append(getUser().getObjectID());
                sql.append(" OR tm.leaderid = ").append(getUser().getObjectID()).append(")");
            } else {
                sql.append(" AND relE.id=").append(getUser().getObjectID());
            }
        }

        //filter incident
        if (fp.isIncident()) {
            if (!fp.isAllByFilter() && (fp.getEmployeeId() == null || fp.getEmployeeId() == 0)) {
                EdsUser currentUser = getUser();
                fp.setEmployeeId(currentUser.getObjectID());
            }

            sql.append(" AND pn.isIncident is true \n");
        } else {
            sql.append(" AND pn.isIncident is not true \n");
        }
        //filter by employee ID
        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            sql.append(" AND relE.id=").append(fp.getEmployeeId()).append(" AND relE.deleted is not true \n");
        }
        //filter by Year
        if (fp.getYear() != null) {
            sql.append(" AND ( date_part('year',pn.startDate)=").append(fp.getYear()).append(" OR date_part('year',pn.endDate)=").append(fp.getYear()).append(" ) \n");
        }

        //searching
        if (fp.getSqlSearchKey() != null) {
            sql.append(" AND (");
            sql.append(" LOWER(relE.firstName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(relE.lastName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(repBy.firstName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(repBy.lastName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(res.firstName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(res.lastName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(st.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(pn.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(pn.description) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") \n");
        }
        sql.append(" ORDER BY ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (PerformanceNoteItem.ACTION.equals(fp.getSortField()) || PerformanceNoteItem.NAME.equals(fp.getSortField())) {
                sql.append("pn.name");
            } else if (PerformanceNoteItem.DESCRIPTION.equals(fp.getSortField())) {
                sql.append("pn.description");
            } else if (PerformanceNoteItem.RELATED_TO.equals(fp.getSortField())) {
                sql.append("relE.firstName");
            } else if (PerformanceNoteItem.PERIOD.equals(fp.getSortField())) {
                sql.append("pn.startDate");
            } else if (PerformanceNoteItem.STATUS.equals(fp.getSortField())) {
                sql.append("st.name");
            } else if (PerformanceNoteItem.REPORTED_BY.equals(fp.getSortField())) {
                sql.append("repBy.firstName");
            } else if (PerformanceNoteItem.RESOLVER.equals(fp.getSortField())) {
                sql.append("res.firstName");
            } else {
                sql.append("pn.name");
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sql.append(" ASC");
                } else {
                    sql.append(" DESC");
                }
            } else {
                sql.append(" DESC");
            }
        } else {
            sql.append(" pn.lastUpdateTime DESC nulls last");
        }

        return findNative(sql.toString(), EdsPerformanceNote.class);
    }

    @Override
    public List<EdsPerformanceNote> getIncidentList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder("SELECT inc FROM EdsPerformanceNote inc");
        sql.append(" WHERE inc.isIncident = true");
        if (fp.getSelectedMonth() != null) {
            sql.append(" AND date_part('month', inc.date_start) = '" + (fp.getSelectedMonth()) + "'");
        }
        sql.append(" ORDER BY inc.objectID desc ");
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public List<EdsPerformanceNote> getIncidentListByEmployee(ListingFilterParameter fp, Integer employeeId) {
        StringBuilder sql = new StringBuilder("SELECT inc FROM EdsPerformanceNote inc");
        sql.append(" WHERE inc.isIncident = true and inc.deleted = false and inc.relatedTo = ").append(employeeId);
//        if (fp.getMonthId() != null) {
//            sql.append(" AND date_part('month', inc.date_start) = '").append(fp.getMonthId() + 1).append("'");
//        } else {
//            return new ArrayList<>();
//        }
//        if (fp.getYear() != null) {
//            sql.append(" AND date_part('year', inc.date_start) = '").append(fp.getYear()).append("'");
//        }
        sql.append(" ORDER BY inc.objectID desc ");
        List<EdsPerformanceNote> notes = (List<EdsPerformanceNote>) findInterval(sql.toString(), fp.getStart(), fp.getLimit());
        Calendar calendar = Calendar.getInstance();
        if (fp.getMonthId() != null) {
            calendar.set(Calendar.MONTH, fp.getMonthId());
            if (fp.getYear() != null) {
                calendar.set(Calendar.YEAR, fp.getYear());
            }
            return notes.stream().filter(item -> ((EdsPerformanceNote) item).getDate_start().compareTo(calendar.getTime()) < 0 && ((EdsPerformanceNote) item).getDate_end().compareTo(calendar.getTime()) > 0).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

}