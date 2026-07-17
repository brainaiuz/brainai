package com.edatasite.workforce.gwt.trainingcenter.client.rpc.student;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 11/20/12
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class StudentAsInvoiceItem implements IsSerializable {

    private Integer objectID;
    private Integer studentID;
    private String firstName;
    private String lastName;

    private String courseCode;
    private Integer courseID;

    private Integer courseScheduleID;
    private String courseScheduleNumber;

    private Integer courseBookingID;
    private String courseBookingNumber;

    private BigDecimal price;
    private BigDecimal stopFee;

    private Integer accountID;

    private Integer customerID;
    private String customerName;

    public boolean includedInInvoice = true;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public Integer getCourseScheduleID() {
        return courseScheduleID;
    }

    public void setCourseScheduleID(Integer courseScheduleID) {
        this.courseScheduleID = courseScheduleID;
    }

    public String getCourseScheduleNumber() {
        return courseScheduleNumber;
    }

    public void setCourseScheduleNumber(String courseScheduleNumber) {
        this.courseScheduleNumber = courseScheduleNumber;
    }

    public Integer getCourseBookingID() {
        return courseBookingID;
    }

    public void setCourseBookingID(Integer courseBookingID) {
        this.courseBookingID = courseBookingID;
    }

    public String getCourseBookingNumber() {
        return courseBookingNumber;
    }

    public void setCourseBookingNumber(String courseBookingNumber) {
        this.courseBookingNumber = courseBookingNumber;
    }

    public BigDecimal getPrice() {
        return price != null ? price : BigDecimal.ZERO;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getStopFee() {
        return stopFee != null ? stopFee : BigDecimal.ZERO;
    }

    public void setStopFee(BigDecimal stopFee) {
        this.stopFee = stopFee;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
    }

    public String getItemDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append("booking # : ").append(getCourseBookingNumber()).append(", \n");
        builder.append("schedule # : ").append(getCourseScheduleNumber()).append(" ").append(getCourseCode()).append(".");
        return builder.toString();
    }
}
