package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.payrolluk.SalaryReportResultTransformer;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollAmountsTO;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollTotalTO;
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionContributionData;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryDetailedReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryReportItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.google.api.client.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.LR_TYPE_ANNUAL_LEAVE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.LR_TYPE_SICK_LEAVE;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 07.03.14
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
@Repository("payslipTableItemManager")
public class PayslipTableItemManagerImpl extends BaseManager<EdsPayslipTableItem> implements PayslipTableItemManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public PayslipTableItemManagerImpl() {
        super(EdsPayslipTableItem.class);
    }

    @Override
    public List<EdsPayslipTableItem> getPayslipTableItemsByTableID(Integer objectID) {
        return getPayslipTableItemsByTableID(objectID, false);
    }

    @Override
    public List<EdsPayslipTableItem> getPayslipTableItemsByTableID(Integer objectID, boolean onlyApprovedItems) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ti from EdsPayslipTableItem ti ");
        sql.append(" join ti.payslipTable t ");
        sql.append(" where (ti.deleted is null or ti.deleted = false)");
        sql.append(" and t.objectID = :payslipTableId");
        sql.append(" and ti.status.code in (:status)");

        return slaveEntityManager.createQuery(sql.toString(), EdsPayslipTableItem.class)
                .setParameter("payslipTableId", objectID)
                .setParameter("status", Arrays.asList(Constants.PAYRUN_STATUS_APPROVED, Constants.PAYRUN_STATUS_PARTIAL_PAID, Constants.PAYRUN_STATUS_PAID))
                .getResultList();
    }

    @Override
    public List<EdsPaymentDeduction> getItemCategories(Integer payslipItemID) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select pd.* from " + companyID + ".paymentdeduction pd ");
        sql.append("left join " + companyID + ".payslip_payments pp on pp.payment_deduction_id = pd.id ");
        sql.append(" where pp.payslip_item_id=" + payslipItemID);
        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public List<EdsPaymentDeduction> getItemCategories(Integer payslipItemId, Boolean forwarded) {
        if (forwarded == null) {
            return this.getItemCategories(payslipItemId);
        }
        final String sql = "select pd, sum(pp.paymentTotal) as paymenttotal from EdsPaymentDeduction pd, EdsPayslipPayments pp" +
                "    where pp.paymentDeductionID = pd.id" +
                "    and pp.payslipItemID= :payslipItemId" +
                "        and pp.forwardedPayment = :forwarded" +
                " group by pd";

        final List<Object[]> list = this.slaveEntityManager.createQuery(sql, Object[].class)
                .setParameter("payslipItemId", payslipItemId)
                .setParameter("forwarded", forwarded)
                .getResultList();

        final List<EdsPaymentDeduction> result = Lists.newArrayList();

        for (Object[] objectItem : list) {
            if (objectItem.length < 1) {
                continue;
            }
            EdsPaymentDeduction pd = (EdsPaymentDeduction) objectItem[0];
            if (objectItem.length > 1) {
                BigDecimal totalPayment = (BigDecimal) objectItem[1];
                pd.setTotalPayment(totalPayment);
            }
            result.add(pd);
        }
        return result;
    }

    @Override
    public List<EdsPaymentDeduction> getItemCategoriesByType(Integer payslipItemID, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct p.* ");
        sql.append(" from " + getCompanyId() + ".payslip_payments pc ");
        sql.append("INNER JOIN " + getCompanyId() + ".PaymentDeduction p on pc.payment_deduction_id=p.id ");
        sql.append("INNER JOIN " + getCompanyId() + ".Category c on p.categoryID=c.id ");
        sql.append(" where (c.isAdvancePayment is null or c.isAdvancePayment=false) and pc.payslip_item_id=" + payslipItemID);
        if (type != null) {
            sql.append(" and c.type='" + type + "'");
        }
        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public BigDecimal getPayslipItemPaymentsTotal(Integer payslipItemID, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct coalesce(sum(pc.payment_total), 0.00) ");
        sql.append(" from " + getCompanyId() + ".payslip_payments pc ");
        sql.append("INNER JOIN " + getCompanyId() + ".PaymentDeduction p on pc.payment_deduction_id=p.id ");
        sql.append("INNER JOIN " + getCompanyId() + ".Category c on p.categoryID=c.id ");
        sql.append(" where (c.isAdvancePayment is null or c.isAdvancePayment=false) and (p.isSalaryObject is null or p.isSalaryObject=false) and pc.payslip_item_id=" + payslipItemID);
        if (type != null) {
            sql.append(" and c.type='" + type + "'");
        }
        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public ArrayList<PensionContributionData> getPensionContributions(Integer month, Integer year) {
        StringBuilder sql = new StringBuilder();
        sql.append("select   MAX(mu.firstname ||' '||  mu.lastname) as employeeFullName, pti.month as month, pti.basicsalary as basicSalary, pti.pensionamount as employeePensionAmount, pti.companypensiontype companyPensionType, pti.companypensionrate as companyPensionRate from ").append(getCompanyId()).append(".paysliptableitem pti ");
        //sql.append("left join " + getCompanyId() + ".paysliptable pt on pt.id=pti.paysliptable_id ");
        sql.append("left join " + getCompanyId() + ".myuser mu on mu.id=pti.employee_id ");
        sql.append("left join " + getCompanyId() + ".reference r on r.id=pti.status_id ");
        sql.append("where r.code='PY_APPROVED' and ").append(ServerUtils.checkForDeleted("pti.deleted")).append(" and pti.pensionrate is not null and pti.companypensionrate is not null and pti.basicsalary is not null ");
        if (month != null) {
            sql.append(" and pti.monthid=" + month);
        }
        if (year != null) {
            sql.append(" and pti.year=" + year);
        }
        sql.append(" group by pti.month, pti.basicsalary, pti.pensionamount, pti.companypensiontype, pti.companypensionrate ");
        ArrayList<PensionContributionData> result = new ArrayList<>(jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(PensionContributionData.class)));

        return result;
    }

    @Override
    public List<EdsPayslipTableItem> getPayslipsByFilter(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p from EdsPayslipTableItem p where (p.approver is not null or p.fromEndOfService is true) and ").append(ServerUtils.checkForDeleted("p.deleted"));
        if (filterParameter.getEmployeeId() != null) {
            sql.append(" and p.employee.id=" + filterParameter.getEmployeeId());
        }
        if (filterParameter.getSqlSearchKey() != null) {
            sql.append(" and (lower(p.employee.firstName) like '" + filterParameter.getSqlSearchKey() + "'");
            sql.append(" or lower(p.approver.firstName) like '" + filterParameter.getSqlSearchKey() + "'");
            sql.append(" or lower(p.month) like '" + filterParameter.getSqlSearchKey() + "'");
            sql.append(" or lower(p.status.name) like '" + filterParameter.getSqlSearchKey() + "') ");
        }

        if (filterParameter.getSortField() != null) {
            String code = filterParameter.getSortField();
            if ("approver".equals(code)) {
                sql.append(" ORDER BY p.approver.name ");
            } else if ("preparer".equals(code)) {
                sql.append(" ORDER BY p.preparer.name ");
            } else if ("month".equals(code)) {
                sql.append(" ORDER BY p.month ");
            } else if ("status".equals(code)) {
                sql.append(" ORDER BY p.status ");
            } else {
                sql.append(" ORDER BY p.month ");
            }
            sql.append(!filterParameter.isAscending() ? " desc " : " ");
        } else {
            sql.append(" ORDER BY p.year desc, p.monthID desc");
        }

        return findInterval(sql.toString(), filterParameter.getStart(), filterParameter.getLimit());
    }

    @Override
    public Integer getPayslipsCount() {
        return find("select pt from EdsPayslipTableItem pt where pt.approver is not null and " + ServerUtils.checkForDeleted("pt.deleted")).size();
    }

    @Override
    public ArrayList<String> getPayedMonthList(Integer objectID, Integer employeeID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct monthid || ',' || year from " + getCompanyId() + ".paysliptableitem  where ").append(ServerUtils.checkForDeleted("deleted"));
        sql.append(" and employee_id=").append(employeeID);
        if (objectID != null) {
            sql.append(" and id !=").append(objectID);
        }
        return (ArrayList<String>) findNative(sql.toString());
    }

    @Override
    public List<Integer> getPayslipTableItemIdsByIds(String IDs) {
        return find("select pt.objectID from EdsPayslipTableItem pt where pt.objectID in (" + IDs + ") and (pt.deleted is null or pt.deleted <> true)");
    }

    @Override
    public List<Integer> getPayslipTableItemIdsWithLimit(Integer startat, Integer limit) {
        return findLimited("select pt.objectID from EdsPayslipTableItem pt where pt.objectID > ? and " + ServerUtils.checkForDeleted("pt.deleted") + " order by pt.objectID ASC", limit, startat);
    }

    @Override
    public List<Integer> getCompanyDeletedPayslipTableItemListForSolr(SolrReindexRpc solrReindex) {
        return find("select pt.objectID from EdsPayslipTableItem pt where pt.deleted=true and pt.lastUpdateTime>='" + solrReindex.getLastUpdateTime() + "'"
                + (solrReindex.getLastUpdateEndTime() != null ? " and pt.lastUpdateTime<='" + solrReindex.getLastUpdateEndTime() + "'" : ""));
    }

    @Override
    public List<EdsPayslipTableItem> getPayslipTableItemListForSolr(SolrReindexRpc solrReindex, int startat, int limit) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select pt from EdsPayslipTableItem pt where (pt.deleted is null or pt.deleted = false) ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("updatedDate", solrReindex.getLastUpdateTime());
            sqlQuery.append(" and pt.lastUpdateTime >= :updatedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sqlQuery.append(" and pt.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sqlQuery.append(" order by pt.objectID DESC ");
        return findIntervalByNamedParams(sqlQuery.toString(), startat, limit, params);
    }

    @Override
    public List<EdsPayslipTableItem> getPayslipTableItemList(ListingFilterParameter fp) {
        StringBuffer sqlQuery = new StringBuffer();
        sqlQuery.append("select pt ");
        getPayslipTableItemListWhereSql(sqlQuery, fp);
        sqlQuery.append(" ORDER BY pt.objectID ");
        if (Integer.valueOf(1).equals(fp.getSortDir())) {
            sqlQuery.append(" ASC");
        } else {
            sqlQuery.append(" DESC");
        }
        return findInterval(sqlQuery.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getPayslipTableItemListTotal(ListingFilterParameter fp) {
        StringBuffer sqlQuery = new StringBuffer();
        sqlQuery.append("select count(pt.objectID) ");
        getPayslipTableItemListWhereSql(sqlQuery, fp);
        Long count = (Long) findSingle(sqlQuery.toString());
        return count != null ? count.intValue() : 0;
    }

    private void getPayslipTableItemListWhereSql(StringBuffer sqlQuery, ListingFilterParameter fp) {
        EdsUser user = getUser();
        sqlQuery.append("FROM EdsPayslipTableItem pt ");
        sqlQuery.append("LEFT JOIN pt.employee emp ");
        sqlQuery.append("LEFT JOIN pt.status st ");
        sqlQuery.append(" WHERE (pt.deleted is null or pt.deleted = false) ");
        if (fp.getStatusCode() != null && !"".equals(fp.getStatusCode())) {
            sqlQuery.append(" AND (lower(st.code) ='").append(fp.getStatusCode().toLowerCase()).append("'");
            sqlQuery.append(" OR lower(st.name) ='").append(fp.getStatusCode().toLowerCase()).append("')");
        }
        if (fp.isFromMobile()) {
            if (!user.hasEitherRoles(EdsRole.ADMIN, EdsRole.DR, EdsRole.HR, EdsRole.ACCOUNTANT) && user.hasRole(EdsRole.MEM_CODE)) {
                sqlQuery.append(" AND st.code in ('").append(Constants.PAYRUN_STATUS_APPROVED).append("', '").append(Constants.PAYRUN_STATUS_PARTIAL_PAID).append("', '").append(Constants.PAYRUN_STATUS_PAID).append("')");
                sqlQuery.append(" AND emp.objectID =").append(user.getObjectID()).append("");
            }
        }
        if (fp.getSqlSearchKey() != null && !fp.getSqlSearchKey().isEmpty()) {
            sqlQuery.append(" AND (lower(st.name) like '").append(fp.getSqlSearchKey()).append("'");
            sqlQuery.append(" OR lower(st.code) like '").append(fp.getSqlSearchKey()).append("'");
            sqlQuery.append(" OR lower(emp.firstName) like '").append(fp.getSqlSearchKey()).append("'");
            sqlQuery.append(" OR lower(emp.lastName) like '").append(fp.getSqlSearchKey()).append("')");
        }
    }

    @Override
    public List<SalaryReportItem> getSalaryReportItems(ListingFilterParameter lfp) {
        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-d  HH:mm:ss");
        StringBuilder sql = new StringBuilder();
        sql.append("select mu.firstname || ' ' || mu.lastname as employeeName, pti.month, pti.year, cur.name as currency, coalesce(pti.basicSalary, 0.00) as basicSalary,\n");
        sql.append(" (select coalesce(sum(pp.payment_total), 0.00) from ").append(getCompanyId()).append(".paysliptableitem ptii\n");
        sql.append("  left join ").append(getCompanyId()).append(".payslip_payments pp on pp.payslip_item_id = ptii.id\n");
        sql.append("  left join ").append(getCompanyId()).append(".paymentdeduction pd on pd.id = pp.payment_deduction_id\n");
        sql.append("  left join ").append(getCompanyId()).append(".category c on c.id = pd.categoryid\n");
        sql.append("where").append(ServerUtils.checkForDeleted("pti.deleted")).append(" and c.type='Payment' AND pd.issalaryobject is not true\n");
        sql.append("and ptii.employee_id=e.id and ptii.id=pti.id) as allowance,");
        sql.append(" case when exr.currencyid = cur.id then coalesce(sum(exr.total),0.00) else coalesce(sum(exr.baseTotal),0) end as expensePayment,\n");
        sql.append(" 0.00 as expenseDeduction,\n");
        sql.append("coalesce(pti.deduction, 0.00) as deduction, coalesce(pti.pensionAmount, 0.00) as pensionAmount, coalesce(pti.total, 0.00) as total, ep.employeeCode from ").append(getCompanyId()).append(".employee e\n");
        sql.append("inner join ").append(getCompanyId()).append(".myuser mu on mu.id = e.id\n");
        sql.append("left join ").append(getCompanyId()).append(".employeeprofile ep on e.profileId = ep.id\n");
        sql.append("left join ").append(getCompanyId()).append(".emp_batch eb on e.id = eb.emp_id\n");
        sql.append("inner join ").append(getCompanyId()).append(".paysliptableitem pti on pti.employee_id = e.id\n");
        sql.append("left join ").append(getPublic()).append(".currency cur on pti.currency_id = cur.id \n");
        sql.append("left join ").append(getCompanyId()).append(".expensereport exr on exr.paysliptableitemid = pti.id\n");
        sql.append("where ").append(ServerUtils.checkForDeleted("pti.deleted"));
        if (lfp.getEmployeeId() != null) {
            sql.append(" and e.id=").append(lfp.getEmployeeId());
        }
        if (lfp.getPayrollBatchID() != null && lfp.getPayrollBatchID() != 0) {
            sql.append(" and eb.batch_id=").append(lfp.getPayrollBatchID());
        }
        sql.append(" and pti.todate between '").append(dformat.format(lfp.getStartDate())).append("' AND '").append(dformat.format(lfp.getEndDate())).append("'");
        sql.append("group by pti.id, e.id, mu.firstname,mu.lastname, pti.month, pti.year, cur.name, pti.basicsalary, pti.allowance, exr.paymenttype,pti.deduction, pti.pensionamount, pti.total, ep.employeeCode, cur.id, exr.currencyid ");
        sql.append("order by pti.year, pti.monthid ");

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(SalaryReportItem.class));
    }

    @Override
    public boolean isLastItemInGroupPayrun(Integer objectID) {
        return find("select pti from EdsPayslipTableItem pti where " + ServerUtils.checkForDeleted("pti.deleted") + " and pti.payslipTable.objectID=?", objectID).size() == 1;
    }

    @Override
    public BigDecimal getTotalPayToDate(Integer employeeID, Date date, Integer year) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(pti.total) from ").append(getCompanyId()).append(".paysliptableitem pti ");
        sql.append("left join " + getCompanyId() + ".reference r on r.id=pti.status_id ");
        sql.append("where r.code='PY_APPROVED' and ");
        sql.append(ServerUtils.checkForDeleted("pti.deleted"));
        sql.append(" and pti.employee_id=").append(employeeID);
        sql.append(" and pti.fromDate<='").append(date);
        sql.append("' and pti.year=").append(year);
        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public Map<Integer, BigDecimal> getRecurringCategoriesTotalByItems(Integer payslipTableId) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT\n")
                .append("  pti.id,\n")
                .append("  sum(CASE WHEN c.type = 'Payment'\n")
                .append("    THEN pp.payment_total\n")
                .append("      ELSE -1 * pp.payment_total END)\n")
                .append("FROM " + getCompanyId() + ".payslip_payments pp\n")
                .append("  JOIN " + getCompanyId() + ".paymentdeduction pd ON pp.payment_deduction_id = pd.id\n")
                .append("  JOIN " + getCompanyId() + ".category c ON pd.categoryid = c.id\n")
                .append("  JOIN " + getCompanyId() + ".paysliptableitem pti ON pp.payslip_item_id = pti.id\n")
                .append("WHERE " + ServerUtils.checkForDeleted("pd.deleted", "pti.deleted") + " AND pd.isrecurring = TRUE\n")
                .append("  AND pti.paysliptable_id = " + payslipTableId + "\n")
                .append("GROUP BY pti.id");

        List<Object[]> result = findNative(sql.toString());
        HashMap<Integer, BigDecimal> map = new HashMap<>();
        for (Object[] obj : result) {
            map.put((Integer) obj[0], (BigDecimal) obj[1]);
        }
        return map;
    }

    @Override
    public HashMap<SelectItem, SelectItem[]> getYearMonthsForWps() {
        List<Object[]> list = find("select distinct e.year, e.monthID, e.month from EdsPayslipTableItem e where (e.deleted is null or e.deleted != true) and e.year is not null and e.monthID is not null order by e.year desc, e.monthID desc");

        HashMap<SelectItem, SelectItem[]> map = new LinkedHashMap<>();
        List<SelectItem> items = null;
        Integer year = -1;
        for (Object[] objects : list) {
            if (!year.equals(objects[0])) {
                if (year != -1) {
                    map.put(new SelectItem(year, year.toString()), items.toArray(new SelectItem[]{}));
                }
                items = new ArrayList<>();
                year = (Integer) objects[0];
            }
            items.add(new SelectItem((Integer) objects[1], (String) objects[2]));
        }
        if (year != -1) map.put(new SelectItem(year, year.toString()), items.toArray(new SelectItem[]{}));
        return map;
    }

    /**
     * Object[0] payslip id
     * Object[1] payslip date
     * Object[2] payslip status code
     * Object[3] payslip status name
     * Object[4] payslip currency
     * Object[5] payslip total payments
     * Object[6] payslip total deductions
     * Object[7] payslip total expenses
     * Object[8] payslip total
     *
     * @param filterParameter contains selectedYear,employeeID params
     * @return List of Object[]
     */
    @Override
    public List<Object[]> getPayslipApiList(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT psi.id id, psi.fromDate as date,");
        sql.append(" ref.code as statusCode,ref.name as statusName,cur.name as currency,");
        sql.append(" (psi.allowance + psi.basicsalary) as payments,");
        sql.append(" psi.deduction as deductions,");
        sql.append(" psi.expense as expenses,");
        sql.append(" psi.total total,psi.employee_id employee, psi.approver_id approver");
        sql.append(" FROM ").append(getCompanyId()).append(".payslipTableItem psi");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".reference ref");
        sql.append(" ON psi.status_id = ref.id");
        sql.append(" LEFT JOIN ").append("currency cur");
        sql.append(" ON psi.currency_id = cur.id");
        sql.append(" WHERE psi.deleted is not true and psi.employee_id=").append(filterParameter.getEmployeeId());
        sql.append(" AND psi.year=").append(filterParameter.getSelectedYear());
        sql.append(" AND ref.code='").append(filterParameter.getStatusCode()).append("'");
        sql.append(" GROUP BY psi.id,ref.code,ref.name,cur.name,psi.employee_id,psi.approver_id");
        sql.append(" ORDER BY psi.fromDate DESC");
        return findNative(sql.toString());
    }

    @Override
    public PayrollTotalTO getTotalAmountGroupId(Integer groupPayrunId) {
        if (groupPayrunId == null) {
            return new PayrollTotalTO();
        }
        final String sql = "select new com.edatasite.workforce.gwt.payroll.client.rpc.PayrollTotalTO(" +
                "      coalesce(sum(ti.total), 0)," +
                "      coalesce(sum(case when r.code in (:status) then ti.total else 0 end), 0) " +
                "  )" +
                "  from EdsPayslipTableItem ti " +
                "  join ti.payslipTable t" +
                "  join ti.status r " +
                "  where (ti.deleted is null or ti.deleted = false)" +
                "      and t.objectID=:groupPayrunId";
        final List<PayrollTotalTO> list = this.masterEntityManager.createQuery(sql, PayrollTotalTO.class)
                .setParameter("groupPayrunId", groupPayrunId)
                .setParameter("status", Arrays.asList(Constants.PAYRUN_STATUS_APPROVED, Constants.PAYRUN_STATUS_PARTIAL_PAID, Constants.PAYRUN_STATUS_PAID))
                .setMaxResults(1)
                .getResultList();

        return list.isEmpty() ? new PayrollTotalTO() : list.get(0);
    }

    @Override
    public PayrollAmountsTO getTotalsByGroupId(Integer groupPayrunId) {
        if (groupPayrunId == null) {
            return new PayrollAmountsTO();
        }
        final String sql = "select new com.edatasite.workforce.gwt.payroll.client.rpc.PayrollAmountsTO(" +
                "      coalesce(sum(ti.basicSalary), 0)," +
                "      coalesce(sum(ti.allowance), 0), " +
                "      coalesce(sum(ti.pensionAmount), 0), " +
                "      coalesce(sum(ti.deduction), 0), " +
                "      coalesce(sum(ti.tax), 0), " +
                "      coalesce(sum(ti.employerContribution), 0), " +
                "      coalesce(sum(ti.expense), 0) " +
                "  )" +
                "  from EdsPayslipTableItem ti " +
                "  join ti.payslipTable t" +
                "  join ti.status r " +
                "  where (ti.deleted is null or ti.deleted = false)" +
                "      and t.objectID=:groupPayrunId";
        final List<PayrollAmountsTO> list = this.slaveEntityManager.createQuery(sql, PayrollAmountsTO.class)
                .setParameter("groupPayrunId", groupPayrunId)
                .setMaxResults(1)
                .getResultList();

        return list.isEmpty() ? new PayrollAmountsTO() : list.get(0);
    }

    @Override
    public Integer getCountByFilter(final ListingFilterParameter fp) {
        if (fp == null || fp.getGroupPayrunID() == null) {
            return 0;
        }
        String sql = "select count(ti.objectID) from EdsPayslipTableItem ti " +
                "  join ti.payslipTable t" +
                "  join ti.employee e" +
                "  left join ti.status s " +
                "  left join e.profile ep " +
                "  where (ti.deleted is null or ti.deleted = false)" +
                "      and t.objectID=:groupPayrunId";

        if (fp.getSqlSearchKey() != null) {
            sql += "      and (lower(e.firstName) like(:searchKey)" +
                    "          or lower(e.lastName) like(:searchKey)" +
                    "          or lower(e.email) like(:searchKey)" +
                    "          or lower(ep.employeeCode) like(:searchKey))";
        }
        if (!StringUtil.isEmpty(fp.getStatusCode())) {
            sql += "      and s.code = :statusCode";
        }
        TypedQuery<Long> query = this.slaveEntityManager.createQuery(sql, Long.class)
                .setParameter("groupPayrunId", fp.getGroupPayrunID())
                .setMaxResults(fp.getLimit());
        if (fp.getSqlSearchKey() != null) {
            query = query.setParameter("searchKey", fp.getSqlSearchKey());
        }
        if (!StringUtil.isEmpty(fp.getStatusCode())) {
            query = query.setParameter("statusCode", fp.getStatusCode());
        }
        final List<Long> result = query.getResultList();

        return result.isEmpty() ? 0 : result.get(0).intValue();
    }

    @Override
    public List<EdsPayslipTableItem> getListByFilter(ListingFilterParameter fp) {
        if (fp == null || fp.getGroupPayrunID() == null) {
            return Collections.emptyList();
        }
        String sql = "select ti from EdsPayslipTableItem ti " +
                "  join ti.payslipTable t" +
                "  join ti.employee e" +
                "  join ti.status r" +
                "  left join e.profile ep " +
                "  where (ti.deleted is null or ti.deleted = false)" +
                "      and t.objectID=:groupPayrunId";

        if (fp.getStatusCode() != null) {
            sql += " and r.code = '" + fp.getStatusCode() + "'";
        }

        if (fp.getSqlSearchKey() != null) {
            sql += "      and (lower(e.firstName) like(:searchKey)" +
                    "          or lower(e.lastName) like(:searchKey)" +
                    "          or lower(e.email) like(:searchKey)" +
                    "          or lower(ep.employeeCode) like(:searchKey))";
        }
        sql += "  order by e.firstName, e.lastName asc ";
        TypedQuery<EdsPayslipTableItem> query = this.slaveEntityManager.createQuery(sql, EdsPayslipTableItem.class)
                .setParameter("groupPayrunId", fp.getGroupPayrunID())
                .setFirstResult(fp.getStart());

        if (fp.getLimit() > 0 && fp.getLimit() <= 500) {
            query = query.setMaxResults(fp.getLimit());
        }
        if (fp.getSqlSearchKey() != null) {
            query = query.setParameter("searchKey", fp.getSqlSearchKey());
        }
        return query.getResultList();
    }

    @Override
    public EdsPayslipTableItem getEmployeePayslipTable(Integer employeeID, int month, int year) {
        final String sql = "select e from EdsPayslipTableItem e " +
                "   where (e.deleted is null or e.deleted != true) and " +
                "        e.employee.objectID=? and " +
                "        e.monthID=? and e.year=?";

        return (EdsPayslipTableItem) findSingle(sql, employeeID, month, year);
    }

    public ArrayList<Integer> getPendingItems(Integer payslipTableId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ti.objectID from EdsPayslipTableItem ti ");
        sql.append(" join ti.payslipTable t ");
        sql.append(" where (ti.deleted is null or ti.deleted = false)");
        sql.append(" and t.objectID = :payslipTableId");
        sql.append(" and ti.status.code = :status");

        return (ArrayList<Integer>) slaveEntityManager.createQuery(sql.toString(), Integer.class)
                .setParameter("payslipTableId", payslipTableId)
                .setParameter("status", Constants.PAYRUN_STATUS_PENDING)
                .getResultList();
    }

    @Override
    public SalaryDetailedReportData getSalaryDetailedReportItems(ListingFilterParameter lfp) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select employeeid, ");
        sql.append("         employeecode, ");
        sql.append("         employeename, ");
        sql.append("         categoryid, ");
        sql.append("         categorycode, ");
        sql.append("         categoryname, ");
        sql.append("         categorytype, ");
        sql.append("         sum(total) as total ");
        sql.append(" from ( (select pti.employee_id              as employeeid, ");
        sql.append("       ep.employeeCode                       as employeecode, ");
        sql.append("       mu.firstname || ' ' || mu.lastname    as employeename, ");
        sql.append("       c.id                                  as categoryid, ");
        sql.append("       c.code                                as categorycode, ");
        sql.append("       c.name                                as categoryname, ");
        sql.append("       c.type                                as categorytype, ");
        sql.append("       coalesce(pp.payment_total, 0.00)      as total ");
        sql.append(" from ").append(getCompanyId()).append(".payslip_payments pp ");
        sql.append("         left join ").append(getCompanyId()).append(".paysliptableitem pti on pti.id = pp.payslip_item_id ");
        sql.append("         left join ").append(getCompanyId()).append(".paymentdeduction pd on pd.id = pp.payment_deduction_id ");
        sql.append("         left join ").append(getCompanyId()).append(".category c ON c.id = pd.categoryid  ");
        sql.append("         left join ").append(getCompanyId()).append(".employee e on e.id = pti.employee_id ");
        sql.append("         left join ").append(getCompanyId()).append(".employeeprofile ep on e.profileId = ep.id ");
        sql.append("         left join ").append(getCompanyId()).append(".myuser mu on mu.id = e.id ");
        sql.append("         left join ").append(getCompanyId()).append(".reference ref on pti.status_id = ref.id ");
        sql.append(" where (pti.deleted is null or pti.deleted != true) ");
        sql.append("  and ref.code in ('").append(Constants.PAYRUN_STATUS_APPROVED).append("', '").append(Constants.PAYRUN_STATUS_PARTIAL_PAID).append("', '").append(Constants.PAYRUN_STATUS_PAID).append("') ");
        sql.append("  and pti.employee_id = :employeeid ");
        sql.append("  and pti.toDate between :startDate AND :endDate) ");

        sql.append(" union all ");

        sql.append(" (select pd.employeeid                             as employeeid, ");
        sql.append("        ep.employeeCode                            as employeecode, ");
        sql.append("        mu.firstname || ' ' || mu.lastname         as employeename, ");
        sql.append("        c.id                                       as categoryid, ");
        sql.append("        c.code                                     as categorycode, ");
        sql.append("        c.name                                     as categoryname, ");
        sql.append("        c.type                                     as categorytype, ");
        sql.append("        coalesce(pd.paymentamount, 0.00)           as total ");
        sql.append(" from ").append(getCompanyId()).append(".paymentdeduction pd ");
        sql.append("          left join ").append(getCompanyId()).append(".additionalPayment ap on ap.id = pd.addpayment_id ");
        sql.append("          left join ").append(getCompanyId()).append(".category c on c.id = pd.categoryID ");
        sql.append("          left join ").append(getCompanyId()).append(".employee e on e.id = pd.employeeid ");
        sql.append("          left join ").append(getCompanyId()).append(".employeeprofile ep on e.profileId = ep.id ");
        sql.append("          left join ").append(getCompanyId()).append(".myuser mu on mu.id = e.id ");
        sql.append("          left join ").append(getCompanyId()).append(".reference ref on ap.overallstatus = ref.id ");
        sql.append(" where (ap.deleted is null or ap.deleted != true) ");
        sql.append("   and ref.code in ('").append(Constants.PAYMENT_STATUS_APPROVED).append("', '").append(Constants.PAYMENT_STATUS_PARTIAL_PAID).append("', '").append(Constants.PAYMENT_STATUS_PAID).append("') ");
        sql.append("  and pd.employeeid = :employeeid ");
        sql.append("   and to_timestamp(ap.defaultpaymentdate / 1000) between :startDate AND :endDate");
        sql.append("   and ap.showInPaySlip is false) ");

        sql.append(" union all ");

        sql.append(" (select pd.employeeid                                                                        as employeeid, ");
        sql.append("        ep.employeeCode                                                                       as employeecode, ");
        sql.append("        mu.firstname || ' ' || mu.lastname                                                    as employeename, ");
        sql.append("        cast(c.category -> 'categoryItem' -> 'valueMap' ->> 'id' as integer)                  as categoryid, ");
        sql.append("        c.category -> 'categoryItem' ->> 'code'                                               as categorycode, ");
        sql.append("        c.category -> 'categoryItem' -> 'valueMap' ->> 'name'                                 as categoryname, ");
        sql.append("        c.category -> 'categoryItem' ->> 'type'                                               as categorytype, ");
        sql.append("        coalesce((pd.paymentamount * cast(c.category ->> 'percentage' as float)) / 100, 0.00) as subtotal ");
        sql.append(" from (select ppd.addpayment_id, ");
        sql.append("                ppd.employeeid, ");
        sql.append("                ppd.paymentamount, ");
        sql.append("                jsonb_array_elements((cast(ppd.taxcategorylist as jsonb)))                  as taxes, ");
        sql.append("                jsonb_array_elements((cast(ppd.employercontributioncategorylist as jsonb))) as contributions, ");
        sql.append("                jsonb_array_elements((cast(ppd.customdeductioncategorylist as jsonb)))      as deductions ");
        sql.append("        from ").append(getCompanyId()).append(".paymentdeduction ppd ");
        sql.append("                where (ppd.deleted is null or ppd.deleted != true) ");
        sql.append("                and ppd.addpayment_id is not null ");
        sql.append("                and ppd.employeeid = :employeeid) as pd ");
        sql.append("        left join ").append(getCompanyId()).append(".additionalPayment ap on ap.id = pd.addpayment_id ");
        sql.append("        left join ").append(getCompanyId()).append(".employee e on e.id = pd.employeeid ");
        sql.append("        left join ").append(getCompanyId()).append(".employeeprofile ep on e.profileId = ep.id ");
        sql.append("        left join ").append(getCompanyId()).append(".myuser mu on mu.id = e.id ");
        sql.append("        left join ").append(getCompanyId()).append(".reference ref on ap.overallstatus = ref.id ");
        sql.append("        cross join lateral (values (pd.taxes), (pd.contributions), (pd.deductions)) as c(category) ");
        sql.append(" where (ap.deleted is null or ap.deleted != true) ");
        sql.append("  and ref.code in ('PAYMENT_APPROVED', 'PAYMENT_PARTIAL_PAID', 'PAYMENT_PAID') ");
        sql.append("  and pd.employeeid = :employeeid ");
        sql.append("  and to_timestamp(ap.defaultpaymentdate / 1000) between :startDate AND :endDate ");
        sql.append("  and ap.showInPaySlip is false) ");

        sql.append(" union all ");

        sql.append(" (select pd.employeeid                          as employeeid, ");
        sql.append("        ep.employeeCode                         as employeecode, ");
        sql.append("        mu.firstname || ' ' || mu.lastname      as employeename, ");
        sql.append("        c.id                                    as categoryid, ");
        sql.append("        c.code                                  as categorycode, ");
        sql.append("        c.name                                  as categoryname, ");
        sql.append("        c.type                                  as categorytype, ");
        sql.append("        coalesce(ppi.payment_amount, 0.00)      as total ");
        sql.append(" from ").append(getCompanyId()).append(".payrollpaymentitem ppi ");
        sql.append("          left join ").append(getCompanyId()).append(".paymentdeduction pd on pd.id = ppi.payment_dedution_id ");
        sql.append("          left join ").append(getCompanyId()).append(".payrollpayment pp on pp.id = ppi.payroll_payment_id ");
        sql.append("          left join ").append(getCompanyId()).append(".category c on c.id = pd.categoryID ");
        sql.append("          left join ").append(getCompanyId()).append(".employee e on e.id = pd.employeeid ");
        sql.append("          left join ").append(getCompanyId()).append(".employeeprofile ep on ep.id =  e.profileId ");
        sql.append("          left join ").append(getCompanyId()).append(".myuser mu on mu.id = e.id ");
        sql.append(" where (ppi.deleted is null or ppi.deleted != true) ");
        sql.append("  and pd.employeeid = :employeeid ");
        sql.append("  and ppi.paymentdate between :startDate AND :endDate) ");

        sql.append(" union all ");

        sql.append(" (select ca.employee_id                      as employeeid, ");
        sql.append("       ep.employeeCode                       as employeecode, ");
        sql.append("       mu.firstname || ' ' || mu.lastname    as employeename, ");
        sql.append("       c.id                                  as categoryid, ");
        sql.append("       c.code                                as categorycode, ");
        sql.append("       c.name                                as categoryname, ");
        sql.append("       c.type                                as categorytype, ");
        sql.append("       coalesce(pp.payment_total, 0.00)      as total ");
        sql.append(" from ").append(getCompanyId()).append(".payslip_payments pp ");
        sql.append("         left join ").append(getCompanyId()).append(".cashAdvance ca on ca.id = pp.cashadvanceid ");
        sql.append("         left join ").append(getCompanyId()).append(".paymentdeduction pd on pd.id = pp.payment_deduction_id ");
        sql.append("         left join ").append(getCompanyId()).append(".category c ON c.id = pd.categoryid  ");
        sql.append("         left join ").append(getCompanyId()).append(".employee e on e.id = ca.employee_id ");
        sql.append("         left join ").append(getCompanyId()).append(".employeeprofile ep on e.profileId = ep.id ");
        sql.append("         left join ").append(getCompanyId()).append(".myuser mu on mu.id = e.id ");
        sql.append(" where (ca.deleted is null or ca.deleted != true) ");
        sql.append("  and ca.employee_id = :employeeid ");
        sql.append("  and pp.paymentDate between :startDate AND :endDate) ) as t ");
        sql.append("  group by employeeid, employeecode, employeename, categoryid, categorycode, categoryname, categorytype ");

        Query query = slaveEntityManager.createNativeQuery(sql.toString());

        query.setParameter("employeeid", lfp.getEmployeeId())
                .setParameter("startDate", lfp.getStartDate())
                .setParameter("endDate", lfp.getEndDate())
                .unwrap(org.hibernate.Query.class)
                .setResultTransformer(new SalaryReportResultTransformer());
        List<SalaryDetailedReportData> result = query.getResultList();

        return result != null && !result.isEmpty() ? result.get(0) : null;
    }

    @Override
    public List<EdsPaymentDeduction> getItemCategoriesByCategoryID(Integer payslipItemID, Integer categoryId) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select pd.* from " + companyID + ".paymentdeduction pd ");
        sql.append("left join " + companyID + ".payslip_payments pp on pp.payment_deduction_id = pd.id ");
        sql.append(" where pd.deleted is not true and pp.deleted is not true and pp.payslip_item_id=" + payslipItemID);
        sql.append(" and pd.categoryID=" + categoryId);
        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public BigDecimal getEmployeeAllowanceByPeriod(Integer employeeID, int month, int year, String type) {
        StringBuilder sql = new StringBuilder(" select coalesce(sum(pp.payment_total),0) from ");
        sql.append(getCompanyId()).append(".PaymentDeduction pd");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".payslip_payments pp on pd.id = pp.payment_deduction_id");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".payslipTableItem pti on pti.id = pp.payslip_item_id");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".reference ref ON pti.status_id = ref.id");
        sql.append(" LEFT JOIN ").append(getCompanyId()).append(".category c on c.id = pd.categoryID");
        sql.append(" where (ref.code='").append(Constants.PAYRUN_STATUS_APPROVED).append("' or ref.code='").append(Constants.PAYRUN_STATUS_PARTIAL_PAID).append("' or ref.code='").append(Constants.PAYRUN_STATUS_PAID).append("') ");
        sql.append(" and (pti.deleted is null or pti.deleted != true) ");
        sql.append(" and (pd.deleted is null or pd.deleted != true) ");
        sql.append(" and (pp.deleted is null or pp.deleted != true) ");
        sql.append(" and pti.employee_id =").append(employeeID);
        sql.append(" and ( pti.year >=").append(year - 1).append(" and pti.year <").append(year).append(" and pti.monthID >= ").append(month);
        sql.append(" or pti.year <=").append(year).append(" and pti.year > ").append(year - 1).append(" and pti.monthID < ").append(month).append(")");
        sql.append(" and c.type='Payment'");
        if (type != null) {
            if (LR_TYPE_SICK_LEAVE.equals(type)) {
                sql.append(" and c.excludeSickLeave is not true");
            } else if (LR_TYPE_ANNUAL_LEAVE.equals(type)) {
                sql.append(" and c.excludeAnnualLeave is not true");
            }
        }
        return (BigDecimal) findNativeSingle(sql.toString());
    }

    @Override
    public SinglePayrunItem getSinglePayrunTO(Integer id) {
        StringBuilder querry = new StringBuilder("SELECT \n")
                .append("    it.id objectID, \n")
                .append("    e.id employeeID, \n")
                .append("    CONCAT(myu.firstname, ' ', myu.lastname, ' ', COALESCE(myu.middlename, '')) AS employeename, \n")
                .append("    pr.employeeCode, \n")
                .append("    it.fromdate, \n")
                .append("    it.todate, \n")
                .append("    CASE \n")
                .append("        WHEN it.processdate IS NOT NULL THEN it.processdate \n")
                .append("        ELSE it.todate \n")
                .append("    END AS processdate, \n")
                .append("    it.daysworked, \n")
                .append("    it.basicsalary, \n")
                .append("    it.dailyrate, \n")
                .append("    it.actualmonthpay, \n")
                .append("    it.allowance, \n")
                .append("    it.additionalpay, \n")
                .append("    it.deduction, \n")
                .append("    it.tax, \n")
                .append("    it.employercontribution, \n")
                .append("    it.expense, \n")
                .append("    it.commision, \n")
                .append("    it.collection, \n")
                .append("    it.used_petrol, \n")
                .append("    it.monthlysalik, \n")
                .append("    it.description, \n")
                .append("    it.total, \n")
                .append("    it.totalinbase, \n")
                .append("    c.id AS currency_id, \n")
                .append("    c.name AS currencyName, \n")
                .append("    it.exchangeRate, \n")
                .append("    it.rejection_note, \n")
                .append("    it.sendemail, \n")
                .append("    it.pensionAmount, \n")
                .append("    it.companyPensionAmount, \n")
                .append("    it.monthID, \n")
                .append("    it.year, \n")
                .append("    it.month, \n")
                .append("    it.frequency, \n")
                .append("    st.id AS statusid, \n")
                .append("    st.name AS statusname, \n")
                .append("    st.code AS statuscode, \n")
                .append("    prp.id AS preparerid, \n")
                .append("    CONCAT(prp.firstName, ' ', prp.lastName, ' ', prp.middleName) AS preparerName, \n")
                .append("    apr.id AS approverid, \n")
                .append("    CONCAT(apr.firstName, ' ', apr.lastName, ' ', COALESCE(apr.middleName, '')) AS approverName, \n")
                .append("    it.fromEndOfService, \n")
                .append("    it.payment_policy, \n")
                .append("    it.pdftemplateid, \n")
                .append("    py.name payMethodName, \n")
                .append("    it.projectId \n")
                .append("FROM ").append(getCompanyId()).append(".payslipTableItem it \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".employee e ON it.employee_id = e.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".myuser myu ON e.id = myu.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".paymentmethod py ON it.paymentmethodid = py.id \n")
                .append("LEFT JOIN  currency c ON it.currency_id = c.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".employeeprofile pr ON e.id = pr.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".Reference st ON it.status_id = st.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".myuser prp ON it.preparer_id = prp.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".myuser apr ON it.approver_id = apr.id \n")
                .append("WHERE (it.deleted IS NULL OR it.deleted = FALSE) \n")
                .append("AND it.id = ").append(id);
        Object[] o = (Object[]) findNativeSingle(querry.toString());
        if (o == null) {
            return null;
        }
        return new SinglePayrunItem(
                (Integer) o[0], (Integer) o[1], (String) o[2], (String) o[3], (Date) o[4], (Date) o[5], (Date) o[6],
                o[7] != null ? new BigDecimal(o[7].toString()) : null,
                o[8] != null ? new BigDecimal(o[8].toString()) : null,
                o[9] != null ? new BigDecimal(o[9].toString()) : null,
                o[10] != null ? new BigDecimal(o[10].toString()) : null,
                o[11] != null ? new BigDecimal(o[11].toString()) : null,
                o[12] != null ? new BigDecimal(o[12].toString()) : null,
                o[13] != null ? new BigDecimal(o[13].toString()) : null,
                o[14] != null ? new BigDecimal(o[14].toString()) : null,
                o[15] != null ? new BigDecimal(o[15].toString()) : null,
                o[16] != null ? new BigDecimal(o[16].toString()) : null,
                o[17] != null ? new BigDecimal(o[17].toString()) : null,
                o[18] != null ? new BigDecimal(o[18].toString()) : null,
                o[19] != null ? new BigDecimal(o[19].toString()) : null,
                o[20] != null ? new BigDecimal(o[20].toString()) : null,
                (String) o[21],
                o[22] != null ? new BigDecimal(o[22].toString()) : null,
                o[23] != null ? new BigDecimal(o[23].toString()) : null,
                (Integer) o[24], (String) o[25],
                o[26] != null ? new BigDecimal(o[26].toString()) : null,
                (String) o[27],
                o[28] != null && (Boolean) o[28],
                o[29] != null ? new BigDecimal(o[29].toString()) : null,
                o[30] != null ? new BigDecimal(o[30].toString()) : null,
                (Integer) o[31], (Integer) o[32], (String) o[33],
                (Integer) o[34], (Integer) o[35], (String) o[36],
                (String) o[37], (Integer) o[38], (String) o[39],
                (Integer) o[40], (String) o[41],
                o[42] != null && (Boolean) o[42],
                (String) o[43], (Integer) o[44], (String) o[45],
                (Integer) o[46]
        );

    }


}
