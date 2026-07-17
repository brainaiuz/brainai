package com.edatasite.workforce.gwt.core.client.rpc.payroll;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class EmployeeSalary implements IsSerializable {

    BigDecimal salary;
    BigDecimal workedDays = BigDecimal.ZERO;
    Date fromDate;
    Date toDate;

    public EmployeeSalary(BigDecimal salary, BigDecimal workedDays) {
        this.salary = salary;
        this.workedDays = workedDays;
    }

    public BigDecimal calculateSalary(BigDecimal numberOfWorkDays) {
        if (salary == null) {
            return BigDecimal.ZERO;
        } else {
            try {
                return salary.multiply(workedDays).divide(numberOfWorkDays, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            } catch (ArithmeticException e) {
                return BigDecimal.ZERO;
            }
        }
    }

    public BigDecimal calculateSalary(BigDecimal workedDays, BigDecimal numberOfWorkDays) {
        if (salary == null) {
            return BigDecimal.ZERO;
        } else {
            try {
                return salary.multiply(workedDays).divide(numberOfWorkDays, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            } catch (ArithmeticException e) {
                return BigDecimal.ZERO;
            }
        }
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
