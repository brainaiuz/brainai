package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableManager;
import com.edatasite.workforce.gwt.core.server.rpc.GroupPayrunItems;
import com.edatasite.workforce.gwt.core.server.rpc.JsonResult;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipFilter;
import com.edatasite.workforce.gwt.payroll.client.rpc.WpsReportItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.WPS_NUMBER;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.MULTI_CURRENCY_FOR_PAYROLL;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAYMENT_STATUS_APPROVED;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 07.03.14
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
@Repository("payslipTableManager")
public class PayslipTableManagerImpl extends BaseManager<EdsPayslipTable> implements PayslipTableManager {


    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public PayslipTableManagerImpl() {
        super(EdsPayslipTable.class);
    }

    @Override
    public List<EdsPayslipTable> getTables(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p from EdsPayslipTable p where p.deleted is not true");
        if (filterParameter.isCorporate()) {
            sql.append(" and p.fromTaxi is true");
        } else {
            sql.append(" and " + ServerUtils.checkForDeleted("p.fromTaxi"));
        }
        if (filterParameter.getSqlSearchKey() != null) {
            sql.append(" and (lower(p.approver.firstName) like '" + filterParameter.getSqlSearchKey() + "'");
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
            sql.append(" ORDER BY p.year desc, p.monthID desc  ");
        }
        return findInterval(sql.toString(), filterParameter.getStart(), filterParameter.getLimit());
    }

    @Override
    public List<EdsEmployee> getEmployeeDetails(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT e.*, mu.*\n")
//                .append("  e.id                                         AS id,\n")
//                .append("  coalesce(mu.firstname || ' ' || mu.lastname) AS name,\n")
//                .append("  e.driver_number\n")
                .append("FROM " + getCompanyId() + ".employee e\n")
                .append("  LEFT JOIN " + getCompanyId() + ".myuser mu ON mu.id = e.id\n")
                .append("  LEFT JOIN " + getCompanyId() + ".reference re ON re.id = mu.accountstatusid\n")
                .append("  LEFT JOIN " + getCompanyId() + ".emp_batch eb ON e.id = eb.emp_id\n")
                .append("WHERE\n")
                .append(filterParameter.getEmployeeId() != null ? "  e.id = " + filterParameter.getEmployeeId() + " AND\n" : "  (mu.deleted <> TRUE OR mu.deleted IS NULL) AND\n")
                .append(filterParameter.isCorporate() ? "  e.driver_number IS NOT NULL AND\n" : "")
                .append(filterParameter.isForChanging() ? "  e.driver_number IS NULL AND\n" : "")
                .append(filterParameter.getPayrollBatchID() != null && filterParameter.getPayrollBatchID() != 0 ? "  eb.batch_id = " + filterParameter.getPayrollBatchID() + " AND\n" : "")
                .append("  re.code <> 'RESIGNED_EMPLOYEE' AND\n")
                .append("  e.id NOT IN (SELECT ec.employee_id\n")
                .append("               FROM " + getCompanyId() + ".eos_calculation ec\n")
                .append("               WHERE (ec.deleted <> TRUE OR ec.deleted IS NULL))\n")
                .append("ORDER BY driver_number, name");

        return findNative(sql.toString(), EdsEmployee.class);
    }

    @Override
    public List<EdsPaymentDeduction> getCategoriesForTransaction(ListingFilterParameter lfp, String type) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select pd.* from " + companyID + ".PaymentDeduction pd ");
        sql.append("left join " + getCompanyId() + ".category c on c.id=pd.categoryID ");
        sql.append("where pd.employeeID=" + lfp.getEmployeeId());
        sql.append(" and  pd.paymentDate >= '" + lfp.getStartDate() + "' and pd.paymentDate<= '" + lfp.getEndDate() + "'");
        sql.append(" and c.type = '" + type + "' and c.niable is true");

        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public List<EdsPaymentDeduction> getCategoriesByEmployee(ListingFilterParameter lfp) {
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        sql.append("select pd.* from " + companyID + ".PaymentDeduction pd ")
                .append("LEFT JOIN " + companyID + ".employee e ON pd.employeeid = e.id ")
                .append("LEFT JOIN " + companyID + ".cashadvance ca ON pd.cashadvanceid = ca.id ")
                .append("LEFT JOIN " + companyID + ".additionalPayment ap ON pd.addpayment_id = ap.id ")
                .append(" where pd.employeeID=" + lfp.getEmployeeId())
                .append(" and (pd.isRecurring is true and pd.startdate is null")
                .append(" or pd.isRecurring is true and pd.startDate <= '" + lfp.getEndDate() + "' ")
                .append(" or (" + ServerUtils.checkForDeleted("pd.isRecurring") + " and ap.showinpayslip is true and ap.overallstatus = (select id from " + companyID + ".reference where code = '" + PAYMENT_STATUS_APPROVED + "') ")
                .append(" and (pd.startDate <= '" + lfp.getStartDate() + "' and pd.endDate >= '" + lfp.getEndDate() + "' or pd.startDate between '" + lfp.getStartDate() + "' and '" + lfp.getEndDate() + "' ))) and ")
                .append(ServerUtils.checkForDeleted("pd.fullPayed", "pd.deleted"))
                .append(" and (ca.id IS NULL OR NOT exists(SELECT * FROM " + companyID + ".companypayrollsettings WHERE key = '" + MULTI_CURRENCY_FOR_PAYROLL + "' AND value = 'true') ")
                .append(" OR ca.currency_id = e.currency_id)");

        return findNative(sql.toString(), EdsPaymentDeduction.class);
    }

    @Override
    public void deleteTableItems(Integer payslipTableID) {
        update("delete from EdsPayslipTableItem pi where pi.payslipTable.objectID=?", payslipTableID);
    }

    @Override
    public List<String> getPayedMonthList(Integer objectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct pt.monthid || ',' || pt.year from " + getCompanyId() + ".paysliptable pt  where ").append(ServerUtils.checkForDeleted("pt.deleted"));
        if (objectID != null) {
            sql.append(" and pt.id !=").append(objectID);
        }
        return findNative(sql.toString());
    }

    @Override
    public ArrayList<WpsReportItem> getWpsReportItems(ListingFilterParameter lfp) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT\n")
                .append("  t.*,\n")
                .append("  trim(coalesce(wps.value, ''))    AS wpsNumber,\n")
                .append("  trim(coalesce(uba.agentid, ''))  AS bankCode,\n")
                .append("  trim(coalesce(uba.ibancode, '')) AS ibanNumber\n")
                .append("FROM (SELECT\n")
                .append("        pti.id                                              AS payslipId,\n")
                .append("        mu.id                                               AS empId,\n")
                .append("        mu.firstname                                        AS firstName,\n")
                .append("        mu.lastname                                         AS lastName,\n")
                .append("        (CASE WHEN ep.employeecode IS NOT NULL AND trim(ep.employeecode) != ''\n")
                .append("         THEN trim(ep.employeecode) || ' -> '\n")
                .append("         ELSE '' END) || mu.firstname || ' ' || mu.lastname AS employeeName,\n")
                .append("        CASE WHEN cr.id IS NOT NULL\n")
                .append("          THEN cr.firstname || ' ' || cr.lastname\n")
                .append("        ELSE '' END                                         AS creator,\n")
                .append("        CASE WHEN ap.id IS NOT NULL\n")
                .append("          THEN ap.firstname || ' ' || ap.lastname\n")
                .append("        ELSE '' END                                         AS approver,\n")
                .append("        pti.fromdate                                        AS fromDate,\n")
                .append("        pti.todate                                          AS toDate,\n")
                .append("        pti.month                                           AS month,\n")
                .append("        pti.monthid                                         AS monthId,\n")
                .append("        pti.year                                            AS year,\n")
                .append("        pti.total                                           AS total,\n")
                .append("        sum(CASE WHEN (pd.isrecurring IS NOT NULL OR pd.issalaryobject) AND c.type = 'Payment'\n")
                .append("            THEN pp.payment_total\n")
                .append("            WHEN (pd.isrecurring IS NOT NULL OR pd.issalaryobject) AND c.type = 'Deduction'\n")
                .append("            THEN -pp.payment_total\n")
                .append("            ELSE 0 END)                                     AS recurringPayments,\n")
                .append("        coalesce(pti.daysworked, 0)                         AS workedDays,\n")
                .append("        sum(CASE WHEN c.code = 'LEAVE_DEDUCTIONS'\n")
                .append("            THEN coalesce(pd.leavedayscount, 0)\n")
                .append("            ELSE 0 END)                                     AS leaveDays\n")
                .append("      FROM " + getCompanyId() + ".paysliptableitem pti\n")
                .append("        JOIN " + getCompanyId() + ".myuser mu ON pti.employee_id = mu.id\n")
                .append("        JOIN " + getCompanyId() + ".employee e ON mu.id = e.id\n")
                .append("        JOIN " + getCompanyId() + ".payslip_payments pp ON pti.id = pp.payslip_item_id\n")
                .append("        JOIN " + getCompanyId() + ".paymentdeduction pd ON pp.payment_deduction_id = pd.id\n")
                .append("        JOIN " + getCompanyId() + ".category c ON pd.categoryid = c.id\n");

        if (lfp.getPayrollBatchID() != null)
            sql.append("        JOIN " + getCompanyId() + ".emp_batch eb ON e.id = eb.emp_id AND eb.batch_id = " + lfp.getPayrollBatchID());

        sql.append("        LEFT JOIN " + getCompanyId() + ".paysliptable pt ON pti.paysliptable_id = pt.id\n")
                .append("        LEFT JOIN " + getCompanyId() + ".employeeprofile ep ON e.profileid = ep.id\n")
                .append("        LEFT JOIN " + getCompanyId() + ".myuser cr ON pti.preparer_id = cr.id\n")
                .append("        LEFT JOIN " + getCompanyId() + ".myuser ap ON pti.approver_id = ap.id\n")
                .append("      WHERE " + ServerUtils.checkForDeleted("pti.deleted", "pd.deleted"));

        if (lfp.getSelectedMonth() != null && lfp.getYear() != null)
            sql.append("        AND pti.monthid = " + lfp.getSelectedMonth() + " AND pti.year = " + lfp.getYear());

        if (lfp.getObjectId() != null)
            sql.append("        AND pt.id = " + lfp.getObjectId());

        if (lfp.getPaymentMethodId() != null)
            sql.append("        AND (pt.paymentmethodid IS NOT NULL AND pt.paymentmethodid = " + lfp.getPaymentMethodId())
                    .append("             OR pt.paymentmethodid IS NULL AND e.paymethodid = " + lfp.getPaymentMethodId() + ")");

        sql.append("      GROUP BY pti.id, mu.id, ep.id, cr.id, ap.id) t\n")
                .append("  LEFT JOIN " + getCompanyId() + ".employeepayrollsettings wps ON t.empId = wps.employeeid AND wps.key = '" + WPS_NUMBER + "'\n")
                .append("  LEFT JOIN " + getCompanyId() + ".userbankaccount uba ON uba.userid = t.empId\n");

        if (lfp.isFromSifFile())
            sql.append("WHERE wps.value IS NOT NULL AND trim(wps.value) != ''\n")
                    .append("      AND uba.agentid IS NOT NULL AND trim(uba.agentid) != ''\n")
                    .append("      AND uba.ibancode IS NOT NULL AND trim(uba.ibancode) != ''");

        sql.append("ORDER BY firstName, lastName");

        return (ArrayList<WpsReportItem>) jdbcSpringManager.getSimJdbcOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(WpsReportItem.class));
    }

    @Override
    public List<Integer> getPayslipTableIdsByIds(String IDs) {
        return find("select pt.objectID from EdsPayslipTable pt where pt.objectID in (" + IDs + ") and " + ServerUtils.checkForDeleted("pt.deleted", "pt.fromTaxi"));
    }

    @Override
    public List<Integer> getPayslipTableIdsWithLimit(Integer startat, Integer limit) {
        return findLimited("select pt.objectID from EdsPayslipTable pt where pt.objectID > ? and " + ServerUtils.checkForDeleted("pt.deleted", "pt.fromTaxi") + " order by pt.objectID", limit, startat);
    }

    @Override
    public List<Integer> getCompanyDeletedPayslipTableListForSolr(SolrReindexRpc solrReindex) {
        return find("select pt.objectID from EdsPayslipTable pt where pt.deleted=true and pt.lastUpdateTime>='" + solrReindex.getLastUpdateTime() + "'"
                + (solrReindex.getLastUpdateEndTime() != null ? " and pt.lastUpdateTime<='" + solrReindex.getLastUpdateEndTime() + "'" : ""));
    }

    @Override
    public List<EdsPayslipTable> getPayslipTableListForSolr(SolrReindexRpc solrReindex, int startat, int limit) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select pt from EdsPayslipTable pt where (pt.deleted <> true or pt.deleted is null) ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("updatedDate", solrReindex.getLastUpdateTime());
            sqlQuery.append(" and pt.lastUpdateTime >= :updatedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sqlQuery.append(" and pt.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sqlQuery.append(" AND " + ServerUtils.checkForDeleted("pt.fromTaxi") + " order by pt.objectID ASC ");
        return findIntervalByNamedParams(sqlQuery.toString(), startat, limit, params);
    }

    @Override
    public Integer getEmployeeDataForGroupPayrunCount(PayslipFilter filter) {
        if (filter == null || (filter.getProjectId() == null && filter.getPayrollBatchID() == null && filter.getLocationId() == null)) {
            return 0;
        }
        String sql = "SELECT count(distinct e.id) FROM " + getCompanyId() + ".employee e " +
                "    LEFT JOIN " + getCompanyId() + ".myuser mu ON e.id = mu.id " +
                "    LEFT JOIN " + getCompanyId() + ".employeeprofile ep ON ep.id = e.profileid " +
                "    LEFT JOIN " + getCompanyId() + ".reference re ON re.id = mu.accountstatusid " +
                "    LEFT JOIN " + getCompanyId() + ".emp_batch eb ON e.id = eb.emp_id " +
                "    LEFT JOIN " + getCompanyId() + ".teamemployee te on te.employeeid = e.id " +
                "    LEFT JOIN " + getCompanyId() + ".projectemployee pe on pe.employeedepartmentid = te.id " +
                "    LEFT JOIN " + getCompanyId() + ".project pro on pe.projectid = pro.id " +
                "    LEFT JOIN " + getCompanyId() + ".location loc on mu.locationId = loc.id " +
                "    LEFT JOIN " + getCompanyId() + ".paysliptableitem  pi on e.id = pi.employee_id and (pi.monthid || ',' || pi.year) =:periodCheck and (pi.deleted <> TRUE OR pi.deleted IS NULL) " +
                " LEFT JOIN " + getCompanyId() + ".paysliptable pt on pt.id = pi.payslipTable_id ";

        if ("200138".equals(ServerSecurityContext.getInstance().getCompanyId())) {
            final List<String> monthYesrList = ServerUtils.getMonthYearList(filter.getFromDate().getNonConvertedDate(), filter.getToDate().getNonConvertedDate());

            StringBuilder months = new StringBuilder();
            if (monthYesrList != null && !monthYesrList.isEmpty()) {
                Iterator<String> iterator = monthYesrList.iterator();
                while (iterator.hasNext()) {
                    String month = iterator.next();
                    if (month != null && !"".equals(month)) {
                        months.append("'").append(month).append("'");
                        if (iterator.hasNext()) {
                            months.append(",");
                        }
                    }
                }
            }
            sql += "    LEFT JOIN " + getCompanyId() + ".monthly_timesheet mt on mt.project_employee_id = pe.id ";
            sql += "    WHERE mu.deleted <> TRUE " +
                   "        AND (e.enddate IS NULL OR e.enddate >= '" + filter.getToDate().getNonConvertedDate() + "') ";

            if (months != null && !"".contentEquals(months)) {
                sql += "        AND mt.month_year IN (" + months + ") ";
            }
            sql += "       AND ((mt.total_days_worked IS NOT NULL AND mt.total_days_worked > 0) OR (mt.worked_hours IS NOT NULL AND mt.worked_hours > 0)) ";
        } else {
            sql += "      WHERE ((mu.deleted <> TRUE AND e.enddate IS NULL) OR (e.enddate >= '" + filter.getFromDate().getNonConvertedDate() + "')) ";
        }
        if (filter.getPayrollBatchID() != null && filter.getPayrollBatchID() > 0) {
            sql += "            AND eb.batch_id = " + filter.getPayrollBatchID();
        }
        if (filter.getProjectId() != null) {
            sql += "            AND pro.id = " + filter.getProjectId();
        }
        if (filter.getLocationId() != null) {
            sql += "            AND loc.id = " + filter.getLocationId();
        }
        if (!StringUtil.isEmpty(filter.getSearchKey())) {
            sql += "      and (lower(mu.firstName) like(:searchKey)" +
                    "          or lower(mu.lastName) like(:searchKey)" +
                    "          or lower(mu.middleName) like(:searchKey)" +
                    "          or lower(mu.email) like(:searchKey)" +
                    "          or lower(ep.employeeCode) like(:searchKey))";
        }
        if (filter.getAvoidEmployees() != null && filter.getAvoidEmployees().size() > 0) {
            sql += " and e.id not in (" + ServerUtils.getAsCommoDelimited(filter.getAvoidEmployees(), "0", ",") + ") ";
        }
//        sql += "    and (pi.monthid || ',' || pi.year) <> :periodCheck";
        if (filter.getProjectId() != null) {
            sql += "   and (pi.employee_id is null or pt.projectid != " + filter.getProjectId() + ") ";
        } else {
            sql += "    and pi.employee_id is null ";
        }
        if (filter.getProjectId() != null) {
            sql += "    and (pe.isdeleted <> true or pe.isdeleted is null)";
        }
        Query query = this.slaveEntityManager.createNativeQuery(sql)
                .setParameter("periodCheck", filter.getPeriodChecker())
                .setMaxResults(1);

        if (!StringUtil.isEmpty(filter.getSearchKey())) {
            query = query.setParameter("searchKey", filter.getSqlSearchKey());
        }
        final List<BigInteger> list = query.getResultList();
        return list.isEmpty() ? 0 : list.get(0).intValue();

    }

    @Override
    public List<Integer> getEmployeeListDataForGroupPayrun(PayslipFilter filter) {
        if (filter == null) {
            return Collections.emptyList();
        }
        String sqlQuery = "SELECT DISTINCT e.id FROM " + getCompanyId() + ".employee e " +
                "     LEFT JOIN " + getCompanyId() + ".myuser mu ON e.id = mu.id " +
                "     LEFT JOIN " + getCompanyId() + ".employeeprofile ep ON ep.id = e.profileid " +
                "     LEFT JOIN " + getCompanyId() + ".reference re ON re.id = mu.accountstatusid " +
                "     LEFT JOIN " + getCompanyId() + ".emp_batch eb ON e.id = eb.emp_id " +
                "     LEFT JOIN " + getCompanyId() + ".teamemployee te on te.employeeid = e.id " +
                "     LEFT JOIN " + getCompanyId() + ".projectemployee pe on pe.employeedepartmentid = te.id " +
                "     LEFT JOIN " + getCompanyId() + ".project pro on pe.projectid = pro.id " +
                "     LEFT JOIN " + getCompanyId() + ".location loc on mu.locationId = loc.id " +
                "     LEFT JOIN " + getCompanyId() + ".paysliptableitem  pi on e.id = pi.employee_id " +
                "                                         and (pi.monthid || ',' || pi.year) = '" + filter.getPeriodChecker() + "' " +
                "                                         and (pi.deleted <> TRUE OR pi.deleted IS NULL)";


        sqlQuery += "      WHERE (e.startdate <=:endDate OR e.startdate is null ) AND ((mu.deleted <> TRUE AND e.enddate IS NULL) OR (e.enddate > :startDate))";
        if (filter.getPayrollBatchID() != null && filter.getPayrollBatchID() > 0) {
            sqlQuery += "            AND eb.batch_id = :payrollBatchId ";
        }
        if (filter.getProjectId() != null) {
            sqlQuery += "            AND pro.id = :projectId ";
        }
        if (filter.getLocationId() != null) {
            sqlQuery += "            AND loc.id = :locationId ";
        }
        if (filter.getAvoidEmployees() != null && filter.getAvoidEmployees().size() > 0) {
            sqlQuery += " and e.id not in (" + ServerUtils.getAsCommoDelimited(filter.getAvoidEmployees(), "0", ",") + ") ";
        }
        if (!StringUtil.isEmpty(filter.getSearchKey())) {
            sqlQuery += "      and (lower(mu.firstName) like(:searchKey)" +
                    "          or lower(mu.lastName) like(:searchKey)" +
                    "          or lower(mu.middleName) like(:searchKey)" +
                    "          or lower(mu.email) like(:searchKey)" +
                    "          or lower(ep.employeeCode) like(:searchKey))";
        }
        sqlQuery += "    and pi.employee_id is null ";
        sqlQuery += "    and (e.id not in (select ec.employee_id from" + getCompanyId() + ".eos_calculation ec where (ec.deleted <> true or ec.deleted is null))) ";
        if (filter.getProjectId() != null) {
            sqlQuery += "    and (pe.isdeleted <> true or pe.isdeleted is null) ";
        }
        sqlQuery += "      ORDER BY e.id ASC ";
        Query query = this.slaveEntityManager.createNativeQuery(sqlQuery)
                .setParameter("startDate", filter.getFromDate().getNonConvertedDate())
                .setParameter("endDate", filter.getToDate().getNonConvertedDate());

        if (filter.getPayrollBatchID() != null && filter.getPayrollBatchID() > 0) {
            query = query.setParameter("payrollBatchId", filter.getPayrollBatchID());
        }
        if (filter.getProjectId() != null) {
            query = query.setParameter("projectId", filter.getProjectId());
        }
        if (filter.getLocationId() != null) {
            query = query.setParameter("locationId", filter.getLocationId());
        }
        if (!StringUtil.isEmpty(filter.getSearchKey())) {
            query = query.setParameter("searchKey", filter.getSqlSearchKey());
        }
        if (filter.getLimit() != null) {
            query = query.setMaxResults(filter.getLimit());
        }
        if (filter.getStart() != null) {
            query = query.setFirstResult(filter.getStart());
        }
        return query.getResultList();
    }

    @Override
    public List<GroupPayrunItems> getEmployeeDataForGroupPayrun(ListingFilterParameter lfp) {
        List<GroupPayrunItems> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder()
                .append("SELECT row_to_json(row) :: VARCHAR AS result\n")
                .append("FROM (SELECT DISTINCT\n")
                .append("        e.id                                                             AS id,\n")
                .append("        (coalesce(mu.firstname, '') || ' ' || coalesce(mu.lastname, '')) AS name,\n")
                .append("        ep.employeeCode                                                  AS code,\n")
                .append("        e.startdate                                                      AS hireDate,\n")
                .append("        e.citizenship                                                     AS countryId,\n")
                .append("        e.currency_id                                                    AS currencyid,\n")
                .append("        (SELECT array_agg(pds) AS paymentDeductions\n")
                .append("         FROM (SELECT\n")
                .append("                 pd.id                AS id,\n")
                .append("                 c.id                 AS categoryId,\n")
                .append("                 c.name               AS categoryName,\n")
                .append("                 c.code               AS categoryCode,\n")
                .append("                 c.type               AS categoryType,\n")
                .append("                 pd.paymentAmount     AS paymentAmount,\n")
                .append("                 pd.paymentDate       AS paymentDate,\n")
                .append("                 pd.totalAmount       AS totalAmount,\n")
                .append("                 pd.startDate         AS startDate,\n")
                .append("                 pd.endDate           AS endDate,\n")
                .append("                 pd.cashAdvanceID     AS cashAdvanceID,\n")
                .append("                 pd.pay_type          AS type,\n")
                .append("                 pd.percentage        AS percentage,\n")
                .append("                 pd.fromallallowances AS isFromAllAllowances,\n")
                .append("                 pd.isSalaryObject    AS isSalaryObject,\n")
                .append("                 pd.addpayment_id     AS addpayment_id,\n")
                .append("                 (SELECT array_agg(linkedCat) AS linkedCategories\n")
                .append("                  FROM (SELECT\n")
                .append("                          cat.id   AS id,\n")
                .append("                          cat.name AS name,\n")
                .append("                          cat.code AS code,\n")
                .append("                          cat.type AS type\n")
                .append("                        FROM " + getCompanyId() + ".category cat\n")
                .append("                          JOIN " + getCompanyId() + ".paymentdeductionscategories pdc ON cat.id = pdc.categoryid\n")
                .append("                        WHERE (cat.deleted IS NULL OR cat.deleted != TRUE) AND\n")
                .append("                              pdc.paymentdeductionid = pd.id) linkedCat)\n")
                .append("               FROM " + getCompanyId() + ".paymentdeduction pd\n")
                .append("                 LEFT JOIN " + getCompanyId() + ".category c ON c.id = pd.categoryid\n")
                .append("                 LEFT JOIN " + getCompanyId() + ".cashadvance ca ON pd.cashadvanceid = ca.id\n")
                .append("                 LEFT JOIN " + getCompanyId() + ".additionalPayment ap ON pd.addpayment_id = ap.id\n")
                .append("               WHERE pd.employeeid = e.id AND pd.deleted IS NOT TRUE AND pd.fullpayed IS NOT TRUE AND\n")
                .append("                     (pd.paymentamount IS NOT NULL AND pd.paymentamount > 0 OR\n")
                .append("                      pd.percentage IS NOT NULL AND pd.percentage > 0) AND\n")
                .append("                     (pd.isRecurring IS TRUE AND pd.startdate IS NULL OR\n")
                .append("                      pd.isRecurring IS TRUE AND pd.startDate <= '" + lfp.getEndDate() + "' OR\n")
                .append(ServerUtils.checkForDeleted("pd.isRecurring") + " AND ap.showinpayslip IS TRUE AND ap.overallstatus = (select id from " + getCompanyId() + ".reference where code = '" + PAYMENT_STATUS_APPROVED + "') AND\n")
                .append("                      pd.startDate <= '" + lfp.getStartDate() + "' and pd.endDate >= '" + lfp.getEndDate() + "') AND\n")
                .append("                     (ca.id IS NULL OR NOT exists(SELECT *\n")
                .append("                                                  FROM ").append(getCompanyId()).append(".companypayrollsettings\n")
                .append("                                                  WHERE key = '" + MULTI_CURRENCY_FOR_PAYROLL + "' AND value = 'true') OR\n")
                .append("                      ca.currency_id = e.currency_id)) pds),\n")
                .append("        (SELECT array_agg(t) AS settings\n")
                .append("         FROM (SELECT\n")
                .append("                 ep.key   AS key,\n")
                .append("                 ep.value AS value\n")
                .append("               FROM ").append(getCompanyId()).append(".employeepayrollsettings ep\n")
                .append("               WHERE ep.employeeid = e.id AND ep.value IS NOT NULL AND ep.value <> '') t)\n")
                .append("      FROM ").append(getCompanyId()).append(".employee e\n")
                .append("        LEFT JOIN ").append(getCompanyId()).append(".myuser mu ON e.id = mu.id\n")
                .append("        LEFT JOIN ").append(getCompanyId()).append(".employeeprofile ep ON ep.id = e.profileid\n")
                .append("        LEFT JOIN ").append(getCompanyId()).append(".reference re ON re.id = mu.accountstatusid\n")
                .append("        LEFT JOIN ").append(getCompanyId()).append(".emp_batch eb ON e.id = eb.emp_id\n")
                .append("        LEFT JOIN ").append(getCompanyId()).append(".teamemployee te on te.employeeid = e.id\n")
                .append("        LEFT JOIN ").append(getCompanyId()).append(".projectemployee pe on pe.employeedepartmentid = te.id\n")
                .append("        LEFT JOIN ").append(getCompanyId()).append(".project pro on pe.projectid = pro.id\n");
        if ("200138".equals(ServerSecurityContext.getInstance().getCompanyId())) {
            List<String> monthYesrList = ServerUtils.getMonthYearList(lfp.getStartDate(), lfp.getEndDate());
            StringBuilder months = new StringBuilder();
            if (monthYesrList != null && monthYesrList.size() > 0) {
                Iterator<String> iterator = monthYesrList.iterator();
                while (iterator.hasNext()) {
                    String month = iterator.next();
                    if (month != null && !"".equals(month)) {
                        months.append("'").append(month).append("'");
                        if (iterator.hasNext()) {
                            months.append(",");
                        }
                    }
                }
            }
            sql.append("        LEFT JOIN ").append(getCompanyId()).append(".monthly_timesheet mt on mt.project_employee_id = pe.id\n");
            sql.append("        WHERE mu.deleted <> TRUE AND (e.enddate IS NULL OR e.enddate >= '").append(lfp.getEndDate()).append("')\n");
            if (months != null && !"".contentEquals(months)) {
                sql.append("        AND mt.month_year IN (" + months + ")\n");
            }
            sql.append("        AND ((mt.total_days_worked IS NOT NULL AND mt.total_days_worked > 0) OR (mt.worked_hours IS NOT NULL AND mt.worked_hours > 0))\n");
        } else {
            sql.append("      WHERE mu.deleted <> TRUE AND (e.enddate IS NULL OR e.enddate >= '").append(lfp.getEndDate()).append("')\n");
        }
        if (lfp.getEmployeeId() != null) {
            sql.append(" and mu.id = " + lfp.getEmployeeId());
        }
        if (lfp.getEmployeeIDs() != null) {
            sql.append(" and mu.id not in (" + lfp.getEmployeeIDs() + ")");
        }
        if (lfp.getPayrollBatchID() != null && lfp.getPayrollBatchID() > 0) {
            sql.append("            AND eb.batch_id = ").append(lfp.getPayrollBatchID()).append("\n");
        }
        if (lfp.getProjectId() != null) {
            sql.append("            AND pro.id = ").append(lfp.getProjectId()).append("\n");
        }
        sql.append("      GROUP BY e.id, mu.id, ep.id\n");
        if (CustomFormConstants.EMPLOYEE_CODE.equals(lfp.getSortField())) {
            sql.append("      ORDER BY code ASC) row");
        } else {
            sql.append("      ORDER BY name ASC) row");
        }


        List<JsonResult> jsonResult = jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(JsonResult.class));

        GroupPayrunItems wrappedItems;
        for (JsonResult json : jsonResult) {
            wrappedItems = parseJsonResult(json.getResult());
            result.add(wrappedItems);
        }

        return result;
    }


    private GroupPayrunItems parseJsonResult(String json) {
        GroupPayrunItems item = new GroupPayrunItems();
        PaymentDeductionObject pdsObject;
        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) parser.parse(json);
            item.setId(((Long) object.get("id")).intValue());
            item.setName((String) object.get("name"));
            item.setCode((String) object.get("code"));
            if (ServerUtils.parseDate((String) object.get("hiredate"), "yyyy-MM-dd") != null) {
                item.setHireDate(new DateNonConvertable(ServerUtils.parseDate((String) object.get("hiredate"), "yyyy-MM-dd")));
            }
            if (object.get("countryid") != null) {
                item.setCountryid(((Long) object.get("countryid")).intValue());
            }
            if (object.get("currencyid") != null) {
                item.setCurrencyId(((Long) object.get("currencyid")).intValue());
            }
            JSONArray pds = (JSONArray) object.get("paymentdeductions");
            JSONObject subItem;
            if (pds != null) {
                for (Object pd : pds) {
                    pdsObject = new PaymentDeductionObject();
                    subItem = (JSONObject) pd;
                    pdsObject.setId(((Long) subItem.get("id")).intValue());
                    if (((Long) subItem.get("categoryid")).intValue() != 0 && subItem.get("categoryname") != null) {
                        pdsObject.setCategoryItem(new PaymentDeductionSelectItem(((Long) subItem.get("categoryid")).intValue(), (String) subItem.get("categoryname")
                                , (String) subItem.get("categorycode"), (String) subItem.get("categorytype")));
                    }
                    if (subItem.get("paymentamount") != null) {
                        pdsObject.setPaymentAmount(new BigDecimal(subItem.get("paymentamount").toString()));
                        pdsObject.setAmount(pdsObject.getPaymentAmount());
                    }
                    if (subItem.get("totalamount") != null) {
                        pdsObject.setTotalAmount(BigDecimal.valueOf((Double) subItem.get("totalamount")));
                    }
                    if (subItem.get("percentage") != null) {
                        pdsObject.setPercentage(BigDecimal.valueOf((Double) subItem.get("percentage")));
                    }
                    if (subItem.get("paymentdate") != null) {
                        pdsObject.setPaymentDate(ServerUtils.parseDate((String) subItem.get("paymentdate"), "yyyy-MM-dd"));
                    }
                    if (subItem.get("startdate") != null) {
                        pdsObject.setStarttDate(new DateNonConvertable(ServerUtils.parseDate((String) subItem.get("startdate"), "yyyy-MM-dd")));
                    }
                    if (subItem.get("enddate") != null) {
                        pdsObject.setEnddDate(new DateNonConvertable(ServerUtils.parseDate((String) subItem.get("enddate"), "yyyy-MM-dd")));
                    }
                    if (subItem.get("cashadvanceid") != null) {
                        pdsObject.setCashAdvanceID(((Long) subItem.get("cashadvanceid")).intValue());
                    }
                    if (subItem.get("type") != null) {
                        pdsObject.setType(((Long) subItem.get("type")).intValue());
                    }
                    if (subItem.get("isfromallallowances") != null) {
                        pdsObject.setFromAllAllowances((Boolean) subItem.get("isfromallallowances"));
                    }
                    if (subItem.get("issalaryobject") != null) {
                        pdsObject.setSalaryObject((Boolean) subItem.get("issalaryobject"));
                    }
                    if (subItem.get("categorytype") != null && subItem.get("categorytype").equals(EdsPayrollCategory.PAYMENT)) {
                        pdsObject.setPaymentCategory(true);
                    }
                    if (subItem.get("addpayment_id") != null) {
                        pdsObject.setAdditionalPayment(true);
                    }
                    if (subItem.get("linkedcategories") != null) {
                        JSONArray linkedCategories = (JSONArray) subItem.get("linkedcategories");
                        for (Object linkedCategory1 : linkedCategories) {
                            JSONObject linkedCategoryItem = (JSONObject) linkedCategory1;
                            PaymentDeductionObject linkedCategory = new PaymentDeductionObject();
                            linkedCategory.setCategoryItem(new PaymentDeductionSelectItem(((Long) linkedCategoryItem.get("id")).intValue(), (String) linkedCategoryItem.get("name"), (String) linkedCategoryItem.get("code"), (String) linkedCategoryItem.get("type")));
                            pdsObject.getLinkedCategories().add(linkedCategory);
                        }
                    }

                    item.getPaymentDeductions().add(pdsObject);
                }
            }
            JSONArray settings = (JSONArray) object.get("settings");
            if (settings != null) {
                for (Object setting : settings) {
                    subItem = (JSONObject) setting;
                    item.getSettingsMap().put((String) subItem.get("key"), (String) subItem.get("value"));
                }
            } else {
                item.getSettingsMap();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return item;
    }

    @Override
    public void updatePayslipTableTotalItems(Integer totalItems, Integer payslipTableID) {
        update("update EdsPayslipTable p set p.totalItems = ? where p.objectID = ?", totalItems, payslipTableID);
    }
}
