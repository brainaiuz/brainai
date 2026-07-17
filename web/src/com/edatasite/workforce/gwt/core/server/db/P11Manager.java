package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.payrolluk.P11;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface P11Manager extends Manager<P11> {

    List<P11> getPayslips(Date datefrom, Date dateto);

    List<P11> getPayslips(Date paymentDate);

    List<P11> getAllPayslips(ListingFilterParameter fp);

    List<P11> getP11Form(Date from, Date to, EdsEmployee employee);

    P11 getP11ForPeriodInYear(Integer employeeId, Integer frequency, Integer payPeriod, Date beginningOfCurrentYear);

    boolean hasP11ForThisPeriod(Integer employeeId, Integer frequency, Integer payPeriod, Integer beginningOfCurrentYear);

    List<P11> getP11ListForThisPeriod(Integer employeeId, Integer frequency, Integer payPeriod, Integer taxYearStart);

    BigDecimal getLastTotalPayToDate(Integer employeeId, Date date);

    BigDecimal getLastTotalTaxDueToDate(Integer employeeId, Date date);

    P11 getLastP11Item(EdsEmployee emp, Integer payPeriod, Integer payeyear);

    P11 getLastP11Item(EdsEmployee emp);

    int getP11ItemsCount(Integer employeeID);

    int getPreviousP11ItemsCount(Integer employeeID, Date date);

    BigDecimal getTotalPayThisEmployment(EdsEmployee emp, Integer taxYear);

    BigDecimal getTotalTaxThisEmployment(EdsEmployee emp, Integer taxYear);

    BigDecimal getTotalPayThisTaxYear(EdsEmployee employee, Integer taxYear);

    BigDecimal getTotalTaxThisTaxYear(EdsEmployee employee, Integer taxYear);

    Date getPayDate(EdsEmployee employee, Date from, Date to);

    List<P11> getEmployeePayslips(EdsEmployee employee, ListingFilterParameter fp);

    BigDecimal getStatutoryPaymentsReceived(Integer employeeID, Date startDate, Date endDate, String category);

    Date getSSPReceivedDate(Integer employeeID, Date startDate, Date endDate);

    P11 getAdvancePayslip(Integer parentPayslipId, Integer period);

    List<P11> getAdvancePayslips(Integer parentPayslipId);

    List<P11> getEmployeeLastPayslip(Integer employeeId);

    List getP14Employees(Date fromDate, Date toDate, Integer companyID);

    List getP35Summary(Date fromDate, Date toDate, Integer companyID);

    List<P11> getPayslipsForRollback(Integer frequency, Integer period, Integer taxYear, Integer employeeID, Integer companyID);

    Integer getMinTaxYear();

    BigDecimal getUnpaidTaxRefunds(Integer employeeID, Date date, boolean setIsOnIndustrialActionToFalse);

    BigDecimal getTotalPayToDate(Integer employeeID, Date date, Integer year);
}

