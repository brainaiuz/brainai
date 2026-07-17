package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsSalaryHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.SalaryHistoryManager;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("salaryHistoryManager")
public class SalaryHistoryManagerImpl extends BaseManager<EdsSalaryHistory> implements SalaryHistoryManager {
    public SalaryHistoryManagerImpl() {
        super(EdsSalaryHistory.class);
    }

    @Override
    public List<SalaryHistory> getByEmployee(ListingFilterParameter filterParameter) {
        String sql = "select new com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory(sh.objectID, e.objectID, sh.salary, sh.effectiveDate, sh.relationId, sh.relationType)" +
                "  FROM EdsSalaryHistory sh JOIN sh.employee e " +
                "  WHERE " + ServerUtils.checkForDeleted("sh.deleted") +
                "  AND e.objectID = (:employeeId) ";
        Query query = this.slaveEntityManager.createQuery(sql)
                .setParameter("employeeId", filterParameter.getEntityID());
        return query.getResultList();
    }

    public EdsSalaryHistory getEmployeeExactSalaryHistory(SalaryHistory salaryHistory) {
        String sql = "select sh from EdsSalaryHistory sh" +
                "  WHERE " + ServerUtils.checkForDeleted("sh.deleted") +
                "  AND sh.employee.objectID = :employeeId " +
                "  AND sh.relationId = :relationId " +
                "  AND sh.relationType = :relationType " +
                "  AND sh.effectiveDate = :date " +
                "  ORDER BY sh.effectiveDate desc";

        List<EdsSalaryHistory> list =  this.slaveEntityManager.createQuery(sql, EdsSalaryHistory.class)
                .setParameter("employeeId", salaryHistory.getEmployeeId())
                .setParameter("relationId", salaryHistory.getRelationId())
                .setParameter("relationType", salaryHistory.getRelationType())
                .setParameter("date", salaryHistory.getEffectiveDate().getNonConvertedDate())
                .getResultList();

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public BigDecimal getEmployeeLastSalaryHistory(Integer employeeId, Date date) {
        String sql = "select sh.salary from EdsSalaryHistory sh" +
                "  WHERE " + ServerUtils.checkForDeleted("sh.deleted") +
                "  AND sh.employee.objectID = :employeeId " +
                "  AND sh.effectiveDate <= :date " +
                "  ORDER BY sh.effectiveDate desc";

        List<BigDecimal> list =  this.slaveEntityManager.createQuery(sql, BigDecimal.class)
                .setParameter("employeeId", employeeId)
                .setParameter("date", date)
                .getResultList();

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<SalaryHistory> getEmployeeSalaryHistory(ListingFilterParameter lp) {
        if (lp.getObjectIDs() == null || lp.getObjectIDs().isEmpty()) {
            return Collections.emptyList();
        }
        String sql = "select new com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory(e.objectID, sh.salary, sh.effectiveDate)" +
                "  FROM EdsSalaryHistory sh JOIN sh.employee e " +
                "  WHERE " + ServerUtils.checkForDeleted("sh.deleted") +
                "  AND e.objectID IN (:employeeIds) ";

        if (lp.getStartDate() != null && lp.getEndDate() != null) {
            sql += " AND sh.effectiveDate between :fromDate AND :toDate";
        }

        sql += " ORDER BY sh.effectiveDate ASC";

        Query query = this.slaveEntityManager.createQuery(sql)
                .setParameter("employeeIds", lp.getObjectIDs());

        if (lp.getStartDate() != null && lp.getEndDate() != null) {
            query = query.setParameter("fromDate", lp.getStartDate());
            query = query.setParameter("toDate", lp.getEndDate());
        }
        return query.getResultList();
    }

    public List<EdsSalaryHistory> getHistories(Integer relationId, String relationType) {
        String sql = "SELECT sh FROM EdsSalaryHistory sh " +
                " WHERE " + ServerUtils.checkForDeleted("sh.deleted") +
                " AND sh.relationId = :relationId" +
                " AND sh.relationType = :relationType" +
                " ORDER BY sh.effectiveDate ASC";

        Query query = this.slaveEntityManager.createQuery(sql)
                .setParameter("relationId", relationId)
                .setParameter("relationType", relationType);
        return query.getResultList();
    }

    @Override
    public void deleteHistory(Integer relationId, String relationType) {
        update("update EdsSalaryHistory set deleted = true where relationId = " + relationId + " and relationType = '" + relationType + "'");
    }

    public HashMap<Integer, BigDecimal> getEmployeeSalaryMap(List<Integer> employeeIds, Date date) {
        String sql = "with emp as (select sh.employeeid, max(effectivedate) as effectivedate " +
                "             from " + getCompanyId() + ".salaryhistory sh " +
                "             where deleted is not true " +
                "               and effectivedate <= :effectiveDate " +
                "             group by employeeid) " +
                "select e.employeeid, sh.salary " +
                "from emp e " +
                "         join " + getCompanyId() + ".salaryhistory sh " +
                "              on (sh.employeeid = e.employeeid and sh.effectivedate = e.effectivedate and sh.deleted is not true) " +
                "where 1=1 ";
        if (employeeIds != null && !employeeIds.isEmpty()) {
            sql += "and e.employeeid in (:employeeIds)";
        }

        Query query = this.slaveEntityManager.createNativeQuery(sql)
                .setParameter("effectiveDate", date);
        if (employeeIds != null && !employeeIds.isEmpty()) {
            query.setParameter("employeeIds", employeeIds);
        }

        List<Object[]> list = query.getResultList();

        HashMap<Integer, BigDecimal> result = new HashMap<>();
        if (list == null || list.isEmpty()) {
            return result;
        }

        for (Object[] item : list) {
            Integer id = (Integer) item[0];
            BigDecimal salary = (BigDecimal) item[1];

            result.put(id, salary);
        }
        return result;
    }

    @Override
    public Map<Integer, BigDecimal> getEmployeeLastSalaryHistoryMap(List<Integer> employeeIds, Date date) {
        Map<Integer, BigDecimal> result = new HashMap<>();
        if (employeeIds == null || employeeIds.isEmpty()) {
            return result;
        }

        String sql = "select new map(" +
                        "   sh.employee.objectID as employeeId, " +
                        "   sh.salary as salary, " +
                        "   sh.effectiveDate as effectiveDate" +
                        ") " +
                        "from EdsSalaryHistory sh " +
                        "where " + ServerUtils.checkForDeleted("sh.deleted") +
                        " and sh.employee.objectID in (:employeeIds) " +
                        " and sh.effectiveDate <= :date " +
                        "order by sh.employee.objectID, sh.effectiveDate asc";

        List<Map<String, Object>> rows = this.slaveEntityManager.createQuery(sql)
                .setParameter("employeeIds", employeeIds)
                .setParameter("date", date)
                .getResultList();

        for (Map<String, Object> row : rows) {
            Integer employeeId = (Integer) row.get("employeeId");
            BigDecimal salary = (BigDecimal) row.get("salary");

            result.put(employeeId, salary);
        }

        return result;
    }
}
