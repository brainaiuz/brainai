package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.P11;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.P11Manager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("p11Manager")
public class P11ManagerImpl extends BaseManager<P11> implements P11Manager {

    @Autowired
    private EmployeePayrollSettingsManager employeePayrollSettingsManager;

    public P11ManagerImpl() {
        super(P11.class);
    }

    public List<P11> getPayslips(Date datefrom, Date dateto) {
        return find("SELECT pt FROM EdsPayslip pt WHERE " + ServerUtils.checkForDeleted("pt.deleted") + " AND pt.datefrom = ? AND pt.dateto = ?", datefrom, dateto);
    }

    public List<P11> getPayslips(Date paymentDate) {
        String sql = "SELECT pt FROM EdsPayslip pt where " + ServerUtils.checkForDeleted("pt.deleted") + " and (? between pt.datefrom and pt.dateto)";
        return find(sql, paymentDate);
    }

    public List<P11> getAllPayslips(ListingFilterParameter fp) {
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT pt FROM P11 pt WHERE " + ServerUtils.checkForDeleted("pt.deleted") + " ");
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(pt.employee.firstName) like '" + fp.getSqlSearchKey() + "'");
            sql.append(" or lower(pt.employee.lastName) like '" + fp.getSqlSearchKey() + "') ");
        }
        if (fp.getSortField() != null && fp.getSortField() != "") {
            sql.append("ORDER BY ");
            if (fp.getSortField().equals("employee")) {
                sql.append("pt.employee.firstName ");
            }
            if (fp.getSortField().equals("payperiod")) {
                sql.append("pt.payPeriod ");
            }
            if (fp.getSortField().equals("paymentmethod")) {
                sql.append("pt.paymentmethod ");
            }
            if (fp.getSortField().equals("grosspay")) {
                sql.append("pt.grossPayInPeriod ");
            }
            if (fp.getSortField().equals("niEmployee")) {
                sql.append("pt.employeeNI ");
            }
            if (fp.getSortField().equals("totalPay")) {
                sql.append("pt.totalPayToDate ");
            }
            if (fp.getSortField().equals("totalTax")) {
                sql.append("pt.totalTax ");
            }
            if (fp.getSortField().equals("netpay")) {
                sql.append("pt.netPay ");
            }
            if (fp.getSortField().equals("status")) {
                sql.append("pt.status ");
            }
            if (fp.getSortField().equals("date")) {
                sql.append("pt.date ");
            }
            if (fp.getSortField().equals("incomeTax")) {
                sql.append("pt.tax ");
            }


            if (!fp.isAscending()) {
                sql.append("desc");
            }
        } else {
            sql.append("ORDER BY  pt.date asc");
        }
        return find(sql.toString());
    }


    /**
     * Inner Joins 3 (P11, P11NI, P11Tax tables)
     *
     * @param from
     * @param to
     * @param employee
     * @return
     */
    public List<P11> getP11Form
    (Date
             from, Date
            to, EdsEmployee
            employee) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("from", from);
        map.put("to", to);
        map.put("employee", employee);
        return (List<P11>) findByNamedParams("select p11 " +
                "from P11 p11 " +
                "where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.employee=:employee and p11.date between :from and :to " +
                "order by p11.payPeriod asc, p11.objectID asc", map);
    }

    public P11 getP11ForPeriodInYear(Integer employeeId, Integer frequency, Integer payPeriod, Date beginningOfCurrentYear) {
        Map<String, Object> map = new HashMap<>();
        map.put("frequency", frequency);
        map.put("payPeriod", payPeriod);
        map.put("date", beginningOfCurrentYear);
        return (P11) findSingleByNamedParams("from P11 p11 where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.frequency=:frequency " +
                "and p11.payPeriod=:payPeriod and p11.date > :date", map);
    }

    public boolean hasP11ForThisPeriod(Integer employeeId, Integer frequency, Integer payPeriod, Integer taxYearStart) {
        final Map<String, Object> map = new HashMap<>();
        map.put("employeeId", employeeId);
        map.put("frequency", frequency);
        map.put("payPeriod", payPeriod);
        map.put("year", taxYearStart);
        return ((BigInteger) findNativeSingleByNamedParams("SELECT COUNT(*) FROM " + getCompanyId() + ".payslip p11 " +
                "WHERE " + ServerUtils.checkForDeleted("p11.deleted") + " AND p11.employeeID =:employeeId AND p11.Frequency=:frequency " +
                "   AND p11.Period=:payPeriod AND p11.payeyear = :year", map)).intValue() > 0;
    }

    public List<P11> getP11ListForThisPeriod(Integer employeeId, Integer frequency, Integer payPeriod, Integer taxYearStart) {
        final Map<String, Object> map = new HashMap<>();
        map.put("employeeId", employeeId);
        map.put("frequency", frequency);
        map.put("payPeriod", payPeriod);
        map.put("year", taxYearStart);
        return find("SELECT DISTINCT p FROM P11 p where " + ServerUtils.checkForDeleted("p.deleted") + " AND p.employee.objectID = ? AND p.frequency = ? AND p.payPeriod = ? AND date_part('year',p.date) >= ?",
                employeeId, frequency, payPeriod, taxYearStart);
    }

    public BigDecimal getLastTotalPayToDate(Integer employeeId, Date date) {
        try {
            final Map<String, Object> map = new HashMap<>();
            map.put("employeeId", employeeId);
            map.put("date", date);
            Object object = findNativeSingleByNamedParams("SELECT totalPayToDate FROM " + getCompanyId() + ".payslip p11 " +
                    "WHERE " + ServerUtils.checkForDeleted("p11.deleted") + " AND p11.employeeID = :employeeId AND p11.date < :date AND totalPayToDate IS NOT NULL " +
                    "ORDER BY p11.date DESC", map);
            return object != null ? (BigDecimal) object : BigDecimal.ZERO;
        } catch (DataAccessException e) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getLastTotalTaxDueToDate(Integer employeeId, Date date) {
        try {
            final Map<String, Object> map = new HashMap<>();
            map.put("employeeId", employeeId);
            map.put("date", date);
            Object object = findNativeSingleByNamedParams("SELECT totalTaxDue FROM " + getCompanyId() + ".payslip p11 " +
                    "WHERE " + ServerUtils.checkForDeleted("p11.deleted") + " AND p11.employeeID = :employeeId AND p11.date < :date AND totalTaxDue IS NOT NULL " +
                    "ORDER BY p11.date DESC ", map);
            return object != null ? (BigDecimal) object : BigDecimal.ZERO;
        } catch (DataAccessException e) {
            return BigDecimal.ZERO;
        }
    }

    public P11 getLastP11Item(EdsEmployee emp, Integer payPeriod, Integer payeyear) {
        if (findSingle("from P11 p11 where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.payPeriod = ?", payPeriod) == null) {
            return (P11) findSingle("from P11 p11  where p11.objectID =  " +
                    "(select max(p11.objectID) from P11 p11 where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.employee =?)", emp);
        } else {
            return (P11) findSingle("from P11 p11 where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.payPeriod < " +
                    "(from P11 p112 where " + ServerUtils.checkForDeleted("p112.deleted") + " and p112.payPeriod = " + payPeriod + " and p112.employee.objectID = " + emp.getObjectID() + ") and p11.employee.objectID = " + emp.getObjectID() + " order by p11.payPeriod desc");
        }
    }

    public P11 getLastP11Item(EdsEmployee emp) {
        return (P11) findSingle("from P11 p11 where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.objectID =  " +
                "(select max(p11.objectID) from P11 p11 where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.employee =?)", emp);
    }

    public int getP11ItemsCount(Integer employeeID) {
        return ((BigInteger) findNativeSingle("SELECT COUNT(*) FROM " + getCompanyId() + ".payslip WHERE " + ServerUtils.checkForDeleted("deleted") + " AND employeeID =?", employeeID)).intValue();
    }

    public int getPreviousP11ItemsCount(Integer employeeID, Date date) {
        return ((BigInteger) findNativeSingle("SELECT COUNT(*) FROM " + getCompanyId() + ".payslip as p11 WHERE " + ServerUtils.checkForDeleted("p11.deleted") + " AND p11.employeeID =? AND p11.date < ? ", employeeID, date)).intValue();
    }

    /*
      taxYear is the end of tax year, i.e. for 20010-2011 tax year it should be 2011
    */

    public BigDecimal getTotalPayThisEmployment(EdsEmployee emp, Integer taxYear) {
        return (BigDecimal) findSingle("select sum(p11.grossPayInPeriod) from P11 p11 where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.employee =? " + (taxYear != null ? "and p11.year=" + taxYear : "") + ") ", emp);
    }

    public BigDecimal getTotalTaxThisEmployment(EdsEmployee emp, Integer taxYear) {
        return (BigDecimal) findSingle("select sum(p11.taxDeductedRefunded) from P11 p11 where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.employee = ? " + (taxYear != null ? "and p11.year=" + taxYear : "") + ")", emp);
    }

    @Override
    public BigDecimal getTotalPayThisTaxYear(EdsEmployee employee, Integer taxYear) {
        /* if employee was employed in current tax year, add total pay & tax accumulated from previous employment */
        final BigDecimal totalPayThisEmployment = getTotalPayThisEmployment(employee, taxYear);
        if (employee.getStartDate() != null && employee.getStartDate().after(PayrollUtils.getBeginningOfTaxYear(employee.getEndDate()).getTime())) {
            final EdsEmployeePayrollSettings totalPayPreviousEmployment = employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), Constants.TOTAL_PAY_TO_DATE);
            if (totalPayPreviousEmployment != null && totalPayPreviousEmployment.getValue() != null && totalPayPreviousEmployment.getValue().trim().length() > 0) {
                return totalPayThisEmployment.add(new BigDecimal(totalPayPreviousEmployment.getValue()));
            }
        }
        return totalPayThisEmployment;
    }

    @Override
    public BigDecimal getTotalTaxThisTaxYear(EdsEmployee employee, Integer taxYear) {
        final BigDecimal totalTaxThisEmployment = getTotalTaxThisEmployment(employee, taxYear);
        if (employee.getStartDate() != null && employee.getStartDate().after(PayrollUtils.getBeginningOfTaxYear(employee.getEndDate()).getTime())) {
            final EdsEmployeePayrollSettings totalTaxPreviousEmployment = employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), Constants.TOTAL_TAX_TO_DATE);
            if (totalTaxPreviousEmployment != null && totalTaxPreviousEmployment.getValue() != null && totalTaxPreviousEmployment.getValue().trim().length() > 0) {
                return totalTaxThisEmployment.add(new BigDecimal(totalTaxPreviousEmployment.getValue()));
            }
        }
        return totalTaxThisEmployment;
    }

    public Date getPayDate(EdsEmployee employee, Date from, Date to) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("employee", employee);
        if (from != null) {
            params.put("from", from);
        }
        params.put("to", to);
        return (Date) findSingleByNamedParams("select " + (from != null ? "min" : "max") + "(p.date) from P11 p where " + ServerUtils.checkForDeleted("p.deleted") + " and p.employee=:employee and p.date < :to" + (from != null ? " and p.date>=:from" : ""), params);
    }

    public List<P11> getEmployeePayslips(EdsEmployee employee, ListingFilterParameter fp) {
        final StringBuilder sql = new StringBuilder();
        if (fp.getSqlSearchKey() != null) {
            sql.append("and ( p11.employee.firstname like '" + fp.getSqlSearchKey() + "'");
            sql.append("or p11.employee.lastname like '" + fp.getSqlSearchKey() + "'");
            sql.append("or p11.paymentmethod like '" + fp.getSqlSearchKey() + "')");
        }
        return find("select p11 from P11 p11 where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.employee = ? " + sql + " order by p11.date desc, p11.payPeriod desc", employee);
    }

    public BigDecimal getStatutoryPaymentsReceived(Integer employeeID, Date startDate, Date endDate, String category) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("employeeID", employeeID);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        final BigDecimal result = (BigDecimal) findSingleByNamedParams("SELECT SUM(p." + category.toLowerCase() + ") FROM P11 p WHERE " +
                ServerUtils.checkForDeleted("p.deleted") + " AND p.employee.objectID = :employeeID AND (p.date >= :startDate AND p.date <= :endDate) ", params);
        return result != null ? result : BigDecimal.ZERO;
    }

    public Date getSSPReceivedDate(Integer employeeID, Date startDate, Date endDate) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("employeeID", employeeID);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return (Date) findSingleByNamedParams("SELECT MAX(p.date) FROM P11 p WHERE " + ServerUtils.checkForDeleted("p.deleted") + " AND p.ssp>0 AND " +
                "p.employee.objectID = :employeeID AND (p.date >= :startDate AND p.date <= :endDate) ", params);
    }

    public P11 getAdvancePayslip(Integer parentPayslipId, Integer period) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("parentID", parentPayslipId);
        params.put("period", period);
        return (P11) findSingleByNamedParams("SELECT p FROM P11 p where " + ServerUtils.checkForDeleted("p.deleted") + " AND p.parent.objectID = :parentID AND " +
                "p.payPeriod = :period", params);
    }

    public List<P11> getAdvancePayslips(Integer parentPayslipId) {
        return find("SELECT p FROM P11 p where " + ServerUtils.checkForDeleted("p.deleted") + " and p.parent.objectID = " + parentPayslipId);
    }

    public List<P11> getEmployeeLastPayslip(Integer employeeId) {
        return findLimited("SELECT p FROM P11 p where " + ServerUtils.checkForDeleted("p.deleted") + " and p.employee.objectID = " + employeeId + " order by p.date desc", 1);
    }

    public List getP14Employees(Date fromDate, Date toDate, Integer companyID) {
        final Map<String, Object> map = new HashMap<>();
        String companyId = "\"" + companyID + "\"";
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT p.employeeid, " +
                "   eps2.value as taxcode," +
                "   COALESCE(SUM(CASE WHEN p.tax > 0 THEN p.tax ELSE 0 END), 0) as incometax, " +
                "   eps.value as nino, " +
                "   u.firstname," +
                "   u.lastname," +
                "   COALESCE(SUM(p.niee), 0) as NIC" +
                " FROM " + companyId + ".payslip p INNER JOIN  " + companyId + ".myuser u ON p.employeeid=u.id \n" +
                " INNER JOIN " + companyId + ".employeepayrollsettings eps ON eps.employeeid=p.employeeid AND eps.key='NI_NUMBER' \n" +
                " INNER JOIN " + companyId + ".employeepayrollsettings eps2 ON eps2.employeeid=p.employeeid and eps2.key='TAX_CODE' " +
                " WHERE " + ServerUtils.checkForDeleted("p.deleted") + " AND p.date>=:fromDate AND p.date<:toDate\n" +
                " GROUP BY p.employeeid, eps2.value, eps.value, u.firstname, u.lastname \n" +
                " ORDER BY p.employeeid");
        return findNativeByNamedParams(sb.toString(), map);
    }

    public List getP35Summary(Date fromDate, Date toDate, Integer companyID) {
        Map<String, Object> map = new HashMap<>();
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        String companyId = "\"" + companyID + "\"";
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT " +
                "   COALESCE(SUM(p.tax), 0) as tax, " +
                "   COALESCE(SUM(p.niTotal), 0) as nic, " +
                "   COALESCE(SUM(p.studentLoanDeductions), 0) as studentLoanDeductions, " +
                "   COALESCE(SUM(p.ssp), 0) as ssp," +
                "   COALESCE(SUM(p.smp), 0) as smp," +
                "   COALESCE(SUM(p.spp), 0) as spp," +
                "   COALESCE(SUM(p.sap), 0) as sap" +
                " FROM " + companyId + ".payslip p INNER JOIN " + companyId + ".myuser u ON p.employeeid=u.id \n" +
                " WHERE " + ServerUtils.checkForDeleted("p.deleted") + " AND p.date>=:fromDate AND p.date<:toDate");
        return findNativeByNamedParams(sb.toString(), map);
    }

    public List<P11> getPayslipsForRollback(Integer frequency, Integer period, Integer taxYear, Integer employeeID, Integer companyID) {
        final boolean weekly = Frequency.getByID(frequency).getCycle() == Calendar.WEEK_OF_YEAR;
        final String f = String.valueOf(weekly ? Frequency.WEEKLY.getId() + ", " + Frequency.WEEKLYx2.getId() + ", " + Frequency.WEEKLYx4.getId() : frequency);

        StringBuilder query = new StringBuilder();
        query.append("select p from P11 p where " + ServerUtils.checkForDeleted("p.deleted") + " ");
        if (employeeID != null) {
            query.append(" and p.employee.objectID = " + employeeID);
        }
        query.append(" and p.frequency IN (" + f + ")");
        query.append(" and p.payPeriod = " + period);
        query.append(" and p.year = " + taxYear);

        return (List<P11>) find(query.toString());
    }

    public Integer getMinTaxYear() {
        final String sql = "SELECT MIN(p11.year) FROM P11 p11 WHERE " + ServerUtils.checkForDeleted("p11.deleted");
        return (Integer) findSingle(sql);
    }

    @Override
    public BigDecimal getUnpaidTaxRefunds(Integer employeeID, Date date, boolean setIsOnIndustrialActionToFalse) {
        final BigDecimal refund = (BigDecimal) findSingle("select SUM(p11.taxDeductedRefunded) from P11 p11 " +
                "where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.isOnIndustrialAction=true and p11.grossPayInPeriod=0 and p11.taxDeductedRefunded<0 and p11.employee.objectID=? and p11.date<?", employeeID, date);
        if (setIsOnIndustrialActionToFalse) {
            update("update P11 p11 set p11.isOnIndustrialAction=false " +
                    "where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.grossPayInPeriod=0 and p11.taxDeductedRefunded<0 and p11.employee.objectID=? and p11.date<?", employeeID, date);
        }
        return refund;
    }

    ;

    @Override
    public BigDecimal getTotalPayToDate(Integer employeeID, Date date, Integer year) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(p.netpay) from ").append(getCompanyId()).append(".payslip p ");
        sql.append("left join " + getCompanyId() + ".reference r on r.id=p.statusid ");
        sql.append("where r.code='COMMITED' and ");
        sql.append(ServerUtils.checkForDeleted("p.deleted"));
        sql.append(" and p.employeeID=").append(employeeID);
        sql.append(" and p.date<='").append(date);
        sql.append("' and cast(date_part('year', p.date) as integer)=").append(year);
        return (BigDecimal) findNativeSingle(sql.toString());
        // return (BigDecimal) findSingle("select SUM(p11.netPay) from P11 p11 where " + ServerUtils.checkForDeleted("p11.deleted") + " and p11.status.code='COMMITED' and p11.employee.objectID=? and p11.date<? ", employeeID, date);
    }
}
