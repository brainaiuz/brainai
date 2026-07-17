package com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/14/12
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeSlotItem implements IsSerializable {
    private Integer day;
    private Integer startTime;
    private Integer endTime;
    private Integer lunchStart;
    private Integer lunchEnd;
    private Integer coffeeStart;
    private Integer coffeeEnd;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getLunchStart() {
        return lunchStart;
    }

    public void setLunchStart(Integer lunchStart) {
        this.lunchStart = lunchStart;
    }

    public Integer getLunchEnd() {
        return lunchEnd;
    }

    public void setLunchEnd(Integer lunchEnd) {
        this.lunchEnd = lunchEnd;
    }

    public Integer getCoffeeStart() {
        return coffeeStart;
    }

    public void setCoffeeStart(Integer coffeeStart) {
        this.coffeeStart = coffeeStart;
    }

    public Integer getCoffeeEnd() {
        return coffeeEnd;
    }

    public void setCoffeeEnd(Integer coffeeEnd) {
        this.coffeeEnd = coffeeEnd;
    }

    public Integer getWorkingTime() {
        if (startTime != null && endTime != null) {
            return (endTime - startTime);
        }
        return 0;
    }
}
