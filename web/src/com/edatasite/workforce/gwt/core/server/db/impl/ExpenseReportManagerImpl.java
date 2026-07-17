package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeDepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository("expenseReportManager")
public class ExpenseReportManagerImpl extends BaseManager<EdsExpenseReport> implements ExpenseReportManager, Constants, AccountingConstants {
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;


    public ExpenseReportManagerImpl() {
        super(EdsExpenseReport.class);
    }

    public List<EdsExpenseReport> getApproversReportsByStatus(EdsReference[] status, Date maxDate, Integer count) {

        maxDate = getThisMonth(maxDate);

        EdsUser user = getUser();

        Map<String, Object> map = new HashMap<>();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT er FROM EdsExpenseReport er WHERE er.approver = :user AND " + ServerUtils.checkForDeleted("er.isDeleted") + " ");
        map.put("user", user);

        if (status != null) {

            sql.append("AND ");

            for (int i = 0; i < status.length; i++) {
                String statusName = "status" + i;
                sql.append("er.status = :" + statusName + " ");
                map.put(statusName, status[i]);

                if (i < (status.length - 1)) {
                    sql.append("OR ");
                }
            }
        }

        if (maxDate != null) {
            sql.append("AND er.lastUpdateTime >= :maxDate ");
            map.put("maxDate", maxDate);
        }

        sql.append("ORDER BY er.lastUpdateTime DESC LIMIT " + count.toString());

        return findByNamedParams(sql.toString(), map);
    }

    public Integer getApproversReportCount(EdsReference[] status, Date maxDate) {

        maxDate = getThisMonth(maxDate);

        EdsUser user = getUser();

        Map<String, Object> map = new HashMap<>();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT COUNT(er) FROM EdsExpenseReport er " +
                "WHERE er.lastUpdateTime >= :maxDate " +
                "AND er.approver = :user AND " + ServerUtils.checkForDeleted("er.isDeleted") + " ");

        map.put("maxDate", maxDate);
        map.put("user", user);

        if (status != null) {

            sql.append("AND ");

            for (int i = 0; i < status.length; i++) {
                String statusName = "status" + i;
                sql.append("er.status = :" + statusName + " ");
                map.put(statusName, status[i]);

                if (i < (status.length - 1)) {
                    sql.append("OR ");
                }
            }
        }

        return ((Long) findSingleByNamedParams(sql.toString(), map)).intValue();
    }

    public List<EdsExpenseReport> getReportersReportsByStatus(EdsReference[] status, Date maxDate, Integer count) {

        maxDate = getThisMonth(maxDate);

        EdsUser user = getUser();

        Map<String, Object> map = new HashMap<>();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT er FROM EdsExpenseReport er WHERE er.reporter = :user AND " + ServerUtils.checkForDeleted("er.isDeleted") + " ");
        map.put("user", user);

        if (status != null) {

            sql.append("AND ");

            for (int i = 0; i < status.length; i++) {
                String statusName = "status" + i;
                sql.append("er.status = :" + statusName + " ");
                map.put(statusName, status[i]);

                if (i < (status.length - 1)) {
                    sql.append("OR ");
                }
            }
        }

        if (maxDate != null) {
            sql.append("AND er.lastUpdateTime >= :maxDate ");
            map.put("maxDate", maxDate);
        }

        sql.append("ORDER BY er.lastUpdateTime DESC ");

        if (count != null) {
            sql.append("LIMIT " + count + " ");
        }

        return findByNamedParams(sql.toString(), map);
    }

    @Override
    public List<EdsExpenseReport> getUnpaidExpenseClaimsForPayslip(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT r FROM EdsExpenseReport r ");
        sql.append(" LEFT JOIN r.overallStatus st");
        sql.append(" WHERE " + ServerUtils.checkForDeleted("r.isDeleted"));
        if (fp.getEmployeeId() != null) {
            sql.append(" AND r.reporter.objectID = '" + fp.getEmployeeId() + "' ");
        }
        sql.append(" AND r.isCompanyExpense=false ");
        sql.append(" AND st.objectID in (" + ServerUtils.getAsCommoDelimited(Arrays.asList(fp.getStatusIDs()), "") + ") ");
        sql.append(" and r.currency.objectID = ").append(fp.getCurrencyID()).append(" ");
        sql.append("AND (NOT EXISTS (FROM EdsCompanyPayrollSettings es WHERE es.key = 'MULTI_CURRENCY_FOR_PAYROLL' AND es.value = 'true') ")
                .append("OR r.reporter.salaryCurrency IS NULL OR r.currency IS NULL OR r.reporter.salaryCurrency = r.currency ");
        if (fp.getBaseCurrencyID() != null) {
            sql.append("OR r.reporter.salaryCurrency.objectID = '" + fp.getBaseCurrencyID() + "' OR r.currency.objectID = '" + fp.getBaseCurrencyID() + "'");
        }
        sql.append(") AND r.payslipTableItemID IS NULL AND r.startDate <= ? ORDER BY r.objectID DESC");

        return find(sql.toString(), fp.getEndDate());
    }

    public List<EdsExpenseReport> getPayslipRelatedExpenseClaims(Integer payslipID) {
        return find("SELECT r FROM EdsExpenseReport r WHERE r.payslipID = ? ORDER BY r.objectID desc", payslipID);
    }

    public List<EdsExpenseReport> getPayslipTableItemRelatedExpenseClaims(Integer payslipTableItemID) {
        return find("SELECT r FROM EdsExpenseReport r WHERE r.payslipTableItemID = ? ORDER BY r.objectID desc", payslipTableItemID);
    }


    public Integer getReportersReportCount(EdsReference[] status, Date maxDate) {

        maxDate = getThisMonth(maxDate);

        EdsUser user = getUser();

        Map<String, Object> map = new HashMap<>();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT COUNT(er) FROM EdsExpenseReport er " +
                "WHERE er.lastUpdateTime >= :maxDate " +
                "AND er.reporter = :user AND " + ServerUtils.checkForDeleted("er.isDeleted") + " ");

        map.put("maxDate", maxDate);
        map.put("user", user);

        if (status != null) {

            sql.append(" AND ");

            for (int i = 0; i < status.length; i++) {
                String statusName = "status" + i;
                sql.append("er.status = :" + statusName + " ");
                map.put(statusName, status[i]);

                if (i < (status.length - 1)) {
                    sql.append("OR ");
                }
            }
        }

        return ((Long) findSingleByNamedParams(sql.toString(), map)).intValue();
    }

    public Integer getAllReportCount(EdsReference[] status, Date maxDate) {

        maxDate = getThisMonth(maxDate);

        Map<String, Object> map = new HashMap<>();

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT COUNT(er) FROM EdsExpenseReport er " +
                "WHERE er.lastUpdateTime >= :maxDate AND " + ServerUtils.checkForDeleted("er.isDeleted") + " ");
        map.put("maxDate", maxDate);

        if (status != null) {

            sql.append(" AND ");

            for (int i = 0; i < status.length; i++) {
                String statusName = "status" + i;
                sql.append("er.status = :" + statusName + " ");
                map.put(statusName, status[i]);

                if (i < (status.length - 1)) {
                    sql.append("OR ");
                }
            }
        }

        return ((Long) findSingleByNamedParams(sql.toString(), map)).intValue();
    }

    public EdsExpenseReport getExpenseReport(Integer objectID) {

        Map<String, Object> map = new HashMap<>();
        map.put("objectID", objectID);
        return (EdsExpenseReport) findSingleByNamedParams("select er from EdsExpenseReport er " +
                "where er.objectID =:objectID and (er.isDeleted is null or er.isDeleted is not true)", map);
    }

    public List<EdsExpenseReport> getEmployeeReports(ListingFilterParameter filterParametrs, Boolean isCount) {
        EdsEmployee user = null;
        if (filterParametrs.getEmployeeId() == null) {
            user = employeeManager.get(getUser().getObjectID());
        } else {
            user = employeeManager.get(filterParametrs.getEmployeeId());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("reporter", user);

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct er from EdsExpenseReport er ");
        addSortingRequiredParameters(filterParametrs, sql);
        sql.append("where er.reporter =:reporter and " + ServerUtils.checkForDeleted("er.isDeleted"));

        EdsReference voidExpenseStatus = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_REVERSED);
        if (voidExpenseStatus != null) {
            sql.append(" and er.overallStatus.objectID != '" + voidExpenseStatus.getObjectID() + "' ");
        }

        if (filterParametrs.getStatusID() != null && filterParametrs.getStatusID() != 0) {
            sql.append(" and er.overallStatus.objectID = '" + filterParametrs.getStatusID() + "' ");
        } else if (!ServerUtils.isNullOrEmpty(filterParametrs.getStatusCode())) {
            sql.append(" and er.overallStatus.code = '" + filterParametrs.getStatusCode() + "' ");
        }


        if (filterParametrs.isValidSearchKey()) {
            String key = filterParametrs.getSqlSearchKey();
            sql.append(" and (lower(er.currentApprover.exactEmployee.firstName) like '" + key
                    + "' or lower(er.currentApprover.exactEmployee.lastName) like '" + key
                    + "' or lower(er.title) like'" + key
                    + "' or lower(er.project.name) like '" + key
                    + "' or lower(er.overallStatus.name) like '" + key + "') ");
        }
        addFilterSortingQuery(filterParametrs, sql);

        if (!isCount) {
            return findIntervalByNamedParams(sql.toString(), filterParametrs.getStart(), filterParametrs.getLimit() > 0 ? filterParametrs.getLimit() : 20, map);
        }
        return findByNamedParams(sql.toString(), map);
    }

    @Override
    public List<Object[]> getEmployeeTopExpenses(Integer employeeId, Date fromDate, Date toDate) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT a.name, sum(coalesce(ti.debit,0)) total FROM ").append(getCompanyId()).append(".expenseReport exp \n");
        sql.append("JOIN ").append(getCompanyId()).append(".reference status on status.id = exp.overallStatus \n");
        sql.append("JOIN ").append(getCompanyId()).append(".transaction t on t.expenseReportid = exp.id \n");
        sql.append("JOIN ").append(getCompanyId()).append(".transactionitem ti on ti.transactionid = t.id \n");
        sql.append("JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid \n");
        sql.append("WHERE exp.isDeleted is not true and status.code != '" + EXPENSE_REVERSED + "' and a.key is null \n");
        sql.append("AND exp.reporterId = " + (employeeId != null ? employeeId : getUser().getObjectID()));
        sql.append("AND t.journaldate between ? and ? \n");

        sql.append(" GROUP BY a.name \n");
        sql.append(" ORDER BY total DESC ");

        return findNative(sql.toString(), fromDate, toDate);
    }

    public List<EdsExpenseReport> getCompanyReports(String status, ListingFilterParameter fp) {
        return find("select distinct er from EdsExpenseReport er where " + ServerUtils.checkForDeleted("er.isDeleted") + " and er.overallStatus.code=? and er.startDate between ? and ? ",
                status, fp.getStartDate(), fp.getEndDate() != null ? fp.getEndDate() : new Date());
    }

    public List<EdsExpenseReport> getWaitingExpenseReports(ListingFilterParameter filterParametrs) {

        EdsEmployee user = (EdsEmployee) getUser();
        EdsReference status;
        if (filterParametrs != null && filterParametrs.getAccountType() != null) {
            status = referenceManager.findReference(EXPENSE_STATUS, filterParametrs.getAccountType());
        } else {
            status = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_SUBMITTED);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("approver", user);
        map.put("status", status);

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct er from EdsExpenseReport er ");
        addSortingRequiredParameters(filterParametrs, sql);
        sql.append("where " + ServerUtils.checkForDeleted("er.isDeleted") + " and (er.currentApprover.exactEmployee =:approver) and (er.overallStatus =:status) ");

        EdsReference voidExpenseStatus = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_REVERSED);
        if (voidExpenseStatus != null) {
            sql.append(" and er.overallStatus.objectID != '" + voidExpenseStatus.getObjectID() + "' ");
        }

        if (filterParametrs != null && filterParametrs.getEmployeeId() != null && filterParametrs.getEmployeeId() != 0) {
            sql.append(" and er.reporter.objectID=" + filterParametrs.getEmployeeId() + " ");
        }
        if (filterParametrs != null && filterParametrs.isValidSearchKey()) {
            String key = filterParametrs.getSqlSearchKey();
            sql.append(" and (lower(er.reporter.firstName) like '" + key
                    + "' or lower(er.reporter.lastName) like '" + key
                    + "' or lower(er.title) like'" + key
                    + "' or lower(er.project.name) like '" + key + "') ");
        }

        addFilterSortingQuery(filterParametrs, sql);
        return findByNamedParams(sql.toString(), map);
    }

    public List<EdsExpenseReport> getAllExpenseReports(ListingFilterParameter filterParametrs) {
        filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
        EdsEmployee user = (EdsEmployee) getUser();

        Map<String, Object> map = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct er from EdsExpenseReport er ");
        addSortingRequiredParameters(filterParametrs, sql);

        StringBuilder filterSql = new StringBuilder();
        filterSql.append(" where " + ServerUtils.checkForDeleted("er.isDeleted"));
        if (filterParametrs.getYear() != null) {
            filterSql.append(" and extract(year from er.startDate)=" + filterParametrs.getYear() + " ");
        }
        if (!(roleManager.hasRole(user, EdsRole.DR) || roleManager.hasRole(user, EdsRole.ADMIN) || roleManager.hasRole(user, EdsRole.ACCOUNTANT))) {
            map.put("user", user);
            filterSql.append(" and (er.currentApprover.exactEmployee =:user or er.reporter=:user)");
        }

        EdsReference voidExpenseStatus = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_REVERSED);
        if (voidExpenseStatus != null) {
            filterSql.append(" and er.overallStatus.objectID != '" + voidExpenseStatus.getObjectID() + "' ");
        }
        if (filterParametrs.getSelectedMonth() != null && filterParametrs.getSelectedYear() != null) {
            filterSql.append(" and er.startDate between :start and :end ");
            Date currentDate = new Date();
            Date startDate = ServerUtils.getYearStartDate(currentDate.getYear());
            Date endDate = new Date(filterParametrs.getSelectedYear(), filterParametrs.getSelectedMonth(), currentDate.getDate());
            map.put("start", startDate);
            map.put("end", endDate);
        }
        if (filterParametrs.getLocationId() != null) {
            List<EdsEmployee> employees = locationManager.getLocationEmployee(filterParametrs.getLocationId());
            List<Integer> employeeIDs = new ArrayList<>();
            if (employees != null && employees.size() > 0) {
                for (EdsEmployee employee : employees) {
                    employeeIDs.add(employee.getObjectID());
                }
                map.put("locationEmployeeIDs", employeeIDs);
                filterSql.append(" and er.reporter.objectID IN (:locationEmployeeIDs) ");
            }
        }
        if (filterParametrs.getDepartmentId() != null) {
            List<EdsEmployeeDepartment> employeeDepartments = employeeDepartmentManager.getTeamEmployees(filterParametrs.getDepartmentId());
            List<Integer> employeeIDs = new ArrayList<>();
            if (employeeDepartments != null && employeeDepartments.size() > 0) {
                for (EdsEmployeeDepartment employeeDepartment : employeeDepartments) {
                    if (employeeDepartment.getEmployee() != null) {
                        employeeIDs.add(employeeDepartment.getEmployee().getObjectID());
                    }
                }
                map.put("teamEmployeeIDs", employeeIDs);
                filterSql.append(" and er.reporter.objectID IN (:teamEmployeeIDs) ");
            }
        }
        if (filterParametrs.getEmployeeId() != null && filterParametrs.getEmployeeId() != 0) {
            filterSql.append(" and er.reporter.objectID=" + filterParametrs.getEmployeeId() + " ");
        }
        if (filterParametrs.getObjectIDs() != null && filterParametrs.getObjectIDs().size() > 0) {
            map.put("reporterIDs", filterParametrs.getObjectIDs());
            filterSql.append(" and er.reporter.objectID IN (:reporterIDs) ");
        }
        if (filterParametrs.isValidSearchKey()) {
            String key = filterParametrs.getSqlSearchKey();
            filterSql.append(" and (lower(er.reporter.firstName) like '" + key
                    + "' or lower(er.reporter.lastName) like '" + key
                    + "' or lower(er.title) like'" + key
                    + "' or lower(er.project.name) like '" + key + "') ");
        }
        sql.append(filterSql);

        addFilterSortingQuery(filterParametrs, sql);
        return findByNamedParams(sql.toString(), map);
    }

    @Override
    public LinkedHashMap<String, BigDecimal> getAllExpenseReportsChartData(ListingFilterParameter fp) {
        LinkedHashMap<String, BigDecimal> resultMap = new LinkedHashMap<>();
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();

        sql.append("select * from ");
        sql.append("(select a.name as name, sum(ex.subtotal) as total from " + companyID + ".expense ex\n");
        sql.append("left join " + companyID + ".expensereport er on er.id = ex.reportid\n");
        sql.append("left join " + companyID + ".account a on a.id = ex.accountid\n");
        sql.append("left join " + companyID + ".employee e on e.id = er.reporterid\n");
        sql.append("left join " + companyID + ".myuser mu on mu.id = e.id\n");
        sql.append("left join " + companyID + ".teamEmployee te on te.id = e.employeedepartmentid\n");
        sql.append("left join " + companyID + ".reference ref on ref.id = er.overallStatus\n");

        sql.append("where " + ServerUtils.checkForDeleted("ex.isdeleted") + "\n");
        sql.append("and " + ServerUtils.checkForDeleted("er.isdeleted") + "\n");
        sql.append("and ref.code in ('" + EXPENSE_APPROVED + "', '" + EXPENSE_PAID + "')\n");
        if (fp.getSelectedMonth() != null && fp.getSelectedYear() != null) {
            sql.append("and er.startdate is not null and extract(year from er.startdate) =" + fp.getSelectedYear() + "\n");
            sql.append("and extract(month from er.startdate) <=" + fp.getSelectedMonth() + "\n");
        }
        if (fp.getEmployeeId() != null && fp.getEmployeeId() != 0) {
            sql.append("and er.reporterid =" + fp.getEmployeeId() + "\n");
        }
        if (fp.getLocationId() != null) {
            sql.append("and mu.locationid is not null and mu.locationid =" + fp.getLocationId() + "\n");
        }
        if (fp.getDepartmentId() != null) {
            sql.append("and ((ex.department_id is not null and ex.department_id = " + fp.getDepartmentId() + ")\n");
            sql.append("or (ex.department_id is null and te.teamId = " + fp.getDepartmentId() + "))\n");
        }
        if (fp.getType() != null) {//Filterda 2015 tanlasa, HireDAte 2015 dagi employeelar YENGI, 1- 2014 31 Dec 2014 gacha OLD
            if (fp.getType() == 1) {//New Employees
                sql.append("and e.startdate is not null and extract(year from e.startdate) = " + fp.getSelectedYear() + "\n");
            } else if (fp.getType() == 2) {//Old Employees
                sql.append("and (e.startdate is null or extract(year from e.startdate) < " + fp.getSelectedYear() + ")\n");
            }
        }
        sql.append("group by a.name having sum(ex.subtotal) > 0) as res order by res.total desc\n");

        List<Object[]> objects = (List<Object[]>) findNative(sql.toString());
        if (objects != null && objects.size() > 0) {
            if (fp.getType() != null) {//for expeses chart
                for (Object[] object : objects) {
                    if (resultMap.containsKey(object[0])) {
                        resultMap.put((String) object[0], resultMap.get(object[0]).add((BigDecimal) object[1]));
                    } else {
                        resultMap.put((String) object[0], (BigDecimal) object[1]);
                    }
                }
            } else {//for expenses by category chart
                int i = 0;
                for (Object[] object : objects) {
                    if (i >= 9) {
                        if (resultMap.containsKey("Other")) {
                            resultMap.put("Other", resultMap.get("Other").add((BigDecimal) object[1]));
                        } else {
                            resultMap.put("Other", (BigDecimal) object[1]);
                        }
                    } else {
                        if (resultMap.containsKey(object[0])) {
                            resultMap.put((String) object[0], resultMap.get(object[0]).add((BigDecimal) object[1]));
                        } else {
                            resultMap.put((String) object[0], (BigDecimal) object[1]);
                        }
                    }
                    i++;
                }
            }
        }

        return resultMap;
    }

    private void addSortingRequiredParameters(ListingFilterParameter fp, StringBuilder sql) {
        if (fp != null && fp.getSortField() != null) {
            if (PROJECT_COLUMN.equals(fp.getSortField())) {
                sql.append("left join fetch er.project p ");
            } else if (REPORTER_COLUMN.equals(fp.getSortField())) {
                sql.append("left join fetch er.reporter repr ");
            } else if (APPROVER_COLUMN.equals(fp.getSortField())) {
                sql.append("left join fetch er.currentApprover appr ");
                sql.append("left join fetch appr.exactEmployee appre ");
            } else if (STATUS_COLUMN.equals(fp.getSortField())) {
                sql.append("left join fetch er.overallStatus s ");
            } else if (DUE_AMOUNT_COLUMN.equals(fp.getSortField())) {
                sql.append("left join fetch er.expenses ex ");
            }
        }
    }

    private void addFilterSortingQuery(ListingFilterParameter fp, StringBuilder sql) {
        if (fp != null && StringUtils.isNotBlank(fp.getSortField())) {
            String ascOrDesc = fp.getSortDir() == 2 ? "desc" : "";
            if (TITLE_COLUMN.equals(fp.getSortField())) {
                sql.append(" order by er.title " + ascOrDesc);
            } else if (PERIOD_COLUMN.equals(fp.getSortField())) {
                sql.append(" order by er.startDate " + ascOrDesc);
            } else if (PROJECT_COLUMN.equals(fp.getSortField())) {
                sql.append(" order by p.name " + ascOrDesc);
            } else if (REPORTER_COLUMN.equals(fp.getSortField())) {
                sql.append(" order by repr.firstName " + ascOrDesc + ", repr.lastName " + ascOrDesc);
            } else if (APPROVER_COLUMN.equals(fp.getSortField())) {
                sql.append(" order by appre.firstName " + ascOrDesc + ", appre.lastName " + ascOrDesc);
            } else if (STATUS_COLUMN.equals(fp.getSortField())) {
                sql.append(" order by s.name " + ascOrDesc);
            } else if (DUE_AMOUNT_COLUMN.equals(fp.getSortField())) {
                sql.append(" order by ex.subtotal " + ascOrDesc);
            }
        } else {
            sql.append(" order by er.startDate DESC");
        }
    }

    private Date getThisMonth(Date maxDate) {

        if (maxDate == null) {
            return null;
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(maxDate);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);

        maxDate = calendar.getTime();
        return maxDate;
    }

    public List<Integer> getCompanyDeletedExpenseReportListForSolr(SolrReindexRpc solrReindex) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select er.objectID from EdsExpenseReport er ");
        sqlQuery.append(" where er.isDeleted=true ");
        sqlQuery.append(" and er.lastUpdateTime>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            sqlQuery.append(" and er.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        EdsReference voidExpenseStatus = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_REVERSED);
        if (voidExpenseStatus != null) {
            sqlQuery.append(" and er.overallStatus.objectID !=" + voidExpenseStatus.getObjectID());
        }
        return (List<Integer>) find(sqlQuery.toString());
    }

    public List<EdsExpenseReport> getCompanyExpenseReportListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select er from EdsExpenseReport er ");
        sqlQuery.append(" where ").append(ServerUtils.checkForDeleted("er.isDeleted"));
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("updatedDate", solrReindex.getLastUpdateTime());
            sqlQuery.append(" and er.lastUpdateTime >= :updatedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sqlQuery.append(" and er.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sqlQuery.append(" order by er.objectID asc ");
        return findIntervalByNamedParams(sqlQuery.toString(), start, limit, params);
    }

    public List<Integer> getExpenseReportClaimsIdsByIDs(String ids) {
        return find("SELECT er.objectID FROM EdsExpenseReport er WHERE er.objectID IN(" + ids + ") and " + ServerUtils.checkForDeleted("er.isDeleted"));
    }

    public List<Integer> getExpenseReportClaimsIdsWithLimit(Integer startat, Integer limit) {
        return findLimited("select er.objectID from EdsExpenseReport er where er.objectID > ? and " + ServerUtils.checkForDeleted("er.isDeleted") + " and er.overallStatus.code != ? order by er.objectID ASC", limit, startat, EXPENSE_REVERSED);
    }

    @Override
    public List<EdsExpense> getPurchaseOrderRelatedExpenseItems(Integer purchaseOrderID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select e from EdsExpense e ");
        sql.append(" left join e.report.currentApprover.exactEmployee a2 ");
        sql.append(" left join e.report.overallStatus s2 ");
        sql.append(" where " + ServerUtils.checkForDeleted("e.report.isDeleted") + " ");
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PO_IN_LINE_ITEM_ENABLE)) {
            sql.append("and e.purchaseOrder.objectID = ? and ");
        } else {
            sql.append("and e.report.purchaseOrder.objectID = ? and ");
        }
        sql.append(" a2 is not null and (s2.code = ? or s2.code = ? or s2.code = ?) ");
        return (List<EdsExpense>) find(sql.toString(), purchaseOrderID, EXPENSE_APPROVED, EXPENSE_PAID, PARTIALLY_PAID);
    }

    public HashMap<Integer, BigDecimal> getExpensesAllocatedToPO(Integer purchaseOrderID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select e from EdsExpense e \n");
        sql.append(" where " + ServerUtils.checkForDeleted("e.report.isDeleted") + " ");
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PO_IN_LINE_ITEM_ENABLE)) {
            sql.append("and e.purchaseOrder.objectID = ? ");
        } else {
            sql.append("and e.report.purchaseOrder.objectID = ? ");
        }
        List<EdsExpense> expenses = find(sql.toString(), purchaseOrderID);
        HashMap<Integer, BigDecimal> allocatedExpenses = new HashMap<>();
        for (EdsExpense exp : expenses) {
            allocatedExpenses.put(exp.getObjectID(), exp.getBaseSubtotal());
        }
        return allocatedExpenses;
    }

    @Override
    public boolean isUsedForInvoices(Integer expenseID) {
        return find("select e.invoice from EdsExpense e where e.objectID=?", expenseID).size() > 0;
    }

    @Override
    public void updateExpenseReport(Integer opportunityID, Integer projectID) {
        update("UPDATE EdsExpenseReport er SET er.project.objectID ='" + projectID + "' WHERE er.opportunity.objectID = '" + opportunityID + "' ");
    }

    @Override
    public void updateExpensesByPayslipTableID(Integer payslipTableItemID) {
        update("update EdsExpenseReport er set er.payslipTableItemID=null where er.payslipTableItemID=?", payslipTableItemID);
    }

    @Override
    public void removeRelatedPO(Integer objectID) {
        update("update EdsExpenseReport er set er.purchaseOrder=null where er.purchaseOrder.objectID=?", objectID);
    }

    @Override
    public void mergeExpenseItemWithOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID) {
        updateNative("UPDATE " + getCompanyId() + ".expense SET client_id = " + newAccountID + " WHERE client_id in (" + ServerUtils.getAsCommoDelimited(oldAccountIDs, "0", ",") + ")");
    }

    @Override
    public List<EdsExpenseReport> getExpensesByCrmAccountID(Integer accId) {
        return find("select er from EdsExpenseReport er left join er.supplier s where (er.isDeleted is null or er.isDeleted = false) and s.objectID = ?", accId);
    }

    public List<EdsExpenseReport> getNotFullyPaidExpenses(Integer supplierId, Integer currencyId, boolean isMultiCurrencyEnabled) {
        StringBuilder sql = new StringBuilder();

        Integer expenseApproved = referenceManager.findReferenceId(EXPENSE_STATUS, EXPENSE_APPROVED);
        Integer expensePartiallyPaid = referenceManager.findReferenceId(EXPENSE_STATUS, PARTIALLY_PAID);

        sql.append("select er.* ")
                .append(" from ").append(getCompanyId()).append(".expensereport er ")
                .append(" left join ").append(getCompanyId()).append(".crmaccount ca on ca.id = er.supplierid ")
                .append(" where (er.isdeleted is null or er.isdeleted <> true) ")
                .append(" and er.overallstatus in (").append(expenseApproved).append(",").append(expensePartiallyPaid).append(") ");

        if (currencyId != null) {
            if (isMultiCurrencyEnabled) {
                EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();

                if (!currencyId.equals(fs.getCurrency().getObjectID())) {
                    List<Integer> currencyIDs = new LinkedList<>();
                    currencyIDs.add(fs.getCurrency().getObjectID());
                    currencyIDs.add(currencyId);

                    sql.append(" and er.currencyID in (").append(ServerUtils.getAsCommoDelimited(currencyIDs, "0")).append(") ");
                }
            } else {
                sql.append(" and er.currencyID=" + currencyId + " ");
            }
        }
        if (supplierId != null) {
            sql.append(" and er.supplierid = ").append(supplierId).append(" ");
            sql.append(" and er.isCompanyExpense = true ");
        }
        return findNative(sql.toString(), EdsExpenseReport.class);
    }

    @Override
    public List<EdsProject> getExpenseProjects(DateNonConvertable startPeriod, DateNonConvertable endPeriod, ListingFilterParameter fp) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct p.* from ").append(getCompanyId()).append(".timesheet tsh \n");
        sql.append("join ").append(getCompanyId()).append(".reference s on s.id = tsh.statusId \n");
        sql.append("join ").append(getCompanyId()).append(".project p on p.id = tsh.projectID \n");
        sql.append("join ").append(getCompanyId()).append(".task t on t.id = tsh.taskId \n");
        sql.append("where (tsh.usedInExpense is null or tsh.usedInExpense is false) and s.code = '_APPROVE' and t.deleted is not true \n");

        if (startPeriod != null && endPeriod != null) {
            sql.append("and tsh.date between '").append(dateFormat.format(startPeriod.getNonConvertedDate())).append("' and '").append(dateFormat.format(endPeriod.getNonConvertedDate())).append("' \n");
        }
        if (fp.getClientId() != null) {
            sql.append("and p.clientId = ").append(fp.getClientId()).append(" \n");
        }
        if (fp.getEmployeeId() != null) {
            sql.append("and tsh.employeeid = ").append(fp.getEmployeeId()).append(" \n");
        }
        sql.append(" order by p.name ");
        return findNative(sql.toString(), EdsProject.class);
    }

    @Override
    public List<EdsCrmAccount> getEmployeeClients(Integer employeeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct c.* from ").append(getCompanyId()).append(".timesheet tsh \n");
        sql.append("join ").append(getCompanyId()).append(".reference s on s.id = tsh.statusId \n");
        sql.append("join ").append(getCompanyId()).append(".project p on p.id = tsh.projectID \n");
        sql.append("join ").append(getCompanyId()).append(".crmaccount c on c.id = p.clientId \n");
        sql.append("where tsh.usedInExpense = false and s.code = '_APPROVE' \n");
        sql.append("and tsh.employeeID = ").append(employeeId).append(" order by c.name ");

        return findNative(sql.toString(), EdsCrmAccount.class);
    }

    @Override
    public Integer getLastIntNumber() {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("select exp.intNumber from EdsExpenseReport exp where (exp.isDeleted = false or exp.isDeleted is null) and exp.intNumber is not null ");
        Calendar financialYearStart = accountingManager.getFinancialYearStartIfEnabled(null);

        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            sql.append(" and exp.startDate >= :financialYearStart");
        }
        sql.append(" order by exp.intNumber desc");
        return (Integer) findSingleByNamedParams(sql.toString(), values);
    }


    public boolean isExpenseNumberExists(String number, Integer expenseId, Date date) {
        if (StringUtils.isNotBlank(number)) {
            StringBuilder sql = new StringBuilder();
            Map<String, Object> values = new HashMap<>();
            Calendar financialYearStart = accountingManager.getFinancialYearStartIfEnabled(date);

            if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
                Calendar financialYearEnd = new GregorianCalendar();
                financialYearEnd.setTime(financialYearStart.getTime());
                financialYearEnd.set(Calendar.YEAR, financialYearEnd.get(Calendar.YEAR) + 1);

                values.put("financialYearStart", financialYearStart.getTime());
                values.put("financialYearEnd", financialYearEnd.getTime());
            }

            if (expenseId == null) {
                sql.append("select exp.objectID from EdsExpenseReport exp where (exp.isDeleted is null or exp.isDeleted<>true) and exp.number = :number");
                values.put("number", number);

                if (values.get("financialYearStart") != null && values.get("financialYearEnd") != null) {
                    sql.append(" and exp.startDate between :financialYearStart and :financialYearEnd ");
                }

                return findByNamedParams(sql.toString(), values).size() > 0;
            } else {
                sql.append("select exp.objectID from EdsExpenseReport exp where (exp.isDeleted is null or exp.isDeleted<>true) and exp.number = :number and exp.objectID <> :expenseId");

                if (values.get("financialYearStart") != null && values.get("financialYearEnd") != null) {
                    sql.append(" and exp.startDate between :financialYearStart and :financialYearEnd ");
                }
                values.put("number", number);
                values.put("expenseId", expenseId);

                return findByNamedParams(sql.toString(), values).size() > 0;
            }
        }
        return false;

    }

    @Override
    public BankTransferNumberData generateNewNumber(BankTransferNumberData numberData) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();

        Calendar financialYearStart = accountingManager.getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            Calendar financialYearEnd = new GregorianCalendar();
            financialYearEnd.setTime(financialYearStart.getTime());
            financialYearEnd.set(Calendar.YEAR, financialYearEnd.get(Calendar.YEAR) + 1);

            values.put("financialYearStart", financialYearStart.getTime());
            values.put("financialYearEnd", financialYearEnd.getTime());
        }

        sql.append("SELECT MAX(exp.intNumber) FROM EdsExpenseReport exp ");
        sql.append(" where (exp.isDeleted is null or exp.isDeleted<>true) and exp.intNumber IS NOT NULL");
        if (values.get("financialYearStart") != null && values.get("financialYearEnd") != null) {
            sql.append(" and exp.startDate between :financialYearStart and :financialYearEnd ");
        }

        Integer lastIntNumber = (Integer) findSingleByNamedParams(sql.toString(), values);

        lastIntNumber = (lastIntNumber != null ? lastIntNumber : 0) + 1;
        String formattedNumber = EdsNumberingSettings.decimalFormat.format(lastIntNumber);
        String number = numberData.getPrefix().concat(formattedNumber);
        if (numberData.isWithDate()) {
            number = number.concat("-").concat(numberData.getDate());
        }

        sql = new StringBuilder();
        boolean exit = true;
        sql.append("SELECT exp.number FROM EdsExpenseReport exp WHERE exp.number = :number and (exp.isDeleted is null or exp.isDeleted<>true)");
        if (values.get("financialYearStart") != null && values.get("financialYearEnd") != null) {
            sql.append(" and exp.startDate between :financialYearStart and :financialYearEnd ");
        }
        values.put("number", number);

        while (exit) {
            String checkNumberExists = (String) findSingleByNamedParams(sql.toString(), values);
            if (checkNumberExists == null || checkNumberExists.isEmpty()) {
                exit = false;
                numberData.setFourDigitNumber(formattedNumber);
                return numberData;
            } else {
                lastIntNumber++;
                formattedNumber = EdsNumberingSettings.decimalFormat.format(lastIntNumber);
                number = numberData.getPrefix().concat(formattedNumber);
                if (numberData.isWithDate()) {
                    number = number.concat("-").concat(numberData.getDate());
                }
                values.replace("number", number);
            }
        }
        return numberData;
    }
}