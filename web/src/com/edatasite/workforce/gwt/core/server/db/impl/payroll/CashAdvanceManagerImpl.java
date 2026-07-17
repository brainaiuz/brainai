package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CashAdvanceManager;
import com.edatasite.workforce.gwt.payroll.client.rpc.CashAdvanceReportItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 02.08.14
 * Time: 0:34
 * To change this template use File | Settings | File Templates.
 */
@Repository("cashAdvanceManager")
public class CashAdvanceManagerImpl extends BaseManager<EdsCashAdvance> implements CashAdvanceManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public CashAdvanceManagerImpl() {
        super(EdsCashAdvance.class);
    }

    @Override
    public List<EdsCashAdvance> getCashAdvancedList(ListingFilterParameter lfp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select  ca from EdsCashAdvance ca where " + ServerUtils.checkForDeleted("ca.deleted"));
        if (lfp.getSqlSearchKey() != null) {
            sql.append(" and (lower(ca.approver.firstName) like '" + lfp.getSqlSearchKey() + "'");
            sql.append(" or lower(ca.employee.firstName) like '" + lfp.getSqlSearchKey() + "'");
            sql.append(" or lower(ca.employee.profile.employeeCode) like '" + lfp.getSqlSearchKey() + "'");
            sql.append(" or lower(ca.status.name) like '" + lfp.getSqlSearchKey() + "') ");
        }
        if (lfp.getYear() != null) {
            sql.append(" and extract(year from requestDate)=" + lfp.getYear() + " ");
        }
        if (lfp.getSortField() != null) {
            String code = lfp.getSortField();
            if ("employee".equals(code)) {
                sql.append(" order by ca.employee ");
            } else if ("date".equals(code)) {
                sql.append(" order by ca.requestDate ");
            } else if ("approver".equals(code)) {
                sql.append(" order by ca.approver ");
            } else if ("amount".equals(code)) {
                sql.append(" order by ca.totalAmount ");
            } else if ("status".equals(code)) {
                sql.append(" order by ca.status ");
            }
            sql.append(!lfp.isAscending() ? " desc " : " ");
        }
        return findInterval(sql.toString(), lfp.getStart(), lfp.getLimit());
    }

    @Override
    public Integer getCashAdvanceCount() {
        return find("select ca from EdsCashAdvance ca where " + ServerUtils.checkForDeleted("deleted")).size();
    }

    @Override
    public List<Integer> getCashAdvanceIdsByIds(String IDs) {
        return find("select ca.objectID from EdsCashAdvance ca where ca.objectID in (" + IDs + ") and " + ServerUtils.checkForDeleted("ca.deleted"));
    }

    @Override
    public List<EdsCashAdvance> getCashAdvanceByIds(List<Integer> IDs) {
        Map<String, Object> params = new HashMap<>();
        params.put("ids", IDs);

        StringBuilder query = new StringBuilder();
        query.append("select ca from EdsCashAdvance ca where ca.objectID in (:ids) and ").append(ServerUtils.checkForDeleted("ca.deleted"));
        return findByNamedParams(query.toString(), params);
    }

    @Override
    public List<Integer> getCashAdvanceIdsWithLimit(Integer startat, Integer limit) {
        return findLimited("select ca.objectID from EdsCashAdvance ca where ca.objectID > ? and " + ServerUtils.checkForDeleted("ca.deleted") + " order by ca.objectID", limit, startat);
    }

    @Override
    public List<Integer> getCompanyDeletedCashAdvanceListForSolr(SolrReindexRpc solrReindex) {
        return find("select ca.objectID from EdsCashAdvance ca where ca.deleted=true and ca.lastUpdateTime>='" + solrReindex.getLastUpdateTime() + "'"
                + (solrReindex.getLastUpdateEndTime() != null ? " and ca.lastUpdateTime<='" + solrReindex.getLastUpdateEndTime() + "'" : ""));
    }

    @Override
    public List<EdsCashAdvance> getCashAdvanceListForSolr(SolrReindexRpc solrReindex, int startat, int limit) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select ca from EdsCashAdvance ca where (ca.deleted is null or ca.deleted is false) ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("updatedDate", solrReindex.getLastUpdateTime());
            sqlQuery.append(" and ca.lastUpdateTime >= :updatedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sqlQuery.append(" and ca.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sqlQuery.append(" order by ca.objectID ASC ");
        return findIntervalByNamedParams(sqlQuery.toString(), startat, limit, params);
    }

    @Override
    public Integer getCashAdvanceReportItemsCount(ListingFilterParameter lfp) {
        String sql = "select count(ca.id) from " + getCompanyId() + ".cashadvance ca" +
                     "  INNER JOIN " + getCompanyId() + ".paymentdeduction pd on pd.cashadvanceid = ca.id" +
                     "  INNER JOIN " + getCompanyId() + ".employee e on ca.employee_id = e.id " +
                     "  where (ca.deleted is null or ca.deleted <> true) ";

        if (lfp.getEmployeeId() != null) {
            sql += "      and e.id = :employeeId";
        }
        if (lfp.getPayrollBatchID() != null && lfp.getPayrollBatchID() != 0) {
            sql += "    and e.id in (select eb.emp_id from " + getCompanyId() + ".emp_batch eb  where eb.batch_id=:payrollBatchId) ";
        }
        if (lfp.getCategoryID() != null && lfp.getCategoryID() != 0) {
            sql += "and ca.category_id = :categoryId ";
        }
        sql += "      and ca.requestDate between :startDate and :endDate";
        Query query = slaveEntityManager.createNativeQuery(sql)
                                   .setMaxResults(1)
                                   .setParameter("startDate", lfp.getStartDate())
                                   .setParameter("endDate", lfp.getEndDate());

        if (lfp.getEmployeeId() != null) {
            query = query.setParameter("employeeId", lfp.getEmployeeId());
        }
        if (lfp.getCategoryID() != null && lfp.getCategoryID() != 0) {
            query = query.setParameter("categoryId", lfp.getCategoryID());
        }
        if (lfp.getPayrollBatchID() != null && lfp.getPayrollBatchID() != 0) {
            query = query.setParameter("payrollBatchId", lfp.getPayrollBatchID());
        }
        List<BigInteger> list = query.getResultList();

        return list.isEmpty() ? 0 : list.get(0).intValue();
    }

    @Override
    public List<CashAdvanceReportItem> getCashAdvanceReportItems(ListingFilterParameter lfp) {
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-d  HH:mm:ss");
        StringBuilder sql = new StringBuilder();
        sql.append("select ")
           .append("    t.employeeName as employeeName, ")
           .append("    t.employeeCode, ")
           .append("    t.date, ")
           .append("    t.amount, ")
           .append("    t.paidAmount as paidAmount, ")
           .append("    (t.amount - t.paidAmount) as remainingAmount ")
           .append("    from (")
           .append("        select ")
           .append("            mu.firstname ||' '|| mu.lastname as employeeName , ")
           .append("            ep.employeeCode as employeeCode, ")
           .append("            ca.requestdate as date, ")
           .append("            ca.totalamount as amount, ")
           .append("            coalesce((select sum(payment_total) from ").append(getCompanyId()).append(".payslip_payments where payment_deduction_id=pd.id ), 0.00) as paidAmount ")
           .append("    from ").append(getCompanyId()).append(".employee e ")
           .append("        left join ").append(getCompanyId()).append(".employeeprofile ep on e.profileId = ep.id ")
           .append("        left join ").append(getCompanyId()).append(".myuser mu on mu.id = e.id ")
           .append("        inner join ").append(getCompanyId()).append(".cashadvance ca on ca.employee_id = e.id ")
           .append("        inner join ").append(getCompanyId()).append(".paymentdeduction pd on pd.cashadvanceid = ca.id ")
           .append("    where ").append(ServerUtils.checkForDeleted("ca.deleted"));

        if (lfp.getEmployeeId() != null) {
            sql.append(" and e.id=").append(lfp.getEmployeeId());
        }
        if (lfp.getPayrollBatchID() != null && lfp.getPayrollBatchID() != 0) {
            sql.append(" and e.id in (select eb.emp_id ");
            sql.append(" from ").append(getCompanyId()).append(".emp_batch eb ");
            sql.append(" where eb.batch_id=").append(lfp.getPayrollBatchID()).append(")");
        }
        if (lfp.getCategoryID() != null && lfp.getCategoryID() != 0) {
            sql.append(" and ca.category_id=").append(lfp.getCategoryID());
        }
        sql.append(" and ca.requestdate between '")
           .append(dformat.format(lfp.getStartDate()))
           .append("' AND '")
           .append(dformat.format(lfp.getEndDate()))
           .append("'")
           .append("    order by ca.id desc ")
           .append(") t")
           .append(" limit ").append(lfp.getLimit())
           .append(" offset ").append(lfp.getStart() == null || lfp.getStart() == 0 ? 0 : lfp.getStart());

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(CashAdvanceReportItem.class));
    }

    @Override
    public BigDecimal getCashAdvanceAppliedAmount(ListingFilterParameter lfp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(" (SELECT sum(payment_total) FROM ").append(getCompanyId()).append(".payslip_payments WHERE payment_deduction_id = pd.id) AS paidAmount ")
                .append("  FROM " + getCompanyId() + ".cashadvance ca ")
                .append("  INNER JOIN " + getCompanyId() + ".paymentdeduction pd ON pd.cashadvanceid = ca.id ")
                .append("WHERE (ca.deleted <> TRUE OR ca.deleted IS NULL) ")
                .append("and (pd.deleted is null or pd.deleted <> true) ")
                .append(" AND ca.id = ").append(lfp.getObjectId());
        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public BigDecimal getCashAdvanceRemainingAmount(Integer objectId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ca.totalAmount - ").append(" coalesce((SELECT sum(payment_total) FROM ").append(getCompanyId()).append(".payslip_payments WHERE payment_deduction_id = pd.id), 0) AS remainingAmount ")
                .append("  FROM " + getCompanyId() + ".cashadvance ca ")
                .append("  INNER JOIN " + getCompanyId() + ".paymentdeduction pd ON pd.cashadvanceid = ca.id ")
                .append("WHERE (ca.deleted <> TRUE OR ca.deleted IS NULL) ")
                .append("and (pd.deleted is null or pd.deleted <> true) ")
                .append(" AND ca.id = ").append(objectId);
        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public List<EdsCashAdvance> getCashAdvanceListByEmployeeId(Integer employeeId) {
        return (List<EdsCashAdvance>) find("select ca from EdsCashAdvance ca where ca.employee.objectID = ? and " + ServerUtils.checkForDeleted("ca.deleted"), employeeId);
    }

    @Override
    public List<EdsCashAdvance> getListByMultiCashAdvance(Integer multiCashAdvanceId) {
        return (List<EdsCashAdvance>) find("select ca from EdsCashAdvance ca where ca.multiCashAdvance.objectID = ? and " + ServerUtils.checkForDeleted("ca.deleted") + " order by ca.objectID ", multiCashAdvanceId);
    }

    @Override
    public Integer getCashAdvanceIntNumber() {
        return (Integer) findSingle("select max(intNumber) from EdsCashAdvance where (deleted is null or deleted <> true)");
    }

    @Override
    public boolean numberExists(String numberString, Integer objectId) {
        if (numberString != null && !"".equals(numberString.trim())) {
            StringBuilder sql = new StringBuilder();
            if (objectId == null) {
                sql.append("select ca.objectID from EdsCashAdvance ca where (ca.deleted is null or ca.deleted<>true) and ca.number = ?");
                return find(sql.toString(), numberString).size() > 0;
            } else {
                sql.append("select ca.objectID from EdsCashAdvance ca where (ca.deleted is null or ca.deleted<>true) and ca.number = ? and ca.objectID <> ?");
                return find(sql.toString(), numberString, objectId).size() > 0;
            }
        }
        return false;
    }

    @Override
    public boolean isCashAdvanceUsedInPayslip(Integer objectId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(pp.id) ")
           .append(" from ").append(getCompanyId()).append(".payslip_payments pp ")
           .append(" join ").append(getCompanyId()).append(".paymentdeduction pd on pd.id=pp.payment_deduction_id ")
           .append(" where pd.cashadvanceid = ").append(objectId)
           .append(" and pp.payslip_item_id is not null ");
        BigInteger total = (BigInteger) findNativeSingle(sql.toString());
        return total.intValue() > 0;
    }
}
