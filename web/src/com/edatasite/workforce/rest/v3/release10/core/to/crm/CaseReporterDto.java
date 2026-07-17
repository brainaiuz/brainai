package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;

import javax.validation.constraints.Pattern;

public class CaseReporterDto {
    @Pattern(regexp = "Contact|Account|Lead|Other",
            message = "reported by type must be one of Contact/Account/Lead/Other")
    private String reportedByType;

    //for contact/account/lead types
    private IdCode reporter;

    //for other type
    private String firstName;
    private String lastName;
    private String company;
    private String email;
    private String phone;
    private String fax;

    public CaseReporterDto() {
    }

    public CaseReporterDto(String reportedByType, IdCode reporter, String firstName, String lastName, String company, String email, String phone, String fax) {
        this.reportedByType = reportedByType;
        this.reporter = reporter;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.email = email;
        this.phone = phone;
        this.fax = fax;
    }

    public String getReportedByType() {
        return reportedByType;
    }

    public void setReportedByType(String reportedByType) {
        this.reportedByType = reportedByType;
    }

    public IdCode getReporter() {
        return reporter;
    }

    public void setReporter(IdCode reporter) {
        this.reporter = reporter;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaseReporterDto)) return false;

        CaseReporterDto that = (CaseReporterDto) o;

        if (getReportedByType() != null ? !getReportedByType().equals(that.getReportedByType()) : that.getReportedByType() != null)
            return false;
        if (getReporter() != null ? !getReporter().equals(that.getReporter()) : that.getReporter() != null)
            return false;
        if (getFirstName() != null ? !getFirstName().equals(that.getFirstName()) : that.getFirstName() != null)
            return false;
        if (getLastName() != null ? !getLastName().equals(that.getLastName()) : that.getLastName() != null)
            return false;
        if (getCompany() != null ? !getCompany().equals(that.getCompany()) : that.getCompany() != null) return false;
        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
        if (getPhone() != null ? !getPhone().equals(that.getPhone()) : that.getPhone() != null) return false;
        if (getFax() != null ? !getFax().equals(that.getFax()) : that.getFax() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getReportedByType() != null ? getReportedByType().hashCode() : 0;
        result = 31 * result + (getReporter() != null ? getReporter().hashCode() : 0);
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getCompany() != null ? getCompany().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + (getFax() != null ? getFax().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CaseReporterDto{" +
                "reportedByType='" + reportedByType + '\'' +
                ", reporter=" + reporter +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", company='" + company + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                '}';
    }
}
