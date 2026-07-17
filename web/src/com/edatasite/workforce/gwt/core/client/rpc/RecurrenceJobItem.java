package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Apr 16, 2010
 * Time: 4:38:54 PM
 * To change this template use File | Settings | File Templates.
 */

public class RecurrenceJobItem extends AbstractRpcMap implements Serializable, IsSerializable {

    private static final String OBJECT_ID = "objectId";
    private static final String NAME = "name";
    private static final String STATUS = "status";
    private static final String TYPE = "type";
    private static final String DAILY_PATTERN_OPTIONS = "dailyPatternOptions";
    private static final String INTERVAL = "interval";
    private static final String AFTER_DAY = "afterDay";
    private static final String END_TYPE = "endType";
    private static final String BUS_OBJECT_ID = "busObjectId";
    private static final String BUS_OBJECT_PARAMS = "busObjectParams";
    private static final String USER_TIME_ZONE = "userTimeZone";
    private static final String START_DATE = "startDate";
    private static final String END_DATE = "endDate";
    private static final String OCCURRENCE = "occurrence";
    private static final String ENABLED = "enabled";
    private static final String SUNDAY = "sunday";
    private static final String MONDAY = "monday";
    private static final String TUESDAY = "tuesday";
    private static final String WEDNESDAY = "wednesday";
    private static final String THURSDAY = "thursday";
    private static final String FRIDAY = "friday";
    private static final String SATURDAY = "saturday";
    private static final String MONTHLY_OR_YEARLY_DAY = "monthlyOrYearlyDay";

    // for Monthly recurrence (first, second,..., last)
    private static final String CUSTOM_PATTERN_DAY = "customPatternDay";

    private static final String YEARLY_MONTH = "yearlyMonth";
    private static final String MONTHLY_OR_YEARLY_PATTERN_OPTION = "monthlyOrYearlyPatternOption";
    private static final String JOB_TYPE = "jobType";
    private static final String TO_ME = "toMe";
    private static final String TO_CLIENT = "toClient";
    private static final String FOR_CURRENT = "forCurrent";
    private static final String FOR_PREVIOUS = "forPrevious";
    private static final String USER_ID = "userID";
    private static final String DEFAULT_REMINDER = "defaultReminder";
    private HashMap<String, String> valueMap = null;
    private SelectItem[] employees;
    private ArrayList<SelectItem> selectedRoles = new ArrayList<>();
    private ArrayList<SelectItem> roles = new ArrayList<>();

    public RecurrenceJobItem() {
    }

    public RecurrenceJobItem(Integer id, String name) {
        setObjectId(id);
        setName(name);
    }

    protected HashMap<String, String> getInstance() {
        return valueMap = valueMap == null ? new HashMap<>() : valueMap;
    }

    public Integer getObjectId() {
        return getInteger(OBJECT_ID);
    }

    public void setObjectId(Integer objectId) {
        addInteger(OBJECT_ID, objectId);
    }

    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        addString(NAME, name);
    }

    public String getStatus() {
        return getString(STATUS);
    }

    public void setStatus(String status) {
        addString(STATUS, status);
    }

    public Integer getType() {
        return getInteger(TYPE);
    }

    public void setType(Integer type) {
        addInteger(TYPE, type);
    }

    public Integer getDailyPatternOptions() {
        return getInteger(DAILY_PATTERN_OPTIONS);
    }

    public void setDailyPatternOptions(Integer dailyPatternOptions) {
        addInteger(DAILY_PATTERN_OPTIONS, dailyPatternOptions);
    }

    public Integer getInterval() {
        return getInteger(INTERVAL);
    }

    public void setInterval(Integer interval) {
        addInteger(INTERVAL, interval);
    }

    public Integer getAfterDay() {
        return getInteger(AFTER_DAY);
    }

    public void setAfterDay(Integer afterDay) {
        addInteger(AFTER_DAY, afterDay);
    }

    public Integer getEndType() {
        return getInteger(END_TYPE);
    }

    public void setEndType(Integer endType) {
        addInteger(END_TYPE, endType);
    }

    public Integer getBusObjectId() {
        return getInteger(BUS_OBJECT_ID);
    }

    public void setBusObjectId(Integer busObjectId) {
        addInteger(BUS_OBJECT_ID, busObjectId);
    }

    public String getBusObjectParams() {
        return getString(BUS_OBJECT_PARAMS);
    }

    public void setBusObjectParams(String busObjectParams) {
        addString(BUS_OBJECT_PARAMS, busObjectParams);
    }

    public String getUserTimeZone() {
        return getString(USER_TIME_ZONE);
    }

    public void setUserTimeZone(String userTimeZone) {
        addString(USER_TIME_ZONE, userTimeZone);
    }

    public Date getStartDate() {
        return getDate(START_DATE);
    }

    public void setStartDate(Date startDate) {
        addDate(START_DATE, startDate, true);
    }

    public Date getEndDate() {
        return getDate(END_DATE);
    }

    public void setEndDate(Date endDate) {
        addDate(END_DATE, endDate, true);
    }

    public Integer getOccurrence() {
        return getInteger(OCCURRENCE);
    }

    public void setOccurrence(Integer occurrence) {
        addInteger(OCCURRENCE, occurrence);
    }

    public SelectItem[] getEmployees() {
        return employees;
    }

    public void setEmployees(SelectItem[] employees) {
        this.employees = employees;
    }

    public ArrayList<SelectItem> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(ArrayList<SelectItem> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public ArrayList<SelectItem> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<SelectItem> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return getBool(ENABLED);
    }

    public void setEnabled(boolean enabled) {
        addBool(ENABLED, enabled);
    }

    public Boolean isSunday() {
        return getBoolean(SUNDAY);
    }

    public void setSunday(Boolean sunday) {
        addBoolean(SUNDAY, sunday);
    }

    public Boolean isMonday() {
        return getBoolean(MONDAY);
    }

    public void setMonday(Boolean monday) {
        addBoolean(MONDAY, monday);
    }

    public Boolean isTuesday() {
        return getBoolean(TUESDAY);
    }

    public void setTuesday(Boolean tuesday) {
        addBoolean(TUESDAY, tuesday);
    }

    public Boolean isWednesday() {
        return getBoolean(WEDNESDAY);
    }

    public void setWednesday(Boolean wednesday) {
        addBoolean(WEDNESDAY, wednesday);
    }

    public Boolean isThursday() {
        return getBoolean(THURSDAY);
    }

    public void setThursday(Boolean thursday) {
        addBoolean(THURSDAY, thursday);
    }

    public Boolean isFriday() {
        return getBoolean(FRIDAY);
    }

    public void setFriday(Boolean friday) {
        addBoolean(FRIDAY, friday);
    }

    public Boolean isSaturday() {
        return getBoolean(SATURDAY);
    }

    public void setSaturday(Boolean saturday) {
        addBoolean(SATURDAY, saturday);
    }

    public Integer getMonthlyOrYearlyDay() {
        return getInteger(MONTHLY_OR_YEARLY_DAY);
    }

    public void setMonthlyOrYearlyDay(Integer monthlyOrYearlyDay) {
        addInteger(MONTHLY_OR_YEARLY_DAY, monthlyOrYearlyDay);
    }

    public Integer getCustomPatternDay() {
        return getInteger(CUSTOM_PATTERN_DAY);
    }

    public void setCustomPatternDay(Integer customPatternDay) {
        addInteger(CUSTOM_PATTERN_DAY, customPatternDay);
    }

    public Integer getYearlyMonth() {
        return getInteger(YEARLY_MONTH);
    }

    public void setYearlyMonth(Integer yearlyMonth) {
        addInteger(YEARLY_MONTH, yearlyMonth);
    }

    public Integer getMonthlyOrYearlyPatternOption() {
        return getInteger(MONTHLY_OR_YEARLY_PATTERN_OPTION);
    }

    public void setMonthlyOrYearlyPatternOption(Integer monthlyOrYearlyPatternOption) {
        addInteger(MONTHLY_OR_YEARLY_PATTERN_OPTION, monthlyOrYearlyPatternOption);
    }

    public Integer getJobType() {
        return getInteger(JOB_TYPE);
    }

    public void setJobType(Integer jobType) {
        addInteger(JOB_TYPE, jobType);
    }

    public Boolean getToMe() {
        return getBoolean(TO_ME);
    }

    public void setToMe(Boolean toMe) {
        addBoolean(TO_ME, toMe);
    }

    public Boolean getToClient() {
        return getBoolean(TO_CLIENT);
    }

    public void setToClient(Boolean toClient) {
        addBoolean(TO_CLIENT, toClient);
    }

    public Boolean isForCurrent() {
        return getBool(FOR_CURRENT);
    }

    public void setForCurrent(Boolean forCurrent) {
        addBoolean(FOR_CURRENT, forCurrent);
    }

    public Boolean isForPrevious() {
        return Boolean.FALSE.equals(getBoolean(FOR_PREVIOUS));
    }

    public void setForPrevious(Boolean forPrevious) {
        addBoolean(FOR_PREVIOUS, forPrevious);
    }

    public Integer getUserID() {
        return getInteger(USER_ID);
    }

    public void setUserID(Integer userID) {
        addInteger(USER_ID, userID);
    }

    public Boolean getDefaultReminder() {
        return getBoolean(DEFAULT_REMINDER);
    }

    public void setDefaultReminder(Boolean defaultReminder) {
        addBoolean(DEFAULT_REMINDER, defaultReminder);
    }

    public RecurrenceJobItem clone() {
        RecurrenceJobItem item = new RecurrenceJobItem();
        item.setName(this.getName());
        item.setType(this.getType());
        item.setDailyPatternOptions(this.getDailyPatternOptions());
        item.setInterval(this.getInterval());
        item.setAfterDay(this.getAfterDay());
        item.setEndType(this.getEndType());
        item.setBusObjectId(this.getBusObjectId());
        item.setBusObjectParams(this.getBusObjectParams());
        item.setUserTimeZone(this.getUserTimeZone());
        item.setStartDate(this.getStartDate());
        item.setEndDate(this.getEndDate());
        item.setOccurrence(this.getOccurrence());
        item.setEmployees(this.employees);
        item.setEnabled(this.isEnabled());
        item.setSunday(this.isSunday());
        item.setMonday(this.isMonday());
        item.setTuesday(this.isTuesday());
        item.setWednesday(this.isWednesday());
        item.setThursday(this.isThursday());
        item.setFriday(this.isFriday());
        item.setSaturday(this.isSaturday());
        item.setMonthlyOrYearlyDay(this.getMonthlyOrYearlyDay());
        item.setCustomPatternDay(this.getCustomPatternDay());
        item.setYearlyMonth(this.getYearlyMonth());
        item.setMonthlyOrYearlyPatternOption(this.getMonthlyOrYearlyPatternOption());
        item.setJobType(this.getJobType());
        item.setToMe(this.getToMe());
        item.setToClient(this.getToClient());
        item.setForCurrent(this.isForCurrent());
        item.setForPrevious(this.isForPrevious());
        item.setUserID(this.getUserID());
        item.setDefaultReminder(this.getDefaultReminder());
        return item;
    }
}
