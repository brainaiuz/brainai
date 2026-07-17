package com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyGovSalaryDto {
    private String companyName;
    private int year;
    private int period;
    private Double salary;
    private Double salaryTaxSum;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getSalaryTaxSum() {
        return salaryTaxSum;
    }

    public void setSalaryTaxSum(Double salaryTaxSum) {
        this.salaryTaxSum = salaryTaxSum;
    }
}
