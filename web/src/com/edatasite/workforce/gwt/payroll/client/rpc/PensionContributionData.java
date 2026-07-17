package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/13/14
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionContributionData implements IsSerializable {

    private String employeeFullName;
    private String month;
    private BigDecimal basicSalary;
    private Integer companyPensionType;
    private BigDecimal companyPensionRate;
    private BigDecimal employeePensionAmount;

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Integer getCompanyPensionType() {
        return companyPensionType;
    }

    public void setCompanyPensionType(Integer companyPensionType) {
        this.companyPensionType = companyPensionType;
    }

    public BigDecimal getCompanyPensionRate() {
        return companyPensionRate;
    }

    public void setCompanyPensionRate(BigDecimal companyPensionRate) {
        this.companyPensionRate = companyPensionRate;
    }

    public BigDecimal getEmployeePensionAmount() {
        return employeePensionAmount;
    }

    public void setEmployeePensionAmount(BigDecimal employeePensionAmount) {
        this.employeePensionAmount = employeePensionAmount;
    }
}
