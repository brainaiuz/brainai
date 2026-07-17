package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsLabourPeriod;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.LabourPeriodManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Repository("labourPeriodManager")
public class LabourPeriodManagerImpl extends BaseManager<EdsLabourPeriod> implements LabourPeriodManager {

    public LabourPeriodManagerImpl() {
        super(EdsLabourPeriod.class);
    }

    @Override
    public List<EdsLabourPeriod> periodList(ListingFilterParameter param) {
        return null;
    }

    @Override
    public List<EdsLabourPeriod> periodListByEmployeeId(Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select per.* from").append(getCompanyId()).append(".employee em join")
                .append(getCompanyId()).append(".labour_period per on em.id = per.employeeid ")
                .append("where em.id = ").append(employeeID)
                .append(" and (date_part('year', now()) + 1) >= date_part('year', per.startDate) ")
                .append("order by startdate");
        return (ArrayList<EdsLabourPeriod>) findNative(sql.toString(), EdsLabourPeriod.class);
    }

    @Override
    public Boolean isUsedEmployeeLabourPeriod(Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(sd.periodid)>0 ")
                .append(" from ").append(getCompanyId()).append(".employee em ")
                .append(" join ").append(getCompanyId()).append(".SickRequest sr on em.id = sr.employeeid ")
                .append(" join ").append(getCompanyId()).append(".SickRequestDuration sd on sr.id = sd.sickRequestID and sd.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "'")
                .append(" where em.id = ").append(employeeID);
        return (Boolean) findNativeSingle(sql.toString());

    }

    @Override
    public List<EdsLabourPeriod> sickRequestPeriods(Integer sickRequestID, boolean orderByDesc) {
        StringBuilder sql = new StringBuilder();
        sql.append("select per.* ");
        sql.append(sickRequestPeriodBase(sickRequestID, null));
        sql.append(" order by per.startdate ");
        sql.append(orderByDesc ? "desc" : "asc");
        return (ArrayList<EdsLabourPeriod>) findNative(sql.toString(), EdsLabourPeriod.class);
    }

    @Override
    public LinkedHashMap<Integer, Double> getSickDaysByPeriod(Integer sickRequestID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct per.id, " + Constants.durationDays);
        sql.append(sickRequestPeriodBase(sickRequestID, null));
        List<Object[]> result = findNative(sql.toString());
        LinkedHashMap<Integer, Double> periodDaysMap = new LinkedHashMap<>();
        for (Object[] item : result) {
            periodDaysMap.put((Integer) item[0], ((BigDecimal) item[1]).doubleValue());
        }
        return periodDaysMap;
    }

    @Override
    public List<EdsSickRequest> getSickRequestByPeriods(Integer periodID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sr.* ");
        sql.append(" from ").append(getCompanyId()).append(".SickRequest sr ");
        sql.append(" join ").append(getCompanyId()).append(".sickrequestduration s on sr.id = s.sickrequestid and s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' ");
        sql.append(" join ").append(getCompanyId()).append(".labour_period per on s.periodId = per.id ");
        sql.append(" where sr.usedExperienceDays>0 and per.id = ").append(periodID);
        return (ArrayList<EdsSickRequest>) findNative(sql.toString(), EdsSickRequest.class);
    }
    private String sickRequestPeriodBase(Integer sickRequestID, String addtionalString) {
        StringBuilder sql = new StringBuilder();
        sql.append(" from ").append(getCompanyId()).append(".SickRequest sr ")
                .append(" join ").append(getCompanyId()).append(".leave_reason sickR on sr.reason_code=sickR.code ")
                .append(" join ").append(getCompanyId()).append(".sickrequestduration s on sr.id = s.sickrequestid and s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "' ")
                .append(" join ").append(getCompanyId()).append(".labour_period per on s.periodId = per.id ")
                .append(" where sr.id = ").append(sickRequestID)
                .append(" group by per.id ");
        if (addtionalString != null) {
            sql.append(addtionalString);
        }
        return sql.toString();
    }

    @Override
    public Double getTotalTakenLeaveDaysByPeriodId(Integer periodId, boolean isApproved) {
        StringBuilder sql = new StringBuilder();
        sql.append("select " + Constants.durationDays + "  \n");
        periodDuationDaysBaseQuery(periodId, null, isApproved, null, sql);
        return ((BigDecimal) findNativeSingle(sql.toString())).doubleValue();
    }

    @Override
    public Double getTotalTakenLeaveDaysByPeriodId(Integer periodId, Integer leaveId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select " + Constants.durationDays + "  \n");
        periodDuationDaysBaseQuery(periodId, leaveId, null, null, sql);
        return ((BigDecimal) findNativeSingle(sql.toString())).doubleValue();
    }

    @Override
    public List<Object[]> getPeriodLeavesData(Integer periodId, boolean isApproved) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sr.id, " + Constants.durationDays + ", sr.startDate " + "  \n");
        periodDuationDaysBaseQuery(periodId, null, isApproved, null, sql);
        sql.append(" group by sr.id");

        return findNative(sql.toString());

    }

    @Override
    public List<Object[]> getDayTypesByPeriod(Integer sickRequestID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct per.id,s.daytype, " + Constants.durationDays);
        sql.append(sickRequestPeriodBase(sickRequestID, ", s.daytype"));
        List<Object[]> result = findNative(sql.toString());
        return result;

    }

    private void periodDuationDaysBaseQuery(Integer periodId, Integer leaveId, Boolean isApproved, Boolean isRecalculate, StringBuilder sql) {
        sql.append("FROM " + getCompanyId() + ".sickrequestduration s \n")
                .append("JOIN " + getCompanyId() + ".sickrequest sr on s.sickrequestid=sr.id  \n")
                .append("JOIN " + getCompanyId() + ".labour_period per on s.periodid=per.id  \n")
                .append("JOIN " + getCompanyId() + ".reference ref ON sr.overallstatus=ref.id \n")
                .append("JOIN " + getCompanyId() + ".leave_reason sickR ON sickR.code = sr.reason_code  \n");
        sql.append(" where per.id=").append(periodId).append(" and s.daytype<>'" + Constants.USED_ANOHTER_LEAVE_OR_RECALL + "'\n");
        if (leaveId != null) {
            if (isRecalculate != null && isRecalculate) {
                sql.append(" and sr.id != ").append(leaveId).append(" \n");
            } else {
                sql.append(" and sr.id=").append(leaveId).append(" \n");
            }
        }
        if (isApproved != null) {
            sql.append(" and ref.code<>'" + EdsSickRequest.DENIED + "'");
            if (isApproved) {
                sql.append(" and ref.code='" + EdsSickRequest.APPROVED + "'");
            }
        }
    }


    @Override
    public EdsLabourPeriod getById(Integer periodID) {
        return (EdsLabourPeriod) findSingle("SELECT per from EdsLabourPeriod per WHERE per.objectID = ?", periodID);
    }

    @Override
    public void updatePeriods(Double allowance) {
        updateNative("update " + getCompanyId() + ".labour_period set allowance=" + allowance);
    }

    @Override
    public void clearEmployeeLabourPeriod(Integer employeeID) {
        updateNative("delete from " + getCompanyId() + ".changes where period_history in (select id from " + getCompanyId() + ".labour_period where employeeid = " + employeeID + ")");
        updateNative("delete from " + getCompanyId() + ".labor_period_history where periodHistory in (select id from " + getCompanyId() + ".labour_period where employeeid = " + employeeID + ")");
        updateNative("delete from " + getCompanyId() + ".labour_period where employeeid=" + employeeID);
    }

    @Override
    public EdsLabourPeriod getByEmployeeIdAndStartDate(Integer employeeID, String startDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT per.* from ").append(getCompanyId()).append(".labour_period per ")
                .append("join ").append(getCompanyId()).append(".employee em on per.employeeid = em.id ")
                .append("WHERE em.id = ").append(employeeID).append(" and per.startdate=TO_DATE('").append(startDate).append("', 'MM/DD/YYYY') ");
        return (EdsLabourPeriod) findNativeSingle(sql.toString(), EdsLabourPeriod.class);
    }

    @Override
    public Double getLeaveDaysByPeriodIdAndExcludeSick(Integer periodId, boolean isApproved, boolean isRecalculate, Integer requestID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select " + Constants.durationDays + "  \n");
        periodDuationDaysBaseQuery(periodId, requestID, isApproved, isRecalculate, sql);
        return ((BigDecimal) findNativeSingle(sql.toString())).doubleValue();
    }

    @Override
    public EdsLabourPeriod getPeriodByEmployeeIdAndDate(Integer employeeID, Date startDate, Date endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT lp FROM EdsLabourPeriod lp where lp.employee.id = ").append(employeeID).append(" AND ")
                .append("( lp.startDate <= '").append(startDate).append("' and '").append(endDate).append("' <= lp.endDate )");
        return (EdsLabourPeriod) findSingle(sql.toString());
    }

    @Override
    public List<EdsLabourPeriod> periodListByEmployee(Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select per.* from").append(getCompanyId()).append(".employee em join")
                .append(getCompanyId()).append(".labour_period per on em.id = per.employeeid ")
                .append("where em.id = ").append(employeeID)
                .append("order by startdate");
        return (ArrayList<EdsLabourPeriod>) findNative(sql.toString(), EdsLabourPeriod.class);
    }
}
