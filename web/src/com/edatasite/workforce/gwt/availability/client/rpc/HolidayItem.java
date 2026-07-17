package com.edatasite.workforce.gwt.availability.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

public class HolidayItem implements IsSerializable {

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String TAKEN_FROM_VACTION_ALLOWANCE = "takenFromVacationAllowance";
    public static final String TAKEN_FROM_ANNUAL_LEAVE_ALLOWANCE = "takenFromAnnualLeaveAllowance";
    public static final String LOCATION = "location";
    public static final String ACTION = "action";
    public static final String RECURRING = "recurring";
    public static final String DAY_OFF = "dayOff";

    private Integer objectID;
    private String name;
    private String description;
    private DateNonConvertable from;
    private DateNonConvertable to;
    private boolean repeat = false;
    private Integer repeatId;
    private boolean dayOff;
    private Integer locationId;
    private String locationName;
    private boolean allDay = false;
    private ArrayList<Integer> locationIds;
    private boolean takenFromAnnual;

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

    public DateNonConvertable getFrom() {
        return from;
    }

    public void setFrom(DateNonConvertable from) {
        this.from = from;
    }

    public DateNonConvertable getTo() {
        return to;
    }

    public void setTo(DateNonConvertable to) {
        this.to = to;
    }

    public boolean isDayOff() {
        return dayOff;
    }

    public void setDayOff(boolean dayOff) {
        this.dayOff = dayOff;
    }

    public Integer getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(Integer repeatId) {
        this.repeatId = repeatId;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public ArrayList<Integer> getLocationIds() {
        if (locationIds == null) {
            locationIds = new ArrayList<>();
        }
        return locationIds;
    }

    public void setLocationIds(ArrayList<Integer> locationIds) {
        this.locationIds = locationIds;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public boolean isTakenFromAnnual() {
        return takenFromAnnual;
    }

    public void setTakenFromAnnual(boolean takenFromAnnual) {
        this.takenFromAnnual = takenFromAnnual;
    }
}
