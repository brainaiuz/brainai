package com.edatasite.workforce.gwt.trainingcenter.client.rpc.coursebooking;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 9/13/12
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseBookingCalculationItem implements IsSerializable {

    private Integer objectID; //scheduled course id;
    private String course;
    private String location;
    private BigDecimal qty;
    private BigDecimal price;
    private BigDecimal stopFee;
    private BigDecimal total;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getStopFee() {
        return stopFee;
    }

    public void setStopFee(BigDecimal stopFee) {
        this.stopFee = stopFee;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
}
