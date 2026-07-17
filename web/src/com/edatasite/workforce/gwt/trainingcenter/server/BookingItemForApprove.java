package com.edatasite.workforce.gwt.trainingcenter.server;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/20/12
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemForApprove implements Serializable {

    private Integer objectID;
    private String number;
    private String location;
    private String customer;
    private String customerNumber;
    private String contact;

    private String statusCode;

    private ArrayList<CourseScheduleStudent> scheduleStudents;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ArrayList<CourseScheduleStudent> getScheduleStudents() {
        return scheduleStudents;
    }

    public void setScheduleStudents(ArrayList<CourseScheduleStudent> scheduleStudents) {
        this.scheduleStudents = scheduleStudents;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
