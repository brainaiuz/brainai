package com.edatasite.workforce.gwt.trainingcenter.server;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/20/12
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseScheduleStudent implements Serializable {

    private String number;
    private String firstName;
    private String lastName;
    private String email;
    private String course;
    private String startDate;

    private String residenceNumber;
    private String companyEmployeeNumber;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getResidenceNumber() {
        return residenceNumber;
    }

    public void setResidenceNumber(String residenceNumber) {
        this.residenceNumber = residenceNumber;
    }

    public String getCompanyEmployeeNumber() {
        return companyEmployeeNumber;
    }

    public void setCompanyEmployeeNumber(String companyEmployeeNumber) {
        this.companyEmployeeNumber = companyEmployeeNumber;
    }
}
