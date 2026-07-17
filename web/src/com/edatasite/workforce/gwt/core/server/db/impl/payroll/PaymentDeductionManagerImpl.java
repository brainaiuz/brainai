package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.core.domain.payrolluk.PaymentDeductionResultTransformer;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PaymentDeductionManager;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 23.02.2009
 * Time: 18:08:38
 * To change this template use File | Settings | File Templates.
 */
@Repository("paymentDeductionManager")
public class PaymentDeductionManagerImpl extends BaseManager<EdsPaymentDeduction> implements PaymentDeductionManager, Constants {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public PaymentDeductionManagerImpl() {
        super(EdsPaymentDeduction.class);
    }

    public List<EdsPaymentDeduction> getPayslipPaymentDeductions(Integer payslipID, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct p.* ");
        sql.append(" from " + getCompanyId() + ".payslip_payments pc ");
        sql.append("INNER JOIN " + getCompanyId() + ".PaymentDeduction p on pc.payment_deduction_id=p.id ");
        sql.append("INNER JOIN " + getCompanyId() + ".Category c on p.categoryID=c.id ");
        sql.append(" where (c.isAdvancePayment is null or c.isAdvancePayment=false) and pc.payslip_id=" + payslipID);
        if (type != null) {
            sql.append(" and c.type='" + type + "'");
        }
        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    public List<EdsPaymentDeduction> getSinglePayrunCashAdvanceDeductions(Integer singlePayrunID) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.*");
        sql.append(" from " + getCompanyId() + ".payslip_payments pc ");
        sql.append("INNER JOIN " + getCompanyId() + ".PaymentDeduction p on pc.payment_deduction_id = p.id ");
        sql.append(" where pc.payslip_item_id=" + singlePayrunID + " and p.cashAdvanceID is not null ");

        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public EdsPaymentDeduction getDeductionOrLoanByCashAdvanceID(Integer objectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select pd.* from ").append(getCompanyId()).append(".PaymentDeduction pd ");
        sql.append("where pd.cashadvanceid=" + objectID + " and " + ServerUtils.checkForDeleted("pd.deleted"));
        return (EdsPaymentDeduction) findNativeSingle(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public EdsPaymentDeduction getByRecurringPayDeductionID(Integer objectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select pd.* from ").append(getCompanyId()).append(".PaymentDeduction pd ");
        sql.append("where pd.recurringpaydeductionid=" + objectID + " and " + ServerUtils.checkForDeleted("pd.deleted"));
        return (EdsPaymentDeduction) findNativeSingle(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public HashMap<Integer, BigDecimal> getEmployeeCategoriesTotal(String employeeIds, Integer categoryType) {
        HashMap<Integer, BigDecimal> result = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n")
                .append("  t.employeeid as id ,\n")
                .append("  cast(coalesce(sum(t.amount), 0.00) as numeric) as total\n")
                .append("FROM (SELECT\n" +
                        "        pd.employeeid AS employeeid,");
        if (!LOAN.equals(categoryType)) {
            sql.append("(CASE WHEN pd.pay_type = 0\n")
                    .append("          THEN\n")
                    .append("            pd.paymentamount\n")
                    .append("         ELSE\n")
                    .append("           (pd.percentage * (SELECT coalesce(cast(value AS NUMERIC), 0.00)\n")
                    .append("                             FROM").append(getCompanyId()).append(".employeepayrollsettings\n")
                    .append("                             WHERE employeeid = pd.employeeid  AND key = 'SALARY'\n")
                    .append("                             ) / 100.00)\n")
                    .append("         END)         AS amount");
        } else {
            sql.append(" (pd.totalamount - (SELECT coalesce(sum(pp.payment_total), 0.00)\n")
                    .append("                           FROM ").append(getCompanyId()).append(".payslip_payments pp\n")
                    .append("                           WHERE pp.payment_deduction_id = pd.id)) AS amount");
        }
        sql.append(" FROM ").append(getCompanyId()).append(".paymentdeduction pd\n")
                .append("        INNER JOIN ").append(getCompanyId()).append(".category c ON c.id = pd.categoryid")
                .append(" WHERE pd.isrecurring IS TRUE AND ").append(ServerUtils.checkForDeleted("pd.deleted"))
                .append(" AND pd.employeeid in (").append(employeeIds).append(")\n");
        if (PAYMENT.equals(categoryType)) {
            sql.append("AND c.type = 'Payment'\n ");
        } else if (DEDUCTION.equals(categoryType)) {
            sql.append("AND c.type = 'Deduction' AND pd.startdate is null\n");
        } else {
            sql.append("AND c.type = 'Deduction' AND pd.startdate is not null\n");
        }
        sql.append("GROUP BY pd.employeeid, pd.id, pd.pay_type, pd.percentage, pd.totalamount, pd.paymentamount) t\n").append("GROUP BY t.employeeid;");

        List<Map<String, Object>> queryResult = jdbcSpringManager.getSimpleJdbcTemplate().queryForList(sql.toString(), new HashMap<String, String>());
        for (Map<String, Object> map : queryResult) {
            for (String key : map.keySet()) {
                result.put((Integer) map.get("id"), (BigDecimal) map.get("total"));
            }
        }
        return result;
    }

    public List<EdsPaymentDeduction> getPayslipAdvancePayments(Integer payslipID) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct p.* ");
        sql.append(" from " + getCompanyId() + ".payslip_payments pc ");
        sql.append("INNER JOIN " + getCompanyId() + ".PaymentDeduction p on pc.payment_deduction_id=p.id ");
        sql.append("INNER JOIN " + getCompanyId() + ".Category c on p.categoryID=c.id ");
        sql.append(" where c.isAdvancePayment=true and pc.payslip_id=" + payslipID);
        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public BigDecimal getTotalPaymentByCategories(Integer employeeID, String categories) {
        StringBuilder sql = new StringBuilder();
        sql.append("select coalesce(sum(pd.paymentamount), 0.00) from " + getCompanyId() + ".paymentdeduction pd ");
        sql.append("left join ").append(getCompanyId()).append(".category c on c.id=pd.categoryid ");
        sql.append("where ").append(ServerUtils.checkForDeleted("pd.deleted"));
        sql.append(" and pd.employeeid=").append(employeeID).append(" and c.type='Payment'");
        sql.append(" and pd.isrecurring is true").append(" and c.code in(").append(categories).append(")");
        Object result = findNativeSingle(sql.toString());
        return result instanceof BigDecimal ? (BigDecimal) result : BigDecimal.valueOf((Double) result);
    }

    public BigDecimal getPayslipMaterialAidTotalPayments(Date startDate, Date endDate, Integer employeeID, String systemCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct coalesce(sum(pc.payment_total), 0.00) ");
        sql.append("FROM " + getCompanyId() + ".payslip_payments pc ");
        sql.append("INNER JOIN " + getCompanyId() + ".PaymentDeduction pd on pc.payment_deduction_id=pd.id ");
        sql.append("INNER JOIN " + getCompanyId() + ".Category c on pd.categoryID=c.id ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("pd.deleted"));
        sql.append("AND pd.employeeID = :employeeID ");
        sql.append("AND (pd.startDate >= :startDate AND pd.endDate <= :endDate) ");
        sql.append("AND c.type = :type ");
        if (systemCode != null) {
            sql.append("AND c.system_code = :systemCode ");
        }

        final HashMap<String, Object> params = new HashMap<>();
        params.put("type", EdsPayrollCategory.MATERIAL_AID);
        params.put("employeeID", employeeID);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        if (systemCode != null) {
            params.put("systemCode", systemCode);
        }

        return (BigDecimal) findNativeSingleByNamedParams(sql.toString(), params);
    }

    public void delete(Integer payslipID) {
        final String sql = "DELETE FROM EdsPaymentDeduction pd " + "WHERE pd.payslip.objectID=" + payslipID;
        update(sql);
    }

    public void deletePaymentOrDeduction(Integer EdsPaymentDeductionID) {
        update("UPDATE FROM EdsPaymentDeduction set deleted=true where objectID = " + EdsPaymentDeductionID);
    }

    public void deletePaymentOrDeductionsByAdditionalPaymentId(Integer additionalPayId) {
        update("DELETE FROM EdsPaymentDeduction pd where pd.additionalPayment.objectID= " + additionalPayId);
    }

    public List<EdsPaymentDeduction> getPaymentDeductionByCatogoryID(Integer categoryID) {
        StringBuffer sql = null;
        sql = new StringBuffer();
        sql.append(" select distinct p.* ");
        sql.append(" FROM " + getCompanyId() + ".payslip_payments pc ");
        sql.append("INNER JOIN " + getCompanyId() + ".PaymentDeduction p on pc.payment_deduction_id=p.id ");
        sql.append("INNER JOIN " + getCompanyId() + ".Category c ON c.id=p.categoryID");
        sql.append(" WHERE c.id=" + categoryID);
        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public EdsPaymentDeduction getEmployeeRecurringPaymentDeductionByCategory(Integer employeeId, Integer categoryId) {
        return (EdsPaymentDeduction) findSingle("select pd from EdsPaymentDeduction pd " +
                "where (pd.deleted is null or pd.deleted <> true) and (pd.isRecurring = true or pd.paymentType = '" + EPPaymentType.ADDITIONAL.name() + "') " +
                "and pd.employee.objectID = ? and pd.category.objectID = ?", employeeId, categoryId);
    }

    public Map<String, PaymentDeductionObject> getRecurringPaymentDeductionByCategoryMap(Collection<Integer> employeeIds) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return new HashMap<>();
        }
        /*if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashMap<>();
        }*/

        final String sql = "select pd.id, " +
                "pd.employeeid, " +
                "coalesce(pd.paymentAmount, 0) as paymentAmount, " +
                "coalesce(pd.percentage, 0) as percentage, " +
                "coalesce(pd.deduction, 0) as deduction, " +
                "coalesce(pd.tax, 0) as tax, " +
                "coalesce(pd.totalAmount, 0) as totalAmount, " +
                "pd.pay_type as pay_type, " +
                "pd.startDate, " +
                "pd.endDate, " +
                "c.id as categoryId, " +
                "c.name as category_name, " +
                "c.code as category_code, " +
                "c.type as category_type, " +
                "lc.id as linked_category_id, " +
                "lc.name as linked_category_name, " +
                "lc.code as linked_category_code, " +
                "lc.type as linked_category_type, " +
                "c.system_code as system_code, " +
                "c.taxable as taxable, " +
                "c.excludeInCustomDeductions as exclude_in_custom_deductions, " +
                "pd.fromAllAllowances as fromAllAllowances, " +
                "coalesce(c.nonMoneyType, false) as nonmoney " +
                "FROM " + getCompanyId() + ".paymentdeduction pd " +
                "LEFT OUTER JOIN " + getCompanyId() + ".paymentDeductionsCategories pdc on pd.id = pdc.paymentDeductionId " +
                "      LEFT JOIN " + getCompanyId() + ".category lc ON lc.id = pdc.categoryid " +
                "      LEFT JOIN " + getCompanyId() + ".category c ON c.id = pd.categoryid " +
//                "      LEFT JOIN " + getCompanyId() + ".additionalPayment ap ON pd.addpayment_id = ap.id " +
//                "      LEFT JOIN " + getCompanyId() + ".employee e on pd.employeeid = e.id " +
                "WHERE pd.employeeid in (:employeeIds) AND " +
//                "       pd.categoryid in (:categoryIds) AND " +
                "       (pd.deleted IS NULL or pd.deleted <> true) AND " +
                "       (pd.isRecurring = true or pd.paymentType = :additionalType)";
        Query query = slaveEntityManager.createNativeQuery(sql);
        query.setParameter("employeeIds", employeeIds)
//                .setParameter("categoryIds", categoryIds)
                .setParameter("additionalType", EPPaymentType.ADDITIONAL.name())
                .unwrap(org.hibernate.Query.class)
                .setResultTransformer(new PaymentDeductionResultTransformer());

        List<PaymentDeductionObject> list = query.getResultList();

        Map<String, PaymentDeductionObject> resultMap = new HashMap<>();
        for (PaymentDeductionObject paymentDeduction : list) {
            final SelectItem employee = paymentDeduction.getEmployee();
            final PaymentDeductionSelectItem category = paymentDeduction.getCategoryItem();

            if (employee == null || category == null) {
                continue;
            }
            String key = employee.getId() + "_" + category.getId();

            resultMap.put(key, paymentDeduction);
        }

        return resultMap;
    }

    @Override
    public List<EdsPaymentDeduction> getPayslipAdditionalPayments(Integer paymentID) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.*");
        sql.append(" from " + getCompanyId() + ".payslip_payments pc ");
        sql.append("INNER JOIN " + getCompanyId() + ".PaymentDeduction p on pc.payment_deduction_id = p.id ");
        sql.append(" where " + ServerUtils.checkForDeleted("p.deleted") + " and p.addpayment_id = " + paymentID);

        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public List<EdsPaymentDeduction> getBackupEmployeeAdditionalPaymentsUsedInPayslips(Integer backupsEmployeeID, Integer monthId, Integer year) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.*");
        sql.append(" from " + getCompanyId() + ".payslip_payments pc ");
        sql.append(" join " + getCompanyId() + ".paysliptableitem pi on pi.id = pc.payslip_item_id ");
        sql.append(" left join " + getCompanyId() + ".PaymentDeduction p on pc.payment_deduction_id = p.id ");
        sql.append(" left join " + getCompanyId() + ".additionalPayment ad on p.addpayment_id = ad.id ");
        sql.append(" where " + ServerUtils.checkForDeleted("p.deleted") + " and ad.backupsEmployeeId = " + backupsEmployeeID);
        if (monthId != null && year != null) {
            sql.append(" and pi.monthid = " + monthId);
            sql.append(" and pi.year = " + year);
        }

        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public Integer getPaymentDeductionIdByCashAdvance(Integer cashAdvanceId) {
        return (Integer) findNativeSingle("select id from " + getCompanyId() + ".paymentdeduction where cashadvanceid = ?", cashAdvanceId);
    }

    @Override
    public List<PaymentDeductionObject> getEmployeesPaymentDeduction(Collection<Integer> employeeIds, PayslipFilter filter) {
        if (employeeIds == null || employeeIds.isEmpty()) {
            return Collections.emptyList();
        }
        final String sql = "select pd.id, " +
                "pd.employeeid, " +
                "coalesce(pd.paymentAmount, 0) as paymentAmount, " +
                "coalesce(pd.percentage, 0) as percentage, " +
                "coalesce(pd.deduction, 0) as deduction, " +
                "coalesce(pd.tax, 0) as tax, " +
                "coalesce(pd.totalAmount, 0) as totalAmount, " +
                "pd.pay_type as pay_type, " +
                "pd.startDate, " +
                "pd.endDate, " +
                "c.id as categoryId, " +
                "c.name as category_name, " +
                "c.code as category_code, " +
                "c.type as category_type, " +
                "lc.id as linked_category_id, " +
                "lc.name as linked_category_name, " +
                "lc.code as linked_category_code, " +
                "lc.type as linked_category_type, " +
                "c.system_code as system_code, " +
                "c.taxable as taxable, " +
                "c.excludeInCustomDeductions as exclude_in_custom_deductions, " +
                "pd.fromAllAllowances as fromAllAllowances, " +
                "coalesce(c.nonMoneyType, false) as nonmoney " +

//        final String sql = "select pd, pdc.categoryId as pdc_category_id " +
                "FROM " + getCompanyId() + ".paymentdeduction pd " +
                "LEFT OUTER JOIN " + getCompanyId() + ".paymentDeductionsCategories pdc on pd.id = pdc.paymentDeductionId " +
                "      LEFT JOIN " + getCompanyId() + ".category lc ON lc.id = pdc.categoryid " +
                "      LEFT JOIN " + getCompanyId() + ".category c ON c.id = pd.categoryid " +
                "      LEFT JOIN " + getCompanyId() + ".cashadvance ca ON pd.cashadvanceid = ca.id " +
                "      LEFT JOIN " + getCompanyId() + ".reference re ON ca.overallstatus = re.id " +
                "      LEFT JOIN " + getCompanyId() + ".additionalPayment ap ON pd.addpayment_id = ap.id " +
                "      LEFT JOIN " + getCompanyId() + ".employee e on pd.employeeid = e.id " +
                "WHERE pd.employeeid in (:employeeIds) AND " +
                "        (pd.deleted IS NULL or pd.deleted <> true) AND " +
                "        pd.fullpayed IS NOT TRUE AND " +
                "        (pd.paymentamount IS NOT NULL AND " +
                "            pd.paymentamount > 0 OR " +
                "            pd.percentage IS NOT NULL AND " +
                "            pd.percentage > 0) AND " +
                "        (pd.isRecurring IS TRUE AND " +
                "            (pd.startDate IS NULL AND pd.endDate IS NULL) OR " +
                "            pd.isRecurring IS TRUE AND " +
                "            ((pd.startDate IS NULL OR pd.startDate <= :endDate) AND " +
                "            (pd.endDate IS NULL OR pd.endDate >= :startDate)) OR " +
                "            (pd.isRecurring <> TRUE OR " +
                "            pd.isRecurring IS NULL) AND " +
                "            (ap.showinpayslip IS TRUE OR ap.categoryType = 'Deduction') AND " +
                "            ap.overallstatus = (" +
                "                    SELECT r.id FROM " + getCompanyId() + ".reference r " +
                "                        WHERE r.code = :overallStatusCode" +
                "                       ) and " +
                "            pd.startDate <= :startDate AND " +
                "            pd.endDate >= :endDate) AND " +
                "        (ca.id IS NULL OR (NOT exists(" +
                "            SELECT * FROM " + getCompanyId() + ".companypayrollsettings " +
                "                WHERE key = :multicurrencyPayroll " +
                "                    AND value = 'true') OR" +
                "        ca.currency_id = e.currency_id) and re.code in ('POSTED', 'PARTIALLY_PAID'))";
        Query query = slaveEntityManager.createNativeQuery(sql);
        query.setParameter("employeeIds", employeeIds)
                .setParameter("overallStatusCode", PAYMENT_STATUS_APPROVED)
                .setParameter("multicurrencyPayroll", MULTI_CURRENCY_FOR_PAYROLL)
                .setParameter("startDate", filter.getFromDate().getNonConvertedDate())
                .setParameter("endDate", filter.getToDate().getNonConvertedDate())
                .unwrap(org.hibernate.Query.class)
                .setResultTransformer(new PaymentDeductionResultTransformer());

        return query.getResultList();
    }

    @Override
    public Integer getAdditionalPaymentCountByFilter(final ListingFilterParameter fp) {
        if (fp == null || fp.getGroupPayrunID() == null) {
            return 0;
        }
        String sql = "select count(pd.objectID) from EdsPaymentDeduction pd " +
                " left join pd.additionalPayment ad" +
                " left join pd.employee e" +
                " left join pd.status s " +
                " left join pd.category ca " +
                " left join e.profile ep " +
                " where (pd.deleted is null or pd.deleted = false)" +
                "      and ad.objectID=:additionalPaymentId";

        if (fp.getSqlSearchKey() != null) {
            sql += "      and (lower(e.firstName) like(:searchKey)" +
                    "          or lower(e.lastName) like(:searchKey)" +
                    "          or lower(e.email) like(:searchKey)" +
                    "          or lower(ep.employeeCode) like(:searchKey))";
        }
        if (!StringUtil.isEmpty(fp.getStatusCode())) {
            sql += "      and s.code = :statusCode";
        }
        if (fp.isNewType()) {
            sql += " and ca.nonMoneyType = true";
        }
        TypedQuery<Long> query = this.slaveEntityManager.createQuery(sql, Long.class)
                .setParameter("additionalPaymentId", fp.getGroupPayrunID())
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
    public List<EdsPaymentDeduction> getAdditionalPaymentItemListByFilter(ListingFilterParameter fp) {
        if (fp == null || fp.getGroupPayrunID() == null) {
            return Collections.emptyList();
        }
        String sql = "select pd from EdsPaymentDeduction pd " +
                " left join pd.additionalPayment ad" +
                " left join pd.employee e" +
                " left join pd.status r" +
                " left join e.profile ep " +
                " where (pd.deleted is null or pd.deleted = false)" +
                "      and ad.objectID=:additionalPaymentId";

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
        TypedQuery<EdsPaymentDeduction> query = this.slaveEntityManager.createQuery(sql, EdsPaymentDeduction.class)
                .setParameter("additionalPaymentId", fp.getGroupPayrunID())
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
    public List<Object> getAdditionalPaymentTotalAmount(Integer additionalPaymentId) {
        String sql = "select coalesce(sum(pd.totalAmount), 0.00) from EdsPaymentDeduction pd " +
                " left join pd.additionalPayment ad" +
                " where (pd.deleted is null or pd.deleted = false)" +
                "      and ad.objectID=:additionalPaymentId";

        Query query = this.slaveEntityManager.createQuery(sql)
                .setParameter("additionalPaymentId", additionalPaymentId);
        return query.getResultList();
    }

    @Override
    public EdsPaymentDeduction getPredefinedPaymentDeduction(Integer employeeId, Integer categoryId, EPPaymentType paymentType) {
        return (EdsPaymentDeduction) findSingle("select pd from EdsPaymentDeduction pd " +
                "where (pd.deleted is null or pd.deleted <> true) " +
                "and pd.employee.objectID = ? and pd.category.objectID = ? and pd.paymentType = ? ", employeeId, categoryId, paymentType);
    }

    @Override
    public EdsPaymentDeduction getPreviousPaymentDeductionByEffectiveDate(Integer employeeId, Integer categoryId, Date effectiveDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("select pd from EdsPaymentDeduction pd ");
        sql.append("where ").append(ServerUtils.checkForDeleted("pd.deleted"));
        sql.append("and pd.employee.objectID = :employeeID ");
        sql.append("and pd.category.objectID = :categoryID ");
        sql.append("and pd.isRecurring is true ");
        if (effectiveDate != null) {
            sql.append("and pd.startDate is null or pd.startDate < :startDate ");
        }
        sql.append("order by pd.startDate desc");

        final HashMap<String, Object> params = new HashMap<>();
        params.put("employeeID", employeeId);
        params.put("categoryID", categoryId);
        if (effectiveDate != null) {
            params.put("startDate", effectiveDate);
        }

        return (EdsPaymentDeduction) findSingleByNamedParams(sql.toString(), params);
    }

    @Override
    public EdsPaymentDeduction getNextPaymentDeductionByEffectiveDate(Integer employeeId, Integer categoryId, Date effectiveDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("select pd from EdsPaymentDeduction pd ");
        sql.append("where ").append(ServerUtils.checkForDeleted("pd.deleted"));
        sql.append("and pd.employee.objectID = :employeeID ");
        sql.append("and pd.category.objectID = :categoryID ");
        sql.append("and pd.isRecurring is true ");
        sql.append("and pd.startDate > :startDate ");
        sql.append("order by pd.startDate asc");

        final HashMap<String, Object> params = new HashMap<>();
        params.put("employeeID", employeeId);
        params.put("categoryID", categoryId);
        params.put("startDate", effectiveDate);

        return (EdsPaymentDeduction) findSingleByNamedParams(sql.toString(), params);
    }

    @Override
    public List<EdsPaymentDeduction> getEmpployeePaymentDeductions(Integer employeeId, boolean payment) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pd.* from ").append(getCompanyId()).append(".paymentdeduction pd where (pd.deleted is null or pd.deleted <> true) and pd.employeeid = ")
                .append(employeeId).append(" or pd.candidateid = ").append(employeeId);

        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public List<EdsPaymentDeduction> getPaymentsByEffectiveDate(Integer employeeId, Date effectiveDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pd from ").append("EdsPaymentDeduction pd where (pd.deleted is null or pd.deleted <> true) ");
        sql.append("and pd.category.type = 'Payment' ");
        sql.append("and (pd.isRecurring is true or paymentType = 'ADDITIONAL')");
        sql.append("and (pd.employee.objectID = :employeeId").append(" or pd.candidate.objectID = :employeeId").append(") ");
        sql.append("and pd.startDate < :startDate and (pd.endDate > :startDate or pd.endDate is null) ");

        final HashMap<String, Object> params = new HashMap<>();
        params.put("employeeId", employeeId);
        params.put("startDate", effectiveDate);
        return findByNamedParams(sql.toString(), params);
    }


}
