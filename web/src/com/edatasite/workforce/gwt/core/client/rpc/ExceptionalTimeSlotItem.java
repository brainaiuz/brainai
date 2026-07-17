package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Ilhombek
 * Date: 2/9/13
 * Time: 2:10 AM
 */
public class ExceptionalTimeSlotItem implements IsSerializable {

    private Integer objectID;
    private String name;
    private String description;
    private DateNonConvertable exceptionalDate;
    private Integer dayNo;

    private int[] weekDay;
    private int[] lunch;
    private int[] coffee;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateNonConvertable getExceptionalDate() {
        return exceptionalDate;
    }

    public void setExceptionalDate(DateNonConvertable exceptionalDate) {
        this.exceptionalDate = exceptionalDate;
    }

    public int[] getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int[] weekDay) {
        this.weekDay = weekDay;
    }

    public int[] getLunch() {
        return lunch;
    }

    public void setLunch(int[] lunch) {
        this.lunch = lunch;
    }

    public int[] getCoffee() {
        return coffee;
    }

    public void setCoffee(int[] coffee) {
        this.coffee = coffee;
    }

    public Integer getDayNo() {
        return dayNo;
    }

    public void setDayNo(Integer dayNo) {
        this.dayNo = dayNo;
    }
}